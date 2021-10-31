package com.example.guessthephrase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {

    private lateinit var edPh: EditText
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val dbHlpr=DBHlpr(applicationContext)
        edPh = findViewById(R.id.edPh)
        btnAdd = findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val ph=edPh.text.toString()
            if (ph.isNotEmpty()){
                val s= dbHlpr.addPha(ph)
                if (s!=-1L){
                    Toast.makeText(applicationContext, "phrase is add $s", Toast.LENGTH_SHORT).show()
                    edPh.text.clear()
                }
            }
        }


    }
}