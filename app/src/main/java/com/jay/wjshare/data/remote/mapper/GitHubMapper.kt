package com.jay.wjshare.data.remote.mapper

import com.jay.wjshare.data.model.RepositoriesModel
import com.jay.wjshare.data.remote.model.repositories.Item

object GitHubMapper : RemoteMapper<Item, RepositoriesModel> {

    override fun mapToData(from: Item): RepositoriesModel {
        return RepositoriesModel(
            onwerName = from.owner.login,
            repositoryName = from.name,
            description = from.description,
            starCount = from.stargazers_count
        )
    }

}
