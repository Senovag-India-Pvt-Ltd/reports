package com.sericulture.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.model.ContentRoot;
import com.sericulture.model.MarketAuctionForPrintRequest;
import com.sericulture.model.MarketAuctionForPrintResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@Service
public class ApiService {

    private final RestTemplate restTemplate;

    @Value("${marketapi.url}")
    private String apiUrl;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public ContentRoot fetchDataFromApi(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = apiUrl + "auction/print/getPrintableDataForLot";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MarketAuctionForPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        MarketAuctionForPrintResponse response = new MarketAuctionForPrintResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ContentRoot response2 = objectMapper.readValue(response1, ContentRoot.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
}

