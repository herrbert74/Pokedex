package com.zsoltbertalan.pokedex.data.network

import com.zsoltbertalan.pokedex.common.async.IoDispatcher
import com.zsoltbertalan.pokedex.data.db.PokemonDataSource
import com.zsoltbertalan.pokedex.data.repository.PokedexAccessor
import com.zsoltbertalan.pokedex.domain.api.PokedexRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL: String = "https://dummyapi.online/"

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {

	@Provides
	@Singleton
	internal fun providePokedexRetrofit(): Retrofit {
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY

		val httpClient = OkHttpClient.Builder()

		httpClient.addInterceptor(logging)
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(httpClient.build())
			.build()
	}

	@Provides
	@Singleton
	internal fun providePokedexService(retroFit: Retrofit): PokedexService {
		return retroFit.create(PokedexService::class.java)
	}

	@Provides
	@Singleton
	fun providePokedexRepository(
		pokedexService: PokedexService,
		pokemonDataSource: PokemonDataSource,
		@IoDispatcher ioContext: CoroutineDispatcher,
	): PokedexRepository {
		return PokedexAccessor(pokedexService, pokemonDataSource, ioContext)
	}

}
