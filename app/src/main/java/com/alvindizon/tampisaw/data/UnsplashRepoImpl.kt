package com.alvindizon.tampisaw.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.data.paging.CollectionPagingSource
import com.alvindizon.tampisaw.data.paging.SearchCollectionsPagingSource
import com.alvindizon.tampisaw.data.paging.SearchPhotosPagingSource
import com.alvindizon.tampisaw.data.paging.UnsplashPagingSource
import com.alvindizon.tampisaw.domain.UnsplashRepo
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class UnsplashRepoImpl(private val unsplashApi: UnsplashApi): UnsplashRepo {

    override fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        // Always create a new UnsplashPagingSource object. Failure to do so would result in a
        // IllegalStateException when adapter.refresh() is called--
        // Exception message states that the same PagingSource was used as the prev request,
        // and a new PagingSource is required
        pagingSourceFactory = { UnsplashPagingSource(unsplashApi, UnsplashPagingSource.GetPhotosType.Gallery) }
    ).observable

    override fun getPhoto(id: String): Single<GetPhotoResponse> = unsplashApi.getPhoto(id)

    override fun getAllCollections(): Observable<PagingData<UnsplashCollection>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        pagingSourceFactory = { CollectionPagingSource(unsplashApi) }
    ).observable

    override fun getCollectionPhotos(id: String): Observable<PagingData<UnsplashPhoto>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        pagingSourceFactory = { UnsplashPagingSource(unsplashApi, UnsplashPagingSource.GetPhotosType.Collection, id) }
    ).observable

    override fun searchPhotos(query: String): Observable<PagingData<UnsplashPhoto>> = Pager(
            config = PagingConfig(Const.PAGE_SIZE),
            remoteMediator = null,
            pagingSourceFactory = { SearchPhotosPagingSource(unsplashApi, query)}
    ).observable

    override fun searchCollections(query: String): Observable<PagingData<UnsplashCollection>> = Pager(
            config = PagingConfig(Const.PAGE_SIZE),
            remoteMediator = null,
            pagingSourceFactory = { SearchCollectionsPagingSource(unsplashApi, query)}
    ).observable
}
