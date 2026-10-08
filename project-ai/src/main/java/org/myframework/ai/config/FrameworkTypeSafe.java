package org.myframework.ai.config;

import org.myframework.ai.helper.TypeSafeHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TypeSafeHelper.class)
public class FrameworkTypeSafe {
}
