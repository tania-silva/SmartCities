package com.example.smartcities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.Marker
import com.example.smartcities.api.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.info_window.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface CallbackInterface {
    fun passResultCallback(id: Int?)
}

class Anomalias : AppCompatActivity() {
    var idA: Int? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anomalias)

         idA= intent.getIntExtra(IDA, 1)
        val image = findViewById<ImageView>(R.id.imagemAnom)
        val spinner = findViewById<Spinner>(R.id.spinner)
        //Get anomalia by id
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAnomById(idA.toString().toInt())
        call.enqueue(object : retrofit2.Callback<List<com.example.smartcities.api.Marker>> {

            override fun onResponse(call: retrofit2.Call<List<com.example.smartcities.api.Marker>>, response: retrofit2.Response<List<com.example.smartcities.api.Marker>>) {
                if (response.isSuccessful){

                    for(Marker in response.body()!!){

                        findViewById<EditText>(R.id.tituloAnom).setText(Marker.titulo)
                        findViewById<EditText>(R.id.descricaoAnom).setText(Marker.descricao)

                        // DESCODIFICAR BASE64
                        Log.d("TAG MARKER", Marker.imagem.toString())
                        val decodedString: ByteArray = Base64.decode(Marker.imagem, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        image.setImageBitmap(decodedByte)

                        image.getLayoutParams().height = 350; // ajudtar tamanho da iamgem
                        image.getLayoutParams().width = 400;
                        image.requestLayout();

                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<List<com.example.smartcities.api.Marker>>, t: Throwable) {
                Log.d("TAG", "err: " + t.message)
            }

        })


        val options = arrayOf("Obras", "Acidente", "Caminhos de Água", "Parques", "Indefinido")

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

    }

    fun Delete(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.eliminarAnom(idA!!.toInt())

        call.enqueue(object : Callback<Marker> {

            override fun onResponse(call: Call<Marker>, response: Response<Marker>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Anomalias, getString(R.string.edit_sucess) , Toast.LENGTH_SHORT).show()
                    startActivity(intent)

                }else{
                    Toast.makeText(this@Anomalias, "QUASE" , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Marker>, t: Throwable) {
                Log.d("TAG", "err: " + t.message)
            }

        })

    }
    fun Update(view: View) {
        val title = findViewById<EditText>(R.id.tituloAnom)
        val desc = findViewById<EditText>(R.id.descricaoAnom)
        val  tipo = findViewById<Spinner>(R.id.spinner)
        val intent = Intent(this, MapsActivity::class.java)


        if (TextUtils.isEmpty(title.text) || TextUtils.isEmpty(desc.text)) {

            if(TextUtils.isEmpty(title.text)) {
                title.setError(getString(R.string.aviso_titulo))
            }
            if(TextUtils.isEmpty(desc.text)) {
                desc.setError(getString(R.string.aviso_desc))
            }

        }else{
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.editarAnom(idA!!.toInt(), title.text.toString(), desc.text.toString(), tipo.selectedItem.toString() )

            call.enqueue(object : Callback<Marker> {

                override fun onResponse(call: Call<Marker>, response: Response<Marker>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Anomalias, getString(R.string.edit_sucess) , Toast.LENGTH_SHORT).show()
                        startActivity(intent)

                    }else{
                        Toast.makeText(this@Anomalias, "QUASE" , Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Marker>, t: Throwable) {
                    Log.d("TAG", "err: " + t.message)
                }

            })
        }

    }
}