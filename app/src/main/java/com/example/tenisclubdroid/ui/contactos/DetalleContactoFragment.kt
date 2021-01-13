package com.example.tenisclubdroid.ui.contactos

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Contacto
import com.example.tenisclubdroid.ui.clases.Usuario
import com.example.tenisclubdroid.ui.perfil.ImagenRedonda
import com.example.tenisclubdroid.ui.perfil.PerfilFragment
import com.squareup.picasso.Picasso

class DetalleContactoFragment : Fragment() {

    lateinit var contacto: Contacto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //recoge el contacto
        arguments?.getSerializable("contacto").let {
            contacto = it as Contacto
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // cargamos los componentes
        val root = inflater.inflate(R.layout.fragment_detalle_contacto, container, false)

        val etEditarNickName = root.findViewById<TextView>(R.id.etDetalleContactoNickName)
        val etEditarDescripcion = root.findViewById<TextView>(R.id.etDetalleContactoDescripcion)
        val ivPerfilFoto = root.findViewById<ImageView>(R.id.ivDetalleContactoFoto)

        //metemos los datos del contacto en los campos
        etEditarNickName.setText(contacto.nickName)
        etEditarDescripcion.setText(contacto.descripcion)
        Picasso.get().load(contacto.fotoPerfil).transform(ImagenRedonda()).into(ivPerfilFoto)

        return root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.menu_editar_perfil, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                val contactos = ContactosFragment()

                val fm = fragmentManager
                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, contactos)
                transaction.addToBackStack(null)
                transaction.commit()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @JvmStatic
        fun newInstance(contacto: Contacto) =
            DetalleContactoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("contacto", contacto)
                }
            }
    }
}