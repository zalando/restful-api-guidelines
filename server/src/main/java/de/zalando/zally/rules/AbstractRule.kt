package de.zalando.zally.rules

abstract class AbstractRule() : Rule {

    override fun getName(): String {
        return javaClass.simpleName.toLowerCase()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractRule) return false

        if (getName() != other.getName()) return false

        return true
    }

    override fun hashCode(): Int {
        return getName().hashCode()
    }
}
