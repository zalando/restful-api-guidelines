package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Model;
import io.swagger.models.Swagger;

import java.util.*;

public class AvoidJavascriptKeywordsRule implements Rule {

    public static final String TITLE = "Should: avoid reserved Javascript keywords";
    public static final String DESC_PATTERN = "Property name '%s' is reserved javascript keyword";
    public static final String RULE_URL = "http://zalando.github.io/restful-api-guidelines/naming/Naming.html" +
            "#should-reserved-javascript-keywords-should-be-avoided";
    private static final Set<String> RESERVED_KEYWORDS = new HashSet<>(Arrays.asList(
            "break", "do", "in", "typeof", "case", "else", "instanceof", "var", "catch", "export", "new", "void",
            "class", "extends", "return", "while", "const", "finally", "super", "with", "continue", "for", "switch",
            "yield", "debugger", "function", "this", "default", "if", "throw", "delete", "import", "try"
    ));

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> res = new ArrayList<>();
        if (swagger.getDefinitions() != null) {
            for (Map.Entry<String, Model> entry : swagger.getDefinitions().entrySet()) {
                String definition = entry.getKey();
                if(entry.getValue().getProperties() != null) {
                    for (String prop : entry.getValue().getProperties().keySet()) {
                        if (RESERVED_KEYWORDS.contains(prop)) {
                            res.add(new Violation(
                                    TITLE,
                                    String.format(DESC_PATTERN, prop),
                                    ViolationType.SHOULD, RULE_URL, definition + "." + prop));
                        }
                    }
                }
            }
        }
        return res;
    }
}
