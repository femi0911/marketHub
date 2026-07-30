package africa.estore.markethub.integration.payment;

import africa.estore.markethub.config.PayStackConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
@AllArgsConstructor
public class PaystackPaymentVerifier {
    private final String algorithm = "HmacSHA512";
    private final PayStackConfig payStackConfig;

    public boolean isValidPaymentSignature(String payload, String signature) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(payStackConfig.getKey().getBytes(StandardCharsets.UTF_8), algorithm));
        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String hashh = HexFormat.of().formatHex(hash);
        return MessageDigest.isEqual(hashh.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8));
    }

}
