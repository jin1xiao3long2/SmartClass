package zrkc.group.new_ui.component;

public class myData {

    private String info = null;
    private int num = 0;

    public myData(String Info, int Num){
        this.info = Info;
        this.num = Num;
    }

    public String getInfo() {
        return info;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "myData{" +
                "info='" + info + '\'' +
                ", num=" + num +
                '}';
    }
}
