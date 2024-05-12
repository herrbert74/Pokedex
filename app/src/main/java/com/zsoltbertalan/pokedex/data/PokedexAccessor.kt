package com.zsoltbertalan.pokedex.data

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.pokedex.data.db.PokemonDataSource
import com.zsoltbertalan.pokedex.data.network.PokedexService
import com.zsoltbertalan.pokedex.data.network.dto.toPokemonList
import com.zsoltbertalan.pokedex.domain.api.PokedexRepository
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.domain.model.PokemonDetails
import com.zsoltbertalan.pokedex.domain.model.PokemonEvolution
import com.zsoltbertalan.pokedex.ext.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class PokedexAccessor(
	private val pokedexService: PokedexService,
	private val pokemonDataSource: PokemonDataSource,
	private val ioContext: CoroutineDispatcher,
) : PokedexRepository {

	override suspend fun getAllPokemons(): Flow<ApiResult<List<Pokemon>>> =
		withContext(ioContext) {
			fetchCacheThenNetwork(
				fetchFromLocal = { pokemonDataSource.getPokemons() },
				makeNetworkRequest = { pokedexService.getPokemons() },
				saveResponseData = { pokemons -> pokemonDataSource.insertPokemons(pokemons.toPokemonList()) }
			)
		}

	/**
	 * @return a Flow of [PokemonDetails] Result, which is a composition of the Pokemon and its evolution, which in
	 * turn is name and an image.
	 * Due to the very limited API, most of the evolution is missing, so in these cases this function will return the
	 * name and the image of the current pokemon as a fallback.
	 */
	override suspend fun getPokemonDetails(pokemonId: Int): Flow<ApiResult<PokemonDetails>> {
		return withContext(ioContext) {
			val pokemon = pokemonDataSource.getPokemon(pokemonId)
			val existingEvolutions = pokemonDataSource.getPokemons().first()?.filter {
				pokemon.evolutions.contains(it.name)
			}?.associateBy { it.name }
			val evolutions = pokemon.evolutions.map { evolution ->
				if (existingEvolutions?.keys?.contains(evolution) == true) {
					PokemonEvolution(evolution, existingEvolutions[evolution]?.imageUrl ?: "")
				} else {
					PokemonEvolution(evolution, pokemon.imageUrl)
				}
			}
			return@withContext flowOf(Ok(PokemonDetails(pokemon, evolutions)))
		}
	}
}
