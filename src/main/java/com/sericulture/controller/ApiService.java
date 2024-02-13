package com.sericulture.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
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
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MarketAuctionForPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        MarketAuctionForPrintResponse response = new MarketAuctionForPrintResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ContentRoot response2 = objectMapper.readValue(response1, ContentRoot.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public DTRReportResponse dtrReport(DTROnlineRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDTROnlineReport";

         String finalapiurl = apiUrl + "auction/report/getDTROnlineReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<DTROnlineRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        DTRReportResponse response = new DTRReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DTRReportResponse response2 = objectMapper.readValue(response1, DTRReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
}

