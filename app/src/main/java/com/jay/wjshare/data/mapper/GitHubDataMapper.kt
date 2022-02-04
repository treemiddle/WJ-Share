package com.jay.wjshare.data.mapper

import com.jay.wjshare.data.model.MyInfoModel
import com.jay.wjshare.data.model.MyRepositoryModel
import com.jay.wjshare.data.model.RepositoriesModel
import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.domain.model.DomainMyInfoModel
import com.jay.wjshare.domain.model.DomainMyRepoModel

object GitHubDataMapper : DataMapper<RepositoriesModel, DomainGithubModel> {

    override fun mapToDomain(from: RepositoriesModel): DomainGithubModel {
        return DomainGithubModel(
            id = from.id,
            onwerName = from.onwerName,
            repositoryName = from.repositoryName,
            description = from.description,
            starCount = from.starCount
        )
    }

}

fun List<MyRepositoryModel>.mapToDomain(): List<DomainMyRepoModel> {
    return this.map {
        DomainMyRepoModel(
            id = it.id,
            onwerName = it.onwerName,
            repositoryName = it.repositoryName,
            description = it.description,
            starCount = it.starCount
        )
    }
}

fun MyInfoModel.mapToDomain(): DomainMyInfoModel {
    return DomainMyInfoModel(
        userName = this.userName,
        profile = this.profile
    )
}