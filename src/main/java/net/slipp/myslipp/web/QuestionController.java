package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.Question;
import net.slipp.myslipp.domain.QuestionRepository;
import net.slipp.myslipp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String form(HttpSession session, Model model){
        if (!HttpSessionUtils.isLoginUser(session)){
            return "/users/loginForm";
        }

        model.addAttribute("user", session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));

        return "/qna/form";
    }

    @PostMapping("")
    public String create(String writer, String title, String contents, HttpSession session){
        //로그인이 되었을 때 글쓰기가 될 수 있도록 한다.
        if (!HttpSessionUtils.isLoginUser((session))){
            return "users/loginForm";
        }

        User session_user = HttpSessionUtils.getUserFromSession(session);
        Question newQuestion = new Question(writer, title, contents);
        questionRepository.save(newQuestion);

        return "redirect:/";
    }
}
