package io.github.awish.uikit;
import android.content.Context

import com.zcapi

class ZC(private val _context: Context) : Service {
    private val instance = zcapi()

    /**
     * Reboot the ZC smart module.
     *
     * @return reboot status.
     */
    override fun reboot() = instance.reboot()

    /**
     * Shutdown the ZC smart module.
     *
     * @return shutdown status.
     */
    override fun shutdown() = instance.shutDown()
}
