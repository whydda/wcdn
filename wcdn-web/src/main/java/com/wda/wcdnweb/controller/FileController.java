package com.wda.wcdnweb.controller;

import com.wda.wcdnweb.file.UdpFileClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: whydda
 * Date: 2019-11-01
 * Time: 오후 12:14
 */
@RestController
public class FileController {

    UdpFileClient udpFileClient;

    public FileController(UdpFileClient udpFileClient){
        this.udpFileClient = udpFileClient;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/file/upload")
    public String FileUploadService() throws SocketException, UnknownHostException {
        udpFileClient.setServerInfo("127.0.0.1", 9999, "D:/data/image/sign.jpg");
        udpFileClient.sendFile(); //파일전송
        return "/index";
    }
}
