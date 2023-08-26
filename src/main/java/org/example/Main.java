package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static final int quantity = 10000;
    public static final int length = 100000;

    public static void main(String[] args) throws InterruptedException {
        Thread generator = new Thread(() -> {
            for (int i = 0; i < quantity; i++) {
                String text = generateText("abc", length);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        generator.start();

        Thread a = new Thread(() -> {
            maxQuantity(queueA, 'a');
        });
        a.start();

        Thread b = new Thread(() -> {
            maxQuantity(queueB, 'b');
        });
        b.start();

        Thread c = new Thread(() -> {
            maxQuantity(queueC, 'c');
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void maxQuantity(BlockingQueue<String> queue, char letter) {
        String text;
        int count = 0;
        int max = 0;
        for (int i = 0; i < quantity; i++) {
            try {
                text = queue.take();
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Максимальное количество символов " + "'" + letter + "' : " + max);
    }
}