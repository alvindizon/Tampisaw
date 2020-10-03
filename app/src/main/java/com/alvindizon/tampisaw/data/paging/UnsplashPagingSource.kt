package com.alvindizon.tampisaw.data.paging

import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UnsplashPagingSource (private val unsplashApi: UnsplashApi,
                            private val getPhotosType: GetPhotosType,
                            private val collectionId: Int? = null)
    : RxPagingSource<Int, UnsplashPhoto>(){

    enum class GetPhotosType {
        Gallery,
        Collection
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UnsplashPhoto>> {
        val page = params.key ?: Const.PAGE_NUM
        val getPhotos = when(getPhotosType ) {
            GetPhotosType.Gallery -> unsplashApi.getAllPhotos(page, Const.PAGE_SIZE, "latest")
            GetPhotosType.Collection -> unsplashApi.getCollectionPhotos(collectionId!!, page, Const.PAGE_SIZE)
        }
        return getPhotos.subscribeOn(Schedulers.io())
            .map { response ->
                response.map { it.toUnsplashPhoto() }
            }
            .map<LoadResult<Int, UnsplashPhoto>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (page == Const.PAGE_NUM) null else page - 1,
                    nextKey =  if (item.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}
