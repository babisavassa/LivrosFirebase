package com.babi.livrosfirebase.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.babi.livrosfirebase.datasource.DataSource
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroLivros(navController: NavController) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    val dataSource = DataSource()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "📚 Menu do App Livros",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Lista de Livros") },
                    selected = false,
                    onClick = { navController.navigate("ListaLivros") }
                )
                NavigationDrawerItem(
                    label = { Text("Cadastro de Livros") },
                    selected = false,
                    onClick = { navController.navigate("CadastroLivros") }
                )
                NavigationDrawerItem(
                    label = { Text("Sair") },
                    selected = false,
                    onClick = { navController.navigate("login") }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Cadastrar Livro", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFFA726),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = { BottomAppBar { } },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("ListaLivros") },
                    containerColor = Color(0xFFFFA726),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Lista de Livros")
                }
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Novo Livro",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF57C00),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título do Livro", color = Color(0xFFF57C00)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Gênero do Livro", color = Color(0xFFF57C00)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor do Livro", color = Color(0xFFF57C00)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (titulo.isNotEmpty() && genero.isNotEmpty() && autor.isNotEmpty()) {
                            dataSource.salvarLivro(
                                titulo,
                                genero,
                                autor,
                                onSucess = {
                                    // Só executa se o livro for salvo com sucesso
                                    mensagem = "✔ Livro salvo com sucesso!"
                                    // Limpa os campos
                                    titulo = ""
                                    genero = ""
                                    autor = ""
                                    // Redireciona para a lista de livros
                                    navController.navigate("ListaLivros")
                                },
                                onFailure = { erro ->
                                    mensagem = "❌ Erro ao salvar livro!"
                                }
                            )
                        } else {
                            mensagem = "⚠ Preencha todos os campos."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        "Cadastrar Livro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))

                if (mensagem.isNotEmpty()) {
                    Text(
                        text = mensagem,
                        color = if (mensagem.contains("sucesso")) Color(0xFF4CAF50) else Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}