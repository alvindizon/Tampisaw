package com.alvindizon.tampisaw.di.app

import android.app.Application
import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManagerImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideNotificationManager(context: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    @Provides
    @Singleton
    fun provideContentResolver(context: Context) = context.contentResolver

    @Provides
    @Singleton
    fun provideWallpaperManager(context: Context) = WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideWallpaperSettingManager(
        contentResolver: ContentResolver,
        wallpaperManager: WallpaperManager
    ): WallpaperSettingManager =
        WallpaperSettingManagerImpl(contentResolver, wallpaperManager)

}
