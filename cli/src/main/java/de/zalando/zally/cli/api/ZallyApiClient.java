package de.zalando.zally.cli.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;
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
import org.json.JSONException;
import org.json.JSONObject;


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
    private static final String PATH = "/api-violations";

    public ZallyApiClient(String baseUrl, String token) {
        this.url = getUrl(baseUrl);
        this.token = token;
    }

    public ViolationsApiResponse validate(String requestBody) throws CliException {
        final HttpResponse<String> response = requestViolationsReport(requestBody);
        final int responseStatus = response.getStatus();
        final String responseBody = response.getBody();

        if (responseStatus >= 400 && responseStatus <= 599) {
            throw new CliException(
                    CliExceptionType.API,
                    "An error occurred while querying Zally server",
                    getErrorReason(responseBody)
            );
        }

        return new ViolationsApiResponse(getJsonObject(responseBody));
    }

    public String getErrorReason(String body) {
        JSONObject errorJson;
        try {
            errorJson = new JSONObject(body);
        } catch (JSONException exception) {
            return null;
        }

        return errorJson.optString("detail", null);
    }

    private HttpResponse<String> requestViolationsReport(final String requestBody) throws CliException {
        try {
            HttpRequestWithBody request = Unirest
                    .post(url)
                    .header("Content-Type", "application/json");

            if (token != null && !token.isEmpty()) {
                request = request.header("Authorization", "Bearer " + token);
            }

            return request.body(requestBody).asString();
        } catch (UnirestException exception) {
            String details = exception.getCause() == null ? exception.getMessage() : exception.getCause().getMessage();
            throw new CliException(
                    CliExceptionType.API,
                    "An error occurred while querying Zally server",
                    details
            );
        }
    }

    private JSONObject getJsonObject(final String responseBody) throws CliException {
        try {
            return new JSONObject(responseBody);
        } catch (JSONException exception) {
            throw new CliException(
                    CliExceptionType.API,
                    "JSON cannot be parsed",
                    exception.getMessage()
            );
        }
    }

    private String getUrl(String baseUrl) {
        String url = baseUrl;
        return url.replaceAll("/$", "") + PATH;
    }
}
