package de.zalando.zally.rules

import io.swagger.models.Info
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VersionInInfoSectionRuleTest {

    private fun swaggerWithVersion(versionValue: String) =
            Swagger().apply {
                info = Info().apply {
                    version = versionValue
                }
            }

    @Test
    fun forEmptySwagger() {
        assertThat(VersionInInfoSectionRule().validate(Swagger())).isNull()
    }

    @Test
    fun correctVersion() {
        val swagger = swaggerWithVersion("1.2.3")
        assertThat(VersionInInfoSectionRule().validate(swagger)).isNull()
    }

    @Test
    fun missingVersion() {
        val swagger = Swagger().apply { info = Info() }
        val result = VersionInInfoSectionRule().validate(swagger)!!
        assertThat(result.description).contains("Version is missing")
    }

    @Test
    fun wrongVersion() {
        val swagger = swaggerWithVersion("1.2.3-a")
        val result = VersionInInfoSectionRule().validate(swagger)!!
        assertThat(result.description).contains("Specified version has incorrect format")
    }
}
