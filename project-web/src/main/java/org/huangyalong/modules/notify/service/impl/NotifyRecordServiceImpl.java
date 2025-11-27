package org.huangyalong.modules.notify.service.impl;

import org.huangyalong.modules.notify.domain.NotifyRecord;
import org.huangyalong.modules.notify.esmp.NotifyRecordMapper;
import org.huangyalong.modules.notify.service.NotifyRecordService;
import org.myframework.es.service.impl.EsServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NotifyRecordServiceImpl extends EsServiceImpl<NotifyRecordMapper, NotifyRecord> implements NotifyRecordService {
}
