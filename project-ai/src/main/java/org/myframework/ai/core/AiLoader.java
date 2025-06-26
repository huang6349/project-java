package org.myframework.ai.core;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.myframework.core.exception.BusinessException;
import org.noear.solon.ai.rag.Document;
import org.noear.solon.ai.rag.loader.PdfLoader;
import org.noear.solon.ai.rag.loader.TextLoader;
import org.noear.solon.ai.rag.loader.WordLoader;
import org.noear.solon.ai.rag.splitter.RegexTextSplitter;
import org.noear.solon.ai.rag.splitter.SplitterPipeline;
import org.noear.solon.ai.rag.splitter.TokenSizeTextSplitter;

import java.io.File;
import java.util.List;

public class AiLoader {

    private static volatile Boolean initialized = Boolean.FALSE;

    private static volatile AiLoader instance;

    public static AiLoader getInstance() {
        if (!initialized) {
            synchronized (AiLoader.class) {
                if (!initialized) {
                    instance = new AiLoader();
                    initialized = Boolean.TRUE;
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    public List<Document> split(File source) {
        var documents = load(source);
        return new SplitterPipeline()
                .next(new RegexTextSplitter("\n\n"))
                .next(new TokenSizeTextSplitter(500))
                .split(documents);
    }

    @SneakyThrows
    public List<Document> load(File source) {
        if (source == null)
            throw new NullPointerException("source can not be null");
        if (StrUtil.endWithAnyIgnoreCase(source.getName(), ".docx", ".doc"))
            return new WordLoader(source).load();
        if (StrUtil.endWithAnyIgnoreCase(source.getName(), ".pdf"))
            return new PdfLoader(source).load();
        if (StrUtil.endWithAnyIgnoreCase(source.getName(), ".txt"))
            return new TextLoader(source).load();
        throw new BusinessException("不支持的文件类型");
    }
}
