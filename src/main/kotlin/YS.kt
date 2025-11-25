package io.github.awish.uikit;

import android.content.Context
import com.ys.rkapi.MyManager

class YS(private val context: Context): Service {
    private val instance: MyManager = MyManager.getInstance(context)

    /**
     * Reboot the YS smart module.
     *
     * @return reboot status.
     */
    override fun reboot() = instance.reboot()

    /**
     * Shutdown the YS smart module.
     *
     * @return shutdown status.
     */
    override fun shutdown() = instance.shutdown()
}
