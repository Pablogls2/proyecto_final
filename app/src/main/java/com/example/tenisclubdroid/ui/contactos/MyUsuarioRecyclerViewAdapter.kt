package com.example.tenisclubdroid.ui.contactos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Contacto
import com.example.tenisclubdroid.ui.perfil.ImagenRedonda
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyUsuarioRecyclerViewAdapter(
    private val values: List<Contacto>,private val fm:FragmentManager
) : RecyclerView.Adapter<MyUsuarioRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contactos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Picasso.get().load(item.fotoPerfil).transform(ImagenRedonda()).into(holder.ivImagen)
        holder.tvNickname.text = item.nickName

        val nickname = item.nickName
        val foto=item.fotoPerfil
        val descripcion= item.descripcion


        //cuando pinche se ira al fragment de detalles para ver contacto
        holder.relativeLayout.setOnClickListener(View.OnClickListener {

            val contacto = Contacto(nickname,foto,"",descripcion)
            //se le pasa el contacto
            val detalle = DetalleContactoFragment.newInstance(contacto)

            val transaction = fm!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, detalle)
            transaction.addToBackStack(null)
            transaction.commit()
        })

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var relativeLayout= view.findViewById<RelativeLayout>(R.id.rlContactos)
        val ivImagen: ImageView = view.findViewById(R.id.ivContactosImagen)
        val tvNickname: TextView = view.findViewById(R.id.tvContactosNickname)

        override fun toString(): String {
            return super.toString() + " '" + tvNickname.text + "'"
        }
    }
}