## Thread#sleep() 方法和 Object#wait() 方法

文件在 `SleepWaitDemo.java`，演示了两个核心区别：

| | `Thread.sleep()` | `Object.wait()` |
|---|---|---|
| 归属 | `Thread` 静态方法 | `Object` 实例方法 |
| 锁 | 不释放锁，抱着锁睡 | 释放锁，进入 WAITING 状态 |
| 唤醒 | 到时间自动醒 | 需要 `notify()`/`notifyAll()` 唤醒 |
| 调用前提 | 任意地方可调 | 必须在 `synchronized` 块内调用（持有对象锁） |


-----

# synchronized

编译运行通过。文件 `SynchronizedDemo.java` 用 4 个场景说明了 `synchronized`：

| 场景 | 说明 |
|---|---|
| 1. 无 synchronized | 两个线程各加 10 万次，结果不是 20 万（丢失更新） |
| 2. 有 synchronized | 同一操作加锁，结果精确 20 万 |
| 3. 不同对象锁 | 锁不同对象互不影响，B 不会被 A 阻塞 |
| 4. 同一对象锁 | 同一把锁，线程 2 必须等线程 1 释放 |

核心结论：`synchronized` 本质上是对**对象监视器（monitor）的互斥访问**——实例方法锁 `this`，静态方法锁 `类对象`，同步块锁指定对象。同一时间同一把锁只能一个线程持有。