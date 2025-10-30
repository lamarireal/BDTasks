package com.example.a00ex

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ad0.TaskManager
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val consultas = TaskManager()

    @Test
    fun fun1() = runBlocking {
        val name = consultas.funGetUserWithDNI("111A")
        println("take it: $name")
    }

    @Test
    fun fun2() = runBlocking {
        val listUser = consultas.funGetUserListWithName("Mikel")
        println("take it: $listUser")
    }

    @Test
    fun fun3() = runBlocking {
        val listUser = consultas.funGetUserUnderAge(18)
        println("take it $listUser")
    }

    @Test
    fun fun4() = runBlocking {
        val listUser = consultas.funGetUserOlderAge(18)
        println("take it $listUser")
    }

    @Test
    fun fun5() = runBlocking {
        val provinciasVascas = listOf("Bizkaia", "Vitoria", "Gipuzkoa")
        val listUser = consultas.funGetUserOnCity(provinciasVascas)
        println("take it $listUser")
    }

    @Test
    fun fun6() = runBlocking {
        val provinciasVascas = listOf("Bizkaia", "Vitoria", "Gipuzkoa")
        val listUser = consultas.funGetUserOtherCity(provinciasVascas)
        println("take it $listUser")
    }
}
