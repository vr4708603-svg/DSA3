package src;

/**
 * Task.java
 * Represents a single task in the project.
 * Stores task details and CPM timing values (used in scheduling).
 */
public class Task {

    // ---- Basic Task Fields ----
    private String taskId;
    private String taskName;
    private int duration; // duration in days

    // ---- CPM (Critical Path Method) Timing Fields ----
    private int earliestStart;   // EST
    private int earliestFinish;  // EFT = EST + duration
    private int latestStart;     // LST = LFT - duration
    private int latestFinish;    // LFT
    private int slack;           // Slack = LST - EST
    private boolean critical;    // true if slack == 0

    // ---- Constructor ----
    public Task(String taskId, String taskName, int duration) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.duration = duration;

        // Initialise timing fields to -1 (not yet calculated)
        this.earliestStart = 0;
        this.earliestFinish = 0;
        this.latestStart = 0;
        this.latestFinish = 0;
        this.slack = 0;
        this.critical = false;
    }

    // ---- Getters ----
    public String getTaskId()      { return taskId; }
    public String getTaskName()    { return taskName; }
    public int    getDuration()    { return duration; }

    public int  getEarliestStart()  { return earliestStart; }
    public int  getEarliestFinish() { return earliestFinish; }
    public int  getLatestStart()    { return latestStart; }
    public int  getLatestFinish()   { return latestFinish; }
    public int  getSlack()          { return slack; }
    public boolean isCritical()     { return critical; }

    // ---- Setters (used by Scheduler) ----
    public void setEarliestStart(int est)  { this.earliestStart  = est; }
    public void setEarliestFinish(int eft) { this.earliestFinish = eft; }
    public void setLatestStart(int lst)    { this.latestStart    = lst; }
    public void setLatestFinish(int lft)   { this.latestFinish   = lft; }
    public void setSlack(int slack)        { this.slack          = slack; }
    public void setCritical(boolean c)     { this.critical       = c; }

    @Override
    public String toString() {
        return String.format("Task[%s | %s | %d days]", taskId, taskName, duration);
    }
}
