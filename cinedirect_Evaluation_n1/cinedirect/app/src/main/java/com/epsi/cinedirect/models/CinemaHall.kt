package com.epsi.cinedirect.models

class CinemaHall(private val cinemaName: String) : CinemaHallContract {
    private val moviesList = mutableListOf<MovieContract>()

    override fun name(): String = cinemaName

    override fun movies(): List<MovieContract> = moviesList.toList()

    override fun addMovie(title: String) {
        if (!moviesList.any { it.title() == title }) {
            moviesList.add(Movie(title))
        }
    }

    override fun deleteMovie(title: String) {
        moviesList.removeIf { it.title() == title }
    }
}