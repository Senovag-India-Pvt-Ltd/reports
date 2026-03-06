package com.sericulture.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.AudioVisual.AudioVisualReportRequest;
import com.sericulture.model.AudioVisual.AudioVisualResponse;
import com.sericulture.model.DTRAllMarket.DTRAllMarketResponse;
import com.sericulture.model.MarketReport.MarketResponse;
import com.sericulture.model.MarketWiseReport.DivisionResponse;
import com.sericulture.model.MonthlyDistrictReport.MonthlyDistrictReport;
import com.sericulture.model.MonthlyDistrictReport.MonthlyDistrictRequest;
import com.sericulture.model.MonthlyDistrictReport.MonthlyDistrictResponse;
import com.sericulture.model.MonthlyReport.MonthlyReportRequest;
import com.sericulture.model.MonthlyReport.ReportMonthlyResponse;
import com.sericulture.model.UnitCounterReport.UnitCounterReportRequest;
import com.sericulture.model.UnitCounterReport.UnitCounterReportResponse;
import com.sericulture.model.VahivaatuReport.Report27bResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    @Value("${marketapi.url}")
    private String apiUrl;

    @Value("${dbtapi.url}")
    private String dbtApiUrl;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public ContentRoot fetchDataFromApi(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = apiUrl + "auction/print/getPrintableDataForLot";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/print/getPrintableDataForLot";

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




    public ContentRoot fetchDataFromApiSeedCocoon(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = apiUrl + "auction/print/getPrintableDataForLotForSeedCocoon";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/print/getPrintableDataForLotForSeedCocoon";

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

//    public ContentRoot fetchDataFromApiSeedCocoonTriplet(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
//        // Make a GET request to the API endpoint
////        String finalapiurl = apiUrl + "auction/print/getPrintableDataForLotForSeedCocoonTriplet";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/print/getPrintableDataForLotForSeedCocoonTriplet";
//
//        // Define the request headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(Util.getTokenData());
//
//        HttpEntity<MarketAuctionForPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
//        MarketAuctionForPrintResponse response = new MarketAuctionForPrintResponse();
//        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ContentRoot response2 = objectMapper.readValue(response1, ContentRoot.class);
//
//        return response2;
//        // Process the API response as needed
//        //return apiResponse;
//    }


    public ContentRoot fetchDataFromApiSeedCocoonTriplet(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

                String finalapiurl = apiUrl + "auction/print/getPrintableDataForLotForSeedCocoonTriplet";

//        final String finalapiurl = "http://localhost:8002/market-auction/v1/auction/print/getPrintableDataForLotForSeedCocoonTriplet";

        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = Util.getTokenData();
        if (token != null && !token.trim().isEmpty()) {
            headers.setBearerAuth(token);
        }

        HttpEntity<MarketAuctionForPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(finalapiurl, HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException rce) {
            throw new RuntimeException("Error calling remote API: " + rce.getMessage(), rce);
        }

        if (responseEntity == null || responseEntity.getBody() == null) {
            throw new RuntimeException("Empty response from API: " + finalapiurl);
        }

        // ✅ FIX: Convert HttpStatusCode → HttpStatus
        HttpStatus status = HttpStatus.valueOf(responseEntity.getStatusCode().value());
        String body = responseEntity.getBody();

        if (!status.is2xxSuccessful()) {
            throw new RuntimeException("API call failed. status=" + status + ", body=" + body);
        }

        // JSON → ContentRoot
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContentRoot contentRoot;
        try {
            contentRoot = objectMapper.readValue(body, ContentRoot.class);
        } catch (JsonProcessingException jpe) {
            throw jpe;
        }

        if (contentRoot == null) {
            contentRoot = new ContentRoot();
        }

        return contentRoot;
    }

    public AuthorisationResponse fetchDataFromAuth(AuthorisationLetterPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl + "service/authorisationLetter";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<AuthorisationLetterPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        AuthorisationLetterReportResponse response = new AuthorisationLetterReportResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AuthorisationResponse response2 = objectMapper.readValue(response1, AuthorisationResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
    public ContentRoot fetchDataFromApiSilk(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = apiUrl + "auction/print/getPrintableDataForLotForSilk";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/print/getPrintableDataForLotForSilk";

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

    public void uploadSanctionToDbt(byte[] pdfBytes, String fileName) {

        try {

            String uploadUrl = dbtApiUrl + "service/uploadSanctionOrder";
//                    String uploadUrl = "http://localhost:8013/dbt/v1/service/uploadSanctionOrder";


            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(Util.getTokenData());

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource fileResource = new ByteArrayResource(pdfBytes) {
                @Override
                public String getFilename() {
                    return fileName;   // ✅ dynamic filename
                }
            };

            body.add("multipartFile", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            logger.info("Sanction Order file name is",fileName);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(uploadUrl, requestEntity, String.class);


            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("DBT Upload Failed: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error calling DBT upload API: " + e.getMessage());
        }
    }


    public SanctionOrder fetchDataFromMscCommercialMarket(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "service/getSanctionOrderForCommercialMarket";

//        String finalapiurl = "http://localhost:8013/dbt/v1/service/getSanctionOrderForCommercialMarket";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response = objectMapper.readValue(responseBody, SanctionOrder.class);

        return response;
    }

    public SanctionOrder fetchDataFromPsfaReelingShed(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "sanctionOrderWorkOrderAcknowledgement/getReelingShedSanctionDetails";

//                String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getReelingShedSanctionDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseJson = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseJson, SanctionOrder.class);
    }

    public SanctionOrder fetchDataFromPsfaReelingShedSelection(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "sanctionOrderWorkOrderAcknowledgement/getReelingShedWorkOrderDetails";

//                String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getReelingShedWorkOrderDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseJson = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseJson, SanctionOrder.class);
    }




    public AcknowledgementResponse fetchDataReelerAcknowledgement(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "service/reelerAcknowledgement";

//        String finalapiurl = "http://localhost:8013/dbt/v1/service/reelerAcknowledgement";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ApplicationFormPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AcknowledgementResponse response = objectMapper.readValue(responseBody, AcknowledgementResponse.class);

        return response;
    }

    public AcknowledgementResponse fetchDataFromCommercialMarket(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "service/commercialAcknowledgement";

//        String finalapiurl = "http://localhost:8013/dbt/v1/service/commercialAcknowledgement";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ApplicationFormPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AcknowledgementResponse response = objectMapper.readValue(responseBody, AcknowledgementResponse.class);

        return response;
    }

    public AcknowledgementResponse fetchDataFromSeedMarket(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        String finalapiurl = dbtApiUrl + "sanctionOrderWorkOrderAcknowledgement/farmerReelerAcknowledgement";

//        String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/farmerReelerAcknowledgement";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ApplicationFormPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AcknowledgementResponse response = objectMapper.readValue(responseBody, AcknowledgementResponse.class);

        return response;
    }

    public SanctionOrder fetchSanctionSeedMarketDetails(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getSanctionOrderForSeedMarketDetailsWithDesignation";

//        String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getSanctionOrderForSeedMarketDetailsWithDesignation";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, SanctionOrder.class);
    }

    public SanctionOrder fetchSanctionCommercialMarketDetails(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getCommercialMarketSanctionDetails";

//        String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getCommercialMarketSanctionDetails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, SanctionOrder.class);
    }

    public SanctionOrder fetchSanctionSeedIncentiveBonus(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getSeedMarketIncentiveBonusSanctionDetails";

//        String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getSeedMarketIncentiveBonusSanctionDetails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, SanctionOrder.class);
    }

    public SanctionOrder fetchSanctionOrderForSeedMarketDetails(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

      String finalapiurl = dbtApiUrl +"service/getSanctionOrderForSeedMarketDetails";


//        String finalapiurl = "http://localhost:8013/dbt/v1/service/getSanctionOrderForSeedMarketDetails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String responseBody = restTemplate.postForObject(finalapiurl, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, SanctionOrder.class);
    }





    public SeedMarket fetchDataFromPermit(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
         String finalapiurl = apiUrl + "lotGroupage/getLotDistributeDetailsForPermitRSP";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "lotGroupage/getLotDistributeDetailsForPermitRSP";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<LotStatusSeedMarketRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        LotDistributeResponse response = new LotDistributeResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SeedMarket response2 = objectMapper.readValue(response1, SeedMarket.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public SeedMarket fetchDataFromInvoice(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
         String finalapiurl = apiUrl + "lotGroupage/getLotDistributeResponseForInvoiceForSeedMarket";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "lotGroupage/getLotDistributeResponseForInvoiceForSeedMarket";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<LotStatusSeedMarketRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        LotDistributeResponse response = new LotDistributeResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SeedMarket response2 = objectMapper.readValue(response1, SeedMarket.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }


    public SeedMarket fetchDataCashAndMarketReciept(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint

        String finalapiurl = apiUrl + "lotGroupage/getLotDistributeDetailsForMarketReceiptAndCashReceipt";
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "lotGroupage/getLotDistributeDetailsForMarketReceiptAndCashReceipt";



        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<LotStatusSeedMarketRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SeedMarket response = restTemplate.postForObject(finalapiurl,requestEntity, SeedMarket.class);

        return response;
        // Process the API response as needed
        //return apiResponse;
    }


    public SanctionOrder fetchDataFromSilkIncentive(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getSilkIncentiveSanctionDetails";
//        String finalapiurl = "http://localhost:8013/dbt/v1/sanctionOrderWorkOrderAcknowledgement/getSilkIncentiveSanctionDetails";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public SanctionOrder fetchDataFromSeedCocoon(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/getSanctionOrderForK2";
//        String finalapiurl = "http://localhost:8013/dbt/v1/service/getSanctionOrderForK2";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
    public AcknowledgementResponse fetchData(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        String finalapiurl = dbtApiUrl +"service/pmksyAcknowledgement";

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/pmksyAcknowledgement";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ApplicationFormPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        AcknowledgementResponse response = restTemplate.postForObject(finalapiurl,requestEntity, AcknowledgementResponse.class);

        return response;
        // Process the API response as needed
        //return apiResponse;
    }


    public SupplyOrderResponse fetchDataFromSupply(SupplyOrderPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/supplyOrderReceipt";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SupplyOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SupplyOrderLetterReportResponse response = new SupplyOrderLetterReportResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SupplyOrderResponse response2 = objectMapper.readValue(response1, SupplyOrderResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
    public SelectionLetterResponse fetchDataFromSelection(SelectionLetterPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/selectionLetter";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SelectionLetterPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SelectionLetterReportResponse response = new SelectionLetterReportResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SelectionLetterResponse response2 = objectMapper.readValue(response1, SelectionLetterResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    public SanctionOrder fetchDataFromSanction(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "sanctionOrderWorkOrderAcknowledgement/getFarmerSanctionForRHDetails";


        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getFarmerSanctionForRHDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            String response = restTemplate.postForObject(finalapiurl, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, SanctionOrder.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            // Get the response body from the exception
            String responseBody = httpEx.getResponseBodyAsString();
            logger.error("Error calling sanction API: {}", responseBody, httpEx);
            throw new RuntimeException("Failed to fetch sanction data: " + responseBody, httpEx);

        } catch (Exception ex) {
            logger.error("Unexpected error calling sanction API: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }


    public SanctionOrder fetchDataFromSanctionBoiler(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "sanctionOrderWorkOrderAcknowledgement/getAdoptingBoilerSanctionDetails";


        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getAdoptingBoilerSanctionDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            String response = restTemplate.postForObject(finalapiurl, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, SanctionOrder.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            // Get the response body from the exception
            String responseBody = httpEx.getResponseBodyAsString();
            logger.error("Error calling sanction API: {}", responseBody, httpEx);
            throw new RuntimeException("Failed to fetch sanction data: " + responseBody, httpEx);

        } catch (Exception ex) {
            logger.error("Unexpected error calling sanction API: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }

    public SanctionOrder fetchDataFromSanctionBoilerSelection(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "sanctionOrderWorkOrderAcknowledgement/getAdoptingBoilerWorkOrderDetails";


        String finalapiurl = dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getAdoptingBoilerWorkOrderDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            String response = restTemplate.postForObject(finalapiurl, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, SanctionOrder.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            // Get the response body from the exception
            String responseBody = httpEx.getResponseBodyAsString();
            logger.error("Error calling sanction API: {}", responseBody, httpEx);
            throw new RuntimeException("Failed to fetch sanction data: " + responseBody, httpEx);

        } catch (Exception ex) {
            logger.error("Unexpected error calling sanction API: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }




    public SanctionOrder fetchDataFromChawkiSanctionOrder(SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException {

        // Using the same base as other DBT calls
        String finalapiurl = dbtApiUrl + "registeredPrivateChawki/getChawkiSanctionOrderFullDetails";

//        String finalapiurl = "http://localhost:8013/dbt/v1/registeredPrivateChawki/getChawkiSanctionOrderFullDetails";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            String response = restTemplate.postForObject(finalapiurl, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, SanctionOrder.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            String responseBody = httpEx.getResponseBodyAsString();
            logger.error("Error calling Chawki sanction API: {}", responseBody, httpEx);
            throw new RuntimeException("Failed to fetch sanction data: " + responseBody, httpEx);

        } catch (Exception ex) {
            logger.error("Unexpected error calling Chawki sanction API: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }



    public SanctionOrder fetchDataFromSanctionEquipment(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/getSanctionOrderRHEquipment";


        String finalapiurl = dbtApiUrl +"service/getSanctionOrderRHEquipment";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            String response = restTemplate.postForObject(finalapiurl, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, SanctionOrder.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            // Get the response body from the exception
            String responseBody = httpEx.getResponseBodyAsString();
            logger.error("Error calling sanction API: {}", responseBody, httpEx);
            throw new RuntimeException("Failed to fetch sanction data: " + responseBody, httpEx);

        } catch (Exception ex) {
            logger.error("Unexpected error calling sanction API: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error: " + ex.getMessage(), ex);
        }
    }



    public List<SanctionOrderResponse> fetchDataOfSanctionAndAll(SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException {

        String finalapiurl = "http://localhost:8013/dbt/pdfDownload";

        Map<String, Object> body = new HashMap<>();
        body.put("schemeId", requestDto.getSchemeId());
        body.put("subSchemeId", requestDto.getSubSchemeId());
        body.put("componentId", requestDto.getComponentId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(finalapiurl, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<SanctionOrderResponse>>() {}
            );

        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch sanction data: " + ex.getMessage(), ex);
        }
    }



    public AcknowledgementResponse fetchAcknowledgementPmksy(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint

        String finalapiurl = dbtApiUrl +"service/pmksyAcknowledgement";

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/pmksyAcknowledgement";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ApplicationFormPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        AcknowledgementResponse response = restTemplate.postForObject(finalapiurl,requestEntity, AcknowledgementResponse.class);

        return response;
        // Process the API response as needed
        //return apiResponse;
    }




        public SanctionOrder fetchSanctionOrderPmksy(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
            // Make a GET request to the API endpoint
//        String finalapiurl = dbtApiUrl +"service/sanctionOrder";
//
            String finalapiurl = dbtApiUrl +"service/sanctionOrder";

//            String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionOrder";

            // Define the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(Util.getTokenData());

            HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
            SanctionOrderResponse response = new SanctionOrderResponse();
            String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

            return response2;
            // Process the API response as needed
            //return apiResponse;
        }
    public SanctionOrder fetchPDMCWorkOrder(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint

        String finalapiurl = dbtApiUrl +"service/sanctionOrderWorkOrder";
//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionOrderWorkOrder";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public SanctionOrder fetchSanctionOrderPmksyCompany(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint

        String finalapiurl = dbtApiUrl +"service/sanctionOrder";
//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionOrder";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public SanctionCompany fetchDataFromSanctionCompany(SanctionCompanyPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/sanctionCompany";

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionCompany";


        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionCompanyPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionCompanyResponse response = new SanctionCompanyResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionCompany response2 = objectMapper.readValue(response1, SanctionCompany.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public SanctionOrder fetchSanctionOrderPDMCFarmer(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/sanctionOrder";
//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionOrder";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }


    public SanctionOrder fetchSanctionOrderPDMCCompany(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/sanctionOrder";
//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionOrder";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<CheckInspectionStatusRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionOrderResponse response = new SanctionOrderResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionOrder response2 = objectMapper.readValue(response1, SanctionOrder.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
    public SanctionBeneficiary fetchDataFromSanctionBeneficiary(SanctionBeneficiaryPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl + "service/sanctionBeneficiary";

//        String finalapiurl = "http://localhost:8013/dbt/v1/" + "service/sanctionBeneficiary";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionBeneficiaryPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        SanctionBeneficiaryResponse response = new SanctionBeneficiaryResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SanctionBeneficiary response2 = objectMapper.readValue(response1, SanctionBeneficiary.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }


    public WorkOrderReportResponse fetchDataApi(WorkOrderPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = dbtApiUrl +"service/workOrderGeneration";

//        String finalapiurl ="http://localhost:8013/dbt/v1/" + "service/workOrderGeneration";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<WorkOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        WorkOrderGenerationReportResponse response = new WorkOrderGenerationReportResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        WorkOrderReportResponse response2 = objectMapper.readValue(response1, WorkOrderReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public WorkOrderReportResponse fetchDataApiWorkOrder(WorkOrderPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl =dbtApiUrl +"sanctionOrderWorkOrderAcknowledgement/getWorkOrderSanctionRHDetails";

//        String finalapiurl ="http://localhost:8013/dbt/v1/" + "sanctionOrderWorkOrderAcknowledgement/getWorkOrderSanctionRHDetails";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<WorkOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        WorkOrderGenerationReportResponse response = new WorkOrderGenerationReportResponse();
        String response1=        restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        WorkOrderReportResponse response2 = objectMapper.readValue(response1, WorkOrderReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }


    public DTRReportResponse dtrReport(DTROnlineRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDTROnlineReport";

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

    public DTRReportResponse blankDtrReport(DTROnlineRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getBlankDTROnlineReport";

        String finalapiurl = apiUrl + "auction/report/getBlankDTROnlineReport";

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


    public DTRReportResponse dtrReportForSilkType(DTROnlineRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDTROnlineReportForSilkType";

        String finalapiurl = apiUrl + "auction/report/getDTROnlineReportForSilkType";

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

    public DTRReportResponse blankDtrReportForSilkType(DTROnlineRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getBlankDTROnlineReportForSilkType";

        String finalapiurl = apiUrl + "auction/report/getBlankDTROnlineReportForSilkType";

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

    public MonthlyDistrictResponse monthlyDistrictReport(MonthlyDistrictRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getMonthlyDistrictReport";

        String finalapiurl = apiUrl + "auction/report/getMonthlyDistrictReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyDistrictRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        MonthlyDistrictResponse response2 = objectMapper.readValue(response1, MonthlyDistrictResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public MonthlyDistrictResponse monthlyDistrictReportSilk(MonthlyDistrictRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getMonthlyDistrictReportSilkType";

        String finalapiurl = apiUrl + "auction/report/getMonthlyDistrictReportSilkType";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyDistrictRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        MonthlyDistrictResponse response2 = objectMapper.readValue(response1, MonthlyDistrictResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }



    public UnitCounterReportResponse unitCounterReport(UnitCounterReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getUnitCounterReport";

        String finalapiurl = apiUrl + "auction/report/getUnitCounterReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<UnitCounterReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        UnitCounterReportResponse response2 = objectMapper.readValue(response1, UnitCounterReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public UnitCounterReportResponse unitCounterReportSilkType(UnitCounterReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getUnitCounterReportSilkType";

        String finalapiurl = apiUrl + "auction/report/getUnitCounterReportSilkType";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<UnitCounterReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        UnitCounterReportResponse response2 = objectMapper.readValue(response1, UnitCounterReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public ReelerMFResponse reelerMFReport(UnitCounterReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerMFReport";

        String finalapiurl = apiUrl + "auction/report/getReelerMFReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<UnitCounterReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ReelerMFResponse response2 = objectMapper.readValue(response1, ReelerMFResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public BiddingReportResponse biddingReport(BiddingReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getBiddingReport";

        String finalapiurl = apiUrl + "auction/report/getBiddingReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<BiddingReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        BiddingReportResponse response = new BiddingReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        BiddingReportResponse response2 = objectMapper.readValue(response1, BiddingReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public BiddingReportResponse reelerBiddingReport(ReelerBiddingReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerBiddingReport";

        String finalapiurl = apiUrl + "auction/report/getReelerBiddingReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ReelerBiddingReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        BiddingReportResponse response = new BiddingReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        BiddingReportResponse response2 = objectMapper.readValue(response1, BiddingReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public PendingReportResponse pendingReportList(PendingReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getPendingLotReport";

        String finalapiurl = apiUrl + "auction/report/getPendingLotReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<PendingReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        PendingReportResponse response = new PendingReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        PendingReportResponse response2 = objectMapper.readValue(response1, PendingReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public FarmerTxnResponse farmerTxnReportList(FarmerTxnRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
         //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getFarmerTxnReport";

        String finalapiurl = apiUrl + "auction/report/getFarmerTxnReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<FarmerTxnRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        FarmerTxnResponse response = new FarmerTxnResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        FarmerTxnResponse response2 = objectMapper.readValue(response1, FarmerTxnResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public ReelerPendingReposne getReelerPendingReport(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerPendingReport";

        String finalapiurl = apiUrl + "auction/report/getReelerPendingReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestDto, headers);
        ReelerPendingReposne response = new ReelerPendingReposne();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ReelerPendingReposne response2 = objectMapper.readValue(response1, ReelerPendingReposne.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public DashboardResponse getDashboardReport(DashboardReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDashboardReport";

        String finalapiurl = apiUrl + "auction/report/getDashboardReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<DashboardReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        DashboardReport response = new DashboardReport();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DashboardResponse response2 = objectMapper.readValue(response1, DashboardResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }


    public DashboardResponse getDashboardReportSilkType(DashboardReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//         String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDashboardReportSilkType";

        String finalapiurl = apiUrl + "auction/report/getDashboardReportSilkType";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<DashboardReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        DashboardReport response = new DashboardReport();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DashboardResponse response2 = objectMapper.readValue(response1, DashboardResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public Form13ReportResponse getForm13Report(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getForm13Report";

        String finalapiurl = apiUrl + "auction/report/getForm13Report";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestDto, headers);
        Form13ReportResponse response = new Form13ReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Form13ReportResponse response2 = objectMapper.readValue(response1, Form13ReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public Form13ReportResponse getForm13ReportByDist(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getForm13ReportByDist";

        String finalapiurl = apiUrl + "auction/report/getForm13ReportByDist";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestDto, headers);
        Form13ReportResponse response = new Form13ReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Form13ReportResponse response2 = objectMapper.readValue(response1, Form13ReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public ReelerTxnResponse reelerTxnReportList(ReelerTxnRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//         String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerTxnReport";

        String finalapiurl = apiUrl + "auction/report/getReelerTxnReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<ReelerTxnRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        ReelerTxnResponse response = new ReelerTxnResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ReelerTxnResponse response2 = objectMapper.readValue(response1, ReelerTxnResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public AverageReportDataResponse averageReport(AverageReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
       // String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/averageReportForYearsReport";

        String finalapiurl = apiUrl + "auction/report/averageReportForYearsReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<AverageReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AverageReportDataResponse response2 = objectMapper.readValue(response1, AverageReportDataResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public Form13ReportResponse getForm13ReportSilk(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getForm13ReportSilk";

        String finalapiurl = apiUrl + "auction/report/getForm13ReportSilk";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestDto, headers);
        Form13ReportResponse response = new Form13ReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Form13ReportResponse response2 = objectMapper.readValue(response1, Form13ReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public Form13ReportResponse getForm13ReportByDistSilkType(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
//        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getForm13ReportByDistrictSilkType";

        String finalapiurl = apiUrl + "auction/report/getForm13ReportByDistrictSilkType";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestDto, headers);
        Form13ReportResponse response = new Form13ReportResponse();
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Form13ReportResponse response2 = objectMapper.readValue(response1, Form13ReportResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public CocoonReport averageCocoonReport(AverageReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/averageCocoonReport";

        String finalapiurl = apiUrl + "auction/report/averageCocoonReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<AverageReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        CocoonReport response2 = objectMapper.readValue(response1, CocoonReport.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public DTRAllMarketResponse dtrAllReport(Form13Request requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
       // String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/dtrReport";

        String finalapiurl = apiUrl + "auction/report/dtrReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<Form13Request> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DTRAllMarketResponse response2 = objectMapper.readValue(response1, DTRAllMarketResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public AudioVisualResponse audioVisualReport(AudioVisualReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        // String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/audioVisualReport";

        String finalapiurl = apiUrl + "auction/report/audioVisualReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<AudioVisualReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AudioVisualResponse response2 = objectMapper.readValue(response1, AudioVisualResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public Report27bResponse get27bReport(MonthlyReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
         //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/27bReport";

        String finalapiurl = apiUrl + "auction/report/27bReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Report27bResponse response2 = objectMapper.readValue(response1, Report27bResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public ReportMonthlyResponse getMonthlyReport(MonthlyReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/monthlyReport";

        String finalapiurl = apiUrl + "auction/report/monthlyReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ReportMonthlyResponse response2 = objectMapper.readValue(response1, ReportMonthlyResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public MarketResponse getMarketReport(MonthlyReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/marketReport";

        String finalapiurl = apiUrl + "auction/report/marketReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        MarketResponse response2 = objectMapper.readValue(response1, MarketResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }

    public DivisionResponse getDistrictWiseReport(MonthlyReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/districtWiseReport";

        String finalapiurl = apiUrl + "auction/report/districtWiseReport";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<MonthlyReportRequest> requestEntity = new HttpEntity<>(requestDto, headers);
        String response1=restTemplate.postForObject(finalapiurl,requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        DivisionResponse response2 = objectMapper.readValue(response1, DivisionResponse.class);

        return response2;
        // Process the API response as needed
        //return apiResponse;
    }
}

