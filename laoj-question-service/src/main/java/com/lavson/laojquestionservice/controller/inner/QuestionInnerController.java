package com.lavson.laojquestionservice.controller.inner;

import com.lavson.laojquestionservice.service.QuestionService;
import com.lavson.laojquestionservice.service.QuestionSubmitService;
import com.lavson.laojserviceclient.service.QuestionFeignClient;
import com.lavson.model.entity.Question;
import com.lavson.model.entity.QuestionSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:30
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

}
