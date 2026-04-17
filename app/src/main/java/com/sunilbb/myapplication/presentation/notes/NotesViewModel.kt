package com.sunilbb.myapplication.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val notesKey = "notes_key"

    private val _state = MutableStateFlow(NotesContract.UIState())
    val state = _state.asStateFlow()

    init {
        // Restore from SavedStateHandle if available
        savedStateHandle.get<List<String>>(notesKey)?.let { savedNotes ->
            _state.update { it.copy(notes = savedNotes) }
        }
    }

    fun handleIntent(intent: NotesContract.Intent) {
        when (intent) {
            is NotesContract.Intent.AddNote -> addNote(intent.note)
            is NotesContract.Intent.DeleteNote -> deleteNote(intent.index)
            is NotesContract.Intent.EditNote -> editNote(intent.index, intent.newText)
        }
    }

    private fun addNote(note: String) {
        if (note.isBlank()) return
        _state.update { currentState ->
            val newNotes = currentState.notes + note
            savedStateHandle[notesKey] = newNotes
            currentState.copy(notes = newNotes)
        }
    }

    private fun deleteNote(index: Int) {
        _state.update { currentState ->
            val newNotes = currentState.notes.toMutableList().apply {
                if (index in indices) removeAt(index)
            }
            savedStateHandle[notesKey] = newNotes
            currentState.copy(notes = newNotes)
        }
    }

    private fun editNote(index: Int, newText: String) {
        if (newText.isBlank()) return
        _state.update { currentState ->
            val newNotes = currentState.notes.toMutableList().apply {
                if (index in indices) this[index] = newText
            }
            savedStateHandle[notesKey] = newNotes
            currentState.copy(notes = newNotes)
        }
    }
}

class NotesContract {
    data class UIState(
        val isLoading: Boolean = false,
        val notes: List<String> = emptyList(),
        val error: String? = null
    )

    sealed class Intent {
        data class AddNote(val note: String) : Intent()
        data class DeleteNote(val index: Int) : Intent()
        data class EditNote(val index: Int, val newText: String) : Intent()
    }
}
