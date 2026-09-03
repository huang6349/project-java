package org.myframework.qdb.config;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.myframework.qdb.properties.QdbProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class FrameworkQdb {

    /**
     * 构建 ILP over HTTP 连接串（短连接，避免 WS 空闲超时被服务端回收断连刷日志）
     */
    @Bean
    public IlpConfig qdbIlpConfig(QdbProperties properties) {
        var username = Opt.ofNullable(properties)
                .map(QdbProperties::getUsername)
                .get();
        var password = Opt.ofNullable(properties)
                .map(QdbProperties::getPassword)
                .get();
        var host = Opt.ofNullable(properties)
                .map(QdbProperties::getHost)
                .get();
        var port = Opt.ofNullable(properties)
                .map(QdbProperties::getPort)
                .get();
        var conf = new LinkedHashMap<String, String>();
        conf.put("addr", host + ":" + port);
        if (StrUtil.isNotBlank(username))
            conf.put("username", username);
        if (StrUtil.isNotBlank(password))
            conf.put("password", password);
        var confStr = MapUtil.join(conf, ";", "=");
        var connStr = "http::" + confStr + ";";
        return new IlpConfig(connStr);
    }
}
