package com.example.a00ex

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



data class Persona(
    val dni: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val provincia: String = ""
)
class TaskManager {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("person")

    //fun1
    suspend fun fun1(dni: String): String? {
        val data = collection.whereEqualTo("dni", dni).get().await()
        if (data.isEmpty) {
            return "NO HAY"
        }
        return data.documents.firstOrNull()?.getString("nombre")
    }

    //fun2
    suspend fun fun2(nombre: String): List<String> {
        val data = collection.whereEqualTo("nombre", nombre).get().await()
        return data.documents.map { "${it.getString("nombre")} ${it.getString("apellidos")}" }
    }

    //fun3
    suspend fun fun3(edad: Int): List<String> {
        val person = collection.get().await().toObjects(Persona::class.java)
        return person as List<String>
    }
}