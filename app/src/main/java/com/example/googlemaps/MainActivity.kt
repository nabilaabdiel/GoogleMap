package com.example.googlemaps

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.base64decrypt
import com.crocodic.core.extension.checkLocationPermission
import com.example.googlemaps.databinding.ActivityMainBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.maps.route.extensions.drawRouteOnMap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : NoViewModelActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(applicationContext, "AIzaSyB1imnmC0gLZc9BKWTrJ1km5ZElt9Q8n2E")
        val autoCompleteFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_auth) as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("Latihan autoCompleted", "Place:${place.name}.${place.id}")
            }

            override fun onError(status: Status) {
                Log.i("Latihan autoCompleted", "An Error Accured: $status")
            }
        })

        binding.mapView.onCreate(savedInstanceState)

        val crocodic = LatLng(-7.0644051, 110.4165274) //Starting point
        val hermina = LatLng(-7.0727976, 110.411677) //Ending point

        binding.mapView.getMapAsync {  googleMap ->
            googleMap.drawRouteOnMap(
                mapsApiKey = getString(R.string.google_api_key).base64decrypt(),
                source = crocodic,
                destination = hermina,
                context = applicationContext
            )
            googleMap.setPadding(100, 100, 100, 100)
        }

        checkLocationPermission {
            listenLocationChange()
        }
    }

    override fun retrieveLocationChange(location: Location) {
        Log.d("lokasi device", "latitude: ${location.latitude} longitude: ${location.longitude}")

        binding.mapView.getMapAsync {

            val latLng = LatLng(location.latitude, location.longitude)

//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
//
//            googleMap.addCircle(
//                CircleOptions()
//                    .center(latLng)
//                    .radius(200.0)
//                    .strokeColor(ContextCompat.getColor(this, R.color.purple_700))
//            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}