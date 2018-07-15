package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.User;
import net.slipp.myslipp.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private List<User> users = new ArrayList<User>();

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session){
        User user = userRepository.findByUserId(userId);
        if (user == null){
            System.out.println("Login Failed");
            return "redirect:/users/loginForm";
        }

        //if (!password.equals(user.getPassword())) {
        if (!user.matchPassword(password)) {
            System.out.println("Login Failed");
            return "redirect:/users/loginForm";
        }

        System.out.println("Login Success!");
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        //세션을 제거한다.
        session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
        System.out.println("logout success");
        return "redirect:/";
    }

    @GetMapping("/form")
    public String form(){
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user){
        System.out.println("user : " + user);
        users.add(user);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session){
        Object tempUser = session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }

        User session_user = HttpSessionUtils.getUserFromSession(session); //(User)tempUser;
        //if(!id.equals(session_user.getId())){
        if(!session_user.matchId(id)){
            throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
        }

        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session){
        Object tempUser = session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
        if (tempUser == null) {
            return "redirect:/users/loginForm";
        }

        User session_user = HttpSessionUtils.getUserFromSession(session);
        //if(!id.equals(session_user.getId())){
        if(!session_user.matchId(id)){
            throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
        }

        User user = userRepository.getOne(id);
        user.update(updatedUser);
        userRepository.save(user);
        return "redirect:/users";
    }
}
