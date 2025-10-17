package com.example.a00ex

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val consultas = TaskManager()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fun1() = runBlocking {
        val nombre = consultas.fun1("111A")
        assertEquals("Mikel", nombre)
    }

    @Test
    fun fun2() {

    }

    @Test
    fun fun3() {

    }

    @Test
    fun fun4() {

    }

    @Test
    fun fun5() {

    }

    @Test
    fun fun6() {

    }

    @Test
    fun addInfo() {

    }
}