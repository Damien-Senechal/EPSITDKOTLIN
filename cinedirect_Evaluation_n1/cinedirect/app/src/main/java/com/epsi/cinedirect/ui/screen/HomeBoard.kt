@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.epsi.cinedirect.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.epsi.cinedirect.HomeMovie
import com.epsi.cinedirect.HomeShow
import com.epsi.cinedirect.HomeState
import com.epsi.cinedirect.MainViewModel
import com.epsi.cinedirect.R

@Composable
fun HomeBoard(modifier: Modifier = Modifier, state: HomeState, viewModel: MainViewModel) {
    if (state.customers.isEmpty()) {
        Column(
            modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Aucun utilisateur.\nCréez un utilisateur dans la partie Admin avant de continuer",
                textAlign = TextAlign.Center,
            )
        }
    } else {
        HomeBoardContent(modifier, state, viewModel::buy)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeBoardContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    buy: (HomeShow, HomeMovie, String) -> Unit,
) {
    var selectedCinema by remember(state.cinemaHalls) {
        mutableStateOf(
            state.cinemaHalls.firstOrNull() ?: ""
        )
    }
    var selectedUser by remember(state.customers) {
        mutableStateOf(
            state.customers.firstOrNull() ?: ""
        )
    }

    Column(Modifier.fillMaxSize()) {
        Text(modifier = Modifier.padding(4.dp), text = "Réservation pour", fontWeight = FontWeight.Bold)

        FlowRow(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.customers.forEach { user ->
                FilterChip(
                    label = { Text(user) }, selected = selectedUser == user,
                    onClick = { selectedUser = user })
            }
        }
        HorizontalDivider()
        Text(modifier = Modifier.padding(4.dp), text = "Lieu de projection", fontWeight = FontWeight.Bold)
        if (state.cinemaHalls.isEmpty()) {
            Text(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                text = "Aucun cinéma disponible",
                textAlign = TextAlign.Center,
            )
        }

        FlowRow(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.cinemaHalls.forEach { cinemaHall ->
                FilterChip(
                    label = { Text(cinemaHall) }, selected = selectedCinema == cinemaHall,
                    onClick = { selectedCinema = cinemaHall })
            }
        }
        fun movies() = state.movies.filter {
            it.showtimes.isNotEmpty() && it.cinema == selectedCinema
        }
        HorizontalDivider()
        val scrollState = rememberScrollState()
        Column(modifier.verticalScroll(scrollState)) {
            movies().forEachIndexed { index, homeMovie ->
                MovieToBuy(
                    Modifier.fillMaxWidth(), homeMovie, index, { buy(it, homeMovie, selectedUser) })
            }
            if (state.cinemaHalls.isNotEmpty() && movies().isEmpty()) {

                Text(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    text = "Aucune séance disponible",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}

@Composable
private fun MovieToBuy(
    modifier: Modifier = Modifier,
    movie: HomeMovie,
    index: Int,
    buy: (HomeShow) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
                .padding(16.dp)
                .heightIn(min = 100.dp)
    ) {
        Box(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(204, 164, 2))
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp), text = movie.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge
                )

                Image(
                    modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(randomDrawable(index)),
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Color(
                                red = 204, green = 164, blue = 2, alpha = 120
                            )
                        )
            )
            if (movie.showtimes.isNotEmpty()) {
                movie.showtimes.forEach {
                    ShowtimeBuy(
                        modifier = Modifier.fillMaxWidth(),
                        showtime = it,
                        buy = { buy(it) }
                    )
                }
            }
        }
    }
}


@Composable
private fun ShowtimeBuy(
    modifier: Modifier = Modifier,
    showtime: HomeShow,
    buy: () -> Unit,
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Séance de ${showtime.start}h  (${showtime.seats} pl.)",
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(
            onClick = buy, enabled = showtime.seats > 0,
            colors = ButtonDefaults.textButtonColors(disabledContentColor = Color.DarkGray)
        ) {
            Text(
                text = if (showtime.seats > 0) "Réserver" else "Complet",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

@Composable
private fun randomDrawable(index: Int) = listOf(
    R.drawable.movie,
    R.drawable.movie2,
    R.drawable.movie3,
)[index.mod(3)]