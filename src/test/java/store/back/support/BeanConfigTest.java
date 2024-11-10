package store.back.support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.back.global.support.ApplicationContext;
import store.back.global.support.BeanConfig;

class BeanConfigTest {

    @BeforeAll
    static void setUp() {
        ApplicationContext.init();
    }

    @ParameterizedTest
    @ValueSource(classes = {}) //TODO bean으로 등록할 클래스 추가 시 추가
    void 빈_생성_테스트(Class<?> beanClass) {
        // given
        Object bean = BeanConfig.getBean(beanClass);

        // when, then
        Assertions.assertThat(bean)
                .isNotNull()
                .isInstanceOf(beanClass);
    }
}