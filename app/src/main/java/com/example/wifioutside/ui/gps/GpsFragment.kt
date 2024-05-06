package com.example.wifioutside.ui.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wifioutside.MainActivityViewModel
import com.example.wifioutside.data.core.GpsLocation
import com.example.wifioutside.databinding.FragmentGpsBinding
import com.google.android.gms.location.LocationServices
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class GpsFragment : Fragment() {

    private var _binding: FragmentGpsBinding? = null

    private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGpsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calculateButton: Button = binding.calculateButton
        val testButton: Button = binding.testButton

        val accuracyValueText: TextView = binding.wigleAccuracyValueTextView

        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean -> if (isGranted) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION),
                    200)
            }
                }
        }

        calculateButton.setOnClickListener {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val permission: Int = ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                        REQUIRED_PERMISSIONS, 123)
                }
            }

            LocationServices.getFusedLocationProviderClient(requireActivity())
                .lastLocation.addOnSuccessListener {
                        location : Location? -> location?.let {
                    val gpsCoordinates = GpsLocation(location.latitude, location.longitude)
                    Log.d("DEBUG", "The coordinates are ${gpsCoordinates.latitude} and ${gpsCoordinates.longitude}")
                    activityViewModel.updateGpsLocation(gpsCoordinates)
                    updateLocationValues(gpsCoordinates)
                        }
                }
        }

        testButton.setOnClickListener {
            if (activityViewModel.gpsLocation.value == null) {
                Toast.makeText(requireActivity(), "GPS data missing.", Toast.LENGTH_SHORT).show()
            }
            else if (activityViewModel.wigleEstimation.value == null) {
                Toast.makeText(requireActivity(), "WiGLE data missing.", Toast.LENGTH_SHORT).show()
            }
            else {
                val gpsData = activityViewModel.gpsLocation.value!!
                val wigleData = activityViewModel.wigleEstimation.value!!
                val results = FloatArray(3)
                Location.distanceBetween(gpsData.latitude,
                    gpsData.longitude,
                    wigleData.trilat,
                    wigleData.trilong,
                    results
                )
                accuracyValueText.text = "${results[0]} meters."
            }
        }

        return root
    }

    fun updateLocationValues(gpsLocation: GpsLocation) {
        val latitudeValueText: TextView = binding.latitudeValueTextView
        val longitudeValueText: TextView = binding.longitudeValueTextView

        Log.d("DEBUG", "Setting up values")

        latitudeValueText.text = gpsLocation.latitude.toString()
        longitudeValueText.text = gpsLocation.longitude.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}