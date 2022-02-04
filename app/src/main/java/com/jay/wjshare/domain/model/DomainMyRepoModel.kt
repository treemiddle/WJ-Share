package com.jay.wjshare.domain.model

data class DomainMyRepoModel(
    val id: Int,
    val onwerName: String,
    val repositoryName: String,
    val description: String?,
    val starCount: Int
) : DomainModel