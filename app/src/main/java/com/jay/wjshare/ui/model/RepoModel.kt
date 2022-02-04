package com.jay.wjshare.ui.model

import android.os.Parcelable
import com.jay.wjshare.ui.base.WJClickable
import io.reactivex.subjects.Subject
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoModel(
    val id: Int = 0,
    val onwerName: String = "",
    val repositoryName: String = "",
    val description: String? = "",
    val starCount: Int = 0,
    var hasLiked: Boolean = false
) : PresentationModel, WJClickable<RepoModel>, Parcelable {
    @IgnoredOnParcel
    override lateinit var onClick: Subject<RepoModel>
}