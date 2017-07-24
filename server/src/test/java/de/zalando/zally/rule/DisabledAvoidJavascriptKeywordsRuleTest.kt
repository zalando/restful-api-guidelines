package de.zalando.zally.rule

import de.zalando.zally.getResourceContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class DisabledAvoidJavascriptKeywordsRuleTest {

    @Autowired
    private val rulesValidator: SwaggerRulesValidator? = null

    @Test
    fun ruleShouldNotBePartOfRulesValidator() {
        val violatedRulesClasses = rulesValidator!!.validate(getResourceContent("avoidJavascriptInvalid.json"))
                .map { (rule) -> rule.javaClass.name }
        assertThat(violatedRulesClasses).doesNotContain(AvoidJavascriptKeywordsRule::class.java.name)
    }
}
