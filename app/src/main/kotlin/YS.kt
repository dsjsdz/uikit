package io.github.awish.uikit

import android.content.Context
import com.ys.rkapi.MyManager

/**
 * 亿晟板卡服务封装类
 * 对应文档：亿晟板卡API开发文档.pdf
 *
 * 注意：使用API前需要先绑定AIDL服务，在Activity销毁时需要解绑
 */
class YS(private val context: Context) : Service {
    private val instance: MyManager = MyManager.getInstance(context)

    // ========== 服务绑定方法 ==========

    /**
     * 绑定AIDL服务
     * 部分功能需要绑定服务后才能使用
     */
    fun bindAIDLService() {
        // 根据文档，亿晟API需要绑定AIDL服务
        instance.bindAIDLService(context)
    }

    /**
     * 解绑AIDL服务
     * 在Activity销毁时需要调用
     */
    fun unBindAIDLService() {
        instance.unBindAIDLService(context)
    }

    // ========== 系统控制方法 ==========

    /**
     * 重启亿晟设备
     */
    override fun reboot() = instance.reboot()

    /**
     * 关机亿晟设备
     */
    override fun shutdown() = instance.shutdown()

    /**
     * 进入recovery模式
     */
    fun rebootRecovery() = instance.rebootRecovery()

    // ========== 系统信息方法 ==========

    /**
     * 获取API版本信息
     * 根据文档：public String getApiVersion()
     */
    fun getYSApiVersion(): String = instance.getApiVersion()

    /**
     * 获取设备型号
     * 根据文档：public String getAndroidMode()
     */
    fun getAndroidMode(): String = instance.androidModle

    /**
     * 获取Android系统版本
     * 根据文档：public String getAndroidVersion()
     */
    fun getAndroidVersion(): String = instance.getAndroidVersion()

    /**
     * 获取设备内存容量
     * 根据文档：public String getRunningMemory()
     */
    fun getRunningMemory(): String = instance.getRunningMemory()

    /**
     * 获取设备存储容量
     * 根据文档：public String getInternalStorageMemory()
     */
    fun getInternalStorageMemory(): String = instance.getInternalStorageMemory()

    /**
     * 获取设备固件版本
     * 根据文档：public String getFirmwareVersion()
     */
    fun getYSFirmwareVersion(): String = instance.getFirmwareVersion()

    /**
     * 获取SN号
     * 根据文档：public String getSn()
     */
    fun getSn(): String = instance.getSn()

    /**
     * 获取内核版本
     * 根据文档：public String getKernelVersion()
     */
    fun getKernelVersion(): String = instance.getKernelVersion()

    /**
     * 获取固件显示信息
     * 根据文档：public String getAndroidDisplay()
     */
    fun getAndroidDisplay(): String = instance.getAndroidDisplay()

    /**
     * 获取固件编译时间
     * 根据文档：public String getFirmwareDate()
     */
    fun getFirmwareDate(): String = instance.getFirmwareDate()

    /**
     * 获取CPU型号
     * 根据文档：public String getCPUType()
     */
    fun getCPUType(): String = instance.getCPUType()

    /**
     * 获取厂商识别码
     * 根据文档：public String getVendorID()
     */
    fun getVendorID(): String = instance.getVendorID()

    /**
     * 获取CPU温度
     * 根据文档：public String getCPUTemperature()
     */
    fun getCPUTemperature(): String = instance.cpuTemperature.toString()

    // ========== 显示控制方法 ==========

    /**
     * 截屏保存
     * @param path 保存图片的绝对路径
     * @return 是否截图成功
     */
    fun takeScreenshot(path: String): Boolean {
        bindAIDLService()
        return instance.takeScreenshot(path)
    }

    /**
     * 设置屏幕旋转角度
     * @param degree 旋转角度，支持 0, 90, 180, 270
     */
    fun rotateScreen(degree: String) = instance.rotateScreen(context, degree)

    /**
     * 获取屏幕宽度像素
     * 根据文档：public int getDisplayWidth(Context context)
     */
    fun getDisplayWidth(): Int = instance.getDisplayWidth(context)

    /**
     * 获取屏幕高度像素
     * 根据文档：public int getDisplayHeight(Context context)
     */
    fun getDisplayHeight(): Int = instance.getDisplayHeight(context)

    /**
     * 导航栏状态设置
     * @param hide true:隐藏导航栏, false:显示导航栏
     */
    fun hideNavBar(hide: Boolean) = instance.hideNavBar(hide)

    /**
     * 导航栏状态查询
     * @return true:隐藏, false:显示
     */
    fun getNavBarHideState(): Boolean = instance.getNavBarHideState()

