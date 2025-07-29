package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.TenantAssoc;
import org.huangyalong.modules.system.enums.UserStatus;
import org.huangyalong.modules.system.request.TenantUserUtil;
import org.huangyalong.modules.system.request.TenantUtil;
import org.huangyalong.modules.system.request.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.core.enums.AssocCategory;
import org.myframework.core.enums.TimeEffective;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.huangyalong.modules.system.domain.table.UserTableDef.USER;

@AutoConfigureMockMvc
@IntegrationTest
class TenantUserControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/tenant")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.post()
                .uri("/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
    }

    @Order(1)
    @Test
    void add() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = TenantUserUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getTenantId())
                .isEqualTo(TenantUtil.getId());
        assertThat(testEntity.getAssoc())
                .isEqualTo(USER.getTableName());
        assertThat(testEntity.getAssocId())
                .isEqualTo(UserUtil.getId());
        assertThat(testEntity.getEffective())
                .isEqualTo(TimeEffective.TYPE0);
        assertThat(testEntity.getEffectiveTime())
                .isNull();
        assertThat(testEntity.getCategory())
                .isEqualTo(AssocCategory.TYPE0);
        assertThat(testEntity.getDesc())
                .isEqualTo(TenantUserUtil.DEFAULT_DESC);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.put()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO(TenantUserUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = TenantUserUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getTenantId())
                .isEqualTo(TenantUtil.getId());
        assertThat(testEntity.getAssoc())
                .isEqualTo(USER.getTableName());
        assertThat(testEntity.getAssocId())
                .isEqualTo(UserUtil.getId());
        assertThat(testEntity.getEffective())
                .isEqualTo(TimeEffective.TYPE0);
        assertThat(testEntity.getEffectiveTime())
                .isNull();
        assertThat(testEntity.getCategory())
                .isEqualTo(AssocCategory.TYPE0);
        assertThat(testEntity.getDesc())
                .isEqualTo(TenantUserUtil.UPDATED_DESC);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/tenant/user/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].username")
                .value(is(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.list.[0].nickname")
                .value(is(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.list.[0].gender")
                .value(is(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.list.[0].mobile")
                .value(is(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.list.[0].email")
                .value(is(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.list.[0].address")
                .value(is(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.list.[0].desc")
                .value(is(TenantUserUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(UserStatus.TYPE0.getValue()));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/tenant/user/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].username")
                .value(is(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.[0].nickname")
                .value(is(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.[0].gender")
                .value(is(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.[0].mobile")
                .value(is(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.[0].email")
                .value(is(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.[0].address")
                .value(is(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.[0].desc")
                .value(is(TenantUserUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(UserStatus.TYPE0.getValue()));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getAssocById() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/tenant/user/{id:.+}/_assoc", TenantUserUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.tenantId")
                .value(is(TenantUtil.getIdAsString()))
                .jsonPath("$.data.userId")
                .value(is(UserUtil.getIdAsString()))
                .jsonPath("$.data.desc");
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void getById() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.get()
                .uri("/tenant/user/{id:.+}", TenantUserUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.username")
                .value(is(UserUtil.DEFAULT_USERNAME))
                .jsonPath("$.data.nickname")
                .value(is(UserUtil.DEFAULT_NICKNAME))
                .jsonPath("$.data.gender")
                .value(is(UserUtil.DEFAULT_GENDER))
                .jsonPath("$.data.mobile")
                .value(is(UserUtil.DEFAULT_MOBILE))
                .jsonPath("$.data.email")
                .value(is(UserUtil.DEFAULT_EMAIL))
                .jsonPath("$.data.address")
                .value(is(UserUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.desc")
                .value(is(TenantUserUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(UserStatus.TYPE0.getValue()));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(7)
    @Test
    void delete() {
        var beforeSize = TenantAssoc.create()
                .count();
        testClient.post()
                .uri("/tenant/user")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUserUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.delete()
                .uri("/tenant/user/{id:.+}", TenantUserUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = TenantAssoc.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}
