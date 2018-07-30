package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/loginController")
public class LoginController {

    @RequestMapping("")
    public String login(HttpSession httpSession){
        System.out.println("here!!!");
        httpSession.setAttribute("login",true);
        return "a.html";
    }
}
