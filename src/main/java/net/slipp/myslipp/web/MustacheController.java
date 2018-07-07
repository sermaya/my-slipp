package net.slipp.myslipp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/mustache_test")
public class MustacheController {
    @GetMapping("mustache_template1")
    public String mustacheTemplate1(Model model){

        model.addAttribute("name", "junelee");
        model.addAttribute("company", "<b>GitHub</b>");

        return "mustache_template1";
    }

    @GetMapping("mustache_template2")
    public String mustacheTemplate2(Model model){
        //List<RepoModel> repo = new ArrayList<RepoModel>();
        List<RepoModel> repo = Arrays.asList(new RepoModel("junelee"), new RepoModel("gildong"));
        model.addAttribute("repo", repo);
        return "mustache_template2";
    }
}
