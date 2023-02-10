package candyenk.api.textediting;

/**
 * 插件日志接口
 * 只有三级
 */
public interface Log {
    /**
     * Info级别日志记录
     * 附带返回值false
     */
    boolean i(Object msg);

    /**
     * Info级别日志记录
     * 附带返回值boolean
     */
    boolean i(Object msg, boolean b);

    /**
     * Info级别日志记录
     * 附带返回值int
     */
    int i(Object msg, int i);

    /**
     * Info级别日志记录
     * 附带返回值Object
     */
    <T> T i(Object msg, T t);

    /**
     * Debug级别日志记录
     * 附带返回值false
     */
    boolean d(Object msg);

    /**
     * Debug级别日志记录
     * 附带返回值boolean
     */
    boolean d(Object msg, boolean b);

    /**
     * Debug级别日志记录
     * 附带返回值int
     */
    int d(Object msg, int i);

    /**
     * Debug级别日志记录
     * 附带返回值Object
     */
    <T> T d(Object msg, T t);

    /**
     * Error级别日志记录
     * 附带返回值false
     */
    boolean e(Object msg);

    /**
     * Error级别日志记录
     * 附带返回值boolean
     */
    boolean e(Object msg, boolean b);

    /**
     * Error级别日志记录
     * 附带返回值int
     */
    int e(Object msg, int i);

    /**
     * Error级别日志记录
     * 附带返回值Object
     */
    <T> T e(Object msg, T t);

    /**
     * Error级别日志记录
     * 附带参数Exception
     * 附带返回值false
     */
    boolean e(Throwable e, Object msg);

    /**
     * Error级别日志记录
     * 附带参数Exception
     * 附带返回值boolean
     */
    boolean e(Throwable e, Object msg, boolean b);

    /**
     * Error级别日志记录
     * 附带参数Exception
     * 附带返回值int
     */
    int e(Throwable e, Object msg, int i);

    /**
     * Error级别日志记录
     * 附带参数Exception
     * 附带返回值Object
     */
    <T> T e(Throwable e, Object msg, T t);
}
