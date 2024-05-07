package com.lavson.laojserviceclient.service;

import com.lavson.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/7 - 20:32
 */
@FeignClient(name = "laoj-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
