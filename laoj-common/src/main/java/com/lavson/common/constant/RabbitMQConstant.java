package com.lavson.common.constant;

public @interface RabbitMQConstant {
    String JUDGE_EXCHANGE = "laoj.code.direct";
    String JUDGE_QUEUE = "laoj.judgingCode.queue";
    String JUDGE_ROUTING_KEY = "judgingCode";

}
