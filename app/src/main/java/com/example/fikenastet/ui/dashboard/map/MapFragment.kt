package com.example.fikenastet.ui.dashboard.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.BaseCustomBottomSheet
import com.example.fikenastet.databinding.CommonBottomLayoutBinding
import com.example.fikenastet.databinding.FragmentMapBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {
    private val viewModel: MapFragmentVM by viewModels()
    private lateinit var googleMap: GoogleMap
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_map
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
    private fun initOnClick() {
        val bottomSheet = binding.bottomSheetLayout
        val behavior = BottomSheetBehavior.from(bottomSheet).apply {
            isHideable = true // Required for STATE_HIDDEN
            state = BottomSheetBehavior.STATE_HIDDEN // Start hidden if desired
        }

        viewModel.onClick.observe(viewLifecycleOwner) { view ->
            when (view?.id) {
                R.id.ivFilter -> {
                    behavior.state = when (behavior.state) {
                        BottomSheetBehavior.STATE_EXPANDED,
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> BottomSheetBehavior.STATE_HIDDEN

                        else -> BottomSheetBehavior.STATE_EXPANDED
                    }
                }
                R.id.tvSeeAll ->{
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "MapSeeAll")
                    startActivity(intent)
                }
            }
        }
    }



    private fun initView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment?
                ?: SupportMapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().replace(R.id.map_container, it).commit()
                }
        val bigMarker = bitmapDescriptorFromVector(R.drawable.iv_marker, 60, 60)
        mapFragment.getMapAsync { gMap ->
            googleMap = gMap
            enableMyLocation()
            // Example marker
            greenSpots.forEach { location ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location.latLng)
                        .title(location.title)
                        .icon(bigMarker)
                        .anchor(0.5f, 1f) // base at bottom center
                )
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(greenSpots[3].latLng, 18f))
        }


    }

    private fun bitmapDescriptorFromVector(
        @DrawableRes vectorResId: Int,
        width: Int,
        height: Int
    ): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
            ?: throw IllegalArgumentException("Resource not found")

        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)

        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableMyLocation()
        } else {
            Toast.makeText(requireContext(), "Location permission is required.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    data class LocationData(val title: String, val latLng: LatLng)

    val greenSpots = listOf(
        LocationData("Park A", LatLng(12.9716, 77.5946)),  // Bangalore
        LocationData("Park B", LatLng(12.9720, 77.5950)),
        LocationData("Park C", LatLng(12.9712, 77.5940)),
        LocationData("Park D", LatLng(12.9725, 77.5935))
    )


}