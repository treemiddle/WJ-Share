package com.jay.wjshare.data.model

data class RepositoriesModel(
    val onwerName: String,
    val repositoryName: String,
    val description: String?,
    val starCount: Int
) : DataModel