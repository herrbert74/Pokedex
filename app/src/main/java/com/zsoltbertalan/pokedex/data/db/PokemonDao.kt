package com.zsoltbertalan.pokedex.data.db

import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.ext.apiRunCatching
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonDao @Inject constructor(private val realm: Realm) : PokemonDataSource {

	override suspend fun purgeDatabase() {
		realm.write {
			val pokemons = this.query(PokemonDbo::class).find()
			delete(pokemons)
		}
	}

	override suspend fun insertPokemons(pokemons: List<Pokemon>) {
		apiRunCatching {
			realm.write {
				pokemons.map { copyToRealm(it.toDbo(), UpdatePolicy.ALL) }
			}
		}
	}

	override fun getPokemons(): Flow<List<Pokemon>?> {
		return realm.query(PokemonDbo::class).asFlow().map { dbo -> dbo.list.map { it.toPokemon() }.takeIf { it.isNotEmpty() } }
	}

	override fun getPokemon(id: Int): Pokemon {
		return realm.query<PokemonDbo>("id = $0", id).first().find()?.toPokemon() ?: Pokemon()
	}

}
