package com.example.tenisclubdroid.ui.reservar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tenisclubdroid.MainActivity
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.Login.LoginActivity
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

        val etReservaPagoNumTarjeta = root.findViewById<EditText>(R.id.etReservaPagoNumTarjeta)
        val etReservaCsv = root.findViewById<EditText>(R.id.etReservaPagoCsv)
        val etReservaFechaTarjeta = root.findViewById<EditText>(R.id.etReservaPagoFechaTarjeta)

        //para que el teclado funcione bien
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //cogemos la bbdd de las reservas
        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")


        databaseReference = database.reference.child("reservas")

        //cogemos el precio final, que es la suma del precio de la pista + los extras
        // lo hacemos aqui por si en un futuro la app se completa y permite poner precio por tramos horarios

        var precio_final = reserva.pista.precio

        var i = 0

        for (precio in reserva.extras) {
            if (i < reserva.extras.size) {
                precio_final += reserva.extras.get(i)
            }
            i++
        }
        //se le muestra al usuario el precio (i.v.a incluido)
        tvReservaPagoPrecio.setText(precio_final.toString() + " € con i.v.a")
        Log.e("precio", "" + precio_final.toString())

        reserva.precio=precio_final


        btnReservarPagar.setOnClickListener(View.OnClickListener {

            //en esta fase de la app no admitimos paypal por lo que será solo con "visa", por motivos del equipo en verdad no se podrá pagar digitalmente nada
            if(!etReservaPagoNumTarjeta.text.toString().isEmpty() && !etReservaCsv.text.isEmpty() && !etReservaFechaTarjeta.text.isEmpty()){
                val random = Random()

                val numerito = random.nextInt(0..100000)
                //se crea la id de la reserva con el id del reservador y un numero aleatorio para que no se repita
                val idReserva = reserva.idReservador+numerito.toString()
                reserva.idReserva=idReserva

                //se introduce en la vase de datos y nos llevará al a pantalla de Home
                databaseReference.child(idReserva).setValue(reserva).addOnCompleteListener {
                    Toast.makeText(requireContext(),"Pista Reservada!",Toast.LENGTH_SHORT).show()
                    //requireActivity().onBackPressed()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                }
            }else{
                Toast.makeText(requireContext(),"Compruebe los datos de la tarjeta",Toast.LENGTH_SHORT).show()
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