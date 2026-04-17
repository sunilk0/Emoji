package com.sunilbb.myapplication.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sunilbb.myapplication.presentation.notes.NotesContract
import com.sunilbb.myapplication.presentation.notes.NotesViewModel

@Composable
fun NotesScreen(notesViewModel: NotesViewModel, navController: NavController) {
    val state by notesViewModel.state.collectAsStateWithLifecycle()
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter note") }
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = {
                if (text.isNotBlank()) {
                    notesViewModel.handleIntent(NotesContract.Intent.AddNote(text))
                    text = ""
                }
            }) {
                Text("Add Note")
            }
        }

        LazyColumn {
            itemsIndexed(state.notes) { index, note ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = note, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        notesViewModel.handleIntent(NotesContract.Intent.DeleteNote(index))
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
