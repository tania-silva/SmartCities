package com.example.smartcities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.OutputPost
import com.example.smartcities.api.ServiceBuilder
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val image = view.findViewById<ImageView>(R.id.imageView2)
        val btn1 = view.findViewById<Button>(R.id.button4)
        val btn2 = view.findViewById<Button>(R.id.button5)

        val strs= marker.snippet.split("+").toTypedArray() // Vai buscar o link da imagem
        Log.d("Tag id_anom", strs[3].toString())
        //Get anomalia by id
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAnomById(strs[3].toInt())
        call.enqueue(object : retrofit2.Callback<List<com.example.smartcities.api.Marker>> {

            override fun onResponse(call: retrofit2.Call<List<com.example.smartcities.api.Marker>>, response: retrofit2.Response<List<com.example.smartcities.api.Marker>>) {
                if (response.isSuccessful){

                    for(Marker in response.body()!!){
                        tvTitle.text = Marker.titulo
                        tvSnippet.text = Marker.descricao



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

        //VISIBILIDADE DOS BOTOES
        if( strs[1].equals(strs[2])){
            btn1.visibility = (View.VISIBLE)
            btn2.visibility = (View.VISIBLE)
        }else{
            btn1.visibility = (View.GONE)
            btn2.visibility = (View.GONE)
        }
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
