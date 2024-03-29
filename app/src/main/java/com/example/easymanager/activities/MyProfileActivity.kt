package com.example.easymanager.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.easymanager.R
import com.example.easymanager.firebase.FirestoreClass
import com.example.easymanager.models.FireUser
import com.example.easymanager.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    private var mSelectedImageFileURI: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: FireUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirestoreClass().loadUserData(this)

        iv_user_image_profile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        btn_update_profile.setOnClickListener {
            if (mSelectedImageFileURI != null) {
                uploadUserImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                Constants.showImageChooser(this)
            }
        } else {
            Toast.makeText(
                this,
                "Oops, you just denied the permission for storage. You can change this in Settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImageFileURI = data.data

            try {
            Glide
                .with(this@MyProfileActivity)
                .load(mSelectedImageFileURI)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_grey)
                .into(iv_user_image_profile)
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_arrow_white_24dp)
            title = resources.getString(R.string.my_profile)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setUserDataInUI(user: FireUser) {
        mUserDetails = user

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_place_holder_grey)
            .into(iv_user_image_profile)

        et_name_profile.setText(user.name)
        et_email.setText(user.email)
        if (user.mobile != 0L) {
            et_mobile_profile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()

        var anyChangesMade = false

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }

        if (et_name_profile.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = et_name_profile.text.toString()
            anyChangesMade = true
        }

        if (et_mobile_profile.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = et_mobile_profile.text.toString().toLong()
            anyChangesMade = true
        }

        if (anyChangesMade) {
            FirestoreClass().updateUserProfileData(this, userHashMap)
        } else {
            hideProgressDialog()
        }
    }

    private fun uploadUserImage() {
        showProgressDialog("Uploading...")

        if (mSelectedImageFileURI != null) {

            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference.child(
                    "USER_IMAGE" + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(this, mSelectedImageFileURI)
                )

            sRef.putFile(mSelectedImageFileURI!!).addOnSuccessListener {
                taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.i("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()

                hideProgressDialog()
            }
        }
    }

    fun profileUpdateSuccess() {
        hideProgressDialog()

        setResult(Activity.RESULT_OK)
        finish()
    }
}