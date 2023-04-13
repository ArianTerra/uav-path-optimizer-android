package com.epslilonlabs.uavpathoptimizer.data.api

import com.epslilonlabs.uavpathoptimizer.data.dto.GeoCoordinateDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resumeWithException

class PathOptimizerClientImpl : PathOptimizerClient {
    override suspend fun sendGeoCoordinates(path: List<GeoCoordinateDto>): List<GeoCoordinateDto> {
        val gson = Gson()
        val toSendJson = gson.toJson(path)

        val url = "https://192.168.43.90:7040/api/optimizePath/"

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()

        val body = RequestBody.create(
            MediaType.get("application/json; charset=utf-8"),
            toSendJson
        )
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val response = suspendCancellableCoroutine<Response> { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response) {
                        // Handle cancellation here
                        call.cancel()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
        }

        val responseJson = response.body()?.string()

        val responsePath = gson.fromJson<List<GeoCoordinateDto>>(
            responseJson,
            object : TypeToken<List<GeoCoordinateDto>>() {}.type
        )

        return responsePath
    }
}
