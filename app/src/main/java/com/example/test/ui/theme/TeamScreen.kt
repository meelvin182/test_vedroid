package com.example.test.ui.theme

import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TeamScreen() {

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Square(modifier = Modifier.weight(1f), boxName = "ViewModel tg")
                Square(modifier = Modifier.weight(1f), boxName = "View modal weather")
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Square(modifier = Modifier.weight(1f), boxName = "View modal MVG")
                Square(modifier = Modifier.weight(1f), boxName = "View modal rout")
            }
        }
    }
}

@Composable
fun Square(modifier: Modifier = Modifier, boxName: String) {
    val showDialog = remember { mutableStateOf(false) }
    val viewModel: MessagesViewModel = viewModel()

    val messages: List<String> by MessagesViewModel().messages.collectAsState(initial = emptyList())

    Box(
        modifier = modifier
            .padding(4.dp)
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(boxName=="ViewModel tg"){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        viewModel.messages.collectAsState(initial = emptyList()).value.forEach { ItemRow(text = it) }
                    }

                }
            } else{
                Button(
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    Text(boxName)
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = { Text("Hello from $boxName") },
            confirmButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
@Composable
fun ItemRow(text: String) {
    Card(modifier = Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()) {
        Column {
            Text(text, fontSize = 10.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(1.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TeamScreen()
}