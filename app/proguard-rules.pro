-optimizationpasses 5

-dontobfuscate
#When not preverifing in a case-insensitive filing system, such as Windows. Because this tool unpacks your processed jars, you should then use:
-dontusemixedcaseclassnames

#Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting
-dontskipnonpubliclibraryclasses

#Preverification is irrelevant for the dex compiler and the Dalvik VM, so we can switch it off with the -dontpreverify option.
-dontpreverify

#Specifies to write out some more information during processing. If the program terminates with an exception, this option will print out the entire stack trace, instead of just the exception message.
-verbose

#The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't handle. Note that the Dalvik VM also can't handle aggressive overloading (of static fields).
#To understand or change this check http://proguard.sourceforge.net/index.html#/manual/optimizations.html
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#To repackage classes on a single package
#-repackageclasses ''

#Uncomment if using com.google.common.annotations to keep them.
-keepattributes *Annotation*, Signature

#Keep classes that are referenced on the AndroidManifest
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

# guava settings
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# guava settings 19
-keep class com.google.j2objc.annotations.** { *; }
-dontwarn   com.google.j2objc.annotations.**
-keep class java.lang.ClassValue { *; }
-dontwarn   java.lang.ClassValue
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }
-dontwarn   org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

#To avoid changing names of methods invoked on layout's onClick.
# Uncomment and add specific method names if using onClick on layouts
#-keepclassmembers class * {
# public void onClickButton(android.view.View);
#}

#Maintain java native methods 
-keepclasseswithmembernames class * {
    native <methods>;
}

#To maintain custom components names that are used on layouts XML.
#Uncomment if having any problem with the approach below
#-keep public class custom.components.package.and.name.**

#To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#Maintain enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#To keep parcelable classes (to serialize - deserialize objects to sent through Intents)
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}

###### ADDITIONAL OPTIONS NOT USED NORMALLY

#To keep callback calls. Uncomment if using any
#http://proguard.sourceforge.net/index.html#/manual/examples.html#callback
#-keep class mypackage.MyCallbackClass {
#   void myCallbackMethod(java.lang.String);
#}

#Uncomment if using Serializable 
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.rockspin.bargainbits.data.models.cheapshark.**  { *; }
-keep class com.rockspin.bargainbits.data.models.currency.**  { *; }

-keepattributes Exceptions, InnerClasses, Signature, Deprecated,  SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface

####### GOOGLE PLAY SERVICES #######
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep public class com.google.** {*;}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule

# Otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Retrofit OKHttp RX
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keep interface com.squareup.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepattributes Exceptions
-dontwarn rx.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit.appengine.UrlFetchClient

# Lambdas
-dontwarn java.lang.invoke.**

-dontwarn org.apache.**

# Lambdas.p
-dontwarn icepick.**
-keep class **$$Icepick { *; }
-keepnames class * { @icepick.State *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}

# Evernote Job API
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.util.GcmAvailableHelper

-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver