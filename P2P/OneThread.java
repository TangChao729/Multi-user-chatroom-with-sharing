import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class OneThread extends Thread {

    LinkedBlockingDeque<Integer> jobs;
    Integer tCount;
    ArrayList<Integer> done;
    // Integer toBeFinshed;
    // Integer jobFinshed;
    boolean stop;

    @Override
    public void run(){

        try {
            while (!isInterrupted()) {
                Integer currJob = this.jobs.take();
                process(currJob);
            }

        } catch (CannotProcessJobException cpe) {
            // cpe.printStackTrace();
            System.out.println("thread: " + this.tCount + " cannot process job: " + cpe.getUnfinishedJob());
            this.jobs.add(cpe.getUnfinishedJob());
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }

    public OneThread(LinkedBlockingDeque<Integer> jobs, ArrayList<Integer> done,
            Integer tCount) {
        this.jobs = jobs;
        this.tCount = tCount;
        this.done = done;
        this.stop = false;
        // this.toBeFinshed = toBeFinshed;
        // this.jobFinshed = jobFinshed;
    }

    public void process(Integer job) throws InterruptedException, CannotProcessJobException {
        Random r = new Random();
        int sleepDuration = r.nextInt(2000);
        Thread.sleep(sleepDuration);
        if (job == 3 && (job % this.tCount == 0)) {
            throw new CannotProcessJobException(job);
        }

        Thread.sleep(2000);
        System.out.println(
                "Thread " + this.tCount + ": process job number: " + job + " and get result: " + job * 3.14);
        this.done.add(1);
        // this.jobFinshed++;
        // System.out.println("So far finished: " + this.jobFinshed);

    }
}
