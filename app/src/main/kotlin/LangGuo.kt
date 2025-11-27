package io.github.awish.uikit;

import android.content.Context
import android.content.Intent

// 广播方式 by LangGuo v1.1
class LangGuo(private val context: Context) : Service {
    /**
     * Reboot the smart module using a broadcast intent.
     *
     * This method sends a broadcast intent with the action
     * "android.intent.action.reboot" to trigger a reboot of the
     * smart module.
     */
    override fun reboot() {
        val intent = Intent().apply {
            action = "android.intent.action.reboot"
        }
        context.sendBroadcast(intent)
    }

    /**
     * Shutdown the smart module using a broadcast intent.
     *
     * This method sends a broadcast intent with the action
     * "android.intent.action.shutdown" to trigger a shutdown of the
     * smart module.
     */
    override fun shutdown() {
        val intent = Intent().apply {
            action = "android.intent.action.shutdown"
        }
        context.sendBroadcast(intent)
    }

    override fun getDeviceModel(): String? {
        TODO("Not yet implemented")
    }

    override fun getSerialNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getApiVersion(): String? {
        TODO("Not yet implemented")
    }

    override fun getFirmwareVersion(): String? {
        TODO("Not yet implemented")
    }

    override fun getMemorySize(): String? {
        TODO("Not yet implemented")
    }

    override fun getStorageSize(): String? {
        TODO("Not yet implemented")
    }

    // 其他未实现的直接 todo
}
