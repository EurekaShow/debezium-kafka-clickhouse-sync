package com.eureka.sync.event;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public class DeleteDmlEvent extends DmlEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DeleteDmlEvent(Object source) {
        super(source);
    }
}
