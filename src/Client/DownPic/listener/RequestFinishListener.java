package Client.DownPic.listener;

public interface RequestFinishListener {
    void log(String response);

    void error(Exception ex);
}
