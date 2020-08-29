package com.alvindizon.tampisaw.di.component

import com.alvindizon.tampisaw.di.module.ViewModelModule
import com.alvindizon.tampisaw.ui.details.DetailsFragment
import com.alvindizon.tampisaw.ui.gallery.GalleryFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface PresentationComponent {
    fun inject(fragment : GalleryFragment)
    fun inject(fragment: DetailsFragment)
}