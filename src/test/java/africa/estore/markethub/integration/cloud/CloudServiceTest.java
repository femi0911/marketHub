package africa.estore.markethub.integration.cloud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CloudServiceTest {
    @Autowired
    private CloudService cloudService;

    @Test
    @DisplayName(
            """
            Given:
            I have a collection of files.
            When:
            I use the upload function of the
            cloud service to upload the files
            Then:
            Files are uploaded, and urls that
            point to the files, are obtained
            from the cloud service provider.
            """
    )
    public void testCanUploadFiles() {
        Path grootFile = Path.of("src/main/resources/assets/groot.mp4");
        Path gymShirtFile = Path.of("src/main/resources/assets/gym shirt.webp");
        Path peakMilkTinFile = Path.of("src/main/resources/assets/peak milk (tin).webp");
        Path peakMilkSachetFile = Path.of("src/main/resources/assets/peak milk sachet.webp");
        try {
            List<MultipartFile> files = List.of(
                    new MockMultipartFile("groot", Files.newInputStream(grootFile)),
                    new MockMultipartFile("gym shirt", Files.newInputStream(gymShirtFile)),
                    new MockMultipartFile("peak milk (tin)", Files.newInputStream(peakMilkTinFile)),
                    new MockMultipartFile("peak milk sachet", Files.newInputStream(peakMilkSachetFile))
            );
            List<String> urls = cloudService.upload(files);
            assertThat(urls).isNotNull();
            assertThat(urls).isNotEmpty();
            assertThat(urls.get(0)).containsAnyOf("cloudinary");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
