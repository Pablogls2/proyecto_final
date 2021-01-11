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
import com.example.tenisclubdroid.ui.perfil.ImagenRedonda
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ReservarFragment : Fragment() {

    lateinit var root :View

    lateinit var lista_pistas: ArrayList<Pista>
    lateinit var spinner : Spinner
    lateinit var check_luces : CheckBox
    lateinit var check_limpieza : CheckBox
    lateinit var tvReservarExtra1 : TextView
    lateinit var tvReservarExtra2 : TextView
    lateinit var btnContinuar : Button
    lateinit var ivReservarFoto : ImageView


    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUsuarios: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_reservar, container, false)

        spinner= root.findViewById<Spinner>(R.id.spinnerReservar)
        check_limpieza= root.findViewById<CheckBox>(R.id.cbReservar1)
        check_luces= root.findViewById<CheckBox>(R.id.cbReservar2)
        tvReservarExtra1 = root.findViewById<TextView>(R.id.tvReservarExtra1)
        tvReservarExtra2= root.findViewById<TextView>(R.id.tvReservarExtra2)
        ivReservarFoto= root.findViewById<ImageView>(R.id.ivReservarFoto)

        btnContinuar= root.findViewById(R.id.btnReservaContinuar)


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


        return root;

    }

    private fun iniciar(){

        val lista_nombres = ArrayList<String> ()
        lista_nombres.add("             ")
        var i =0
        for (nombre in lista_pistas){
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

        spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //se hace un switch dependiendo de lo seleccionado
                val seleccion = spinner.getSelectedItem() as String

                when (seleccion){
                    lista_nombres.get(1)-> {
                        tvReservarExtra1.setText("Limpieza: "+ " "+lista_pistas.get(0).extras.get(0).toString() + " €")
                        tvReservarExtra2.setText("Luces: " + " "+lista_pistas.get(0).extras.get(1).toString() + " €")
                        Picasso.get().load(lista_pistas.get(0).foto).into(ivReservarFoto)


                    }
                    lista_nombres.get(2)->{
                        tvReservarExtra1.setText("Limpieza: " + " "+lista_pistas.get(1).extras.get(0).toString() + " €")
                        tvReservarExtra2.setText("Luces" + " "+lista_pistas.get(1).extras.get(1).toString() + " €")
                        Picasso.get().load(lista_pistas.get(1).foto).into(ivReservarFoto)

                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }






    }

}