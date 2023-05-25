import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author: lmw
 * @create: 2023-05-06
 */

public class AnalysisMessage {

    public static void main(String[] args) {
        try (DatagramSocket serversocket = new DatagramSocket(1234)){
            while (true){
                int length ;
                int sum=0,num=0,startnum=0;
                float tempf;
                byte[] buff = new byte[1024];
                DatagramPacket packet=new DatagramPacket(buff,buff.length);
                serversocket.receive(packet);
                String word = new String( packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8 );
                //获取报文长度
                length=(buff[2]<<8)+buff[3];
                System.out.println("收到字节信息：");
                System.out.println("----------------");
                for (int i=0;i<length;i++){
                    System.out.printf("%02x ",buff[i]);
                }
                //校验包头
                System.out.println();
                if (Integer.toHexString(buff[0]).equals("68") && Integer.toHexString(buff[4]).equals("68") )
                    System.out.println("----包头校验成功-----");
                else {
                    System.out.println("----包头信息不完整，放弃解析本次信息-----");
                    continue;
                }
                System.out.println("站地址："+Integer.toHexString(buff[1]));

                System.out.println("本条报文长度："+length);
                //校验报文内容
                for (int i=0;i<length-2;i++){
                    sum+=buff[i];
                }

                //if (sum==((buff[length-2]<<8)+buff[length-1]))

                //System.out.println("\n");
                System.out.println("----------------");
                //System.out.printf("%02d",(buff[length-2]&0xFF));
                //System.out.print((buff[length-1])<<8);
                int temp1=(int)((buff[length-1]&0xFF)<<8)+(int)(buff[length-2]&0xFF);
                //System.out.println("temp:"+temp1);
                if (sum==temp1)
                    System.out.println("----报文内容校验成功-----");
                else {
                    System.out.println("----本次报文内容不完整，放弃解析本次信息-----");
                    continue;
                }

                num=buff[6];
                startnum=((buff[7]<<8)+buff[8]);
                if (Integer.toHexString(buff[5]).equals("1")||Integer.toHexString(buff[5]).equals("11")){
                    System.out.println("测定类型：开入点");
                    System.out.println("本条报文包含测绘点数："+num);
                    System.out.println("起始测点号："+startnum);
                    for (int i=0,j=9;i<num;i++,j++){
                        System.out.println("测点号："+startnum+"  测值数据："+buff[j]);
                        startnum+=1;
                    }
                }
                else{
                    System.out.println("测定类型：模拟量");
                    System.out.println("本条报文包含测绘点数："+num);
                    System.out.println("起始测点号："+startnum);
                    for (int i=0,j=9;i<num;i++,j++){
                        tempf=(buff[j]<<24)+(buff[j+1]<<16)+(buff[j+2]<<8)+(buff[j+3]);
                        System.out.println("测点号："+startnum+"  测值数据："+tempf);
                        startnum+=1;
                    }
                }
                System.out.println("-----本次解析结束------");
//                System.out.println("\n收到的第一个字节信息："+buff[0]);
//                int temp=buff[0];
//                System.out.println("转成int型的值："+temp);
//                System.out.println("转成十六进制的值："+Integer.toHexString(temp));
//                System.out.println("收到的第四个字节信息："+buff[3]);
//                System.out.println("收到信息："+word);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
