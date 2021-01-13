package com.example.tenisclubdroid.ui.reservar

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Reserva
import com.example.tenisclubdroid.ui.clases.TimePickerFragment
import com.google.firebase.database.*
import java.time.LocalTime
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservarFechaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservarFechaFragment : Fragment() {

    lateinit var reserva: Reserva
    lateinit var root: View
    lateinit var calendarioReserva: CalendarView
    lateinit var fecha: String
    lateinit var hora_inicio: String
    lateinit var hora_final: String
    lateinit var etTimeInicio: TextView
    lateinit var etTimeFinal: TextView
     var fecha_cogida = true
    lateinit var fechas_reservadas : ArrayList<String>
    lateinit var pistas_cogidas : ArrayList<String>
    lateinit var lista_reservas : ArrayList <String>
    var fecha_elegida = ""


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getSerializable("reserva").let {
            reserva = it as Reserva
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_reservar_fecha, container, false)
        calendarioReserva = root.findViewById<CalendarView>(R.id.calendarReservarFecha)
        etTimeInicio = root.findViewById<TextView>(R.id.tvReservarHoraInicial)
        etTimeFinal = root.findViewById<TextView>(R.id.tvReservarHoraFinal)
        val btnReservaFechaContinuar = root.findViewById<Button>(R.id.btnReservarFechaContinuar)
        fecha = ""
        pistas_cogidas= ArrayList()
        fechas_reservadas= ArrayList()
        lista_reservas= ArrayList()
        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")


        databaseReference = database.reference.child("reservas")


        calendarioReserva.setOnDateChangeListener(CalendarView.OnDateChangeListener { _, year, month, dayOfMonth ->

            fecha = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            Log.e("fecha", fecha.length.toString())
        })


        etTimeInicio.setOnClickListener { showTimePickerDialog() }
        etTimeFinal.setOnClickListener { showTimePickerDialog2() }


        btnReservaFechaContinuar.setOnClickListener(View.OnClickListener {

            hora_inicio = etTimeInicio.text.toString()
            hora_final = etTimeFinal.text.toString()


            //comprobamos que ha introducido una fecha
            if (fecha.length != 0) {

                //comprobamos que ha introducido la hora de inicio y final de la reserva
                if (!hora_inicio.equals("Hora de inicio:") && !hora_final.equals("Hora final:")) {

                    //comprobamos que cumpla el horario (para no hacer una reserva a las 2 de la madrugada
                    if (!horas_validas(hora_inicio, hora_final)) {
                        Toast.makeText(
                            activity?.baseContext,
                            "Nuestro horario es de 10:00 a 19:00",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        //se pasan las horas (String) a horas de tipo LocalTime para poder trabajar con ellas y comprobar que estan bien
                        val horaInicio = LocalTime.parse(hora_inicio)
                        val horaFinal = LocalTime.parse(hora_final)
                        if (horaInicio.isAfter(horaFinal) || horaInicio.equals(horaFinal)) {
                            Toast.makeText(
                                activity?.baseContext,
                                "Revise las horas ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            //por como funciona el calendario cuando es un mes menor a 10 lo deja con formato 15/1/2021 en vez de 15/01/2021 y aqui se soluciona
                            if (fecha.length < 10) {
                                fecha = fecha.substring(0, 3) + "0" + fecha.substring(3, 9)
                                //Log.e("fecha_nueva", " " + fecha)
                            }

                            //ahora vamos a comprobar que no este la pista cogida ya
                            /////
                            /////
                            //ponemos la fecha junto con las horas
                            fecha_elegida = fecha + "/" + hora_inicio + "-" + hora_final
                            //se llama al metodo de comprobar que no haya una reserva previa para la misma pista en el mismo intervalo de horas
                            reservas_cogidas()


                           /* reserva.fecha = fecha_elegida

                            val reservar_pago = ReservarPagoFragment.newInstance(reserva)

                            val fm = fragmentManager
                            val transaction = fm!!.beginTransaction()
                            transaction.replace(R.id.nav_host_fragment, reservar_pago)
                            transaction.addToBackStack(null)
                            transaction.commit()*/


                        }
                    }
                } else {
                    Toast.makeText(
                        activity?.baseContext,
                        "por favor elige el horario",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    activity?.baseContext,
                    "elige una fecha",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


        })

        Log.e("reserva", " " + reserva.toString())
        return root;
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(reserva: Reserva) =
            ReservarFechaFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("reserva", reserva)
                }
            }
    }

    private fun horas_validas(hora_inicio: String, hora_final: String): Boolean {
        var hora_valida = false

        val hora_inicio_valida =
            (hora_inicio.startsWith("1:") || hora_inicio.startsWith("2:") || hora_inicio.startsWith(
                "3"
            ) || hora_inicio.startsWith("4:") || hora_inicio.startsWith("5:") || hora_inicio.startsWith(
                "6:"
            ) || hora_inicio.startsWith("7:") || hora_inicio.startsWith("8:") || hora_inicio.startsWith(
                "9:"
            ) || hora_inicio.startsWith("20:") || hora_inicio.startsWith("21:") || hora_inicio.startsWith(
                "22:"
            ) || hora_inicio.startsWith("23:") || hora_inicio.startsWith("0:"))
        val hora_final_valida =
            (hora_final.startsWith("1:") || hora_final.startsWith("2:") || hora_final.startsWith(
                "3:"
            ) || hora_final.startsWith(
                "4:"
            ) || hora_final.startsWith("5:") || hora_final.startsWith("6:") || hora_final.startsWith(
                "7:"
            ) || hora_final.startsWith("8:") || hora_final.startsWith("9:") || hora_final.startsWith(
                "20:"
            ) || hora_final.startsWith("21:") || hora_final.startsWith("22:") || hora_final.startsWith(
                "23:"
            ) || hora_final.startsWith("0:"))

        if (!hora_inicio_valida && !hora_final_valida) {
            hora_valida = true
        }


        return hora_valida
    }

    private fun reservas_cogidas (){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val id = it.key
                    lista_reservas.add(id.toString())
                }
                fechas_pistas(lista_reservas)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }

    private fun fechas_pistas(lista_id : ArrayList<String>){
        for(idLista in lista_id){
            var referencia = databaseReference.child(idLista.toString())
            referencia
                .addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //Log.e(" fechas_de_reservas", " " + snapshot.child("fecha").value.toString())
                        //Log.e(" fecha-PISTAS", " " + snapshot.child("pista").child("nombre").value.toString())
                        val fechaa =snapshot.child("fecha").value.toString()
                        val pista=snapshot.child("pista").child("nombre").value.toString()
                        //Log.e("fecha",fechaa+pista)

                        fechas_reservadas.add( fechaa)
                        pistas_cogidas.add(pista)

                        if(lista_id.size== fechas_reservadas.size){
                            fecha_valida(fechas_reservadas,pistas_cogidas)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })



        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fecha_valida(fechas_reservada: ArrayList<String>, pistas_cogida: ArrayList<String>) {
        val i=0

        for (fe in fechas_reservada){

            if(pistas_cogida.get(i).equals(reserva.pista.nombre)){

                    val fecha_reserva = fechas_reservada.get(i).substring(0,10)
                    Log.e("fecha_mini"," "+fecha_reserva)
                    //se comprueba que la fecha de la pista no este , puesto que si no es la misma no hay por que comparar mas
                    if(fecha_reserva.equals(fecha)){
                        val h1=fechas_reservada.get(i).substring(11,16)
                        val h2= fechas_reservada.get(i).substring(17,fechas_reservada.get(i).length)

                        Log.e("fecha_ hora inicial / final "," "+h1 + " AA" +h2)

                        /*
                        Aqui empezamos a comprobar que las horas no se solapan y no se reserva en el intervalo de tiempo que ya tiene reserva
                        Para enterder mas facilmente cada caso voy a explicarlo con un ejemplo practico.
                        Partimos de que hay una reserva de 15:00 a 19:00 de la tarde
                        */

                        //para cuando el usuario meta 15:00 y 19:00
                        if(hora_inicio.equals(h1) || (hora_final.equals(h2))) {
                            Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                        }else{
                            //para trabajar con las horas las pasamos al tipo de objeto de LocalTime
                            val horaInicioBBDDLocal = LocalTime.parse(h1)
                            val horaFinalBBDDLocal = LocalTime.parse(h2)

                            val horaInicio = LocalTime.parse(hora_inicio)
                            val horaFinal = LocalTime.parse(hora_final)

                            //cuando meta 14:00 y 16:00
                            if(horaInicio.isBefore(horaInicioBBDDLocal) && horaFinal.isAfter(horaInicioBBDDLocal) ){
                                Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                            }else{
                                //cuando meta 14:00 y 20:00
                                if(horaInicio.isBefore(horaFinalBBDDLocal) && horaFinal.isAfter(horaFinalBBDDLocal)){
                                    Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                                }else{
                                    //cuando meta 15:00 y 17:00
                                    if(hora_inicio.equals(h1) && horaFinal.isBefore(horaFinalBBDDLocal)){
                                        Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                                    }else{
                                        //cuando meta 14:00 y 19:00
                                        if(horaInicio.isBefore(horaFinalBBDDLocal) && hora_final.equals(h2)){
                                            Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                                        }else{
                                            //cuando meta 17:00 y 18:00
                                            if(horaInicio.isAfter(horaInicioBBDDLocal) && horaFinal.isBefore(horaFinalBBDDLocal)){
                                                Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                                            }else {
                                                //cuando meta 17:00 y 19:00
                                                if(horaInicio.isAfter(horaInicioBBDDLocal) && hora_final.equals(h2)){
                                                    Toast.makeText(activity?.baseContext, "horas en uso", Toast.LENGTH_SHORT).show()
                                                }else{
                                                    //cuando se compruebe se pasa a la parte del pago dandole el objeto de Reserva con todos los datos acumulados
                                                    fecha_cogida = false
                                                    guardarFecha()
                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }else{
                        guardarFecha()
                    }


            }else{
                guardarFecha()
            }


        }

    }

    private fun guardarFecha(){
        Log.e("hora_bien","hora bien ")
        reserva.fecha = fecha_elegida
        Toast.makeText(activity?.baseContext, "Fecha elegida:  "+fecha_elegida, Toast.LENGTH_SHORT).show()
        val reservar_pago = ReservarPagoFragment.newInstance(reserva)

        val fm = fragmentManager
        val transaction = fm!!.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, reservar_pago)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "timePicker")

    }

    private fun onTimeSelected(time: String) {
        etTimeInicio.setText(time.substring(0, 3) + "00")
    }

    private fun showTimePickerDialog2() {
        val timePicker = TimePickerFragment { onTimeSelected2(it) }
        timePicker.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected2(time: String) {
        etTimeFinal.setText(time.substring(0, 3) + "00")
    }
}