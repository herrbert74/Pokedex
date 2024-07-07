package com.zsoltbertalan.pokedex.ui

import com.github.michaelbull.result.Ok
import com.zsoltbertalan.pokedex.common.testhelper.PokemonMother
import com.zsoltbertalan.pokedex.domain.api.PokedexRepository
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class PokemonsViewModelTest {

	private val pokedexRepository = mockk<PokedexRepository>(relaxed = true)

	private lateinit var pokemonsViewModel: PokemonsViewModel

	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setUp() {

		Dispatchers.setMain(dispatcher)
		coEvery { pokedexRepository.getAllPokemons() } answers { flowOf(Ok(PokemonMother.createPokemonList())) }
		pokemonsViewModel = PokemonsViewModel(pokedexRepository)

	}

	@After
	fun tearDown() {

		Dispatchers.resetMain()

	}

	@Test
	fun `when started then getPokemons is called and returns correct list`() = runTest {

		coVerify(exactly = 1) { pokedexRepository.getAllPokemons() }
		pokemonsViewModel.state.value.pokemons shouldBe PokemonMother.createPokemonList()

	}

	@Test
	fun `when sort by hit points button is pressed then getPokemons returned in order of hit points`() = runTest {

		pokemonsViewModel.sortPokemons(PokemonsViewModel.Sort.HIT_POINTS)

		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name2"

	}

	@Test
	fun `when sort by name button is pressed then getPokemons returned in order of names`() = runTest {

		pokemonsViewModel.sortPokemons(PokemonsViewModel.Sort.ALPHABETICAL)

		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name1"

	}

	@Test
	fun `when sort by type button is pressed then getPokemons returned in order of types`() = runTest {

		pokemonsViewModel.sortPokemons(PokemonsViewModel.Sort.TYPE)

		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name6"

	}

	@Test
	fun `when filter by type and no region then getPokemons returns only chosen types`() = runTest {

		pokemonsViewModel.filterPokemonsByType("Fire")

		pokemonsViewModel.state.value.processedPokemons.size shouldBe 3
		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name4"

	}

	@Test
	fun `when filter by region and no type then getPokemons returns only Pokemons from chosen region`() = runTest {

		pokemonsViewModel.filterPokemonsByRegion("")

		pokemonsViewModel.state.value.processedPokemons.size shouldBe 2
		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name4"

	}

	@Test
	fun `when filter by both region type then getPokemons returns only correctly filtered data`() =
		runTest {

			pokemonsViewModel.filterPokemonsByType("Fire")
			pokemonsViewModel.filterPokemonsByRegion("Kanto region")

		pokemonsViewModel.state.value.processedPokemons.size shouldBe 1
		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name7"

	}

	@Test
	fun `when filter by region reset to all then getPokemons returns all pokemons`() = runTest {

		pokemonsViewModel.filterPokemonsByRegion("Kanto region")
		pokemonsViewModel.filterPokemonsByRegion("ALL")

		pokemonsViewModel.state.value.processedPokemons.size shouldBe 8
		pokemonsViewModel.state.value.processedPokemons[0].name shouldBe "name1"

	}

}


