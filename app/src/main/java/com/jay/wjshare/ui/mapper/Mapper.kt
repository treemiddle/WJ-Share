package com.jay.wjshare.ui.mapper

import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.domain.model.DomainMyInfoModel
import com.jay.wjshare.domain.model.DomainMyRepoModel
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.ui.mapper.PresentationMapper
import com.jay.wjshare.ui.model.MyInfoModel

object Mapper : PresentationMapper<DomainGithubModel, RepoModel> {

    override fun mapToPresentation(from: DomainGithubModel): RepoModel {
        return RepoModel(
            onwerName = from.onwerName,
            repositoryName = from.repositoryName,
            description = from.description,
            starCount = from.starCount
        )
    }

}

fun Pair<DomainMyInfoModel, List<DomainMyRepoModel>>.mapToPresentation(): Pair<MyInfoModel, List<RepoModel>> {
    val myInfoModel = MyInfoModel(
        userName = first.userName,
        profile = first.profile
    )

    val repos = second.map {
        RepoModel(
            onwerName = it.onwerName,
            repositoryName = it.repositoryName,
            description = it.description,
            starCount = it.starCount
        )
    }

    return myInfoModel to repos
}