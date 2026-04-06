package com.finance.dataprocessing.documentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


@RestController
public class RedocController {

    @GetMapping(value = "/redoc", produces = MediaType.TEXT_HTML_VALUE)
    public String redoc() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>ReDoc - Finance API</title>" +
                "<meta charset=\"utf-8\"/>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "</head>" +
                "<body>" +
                "<redoc spec-url='/v3/api-docs'></redoc>" +
                "<script src='https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js'></script>" +
                "</body>" +
                "</html>";
    }
}
