import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Math.*;


public class test {
    public static void main(String[] args) {
        Long a=22222222l;
        ExecutorService service = Executors.newFixedThreadPool(30);
        int i = 0;
        int num = 22222222;
        ArrayList<Integer> primeNumbers = new ArrayList<>();
        while (i<1000){
            num++;
            PrimeCallable t = new PrimeCallable(num);
            Future<Integer> s = service.submit(t);
            try {
                Integer primeNumber = s.get();
                if (primeNumber!=0){
                    primeNumbers.add(primeNumber);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        System.out.println(primeNumbers.size());


    }
    //判断质数
    public static boolean IsPrime(int num){
        boolean flag =true;
        for(int i=2; i<=Math.sqrt(num); i++){
            if(num % i ==0){
                //System.out.println(num + "不是素数");
                flag = false;
                break;
            }
        }
        return flag;
    }
    //线程任务
    static class PrimeCallable implements Callable<Integer> {
        private int number;

        public PrimeCallable(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public Integer call() throws Exception {
            long l = System.currentTimeMillis();
            if (IsPrime(number)){
                long l1 = System.currentTimeMillis();
                System.out.println("质数："+number+",线程号："+Thread.currentThread().getId()+","+"耗时："+(l1-l));
                return number;
            }else {
                return 0;
            }
        }
    }


}


