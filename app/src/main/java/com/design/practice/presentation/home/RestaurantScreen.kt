package com.design.practice.presentation.home

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Restaurant
import java.util.jar.Manifest


@Composable
fun NotificationPermissionScreen(

) {

    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        if(isGranted){

            Toast.makeText(context,"Notification Permission granted",Toast.LENGTH_SHORT).show()
        }else{

            Toast.makeText(context,"Notification Permission Denied",Toast.LENGTH_SHORT).show()
        }



    }

    Column(

        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center


    ) {

        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    notificationPermissionLauncher.launch(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                } else {

                    Toast.makeText(
                        context,
                        "Notification permission is automatically allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {

            Text("Allow Notification")
        }
    }
}

@Composable
fun RestaurantScreen(
    viewModel: RestaurantViewModel = hiltViewModel(),
    onRestaurantClick: (String) -> Unit
) {


    val restaurantState by viewModel.restaurantState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {

        viewModel.getNearbyRestaurants(
            latitude = 17.3850,
            longitude = 78.4867
        )
    }

    when (val state = restaurantState) {

        ApiState.Idle -> {
            // Initial state
        }

        ApiState.Loading -> {
            CircularProgressIndicator()
        }

        is ApiState.Success -> {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    items = state.data,
                    key = { it.placeId }
                ) { restaurant ->

                    RestaurantItem(
                        restaurant = restaurant,
                        onClick = {
                            onRestaurantClick(
                                restaurant.placeId
                            )
                        }
                    )
                }
            }
        }

        is ApiState.Failure -> {

            Text(
                text = state.message
            )
        }
    }
}



@Composable
fun RestaurantItem(

    restaurant: Restaurant,
    onClick: () -> Unit
) {


    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {

                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

       Column {

           AsyncImage(
               model = restaurant.photoUri,
               contentDescription = restaurant.name,
               modifier = Modifier
                   .fillMaxWidth()
                   .height(190.dp)
                   .clip(
                       RoundedCornerShape(
                           topStart = 20.dp,
                           topEnd = 20.dp
                       )
                   ),
               contentScale = ContentScale.Crop
           )

           Column(
               modifier = Modifier.padding(16.dp)
           ) {

               Text(
                   text = restaurant.name,
                   style = MaterialTheme
                       .typography
                       .titleLarge,
                   fontWeight = FontWeight.Bold
               )

               Spacer(
                   modifier = Modifier.height(8.dp)
               )

               Row {

                   restaurant.rating?.let {

                       Text(
                           text = "⭐ $it"
                       )
                   }

                   Spacer(
                       modifier = Modifier.width(12.dp)
                   )

                   restaurant.priceLevel?.let {

                       Text(
                           text = "₹".repeat(
                               it.coerceAtLeast(1)
                           )
                       )
                   }
               }

               Spacer(
                   modifier = Modifier.height(8.dp)
               )

               restaurant.address?.let {

                   Text(
                       text = it,
                       maxLines = 2,
                       overflow = TextOverflow.Ellipsis,
                       color = MaterialTheme
                           .colorScheme
                           .onSurfaceVariant
                   )
               }
               Spacer(
                   modifier = Modifier.height(8.dp)
               )

               restaurant.isOpen?.let { isOpen ->

                   Text(
                       text = if (isOpen) {
                           "Open"
                       } else {
                           "Closed"
                       },
                       color = if (isOpen) {
                           Color(0xFF2E7D32)
                       } else {
                           Color(0xFFC62828)
                       },
                       fontWeight = FontWeight.SemiBold
                   )
               }
           }
       }
    }

}


private fun getPriceLevelText(
    priceLevel: Int
): String {

    return when (priceLevel) {
        0 -> "Free"
        1 -> "₹"
        2 -> "₹₹"
        3 -> "₹₹₹"
        4 -> "₹₹₹₹"
        else -> ""
    }
}