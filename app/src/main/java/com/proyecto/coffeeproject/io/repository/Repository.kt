package com.proyecto.coffeeproject.io.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.callbackFlow

class Repository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getBookDetails() {//= callbackFlow {

        val collection = firestore.collection("books")
        /*val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                //OnSuccess(value)
            } else {
                //OnError(error)
            }

            //offer(response)
        }

        awaitClose {
            snapshotListener.remove()
        }*/
    }

}