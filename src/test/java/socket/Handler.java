package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    final SocketChannel channel;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        channel = socketChannel;
        socketChannel.configureBlocking(false);
        sk = channel.register(selector, 0);
        //将handler作为callback对象
        sk.attach(this);
        //注册read就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }
    boolean inputIsComplete()
    {
        /* ... */
        return false;
    }

    boolean outputIsComplete()
    {

        /* ... */
        return false;
    }

    void process()
    {
        /* ... */
        return;
    }

    @Override
    public void run() {
        try
        {
            if (state == READING)
            {
                read();
            }
            else if (state == SENDING)
            {
                send();
            }
        } catch (IOException ex)
        { /* ... */ }
    }
    void read() throws IOException
    {
        channel.read(input);
        if (inputIsComplete())
        {

            process();

            state = SENDING;
            // Normally also do first write now

            //第三步,接收write就绪事件
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException
    {
        channel.write(output);

        //write完就结束了, 关闭select key
        if (outputIsComplete())
        {
            sk.cancel();
        }
    }
}
