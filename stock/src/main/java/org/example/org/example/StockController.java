package org.example.org.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @RequestMapping("/stockGeneralCall")
    public String stockGeneralCall(){
        return "这是一个普通stock接口调用！";
    }
}
