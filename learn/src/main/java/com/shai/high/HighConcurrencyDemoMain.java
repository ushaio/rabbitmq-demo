package com.shai.high;

/**
 * 高并发场景下的技术问题演示主类
 * 包含多个高并发相关的示例
 */
public class HighConcurrencyDemoMain {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("高并发场景下的技术问题演示");
        System.out.println("==================================");
        
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String demoType = args[0].toLowerCase();
        
        switch (demoType) {
            case "unsafe":
                System.out.println("运行线程不安全演示...");
                ThreadUnsafeDemo.main(new String[]{});
                break;
                
            case "safe":
                System.out.println("运行线程安全演示...");
                ThreadSafeDemo.main(new String[]{});
                break;
                
            case "atomic":
                System.out.println("运行原子操作演示...");
                AtomicDemo.main(new String[]{});
                break;
                
            case "pc":
                System.out.println("运行生产者-消费者演示...");
                ProducerConsumerDemo.main(new String[]{});
                break;
                
            case "pool":
                System.out.println("运行线程池演示...");
                ThreadPoolDemo.main(new String[]{});
                break;
                
            case "perf":
                System.out.println("运行性能优化演示...");
                PerformanceOptimizationDemo.main(new String[]{});
                break;
                
            case "all":
                runAllDemos();
                break;
                
            default:
                printUsage();
                break;
        }
    }
    
    private static void printUsage() {
        System.out.println("使用方法:");
        System.out.println("  java -cp <classpath> com.shai.high.HighConcurrencyDemoMain <demo_type>");
        System.out.println();
        System.out.println("可选的演示类型:");
        System.out.println("  unsafe - 线程不安全演示");
        System.out.println("  safe   - 线程安全演示");
        System.out.println("  atomic - 原子操作演示");
        System.out.println("  pc     - 生产者-消费者演示");
        System.out.println("  pool   - 线程池演示");
        System.out.println("  perf   - 性能优化演示");
        System.out.println("  all    - 运行所有演示");
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java -cp <classpath> com.shai.high.HighConcurrencyDemoMain unsafe");
    }
    
    private static void runAllDemos() throws InterruptedException {
        System.out.println("运行所有高并发演示...\n");
        
        System.out.println("1. 线程不安全演示:");
        ThreadUnsafeDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("2. 线程安全演示:");
        ThreadSafeDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("3. 原子操作演示:");
        AtomicDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("4. 生产者-消费者演示:");
        ProducerConsumerDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("5. 线程池演示:");
        ThreadPoolDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("6. 性能优化演示:");
        PerformanceOptimizationDemo.main(new String[]{});
        System.out.println("\n" + getRepeatedString("=", 50) + "\n");
        
        System.out.println("所有演示运行完成!");
    }
    
    // Java 8兼容的字符串重复方法
    private static String getRepeatedString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}