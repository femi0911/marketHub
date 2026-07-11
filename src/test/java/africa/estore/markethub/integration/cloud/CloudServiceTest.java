package africa.estore.markethub.integration.cloud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static africa.estore.markethub.util.TestUtils.getTestMediaFiles;
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
        List<String> urls = cloudService.upload(getTestMediaFiles());
        assertThat(urls).isNotNull();
        assertThat(urls).isNotEmpty();
        assertThat(urls.getFirst()).containsAnyOf("cloudinary");
    }
}
