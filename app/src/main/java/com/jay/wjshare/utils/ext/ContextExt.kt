package com.jay.wjshare.utils.ext

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * 브로드캐스트리시버 관련 확장
 * 코드를 줄이고자 addReceiver를 만들었는데 일관성을 위해 remove, send도 만듦.
 */
fun Context.addReceiver(
    receiver: BroadcastReceiver,
    action: String
) {
    registerReceiver(
        receiver,
        IntentFilter().apply { addAction(action) }
    )
}

fun Context.removeReceiver(receiver: BroadcastReceiver) = unregisterReceiver(receiver)

fun Context.sendReceiverMessage(intent: Intent) = sendBroadcast(intent)