package org.huangyalong.modules.file.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.modules.file.domain.File;
import org.huangyalong.modules.file.request.FileQueries;
import reactor.core.publisher.Mono;

import static org.huangyalong.modules.file.domain.table.FileTableDef.FILE;

public interface FileService extends ReactorService<File> {

    default QueryWrapper getQueryWrapper(FileQueries queries,
                                         QueryWrapper query) {
        query.where(FILE.ORIG_FILENAME.like(queries.getOrigFilename(), If::hasText));
        query.where(FILE.EXT.like(queries.getExt(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper(FileQueries queries) {
        var query = QueryWrapper.create();
        query.orderBy(FILE.ID, Boolean.FALSE);
        return getQueryWrapper(queries, query);
    }

    default Mono<File> getByFilename(String filename) {
        var query = QueryWrapper.create()
                .where(FILE.FILENAME.eq(filename));
        return getOne(query);
    }
}
