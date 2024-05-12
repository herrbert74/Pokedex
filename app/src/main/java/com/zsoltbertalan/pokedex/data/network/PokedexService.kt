package com.zsoltbertalan.pokedex.data.network

import com.zsoltbertalan.pokedex.data.network.dto.PokemonDto
import retrofit2.http.GET

interface PokedexService {
	@GET("api/pokemon")
	suspend fun getPokemons(): List<PokemonDto>
}