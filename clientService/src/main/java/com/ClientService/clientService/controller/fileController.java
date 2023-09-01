package com.ClientService.clientService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@RestController
public class fileController
{


    @Autowired
    RestTemplate restTemplate;


    ///download/{fileName}
    String baseUrl = "http://localhost:8081";
    @GetMapping("getAndSave/{fileName}")
    String getAndSaveFile(@PathVariable String fileName) throws IOException {
      Resource resource =  restTemplate.getForObject(baseUrl + "/download/" + fileName ,Resource.class);

      Path path = Paths.get("temp").toAbsolutePath().normalize();
        Files.createDirectories(Paths.get("temp"));

        Files.copy(resource.getInputStream(),Paths.get(path +  "\\" + fileName), StandardCopyOption.REPLACE_EXISTING);

        return "date retrieved properly";
    }
@GetMapping("/PerformTheTask")
        String performtask() throws IOException
{
    Stream<Path> list = Files.list(Paths.get("C:\\Users\\Bhimesh\\Downloads\\New folder"));

    MultiValueMap filesTOUpload = new LinkedMultiValueMap<>();

    list.forEach(
            file -> {

                try {
                    filesTOUpload.add("files",new UrlResource(file.toAbsolutePath().toUri()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
    );

    System.out.println("Hi");

    String resp = restTemplate.postForObject(baseUrl + "/multiple/upload",filesTOUpload,String.class);

    System.out.println(resp);



    return resp;
}

}
