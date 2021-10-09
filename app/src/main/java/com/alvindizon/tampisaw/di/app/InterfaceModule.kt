package com.alvindizon.tampisaw.di.app

import com.alvindizon.tampisaw.data.file.FileManager
import com.alvindizon.tampisaw.data.file.FileManagerImpl
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    // use binds to provide interface implementation
    @Binds
    abstract fun bindWallpaperSettingManager(
        wallpaperSettingManager: WallpaperSettingManagerImpl
    ): WallpaperSettingManager

    @Binds
    abstract fun bindFileManager(fileManager: FileManagerImpl): FileManager
}
