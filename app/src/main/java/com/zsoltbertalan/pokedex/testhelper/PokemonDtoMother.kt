package com.zsoltbertalan.pokedex.testhelper

import com.zsoltbertalan.pokedex.data.network.dto.PokemonDto

object PokemonDtoMother {

	fun createPokemonDtoList() = listOf(
		createDefaultPokemonDto(id = 0, name = "name1", hitPoints = 140, type = "Normal"),
		createDefaultPokemonDto(id = 1, name = "name2", hitPoints = 30, type = "Grass"),
		createDefaultPokemonDto(id = 2, name = "name3", hitPoints = 45, type = "Water"),
		createDefaultPokemonDto(id = 3, name = "name4", hitPoints = 120, type = "Fire"),
		createDefaultPokemonDto(id = 4, name = "name5", hitPoints = 87, type = "Psychic"),
		createDefaultPokemonDto(id = 5, name = "name6", hitPoints = 100, type = "Fighting"),
	)

}

private fun createDefaultPokemonDto(
	id: Int = 0,
	abilities: List<String> = emptyList(),
	evolutions: List<String> = emptyList(),
	hitPoints: Int = 0,
	imageUrl: String = "",
	location: String = "",
	name: String = "",
	type: String = ""
): PokemonDto = PokemonDto(abilities, evolutions, hitPoints, id, imageUrl, location, name, type)
