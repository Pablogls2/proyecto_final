package com.example.tenisclubdroid

import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Comunicacion {

    //acceder con login propio
    fun login(email: String, passwd: String, auth: FirebaseAuth): Int {

        var resultado = 404

        // auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(
            email,
            passwd
        ).addOnCompleteListener {

            if (it.isSuccessful) {
                resultado = 0
            } else {
                resultado = 1
            }
        }


        return resultado


    }

    //registro
    fun registro(
        email: String,
        nickname: String,
        passwd: String,
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        databaseReference: DatabaseReference
    ): Int {
        var resultado = 404


        // databaseReference = database?.reference!!.child("usuario")

        auth.createUserWithEmailAndPassword(
            email,
            passwd
        ).addOnCompleteListener {

            if (it.isSuccessful) {
                // val currentUser = auth.currentUser

                val user: FirebaseUser = auth.currentUser!!
                //val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                //val currentUserDb = currentUser?.let { it1 -> databaseReference!!.child(it1.uid) }

                val currentUserDb = databaseReference.child(user.uid)

                currentUserDb.child("nickname").setValue(nickname)
                currentUserDb.child("rol").setValue(0)


                resultado = 0


            } else {
                resultado = 1
            }

        }





        return resultado
    }

}