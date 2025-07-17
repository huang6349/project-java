package org.huangyalong.modules.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import com.mybatisflex.core.query.If;
import lombok.AllArgsConstructor;
import org.huangyalong.core.satoken.helper.UserHelper;
import org.huangyalong.modules.system.domain.TenantAssoc;
import org.huangyalong.modules.system.domain.User;
import org.huangyalong.modules.system.mapper.UserMapper;
import org.huangyalong.modules.system.request.RoleAssocBO;
import org.huangyalong.modules.system.request.RoleDissocBO;
import org.huangyalong.modules.system.request.TenantUserBO;
import org.huangyalong.modules.system.service.RoleAssocService;
import org.huangyalong.modules.system.service.TenantAssocService;
import org.huangyalong.modules.system.service.TenantUserService;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.huangyalong.core.constants.RoleConstants.USER_ID;
import static org.huangyalong.modules.system.domain.table.TenantAssocTableDef.TENANT_ASSOC;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;

@AllArgsConstructor
@Service
public class TenantUserServiceImpl extends ReactorServiceImpl<UserMapper, User> implements TenantUserService {

    private final TenantAssocService tenantService;

    private final RoleAssocService roleService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add(TenantUserBO userBO) {
        validateAddOrUpdate(userBO);
        var tenantId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getTenantId)
                .get();
        var userId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getUserId)
                .get();
        var desc = Opt.ofNullable(userBO)
                .map(TenantUserBO::getDesc)
                .get();
        var data = TenantAssoc.create()
                .setTenantId(tenantId)
                .setAssoc(USER.getTableName())
                .setAssocId(userId)
                .setDesc(desc);
        return tenantService.save(data)
                .thenReturn(userBO)
                .flatMap(this::roleAssoc);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update(TenantUserBO userBO) {
        validateAddOrUpdate(userBO);
        var desc = Opt.ofNullable(userBO)
                .map(TenantUserBO::getDesc)
                .get();
        var id = Opt.ofNullable(userBO)
                .map(TenantUserBO::getId)
                .get();
        var data = tenantService.getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .setDesc(desc);
        return tenantService.updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = tenantService.getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return tenantService.removeById(data)
                .thenReturn(data)
                .flatMap(this::roleDissoc);
    }

    void validateAddOrUpdate(TenantUserBO userBO) {
        var tenantId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getTenantId)
                .get();
        var userId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getUserId)
                .get();
        var id = Opt.ofNullable(userBO)
                .map(TenantUserBO::getId)
                .get();
        var exists = TenantAssoc.create()
                .where(TENANT_ASSOC.EFFECTIVE.eq(TimeEffective.TYPE0))
                .and(TENANT_ASSOC.CATEGORY.eq(AssocCategory.TYPE0))
                .and(TENANT_ASSOC.TENANT_ID.eq(tenantId))
                .and(TENANT_ASSOC.ASSOC.eq(USER.getTableName()))
                .and(TENANT_ASSOC.ASSOC_ID.eq(userId))
                .and(TENANT_ASSOC.ID.ne(id, If::notNull))
                .exists();
        if (BooleanUtil.isFalse(exists)) return;
        throw new BusinessException("用户已存在");
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }

    Mono<Boolean> roleAssoc(TenantUserBO userBO) {
        var tenantId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getTenantId)
                .get();
        var userId = Opt.ofNullable(userBO)
                .map(TenantUserBO::getUserId)
                .get();
        var assocBO = new RoleAssocBO();
        assocBO.setTenantId(tenantId);
        assocBO.setRoleIds(ListUtil.of(USER_ID));
        assocBO.setAssoc(USER.getTableName());
        assocBO.setId(userId);
        return roleService.assoc(assocBO)
                .thenReturn(userId)
                .doOnNext(UserHelper::load)
                .thenReturn(Boolean.TRUE);
    }

    Mono<Boolean> roleDissoc(TenantAssoc assoc) {
        var tenantId = Opt.ofNullable(assoc)
                .map(TenantAssoc::getTenantId)
                .get();
        var userId = Opt.ofNullable(assoc)
                .map(TenantAssoc::getAssocId)
                .get();
        var dissocBO = new RoleDissocBO();
        dissocBO.setTenantId(tenantId);
        dissocBO.setAssoc(USER.getTableName());
        dissocBO.setId(userId);
        return roleService.dissoc(dissocBO)
                .thenReturn(userId)
                .doOnNext(UserHelper::load)
                .thenReturn(Boolean.TRUE);
    }
}
