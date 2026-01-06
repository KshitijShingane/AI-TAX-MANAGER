package com.deducto.app.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseModule {
    fun init(context: Context) {
        // Initialize Firebase SDK; will use google-services.json placed in app/
        FirebaseApp.initializeApp(context)
    }
}
