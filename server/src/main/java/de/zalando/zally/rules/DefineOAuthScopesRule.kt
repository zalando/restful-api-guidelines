package de.zalando.zally.rules

import com.google.common.collect.Sets
import de.zalando.zally.Violation
import de.zalando.zally.ViolationType.MUST
import io.swagger.models.Operation
import io.swagger.models.Swagger
import io.swagger.models.auth.OAuth2Definition
import org.springframework.stereotype.Component

@Component
open class DefineOAuthScopesRule : Rule {
    private val TITLE = "Define and Assign Access Rights (Scopes)"
    private val RULE_LINK = "http://zalando.github.io/restful-api-guidelines/security/Security.html" +
            "#must-secure-endpoints-with-oauth-20"

    override fun validate(swagger: Swagger): Violation? {
        val definedScopes = getDefinedScopes(swagger)
        val hasTopLevelScope = hasTopLevelScope(swagger, definedScopes)
        val res = swagger.getPaths().orEmpty().entries.flatMap {
            val (pathKey, path) = it
            path.getOperationMap().orEmpty().entries.map {
                val (method, operation) = it
                val actualScopes = extractAppliedScopes(operation)
                val undefinedScopes = Sets.difference(actualScopes, definedScopes)
                val unsecured = undefinedScopes.size == actualScopes.size && !hasTopLevelScope
                val msg = when {
                    undefinedScopes.isNotEmpty() ->
                        "undefined scopes " + undefinedScopes.map { "'${it.second}'" }.joinToString(", ")
                    unsecured ->
                        "no valid OAuth2 scope"
                    else -> null
                }
                if (msg != null) "Path $pathKey $method has $msg" to "$pathKey $method" else null
            }.filterNotNull()
        }
        return if (!res.isEmpty()) {
            val (desc, paths) = res.unzip()
            Violation(TITLE, desc.joinToString("\n"), MUST, RULE_LINK, paths)
        } else null
    }

    // get the scopes from security definition
    private fun getDefinedScopes(swagger: Swagger): Set<Pair<String, String>> =
            swagger.getSecurityDefinitions().orEmpty().entries.flatMap {
                val (group, def) = it
                (def as? OAuth2Definition)?.scopes?.keys?.map { scope -> group to scope }.orEmpty()
            }.toSet()

    // Extract all oauth2 scopes applied to the given operation into a simple list
    private fun extractAppliedScopes(operation: Operation): Set<Pair<String, String>> =
            operation.security?.flatMap { groupDefinition ->
                groupDefinition.entries.flatMap {
                    val (group, scopes) = it
                    scopes.map { group to it }
                }
            }.orEmpty().toSet()

    private fun hasTopLevelScope(swagger: Swagger, definedScopes: Set<Pair<String, String>>): Boolean =
            swagger.security?.any { securityRequirement ->
                securityRequirement.requirements.entries.any {
                    val (group, scopes) = it
                    scopes.any { scope -> (group to scope) in definedScopes }
                }
            } ?: false

}
