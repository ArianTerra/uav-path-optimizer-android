package com.epslilonlabs.uavpathoptimizer.di

import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClient
import com.epslilonlabs.uavpathoptimizer.domain.usecases.SendPath
import com.epslilonlabs.uavpathoptimizer.domain.usecases.SendPathImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Provides
    fun provideSendPath(client: PathOptimizerClient): SendPath = SendPathImpl(client)
}