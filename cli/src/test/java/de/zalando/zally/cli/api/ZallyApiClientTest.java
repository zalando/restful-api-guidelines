package de.zalando.zally.cli.api;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static net.jadler.Jadler.verifyThatRequest;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.zalando.zally.cli.domain.Rule;
import de.zalando.zally.cli.exception.CliException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;


public class ZallyApiClientTest {
    private final String token = "1956eeee-ffff-eeee-ffff-abcdeff767325";
    private final String requestBody = "{\"hello\":\"world\"}";

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        initJadler();
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test
    public void validateReturnsOutputFromZallyServerWhenTokenIsPassed() throws Exception {
        final ViolationsApiResponse response = makeSuccessfulViolationsRequest(token);

        assertEquals(0, response.getViolations().size());
        verifyThatRequest().havingHeaderEqualTo("Authorization", "Bearer " + token).receivedOnce();
    }

    @Test
    public void validateReturnsOutputFromZallyServerWhenTokenIsNull() throws Exception {
        final ViolationsApiResponse response = makeSuccessfulViolationsRequest(null);

        assertEquals(0, response.getViolations().size());
        verifyThatRequest().havingHeader("Authorization", nullValue());
    }

    @Test
    public void validateReturnsOutputFromZallyServerWhenTokenIsEmpty() throws Exception {
        final ViolationsApiResponse response = makeSuccessfulViolationsRequest("");

        assertEquals(0, response.getViolations().size());
        verifyThatRequest().havingHeader("Authorization", nullValue());
    }

    @Test
    public void validateRaisesCliException() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("A JSONObject text must begin with '{' at 1 [character 2 line 1]");

        mockViolationsServer(200, "");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhen400IsReturned() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("API: An error occurred while querying Zally server");


        mockViolationsServer(400, "");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhen400WithDetailsIsReturned() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage(
                "API: An error occurred while querying Zally server\n\n"
                + "Could not read document: Unexpected end-of-input"
        );

        mockViolationsServer(400, "{\"detail\":\"Could not read document: Unexpected end-of-input\"}");

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateRaisesCliExceptionWhenServerUnaccessible() throws Exception {
        expectedException.expect(CliException.class);
        expectedException.expectMessage("API: An error occurred while querying Zally server");

        ZallyApiClient client = new ZallyApiClient("http://localhost:65534/", token);
        assertNotNull(client);
        client.validate(requestBody);
    }

    @Test
    public void validateOutputFromListRules() {
        final List<Rule> rules = makeSuccessfulListRulesRequest(token);
        assertEquals(rules.size(), 2);
    }

    private ViolationsApiResponse makeSuccessfulViolationsRequest(String token) {
        final String responseBody = "{\"violations\":[], \"violations_count\":{}}";

        mockViolationsServer(200, responseBody);

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        ViolationsApiResponse response = client.validate(requestBody);

        return response;
    }

    private List<Rule> makeSuccessfulListRulesRequest(String token) {
        final String responseBody = "{\"rules\": [\n"
                + "{\"title\": \"Test Rule 1\",\"type\": \"MUST\",\"code\": \"M001\",\"is_active\": true,"
                + "\"url\": \"https://example.com/test-rule-1\"},\n"
                + "{\"title\": \"Test Rule 2\",\"type\": \"SHOULD\",\"code\": \"S001\",\"is_active\": false,"
                + "\"url\": \"https://example.com/test-rule-2\"}\n"
                + "]}";

        mockRulesServer(200, responseBody);

        ZallyApiClient client = new ZallyApiClient("http://localhost:" + port() + "/", token);
        return client.listRules();
    }

    private void mockViolationsServer(int status, String body) {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo("/api-violations")
                .havingHeaderEqualTo("Content-Type", "application/json")
                .havingBodyEqualTo(requestBody)
                .respond()
                .withStatus(status)
                .withBody(body);
    }

    private void mockRulesServer(int status, String body) {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo("/supported-rules")
                .respond()
                .withStatus(status)
                .withBody(body);
    }
}
