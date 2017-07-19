package de.zalando.zally

import com.codahale.metrics.MetricRegistry
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.swagger.models.ModelImpl
import io.swagger.models.Operation
import io.swagger.models.Path
import io.swagger.models.Response
import io.swagger.models.Swagger
import io.swagger.models.parameters.HeaderParameter
import io.swagger.models.properties.StringProperty
import io.swagger.parser.SwaggerParser
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices

val testConfig: Config by lazy {
    ConfigFactory.load("rules-config-test.conf")
}

val testMetricRegistry: MetricRegistry by lazy {
    MetricRegistry()
}

val testMetricServices: DropwizardMetricServices by lazy {
    DropwizardMetricServices(testMetricRegistry)
}

fun getFixture(fileName: String): Swagger = SwaggerParser().read("fixtures/$fileName")

fun swaggerWithPaths(vararg specificPaths: String): Swagger =
    Swagger().apply {
        paths = specificPaths.map { it to Path() }.toMap()
    }

fun swaggerWithHeaderParams(vararg names: String) =
    Swagger().apply {
        parameters = names.map { header ->
            header to HeaderParameter().apply { name = header }
        }.toMap()
    }

fun swaggerWithDefinitions(vararg defs: Pair<String, List<String>>): Swagger =
        Swagger().apply {
            definitions = defs.map { def ->
                def.first to ModelImpl().apply {
                    properties = def.second.map { prop -> prop to StringProperty() }.toMap()
                }
            }.toMap()
        }

fun swaggerWithOperations(operations: Map<String, Iterable<String>>): Swagger =
        Swagger().apply {
            val path = Path()
            operations.forEach { method, statuses ->
                val operation = Operation().apply {
                    statuses.forEach { addResponse(it, Response()) }
                }
                path.set(method, operation)
            }
            paths = mapOf("/test" to path)
        }
