package com.epslilonlabs.uavpathoptimizer.domain.usecases

import com.google.android.gms.maps.model.LatLng

interface SendPath {
    suspend operator fun invoke(data: List<LatLng>): List<LatLng>
}