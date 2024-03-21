package com.example.wifioutside.ui.wigle

import android.annotation.SuppressLint
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.wifioutside.MainActivityViewModel
import com.example.wifioutside.R
import com.example.wifioutside.api.client.WigleClient
import com.example.wifioutside.data.core.WigleCalculatedLocation
import com.example.wifioutside.databinding.FragmentWigleBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WigleFragment : Fragment(), OnMapReadyCallback {

    private val wigleClient: WigleClient = WigleClient()

    private var _binding: FragmentWigleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var mMap: GoogleMap

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWigleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialConfiguration()
        configureButtons()
        configureLiveDataObservers()

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun initialConfiguration() {
        val availabilityText: TextView = binding.availabilityText
        val statusText: TextView = binding.availabilityStatusText
        val latitudeValueText: TextView = binding.wigleLatitudeValue
        val longitudeValueText: TextView = binding.wigleLongitudeValue
        val wigleProgressBar: ProgressBar = binding.wigleLoadingBar

        val wigleButton: Button = binding.mainWigleButton

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        availabilityText.text = "Availability Status: Unavailable"
        statusText.text = "No available WiFi networks found."
        latitudeValueText.text = ""
        longitudeValueText.text = ""
        wigleButton.isEnabled = false
        wigleProgressBar.visibility = View.GONE
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("SetTextI18n")
    private fun configureButtons() {
        val wigleButton: Button = binding.mainWigleButton
        val latitudeValueText: TextView = binding.wigleLatitudeValue
        val longitudeValueText: TextView = binding.wigleLongitudeValue
        val wigleProgressBar: ProgressBar = binding.wigleLoadingBar


        wigleButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    wigleProgressBar.visibility = View.VISIBLE
                }

                val wigleResponse = wigleClient
                    .getNetworkByBSSID(activityViewModel.getStrongestWifiScanResult()?.BSSID!!)


                withContext(Dispatchers.Main) {
                    wigleProgressBar.visibility = View.GONE
                    if (wigleResponse != null) {
                        latitudeValueText.text = wigleResponse.trilat.toString()
                        longitudeValueText.text = wigleResponse.trilong.toString()
                        activityViewModel.updateWigleEstimation(WigleCalculatedLocation(wigleResponse))
                        addPointToMap(wigleResponse.trilat, wigleResponse.trilong)
                    } else {
                        latitudeValueText.text = "Not found!"
                        longitudeValueText.text = "Not found!"
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureLiveDataObservers() {
        val availabilityText: TextView = binding.availabilityText
        val statusText: TextView = binding.availabilityStatusText
        val wigleButton: Button = binding.mainWigleButton
        activityViewModel.wifiList.observe(viewLifecycleOwner) { scanResults ->
            if (scanResults.isEmpty()) {
                availabilityText.text = "Availability Status: Unavailable"
                statusText.text = "No available WiFi networks found."
                wigleButton.isEnabled = false
            } else {
                val strongestNetwork: ScanResult = scanResults.maxWith(Comparator.comparing { it.level })
                availabilityText.text = "Availability Status: Available"
                statusText.text = "Using: ${strongestNetwork.SSID}"
                wigleButton.isEnabled = true
            }
        }
    }

    private fun addPointToMap(lat: Double, long: Double) {
        val coordinates: LatLng = LatLng(lat, long)
        mMap.addMarker(MarkerOptions().position(coordinates).title("Estimated location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}