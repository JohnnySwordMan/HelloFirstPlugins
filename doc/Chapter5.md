# 对startActivity方法进行Hook

startActivity启动方法有两种，最终都是调用`Instrumentation#execStartActivity`

1. `Activity#startActivity`
2. `Context#startActivity`

对startActivity的hook可以分为两部分，上半场：FirstActivity通知AMS，要启动SecondActivity；下半场：AMS通知App进程，要启动SecondActivity。

上半场可以hook的地方：

1. Activity的mInstrumentation字段。详见Chapter5Hook11
2. AMN的getDefault方法获取到的对象。详见Chapter5Hook12

下半场可以hook的地方：

1. H的mCallback字段
2. Activity的mInstrumentation对象，对应的newActivity方法和callActivityOnCreate方法


### Chapter5Hook12   

对AMN的getDefault方法进行hook

### Chapter5Hook13  

对H类的mCallback字段进行hook。H是一个Handler对象，AMS发送启动Activity消息给App进程的时候， ActivityThread是调用mH的sendMessage方法，最终会调用mCallback的handleMessage方法，发送消息。

```java
// Handle system messages here.
public void dispatchMessage(Message msg) {
    if (msg.callback != null) {
        handleCallback(msg);
    } else {
        if (mCallback != null) {
            if (mCallback.handleMessage(msg))) {
                return;
            }
            handleMessage(msg);
        }
    }
}
```

因此，我们 可以对H类的mCallback字段进行hook，拦截其handleMessage方法。

- 使用静态代理，只有两个类，一个是Handler.Callback，另一个是Instrumentation。参与Android系统运转的类，暴露给我们的只有这两个。
- 使用动态代理，只有两个接口，一个是IPackageManager，另一个是IActviityManager。


### 再次对Instrumentation字段进行Hook  

startActivity的下半场  
ActivityThread内部也有一个mInstrumentation字段，ActivityThread会调用mInstrumentation的newActivity方法生成Activity对象，然后调用mInstrumentation的callActivityOnCreate方法启动这个Activity。
