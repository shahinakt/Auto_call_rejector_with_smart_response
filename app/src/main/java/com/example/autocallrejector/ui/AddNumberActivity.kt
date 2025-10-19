package com.example.autocallrejector.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autocallrejector.AutoCallRejectorApplication

class AddNumberActivity : ComponentActivity() {

    private val viewModel: AddNumberViewModel by viewModels {
        AddNumberViewModelFactory((application as AutoCallRejectorApplication).database.blockedNumberDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddNumberScreen(onAddNumber = {
                viewModel.addBlockedNumber(it)
                finish()
            })
        }
    }
}

@Composable
fun AddNumberScreen(onAddNumber: (String) -> Unit) {
    var number by remember { mutableStateOf("") }

    Scaffold {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Enter number to block") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { onAddNumber(number) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}
