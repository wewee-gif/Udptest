/**
 * @author:lmw
 * @date:2023/6/6 14:15
 **/
public class test1 {
    private static int a=0;

    private  void add() throws InterruptedException {
        Thread.currentThread().sleep(500);
        a=100;
    }
    private  void minus(){
        if (a==100){
            System.out.println(true);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        test1 temp =new test1();
        Thread T1=new Thread(()->{

            try {
                temp.add();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        Thread T2=new Thread(()->{

                temp.minus();

        });
        T1.start();
        T1.join();
        T2.start();


    }
}
