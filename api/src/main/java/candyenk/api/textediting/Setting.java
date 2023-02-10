package candyenk.api.textediting;

import java.util.Set;

/**
 * 插件持久存储接口
 */
public interface Setting {
    /**
     * 获取Int
     * 版本:001
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取Long
     * 版本:001
     */
    long getLong(String key, long defaultValue);

    /**
     * 获取Float
     * 版本:001
     */
    float getFloat(String key, float defaultValue);

    /**
     * 获取Double
     * 版本:001
     */
    double getDouble(String key, double defaultValue);

    /**
     * 获取String
     * 版本:001
     */
    String getString(String key, String defaultValue);

    /**
     * 获取Boolean
     * 版本:001
     */
    boolean getBoolean(String key, boolean defaultValue);


    /**
     * 保存Int
     * 版本:001
     */
    void setInt(String key, int value);

    /**
     * 保存Long
     * 版本:001
     */
    void setLong(String key, long value);

    /**
     * 保存Float
     * 版本:001
     */
    void setFloat(String key, float value);

    /**
     * 保存Double
     * 版本:001
     */
    void setDouble(String key, double value);

    /**
     * 保存String
     * 版本:001
     */
    void setString(String key, String value);

    /**
     * 保存Boolean
     * 版本:001
     */
    void setBoolean(String key, boolean value);

    /**
     * 获取启动次数
     * 版本:001
     */
    int getOpenCount();

    /**
     * 获取崩溃次数
     * 版本:001
     */
    int getErrorCount();

    /**
     * 获取最近更新时间
     * 版本:001
     */
    long getUpdateTime();

    /**
     * 获取首次启动时间
     * 版本:001
     */
    long getInstallTime();

    /**
     * 获取Key集合
     * 版本:001
     */
    Set<String> keySet();

    /**
     * 清空数据
     * 版本:001
     */
    void clear();
}
