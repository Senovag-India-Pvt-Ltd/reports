package com.sericulture.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.AudioVisual.AudioVisualReportRequest;
import com.sericulture.model.AudioVisual.AudioVisualResponse;
import com.sericulture.model.DTRAllMarket.DTRAllMarketResponse;
import com.sericulture.model.MarketReport.MarketResponse;
import com.sericulture.model.MarketWiseReport.DivisionResponse;
import com.sericulture.model.MonthlyReport.MonthlyReportRequest;
import com.sericulture.model.MonthlyReport.ReportMonthlyResponse;
import com.sericulture.model.VahivaatuReport.Report27bResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

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

    public AuthorisationResponse fetchDataFromAuth(AuthorisationLetterPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = "http://localhost:8013/dbt/v1/service/authorisationLetter";

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




    public AcknowledgementResponse fetchData(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = "http://localhost:8013/dbt/v1/service/acknowledgementReceipt";

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
        String finalapiurl = "http://localhost:8013/dbt/v1/service/supplyOrderReceipt";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        String finalapiurl = "http://localhost:8013/dbt/v1/service/selectionLetter";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

    public SanctionOrder fetchDataFromSanction(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = "http://localhost:8013/dbt/v1/service/sanctionOrder";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(Util.getTokenData());

        HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);
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
        String finalapiurl = "http://localhost:8013/dbt/v1/service/sanctionCompany";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

    public SanctionBeneficiary fetchDataFromSanctionBeneficiary(SanctionBeneficiaryPrintRequest requestDto) throws JsonProcessingException {
        // Make a GET request to the API endpoint
        String finalapiurl = "http://localhost:8013/dbt/v1/service/sanctionBeneficiary";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        String finalapiurl = "http://localhost:8013/dbt/v1/service/workOrderGeneration";

        // Define the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

