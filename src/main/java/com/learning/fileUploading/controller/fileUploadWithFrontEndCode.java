package com.learning.fileUploading.controller;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class fileUploadWithFrontEndCode
{
    @GetMapping("/files")
    ModelAndView fileUpload()

    {
        Model m = new ExtendedModelMap();
        return new ModelAndView("index.html");
    }
}
