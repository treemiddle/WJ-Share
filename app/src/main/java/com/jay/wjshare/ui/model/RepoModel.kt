package com.jay.wjshare.ui.model

import com.jay.wjshare.ui.model.PresentationModel

data class RepoModel(
    val onwerName: String = "",
    val repositoryName: String = "",
    val description: String? = "",
    val starCount: Int = 0
) : PresentationModel