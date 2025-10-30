package com.example.ex01

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // 💬 Состояние для сообщения (вместо var mensaje в UI)
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    // ----------------------------------------------------------
    // 1️⃣ Alta (Добавление продукта)
    // ----------------------------------------------------------
    fun alta(codigo: String, descripcion: String, precio: String) {
        viewModelScope.launch {
            if (codigo.isBlank() || descripcion.isBlank() || precio.isBlank()) {
                _mensaje.value = "Por favor, completa todos los campos."
                return@launch
            }

            try {
                val result = db.collection("product")
                    .whereEqualTo("codigo", codigo)
                    .get()
                    .await()

                if (!result.isEmpty) {
                    _mensaje.value = "⚠️ Ya existe un producto con el mismo código."
                    return@launch
                }

                val product = hashMapOf(
                    "codigo" to codigo,
                    "descripcion" to descripcion,
                    "precio" to precio
                )

                db.collection("product").add(product).await()
                _mensaje.value = "✅ Producto dado de alta correctamente."
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al dar de alta: ${e.message}"
            }
        }
    }

    // ----------------------------------------------------------
    // 2️⃣ Consulta por Código
    // ----------------------------------------------------------
    fun consultaCodigo(codigo: String) {
        viewModelScope.launch {
            if (codigo.isBlank()) {
                _mensaje.value = "Ingrese un código para consultar."
                return@launch
            }

            try {
                val result = db.collection("product")
                    .whereEqualTo("codigo", codigo)
                    .get()
                    .await()

                if (result.isEmpty) {
                    _mensaje.value = "❌ No se encontró producto con código $codigo"
                } else {
                    val doc = result.documents.first()
                    val desc = doc.getString("descripcion") ?: "Sin descripción"
                    val precio = doc.getString("precio") ?: "0"
                    _mensaje.value = "✅ Producto encontrado:\nCódigo: $codigo\nDescripción: $desc\nPrecio: $precio"
                }
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al consultar: ${e.message}"
            }
        }
    }
}
