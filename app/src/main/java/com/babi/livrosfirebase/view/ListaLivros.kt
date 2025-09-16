package com.babi.livrosfirebase.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.babi.livrosfirebase.datasource.DataSource
import com.babi.livrosfirebase.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivro(navController: NavController) {
    val dataSource = DataSource()
    var listaLivros by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var mensagem by remember { mutableStateOf("") }
    var livroSelecionado by remember { mutableStateOf<Map<String, Any>?>(null) }
    val scope = rememberCoroutineScope()

    // Carrega os livros quando a tela abre
    LaunchedEffect(Unit) {
        dataSource.listarLivros(
            onResult = { listaLivros = it },
            onFailure = { e -> mensagem = "Erro: ${e.message}" }
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menu do App Livros", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text("Cadastrar Livro") },
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
                            if (livroSelecionado == null) "Lista de Livros"
                            else "Detalhes do Livro"
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = { BottomAppBar {} },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("CadastroLivros") }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                }
            }
        ) { innerPadding ->
            if (livroSelecionado == null) {
                // LISTA DE LIVROS
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                        .padding(innerPadding)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(listaLivros) { livro ->
                            val t = livro["titulo"] as? String ?: ""
                            val a = livro["autor"] as? String ?: ""
                            val g = livro["genero"] as? String ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { livroSelecionado = livro },
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(containerColor = White)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("📌 $t", fontWeight = FontWeight.Bold, color = Purple40)
                                        Text("Autor: $a", color = Black)
                                        Text("Gênero: $g", color = Black)
                                    }

                                    // Botão de excluir
                                    IconButton(
                                        onClick = {
                                            dataSource.deletarLivro(t)
                                            dataSource.listarLivros(
                                                onResult = { listaLivros = it },
                                                onFailure = { e -> mensagem = "Erro: ${e.message}" }
                                            )
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Excluir livro",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(text = mensagem, modifier = Modifier.padding(8.dp))
                }
            } else {
                // DETALHES DO LIVRO
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        "Título: ${livroSelecionado!!["titulo"]}",
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text("Autor: ${livroSelecionado!!["autor"]}", color = Black)
                    Text("Gênero: ${livroSelecionado!!["genero"]}", color = Black)

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { livroSelecionado = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Voltar para lista", color = White)
                    }
                }
            }
        }
    }
}
