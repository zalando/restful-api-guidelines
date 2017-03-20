package de.zalando.zally.rules

import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions
import org.junit.Test

class NoUnusedDefinitionsRuleTest {
    @Test
    fun positiveCase() {
        Assertions.assertThat(NoUnusedDefinitionsRule().validate(getFixture("unusedDefinitionsValid.json"))).isNull()
    }

    @Test
    fun negativeCase() {
        val results = NoUnusedDefinitionsRule().validate(getFixture("unusedDefinitionsInvalid.json"))!!.paths
        Assertions.assertThat(results).hasSameElementsAs(listOf(
                "#/definitions/PetName",
                "#/parameters/FlowId"
        ))
    }

    @Test
    fun emptySwaggerShouldPass() {
        val swagger = Swagger()
        Assertions.assertThat(NoUnusedDefinitionsRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        Assertions.assertThat(NoUnusedDefinitionsRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        Assertions.assertThat(NoUnusedDefinitionsRule().validate(swagger)).isNull()
    }

}