package com.fiberhome.mapps.intergration.mybatis;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.spring.mapper.MapperFactoryBean;

public class MyMapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
	private ResourceLoader resourceLoader;
	private MapperHelper mapperHelper = new MapperHelper();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		AnnotationAttributes annoAttrs = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(MyMapperScan.class.getName()));
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

		// this check is needed in Spring 3.1
		if (resourceLoader != null) {
			scanner.setResourceLoader(resourceLoader);
		}

		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
		if (!Annotation.class.equals(annotationClass)) {
			scanner.setAnnotationClass(annotationClass);
		}

		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
		if (!Class.class.equals(markerInterface)) {
			scanner.setMarkerInterface(markerInterface);
		}
		if (Marker.class.isAssignableFrom(markerInterface)) {
			mapperHelper.registerMapper(markerInterface);
		}

		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
		if (!BeanNameGenerator.class.equals(generatorClass)) {
			scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
		}

		Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
		if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
			scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
		}

		scanner.setSqlSessionTemplateBeanName(annoAttrs.getString("sqlSessionTemplateRef"));
		scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));

		List<String> basePackages = new ArrayList<String>();
		for (String pkg : annoAttrs.getStringArray("value")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (String pkg : annoAttrs.getStringArray("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}
		scanner.registerFilters();
		scanner.doScan(StringUtils.toStringArray(basePackages));

		// 如果没有注册过接口，就注册默认的Mapper接口
		this.mapperHelper.ifEmptyRegisterDefaultInterface();
		String[] names = registry.getBeanDefinitionNames();
		GenericBeanDefinition definition;
		for (String name : names) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(name);
			if (beanDefinition instanceof GenericBeanDefinition) {
				definition = (GenericBeanDefinition) beanDefinition;
				if (StringUtil.isNotEmpty(definition.getBeanClassName())
						&& definition.getBeanClassName().equals("org.mybatis.spring.mapper.MapperFactoryBean")) {
					definition.setBeanClass(MapperFactoryBean.class);
					definition.getPropertyValues().add("mapperHelper", this.mapperHelper);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
