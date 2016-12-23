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

    private static final String TITLE = "Prefer standard media type names";
    private static final String DESCRIPTION = "Custom media types should only be used for versioning";
    private static final String RULE_LINK = "http://zalando.github.io/restful-api-guidelines/data-formats/DataFormats.html"+
            "#should-prefer-standard-media-type-name-applicationjson";
    private static final String PATTERN_APPLICATION_PROBLEM_JSON = "^application/(problem\\+)?json$";
    private static final String PATTERN_CUSTOM_WITH_VERSIONING = "^\\w+/[-+.\\w]+;v(ersion)?=\\d+$";

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
                        if(!isApplicationJsonOrProblemJson(mediaType)){
                            if(!isCustomMediaTypeWithVersioning(mediaType)){
                                violations.add(new Violation(TITLE, DESCRIPTION, ViolationType.SHOULD, RULE_LINK, verb.name() + " " + pathName));
                            }

                        }
                    }
                }
            }
        }

        return violations;
    }

    static boolean isApplicationJsonOrProblemJson(String mediaType) {
        return mediaType.matches(PATTERN_APPLICATION_PROBLEM_JSON);
    }

    static boolean isCustomMediaTypeWithVersioning(String mediaType) {
        return mediaType.matches(PATTERN_CUSTOM_WITH_VERSIONING);
    }
}
