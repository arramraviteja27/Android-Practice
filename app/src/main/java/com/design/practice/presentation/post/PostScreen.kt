package com.design.practice.presentation.post

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Post

@Composable
fun PostScreen(

    viewModel : PostViewModel = hiltViewModel(),

    onClick : (Post) -> Unit
) {
    val context = LocalContext.current
     when(val state = viewModel.postState){

         ApiState.Idle -> {


         }

         ApiState.Loading -> {

              Box(

                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
              ){

                  CircularProgressIndicator()
              }
         }

        is ApiState.Success -> {

            LazyColumn {

                items(state.data){ post ->

                    PostItem(post , onClick = { it ->


                         Toast.makeText(context,it.title,Toast.LENGTH_SHORT).show()


                    },onLongClick = { it ->



                    })
                }
            }


         }

         is ApiState.Failure -> {


         }
     }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    TextField(
        value = email,

        onValueChange = {
            email = it
        },
        placeholder = {
            Text(text = "Email")
        },
        label = {
            Text(text = "Email")
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = password,
        onValueChange = {

            password = it
        },
        placeholder = {
            Text(text = "Password")
        },
        label = {
            Text(text = "Password")
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    ShowButton(label = "Login", onClick = { it ->

        Toast.makeText(context,it,Toast.LENGTH_SHORT).show()

    }

    )


}

fun validation(email : String , pswrd : String) : Boolean{

    if(email.isEmpty() || pswrd.isEmpty()){

        return false
    }

    return true
}
@Composable
fun ShowButton(label : String , onClick :(String) -> Unit){

    Button(onClick = {

        onClick("Login")
    } ) {

        Text(text = label)
    }
}

@Composable
fun PostItem(

    post : Post ,
    onClick : (Post) -> Unit,
    onLongClick : (Post) -> Unit
){

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

                onClick(post)
            }
    ) {


        Column(
            modifier = Modifier.padding(8.dp).clickable(onClick = {

                onLongClick(post)
            })
        ) {

            Text(

                text = post.title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(post.body)
        }
    }

}