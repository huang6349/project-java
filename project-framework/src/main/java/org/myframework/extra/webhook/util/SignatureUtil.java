package org.myframework.extra.webhook.util;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import org.myframework.extra.webhook.WebhookRequest;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static cn.hutool.crypto.SecureUtil.hmacSha256;
import static cn.hutool.json.JSONUtil.toJsonStr;

public class SignatureUtil {

    public static String generateSignature(WebhookRequest request,
                                           String deliveryId) {
        // 获取签名密钥
        var secret = Opt.ofNullable(request)
                .map(WebhookRequest::getSecret)
                .get();
        // 如果没有提供密钥，返回空签名
        if (StrUtil.isBlank(secret))
            return "";
        // 构建签名字符串：推送内容 + 请求ID
        var payload = toJsonStr(request.getData());
        var signStr = format("{}.{}", payload, deliveryId);
        // 使用密钥对签名字符串进行 SHA256 HMAC 哈希计算
        var hash = hmacSha256(secret).digestHex(signStr);
        // 返回 X-Webhook-Signature 请求头的值，格式为"sha256={hash}"
        return format("sha256={}", hash);
    }
}
