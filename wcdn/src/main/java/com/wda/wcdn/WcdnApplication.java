package com.wda.wcdn;

import com.wda.wcdn.file.UdpServer_file;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WcdnApplication {

	public static void main(String[] args) {
		SpringApplication.run(WcdnApplication.class, args);

		UdpServer_file server = new UdpServer_file(9999);
//		UdpServer_file server2 = new UdpServer_file(9999);
		server.createAndListenSocket();
	}

}
