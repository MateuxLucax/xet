@file:JvmName("Log")

// For preventing errors like
// Method v in android.util.Log not mocked
// when running tests

package android.util

fun e(tag: String, msg: String, t: Throwable): Int {
    println("ERROR: $tag: $msg")
    return 0
}

fun e(tag: String, msg: String): Int {
    println("ERROR: $tag: $msg")
    return 0
}

fun v(tag: String, msg: String): Int {
    println("VERBOSE: $tag: $msg")
    return 0
}

fun i(tag: String, msg: String): Int {
    println("INFO: $tag: $msg")
    return 0
}

fun w(tag: String, msg: String): Int {
    println("WARN: $tag: $msg")
    return 0
}
