package org.myframework.qdb.config;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import io.questdb.client.QuestDB;
import org.myframework.qdb.properties.QdbProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrameworkQdb {

    @Bean
    public QuestDB qdbQuestDB(QdbProperties properties) {
        var host = Opt.ofNullable(properties)
                .map(QdbProperties::getHost)
                .get();
        var port = Opt.ofNullable(properties)
                .map(QdbProperties::getPort)
                .get();
        var username = Opt.ofNullable(properties)
                .map(QdbProperties::getUsername)
                .get();
        var password = Opt.ofNullable(properties)
                .map(QdbProperties::getPassword)
                .get();
        // HTTP 短连接，避免 WS 空闲超时被服务端回收断连刷日志
        var ilpUrl = new StrBuilder("http::addr=")
                .append(host)
                .append(':')
                .append(port)
                .append(';');
        if (StrUtil.isNotBlank(username))
            ilpUrl.append("username=")
                    .append(username)
                    .append(';');
        if (StrUtil.isNotBlank(password))
            ilpUrl.append("password=")
                    .append(password)
                    .append(';');
        return QuestDB.connect(ilpUrl);
    }
}
