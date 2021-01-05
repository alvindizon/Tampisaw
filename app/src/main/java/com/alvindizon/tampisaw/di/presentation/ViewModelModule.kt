package com.alvindizon.tampisaw.di.presentation

import androidx.lifecycle.ViewModel
import com.alvindizon.tampisaw.ui.collections.CollectionListViewModel
import com.alvindizon.tampisaw.ui.collections.CollectionViewModel
import com.alvindizon.tampisaw.ui.details.DetailsViewModel
import com.alvindizon.tampisaw.ui.gallery.GalleryViewModel
import com.alvindizon.tampisaw.ui.search.SearchViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
annotation class PresentationScope

@Module
abstract class ViewModelModule {
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

    // previously, we used @Provides, which means that we had to use method bodies
    // @Binds methods must be abstract and thus they must go on an interface or abstract classes
    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun provideGalleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun provideDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionListViewModel::class)
    abstract fun provideCollectionsViewModel(collectionListViewModel: CollectionListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    abstract fun provideCollectionViewModel(collectionViewModel: CollectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideSearchPhotosViewModel(searchViewModel: SearchViewModel): ViewModel
}
