package co.cellarcollective.tools.dpd.config;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@AllArgsConstructor
public class SwaggerConfig {

    private final BuildProperties buildProperties;

    @Bean
    public Docket swaggerPersonApi10() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("co.cellarcollective.tools.dpd.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .protocols(Sets.newHashSet("HTTPS"))
                .apiInfo(new ApiInfoBuilder()
                        .version(buildProperties.getVersion())
                        .title("DPD Emulator API")
                        .description("DPD emulator service is responsible for emulating tracking events for Cellar Collective platform.")
                        .build());
    }
}
