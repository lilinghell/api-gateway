package com.hell.config.template;

import com.hell.core.interceptor.BaseTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TemplateRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private TemplateProperties templateProperties;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        if(null != templateProperties) {
            List<TemplateDefinition> ts = templateProperties.getTemplates();
            for (TemplateDefinition templateDefinition : ts) {
                registryTemplate(beanDefinitionRegistry, templateDefinition);
            }
        }
    }

    private void registryTemplate(BeanDefinitionRegistry beanDefinitionRegistry, TemplateDefinition templateDefinition) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        if (null == templateDefinition.getTemplateClassName() || "".equals(templateDefinition.getTemplateClassName())) {
            beanDefinition.setBeanClass(BaseTemplate.class);
        } else {
            beanDefinition.setBeanClassName(templateDefinition.getTemplateClassName());
        }
        beanDefinition.getPropertyValues().add("commandsPre", templateDefinition.getCommandsPre());
        beanDefinition.getPropertyValues().add("commandsPost", templateDefinition.getCommandsPost());
        beanDefinition.getPropertyValues().add("commandsAfter", templateDefinition.getCommandsAfter());
        beanDefinition.getPropertyValues().add("pathPatterns", templateDefinition.getPathPatterns());
        beanDefinition.getPropertyValues().add("excludePathPatterns", templateDefinition.getExcludePathPatterns());
        beanDefinitionRegistry.registerBeanDefinition("Template" + templateDefinition.getId(), beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {

        BindResult<TemplateProperties> bindResult = Binder.get(environment).bind("com.hell", TemplateProperties.class);
        if(bindResult.isBound()) {
            templateProperties = bindResult.get();
        }
    }
}
