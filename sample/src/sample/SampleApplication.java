package sample;

import com.berlioz.Berlioz;

public class SampleApplication
{
    public static void main(String args[])
    {
        System.out.println("Hello World!!");

        Berlioz.run();

        Berlioz.service("web").all();

    }
}
