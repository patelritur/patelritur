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
-optimizationpasses 5
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging*/
-allowaccessmodification
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.databinding.BaseObservable

-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable
-keepattributes Exceptions, Signature

-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class org.javarosa.** { *; }
-keep class com.sap.** { *; }

-keep class jcifs.** { *; }
-keep class com.demo.registrationLogin.model.CommanRequestModel
-keep class com.demo.home.model.HomeModel
-keepclassmembers class com.demo.registrationLogin.model.** { <fields>; }
-keep  class com.demo.**


-dontwarn com.google.**
-dontwarn org.apache.**
-dontwarn com.sap.**
-dontwarn au.com.bytecode.**
-dontwarn org.joda.**
-dontwarn android.content.**
-dontwarn android.graphics.**
-dontwarn android.util.**
-dontwarn android.view.**

-dontwarn javax.servlet.**
-dontwarn jcifs.http.**
-dontwarn org.codehaus.**