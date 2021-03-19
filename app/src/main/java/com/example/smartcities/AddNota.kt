package com.example.smartcities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.recyclerline.*

class AddNota : AppCompatActivity() {
    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        tituloText = findViewById(R.id.tituloText)
        descricaoText = findViewById(R.id.descricaoText)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(tituloText.text) || TextUtils.isEmpty(descricaoText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

                if(TextUtils.isEmpty(tituloText.text)){
                    tituloText.setError(getString(R.string.aviso_titulo))
                }
                if(TextUtils.isEmpty(descricaoText.text)) {
                    descricaoText.setError(getString(R.string.aviso_desc))
                }

            }else {
                replyIntent.putExtra(EXTRA_REPLY_TITULO, tituloText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricaoText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }

        }
    }

    companion object {
        const val EXTRA_REPLY_TITULO = "com.example.android.titulo"
        const val EXTRA_REPLY_DESCRICAO = "com.example.android.descricao"
    }
}