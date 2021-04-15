package com.example.smartcities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcities.DataClass.Place
import com.example.smartcities.api.EndPoints
import com.example.smartcities.api.OutputPost
import com.example.smartcities.api.ServiceBuilder
import com.example.smartcities.api.User
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.login_p), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.login_shared)]==true){
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }

    }



    fun button(view: View) {
        val intent = Intent(this, MainActivity2::class.java).apply {

        }
        startActivity(intent)

    }


    fun login(view: View) {

        var email = findViewById<EditText>(R.id.email)
        var pass = findViewById<EditText>(R.id.pass)
        val intent = Intent(this, MapsActivity::class.java)


 //Validações dos campos email e pass
        if(email.text.isNullOrEmpty() || pass.text.isNullOrEmpty()){

                if(email.text.isNullOrEmpty()){
                    email.error = getString(R.string.aviso_email)
                }
                if(pass.text.isNullOrEmpty()){
                    pass.error = getString(R.string.aviso_pass)
                }

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error= getString(R.string.email_erro)
        }else{
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.postUtl(email.text.toString(), pass.text.toString())

                call.enqueue(object : Callback<List<OutputPost>>{
                    override fun onResponse(call: Call<List<OutputPost>>, response: Response<List<OutputPost>>) {
                        if (response.isSuccessful){

                            for(OutputPost in response.body()!!){
                                // Log.d("TAG_", OutputPost.email.toString() + OutputPost.pass.toString())

                                //Shared Preferences Login
                                val sharedPref: SharedPreferences = getSharedPreferences(
                                        getString(R.string.login_p), Context.MODE_PRIVATE
                                )
                                with(sharedPref.edit()){
                                    putBoolean(getString(R.string.login_shared), true)
                                    putString(getString(R.string.nome), OutputPost.nome)
                                    putInt(getString(R.string.id_utl), OutputPost.id_utl)
                                    commit()
                                }

                            }

                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<List<OutputPost>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, getString(R.string.pass_email_erro), Toast.LENGTH_SHORT).show()
                    }
                })

        }

    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@MainActivity, getString(R.string.back), Toast.LENGTH_SHORT).show()
    }


}