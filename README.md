# 现接口 (in mainWindow.java)

注: RequestFinishListener,用于显示正常结果与异常结果后的处理(如日志loBaseUtil.saveLog()或恢复), 用法如下:
```java
 mainWindow.changePaper(username, selectedImage, new RequestFinishListener() {
                    @Override
                    public void log(String response) {
                        MainWindow.showMessage(response);
                        LogBaseUtil.saveLog(SUCCESS, response);
                    }

                    @Override
                    public void error(Exception ex) {
                        MainWindow.showWarning(ex.getMessage());
                        LogBaseUtil.saveLog(FAILED, ex.getMessage());
                    }
                });
```
大部分可以直接用展示警告框即可.
+ addUser(String, ServerImg, RequestFinishListener) void, 添加用户
+ delUser(String, RequestFinishListener) void 删除用户
+ changePaper(String, ServerImg, RequestFinishListener) void 修改壁纸
+ uploadPic(String, RequestFinishListener) void 上传图片
+ delPic(String, RequestFinishListener) void 
+ setDefaultPic(String, RequestFinishListener) void 设置默认图片
+ has_defaultImg() boolean 检测是否有默认图片,需要时使用(如删除默认图片后的操作)  
+ getImgs() List<ServerImg> 返回ServerImg列表
+ setDefaultImg(String, RequestFinishListener) void
+ setData(Data, RequestFinishListener) void 

注: 图片有两种储存形式,一种是byte[]储存(ServerImg), 一种是Image储存(ShowImg), 为减少传输负担,都用的ServerImg获得数据,并在合适的时候利用ImgUtil.getImgbyByte转换为Image()(这个没做好)
以上直接(赋值)使用即可

+ init(AdminwallpaperSetting, Admin) void 参考格式,初始化Admin与AdminwallpaperSetting相关的初始化即可
+ updateData() void 读取服务器/本地数据: 初始化时候使用, 更新设置时使用, 刷新时使用(如之前没连网,现在连上后使用刷新)
