package com.example.smartcities

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

class Mapa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
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
            } R.id.m_notas -> {
                //Navegar para o menu das notas
                var intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@Mapa, getString(R.string.back), Toast.LENGTH_SHORT).show()
    }


}

