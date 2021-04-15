package com.example.smartcities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.OutputPost
import com.example.smartcities.api.ServiceBuilder

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.smartcities.api.Marker
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var id_utl: Any? = null;

        //Obter id do Utilizador
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            id_utl = sharedPref.all[getString(R.string.id_utl)]
        }

        Toast.makeText(this@MapsActivity, id_utl.toString(), Toast.LENGTH_SHORT).show()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAnomalias()
        var position: LatLng

        call.enqueue(object : Callback<List<Marker>>{
            override fun onResponse(call: Call<List<Marker>>, response: Response<List<Marker>>) {
                if (response.isSuccessful){

                    for(Marker in response.body()!!){
                        Log.d("TAG_",  Marker.utilizador_id.toString() +"-" + Marker.id_anom.toString())
                        position = LatLng(Marker.lat, Marker.lng)

                        if(id_utl.toString().toInt() == Marker.utilizador_id){
                            mMap.addMarker(MarkerOptions()
                                .position(position).title(Marker.utilizador_id.toString() + " - " + Marker.titulo)
                                .snippet(Marker.descricao + "," + Marker.imagem)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                    }else{
                            mMap.addMarker(MarkerOptions()
                                .position(position).title(Marker.utilizador_id.toString() + " - " + Marker.titulo)
                                .snippet(Marker.descricao + "," + Marker.imagem)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
                    }

                    }
                }
            }

            override fun onFailure(call: Call<List<Marker>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, getString(R.string.pass_email_erro), Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Adicionar um marcador
        val braga = LatLng(41.542114,-8.423440)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(braga, 7.0f)) // centra o mapa nas cordenadas do ponto e com o zoom já aplicado
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
        Toast.makeText(this@MapsActivity, getString(R.string.back), Toast.LENGTH_SHORT).show()
    }


}