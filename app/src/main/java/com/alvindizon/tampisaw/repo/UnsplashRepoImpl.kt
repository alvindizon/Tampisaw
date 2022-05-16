package com.alvindizon.tampisaw.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.alvindizon.tampisaw.api.UnsplashApi
import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.api.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.paging.CollectionPagingSource
import com.alvindizon.tampisaw.data.paging.SearchCollectionsPagingSource
import com.alvindizon.tampisaw.data.paging.SearchPhotosPagingSource
import com.alvindizon.tampisaw.data.paging.UnsplashPagingSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UnsplashRepoImpl @Inject constructor(private val unsplashApi: UnsplashApi) : UnsplashRepo {

    override fun getAllPhotos(): Observable<PagingData<ListPhotosResponse>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        // Always create a new UnsplashPagingSource object. Failure to do so would result in a
        // IllegalStateException when adapter.refresh() is called--
        // Exception message states that the same PagingSource was used as the prev request,
        // and a new PagingSource is required
        pagingSourceFactory = {
            UnsplashPagingSource(
                unsplashApi,
                UnsplashPagingSource.GetPhotosType.Gallery
            )
        }
    ).observable

    override fun getPhoto(id: String): Single<GetPhotoResponse> = unsplashApi.getPhoto(id)

    override fun getAllCollections(): Observable<PagingData<GetCollectionsResponse>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        pagingSourceFactory = { CollectionPagingSource(unsplashApi) }
    ).observable

    override fun getCollectionPhotos(id: String): Observable<PagingData<ListPhotosResponse>> =
        Pager(
            config = PagingConfig(Const.PAGE_SIZE),
            remoteMediator = null,
            pagingSourceFactory = {
                UnsplashPagingSource(
                    unsplashApi,
                    UnsplashPagingSource.GetPhotosType.Collection,
                    id
                )
            }
        ).observable

    override fun searchPhotos(query: String): Observable<PagingData<ListPhotosResponse>> = Pager(
        config = PagingConfig(Const.PAGE_SIZE),
        remoteMediator = null,
        pagingSourceFactory = { SearchPhotosPagingSource(unsplashApi, query) }
    ).observable

    override fun searchCollections(query: String): Observable<PagingData<GetCollectionsResponse>> =
        Pager(
            config = PagingConfig(Const.PAGE_SIZE),
            remoteMediator = null,
            pagingSourceFactory = { SearchCollectionsPagingSource(unsplashApi, query) }
        ).observable
}
