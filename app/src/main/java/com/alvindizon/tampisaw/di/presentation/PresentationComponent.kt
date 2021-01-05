package com.alvindizon.tampisaw.di.presentation

import com.alvindizon.tampisaw.ui.collections.CollectionFragment
import com.alvindizon.tampisaw.ui.collections.CollectionListFragment
import com.alvindizon.tampisaw.ui.details.DetailsFragment
import com.alvindizon.tampisaw.ui.details.InfoBottomSheet
import com.alvindizon.tampisaw.ui.gallery.GalleryFragment
import com.alvindizon.tampisaw.ui.search.SearchCollectionListFragment
import com.alvindizon.tampisaw.ui.search.SearchFragment
import com.alvindizon.tampisaw.ui.search.SearchPhotosFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [ViewModelModule::class])
interface PresentationComponent {
    fun inject(fragment : GalleryFragment)
    fun inject(fragment: DetailsFragment)
    fun inject(dialog: InfoBottomSheet)
    fun inject(fragment: CollectionListFragment)
    fun inject(fragment: CollectionFragment)
    fun inject(fragment: SearchPhotosFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SearchCollectionListFragment)
}