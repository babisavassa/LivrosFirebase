package com.babi.livrosfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.babi.livrosfirebase.ui.theme.LivrosFirebaseTheme
import com.babi.livrosfirebase.view.ListaLivro
import com.babi.livrosfirebase.view.CadastroLivro

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LivrosFirebaseTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "CadastroLivros"
                ) {
                    composable("CadastroLivros") { CadastroLivro(navController) }
                    composable("ListaLivros") { ListaLivro(navController) }
                }
            }
        }
    }
}




