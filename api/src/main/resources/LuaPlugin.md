### Lua插件API列表:

*斜体表示与Java版API不同的函数*

#### Table:API下函数

| 函数名          | 参数类型 | 返回值类型             | 作用          |
|--------------|------|-------------------|-------------|
| version      | null | Number            | 获取当前API版本   |
| input/output | null | **Table:Edit**    | 获取Edit实例    |
| config       | null | **Table:Config**  | 获取Config实例  |
| panel        | null | **Table:Panel**   | 获取Panel实例   |
| setting      | null | **Table:Setting** | 获取Setting实例 |
| tool         | null | **Table:Tool**    | 获取Tool实例    |

#### Table:Edit下函数

| 函数名    | 参数类型   | 返回值类型  | 作用       |
|--------|--------|--------|----------|
| *text* | String | null   | 设置Edit内容 |
| *text* | null   | String | 获取Edit内容 |

#### Table:Config下函数

| 函数名        | 参数类型 | 返回值类型  | 作用           |
|------------|------|--------|--------------|
| uuid       | null | String | 获取插件UUID     |
| icon       | null | String | 获取插件图标url    |
| title      | null | String | 获取插件标题       |
| describe   | null | String | 获取插件描述       |
| version    | null | Number | 获取插件版本       |
| createTime | null | Number | 获取插件创建时间     |
| updateTime | null | Number | 获取插件更新时间     |
| author     | null | String | 获取插件作者       |
| authorUrl  | null | String | 获取作者链接       |
| updateUrl  | null | String | 获取插件更新链接     |
| mainLua    | null | String | 获取插件入口       |
| *api*      | null | Number | 获取插件支持的API版本 |

#### Table:Panel下函数

*不建议Lua插件使用布局相关,因为对luajava支持不太美好*
**使用第一步请先设置布局内容**

| 函数名             | 参数类型                    | 返回值类型                   | 作用             |
|-----------------|-------------------------|-------------------------|----------------|
| context         | Nil                     | android.content.Context | 获取用于创建View的上下文 |
| title           | String                  | nil                     | 用于设置标题         |
| content         | android.view.View       | null                    | 设置弹窗内容         |
| contentItem     | Table                   | null                    | 设置弹窗列表内容       |
| itemCenter      | Boolean                 | null                    | 设置Item布局项目是否居中 |
| onItemClick     | Funation(Number,String) | null                    | 设置弹窗列表点击事件     |
| onItemLongClick | Funation(Number,String) | null                    | 设置弹窗列表长按事件     |
| show            | Nil                     | null                    | 拉起弹窗           |
| close           | Nil                     | null                    | 关闭弹窗           |

#### Table:Setting下函数

*Lua函数只允许存取字符串类型*

| 函数名         | 参数类型          | 返回值类型  | 作用        |
|-------------|---------------|--------|-----------|
| openTime    | null          | Number | 获取打开次数    |
| errorTime   | null          | Number | 获取崩溃次数    |
| installTime | null          | Number | 获取安装时间    |
| startTime   | null          | Number | 获取首次启动时间  |
| *keyValue*  | null          | Table  | 获取所有项目    |
| clear       | null          | null   | 清空所有数据    |
| *get*       | String,String | String | 获取单个项目默认值 |
| *set*       | String,String | null   | 保存单个项目    |

#### Table:Tool下函数

| 函数名       | 参数类型                                  | 返回值类型                              | 作用                              |
|-----------|---------------------------------------|------------------------------------|---------------------------------|
| toast     | String                                | null                               | 展示Toast                         |
| icon      | null                                  | android.graphics.drawable.Drawable | 获取插件图标Drawable对象                |
| resources | String                                | java.io.InputStream                | 获取插件包内文件                        |
| text      | String                                | String                             | 获取插件包内文本文件内容                    |
| drawable  | String                                | android.graphics.drawable.Drawable | 获取插件包内图片文件Drawable              |
| *getLua*  | String                                | null                               | **没有这个东西**                      |
| getClass  | String,String                         | java.lang.Class                    | 加载dex文件                         |
| getHttp   | String,funation(Number,String)        | null                               | Http-GET请求(url,回调(状态码,内容))      |
| postHttp  | String,String,funation(Number,String) | null                               | Http-POST请求(url,参数链,回调(状态码,内容)) |


