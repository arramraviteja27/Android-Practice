package com.design.practice.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.SendToMobile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.design.practice.domain.model.ChatMessage

@Composable
fun ChatPage(
    modifier: Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {

   val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            items(
                items = uiState.messages,
                key = {it.id}
            ){  message ->

                ChatMessageItem(message = message)


            }

            if(uiState.isLoading){

                item {

                    TypingIndicator()
                }
            }
        }

        ChatInput(

            text = uiState.inputText,
            isLoading = uiState.isLoading,
            onTextChanged = viewModel::onInputChanged,
            onSendClick = viewModel::sendMessage
        )
    }
    LazyColumn {
        items(
            items = uiState.messages,
            key = {it.id}
        ){

        }
    }
    
}

@Composable
fun LoginScreen(){

     var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val list = listOf<String>("Sports","Entertainment","Games","Events")

    LazyColumn(
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    ) {

        items(
            items = list
        ){ item ->

            ShowItem(str = item , itemClick = { it ->




            })
        }
    }

    TextField(
        value = email,
        placeholder = {
            Text(text = "Email Id")
        },
        onValueChange = {

            email = it
        },

    )

    Spacer(modifier = Modifier.height(10.dp))

    TextField(
        value = password,
        onValueChange = {
            password = it
        },
        placeholder = {
            Text(text = "Enter Password")
        }
    )

    Spacer(modifier = Modifier.height(10.dp))

     showButton(label = "Submit" , onClick = {


     })
}

@Composable
fun ShowItem(str : String , itemClick : (String) -> Unit){

    Text(
        text = str,
        modifier = Modifier.padding(10.dp).clickable{

             itemClick(str)
        },
        textAlign = TextAlign.Center
    )
}

@Composable
fun showButton(label : String , onClick : () -> Unit){

      Button(
          onClick = onClick,
          modifier = Modifier.padding(10.dp)
      ) {

          Text(text = "Submit")
      }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {

            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun TypingIndicator() {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = "Gemini is typing..."
        )
    }
}

@Composable
fun ChatInput(
    text: String,
    isLoading: Boolean,
    onTextChanged: (String) -> Unit,
    onSendClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Ask something...")
            },
            maxLines = 4
        )


        Spacer(
            modifier = Modifier.width(8.dp)
        )


        IconButton(
            onClick = onSendClick,
            enabled = text.isNotBlank() && !isLoading
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = "Send"
            )
        }
    }
}

@Composable
fun MessageInput(onMessageSend : (String) -> Unit) {

    var message by remember {

        mutableStateOf("")
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {

                message = it
            }
        )

        IconButton(
            onClick = {

                onMessageSend(message)
                message = ""
            }
        ) {

            Icon(imageVector = Icons.Filled.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun AppHeader() {

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ){

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Easy Bot",
            color = Color.White,
            fontSize = 22.sp
        )
    }

}