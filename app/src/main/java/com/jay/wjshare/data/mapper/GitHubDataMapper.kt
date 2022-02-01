package com.jay.wjshare.data.mapper

import com.jay.wjshare.data.model.RepositoriesModel
import com.jay.wjshare.domain.model.DomainGithubModel

object GitHubDataMapper : DataMapper<RepositoriesModel, DomainGithubModel> {

    override fun mapToDomain(from: RepositoriesModel): DomainGithubModel {
        return DomainGithubModel(
            onwerName = from.onwerName,
            repositoryName = from.repositoryName,
            description = from.description,
            starCount = from.starCount
        )
    }

}