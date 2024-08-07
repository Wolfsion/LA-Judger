package com.lavson.laojcodesandbox.service.java;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lavson.common.constant.SandBoxConstant;
import com.lavson.laojcodesandbox.service.CodeSandbox;
import com.lavson.laojcodesandbox.utils.ProcessUtil;
import com.lavson.model.codesandbox.ExecuteCodeRequest;
import com.lavson.model.codesandbox.ExecuteCodeResponse;
import com.lavson.model.codesandbox.ExecuteMessage;
import com.lavson.model.codesandbox.JudgeInfo;
import com.lavson.model.entity.JudgeConfig;
import com.lavson.model.enums.ExitCodeEnum;
import com.lavson.model.enums.JudgeResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/14 - 20:30
 */
@Slf4j
public abstract class JavaCodeBoxTemplate implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = SandBoxConstant.GLOBAL_CODE_DIR_NAME;

    private static final String GLOBAL_JAVA_CLASS_NAME = SandBoxConstant.GLOBAL_JAVA_CLASS_NAME;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        JudgeConfig config = executeCodeRequest.getConfig();

//        1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);

//        2. 编译代码，得到 class 文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        log.info("java代码编译结果: " + compileFileExecuteMessage);

        // 3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList, config);

//        4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList);

//        5. 文件清理
        boolean b = deleteFile(userCodeFile);
        if (!b) {
            log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        return outputResponse;
    }


    /**
     * 1. 把用户的代码保存为文件
     * @param code 用户代码
     * @return
     */
    public File saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        return FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    }

    /**
     * 2、编译代码
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(compileProcess, "编译");
            if (!Objects.equals(executeMessage.getExitValue(), ExitCodeEnum.SUCCESS.getValue())) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
//            return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3、执行文件，获得执行结果列表
     * @param userCodeFile
     * @param inputList
     * @return
     */
    abstract public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, JudgeConfig config);

    /**
     * 4、获取输出结果
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        List<JudgeInfo> jis = new ArrayList<>();

        for (ExecuteMessage executeMessage : executeMessageList) {
            JudgeInfo judgeInfo = new JudgeInfo();

            String errorMessage = executeMessage.getErrorMessage();
            if (!SandBoxConstant.BLANK_ERROR_MESSAGE.equals(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(ExitCodeEnum.UNKNOWN_ERROR.getValue());
                judgeInfo.setJudge(JudgeResultEnum.RUNTIME_ERROR);
                jis.add(judgeInfo);
                if (StrUtil.isNotBlank(executeCodeResponse.getMessage())) {
                    executeCodeResponse.setMessage(executeMessage.getErrorMessage());
                }
                continue;
            }

            outputList.add(executeMessage.getOutput());

            judgeInfo.setTime(executeMessage.getTime());
            judgeInfo.setMemory(executeMessage.getMemory());
            if (ObjectUtil.isNotNull(executeMessage.getJudge())) {
                judgeInfo.setJudge(executeMessage.getJudge());
            } else {
                judgeInfo.setJudge(JudgeResultEnum.JUDGING_WAITING);
            }

            jis.add(judgeInfo);
        }

        // 正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(ExitCodeEnum.SUCCESS.getValue());
        }

        executeCodeResponse.setOutputList(outputList);
        executeCodeResponse.setJudgeInfos(jis);
        return executeCodeResponse;
    }

    /**
     * 5、删除文件
     * @param userCodeFile
     * @return
     */
    public boolean deleteFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            boolean del = FileUtil.del(userCodeParentPath);
            log.info("代码和字节码" + "删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    /**
     * 6、获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(ExitCodeEnum.SANDBOX_ERROR.getValue());
        executeCodeResponse.setJudgeInfos(Collections.singletonList(new JudgeInfo()));
        return executeCodeResponse;
    }
}
