package com.eureka.sync.event;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public class TruncateDmlEvent extends DmlEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public TruncateDmlEvent(Object source) {
        super(source);
    }
}
