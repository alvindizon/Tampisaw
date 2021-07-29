package com.alvindizon.tampisaw.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchPhotosPagingSource (private val unsplashApi: UnsplashApi, private val query: String)
    : RxPagingSource<Int, UnsplashPhoto>(){

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UnsplashPhoto>> {
        val page = params.key ?: Const.PAGE_NUM
        return unsplashApi.searchPhotos(
                query, page, Const.PAGE_SIZE, "relevant", null,
                "low", null, null
            )
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.results.map { it.toUnsplashPhoto() }
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
