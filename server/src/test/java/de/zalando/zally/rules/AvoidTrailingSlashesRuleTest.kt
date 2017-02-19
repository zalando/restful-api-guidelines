package de.zalando.zally.rules

import de.zalando.zally.swaggerWithPaths
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AvoidTrailingSlashesRuleTest {

    @Test
    fun emptySwagger() {
        assertThat(AvoidTrailingSlashesRule().validate(Swagger())).isNull()
    }

    @Test
    fun positiveCase() {
        val testAPI = swaggerWithPaths("/api/test-api")
        assertThat(AvoidTrailingSlashesRule().validate(testAPI)).isNull()
    }

    @Test
    fun negativeCase() {
        val testAPI = swaggerWithPaths("/api/test-api/", "/api/test", "/some/other/path", "/long/bad/path/with/slash/")
        assertThat(AvoidTrailingSlashesRule().validate(testAPI)!!.paths).hasSameElementsAs(
                listOf("/api/test-api/", "/long/bad/path/with/slash/"))
    }
}
