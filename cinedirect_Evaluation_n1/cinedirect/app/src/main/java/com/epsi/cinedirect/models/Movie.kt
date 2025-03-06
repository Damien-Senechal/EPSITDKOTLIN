package com.epsi.cinedirect.models

class Movie(private val movieTitle: String) : MovieContract {
    private val showtimesList = mutableListOf<ShowtimeContract>()

    override fun title(): String = movieTitle

    override fun showtimes(): List<ShowtimeContract> = showtimesList.toList()

    override fun addShowtime(startHour: Int, seats: Int) {
        showtimesList.add(Showtime(startHour, seats))
    }
}