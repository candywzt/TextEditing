**Java插注意事项:**

1. Java插件尽量不要混淆,如必要请保留入口类
2. > 采用Java编写插件时,可使用Resources资源获取功能\
   > `Context.getDrawable`获取图片\
   > `Context.getText`获取文本\
   > `Context.getString`获取字符串\
   > `Context.getColor`获取颜色值\
   > `Context.getAnim`获取动画\
   > `Context.getAsset`获取Asset文件\
   > ...\
   > <font color=red>很可惜不能使用Layout</font>\
   > 意味着无法使用XML绘制UI,只能Java创建了\
   > 为此我准备了`candyenk.android.tools.V`View帮助类,用来设置各种控件属性

**Lua插件注意事项:**

1. Lua插件目前正在开发中
2. > 已知问题如下:
   > 使用的工具是LuaJ\
   > <font color=red>不出所料,不能使用AndroidLayout</font>\
   > 因此使用LuaJava极其费劲\
   > 似乎也能够创建Layout,但是不能使用aly文件,只能使用LuaJava原生创建(悲)\
   > 我为此提供了一个StringItemList,可以使用Table创建一个字符串项目列表\
   > 将就着用吧,不要拘泥于UI,多多注重功能(悲)
