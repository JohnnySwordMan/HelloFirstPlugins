# 对startActivity方法进行Hook

startActivity启动方法有两种，最终都是调用`Instrumentation#execStartActivity`

1. `Activity#startActivity`
2. `Context#startActivity`

对startActivity的hook可以分为两部分，上半场：FirstActivity通知AMS，要启动SecondActivity；下半场：AMS通知App进程，要启动SecondActivity。

上半场可以hook的地方：

1. Activity的mInstrumentation字段。详见Chapter5Hook11
2. AMN的getDefault方法获取到的对象

下半场可以hook的地方：

1. H的mCallback字段
2. Activity的mInstrumentation对象，对应的newActivity方法和callActivityOnCreate方法