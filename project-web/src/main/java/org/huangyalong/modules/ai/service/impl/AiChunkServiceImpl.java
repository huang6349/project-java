package org.huangyalong.modules.ai.service.impl;

import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.modules.ai.domain.AiChunk;
import org.huangyalong.modules.ai.mapper.AiChunkMapper;
import org.huangyalong.modules.ai.service.AiChunkService;
import org.springframework.stereotype.Service;

@Service
public class AiChunkServiceImpl extends ReactorServiceImpl<AiChunkMapper, AiChunk> implements AiChunkService {
}
