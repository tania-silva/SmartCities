package com.example.smartcities

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

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

        tvTitle.text = marker.title
        tvSnippet.text = strs[0]

        Picasso.get().load(strs[1]).into(image);       //Passar o link da imagem

        image.getLayoutParams().height = 450;
        image.getLayoutParams().width = 450;
        image.requestLayout();


        //VISIBILIDADE DOS BOTOES
        if( strs[2].equals(strs[3])){
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
