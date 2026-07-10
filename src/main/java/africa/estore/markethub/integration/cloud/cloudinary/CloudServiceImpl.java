package africa.estore.markethub.integration.cloud.cloudinary;

import africa.estore.markethub.integration.cloud.CloudService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
public class CloudServiceImpl implements CloudService {
    @Value("${cloudinary.api.key}")
    private String apiKey;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;
    @Value("${cloudinary.api.name}")
    private String cloudName;

    @Override
    public List<String> upload(List<MultipartFile> files) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
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
