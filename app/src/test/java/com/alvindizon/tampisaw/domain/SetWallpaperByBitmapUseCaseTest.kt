package com.alvindizon.tampisaw.domain

import android.net.Uri
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

class SetWallpaperByBitmapUseCaseTest {

    private val wallpaperSettingManager: WallpaperSettingManager = mockk()

    private val uri: Uri = mockk()

    private lateinit var useCase: SetWallpaperByBitmapUseCase

    @BeforeEach
    fun setUp() {
        useCase = SetWallpaperByBitmapUseCase(wallpaperSettingManager)
    }

    @Test
    fun `verify usecase returns complete when wallpaper setting succeeds`() {
        every { wallpaperSettingManager.setWallpaperByBitmap(any()) } just Runs

        useCase.setWallpaperByBitmap(uri).test().assertComplete()
    }

    @Test
    fun `verify usecase returns error when wallpaper setting errors`() {
        every {
            wallpaperSettingManager.setWallpaperByBitmap(any())
        } throws IOException()

        useCase.setWallpaperByBitmap(uri).test().assertError(IOException::class.java)
    }
}
