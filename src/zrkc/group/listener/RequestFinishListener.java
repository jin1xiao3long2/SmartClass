package zrkc.group.listener;

public interface RequestFinishListener {
    void log(String response);

    void error(Exception ex);
}
