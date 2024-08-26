package kr.co.lionkorea.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/members/{roleName}")
    public String members(@PathVariable(value = "roleName") String roleName){
        return "members";
    }

    @GetMapping("/members/{roleName}/{memberId}")
    public String memberDetail(@PathVariable("memberId") Long memberId, @PathVariable("roleName") String roleName){
        return "memberDetail";
    }

    @GetMapping("/project")
    public String project(){
        return "project";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }


}
