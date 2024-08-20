package hiyen.galmanhae.dataprocess.util;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.AreaInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.LocationInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.WeatherInfo;
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

	public List<PlaceInfo> parse(final Map<String, byte[]> fileMap) throws IOException {
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

				final AreaInfo areaInfo = new AreaInfo(name, code);
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

