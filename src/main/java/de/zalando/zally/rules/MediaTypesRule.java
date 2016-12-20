package de.zalando.zally.rules;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MediaTypesRule implements Rule{

    private static final String TITLE = "Use Media Type Versioning";
    private static final String DESCRIPTION = "Version information and media type are provided together via the HTTP Content-Type header";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html"+
            "#must-use-media-type-versioning";
    private static final String APPLICATION_JSON = "application/json";
    private static final String PROBLEM_JSON = "application/problem+json";
    private static final String PATTERN = "^application/(problem\\+)?json(;v(ersion)?=(\\d)+)?$";

    @Override
    public List<Violation> validate(Swagger swagger) {

        List<Violation> violations = new ArrayList<>();

        if(swagger.getPaths() != null) {
            Map<String, Path> paths = swagger.getPaths();
            for (Map.Entry<String,Path> pathEntry : paths.entrySet()) {
                String pathName= pathEntry.getKey();
                Path path = pathEntry.getValue();
                for (Map.Entry<HttpMethod, Operation> operationEntry : path.getOperationMap().entrySet()) {
                    Operation operation = operationEntry.getValue();
                    HttpMethod verb = operationEntry.getKey();
                    List<String> mediaTypes = new ArrayList<>();
                    if(operation.getProduces() != null){
                       mediaTypes.addAll(operation.getProduces());
                    }
                    if(operation.getConsumes() != null){
                       mediaTypes.addAll(operation.getConsumes());
                    }
                    for(String mediaType : mediaTypes){
                        if(!isMediaTypeCorrectOrVersion(mediaType)){
                            violations.add(new Violation(TITLE, DESCRIPTION, ViolationType.MUST, RULE_LINK, verb.name() + " " + pathName));
                        }
                    }

                }
            }

        }

        return violations;
    }

    static boolean isMediaTypeCorrectOrVersion(String mediaType) {
        return mediaType.matches(PATTERN);
    }
}
