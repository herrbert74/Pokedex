package com.zsoltbertalan.pokedex.domain.model

data class Pokemon(
	val id: Int = 0,
	val abilities: List<String> = emptyList(),
	val evolutions: List<String> = emptyList(),
	val hitPoints: Int = 0,
	val imageUrl: String = "",
	val location: String = "",
	val region: String = "",
	val name: String = "",
	val type: String = ""
)
