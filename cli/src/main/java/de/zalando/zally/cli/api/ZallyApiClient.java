package de.zalando.zally.cli.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.zalando.zally.cli.domain.Rule;
import de.zalando.zally.cli.exception.CliException;
import de.zalando.zally.cli.exception.CliExceptionType;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
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

    private final String baseUrl;
    private final String token;
    private static final String VIOLATIONS_URI = "/api-violations";
    private static final String RULES_URI = "/supported-rules";
    private static final String ERROR_MESSAGE = "An error occurred while querying Zally server";

    public ZallyApiClient(String baseUrl, String token) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
        this.token = token;
    }

    public ViolationsApiResponse validate(String requestBody) throws CliException {
        final HttpResponse<String> response = requestViolationsReport(requestBody);
        return new ViolationsApiResponse(getJsonResponseBody(response));
    }

    public List<Rule> listRules() throws CliException {
        final HttpResponse<String> response = requestRuleList();
        final JSONObject result = getJsonResponseBody(response);
        final JSONArray ruleJsons = result.getJSONArray("rules");
        final List<Rule> rules = new ArrayList<>();

        for (int i = 0; i < ruleJsons.length(); i++) {
            rules.add(new Rule(ruleJsons.getJSONObject(i)));
        }

        return rules;
    }

    private HttpResponse<String> requestViolationsReport(final String requestBody) throws CliException {
        try {
            return Unirest
                    .post(baseUrl + VIOLATIONS_URI)
                    .headers(getHeaders())
                    .body(requestBody)
                    .asString();
        } catch (UnirestException exception) {
            throw new CliException(CliExceptionType.API, ERROR_MESSAGE, getUnirestErrorDetails(exception));
        }
    }

    private HttpResponse<String> requestRuleList() throws CliException {
        try {
            return Unirest
                    .get(baseUrl + RULES_URI)
                    .headers(getHeaders())
                    .asString();
        } catch (UnirestException exception) {
            throw new CliException(CliExceptionType.API, ERROR_MESSAGE, getUnirestErrorDetails(exception));
        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }

        return headers;
    }

    private JSONObject getJsonResponseBody(final HttpResponse<String> response) throws CliException {
        final int responseStatus = response.getStatus();
        final String responseBody = response.getBody();

        if (responseStatus >= 400 && responseStatus <= 599) {
            throw new CliException(CliExceptionType.API, ERROR_MESSAGE, getErrorReason(responseBody));
        }

        return getJsonObject(responseBody);
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

    private String getUnirestErrorDetails(final UnirestException exception) {
        return exception.getCause() == null ? exception.getMessage() : exception.getCause().getMessage();
    }

    private String getErrorReason(String body) {
        JSONObject errorJson;
        try {
            errorJson = new JSONObject(body);
        } catch (JSONException exception) {
            return null;
        }

        return errorJson.optString("detail", null);
    }
}
