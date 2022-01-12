package com.example.trailappinkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.textfield.TextInputEditText
import android.widget.AdapterView.OnItemClickListener


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val Dropmenu = findViewById<AutoCompleteTextView>(R.id.dropdown_menu)
        val plaintext = findViewById<TextInputEditText>(R.id.inputtext)
        val key = findViewById<TextInputEditText>(R.id.key)
        val out = findViewById<TextInputEditText>(R.id.output)
        var algo =""
        val conv= findViewById<Button>(R.id.Convert)
        val menu = resources.getStringArray(R.array.Algos)
        val arrayAdapter = ArrayAdapter(this,R.layout.menu_item,menu)
        Dropmenu.setAdapter(arrayAdapter)
        Dropmenu.onItemClickListener =
            OnItemClickListener { p0, p1, p2, p3 -> algo = menu[p2] }
        conv.setOnClickListener(){
            if(algo == "Vignere Cipher" ) {
                out.setText(Algorithms.vignere.vigenere(plaintext.text.toString(),key.text.toString()))
            }
            if (algo == "Ceaser Cipher"){
                var numeric = true
                try {
                    val num =key.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    numeric = false

                }
                if ( numeric)
                    out.setText(Algorithms.Caesar.encrypt(plaintext.text.toString(),key.text.toString().toInt()))
                else
                    Toast.makeText(this, "Enter key as a number", Toast.LENGTH_SHORT).show()

            }
            if (algo == "Play Fair"){
                var playfair = Algorithms.Playfair("")
                var numeric = true
                try {
                    val num =key.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    numeric = false

                }
                if ( numeric)
                    Toast.makeText(this, "Enter key without numbers", Toast.LENGTH_SHORT).show()
                else{
                     playfair = Algorithms.Playfair(key.text.toString())

                    }
                var numeric2 = true
                try {
                    val num =plaintext.text.toString().replace("\\s".toRegex(), "").toInt()
                } catch (e: NumberFormatException) {
                    numeric2 = false

                }
                if ( numeric2)
                    Toast.makeText(this, "Enter text without numbers", Toast.LENGTH_SHORT).show()
                else {
                    val plain = plaintext.text.toString().replace("\\s".toRegex(), "")
                    val encodedText = playfair.encode(plain)
                    out.setText(encodedText)




                }





            }

        }


            }
        }

