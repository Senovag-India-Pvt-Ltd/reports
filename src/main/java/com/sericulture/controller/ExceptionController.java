package com.sericulture.controller;


import com.sericulture.model.UseMe;
import com.sericulture.service.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @Autowired
    ExceptionService exceptionService;

    @GetMapping("/getAll")
    public Response getUseme(){

        return exceptionService.getUseme();

    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUseMe(@RequestBody UseMe useme){

        Response<UseMe> response = exceptionService.saveUseme(useme);
        response.setErrorMessage("save success");

        return new ResponseEntity<Response>(response,HttpStatus.CREATED);

    }
}
