package com.jay.wjshare.data.remote.mapper

import com.jay.wjshare.data.model.MyInfoModel
import com.jay.wjshare.data.model.MyRepositoryModel
import com.jay.wjshare.data.model.RepositoriesModel
import com.jay.wjshare.data.remote.model.profile.MyInfoResponse
import com.jay.wjshare.data.remote.model.profile.ProfileResponseItem
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

fun List<ProfileResponseItem>.mapToData(): List<MyRepositoryModel> {
    return this.map {
        MyRepositoryModel(
            onwerName = it.owner.login,
            repositoryName = it.name,
            description = it.description,
            starCount = it.stargazers_count
        )
    }
}

fun MyInfoResponse.mapToData(): MyInfoModel {
    return MyInfoModel(
        userName = this.login,
        profile = this.avatar_url
    )
}