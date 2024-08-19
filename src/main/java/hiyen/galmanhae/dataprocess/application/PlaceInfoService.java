package hiyen.galmanhae.dataprocess.application;

import feign.Response;
import hiyen.galmanhae.dataprocess.client.PlaceInfoClient;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceInfoService {

	private final PlaceInfoClient placeInfoClient;

	public Map<String, byte[]> fetch() {
		Response response;
		try {
			response = placeInfoClient.fetch();
		} catch (Exception e) {
			throw new FailFetchAPIUncheckedException(e);
		}
		return processZipFile(response);
	}

	private Map<String, byte[]> processZipFile(final Response response) {
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
		} catch (IOException e) {
			throw new FailFetchAPIUncheckedException(e);
		}

		return extractedFiles;
	}
}
