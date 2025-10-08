package com.fisken_astet.fikenastet.ui.auth

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.permission.PermissionHandler
import com.fisken_astet.fikenastet.base.permission.Permissions
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.BindingUtils.preventMultipleClick
import com.fisken_astet.fikenastet.base.utils.BindingUtils.showOrHidePassword
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.SignupModel
import com.fisken_astet.fikenastet.databinding.CameraGalleryBottomSheetBinding
import com.fisken_astet.fikenastet.databinding.FragmentSignupBinding
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    private val viewmodel: AuthCommonVM by viewModels()
    private var isAgeClicked: Boolean = false
    private var isTermsClicked: Boolean = false

    // image handling
    private lateinit var cameraGalleryBottomSheet: BaseCustomBottomSheet<CameraGalleryBottomSheetBinding>
    private var imageMultiplatform: MultipartBody.Part? = null
    private var photoFile: File? = null
    private var photoURI: Uri? = null
    private var deviceToken: String? = null
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_signup
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, {
            when (it?.id) {
                R.id.ivBack -> {
                    sharedPrefManager.clear()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.auth_navigation, true)
                        .build()

                    findNavController().navigate(R.id.fragmentSplash, null, navOptions)

                }

                R.id.ivPassword -> {
                    showOrHidePassword(binding.edtPassword, binding.ivPassword)
                }

                R.id.ivConfirmPassword -> {
                    showOrHidePassword(binding.edtConfirmPassword, binding.ivConfirmPassword)
                }

                R.id.ivTickAge, R.id.tvAgeCheck -> {

                    isAgeClicked = !isAgeClicked
                    binding.ageCheck = isAgeClicked
                }

                R.id.ivTickPrivacy, R.id.tvPrivacy -> {
                    isTermsClicked = !isTermsClicked
                    binding.termsCheck = isTermsClicked
                }

                R.id.tvUploadPic -> {
                    cameraGalleryBottomSheet.show()
                }

                R.id.tvRegisterLink -> {
                    findNavController().popBackStack()
                }


                R.id.consRegisterButton -> {
                    preventMultipleClick(it)
                    if (validate()) {
                        signupApiCall()
                    }
                }
            }
        })
    }

    private fun initObserver() {
        viewmodel.observeCommon.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it?.message) {
                        "SIGNUP" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<SignupModel>(it.data.toString())
                                if (myDataModel?.success == 200) {
                                    sharedPrefManager.setToken(myDataModel.access_token.toString())
                                    showToast(myDataModel.message.toString())
                                    val bundle = Bundle()
                                    bundle.putString("Form", "Register")
                                    bundle.putString("Email", binding.edtEmail.text.toString())
                                    findNavController().navigate(R.id.fragmentVerify, bundle)
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

    private fun initView() {
        initBottomSheet()
        getFCMToken()
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
                        convertImageToMultipart(photoURI!!)?.let { part ->
                            imageMultiplatform = part
//                            photoUploadApi()
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

    /** Get Firebase Token **/
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            deviceToken = it.result
            Log.d("deviceToken", it.result)
        }
    }


    private fun validate(): Boolean {
//        if (imageMultiplatform == null) {
//            showToast("Please add image to continue")
//            return false
//        }
        if (binding.edtUsername.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid username")
            return false
        } else if (binding.edtEmail.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid email address")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text?.trim().toString())
                .matches()
        ) {
            showToast("Please enter valid email address")
            return false
        } else if (binding.edtPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid password")
            return false
        } else if (binding.edtPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid password of length 8")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Password must contain atleast one digit")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.edtPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.edtPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Password must contain atleast one special character")
            return false
        } else if (binding.edtConfirmPassword.text?.trim().toString().isEmpty()) {
            showToast("Please enter valid confirm password")
            return false
        } else if (binding.edtConfirmPassword.text?.trim().toString().length < 8) {
            showToast("Please enter valid confirm password of length 8")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isDigit() }) {
            showToast("Confirm Password must contain atleast one digit")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isLowerCase() }) {
            showToast("Confirm Password must contain atleast one lowercase letter")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString().any { it.isUpperCase() }) {
            showToast("Confirm Password must contain atleast one uppercase letter")
            return false
        } else if (!binding.edtConfirmPassword.text?.trim().toString()
                .any { "!#$%&()*+,-.:;<=>?@[\\]^_|~".contains(it) }
        ) {
            showToast("Confirm Password must contain atleast one special character")
            return false
        } else if (binding.edtPassword.text?.trim()
                .toString() != binding.edtConfirmPassword.text?.trim().toString()
        ) {
            showToast("Passwords don't match")
            return false
        } else if (!isAgeClicked) {
            showToast("Please agree to older age")
            return false
        } else if (!isTermsClicked) {
            showToast("Please agree to terms of service and privacy")
            return false
        } else {
            return true
        }
    }

    /** api call **/
    private fun signupApiCall() {
        if (deviceToken != null && Constants.latitude != null && Constants.longitude != null) {
            val request = HashMap<String, RequestBody>()
            request["username"] = binding.edtUsername.text.toString().trim().toRequestBody()
            request["email"] = binding.edtEmail.text.toString().trim().toRequestBody()
            request["password"] = binding.edtPassword.text.toString().trim().toRequestBody()
            request["confirm_password"] = binding.edtPassword.text.toString().trim().toRequestBody()
            request["device_type"] = 1.toString().toRequestBody()
            request["device_token"] = deviceToken.toString().toRequestBody()
            request["latitude"] = Constants.latitude.toRequestBody()
            request["longitude"] = Constants.longitude.toRequestBody()
            viewmodel.signupApi(Constants.SIGNUP, request, imageMultiplatform)
        }
    }

}