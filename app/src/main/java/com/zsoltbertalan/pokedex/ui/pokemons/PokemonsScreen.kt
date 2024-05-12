package com.zsoltbertalan.pokedex.ui.pokemons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zsoltbertalan.pokedex.R
import com.zsoltbertalan.pokedex.design.Dimens
import com.zsoltbertalan.pokedex.design.PokedexTheme
import com.zsoltbertalan.pokedex.design.PokedexTypography
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.ui.pokemons.PokemonsViewModel.Sort.ALPHABETICAL
import com.zsoltbertalan.pokedex.ui.pokemons.PokemonsViewModel.Sort.HIT_POINTS
import com.zsoltbertalan.pokedex.ui.pokemons.PokemonsViewModel.Sort.TYPE
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PokemonsScreen(
	stateFlow: StateFlow<PokemonsViewModel.UiState>,
	onItemClick: (Pokemon) -> Unit,
	onSortPokemonsClick: (PokemonsViewModel.Sort) -> Unit,
	onTypeChanged: (String) -> Unit,
	onRegionChanged: (String) -> Unit,
	onReload: () -> Unit,
) {

	val uiState by stateFlow.collectAsStateWithLifecycle()

	var isTypesExpanded by rememberSaveable { mutableStateOf(false) }
	var isRegionExpanded by rememberSaveable { mutableStateOf(false) }

	var selectedType by rememberSaveable { mutableStateOf("ALL") }
	var selectedRegion by rememberSaveable { mutableStateOf("ALL") }

	val typeIcon = if (isTypesExpanded)
		Icons.Filled.KeyboardArrowUp
	else
		Icons.Filled.KeyboardArrowDown

	val regionIcon = if (isRegionExpanded)
		Icons.Filled.KeyboardArrowUp
	else
		Icons.Filled.KeyboardArrowDown

	Scaffold(
		topBar = {
			TopAppBar(
				colors = topAppBarColors(
					containerColor = PokedexTheme.colorScheme.primaryContainer,
					titleContentColor = PokedexTheme.colorScheme.onPrimaryContainer,
				),
				title = {
					Text("Pokedex")
				},
				actions = {
					IconButton(onClick = { onSortPokemonsClick(ALPHABETICAL) }) {
						Icon(
							painter = painterResource(R.drawable.ic_sort_by_alpha),
							contentDescription = "Sort by alpha icon",
						)
					}
					IconButton(onClick = { onSortPokemonsClick(TYPE) }) {
						Icon(
							painter = painterResource(R.drawable.ic_sort_by_type),
							contentDescription = "Sort by type icon",
						)
					}
					IconButton(onClick = { onSortPokemonsClick(HIT_POINTS) }) {
						Icon(
							painter = painterResource(R.drawable.ic_sort_by_hit_points),
							contentDescription = "Sort by hit points icon",
						)
					}
				}
			)
		}
	) { innerPadding ->
		if (uiState.error == null) {

			LazyColumn(
				modifier = Modifier.padding(innerPadding),
				content = {
					if (uiState.types.isNotEmpty()) {
						item {
							Row(
								modifier = Modifier.padding(
									top = Dimens.marginNormal,
									start = Dimens.marginLarge,
									end = Dimens.marginLarge
								)
							) {

								ExposedDropdownMenuBox(
									modifier = Modifier
										.weight(1f)
										.padding(end = Dimens.marginNormal),
									expanded = isTypesExpanded,
									onExpandedChange = { isTypesExpanded = it },
								) {
									OutlinedTextField(
										modifier = Modifier
											.menuAnchor(),
										value = selectedType,
										onValueChange = {
											selectedType = it
										},
										singleLine = true,
										readOnly = true,
										trailingIcon = {
											Icon(
												typeIcon,
												"Type dropdown icon",
											)
										}
									)

									ExposedDropdownMenu(
										expanded = isTypesExpanded,
										onDismissRequest = { isTypesExpanded = false },
										modifier = Modifier
											.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginLarge)
									) {
										uiState.types.forEach { type ->
											DropdownMenuItem(
												text = { Text(type) },
												onClick = {
													onTypeChanged(type)
													selectedType = type
													isTypesExpanded = false
												}
											)
										}
									}
								}
								ExposedDropdownMenuBox(
									modifier = Modifier
										.weight(1f),
									expanded = isRegionExpanded,
									onExpandedChange = { isRegionExpanded = it },
								) {
									OutlinedTextField(
										modifier = Modifier
											.menuAnchor(),
										value = selectedRegion,
										onValueChange = {
											selectedRegion = it
										},
										singleLine = true,
										readOnly = true,
										trailingIcon = {
											Icon(
												regionIcon,
												"Region dropdown icon",
											)
										}
									)

									ExposedDropdownMenu(
										expanded = isRegionExpanded,
										onDismissRequest = { isRegionExpanded = false },
										modifier = Modifier
											.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginLarge)
									) {
										uiState.regions.forEach { region ->
											DropdownMenuItem(
												text = { Text(region) },
												onClick = {
													onRegionChanged(region)
													selectedRegion = region
													isRegionExpanded = false
												}
											)
										}
									}
								}
							}
						}

					}
					items(
						uiState.processedPokemons.size,
						{ index -> uiState.processedPokemons[index].hashCode() }
					) { index ->
						val pokemon = uiState.processedPokemons[index]
						PokemonRow(
							pokemon = pokemon,
							onItemClicked = onItemClick
						)
					}
				}
			)
		} else {
			ErrorView(innerPadding, uiState.error, onReload)
		}
	}
}

@Composable
private fun ErrorView(
	innerPadding: PaddingValues,
	throwable: Throwable?,
	onReload: () -> Unit
) {
	Column(
		Modifier
			.fillMaxSize(1f)
			.padding(innerPadding),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(R.drawable.ic_business_error),
			contentDescription = null
		)
		Text(
			text = throwable?.message ?: "Something went wrong",
			modifier = Modifier
				.fillMaxWidth()
				.padding(Dimens.marginLarge)
				.align(Alignment.CenterHorizontally),
			textAlign = TextAlign.Center,
			style = PokedexTypography.titleLarge,
		)
		Button(
			modifier = Modifier
				.padding(Dimens.marginLarge),
			onClick = { onReload() }
		) {
			Text(text = "Reload")
		}
	}
}
