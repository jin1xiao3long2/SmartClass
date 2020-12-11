package Client.DownPic.listener;

public interface DownloadFinishListener {
    void finish();

    void error(Exception ex);
}
