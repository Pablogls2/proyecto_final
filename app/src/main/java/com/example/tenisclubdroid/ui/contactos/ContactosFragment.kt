package com.example.tenisclubdroid.ui.contactos

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Contacto
import com.example.tenisclubdroid.ui.clases.Usuario
import com.example.tenisclubdroid.ui.perfil.PerfilFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.concurrent.fixedRateTimer


/**
 * A fragment representing a list of Items.
 */
class ContactosFragment : Fragment() {

    private var columnCount = 1
    lateinit var lista_contactos: ArrayList<Contacto>
    lateinit var lista_ids: ArrayList<String>

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUsuarios: DatabaseReference
    lateinit var root: View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_contactos_list, container, false)
        //inicializamos las listas
        lista_ids = ArrayList()
        lista_contactos = ArrayList()

        //cogemos la base de datos y las referencias necesarias, que son los contactos y los usuarios
        //En los contactos se encuentran los id de cada contacto (que son usuarios normales)
        database = FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("Contactos")
        databaseReferenceUsuarios = database.reference.child("usuarios")

        //busca las referencias de los contactos que son los propios uid de los usuarios, cada usuario con su id guarda dentro los ids de sus contactos
        val id_usuario = FirebaseAuth.getInstance().currentUser?.uid
        val referencia_usuario = databaseReference.child(id_usuario!!)

        referencia_usuario.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //se recorren todos los valores
                snapshot.children.forEach {
                    val id = it.getValue()
                    //se añaden en una lista todos los ids de los contactos
                    lista_ids.add(id.toString())
                }
                //llamamos a otro metodo para verlos
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
        //se recorre la lista de los ids de los contactos
        for (id_lista in lista_id) {
            //ahora la referencia seran los ids de los contactos contra la bbdd de los usuarios
            var refrencia = databaseReferenceUsuarios.child(id_lista.toString())
            Log.e("lista_id", " " + id_lista.toString())
            refrencia.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //public Contacto(String nickName, String fotoPerfil, String idUsuario) {
                    val nickname = snapshot.child("nickName").value.toString()
                    val fotoPerfil = snapshot.child("fotoPerfil").value.toString()
                    val idUsuario = snapshot.child("idUsuario").value.toString()
                    val descripcion= snapshot.child("descripcion").value.toString()
                    val contacto = Contacto(nickname, fotoPerfil, idUsuario, descripcion)
                    //se guardan los contactos (usuarios) en otra lista
                    añadir_contacto(contacto)

                    //cuando toda la lista está recorrida y los contactos cogidos se monta la lista (recyclerview)
                    if (lista_id.size == lista_contactos.size) {

                        if (root is RecyclerView) {
                            with(root) {
                                (root as RecyclerView).layoutManager = when {
                                    columnCount <= 1 -> LinearLayoutManager(context)
                                    else -> GridLayoutManager(context, columnCount)
                                }


                                Log.e("lista", "tamaño_contactos " + lista_contactos.size)
                                val fm= fragmentManager
                                (root as RecyclerView).adapter =
                                    MyUsuarioRecyclerViewAdapter(lista_contactos, fm!!)
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.menu_buscarl, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_buscar -> {
                /*val buscar = BuscarContactoFragment()

                val fm = fragmentManager
                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, buscar)
                transaction.addToBackStack(null)
                transaction.commit()*/
                val toast1 = Toast.makeText(
                    context,
                    "proximamente...", Toast.LENGTH_SHORT
                )
                toast1.show()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ContactosFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}