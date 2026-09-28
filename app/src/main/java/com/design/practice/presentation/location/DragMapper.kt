package com.design.practice.presentation.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun LocationPickerScreen(){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }

    var selectedLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    var address by remember {
        mutableStateOf("Getting current location...")
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

        snapshotFlow{

            cameraPositionState.isMoving
        }.collect { isMoving ->

            if (!isMoving) {

                val location =
                    cameraPositionState.position.target

                selectedLocation = location

                address = "Fetching address..."

                address = getAddressFromLocation(
                    context = context,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }
        }



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

    // Step 2: Get current location
    LaunchedEffect(hasLocationPermission) {

        if (hasLocationPermission) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    location?.let {

                        selectedLocation = LatLng(
                            it.latitude,
                            it.longitude
                        )
                    }
                }
        }
    }

    LaunchedEffect(
        selectedLocation,
        mapLoaded
    ) {

        if(selectedLocation != null && mapLoaded){

            val location = selectedLocation!!

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    16f
                )
            )

            address = getAddressFromLocation(
                context = context,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }

    LaunchedEffect(Unit) {

        snapshotFlow {
            cameraPositionState.isMoving
        }
            .distinctUntilChanged()
            .filter { isMoving ->
                !isMoving
            }
            .collect {

                val location =
                    cameraPositionState.position.target

                selectedLocation = location

                address = getAddressFromLocation(
                    context = context,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }
    }

    Box(

        modifier = Modifier.fillMaxSize()
    ){

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
            },



        )

          Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Selected Location",

              modifier = Modifier.align(
                  Alignment.Center
              ).size(50.dp)
          )

        Card(

            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Selected Address",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = address
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Lat: ${selectedLocation?.latitude}"
                )

                Text(
                    text = "Lng: ${selectedLocation?.longitude}"
                )
            }

        }



    }
}