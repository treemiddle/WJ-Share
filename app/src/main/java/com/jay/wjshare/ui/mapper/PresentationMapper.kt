package com.jay.wjshare.ui.mapper

import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.ui.model.PresentationModel

interface PresentationMapper<M : DomainGithubModel, P : PresentationModel> {

    fun mapToPresentation(from: M): P

}