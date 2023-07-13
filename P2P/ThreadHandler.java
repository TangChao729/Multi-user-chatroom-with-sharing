import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadHandler extends Thread {

    public LinkedBlockingDeque<Integer> jobs;
    public ArrayList<Integer> peers;
    public Integer tCount;
    public ArrayList<Integer> done;
    public boolean allKilled;
    public boolean finishedDownload;
    public boolean killed;
    // public Integer toBeFinshed;
    // public Integer jobFinshed;

    public ThreadHandler(LinkedBlockingDeque<Integer> jobs, ArrayList<Integer> done, ArrayList<Integer> peers,
            boolean finishedDownload) {
        this.jobs = jobs;
        this.peers = peers;
        this.tCount = 1;
        this.done = done;
        this.allKilled = false;
        this.finishedDownload = finishedDownload;
        this.killed = false;
        // this.toBeFinshed = jobs.size();
        // this.jobFinshed = 0;
    }

    @Override
    public void run() {
        try {
            while (!killed) {
                process();
            }
        } catch (InterruptedException ie) {
            // ie.printStackTrace();
            System.out.println("ThreadHandler killed");
        }
    }

    /**
     * 
     */
    private void process() throws InterruptedException {
        ArrayList<OneThread> allThreads = new ArrayList<>();
        try {
            for (int i = 1; i <= this.peers.size(); i++) {
                System.out.println("Creating thread no: " + i);
                OneThread oneThread = new OneThread(jobs, this.done, this.tCount);
                this.tCount++;
                allThreads.add(oneThread);
                oneThread.start();
            }

            while (true) {
                // Integer threadsAlive = 0;
                // for (OneThread oneThread : allThreads) {
                // if (oneThread.isDaemon()) {
                // threadsAlive = threadsAlive + 1;
                // }
                // }
                System.out.println("Thread hanlder is running, job finished: " +
                        this.done.size());
                if (this.done.size() == 9 && this.allKilled == false) {
                    for (OneThread oneThread : allThreads) {
                        oneThread.interrupt();
                    }
                    this.allKilled = true;
                    this.finishedDownload = true;
                    System.out.println("All thread killed");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
