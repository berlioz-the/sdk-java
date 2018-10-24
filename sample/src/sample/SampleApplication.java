package sample;

import com.berlioz.Berlioz;

import java.util.Scanner;

public class SampleApplication
{
    public static void main(String args[])
    {
        System.out.println("Hello World!!");

        Berlioz.run();

        Berlioz.service("web").all();

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
