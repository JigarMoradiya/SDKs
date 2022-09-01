package com.example.iiifa_fan_android.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.developer.filepicker.view.FilePickerDialog
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.ForceUpdate
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.ui.view.login.activities.ForceUpdateActivity
import com.example.iiifa_fan_android.ui.view.login.activities.LoginActivity
import com.example.iiifa_fan_android.utils.*
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.text.StringEscapeUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


object CustomFunctions {
    //Convert into JSONObject
    fun convertObjectToJsonObject(content: Any?): JsonObject {
        val gson = Gson()
        val value = gson.toJson(content);
        return gson.fromJson(value, JsonObject::class.java)
    }

    @JvmStatic
    fun roundOffDecimal(number: Double): String? {
//        val df = DecimalFormat("0.#")
//        df.roundingMode = RoundingMode.CEILING
        return BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros()
            .toPlainString()
    }

    @JvmStatic
    fun roundOffDecimalWithCurrency(number: Double, cur: String): String? {
//        val df = DecimalFormat("0.#")
//        df.roundingMode = RoundingMode.CEILING
        val uk = Locale("en", "GB")
        val currency = Currency.getInstance(cur.capitalize())

        Log.e("currency", currency.toString())
//        val symbol = currency.symbol + " " + df.format(number).toDouble()
        return currency.getSymbol(uk) + " " + BigDecimal(number).setScale(
            2,
            BigDecimal.ROUND_HALF_EVEN
        ).stripTrailingZeros().toPlainString()
    }

    //Logout user and clear local storage
    @JvmStatic
    fun handleForbiddenResponse(showingAfterLogout: Boolean? = true) {
        val context: Context = MyApplication.getInstance()
        val prefManager = AppPreferencesHelper(context, Constants.PREF_NAME)
        if (!TextUtils.isEmpty(prefManager.getUserData())) {
            LoginManager.getInstance().logOut()
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // .requestIdToken(getString(R.string.server_client_id))
                    //  .requestIdToken(BuildConfig.google_sign_in_key)
                    .requestEmail()
                    .build()
            GoogleSignIn.getClient(context, gso).signOut()
            prefManager.setUserEmail(null)
            prefManager.setUserData(null)
            prefManager.setUserId(null)
            prefManager.setToken(null)
            prefManager.setNotificationToken(null)

//showingAfterLogout means user was in the app and then user going back so we need to show close icon show user can come back to non logged in flow
            LoginActivity.getInstance(context, false, showingAfterLogout)

        }
    }

    private var pictureImagePath: String? = null

    //Decode string
    @JvmStatic
    val keyThree: String
        get() {
            return when {
                BuildConfig.FLAVOR.contains("dev") -> String(
                    Base64.decode(
                        BuildConfig.CONSTANT,
                        Base64.DEFAULT
                    )
                )
                BuildConfig.FLAVOR.contains("test") -> String(
                    Base64.decode(
                        BuildConfig.TEST_CONSTANT,
                        Base64.DEFAULT
                    )
                )
                BuildConfig.FLAVOR.contains("prod") -> String(
                    Base64.decode(
                        BuildConfig.PROD_CONSTANT,
                        Base64.DEFAULT
                    )
                )
                else -> ""
            }
        }


    @JvmStatic

    fun selectAudio(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(
            Intent.createChooser(intent, "Select audio"),
            Constants.PICK_AUDIO_REQUEST_CODE
        )
    }

    @JvmStatic
    fun getPathFromAudio(context: Context, uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    @JvmStatic
    fun getDurationFromAudio(pathStr: String, context: Context): Int {
        val uri = Uri.parse(pathStr)
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context, uri)
        val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val millSecond = durationStr!!.toInt()
        return millSecond / 1000
    }


