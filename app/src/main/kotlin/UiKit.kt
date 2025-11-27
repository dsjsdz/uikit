package io.github.awish.uikit

import android.content.Context
import android.os.Build
import android.util.Log

/**
 * 智能板卡服务接口
 * 封装了亿晟、卓策和LangGuo板卡的通用功能
 */
interface Service {
    /**
     * 重启智能模块
     */
    fun reboot()

    /**
     * 关闭智能模块
     */
    fun shutdown()

    /**
     * 获取设备型号
     */
    fun getDeviceModel(): String?

    /**
     * 获取设备SN号
     */
    fun getSerialNumber(): String?

    /**
     * 获取API版本信息
     */
    fun getApiVersion(): String?

    /**
     * 获取固件版本
     */
    fun getFirmwareVersion(): String?

    /**
     * 获取内存容量
     */
    fun getMemorySize(): String?

    /**
     * 获取存储容量
     */
    fun getStorageSize(): String?
}

/**
 * 统一板卡控制工具类
 * 采用委托模式自动选择亿晟、卓策或LangGuo板卡服务
 */
class UiKit private constructor(private val context: Context) : Service {

    companion object {
        private const val TAG = "UiKit"

        /**
         * 创建UiKit实例
         */
        @JvmStatic
        fun create(context: Context): UiKit {
            return UiKit(context)
        }

        /**
         * 创建内部服务实例
         */
        private fun createService(context: Context): Service {
            val ctx = context.applicationContext
            val model = Build.MODEL.uppercase()

            return when {
                model.startsWith("ZC", ignoreCase = true) -> {
                    Log.d(TAG, "使用卓策板卡服务，设备型号: $model")
                    ZC(ctx)
                }

                listOf("lg").any { Build.MODEL.startsWith(it, ignoreCase = true) } -> {
                    Log.d(TAG, "使用LangGuo板卡服务，设备型号: $model")
                    LangGuo(ctx)
                }

                listOf("RK", "YS", "A527").any { model.startsWith(it, ignoreCase = true) } -> {
                    Log.d(TAG, "使用亿晟板卡服务，设备型号: $model")
                    YS(ctx).apply {
                        // 亿晟服务需要绑定AIDL
                        bindAIDLService()
                    }
                }

                else -> {
                    Log.d(TAG, "使用默认亿晟板卡服务，设备型号: $model")
                    YS(ctx).apply {
                        bindAIDLService()
                    }
                }
            }
        }

        /**
         * 获取当前设备支持的板卡类型
         */
        @JvmStatic
        fun getBoardType(): String {
            val model = Build.MODEL.uppercase()
            return when {
                model.startsWith("ZC", ignoreCase = true) -> "卓策板卡"
                listOf("lg").any { Build.MODEL.startsWith(it, ignoreCase = true) } -> "LangGuo板卡"
                listOf("RK", "YS", "A527").any { model.startsWith(it, ignoreCase = true) } -> "亿晟板卡"
                else -> "未知板卡(默认使用亿晟)"
            }
        }
    }

    // 内部服务实例
    private val internalService: Service = createService(context)

    /**
     * 获取内部服务实例
     */
    fun getInternalService(): Service = internalService

    /**
     * 获取服务类型信息
     */
    fun getServiceInfo(): String {
        return when (internalService) {
            is ZC -> "卓策板卡服务 v2.x"
            is YS -> "亿晟板卡服务"
            is LangGuo -> "LangGuo板卡服务"
            else -> "未知服务"
        }
    }

    // ========== Service接口实现 ==========

    override fun reboot() {
        internalService.reboot()
    }

    override fun shutdown() {
        internalService.shutdown()
    }

    override fun getDeviceModel(): String? {
        return internalService.getDeviceModel()
    }

    override fun getSerialNumber(): String? {
        return internalService.getSerialNumber()
    }

    override fun getApiVersion(): String? {
        return internalService.getApiVersion()
    }

    override fun getFirmwareVersion(): String? {
        return internalService.getFirmwareVersion()
    }

    override fun getMemorySize(): String? {
        return internalService.getMemorySize()
    }

    override fun getStorageSize(): String? {
        return internalService.getStorageSize()
    }

    // ========== 系统控制方法 ==========

    /**
     * 安全重启设备
     */
    fun safeReboot() {
        Log.i(TAG, "开始安全重启设备")
        try {
            reboot()
            Log.i(TAG, "重启命令已发送")
        } catch (e: Exception) {
            Log.e(TAG, "重启设备失败", e)
            throw e
        }
    }

    /**
     * 安全关闭设备
     */
    fun safeShutdown() {
        Log.i(TAG, "开始安全关闭设备")
        try {
            shutdown()
            Log.i(TAG, "关闭命令已发送")
        } catch (e: Exception) {
            Log.e(TAG, "关闭设备失败", e)
            throw e
        }
    }

