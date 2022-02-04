package com.jay.wjshare.data.model

data class MyRepositoryModel(
    val id: Int,
    val onwerName: String,
    val repositoryName: String,
    val description: String?,
    val starCount: Int
) : DataModel