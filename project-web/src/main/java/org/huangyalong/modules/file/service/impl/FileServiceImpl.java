package org.huangyalong.modules.file.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.mapper.FileMapper;
import org.huangyalong.modules.file.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ReactorServiceImpl<FileMapper, File> implements FileService {
}
