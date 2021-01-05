package com.alvindizon.tampisaw.di.presentation

import androidx.lifecycle.ViewModel
import com.alvindizon.tampisaw.domain.*
import com.alvindizon.tampisaw.ui.collections.CollectionListViewModel
import com.alvindizon.tampisaw.ui.collections.CollectionViewModel
import com.alvindizon.tampisaw.ui.details.DetailsViewModel
import com.alvindizon.tampisaw.ui.gallery.GalleryViewModel
import com.alvindizon.tampisaw.ui.search.SearchViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
annotation class PresentationScope

@Module
class ViewModelModule {
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

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

    @Provides
    @IntoMap
    @ViewModelKey(CollectionListViewModel::class)
    fun provideCollectionsViewModel(getAllCollectionsUseCase: GetAllCollectionsUseCase): ViewModel {
        return CollectionListViewModel(getAllCollectionsUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    fun provideCollectionViewModel(getCollectionPhotosUseCase: GetCollectionPhotosUseCase): ViewModel {
        return CollectionViewModel(getCollectionPhotosUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideSearchPhotosViewModel(
        searchPhotosUseCase: SearchPhotosUseCase,
        searchCollectionsUseCase: SearchCollectionsUseCase
    ): ViewModel {
        return SearchViewModel(searchPhotosUseCase, searchCollectionsUseCase)
    }
}
