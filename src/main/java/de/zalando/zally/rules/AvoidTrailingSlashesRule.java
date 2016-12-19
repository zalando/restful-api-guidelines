package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;

public class AvoidTrailingSlashesRule implements Rule{

    @Override
    public List<Violation> validate(Swagger swagger) {
        List<Violation> violations = new ArrayList<>();
        // TODO validate the swagger object and add the violations.
        return violations;
    }
}
