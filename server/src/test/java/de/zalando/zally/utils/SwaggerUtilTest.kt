package de.zalando.zally.utils

import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SwaggerUtilTest {
    @Test
    fun `empty swagger has no definitions`() {
        val swagger = Swagger()
        assertThat(swagger.getAllJsonObjects()).isEmpty()
    }

    @Test
    fun `should find all definitions`() {
        val swagger = getFixture("all_definitions.yaml")
        assertThat(swagger.getAllJsonObjects().map { it.second }).hasSameElementsAs(listOf(
                "/pets GET 404 items",
                "/pets/{petId} GET 200",
                "/pets/{petId} GET 200 size",
                "/pets/{petId} GET 200 owners items",
                "/pets/{petId} GET no_limit",
                "/pets/{petId} GET no_limit limit_desc",
                "#/definitions/Pet",
                "#/definitions/Names items",
                "#/definitions/Nickname",
                "#/definitions/PetName",
                "#/definitions/Importance",
                "#/definitions/NameForParam",
                "#/definitions/PetName last_name",
                "#/definitions/PetName middle_name items",
                "#/definitions/Error"
        ))
    }
}