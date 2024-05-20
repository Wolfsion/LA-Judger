package com.lavson.laojcodesandbox.dockerpool;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.lavson.common.constant.DockerConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * todo
 *
 * @author LA
 * @version 1.0
 * 2024/5/16 - 19:22
 */
@Component
public class DockerClientPool {
    private static String dockerUrl;

    @Value("${docker.url}")
    private String url;

    @PostConstruct
    public void init() {
        dockerUrl = url;
    }

    @Bean
    static public DockerClient dockerClient() {
        // 创建并配置 DockerClient 对象
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DockerConstant.DOCKER_TCP + dockerUrl).build();
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        return DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }
}
