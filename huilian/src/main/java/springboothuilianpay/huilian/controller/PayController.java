package springboothuilianpay.huilian.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboothuilianpay.huilian.vo.QueryBindCard;

@RestController
@RequestMapping("/huilian")
public class PayController {

    @ApiOperation(value = "test",notes = "test")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        return "success";
    }

    @RequestMapping(value = "/queryBindCard",method = RequestMethod.POST)
    public Object queryBindCard(@RequestBody QueryBindCard queryBindCard){

        return null;
    }
}
