package com.example.freelance_authserver.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SpaForwardingConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Serve the Vite build normally from classpath:/static/
		registry.addResourceHandler(
						"/",              // root
						"/index.html"
				)
				.addResourceLocations("classpath:/static");

		// SPA fallback: anything else under / that isn't an actual file -> index.html
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.resourceChain(true)
				.addResolver(new SpaFallbackResourceResolver());
	}

	static class SpaFallbackResourceResolver extends PathResourceResolver {
		@Override
		protected Resource getResource(String resourcePath, Resource location) throws IOException {
			// If the requested resource exists, serve it
			Resource requested = location.createRelative(resourcePath);
			if (requested.exists() && requested.isReadable()) {
				return requested;
			}
			// Don't swallow API calls — let them hit controllers (returning null continues handler chain)
			if (resourcePath.startsWith("api/")) {
				return null;
			}
			// Otherwise, fall back to the SPA entry point
			return location.createRelative("index.html");
		}
	}
}
