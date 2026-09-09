package org.huangyalong.modules.notify.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.huangyalong.modules.notify.request.RecordQueries;
import org.myframework.core.satoken.annotation.PreAuth;
import org.myframework.qdb.web.QdbController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuth(replace = "@notify")
@RestController
@RequestMapping("/notify/record")
@Tag(name = "消息记录")
public class NotifyRecordController extends QdbController<
        String,
        RecordQueries> {
}
