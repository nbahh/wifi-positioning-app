package com.example.wifioutside.ui.manual

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wifioutside.MainActivityViewModel
import com.example.wifioutside.R
import com.example.wifioutside.api.client.WigleClient
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

class ManualFragment : Fragment() {

    private var _binding: FragmentManualBinding? = null

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


        return root
    }


}