package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateController
{
    @Value("classpath:/promptTemplates/userPromptTemplate.st")
    Resource userPromptTemplate;

    private final ChatClient chatClient;

    @Autowired // writing autowired is optional here
    public PromptTemplateController(ChatClient chatClient)
    {
        this.chatClient = chatClient;
    }

    @GetMapping("/email")
    public String emailResponse(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerMessage") String customerMessage
    )
    {
        return chatClient.prompt()
                .system("""
                                You are a professional customer service assistant which helps drafting email
                                responses to improve the productivity of the customer support team
                                """)
                //                .user(message)
                .user(promptTemplateSpec -> promptTemplateSpec
                        .text(userPromptTemplate)
                        .param("customerName", customerName)
                        .param("customerMessage", customerMessage))
                .call()
                .content();
    }
}
