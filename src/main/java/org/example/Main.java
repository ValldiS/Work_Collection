package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> queueForA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueForB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueForC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        Thread threadGenerate = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueForA.put(text);
                    queueForB.put(text);
                    queueForC.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread threadFindMaxCountA = new Thread(() -> {
            String maxRepetitions = "";
            int count = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueForA.take();
                    maxRepetitions = findMaxText("a", text, maxRepetitions);
                    count = countMax("a", text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(maxRepetitions + "Число повторений " + count + "\n");
        });

        Thread threadFindMaxCountB = new Thread(() -> {
            String maxRepetitions = "";
            int count = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueForB.take();
                    maxRepetitions = findMaxText("b", text, maxRepetitions);
                    count = countMax("b", text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(maxRepetitions + "Число повторений " + count + "\n");
        });

        Thread threadFindMaxCountC = new Thread(() -> {
            String maxRepetitions = "";
            int count = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueForC.take();
                    maxRepetitions = findMaxText("c", text, maxRepetitions);
                    count = countMax("c", text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(maxRepetitions + "Число повторений " + count + "\n");
        });


        threadGenerate.start();
        threadFindMaxCountA.start();
        threadFindMaxCountB.start();
        threadFindMaxCountC.start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countMax(String symbol, String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (String.valueOf(text.charAt(i)).equals(symbol)) {
                count++;
            }
        }
        return count;
    }

    public static String findMaxText(String symbol, String text, String maxRepetitions) {
        if (maxRepetitions.length() != text.length()) {
            return text;
        }

        int countText = 0;
        int countMaxRepetitions = 0;

        for (int i = 0; i < text.length(); i++) {
            if (String.valueOf(text.charAt(i)).equals(symbol)) {
                countText++;
            }
            if (String.valueOf(maxRepetitions.charAt(i)).equals(symbol)) {
                countMaxRepetitions++;
            }
        }
        return (countMaxRepetitions > countText) ? maxRepetitions : text;
    }
}