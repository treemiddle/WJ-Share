package com.jay.wjshare.data.remote.mapper

import com.jay.wjshare.data.model.DataModel
import com.jay.wjshare.data.remote.model.RemoteModel

interface RemoteMapper<R : RemoteModel, D : DataModel> {

    fun mapToData(from: R): D

}