package com.jay.wjshare.ui.main.search.mapper

import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.ui.main.search.model.RepoModel
import com.jay.wjshare.ui.mapper.PresentationMapper

object SearchMapper : PresentationMapper<DomainGithubModel, RepoModel> {

    override fun mapToPresentation(from: DomainGithubModel): RepoModel {
        return RepoModel(
            onwerName = from.onwerName,
            repositoryName = from.repositoryName,
            description = from.description,
            starCount = from.starCount
        )
    }

}