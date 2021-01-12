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
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

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


        calendarioReserva.setOnDateChangeListener(CalendarView.OnDateChangeListener { _, year, month, dayOfMonth ->
            fecha = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            Log.e("fecha", fecha)
        })




        etTimeInicio.setOnClickListener { showTimePickerDialog() }
        etTimeFinal.setOnClickListener { showTimePickerDialog2() }


        btnReservaFechaContinuar.setOnClickListener(View.OnClickListener {

            hora_inicio = etTimeInicio.text.toString()
            hora_final = etTimeFinal.text.toString()

            val hora_inicio_valida =
                (hora_inicio.startsWith("1:") || hora_inicio.startsWith("2:") || hora_inicio.startsWith(
                    "3"
                ) || hora_inicio.startsWith("4:") || hora_inicio.startsWith("5:") || hora_inicio.startsWith(
                    "6:"
                ) || hora_inicio.startsWith("7:") || hora_inicio.startsWith("8:") || hora_inicio.startsWith(
                    "9:"
                ))
            val hora_final_valida =
                (hora_final.startsWith("1:") || hora_final.startsWith("2:") || hora_final.startsWith("3:") || hora_final.startsWith(
                    "4:"
                ) || hora_final.startsWith("5:") || hora_final.startsWith("6:") || hora_final.startsWith(
                    "7:"
                ) || hora_final.startsWith("8:") || hora_final.startsWith("9:"))
            if (hora_inicio_valida || hora_final_valida) {
                Toast.makeText(
                    activity?.baseContext,
                    "Nuestro horario es de 10:00 a 19:00",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                /*if (hora_inicio.length < 5) {
                    hora_inicio = hora_inicio + "0"
                }
                if (hora_final.length < 5) {
                    hora_final = hora_final + "0"
                }*/

                hora_inicio= hora_inicio.substring(0,3) + "00"
                hora_final= hora_final.substring(0,3) + "00"

                val horaInicio = LocalTime.parse(hora_inicio)
                val horaFinal = LocalTime.parse(hora_final)
                if (horaInicio.isAfter(horaFinal) || horaInicio.equals(horaFinal)) {
                    Toast.makeText(activity?.baseContext, "Revise las horas ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val fecha_elegida = fecha + "/" + hora_inicio + "-" + hora_final
                    Toast.makeText(activity?.baseContext, fecha_elegida, Toast.LENGTH_SHORT)
                        .show()
                }
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

    /*private fun rellenar_horas(){
        spinnerHoraInicio= root.findViewById<Spinner>(R.id.spinnerReservarFechaHora1)
        spinnerHoraFinal = root.findViewById<Spinner>(R.id.spinnerReservarFechaHora2)

        val lista_horas  = arrayOf(" ","09:00","10:00","11:00","12:00","13:00","16:00","17:00","18:00","19:00")


        spinnerHoraInicio.setAdapter(
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lista_horas
            )
        )

        spinnerHoraFinal.setAdapter(
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                lista_horas
            )
        )

        spinnerHoraInicio.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //se hace un switch dependiendo de lo seleccionado
                val seleccion = spinnerHoraInicio.getSelectedItem() as String

                when (seleccion){
                    "09:00"-> {
                        hora_inicio = lista_horas.get(1)
                    }
                    "10:00"->{

                        hora_inicio = lista_horas.get(2)
                    }
                    "11:00"->{
                        hora_inicio = lista_horas.get(3)
                    }
                    "12:00"->{
                        hora_inicio = lista_horas.get(4)
                    }
                    "13:00"->{
                        hora_inicio = lista_horas.get(5)
                    }
                    "16:00"->{
                        hora_inicio = lista_horas.get(6)
                    }
                    "17:00"->{
                        hora_inicio = lista_horas.get(7)
                    }
                    "18:00"->{
                        hora_inicio = lista_horas.get(8)
                    }
                    "19:00"->{
                        Toast.makeText(
                            activity?.baseContext, "No se puede elegir esta hora para iniciar", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinnerHoraFinal.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //se hace un switch dependiendo de lo seleccionado
                val seleccion = spinnerHoraFinal.getSelectedItem() as String

                when (seleccion){
                    "09:00"-> {
                        Toast.makeText(
                            activity?.baseContext, "No puedes elegir esta hora como final ", Toast.LENGTH_SHORT).show()
                    }
                    "10:00"->{

                        hora_final = lista_horas.get(2)
                    }
                    "11:00"->{
                        hora_final = lista_horas.get(3)
                    }
                    "12:00"->{
                        hora_final = lista_horas.get(4)
                    }
                    "13:00"->{
                        hora_final = lista_horas.get(5)
                    }
                    "16:00"->{
                        hora_final = lista_horas.get(6)
                    }
                    "17:00"->{
                        hora_final = lista_horas.get(7)
                    }
                    "18:00"->{
                        hora_final = lista_horas.get(8)
                    }
                    "19:00"->{
                        hora_final = lista_horas.get(9)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }*/

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "timePicker")

    }

    private fun onTimeSelected(time: String) {
        etTimeInicio.setText(time)
    }

    private fun showTimePickerDialog2() {
        val timePicker = TimePickerFragment { onTimeSelected2(it) }
        timePicker.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected2(time: String) {
        etTimeFinal.setText(time)
    }
}