package com.proyecto.coffeeproject.model.productive_unit

data class ProductiveUnit(
    val name: String,
    val user: String,
    val lots: List<Lot>
)
