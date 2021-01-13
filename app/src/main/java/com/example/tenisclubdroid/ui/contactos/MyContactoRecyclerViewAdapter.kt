package com.example.tenisclubdroid.ui.contactos

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Contacto

import com.example.tenisclubdroid.ui.perfil.ImagenRedonda
import com.example.tenisclubdroid.ui.reservar.ReservarFragment
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyContactoRecyclerViewAdapter(
    private val values: List<Contacto>,private val fm : FragmentManager
) : RecyclerView.Adapter<MyContactoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_buscar_contacto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Picasso.get().load(item.fotoPerfil).transform(ImagenRedonda()).into(holder.ivImagen)
        holder.tvNickname.text = item.nickName

        holder.relativeLayout.setOnClickListener(View.OnClickListener {
            val reserva = ReservarFragment()

            val transaction = fm!!.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, reserva)
            transaction.addToBackStack(null)
            transaction.commit()


        })
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val relativeLayout : RelativeLayout = view.findViewById(R.id.ivBuscarContactosImagen)
        val ivImagen: ImageView = view.findViewById(R.id.ivBuscarContactosImagen)
        val tvNickname: TextView = view.findViewById(R.id.tvBuscarContactosNickname)

        override fun toString(): String {
            return super.toString() + " '" + tvNickname.text + "'"
        }
    }
}