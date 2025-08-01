package com.example.demoapp.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.data.repository.UserRepository
import com.example.demoapp.api.UserListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState

    init {
        getUsers()
    }

    fun getUsers() {

        viewModelScope.launch {
            try {
                _uiState.value = UserListUiState.Loading
                val result = userRepository.getUsers()
                result.onSuccess { users ->
                    _uiState.value = UserListUiState.Success(users)
                }.onFailure { error ->
                    _uiState.value = UserListUiState.Error(error.localizedMessage ?: "An unknown error occurred")
                }

            } catch (e: Exception) {
                println(e.printStackTrace())
//                Result.failure(e)
            }
        }
    }

}