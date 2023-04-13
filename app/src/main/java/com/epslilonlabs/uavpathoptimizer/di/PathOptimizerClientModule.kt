package com.epslilonlabs.uavpathoptimizer.di

import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClient
import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PathOptimizerClientModule {
    @Provides
    fun providesClient(): PathOptimizerClient = PathOptimizerClientImpl()
}