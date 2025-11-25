package io.github.awish.uikit;

import android.content.Context
import android.os.Build

interface Service {
    /**
     * Reboot the smart module.
     *
     * @return reboot status.
     */
    fun reboot()
    /**
     * Shutdown the smart module.
     *
     * @return shutdown status.
     */
    fun shutdown()
}

/**
 * 委托模式
 */
abstract class UiKit(private val context: Context): Service by createService(context) {
    companion object {
        /**
         * Creates a new instance of the UiKit.
         *
         * @param context The application context.
         * @return A new instance of the UiKit.
         */
        fun create(context: Context): UiKit {
            return object : UiKit(context) {}
        }

        /**
         * Creates a new instance of the internal service used by the UiKit.
         *
         * This method will create an instance of the internal service based on the
         * device model. If the device model starts with "ZC", a ZC service will be
         * created. If the device model starts with "rk", "ys", or "a527", a YS service will
         * be created. Otherwise, a YS service will be created.
         *
         * @param context The application context.
         * @return A new instance of the internal service used by the UiKit.
         */
        private fun createService(context: Context): Service {
            val ctx = context.applicationContext
            return when {
                Build.MODEL.uppercase().startsWith("ZC", ignoreCase = true) -> ZC(ctx)
                listOf("rk", "ys", "a527").any { Build.MODEL.startsWith(it, ignoreCase = true) } -> YS(ctx)
                else -> YS(ctx)
            }
        }
    }

    /**
     * Get the internal service used by the UiKit.
     *
     * @return the internal service used by the UiKit.
     */
    fun getInternalService(): Service = createService(context)
}
