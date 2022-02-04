package com.jay.wjshare.data.remote.model.repositories

data class RepositoriesResponse(
    val incomplete_results: Boolean,
    val items: List<Item>?,
    val total_count: Int
)