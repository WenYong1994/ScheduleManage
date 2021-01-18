package com.jimmy.common.skin

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.jimmy.common.util.ToastUtils
import java.io.*

/**
 * @author WhenYoung
 *  CreateAt Time 2021/1/18  15:16
 */
object SkinHelper {
    fun checkStartSkin(context: Context,skinName: String,success : (skinName:String)->Unit) {
        //检查文件里面有没有这个
        val path = SkinEngine.getSKinPath() + "/" + skinName
        val file = File(path)
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val assets: AssetManager =context.assets
                    if (file.exists()) {
                    }
                    if (writeFile(path, assets.open(skinName))) {
                        (context as Activity)?.runOnUiThread(Runnable {
                            success(skinName)

                        })
                    }
                } catch (e: Exception) {
                }
            }
        }.start()
    }


    @Throws(IOException::class)
    private fun writeFile(fileName: String, `in`: InputStream): Boolean {
        var ins: InputStream?= null
        var bRet = true
        try {
            var os: OutputStream? = FileOutputStream(fileName)
            val buffer = ByteArray(4112)
            var read: Int
            while (`in`!!.read(buffer).also { read = it } != -1) {
                os!!.write(buffer, 0, read)
                Log.e("writeFile", "复制中")
            }
            ins?.close()
            ins = null
            os?.flush()
            os?.close()
            os = null
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            bRet = false
        }
        return bRet
    }












}