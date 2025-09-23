package com.babi.livrosfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.babi.livrosfirebase.ui.theme.LivrosFirebaseTheme
import com.babi.livrosfirebase.view.Cadastro
import com.babi.livrosfirebase.view.CadastroLivros
import com.babi.livrosfirebase.view.ListaLivros
import com.babi.livrosfirebase.view.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LivrosFirebaseTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login",

                    ) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("Cadastro") {
                        Cadastro(navController = navController)
                    }
                    composable("CadastroLivros") {
                        CadastroLivros(navController = navController)
                    }
                    composable("ListaLivros") {
                        ListaLivros(navController = navController)
                    }
                }
            }
        }
    }
}




