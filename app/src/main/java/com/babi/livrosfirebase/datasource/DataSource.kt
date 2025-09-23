package com.babi.livrosfirebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataSource {
 private val db = FirebaseFirestore.getInstance()

 fun salvarLivro(
  titulo: String,
  genero: String,
  autor: String,
  onSucess: () -> Unit,
  onFailure: (Any) -> Unit
 ) {
  val userId = FirebaseAuth.getInstance().currentUser?.uid
  if (userId == null) {
   onFailure("Usuário não autenticado")
   return
  }

  val livroMap = hashMapOf(
   "titulo" to titulo,
   "genero" to genero,
   "autor" to autor,
   "userId" to userId   // 🔥 vínculo com o usuário
  )

  db.collection("Livros")
   .document(titulo) // cuidado: sobrescreve se tiver outro com mesmo título
   .set(livroMap)
   .addOnSuccessListener { onSucess() }
   .addOnFailureListener { erro -> onFailure(erro) }
 }

 fun listarLivros(
  onResult: (List<Map<String, Any>>) -> Unit,
  onFailure: (Exception) -> Unit
 ) {
  val userId = FirebaseAuth.getInstance().currentUser?.uid
  if (userId == null) {
   onFailure(Exception("Usuário não autenticado"))
   return
  }

  db.collection("Livros")
   .whereEqualTo("userId", userId) // 🔥 pega só os livros do usuário logado
   .get()
   .addOnSuccessListener { result ->
    val lista = result.mapNotNull { it.data }
    onResult(lista)
   }
   .addOnFailureListener { e -> onFailure(e) }
 }

 fun deletarLivro(titulo: String) {
  db.collection("Livros")
   .document(titulo)
   .delete()
 }
}
