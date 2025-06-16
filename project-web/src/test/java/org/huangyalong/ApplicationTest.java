package org.huangyalong;

import org.huangyalong.core.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.myframework.test.MyFrameworkTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
@IntegrationTest
class ApplicationTest extends MyFrameworkTest {

    @Test
    void contextLoads() {
    }
}
