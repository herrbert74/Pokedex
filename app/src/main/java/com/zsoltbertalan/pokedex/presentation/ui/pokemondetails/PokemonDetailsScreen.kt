package com.zsoltbertalan.pokedex.presentation.ui.pokemondetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.zsoltbertalan.pokedex.R
import com.zsoltbertalan.pokedex.presentation.design.Colors
import com.zsoltbertalan.pokedex.presentation.design.Dimens
import com.zsoltbertalan.pokedex.presentation.design.PokedexTheme
import com.zsoltbertalan.pokedex.presentation.design.PokedexTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonDetailsScreen(
	stateFlow: StateFlow<PokemonDetailsViewModel.UiState>,
	onBackClick: () -> Unit,
) {

	val uiState by stateFlow.collectAsStateWithLifecycle()

	BackHandler(onBack = { onBackClick() })

	val pokemon = uiState.pokemonDetails.pokemon

	Scaffold(
		topBar = {
			TopAppBar(
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = PokedexTheme.colorScheme.primaryContainer,
					titleContentColor = PokedexTheme.colorScheme.onPrimaryContainer,
				),
				title = {
					Text(uiState.pokemonDetails.pokemon.name)
				},
				navigationIcon = {
					IconButton(onClick = { onBackClick() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Finish",
							tint = MaterialTheme.colorScheme.onPrimaryContainer
						)
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.background(Colors.primaryContainer)
				.padding(innerPadding)
				.testTag("PokemonDetail")
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.background(Colors.primaryContainer)
					.wrapContentHeight()
					.fillMaxWidth()
			) {

				Column(
					modifier = Modifier
						.weight(2f)
						.background(Colors.primaryContainer)
						.padding(vertical = Dimens.marginNormal, horizontal = Dimens.marginLarge)
						.testTag("PokemonsRow")
				) {

					Text(
						text = "Type: ${pokemon.type}",
						style = PokedexTypography.bodyLarge,
					)

					Text(
						text = "Location: ${pokemon.region}",
						style = PokedexTypography.bodyLarge,
					)

					Text(
						text = "Abilities: ${pokemon.abilities}",
						style = PokedexTypography.bodyLarge,
					)

					Text(
						text = "Hit points: ${pokemon.hitPoints}",
						style = PokedexTypography.bodyLarge,
					)
				}

				val imageRequest = ImageRequest.Builder(LocalContext.current)
					.data(pokemon.imageUrl)
					.dispatcher(Dispatchers.IO)
					.memoryCacheKey(pokemon.imageUrl)
					.diskCacheKey(pokemon.imageUrl)
					.diskCachePolicy(CachePolicy.ENABLED)
					.memoryCachePolicy(CachePolicy.ENABLED)
					.build()
				AsyncImage(
					model = imageRequest,
					contentDescription = null,
					modifier = Modifier
						.weight(1f)
						.padding(Dimens.marginSmall)
						.clip(RoundedCornerShape(Dimens.marginLarge))
						.testTag("PokemonImage"),
					placeholder = painterResource(R.drawable.ic_transparent),
					error = painterResource(id = R.drawable.ic_error),
					contentScale = ContentScale.FillWidth,
				)

			}

			if (pokemon.evolutions.isNotEmpty()) {
				Text(
					text = "Evolutions",
					modifier = Modifier.padding(Dimens.marginLarge),
					style = PokedexTypography.titleLarge
				)
			}

			FlowRow {
				uiState.pokemonDetails.evolutions.forEach { evolution ->
					Column(
						modifier = Modifier
							.padding(
								vertical = Dimens.marginLarge,
								horizontal = Dimens.marginLarge
							)
							.weight(1f)
							.width(IntrinsicSize.Max)
					) {
						Text(text = evolution.name, style = PokedexTypography.titleMedium)

						val imageRequest = ImageRequest.Builder(LocalContext.current)
							.data(evolution.imageUrl)
							.dispatcher(Dispatchers.IO)
							.memoryCacheKey(evolution.imageUrl)
							.diskCacheKey(evolution.imageUrl)
							.diskCachePolicy(CachePolicy.ENABLED)
							.memoryCachePolicy(CachePolicy.ENABLED)
							.build()
						AsyncImage(
							model = imageRequest,
							contentDescription = null,
							modifier = Modifier
								.fillMaxWidth()
								.padding(Dimens.marginSmall)
								.clip(RoundedCornerShape(Dimens.marginLarge))
								.testTag("PokemonImage"),
							placeholder = painterResource(R.drawable.ic_transparent),
							error = painterResource(id = R.drawable.ic_error),
							contentScale = ContentScale.FillWidth,
						)
					}
				}
			}
		}
	}

}
