package com.wda.wcdnweb.file;
import com.wda.wcdn.core.FileEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

@Slf4j
@Component
public class UdpFileClient {
	private static final String DEF_PATH = "D:/data/image";
	private static final String key = "key1";
	DatagramSocket dsock;
	DatagramPacket sPack, rPack;
	InetAddress server;
	int port = 9998;
	String srcPath, destPath;
	FileEvent event;

	public void setServerInfo(String ip, int port, String srcPath) throws UnknownHostException, SocketException {
		server = InetAddress.getByName(ip);
		this.port = port;
		this.dsock = new DatagramSocket();
		this.srcPath = srcPath;
		this.destPath = DEF_PATH + File.separator + key + File.separator;
	}

	public UdpFileClient() {
			log.info("connecting to server...");
	}

	public void sendFile(){

		try{
			byte[] inputData = new byte[1024 * 64];
			event = getFileEvent();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);

			os.writeObject(event);

			byte[] data = outputStream.toByteArray();

			sPack = new DatagramPacket(data, data.length, server, port);
			dsock.send(sPack);
			rPack = new DatagramPacket(inputData, inputData.length);
			dsock.receive(rPack);
			log.info(new String(rPack.getData()));

//			Thread.sleep(2000);
//			log.info("UDP client를 종료합니다.");
//			System.exit(0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileEvent getFileEvent() {

		FileEvent fileEvent = new FileEvent();

		String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1, srcPath.length());
		//String path = srcPath.substring(0, srcPath.lastIndexOf("/") + 1);
		fileEvent.setDestDir(destPath);
		fileEvent.setFilename(fileName);
		fileEvent.setSrcDir(srcPath);

		File file = new File(srcPath);

		if (file.isFile()) {
			try {
				DataInputStream diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];

				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}

				fileEvent.setFileSize(len);
				fileEvent.setFileData(fileBytes);
				fileEvent.setStatus("Success");
				diStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				fileEvent.setStatus("Error");
			}
		} else {
			log.info("path is not pointing to a file");
			fileEvent.setStatus("Error");
			log.info("UDP client를 종료합니다.");
			System.exit(0);
		}
		return fileEvent;
	}

//	public static void main(String[] args) {
//		UdpClient_file client = new UdpClient_file("127.0.0.1", 9999, "D:/data/image/sign.jpg");
//		client.createConnection();
//		client = new UdpClient_file("127.0.0.1", 9999, "D:/data/image/sign.jpg");
//	}
}
