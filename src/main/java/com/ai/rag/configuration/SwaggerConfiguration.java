package com.ai.rag.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.title}") final String title,
            @Value("${openapi.version}") final String version,
            @Value("${openapi.description}") final String description,
            @Value("${openapi.termsOfService:@null}") final String termsOfService,
            @Value("${openapi.contact.email:@null}") final String email,
            @Value("${openapi.contact.name:@null}") final String name,
            @Value("${openapi.contact.url:@null}") final String url,
            @Value("${openapi.license.name:@null}") final String licenseName,
            @Value("${openapi.license.url:@null}") final String licenseUrl
    ) {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(title)
                                .version(version)
                                .description(description)
                                .termsOfService(termsOfService)
                                .license(
                                        getLicense(
                                                licenseName,
                                                licenseUrl
                                        )
                                )
                                .contact(
                                        getContact(
                                                email,
                                                name,
                                                url
                                        )
                                )
                );
    }

    private Contact getContact(
            final String email,
            final String name,
            final String url
    ) {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setName(name);
        contact.setUrl(url);
        contact.setExtensions(Collections.emptyMap());
        return contact;
    }

    private License getLicense(
            final String licenseName,
            final String licenseUrl
    ) {
        License license = new License();
        license.setName(licenseName);
        license.setUrl(licenseUrl);
        return license;
    }

}