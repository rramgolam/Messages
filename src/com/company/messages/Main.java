package com.company.messages;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Message message = new Message();
        (new Thread(new Writer(message))).start();
        (new Thread(new Reader(message))).start();
    }
}

class Message {
    private String message;
    private boolean empty = true;

    public synchronized String read() {
        while(empty){
            // wait until there's a message to read
            try {
                wait();
            } catch (InterruptedException e) {

            }

        }
        empty = true;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {
        while(!empty) {
            // wait until its empty before writing
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        empty = false;
        this.message = message;
        notifyAll();
    }
}

class Writer implements Runnable {
    private Message message;

    public Writer(Message message) {
        this.message = message;
    }

    // Create the messages
    @Override
    public void run() {
        String[] messages = {
                "Message One ...",
                "Message Two ...",
                "Message Three ...",
                "Message Four ..."
        };

        Random random = new Random();
        for (int i = 0; i < messages.length; i++) {
            message.write(messages[i]);
            // now wait
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {

            }
        }
        message.write("Finished");
    }
}

class Reader implements Runnable {
    private Message message;

    public Reader(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        Random random = new Random();
        // Start with the first message and carry on reading until "Finished" is received
        for(String current = message.read(); !current.equals("Finished"); current = message.read()) {
            System.out.println(current);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {

            }
        }
    }
}
