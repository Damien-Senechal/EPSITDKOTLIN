@file:OptIn(ExperimentalLayoutApi::class)

package com.epsi.cinedirect.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.epsi.cinedirect.AdminState
import com.epsi.cinedirect.MainViewModel
import com.epsi.cinedirect.models.MovieContract

@Composable
fun AdminBoard(modifier: Modifier = Modifier, state: AdminState, viewModel: MainViewModel) {
    BackHandler(enabled = true) {

    }
    AdminBoardContent(
        modifier, state,
        onCreateCinema = viewModel::onCreateCinemaHall,
        onDeleteCinema = viewModel::onDeleteCinemaHall,
        onCreateUser = viewModel::onCreateUser,
        onDeleteUser = viewModel::onDeleteUser,
        onCreateMovie = viewModel::onCreateMovie,
        onSelectCinema = viewModel::onSelectCinema,
        onDeleteMovie = viewModel::onDeleteMovie,
        onCreateShowtime = viewModel::onCreateShowtime
    )
}

@Composable
private fun AdminBoardContent(
    modifier: Modifier = Modifier,
    state: AdminState,
    onCreateCinema: (String) -> Unit,
    onDeleteCinema: (String) -> Unit,
    onCreateUser: (String) -> Unit,
    onDeleteUser: (String) -> Unit,
    onCreateMovie: (String, String) -> Unit,
    onCreateShowtime: (String, Int, Int) -> Unit,
    onDeleteMovie: (String, String) -> Unit,
    onSelectCinema: (String) -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .padding(32.dp)
    ) {
        Text(
            "Administration",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(8.dp))
        GestionElement(
            modifier = Modifier.fillMaxWidth(),
            list = state.customers,
            label = "Utilisateurs",
            hint = "Nom",
            onCreate = onCreateUser,
            onDelete = onDeleteUser
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        GestionElement(
            modifier = Modifier.fillMaxWidth(),
            list = state.cinemaHalls,
            label = "Cinémas",
            hint = "Ville du cinéma",
            onCreate = onCreateCinema,
            onDelete = onDeleteCinema
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        MoviesView(
            modifier = Modifier.fillMaxWidth(),
            onCreateMovie = onCreateMovie,
            onCreateShowtime = onCreateShowtime,
            onDeleteMovie = onDeleteMovie,
            movies = state.moviesOnSelectedCinema,
            cinemas = state.cinemaHalls,
            selectedCinema = state.currentCinema,
            onSelectCinema = onSelectCinema
        )
        Spacer(Modifier.height(8.dp))
    }
}


@Composable
private fun GestionElement(
    modifier: Modifier = Modifier,
    label: String,
    hint: String,
    list: List<Any>,
    onCreate: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    var newElementName by remember(list) { mutableStateOf("") }
    Column(modifier) {
        Spacer(Modifier.height(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier
                        .weight(1f),
                value = newElementName,
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                onValueChange = { newElementName = it },
                placeholder = { Text(hint) })
            Spacer(Modifier.width(16.dp))
            Button(shape = RectangleShape,
                   enabled = newElementName.isNotEmpty(),
                   onClick = { onCreate(newElementName) }) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Créer",
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            list.forEach { element ->
                GestionElementItem(element, onDelete)
            }
        }
    }

}

@Composable
private fun GestionElementItem(element: Any, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp), text = element.toString(),
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        IconButton({ onDelete(element.toString()) }) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
        }
    }
}

@Composable
private fun Showtime(
    modifier: Modifier = Modifier,
    movie: MovieContract,
    onCreateShowtime: (Int, Int) -> Unit,
) {
    val capacityChoice = listOf(5, 10, 30)
    val hourChoice = listOf(14, 18, 20, 22)
    var selectedHour by remember(movie) { mutableIntStateOf(hourChoice.first()) }
    var selectedCapacity by remember(movie) { mutableIntStateOf(capacityChoice.first()) }

    Column(modifier) {
        Spacer(Modifier.height(8.dp))
        Text("Heure de la séance", fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            hourChoice.forEach { hour ->
                FilterChip(
                    label = { Text("${hour}h") }, selected = selectedHour == hour,
                    onClick = { selectedHour = hour })
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Capacité de la salle", fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            capacityChoice.forEach { capacity ->
                FilterChip(
                    label = { Text("$capacity places") },
                    selected = selectedCapacity == capacity,
                    onClick = { selectedCapacity = capacity })
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                enabled = !movie.showtimes().any { it.startHour() == selectedHour },
                onClick = { onCreateShowtime(selectedHour, selectedCapacity) }) {
                Text(
                    text = "Ajouter",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MoviesView(
    modifier: Modifier = Modifier,
    cinemas: List<String>,
    movies: List<MovieContract>,
    selectedCinema: String,
    onSelectCinema: (String) -> Unit,
    onCreateMovie: (String, String) -> Unit,
    onDeleteMovie: (String, String) -> Unit,
    onCreateShowtime: (String, Int, Int) -> Unit,
) {
    var newMovieName by remember(movies, selectedCinema) { mutableStateOf("") }
    var movieToAddShowOn by remember(movies, selectedCinema) { mutableStateOf("") }

    Column(modifier) {
        Spacer(Modifier.height(8.dp))
        Text(
            "Films",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))
        if (cinemas.isEmpty()) {
            Text("Aucune salle de cinéma active")
        } else {
            Spacer(Modifier.height(4.dp))
            Text("Lieu de projection", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                cinemas.forEach { cinemaHall ->
                    FilterChip(
                        label = { Text(cinemaHall) }, selected = selectedCinema == cinemaHall,
                        onClick = { onSelectCinema(cinemaHall) })
                }
            }

            if (selectedCinema.isNotEmpty()) {

                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                        value = newMovieName,
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = { newMovieName = it },
                        placeholder = { Text("Nom du film") })
                    Spacer(Modifier.width(16.dp))
                    OutlinedButton(enabled = newMovieName.isNotEmpty(),
                                   shape = RectangleShape,
                                   onClick = { onCreateMovie(newMovieName, selectedCinema) }) {
                        Text(
                            modifier = Modifier,
                            text = "Ajouter",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // spacer
            if (movies.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text("Films existants", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                movies.forEach { movie ->

                    Card(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        colors = CardDefaults
                                .cardColors()
                                .copy(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 0.5f
                                    )
                                )
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f),
                                    maxLines = 2,
                                    text = "🎬 ${movie.title()}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                IconButton({
                                               onDeleteMovie(
                                                   movie.title(), selectedCinema
                                               )
                                           }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = null)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                            ) {
                                if (movieToAddShowOn == movie.title()) {
                                    Showtime(
                                        modifier = Modifier.fillMaxWidth(),
                                        onCreateShowtime = { hour, capacity ->
                                            onCreateShowtime(
                                                movie.title(), hour, capacity
                                            )
                                            movieToAddShowOn = ""
                                        },
                                        movie = movie
                                    )
                                } else {

                                    TextButton(modifier = Modifier.fillMaxWidth(),
                                               onClick = {
                                                   movieToAddShowOn = movie.title()
                                               }) {
                                        Text(
                                            text = "Ajouter une séance"
                                        )
                                    }

                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                movie.showtimes().forEachIndexed { index, item ->
                                    Text(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp),
                                        text = "- Séance #${index + 1} à ${item.startHour()}h",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }


    }

}