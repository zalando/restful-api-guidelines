package de.zalando.zally.configuration;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "ui")
public class UiConfiguration {

    private Oauth oauth = new Oauth();

    public Oauth getOauth() {
        return oauth;
    }

    public static class Oauth {
        private Boolean enabled;

        private String authorizationUrl;

        private String clientId;

        private String redirectUri;

        private String tokeninfoUrl;

        private String scopes;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getAuthorizationUrl() {
            return authorizationUrl;
        }

        public void setAuthorizationUrl(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public String getScopes() {
            return scopes;
        }

        public void setScopes(String scopes) {
            this.scopes = scopes;
        }

        public String getTokeninfoUrl() {
            return tokeninfoUrl;
        }

        public void setTokeninfoUrl(String tokeninfoUrl) {
            this.tokeninfoUrl = tokeninfoUrl;
        }
    }
}