    /**
     * 恢复出厂设置
     */
    fun factoryReset() {
        when (val service = internalService) {
            is ZC -> service.factoryReset()
            is YS -> service.rebootRecovery()
            else -> throw UnsupportedOperationException("当前板卡不支持恢复出厂设置")
        }
    }

    /**
     * OTA升级
     */
    fun updateOta() {
        when (val service = internalService) {
            is ZC -> service.updateOta()
            else -> throw UnsupportedOperationException("当前板卡不支持OTA升级")
        }
    }

    // ========== 系统信息方法 ==========

    /**
     * 获取设备详细信息
     */
    fun getDeviceInfo(): Map<String, String?> {
        return mapOf(
            "设备型号" to Build.MODEL,
            "板卡类型" to Companion.getBoardType(),
            "Android版本" to (Build.VERSION.RELEASE ?: "未知"),
            "API级别" to Build.VERSION.SDK_INT.toString(),
            "板卡型号" to getDeviceModel(),
            "SN序列号" to getSerialNumber(),
            "API版本" to getApiVersion(),
            "固件版本" to getFirmwareVersion(),
            "内存容量" to getMemorySize(),
            "存储容量" to getStorageSize()
        )
    }

    /**
     * 获取Android系统版本
     */
    fun getAndroidVersion(): String? {
        return when (val service = internalService) {
            is YS -> service.getAndroidVersion()
            else -> Build.VERSION.RELEASE
        }
    }

    /**
     * 获取CPU温度
     */
    fun getCpuTemperature(): String? {
        return when (val service = internalService) {
            is YS -> service.getCPUTemperature()
            else -> null
        }
    }

    // ========== 显示控制方法 ==========

    /**
     * 截屏保存
     */
    fun takeScreenshot(path: String, fileName: String? = null): Boolean {
        return when (val service = internalService) {
            is ZC -> {
                service.screenshot(path, fileName ?: "screenshot.png")
                true
            }

            is YS -> service.takeScreenshot("$path/${fileName ?: "screenshot.jpg"}")
            else -> false
        }
    }

    /**
     * 设置屏幕旋转角度
     */
    fun rotateScreen(degree: String) {
        when (val service = internalService) {
            is YS -> service.rotateScreen(degree)
            else -> Log.w(TAG, "当前板卡不支持屏幕旋转")
        }
    }

    /**
     * 背光控制
     */
    fun setBacklight(enabled: Boolean) {
        when (val service = internalService) {
            is YS -> if (enabled) service.turnOnBacklight() else service.turnOffBacklight()
            is ZC -> service.setLcdOnOff(enabled)
            else -> Log.w(TAG, "当前板卡不支持背光控制")
        }
    }

    /**
     * 获取背光状态
     */
    fun isBacklightOn(): Boolean? {
        return when (val service = internalService) {
            is YS -> service.isBacklightOn()
            else -> null
        }
    }

    /**
     * HDMI控制
     */
    fun setHdmiOutput(enabled: Boolean) {
        when (val service = internalService) {
            is YS -> if (enabled) service.turnOnHDMI() else service.turnOffHDMI()
            is ZC -> service.setHdmiOnOff(enabled, -1)
            else -> Log.w(TAG, "当前板卡不支持HDMI控制")
        }
    }

    /**
     * 获取HDMI状态
     */
    fun getHdmiStatus(): Boolean? {
        return when (val service = internalService) {
            is YS -> service.getHdminStatus()
            else -> null
        }
    }

    // ========== 导航栏和状态栏控制方法 ==========

    /**
     * 设置导航栏显示/隐藏
     */
    fun setNavigationBarVisibility(visible: Boolean) {
        when (val service = internalService) {
            is YS -> service.hideNavBar(!visible)
            is ZC -> service.setStatusBar(visible)
            else -> Log.w(TAG, "当前板卡不支持导航栏控制")
        }
    }

    /**
     * 获取导航栏状态
     */
    fun getNavigationBarState(): Boolean? {
        return when (val service = internalService) {
            is YS -> !service.getNavBarHideState()
            else -> null
        }
    }

    /**
     * 设置状态栏显示/隐藏
     */
    fun setStatusBarVisibility(visible: Boolean) {
        when (val service = internalService) {
            is YS -> service.hideStatusBar(!visible)
            is ZC -> service.setStatusBar(visible)
            else -> Log.w(TAG, "当前板卡不支持状态栏控制")
        }
    }

    /**
     * 获取状态栏状态
     */
    fun getStatusBarState(): Boolean? {
        return when (val service = internalService) {
            is YS -> !service.getStatusBarState()
            else -> null
        }
    }

