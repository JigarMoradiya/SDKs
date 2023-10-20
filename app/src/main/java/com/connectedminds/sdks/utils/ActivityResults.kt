package com.connectedminds.sdks.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import java.util.*


// permission

fun hasPermission(context: Context, permission:String): Boolean {
    return (ActivityCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED)
}
fun Context.checkPermissions(type: String,launcherPermission: ActivityResultLauncher<Array<String>>) : Boolean {
        var permission = true
        val listPermissionsNeeded = ArrayList<String>()
        when (type) {
            //these types are only for the AppSync Thing

        }
        if (listPermissionsNeeded.isNotEmpty()) {
            launcherPermission.launch(listPermissionsNeeded.toArray(Array(listPermissionsNeeded.size) { "it = $it" }))
            permission = false
        }
        return permission
    }
