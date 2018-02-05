package org.lmelaia.iseries.common.net.xcom;

/**
 * Used to receive messages from clients.
 */
public interface MessageListener {

    /**
     * Called when a new message is received.
     *
     * @param m the message sent.
     */
    void onMessageReceived(Message m);
}