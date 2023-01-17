# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-renamesourcefileattribute SourceFile
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


########--------Retrofit + RxJava--------#########
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn sun.misc.Unsafe
-dontwarn com.octo.android.robospice.retrofit.RetrofitJackson**
-dontwarn retrofit.appengine.UrlFetchClient
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit.**

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}


# ALSO REMEMBER KEEPING YOUR MODEL CLASSES
-keepclassmembers class  com.jigar.me.data.model.* { *; }
-keepclassmembers class  com.jigar.me.data.model.stripe_models.* { *; }
-keepclassmembers class  com.jigar.me.data.model.chat.* {*;}


#json library
-keepnames class com.fasterxml.jackson.databind.** { *; }
-dontwarn com.fasterxml.jackson.databind.*
-keepattributes InnerClasses

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.* { *; }
-dontwarn org.bouncycastle.*

-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn io.jsonwebtoken.impl.Base64Codec

-keepnames interface com.fasterxml.jackson.** { *; }





-keep class com.google.android.material.textfield.TextInputLayout { *; }
-keep class com.google.android.material.internal.CollapsingTextHelper { *; }


# Rules for BouncyCastle
-keep class org.bouncycastle.jcajce.provider.** { *; }
-keep class org.bouncycastle.jce.provider.** { *; }
-keep class !org.bouncycastle.jce.provider.X509LDAPCertStoreSpi { *; }
-keep class com.stripe.android.** { *; }
-dontwarn com.stripe.android.view.**
-keep class com.stripe.** { *; }

# Rules for Kotlin Coroutines
# https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/kotlinx-coroutines-android/example-app/app/proguard-rules.pro
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}


#zoom
-keep class  us.zoom.**{*;}
-keep class  com.zipow.**{*;}
-keep class  us.zipow.**{*;}
-keep class  org.webrtc.**{*;}
-keep class  us.google.protobuf.**{*;}


###---------------Begin: proguard configuration for ButterKnife  ----------
# For Butterknife:
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**

# Version 7
-keep class **$$ViewBinder { *; }
# Version 8
-keep class **_ViewBinding { *; }

-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
###---------------End: proguard configuration for ButterKnife  ----------

-assumenosideeffects class android.util.Log {
  public static *** d(...);
  public static *** w(...);
  public static *** v(...);
  public static *** i(...);
  public static *** e(...);
}


-keep class com.facebook.** {
   *;
}

#-keepattributes Signature
#-keep class com.google.* {*;}
#-keep class com.google.impl.* {*;}
#-keep class com.google.firebase.* {*;}
#-keep class com.google.googlesignin.** { *; }
#-keepnames class com.google.googlesignin.* { *; }
#-keep class com.google.gms.** {*;}
#-keep class com.google.android.gms.auth.** { *; }
#-keep class com.google.android.* {*;}
#-keep class com.google.unity.* {*;}


#appsync
# Class names are needed in reflection
#-keepnames class com.amazonaws.**
#-keepnames class com.amazon.**

#-keepclassmembers class com.example.iiifa_fan_android.fragment.** {*;}
#-keepclassmembers class com.example.iiifa_fan_android.type.** {*;}
#
#




# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**
-dontwarn com.amazonaws.mobile.**


-keep class * extends androidx.fragment.app.Fragment{}
-keep class androidx.navigation.fragment.NavHostFragment
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
