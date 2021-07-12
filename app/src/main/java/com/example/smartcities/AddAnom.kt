package com.example.smartcities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.Call
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.Marker
import com.example.smartcities.api.ServiceBuilder
import com.google.android.gms.common.api.Response
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class AddAnom : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var encodedImg: String

    var lat = ""
    var long = ""
    var id_utl: Any? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_anom)
        //Spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        val options = arrayOf("Obras", "Acidente", "Caminhos de Água", "Parques", "Indefinido")

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        lat = intent.getStringExtra(LAT)
        long = intent.getStringExtra(LNG)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.login_p), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            id_utl = sharedPref.all[getString(R.string.id_utl)]
        }

        title = "KotlinApp"
        imageView = findViewById(R.id.IVPreviewImage)
        button = findViewById(R.id.BSelectImage)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var bitmap : Bitmap
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            bitmap = (imageView.drawable as BitmapDrawable).bitmap  // passar para bitmap

            /* ----- PASSAR PARA BASE64 ------ */
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            encodedImg = Base64.encodeToString(b, Base64.DEFAULT) //Imagem em base 64

            // DESCODIFICAR BASE64
            val decodedString: ByteArray = Base64.decode(encodedImg, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageView.setImageBitmap(decodedByte)
        }
    }

    fun adicionarAnom(view: View) {
        val title = findViewById<EditText>(R.id.tituloAnom)
        val desc = findViewById<EditText>(R.id.descricaoAnom)
        val  tipo = findViewById<Spinner>(R.id.spinner)

        var intent = Intent(this, MapsActivity::class.java)

        if (TextUtils.isEmpty(title.text) || TextUtils.isEmpty(desc.text)) {

            if(TextUtils.isEmpty(title.text)) {
                title.setError(getString(R.string.aviso_titulo))
            }
            if(TextUtils.isEmpty(desc.text)) {
                desc.setError(getString(R.string.aviso_desc))
            }

        }else{
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.addAnom(id_utl.toString().toInt(), title.text.toString(), desc.text.toString(), tipo.selectedItem.toString(), encodedImg, lat.toFloat(), long.toFloat())

            call.enqueue(object : retrofit2.Callback<Marker> {

                override fun onResponse(call: retrofit2.Call<Marker>, response: retrofit2.Response<Marker>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddAnom, getString(R.string.edit_sucess) , Toast.LENGTH_SHORT).show()
                        startActivity(intent)

                    }else{
                        Toast.makeText(this@AddAnom, "QUASE" , Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Marker>, t: Throwable) {
                    Log.d("TAG", "err: " + t.message)
                }

            })
        }
    }

    fun Cancelar(view: View) {
        finish()
    }
}