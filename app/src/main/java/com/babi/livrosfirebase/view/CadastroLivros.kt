package com.babi.livrosfirebase.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.babi.livrosfirebase.datasource.DataSource
import com.babi.livrosfirebase.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroLivro(navController: NavController) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val dataSource = DataSource()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menu do App Livros", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Lista de Livros:") },
                    selected = false,
                    onClick = { navController.navigate("ListaLivros") }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Cadastrar Livro") },
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
            bottomBar = { BottomAppBar{} },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("ListaLivros") }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título do Livro") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Purple40,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Purple40,
                        cursorColor = Purple40
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp,vertical=4.dp)
                )
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = Purple40,
                        unfocusedBorderColor = Color.Black,
                        unfocusedLabelColor = Purple40,
                        cursorColor = Purple40
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                )

                OutlinedTextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Gênero") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple40,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Purple40,
                        cursorColor = Purple40
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Button(
                    onClick = {
                        if (titulo.isNotBlank() && autor.isNotBlank() && genero.isNotBlank()) {
                            dataSource.salvarLivro(
                                titulo, autor, genero,
                                onSuccess = { mensagem = "Livro cadastrado" },
                                onFailure = { _ -> mensagem = " Erro no cadastro" }
                            )
                            titulo = ""
                            autor = ""
                            genero = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                 {
                    Text("Cadastrar Livro", color = Color.White)
                }
                Spacer(modifier = Modifier.size(20.dp))

                if (mensagem.isNotBlank()) {
                    Text(text = mensagem)
                }
            }
        }
    }
}
