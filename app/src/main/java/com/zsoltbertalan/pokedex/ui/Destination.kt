package com.zsoltbertalan.pokedex.ui

import androidx.annotation.StringRes
import com.zsoltbertalan.pokedex.R

enum class Destination(
	@StringRes val titleId: Int,
	val route: String
) {
	POKEMONS(R.string.pokemons, "pokemons"),
	DETAILS(R.string.details, "details/{pokemonId}"),
}
