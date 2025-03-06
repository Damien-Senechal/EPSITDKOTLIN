package com.epsi.cinedirect.models

interface Town {
    fun cinemaHalls(): List<CinemaHallContract>
    fun createCinemaHall(name: String)
    fun deleteCinemaHall(name: String)

    fun customers(): List<Customer>
    fun createCustomer(name: String)
    fun deleteCustomer(name: String)
}

interface CinemaHallContract {
    fun name(): String
    fun movies(): List<MovieContract>
    fun addMovie(title: String)
    fun deleteMovie(title: String)
}

interface MovieContract {
    fun title(): String
    fun showtimes(): List<ShowtimeContract>
    fun addShowtime(startHour: Int, seats: Int)
}

interface ShowtimeContract {
    fun startHour(): Int
    fun seatsAvailableToBuy(): Int
    fun isBookedBy(customerName: String): Boolean
    fun buy(customerName: String): Reservation
}

