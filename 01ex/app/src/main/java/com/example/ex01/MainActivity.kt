package com.example.ex01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ex01.ui.theme.Ex01Theme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ex01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductForm(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProductForm(modifier: Modifier = Modifier) {
    var codigo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Поля ввода
        FormInput(value = codigo, onValueChange = { codigo = it }, label = "Código del producto", placeholder = "Ej: 1")
        FormInput(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción", placeholder = "Ej: Televisor")
        FormInput(value = precio, onValueChange = { precio = it }, label = "Precio", placeholder = "Ej: 500")

        Spacer(Modifier.height(20.dp))

        // Кнопки
        ActionButton("Consulta por Código") {
            coroutineScope.launch {
                mensaje = consultaCodigo(codigo)
            }
        }

        ActionButton("Consulta por Descripción") {
            coroutineScope.launch {
                mensaje = consultaDescripcion(descripcion)
            }
        }

        ActionButton("Alta") {
            coroutineScope.launch {
                mensaje = alta(codigo, descripcion, precio)
            }
        }

        ActionButton("Modificación") {
            coroutineScope.launch {
                mensaje = modifica(codigo, descripcion, precio)
            }
        }

        ActionButton("Baja por código") {
            coroutineScope.launch {
                mensaje = bajaCodigo(codigo)
            }
        }

        ActionButton("Listar") {
            coroutineScope.launch {
                mensaje = listar()
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(text = "Mensaje:", style = MaterialTheme.typography.titleMedium)
        Text(text = mensaje)
    }
}

@Composable
fun FormInput(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String = "") {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = text)
    }
}

//
// ------------------ FIREBASE / ЛОГИКА ------------------
//

///////////////////////////////////////////////////////////
//                              3 - Alta                 //
///////////////////////////////////////////////////////////
suspend fun alta(codigo: String, descripcion: String, precio: String): String {
    var resultado: String

    if (codigo.isBlank() || descripcion.isBlank() || precio.isBlank()) {
        resultado = "Por favor, completa todos los campos."
        return resultado
    }

    val db = FirebaseFirestore.getInstance()

    val result = db.collection("product").whereEqualTo("codigo", codigo).get().await()
    if (!result.isEmpty) {
        return "en bd ya esta el producto con mismo codigo"
    }
    val product = hashMapOf(
        "codigo" to codigo,
        "descripcion" to descripcion,
        "precio" to precio
    )

    resultado = try {
        db.collection("product").add(product).await()
        "✅ Producto dado de alta correctamente."
    } catch (e: Exception) {
        "❌ Error al dar de alta: ${e.message}"
    }

    return resultado
}

///////////////////////////////////////////////////////////
//                              1 - Consulta Código       //
///////////////////////////////////////////////////////////
suspend fun consultaCodigo(codigo: String): String {
    var resultado: String

    if (codigo.isBlank()) {
        resultado = "Ingrese un código para consultar."
        return resultado
    }

    val db = FirebaseFirestore.getInstance()

    resultado = try {
        val result = db.collection("product").whereEqualTo("codigo", codigo).get().await()
        if (result.isEmpty) {
            "❌ No se encontró producto con código $codigo"
        } else {
            val doc = result.documents.first()
            val desc = doc.getString("descripcion") ?: "Sin descripción"
            val precio = doc.getString("precio") ?: "0"
            "✅ Producto encontrado:\nCódigo: $codigo\nDescripción: $desc\nPrecio: $precio"
        }
    } catch (e: Exception) {
        "❌ Error al consultar: ${e.message}"
    }

    return resultado
}

///////////////////////////////////////////////////////////
//                              2 - Consulta Descripción  //
///////////////////////////////////////////////////////////
suspend fun consultaDescripcion(descripcion: String): String {
    var resultado: String

    if (descripcion.isBlank()) {
        resultado = "Ingrese una descripción para consultar."
        return resultado
    }

    val db = FirebaseFirestore.getInstance()

    resultado = try {
        val result = db.collection("product").whereEqualTo("descripcion", descripcion).get().await()
        if (result.isEmpty) {
            "❌ No se encontró producto con descripción \"$descripcion\""
        } else {
            val doc = result.documents.first()
            val codigo = doc.getString("codigo") ?: "Sin código"
            val precio = doc.getString("precio") ?: "0"
            "✅ Producto encontrado:\nCódigo: $codigo\nDescripción: $descripcion\nPrecio: $precio"
        }
    } catch (e: Exception) {
        "❌ Error al consultar descripción: ${e.message}"
    }

    return resultado
}

///////////////////////////////////////////////////////////
//                              4 - Modifica              //
///////////////////////////////////////////////////////////
suspend fun modifica(codigo: String, descripcion: String, precio: String): String {
    var resultado: String

    if (codigo.isBlank()) {
        resultado = "Ingrese un código para modificar."
        return resultado
    }

    val db = FirebaseFirestore.getInstance()

    resultado = try {
        val result = db.collection("product").whereEqualTo("codigo", codigo).get().await()
        if (result.isEmpty) {
            "❌ No se encontró producto con código $codigo"
        } else {
            for (doc in result.documents) {
                db.collection("product").document(doc.id)
                    .update(mapOf("descripcion" to descripcion, "precio" to precio)).await()
            }
            "✏️ Producto modificado correctamente."
        }
    } catch (e: Exception) {
        "❌ Error al modificar: ${e.message}"
    }

    return resultado
}

///////////////////////////////////////////////////////////
//                      5 - Baja (Eliminar)              //
///////////////////////////////////////////////////////////
suspend fun bajaCodigo(codigo: String): String {
    var resultado: String

    if (codigo.isBlank()) {
        resultado = "Ingrese un código para eliminar."
        return resultado
    }

    val db = FirebaseFirestore.getInstance()

    resultado = try {
        val result = db.collection("product")
            .whereEqualTo("codigo", codigo)
            .get()
            .await()

        if (result.isEmpty) {
            "❌ No se encontró producto con código $codigo"
        } else {
            // 👇 coroutineScope гарантирует, что все async будут завершены
            coroutineScope {
                result.documents.map { doc ->
                    async {
                        db.collection("product")
                            .document(doc.id)
                            .delete()
                            .await()
                    }
                }.awaitAll() // ← ждем завершения всех async
            }

            "🗑 Producto(s) con código $codigo eliminado(s) correctamente."
        }
    } catch (e: Exception) {
        "❌ Error al eliminar: ${e.message}"
    }

    return resultado
}

///////////////////////////////////////////////////////////
//                              6 - Listar                //
///////////////////////////////////////////////////////////
suspend fun listar(): String {
    var resultado: String

    val db = FirebaseFirestore.getInstance()

    resultado = try {
        val result = db.collection("product").get().await()
        if (result.isEmpty) {
            "📭 No hay productos registrados."
        } else {
            val lista = result.documents.joinToString("\n\n") { doc ->
                val codigo = doc.getString("codigo")
                val desc = doc.getString("descripcion")
                val precio = doc.getString("precio")
                "Código: $codigo\nDescripción: $desc\nPrecio: $precio"
            }
            "📋 Listado de productos:\n\n$lista"
        }
    } catch (e: Exception) {
        "❌ Error al listar productos: ${e.message}"
    }

    return resultado
}
