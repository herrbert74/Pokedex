package com.zsoltbertalan.pokedex.domain.api

import com.zsoltbertalan.pokedex.common.util.Outcome
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
	fun getAllPokemons(): Flow<Outcome<List<Pokemon>>>
	fun getPokemonDetails(pokemonId: Int): Flow<Outcome<PokemonDetails>>
}
