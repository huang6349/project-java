package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.myframework.base.response.OptionVO;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import reactor.core.publisher.Flux;

import java.io.Serializable;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.core.satoken.helper.SystemHelper.allowTenant;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;
import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserTenantService extends ReactorService<User> {

    default QueryWrapper getOptionWrapper(Serializable id) {
        var query = QueryWrapper.create()
                .select(TENANT.NAME.as(OptionVO::getLabel),
                        TENANT.ID.as(OptionVO::getValue));
        query.where(TENANT_ASSOC.ASSOC_ID.eq(id));
        query.orderBy(TENANT.ID, Boolean.TRUE);
        return getQueryWrapper(query);
    }

    /**
     * 查询主体：来源表、关联方式与固定过滤条件
     * <p>
     * 主表为租户关联表，租户信息左关联带出，故须额外过滤未命中租户的关联行
     *
     * @param query 查询包装器
     * @return 查询包装器
     */
    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.from(TENANT_ASSOC)
                .leftJoin(TENANT)
                .on(TENANT.ID.eq(TENANT_ASSOC.TENANT_ID))
                .where(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(TENANT_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(TENANT_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(TENANT_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(TENANT.STATUS.ne(TenantStatus.TYPE1));
    }

    /**
     * 查询当前登录用户关联的可切换租户选项
     * <p>
     * 租户功能未启用时返回空，启用时仅返回关联有效且未被禁用的租户
     *
     * @return 租户选项列表
     */
    default Flux<OptionVO> option() {
        if (allowTenant()) {
            var loginId = UserHelper.getLoginIdAsLong();
            var query = getOptionWrapper(loginId);
            return listAs(query, OptionVO.class);
        } else return Flux.empty();
    }
}
