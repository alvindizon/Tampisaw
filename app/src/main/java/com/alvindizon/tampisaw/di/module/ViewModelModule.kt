package com.alvindizon.tampisaw.di.module

import androidx.lifecycle.ViewModel
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.domain.GetAllPhotosUseCase
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.ui.details.DetailsViewModel
import com.alvindizon.tampisaw.ui.gallery.GalleryViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    fun provideViewModelFactory(
        providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(providerMap)
    }

    @Provides
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    fun provideGalleryViewModel(getAllPhotosUseCase: GetAllPhotosUseCase): ViewModel {
        return GalleryViewModel(getAllPhotosUseCase)
    }


    @Provides
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun provideDetailsViewModel(getPhotoUseCase: GetPhotoUseCase): ViewModel {
        return DetailsViewModel(getPhotoUseCase)
    }
}
