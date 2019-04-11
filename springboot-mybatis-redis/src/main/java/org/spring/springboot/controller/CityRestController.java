package org.spring.springboot.controller;

import org.json.JSONObject;
import org.spring.springboot.entity.user.City;
import org.spring.springboot.service.intf.user.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;


    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.GET)
    public City findOneCity(@PathVariable("id") Long id) {
        return cityService.findCityById(id);
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.POST)
    public void createCity(@RequestBody City city) {
        cityService.saveCity(city);
    }

    @RequestMapping(value = "/api/city", method = RequestMethod.PUT)
    public void modifyCity(@RequestBody City city) {
        cityService.updateCity(city);
    }

    @RequestMapping(value = "/api/city/{id}", method = RequestMethod.DELETE)
    public void modifyCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
    }

    @RequestMapping(value = "/api/test/{id}",method = RequestMethod.GET)
    public String test(@PathVariable("id") int id) {
        System.out.println("==========="+id);
        try {
            JSONObject jsonObject = new JSONObject(){{
                put("status","ok");
                put("name","wkl");
            }};
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/api/addcity", method = RequestMethod.POST)
    public String addCity(@RequestBody City city) throws Exception{
        System.out.println(city.getCityName());
        cityService.saveCity(city);
        return  new JSONObject(){{put("isok","yes");}}.toString();
    }
}