    /**
     * 设置滑出导航栏开关
     */
    fun setSlideNavigationBar(enabled: Boolean) {
        when (val service = internalService) {
            is YS -> service.setSlideShowNavBar(enabled)
            else -> Log.w(TAG, "当前板卡不支持滑出导航栏控制")
        }
    }

    /**
     * 获取滑出导航栏状态
     */
    fun getSlideNavigationBarState(): Boolean? {
        return when (val service = internalService) {
            is YS -> service.isSlideShowNavBarOpen()
            else -> null
        }
    }

    /**
     * 设置滑出通知栏开关
     */
    fun setSlideNotificationBar(enabled: Boolean) {
        when (val service = internalService) {
            is YS -> service.setSlideShowNotificationBar(enabled)
            else -> Log.w(TAG, "当前板卡不支持滑出通知栏控制")
        }
    }

    /**
     * 获取滑出通知栏状态
     */
    fun getSlideNotificationBarState(): Boolean? {
        return when (val service = internalService) {
            is YS -> service.isSlideShowNotificationBarOpen()
            else -> null
        }
    }

    // ========== 安装升级方法 ==========

    /**
     * 静默安装APK
     */
    fun silentInstallApk(apkPath: String): Boolean {
        return when (val service = internalService) {
            is YS -> service.silentInstallApk(apkPath)
            is ZC -> {
                service.installApk(apkPath, true)
                true
            }

            else -> false
        }
    }

    /**
     * 静默卸载APK
     */
    fun silentUninstallApk(packageName: String): Boolean {
        return when (val service = internalService) {
            is YS -> service.unInstallApk(packageName)
            else -> false
        }
    }

    /**
     * 固件升级
     */
    fun upgradeSystem(updatePath: String) {
        when (val service = internalService) {
            is YS -> service.upgradeSystem(updatePath)
            else -> throw UnsupportedOperationException("当前板卡不支持固件升级")
        }
    }

    // ========== 网络控制方法 ==========

    /**
     * 设置以太网为动态获取模式
     */
    fun setEthernetDhcp() {
        when (val service = internalService) {
            is YS -> service.setDhcpIpAddress()
            is ZC -> service.setDHCP()
            else -> Log.w(TAG, "当前板卡不支持以太网DHCP设置")
        }
    }

    /**
     * 设置以太网为静态IP模式
     */
    fun setEthernetStaticIp(
        ip: String,
        gateway: String,
        netmask: String,
        dns1: String,
        dns2: String = "0.0.0.0"
    ) {
        when (val service = internalService) {
            is YS -> service.setStaticEthIPAddress(ip, gateway, netmask, dns1, dns2)
            is ZC -> service.setStaticIP(ip, gateway, netmask, dns1, dns2)
            else -> Log.w(TAG, "当前板卡不支持以太网静态IP设置")
        }
    }

    /**
     * 获取以太网MAC地址
     */
    fun getEthernetMacAddress(): String? {
        return when (val service = internalService) {
            is YS -> service.getEthMacAddress()
            is ZC -> service.getEthMacAddress("eth0")
            else -> null
        }
    }

    /**
     * 控制以太网开关
     */
    fun setEthernetEnabled(enabled: Boolean) {
        when (val service = internalService) {
            is YS -> service.ethEnabled(enabled)
            else -> Log.w(TAG, "当前板卡不支持以太网开关控制")
        }
    }

    // ========== 定时开关机方法 ==========

    /**
     * 设置周模式定时开关机
     */
    fun setWeeklyPowerSchedule(powerOnTime: IntArray, powerOffTime: IntArray, weekdays: IntArray) {
        when (val service = internalService) {
            is YS -> service.setPowerOnOffWithWeekly(powerOnTime, powerOffTime, weekdays)
            else -> Log.w(TAG, "当前板卡不支持周模式定时开关机")
        }
    }

    /**
     * 设置单次定时开关机
     */
    fun setOneTimePowerSchedule(powerOnTime: IntArray, powerOffTime: IntArray) {
        when (val service = internalService) {
            is YS -> service.setPowerOnOff(powerOnTime, powerOffTime)
            is ZC -> service.setPowerOnOffTime(true, powerOnTime, powerOffTime)
            else -> Log.w(TAG, "当前板卡不支持定时开关机")
        }
    }

    /**
     * 清除定时开关机设置
     */
    fun clearPowerSchedule() {
        when (val service = internalService) {
            is YS -> service.clearPowerOnOffTime()
            else -> Log.w(TAG, "当前板卡不支持清除定时开关机")
        }
    }

    // ========== GPIO控制方法 ==========

