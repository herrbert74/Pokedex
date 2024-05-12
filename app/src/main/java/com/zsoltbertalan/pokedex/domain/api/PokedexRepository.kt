package com.zsoltbertalan.pokedex.domain.api

import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.domain.model.PokemonDetails
import com.zsoltbertalan.pokedex.ext.ApiResult
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
	suspend fun getAllPokemons(): Flow<ApiResult<List<Pokemon>>>
	suspend fun getPokemonDetails(pokemonId: Int): Flow<ApiResult<PokemonDetails>>
}
