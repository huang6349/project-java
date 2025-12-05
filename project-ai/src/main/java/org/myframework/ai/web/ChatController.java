package org.myframework.ai.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.myframework.ai.core.AiBot;
import org.myframework.base.response.IgnoreResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Tag(name = "智能助手")
public class ChatController {

    @IgnoreResponse
    @SaCheckLogin
    @GetMapping
    @Operation(summary = "文字聊天")
    public String chat(String message) {
        return AiBot.getInstance()
                .chat(message);
    }
}
