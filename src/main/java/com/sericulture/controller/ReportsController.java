package com.sericulture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sericulture.model.Content;
import com.sericulture.model.ContentRoot;
import com.sericulture.model.MarketAuctionForPrintRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/reports")
public class ReportsController {

    private final ApiService apiService;

    public ReportsController(ApiService apiService) {
        this.apiService = apiService;
    }
    @PostMapping("/gettripletpdf")
    public ResponseEntity<byte[]> gettripletpdf(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {


        String destFileName = "report.pdf";
        JasperReport jasperReport = getJasperReport(requestDto);

        // 2. parameters "empty"
        Map<String, Object> parameters = getParameters();

        // 3. datasource "java object"
        JRDataSource dataSource = getDataSource(requestDto);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");


        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
        pdfExporter.exportReport();

        return new ResponseEntity<>(pdfStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

        //return "Hi";
    }
    private JasperReport getJasperReport(MarketAuctionForPrintRequest requestDto) throws FileNotFoundException, JRException {
        File template = ResourceUtils.getFile("classpath:triplet.jrxml");
        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }
    private  Map<String, Object> getParameters(){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "hmkcode");
        return parameters;
    }

    private  JRDataSource getDataSource(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApi(requestDto);

        List<Content> countries = new LinkedList<>();
        countries.add(apiResponse.content);
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));

        return new JRBeanCollectionDataSource(countries);
    }

}
