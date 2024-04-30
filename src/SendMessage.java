import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.DatagramChannel;
import java.util.BitSet;

/**
 * @author:lmw
 * @date:2023/12/27 21:59
 **/
public class SendMessage {
    /**
     * 发包的 datagram
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void sendDatagram() throws IOException, InterruptedException {
        BitSet bitSet=new BitSet(4);
        System.out.println(bitSet.get(1));
        bitSet.set(2,3);
        System.out.println(bitSet);
//        DatagramChannel sendChannel= DatagramChannel.open();
//        InetSocketAddress sendAddress= new InetSocketAddress("127.0.0.1", 9999);
//        ByteBuffer buffer=ByteBuffer.allocate(6);
//        FloatBuffer buffer1= FloatBuffer.allocate(1);
//        Float a=43.126f;
//        String str="发包";
//        while (true) {
//            buffer.clear();
//            //sendChannel.send(ByteBuffer.wrap("发包".getBytes("UTF-8")), sendAddress);
//            buffer.putFloat(a);
//            //buffer.putFloat(a);
////            buffer.put(str.getBytes());
//            buffer.flip();
//            sendChannel.send(buffer,sendAddress);
//            buffer.rewind();
//            while (buffer.hasRemaining()){
//                System.out.printf("%02x",buffer.get());
//            }
//            System.out.println();
//            System.out.println("发包端发包");
//            Thread.sleep(10000);
//        }

    }


}
