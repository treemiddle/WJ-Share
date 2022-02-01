package com.jay.wjshare.data.mapper

import com.jay.wjshare.data.model.DataModel
import com.jay.wjshare.domain.model.DomainModel

interface DataMapper<D : DataModel, M : DomainModel> {

    fun mapToDomain(from: D): M

}