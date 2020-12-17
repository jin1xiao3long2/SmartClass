package Client.ui;

import Client.DownPic.utils.BaseUtils.ImgBaseUtil;
import entity.Server;
import entity.ServerImg;
import entity.ShowImg;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PicArchiveListener  extends JFrame implements ActionListener{
	private List<ServerImg> imgs = new ArrayList<>();
	MainWindow mainWindow = null;
	JFrame frame = new JFrame("");
	JPanel panel = new JPanel();

	@Override
	public void repaint() {
		super.repaint();
		frame.getContentPane().removeAll();
		panel.removeAll();
		showInfo();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	void showInfo(){
		System.out.println("imgs size is " +imgs.size());

		frame.setPreferredSize(new Dimension(566,410));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel.setLayout(null);

		JButton uploadButton = new JButton();
		uploadButton.setText("上传图片");
		uploadButton.setBounds(37, 60, 60, 40);
		uploadButton.setMargin(new Insets(0,0,0,0));

		uploadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg,png","jpg");
				chooser.setFileFilter(filter);
				int returnval = chooser.showOpenDialog(uploadButton);
				if(returnval == JFileChooser.APPROVE_OPTION)
				{
					File[] arrfiles = chooser.getSelectedFiles();
					if(arrfiles == null || arrfiles.length == 0)
					{
						System.out.println("length=0 or cannot get file");
						return;
					}
					File ff = chooser.getSelectedFile();
					String fileName = ff.getName();
					String prefix = fileName.substring(fileName.lastIndexOf(".")+1);//extends name like jpg/png
					if(!(prefix.equals("jpg")||prefix.equals("png")))
					{
						JOptionPane.showMessageDialog(new JDialog(), "仅支持上传jpg与png图片");
						return;
					}
					String absolutePath = chooser.getSelectedFile().getAbsolutePath();
					ImageIcon imageicon = new ImageIcon(absolutePath);
					ServerImg img = new ServerImg();
					String url = mainWindow.mainApp.uploadPic(absolutePath);
					img.setUrl(url);
					try {
						img.setData(ImgBaseUtil.GetByteByImage(imageicon.getImage()));
					}catch (Exception ex){
						ex.printStackTrace();
					}
					imgs.add(img);
					JOptionPane.showMessageDialog(new JDialog(), "上传成功");
					repaint();

				}
			}
		});

		panel.add(uploadButton);

		if(imgs == null)
		{
			System.out.println("NULL IMGS");
			return;
		}
		else if(imgs.size() == 0)
		{
			System.out.println("0 IMG");
			return;
		}

		int col = (imgs.size() / 5) + 1;//����
		int row = 5;

		JLabel[] labelpics = new JLabel[imgs.size()];

		for(int i = 0; i < col; i++)
		{
			for(int j = 0; j < row; j++)
			{
				int index = j + i * 5;
				if(index >= imgs.size())
				{
					break;
				}


				if(imgs.get(index) == null)
				{

				}
				else
				{
					Image image = null;
					try {
						image = ImgBaseUtil.GetImageByByte(imgs.get(index).getData());
					}catch (Exception ex){
						ex.printStackTrace();
					}
					ImageIcon img = new ImageIcon(image);
					img.setImage(img.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
					labelpics[index] = new JLabel();
					labelpics[index].setBounds(37+j*118, 110+i*84, 80, 60);
					labelpics[index].setIcon(img);

					labelpics[index].addMouseListener(new MouseListener()
													  {

														  @Override
														  public void mouseClicked(MouseEvent arg0) {
															  int option = JOptionPane.showConfirmDialog(null, "确认要删除图片么", "删除图片", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
															  if(option == JOptionPane.YES_OPTION)
															  {
																  mainWindow.delPic(imgs.get(index).getUrl());
																  imgs.remove(index);
																  System.out.println("remove " + index + " now imgs size " + imgs.size());
																  repaint();
															  }
														  }

														  @Override
														  public void mouseEntered(MouseEvent arg0) {
														  }
														  @Override
														  public void mouseExited(MouseEvent arg0) {
														  }
														  @Override
														  public void mousePressed(MouseEvent arg0) {
														  }
														  @Override
														  public void mouseReleased(MouseEvent arg0) {
														  }
													  }
					);
					panel.add(labelpics[index]);
				}
			}
		}

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		imgs = mainWindow.mainApp.getImgs();
		showInfo();
	}

	
	public PicArchiveListener(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

}
