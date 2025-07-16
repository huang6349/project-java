package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.Tenant;
import org.huangyalong.modules.system.domain.TenantExtras;
import org.huangyalong.modules.system.enums.TenantCategory;
import org.huangyalong.modules.system.enums.TenantStatus;
import org.huangyalong.modules.system.request.TenantUtil;
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
class TenantControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @Order(1)
    @Test
    void add() {
        var beforeSize = Tenant.create()
                .count();
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
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = TenantUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(TenantUtil.DEFAULT_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getCategory())
                .isEqualTo(TenantCategory.TYPE1);
        assertThat(testEntity.getAddress())
                .isEqualTo(TenantUtil.DEFAULT_ADDRESS);
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .hasSize(3);
        assertThat(testEntity.getDesc())
                .isEqualTo(TenantUtil.DEFAULT_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(TenantStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(3);
        assertThat(testExtras.getByPath(TenantExtras.NAME_ABBR))
                .isEqualTo(TenantUtil.DEFAULT_ABBR);
        assertThat(testExtras.getByPath(TenantExtras.NAME_AREA))
                .isEqualTo(TenantUtil.DEFAULT_AREA);
    }

    @Order(2)
    @Test
    void update() {
        var beforeSize = Tenant.create()
                .count();
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
        testClient.put()
                .uri("/tenant")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(TenantUtil.createBO(TenantUtil.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
        var testEntity = TenantUtil.getEntity();
        assertThat(testEntity)
                .isNotNull();
        assertThat(testEntity.getId())
                .isNotNull();
        assertThat(testEntity.getName())
                .isEqualTo(TenantUtil.UPDATED_NAME);
        assertThat(testEntity.getCode())
                .isNotNull();
        assertThat(testEntity.getCategory())
                .isEqualTo(TenantCategory.TYPE2);
        assertThat(testEntity.getAddress())
                .isEqualTo(TenantUtil.UPDATED_ADDRESS);
        assertThat(testEntity.getConfigs())
                .isNull();
        assertThat(testEntity.getExtras())
                .hasSize(3);
        assertThat(testEntity.getDesc())
                .isEqualTo(TenantUtil.UPDATED_DESC);
        assertThat(testEntity.getStatus())
                .isEqualTo(TenantStatus.TYPE0);
        var extras = testEntity.getExtras();
        var testExtras = JSONUtil.parseObj(extras);
        assertThat(testExtras.getRaw())
                .isNotNull();
        assertThat(testExtras.getRaw())
                .hasSize(3);
        assertThat(testExtras.getByPath(TenantExtras.NAME_ABBR))
                .isEqualTo(TenantUtil.UPDATED_ABBR);
        assertThat(testExtras.getByPath(TenantExtras.NAME_AREA))
                .isEqualTo(TenantUtil.UPDATED_AREA);
    }

    @Order(3)
    @Test
    void queryPage() {
        var beforeSize = Tenant.create()
                .count();
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
        testClient.get()
                .uri("/tenant/_query/paging?pageSize={pageSize}", Long.MAX_VALUE)
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.list.[0].name")
                .value(is(TenantUtil.DEFAULT_NAME))
                .jsonPath("$.data.list.[0].category")
                .value(is(TenantUtil.DEFAULT_CATEGORY))
                .jsonPath("$.data.list.[0].address")
                .value(is(TenantUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.list.[0].abbr")
                .value(is(TenantUtil.DEFAULT_ABBR))
                .jsonPath("$.data.list.[0].area")
                .value(is(TenantUtil.DEFAULT_AREA))
                .jsonPath("$.data.list.[0].desc")
                .value(is(TenantUtil.DEFAULT_DESC))
                .jsonPath("$.data.list.[0].status")
                .value(is(TenantStatus.TYPE0.getValue()));
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(4)
    @Test
    void query() {
        var beforeSize = Tenant.create()
                .count();
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
        testClient.get()
                .uri("/tenant/_query")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.[0].name")
                .value(is(TenantUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].category")
                .value(is(TenantUtil.DEFAULT_CATEGORY))
                .jsonPath("$.data.[0].address")
                .value(is(TenantUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.[0].abbr")
                .value(is(TenantUtil.DEFAULT_ABBR))
                .jsonPath("$.data.[0].area")
                .value(is(TenantUtil.DEFAULT_AREA))
                .jsonPath("$.data.[0].desc")
                .value(is(TenantUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(TenantStatus.TYPE0.getValue()));
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(5)
    @Test
    void getById() {
        var beforeSize = Tenant.create()
                .count();
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
        testClient.get()
                .uri("/tenant/{id:.+}", TenantUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE))
                .jsonPath("$.data.name")
                .value(is(TenantUtil.DEFAULT_NAME))
                .jsonPath("$.data.category")
                .value(is(TenantUtil.DEFAULT_CATEGORY))
                .jsonPath("$.data.address")
                .value(is(TenantUtil.DEFAULT_ADDRESS))
                .jsonPath("$.data.abbr")
                .value(is(TenantUtil.DEFAULT_ABBR))
                .jsonPath("$.data.area")
                .value(is(TenantUtil.DEFAULT_AREA))
                .jsonPath("$.data.desc")
                .value(is(TenantUtil.DEFAULT_DESC))
                .jsonPath("$.data.status")
                .value(is(TenantStatus.TYPE0.getValue()));
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }

    @Order(6)
    @Test
    void delete() {
        var beforeSize = Tenant.create()
                .count();
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
        testClient.delete()
                .uri("/tenant/{id:.+}", TenantUtil.getId())
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var afterSize = Tenant.create()
                .count();
        assertThat(beforeSize)
                .isEqualTo(afterSize);
    }
}
