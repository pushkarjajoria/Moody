package com.example.moody.text_watcher;

public class QueueFactory {

    private static final QueueFactory singleton = new QueueFactory();

    public static QueueFactory getInstance() {
        return singleton;
    }


    private QueueFactory() {
        beforeQueue = new TextChangeQueue();
        afterQueue = new TextChangeQueue();
    }

    private static TextChangeQueue beforeQueue;
    private static TextChangeQueue afterQueue;

    public static TextChangeQueue getBeforeChangeInstance() {
        return beforeQueue;
    }

    public static TextChangeQueue getAfterChangeInstance() {
        return afterQueue;
    }

}
