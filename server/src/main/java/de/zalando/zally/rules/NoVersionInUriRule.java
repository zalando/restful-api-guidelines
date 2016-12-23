package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;

import java.util.List;
import java.util.ArrayList;


public class NoVersionInUriRule implements Rule {

    public final String LINK =
            "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#must-do-not-use-uri-versioning";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        String basePath = swagger.getBasePath();

        if (basePath != null && PatternUtil.hasVersionInUrl(basePath)) {
            violations.add(
                new Violation(
                    "Do Not Use URI Versioning",
                    "basePath attribute contains version number",
                    ViolationType.MUST,
                    LINK
                )
            );
        }

        return violations;
    }
}
