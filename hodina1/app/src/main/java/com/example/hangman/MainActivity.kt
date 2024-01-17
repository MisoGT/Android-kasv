package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.TextView
import android.widget.EditText
import android.widget.Button

class MainActivity : AppCompatActivity() { //dedi od AppCompactActivity

    private val hangman = listOf("""
    ----
    |  |
    |  O
    | /|\
    | / \
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  O
    | /|\
    | / 
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  O
    | /|\
    | 
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  O
    | /|
    | 
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  O
    | /
    | 
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  O
    | 
    |
    |
    ------
""".trimIndent(), """
    ----
    |  |
    |  
    | 
    | 
    |
    ------
""".trimIndent(), """
    ----
    |  
    | 
    | 
    | 
    |
    ------
""".trimIndent(), """
    
    | 
    | 
    | 
    | 
    |
    ------
""".trimIndent(),"""
    
    
    
    
    
    
    ------
""".trimIndent())

    private lateinit var word: String
    private lateinit var status: CharArray
    private var life = hangman.lastIndex
    private var score = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        word = "application"
        status = ".".repeat(word.length).toCharArray()

        val hangmanTextView: TextView = findViewById(R.id.hangmanTextView)
        val checkButton: Button = findViewById(R.id.checkButton)
        val inputEditText: EditText = findViewById(R.id.inputEditText)

        checkButton.setOnClickListener {
            //score--
            //hangmanTextView.text = "Prve a posledne kliknutie"

            val input = inputEditText.text.toString()
            hangmanTextView.text = "$input"

            if(input.length > 1){
                if(input == word)
                    status = input.toCharArray()
                else life--
            } else if( input in word ) { //v kotline je contains vyraz TAKTO: INPUT IN WORD
                word.forEachIndexed { index, c ->
                    if (c in input )
                        status[index] = c
                }
            } else life--

            if (life > 0){
                hangmanTextView.text = "Gratulujem k vyhre"
            }
            else {
                hangmanTextView.text ="Prehral si, slovo bolo $word"
            }
        }
    }

}