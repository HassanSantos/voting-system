package com.dbserver.voting_system.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OpenApiYamlController {

    @GetMapping(value = "/api/v1/openapi.yaml", produces = {"application/yaml", "text/yaml", MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public Resource openApiYaml() {
        return new ClassPathResource("openapi.yaml");
    }
}
