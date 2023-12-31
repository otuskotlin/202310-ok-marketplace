import kotlin.reflect.KClass

/*
fun <T> willNotCompile(variable: T) {
    println(T::class.java)
}
 */

fun <T> variant1(klass: KClass<*>) {
    println(klass.java)
}

inline fun <reified T> variant2() {
    println(T::class.java)
}

