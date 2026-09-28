package com.design.practice.presentation.sharedPreference

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

// Crypto Manager using Android KeyStore
class CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(text: String): String {
        if (text.isEmpty()) return ""
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
        val encrypted = cipher.doFinal(text.toByteArray())
        val combined = cipher.iv + encrypted
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedBase64: String): String {
        if (encryptedBase64.isEmpty()) return ""
        return try {
            val combined = Base64.decode(encryptedBase64, Base64.DEFAULT)
            val iv = combined.copyOfRange(0, 12)
            val ciphertext = combined.copyOfRange(12, combined.size)

            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))
            }
            String(cipher.doFinal(ciphertext))
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val ALIAS = "my_secure_prefs_key"
    }
}

// DataStore Extension
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

// DataStore Manager
class UserPreferences(private val context: Context) {
    private val cryptoManager = CryptoManager()

    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
        val DOB_KEY = stringPreferencesKey("dob")
        val GENDER_KEY = stringPreferencesKey("gender")
    }

    suspend fun saveUser(username: String, dob: String, gender: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = cryptoManager.encrypt(username)
            preferences[DOB_KEY] = cryptoManager.encrypt(dob)
            preferences[GENDER_KEY] = cryptoManager.encrypt(gender)
        }
    }

    suspend fun getUser(): Flow<Triple<String, String, String>> {
        return context.dataStore.data.map { preferences ->
            val encName = preferences[USERNAME_KEY] ?: ""
            val encDob = preferences[DOB_KEY] ?: ""
            val encGender = preferences[GENDER_KEY] ?: ""

            Triple(
                cryptoManager.decrypt(encName),
                cryptoManager.decrypt(encDob),
                cryptoManager.decrypt(encGender)
            )
        }
    }

    suspend fun deleteUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStoreScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStoreManager = remember { UserPreferences(context) }

    var username by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")

    // Date Picker State
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("DataStore Practice", style = MaterialTheme.typography.headlineMedium)

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        // DOB Field
        OutlinedTextField(
            value = dob,
            onValueChange = { },
            label = { Text("Date of Birth") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedDate = datePickerState.selectedDateMillis
                        if (selectedDate != null) {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            dob = sdf.format(Date(selectedDate))
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Gender Group
        Text("Gender", style = MaterialTheme.typography.titleMedium)
        Column(Modifier.selectableGroup()) {
            genderOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (text == gender),
                            onClick = { gender = text },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (text == gender), onClick = null)
                    Text(text, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        dataStoreManager.saveUser(username, dob, gender)
                        username = ""
                        dob = ""
                        gender = ""
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Button(
                onClick = {
                    scope.launch {
                        dataStoreManager.getUser().first().let { (savedUser, savedDob, savedGender) ->
                            username = savedUser
                            dob = savedDob
                            gender = savedGender
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Show")
            }
        }

        Button(
            onClick = {
                scope.launch {
                    dataStoreManager.deleteUser()
                    username = ""
                    dob = ""
                    gender = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Delete All")
        }
    }
}
