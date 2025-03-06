package com.epsi.cinedirect.models

class Showtime(private val hour: Int, private val capacity: Int) : ShowtimeContract {
    private val reservations = mutableListOf<Reservation>()

    override fun startHour(): Int = hour

    override fun seatsAvailableToBuy(): Int = capacity - reservations.size

    override fun isBookedBy(customerName: String): Boolean {
        return reservations.any { it.customerName == customerName }
    }

    override fun buy(customerName: String): Reservation {
        if (seatsAvailableToBuy() <= 0) {
            throw IllegalStateException("Aucune place disponible")
        }

        if (isBookedBy(customerName)) {
            throw IllegalStateException("Client a déjà une réservation")
        }

        val reservation = Reservation(customerName)
        reservations.add(reservation)
        return reservation
    }
}