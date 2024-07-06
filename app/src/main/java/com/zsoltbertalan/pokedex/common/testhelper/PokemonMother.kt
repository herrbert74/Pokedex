package com.zsoltbertalan.pokedex.common.testhelper

import com.zsoltbertalan.pokedex.domain.model.Pokemon

object PokemonMother {

	fun createPokemonList() = listOf(
		createDefaultPokemon(id = 0, name = "name1", hitPoints = 140, type = "Normal"),
		createDefaultPokemon(id = 1, name = "name2", hitPoints = 30, type = "Grass"),
		createDefaultPokemon(id = 2, name = "name3", hitPoints = 45, type = "Water"),
		createDefaultPokemon(id = 3, name = "name4", hitPoints = 120, type = "Fire"),
		createDefaultPokemon(id = 4, name = "name5", hitPoints = 87, type = "Psychic"),
		createDefaultPokemon(id = 5, name = "name6", hitPoints = 100, type = "Fighting"),
	)

}

private fun createDefaultPokemon(
	id: Int = 0,
	abilities: List<String> = emptyList(),
	evolutions: List<String> = emptyList(),
	hitPoints: Int = 0,
	imageUrl: String = "",
	location: String = "",
	region: String = "",
	name: String = "",
	type: String = ""
): Pokemon = Pokemon(id, abilities, evolutions, hitPoints, imageUrl, location, region, name, type)
