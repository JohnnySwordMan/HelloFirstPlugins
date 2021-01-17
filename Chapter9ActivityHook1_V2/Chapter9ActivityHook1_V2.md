# Chapter9ActivityHook1_V2

Activity的插件化解决方案：

1. 启动没有在宿主AndroidManifest.xml中声明的插件Activity
2. 为每个插件构建一个ClassLoader
3. 暂不考虑资源


### 加载插件中的类