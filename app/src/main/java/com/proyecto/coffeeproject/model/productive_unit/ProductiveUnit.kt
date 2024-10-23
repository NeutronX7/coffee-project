package com.proyecto.coffeeproject.model.productive_unit

data class ProductiveUnit(
    val id: String,
    val name: String,
    val user: String,
    val lots: List<Lot>
)
