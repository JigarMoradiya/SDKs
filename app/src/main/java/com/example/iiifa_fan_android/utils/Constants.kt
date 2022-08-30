package com.example.iiifa_fan_android.utils

import java.util.*


//Application Constants Values
object Constants {

    const val ANDROID = "android"
    const val ENTITY_TYPE = "fan"
    const val REGISTRATION = "registration"
    const val PREF_NAME = "androidhive-welcome"

    const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$"
    const val WHITE_LIST_CHARCTER = "^((?!(\\*\\/|\\/\\*|\\-\\-|<|>)).)*$"
    const val PASSWORD_REGEX = "^(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*?[0-9]).*\$"

    const val ONLY_ALPHABETS = "^[a-zA-Z -]+$"

    //   public static final String UK_POSTAL_CODE = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
    const val UK_POSTAL_CODE = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$"

    //login  error code
    const val EMAIL_NOT_VERIFIED = "EMAIL_NOT_VERIFIED"
    const val EMAIL_NOT_APPROVED = "email_not_approved"
    const val EMAIL_REJECTED = "email_rejected"
    const val EMAIL_SUSPENDED = "email_suspended"
    const val EMAIL_ALREADY_REGISTERED = "EMAIL_ALREADY_REGISTERED"


    const val MEDIA_ATTACHMENT_FOLDER = "mediaAttachments/"
    const val AUDIO_FOLDER = "audios/"
    const val VIDEO_FOLDER = "videos/"
    const val IMAGE_FOLDER = "images/"
    const val DOCUMENT_FOLDER = "documents/"
    const val THUMBNAIL_FOLDER = "thumbnails/"
    const val JOURNAL_FOLDER = "user/journal/"


    //foldet for media
    const val POST_IMAGE = "post/images/"
    const val POST_VIDEO = "post/videos/"
    const val POST_THUMBNAIL = "post/thumbnailImages/"
    const val PROFILE_IMAGE = "user/profilePictures/"

    //Error type const
    const val LOGIN_INTO_ANOTHER_DEVICE = "LOGIN_IN_ANOTHER_DEVICE"
    const val PHONE_NOT_VERIFIED = "PHONE_NOT_VERIFIED"
    const val OTP_EXPIRED = "OTP_EXPIRED"
    const val ENROLMENT_ALREADY_EXISTS = "ENROLMENT_ALREADY_EXISTS"
    const val NO_SEATS_AVAILABLE = "NO_SEATS_AVAILABLE"
    const val PLEASE_TRY_AFTER_SOMETIME = "PLEASE_TRY_AFTER_SOMETIME"
    const val PROFILE_IS_SUSPENDED = "PROFILE_IS_SUSPENDED"
    const val ACTIVE_SESSION_EXCEEDS = "ACTIVE_SESSION_EXCEEDS"
    const val REQUEST_SMS_PERMISSION = 200
    const val SELECT_IMAGE = 100
    const val READ_PERMISSION_CODE = 101
    const val MY_PERMISSIONS_RECORD_AUDIO = 104
    const val RC_SIGN_IN = 123
    const val PICK_DOC = 1233

    //selection
    const val PICKFILE_REQUEST_CODE = 1
    const val PICK_AUDIO_REQUEST_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 3
    const val PICK_VIDEO_REQUEST_CODE = 4
    const val DONT_DISPLAY_ERROR_ICON = "do not display error icon"
    const val CERT_URL = "https://connectlab-certificate.s3.eu-west-2.amazonaws.com/certificate.crt"

    //response key const
    const val RESPONSE_MESSAGE = "response_message"
    const val DATA = "data"

    @JvmField
    val GENDER = ArrayList(Arrays.asList("Select Gender", "Male", "Female", "Other"))
    val NATIONALITY = ArrayList(Arrays.asList("Select Country", "India", "Other"))

    //cms
    const val PRIVACY_POLICY = "privacy_policy"
    const val TERMS_CONDITION = "terms_and_condition"
    const val CANCELLATION_POLICY = "cancellation_policy"
    const val CONTACT_US = "contact_us"
    const val ABOUT_US = "about_us"

    //content gender
    const val MALE = "Male"
    const val FEMALE = "Female"
    const val OTHER = "Other"

    const val CAMERA = "camera"
    const val CAMERA_EXTERNAL_READ = "camera_read_external_storage"
    const val EXTERNAL_READ = "EXTERNAL_READ"

    //create post permissions
    const val REQUEST_TAKE_GALLERY_VIDEO = 0
    const val PICK_IMAGE_MULTIPLE = 1
    const val CAMERA_REQUEST = 1888
    const val REQUEST_VIDEO_CAPTURE = 100


    //password type
    const val WEAK = "Weak"
    const val MODERATED = "Moderate"
    const val STRONG = "Strong"
    const val NOT_ACCEPTED = "not_accepted"


    //--select type
    const val AUDIO_TYPE = "audio"
    const val VIDEO_TYPE = "video"
    const val IMAGE_TYPE = "image"
    const val DOCUMENT_TYPE = "document"
    const val MEDIA_TYPE = "media"
    const val CHAT_ACTIVITY = 1800

    //create post permissions
    const val OPEN_FILE_PICKER = 900
    const val RECORD_AUDIO = "record_audio"
    const val MULTIPLE_PERMISSIONS = 123


    //appointment status
    const val APP_NEW = "new"
    const val APP_FOLLOW_UP = "follow-up"
    const val APP_AUTHORIZED = "authorized"
    const val APP_FAILED = "failed"
    const val APP_PAID = "paid"
    const val APP_CANCELED = "cancelled"
    const val APP_RESCHEDULED = "rescheduled"
    const val APP_COMPLETED = "completed"
    const val APP_MISSED = "missed"
    const val APP_PENDING = "pending"
    const val APP_CONFIRMED = "confirmed"
    const val APP_ACCEPTED = "accepted"
    const val APP_EXPIRED = "expired"
    const val APP_REJECTED = "rejected"
    const val APPROVED = "approved"


    const val GET_APPOINTMENT_LIST = "GET_APPOINTMENT_LIST"
    const val GET_AVAILABILITY_LIST = "GET_AVAILABILITY_LIST"
    const val GET_RECUURING_AVAILABILITY_LIST = "GET_RECURRING_AVAILABILITY_LIST"

    const val MORNING = "MORNING"
    const val NOON = "NOON"
    const val EVENING = "EVENING"
    const val NIGHT = "NIGHT"


    //pasasword meter
    const val AT_LEAST_8 = "Atleast 8 characters"
    const val UPPERCASE_CHAR = "Contains an uppercase character"
    const val LOWERCASE_CHAR = "Contains a lowercase character"
    const val NUMERIC = "Contains a number"

    const val API_ERROR = "not getting error message from API"
    const val API_ERROR_TYPE = "no error type"

    // message type
    const val TYPE_TEXT = "text"

    //select type
    const val AUDIO = "audio"
    const val VIDEO = "video"
    const val IMAGE = "image"
    const val DOCUMENT = "document"
    const val MEDIA = "media"

    // API key
    const val CHECK_USER_EXIST = "checkuserexists"
    const val SEND_RESEND_OTP = "sendresendotp"
    const val VALIDATE_OTP = "validateotp"
    const val VALIDATE_REFFERAL_CODE: String = "validateReferralCode"
    const val ADD_FAN: String = "addfan"
    const val LOGIN = "login"

}