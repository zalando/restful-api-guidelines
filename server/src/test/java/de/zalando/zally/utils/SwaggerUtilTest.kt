package de.zalando.zally.utils

import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SwaggerUtilTest {
    @Test
    fun `empty swagger has no definitions`() {
        val swagger = Swagger()
        assertThat(swagger.getAllDefinitions()).isEmpty()
    }

    @Test
    fun `should find all definitions`() {
        val swagger = getFixture("all_definitions.yaml")
        assertThat(swagger.getAllDefinitions().map { it.second }).containsExactly(
                "/pets GET 404 items",
                "/pets POST name",
                "/pets/{petId} GET 200 owners items",
                "/pets/{petId} GET no_limit",
                "/pets/{petId} GET no_limit limit_desc",
                "#/definitions/Pet",
                "#/definitions/Pets",
                "#/definitions/Names",
                "#/definitions/Nickname",
                "#/definitions/PetName",
                "#/definitions/Importance",
                "#/definitions/NameForParam",
                "#/definitions/PetName last_name",
                "#/definitions/PetName middle_name items",
                "#/definitions/Error"
        )
    }
}