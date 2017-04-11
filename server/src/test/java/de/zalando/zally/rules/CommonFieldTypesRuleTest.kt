package de.zalando.zally.rules

import de.zalando.zally.getFixture
import de.zalando.zally.testConfig
import io.swagger.models.Swagger
import io.swagger.models.properties.AbstractProperty
import io.swagger.models.properties.IntegerProperty
import io.swagger.models.properties.StringProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CommonFieldTypesRuleTest {
    private val rule = CommonFieldTypesRule(testConfig)

    object PropertyWithNullType : AbstractProperty() {
        override fun getType(): String? {
            return null
        }
    }

    @Test
    fun matchesCommonFieldsTypeEmpty() {
        assertThat(rule.checkField("", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsTypeNotCommon() {
        assertThat(rule.checkField("unknown", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsType() {
        assertThat(rule.checkField("id", StringProperty())).isNull()
        assertThat(rule.checkField("id", StringProperty("UUID"))).isNull()
        assertThat(rule.checkField("created", StringProperty("date-time"))).isNull()
        assertThat(rule.checkField("modified", StringProperty("date-time"))).isNull()
        assertThat(rule.checkField("type", StringProperty())).isNull()
    }

    @Test
    fun matchingShouldBeCaseInsensitive() {
        assertThat(rule.checkField("iD", IntegerProperty())).isNotNull()
        assertThat(rule.checkField("CREATED", IntegerProperty())).isNotNull()
        assertThat(rule.checkField("tYpE", PropertyWithNullType)).isNotNull()
        assertThat(rule.checkField("CREated", StringProperty("time"))).isNotNull()
        assertThat(rule.checkField("ID", StringProperty("uuid"))).isNull()
        assertThat(rule.checkField("Id", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsTypeInvalid() {
        assertThat(rule.checkField("id", IntegerProperty())).isNotNull()
    }

    @Test
    fun matchesCommonFieldsFormatEmpty() {
        assertThat(rule.checkField("", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsFormatNotCommon() {
        assertThat(rule.checkField("unknown", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsFormat() {
        assertThat(rule.checkField("id", StringProperty("UUID"))).isNull()
        assertThat(rule.checkField("created", StringProperty("date-time"))).isNull()
        assertThat(rule.checkField("modified", StringProperty("date-time"))).isNull()
    }

    @Test
    fun matchesCommonFieldFormatInvalid() {
        assertThat(rule.checkField("id", IntegerProperty())).isNotNull()
    }

    @Test
    fun validateEmpty() {
        assertThat(rule.validate(Swagger())).isNull()
    }

    @Test
    fun positiveCase() {
        assertThat(rule.validate(getFixture("common_fields.yaml"))).isNull()
    }

    @Test
    fun negativeCase() {
        val result = rule.validate(getFixture("common_fields_invalid.yaml"))!!
        assertThat(result.paths).hasSameElementsAs(listOf("#/definitions/Partner", "#/definitions/JobSummary"))
        assertThat(result.description).contains(listOf("'id'", "'created'", "'modified'", "'type"))
    }

}
