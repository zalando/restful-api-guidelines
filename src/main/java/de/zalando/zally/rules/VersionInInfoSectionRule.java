package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

import static de.zalando.zally.utils.PatternUtil.isVersion;

public class VersionInInfoSectionRule implements Rule {

    private static final String TITLE = "Provide version information";
    private static final String DESCRIPTION = "Only the documentation, not the API itself, needs version information. It should be in the format MAYOR.MINOR.DRAFT";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#should-provide-version-information-in-openapi-documentation";

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();

        if (swagger != null && swagger.getInfo() != null) {

            String version = swagger.getInfo().getVersion();
            if (version != null) {
                if (!isVersion(version)) {
                    //not a valid version
                    violations.add(new Violation(TITLE, DESCRIPTION + "\nVersion is not in the correct format.", ViolationType.SHOULD, RULE_LINK));
                }
            } else {
                //no version at all
                violations.add(new Violation(TITLE, DESCRIPTION + "\nVersion is missing.", ViolationType.SHOULD, RULE_LINK));
            }
        }
        return violations;
    }
}
