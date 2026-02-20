# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Room and Hilt
-keep class * extends androidx.room.RoomDatabase
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-dontwarn javax.annotation.**
