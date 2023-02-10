package com.candyenk.textediting.plugin;

import android.text.TextUtils;
import android.widget.EditText;
import candyenk.api.textediting.Edit;

/**
 * 插件Edit实现
 * TODO:包括Service拉起
 */
public class PEdit implements Edit {
    private final EditText edit;

    static PEdit create(EditText edit) {
        return new PEdit(edit);
    }

    private PEdit(EditText edit) {
        this.edit = edit;
    }

    @Override
    public void setText(CharSequence text) {
        this.edit.setText(text);
    }

    @Override
    public CharSequence getText() {
        return this.edit.getText();
    }

    @Override
    public void setError(CharSequence text) {
        this.edit.setError(text);
        requestFocus();
    }

    @Override
    public void requestFocus() {
        edit.requestFocus();
    }

    @Override
    public boolean isEmpty() {
        return TextUtils.isEmpty(getText());
    }

}
