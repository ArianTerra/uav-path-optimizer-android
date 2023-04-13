package com.epslilonlabs.uavpathoptimizer.presentation.composable

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import com.epslilonlabs.uavpathoptimizer.R
import com.epslilonlabs.uavpathoptimizer.presentation.MainViewModel
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GoogleMap(viewModel: MainViewModel) {
    val mapView = rememberMapViewWithLifecycle()
    val markers = remember { mutableStateListOf<LatLng>() }
    var path = emptyList<LatLng>()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(0.9f)) {
            AndroidView({ mapView }) { view ->
                CoroutineScope(Dispatchers.Main).launch {
                    val map = view.getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        googleMap.setOnMapClickListener { latLng ->
                            markers.add(latLng)
                            googleMap.addMarker(MarkerOptions().position(latLng))
                        }
                    }
                }
            }

        }
        Button(
            modifier = Modifier.weight(0.1f).padding(16.dp),
            onClick = {
                viewModel.viewModelScope.launch {
                    path = viewModel.sendPathRequest(markers)
                    mapView.getMapAsync { googleMap ->
                        googleMap.addPolyline(PolylineOptions().addAll(path))
                    }
                }
            }
        ) {
            Text("Send Path Request")
        }
    }
}


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    // on below line initializing our maps view with id.
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    // on below line initializing lifecycle variable.
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    // on below line adding observer for lifecycle.
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    // returning map view on below line.
    return mapView
}

@Composable
// creating a function for map lifecycle observer.
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        // on below line adding different events for maps view
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }