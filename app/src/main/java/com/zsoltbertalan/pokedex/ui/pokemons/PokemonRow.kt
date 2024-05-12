package com.zsoltbertalan.pokedex.ui.pokemons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.zsoltbertalan.pokedex.R
import com.zsoltbertalan.pokedex.design.Colors
import com.zsoltbertalan.pokedex.design.Dimens
import com.zsoltbertalan.pokedex.design.PokedexTypography
import com.zsoltbertalan.pokedex.domain.model.Pokemon
import com.zsoltbertalan.pokedex.testhelper.PokemonMother
import kotlinx.coroutines.Dispatchers

@Composable
fun PokemonRow(pokemon: Pokemon, onItemClicked: (Pokemon) -> Unit) {

	Card(
		modifier = Modifier
			.padding(vertical = Dimens.marginNormal, horizontal = Dimens.marginLarge)
			.clip(RoundedCornerShape(Dimens.marginSmall))
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.background(Colors.primaryContainer)
				.wrapContentHeight()
				.fillMaxWidth()
		) {

			Column(modifier = Modifier
				.clickable { onItemClicked(pokemon) }
				.weight(2f)
				.background(Colors.primaryContainer)
				.padding(vertical = Dimens.marginNormal, horizontal = Dimens.marginNormal)
				.testTag("PokemonsRow")
			) {

				Text(
					text = pokemon.name,
					style = PokedexTypography.titleLarge,
				)

				Text(
					text = "Type: ${pokemon.type}",
					style = PokedexTypography.bodyMedium,
				)

				Text(
					text = "Location: ${pokemon.region}",
					style = PokedexTypography.bodyMedium,
				)

				Text(
					text = "Abilities: ${pokemon.abilities}",
					style = PokedexTypography.bodyMedium,
				)

				Text(
					text = "Evolutions: ${pokemon.evolutions.ifEmpty { "None" }}",
					style = PokedexTypography.bodyMedium,
				)

				Text(
					text = "Hit points: ${pokemon.hitPoints}",
					style = PokedexTypography.bodyMedium,
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
					//.fillMaxWidth()
					.weight(1f)
					.padding(Dimens.marginSmall)
					.clip(RoundedCornerShape(Dimens.marginLarge))
					.clickable { onItemClicked(pokemon) }
					.testTag("PokemonImage"),
				placeholder = painterResource(R.drawable.ic_transparent),
				error = painterResource(id = R.drawable.ic_error),
				contentScale = ContentScale.FillWidth,
			)

		}
	}

}

@Preview
@Composable
fun PokemonPreview() {
	PokemonRow(pokemon = PokemonMother.createPokemonList().first()) {}
}
