##（续）
#### 1、ListView取消点击效果
**描述：**用LIstView呈现的数据，类似于静态的那中，但是不要点击效果，默认的就是点击一下显示成橙黄色  
**原因：**主要是实现点击没效果，其实是有效果只不过是透明给掩盖了
**解决：**在listview布局属性中添加如下属性android:listSelector="@android:color/transparent"；
#### 2、Gradle进行dex遇到内存不够用的情况
**描述：**java.lang.OutOfMemoryError: GC overhead limit exceeded；
**原因：**在Gradle进行dex的可能会遇到内存不够用的情况；
**解决：**在build文件的android属性下配置dexOptions下的javaMaxHeapSize大小即可，我这里配置2g；
```
dexOptions {
        javaMaxHeapSize "2g"
    }
```
#### 3、Manifest merger failed : uses-sdk:minSdkVersion 15 cannot be smaller than version 18 declared in library
**描述：**Manifest merger failed : uses-sdk:minSdkVersion 15 cannot be smaller than version 18 declared in library;Suggestion:use tools:overrideLibrary="xxx.xxx.xxx" to force usage.
**原因：**出现这个错误一般是引用库的最低版本高于该工程最低版本；
**解决：**在AndroidManifest.xml文件中 标签中添加<uses-sdk tools:overrideLibrary="xxx.xxx.xxx"/>，其中的xxx.xxx.xxx为第三方库包名，如果存在多个库有此异常，则用逗号分割它们，例如：<uses-sdk tools:overrideLibrary="xxx.xxx.aaa, xxx.xxx.bbb"/>，这样做是为了项目中的AndroidManifest.xml和第三方库的AndroidManifest.xml合并时可以忽略最低版本限制。
#### 4、提交svn时忽略文件无效
**描述：**
**原因：**
**解决：**