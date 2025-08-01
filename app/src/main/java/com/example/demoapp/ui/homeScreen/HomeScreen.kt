package com.example.demoapp.ui.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R
import androidx.compose.runtime.collectAsState
import com.example.demoapp.data.model.User
import com.example.demoapp.api.UserListUiState
import androidx.compose.runtime.getValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    onNavigateToRecording: () -> Unit
) {
// Collect the UI state from the ViewModel
    val uiState by homeViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice User List") },
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.
                padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(weight = 0.1f, fill = true)
                        .padding(innerPadding), // Apply the padding from Scaffold
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (val state = uiState) {
                        is UserListUiState.Loading -> {
                            println("Loading...")
                        }
                        is UserListUiState.Success -> {
                            // Inside this block, `state` is smart-casted to UserListUiState.Success.
                            // This is why you can now safely access `state.users`.
                            val userlist : List<User> =   state.users
                            items(items = userlist, key = { it }) { item ->
                                GetUserList(name = item.firstName,
                                    modifier = Modifier.padding(innerPadding),
                                )
                            }
                        }
                        is UserListUiState.Error -> {
                           println("Eroor")
                        }
                    }

                }

                Button(
                    onClick = {
                        println("On Click")
                        onNavigateToRecording()
                    }, modifier = Modifier.fillMaxWidth()
                        .semantics { contentDescription = "Record new note" }
                ) {
                    Text(text = "Add New User")

                }

            }
        }

    }

}

@Composable
fun GetUserList(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_user), "", modifier = Modifier.padding(3.dp)
            )
            Text(
                text = name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier
            )
        }
    }
}
