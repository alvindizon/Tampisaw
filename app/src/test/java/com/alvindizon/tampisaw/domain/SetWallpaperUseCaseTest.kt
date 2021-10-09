package com.alvindizon.tampisaw.domain

import android.app.Activity
import android.net.Uri
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetWallpaperUseCaseTest {

    private val wallpaperSettingManager: WallpaperSettingManager = mockk()

    private val uri: Uri = mockk()

    private val activity: Activity = mockk()

    private lateinit var useCase: SetWallpaperUseCase

    @BeforeEach
    fun setUp() {
        useCase = SetWallpaperUseCase(wallpaperSettingManager)
    }

    @Test
    fun `verify usecase returns complete when wallpaper setting succeeds`() {
        every { wallpaperSettingManager.setWallpaper(any(), any()) } just Runs

        useCase.setWallpaper(uri, activity).test().assertComplete()
    }

    @Test
    fun `verify usecase returns error when wallpaper setting errors`() {
        every {
            wallpaperSettingManager.setWallpaper(
                any(),
                any()
            )
        } throws IllegalArgumentException()

        useCase.setWallpaper(uri, activity).test().assertError(IllegalArgumentException::class.java)
    }
}
