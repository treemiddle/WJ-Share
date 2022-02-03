package com.jay.wjshare.ui.model

import com.jay.wjshare.ui.base.WJClickable
import io.reactivex.subjects.Subject

data class RepoModel(
    val onwerName: String = "",
    val repositoryName: String = "",
    val description: String? = "",
    val starCount: Int = 0,
    var hasLiked: Boolean = false
) : PresentationModel, WJClickable<RepoModel> {
    override lateinit var onClick: Subject<RepoModel>
}