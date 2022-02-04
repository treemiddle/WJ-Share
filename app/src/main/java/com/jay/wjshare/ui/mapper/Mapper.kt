package com.jay.wjshare.ui.mapper

import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.domain.model.DomainMyInfoModel
import com.jay.wjshare.domain.model.DomainMyRepoModel
import com.jay.wjshare.ui.model.MyInfoModel
import com.jay.wjshare.ui.model.RepoModel
import io.reactivex.subjects.Subject

object Mapper : PresentationMapper<DomainGithubModel, RepoModel> {

    override fun mapToPresentation(from: DomainGithubModel): RepoModel {
        return RepoModel(
            id = from.id,
            onwerName = from.onwerName,
            repositoryName = from.repositoryName,
            description = from.description,
            starCount = from.starCount
        )
    }

}

fun Pair<DomainMyInfoModel, List<DomainMyRepoModel>>.mapToPresentation(
    subject: Subject<RepoModel>
) : Pair<MyInfoModel, List<RepoModel>> {
    val myInfoModel = MyInfoModel(
        userName = first.userName,
        profile = first.profile
    )

    val repos = second.map {
        RepoModel(
            id = it.id,
            onwerName = it.onwerName,
            repositoryName = it.repositoryName,
            description = it.description,
            starCount = it.starCount
        ).apply {
            onClick = subject
        }
    }

    return myInfoModel to repos
}

fun RepoModel.applyClick(subject: Subject<RepoModel>): RepoModel {
    return this.apply {
        onClick = subject
    }
}