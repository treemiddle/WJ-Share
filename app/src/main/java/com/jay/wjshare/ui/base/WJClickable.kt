package com.jay.wjshare.ui.base

import com.jay.wjshare.ui.model.PresentationModel
import io.reactivex.subjects.Subject

interface WJClickable<E : PresentationModel> {
    var onClick: Subject<E>
}