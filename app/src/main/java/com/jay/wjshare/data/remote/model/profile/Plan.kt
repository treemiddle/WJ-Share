package com.jay.wjshare.data.remote.model.profile

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)