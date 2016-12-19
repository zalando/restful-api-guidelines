package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HyphenateHttpHeadersRule implements Rule {

    @Override
    public List<Violation> validate(Swagger swagger) {
        ArrayList<Violation> res = new ArrayList<>();
        if (swagger.getParameters() != null) {
            res.addAll(validate(swagger.getParameters().values()));
        }
        for (Path path : swagger.getPaths().values()) {
            res.addAll(validate(path.getParameters()));
            for (Operation operation : path.getOperations()) {
                res.addAll(validate(operation.getParameters()));
            }
        }
        return res;
    }

    private List<Violation> validate(Collection<Parameter> parameters) {
        if (parameters == null) {
            return Collections.emptyList();
        }
        return parameters
                .stream()
                .filter(p -> p.getIn().equals("header"))
                .filter(p -> !isHyphenated(p.getName()))
                .map(this::createViolation)
                .collect(Collectors.toList());
    }

    protected boolean isHyphenated(String s) {
        return false;
    }

    private Violation createViolation(Parameter p) {
        return new Violation("Must: Use Hyphenated HTTP Headers",
                "Header name '" + p.getName() + "' is not hyphenated",
                ViolationType.MUST,
                "http://zalando.github.io/restful-api-guidelines/naming/Naming.html");
    }
}
