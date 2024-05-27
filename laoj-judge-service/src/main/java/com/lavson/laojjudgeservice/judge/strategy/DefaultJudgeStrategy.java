package com.lavson.laojjudgeservice.judge.strategy;

import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.enums.JudgeResultEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/8 - 16:45
 */
@Slf4j
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<JudgeInfo> judgeInfos = judgeContext.getJudgeInfos();
        List<String> outputList = judgeContext.getOutputList();
        List<String> target = judgeContext.getTarget();
        JudgeInfo judgeInfoResponse = new JudgeInfo();

        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (target.size() != outputList.size()) {
            judgeInfoResponse.setJudge(JudgeResultEnum.SYSTEM_ERROR);
            return judgeInfoResponse;
        }

        boolean allJudgingWaiting = judgeInfos.stream()
                .allMatch(judgeInfo -> judgeInfo.getJudge() == JudgeResultEnum.JUDGING_WAITING);

        // todo: time and memory limit judge
        if (!allJudgingWaiting) {
            judgeInfoResponse.setJudge(JudgeResultEnum.RUNTIME_ERROR);
            return judgeInfoResponse;
        }

        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeInfos.size(); i++) {
            if (!target.get(i).equals(outputList.get(i))) {
                judgeInfoResponse.setJudge(JudgeResultEnum.WRONG_ANSWER);
                return judgeInfoResponse;
            }
        }

        judgeInfoResponse.setJudge(JudgeResultEnum.ACCEPTED);

        // 获取最大时间
        Optional<Long> maxTime = judgeInfos.stream()
                .map(JudgeInfo::getTime)
                .max(Comparator.comparingLong(Long::valueOf));

        // 获取最大内存
        Optional<Long> maxMemory = judgeInfos.stream()
                .map(JudgeInfo::getMemory)
                .max(Comparator.comparingLong(Long::valueOf));

        if (maxTime.isPresent()) {
            judgeInfoResponse.setTime(maxTime.get());
        } else {
            log.error("No time data.");
        }

        if (maxMemory.isPresent()) {
            judgeInfoResponse.setMemory(maxMemory.get());
        } else {
            log.error("No memory data.");
        }

        return judgeInfoResponse;
    }
}
