package com.example.sodokuprueba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sodokuprueba.Logica.SodokuGame
import com.example.sodokuprueba.ui.theme.SodokuPruebaTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = arrayOf("Facil", "Medio", "Dificil")
        val spinner= findViewById<Spinner>(R.id.spinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val btnIniciar= findViewById<Button>(R.id.btnPlay)

        btnIniciar.setOnClickListener {
            val selectedDifficulty = spinner.selectedItem.toString() // Obtener la opción seleccionada en el Spinner
            val timeInSeconds = when (selectedDifficulty) {
                "Facil" -> 10 // 5 minutos
                "Medio" -> 600 // 10 minutos
                "Dificil" -> 900 // 15 minutos
                else -> 300 // Default 5 minutos
            }

            val intent = Intent(this, juego::class.java).apply {
                putExtra("difficulty", selectedDifficulty) // Agregar la opción seleccionada como un extra en el Intent
                putExtra("time", timeInSeconds)
            }

            startActivity(intent)
        }
    }
}

