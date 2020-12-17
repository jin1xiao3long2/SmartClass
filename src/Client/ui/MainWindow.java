package Client.ui;

import java.awt.*;
//输入一个list<Image,ID>，输出图片，点击时获取图片id
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.Server;
import entity.ServerImg;
import entity.ShowImg;
import entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MainWindow extends JFrame{
	public JFrame frame = new JFrame("Login Example");//初始界面，为了便于跳转页面
	public List<ShowImg> showImgs = new ArrayList<>();
	public AdminwallpaperSetting mainApp = new AdminwallpaperSetting();
	public JScrollPane scrollpane = null;
	public JPanel panel = new JPanel();


	public void repaint() {
		super.repaint();

		frame.getContentPane().removeAll();
		panel.removeAll();
		placeComponentsHome(panel);
		JScrollPane scrollpane = new JScrollPane(panel);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane = new JScrollPane(panel);
		this.scrollpane = scrollpane;
		this.frame.getContentPane().add(this.scrollpane);
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	public void Init(AdminwallpaperSetting mainapp) {
		frame.setPreferredSize(new Dimension(566,410));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainApp = mainapp;

		if(mainApp.server_correct()){
			mainApp.initImgs();
			try {
				mainApp.initServerFile();
			}catch (Exception e){
				e.printStackTrace();
			}
			int i = 0;
			showImgs = mainApp.getList();
			for(ShowImg I : mainApp.getList()){
				i++;
				System.out.println(I.getName() + i);
			}
			placeComponentsHome(panel);
		}else{
			placeComponentsHome(panel);
			int option = JOptionPane.showConfirmDialog(null, "P1 win, continue?", "Tips", JOptionPane.YES_NO_OPTION);
			if(option == JOptionPane.NO_OPTION)
			{
				System.exit(0);
			}
			else if(option == JOptionPane.YES_OPTION)
			{
				JButton setButton = new JButton("设置");
				setButton.setVisible(true);
				setButton.addActionListener(new SwitchToOptionListener("设置", this));
				setButton.doClick();

			}
		}

		panel.setPreferredSize(new Dimension(640,480));
		JScrollPane scrollpane = new JScrollPane(panel);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollpane = scrollpane;
		frame.getContentPane().add(this.scrollpane);
//		frame.add(scrollpane);
		frame.pack();
		frame.setVisible(true);
	}


	public static class ChangePageListener implements ActionListener//点击返回键的操作
	{
		private List<ShowImg> imgs = new ArrayList<ShowImg>();
		private JFrame frame;
		private List<Image> listImgs = new ArrayList<>();

		public List<ShowImg> getImgs() {
			return imgs;
		}

		public void setImgs(List<ShowImg> imgs) {
			this.imgs = imgs;
		}



		public JFrame getFrame() {
			return frame;
		}

		public void setFrame(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.frame.dispose();
		}
	}


	public void placeComponentsHome(JPanel panel)//在页面中放置组件
	{//base on list get paint the panel
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(223, 74, 126, 24);
		lblNewLabel.setText("各教室状况");
		panel.add(lblNewLabel);

		JButton addNewUserButton = new JButton("");
		addNewUserButton.setText("增加用户");
		addNewUserButton.setBounds(400,74,60,20);
		addNewUserButton.setMargin(new Insets(0,0,0,0));

		JButton option = new JButton();
		option.setBounds(300, 27, 34, 28);
		option.setText("设置");
		option.setMargin(new Insets(0,0,0,0));
		option.addActionListener(new SwitchToOptionListener(option.getText(), this));
		panel.add(option);

		JButton PicArchiveButton = new JButton();
		PicArchiveButton.setText("图片库");
		PicArchiveButton.setBounds(470,74,60,20);
		PicArchiveButton.setMargin(new Insets(0,0,0,0));
		PicArchiveButton.addActionListener(new PicArchiveListener(this));
		panel.add(PicArchiveButton);

		addNewUserButton.addActionListener(new AddUserListener(this));
		panel.add(addNewUserButton);



		if(showImgs == null)
		{
			System.out.println("NULL IMGS");
			return;
		}
		if(showImgs.size() == 0)
		{

		}
		else
		{
			int col = (showImgs.size() / 5) + 1;//行数
			int row = 5;//5列

			JLabel[] labelpics = new JLabel[showImgs.size()];
			JLabel[] labels = new JLabel[showImgs.size()];

			for(int i = 0; i < col; i++)
			{
				for(int j = 0; j < row; j++)
				{
					int index = j + i * 5;
					if(index >= showImgs.size())
					{
						break;
					}


					if(showImgs.get(index).getImg() == null)
					{

					}
					else
					{
						ImageIcon img = new ImageIcon(showImgs.get(index).getImg());//获取图像
						img.setImage(img.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
						labelpics[index] = new JLabel();
						labelpics[index].setBounds(37+j*118, 110+i*84, 80, 60);
						labelpics[index].setIcon(img);
						labelpics[index].addMouseListener(new LabelMouseListener(showImgs.get(index),labelpics[index],frame, this));
						panel.add(labelpics[index]);
					}

					labels[index] = new JLabel();
					labels[index].setBounds(37+j*118, 175+i*84, 90, 24);
					labels[index].setText(showImgs.get(index).getName());
					panel.add(labels[index]);
				}
			}
		}

	}

	public void addUser(User user, ServerImg img){
		mainApp.addUser(user, img.getUrl());
		showImgs = mainApp.getList();
//		frame.validate();
	}

	public void delUser(String username){
		List<User> users = mainApp.getUsers();
		User deluser = new User();
		for(User user: users){
			if(user.getName().equals((String)username)){
				deluser = user;
			}
		}
		mainApp.delUser(deluser);
		showImgs = mainApp.getList();
	}

	public void changePaper(String username, ServerImg img){
		List<User> users = mainApp.getUsers();
		User chuser = new User();
		for(User user: users){
			if(user.getName().equals((String)username)){
				chuser = user;
			}
		}
		mainApp.changeWallpaper(chuser, img);
		showImgs = mainApp.getList();
	}

	public String uploadPic(String path){
		return mainApp.uploadPic(path);
	}

	public void delPic(String imgName){
		mainApp.delPic(imgName);
	}

	public void setDefaultPic(String imgName){
		mainApp.setDefaultImg(imgName);
	}



}	