    /**
     * 设置滑出导航栏开关
     * @param flag true:打开滑出导航栏, false:关闭滑出导航栏
     */
    fun setSlideShowNavBar(flag: Boolean) = instance.setSlideShowNavBar(flag)

    /**
     * 查询滑出导航栏是否打开
     * 根据文档：public boolean isSlideShowNavBarOpen
     * 注意：这是一个属性，不是方法
     */
    fun isSlideShowNavBarOpen(): Boolean = instance.isSlideShowNavBarOpen

    /**
     * 设置滑出通知栏开关
     * @param flag true:打开滑出通知栏, false:关闭滑出通知栏
     */
    fun setSlideShowNotificationBar(flag: Boolean) = instance.setSlideShowNotificationBar(flag)

    /**
     * 查询滑出通知栏状态
     * 根据文档：public boolean isSlideShowNotificationBarOpen()
     */
    fun isSlideShowNotificationBarOpen(): Boolean = instance.isSlideShowNotificationBarOpen()

    /**
     * 设置屏幕亮度
     * @param value 亮度值 1-100
     */
    fun changeScreenLight(value: Int) = instance.changeScreenLight(value)

    /**
     * 背光控制
     * 根据文档方法名称修正
     */
    fun turnOffBacklight() = instance.turnOffBackLight()
    fun turnOnBacklight() = instance.turnOnBackLight()
    fun isBacklightOn(): Boolean = instance.isBacklightOn()
    fun getSystemBrightness(): Int = instance.getSystemBrightness()

    /**
     * HDMI控制
     * 根据文档方法名称修正
     */
    fun turnOffHDMI() = instance.turnOffHDMI()
    fun turnOnHDMI() = instance.turnOnHDMI()

    /**
     * 状态栏控制
     */
    fun hideStatusBar(hide: Boolean) = instance.hideStatusBar(hide)
    fun getStatusBarState(): Boolean = instance.getStatusBar()

    /**
     * 副屏截图
     * @param path 保存图片的绝对路径
     * @return 是否截图成功
     */
    fun viceScreenshot(path: String): Boolean {
        bindAIDLService()
        return instance.viceScreenshot(path)
    }

    /**
     * 获取主屏幕类型
     */
    fun getHomeScreenType(): String = instance.getHomeScreenType()

    /**
     * 获取副屏幕类型
     */
    fun getSecondaryScreenType(): String = instance.getSecondaryScreenType()

    /**
     * 获取DPI
     */
    fun getDpi(): String = instance.getDpi()

    /**
     * 设置DPI
     * @param value 0=160, 1=240, 2=360, 3=480
     * @return 是否设置成功
     */
    fun setDpi(value: Int): Boolean = instance.setDpi(value)

    // ========== 安装升级方法 ==========

    /**
     * 固件升级
     * @param absolutePath 固件文件的绝对路径
     */
    fun upgradeSystem(absolutePath: String) = instance.upgradeSystem(absolutePath)

    /**
     * 静默安装APK
     * @param apkPath APK文件的绝对路径
     * @return 是否安装成功
     */
    fun silentInstallApk(apkPath: String): Boolean = instance.silentInstallApk(apkPath,true)

