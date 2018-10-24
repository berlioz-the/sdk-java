package sample1.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    String hello() {
        return "Hello World!!!";
    }

    @RequestMapping(value = "/greet",
                    method = RequestMethod.POST)
    public String greeting(@RequestParam(value = "name") String name) {
        return "Hello " + name;
    }
}
