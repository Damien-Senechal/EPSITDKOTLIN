package com.epsi.cinedirect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.epsi.cinedirect.business.Montpellier
import com.epsi.cinedirect.models.MovieContract

class MainViewModel : ViewModel() {

    var adminState by mutableStateOf(AdminState())
        private set

    var homeState by mutableStateOf(HomeState())
        private set

    private fun town() = Montpellier

    init {
        refreshState()
    }

    private fun refreshHomeState() {
        homeState = HomeState(
            customers = town().customers().map { it.name }.toMutableList(),
            cinemaHalls = town().cinemaHalls().map { it.name() }.toMutableList(),
            movies = town().cinemaHalls().flatMap { cine ->
                cine.movies().map {
                    HomeMovie(
                        name = it.title(), cinema = cine.name(),
                        showtimes = it.showtimes().distinctBy { it.startHour() }.map { show ->
                            HomeShow(show.startHour(), show.seatsAvailableToBuy())
                        })
                }
            }
        )
    }

    private fun refreshState() {
        adminState = AdminState(
            cinemaHalls = town().cinemaHalls().map { it.name() },
            customers = town().customers().map { it.name },
            moviesOnSelectedCinema = town().cinemaHalls()
                    .firstOrNull { it.name() == adminState.currentCinema }?.movies().orEmpty()
                    .toList(),
            currentCinema = validCinema(adminState.currentCinema),
        )
        refreshHomeState()
    }

    private fun validCinema(cinemaHallName: String): String {
        val withSameName = town().cinemaHalls().map { it.name() }
                .firstOrNull { it == cinemaHallName }
        val firstOne = town().cinemaHalls().map { it.name() }
                .firstOrNull()

        return withSameName ?: firstOne ?: ""

    }

    fun onCreateCinemaHall(name: String) {
        town().createCinemaHall(name)
        refreshState()
    }

    fun onDeleteCinemaHall(name: String) {
        town().deleteCinemaHall(name)
        refreshState()
    }

    fun onCreateUser(userName: String) {
        town().createCustomer(userName)
        refreshState()
    }

    fun onDeleteUser(userName: String) {
        town().deleteCustomer(userName)
        refreshState()
    }


    fun onCreateMovie(title: String, cinema: String) {
        town().cinemaHalls().firstOrNull { it.name() == cinema }?.addMovie(title)
        refreshState()
    }

    fun onDeleteMovie(title: String, cinema: String) {
        town().cinemaHalls().firstOrNull { it.name() == cinema }?.deleteMovie(title)
        refreshState()
    }

    fun onSelectCinema(selectedCinema: String) {
        adminState = adminState.copy(currentCinema = selectedCinema)
        refreshState()
    }

    fun onCreateShowtime(movieName: String, hour: Int, capacity: Int) {
        town().cinemaHalls().firstOrNull { it.name() == adminState.currentCinema }
                ?.let { cinemaHall ->
                    cinemaHall.movies().firstOrNull { it.title() == movieName }
                            ?.addShowtime(hour, capacity)
                }

        refreshState()
    }

    fun buy(homeShow: HomeShow, movie: HomeMovie, userName: String) {

        //        town().cinemaHalls().flatMap { it.movies() }.flatMap { it.showtimes() }
        //                .firstOrNull { it == showtimeContract }?.let {
        //            it.buy("Marie")
        //        }
        Log.d("LOL", "But on ${movie.cinema} for ${movie.name} at ${homeShow.start}")

        town().cinemaHalls().firstOrNull { it.name() == movie.cinema }
                ?.let { cinemaHall ->
                    cinemaHall.movies().firstOrNull { it.title() == movie.name }?.let {
                        it.showtimes()
                                .firstOrNull { show -> show.startHour() == homeShow.start }
                    }?.let {
                        it.buy(userName)
                        Log.d("LOL", "on arrive ici avec ${it.seatsAvailableToBuy()}")
                    }
                }
        refreshHomeState()
    }


}

data class AdminState(
    val cinemaHalls: List<String> = mutableListOf(),
    val customers: List<String> = mutableListOf(),
    val moviesOnSelectedCinema: List<MovieContract> = mutableListOf(),
    val currentCinema: String = "",
)

data class HomeState(
    val cinemaHalls: List<String> = mutableListOf(),
    val customers: List<String> = mutableListOf(),
    val movies: List<HomeMovie> = mutableListOf(),
)

data class HomeMovie(val cinema: String, val name: String, val showtimes: List<HomeShow>)
data class HomeShow(val start: Int, val seats: Int)
