package com.tricode.easyinventory

import android.os.Bundle
import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().subscribeToTopic("expiring_products")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "Fallo al suscribirse al tema de notificaciones")
                } else {
                    Log.d("FCM", "Suscripción a productos por caducar completada")
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic("expired_products")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "Fallo al suscribirse al tema de productos caducados")
                } else {
                    Log.d("FCM", "Suscripción a productos caducados completada")
                }
            }

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current.applicationContext as Application
            val viewModel: InventoryViewModel = viewModel(factory = InventoryViewModelFactory(context))

            InventoryNavHost(navController, viewModel)
        }
    }
}
