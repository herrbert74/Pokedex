package com.zsoltbertalan.pokedex.domain.model

data class PokemonDetails(
	val pokemon: Pokemon = Pokemon(),
	val evolutions: List<PokemonEvolution> = emptyList(),
)

data class PokemonEvolution(val name: String, val imageUrl: String)
