package com.zsoltbertalan.pokedex.data

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.pokedex.data.db.PokemonDataSource
import com.zsoltbertalan.pokedex.data.network.PokedexService
import com.zsoltbertalan.pokedex.data.network.dto.toPokemonList
import com.zsoltbertalan.pokedex.testhelper.PokemonDtoMother
import com.zsoltbertalan.pokedex.testhelper.PokemonMother
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class PokedexAccessorTest {

	private lateinit var underTest: PokedexAccessor

	private var api = mockk<PokedexService>()
	private var dataSource = mockk<PokemonDataSource>()

	@Before
	fun setUp() {
		underTest = PokedexAccessor(
			api, dataSource, Dispatchers.Unconfined
		)
	}

	@Test
	internal fun `should return pokemons from api`() = runTest {
		val apiResponse = PokemonDtoMother.createPokemonDtoList()

		coEvery { api.getPokemons() } returns apiResponse
		coEvery { dataSource.getPokemons() } returns flowOf(PokemonMother.createPokemonList())
		coEvery { dataSource.insertPokemons(any()) } returns Unit

		val result = underTest.getAllPokemons().first()

		result shouldBe Ok(apiResponse.toPokemonList())

	}

}
