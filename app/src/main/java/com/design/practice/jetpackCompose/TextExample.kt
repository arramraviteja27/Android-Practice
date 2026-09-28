package com.design.practice.jetpackCompose

import com.design.practice.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TextExample(modifier: Modifier = Modifier) {

    var name by rememberSaveable { mutableStateOf("") }

    var email by rememberSaveable { mutableStateOf("") }

    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var gender by rememberSaveable { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")

    var expanded by rememberSaveable { mutableStateOf(false) }
    val countries = listOf("India", "USA", "Japan", "China")
    var selectedCountry by rememberSaveable { mutableStateOf("") }

    var dateOfBirth by rememberSaveable { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()


    var termsAccepted by rememberSaveable() { mutableStateOf(false) }


    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }
    var countryError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var termsError by remember { mutableStateOf(false) }


    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)

        )

      OutlinedTextField(

          value = name,
          onValueChange = {

              name = it ;
              nameError = false
          },
          label = { Text("Email")},
          modifier = Modifier.fillMaxWidth(),
          isError = nameError,
          supportingText = {

              if(nameError){

                  Text(
                      text = "Name is Required",
                      color = MaterialTheme.colorScheme.error
                  )
              }
          }

      )

        //Email Field

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text(
                        text = "Email is required",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        )

        // password Field

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {

                val img =
                    if (passwordVisible) painterResource(R.drawable.invisible) else painterResource(
                        R.drawable.passwordvisible
                    )
                IconButton(

                    onClick = { passwordVisible = !passwordVisible }
                ) {


                    Icon(
                        painter = img,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },

            isError = passwordError,
            supportingText = {
                if (passwordError) {
                    Text(
                        text = "Password is required",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }


        )


        Text("Gender", style = MaterialTheme.typography.titleMedium)

        Column(

            Modifier.selectableGroup()
        ) {


            genderOptions.forEach { text ->

                Row(

                    Modifier.fillMaxWidth()
                        .height(48.dp)
                        .selectable(

                            selected = (text == gender),
                            onClick = {

                                gender = text
                                genderError = false
                            },
                            role = Role.RadioButton

                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (text == gender), onClick = null)

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )


                }

            }


        }

        if (genderError) {

            Text(
                text = "Gender is required",
                color = MaterialTheme.colorScheme.error
            )
        }


        ExposedDropdownMenuBox(

            expanded = !expanded,
            onExpandedChange = { expanded = !expanded },
        ) {


            OutlinedTextField(

                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                label = { Text("Country") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                isError = countryError,
                supportingText = {
                    if (countryError) {
                        Text(
                            text = "Country is required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

            )

            ExposedDropdownMenu(

                expanded = expanded,onDismissRequest = { expanded = false },
            ) {


                countries.forEach { item ->

                    DropdownMenuItem(

                        text = { Text(text = item) },
                        onClick = {
                            selectedCountry = item
                            expanded = false
                            countryError = false
                        }

                    )


                }
            }
        }


        OutlinedTextField(

            value = dateOfBirth,
            onValueChange = {
                dateOfBirth = it
                dateError = false
            },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = { showDatePicker.value = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "Calendar"
                    )
                }
            },
            isError = dateError,
            supportingText = {
                if (dateError) {
                    Text(
                        text = "Date of Birth is required",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        if(showDatePicker.value){

            DatePickerDialog(

                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {

                    TextButton(

                        onClick = {

                            val date = datePickerState.selectedDateMillis

                            if(date != null){

                                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                                showDatePicker.value = false
                            }
                        }
                    ) {

                        Text("OK")
                    }
                }
            ) {


                DatePicker(state = datePickerState)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){

            Checkbox(

                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it ; termsError = false }
            )
            Text("I accept the terms and conditions")
        }

        if(termsError){

               Text("You must Accept the terms and conditions",
                   color = MaterialTheme.colorScheme.error,
                   style = MaterialTheme.typography.bodySmall,
               )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = {

                nameError = name.isEmpty()
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                passwordError = password.length < 6
                genderError = gender.isEmpty()
                countryError = selectedCountry.isEmpty()
                dateError = dateOfBirth.isEmpty()
                termsError = !termsAccepted

                if(!nameError && !emailError && !passwordError && !genderError && !countryError && !dateError && !termsError){



                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Sign UP")
        }

    }


}











