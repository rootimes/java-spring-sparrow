package sparrow.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
@PropertySource("classpath:application.properties")
public class JwtUtil {

    final static int ACCESS_TOKEN_EXPIRATION_MINUTES = 30;

    @Autowired
    private Environment env;

    private final long expirationTime = TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_EXPIRATION_MINUTES);

    public String generateToken(String email) {
        try {
            JWSSigner signer = new MACSigner(getSecret().getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .claim("email", email)
                    .issuer("sparrow")
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Error generating JWT", e);
        }
    }

    public String getValueFromToken(String token, String claimKey) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            validateTokenSignature(signedJWT);

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date expiration = claimsSet.getExpirationTime();

            validateTokenExpiration(expiration);

            return claimsSet.getStringClaim(claimKey);
        } catch (Exception e) {
            throw new BadCredentialsException("Error extracting claim value from JWT", e);
        }
    }

    private String getSecret() {
        String secret = env.getProperty("jwt.secret");
        if (secret == null || secret.length() < 32) {
            return "default-secret-at-least-32-chars-long!";
        }
        return secret;
    }

    private void validateTokenSignature(SignedJWT signedJWT) throws BadCredentialsException {
        try {
            JWSVerifier verifier = new MACVerifier(getSecret().getBytes());
            if (!signedJWT.verify(verifier)) {
                throw new BadCredentialsException("Invalid JWT signature");
            }
        } catch (JOSEException e) {
            throw new BadCredentialsException("Error validating JWT signature", e);
        }
    }

    private void validateTokenExpiration(Date expiration) throws BadCredentialsException {
        try {
            if (expiration != null && expiration.before(new Date())) {
                throw new BadCredentialsException("JWT token has expired");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Error validating JWT expiration", e);
        }
    }
}
