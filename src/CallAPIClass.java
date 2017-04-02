/**
 * Created by tarushjain on 01/04/17.
 *
 **/

import java.util.*;

public class CallAPIClass extends Thread{

    int id;
    String url;

    CallAPIClass(int id, String url){
        this.id = id;
        this.url = url;
    }
    public void run(){
        //Pass this parameter to Image Processing Server
        //System.out.println("thread is running with ID   :" + this.id + "    and URL   :" + this.url);
        Thread currentThread = Thread.currentThread();
        long ThreadID = currentThread.getId();
        List details = Arrays.asList(this.id, this.url);
        printList(details, ThreadID);
    }

/*    public void main(String args[]) {
        CallAPIClass face = new CallAPIClass(1 , null );
        new Thread(face).start();
}*/

    public void printList(List l, long tid){
        System.out.println("ID of the Thread is :" + tid);
        System.out.println("Image ID    :" + l.get(0));
        System.out.println("Image URL    :" + l.get(1));
    }
 }

