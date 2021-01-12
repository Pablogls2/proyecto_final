package com.example.tenisclubdroid.ui.reservar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Pista
import com.example.tenisclubdroid.ui.clases.Reserva
import com.example.tenisclubdroid.ui.perfil.EditarPerfilFragment
import com.example.tenisclubdroid.ui.perfil.ImagenRedonda
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ReservarFragment : Fragment() {

    lateinit var root: View

    lateinit var lista_pistas: ArrayList<Pista>
    lateinit var spinner: Spinner
    lateinit var check_luces: CheckBox
    lateinit var check_limpieza: CheckBox
    lateinit var tvReservarExtra1: TextView
    lateinit var tvReservarExtra2: TextView
    lateinit var btnContinuar: Button
    lateinit var ivReservarFoto: ImageView

    lateinit var pista_elegida: Pista


    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState?.getSerializable("pista") != null) {
            pista_elegida = savedInstanceState?.getSerializable("pista") as Pista
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_reservar, container, false)

        spinner = root.findViewById<Spinner>(R.id.spinnerReservar)
        check_limpieza = root.findViewById<CheckBox>(R.id.cbReservar1)
        check_luces = root.findViewById<CheckBox>(R.id.cbReservar2)
        tvReservarExtra1 = root.findViewById<TextView>(R.id.tvReservarExtra1)
        tvReservarExtra2 = root.findViewById<TextView>(R.id.tvReservarExtra2)
        ivReservarFoto = root.findViewById<ImageView>(R.id.ivReservarFoto)

        btnContinuar = root.findViewById<Button>(R.id.btnReservaContinuar)
        pista_elegida= Pista(null,null,0,null)

        lista_pistas = ArrayList()

        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("pistas")

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //se rrecorren todos los valores
                snapshot.children.forEach {

                    val foto = it.child("foto").value.toString()
                    val nombre = it.child("nombre").value.toString()
                    val precio = it.child("precio").value.toString().toInt()
                    val limpieza = it.child("extras").child("limpieza").value.toString().toInt()
                    val luz = it.child("extras").child("luz").value.toString().toInt()
                    val lista_extras = ArrayList<Int>()
                    lista_extras.add(limpieza)
                    lista_extras.add(luz)

                    val pista = Pista(foto, nombre, precio, lista_extras)
                    lista_pistas.add(pista)
                    Log.e("pista", " d" + pista.toString())
                    iniciar()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnContinuar.setOnClickListener(View.OnClickListener {
            val fm = fragmentManager

            if (pista_elegida.nombre!=null) {


                val lista_extras = ArrayList<Int>()

                if (check_limpieza.isChecked) {
                    lista_extras.add(pista_elegida.extras.get(0))
                }
                if (check_luces.isChecked) {
                    lista_extras.add(pista_elegida.extras.get(1))
                }
                val idReservador = FirebaseAuth.getInstance().uid.toString()

                val reserva = Reserva(pista_elegida, lista_extras, idReservador)


                val reservar_fecha = ReservarFechaFragment.newInstance(reserva)

                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, reservar_fecha)
                transaction.addToBackStack(null)
                transaction.commit()
            }else{
                Toast.makeText(activity?.baseContext, "elige una pista!", Toast.LENGTH_SHORT).show()
            }

        })

        return root;

    }

    private fun iniciar() {

        val lista_nombres = ArrayList<String>()
        lista_nombres.add("             ")
        var i = 0
        for (nombre in lista_pistas) {
            lista_nombres.add(lista_pistas.get(i).nombre)
            i++
        }

        spinner.setAdapter(
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lista_nombres
            )
        )

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //se hace un switch dependiendo de lo seleccionado
                val seleccion = spinner.getSelectedItem() as String

                when (seleccion) {
                    lista_nombres.get(1) -> {
                        tvReservarExtra1.setText(
                            "Limpieza: " + " " + lista_pistas.get(0).extras.get(
                                0
                            ).toString() + " €"
                        )
                        tvReservarExtra2.setText(
                            "Luces: " + " " + lista_pistas.get(0).extras.get(1).toString() + " €"
                        )
                        Picasso.get().load(lista_pistas.get(0).foto).into(ivReservarFoto)
                        pista_elegida = Pista(
                            lista_pistas.get(0).foto,
                            lista_pistas.get(0).nombre,
                            lista_pistas.get(0).precio,
                            lista_pistas.get(0).extras
                        )


                    }
                    lista_nombres.get(2) -> {
                        tvReservarExtra1.setText(
                            "Limpieza: " + " " + lista_pistas.get(1).extras.get(
                                0
                            ).toString() + " €"
                        )
                        tvReservarExtra2.setText(
                            "Luces" + " " + lista_pistas.get(1).extras.get(1).toString() + " €"
                        )
                        Picasso.get().load(lista_pistas.get(1).foto).into(ivReservarFoto)
                        pista_elegida = Pista(
                            lista_pistas.get(1).foto,
                            lista_pistas.get(1).nombre,
                            lista_pistas.get(1).precio,
                            lista_pistas.get(1).extras
                        )

                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (pista_elegida != null) {
            outState.putSerializable("pista", pista_elegida)
        }

    }
}