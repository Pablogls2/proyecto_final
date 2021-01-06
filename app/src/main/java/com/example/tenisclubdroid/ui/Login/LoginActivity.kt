package com.example.tenisclubdroid.ui.Login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenisclubdroid.Comunicacion
import com.example.tenisclubdroid.MainActivity
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val GOOGLE_SIGN_IN = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        var btnLogin = this.findViewById<Button>(R.id.btnRegistroRegistrar)
        var btnRegistro = this.findViewById<Button>(R.id.btnLoginRegistro)
        var etLoginEmail = this.findViewById<EditText>(R.id.etLoginEmail)
        var etLoginPassword = this.findViewById<EditText>(R.id.etRegistroContra)
        var btnLoginGoogle = this.findViewById<Button>(R.id.btnLoginGoogle)
        val btnLoginNuevaContra = this.findViewById<Button>(R.id.btnLoginNuevaContra)

        auth = FirebaseAuth.getInstance()

        session()
        pedirMultiplesPermisos()

        btnLogin.setOnClickListener(View.OnClickListener {

            if (!etLoginEmail.text.toString().isEmpty() && !etLoginPassword.text.toString()
                    .isEmpty()
            ) {


                auth.signInWithEmailAndPassword(
                    etLoginEmail.text.toString(),
                    etLoginPassword.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {
                        //Guardado de datos de sesion
                        val prefs: SharedPreferences.Editor = getSharedPreferences(
                            getString(R.string.prefs_file),
                            Context.MODE_PRIVATE
                        ).edit()
                        prefs.putString("email", etLoginEmail.text.toString())
                        prefs.putString("password", etLoginPassword.text.toString())
                        prefs.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Datos incorrectos!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Rellene todos los campos!", Toast.LENGTH_SHORT).show()
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

        btnLoginNuevaContra.setOnClickListener(View.OnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val ContraOlvidada = ContraOlvidada()
            transaction.replace(R.id.coordinatorLayout, ContraOlvidada)
            transaction.addToBackStack(null)
            transaction.commit()


        })


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
                                //se recoge el usuario
                                val user = FirebaseAuth.getInstance().currentUser
                                //se cogen sus datos
                                val nickname = user?.displayName.toString()
                                val id = user?.uid.toString()
                                val foto = user?.photoUrl.toString()
                                val u = Usuario(nickname, foto,"Tu descripcion", 0, id)
                                //se guarda en la base de datos
                                FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                    FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")
                                        .getReference("usuarios").child(
                                            it1.uid
                                        ).setValue(u).addOnCompleteListener {

                                            if (it.isSuccessful) {

                                            } else {

                                            }

                                        }
                                }

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

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

    private fun session() {
        val prefs: SharedPreferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val password = prefs.getString("password", null)

        if (email != null && password != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun pedirMultiplesPermisos() {
        // Indicamos el permisos y el manejador de eventos de los mismos
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // ccomprbamos si tenemos los permisos de todos ellos
                    if (report.areAllPermissionsGranted()) {
                        //Toast.makeText(getContext(), "¡Todos los permisos concedidos!", Toast.LENGTH_SHORT).show();
                    }

                    // comprobamos si hay un permiso que no tenemos concedido ya sea temporal o permanentemente
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // abrimos un diálogo a los permisos
                        //openSettingsDialog();
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError?) {

                }
            })
            .onSameThread()
            .check()
    }
}
