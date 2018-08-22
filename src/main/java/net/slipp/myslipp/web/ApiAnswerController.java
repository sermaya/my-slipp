package net.slipp.myslipp.web;

import net.slipp.myslipp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("")
    public Answer create(@PathVariable Long questionId, String contents, HttpSession session){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return null;
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.getOne(questionId);
        Answer answer = new Answer(loginUser, question, contents);
        question.addAnswer();
        return answerRepository.save(answer);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session){
        //로그인 여부 체크
        if (!HttpSessionUtils.isLoginUser(session)){
            return Result.fail("로그인해야 합나다.");
        }

        Answer answer = answerRepository.getOne(id);
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if (!answer.isSameWriter(loginUser)){
            return Result.fail("자신의 글만 삭제할 수 있습니다.");
        }

        answerRepository.deleteById(id);

        Question question = questionRepository.getOne(questionId);
        question.deleteAnswer();
        questionRepository.save(question);
        return Result.ok();
    }
}
