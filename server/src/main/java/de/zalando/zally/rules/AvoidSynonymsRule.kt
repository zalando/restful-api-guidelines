package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger

class AvoidSynonymsRule : AbstractRule() {

    val TITLE = "Use common property names"
    val DESC_PATTERN = "Property names should utilize common dictionary"
    val RULE_URL = ""

    private val COMMON_DICTIONARY = listOf(
            "customer_id" to listOf("customerid", "c_id", "cid", "custid", "cust_id"),
            "order_id" to listOf("orderid", "o_id", "ord_id"),
            "payment_id" to listOf("paymentid", "p_id"),
            "parcel_id" to listOf("parcelid"),
            "article_id" to listOf("articleid", "a_id", "art_id")
    )

    override fun validate(swagger: Swagger): Violation? {
        val dict = COMMON_DICTIONARY.flatMap { (right, wrongList) -> wrongList.map { it to right } }.toMap()
        val res = swagger.definitions.orEmpty().flatMap { (defName, def) ->
            val props = def?.properties.orEmpty()
            props.keys.filter { it in dict }.map { it to "#/definitions/$defName" }
        }
        return if (res.isNotEmpty()) {
            val (names, paths) = res.unzip()
            val details = names.toSet().groupBy(dict::get)
                    .map { (right, wrongList) -> right + " instead of " + wrongList.joinToString(", ") }
                    .joinToString("\n")
            Violation(this, TITLE, "$DESC_PATTERN:\n$details", ViolationType.SHOULD, RULE_URL, paths.toSet().toList())
        } else null
    }
}