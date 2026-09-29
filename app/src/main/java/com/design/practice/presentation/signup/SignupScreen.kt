package com.design.practice.presentation.signup

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {

      var email by remember { mutableStateOf("") }
       var pswrd by remember { mutableStateOf("") }

      var isVisible by remember { mutableStateOf(false
      ) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(modifier = Modifier.fillMaxWidth() , onValueChange = { it ->

             email = it
        },
            value = email,
            placeholder = {
                Text(text = "Please Enter Email")
            }

        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(modifier = Modifier.fillMaxWidth() , onValueChange = { it ->

            pswrd = it
        },
            value = pswrd,
            placeholder = {
                Text(text = "Please Enter Password")
            },
            singleLine = true,



        )

        Button(onClick = {


        }) {

            Text(text = "SignUp")
        }

    }
}

fun validation(email : String , pswrd : String ){

    

}