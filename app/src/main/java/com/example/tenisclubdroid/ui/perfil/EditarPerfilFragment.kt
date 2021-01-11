package com.example.tenisclubdroid.ui.perfil

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tenisclubdroid.R
import com.example.tenisclubdroid.ui.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
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
    lateinit var btnEditarFoto: ImageButton
    lateinit var btnPerfilActualizar: Button


    lateinit var usuario: Usuario
    lateinit var currentPhotoPath: String
    lateinit var photoUri: Uri
    lateinit var root: View

    var fotoUri: Uri? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var nombresCogidos: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getSerializable("usuario").let {
            usuario = it as Usuario
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_editar_perfil, container, false)


        //para que el teclado no se vuelva loco
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //inicializamos
        inicializar()

        database =
            FirebaseDatabase.getInstance("https://tenisclubdroid-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("usuarios")
        nombresCogidos = database.reference.child("NombresCogidos")



        btnEditarFoto.setOnClickListener(View.OnClickListener {

            mostrarDialogoFoto()


        })

        btnPerfilActualizar.setOnClickListener(View.OnClickListener {

            //se comprueba que estan los campos rellenos
            if (!etEditarNickName.text.toString().isEmpty() && !etEditarDescripcion.text.toString()
                    .isEmpty()
            ) {

                val nickname = etEditarNickName.text.toString()

                nombresCogidos.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.e("repetido", "" + dataSnapshot.child(nickname).key)
                        if (!dataSnapshot.hasChild(nickname) || dataSnapshot.child(nickname).value == usuario.idUsuario) {

                            if (!etEditarNickName.text.toString()
                                    .equals(nombresCogidos.child(usuario.nickName))
                            ) {

                                nombresCogidos.child(usuario.nickName).removeValue()
                            }
                            nombresCogidos.child(nickname).setValue(usuario.idUsuario)
                            subirUsuario()
                        } else {
                            Toast.makeText(
                                activity?.baseContext,
                                "Nombre de usuario no valido",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                ///


            } else {
                Toast.makeText(activity?.baseContext, "Revise los campos", Toast.LENGTH_SHORT)
                    .show()
            }
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
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Imagen")
        fotoUri =
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)!!
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
        startActivityForResult(camaraIntent, CAMARA)
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
                    Picasso.get().load(fotoUri).rotate(90F) .transform(ImagenRedonda()).into(ivPerfilFoto)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "¡Fallo Galeria!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMARA) {

            Picasso.get().load(fotoUri).transform(ImagenRedonda()).into(ivPerfilFoto)

        }
    }

    private fun inicializar() {
        etEditarNickName = root.findViewById<EditText>(R.id.etEditarPerfilNickName)
        etEditarDescripcion = root.findViewById<EditText>(R.id.etEditarPerfilDescripcion)
        btnEditarFoto = root.findViewById<ImageButton>(R.id.ibEditarPerfilFoto)
        ivPerfilFoto = root.findViewById<ImageView>(R.id.ivEditarPerfilFoto)
        btnPerfilActualizar = root.findViewById<Button>(R.id.btnEditarActualizar)

        etEditarNickName.setText(usuario.nickName)
        etEditarDescripcion.setText(usuario.descripcion)
        Picasso.get().load(usuario.fotoPerfil).transform(ImagenRedonda()).into(ivPerfilFoto)
    }

    companion object {

        @JvmStatic
        fun newInstance(usuario: Usuario) =
            EditarPerfilFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("usuario", usuario)
                }
            }
    }

    private fun subirUsuario() {
        if (fotoUri == null) {
            guardar(usuario.fotoPerfil)
        } else {
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
        //se crea el usuario con los datos
        val user = Usuario(
            etEditarNickName.text.toString().trim(),
            fotoUrl,
            etEditarDescripcion.text.toString().trim(),
            0,
            fUser.uid.toString()
        )
        //se guardan los cambios
        currentUserDb.setValue(user)
        Toast.makeText(activity?.baseContext, "Perfil Actualizado", Toast.LENGTH_SHORT).show()


        val fm = fragmentManager
        val perfil = PerfilFragment()
        val transaction = fm!!.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, perfil)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.menu_editar_perfil, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                val perfil = PerfilFragment()
              
                val fm = fragmentManager
                val transaction = fm!!.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, perfil)
                transaction.addToBackStack(null)
                transaction.commit()

            }

        }
        return super.onOptionsItemSelected(item)
    }
}