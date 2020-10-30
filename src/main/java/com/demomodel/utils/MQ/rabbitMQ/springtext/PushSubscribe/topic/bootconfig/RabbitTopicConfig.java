package com.demomodel.utils.MQ.rabbitMQ.springtext.PushSubscribe.topic.bootconfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitTopicConfig {
    final static String message = "topic.message";
    final static String messages = "topic.messages";
    //创建队列
    @Bean
    public Queue queueMessage() {
        return new Queue(RabbitTopicConfig.message);
    }
    //创建队列
    @Bean
    public Queue queueMessages() {
        return new Queue(RabbitTopicConfig.messages);
    }
    //创建交换器
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }
 //对列绑定并关联到ROUTINGKEY
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }
    //对列绑定并关联到ROUTINGKEY
    @Bean
    Binding bindingExchangeMessages(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.#");//*表示一个词,#表示零个或多个词
    }

}