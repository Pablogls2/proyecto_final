package com.example.tenisclubdroid.ui.reservar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Pista
import com.example.tenisclubdroid.ui.clases.Reserva
import com.example.tenisclubdroid.ui.contactos.BuscarContactoFragment
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
    lateinit var tvReservarContricante : TextView


     var  nombre_adversario=""
     var id_adversario=""

     var pista_elegida= Pista()


    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //si volvemos del Fragment de buscar advsario cargaremos la pista que eligió antes de irse y cogeremos el nombre y id del adversario
        if (arguments?.getSerializable("pista") != null) {


            arguments?.getSerializable("pista").let {
                pista_elegida = it as Pista
            }
            nombre_adversario = arguments?.getString("nombreAdver").toString()
            id_adversario = arguments?.getString("idAdver").toString()



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
        tvReservarContricante= root.findViewById<TextView>(R.id.tvReservarContrincante)
        val ibReservarAdversario = root.findViewById<ImageButton>(R.id.ibReservarContrincante)

        //aqui se carga la pista al volver del adversario (si lo ha hecho)
        if (pista_elegida.nombre!=null) {

            Picasso.get().load(pista_elegida.foto).into(ivReservarFoto)
            tvReservarExtra1.setText(
                "Limpieza: " + " " + pista_elegida.extras.get(0).toString() + " €"
            )
            tvReservarExtra2.setText("Luz: " + " " + pista_elegida.extras.get(1).toString() + " €")

            tvReservarContricante.setText(nombre_adversario)

        }
        //como en otros Fragment se cargan los objetos necesarios para manipular la base de datos
        lista_pistas = ArrayList()

        //se cargan las pistas de la base de datos
        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("pistas")

        //primero cogemos todas las pistas que tenga el club y las guardamos en un ArrayList
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
                    //este metodo metera las pistas en el spinner para que el usuario pueda verlas
                    iniciar()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //una vez tenga una pista elegida pulsara el boton de continuar
        btnContinuar.setOnClickListener(View.OnClickListener {
            val fm = fragmentManager
            //se comprueba que efectivamente ha elegido una pista
            if (pista_elegida.nombre != null) {


                val lista_extras = ArrayList<Int>()
                //se comprueba si ha elegido los extras para añadirlos a la reserva
                if (check_limpieza.isChecked) {
                    lista_extras.add(pista_elegida.extras.get(0))
                }
                if (check_luces.isChecked) {
                    lista_extras.add(pista_elegida.extras.get(1))
                }
                //se coge el id del propio usuario para guardarlo en la reserva
                val idReservador = FirebaseAuth.getInstance().uid.toString()

                //se crea la reserva con la pista, la lista de los extras y el usuario
                val reserva = Reserva(pista_elegida, lista_extras, idReservador)

                //si ha elegido un contricante se añadirá a la reserva
                if(!id_adversario.isEmpty()){
                    reserva.idAdversario=id_adversario
                }
                //pasamos a elegir la fecha de la reserva y le pasamos la reserva con los datos hasta ahora
                Log.e("reserva",reserva.toString())
                val reservar_fecha = ReservarFechaFragment.newInstance(reserva)

                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, reservar_fecha)
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                Toast.makeText(activity?.baseContext, "elige una pista!", Toast.LENGTH_SHORT).show()
            }

        })

        //Listener del boton de añadir un contrincante
        ibReservarAdversario.setOnClickListener(View.OnClickListener {
            //previamente se debe elegir una pista
            if (pista_elegida.nombre != null) {

                //nos vamos al fragment de buscar contactos dandole la pista elegida para despues recargarla para que sea invisible para el usuario
                val buscarAdversario = BuscarContactoFragment.newInstance(pista_elegida)
                val fm = fragmentManager
                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, buscarAdversario)
                transaction.addToBackStack(null)
                transaction.commit()

            } else {
                Toast.makeText(activity?.baseContext, "Elige una pista", Toast.LENGTH_SHORT).show()

            }


        })


        return root;

    }

    //se rellena el spinner con las pistas
    private fun iniciar() {
        //se cogen los nombres de las pistas de nuestra lista previamente rellanada
        val lista_nombres = ArrayList<String>()
        lista_nombres.add("             ")
        var i = 0
        for (nombre in lista_pistas) {
            lista_nombres.add(lista_pistas.get(i).nombre)
            i++
        }
        //se agrega al spinner la lista de los nombres de las pistas
        spinner.setAdapter(
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lista_nombres
            )
        )
        //con este Listener vamos a ir cambiando de pista segun el usuario la elija mediante el nombre
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //depende del indice seleccionado será una pista u otra
                var id_seleccion = position

                if(id_seleccion!=0){
                    //se le resta 1 porque la lista de pistas empieza en 0 y la de nombres del spinner en 1
                    id_seleccion= id_seleccion -1
                    Log.e("spinner",""+id_seleccion)
                    tvReservarExtra1.setText("Limpieza: " + " " + lista_pistas.get(id_seleccion).extras.get(0).toString() + " €"
                    )
                    tvReservarExtra2.setText(
                        "Luces: " + " " + lista_pistas.get(id_seleccion).extras.get(1).toString() + " €"
                    )
                    Picasso.get().load(lista_pistas.get(id_seleccion).foto).into(ivReservarFoto)
                    pista_elegida = Pista(
                        lista_pistas.get(id_seleccion).foto,
                        lista_pistas.get(id_seleccion).nombre,
                        lista_pistas.get(id_seleccion).precio,
                        lista_pistas.get(id_seleccion).extras
                    )
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pista: Pista, nombreAdver: String, idAdver: String) =
            ReservarFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("pista", pista)
                    putString("nombreAdver", nombreAdver)
                    putString("idAdver", idAdver)
                }
            }
    }
}