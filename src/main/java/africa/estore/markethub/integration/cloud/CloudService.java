package africa.estore.markethub.integration.cloud;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudService {
    List<String> upload(List<MultipartFile> files);
}
