package com.example.smartcities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartcities.Adapter.DESCRICAO
import com.example.smartcities.Adapter.ID
import com.example.smartcities.Adapter.TITULO
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.Marker
import com.example.smartcities.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val TITULOA="TITULO"
const val DESCRICAOA="DESCRICAO"
const val IDA="ID"
const val IMAGEM="IMAGEM"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private lateinit var mMap: GoogleMap

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback
    private lateinit var lastLocation: Location

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

        call.enqueue(object : Callback<List<Marker>> {
            override fun onResponse(call: Call<List<Marker>>, response: Response<List<Marker>>) {
                if (response.isSuccessful) {

                    for (Marker in response.body()!!) {
                        Log.d("TAG_", Marker.utilizador_id.toString() + "-" + Marker.id_anom.toString())
                        position = LatLng(Marker.lat, Marker.lng)

                        if (id_utl.toString().toInt() == Marker.utilizador_id) {
                            mMap.addMarker(MarkerOptions()
                                    .position(position).title(Marker.utilizador_id.toString() + "+" + Marker.titulo)
                                    .snippet(Marker.descricao + "+" + Marker.imagem + "+" + Marker.utilizador_id + "+" + id_utl.toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        } else {
                            mMap.addMarker(MarkerOptions()
                                    .position(position).title(Marker.utilizador_id.toString() + "+" + Marker.titulo)
                                    .snippet(Marker.descricao + "+" + Marker.imagem + "+" + Marker.utilizador_id + "+" + id_utl.toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Marker>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, getString(R.string.pass_email_erro), Toast.LENGTH_SHORT).show()
            }
        })


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0!!.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                Log.d("***Latitude", loc.latitude.toString())
            }
        }
        //Localização do Utilizador
        createLocationRequest()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Adicionar um marcador
        val braga = LatLng(41.542114, -8.423440)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(braga, 10.0f)) // centra o mapa nas cordenadas do ponto e com o zoom já aplicado

        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))

        googleMap.setOnInfoWindowClickListener(this)

    }




    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    public override fun onResume(){
        super.onResume()
        startLocationUpdates()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.m_logout -> {

                //Alterar o Shared Preferences
                val sharedPref: SharedPreferences = getSharedPreferences(
                        getString(R.string.login_p), Context.MODE_PRIVATE
                )
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.login_shared), false)
                    putString(getString(R.string.nome), "")
                    putInt(getString(R.string.id_utl), 0)
                    commit()
                }

                //Navegar para o menu inicial
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.m_notas -> {
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


    override fun onInfoWindowClick(p0: com.google.android.gms.maps.model.Marker?) {
        val title = p0?.title?.split("+")?.toTypedArray()  //ID e tituo
        val snippet = p0?.snippet?.split("+")?.toTypedArray() //Divide o snippet
        Toast.makeText(this, p0?.title , Toast.LENGTH_SHORT).show()

       val intent = Intent(this ,Anomalias::class.java).apply {
            putExtra(IDA, title?.get(0))
            putExtra(TITULOA, title?.get(1))
            putExtra(DESCRICAOA, snippet?.get(0))
            putExtra(IMAGEM, snippet?.get(1))
        }
        startActivity(intent)
    }

    fun add_anom(view: View) {

        Toast.makeText(this, "Adicionar nota", Toast.LENGTH_SHORT).show()
    }
}