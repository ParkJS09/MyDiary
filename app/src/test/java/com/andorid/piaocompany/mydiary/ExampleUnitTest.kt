package com.andorid.piaocompany.mydiary

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {
        //Extension Functions : 함수확장
        //원래 있던 함수처럼 접근하여 사용이 가능
        //Extension보다 Class내에 있던 원래 함수가 더 우선순위가 높음.
//        println("getSum : ${getSum(10,12)}")
//        println("FunctionExtension_1.thisGetSum = ${10.thisGetSum(90)}")
        //함수확장 String Example
//        println("한경대".thisFuck("fuck"))
        //함수확장 infix Example
//        println(10 max 180)
        //함수 확장시 주의사항 함수의 우선순위!
//        printFoo(extensionClass())

        println("null is ${getSize(null)}")
        println("ParkJongSun is ${getSize("ParkJongSun")}")
        println("TEST Elvis Opreator is ${getSize("TEST Elvis Opreator")}")

    }

    fun getSum(a : Int, b : Int) = a + b

    fun Int.thisGetSum( b : Int) : Int? = this + b

    fun String.thisFuck( name : String) = " ${name},${this } you "

    //파라미터가 하나일 경우에만 사용 가능
    infix fun Int.max ( y : Int) = if(this > y ) this else y
}


open class originalClass

fun originalClass.foo() = "i'm originalClass"

class extensionClass : originalClass()

fun extensionClass.foo(num : Int) = "i'm extensionClass"

fun printFoo( original : extensionClass ) {
    println(original.foo())
    //확장함수에 이름이 동일한 함수와 같은 경우 별도의 파라미터를 추가하여 구분할 수 있다.
    println(original.foo(1))
}


fun getSize( text : String? ) : Int = text?.length?:0

