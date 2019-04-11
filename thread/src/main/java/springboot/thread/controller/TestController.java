package springboot.thread.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.thread.service.async.AsyncService;

@RestController
@RequestMapping("/demo")
public class TestController {

    @Autowired
    private AsyncService asyncService;

//    @ResponseBody
    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public String test(@PathVariable Integer id){
        System.out.println("start");
        asyncService.executorAsyncTask(id);
        System.out.println("end");
        return "success";
    }
}
