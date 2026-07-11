package africa.estore.markethub.integration.cloud.cloudinary;

import africa.estore.markethub.config.CloudConfig;
import africa.estore.markethub.integration.cloud.CloudService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CloudServiceImpl implements CloudService {
    private final CloudConfig cloudConfig;
    @Override
    public List<String> upload(List<MultipartFile> files) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudConfig.getName(),
                "api_key", cloudConfig.getKey(),
                "api_secret", cloudConfig.getSecret(),
                "secure", true));
        return files.stream().map(uploadFileWith(cloudinary)).toList();
    }

    private static Function<MultipartFile, String> uploadFileWith(Cloudinary cloudinary) {
        return file -> {
            try {
                return cloudinary.uploader()
                        .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"))
                        .get("url").toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
