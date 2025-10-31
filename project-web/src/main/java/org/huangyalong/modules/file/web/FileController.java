package org.huangyalong.modules.file.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.net.URLEncodeUtil;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.request.FileQueries;
import org.huangyalong.modules.file.service.FileService;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.SuperSimpleController;
import org.myframework.base.web.curd.QueryController;
import org.myframework.core.exception.BusinessException;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.mybatis.flex.reactor.core.utils.ReactorUtils.runBlock;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Getter
@PreAuth(replace = "@file")
@RestController
@RequestMapping("/file")
@Tag(name = "文件管理")
public class FileController extends SuperSimpleController<
        FileService,
        File
        > implements QueryController<
        File,
        Long,
        FileQueries> {

    @Resource
    private FileStorageService storageService;

    @Override
    public ApiResponse<QueryWrapper> handlerQuery(FileQueries queries) {
        var data = getBaseService()
                .getQueryWrapper(queries);
        return ApiResponse.ok(data);
    }

    @SaCheckLogin
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件")
    public Mono<String> upload(@RequestParam("file") MultipartFile file) {
        var path = DatePattern.PURE_DATE_FORMAT.format(new DateTime());
        var fileInfo = getStorageService().of(file)
                .setPath(format("{}/", path))
                .upload();
        var filename = Opt.ofNullable(fileInfo)
                .map(FileInfo::getFilename)
                .get();
        return Mono.justOrEmpty(filename);
    }

    @GetMapping(value = "/download/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "下载文件")
    public void download(@PathVariable("filename") String filename,
                         HttpServletResponse response) {
        try (var out = response.getOutputStream()) {
            var fileInfo = runBlock(getBaseService()
                    .getByFilename(filename)
                    .switchIfEmpty(Mono.error(new BusinessException("文件不存在")))
                    .map(File::without));
            var origFilename = Opt.ofNullable(fileInfo)
                    .map(FileInfo::getOriginalFilename)
                    .map(URLEncodeUtil::encodeFragment)
                    .get();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(CONTENT_DISPOSITION, format("attachment; filename=\"{}\"", origFilename));
            getStorageService()
                    .download(fileInfo)
                    .outputStream(out);
        } catch (IOException e) {
            throw new BusinessException("下载失败，服务器暂时无法处理这个文件");
        }
    }
}
