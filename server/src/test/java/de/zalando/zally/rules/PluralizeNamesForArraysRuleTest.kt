package de.zalando.zally.rules

import de.zalando.zally.getFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class PluralizeNamesForArraysRuleTest {

    @Test
    fun positiveCase() {
        val swagger = getFixture("pluralizeArrayNamesValid.json")
        assertThat(PluralizeNamesForArraysRule().validate(swagger)).isNull()
    }

    @Test
    fun negativeCase() {
        val swagger = getFixture("pluralizeArrayNamesInvalid.json")
        val result = PluralizeNamesForArraysRule().validate(swagger)!!
        assertThat(result.paths).hasSameElementsAs(Arrays.asList("Pet.tag", "Pet.name"))
    }

    @Test
    fun positiveCaseSpp() {
        val swagger = getFixture("api_spp.json")
        assertThat(PluralizeNamesForArraysRule().validate(swagger)).isNull()
    }

    @Test
    fun positiveCaseTinbox() {
        val swagger = getFixture("api_tinbox.yaml")
        assertThat(PluralizeNamesForArraysRule().validate(swagger)).isNull()
    }
}
