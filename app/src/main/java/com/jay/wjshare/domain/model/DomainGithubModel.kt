package com.jay.wjshare.domain.model

data class DomainGithubModel(
    val id: Int,
    val onwerName: String,
    val repositoryName: String,
    val description: String?,
    val starCount: Int
) : DomainModel