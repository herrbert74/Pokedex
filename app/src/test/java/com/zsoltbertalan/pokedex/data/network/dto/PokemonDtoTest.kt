package com.zsoltbertalan.pokedex.data.network.dto

import com.zsoltbertalan.pokedex.common.testhelper.PokemonDtoMother
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class PokemonDtoTest {

	private var mappedResponse :List<Pokemon> = emptyList()

	@Before
	fun setup() {
		val responseDto = PokemonDtoMother.createPokemonDtoList()
		mappedResponse = responseDto.toPokemonList()
	}

	@Test
	fun `when there is a response then name is mapped`() {
		mappedResponse[0].name shouldBe "name1"
	}

}
