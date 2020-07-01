package uj.java.pwj2019.kindergarten;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Kindergarten {
    public static void main(String[] args) throws IOException {
        init();

        final var fileName = args[0];
        System.out.println("File name: " + fileName);
        //TODO: read children file, and keep children NOT hungry!
        BufferedReader kidsReader=new BufferedReader(new FileReader(new File(fileName)));
        int kidsCount=Integer.parseInt(kidsReader.readLine());
        List<Kid> kidsList = new ArrayList<>();

        Fork leftFork;
        final Fork firstFork=new Fork();
        Fork rightFork=firstFork;
        Kid kid = null;
        while(kidsCount-- > 0){
            final String line = kidsReader.readLine();
            final int spaceIndex = line.indexOf(" ");
            final String name = line.substring(0, spaceIndex);
            final int time = Integer.parseInt(line.substring(spaceIndex+1));
            leftFork=rightFork;
            rightFork=new Fork();
            kid=new Kid(name, time, leftFork, rightFork);
            kidsList.add(kid);
        }
        kid.setRightFork(firstFork);
        kidsReader.close();
        feedKids(kidsList);
    }

    private static void feedKids(List<Kid> kidList){
        final int feeingNumber = kidList.size() / 2;
        int firstFeeded = 0;
        while (true){
            for(int i = 0; i < feeingNumber;i++) {
                kidList.get((2 * i + firstFeeded) % kidList.size()).feedChild();
            }
            /*try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            firstFeeded=(firstFeeded+1)%kidList.size();
        }
    }

    private static void init() throws IOException {
        Files.deleteIfExists(Path.of("out.txt"));
        System.setErr(new PrintStream(new FileOutputStream("out.txt")));
        new Thread(Kindergarten::runKindergarden).start();
    }

    private static void runKindergarden() {
        try {
            Thread.sleep(10100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            List<String> errLines = Files.readAllLines(Path.of("out.txt"));
            System.out.println("Children cries count: " + errLines.size());
            errLines.forEach(System.out::println);
            System.exit(errLines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
