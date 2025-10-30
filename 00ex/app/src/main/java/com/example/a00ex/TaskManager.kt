package com.example.ad0

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class Persona(
    val nombre: String = "",
    val apellidos: String = "",
    val dni: String = "",
    val fechaNacimiento: String = "",
    val provincia: String = ""
)

class TaskManager {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("person")

    suspend fun funGetUserWithDNI(dni: String): String {
        val result = collection.whereEqualTo("dni", dni).get().await()
        var nombre = ""
        if (!result.isEmpty) {
            nombre = result.documents[0].getString("nombre") ?: "Amogus"
        } else {
            return "nuh uh"
        }
        return nombre
    }

    suspend fun funGetUserListWithName(nombre: String): List<String> {
        val result = collection.whereEqualTo("nombre", nombre).get().await()
        val listUser = mutableListOf<String>()
        if (!result.isEmpty) {
            for (doc in result.documents) {
                val name = doc.getString("nombre")
                val apellido = doc.getString("Apellidos")
                if (name != null && apellido != null) {
                    listUser.add("$name $apellido")
                }
            }
        }
        return listUser
    }

    private fun calculatorAge(fechaNacimiento: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fecha = LocalDate.parse(fechaNacimiento, formatter)
        return Period.between(fecha, LocalDate.now()).years
    }

    suspend fun funGetUserUnderAge(edad: Int): List<String> {
        val result = collection.get().await()
        val listUser = mutableListOf<String>()

        if (!result.isEmpty) {
            for (doc in result.documents) {
                val date = doc.getString("fechaNacimiento") ?: continue
                val edadPersona = calculatorAge(date)

                if (edadPersona < edad) {
                    val name = doc.getString("nombre") ?: continue
                    val apellido = doc.getString("apellidos") ?: continue
                    listUser.add("$name $apellido")
                }
            }
        }
        return listUser
    }

    suspend fun funGetUserOlderAge(edad: Int): List<String> {
        val result = collection.get().await()
        val listUser = mutableListOf<String>()
        if (!result.isEmpty) {
            for (doc in result.documents) {
                val date = doc.getString("fechaNacimiento") ?: continue
                val edadPersona = calculatorAge(date)

                if (edadPersona >= edad) {
                    val name = doc.getString("nombre") ?: continue
                    val apellido = doc.getString("apellidos") ?: continue
                    listUser.add("$name $apellido")
                }
            }
        }
        return listUser
    }

    suspend fun funGetUserOnCity(city: List<String>): List<String> {
        val result = collection.whereIn("provincia", city).get().await()
        val listUser = mutableListOf<String>()
        if (!result.isEmpty) {
            for (doc in result.documents) {
                val name = doc.getString("nombre") ?: continue
                val apellido = doc.getString("apellidos") ?: continue
                listUser.add("$name $apellido")
            }
        }
        return listUser
    }

    suspend fun funGetUserOtherCity(city: List<String>): List<String> {
        val result = collection.get().await()
        val listUser = mutableListOf<String>()
        if (!result.isEmpty) {
            for (doc in result.documents) {
                val provincia = doc.getString("provincia") ?: continue
                if (provincia !in city) {
                    val name = doc.getString("nombre") ?: continue
                    val apellido = doc.getString("apellidos") ?: continue
                    listUser.add("$name $apellido")
                }
            }
        }
        return listUser
    }
}
