package com.example.smartcities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcities.Adapter.NotasAdapter
import com.example.smartcities.DataClass.Place
import com.example.smartcities.ENTITIES.Notas
import com.example.smartcities.viewModel.NotasViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    private lateinit var notasViewModel: NotasViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        /* calling the action bar
        var actionBar = getSupportActionBar()
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar?.setDisplayHomeAsUpEnabled(true)*/


        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NotasAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotes.observe(this, Observer { cities ->
            // Update the cached copy of the words in the adapter.
            cities?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.floatbutton)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity2, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }


    fun recycler(view: View) {
        Toast.makeText(this, "Olaaaaa", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitulo = data?.getStringExtra(AddNota.EXTRA_REPLY_TITULO)
            val pdescricao = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRICAO)

            if (ptitulo!= null && pdescricao != null) {
                val nota = Notas(titulo = ptitulo, descricao = pdescricao)
                notasViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                    applicationContext,
                    "Erro ao guardar",
                    Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_notas, menu)

//Se o utilizador estiver logado o menu aparece
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.login_shared)]==true){
                val logout = menu!!.findItem(R.id.m_logout)
                logout.isVisible = true

                val map = menu!!.findItem(R.id.m_map)
                map.isVisible = true
            }
        }
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.m_logout -> {

                //Alterar o Shared Preferences
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.login_p), Context.MODE_PRIVATE
                )
                with(sharedPref.edit()){
                    putBoolean(getString(R.string.login_shared), false)
                    putString(getString(R.string.nome), "")
                    putInt(getString(R.string.id_utl), 0)
                    commit()
                }

                //Navegar para o menu inicial
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            } R.id.m_map -> {
                //Navegar para o menu das notas
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}