package org.huangyalong.modules.system.web;

import cn.dev33.satoken.stp.StpUtil;
import org.huangyalong.core.IntegrationTest;
import org.huangyalong.modules.system.domain.PermAssoc;
import org.huangyalong.modules.system.domain.Role;
import org.huangyalong.modules.system.enums.PermStatus;
import org.huangyalong.modules.system.request.PermUtil;
import org.huangyalong.modules.system.request.RolePermUtil;
import org.huangyalong.modules.system.request.RoleUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.huangyalong.modules.system.domain.table.RoleTableDef.ROLE;

@AutoConfigureMockMvc
@IntegrationTest
class RolePermControllerTest extends MyFrameworkTest {

    @Autowired
    WebTestClient testClient;

    @BeforeEach
    void initTest() {
        testClient.post()
                .uri("/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PermUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        testClient.post()
                .uri("/role")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RoleUtil.createBO())
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
    void assoc() {
        var beforeSize = PermAssoc.create()
                .count();
        testClient.patch()
                .uri("/role/perm")
                .header(StpUtil.getTokenName(), StpUtil.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(RolePermUtil.createBO())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.success")
                .value(is(Boolean.TRUE));
        var role = Role.create()
                .orderBy(ROLE.ID, Boolean.FALSE)
                .one();
        assertThat(role)
                .isNotNull();
        assertThat(role.getId())
                .isNotNull();
        testClient.get()
                .uri("/role/perm/{id:.+}", role.getId())
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
                .value(is(PermUtil.DEFAULT_NAME))
                .jsonPath("$.data.[0].code")
                .value(is(PermUtil.DEFAULT_CODE))
                .jsonPath("$.data.[0].desc")
                .value(is(PermUtil.DEFAULT_DESC))
                .jsonPath("$.data.[0].status")
                .value(is(PermStatus.TYPE0.getValue()));
        var afterSize = PermAssoc.create()
                .count();
        assertThat(beforeSize + 1)
                .isEqualTo(afterSize);
    }
}
