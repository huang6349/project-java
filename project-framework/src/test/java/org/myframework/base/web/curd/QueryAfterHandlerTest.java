package org.myframework.base.web.curd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.myframework.base.response.PageVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link QueryAfterHandler} 单元测试。
 *
 * <p>测试默认实现透传，以及自定义覆写后的处理逻辑。
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueryAfterHandlerTest {

    private static final String ENTITY_A = "Entity-A";
    private static final String ENTITY_B = "Entity-B";
    private static final String ENTITY_C = "Entity-C";

    // ========== 默认实现测试 ==========

    @Order(0)
    @Test
    @DisplayName("handlerAfter 默认实现：null 输入返回空 Mono")
    void handlerAfter_defaultNull() {
        var handler = new QueryAfterHandler<String>() {};
        var result = handler.handlerAfter(null).block();
        assertThat(result).isNull();
    }

    @Order(1)
    @Test
    @DisplayName("handlerAfter 默认实现：非 null 输入原样透传")
    void handlerAfter_defaultPassthrough() {
        var handler = new QueryAfterHandler<String>() {};
        var result = handler.handlerAfter(ENTITY_A).block();
        assertThat(result).isEqualTo(ENTITY_A);
    }

    @Order(2)
    @Test
    @DisplayName("handlerAfterList 默认实现：空列表透传")
    void handlerAfterList_defaultEmpty() {
        var handler = new QueryAfterHandler<String>() {};
        var list = List.<String>of();
        var result = handler.handlerAfterList(Mono.just(list)).block();
        assertThat(result).isEmpty();
    }

    @Order(3)
    @Test
    @DisplayName("handlerAfterList 默认实现：多元素列表原样透传")
    void handlerAfterList_defaultPassthrough() {
        var handler = new QueryAfterHandler<String>() {};
        var list = Arrays.asList(ENTITY_A, ENTITY_B, ENTITY_C);
        var result = handler.handlerAfterList(Mono.just(list)).block();
        assertThat(result).containsExactly(ENTITY_A, ENTITY_B, ENTITY_C);
    }

    @Order(4)
    @Test
    @DisplayName("handlerAfterPage 默认实现：空分页透传")
    void handlerAfterPage_defaultEmptyPage() {
        var handler = new QueryAfterHandler<String>() {};
        var page = new PageVO<String>().setList(List.of()).setTotal(0L);
        var result = handler.handlerAfterPage(Mono.just(page)).block();
        assertThat(result.getList()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0L);
    }

    @Order(5)
    @Test
    @DisplayName("handlerAfterPage 默认实现：分页内列表原样透传")
    void handlerAfterPage_defaultPassthrough() {
        var handler = new QueryAfterHandler<String>() {};
        var page = new PageVO<String>()
                .setList(Arrays.asList(ENTITY_A, ENTITY_B))
                .setTotal(2L);
        var result = handler.handlerAfterPage(Mono.just(page)).block();
        assertThat(result.getList()).containsExactly(ENTITY_A, ENTITY_B);
        assertThat(result.getTotal()).isEqualTo(2L);
    }

    // ========== 自定义覆写测试 ==========

    @Order(10)
    @Test
    @DisplayName("handlerAfter 自定义：追加后缀")
    void handlerAfter_customSuffix() {
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<String> handlerAfter(String entity) {
                return Mono.justOrEmpty(entity)
                        .map(s -> s + "_processed");
            }
        };
        assertThat(handler.handlerAfter(ENTITY_A).block())
                .isEqualTo("Entity-A_processed");
    }

    @Order(11)
    @Test
    @DisplayName("handlerAfterList 自定义：批量转大写")
    void handlerAfterList_customUpperCase() {
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<List<String>> handlerAfterList(Mono<List<String>> entitiesMono) {
                return entitiesMono.map(list -> list.stream()
                        .map(String::toUpperCase)
                        .toList());
            }
        };
        var result = handler.handlerAfterList(Mono.just(Arrays.asList(ENTITY_A, ENTITY_B))).block();
        assertThat(result).containsExactly("ENTITY-A", "ENTITY-B");
    }

    @Order(12)
    @Test
    @DisplayName("handlerAfterList 自定义：追加合计行")
    void handlerAfterList_customAppendTotal() {
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<List<String>> handlerAfterList(Mono<List<String>> entitiesMono) {
                return entitiesMono.map(list -> {
                    var mutable = new java.util.ArrayList<>(list);
                    mutable.add("TOTAL");
                    return mutable;
                });
            }
        };
        var result = handler.handlerAfterList(Mono.just(Arrays.asList(ENTITY_A, ENTITY_B))).block();
        assertThat(result).containsExactly(ENTITY_A, ENTITY_B, "TOTAL");
    }

    @Order(13)
    @Test
    @DisplayName("handlerAfterList 自定义：过滤空值")
    void handlerAfterList_customFilter() {
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<List<String>> handlerAfterList(Mono<List<String>> entitiesMono) {
                return entitiesMono.map(list -> list.stream()
                        .filter(s -> s != null && !s.isEmpty())
                        .toList());
            }
        };
        var result = handler.handlerAfterList(Mono.just(Arrays.asList(ENTITY_A, "", null, ENTITY_C))).block();
        assertThat(result).containsExactly(ENTITY_A, ENTITY_C);
    }

    @Order(14)
    @Test
    @DisplayName("handlerAfterPage 自定义：追加合计行（覆写整个方法）")
    void handlerAfterPage_customAppendTotal() {
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<PageVO<String>> handlerAfterPage(Mono<PageVO<String>> pageMono) {
                return pageMono.flatMap(page -> {
                    var mutable = new java.util.ArrayList<>(page.getList());
                    mutable.add("TOTAL");
                    page.setList(mutable);
                    return Mono.just(page);
                });
            }
        };
        var page = new PageVO<String>()
                .setList(Arrays.asList(ENTITY_A, ENTITY_B))
                .setTotal(2L);
        var result = handler.handlerAfterPage(Mono.just(page)).block();
        assertThat(result.getList()).containsExactly(ENTITY_A, ENTITY_B, "TOTAL");
        assertThat(result.getTotal()).isEqualTo(2L);
    }

    @Order(15)
    @Test
    @DisplayName("handlerAfterPage 使用默认实现时，内部调用 handlerAfterList（集成场景）")
    void handlerAfterPage_viaHandlerAfterList() {
        // 场景：只想用 handlerAfterList 统一处理列表，分页透传
        var handler = new QueryAfterHandler<String>() {
            @Override
            public Mono<List<String>> handlerAfterList(Mono<List<String>> entitiesMono) {
                // 统一追加 "_tag"
                return entitiesMono.map(list -> list.stream()
                        .map(s -> s + "_tag")
                        .toList());
            }
        };
        var page = new PageVO<String>()
                .setList(Arrays.asList(ENTITY_A, ENTITY_B))
                .setTotal(2L);
        var result = handler.handlerAfterPage(Mono.just(page)).block();
        assertThat(result.getList()).containsExactly("Entity-A_tag", "Entity-B_tag");
    }

    @Order(16)
    @Test
    @DisplayName("完整链路模拟：handlerAfterList 批量查关联角色")
    void fullChain_batchRoleLookup() {
        // 模拟场景：查询用户时补全角色名称
        record User(Long id, String name, Long roleId, String roleName) {}

        var handler = new QueryAfterHandler<User>() {
            @Override
            public Mono<List<User>> handlerAfterList(Mono<List<User>> entitiesMono) {
                return entitiesMono.flatMapMany(Flux::fromIterable)
                        .collectList()
                        .flatMap(users -> {
                            if (users.isEmpty()) {
                                return Mono.just(List.of());
                            }
                            // 模拟批量查关联角色
                            var roleIds = users.stream()
                                    .map(User::roleId)
                                    .distinct()
                                    .toList();
                            // 模拟 roleService.listByIds(roleIds)
                            Map<Long, String> roleMap = roleIds.stream()
                                    .collect(Collectors.toMap(id -> id, id -> "Role-" + id));

                            var enriched = users.stream()
                                    .map(u -> new User(u.id(), u.name(), u.roleId(),
                                            roleMap.getOrDefault(u.roleId(), "未知")))
                                    .toList();
                            return Mono.just(enriched);
                        });
            }
        };

        var users = Arrays.asList(
                new User(1L, "Alice", 10L, null),
                new User(2L, "Bob", 20L, null)
        );
        var result = handler.handlerAfterList(Mono.just(users)).block();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).roleName()).isEqualTo("Role-10");
        assertThat(result.get(1).roleName()).isEqualTo("Role-20");
    }
}
