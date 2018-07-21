package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.Question;
import net.slipp.myslipp.domain.QuestionRepository;
import net.slipp.myslipp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/questions")
public class QuestionController{

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
        Question newQuestion = new Question(session_user, title, contents);
        questionRepository.save(newQuestion);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model){
        Question question = questionRepository.getOne(id);
        model.addAttribute("question", question);

        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return "/users/loginForm";
        }

        //세션에서 사용자 정보를 조회
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.getOne(id);

        //로그인한 사용자가 글쓴 사람과 일치하는지 체크
        if (!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, String title, String contents, HttpSession session){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return "/users/loginForm";
        }

        //세션에서 사용자 정보를 조회
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.getOne(id);

        //로그인한 사용자가 글쓴 사람과 일치하는지 체크
        if (!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        question.update(title, contents);
        questionRepository.save(question);
        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return "/users/loginForm";
        }

        //세션에서 사용자 정보를 조회
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.getOne(id);

        //로그인한 사용자가 글쓴 사람과 일치하는지 체크
        if (!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        questionRepository.deleteById(id);
        return "redirect:/";
    }
}
