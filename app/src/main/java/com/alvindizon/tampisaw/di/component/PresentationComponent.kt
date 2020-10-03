package com.alvindizon.tampisaw.di.component

import com.alvindizon.tampisaw.di.module.ActivityModule
import com.alvindizon.tampisaw.di.module.ViewModelModule
import com.alvindizon.tampisaw.ui.collections.CollectionFragment
import com.alvindizon.tampisaw.ui.collections.CollectionListFragment
import com.alvindizon.tampisaw.ui.details.DetailsFragment
import com.alvindizon.tampisaw.ui.details.InfoBottomSheet
import com.alvindizon.tampisaw.ui.gallery.GalleryFragment
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(fragment : GalleryFragment)
    fun inject(fragment: DetailsFragment)
    fun inject(dialog: InfoBottomSheet)
    fun inject(fragment: CollectionListFragment)
    fun inject(fragment: CollectionFragment)
}