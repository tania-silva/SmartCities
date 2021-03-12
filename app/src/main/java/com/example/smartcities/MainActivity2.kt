package com.example.smartcities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcities.Adapter.LineAdapter
import com.example.smartcities.DataClass.Place
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    private lateinit var myList: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        myList = ArrayList<Place>()

        for (i in 0 until 100) {
            myList.add(Place("Nota $i", "Não passar pela rua $i porque está em obras"))
        }

        recycler_view.adapter = LineAdapter(myList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        //recycler_view.setHasFixedSize(true)
    }

    fun floatbutton(view: View) {
        Toast.makeText(this, "Adicionar uma nova nota", Toast.LENGTH_SHORT).show()
    }

    fun recycler(view: View){

        Toast.makeText(this, "Editar Eliminar Notas", Toast.LENGTH_SHORT).show()
    }

}