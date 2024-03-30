package com.sericulture.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.DTRAllMarket.DTRAllMarketResponse;
import com.sericulture.model.DTRAllMarket.DTRInfoResponse;
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

    public BiddingReportResponse biddingReport(BiddingReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getBiddingReport";

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
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerBiddingReport";

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
        //String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerPendingReport";

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
       // String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getDashboardReport";

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

    public Form13ReportResponse getForm13Report(RequestBody requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
       // String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getForm13Report";

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

    public ReelerTxnResponse reelerTxnReportList(ReelerTxnRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
      //   String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/getReelerTxnReport";

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

    public CocoonReport averageCocoonReport(AverageReportRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = "http://localhost:8002/market-auction/v1/" + "auction/report/averageCocoonReport";

        //String finalapiurl = apiUrl + "auction/report/averageCocoonReport";

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
}

