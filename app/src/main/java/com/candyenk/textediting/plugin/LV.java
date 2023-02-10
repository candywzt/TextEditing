package com.candyenk.textediting.plugin;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.*;

/**
 * 方便使用Lambda创建Lua函数的接口
 */
public interface LV {
    LuaValue NIL = LuaValue.NIL;

    /**
     * 无参数
     */
    static LuaValue zero(LV lv) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                LuaValue v = lv.call();
                if (v == null) {
                    lv.c();
                    return NIL;
                } else {
                    return v;
                }
            }
        };
    }

    /**
     * 单参数
     */
    static LuaValue one(LV lv) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                LuaValue v = lv.call(arg);
                if (v == null) {
                    lv.c(arg);
                    return NIL;
                } else {
                    return v;
                }
            }
        };
    }

    /**
     * 双参数
     */
    static LuaValue two(LV lv) {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2) {
                LuaValue v = lv.call(arg1, arg2);
                if (v == null) {
                    lv.c(arg1, arg2);
                    return NIL;
                } else {
                    return v;
                }
            }
        };
    }

    /**
     * 三参数
     */
    static LuaValue three(LV lv) {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
                LuaValue v = lv.call(arg1, arg2, arg3);
                if (v == null) {
                    lv.c(arg1, arg2, arg3);
                    return NIL;
                } else {
                    return v;
                }
            }
        };
    }

    /**
     * 多参数
     */
    static LuaValue var(LV lv) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                Varargs v = lv.call(args);
                if (v == null) {
                    lv.c(args);
                    return NIL;
                } else {
                    return v;
                }
            }
        };
    }

    default LuaValue call() {return null;}

    default LuaValue call(LuaValue a) {return call();}

    default LuaValue call(LuaValue a1, LuaValue a2) {return call(a1);}

    default LuaValue call(LuaValue a1, LuaValue a2, LuaValue a3) {return call(a1, a2);}

    default Varargs call(Varargs v) {return call(v.arg(1), v.arg(2), v.arg(3));}

    default void c() {}

    default void c(LuaValue a) {c();}

    default void c(LuaValue a1, LuaValue a2) {c(a1);}

    default void c(LuaValue a1, LuaValue a2, LuaValue a3) {c(a1, a2);}

    default void c(Varargs v) {c(v.arg(1), v.arg(2), v.arg(3));}

    interface Zero extends LV {
        @Override
        LuaValue call();
    }

    interface One extends LV {
        @Override
        LuaValue call(LuaValue a);
    }

    interface Two extends LV {
        @Override
        LuaValue call(LuaValue a1, LuaValue a2);
    }

    interface Three extends LV {
        @Override
        LuaValue call(LuaValue a1, LuaValue a2, LuaValue a3);
    }

    interface Var extends LV {
        @Override
        LuaValue call(Varargs v);
    }

    interface ZeroZero extends LV {
        @Override
        void c();
    }

    interface OneZero extends LV {
        @Override
        void c(LuaValue a);
    }

    interface TwoZero extends LV {
        @Override
        void c(LuaValue a1, LuaValue a2);
    }

    interface ThreeZero extends LV {
        @Override
        void c(LuaValue a1, LuaValue a2, LuaValue a3);
    }

    interface VarZero extends LV {
        @Override
        void c(Varargs v);
    }
}
