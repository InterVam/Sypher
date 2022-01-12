package com.example.trailappinkt

import java.util.*
@Suppress("DEPRECATION")
class Algorithms {
    object vignere{
    fun vigenere(text: String, key: String, encrypt: Boolean = true): String {
        val t = if (encrypt) text.toUpperCase() else text
        val sb = StringBuilder()
        var ki = 0
        for (c in t) {
            if (c !in 'A'..'Z') continue
            val ci = if (encrypt)
                (c.toInt() + key[ki].toInt() - 130) % 26
            else
                (c.toInt() - key[ki].toInt() +  26) % 26
            sb.append((ci + 65).toChar())
            ki = (ki + 1) % key.length
        }
        return sb.toString()
    }}
    object Caesar {
        fun encrypt(s: String, key: Int): String {
            val offset = key % 26
            if (offset == 0) return s
            var d: Char
            val chars = CharArray(s.length)
            for ((index, c) in s.withIndex()) {
                if (c in 'A'..'Z') {
                    d = c + offset
                    if (d > 'Z') d -= 26
                }
                else if (c in 'a'..'z') {
                    d = c + offset
                    if (d > 'z') d -= 26
                }
                else
                    d = c
                chars[index] = d
            }
            return chars.joinToString("")
        }

        fun decrypt(s: String, key: Int): String {
            return encrypt(s, 26 - key)
        }
    }

//    fun main(args: Array<String>) {
//        val encoded = Caesar.encrypt("Bright vixens jump; dozy fowl quack.", 8)
//        println(encoded)
//        val decoded = Caesar.decrypt(encoded, 8)
//        println(decoded)
//    }



//    fun main(args: Array<String>) {
//        val key = "VIGENERECIPHER"
//        val text = "Beware the Jabberwock, my son! The jaws that bite, the claws that catch!"
//        val encoded = vigenere(text, key)
//        println(encoded)
//        val decoded = vigenere(encoded, key, false)
//        println(decoded)

//    enum class PlayfairOption {
//        NO_Q,
//        I_EQUALS_J
//    }

    class Playfair(keyword: String) {
        private val table: Array<CharArray> = Array(5, { CharArray(5) })  //5 x 5 char array




        init {
            //build table
            val used = BooleanArray(26)  //all elements false

                used[9]  = true  //J used
            val alphabet = keyword.toUpperCase() + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            var i = 0
            var j = 0
            var c: Char
            var d: Int
            for (element in alphabet) {
                c = element
                if (c !in 'A'..'Z') continue
                d = c.toInt() - 65
                if (!used[d]) {
                    table[i][j] = c
                    used[d] = true
                    if (++j == 5) {
                        if (++i == 5) break //table has been filled
                        j = 0
                    }
                }
            }
        }

        private fun getCleanText(plainText: String): String {
            val plainText2 = plainText.toUpperCase() //ensure everything is upper case
            //get rid of any non-letters and insert X between duplicate letters
            var cleanText = ""
            var prevChar = '\u0000'  //safe to assume null character won't be present in plainText
            var nextChar: Char
            for (element in plainText2) {
                nextChar = element

                //If I_EQUALS_J option specified, replace J with I
                if (nextChar == 'J' ) nextChar = 'I'
                if (nextChar != prevChar)
                    cleanText += nextChar
                else
                    cleanText += "X" + nextChar
                prevChar = nextChar
            }
            val len = cleanText.length
            if (len % 2 == 1)  {  //dangling letter at end so add another letter to complete digram
                if (cleanText[len - 1] != 'X')
                    cleanText += 'X'
                else
                    cleanText += 'Z'
            }
            return cleanText
        }

        private fun findChar(c: Char): Pair<Int, Int> {
            for (i in 0..4)
                for (j in 0..4)
                    if (table[i][j] == c) return Pair(i, j)
            return Pair(-1, -1)
        }

        fun encode(plainText: String): String {
            val cleanText = getCleanText(plainText)
            var cipherText = ""
            val length = cleanText.length
            for (i in 0 until length step 2) {
                val (row1, col1) = findChar(cleanText[i])
                val (row2, col2) = findChar(cleanText[i + 1])
                cipherText += when {
                    row1 == row2 -> table[row1][(col1 + 1) % 5].toString() + table[row2][(col2 + 1) % 5]
                    col1 == col2 -> table[(row1 + 1) % 5][col1].toString() + table[(row2 + 1) % 5][col2]
                    else         -> table[row1][col2].toString() + table[row2][col1]
                }
                if (i < length - 1) cipherText += " "
            }
            return cipherText
        }

        fun decode(cipherText: String): String {
            var decodedText = ""
            val length = cipherText.length
            for (i in 0 until length step 3) {  //cipherText will include spaces so we need to skip them
                val (row1, col1) = findChar(cipherText[i])
                val (row2, col2) = findChar(cipherText[i + 1])
                decodedText += when {
                    row1 == row2 -> table[row1][if (col1 > 0) col1 - 1 else 4].toString() + table[row2][if (col2 > 0) col2 - 1 else 4]
                    col1 == col2 -> table[if (row1 > 0) row1- 1 else 4][col1].toString() + table[if (row2 > 0) row2 - 1 else 4][col2]
                    else         -> table[row1][col2].toString() + table[row2][col1]
                }
                if (i < length - 1) decodedText += " "
            }
            return decodedText
        }

        fun printTable() {
            println("The table to be used is :\n")
            for (i in 0..4) {
                for (j in 0..4) print(table[i][j] + " ")
                println()
            }
        }
    }

//    fun main(args: Array<String>) {
//        print("Enter Playfair keyword : ")
//        val keyword: String = readLine()!!
//        var ignoreQ: String
//        do {
//            print("Ignore Q when buiding table  y/n : ")
//            ignoreQ = readLine()!!.toLowerCase()
//        }
//        while (ignoreQ != "y" && ignoreQ != "n")
//        val pfo = if (ignoreQ == "y") PlayfairOption.NO_Q else PlayfairOption.I_EQUALS_J
//        val playfair = Playfair(keyword, pfo)
//        playfair.printTable()
//        print("\nEnter plain text : ")
//        val plainText: String = readLine()!!
//        val encodedText = playfair.encode(plainText)
//        println("\nEncoded text is : $encodedText")
//        val decodedText = playfair.decode(encodedText)
//        println("Decoded text is : $decodedText")

    }
