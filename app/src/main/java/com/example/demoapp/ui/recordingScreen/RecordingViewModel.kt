package com.example.demoapp.ui.recordingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.data.model.User
import com.example.demoapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _recognizedText = MutableStateFlow("Enter or Record User Details")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()


    fun onInputTextChanged(newText: String) {
        _recognizedText.value = newText
    }

    fun saveUser(useName : String) {
        viewModelScope.launch {
            if (recognizedText.value.isEmpty()){
                _snackbarMessage.tryEmit("User name could not be blank...")
            }else {
                val newUser = User(
                    firstName = recognizedText.value,
                )

                val result = userRepository.addUser(newUser)
                result.onSuccess { user ->
                    print("User Added...!")
                    _snackbarMessage.tryEmit("User Added Successfully...!!!${user.id}")

                }.onFailure { error ->
                    print(error.localizedMessage ?: "An unknown error occurred")

                }
            }
        }
    }
}