package candyenk.api.textediting;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.LayoutRes;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.InputStream;

public interface Tool {
    /**
     * 发送Toast
     * 版本:001
     */
    void toast(CharSequence text);


    /**
     * 获取插件包内文件
     * 不同于context.getResource
     * 该方法可获取插件包内任何文件
     * 例如(test/test1/test2.txt)
     * 版本:001
     */
    InputStream getResource(String fileName);


    /**
     * 获取文本文件内容
     * 对getResource的包装
     * 版本:001
     */
    String getText(String path);

    /**
     * 获取图片文件Drawable(BitmapDrawable)实例
     * 对getResource的包装
     * 版本:001
     */
    Drawable getDrawable(String path);

    /**
     * 获取插件缓存目录
     * 版本:001
     */
    String getCachePath();

    /**
     * 获取插件存储目录
     * 版本:001
     */
    String getFilesPath();

    /**
     * 获取插件临时缓存目录文件
     * 版本:001
     */
    File getCacheFile(String path);

    /**
     * 获取插件持久存储目录文件
     * 版本:001
     */
    File getFilesFile(String path);

    /**
     * 获取布局View
     * 版本:001
     */
    <T extends View> T getLayout(@LayoutRes int id);


    /**
     * 加载lua脚本
     * 版本:001
     */
    LuaValue getLua(String script);

    LuaValue getLua(File file);


    /**
     * 加载dex文件
     * 版本:001
     */
    ClassLoader getClassLoader(String dexpath) throws ClassNotFoundException;

    /**
     * HTTP-GET请求
     * 版本:001
     */
    void getHttp(String url, HttpCallBack callBack);

    /**
     * HTTP-POST请求
     * 版本:001
     */
    void postHttp(String url, String params, HttpCallBack callBack);

    /**
     * Http访问回调
     */
    interface HttpCallBack {
        void callback(int response, String content);
    }
}
