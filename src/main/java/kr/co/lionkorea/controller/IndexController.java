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

    @GetMapping("/projects/admin")
    public String projectAdmin(){
        return "project-admin";
    }

    @GetMapping("/projects/translator")
    public String projectTranslator(){
        return "project-translator";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/company/domestic")
    public String domesticCompany(){
        return "company-domestic";
    }

    @GetMapping("/company/buyer")
    public String buyer(){
        return "company-buyer";
    }

    @GetMapping("/company/register")
    public String companySaveForm(){
        return "companyRegister";
    }

    @GetMapping("/company/domestic/{companyId}")
    public String domesticCompanyDetail(@PathVariable Long companyId) {
        return "companyDetail-domestic";
    }

    @GetMapping("/company/buyer/{companyId}")
    public String buyerDetail(@PathVariable Long companyId) {
        return "companyDetail-buyer";
    }

    @GetMapping("/password/{shortUrl}")
    public String changePassword(@PathVariable String shortUrl) {
        return "password";
    }


}
