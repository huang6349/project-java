package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import org.huangyalong.core.satoken.helper.PermCodeHelper;
import org.huangyalong.core.satoken.helper.PermHelper;
import org.huangyalong.core.satoken.helper.RoleCodeHelper;
import org.huangyalong.core.satoken.helper.RoleHelper;
import org.huangyalong.core.satoken.helper.TenantHelper;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.TenantSwitchBO;
import org.huangyalong.modules.system.service.UserTenantService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static cn.hutool.core.lang.Opt.ofNullable;
import static com.mybatisflex.core.query.QueryMethods.now;
import static org.huangyalong.core.constants.TenantConstants.NONE;
import static org.huangyalong.core.satoken.helper.SystemHelper.allowTenant;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;
import static org.huangyalong.modules.system.domain.table.TenantTableDef.TENANT;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@Service
public class UserTenantServiceImpl extends ReactorServiceImpl<UserMapper, User> implements UserTenantService {

    /**
     * 切换当前登录用户的默认租户
     * <p>
     * 校验通过后持久化 user.tenant_id，并即时刷新该用户的租户与权限缓存，
     * 使切换结果对后续请求立即生效，而不是等缓存自然过期
     *
     * @param switchBO 目标租户
     * @return 是否切换成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> switchTo(TenantSwitchBO switchBO) {
        validateSwitch(switchBO);
        var tenantId = getTenantId(switchBO);
        var id = UserHelper.getLoginIdAsLong();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .setTenantId(tenantId);
        return updateById(data)
                .thenReturn(id)
                .doOnNext(this::reload)
                .thenReturn(Boolean.TRUE);
    }

    /**
     * 校验目标租户是否可切换
     * <p>
     * 关联须处于有效期内、类别与关联对象须匹配，且租户未被禁用；
     * 过滤口径须与 option() 返回的可切换列表保持一致，否则会出现列表里有、却切不过去的情况
     *
     * @param switchBO 目标租户
     */
    void validateSwitch(TenantSwitchBO switchBO) {
        var tenantId = getTenantId(switchBO);
        var id = UserHelper.getLoginIdAsLong();
        var exists = queryChain()
                .from(TENANT_ASSOC)
                .leftJoin(TENANT)
                .on(TENANT.ID.eq(TENANT_ASSOC.TENANT_ID))
                .where(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0)
                        .or(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE1)
                                .and(TENANT_ASSOC.EFFECTIVE_TIME.ge(now()))))
                .and(TENANT_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(TENANT_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(TENANT_ASSOC.TENANT_ID.eq(tenantId))
                .and(TENANT_ASSOC.ASSOC_ID.eq(id))
                .and(TENANT.STATUS.ne(TenantStatus.TYPE1))
                .exists();
        if (BooleanUtil.isTrue(exists)) return;
        throw new BusinessException("该租户不可切换");
    }

    /**
     * 获取目标租户编号
     * <p>
     * 租户功能未启用时返回 NONE 而非在此抛错，交由 validateSwitch 以同一口径拒绝切换
     *
     * @param switchBO 目标租户
     * @return 租户编号，租户功能未启用时为 NONE
     */
    Long getTenantId(TenantSwitchBO switchBO) {
        if (allowTenant()) {
            return ofNullable(switchBO)
                    .map(TenantSwitchBO::getTenantId)
                    .orElseThrow(() -> new BusinessException("租户不能为空"));
        } else return NONE;
    }

    /**
     * 刷新当前用户的租户与权限缓存
     * <p>
     * 这几类缓存的键都只含用户编号、不含租户，切租户后不主动重刷就会残留旧租户的数据；
     * TenantHelper 须最先刷新，其余 Helper 的 fetch 依赖 getTenant()；
     * 参数须为 Object，否则会被 FetchLoadHelper.load(Serializable) 实例方法遮蔽
     *
     * @param id 用户编号
     */
    void reload(Object id) {
        TenantHelper.load(id);
        PermCodeHelper.load(id);
        RoleCodeHelper.load(id);
        PermHelper.load(id);
        RoleHelper.load(id);
    }
}
