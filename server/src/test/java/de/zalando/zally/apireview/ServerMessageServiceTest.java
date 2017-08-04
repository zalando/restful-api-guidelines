package de.zalando.zally.apireview;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerMessageServiceTest {

    private final ServerMessageService serverMessageService = new ServerMessageService(
        Arrays.asList("unirest-java/1.3.11", "Zally-CLI/1.0"),
        "https://github.com/zalando-incubator/zally/releases");

    @Test
    public void shouldReturnEmptyStringIfUserAgentIsUpToDate() throws Exception {
        String message = serverMessageService.serverMessage("Zally-CLI/1.1");

        assertThat(message).isEqualTo("");
    }

    @Test
    public void shouldReturnDeprecationMessageIfUserAgentIsDeprecated() throws Exception {
        String message = serverMessageService.serverMessage("Zally-CLI/1.0");

        assertThat(message).isEqualTo("Please update your CLI: https://github.com/zalando-incubator/zally/releases");
    }

    @Test
    public void shouldReturnDeprecationMessageIfUserAgentIsNotSet() throws Exception {
        String messageIfNull = serverMessageService.serverMessage(null);
        String messageIfEmptyString = serverMessageService.serverMessage("");

        assertThat(messageIfNull).isEqualTo("");
        assertThat(messageIfEmptyString).isEqualTo("");
    }
}
