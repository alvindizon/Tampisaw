package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val getPhotosType: GetPhotosType,
    private val collectionId: String? = null
) : RxPagingSource<Int, ListPhotosResponse>() {

    enum class GetPhotosType {
        Gallery,
        Collection
    }

    override fun getRefreshKey(state: PagingState<Int, ListPhotosResponse>): Int? =
        state.anchorPosition

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListPhotosResponse>> {
        val page = params.key ?: Const.PAGE_NUM
        val getPhotos = when (getPhotosType) {
            GetPhotosType.Gallery -> unsplashApi.getAllPhotos(page, Const.PAGE_SIZE, "latest")
            GetPhotosType.Collection -> unsplashApi.getCollectionPhotos(
                collectionId!!,
                page,
                Const.PAGE_SIZE
            )
        }
        return getPhotos.subscribeOn(Schedulers.io())
            .map<LoadResult<Int, ListPhotosResponse>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (page == Const.PAGE_NUM) null else page - 1,
                    nextKey = if (item.isEmpty()) null else page + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}
