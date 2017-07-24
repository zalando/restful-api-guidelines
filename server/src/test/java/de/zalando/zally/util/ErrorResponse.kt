package de.zalando.zally.util

data class ErrorResponse (
        var title: String? = null,
        var status: String? = null,
        var detail: String? = null
)
