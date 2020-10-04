package com.eureka.sync.event;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import org.json.JSONObject;
import org.springframework.context.ApplicationEvent;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public abstract class DmlEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DmlEvent(Object source) {
        super(source);
    }

    public Object getEntry() {
        return source;
    }
}
