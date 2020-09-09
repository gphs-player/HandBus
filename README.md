## 手写EventBus

[EventBus Github](https://github.com/greenrobot/EventBus)



### 1.事件通知

* 事件的发送与相应
* 事件的订阅与取消

### 2.线程切换

* 主线程
* 子线程

### 3.粘性事件与优先级

* 粘性事件
* 事件优先级

### 4.编译期优化

* APT 收集订阅者信息



### 其他

#### 1.debug APT 

1.添加默认remote

2../gradlew --no-daemon -Dorg.gradle.debug=true :app:clean :app:compileDebugJavaWithJavac

3.打断点，点击debug run 按钮