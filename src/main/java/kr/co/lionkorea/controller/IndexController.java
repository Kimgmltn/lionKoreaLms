package kr.co.lionkorea.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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

    @GetMapping("/members")
    public String members(){
        return "members";
    }

    @GetMapping("/members/{memberId}")
    public String memberDetail(@PathVariable("memberId") Long memberId){
        return "memberDetail";
    }

    @GetMapping("/members/register")
    public String memberSaveForm(){
        return "memberRegister";
    }
    @GetMapping("/project")
    public String project(){
        return "project";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/company")
    public String company(){
        return "company";
    }


}
