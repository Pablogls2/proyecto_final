package com.example.tenisclubdroid.ui.contactos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Contacto
import com.example.tenisclubdroid.ui.clases.Pista
import com.example.tenisclubdroid.ui.clases.Reserva
import com.example.tenisclubdroid.ui.reservar.ReservarFechaFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A fragment representing a list of Items.
 */
class BuscarContactoFragment : Fragment() {

    private var columnCount = 1
    lateinit var lista_contactos: ArrayList<Contacto>
    lateinit var lista_ids: ArrayList<String>

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUsuarios: DatabaseReference
    lateinit var root: View

    lateinit var pista: Pista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //recibe la pista para que la cargue al volver
        arguments?.getSerializable("pista").let {
            pista = it as Pista
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_contactos_list, container, false)
        //mismo funcionamiento que con la lista de contactos , la diferencia está en el recycler view
        lista_ids = ArrayList()
        lista_contactos = ArrayList()

        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("Contactos")
        databaseReferenceUsuarios = database.reference.child("usuarios")

        val id_usuario = FirebaseAuth.getInstance().currentUser?.uid
        val referencia_usuario = databaseReference.child(id_usuario!!)
        Log.e("lista_id_buscar"," "+id.toString())
        referencia_usuario.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val id = it.getValue()
                    lista_ids.add(id.toString())
                    Log.e("lista_id_buscar"," "+id.toString())
                }
                ver_contactos(lista_ids)



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        return root
    }

    private fun añadir_contacto(contacto: Contacto) {

        lista_contactos.add(contacto)
        Log.e("contacto", " " + lista_contactos.get(0).toString())
    }

    private fun ver_contactos(lista_id: ArrayList<String>) {
        for(id_lista in lista_id){

            var referencia = databaseReferenceUsuarios.child(id_lista.toString())
            referencia.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //public Contacto(String nickName, String fotoPerfil, String idUsuario) {
                    val nickname = snapshot.child("nickName").value.toString()
                    val fotoPerfil = snapshot.child("fotoPerfil").value.toString()
                    val idUsuario = snapshot.child("idUsuario").value.toString()
                    val contacto = Contacto(nickname, fotoPerfil, idUsuario)
                    añadir_contacto(contacto)

                    if (lista_id.size == lista_contactos.size) {


                        if (root is RecyclerView) {
                            with(root) {
                                (root as RecyclerView).layoutManager = when {
                                    columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(
                                        context
                                    )
                                    else -> androidx.recyclerview.widget.GridLayoutManager(
                                        context,
                                        columnCount
                                    )
                                }


                                android.util.Log.e(
                                    "lista_buscar",
                                    "tamaño_contactos " + lista_contactos.size
                                )
                                val fm= fragmentManager
                                //esta vez tambien le agregamos la pista para devolverla a la reserva
                                (root as RecyclerView).adapter =
                                    com.example.tenisclubdroid.ui.contactos.MyContactoRecyclerViewAdapter(
                                        lista_contactos, fm!!,pista
                                    )
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }





    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pista: Pista) =
            BuscarContactoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("pista", pista)
                }
            }
    }

}