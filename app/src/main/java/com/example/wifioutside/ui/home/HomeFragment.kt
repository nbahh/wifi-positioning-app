package com.example.wifioutside.ui.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wifioutside.MainActivity
import com.example.wifioutside.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var wifiManager : WifiManager;

    private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE)

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("DEBUG", requireActivity().localClassName)

        wifiManager = requireActivity().getSystemService(Context.WIFI_SERVICE) as WifiManager


        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(requireActivity(), "Wi-Fi is disabled.", Toast.LENGTH_SHORT).show()
        }

        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val success = requireActivity().intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireContext().registerReceiver(wifiScanReceiver, intentFilter)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        configureButtons()

        val wifiListView: LinearLayout = binding.wifiList
        (requireActivity() as MainActivity).model.wifiList.observe(viewLifecycleOwner) {
            wifiListView.removeAllViews();
            for (wifiEntry in it) {
                val networkSSID: String = wifiEntry.SSID
                val button = Button(requireActivity())
                button.text = networkSSID
                wifiListView.addView(button)
                button.setOnClickListener {
                    Toast.makeText(requireActivity(), wifiEntry.level, Toast.LENGTH_LONG).show()
                }
            }
        }
        return root
    }

    private fun scanSuccess() {

        Toast.makeText(requireActivity(), "Wi-Fi Capture Successfully.", Toast.LENGTH_SHORT).show()
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CHANGE_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireActivity(), "No permissions", Toast.LENGTH_SHORT).show()

        } else {
            val scanResult : List<ScanResult> = wifiManager.scanResults;
            (requireActivity() as MainActivity).model.updateWifiList(scanResult.toMutableList())
        }

    }

    private fun scanFailure() {
        Toast.makeText(requireActivity(), "Wi-Fi Capture Not success", Toast.LENGTH_SHORT).show()
    }

    private fun configureButtons() {
        binding.scanButton.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val permission: Int = ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                        REQUIRED_PERMISSIONS, 123)
                }
            }
            val success = wifiManager.startScan()
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
        binding.statusButton.setOnClickListener {
            val statusInfo = when (wifiManager.wifiState) {
                WifiManager.WIFI_STATE_DISABLING -> "Disabling"
                WifiManager.WIFI_STATE_DISABLED -> "Disabled"
                WifiManager.WIFI_STATE_ENABLING -> "ENABLING"
                WifiManager.WIFI_STATE_ENABLED -> "ENABLED"
                else -> {
                    "Unknown"
                }
            }
            Toast.makeText(requireActivity(), "Status: $statusInfo", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}