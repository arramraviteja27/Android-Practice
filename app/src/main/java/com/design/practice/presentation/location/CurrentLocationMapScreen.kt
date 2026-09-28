package com.design.practice.presentation.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

@Composable
fun CurrentLocationMapScreen() {

    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }

    var currentLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    var address by remember {
        mutableStateOf("")
    }

    var mapLoaded by remember {
        mutableStateOf(false)
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraPositionState = rememberCameraPositionState()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            hasLocationPermission =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }


    LaunchedEffect(Unit) {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED


        hasLocationPermission =
            fineLocationGranted || coarseLocationGranted


        if (!hasLocationPermission) {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    LaunchedEffect(hasLocationPermission) {

        if (hasLocationPermission) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    location?.let {

                        currentLocation = LatLng(
                            it.latitude,
                            it.longitude
                        )
                    }
                }
        }
    }

    LaunchedEffect(currentLocation) {

        currentLocation?.let { location ->

            address = getAddressFromLocation(
                context = context,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),

        cameraPositionState = cameraPositionState,

        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),

        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true
        ),

        onMapLoaded = {
            mapLoaded = true
        }
    ) {

        currentLocation?.let { location ->

            Marker(
                state = MarkerState(
                    position = location
                ),
                title = address
            )
        }
    }


    LaunchedEffect(currentLocation, mapLoaded) {

        if (currentLocation != null && mapLoaded) {

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    currentLocation!!,
                    18f
                )
            )
        }
    }
}

suspend fun getAddressFromLocation(
    context: Context,
    latitude: Double,
    longitude: Double
): String = withContext(Dispatchers.IO) {

    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            suspendCancellableCoroutine { continuation ->

                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) { addresses ->

                    val address = addresses.firstOrNull()

                    continuation.resume(
                        address?.getAddressLine(0)
                            ?: "Address not found"
                    )
                }
            }

        } else {

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            )

            addresses?.firstOrNull()
                ?.getAddressLine(0)
                ?: "Address not found"
        }

    } catch (e: Exception) {
        "Unable to get address"
    }
}