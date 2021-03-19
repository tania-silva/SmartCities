package com.example.smartcities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.smartcities.Adapter.DESCRICAO
import com.example.smartcities.Adapter.ID
import com.example.smartcities.Adapter.TITULO
import com.example.smartcities.ENTITIES.Notas
import com.example.smartcities.viewModel.NotasViewModel

class VerNotas : AppCompatActivity() {
    private lateinit var desc: EditText
    private lateinit var title: EditText
    private lateinit var notaViewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        val editTitl = intent.getStringExtra(TITULO)
        val editDesc = intent.getStringExtra(DESCRICAO)


        findViewById<EditText>(R.id.tituloE).setText(editTitl)
        findViewById<EditText>(R.id.descricaoE).setText(editDesc)
        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)


    }

    fun editar(view: View) {
        title = findViewById(R.id.tituloE)
        desc = findViewById(R.id.descricaoE)
        var idd = intent.getIntExtra(ID, 0)

        val intent = Intent()
        if (TextUtils.isEmpty(title.text) || TextUtils.isEmpty(desc.text)) {

            if(TextUtils.isEmpty(title.text)) {
                title.setError(getString(R.string.aviso_titulo))
            }
            if(TextUtils.isEmpty(desc.text)) {
                desc.setError(getString(R.string.aviso_desc))
            }

        }else{
            Toast.makeText(this,"ola $idd "  , Toast.LENGTH_SHORT).show()
            val nota = Notas(id = idd, titulo = title.text.toString(), descricao = desc.text.toString())
            notaViewModel.update(nota)
            finish()
        }

    }

    fun eliminar(view: View) {
        val idd = intent.getIntExtra(ID, 0)
        notaViewModel.delete(idd)
        finish()
    }


}