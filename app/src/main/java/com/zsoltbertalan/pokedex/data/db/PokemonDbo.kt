package com.zsoltbertalan.pokedex.data.db

import com.zsoltbertalan.pokedex.domain.model.Pokemon
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

const val REGION_REGEX = "(.*?)region"
class PokemonDbo() : RealmObject {

	constructor(
		id: Int,
		abilities: List<String> = emptyList(),
		evolutions: List<String> = emptyList(),
		hitPoints: Int = 0,
		imageUrl: String = "",
		location: String = "",
		name: String = "",
		type: String = ""
	) : this() {
		this.id = id
		this.abilities = abilities.toRealmList()
		this.evolutions = evolutions.toRealmList()
		this.hitPoints = hitPoints
		this.imageUrl = imageUrl
		this.location = location
		this.name = name
		this.type = type
	}

	@PrimaryKey
	var id: Int = 0
	var abilities: RealmList<String> = realmListOf()
	var evolutions: RealmList<String> = realmListOf()
	var hitPoints: Int = 0
	var imageUrl: String = ""
	var location: String = ""
	var name: String = ""
	var type: String = ""

}

fun Pokemon.toDbo(): PokemonDbo = PokemonDbo(
	id = this.id,
	abilities = this.abilities,
	evolutions = this.evolutions,
	hitPoints = this.hitPoints,
	imageUrl = this.imageUrl,
	location = this.location,
	name = this.name,
	type = this.type,
)

fun PokemonDbo.toPokemon(): Pokemon = Pokemon(
	id = this.id,
	abilities = this.abilities,
	evolutions = this.evolutions,
	hitPoints = this.hitPoints,
	imageUrl = this.imageUrl,
	location = this.location,
	region = Regex(REGION_REGEX).find(location)?.value ?: location,
	name = this.name,
	type = this.type,
)