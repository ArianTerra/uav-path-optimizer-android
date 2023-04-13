package com.epslilonlabs.uavpathoptimizer.data.api

import com.epslilonlabs.uavpathoptimizer.data.dto.GeoCoordinateDto

interface PathOptimizerClient {
    suspend fun sendGeoCoordinates(path: List<GeoCoordinateDto>) : List<GeoCoordinateDto>
}