    /**
     * 静默卸载APK
     * @param packageName 应用包名
     * @return 是否卸载成功
     */
    fun unInstallApk(packageName: String): Boolean {
        // 根据文档，静默卸载方法需要确认
        return try {
            instance.unInstallApk(packageName)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 设置升级固件是否弹窗
     * @param flag true:升级时弹窗, false:升级不弹窗
     */
    fun setUpdateSystemWithDialog(flag: Boolean) = instance.setUpdateSystemWithDialog(flag)

    /**
     * 设置升级后是否删除固件包
     * @param flag true:升级成功后删除, false:升级成功后不删除
     */
    fun setUpdateSystemDelete(flag: Boolean) = instance.setUpdateSystemDelete(flag)

    // ========== 网络控制方法 ==========

    /**
     * 获取以太网MAC地址
     */
    fun getEthMacAddress(): String = instance.getEthMacAddress()

    /**
     * 获取以太网连接模式
     */
    fun getEthMode(): String = instance.getEthMode()

    /**
     * 获取以太网开关状态
     */
    fun getEthStatus(): Boolean {
        bindAIDLService()
        return instance.getEthStatus()
    }

    /**
     * 获取子网掩码
     */
    fun getNetMask(): String {
        bindAIDLService()
        return instance.getNetMask()
    }

    /**
     * 获取网关
     */
    fun getGateway(): String {
        bindAIDLService()
        return instance.getGateway()
    }

    /**
     * 获取DNS1
     */
    fun getEthDns1(): String {
        bindAIDLService()
        return instance.getEthDns1()
    }

    /**
     * 获取DNS2
     */
    fun getEthDns2(): String {
        bindAIDLService()
        return instance.getEthDns2()
    }

    /**
     * 设置以太网为动态获取模式
     */
    fun setDhcpIpAddress() = instance.setDhcpIpAddress(context)

    /**
     * 设置以太网为静态地址模式
     */
    fun setStaticEthIPAddress(ip: String, gateway: String, mask: String, dns1: String, dns2: String) =
        instance.setStaticEthIPAddress(ip, gateway, mask, dns1, dns2)

    /**
     * 获取以太网动态IP地址
     */
    fun getDhcpIpAddress(): String = instance.getDhcpIpAddress()

    /**
     * 获取以太网静态IP地址
     */
    fun getStaticEthIPAddress(): String {
        bindAIDLService()
        return instance.getStaticEthIPAddress()
    }

    /**
     * 控制以太网开关
     * @param enable true:打开以太网, false:关闭以太网
     */
    fun ethEnabled(enable: Boolean) = instance.ethEnabled(enable)

    /**
     * PPPoE拨号上网
     * @param userName 用户名
     * @param password 密码
     */
    fun setPppoeDial(userName: String, password: String) =
        instance.setPppoeDial(context, userName, password)

    /**
     * 获取当前网络类型
     * @return 0:以太网, 1:WIFI, 2:移动网络, -100:未知网络
     */
    fun getCurrentNetType(): Int = instance.getCurrentNetType()

    // ========== 存储路径方法 ==========

    /**
     * 获取SD卡路径
     */
    fun getSDcardPath(): String = instance.getSDcardPath()

    /**
     * 获取U盘路径
     * @param num U盘序号，从0开始
     */
    fun getUSBStoragePath(num: Int): String = instance.getUSBStoragePath(num)

    /**
     * 获取串口绝对路径
     * @param uart 串口号，如 "ttyS0"
     */
    fun getUartPath(uart: String): String = instance.getUartPath(uart)

    // ========== 定时开关机方法 ==========

    /**
     * 设置周模式的定时开关机
     * @param powerOnTime 开机时间 [时, 分]
     * @param powerOffTime 关机时间 [时, 分]
     * @param weekdays 周一到周日的工作状态 [1,1,1,1,1,0,0]
     */
    fun setPowerOnOffWithWeekly(powerOnTime: IntArray, powerOffTime: IntArray, weekdays: IntArray) =
        instance.setPowerOnOffWithWeekly(powerOnTime, powerOffTime, weekdays)

    /**
     * 设置一组定时开关机
     * @param powerOnTime 开机时间 [年, 月, 日, 时, 分]
     * @param powerOffTime 关机时间 [年, 月, 日, 时, 分]
     */
    fun setPowerOnOff(powerOnTime: IntArray, powerOffTime: IntArray) =
        instance.setPowerOnOff(powerOnTime, powerOffTime)

    /**
     * 获取当前定时开关机模式
     * @return "0":本地设置, "1":setPowerOnOff方法, "2":setPowerOnOffWithWeekly方法
     */
    fun getPowerOnMode(): String = instance.getPowerOnMode()

    /**
     * 获取当前定时开关机的开机时间
     */
    fun getPowerOnTime(): String = instance.getPowerOnTime()

    /**
     * 获取当前定时开关机的关机时间
     */
    fun getPowerOffTime(): String = instance.getPowerOffTime()

    /**
     * 获取设备上一次的定时开关机的开机时间
     */
    fun getLastestPowerOnTime(): String = instance.getLastestPowerOnTime()

    /**
     * 获取设备上一次的定时开关机的关机时间
     */
    fun getLastestPowerOffTime(): String = instance.getLastestPowerOffTime()

    /**
     * 清除定时开关机数据
     */
    fun clearPowerOnOffTime() = instance.clearPowerOnOffTime()

    /**
     * 获取定时开关机版本
     */
    fun getPowerVersion(): String = instance.getVersion()

    /**
     * 获取设备是否设置了定时开关机
     */
    fun isSetPowerOnTime(): Boolean = instance.isSetPowerOnTime()

    // ========== GPIO控制方法 ==========

    /**
     * 设置GPIO方向
     * @param gpio GPIO编号
     * @param arg 1:输入, 0:输出
     * @return 是否设置成功
     */
    fun setGpioDirection(gpio: Int, arg: Int): Boolean = instance.setGpioDirection(gpio, arg)

    /**
     * 获取GPIO方向
     * @param gpio GPIO编号
     * @return "in":输入, "out":输出
     */
    fun getGpioDirection(gpio: Int): String = instance.getGpioDirection(gpio)

    /**
     * 设置GPIO电平
     * @param gpio GPIO编号
     * @param arg "1":高电平, "0":低电平
     * @return 是否设置成功
     */
    fun writeGpioValue(gpio: Int, arg: String): Boolean = instance.writeGpioValue(gpio, arg)

    /**
     * 获取GPIO电平
     * @param gpio GPIO编号
     * @return "1":高电平, "0":低电平
     */
    fun getGpioValue(gpio: Int): String = instance.getGpioValue(gpio)

    // ========== 其他功能方法 ==========

    /**
     * 设置系统时间
     */
    fun setTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, sec: Int) =
        instance.setTime(year, month, day, hour, minute, sec)

    /**
     * 打开或关闭自动确定时间
     * @param open true:打开, false:关闭
     */
    fun switchAutoTime(open: Boolean) = instance.switchAutoTime(open)

    /**
     * 控制软键盘是否能弹出
     * @param hidden true:隐藏软键盘, false:显示软键盘
     */
    fun setSoftKeyboardHidden(hidden: Boolean) = instance.setSoftKeyboardHidden(hidden)

    /**
     * 休眠时间的控制
     * @param time 间隔时间（毫秒）
     */
    fun setDormantInterval(time: Long) = instance.setDormantInterval(context, time)

    /**
     * 查询自动确定网络时间状态
     */
    fun isAutoSyncTime(): Boolean = instance.isAutoSyncTime()

    /**
     * 以ROOT权限运行shell命令
     * @param command shell命令
     */
    fun execSuCmd(command: String) = instance.execSuCmd()

    /**
     * 查询HDMI输入状态
     * 根据文档：public int getHdminStatus()
     */
    fun getHdminStatus(): Boolean = instance.hdmiinStatus

    /**
     * 设置默认输入法
     * @param defaultInputMethod 输入法包名
     * @return 是否设置成功
     */
    fun isSetDefaultInputMethodSuccess(defaultInputMethod: String): Boolean =
        instance.isSetDefaultInputMethodSuccess(defaultInputMethod)

    /**
     * 获取默认输入法
     */
    fun getDefaultInputMethod(): String = instance.getDefaultInputMethod()

    /**
     * 设置系统语言
     * @param language 语言代码，如 "zh"
     * @param country 国家代码，如 "CN"
     */
    fun setLanguage(language: String, country: String) = instance.setLanguage(language, country)

    /**
     * 打开或关闭ADB连接
     * @param open true:打开, false:关闭
     */
    fun setADBOpen(open: Boolean) = instance.setADBOpen(open)

    /**
     * 替换开机动画
     * @param path 开机动画文件路径
     */
    fun replaceBootanimation(path: String) = instance.replaceBootanimation(path)

    /**
     * 设置默认Launcher
     * @param packageAndClassName Launcher包名和类名
     */
    fun setDefaultLauncher(packageAndClassName: String) =
        instance.setDefaultLauncher(packageAndClassName)

    /**
     * 设置开机自启动
     * @param packageName 应用包名
     */
    fun selfStart(packageName: String) = instance.selfStart(packageName)

    /**
     * 设置守护进程应用
     * @param packageName 应用包名
     * @param value 守护时间 0=30秒, 1=60秒, 2=180秒
     */
    fun daemon(packageName: String, value: Int) = instance.daemon(packageName, value)

    /**
     * 应用安装白名单
     * @param isopen true:启用白名单, false:不启用
     * @param packageName 应用包名
     */
    fun setAppInstallWhitelist(isopen: String, packageName: String) =
        instance.setAppInstallWhitelist(isopen, packageName)

    /**
     * 应用安装黑名单
     * @param isopen true:启用黑名单, false:不启用
     * @param packageName 应用包名
     */
    fun setAppInstallBlacklist(isopen: String, packageName: String) =
        instance.setAppInstallBlacklist(isopen, packageName)

    /**
     * 打开或关闭网络ADB连接
     * @param open true:打开, false:关闭
     */
    fun setNetworkAdb(open: Boolean) = instance.setNetworkAdb(open)

    // ========== Service接口实现 ==========

    override fun getDeviceModel(): String = getAndroidMode()
    override fun getSerialNumber(): String = getSn()
    override fun getApiVersion(): String = getYSApiVersion()
    override fun getFirmwareVersion(): String = getYSFirmwareVersion()
    override fun getMemorySize(): String = getRunningMemory()
    override fun getStorageSize(): String = getInternalStorageMemory()
}


