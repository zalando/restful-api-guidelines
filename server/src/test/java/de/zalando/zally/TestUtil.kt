package de.zalando.zally

import io.swagger.models.ModelImpl
import io.swagger.models.Path
import io.swagger.models.Swagger
import io.swagger.models.parameters.HeaderParameter
import io.swagger.models.properties.StringProperty
import io.swagger.parser.SwaggerParser

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
