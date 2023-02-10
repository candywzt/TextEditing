package candyenk.api.textediting;

/**
 * 插件文本编辑框错做接口
 */
public interface Edit {
    /**
     * 设置编辑框内容
     * 版本:001
     */
    void setText(CharSequence text);

    /**
     * 获取编辑框内容
     * 版本:001
     */
    CharSequence getText();

    /**
     * 设置错误提示
     * 版本:001
     */
    void setError(CharSequence text);

    /**
     * 聚焦
     * 将焦点拉到该编辑框
     * 版本:001
     */
    void requestFocus();

    /**
     * 是不是空内容
     * 版本:001
     */
    boolean isEmpty();
}
