package com.example.guessthephrase

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var guesstext: EditText
    private lateinit var guessBtn: Button
    private lateinit var messages: ArrayList<String>
    private lateinit var tvPhrase: TextView
    private lateinit var tvLetters: TextView
    private lateinit var myHighScore: TextView
    private val phrase = "Hello There"
    private var guessChar = ""
    private var count = 0
    private var guessPhrase = true
    private val phraseChar = mutableMapOf<Int, Char>()
    private var youranswer = ""
    private var score = 0
    private var highScore = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("HighScore", 0)

        myHighScore = findViewById(R.id.tvHS)
        myHighScore.text = "High Score: $highScore"


        for (ch in phrase.indices) {
            if (phrase[ch] == ' ') {
                phraseChar[ch] = ' '
                youranswer += ' '
            } else {
                phraseChar[ch] = '*'
                youranswer += '*'
            }
        }

        messages = ArrayList()

        rvMsgs.adapter = MessageAdapter(this, messages)// adap and msg that send
        rvMsgs.layoutManager = LinearLayoutManager(this) //xml

        guesstext = findViewById(R.id.etGuessField) // take user input
        guessBtn = findViewById(R.id.btGuessButton)

        guessBtn.setOnClickListener {
            val msg = guesstext.text.toString()
            if (guessPhrase) {
                if (msg == phrase) {
                    disableEntry()
                    showAlert("You win Do you want to play again:")
                } else {
                    messages.add("Wrong guess: $msg")
                    guessPhrase = false
                    tvPhrase.text = "Phrase:  " + youranswer
                    tvLetters.text = "your guess :  " + guessChar
                    if (guessPhrase) {
                        guesstext.hint = "Guess the full phrase"
                    } else {
                        guesstext.hint = "Guess a letter"
                    }
                }
            } else {
                if (msg.isNotEmpty() && msg.length == 1) {
                    youranswer = ""
                    guessPhrase = true
                    checkLetters(msg[0])
                } else {
                    Snackbar.make(clRoot, "enter a letter", Snackbar.LENGTH_LONG)
                        .show()
                }


            }
        }
        tvPhrase = findViewById(R.id.tvPrompt)
        tvLetters = findViewById(R.id.tvLetters)

        tvPhrase.text = "Phrase:  " + youranswer
        tvLetters.text = "Guessed Letters:  " + guessChar
        if (guessPhrase) {
            guesstext.hint = "Guess the full phrase"
        } else {
            guesstext.hint = "Guess a letter"
        }
    }


    private fun disableEntry() {
        guessBtn.isEnabled = false
        guessBtn.isClickable = false
        guesstext.isEnabled = false
        guesstext.isClickable = false
    }

    private fun showAlert(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in phrase.indices){
            if(phrase[i] == guessedLetter){
                phraseChar[i] = guessedLetter
                found++
            }
        }
        for(i in phraseChar){youranswer += phraseChar[i.key]}
        if(youranswer==phrase){
            disableEntry()
            updateScore()
            showAlert("You win Do you want to play again:")
        }
        if(guessChar.isEmpty()){guessChar+=guessedLetter}else{guessChar+=", "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        tvPhrase.text = "Phrase:  " + youranswer.toUpperCase()
        tvLetters.text = "Guessed Letters:  " + guessChar
        if(guessPhrase){
            guesstext.hint = "Guess the full phrase"
        }else{
            guesstext.hint = "Guess a letter"
        }
        rvMsgs.scrollToPosition(messages.size - 1)
    }
    private fun updateScore(){
        score = 10 - count
        if(score >= highScore){
            highScore = score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highScore)
                apply()
            }
            Snackbar.make(clRoot, "NEW HIGH SCORE!", Snackbar.LENGTH_LONG).show()
        }
    }

}
