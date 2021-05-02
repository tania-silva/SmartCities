package com.example.smartcities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso


interface CallbackInterface {
    fun passResultCallback(id: Int?)
}

class Anomalias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anomalias)

        val id = intent.getStringExtra(IDA)
        val titulo = intent.getStringExtra(TITULOA)
        val descricao = intent.getStringExtra(DESCRICAOA)
        val imagem = intent.getStringExtra(IMAGEM)

        findViewById<EditText>(R.id.tituloAnom).setText(titulo)
        findViewById<EditText>(R.id.descricaoAnom).setText(descricao)
        val image = findViewById<ImageView>(R.id.imagemAnom)

        Picasso.get().load(imagem).into(image);       //Passar o link da imagem

        image.getLayoutParams().height = 700;
        image.getLayoutParams().width = 700;
        image.requestLayout();

    }
}