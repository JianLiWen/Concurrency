import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * java使用循环CAS实现原子操作
 */
public class Counter {
    private AtomicInteger  atomic1 = new AtomicInteger(1);
    private  int i=1;

    public static void main(String[] args) {
        final  Counter cas = new Counter();
        List<Thread> ts = new ArrayList<>(600);
        long start = System.currentTimeMillis();
        for(int j=0;j<100;j++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i< 10000;i++){
                        cas.count();
                        cas.safeCount();
                    }
                }
            });
            ts.add(t);
        }
        for (Thread t:ts){
            t.start();
        }
        for (Thread t:ts){
            try {
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomic1.get());
        System.out.println(System.currentTimeMillis()-start);
    }

    /**
     * 使用CAS实现线程安全计数器
     */
    private  void safeCount(){
        for(;;){
            int i = atomic1.get();
            boolean suc = atomic1.compareAndSet(i,++i);
            if(suc){
                break;
            }
        }
    }

    /**
     * 非线程安全计数器
     */
    private  void count(){
        i++;
    }
}
