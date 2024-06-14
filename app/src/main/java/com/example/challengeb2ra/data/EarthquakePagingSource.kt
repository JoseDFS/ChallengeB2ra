package com.example.challengeb2ra.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.challengeb2ra.data.Api
import com.example.challengeb2ra.data.Earthquake

class EarthquakePagingSource(
    private val api: ApiService,
    private val startDate: String?,
    private val endDate: String?
) : PagingSource<Int, Earthquake>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Earthquake> {
        return try {
            val currentPage = params.key ?: 1
            val response = api.getEarthquakes(startDate, endDate)
            LoadResult.Page(
                data = response.features,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.features.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Earthquake>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

