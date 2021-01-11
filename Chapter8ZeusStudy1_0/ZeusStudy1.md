### 最简单的插件化

1. 插件的四大组件都定义在宿主中
2. 不考虑资源
3. 通过合并dex的方式，将插件的dex添加到宿主的dexElements数组中，这样就可以直接使用宿主的ClassLoader去加载插件的类了

这样就可以做到，在宿主中直接调起插件中的四大组件，例如Service。不需要hook ActivityManagerNative和ActivityThread。
因为在宿主中，是直接标明要启动的插件的组件的