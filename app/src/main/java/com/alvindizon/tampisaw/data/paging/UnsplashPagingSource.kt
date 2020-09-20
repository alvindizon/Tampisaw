package com.alvindizon.tampisaw.data.paging

import androidx.paging.rxjava2.RxPagingSource
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UnsplashPagingSource (private val unsplashApi: UnsplashApi)
    : RxPagingSource<Int, UnsplashPhoto>(){

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, UnsplashPhoto>> {
        val id = params.key ?: Const.PAGE_NUM
        return unsplashApi.getAllPhotos(id, Const.PAGE_SIZE, "latest")
        .subscribeOn(Schedulers.io())
            .map { response ->
                response.map { it.toUnsplashPhoto() }
            }
            .map<LoadResult<Int, UnsplashPhoto>> { item ->
                LoadResult.Page(
                    data = item,
                    prevKey = if (id == Const.PAGE_NUM) null else id - 1,
                    nextKey =  if (item.isEmpty()) null else id + 1
                )
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }
}
