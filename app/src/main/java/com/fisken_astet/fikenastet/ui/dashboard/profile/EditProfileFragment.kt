package com.fisken_astet.fikenastet.ui.dashboard.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.permission.PermissionHandler
import com.fisken_astet.fikenastet.base.permission.Permissions
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ProfileCompleteModel
import com.fisken_astet.fikenastet.databinding.CameraGalleryBottomSheetBinding
import com.fisken_astet.fikenastet.databinding.FragmentEditProfileBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.io.extension

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    private val viewModel: SettingsVM by viewModels()
    // image handling
    private lateinit var cameraGalleryBottomSheet: BaseCustomBottomSheet<CameraGalleryBottomSheetBinding>
    private var imageMultiplatform: MultipartBody.Part? = null
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_edit_profile
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initObserver()
        initOnClick()
    }

    private fun initView(){
        val user =sharedPrefManager.getLoginData()
        if (user!=null){
            binding.etUsername.setText(user.email ?: "")
            binding.etName.setText(user.username ?: "")
            binding.etLocation.setText(user.location?:"")
            binding.etDesc.setText(user.short_bio?:"")
            if (!user.user_links.isNullOrEmpty()){
                binding.etAddLinks.setText(user.user_links[0]?.url?:"")
            }
            if (user.profile_picture != null) {
                Glide.with(requireContext()).load(user.profile_picture).placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(binding.ivImage2)
            }


        }
        // bottom sheet
        initBottomSheet()

        // enable text scroll
        binding.etDesc.movementMethod = ScrollingMovementMethod()
        binding.etDesc.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    private fun initObserver(){
        viewModel.settingsObserver.observe(viewLifecycleOwner){
            when(it?.status){
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "UPDATE_PROFILE"->{
                            val myDataModel = BindingUtils.parseJson<ProfileCompleteModel>(it.data.toString())
                            if(myDataModel!=null){
                                if (myDataModel.success==200){
                                    sharedPrefManager.setLoginData(myDataModel.user!!)
                                    showToast(myDataModel.message.toString())
                                    val resultIntent = Intent()
                                    resultIntent.putExtra("isProfileUpdated", true)
                                    requireActivity().setResult(RESULT_OK, resultIntent)
                                    requireActivity().finish()
                                }
                            }

                        }
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    }
                    catch (e:Exception){
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }
                else -> {

                }
            }
        }

    }

    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
                R.id.tvImageTitle->{
                    cameraGalleryBottomSheet.show()
                }
                R.id.ivLocation -> {
                    // open locator
                    openLocationPicker()
                }

                R.id.consButton->{
                    if (validate()){
                        updateProfileApiCall()
                    }
                }
            }
        }

    }

    /** handle bottom sheet **/
    private fun initBottomSheet() {
        cameraGalleryBottomSheet =
            BaseCustomBottomSheet(requireActivity(), R.layout.camera_gallery_bottom_sheet) {
                when (it.id) {
                    R.id.openCamara, R.id.openCamaraImage -> {
                        openCamera()
                        cameraGalleryBottomSheet.dismiss()
                    }

                    R.id.icon_emoji_new, R.id.tvChooseFromGallery -> {
                        if (!BindingUtils.hasPermissions(
                                requireActivity(), BindingUtils.permissions
                            )
                        ) {
                            permissionResultLauncher.launch(BindingUtils.permissions)
                        } else {
                            selectImage()
                        }
                        cameraGalleryBottomSheet.dismiss()
                    }
                }

            }
        cameraGalleryBottomSheet.behavior.isDraggable = true
        cameraGalleryBottomSheet.setCancelable(true)
    }

    // launcher
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            action = Intent.ACTION_GET_CONTENT
        }
        pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    // gallery launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.ivImage2.setImageURI(uri)
                    imageMultiplatform = convertImageToMultipart(uri)
                }
            }
        }

    private var allGranted = false
    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            for (it in permissions.entries) {
                it.key
                val isGranted = it.value
                allGranted = isGranted
            }
            when {
                allGranted -> {
                    selectImage()
                }

            }
        }

    // camera intent
    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Permissions.check(
                requireActivity(), Manifest.permission.CAMERA, 0, object : PermissionHandler() {
                    override fun onGranted() {
                        openCameraIntent()
                    }
                })
        } else {
            openCameraIntent()
        }
    }


    private fun openCameraIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = BindingUtils.createImageFile(requireActivity())
        val authority = "${requireActivity().packageName}.provider"
        val photoURI: Uri = FileProvider.getUriForFile(
            requireActivity(), authority, photoFile!!
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        cameraLauncher.launch(cameraIntent)

    }

    // camera launcher
    private var cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode === RESULT_OK) {
                try {
                    photoURI = photoFile!!.absoluteFile.toUri()
                    if (photoURI != null) {
                        binding.ivImage2.setImageURI(photoURI)
                        convertImageToMultipart(photoURI!!).let { part ->
                            imageMultiplatform = part
                        }

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

    /** convert to multipart **/
    private fun convertImageToMultipart(imageUri: Uri): MultipartBody.Part {
        val file = FileUtil.getTempFile(requireActivity(), imageUri)
        val fileName =
            "${file!!.nameWithoutExtension}_${System.currentTimeMillis()}.${file.extension}"
        val newFile = File(file.parent, fileName)
        file.renameTo(newFile)
        return MultipartBody.Part.createFormData(
            "profile_picture", newFile.name, newFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }

    // locator
    private val autocompleteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    binding.etLocation.setText(place.address ?: place.displayName)
                    Log.d("Places", "Selected: ${place.address ?: place.displayName}")
                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                val intent = result.data
                if (intent != null) {
                    val status = Autocomplete.getStatusFromIntent(intent)
                    Log.e("Places", status.statusMessage ?: "Error")
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d("Places", "Autocomplete cancelled")
            }
        }
    }

    private fun openLocationPicker() {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireContext())
        autocompleteLauncher.launch(intent)
    }

    private fun validate(): Boolean{
        if (binding.etName.text.toString().trim().isNullOrEmpty()){
            showToast("Please enter valid username")
            return false
        }
        else if (binding.etLocation.text.toString().trim().isNullOrEmpty()){
            showToast("Please enter valid location")
            return false
        }
        else if (binding.etDesc.text.toString().trim().isNullOrEmpty()){
            showToast("Please enter valid bio")
            return false
        }
        else{
            return true
        }
    }

    private fun updateProfileApiCall(){
        val request = HashMap<String, RequestBody>()
        if (binding.etAddLinks.text.toString().trim().isNotEmpty()){
            if (isValidUrl(binding.etAddLinks.text.toString().trim())){
                val siteName = extractSiteNameSmart(binding.etAddLinks.text.toString().trim())?.lowercase() ?: "link"
                request["add_links[0][title]"] = siteName.toRequestBody()
                request["add_links[0][url]"] = binding.etAddLinks.text.toString().trim().toRequestBody()
            }
            else{
                showToast("Url is not valid")
                return
            }
        }
        request["type"]="2".toRequestBody()
        request["username"]=binding.etName.text.toString().trim().toRequestBody()
        request["location"]=binding.etLocation.text.toString().trim().toRequestBody()
        request["short_bio"]=binding.etDesc.text.toString().trim().toRequestBody()
        viewModel.updateProfileApi(Constants.COMPLETE_PROFILE,request,imageMultiplatform)
    }

    private fun extractSiteNameSmart(urlString: String): String? {
        var formatted = urlString.trim()

        if (!formatted.startsWith("http://", ignoreCase = true) &&
            !formatted.startsWith("https://", ignoreCase = true)) {
            formatted = "https://$formatted"
        }

        return try {
            val url = java.net.URL(formatted)
            val host = url.host ?: return null
            val parts = host.split(".")

            if (parts.size >= 3 && parts.last().length == 2 && parts[parts.size - 2].length <= 3) {
                // e.g., "www.google.co.uk" → "google"
                parts[parts.size - 3].replaceFirstChar { it.uppercase() }
            } else if (parts.size >= 2) {
                parts[parts.size - 2].replaceFirstChar { it.uppercase() }
            } else {
                host.replaceFirstChar { it.uppercase() }
            }
        } catch (e: Exception) {
            null
        }
    }

//    fun isValidUrl(urlString: String): Boolean {
//        return try {
//            var formatted = urlString.trim()
//
//            if (!formatted.startsWith("http://", ignoreCase = true) &&
//                !formatted.startsWith("https://", ignoreCase = true)) {
//                formatted = "https://$formatted"
//            }
//
//            val url = java.net.URL(formatted)
//            url.toURI() // syntax check
//
//            val host = url.host ?: return false
//
//            // Must contain a dot and a valid TLD (e.g., .com, .org, .co.uk)
//            val parts = host.split(".")
//            if (parts.size < 2) return false
//
//            val tld = parts.last()
//            val tldRegex = Regex("^[a-zA-Z]{2,63}$")
//
//            host.contains('.') &&
//                    !host.startsWith(".") &&
//                    !host.endsWith(".") &&
//                    tldRegex.matches(tld)
//        } catch (e: Exception) {
//            false
//        }
//    }

    private fun isValidUrl(urlString: String): Boolean {
        return try {
            val formatted = if (!urlString.startsWith("http://", ignoreCase = true) &&
                !urlString.startsWith("https://", ignoreCase = true)) {
                "https://$urlString"
            } else {
                urlString
            }

            val url = java.net.URL(formatted)
            url.toURI() // Checks for URI syntax validity too
            true
        } catch (e: Exception) {
            false
        }
    }

}