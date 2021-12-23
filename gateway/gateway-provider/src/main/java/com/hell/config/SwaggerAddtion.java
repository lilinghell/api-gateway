package com.hell.config;

import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Component
public class SwaggerAddtion implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        return new ArrayList<>(Arrays.asList(
                new ApiDescription("1",
                        "/api/qry",
                        "2",
                        "3",
                        Arrays.asList(
                                new OperationBuilder(
                                        new CachingOperationNameGenerator())
                                        .method(HttpMethod.POST)
                                        .produces(Sets.newHashSet(MediaType.ALL_VALUE))
                                        .summary("4")
                                        .notes("5")
                                        .tags(Sets.newHashSet("6"))
                                        .build()),
                        false)));
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
