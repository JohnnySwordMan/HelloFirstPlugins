#### 启动没有在AndoridManifest.xml中声明的插件Activity    


使用AMSHookHelper，欺上瞒下。

1. 对AMN进行hook，把真正要启动的插件Activity替换为在manifest中声明的替身StubActivity，进而骗过AMS。AMSHookHelper#hookAMN
2. 对ActivityThread进行hook，把StubActivity换回要启动的插件Activity。AMSHookHelper#hookActivityThread
3. 把插件apk对应的LoadedApk对象直接存储到mPackages中，同时将LoadedApk对象的ClassLoader改成插件的ClassLoader。

LoadedApk是apk在内存中的表现，可以从中获取apk的各种信息。

```java
// performLaunchActivity方法
java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
```  

可以看到，创建Activity对象，需要classloader对象，r.packageInfo返回的是LoadedApk对象。因此，`我们需要构建插件的LoadedApk对象`。

在handleMessage方法中，会先通过`getPackageInfoNoCheck`方法获取LoadedApk对象，对在mPackages对象中获取，如果为空，则new LoadedApk。那么我们将创建好的插件的LoadedApk对象存储到mPackages对象中。


创建ClassLoader对象时，需要传入apk的path，因此，宿主和插件的ClassLoader是否一样？ 可以一样，也可以不一样。
