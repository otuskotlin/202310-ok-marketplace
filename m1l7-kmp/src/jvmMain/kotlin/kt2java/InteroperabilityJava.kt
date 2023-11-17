package ru.otus.otuskotlin.m1l7.kt2java

class InteroperabilityJava {


    @JvmName("customName")
    fun `asd asd`() = "JVM: someFunction"


    /**
     * JvmOverloads указывает компилятору сгенерировать соответствующее число вариантов метода, доступных из Java
     */
    @JvmOverloads
    fun defaults(param1: String = "param1-default-val", param2: Int = 1, param3: Boolean = false) =
        "param1 = $param1, param2 = $param2, param3 = $param3"


    companion object {
        @JvmStatic
        fun functionOne() {
            println("InteroperabilityJava, method = functionOne")
        }
    }
}

