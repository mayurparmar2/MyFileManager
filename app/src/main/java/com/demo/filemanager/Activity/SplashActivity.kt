package com.demo.filemanager.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.demo.filemanager.R

class SplashActivity : AppCompatActivity() {
    var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            if (activityResult.resultCode == -1) {
                if (Build.VERSION.SDK_INT >= 30) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(
                            this@SplashActivity,
                            "Permission granted",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@ActivityResultCallback
                    }
                    this@SplashActivity.startActivity(
                        Intent(
                            this@SplashActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }
            } else if (activityResult.resultCode == 0) {
                this@SplashActivity.startActivity(
                    Intent(
                        this@SplashActivity,
                        SlideActivity::class.java
                    )
                )
                finish()
            }
        })

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_splash)
        if (checkPermission()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        requestPermission()

    }
    fun checkPermissionReadStorage(activity: Activity?) {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                "android.permission.READ_EXTERNAL_STORAGE"
            ) != 0
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    "android.permission.READ_EXTERNAL_STORAGE"
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, "android.permission.WRITE_EXTERNAL_STORAGE"
                )
            ) {
                startActivity(Intent(this, MainActivity::class.java))
                return
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"
                    ),
                    MY_PERMISSIONS_REQUEST_READ_STORAGE
                )
                return
            }
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onRequestPermissionsResult(i: Int, strArr: Array<String>, iArr: IntArray) {
        super.onRequestPermissionsResult(i, strArr, iArr)
        if (i == 111 && iArr.size > 0) {
            val z = iArr[0] == 0
            val z2 = iArr[1] == 0
            if (z && z2) {
                startActivity(Intent(this, MainActivity::class.java))
                return
            }
            Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }

    public override fun onActivityResult(i: Int, i2: Int, intent: Intent?) {
        super.onActivityResult(i, i2, intent)
        if (i != 2296 || Build.VERSION.SDK_INT < 30) {
            return
        }
        if (Environment.isExternalStorageManager()) {
            startActivity(Intent(this, MainActivity::class.java))
            return
        }
        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
        finishAffinity()
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 30) {
            Environment.isExternalStorageManager()
        } else ContextCompat.checkSelfPermission(
            this,
            "android.permission.READ_EXTERNAL_STORAGE"
        ) == 0 && ContextCompat.checkSelfPermission(
            this,
            "android.permission.WRITE_EXTERNAL_STORAGE"
        ) == 0
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                val intent = Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION")
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(
                    Uri.parse(
                        String.format(
                            "package:%s",
                            applicationContext.packageName
                        )
                    )
                )
                activityResultLauncher.launch(intent)
                return
            } catch (unused: Exception) {
                val intent2 = Intent()
                intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION")
                activityResultLauncher.launch(intent2)
                return
            }
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
            ),
            111
        )
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_STORAGE = 9920
        private const val NUM_BYTES_NEEDED_FOR_MY_APP: Long = 10485760
        private const val PERMISSION_REQUEST_CODE = 111
    }
}