    /**
     * 设置GPIO方向
     */
    fun setGpioDirection(gpio: Any, direction: Int): Boolean {
        return when (val service = internalService) {
            is YS -> service.setGpioDirection(gpio as Int, direction)
            is ZC -> {
                val (group, num) = gpio as Pair<Char, Int>
                service.setGpioDirection(group, num, direction)
                true
            }

            else -> false
        }
    }

    /**
     * 写入GPIO值
     */
    fun writeGpioValue(gpio: Any, value: Any): Boolean {
        return when (val service = internalService) {
            is YS -> service.writeGpioValue(gpio as Int, value as String)
            is ZC -> {
                val (group, num) = gpio as Pair<Char, Int>
                service.writeGpio(group, num, value as Int)
                true
            }

            else -> false
        }
    }

    /**
     * 读取GPIO值
     */
    fun readGpioValue(gpio: Any): String? {
        return when (val service = internalService) {
            is YS -> service.getGpioValue(gpio as Int)
            is ZC -> {
                val (group, num) = gpio as Pair<Char, Int>
                service.readGpio(group, num).toString()
            }

            else -> null
        }
    }

    // ========== 其他功能方法 ==========

    /**
     * 执行SU命令
     */
    fun executeSuCommand(command: String) {
        when (val service = internalService) {
            is YS -> service.execSuCmd(command)
            is ZC -> service.execShellCmd(command)
            else -> Log.w(TAG, "当前板卡不支持SU命令执行")
        }
    }

    /**
     * 设置系统时间
     */
    fun setSystemTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {
        when (val service = internalService) {
            is YS -> service.setTime(year, month, day, hour, minute, second)
            is ZC -> service.setSystemTime(intArrayOf(year, month, day, hour, minute, second))
            else -> Log.w(TAG, "当前板卡不支持系统时间设置")
        }
    }

    /**
     * 设置屏幕亮度
     */
    fun setScreenBrightness(level: Int) {
        when (val service = internalService) {
            is YS -> service.changeScreenLight(level)
            else -> Log.w(TAG, "当前板卡不支持屏幕亮度设置")
        }
    }

    /**
     * 设置开机自启动应用
     */
    fun setAutoStartApp(packageName: String) {
        when (val service = internalService) {
            is YS -> service.selfStart(packageName)
            else -> Log.w(TAG, "当前板卡不支持开机自启动设置")
        }
    }

    /**
     * 设置守护进程
     */
    fun setDaemonApp(packageName: String, guardTime: Int) {
        when (val service = internalService) {
            is YS -> service.daemon(packageName, guardTime)
            else -> Log.w(TAG, "当前板卡不支持守护进程设置")
        }
    }

    /**
     * 设置默认Launcher
     */
    fun setDefaultLauncher(packageAndClassName: String) {
        when (val service = internalService) {
            is YS -> service.setDefaultLauncher(packageAndClassName)
            else -> Log.w(TAG, "当前板卡不支持默认Launcher设置")
        }
    }

    /**
     * 设置系统语言
     */
    fun setSystemLanguage(language: String, country: String) {
        when (val service = internalService) {
            is YS -> service.setLanguage(language, country)
            else -> Log.w(TAG, "当前板卡不支持系统语言设置")
        }
    }

    /**
     * 设置默认输入法
     */
    fun setDefaultInputMethod(inputMethod: String): Boolean {
        return when (val service = internalService) {
            is YS -> service.isSetDefaultInputMethodSuccess(inputMethod)
            else -> false
        }
    }

    /**
     * 看门狗控制
     */
    fun setWatchdog(enabled: Boolean) {
        when (val service = internalService) {
            is ZC -> service.setWatchDog(enabled)
            else -> Log.w(TAG, "当前板卡不支持看门狗控制")
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        when (val service = internalService) {
            is YS -> service.unBindAIDLService()
        }
    }
}

// 扩展函数和工具类
object DeviceCapabilityChecker {
    /**
     * 检查设备是否支持GPIO控制
     */
    @JvmStatic
    fun supportsGpioControl(): Boolean {
        return UiKit.getBoardType() != "未知板卡(默认使用亿晟)"
    }

    /**
     * 检查设备是否支持导航栏控制
     */
    @JvmStatic
    fun supportsNavigationBarControl(): Boolean {
        return UiKit.getBoardType() != "未知板卡(默认使用亿晟)"
    }

    /**
     * 检查设备是否支持状态栏控制
     */
    @JvmStatic
    fun supportsStatusBarControl(): Boolean {
        return UiKit.getBoardType() != "未知板卡(默认使用亿晟)"
    }
}

// 便捷扩展函数
fun UiKit.Companion.createWithValidation(context: Context): UiKit {
    val uikit = create(context)
    Log.d("UiKit", "UiKit初始化完成，板卡类型: ${getBoardType()}")
    return uikit
}
