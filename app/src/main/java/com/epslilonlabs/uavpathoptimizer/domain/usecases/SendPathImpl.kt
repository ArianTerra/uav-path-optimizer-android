package com.epslilonlabs.uavpathoptimizer.domain.usecases

import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClient
import com.epslilonlabs.uavpathoptimizer.data.dto.GeoCoordinateDto
import com.google.android.gms.maps.model.LatLng

class SendPathImpl(private val client: PathOptimizerClient) : SendPath {
    override suspend fun invoke(data: List<LatLng>): List<LatLng> {
        val mappedList: List<GeoCoordinateDto> = data.map {
            GeoCoordinateDto(it.latitude, it.longitude)
        }

        val response = client.sendGeoCoordinates(mappedList)

        val mappedResponse: List<LatLng> = response.map {
            LatLng(it.latitude, it.longitude)
        }

        return mappedResponse
    }
}