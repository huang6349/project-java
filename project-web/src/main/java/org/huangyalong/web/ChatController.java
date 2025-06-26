package org.huangyalong.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.huangyalong.modules.ai.core.AiBot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
@Tag(name = "人工智能")
public class ChatController {

    @GetMapping
    @Operation(summary = "文字聊天")
    public String chat(String message) {
        return AiBot.getInstance()
                .chat(message);
    }
}
