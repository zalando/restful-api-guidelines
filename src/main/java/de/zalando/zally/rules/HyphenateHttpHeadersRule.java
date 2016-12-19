package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;

import java.util.List;

public class HyphenateHttpHeadersRule implements Rule {

    @Override
    public List<Violation> validate(Swagger swagger) {

        return null;
    }
}
