package ge.gmegrelishvili.messengerapp.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.model.entity.User
import ge.gmegrelishvili.messengerapp.model.entity.UserUpdate
import ge.gmegrelishvili.messengerapp.model.repository.FirebaseImageRepository
import ge.gmegrelishvili.messengerapp.view.SignInActivity
import ge.gmegrelishvili.messengerapp.view.errors.ViewError
import ge.gmegrelishvili.messengerapp.view.util.ImageUtil
import ge.gmegrelishvili.messengerapp.view.util.ToastWrapper
import ge.gmegrelishvili.messengerapp.viewmodel.MessengerAppViewModel


class ProfileFragment(
    private val fragmentActivity: FragmentActivity
) : Fragment() {

    private val toast = ToastWrapper(fragmentActivity)
    private lateinit var currentUser: User

    private lateinit var usernameEdittext: EditText
    private lateinit var whatIDoEdittext: EditText
    private lateinit var profileImage: ImageView

    private val launchSomeActivity =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data?.data == null) {
                    toast.short(CantFindLocalImageExceptionString)
                    return@registerForActivityResult
                }
                val uri = data.data!!
                val fileInfo = fragmentActivity.contentResolver.openFileDescriptor(uri, "r")
                if (fileInfo == null) {
                    toast.short(CantFindLocalImageExceptionString)
                    return@registerForActivityResult
                }
                if (fileInfo.statSize > FirebaseImageRepository.ImageMaxSize) {
                    toast.short(ImageTooBigExceptionString)
                    return@registerForActivityResult
                }
                viewModel.uploadImage(viewModel.getCurrentUid(), uri) { error ->
                    if (error != null) {
                        toast.short(CantUploadImageExceptionString)
                        return@uploadImage
                    }
                    val bitmap = ImageUtil.loadBitmap(uri, fragmentActivity.contentResolver)
                    if (bitmap == null) {
                        toast.short(CantUploadImageExceptionString)
                        return@uploadImage
                    }
                    profileImage.setImageBitmap(ImageUtil.cropImageRounded(bitmap))
                }
            }
        }

    private val viewModel: MessengerAppViewModel by lazy {
        MessengerAppViewModel.getViewModelProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.fragment_profile, container, false)

        usernameEdittext = createdView.findViewById(R.id.profile_edittext_username)
        whatIDoEdittext = createdView.findViewById(R.id.profile_edittext_what_i_do)
        profileImage = createdView.findViewById(R.id.user_profile_image_holder)

        usernameEdittext.inputType = InputType.TYPE_NULL

        createdView.findViewById<Button>(R.id.profile_sign_out_button).setOnClickListener {
            viewModel.signOut()
            fragmentActivity.startActivity(
                Intent(fragmentActivity, SignInActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }

        profileImage.setOnClickListener {
            launchSomeActivity.launch(Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            })
        }

        toast.long(LoadingUserString)

        viewModel.getUser { user ->
            if (user == null) {
                viewModel.signOut()
                fragmentActivity.finishAffinity()
            } else {
                currentUser = user
                updateProfileUI(createdView, user)
            }
        }

        return createdView
    }

    private fun updateProfileUI(createdView: View, user: User) {
        usernameEdittext.setText(user.username)
        whatIDoEdittext.setText(user.whatIDo)

        toast.long(LoadingImageString)

        viewModel.downloadImage(user.key) { bitmap, exception ->
            if (exception != null) {
                toast.short(CantFindRemoteImageExceptionString)
            } else {
                profileImage.setImageBitmap(ImageUtil.cropImageRounded(bitmap!!))
            }
        }

        createdView.findViewById<Button>(R.id.profile_update_button).setOnClickListener {
            val whatIDo = whatIDoEdittext.text.toString().trim()
            if (toast.shortIf(ViewError.EmptyWhatIDo, whatIDo.isEmpty())) {
                return@setOnClickListener
            }
            val key = viewModel.getCurrentUid()
            val userUpdated = UserUpdate(whatIDo)
            viewModel.updateUser(key, userUpdated) { error ->
                if (error == null) {
                    toast.short(UpdateSuccessExceptionString)
                } else {
                    toast.short(it.toString())
                }
            }
        }
    }

    companion object {
        private const val LoadingUserString = "Loading user info"
        private const val UpdateSuccessExceptionString = "Profile Updated"
        private const val CantFindLocalImageExceptionString =
            "Can't find or load the image on device"
        private const val CantUploadImageExceptionString = "Can't upload image to remote server"
        private const val CantFindRemoteImageExceptionString =
            "Can't find or load the image from remote server"
        private const val LoadingImageString = "Loading image. It will finish soon"
        private const val ImageTooBigExceptionString = "Can't find or load the image"
    }

}