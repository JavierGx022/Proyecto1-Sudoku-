package com.example.sodokuprueba

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sodokuprueba.Logica.Cell
import com.example.sodokuprueba.Logica.SodokuBoard
import com.example.sodokuprueba.Logica.SodokuGame
import com.example.sodokuprueba.Logica.saveStateGame
import java.util.concurrent.TimeUnit

class juego : AppCompatActivity(), SodokuBoard.onTouchListener {
    private lateinit var viewModel: saveStateGame
    private lateinit var sodokuBoard: SodokuBoard

    private var timeInSeconds = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_juego)
        sodokuBoard = findViewById(R.id.sodokuBoard)
        sodokuBoard.registerListener(this)
        viewModel= ViewModelProvider(this)[saveStateGame::class.java]
        viewModel.sodokuGame.selectedCellData.observe(this, Observer {updateSelectedCellUI(it)})
        viewModel.sodokuGame.cellLiveData.observe(this, Observer { updateCells(it) })



        val button1= findViewById<Button>(R.id.oneBtn)
        val button2= findViewById<Button>(R.id.twoBtn)
        val button3= findViewById<Button>(R.id.threeBtn)
        val button4= findViewById<Button>(R.id.fourBtn)
        val button5= findViewById<Button>(R.id.fiveBtn)
        val button6= findViewById<Button>(R.id.sixBtn)
        val button7= findViewById<Button>(R.id.sevenBtn)
        val button8= findViewById<Button>(R.id.eightBtn)
        val button9= findViewById<Button>(R.id.nineBtn)
        val buttons= listOf(button1, button2, button3, button4,button5,button6,button7,button8,button9)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val number = index + 1
                val selectedCell = viewModel.sodokuGame.selectedCellData.value
                if (selectedCell != null) {
                    val (row, column) = selectedCell
                    if (sodokuBoard.validateInput(row, column, number)) {
                        // El número ingresado es válido, lo enviamos al juego
                        viewModel.sodokuGame.Input(number)
                        // Reproducir sonido correspondiente al número ingresado
                        playNumberSound(number)
                    } else {
                        val mediaPlayer = MediaPlayer.create(this@juego, R.raw.error)
                        mediaPlayer.start()
                        Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        var dificultad= intent.getStringExtra("difficulty")
        Toast.makeText(this,"Haz elegido la dificultad: "+dificultad, Toast.LENGTH_SHORT).show()
        // Recuperar el tiempo del Intent
        timeInSeconds = intent.getIntExtra("time", 0)

        // Comenzar a contar el tiempo
        startTimer()


        val btnVerificar= findViewById<Button>(R.id.btnVerificar)
        btnVerificar.setOnClickListener {
            win()

        }
    }



    private fun updateCells(cells: List<Cell>?)= cells?.let{
        sodokuBoard.updateCells(cells)
    }

    private fun updateSelectedCellUI (cell: Pair<Int,Int>?) = cell?.let {
        sodokuBoard.updateSelectedCellUI(cell.first, cell.second)
    }


    override fun onCellTouched(row: Int, colum:Int){
        viewModel.sodokuGame.updateSelected(row,colum)
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(timeInSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calcular minutos y segundos
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60

                // Actualizar la UI con el tiempo restante en minutos y segundos
                val tvTimer = findViewById<TextView>(R.id.txtTiempoTranscurrido)
                tvTimer.text = "Tiempo restante: $minutes min $seconds seg"
            }

            override fun onFinish() {
                val mediaPlayer = MediaPlayer.create(this@juego, R.raw.perder)
                mediaPlayer.start()
                // Mostrar una alerta de tiempo agotado
                val alertDialogBuilder = AlertDialog.Builder(this@juego)
                alertDialogBuilder.setTitle("¡Tiempo agotado!")
                alertDialogBuilder.setMessage("Has perdido la partida.")
                alertDialogBuilder.setPositiveButton("Aceptar") { dialog, which ->
                    // Redirigir al usuario a MainActivity
                    val intent = Intent(this@juego, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cerrar la actividad actual
                }
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.show()
            }
        }

        // Iniciar el temporizador
        countDownTimer.start()
    }

    private fun win() {
        val isSolutionCorrect = viewModel.sodokuGame.isUserSolutionValid()

        if (isSolutionCorrect) {
            val mediaPlayer = MediaPlayer.create(this@juego, R.raw.win)
            mediaPlayer.start()

            val alertDialogBuilder = AlertDialog.Builder(this@juego)
            alertDialogBuilder.setTitle("¡Felicitaciones!")
            alertDialogBuilder.setMessage("Has ganado la partida")
            alertDialogBuilder.setPositiveButton("Genial") { dialog, which ->
                // Redirigir al usuario a MainActivity
                val intent = Intent(this@juego, MainActivity::class.java)
                startActivity(intent)
                finish() // Cerrar la actividad actual
            }
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.show()
        } else {
            val mediaPlayer = MediaPlayer.create(this@juego, R.raw.error)
            mediaPlayer.start()
            Toast.makeText(this, "Hubo un error, sigue intentando", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playNumberSound(number: Int) {
        val soundId = when (number) {
            1 -> R.raw.uno
            2 -> R.raw.dos
            3 -> R.raw.tres
            4 -> R.raw.cuatro
            5 -> R.raw.cinco
            6 -> R.raw.seis
            7 -> R.raw.siete
            8 -> R.raw.ocho
            9 -> R.raw.nueve
            else -> return
        }
        val mediaPlayer = MediaPlayer.create(this@juego, soundId)
        mediaPlayer.start()
    }



}

