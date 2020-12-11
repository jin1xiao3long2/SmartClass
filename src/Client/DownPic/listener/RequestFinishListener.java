package Client.DownPic.listener;

public interface RequestFinishListener {
    void finish(String response);

    void error(Exception ex);
}
