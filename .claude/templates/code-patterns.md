# 代码模式模板库

本文件包含项目通用的代码模式，供 Skills/Agents/Commands 引用。

---

## CRUD 模块模板

### 接口层

```text
// {Module}Controller.java
package {package}.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import {package}.domain.{Module};
import {package}.request.{Module}BO;
import {package}.request.{Module}Queries;
import {package}.service.{Module}Service;
import org.myframework.base.response.ApiResponse;
import org.myframework.base.web.ReactorController;
import org.myframework.core.satoken.annotation.PreAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuth(replace = "@{module}")
@RestController
@RequestMapping("/{module}")
@Tag(name = "{name}管理")
public class {Module}Controller extends ReactorController<
        {Module}Service,
        Long,
        {Module},
        {Module}Queries,
        {Module}BO,
        {Module}BO
        > {

    @Override
    public ApiResponse<Mono<Boolean>> handlerSave({Module}BO {module}BO) {
        var data = getBaseService()
                .add({module}BO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerUpdate({Module}BO {module}BO) {
        var data = getBaseService()
                .update({module}BO);
        return ApiResponse.ok(data);
    }

    @Override
    public ApiResponse<Mono<Boolean>> handlerDelete(Long id) {
        var data = getBaseService()
                .delete(id);
        return ApiResponse.ok(data);
    }
}
```

### 服务层

```text
// {Module}Service.java
package {package}.service;

import com.mybatis.flex.reactor.core.ReactorService;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import {package}.domain.{Module};
import {package}.request.{Module}BO;
import {package}.request.{Module}Queries;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static {package}.domain.table.{Module}TableDef.{MODULE};

public interface {Module}Service extends ReactorService<Module> {

    default QueryWrapper getQueryWrapper({Module}Queries queries,
                                         QueryWrapper query) {
        query.where({MODULE}.DESC.like(queries.getDesc(), If::hasText));
        return query;
    }

    default QueryWrapper getQueryWrapper({Module}Queries queries) {
        var query = QueryWrapper.create();
        query.orderBy({MODULE}.ID, Boolean.TRUE);
        return getQueryWrapper(queries, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(Serializable id,
                                         QueryWrapper query) {
        query.where({MODULE}.ID.eq(id));
        return query;
    }

    default QueryWrapper getQueryWrapper(Serializable id) {
        var query = QueryWrapper.create();
        return getQueryWrapper(id, getQueryWrapper(query));
    }

    default QueryWrapper getQueryWrapper(QueryWrapper query) {
        return query.select({MODULE}.ID,
                        {MODULE}.DESC,
                        {MODULE}.CREATE_TIME,
                        {MODULE}.UPDATE_TIME)
                .from({MODULE});
    }

    @Override
    default Mono<{Module}> getById(Serializable id) {
        var query = getQueryWrapper(id);
        return getOne(query);
    }

    Mono<Boolean> add({Module}BO {module}BO);

    Mono<Boolean> update({Module}BO {module}BO);

    Mono<Boolean> delete(Serializable id);
}
```

```text
// {Module}ServiceImpl.java
package {package}.service.impl;

import cn.hutool.core.lang.Opt;
import com.mybatis.flex.reactor.spring.ReactorServiceImpl;
import {package}.domain.{Module};
import {package}.mapper.{Module}Mapper;
import {package}.request.{Module}BO;
import {package}.service.{Module}Service;
import org.myframework.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.Serializable;

import static cn.hutool.core.convert.Convert.toLong;
import static org.myframework.core.constants.Constants.SYSTEM_RESERVED;
import static org.myframework.core.exception.ErrorCode.ERR_RESERVED;
import static org.myframework.core.exception.ErrorCode.NOT_FOUND;
import static org.myframework.core.util.ServiceUtil.randomCode;

@Service
public class {Module}ServiceImpl extends ReactorServiceImpl<{Module}Mapper, {Module}> implements {Module}Service {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add({Module}BO {module}BO) {
        var data = {Module}.create()
                .setCode(randomCode())
                .with({module}BO);
        return save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> update({Module}BO {module}BO) {
        var id = Opt.ofNullable({module}BO)
                .map({Module}BO::getId)
                .get();
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND))
                .with({module}BO);
        return updateById(data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> delete(Serializable id) {
        validateDelete(id);
        var data = getBlockService()
                .getByIdOpt(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        return removeById(data);
    }

    void validateDelete(Serializable id) {
        if (SYSTEM_RESERVED <= toLong(id)) return;
        throw new BusinessException(ERR_RESERVED);
    }
}
```

### 实体层

```text
// {Module}.java
package {package}.domain;

import cn.hutool.core.lang.Opt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.AutoTable;
import {package}.request.{Module}BO;
import org.myframework.base.domain.Entity;

@Data(staticConstructor = "create")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AutoTable(comment = "{name}信息")
@Table(value = "tb_{module}")
@Schema(name = "{name}信息")
public class {Module} extends Entity<{Module}, Long> {

    @AutoColumn(comment = "备注", length = 512)
    @Schema(description = "备注")
    private String desc;

    @Column(tenantId = true)
    @JsonIgnore
    @AutoColumn(comment = "租户主键(所属租户)", notNull = true)
    @Schema(description = "租户主键")
    private Long tenantId;

    /****************** with ******************/

    public {Module} with({Module}BO {module}BO) {
        Opt.ofNullable({module}BO)
                .map({Module}BO::getDesc)
                .ifPresent(this::setDesc);
        return this;
    }
}
```

```text
// {Module}Queries.java
package {package}.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseQueries;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "{name}信息-Queries")
public class {Module}Queries extends BaseQueries {

    @Schema(description = "备注")
    private String desc;
}
```

```text
// {Module}BO.java
package {package}.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.myframework.base.request.BaseBO;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "{name}信息-BO")
public class {Module}BO extends BaseBO<Long> {

    @Schema(description = "备注")
    private String desc;
}
```

---

## 变量替换速查表

| 变量          | 使用场景                                | 示例                |
|-------------|-------------------------------------|-------------------|
| `{package}` | 包路径，用于 import 和 package 声明          | `org.huangyalong` |
| `{module}`  | 模块名称，用于类名小驼峰、包路径目录名、URL 路径          | `example`         |
| `{Module}`  | 模块名称，用于类名大驼峰                        | `Example`         |
| `{MODULE}`  | `{Module}` 的大写形式，用于引用 TableDef 静态常量 | `EXAMPLE`         |
| `{name}`    | 中文功能名称，用于 Swagger 文档注释              | `示例`              |
