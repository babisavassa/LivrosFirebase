package com.babi.livrosfirebase.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.babi.livrosfirebase.datasource.DataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivros(navController: NavController) {

    val dataSource = DataSource()
    var listaLivros by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var mensagem by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Carregar lista ao abrir a tela
    LaunchedEffect(Unit) {
        dataSource.listarLivros(
            onResult = { livros -> listaLivros = livros },
            onFailure = { e -> mensagem = "Erro: ${e.message}" }
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Usuário", fontWeight = FontWeight.Bold) },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = { Text(user?.email ?: "Não logado") },
                    selected = false,
                    onClick = {}
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    icon = { Icon(Icons.Default.Close, contentDescription = "") },
                    onClick = {
                        auth.signOut()
                        navController.navigate("login")
                    }
                )

                HorizontalDivider()
                Text(
                    "Menu de Opções",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    label = { Text(text = "Lista de Livros") },
                    selected = false,
                    onClick = { navController.navigate("ListaLivros") }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Cadastrar Livros") },
                    selected = false,
                    onClick = { navController.navigate("CadastroLivros") }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Lista de Livros",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            },
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Ícone do Menu",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFFA726)
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("CadastroLivros") },
                    containerColor = Color(0xFFFFA726),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Cadastrar Livro")
                }
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(listaLivros.size) { index ->
                        val livro = listaLivros[index]
                        val titulo = livro["titulo"] as? String ?: "Sem título"
                        val genero = livro["genero"] as? String ?: "Sem gênero"
                        val autor = livro["autor"] as? String ?: "Sem autor"
                        var lido by remember {
                            mutableStateOf(livro["lido"] as? Boolean ?: false)
                        }

                        Row(
                            modifier = Modifier.padding(
                                start = 10.dp,
                                top = 10.dp,
                                end = 10.dp
                            )
                        ) {
                            Column(modifier = Modifier.width(250.dp)) {
                                Text("📖 $titulo", fontWeight = FontWeight.Bold)
                                Text("✍ Autor: $autor")
                                Text("🏷 Gênero: $genero")
                            }

                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir Livro",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        dataSource.deletarLivro(titulo)
                                        // recarrega a lista
                                        dataSource.listarLivros(
                                            onResult = { livros -> listaLivros = livros },
                                            onFailure = { e -> mensagem = "Erro: ${e.message}" }
                                        )
                                    }
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }

                if (mensagem.isNotEmpty()) {
                    Text(
                        text = mensagem,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
