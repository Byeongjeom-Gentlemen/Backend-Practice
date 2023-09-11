package com.sh.global.util;

import com.sh.global.exception.customexcpetion.FileCustomException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

    private static final Tika tika = new Tika();

    // 파일 확장자 변조 체크
    public void validImgFile(InputStream inputStream) {
        try {
            List<String> notValidTypeList =
                    Arrays.asList(
                            "image/jpeg",
                            "image/pjpeg",
                            "image/png",
                            "image/gif",
                            "image/bmp",
                            "image/x-windows-bmp");

            String mimeType = tika.detect(inputStream);
            boolean isValid =
                    notValidTypeList.stream()
                            .anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));

            if (!isValid) {
                throw FileCustomException.IS_NOT_IMAGE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
