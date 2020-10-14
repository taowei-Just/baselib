package com.tao.mvpbaselibrary.basic.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtils {

    //保存文件到指定路径
    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator + "Camera" + File.separator;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(storePath, fileName);

            outStream = new FileOutputStream(file);

            //通过io流的方式来压缩保存图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outStream) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通知相册更新
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, fileName, null);
        //保存图片后发送广播通知更新数据库
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }
}
