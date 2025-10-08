package com.fisken_astet.fikenastet.ui.auth

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.permission.PermissionHandler
import com.fisken_astet.fikenastet.base.permission.Permissions
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ProfileCompleteModel
import com.fisken_astet.fikenastet.databinding.CameraGalleryBottomSheetBinding
import com.fisken_astet.fikenastet.databinding.FragmentProfileReadyBinding
import com.fisken_astet.fikenastet.databinding.HolderSkillsBinding
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class ProfileReadyFragment : BaseFragment<FragmentProfileReadyBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()

    // skills adapter
    private lateinit var skillsAdapter: SimpleRecyclerViewAdapter<String, HolderSkillsBinding>
    private var isClicked = false

    // image handling
    private lateinit var cameraGalleryBottomSheet: BaseCustomBottomSheet<CameraGalleryBottomSheetBinding>
    private var imageMultiplatform: MultipartBody.Part? = null
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile_ready
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, {
            when (it?.id) {
                R.id.ivBack -> {
                    findNavController().popBackStack()
                }
                R.id.tvUploadPic -> {
                    cameraGalleryBottomSheet.show()
                }

                R.id.ivLocation -> {
                    // open locator
                    openLocationPicker()
                }

                R.id.ivSkillArrow, R.id.tvSkillLevel -> {
                    isClicked = !isClicked
                    binding.rvSkills.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.ivSkillArrow.rotation = if (isClicked) 180f else 0f
                }

                R.id.consSaveAndContinue -> {
                    if (validate()) {
                        completeProfileApi()
                    }
                }
            }
        })
    }

    private fun initView() {
        val user = sharedPrefManager.getLoginData()
        if (user != null) {
            if (user.profile_picture != null) {
                Glide.with(requireContext()).load(user.profile_picture).placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(binding.ivPersonImage)
            }
            if (!user.username.isNullOrEmpty()) {
                binding.edtEmail.setText(user.username)
                binding.tvWelcome.setText("Welcome, ${user.username}!")
            }
        }
        // adapter
        initAdapter()
        // bottom sheet
        initBottomSheet()

        binding.edtShortBio.movementMethod = ScrollingMovementMethod()
        binding.edtShortBio.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
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
                    binding.ivPersonImage.setImageURI(uri)
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
                        binding.ivPersonImage.setImageURI(photoURI)
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

    /** adapter **/
    private fun initAdapter() {
        val skillsList = listOf<String>("Beginner", "Advanced", "Expert")
        skillsAdapter = SimpleRecyclerViewAdapter(R.layout.holder_skills, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.tvLevel -> {
                    binding.rvSkills.visibility = View.GONE
                    binding.ivSkillArrow.rotation = 0f
                    isClicked = false
                    binding.tvSkillLevel.text = m
                }
            }
        }
        skillsAdapter.list = skillsList
        binding.rvSkills.adapter = skillsAdapter
    }

    private fun validate(): Boolean {
        if (binding.edtEmail.text.toString().trim().isNullOrEmpty()) {
            showToast("Please enter valid username")
            return false
        }
//        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text?.trim().toString())
//                .matches()
//        ) { showToast("Please enter valid email address")
//            return false
//        }
        else if (binding.edtLocation.text.toString().isNullOrEmpty()) {
            showToast("Please enter valid location")
            return false
        } else if (binding.edtShortBio.text.toString().isNullOrEmpty()) {
            showToast("Please enter valid bio")
            return false
        } else if (binding.tvSkillLevel.text.contains("Skill level")) {
            showToast("Please choose your skill level")
            return false
        } else {
            return true
        }
    }

    /** handle observer **/
    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "COMPLETE_PROFILE" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<ProfileCompleteModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.success == 200) {
                                        if (myDataModel.user != null) {
                                            sharedPrefManager.setLoginData(myDataModel.user)
                                        }
                                        val bundle = Bundle()
                                        bundle.putString("Form", "ProfileReadyFragment")
                                        findNavController().navigate(
                                            R.id.fragmentAccountCreated,
                                            bundle
                                        )
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }

                else -> {

                }
            }
        }
    }

    /** api call **/
    private fun completeProfileApi() {
        val skillLevel = when (binding.tvSkillLevel.text.toString()) {
            "Beginner" -> "1"
            "Advanced" -> "2"
            "Expert" -> "3"
            else -> {
                "0"
            }
        }
            val request = HashMap<String, RequestBody>()
            request["type"] = "1".toRequestBody()
            request["location"] = binding.edtLocation.text.toString().trim().toRequestBody()
            request["username"] = binding.edtEmail.text.toString().trim().toRequestBody()
            request["short_bio"] = binding.edtShortBio.text.toString().trim().toRequestBody()
            request["skill_level"] = skillLevel.toRequestBody()
//            request["latitude"] = Constants.latitude.toRequestBody()
//            request["longitude"] = Constants.longitude.toRequestBody()
            viewmodel.completeProfileApi(Constants.COMPLETE_PROFILE, request, imageMultiplatform)
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
                    binding.edtLocation.setText(place.address ?: place.displayName)
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

}