import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * @author:lmw
 * @date:2023/12/27 21:56
 **/
public class AnalysisNioVersion {
    /**
     * 收包端
     *
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        DatagramChannel receiveChannel= DatagramChannel.open();
        InetSocketAddress receiveAddress= new InetSocketAddress(9999);
        receiveChannel.bind(receiveAddress);
        ByteBuffer receiveBuffer= ByteBuffer.allocate(512);
        System.out.println("初始化成功");
        byte a;
        int length; //报文长度
        int sum,temp; // 和校验
        int num;//测点个数
        int startnum;//起始测点号
        while (true) {
            sum=0;
            receiveBuffer.clear();
            SocketAddress sendAddress= receiveChannel.receive(receiveBuffer);
            receiveBuffer.flip();
            //打印总信息 发信息站地址和报文内容
            System.out.println("从站地址："+sendAddress.toString() + " ");
            System.out.println("收到"+receiveBuffer.limit()+"字节信息：");
            System.out.println("----------------");
            while (receiveBuffer.hasRemaining()){
                a=receiveBuffer.get();
                System.out.printf("%02x ",a);
            }
            for (int i = 0; i < receiveBuffer.limit()-2; i++) {
                sum+=(receiveBuffer.get(i)&0xff);
            }
            System.out.println();
            //重置position 重新读receiveBuffer
            receiveBuffer.rewind();
            //验证报文头
            if (Integer.toHexString(receiveBuffer.get(0)&0xff).equals("68") && Integer.toHexString(receiveBuffer.get(4)&0xff).equals("68") )
                System.out.println("----包头校验成功-----");
            else {
                System.out.println("----包头信息不完整，放弃解析本次信息-----");
                continue;
            }
            //获取报文长度  应与limit一样长 一个是实际收到长度一个是发送端计算的长度
            //发包程序有bug 先不检验这个
//            length=(xiaodu(receiveBuffer.get(2))<<8)+xiaodu(receiveBuffer.get(3));
//            if(length!=receiveBuffer.limit()){
//                System.out.println("----实际报文长度与发送端计算的长度不一致，请检查-----");
//                continue;
//            }
//            System.out.println("站地址："+Integer.toHexString(xiaodu(receiveBuffer.get(1))));
//            System.out.println("报文自述长度："+length);

            //和校验 因为发来的报文是低自序
            //temp=((xiaodu(receiveBuffer.get(receiveBuffer.limit()-1))<<8)+(receiveBuffer.get(receiveBuffer.limit()-2))&0xff);
            temp=((receiveBuffer.get(receiveBuffer.limit()-1)&0xff)<<8)+(receiveBuffer.get(receiveBuffer.limit()-2)&0xff);
            if (sum==temp){
                System.out.println("----报文和校验成功-----"); }
            else {
                System.out.println("----报文和校验失败，放弃解析本次信息-----");
                continue;
            }

            //确认大小端
            if (receiveBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
                System.out.println("当前系统采用小端字节序");
            } else if (receiveBuffer.order() == ByteOrder.BIG_ENDIAN) {
                System.out.println("当前系统采用大端字节序 转换为小端");
                receiveBuffer.order(ByteOrder.LITTLE_ENDIAN);
            } else {
                System.out.println("无法确定当前系统的字节序");
            }


            //调整position位置 开始解析
            receiveBuffer.position(9);
            num=receiveBuffer.get(6)&0xff; //总共有多少测点
            startnum=((receiveBuffer.get(7)&0xff)<<8)+(receiveBuffer.get(8)&0xff); //起始测点号
            System.out.println("本条报文包含测绘点数："+num);
            //1和11是int型
            if(Integer.toHexString(receiveBuffer.get(5)&0xff).equals("1")||Integer.toHexString(receiveBuffer.get(5)&0xff).equals("11")){
                System.out.println("测定类型：开入点");
                System.out.println("起始测点号："+startnum);
                for (int i=0;i<num;i++){
                    System.out.println("测点号："+startnum+"  测值数据："+receiveBuffer.get());
                    startnum+=1;
                }
            }else{
                System.out.println("测定类型：模拟量");
                System.out.println("起始测点号："+startnum);
                for (int i=0;i<num;i++){
                    System.out.println("测点号："+startnum+"  测值数据："+receiveBuffer.getFloat());
                    startnum+=1;
                }
            }

            System.out.println("-----本次解析结束------");
        }
    }

}
