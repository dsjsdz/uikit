package io.github.awish.uikit

import android.content.Context
import android.os.Build
import com.zcapi

/**
 * 卓策板卡服务封装类
 * 对应文档：卓策开发说明文档_v5.5.pdf
 *
 * 注意：部分功能需要系统级权限，需在manifest中加入
 * android:sharedUserId="android.uid.system" 至<manifest />结点中，并为应用使用系统签名
 */
class ZC(private val _context: Context) : Service {
    private val instance = zcapi().apply {
        getContext(_context.applicationContext)
    }

    // ========== 系统控制方法 ==========

    /**
     * 重启卓策设备
     */
    override fun reboot() = instance.reboot()

    /**
     * 关机卓策设备
     */
    override fun shutdown() = instance.shutDown()

    /**
     * 恢复出厂设置
     * 注意：恢复出厂设置时不要插着U盘，否则可能格式化U盘
     */
    fun factoryReset() = instance.factoryReset()

    /**
     * OTA升级
     * 注意：不同型号的OTA包文件名限制不同，升级后OTA包会被删除
     */
    fun updateOta() = instance.updateOta()

    /**
     * 设置定时开关机
     * @param enable 是否启用定时开关机
     * @param onTime 开机时间数组 [年, 月, 日, 时, 分]
     * @param offTime 关机时间数组 [年, 月, 日, 时, 分]
     */
    fun setPowerOnOffTime(enable: Boolean, onTime: IntArray, offTime: IntArray) {
        // 根据文档，卓策的定时开关机方法名为 setPowerOnOffTime
        // 参数顺序：enable, onTime, offTime
        instance.setPowetOnOffTime(enable, onTime, offTime)
    }

    /**
     * 设置系统时间
     * @param time 时间数组 [年, 月, 日, 时, 分, 秒]
     */
    fun setSystemTime(time: IntArray) = instance.setSystemTime(time)

    // ========== 系统信息方法 ==========

    /**
     * 获取板卡型号
     */
    fun getBuildModel(): String = instance.getBuildModel()

    /**
     * 获取板卡SN号
     */
    fun getBuildSerial(): String = instance.getBuildSerial()

    /**
     * 获取以太网MAC地址
     * @param ifaceName 网卡名称，如 "eth0"
     */
    fun getEthMacAddress(ifaceName: String): String = instance.getEthMacAddress(ifaceName)

    /**
     * 获取WLAN MAC地址（前提是打开WIFI）
     */
    fun getWifiMacAddress(): String = instance.getWifiMacAddress()

    // ========== 显示控制方法 ==========

    /**
     * 背光控制
     * @param enable true:打开背光, false:关闭背光
     */
    fun setLcdOnOff(enable: Boolean) = instance.setLcdOnOff(enable)

    /**
     * HDMI控制（仅支持328、339系列产品）
     * @param enable true:打开, false:关闭
     * @param lcdOrHdmi 0:只操控LCD背光, 1:同时操控HDMI和LCD背光, -1:仅操控HDMI
     */
    fun setHdmiOnOff(enable: Boolean, lcdOrHdmi: Int) = instance.setLcdOnOff(enable, lcdOrHdmi)

    /**
     * 隐藏导航栏、状态栏
     * @param enable true:显示, false:隐藏
     */
    fun setStatusBar(enable: Boolean) = instance.setStatusBar(enable)

    /**
     * 允许、禁止滑动呼出状态栏
     * @param enable true:允许, false:禁止
     */
    fun setGestureStatusBar(enable: Boolean) = instance.setGestureStatusBar(enable)

    // ========== GPIO控制方法 ==========

    /**
     * GPIO控制 - 设置IO引脚模式
     * @param group 引脚组名，如 'B'
     * @param num 引脚号，如 18
     * @param value 1:输出, 0:输入
     */
    fun setGpioDirection(group: Char, num: Int, value: Int) =
        instance.setMulSelGpio(group, num, value)

    /**
     * GPIO控制 - 读取IO引脚状态
     * @param group 引脚组名
     * @param num 引脚号
     * @return 1:高电平, 0:低电平
     */
    fun readGpio(group: Char, num: Int): Int = instance.readGpio(group, num)

    /**
     * GPIO控制 - 写入IO引脚状态
     * @param group 引脚组名
     * @param num 引脚号
     * @param value 1:高电平, 0:低电平
     */
    fun writeGpio(group: Char, num: Int, value: Int) = instance.writeGpio(group, num, value)

    // ========== 其他功能方法 ==========

    /**
     * 看门狗控制
     * @param enable true:使能看门狗, false:关闭看门狗
     *                使能后两分钟内需要再次喂狗，否则系统重启
     */
    fun setWatchDog(enable: Boolean) = instance.watchDogEnable(enable)

    /**
     * 执行SU命令
     * @param cmd 需要执行的shell命令
     */
    fun execShellCmd(cmd: String) = instance.execShellCmd(cmd)

    /**
     * 截屏
     * @param path 图片保存路径
     * @param fileName 图片文件名
     */
    fun screenshot(path: String, fileName: String) = instance.screenshot(path, fileName)

    /**
     * 静默安装应用
     * @param apkPath APK文件路径
     * @param silentMode true:使用超级权限静默安装, false:普通方式安装
     */
    fun installApk(apkPath: String, silentMode: Boolean) =
        instance.InstallApk(apkPath, silentMode)

    /**
     * 设置以太网静态IP
     * @param ip IP地址
     * @param gateway 网关
     * @param netMask 子网掩码
     * @param dns1 DNS1
     * @param dns2 DNS2
     * @param ifaceName 网卡名称，默认"eth0"
     */
    fun setStaticIP(ip: String, gateway: String, netMask: String, dns1: String, dns2: String, ifaceName: String = "eth0") =
        instance.SetStaticIP(ip, gateway, netMask, dns1, dns2, ifaceName)

    /**
     * 设置以太网动态IP
     * @param ifaceName 网卡名称，默认"eth0"
     */
    fun setDHCP(ifaceName: String = "eth0") = instance.setDHCP(ifaceName)

    // ========== Service接口实现 ==========

    override fun getDeviceModel(): String = getBuildModel()
    override fun getSerialNumber(): String = getBuildSerial()

    // 卓策板卡不支持以下方法，返回默认值
    override fun getApiVersion(): String? = null
    override fun getFirmwareVersion(): String? = null
    override fun getMemorySize(): String? = null
    override fun getStorageSize(): String? = null
}




