package de.zalando.zally.rule

import com.google.common.collect.Sets
import de.zalando.zally.violation.Violation
import de.zalando.zally.violation.ViolationType.MUST
import de.zalando.zally.violation.ViolationType.SHOULD
import io.swagger.models.Operation
import io.swagger.models.Swagger
import io.swagger.models.auth.OAuth2Definition
import org.springframework.stereotype.Component

@Component
class ExtensibleEnumRule : AbstractRule() {
    override val title = "Prefer Compatible Extensions"
    override val url = "http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
        "#should-prefer-compatible-extensions"
    override val violationType = SHOULD
    override val code = "S012"
    private val DESC = "Prefer using extensible extensions on the server-side"

    override fun validate(swagger: Swagger): Violation? {
        return null
    }

}
