package com.example.tenisclubdroid.ui.registro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.Comunicacion
import com.example.tenisclubdroid.LoginActivity
import com.example.tenisclubdroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 * Use the [RegistroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroFragment : Fragment() {

    //lateinit var auth: FirebaseAuth
   // var databaseReference: DatabaseReference? = null
   // var database: FirebaseDatabase? = null
    var comunicacion = Comunicacion ()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_registro, container, false)



        val btnRegistrar = root.findViewById<Button>(R.id.btnRegistroRegistrar)
        val etRegistroEmail = root.findViewById<EditText>(R.id.etRegistroEmail)
        val etRegistroContra = root.findViewById<EditText>(R.id.etRegistroContra)
        val etRegistroConfirmContra = root.findViewById<EditText>(R.id.etRegistroConfirm)
        val etRegistroUserName = root.findViewById<EditText>(R.id.etRegistroUserName)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        databaseReference = database.reference.child("usuarios")

        btnRegistrar.setOnClickListener(View.OnClickListener {

            var email = etRegistroEmail.text.toString()
            var contra = etRegistroContra.text.toString()
            var confirm_contra = etRegistroConfirmContra.text.toString()
            var usuario = etRegistroUserName.text.toString()

            //se comprueba que todos los campos esten rellenos
            if (!email.isEmpty() && !contra.isEmpty() && !confirm_contra.isEmpty() && !usuario.isEmpty()) {
                //se comprueba que el email tiene un formato correcto
                if (comprobarEmail(email)) {
                    etRegistroEmail.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                        ContextCompat.getColorStateList(
                            it1, R.color.background_tint_azul
                        )
                    })

                    //se comprueba que la contraseña es correcta
                    if (contra.equals(confirm_contra)) {

                        etRegistroConfirmContra.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                            ContextCompat.getColorStateList(
                                it1, R.color.background_tint_azul
                            )
                        })

                        //var registro =comunicacion.registro(email,usuario,contra, auth,database,databaseReference)

                        /*when (registro){
                            0 ->  Toast.makeText(context, "Usuario Creado", Toast.LENGTH_SHORT).show()
                            1->   Toast.makeText(context, "Usuario NO Creado", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()

                        }*/

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            etRegistroEmail.text.toString(),
                            etRegistroContra.text.toString()
                        ).addOnCompleteListener {

                            if (it.isSuccessful) {
                                val user: FirebaseUser = auth.currentUser!!
                                /*val currentUser = auth.currentUser
                                val currentUserDb = databaseReference?.child((currentUser?.uid!!))

                                currentUserDb?.child("nickname")?.setValue(usuario)
                                currentUserDb?.child("rol")?.setValue(0)

                                val toast1 = Toast.makeText(
                                    context,
                                    "Usuario Creado", Toast.LENGTH_SHORT
                                )
                                toast1.show()*/

                                val currentUserDb = databaseReference.child(user.uid)

                                currentUserDb.child("nickname").setValue(usuario)
                                currentUserDb.child("rol").setValue(0)


                                val intent = Intent(activity, LoginActivity::class.java)
                                activity?.startActivity(intent)


                            } else {
                                val toast1 = Toast.makeText(
                                    context,
                                    "MAL", Toast.LENGTH_SHORT
                                )
                                toast1.show()
                            }


                        }
                    } else {
                        val toast1 = Toast.makeText(
                            context,
                            "revise la contraseña", Toast.LENGTH_SHORT
                        )
                        toast1.show()
                        etRegistroConfirmContra.setText("")
                        etRegistroConfirmContra.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                            ContextCompat.getColorStateList(
                                it1, R.color.rojo_google
                            )
                        })
                    }

                } else {
                    etRegistroEmail.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                        ContextCompat.getColorStateList(
                            it1, R.color.rojo_google
                        )
                    })
                    val toast1 = Toast.makeText(
                        context,
                        "revise el email", Toast.LENGTH_SHORT
                    )
                    toast1.show()

                }
            } else {
                val toast1 = Toast.makeText(
                    context,
                    "rellene todos los campos", Toast.LENGTH_SHORT
                )
                toast1.show()
            }


        })

        return root
    }

    private fun comprobarEmail(email: String): Boolean {

        // Patrón para validar el email
        val pattern = Pattern
            .compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            )
        val mather = pattern.matcher(email)
        return if (mather.find() == true) {
            true
        } else {
            false
        }
    }


}