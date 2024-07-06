package com.zsoltbertalan.pokedex.presentation.ui.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.pokedex.domain.api.PokedexRepository
import com.zsoltbertalan.pokedex.domain.model.Failure
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel.Sort.ALPHABETICAL
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel.Sort.TYPE
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel.SortOrder.ASCENDING
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel.SortOrder.DESCENDING
import com.zsoltbertalan.pokedex.presentation.ui.pokemons.PokemonsViewModel.SortOrder.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonsViewModel @Inject constructor(private val pokedexRepository: PokedexRepository) : ViewModel() {

	private val _state = MutableStateFlow(UiState())
	val state: StateFlow<UiState> = _state.asStateFlow()

	init {
		requestPokemons()
	}

	fun requestPokemons() {
		viewModelScope.launch {
			_state.update { it.copy(loading = true) }
			pokedexRepository.getAllPokemons().collect { result ->
				_state.update { uiState ->
					when {
						result.isOk -> {
							val types = listOf("ALL").plus(result.value.associateBy { it.type }.keys)
							val regions = listOf("ALL").plus(result.value.associateBy { it.region }.keys)
							uiState.copy(
								pokemons = result.value,
								processedPokemons = result.value,
								loading = false,
								error = null,
								types = types,
								regions = regions,
							)
						}

						else -> uiState.copy(loading = false, error = result.error)
					}
				}
			}
		}
	}

	fun sortPokemons(sort: Sort) {
		val sortOrder = if (_state.value.sort == sort && _state.value.sortOrder == ASCENDING) DESCENDING
		else ASCENDING

		val processedPokemons = when (sort) {
			ALPHABETICAL -> sortPokemonsOnString(sortOrder, Pokemon::name)
			TYPE -> sortPokemonsOnString(sortOrder, Pokemon::type)
			else -> {
				if (sortOrder == ASCENDING) _state.value.processedPokemons.sortedBy { it.hitPoints }
				else _state.value.processedPokemons.sortedByDescending { it.hitPoints }
			}
		}
		_state.update { uiState ->
			uiState.copy(
				processedPokemons = processedPokemons,
				sort = sort,
				sortOrder = sortOrder
			)
		}
	}

	private fun sortPokemonsOnString(sortOrder: SortOrder, param: (Pokemon) -> String) =
		if (sortOrder == ASCENDING) _state.value.processedPokemons.sortedBy(param)
		else _state.value.processedPokemons.sortedByDescending(param)

	fun filterPokemonsByType(newType: String) {
		if (newType == "ALL") {
			_state.update { uiState ->
				uiState.copy(
					processedPokemons = uiState.pokemons,
					sort = Sort.NONE,
					sortOrder = NONE,

				)
			}
		} else {
			val processedPokemons = _state.value.pokemons.filter { it.type == newType }
			_state.update { uiState ->
				uiState.copy(
					processedPokemons = processedPokemons,
					sort = Sort.NONE,
					sortOrder = NONE,
					type = newType,
				)
			}
		}
	}

	fun filterPokemonsByRegion(newRegion: String) {
		if (newRegion == "ALL") {
			_state.update { uiState ->
				uiState.copy(
					processedPokemons = uiState.pokemons,
					sort = Sort.NONE,
					sortOrder = NONE,
					region = ""
				)
			}
		} else {
			val processedPokemons = _state.value.pokemons.filter { it.region == newRegion }
			_state.update { uiState ->
				uiState.copy(
					processedPokemons = processedPokemons,
					sort = Sort.NONE,
					sortOrder = NONE,
					region = newRegion,
				)
			}
		}
	}

	data class UiState(
		val loading: Boolean = false,
		val pokemons: List<Pokemon> = emptyList(),
		val processedPokemons: List<Pokemon> = emptyList(),
		val pokemonId: Int? = null,
		val sort: Sort = Sort.NONE,
		val sortOrder: SortOrder = NONE,
		val types: List<String> = emptyList(),
		val regions: List<String> = emptyList(),
		val type: String = "",
		val region: String = "",
		val error: Failure? = null
	)

	enum class Sort {
		NONE,
		ALPHABETICAL,
		TYPE,
		HIT_POINTS
	}

	enum class SortOrder {
		NONE,
		ASCENDING,
		DESCENDING
	}

}
