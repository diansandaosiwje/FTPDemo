import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class FTP_Passive {
	//命令通道
	Socket controlSocket;
	private BufferedReader controlReader;
	private PrintWriter controlWriter;
	private FileInputStream is;
	//数据通道
	Socket dataSocket; 	
	private String passHost; 
	private int passPort;

	private String ftpusername;
	private String ftppassword;


	private static final int PORT = 21;

	public FTP_Passive(String url, String username, String password) throws Exception {
		try {
			setUsername(username);
			setPassword(password);
			
			controlSocket = new Socket(url, PORT);	
			controlReader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
			controlWriter = new PrintWriter(new OutputStreamWriter(controlSocket.getOutputStream()), true);

			logInSever(); // 登录到ftp服务器				
		} catch (Exception e) {
			throw e;
		}
	}
	
	// 将服务器设置为passive模式 并设置数据IP和端口
	private void setPassiveMode() throws Exception {
		String response;
		controlWriter.println("PASV mode");
		response = controlReader.readLine();
		System.out.println(response);
		if (!response.startsWith("227 ")) {
			throw new IOException("FTPClient could not request passive mode: " + response);
		}
		//Example:227 Entering Passive Mode (127,0,0,1,206,85).
		int opening = response.indexOf('(');
		int closing = response.indexOf(')', opening + 1);
		if (closing > 0) {
			String dataLink = response.substring(opening + 1, closing);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				passHost = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken();
				passPort = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
				throw new IOException("FTPClient received bad data link information: " + response);
			}		
		}					
	}
		
	private void setUsername(String username) {
		this.ftpusername = username;
	}

	private void setPassword(String password) {
		this.ftppassword = password;
	}

	//使用指定IP账户密码登陆
	public void logInSever() throws Exception {
		String msg;
		do {
			msg = controlReader.readLine();
			System.out.println(msg);
		} while (!msg.startsWith("220 "));

		controlWriter.println("USER " + ftpusername);

		String response = controlReader.readLine();
		System.out.println(response);

		if (!response.startsWith("331 ")) {
			throw new IOException("SimpleFTP received an unknown response after sending the user: " + response);
		}

		controlWriter.println("PASS " + ftppassword);

		response = controlReader.readLine();
		System.out.println(response);
		if ((!response.startsWith("230 ")) || response.equals("230 User Anonymous logged in.")) {
			throw new IOException("SimpleFTP was unable to log in with the supplied password: " + response);		
		}
	}
	// 获取所有文件和文件夹的名字
	public FTPFile[] getAllFile() throws Exception {
		String response;

		setPassiveMode();
		// Send LIST command
		controlWriter.println("LIST");
		//Open data connection
		dataSocket = new Socket(passHost, passPort);
		
		// Read command response
		response = controlReader.readLine();
		System.out.println(response);

		// Read data from server
		Vector<FTPFile> tempfiles = new Vector<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			FTPFile temp = new FTPFile();
			setFtpFileInfo(temp, line);
			tempfiles.add(temp);
		}

		reader.close();
		// Read command response
		response = controlReader.readLine();
		System.out.println(response);

		FTPFile[] files = new FTPFile[tempfiles.size()];
		tempfiles.copyInto(files);// 将vector数据存到数组里

		return files;

	}

	// 通过字符串解析构造一个FTPfile对象
	private void setFtpFileInfo(FTPFile in, String info) {
		String infos[] = info.split(" ");
		Vector<String> vinfos = new Vector<>();
		for (int i = 0; i < infos.length; i++) {
			if (!infos[i].equals(""))
				vinfos.add(infos[i]);
		}
		in.setName(vinfos.get(8));
		in.setSize(Integer.parseInt(vinfos.get(4)));
		
		String type = info.substring(0, 1);
		if (type.equals("d")) {
			in.setType(1);// 设置为文件夹
		} else {
			in.setType(0);// 设置为文件
		}
	}

	// 生成InputStream用于上传本地文件
	public void upload(String File_path) throws Exception {
		System.out.print("File Path :" + File_path);
        //读取文件
		File f = new File(File_path);
		if (!f.exists()) {
			System.out.println("File not Exists...");
			return;
		}

		is = new FileInputStream(f);
		BufferedInputStream input = new BufferedInputStream(is);
		String response;	

		setPassiveMode();
		// Send command STOR
		controlWriter.println("STOR " + f.getName());
		//Open data connection
		dataSocket = new Socket(passHost, passPort);

		// Read command response
		response = controlReader.readLine();
		System.out.println(response);

		// Read data from server
		BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}

		output.flush();
		input.close();
		output.close();
		
		response = controlReader.readLine();
		System.out.println(response);

	}

	// 下载 from_file_name是下载的文件名,to_path是下载到的路径地址
	public void download(String from_file_name, String to_path) throws Exception {
		
		setPassiveMode();
		// Send RETR command
		controlWriter.println("RETR " + from_file_name);
		//Open data connection
		dataSocket = new Socket(passHost, passPort);
		
		// Read data from server
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(to_path, from_file_name)));
		BufferedInputStream input = new BufferedInputStream(dataSocket.getInputStream());
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}

		output.flush();
		output.close();
		input.close();

		String response;
		response = controlReader.readLine();
		System.out.println(response);

		response = controlReader.readLine();
		System.out.println(response);
	}

}