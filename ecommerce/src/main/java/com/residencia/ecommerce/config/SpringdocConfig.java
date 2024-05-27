package com.residencia.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SpringdocConfig {

	private final String apiTitle;
	private final String apiVersion;
	private final String apiDescription;
	private final String apiLicense;
	private final String apiTag;
	private final String apiUrl;
	private final String apiExternalDescription;
	private final String apiExternalUrl;

	public SpringdocConfig(@Value("${api-title}") String apiTitle, @Value("${api-version}") String apiVersion,
			@Value("${api-description}") String apiDescription, @Value("${api-license}") String apiLicense,
			@Value("${api-tag}") String apiTag, @Value("${api-url}") String apiUrl,
			@Value("${api-external-description}") String apiExternalDescription,
			@Value("${api-external-url}") String apiExternalUrl) {
		this.apiTitle = apiTitle;
		this.apiVersion = apiVersion;
		this.apiDescription = apiDescription;
		this.apiLicense = apiLicense;
		this.apiTag = apiTag;
		this.apiUrl = apiUrl;
		this.apiExternalDescription = apiExternalDescription;
		this.apiExternalUrl = apiExternalUrl;
	}

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")))
				.info(new Info().title(apiTitle).version(apiVersion).description(apiDescription)
						.license(new License().name(apiLicense).url(apiUrl)))
				.externalDocs(new ExternalDocumentation().description(apiExternalDescription).url(apiExternalUrl))
				.addTagsItem(new Tag().name(apiTag));

	}

}
