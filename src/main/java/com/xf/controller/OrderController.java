package com.xf.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.xf.server.NoticeWebsocket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
	@GetMapping("/test")
    public String test() {
    	NoticeWebsocket.sendMessage("你好，WebSocket");
        return "Ok";
    }
}