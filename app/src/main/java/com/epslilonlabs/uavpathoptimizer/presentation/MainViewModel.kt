package com.epslilonlabs.uavpathoptimizer.presentation

import androidx.lifecycle.ViewModel
import com.epslilonlabs.uavpathoptimizer.domain.usecases.SendPath
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val sendPath: SendPath) : ViewModel() {
    suspend fun sendPathRequest(path: List<LatLng>): List<LatLng> {
        return sendPath.invoke(path)
    }
}