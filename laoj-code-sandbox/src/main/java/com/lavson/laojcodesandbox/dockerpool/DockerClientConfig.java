package com.lavson.laojcodesandbox.dockerpool;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.lavson.common.constant.DockerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:22
 */
@Slf4j
@Deprecated
public class DockerClientConfig {
    private static String dockerUrl;
    private static ApacheDockerHttpClient httpClient;

    @Value("${docker.url}")
    private String url;

    @PostConstruct
    public void init() {
        dockerUrl = url;
    }

    @Bean
    public DockerClient dockerClient() {
        // 创建并配置 DockerClient 对象
        com.github.dockerjava.core.DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DockerConstant.DOCKER_TCP + dockerUrl).build();
        httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        return DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }

    @PreDestroy
    public void cleanup() {
        if (httpClient != null) {
            try {
                httpClient.close();
                log.info("HTTP Client is closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
