public class CannotProcessJobException extends Exception {
    Integer job;

    public CannotProcessJobException(Integer job) {
        this.job = job;
    }

    public Integer getUnfinishedJob() {
        return this.job;
    }

    public String getMessage() {
        return "cannot process this job";
    }
}
