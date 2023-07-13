import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        LinkedBlockingDeque<Integer> jobs = new LinkedBlockingDeque<>();
        jobs.add(1);
        jobs.add(2);
        jobs.add(3);
        jobs.add(4);
        jobs.add(5);
        jobs.add(6);
        jobs.add(7);
        jobs.add(8);
        jobs.add(9);

        ArrayList<Integer> peers = new ArrayList<>();
        peers.add(1);
        peers.add(2);

        ArrayList<Integer> done = new ArrayList<>();

        boolean finishedDownload = false;

        ThreadHandler threadHandler = new ThreadHandler(jobs, done, peers, finishedDownload);
        threadHandler.start();

        while (true) {
            System.out.println("main thread is running, jobs remaining: " + jobs.size() + " finished? " + done);
            if (done.size() == 3) {
                System.out.println("Finished download");
                System.out.println("I am going to kill thread handler");
                threadHandler.killed = true;

            }
            Thread.sleep(1000);

        }

    }
}