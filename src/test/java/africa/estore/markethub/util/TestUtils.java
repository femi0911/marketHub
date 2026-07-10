package africa.estore.markethub.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class TestUtils {
    private TestUtils() {}

    public static List<MultipartFile> getTestMediaFiles() {
        try {
            Path grootFile = Path.of("src/main/resources/assets/groot.mp4");
            Path gymShirtFile = Path.of("src/main/resources/assets/gym shirt.webp");
            Path peakMilkTinFile = Path.of("src/main/resources/assets/peak milk (tin).webp");
            Path peakMilkSachetFile = Path.of("src/main/resources/assets/peak milk sachet.webp");
            return List.of(
                    new MockMultipartFile("groot", Files.newInputStream(grootFile)),
                    new MockMultipartFile("gym shirt", Files.newInputStream(gymShirtFile)),
                    new MockMultipartFile("peak milk (tin)", Files.newInputStream(peakMilkTinFile)),
                    new MockMultipartFile("peak milk sachet", Files.newInputStream(peakMilkSachetFile))
            );
        }catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
