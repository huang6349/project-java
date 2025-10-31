package org.huangyalong.modules.file.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.file.domain.FilePart;
import org.huangyalong.modules.file.mapper.FilePartMapper;
import org.huangyalong.modules.file.service.FilePartService;
import org.springframework.stereotype.Service;

@Service
public class FilePartServiceImpl extends ReactorServiceImpl<FilePartMapper, FilePart> implements FilePartService {
}
