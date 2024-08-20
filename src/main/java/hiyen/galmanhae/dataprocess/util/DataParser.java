package hiyen.galmanhae.dataprocess.util;

import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.AreaInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.LocationInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.WeatherInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Component;

/**
 * .shp, .shx, .dbf 파일을 파싱하여 장소 이름, 코드, 위도, 경도 정보를 추출
 * 위도와 경도를 람베르트 좌표계로 변환 후 PlaceInfo 객체로 변환
 */
//TODO 리팩토링 필요
@Slf4j
@Component
@RequiredArgsConstructor
public class DataParser {

	private final LambertCoordinateConverter lambertCoordinateConverter;

	public Map<String, byte[]> processZipFile(final InputStream inputStream) {
		final Map<String, byte[]> extractedFiles = new HashMap<>();

		try (final ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("EUC-KR"))) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) { //하나의 엔트리를 읽어옴
				final String fileName = entry.getName(); //엔트리의 이름을 가져옴
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //바이트 배열을 저장할 ByteArrayOutputStream 객체 생성

				final byte[] buffer = new byte[4096]; //버퍼 생성
				int len;
				while ((len = zipInputStream.read(buffer)) > 0) {
					byteArrayOutputStream.write(buffer, 0, len); //바이트 배열을 읽어와서 ByteArrayOutputStream에 저장
				}
				extractedFiles.put(fileName, byteArrayOutputStream.toByteArray()); //파일 이름과 바이트 배열을 맵에 저장
				zipInputStream.closeEntry(); //엔트리 닫기
			}
		} catch (IOException e) {
			throw new FailFetchAPIUncheckedException(e);
		}
		log.info("파일 읽고 맵으로 만들기 완료!");

		return extractedFiles;
	}

	public List<PlaceInfo> parse(final Map<String, byte[]> fileMap, final String fileName) throws IOException {
		log.info("파싱 시작, 파일 이름: {}", fileName);

		// fileMap에 저장된 파일들을 각각의 확장자에 맞게 임시파일로 생성.
		final File shpTempFile = createTempFile(".shp", fileMap.get(fileName + ".shp"));
		final File shxTempFile = createTempFile(".shx", fileMap.get(fileName + ".shx"));
		final File dbfTempFile = createTempFile(".dbf", fileMap.get(fileName + ".dbf"));

		// shapefile의 URL을 생성
		final URL shpUrl = shpTempFile.toURI().toURL();

		// shape file 데이터스토어를 생성하기 위한 맵 객체 생성
		final Map<String, Object> map = new HashMap<>();
		map.put("url", shpUrl);
		map.put(ShapefileDataStoreFactory.DBFCHARSET.key, Charset.forName("EUC-KR"));

		final ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		final ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createDataStore(map);
		// Shapefile에서 피처(Feature)들을 가져옴
		final SimpleFeatureCollection collection = dataStore.getFeatureSource().getFeatures();

		List<PlaceInfo> placeInfos = new ArrayList<>();
		try (final SimpleFeatureIterator iterator = collection.features()) { // feature iterator를 이용하여 각 feature를 가져옴
			while (iterator.hasNext()) {
				final SimpleFeature feature = iterator.next();

				final String name = (String) feature.getAttribute("AREA_NM"); // AREA_NM을 가져옴
				final String code = (String) feature.getAttribute("AREA_CD"); // AREA_CD를 가져옴
				final Geometry geom = (Geometry) feature.getAttribute("the_geom"); // feature의 Geometry를 가져옴

				final Point centroid = geom.getCentroid(); // Geometry의 중심점을 가져옴
				final double latitude = centroid.getY(); // 중심점의 위도
				final double longitude = centroid.getX(); // 중심점의 경도

				int[] converted = lambertCoordinateConverter.convert(latitude, longitude);// 위도와 경도를 람베르트 좌표계로 변환

				final AreaInfo areaInfo = new AreaInfo(code, name);
				final LocationInfo locationInfo = new LocationInfo(String.valueOf(latitude), String.valueOf(longitude));
				final WeatherInfo weatherInfo = new WeatherInfo(String.valueOf(converted[0]), String.valueOf(converted[1]));
				placeInfos.add(new PlaceInfo(areaInfo, locationInfo, weatherInfo));
			}
		}
		finally {
			dataStore.dispose();
			Files.deleteIfExists(shpTempFile.toPath());
			Files.deleteIfExists(shxTempFile.toPath());
			Files.deleteIfExists(dbfTempFile.toPath());
		}
		return placeInfos;
	}

	private File createTempFile(final String suffix, final byte[] data) throws IOException {
		final File tempFile = new File(System.getProperty("java.io.tmpdir"), "tempShapefile" + suffix);
		try (final FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			outputStream.write(data);
		}
		return tempFile;
	}
}

