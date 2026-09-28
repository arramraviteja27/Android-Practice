package com.design.practice.presentation.Details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Restaurant

@Composable
fun RestaurantDetailsScreen(
    placeId: String,
    onBackClick: () -> Unit,
    viewModel: RestaurantDetailsViewModel = hiltViewModel()
) {

    val state by viewModel.restaurantState
        .collectAsStateWithLifecycle()

    LaunchedEffect(placeId) {
        viewModel.getRestaurantDetails(placeId)
    }

    when (val result = state) {

        ApiState.Idle -> {
        }

        ApiState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        is ApiState.Success -> {

            RestaurantDetailsContent(
                restaurant = result.data,
                onBackClick = onBackClick
            )
        }

        is ApiState.Failure -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = result.message
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsContent(
    restaurant: Restaurant,
    onBackClick: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = restaurant.name
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {

                AsyncImage(
                    model = restaurant.photoUri,
                    contentDescription =
                        restaurant.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale =
                        ContentScale.Crop
                )
            }

            item {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = restaurant.name,
                        style = MaterialTheme
                            .typography
                            .headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    // Rating

                    restaurant.rating?.let { rating ->

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Star,
                                contentDescription =
                                    "Rating",
                                tint = Color(0xFFFFB300)
                            )

                            Spacer(
                                modifier = Modifier.width(4.dp)
                            )

                            Text(
                                text = rating.toString(),
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // Address

                    restaurant.address?.let {

                        RestaurantDetailRow(
                            icon = Icons.Default.LocationOn,
                            title = "Address",
                            value = it
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // Open status

                    restaurant.isOpen?.let { isOpen ->

                        RestaurantDetailRow(
                            icon = Icons.Default.Schedule,
                            title = "Status",
                            value = if (isOpen) {
                                "Open now"
                            } else {
                                "Closed"
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    // Price

                    restaurant.priceLevel?.let {

                        RestaurantDetailRow(
                            icon = Icons.Default
                                .CurrencyRupee,
                            title = "Price",
                            value = "₹".repeat(
                                it.coerceAtLeast(1)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantDetailRow(
    icon: ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.Top
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme
                .colorScheme
                .primary
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = title,
                style = MaterialTheme
                    .typography
                    .labelMedium,
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value,
                style = MaterialTheme
                    .typography
                    .bodyLarge
            )
        }
    }
}