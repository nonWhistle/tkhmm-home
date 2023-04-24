package com.tkhmm.application.broadcaster;

import com.tkhmm.application.views.cvview.CvViewData;
import com.vaadin.flow.shared.Registration;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class CvViewBroadcaster {

    private static final Logger log = Logger.getLogger(CvViewBroadcaster.class.getSimpleName());

    public static Executor executor = Executors.newSingleThreadExecutor();

    static HashMap<Consumer<CvViewData>, Long> listeners = new HashMap<>();

    public static synchronized Registration register(Consumer<CvViewData> listener, long id) {
        listeners.put(listener, id);
        log.info("REGISTERING: " + id);
        listeners.forEach((k, v) -> log.info(k + "\t" + v));
        return () -> {
            synchronized (CvViewBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(CvViewData cvViewData, long id) {
        for (Consumer<CvViewData> listener : listeners.keySet()) {
            if (listeners.get(listener) == id) {
                executor.execute(() -> listener.accept(cvViewData));
            }
        }
    }
}
