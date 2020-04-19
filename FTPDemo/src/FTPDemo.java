import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

public class FTPDemo extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField_seven;
	private JTextField textField_Password;
	private JTextField textField_IP;
		
	
	// 初始化参数--------------------------------
	static String FTP = "127.0.0.1";
	static String username = "seven";
	static String password = "123456";
	
	static FTPFile[] file;
	static FTP_Passive ftp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FTPDemo frame = new FTPDemo();
		frame.setVisible(true);
				
	}

	/**
	 * Create the frame.
	 */
	public FTPDemo() {
		setTitle("FTPClient");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{72, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 15, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel_2 = new JLabel("IP：");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_IP = new JTextField();
		textField_IP.setText(FTP);
		GridBagConstraints gbc_textField_IP = new GridBagConstraints();
		gbc_textField_IP.insets = new Insets(0, 0, 5, 5);
		gbc_textField_IP.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_IP.gridx = 1;
		gbc_textField_IP.gridy = 0;
		panel.add(textField_IP, gbc_textField_IP);
		textField_IP.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("状态：");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 2;
		gbc_lblNewLabel_4.gridy = 0;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		JLabel lblNewLabel_loginState = new JLabel("未登录");
		GridBagConstraints gbc_lblNewLabel_loginState = new GridBagConstraints();
		gbc_lblNewLabel_loginState.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_loginState.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_loginState.gridx = 3;
		gbc_lblNewLabel_loginState.gridy = 0;
		panel.add(lblNewLabel_loginState, gbc_lblNewLabel_loginState);
		
		JLabel lblNewLabel_1 = new JLabel("UserName：");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_seven = new JTextField();
		textField_seven.setText(username);
		GridBagConstraints gbc_textField_seven = new GridBagConstraints();
		gbc_textField_seven.insets = new Insets(0, 0, 5, 5);
		gbc_textField_seven.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_seven.gridx = 1;
		gbc_textField_seven.gridy = 1;
		panel.add(textField_seven, gbc_textField_seven);
		textField_seven.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Password：");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		textField_Password = new JTextField();
		textField_Password.setText(password);
		GridBagConstraints gbc_textField_Password = new GridBagConstraints();
		gbc_textField_Password.insets = new Insets(0, 0, 5, 0);
		gbc_textField_Password.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_Password.gridx = 3;
		gbc_textField_Password.gridy = 1;
		panel.add(textField_Password, gbc_textField_Password);
		textField_Password.setColumns(10);
		
		JButton btnNewButton = new JButton("登录");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("登录==============");
				lblNewLabel_loginState.setText("未登录");
				try {					
					FTP = textField_IP.getText().trim();
					username = textField_seven.getText().trim();
					password = textField_Password.getText().trim();

					ftp = new FTP_Passive(FTP, username, password);
					lblNewLabel_loginState.setText(username+"已登录");
					JOptionPane.showMessageDialog(null, "成功登录！", "SUCCESS!",
							JOptionPane.NO_OPTION);	
					file = ftp.getAllFile();
					setTableInfo();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "用户名/密码错误或服务器无响应\n username：" + username, "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);	
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane();
		panel_1.add(splitPane);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{97, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 23, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JButton btnNewButton_update = new JButton("刷新");
		btnNewButton_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					file = ftp.getAllFile();
					setTableInfo();
				} catch (Exception e1) {
					e1.printStackTrace();
				}			
			}
		});
		GridBagConstraints gbc_btnNewButton_update = new GridBagConstraints();
		gbc_btnNewButton_update.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_update.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_update.gridx = 0;
		gbc_btnNewButton_update.gridy = 0;
		panel_2.add(btnNewButton_update, gbc_btnNewButton_update);
		
		JButton btnNewButton_upload = new JButton("上传");
		btnNewButton_upload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 上传点击按钮触发------------------------------------
				System.out.println("上传！！！！！");
				int result = 0;
				String path = null;
				JFileChooser fileChooser = new JFileChooser();
				FileSystemView fsv = FileSystemView.getFileSystemView();
				System.out.println(fsv.getHomeDirectory()); // 得到桌面路径
				fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
				fileChooser.setDialogTitle("请选择要上传的文件...");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				result = fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == result) {
					path = fileChooser.getSelectedFile().getPath();
					System.out.println("path: " + path);
					try {
						// 下载
						ftp.upload(path);
						System.out.println("文件上传成功");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_upload = new GridBagConstraints();
		gbc_btnNewButton_upload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_upload.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_upload.gridx = 0;
		gbc_btnNewButton_upload.gridy = 1;
		panel_2.add(btnNewButton_upload, gbc_btnNewButton_upload);
		
		JButton btnNewButton_download = new JButton("下载");
		btnNewButton_download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow()>-1) {
					String from_file_name=file[table.getSelectedRow()].getName();
					int result = 0;
					String path = null;
					JFileChooser fileChooser = new JFileChooser();
					FileSystemView fsv = FileSystemView.getFileSystemView();
					fsv.createFileObject(from_file_name);				  
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
					fileChooser.setDialogTitle("另存为:"); 
					result = fileChooser.showSaveDialog(null);
					if (JFileChooser.APPROVE_OPTION == result) {
						path=fileChooser.getSelectedFile().getPath()+"\\"; //加"\\"是为了防止在桌面的时候C:destop最后没有\ 
						System.out.println("path: "+path);
						System.out.println("from_file_name:"+from_file_name);
						try {
							ftp.download(from_file_name, path);
							System.out.println("下载成功! ");
							
				            } catch (Exception e1) {
				            	// TODO Auto-generated catch block
				            	e1.printStackTrace();
				            }
				        }				
				}
				else {
					JOptionPane.showMessageDialog(null, "请选择文件！", "ERROR_MESSAGE",	JOptionPane.ERROR_MESSAGE);	
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_download = new GridBagConstraints();
		gbc_btnNewButton_download.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_download.gridx = 0;
		gbc_btnNewButton_download.gridy = 2;
		panel_2.add(btnNewButton_download, gbc_btnNewButton_download);
		
		JPanel panel_3 = new JPanel();
		splitPane.setLeftComponent(panel_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 73, 400, 384);
		panel_3.add(scrollPane);		
		
		table = new JTable() {
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};		
		scrollPane.setViewportView(table);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setToolTipText("可以点击下载");	
	}	

	// 显示基本信息-----------------------------------------------
	private void setTableInfo() {
		// table数据初始化 从FTP读取所有文件
		String[][] data1 = new String[file.length][3];
		for (int row = 0; row < file.length; row++) {

			data1[row][0] = file[row].getName();
			if (file[row].isDirectory()) {
				data1[row][1] = "文件夹";
			} else if (file[row].isFile()) {
				String[] geshi = file[row].getName().split("\\.");
				data1[row][1] = geshi[1];
			}
			data1[row][2] = file[row].getSize() + "";
		}
		// table列名-----------------------------------------------------
		String[] columnNames = { "文件", "文件类型", "文件大小(B)" };
		DefaultTableModel model = new DefaultTableModel();
		model.setDataVector(data1, columnNames);
		table.setModel(model);
	}
}
