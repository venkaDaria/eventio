package com.example.venka.eventio.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

/**
 * Spring Template Engine configuration
 */
@Configuration
class TemplateEngineConfiguration {

    @Bean
    fun templateResolver(): ITemplateResolver =
            with(ClassLoaderTemplateResolver()) {
                prefix = "templates/"
                suffix = ".html"
                templateMode = TemplateMode.HTML
                order = 1
                isCacheable = true

                return this
            }

    @Bean
    fun templateEngine(templateResolver: ITemplateResolver): SpringTemplateEngine =
            with(SpringTemplateEngine()) {
                addTemplateResolver(templateResolver)

                return this
            }
}
