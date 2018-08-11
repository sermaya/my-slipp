package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/questions")
public class QuestionController{

    @Autowired
    private AnswerRepository answerRepository;

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
        Question question = questionRepository.getOne(id);
        Result result = valid(session, question);
        if(!result.isValid()){
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        model.addAttribute("question", question);
        return "qna/updateForm";
    }

    private Result valid(HttpSession session, Question question){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return Result.fail("로그인이 필요합니다.");
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if (!question.isSameWriter(loginUser)){
            return Result.fail("자신이 쓴 글만 수정, 삭제가 가능합니다.");
        }

        return Result.ok();
    }

    private boolean hasPermission(HttpSession session, Question question){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if (!question.isSameWriter(loginUser)){
            throw new IllegalStateException("자신이 쓴 글만 수정, 삭제가 가능합니다.");
        }

        return true;
    }

    @PutMapping("{id}")
    public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session){
        Question question = questionRepository.getOne(id);
        Result result = valid(session, question);
        if(!result.isValid()){
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        question.update(title, contents);
        questionRepository.save(question);
        return String.format("redirect:/questions/%d", id);

        /*
        try {
            Question question = questionRepository.getOne(id);
            hasPermission(session, question);
            question.update(title, contents);
            questionRepository.save(question);
            return String.format("redirect:/questions/%d", id);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/login";
        }
        */
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model, HttpSession session){
        Question question = questionRepository.getOne(id);
        Result result = valid(session, question);
        if(!result.isValid()){
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        questionRepository.deleteById(id);
        return "redirect:/";

        /*
        try {
            Question question = questionRepository.getOne(id);
            hasPermission(session, question);
            questionRepository.deleteById(id);
            return "redirect:/";
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/login";
        }
        */
    }
}
