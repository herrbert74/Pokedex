package com.zsoltbertalan.pokedex.ui.pokemondetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.zsoltbertalan.pokedex.domain.api.PokedexRepository
import com.zsoltbertalan.pokedex.domain.model.PokemonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val pokedexRepository: PokedexRepository
) : ViewModel() {

	private val _state = MutableStateFlow(UiState())
	val state: StateFlow<UiState> = _state.asStateFlow()

	private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

	init {
		viewModelScope.launch {
			requestPokemonDetails()
		}
	}

	private fun requestPokemonDetails() {
		viewModelScope.launch {
			_state.update { it.copy(loading = true) }
			pokedexRepository.getPokemonDetails(pokemonId).collect { result ->
				_state.update { uiState ->
					when (result) {
						is Ok -> {

							uiState.copy(
								pokemonDetails = result.value,
								loading = false,
								error = null,
							)
						}

						is Err -> uiState.copy(loading = false, error = result.error)
					}
				}

			}
		}
	}

	data class UiState(
		val loading: Boolean = false,
		val pokemonDetails: PokemonDetails = PokemonDetails(),
		val error: Throwable? = null
	)

}
