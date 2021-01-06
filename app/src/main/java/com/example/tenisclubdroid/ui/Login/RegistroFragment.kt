package com.example.tenisclubdroid.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 * Use the [RegistroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistroFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var nombresCogidos: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    var nickname_cogido = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_registro, container, false)

        //para que el teclado no se vuelva loco
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btnRegistrar = root.findViewById<Button>(R.id.btnRegistroRegistrar)
        val etRegistroEmail = root.findViewById<EditText>(R.id.etRegistroEmail)
        val etRegistroContra = root.findViewById<EditText>(R.id.etRegistroContra)
        val etRegistroConfirmContra = root.findViewById<EditText>(R.id.etRegistroConfirm)
        val etRegistroUserName = root.findViewById<EditText>(R.id.etRegistroUserName)
        val ivRegistroAtras = root.findViewById<ImageView>(R.id.ivRegistroAtras)

        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")


        databaseReference = database.reference.child("usuarios")
        nombresCogidos = database.reference.child("NombresCogiodos")



        btnRegistrar.setOnClickListener(View.OnClickListener {

            var email = etRegistroEmail.text.toString()
            var contra = etRegistroContra.text.toString()
            var confirm_contra = etRegistroConfirmContra.text.toString()
            var usuario = etRegistroUserName.text.toString()


            //se comprueba que todos los campos esten rellenos
            if (!email.isEmpty() && !contra.isEmpty() && !confirm_contra.isEmpty() && !usuario.isEmpty()) {
                //se comprueba que el email tiene un formato correcto
                if (comprobarEmail(email.trim())) {
                    etRegistroEmail.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                        ContextCompat.getColorStateList(
                            it1, R.color.background_tint_azul
                        )
                    })
                    if (contra.trim().length >= 6) {
                        etRegistroContra.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                            ContextCompat.getColorStateList(
                                it1, R.color.background_tint_azul
                            )
                        })

                        //se comprueba que la contraseña es correcta
                        if (contra.trim().equals(confirm_contra)) {

                            etRegistroConfirmContra.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                                ContextCompat.getColorStateList(
                                    it1, R.color.background_tint_azul
                                )
                            })


                            //se han pasado los filtros y se crea la cuenta con el email y la contraseña
                            registrar(usuario, email, contra)

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
                        etRegistroContra.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                            ContextCompat.getColorStateList(
                                it1, R.color.rojo_google
                            )
                        })
                        val toast1 = Toast.makeText(
                            context,
                            "La contraseña debe de ser de al menos 6  caracteres",
                            Toast.LENGTH_LONG
                        )
                        toast1.show()
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

        ivRegistroAtras.setOnClickListener(View.OnClickListener
        {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
        })

        return root
    }

    private fun registrar(usuario: String, email: String, contra: String) {
        nombresCogidos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("repetido", "" + dataSnapshot.child(usuario).key)
                if (!dataSnapshot.hasChild(usuario)) {
                    // dato =dataSnapshot.child(sUsername).key.toString()

                    Log.e("repetido2", dataSnapshot.child(usuario).key.toString())

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email,
                        contra
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            val id =
                                FirebaseAuth.getInstance().currentUser?.uid.toString()
                            var u = Usuario(usuario, email, contra, id, 0)


                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("usuarios").child(
                                        it1.uid
                                    ).setValue(u).addOnCompleteListener {

                                        if (it.isSuccessful) {
                                            nombresCogidos.child(usuario).setValue(true)
                                            val toast1 = Toast.makeText(
                                                context,
                                                "guardado", Toast.LENGTH_SHORT
                                            )
                                            toast1.show()
                                        } else {
                                            val toast1 = Toast.makeText(
                                                context,
                                                it.result.toString(), Toast.LENGTH_SHORT
                                            )
                                            toast1.show()
                                        }

                                    }
                            }

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
                }else{
                    Toast.makeText(
                        context,
                        "Usuario repetido ,escoja otro",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("repetido2", dataSnapshot.child(usuario).key.toString())
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context,
                    "ERROR",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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