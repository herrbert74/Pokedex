package com.zsoltbertalan.pokedex.di

import com.zsoltbertalan.pokedex.data.db.PokemonDbo
import com.zsoltbertalan.pokedex.data.db.PokemonDao
import com.zsoltbertalan.pokedex.data.db.PokemonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

	@Provides
	@Singleton
	fun provideRealmConfiguration() = RealmConfiguration.Builder(
		schema = setOf(PokemonDbo::class)
	).build()

	@Provides
	@Singleton
	fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.open(realmConfiguration)

	@Provides
	@Singleton
	internal fun providePokemonDataSource(
		realm: Realm
	): PokemonDataSource {
		return PokemonDao(realm)
	}

}
