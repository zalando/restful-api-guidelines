package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;


public class ZallyApiClient {

    static {
        try {
            SSLContext ignoreSslChecks = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            CloseableHttpClient unsafeHttpClient = HttpClients.custom().setSSLContext(ignoreSslChecks)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            Unirest.setHttpClient(unsafeHttpClient);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException exception) {
            System.out.println("Could not disable verification of ssl certificates. "
                    + "Support for linter services available through HTTPS can be limited.");
        }
    }

    private final String url;
    private final String token;

    public ZallyApiClient(String url, String token) {
        this.url = url;
        this.token = token;
    }

    public JsonValue validate(String body) throws CliException {
        HttpResponse<String> response;

        try {
            response = Unirest
                    .post(url)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        } catch (UnirestException exception) {
            String details = exception.getCause() == null ? exception.getMessage() : exception.getCause().getMessage();
            throw new CliException(
                    CliExceptionType.API,
                    "An error occurred while querying Zally server",
                    details
            );
        }

        final int responseStatus = response.getStatus();
        final String responseBody = response.getBody();

        if (responseStatus >= 400 && responseStatus <= 599) {
            throw new CliException(
                    CliExceptionType.API,
                    "An error occurred while querying Zally server",
                    getErrorReason(responseBody)
            );
        }

        return Json.parse(responseBody);
    }

    public String getErrorReason(String body) {
        JsonValue errorJson;
        try {
            errorJson = Json.parse(body);
        } catch (Exception exception) {
            return null;
        }

        if (!errorJson.asObject().isEmpty()) {
            return errorJson.asObject().getString("detail", null);
        }

        return null;
    }
}
