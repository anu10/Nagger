package edu.ohio_state.cse.nagger;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class PubSub {

    HashMap<String, Set<PubSubListener>> listenerMap;

    Queue<String> queue;
    private static PubSub mPubSub;

    public interface PubSubListener{
        public void onEventReceived(String string);
    }

    public class MyThread extends Thread {

        @Override
        public void run() {
            execute();
        }
    }


    private PubSub(){

        this.listenerMap = new HashMap<>();
        queue = new ConcurrentLinkedQueue<>();

        MyThread t = new MyThread();
        t.start();
    }

    public static PubSub getInstance(){
        if (mPubSub == null){
            mPubSub = new PubSub();
        }

        return mPubSub;
    }

    public void addToListenerMap(PubSubListener listener, String key) {

        if (listenerMap.containsKey(key)){
            listenerMap.get(key).add(listener);
        }
        else {
            Set<PubSubListener> listeners = new HashSet<>();
            listeners.add(listener);
            listenerMap.put(key, listeners);
        }
    }

    public void publish(String event){
        queue.add(event);
    }

    public void removeListener(String key,PubSubListener listener){
        if (listenerMap.containsKey(key)){
            listenerMap.get(key).remove(listener);
        }
    }

    private void execute() {
        while (true){

            while(!queue.isEmpty()) {

                String event = queue.remove();

                if(listenerMap.containsKey(event)) {
                   Set<PubSubListener> subscribers = listenerMap.get(event);

                    for (PubSubListener obj : subscribers) {

                        obj.onEventReceived(event);
                    }
                }
            }

        }
    }


}
