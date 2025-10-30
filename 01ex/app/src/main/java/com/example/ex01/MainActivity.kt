package com.example.ex01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.foundation.rememberScrollState
import com.example.ex01.ui.theme.Ex01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ex01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // INI codigo ventana
                    myForm(Modifier.padding(innerPadding))
                    // FIN codigo ventana

                }
            }
        }
    }
}

@Composable
fun myForm(modifier: Modifier) {
    var codigo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally, // centra horizontalmente
        verticalArrangement = Arrangement.Top // empieza desde arriba
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("Prueba") },
            placeholder = { Text("ej") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        formInput(value=codigo,onValueChange = { codigo = it },"Código del producto","Ejemplo: 1")
        formInput(descripcion,{ descripcion = it },"Descripcion")
        formInput(precio,{ precio = it },"Precio")
        Spacer(modifier = modifier.height(20.dp)) // opcional: espacio entre Crono y el botón
        funButton("Consulta Por Código") { consultaCodigo(codigo) {mensaje = it} }
        funButton("Consulta Por Descripción") { consultaDescripcion(descripcion) {mensaje = it} }
        funButton("Baja por código") { bajaCodigo(codigo) {mensaje = it} }
        funButton("Alta") { alta(codigo, descripcion, precio) {mensaje = it} }
        funButton("Modificación") { modifica(codigo, descripcion, precio) {mensaje = it} }
        funButton("Listar") { listar() {mensaje = it} }
        Text(text = "mensaje:")
        Text(text = mensaje)
    }
}

@Composable
fun formInput(value: String, onValueChange: (String) -> Unit,desc: String, ej: String = "") {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(desc) },
        placeholder = { Text(ej) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun funButton(
    buttonText: String,
    onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = buttonText)
    }
}
fun alta(codigo: String, descripcion: String, precio: String, callback: (String) -> Unit) {
    // Aquí iría la lógica para dar de alta
    callback("Producto dado de alta:\nCódigo: $codigo\nDescripción: $descripcion\nPrecio: $precio")
}

fun consultaCodigo(codigo: String, callback: (String) -> Unit) {
    // Lógica para consultar por código
    if (codigo.isBlank()) callback("Ingrese un código para consultar")
    else callback("Consulta por código: $codigo\nProducto encontrado (ejemplo)")
}

fun consultaDescripcion(descripcion: String, callback: (String) -> Unit) {
    // Lógica para consultar por descripción
    if (descripcion.isBlank()) callback("Ingrese una descripción para consultar")
    else callback("Consulta por descripción: $descripcion\nProducto encontrado (ejemplo)")
}

fun bajaCodigo(codigo: String, callback: (String) -> Unit) {
    // Lógica para eliminar producto
    if (codigo.isBlank()) callback("Ingrese un código para eliminar")
    else callback("Producto con código $codigo eliminado (ejemplo)")
}

fun modifica(codigo: String, descripcion: String, precio: String, callback: (String) -> Unit) {
    // Lógica para modificar producto
    if (codigo.isBlank()) callback("Ingrese un código para modificar")
    else callback("Producto modificado:\nCódigo: $codigo\nDescripción: $descripcion\nPrecio: $precio")
}
fun listar(callback: (String) -> Unit) {
    // Lógica para listar
    callback("Muestra todo")
}