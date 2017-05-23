package de.zalando.zally.rule

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PluralizeResourceNamesRuleTest {

    @Test
    fun positiveCase() {
        val swagger = getFixture("pluralizeResourcesValid.json")
        assertThat(PluralizeResourceNamesRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("pluralizeResourcesInvalid.json")
        val result = PluralizeResourceNamesRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("/pet/cats", "/pets/cats/{cat-id}/tail/{tail-id}/strands"))
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(PluralizeResourceNamesRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        val result = PluralizeResourceNamesRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(listOf("/queue/configs/{config-id}", "/queue/models",
            "/queue/models/{model-id}", "/queue/summaries"))
    }

}