    @Throws(IOException::class)
    private fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = context.getExternalFilesDir(null)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        return image
    }

    fun openCamera(context: Context, launcher: ActivityResultLauncher<Intent>): String {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        var picturePath = ""
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(context)
                picturePath = photoFile?.absolutePath!!
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    context, BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcher.launch(takePictureIntent)
            }
        }

        return picturePath
    }


    fun openGallary(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        allowMultiple: Boolean? = true
    ) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (allowMultiple == true)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        launcher.launch(intent)
    }

    fun recordVideo(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(context.packageManager) != null) {
            launcher.launch(takeVideoIntent)
        }
    }

    fun getVideo(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        i.type = "video/*"
        launcher.launch(i)
    }

    fun getPath(context: Context, uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    fun whenImageIsCaptured(
        context: Context
    ): ArrayList<String?> {
        val imagesEncodedList = ArrayList<String?>()

        if (!pictureImagePath.isNullOrBlank()) {
            val imgFile = File(pictureImagePath)
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source =
                    ImageDecoder.createSource(context.contentResolver, Uri.fromFile(imgFile))
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    Uri.fromFile(imgFile)
                )


            }
            if (bitmap != null) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                val selectedImagePath = getImageUri(context, myBitmap).toString()
                val path = Uri.parse(selectedImagePath)
                imagesEncodedList.add(getPath(context, path))
            }
        }
        return imagesEncodedList
    }

    fun whenVideoIsPicked(context: Context, data: Intent): String? {
        var selectedVideoPath: String
        val selectedImageUri = data.data
        selectedVideoPath = getPath(context, selectedImageUri)!!
        return selectedVideoPath
    }

    fun whenVideoRecorded(context: Context, data: Intent): String? {
        var selectedVideoPath: String
        val videoUri = data.data
        selectedVideoPath = getPath(context, videoUri)!!
        return selectedVideoPath
    }

    fun matches(`val`: String?): Boolean {
        val p = Pattern.compile(Constants.WHITE_LIST_CHARCTER, Pattern.DOTALL)
        val m = p.matcher(`val`)
        return m.matches()
    }

    fun linkfiy(
        a: String?,
        textView: TextView,
        context: Context,
        subtext: String? = null,
        subtextcolor: Int = R.color.white
    ) {
        val urlPattern = Patterns.WEB_URL
        val mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
        val hashtagPattern = Pattern.compile("#(\\w+|\\W+)")
        val o = hashtagPattern.matcher(a)
        val mention = mentionPattern.matcher(a)
        val weblink = urlPattern.matcher(a)
        val spannableString = SpannableString(a)
        //#hashtags
        while (o.find()) {
            spannableString.setSpan(
                NonUnderlinedClickableSpan(
                    o.group(),
                    0, context
                ), o.start(), o.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // --- @mention
        while (mention.find()) {
            spannableString.setSpan(
                NonUnderlinedClickableSpan(mention.group(), 1, context),
                mention.start(),
                mention.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        //@weblink
        while (weblink.find()) {
            spannableString.setSpan(
                NonUnderlinedClickableSpan(weblink.group(), 2, context),
                weblink.start(),
                weblink.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (subtext != null) {
            val i: Int = a!!.indexOf(subtext)
            val boldSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, subtextcolor)),
                i,
                i + subtext.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannableString.setSpan(
                boldSpan,
                i,
                i + subtext.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun convertBitmapToFile(context: Context, name: String, path: String?): String {
        //create a file to write bitmap data
        return try {

            //create bitmap from video
            val bitmap = ThumbnailUtils.createVideoThumbnail(
                path!!,
                MediaStore.Images.Thumbnails.MINI_KIND
            )
            val f = File(context.cacheDir, "$name.png")
            f.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()

            bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            f.absolutePath
        } catch (e: Exception) {
            Log.e("exception_file_upload", e.localizedMessage)
            return ""
        }
    }

    fun encodeMessage(message: String): String {
        val sb = StringBuilder(message.length * 3)
        for (c in message.toCharArray()) {
            if (c.toInt() < 256 && c.toInt() != 39) {
                sb.append(c)
            } else {
                sb.append("\\u")
                sb.append(Character.forDigit(c.toInt() ushr 12 and 0xf, 16))
                sb.append(Character.forDigit(c.toInt() ushr 8 and 0xf, 16))
                sb.append(Character.forDigit(c.toInt() ushr 4 and 0xf, 16))
                sb.append(Character.forDigit(c.toInt() and 0xf, 16))
            }
        }
        return sb.toString()
    }


    @JvmStatic
    fun decodeMessage(message: String?): String? {
        return try {
            StringEscapeUtils.unescapeJava(message)
        } catch (e: java.lang.Exception) {
            ""
        }
    }


    @JvmStatic
    fun openInBrowser(context: Context, url: String) {
        val defaultBrowser =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(url)
        context.startActivity(defaultBrowser)
    }

    @JvmStatic
    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }


    @JvmStatic
    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    @JvmStatic
    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }


    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            MyApplication.getInstance().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


    @JvmStatic
    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }


    //------------------Chat related
    //Decode chat string
    @JvmStatic
    fun getChatDevOther(): String? {
        return String(Base64.decode(BuildConfig.CHAT_DEV_OTHER_CONSTANT, Base64.DEFAULT))
    }

    @JvmStatic
    fun getChatTestOther(): String? {
        return String(Base64.decode(BuildConfig.CHAT_TEST_OTHER_CONSTANT, Base64.DEFAULT))
    }

    @JvmStatic
    fun getChatProd(): String? {
        return String(Base64.decode(BuildConfig.CHAT_PROD_CONSTANT, Base64.DEFAULT))
    }


    @JvmStatic
    fun getChatUAT(): String? {
        return String(Base64.decode(BuildConfig.CHAT_UAT_CONSTANT, Base64.DEFAULT))
    }


    @JvmStatic
    fun openUrl(url: String?, context: Context) {
        if (!TextUtils.isEmpty(url)) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                //   Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG);
            }
        }
    }


    @JvmStatic
    fun selectAudioForChat(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }


    @JvmStatic
    fun checkPermissions(
        context: Context,
        type: String,
        launcherPermision: ActivityResultLauncher<Array<String>>,
        checkCurrentRequest: Int,
        launcherResultLauncher: ActivityResultLauncher<Intent>,
        allowMultipleImagePicked: Boolean? = true,
        dialog: FilePickerDialog? = null,
    ) {
        var permission = true
        val listPermissionsNeeded = ArrayList<String>()
        when (type) {
            //these types are only for the AppSync Thing
            Constants.DOCUMENT,
            Constants.IMAGE,
            Constants.VIDEO,
            Constants.AUDIO -> {
                val storagePermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            Constants.CAMERA -> {
                val permissionCAMERABoth = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                val storagePermissionBoth = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permissionCAMERABoth != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.CAMERA)
                }
                if (storagePermissionBoth != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            Constants.RECORD_AUDIO -> {
                val storagePermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                )
                if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
                }
            }
            Constants.EXTERNAL_READ -> {
                val storagePermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            Constants.CAMERA_EXTERNAL_READ -> {
                val permissionCAMERABoth = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                val storagePermissionBoth = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permissionCAMERABoth != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.CAMERA)
                }
                if (storagePermissionBoth != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            launcherPermision.launch(
                listPermissionsNeeded.toArray(Array(listPermissionsNeeded.size) { "it = $it" })
            )
            permission = false
        }

        if (permission)
            goToNext(
                checkCurrentRequest,
                context,
                launcherResultLauncher,
                allowMultipleImagePicked,
                dialog,
            )
    }


    fun handleOnPermissionResult(
        permissions: Map<String, Boolean>,
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        checkCurrentRequest: Int,
        layoutInflater: LayoutInflater,
        allowMultipleImagePicked: Boolean? = true,
        dialog: FilePickerDialog? = null,
    ) {
        var value = true
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")

            if (!it.value) {
                value = false
                return@forEach
            }
        }

        if (value) {
            goToNext(
                checkCurrentRequest,
                context,
                launcher,
                allowMultipleImagePicked,
                dialog
            )
        } else {
            CustomViews.showFailToast(layoutInflater, context.getString(R.string.permission_denied))
        }
    }

    fun goToNext(
        checkCurrentRequest: Int,
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        allowMultipleImagePicked: Boolean? = true,
        dialog: FilePickerDialog? = null,
    ) {
        when (checkCurrentRequest) {
            Constants.MY_PERMISSIONS_RECORD_AUDIO -> {
                //  fragment?.recordAudio()
            }
            Constants.REQUEST_TAKE_GALLERY_VIDEO -> {
                getVideo(context, launcher)
            }
            Constants.PICK_AUDIO_REQUEST_CODE -> {
                selectAudioForChat(context, launcher)
            }
            Constants.PICK_IMAGE_MULTIPLE -> {
                openGallary(context, launcher, allowMultipleImagePicked)
            }
            Constants.REQUEST_VIDEO_CAPTURE -> {
                recordVideo(context, launcher)
            }
            Constants.OPEN_FILE_PICKER -> {
                dialog?.show()
            }
            Constants.CAMERA_REQUEST -> {
                pictureImagePath = openCamera(context, launcher)
                (!pictureImagePath.isNullOrBlank()).let {
                    Log.e("picture_path", pictureImagePath!!)
                }

            }
        }
    }


    @JvmStatic
    fun whenAudioSelected(context: Context, data: Intent): String? {
        val uri = data.data
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    fun whenImageIsPicked(context: Context, data: Intent): ArrayList<String?> {
        val imagesEncodedList = ArrayList<String?>()
        // Get the Image from data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        if (data.data != null) {
            val mImageUri = data.data

            // Get the cursor
            val cursor: Cursor = context.getContentResolver().query(
                mImageUri!!,
                filePathColumn, null, null, null
            )!!
            // Move to first row
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            imagesEncodedList.add(getPath(context, mImageUri))
            cursor.close()
        } else {
            if (data.clipData != null) {
                val mClipData = data.clipData
                val mArrayUri = java.util.ArrayList<Uri>()
                for (i in 0 until mClipData!!.itemCount) {
                    val item = mClipData!!.getItemAt(i)
                    val uri = item.uri
                    mArrayUri.add(uri)
                    // Get the cursor
                    val cursor: Cursor =
                        context.contentResolver.query(uri, filePathColumn, null, null, null)!!
                    // Move to first row
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    imagesEncodedList.add(getPath(context, uri))
                    cursor.close()
                }
                Log.e("LOG_TAG", "Selected Images" + mArrayUri.size)
                Log.e("LOG_TAG", "Selected Images$imagesEncodedList")
            }
        }

        return imagesEncodedList
    }


    @JvmStatic
    fun containsLowerCase(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("(?=.*[a-z])")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsUpperCase(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("(?=.*[A-Z])")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsNumericCharacter(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("[0-9]")
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    fun containsSpecialCharacter(value: String): Boolean {
        var bool = false
        val pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(value)
        while (matcher.find()) {
            bool = true
        }
        return bool
    }

    @JvmStatic
    open fun handleForceUpdates(forceUpdate: ForceUpdate?) {

        //this is coz we dont want evrytimne user clicks on login main screen open the the disloag

        val context = MyApplication.getInstance()
        val intent = Intent(context, ForceUpdateActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("force_update", Gson().toJson(forceUpdate))
        context.startActivity(intent)

    }


    @JvmStatic
    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager =
            context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }

        }
        return false
    }

    @JvmStatic
    fun getIpAddress(context: Context): String {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }


}
