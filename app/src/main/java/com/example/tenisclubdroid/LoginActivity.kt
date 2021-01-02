package com.example.tenisclubdroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenisclubdroid.ui.registro.RegistroFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val GOOGLE_SIGN_IN = 100
    var comunicacion = Comunicacion()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        var btnLogin = this.findViewById<Button>(R.id.btnRegistroRegistrar)
        var btnRegistro = this.findViewById<Button>(R.id.btnLoginRegistro)
        var etLoginEmail = this.findViewById<EditText>(R.id.etLoginEmail)
        var etLoginPassword = this.findViewById<EditText>(R.id.etRegistroContra)
        var btnLoginGoogle = this.findViewById<Button>(R.id.btnLoginGoogle)

        auth = FirebaseAuth.getInstance()


        btnLogin.setOnClickListener(View.OnClickListener {
            //se llama al metodo de login de la clase comunicacion una vez se hayan comprobado los campos
            /*var resultado =
                comunicacion.login(etLoginEmail.text.toString(), etLoginPassword.text.toString(), auth)

            if (resultado == 0) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                if (resultado == 1) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Datos incorrectos!", Toast.LENGTH_SHORT).show()
                }

            }*/
            auth.signInWithEmailAndPassword(
                etLoginEmail.text.toString(),
                etLoginPassword.text.toString()
            ).addOnCompleteListener {

                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Datos incorrectos!", Toast.LENGTH_SHORT).show()
                }
            }


        })


        btnLoginGoogle.setOnClickListener(View.OnClickListener {
            //Configuracion
            val googleConf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                    getString(
                        R.string.default_web_client_id
                    )
                ).requestEmail().build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        })

        btnRegistro.setOnClickListener(View.OnClickListener {

            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val Registro = RegistroFragment()
            transaction.replace(R.id.coordinatorLayout, Registro)
            transaction.addToBackStack(null)
            transaction.commit()


        })

        //Guardado de datos de sesion
        val prefs: SharedPreferences.Editor = getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        ).edit()
        prefs.putString("email", etLoginEmail.text.toString())
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
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                //Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Sale mal", Toast.LENGTH_SHORT).show()
            }

        }
    }


}