package de.zalando.zally;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.Boolean;
import java.lang.System;
import java.util.Map;
import de.zalando.zally.configuration.UiConfiguration;

@Controller
public class UiController {

    private UiConfiguration uiConfiguration;

    @Autowired
    public UiController(UiConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String ui(Map<String, Object> model) {
        model.put("oauth", this.uiConfiguration.getOauth());
        return "ui";
    }
}
