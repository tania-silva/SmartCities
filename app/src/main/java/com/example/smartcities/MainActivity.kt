package com.example.smartcities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postUtl("stania@ipvc.pt", "1234")

        call.enqueue(object : Callback<List<OutputPost>>{
            override fun onResponse(call: Call<List<OutputPost>>, response: Response<List<OutputPost>>) {
                if (response.isSuccessful){
                    for(OutputPost in response.body()!!){

                        Log.d("TAG_", OutputPost.email.toString() + OutputPost.pass.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<OutputPost>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })




    }



    fun button(view: View) {
       /* val intent = Intent(this, MainActivity2::class.java).apply {

        }
        startActivity(intent)*/



    }

}