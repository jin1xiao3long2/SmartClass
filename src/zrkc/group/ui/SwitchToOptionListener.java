package zrkc.group.ui;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import zrkc.group.listener.RequestFinishListener;
import zrkc.group.utils.BaseUtils.LogBaseUtil;
import zrkc.group.javabean.Data;

public class SwitchToOptionListener implements ActionListener{
	public String ButtonName;
	public MainWindow mainWindow;
	private final int SUCCESS = 1;
	private final int FAILED = 0;

	public SwitchToOptionListener(String buttonName, MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		this.ButtonName = buttonName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//	System.out.println(ButtonName);

		JFrame frame = new JFrame();
		CreateOptionFrame(frame);
	}

	public void CreateOptionFrame(JFrame frame)
	{
		// Server(host, port, username, password, http_root)
		// user_root
		// namebase
		frame.setSize(580, 670);
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);


		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel ServerHostLabel = new JLabel();
		ServerHostLabel.setText("Server Host:");
		ServerHostLabel.setBounds(20,10,97,30);
		panel.add(ServerHostLabel);

		JTextField ServerHost = new JTextField();
		ServerHost.setText(mainWindow.mainApp.getServer().HOST);
		ServerHost.setBounds(97, 10, 196, 30);
		panel.add(ServerHost);


		JLabel ServerPortLabel = new JLabel();
		ServerPortLabel.setText("Server Port:");
		ServerPortLabel.setBounds(20,56,97,30);
		panel.add(ServerPortLabel);

		JTextField ServerPort = new JTextField();
		ServerPort.setText(mainWindow.mainApp.getServer().PORT);
		ServerPort.setBounds(97, 56, 196, 30);
		panel.add(ServerPort);


		JLabel ServerUsernameLabel = new JLabel();
		ServerUsernameLabel.setText("Username:");
		ServerUsernameLabel.setBounds(30, 102, 97, 30);
		panel.add(ServerUsernameLabel);

		JTextField ServerUsername = new JTextField();
		ServerUsername.setText(mainWindow.mainApp.getServer().USERNAME);
		ServerUsername.setBounds(97, 102, 196, 30);
		panel.add(ServerUsername);


		JLabel ServerPasswordLabel = new JLabel();
		ServerPasswordLabel.setText("Password:");
		ServerPasswordLabel.setBounds(30, 148, 97, 30);
		panel.add(ServerPasswordLabel);

		JTextField ServerPassword = new JTextField();
		ServerPassword.setText(mainWindow.mainApp.getServer().PASSWORD);
		ServerPassword.setBounds(97, 148, 196, 30);
		panel.add(ServerPassword);


		JLabel ServerHttpRootLabel = new JLabel();
		ServerHttpRootLabel.setText("Http_root:");
		ServerHttpRootLabel.setBounds(25, 194, 97, 30);
		panel.add(ServerHttpRootLabel);

		JTextField ServerHttpRoot = new JTextField();
		ServerHttpRoot.setText(mainWindow.mainApp.getServer().HTTP_ROOT);
		ServerHttpRoot.setBounds(97, 194, 196, 30);
		panel.add(ServerHttpRoot);


		JLabel UserRootLabel = new JLabel();
		UserRootLabel.setText("User_root:");
		UserRootLabel.setBounds(25, 242, 97, 30);
		panel.add(UserRootLabel);

		JTextField UserRoot = new JTextField();
		UserRoot.setText(mainWindow.mainApp.getUser_root());
		UserRoot.setBounds(97, 242, 196, 30);
		panel.add(UserRoot);


		JLabel NameBaseLabel = new JLabel();
		NameBaseLabel.setText("Namebase:");
		NameBaseLabel.setBounds(30, 290, 97, 30);
		panel.add(NameBaseLabel);

		JTextField NameBase = new JTextField();
		NameBase.setText(mainWindow.mainApp.getName_Base());
		NameBase.setBounds(97, 290, 196, 30);
		panel.add(NameBase);


		JButton Confirm = new JButton();
		Confirm.setText("确认");
		Confirm.setBounds(97, 338, 50, 30);
		Confirm.setMargin(new Insets(0,0,0,0));
		Confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Data originData = mainWindow.mainApp.getData();
				String server_host = ServerHost.getText();
				String server_port = ServerPort.getText();
				String server_username = ServerUsername.getText();
				String server_password = ServerPassword.getText();
				String server_http_root = ServerHttpRoot.getText();
				String user_root = UserRoot.getText();
				String namebase = NameBase.getText();
				Data data = new Data(server_host,server_port,server_username,server_password,server_http_root,user_root,namebase);
//				System.out.println(server_host);

					mainWindow.setData(data, new RequestFinishListener() {
						@Override
						public void log(String response) {
							LogBaseUtil.saveLog(SUCCESS, response);
						}

						@Override
						public void error(Exception ex) {
							MainWindow.showWarning(ex.getMessage());
						}
					});

				frame.dispose();

				mainWindow.repaint();
			}
		});
		panel.add(Confirm);

		panel.setPreferredSize(new Dimension(480,370));
		JScrollPane scrollpane = new JScrollPane(panel);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		frame.add(scrollpane);
		frame.pack();
		frame.setVisible(true);
	}
}	
