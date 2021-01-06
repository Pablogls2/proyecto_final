package com.example.tenisclubdroid.ui.perfil

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditarPerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private val GALERIA: Int = 1
private const val CAMARA = 2

class EditarPerfilFragment : Fragment() {

    lateinit var ivPerfilFoto: ImageView
    lateinit var etEditarNickName: EditText
    lateinit var etEditarDescripcion: EditText

    var fotoUri: Uri? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        //para que el teclado no se vuelva loco
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("usuarios")

        etEditarNickName = root.findViewById<EditText>(R.id.etEditarPerfilNickName)
        etEditarDescripcion = root.findViewById<EditText>(R.id.etEditarPerfilDescripcion)
        val btnEditarFoto = root.findViewById<ImageButton>(R.id.ibEditarPerfilFoto)
        ivPerfilFoto = root.findViewById<ImageView>(R.id.ivEditarPerfilFoto)
        val btnPerfilActualizar = root.findViewById<Button>(R.id.btnEditarActualizar)


        btnEditarFoto.setOnClickListener(View.OnClickListener {

            mostrarDialogoFoto()


        })

        btnPerfilActualizar.setOnClickListener(View.OnClickListener {
            subirUsuario()
        })


        return root;
    }

    private fun mostrarDialogoFoto() {
        val fotoDialogo = AlertDialog.Builder(context)
        fotoDialogo.setTitle("Seleccionar Acción")
        val fotoDialogoItems = arrayOf(
            "Seleccionar fotografía de galería",
            "Capturar fotografía desde la cámara"
        )
        fotoDialogo.setItems(
            fotoDialogoItems
        ) { dialog, which ->
            when (which) {
                0 -> elegirFotoGaleria()
                1 -> tomarFotoCamara()
            }
        }
        fotoDialogo.show()
    }

    fun elegirFotoGaleria() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            galleryIntent,
            GALERIA
        )
    }

    private fun tomarFotoCamara() {
        try {


            // Si queremos hacer uso de fotos en aklta calidad
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            // Eso para alta o baja
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Esto para alta calidad
            // photoURI = Uri.fromFile(this.crearFichero());
            // intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);

            // Esto para alta y baja
            startActivityForResult(intent, CAMARA)
        } catch (e: Exception) {
            Toast.makeText(activity, "¡Fallito wey!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("FOTO", "Opción::--->$requestCode")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALERIA) {
            Log.d("FOTO", "Entramos en Galería")
            if (data != null) {
                // Obtenemos su URI con su dirección temporal
                fotoUri = data.data
                try {
                    // Obtenemos el bitmap de su almacenamiento externo
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().applicationContext.contentResolver,
                        fotoUri
                    )
                    //guardamos en un bitmap publico el bitmap cogido
                    // imagenFinal = bitmap
                    Toast.makeText(activity, "¡Foto salvada!", Toast.LENGTH_SHORT).show()
                    //mostramos en el ImageView el bitmap seleccionado
                    this.ivPerfilFoto.setImageBitmap(bitmap)
                    // imagen = bitmapToBase64(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "¡Fallo Galeria!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMARA) {
            Log.d("FOTO", "Entramos en Camara")
            // Cogemos la imagen, pero podemos coger la imagen o su modo en baja calidad (thumbnail
            var thumbnail: Bitmap? = null
            try {
                try {


                    // Esta línea para baja
                    thumbnail = data!!.extras!!["data"] as Bitmap?
                    // Esto para alta
                    // thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), photoURI);
                    //imagenFinal = thumbnail
                    // salvamos
                    // path = salvarImagen(thumbnail); //  photoURI.getPath(); Podríamos poner esto, pero vamos a salvarla comprimida y borramos la no comprimida (por gusto)
                    //mostramos en el ImageView el bitmap seleccionado
                    this.ivPerfilFoto.setImageBitmap(thumbnail)
                    //imagen = bitmapToBase64(thumbnail)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(activity, "¡Fallito wey!", Toast.LENGTH_SHORT).show()
                }
                // Borramos el fichero de la URI
                //borrarFichero(photoURI.getPath());
                Toast.makeText(activity, "¡Foto Salvada!", Toast.LENGTH_SHORT).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "¡Fallo Camara!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarPerfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditarPerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

    private fun subirUsuario() {
        if (fotoUri == null){
            //guardar(user.getFotoPerfil)
        }else{
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/imagenes/$filename")

            ref.putFile(fotoUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    guardar(it.toString())
                }
            }
        }

    }

    private fun guardar(fotoUrl: String) {
        val fUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val currentUserDb = databaseReference.child(fUser.uid)


        //public Usuario(String nickName, String fotoPerfil, String descripcion, int rol)
        val user = Usuario(etEditarNickName.text.toString() ,fotoUrl,"pito", 0)
        currentUserDb.setValue(user)
        Toast.makeText(activity?.baseContext, "Perfil Actualizado", Toast.LENGTH_SHORT).show()


        val fm = fragmentManager
        val perfil = PerfilFragment()
        val transaction = fm!!.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, perfil)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}