// public class OneThreadR extends ThreadHandler implements Runnable {
//     @Override
//     public void run() {

//         try {
//             while (!done) {
//                 Integer currJob = this.jobs.take();
//                 process(currJob);
//             }
//         } catch (CannotProcessJobException cpe) {
//             // cpe.printStackTrace();
//             System.out.println("thread: " + this.tCount + " cannot process job: " + cpe.getUnfinishedJob());
//             this.jobs.add(cpe.getUnfinishedJob());
//         } catch (InterruptedException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }

//     public OneThread(LinkedBlockingDeque<Integer> jobs, Integer toBeFinshed, Integer jobFinshed, boolean done,
//             Integer tCount) {
//         this.jobs = jobs;
//         this.tCount = tCount;
//         this.done = done;
//         this.toBeFinshed = toBeFinshed;
//         this.jobFinshed = jobFinshed;
//     }

//     public void process(Integer job) throws InterruptedException, CannotProcessJobException {
//         Random r = new Random();
//         int sleepDuration = r.nextInt(2000);
//         Thread.sleep(sleepDuration);
//         if (job == 3 && (job % this.tCount == 0)) {
//             throw new CannotProcessJobException(job);
//         }

//         Thread.sleep(2000);
//         System.out.println(
//                 "Thread " + this.tCount + ": process job number: " + job + " and get result: " + job * 3.14);
//         this.jobFinshed++;
//         System.out.println("So far finished: " + this.jobFinshed);
//         if (this.jobFinshed == this.toBeFinshed) {
//             done = true;
//             System.out.println("DONE~~~~~~~~~~~");
//         }

//     }
// }
