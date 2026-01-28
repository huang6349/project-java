package org.huangyalong.modules.system.web;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.huangyalong.modules.system.request.CaptchaBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Getter
@RestController
@RequestMapping("/captcha")
@Tag(name = "验证码")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/_gen")
    @Operation(summary = "获取验证码图片")
    public ResponseModel getCaptcha(@RequestBody CaptchaBO captchaBO,
                                    HttpServletRequest request) {
        var captchaVO = new CaptchaVO();
        captchaVO.setCaptchaType("blockPuzzle");
        captchaVO.setClientUid(captchaBO.getClientUid());
        captchaVO.setTs(captchaBO.getTs());
        captchaVO.setBrowserInfo(getRemoteId(request));
        return getCaptchaService().get(captchaVO);
    }

    @PostMapping("/_check")
    @Operation(summary = "校验验证码")
    public ResponseModel checkCaptcha(@RequestBody CaptchaBO captchaBO,
                                      HttpServletRequest request) {
        var captchaVO = new CaptchaVO();
        captchaVO.setCaptchaType("blockPuzzle");
        captchaVO.setPointJson(captchaBO.getPointJson());
        captchaVO.setToken(captchaBO.getToken());
        captchaVO.setTs(captchaBO.getTs());
        captchaVO.setBrowserInfo(getRemoteId(request));
        return getCaptchaService().check(captchaVO);
    }

    String getRemoteIpFromXfwd(String xfwd) {
        if (StrUtil.isNotBlank(xfwd)) {
            var ipList = xfwd.split(",");
            return StrUtil.trim(ipList[0]);
        } else return null;
    }

    String getRemoteId(HttpServletRequest request) {
        var xfwd = request.getHeader("X-Forwarded-For");
        var ip = getRemoteIpFromXfwd(xfwd);
        var ua = request.getHeader("user-agent");
        if (StrUtil.isBlank(ip)) {
            return request.getRemoteAddr() + ua;
        } else return ip + ua;
    }
}
