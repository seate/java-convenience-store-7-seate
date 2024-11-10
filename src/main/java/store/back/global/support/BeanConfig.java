package store.back.global.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanConfig {

    private static final Map<Class<?>, Class<?>> interfaceImplementation = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<>();

    private static final Set<Class<?>> currentRegisterLog = new HashSet<>(); // 순환 참조 검사를 위한 Set 객체


    public static void clear() {
        interfaceImplementation.clear();
        beanContainer.clear();
        currentRegisterLog.clear();
    }

    public static void registerBeanContainer(Set<Class<?>> classesToBeBeans) {
        classesToBeBeans.forEach(clazz -> {
            if (interfaceImplementation.containsKey(clazz)) {
                throw new IllegalArgumentException(String.format("%s에 해당하는 bean 설정이 이미 존재합니다.", clazz));
            }

            interfaceImplementation.put(clazz, clazz);
            findInterfacesRecursively(clazz).forEach(myInterface -> interfaceImplementation.put(myInterface, clazz));
        });

        interfaceImplementation.keySet().forEach(BeanConfig::registerBean);
    }

    private static Set<Class<?>> findInterfacesRecursively(final Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces())
                .flatMap(myInterface -> {
                    Set<Class<?>> superInterfaces = findInterfacesRecursively(myInterface);
                    superInterfaces.add(myInterface);

                    return superInterfaces.stream();
                }).collect(Collectors.toSet());
    }

    // bean을 생성하고 등록
    private static void registerBean(final Class<?> abstractClass) {
        if (!isRegistrable(abstractClass)) {
            return;
        }
        currentRegisterLog.add(abstractClass);

        if (!interfaceImplementation.containsKey(abstractClass)) {
            throw new IllegalStateException(String.format("%s에 해당하는 bean 설정이 존재하지 않습니다.", abstractClass));
        }
        Class<?> specificClass = interfaceImplementation.get(abstractClass);
        putToBeanContainer(abstractClass, specificClass);
    }

    private static Boolean isRegistrable(final Class<?> clazz) {
        if (beanContainer.containsKey(clazz)) {
            return Boolean.FALSE;
        }
        if (currentRegisterLog.contains(clazz)) {
            throw new IllegalStateException("순환 참조가 발생했습니다.");
        }

        return Boolean.TRUE;
    }

    private static void putToBeanContainer(final Class<?> abstractClass, final Class<?> specificClass) {
        Constructor<?> constructor = specificClass.getConstructors()[0]; // 현재는 하나의 생성자만 허용
        List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();
        List<Object> dependencies = getDependencies(parameterTypes);
        try {
            beanContainer.put(abstractClass, constructor.newInstance(dependencies.toArray()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        currentRegisterLog.remove(abstractClass);
    }

    private static List<Object> getDependencies(List<Class<?>> parameterTypes) {
        return parameterTypes.stream().map(dependency -> {
            if (!beanContainer.containsKey(dependency)) {
                registerBean(dependency);
            }
            return beanContainer.get(dependency);
        }).toList();
    }


    public static <T> T getBean(final Class<T> clazz) {
        Class<?> implementClass = interfaceImplementation.get(clazz);
        if (implementClass == null) {
            throw new IllegalArgumentException(String.format("%s의 bean이 존재하지 않습니다.", clazz.getName()));
        }
        Object beanObject = beanContainer.get(implementClass);
        if (beanObject != null) {
            return clazz.cast(beanObject);
        }

        throw new IllegalArgumentException(String.format("%s의 bean이 존재하지 않습니다.", clazz.getName()));
    }
}
