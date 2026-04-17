package com.sunilbb.myapplication.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CountryDetailScreen(code: String,viewModel: CountryViewModel,navcontroller: NavController) {

    var showDialog by rememberSaveable() { mutableStateOf(false) }
    var value by rememberSaveable() { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
          Text("Country Detail Screen")

            val country = viewModel.state.collectAsState().value.countries.find { it.code == code }
            if (country != null) {
                Text(text = "Name: ${country.name}")
                Text(text = "Code: ${country.code}")
                //Text(text = "Emoji: ${country.emoji}")
                Button(onClick = {
                    navcontroller.navigate("notes")
                }) { Text("Add Notes")}
                Text(text = "Alert Value: = $value", fontSize = 30.sp, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
                Button(onClick = {
                   showDialog = true
                }
                ) {
                    Text("Show Alert with price")
                }
            }
        }
    }

    if(showDialog){
   showAlert(value, onDismiss = {
       showDialog = false
   }, onCOnfirm = {
       value = it
   })

        BackHandler {
            navcontroller.popBackStack()
        }
    }
}


@Composable
fun showAlert(intialValue:Int,onDismiss: () -> Unit,onCOnfirm: (Int) -> Unit){
    var tempValue by rememberSaveable { mutableIntStateOf(intialValue) }
    var counter = tempValue

 AlertDialog(onDismissRequest =  onDismiss,
    confirmButton = {
        Button(onClick = {
            onCOnfirm(counter)
            onDismiss()
        }) {
            Text("Confirm")
        }

    },
    title = {
        Text("Set Counter Value")
    },
    text = {
        Column {
            TextField(value = counter.toString(), onValueChange = {
                tempValue = it.toIntOrNull() ?: 0
            })

            Row (horizontalArrangement = Arrangement.SpaceEvenly){
              Button(onClick = {
                  tempValue++
              }) {
                  Text("+")
              }
                Button(onClick = {
                 tempValue--
                }) {
                    Text("-")
                }
            }


        }
    },

    dismissButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
            })

}

