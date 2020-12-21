package com.example.tenisclubdroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        var btnLogin = this.findViewById<Button>(R.id.btnLoginEntrar)
        var etLoginUsername= this.findViewById<EditText>(R.id.etLoginUsu)
        var etLoginPassword = this.findViewById<EditText>(R.id.etpLoginContra)

        btnLogin.setOnClickListener( View.OnClickListener {
            auth=FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(etLoginUsername.text.toString(),
                                            etLoginPassword.text.toString()).addOnCompleteListener{

                                                if(it.isSuccessful){
                                                    val intent= Intent(this, MainActivity::class.java)
                                                    startActivity(intent)
                                                }else{
                                                    Toast.makeText(this, "TONTO!", Toast.LENGTH_SHORT).show()
                                                }
            }




        })





    }



}