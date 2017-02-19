package de.zalando.zally.utils

val <T> T.pp: T
    get(): T {
    println(this)
    return this
}


