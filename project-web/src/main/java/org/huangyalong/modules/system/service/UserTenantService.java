package org.huangyalong.modules.system.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.QueryWrapper;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.huangyalong.modules.system.request.TenantSwitchBO;
import org.myframework.base.response.OptionVO;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.core.satoken.helper.SystemHelper.allowTenant;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;
import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

public interface UserTenantService extends ReactorService<User> {

    /**
     * 构造当前用户的可切换租户选项查询
     * <p>
     * 按用户编号限定关联，再叠加公共过滤
     *
     * @param id 用户编号
     * @return 查询包装器
     */
    default QueryWrapper getOptionWrapper(Serializable id) {
        var query = QueryWrapper.create()
                .select(TENANT.NAME.as(OptionVO::getLabel),
                        TENANT.ID.as(OptionVO::getValue));
        query.where(TENANT_ASSOC.ASSOC_ID.eq(id));
        query.orderBy(TENANT.ID, Boolean.TRUE);
        return getQueryWrapper(query);
    }

    /**
     * 叠加租户选项查询的公共过滤条件
     * <p>
     * 主表为租户关联表、租户信息左关联带出，故须过滤掉未命中租户的关联行；
     * 过滤口径须与 UserTenantServiceImpl.validateSwitch 保持一致，
     * 否则会出现列表里有、却切不过去的租户
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
     * 只返回该用户关联有效、且未被禁用的租户，供前端展示可切换范围
     *
     * @return 租户选项列表，租户功能未启用时为空
     */
    default Flux<OptionVO> option() {
        if (allowTenant()) {
            var loginId = UserHelper.getLoginIdAsLong();
            var query = getOptionWrapper(loginId);
            return listAs(query, OptionVO.class);
        } else return Flux.empty();
    }

    /**
     * 切换当前登录用户的默认租户
     * <p>
     * 仅允许切换到该用户关联且有效的租户，切换后立即生效；
     * 可切换范围与 option() 返回的列表一致
     *
     * @param switchBO 目标租户
     * @return 是否切换成功，目标租户不可切换或租户为空时抛出业务异常
     */
    Mono<Boolean> switchTo(TenantSwitchBO switchBO);
}
