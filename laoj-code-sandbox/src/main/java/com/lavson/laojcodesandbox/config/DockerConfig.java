package com.lavson.laojcodesandbox.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.lavson.common.constant.DockerConstant;
import com.lavson.common.constant.SandBoxConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:12
 */
@Configuration
@Slf4j
public class DockerConfig {

    @Bean
    public PullImageCmd pullDockerImage(DockerClient dockerClient) {
        // 拉取镜像的代码
        String imageName = DockerConstant.DOCKER_IMAGE_NAME;
        boolean imageExists = false;
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (Image image : images) {
            if (Arrays.asList(image.getRepoTags()).contains(imageName)) {
                imageExists = true;
                break;
            }
        }

        PullImageCmd pullImageCmd = null;
        if (!imageExists) {
            pullImageCmd = dockerClient.pullImageCmd(imageName);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    log.info("下载镜像：" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd
                        .exec(pullImageResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                log.info("拉取镜像异常");
                throw new RuntimeException(e);
            }
        }

        log.info("下载完成");
        return pullImageCmd;
    }
}
