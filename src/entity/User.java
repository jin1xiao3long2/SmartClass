package entity;

public class User {

    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


//serverFile 教室url -> imgname
//localFile users(教室url -> 教室name), (教室name->imgname)
//教室name->byte[]

//(教室name->imgname) ->  (教室name->img)
//(imgname) -> (imagename->image)