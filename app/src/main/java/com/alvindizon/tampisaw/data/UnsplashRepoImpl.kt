package com.alvindizon.tampisaw.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.data.paging.UnsplashPagingSource
import com.alvindizon.tampisaw.domain.UnsplashRepo
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import io.reactivex.Single

class UnsplashRepoImpl(private val unsplashPagingSource: UnsplashPagingSource,
    private val unsplashApi: UnsplashApi): UnsplashRepo {

    override fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        pagingSourceFactory ={ unsplashPagingSource }
    ).observable

    override fun getPhoto(id: String): Single<GetPhotoResponse> = unsplashApi.getPhoto(id)
}
