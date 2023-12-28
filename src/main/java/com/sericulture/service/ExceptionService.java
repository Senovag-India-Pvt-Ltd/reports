package com.sericulture.service;


import com.sericulture.controller.Response;
import com.sericulture.exception.EmptyInputException;
import com.sericulture.model.UseMe;
import com.sericulture.repository.UsemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExceptionService {
    @Autowired
    UsemeRepository usemeRepository;

    public Response getUseme() {
        List<UseMe> useMeList = (List<UseMe>) usemeRepository.findAll();
        Response<List<UseMe>> response = new Response<>();
        response.setResponse(useMeList);
        return response;
    }

    public Response saveUseme(UseMe useMe) {
        if (useMe.getDescription() == null || useMe.getDescription().isEmpty()) {
            throw new EmptyInputException("100", "please provide the description");
        }
        UseMe useme = usemeRepository.save(useMe);
        return new Response<UseMe>(useme);
    }
}
