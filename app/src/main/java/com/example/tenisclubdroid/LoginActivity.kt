package com.example.tenisclubdroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val GOOGLE_SIGN_IN = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        var btnLogin = this.findViewById<Button>(R.id.btnLoginEntrar)
        var etLoginUsername = this.findViewById<EditText>(R.id.etLoginUsu)
        var etLoginPassword = this.findViewById<EditText>(R.id.etpLoginContra)
        var btnLoginGoogle = this.findViewById<Button>(R.id.btnLoginGoogle)

        btnLogin.setOnClickListener(View.OnClickListener {
            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(etLoginUsername.text.toString(),
                    etLoginPassword.text.toString()).addOnCompleteListener {

                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "TONTO!", Toast.LENGTH_SHORT).show()
                }
            }
        })


        btnLoginGoogle.setOnClickListener(View.OnClickListener {
            //Configuracion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        })

        //Guardado de datos de sesion
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", etLoginUsername.text.toString())
        prefs.putString("password", etLoginPassword.text.toString())
        prefs.apply()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {


                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "TONTO!", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Sale mal", Toast.LENGTH_SHORT).show()
            }

        }
    }


}