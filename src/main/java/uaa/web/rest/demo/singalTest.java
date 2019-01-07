package uaa.web.rest.demo;

import io.netty.util.Signal;
import sun.misc.SignalHandler;

public class singalTest implements SignalHandler{
//    public  static void  main(String[] args){
//        Signal sig = new Signal(getOSSignalType());
////        Signal.handle(sig, this.handle(sig););
//    }
    private static  String getOSSignalType(){
        return System.getProperties().getProperty("os.name").toLowerCase()
            .startsWith("win")?"INI":"USR2";
    }

    @Override
    public void handle(sun.misc.Signal signal) {

    }
}
