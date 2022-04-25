package com.alvindizon.tampisaw.setwallpaper.di

import com.alvindizon.tampisaw.setwallpaper.WallpaperSettingManager
import com.alvindizon.tampisaw.setwallpaper.WallpaperSettingManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SetWallpaperModule {
    // use binds to provide interface implementation
    @Binds
    abstract fun bindWallpaperSettingManager(
        wallpaperSettingManager: WallpaperSettingManagerImpl
    ): WallpaperSettingManager
}
