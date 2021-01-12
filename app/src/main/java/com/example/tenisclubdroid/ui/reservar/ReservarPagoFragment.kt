package com.example.tenisclubdroid.ui.reservar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Reserva

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservarPagoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservarPagoFragment : Fragment() {
    lateinit var reserva: Reserva
    lateinit var root : View

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
        root = inflater.inflate(R.layout.fragment_reservar_pago, container, false)

        return  root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(reserva: Reserva) =
            ReservarPagoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("reserva", reserva)
                }
            }
    }
}