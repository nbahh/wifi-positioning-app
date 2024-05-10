package com.example.wifioutside.ui.manual

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wifioutside.DataLogger
import com.example.wifioutside.MainActivityViewModel
import com.example.wifioutside.R
import com.example.wifioutside.api.client.WigleClient
import com.example.wifioutside.data.core.GpsLocation
import com.example.wifioutside.data.core.WifiScanResult
import com.example.wifioutside.data.core.WigleCalculatedLocation
import com.example.wifioutside.databinding.FragmentManualBinding
import com.example.wifioutside.databinding.FragmentWigleBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException

class ManualFragment : Fragment() {

    private var _binding: FragmentManualBinding? = null

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentManualBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textGpsValue.text = ""
        binding.textGpsStatusValue.text = "Not available"

        binding.textWigleValue.text = ""
        binding.textWigleStatusValue.text = "Not available"

        val wigleButton = binding.buttonWigle
        val gpsButton = binding.buttonGpsCompare

        wigleButton.isEnabled = false
        gpsButton.isEnabled = false

        activityViewModel.wigleEstimation.observe(viewLifecycleOwner) { result ->
            if (result == null) {
                binding.textWigleStatusValue.text = "Not available"
                wigleButton.isEnabled = false
            } else {
                binding.textWigleStatusValue.text = "Available"
                wigleButton.isEnabled = true
            }
        }

        activityViewModel.gpsLocation.observe(viewLifecycleOwner) { result ->
            if (result == null) {
                binding.textGpsStatusValue.text = "Not available"
                gpsButton.isEnabled = false
            } else {
                binding.textGpsStatusValue.text = "Available"
                gpsButton.isEnabled = true
            }
        }

        configureButtons()

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun configureButtons() {
        binding.saveButton.setOnClickListener {
            try {
                val latitude = binding.textLatitude.text.toString().toDouble()
                val longitude = binding.textLongitude.text.toString().toDouble()
                if (latitude < -90 || latitude > 90) throw NumberFormatException()
                if (longitude < -180 || longitude > 180) throw NumberFormatException()
                activityViewModel.updateManualLocation(GpsLocation(latitude, longitude))
                Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_SHORT).show()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireActivity(), "Invalid coordinates format", Toast.LENGTH_SHORT).show()
                binding.textLatitude.setText("")
                binding.textLongitude.setText("")
            }
        }

        binding.buttonGpsCompare.setOnClickListener {
            val manualLocation = activityViewModel.manualLocation.value
            if (manualLocation == null) {
                Toast.makeText(requireActivity(), "Manual coordinates missing", Toast.LENGTH_SHORT).show()
            } else {
                val gpsLocation = activityViewModel.gpsLocation.value
                val results = FloatArray(3)
                Location.distanceBetween(gpsLocation!!.latitude,
                    gpsLocation.longitude,
                    manualLocation.latitude,
                    manualLocation.longitude,
                    results
                )
                binding.textGpsValue.text = "${results[0]} meters."
                DataLogger.logGpsManualAccuracy(requireContext(), gpsLocation, manualLocation, results[0])
            }
        }

        binding.buttonWigle.setOnClickListener {
            val manualLocation = activityViewModel.manualLocation.value
            if (manualLocation == null) {
                Toast.makeText(requireActivity(), "Manual coordinates missing", Toast.LENGTH_SHORT).show()
            } else {
                val wigleLocation = activityViewModel.wigleEstimation.value
                val results = FloatArray(3)
                Location.distanceBetween(wigleLocation!!.trilat,
                    wigleLocation.trilong,
                    manualLocation.latitude,
                    manualLocation.longitude,
                    results
                )
                binding.textWigleValue.text = "${results[0]} meters."
                DataLogger.logWigleManualAccuracy(requireContext(), wigleLocation, manualLocation, results[0])
            }
        }
    }



}