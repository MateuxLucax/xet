package com.xet

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Before
    fun setUp() {
        print("Entrou setup")
    }

    @Test
    fun addition_isCorrect() {
        println("Entrou corret")
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_isCorrectadsad() {
        println("Entrou corretasdas")
        assertEquals(4, 2 + 2)
    }
}