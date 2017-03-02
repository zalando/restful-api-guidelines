package de.zalando.zally

import io.swagger.models.Swagger
import io.swagger.models.parameters.HeaderParameter
import io.swagger.parser.SwaggerParser

fun getFixture(fileName: String): Swagger = SwaggerParser().read("fixtures/$fileName")

fun swaggerWithPaths(vararg specificPaths: String): Swagger =
        Swagger().apply {
            paths = specificPaths.map { it to null }.toMap()
        }

fun swaggerWithHeaderParams(vararg names: String) =
        Swagger().apply {
            parameters = names.map { header ->
                header to HeaderParameter().apply { name = header }
            }.toMap()
        }
