package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NoVersionInUriRule implements Rule {

    public final String LINK =
            "https://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#must-do-not-use-uri-versioning";
    public final String PATTERN = "(.*)/v[0-9]+(.*)";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        String basePath = swagger.getBasePath();

        Pattern pattern = Pattern.compile(PATTERN);

        if (pattern.matcher(basePath).matches()) {
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
