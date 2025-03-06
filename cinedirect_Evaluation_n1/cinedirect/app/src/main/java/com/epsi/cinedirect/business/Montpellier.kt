package com.epsi.cinedirect.business

import com.epsi.cinedirect.models.CinemaHallContract
import com.epsi.cinedirect.models.Customer
import com.epsi.cinedirect.models.Town

object Montpellier: Town {
    override fun cinemaHalls(): List<CinemaHallContract> {
        return listOf() // TODO : "Not yet implemented"
    }

    override fun createCinemaHall(name: String) {
        //TODO("Not yet implemented")
    }

    override fun deleteCinemaHall(name: String) {
        //TODO("Not yet implemented")
    }

    override fun customers(): List<Customer> {
        return listOf() //TODO("Not yet implemented")

    }

    override fun createCustomer(name: String) {
        //TODO("Not yet implemented")
    }

    override fun deleteCustomer(name: String) {
        //TODO("Not yet implemented")
    }
}