package com.example.tenisclubdroid.ui.reservar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Reserva
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class ReservarPagoFragment : Fragment() {
    lateinit var reserva: Reserva
    lateinit var root: View
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


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
        val tvReservaPagoPrecio = root.findViewById<TextView>(R.id.tvReservaPagoPrecio)
        val btnReservarPagar = root.findViewById<Button>(R.id.btnReservarPagoPagar)

        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")


        databaseReference = database.reference.child("reservas")

        var precio_final = reserva.pista.precio

        var i = 0

        for (precio in reserva.extras) {
            if (i < reserva.extras.size) {
                precio_final += reserva.extras.get(i)
            }
            i++
        }
        tvReservaPagoPrecio.setText(precio_final.toString() + " € con i.v.a")
        Log.e("precio", "" + precio_final.toString())

        reserva.precio=precio_final


        btnReservarPagar.setOnClickListener(View.OnClickListener {

            val random = Random()

            val numerito = random.nextInt(0..100000)
            val idReserva = reserva.idReservador+numerito.toString()
            reserva.idReserva=idReserva

            databaseReference.child(idReserva).setValue(reserva).addOnCompleteListener {
                Toast.makeText(requireContext(),"Pista Reservada!",Toast.LENGTH_LONG).show()
            }


        })


        return root
    }
    fun Random.nextInt(range: IntRange): Int {
        return range.start + nextInt(range.last - range.start)
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