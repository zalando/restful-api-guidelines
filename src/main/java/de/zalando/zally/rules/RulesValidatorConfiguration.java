package de.zalando.zally.rules;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RulesValidatorConfiguration {

    @Bean
    public RulesValidator createRulesValidator() {
        return new RulesValidator(Arrays.asList(
                new AvoidJavascriptKeywordsRule(),
                new AvoidLinkHeadersRule(),
                new AvoidTrailingSlashesRule(),
                new CommonFieldNamesRule(),
                new DefineOAuthScopesRule(),
                new EverySecondPathLevelParameterRule(),
                new HyphenateHttpHeadersRule(),
                new KebabCaseInPathSegmentsRule(),
                new LimitNumberOfResourcesRule(),
                new LimitNumberOfSubresourcesRule(),
                new MediaTypesRule(),
                new NestedPathsCouldBeRootPathsRule(),
                new NoVersionInUriRule(),
                new PascalCaseHttpHeadersRule(),
                new PluralizeNamesForArraysRule(),
                new PluralizeResourceNamesRule(),
                new SecureWithOAuth2Rule(),
                new SnakeCaseForQueryParamsRule(),
                new SnakeCaseInPropNameRule(),
                new SuccessResponseAsJsonObjectRule(),
                new Use429HeaderForRateLimitRule(),
                new UseProblemJsonRule(),
                new VersionInInfoSectionRule()
        ));
    }
}
