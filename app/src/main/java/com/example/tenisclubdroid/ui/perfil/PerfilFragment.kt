package com.example.tenisclubdroid.ui.perfil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.Login.LoginActivity
import com.example.tenisclubdroid.ui.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fotoUrl : String
    private lateinit var usuario : Usuario




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_perfil, container, false)


        //para que el teclado no se vuelva loco
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val tvPerfilNickName = root.findViewById<TextView>(R.id.etPerfilNickName)
        val tvPerfilDescripcion = root.findViewById<TextView>(R.id.etPerfilDescripcion)
        val ivPerfilFoto = root.findViewById<ImageView>(R.id.ivPerfilFoto)
        val btnPerfilQr = root.findViewById<Button>(R.id.btnPerfilQr)


        //se coge el usuario por su uid
        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("usuarios")
        val id_usuario = FirebaseAuth.getInstance().currentUser?.uid

        val referencia_usuario = databaseReference.child(id_usuario!!)

        referencia_usuario.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                tvPerfilNickName.setText(snapshot.child("nickName").value.toString())
                tvPerfilDescripcion.setText(snapshot.child("descripcion").value.toString())
                fotoUrl = snapshot.child("fotoPerfil").value.toString()
                Picasso.get().load(fotoUrl).transform(ImagenRedonda()).into(ivPerfilFoto)
                usuario = Usuario(
                    tvPerfilNickName.text.toString(),
                    fotoUrl,
                    tvPerfilDescripcion.text.toString(),
                    0,
                    id_usuario
                )


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //public Usuario(String nickName, String fotoPerfil, String descripcion, int rol) {

        return root;

    }

    //para tener un menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.menu_perfil, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {

                val fm = fragmentManager
                val edit_perfil = EditarPerfilFragment.newInstance(usuario)

                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, edit_perfil)
                transaction.addToBackStack(null)
                transaction.commit()


            }
            R.id.action_logout -> {
                val prefs = context?.getSharedPreferences(
                    resources.getString(R.string.prefs_file),
                    Context.MODE_PRIVATE
                )
                    ?.edit()
                prefs?.clear()
                prefs?.apply()
                val intent = Intent(activity, LoginActivity::class.java)
                activity?.startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }





}