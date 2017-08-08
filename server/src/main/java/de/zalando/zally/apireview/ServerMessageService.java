package de.zalando.zally.apireview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class ServerMessageService {

    private Logger logger = LoggerFactory.getLogger(ServerMessageService.class);

    private List<String> deprecatedCliAgents = new ArrayList<>();
    private String releasesPage;

    ServerMessageService(@Value("#{'${zally.cli.deprecatedCliAgents}'.split(',')}") List<String> deprecatedCliAgents,
                         @Value("${zally.cli.releasesPage:}") String releasesPage){
        this.deprecatedCliAgents = deprecatedCliAgents;
        this.releasesPage = releasesPage;
    }

    protected String serverMessage(String userAgent) {
        if (userAgent != null && !userAgent.isEmpty() && deprecatedCliAgents.contains(userAgent)) {
            logger.info("received request from user-agent {}", userAgent);
            return "Please update your CLI: " + releasesPage;
        }
        return "";
    }
}
