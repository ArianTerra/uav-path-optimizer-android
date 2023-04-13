package com.epslilonlabs.uavpathoptimizer

import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClient
import com.epslilonlabs.uavpathoptimizer.data.api.PathOptimizerClientImpl
import com.epslilonlabs.uavpathoptimizer.data.dto.GeoCoordinateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class ApiServiceBuilderTest {
    @Test
    fun sendData() {
        val client: PathOptimizerClient = PathOptimizerClientImpl()

        val data = mutableListOf(
            GeoCoordinateDto(50.021208, 36.343257), //1
            GeoCoordinateDto(50.021129, 36.340071), //2
            GeoCoordinateDto(50.018267, 36.342377), //3
            GeoCoordinateDto(50.016099, 36.342343), //4
        )

        runBlocking {
            val response = async(Dispatchers.IO) {
                client.sendGeoCoordinates(data)
            }.await()

            if(response != null && response.isNotEmpty()) {
                assert(true)
            }
            else {
                assert(false)
            }
        }
    }
}