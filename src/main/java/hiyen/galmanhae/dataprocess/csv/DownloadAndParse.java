package hiyen.galmanhae.dataprocess.csv;

import feign.Response;
import hiyen.galmanhae.place.domain.POIArea;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadAndParse {

	private final LambertCoordinateConverter lambertCoordinateConverter;

	public Map<String, byte[]> processZipFile(final Response response) throws IOException {
		final Map<String, byte[]> extractedFiles = new HashMap<>();

		try (final ZipInputStream zipInputStream = new ZipInputStream(response.body().asInputStream(), Charset.forName("EUC-KR"))) {
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
		}

		return extractedFiles;
	}

	public List<POIArea> aggregate(final Map<String, byte[]> fileMap) throws IOException {
		// fileMap에 저장된 파일들을 각각의 확장자에 맞게 임시파일로 생성.
		final File shpTempFile = createTempFile(".shp", fileMap.get("서울시 주요 115장소 영역.shp"));
		final File shxTempFile = createTempFile(".shx", fileMap.get("서울시 주요 115장소 영역.shx"));
		final File dbfTempFile = createTempFile(".dbf", fileMap.get("서울시 주요 115장소 영역.dbf"));

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

		List<POIArea> poiAreas = new ArrayList<>();
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
				final POIArea poiArea = POIArea.builder()
					.name(name)
					.code(code)
					.latitude(String.valueOf(latitude))
					.longitude(String.valueOf(longitude))
					.weatherX(converted[0] + "")
					.weatherY(converted[1] + "")
					.build();
				poiAreas.add(poiArea);
			}
		} finally {
			dataStore.dispose();
			Files.deleteIfExists(shpTempFile.toPath());
			Files.deleteIfExists(shxTempFile.toPath());
			Files.deleteIfExists(dbfTempFile.toPath());
		}
		return poiAreas;
	}

	private File createTempFile(final String suffix, final byte[] data) throws IOException {
		final File tempFile = new File(System.getProperty("java.io.tmpdir"), "tempShapefile" + suffix);
		try (final FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			outputStream.write(data);
		}
		return tempFile;
	}
}

