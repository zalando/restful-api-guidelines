package de.zalando.zally.rules

import de.zalando.zally.getFixture
import io.swagger.models.Swagger
import io.swagger.models.properties.AbstractProperty
import io.swagger.models.properties.IntegerProperty
import io.swagger.models.properties.StringProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CommonFieldNamesRuleTest {
    private val rule = CommonFieldNamesRule()

    object PropertyWithNullType : AbstractProperty() {
        override fun getType(): String? {
            return null
        }
    }

    @Test
    fun matchesCommonFieldsTypeEmpty() {
        assertThat(CommonFieldNamesRule.checkField("", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsTypeNotCommon() {
        assertThat(CommonFieldNamesRule.checkField("unknown", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsType() {
        assertThat(CommonFieldNamesRule.checkField("id", StringProperty())).isNull()
        assertThat(CommonFieldNamesRule.checkField("id", StringProperty("UUID"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("created", StringProperty("date-time"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("modified", StringProperty("date-time"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("type", StringProperty())).isNull()
    }

    @Test
    fun matchingShouldBeCaseInsensitive() {
        assertThat(CommonFieldNamesRule.checkField("iD", IntegerProperty())).isNotNull()
        assertThat(CommonFieldNamesRule.checkField("CREATED", IntegerProperty())).isNotNull()
        assertThat(CommonFieldNamesRule.checkField("tYpE", PropertyWithNullType)).isNotNull()
        assertThat(CommonFieldNamesRule.checkField("CREated", StringProperty("time"))).isNotNull()
        assertThat(CommonFieldNamesRule.checkField("ID", StringProperty("uuid"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("Id", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsTypeInvalid() {
        assertThat(CommonFieldNamesRule.checkField("id", IntegerProperty())).isNotNull()
    }

    @Test
    fun matchesCommonFieldsFormatEmpty() {
        assertThat(CommonFieldNamesRule.checkField("", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsFormatNotCommon() {
        assertThat(CommonFieldNamesRule.checkField("unknown", StringProperty())).isNull()
    }

    @Test
    fun matchesCommonFieldsFormat() {
        assertThat(CommonFieldNamesRule.checkField("id", StringProperty("UUID"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("created", StringProperty("date-time"))).isNull()
        assertThat(CommonFieldNamesRule.checkField("modified", StringProperty("date-time"))).isNull()
    }

    @Test
    fun matchesCommonFieldFormatInvalid() {
        assertThat(CommonFieldNamesRule.checkField("id", IntegerProperty())).isNotNull()
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
