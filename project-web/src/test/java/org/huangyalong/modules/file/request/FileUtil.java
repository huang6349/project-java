package org.huangyalong.modules.file.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import org.huangyalong.modules.file.domain.File;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;

import static cn.hutool.core.text.StrFormatter.format;
import static org.huangyalong.modules.file.domain.table.FileTableDef.FILE;

public interface FileUtil {

    String DEFAULT_NAME = "测试文件";

    String DEFAULT_EXT = "txt";

    String DEFAULT_ORIG_FILENAME = format("{}.{}", DEFAULT_NAME, DEFAULT_EXT);

    String DEFAULT_CONTENT = RandomUtil.randomString(12);

    static MultiValueMap<String, HttpEntity<?>> createFile() {
        var builder = new MultipartBodyBuilder();
        builder.part("file", DEFAULT_CONTENT)
                .contentType(MediaType.TEXT_PLAIN)
                .filename(DEFAULT_ORIG_FILENAME);
        return builder.build();
    }

    static File getEntity() {
        return File.create()
                .orderBy(FILE.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map(File::getId)
                .get();
    }
}
