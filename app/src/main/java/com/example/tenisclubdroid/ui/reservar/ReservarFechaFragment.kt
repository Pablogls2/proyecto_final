package com.example.tenisclubdroid.ui.reservar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Pista
import com.example.tenisclubdroid.ui.clases.Reserva
import com.example.tenisclubdroid.ui.clases.TimePickerFragment
import com.example.tenisclubdroid.ui.clases.Usuario
import com.squareup.picasso.Picasso
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

    lateinit var reserva : Reserva
    lateinit var root: View
    lateinit var calendarioReserva : CalendarView
    lateinit var fecha : String
    lateinit var spinnerHoraInicio : Spinner
    lateinit var spinnerHoraFinal: Spinner
    lateinit var hora_inicio: String
    lateinit var hora_final: String
    lateinit var etTimeInicio : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getSerializable("reserva").let {
            reserva = it as Reserva
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_reservar_fecha, container, false)

        calendarioReserva= root.findViewById<CalendarView>(R.id.calendarReservarFecha)
        fecha=""


        calendarioReserva.setOnDateChangeListener(CalendarView.OnDateChangeListener{_,year  , month, dayOfMonth  ->
             fecha =dayOfMonth.toString() + "-" + (month +1) +"-"+ year
            Log.e("fecha",fecha)
        })

        rellenar_horas()
        etTimeInicio= root.findViewById<EditText>(R.id.editHora1)

        etTimeInicio.setOnClickListener { showTimePickerDialog()}

        Log.e("reserva", " "+ reserva.toString())
        return root;
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(reserva : Reserva) =
            ReservarFechaFragment().apply {
                arguments = Bundle().apply {
                   putSerializable("reserva",reserva)
                }
            }
    }

    private fun rellenar_horas(){
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
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String) {
        etTimeInicio.setText(time)
    }
}