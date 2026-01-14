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
        {Module}BO> {

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
\n
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
\n
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

@Service
public class {Module}ServiceImpl extends ReactorServiceImpl<{Module}Mapper, {Module}> implements {Module}Service {

    @Transactional(rollbackFor = Exception.class)
    public Mono<Boolean> add({Module}BO {module}BO) {
        var data = {Module}.create()
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
\n
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
\n
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
\n
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
\n
```

### 测试层

```text
// {Module}ControllerTest.java
package {package}.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import {package}.domain.{Module};
import {package}.request.{Module}Util;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@IntegrationTest
class {Module}ControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = {Module}Util.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getDesc())
                .isEqualTo({Module}Util.DEFAULT_DESC);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO({Module}Util.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = {Module}Util.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getDesc())
                .isEqualTo({Module}Util.UPDATED_DESC);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/{module}/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].desc")
                .value(is({Module}Util.DEFAULT_DESC));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/{module}/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].desc")
                .value(is({Module}Util.DEFAULT_DESC));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/{module}/{id:.+}", {Module}Util.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.desc")
                .value(is({Module}Util.DEFAULT_DESC));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = {Module}.create()
                .count();
        testClient.post()
                .uri("/{module}")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue({Module}Util.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/{module}/{id:.+}", {Module}Util.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = {Module}.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}
\n
```

```text
// {Module}Util.java
package {package}.request;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import {package}.domain.{Module};

import java.io.Serializable;

import static {package}.domain.table.{Module}TableDef.{MODULE};

public interface {Module}Util {

    String DEFAULT_DESC = RandomUtil.randomString(12);

    String UPDATED_DESC = RandomUtil.randomString(12);

    static {Module}BO createBO(JSONObject object) {
        var {module}BO = new {Module}BO();
        {module}BO.setId(object.getLong("id"));
        {module}BO.setDesc(object.getStr("desc", DEFAULT_DESC));
        return {module}BO;
    }

    static {Module}BO createBO(Serializable id) {
        var obj = JSONUtil.createObj()
                .set("id", id)
                .set("desc", UPDATED_DESC);
        return createBO(obj);
    }

    static {Module}BO createBO() {
        var obj = JSONUtil.createObj();
        return createBO(obj);
    }

    static {Module} getEntity() {
        return {Module}.create()
                .orderBy({MODULE}.ID, Boolean.FALSE)
                .one();
    }

    static Long getId() {
        var entity = getEntity();
        return Opt.ofNullable(entity)
                .map({Module}::getId)
                .get();
    }
}
\n
```

---

## 变量替换速查表

| 变量          | 使用场景                                          | 示例        |
|-------------|-----------------------------------------------|-----------|
| `{package}` | 包路径：`package` 声明、import 语句                    | -         |
| `{Module}`  | 类名大驼峰：Controller、Service、Entity 等类名           | `Example` |
| `{module}`  | 模块名：`@RequestMapping`、`@Table` 表名前缀、类名小驼峰     | `example` |
| `{MODULE}`  | TableDef 静态常量：`import static`、查询条件字段引用        | `EXAMPLE` |
| `{name}`    | 中文名称：Swagger `@Tag`、`@AutoTable`、`@Schema` 注释 | `示例`      |
