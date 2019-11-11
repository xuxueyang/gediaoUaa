package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{

    final Selector selector;
    final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        //非阻塞
        serverSocket.socket().bind(new InetSocketAddress(9100));

        //分布处理
        SelectionKey key = serverSocket.register(selector,SelectionKey.OP_ACCEPT);
        //attach callback object, acceptor
        key.attach(new Acceptor());

    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey next = iterator.next();
                    dispatch(next);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey next) {
        Object attachment = next.attachment();
        if(attachment!=null){
            ((Runnable)attachment).run();
        }
    }

    private class Acceptor implements Runnable{
        @Override
        public void run() {
            try {
                SocketChannel channel  = serverSocket.accept();
                if(channel !=null){
                    new Handler(selector,channel);
                }
            }catch (IOException e){

            }
        }
    }
}
