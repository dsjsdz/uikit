package io.github.awish.example;

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import io.github.awish.uikit.UiKit
import io.github.awish.uikit.ZC
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class UiKitUnitTest {
    /**
     * Verify that when the device model is "ZC-3288", the
     * internal service used by the UiKit is a ZC service.
     */
    @Test
    fun uiKit_isZC() {
        ReflectionHelpers.setStaticField(Build::class.java, "MODEL", "ZC-3288")

        val context = ApplicationProvider.getApplicationContext<Context>()
        val service = UiKit.create(context)

        assertTrue(service.getInternalService() is ZC)
    }
}
