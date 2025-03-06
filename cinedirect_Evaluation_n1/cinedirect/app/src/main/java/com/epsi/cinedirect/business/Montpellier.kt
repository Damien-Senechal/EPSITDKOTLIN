package com.epsi.cinedirect.business

import com.epsi.cinedirect.models.CinemaHall
import com.epsi.cinedirect.models.CinemaHallContract
import com.epsi.cinedirect.models.Customer
import com.epsi.cinedirect.models.Town

object Montpellier: Town {
    private val cinemaHallsList = mutableListOf<CinemaHallContract>()
    private val customersList = mutableListOf<Customer>()

    override fun cinemaHalls(): List<CinemaHallContract> {
        return cinemaHallsList.toList()
    }

    override fun createCinemaHall(name: String) {
        if (!cinemaHallsList.any { it.name() == name }) {
            cinemaHallsList.add(CinemaHall(name))
        }
    }

    override fun deleteCinemaHall(name: String) {
        cinemaHallsList.removeIf { it.name() == name }
    }

    override fun customers(): List<Customer> {
        return customersList.toList()
    }

    override fun createCustomer(name: String) {
        if (!customersList.any { it.name == name }) {
            customersList.add(Customer(name))
        }
    }

    override fun deleteCustomer(name: String) {
        customersList.removeIf { it.name == name }
    }
}