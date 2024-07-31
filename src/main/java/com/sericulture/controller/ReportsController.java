package com.sericulture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.DTRAllMarket.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.math.DoubleMath.roundToLong;
import static org.hibernate.type.descriptor.java.CoercionHelper.toLong;

@RestController
@RequestMapping("marketreport")
public class ReportsController {
    private final ApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    public ReportsController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/gettripletpdf")
    public ResponseEntity<?> gettripletpdf(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report.pdf";
            JasperReport jasperReport = getJasperReport("triplet.jrxml");

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/gettripletpdf-kannada")
    public ResponseEntity<?> gettripletpdfKannada(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("kannada_triplicate_with_variable_1.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForTriplet(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }


    @PostMapping("/getBlankSample")
    public ResponseEntity<?> getBlankSample(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getBlankSample");
            logger.info("enter to getBlankSample");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Acknowledgement.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForAcknowledgementReceipt(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }


    @PostMapping("/getAuthorisationLetter")
    public ResponseEntity<?> getAuthorisationLetter(@RequestBody AuthorisationLetterPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getAuthorisationLetter");
            logger.info("enter to getAuthorisationLetter");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Authorisation Letter to the Bank _Landscape_1.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForAuthorisationLetter(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }


    @PostMapping("/getSelectionLetter")
    public ResponseEntity<?> getSelectionLetter(@RequestBody SelectionLetterPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSelectionLetter");
            logger.info("enter to getSelectionLetter");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Selection letters_Landscape.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSelectionLetter(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }@PostMapping("/getSupplyOrder")
    public ResponseEntity<?> getSupplyOrder(@RequestBody SupplyOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSupplyOrder");
            logger.info("enter to getSupplyOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Farm Mechanization Supply Order Receipt_Landscape.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSupplyOrder(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }


    @PostMapping("/getSanctionOrder")
    public ResponseEntity<?> getSanctionOrder(@RequestBody SanctionOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_Order.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrder(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/getSanctionCompany")
    public ResponseEntity<?> getSanctionOrder(@RequestBody SanctionCompanyPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionCompany");
            logger.info("enter to getSanctionCompany");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("SanctionCompany.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionCompany(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/getSanctionBeneficiary")
    public ResponseEntity<?> getSanctionBeneficiary(@RequestBody SanctionBeneficiaryPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionBeneficiary");
            logger.info("enter to getSanctionBeneficiary");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("SanctionOrderBenef.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionBeneficiary(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }
        @PostMapping("/getAuthorisationLetterFromFarmer")
    public ResponseEntity<?> getAuthorisationLetterFromFarmer(@RequestBody WorkOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getAuthorisationLetterFromFarmer");
            logger.info("enter to getAuthorisationLetterFromFarmer");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Authorisation Letter From Farmer _Landscape_1.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceAuthorisationLetterFromFarmer(requestDto);

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

//    @PostMapping("/getAuthorisationLetterFromFarmerLand")
//    public ResponseEntity<?> getAuthorisationLetterFromFarmerLand(@RequestBody WorkOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {
//
//        try {
//            System.out.println("enter to getAuthorisationLetterFromFarmerLand");
//            logger.info("enter to getAuthorisationLetterFromFarmerLand");
//            String destFileName = "report_kannada.pdf";
//            JasperReport jasperReport = getJasperReport("Authorisation Letter From Farmer _Landscape.jrxml");
//
//            // 2. parameters "empty"
//            Map<String, Object> parameters = getParameters();
//
//            // 3. datasource "java object"
//            JRDataSource dataSource = getDataSourceForAuthorisationLetterFromFarmerLand(requestDto);
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("attachment", "report.pdf");
//
//
//            JRPdfExporter pdfExporter = new JRPdfExporter();
//            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
//            pdfExporter.exportReport();
//            return new ResponseEntity<>(pdfStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            logger.info(ex.getMessage() + ex.getStackTrace());
//            HttpHeaders headers = new HttpHeaders();
//            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
//            //return  ex.getMessage();
//            //throw new RuntimeException("fail export file: " + ex.getMessage());
//        }
//
//
//        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
//
//    }
    @PostMapping("/getfarmercopy")
    public ResponseEntity<?> getfarmercopy(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report.pdf";
            JasperReport jasperReport = getJasperReport("farmercopy.jrxml");

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/gatepass")
    public ResponseEntity<?> gatepass(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report.pdf";
            JasperReport jasperReport = getJasperReport("Gate_pass.jrxml");

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/getfarmercopy-kannada")
    public ResponseEntity<?> getfarmercopyKannada(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("farmer_copy_with_variable - Copy.jrxml");

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

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/gettripletpdfkannada")
    public ResponseEntity<?> gettripletpdfkannada(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report.pdf";
            JasperReport jasperReport = getJasperReport("BidSlipTriplicate_kannada.jrxml");

            String ttfFontFilePath = "C:/reports/NIRMALAB.ttf";

            //JRLoader.loadLibrary("JasperReportsFonts", ttfFontFilePath);

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            //parameters.put("net.sf.jasperreports.awt.ignore.missing.font", "true");
            //parameters.put("net.sf.jasperreports.default.font.name", "Nirmala UI Semilight");
            //parameters.put("net.sf.jasperreports.default.font.pdf.embedded", "true");
            //parameters.put("net.sf.jasperreports.default.font.pdf.encoding", "Identity-H");
            //parameters.put("net.sf.jasperreports.export.pdf.font.path", ttfFontFilePath);

            // Set font path

            //net.sf.jasperreports.extensions..factory.fonts=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory;
            //net.sf.jasperreports.extension.simple.font.families.myfontfamily="fonts/fonts.xml";


            // 3. datasource "java object"
            JRDataSource dataSource = getDataSource(requestDto);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //jasperPrint.setProperty("net.sf.jasperreports.export.pdf.font.path", ttfFontFilePath);
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report.pdf");


            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();
            return new ResponseEntity<>(pdfStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    private JasperReport getJasperReport(String reportpath) throws FileNotFoundException, JRException {
        File template = ResourceUtils.getFile("/reports/" + reportpath);
        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }

    private Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "hmkcode");
        return parameters;
    }

    private JRDataSource getDataSource(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApi(requestDto);
        List<Content> countries = new LinkedList<>();
        if (apiResponse.content != null) {

            long farmerMarketFee = (long) apiResponse.content.getFarmerMarketFee();
            long reelerMarketFee = (long) apiResponse.content.getReelerMarketFee();
            long totalFee = farmerMarketFee + reelerMarketFee;

            String formatfees = farmerMarketFee + "+" + reelerMarketFee + "=" + totalFee;
            apiResponse.content.setFeespaid(formatfees);
//            String formatfees = roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + roundToTwoDecimalPlaces((apiResponse.content.getFarmerMarketFee() + apiResponse.content.getReelerMarketFee()));
//            apiResponse.content.setFeespaid(formatfees);

            Double total = Double.valueOf(apiResponse.content.getLotSoldOutAmount());
            Double farmerfee = apiResponse.content.getFarmerMarketFee();
            Double realerfee = apiResponse.content.getReelerMarketFee();
            String farmeramout = "" + roundToTwoDecimalPlaces((total - farmerfee));
            String relaramout = "" + roundToTwoDecimalPlaces((total - realerfee));
            Double slip1Amount = 0.0;
            slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
            apiResponse.content.setAmountfarmer(farmeramout);
            apiResponse.content.setAmountrealar(relaramout);
            apiResponse.content.setLoginname_accountnumber_ifsccode("    (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
            apiResponse.content.setAccountnumber_ifsccode("Bank - " + apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount.toString());
            //  apiResponse.content.setReeleramount(relaramout);
            String inputDateTime = "";
            if (apiResponse.content.getAuctionDateWithTime() != null) {
                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            } else {
                apiResponse.content.setAuctionDate_time("");
            }
            // Parse the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date parsedDate;
            try {
                if (inputDateTime != null && !inputDateTime.equals("")) {
                    parsedDate = inputFormat.parse(inputDateTime);
                    // Format the output date and time
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                    SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDateTime = outputFormat.format(parsedDate);
                    String formattedDateTime1 = outputFormat1.format(parsedDate);
                    apiResponse.content.setAuctionDate_time(formattedDateTime);
                    apiResponse.content.setAuctionDate(formattedDateTime1);
                }
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing input date and time", e);
            }

            String smallBins = "";
            String bigBins = "";

            if (apiResponse.content.getReelerNameKannada() == null) {
                apiResponse.content.setReelerNameKannada("");
            }
            if (apiResponse.content.getFarmerNameKannada() == null) {
                apiResponse.content.setFarmerNameKannada("");
            }
            if (apiResponse.content.getFatherNameKan() == null) {
                apiResponse.content.setFatherNameKan("");
            }
            if (apiResponse.content.getBinno() == null) {
                apiResponse.content.setBinno("");
            }
            if (apiResponse.content.getFarmerAddress() == null) {
                apiResponse.content.setFarmerAddress("");
            }
            if (apiResponse.content.getReelerLicense() == null) {
                apiResponse.content.setReelerLicense("");
            }

            apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
            String farmerNumber = "";
            if (apiResponse.content.getFruitsId() != null && !apiResponse.content.getFruitsId().equals("")) {
                farmerNumber = apiResponse.content.getFruitsId();
            } else {
                farmerNumber = apiResponse.content.getFarmerNumber();
            }
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")                        ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());
            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText +  apiResponse.content.getReelerName() +apiResponse.content.getReelerNameKannada() + " " + reelerAddressText);

            if (apiResponse.content.getSmallBinList() != null) {
                List<String> smallBinList = apiResponse.content.getSmallBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                smallBins = String.join(",", smallBinList);
            }
            apiResponse.content.setAcknowledgmentString("ಈ ಮೇಲೆ ನಮೂದಿಸಿದ ವಿಷಯಗಳು ಸರಿಯಾಗಿವೆಯೆಂದು ದೃಢೀಕರಿಸುತ್ತೇನೆ ಹಾಗು ಲೈಸೆನ್ಸ್ ಪಡೆದವರಿಗೆ /ಪ್ರತಿನಿಧಿಗೆ ಕೆ.ಜಿ. ಗೂಡುಗಳನ್ನು " + apiResponse.content.getAuctionDate() + " ದಿನ _______ ಘಂಟೆಯೊಳಗಾಗಿ    ಸಾಗಿಸಲು ಅನುಮತಿ ನೀಡಿದ್ದೇನೆ.");
            if (apiResponse.content.getBigBinList() != null) {
                List<String> bigBinList = apiResponse.content.getBigBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                bigBins = String.join(",", bigBinList);
            }
//            apiResponse.content.setBinno("Big: " + bigBins + " Small: " + smallBins);
            apiResponse.content.setBinno("  ಜಾಲರಿ ಸಂಖ್ಯೆ : " + bigBins );
            apiResponse.content.setDescription("  ಲಾಭದ ಗೂಡು ಕೊಡುವುದು /ಸಂಗ್ರಹಿಸುವುದನ್ನು  ಕಡ್ಡಾಯವಾಗಿ ನಿಷೇದಿಸಿದೆ .\n" +
                    "  ಕೊಟ್ಟಿದ್ದಲ್ಲಿ  / ಸಂಗ್ರಹಿಸಿದಲ್ಲಿ  ದಂಡ ವಿಧಿಸಲಾಗುವುದು \n  ");

            for (int i = 0; i < 15; i++) {
                switch (i) {
                    case 0:
                        apiResponse.content.setLotDetail0("");
                        break;
                    case 1:
                        apiResponse.content.setLotDetail1("");
                        break;
                    case 2:
                        apiResponse.content.setLotDetail2("");
                        break;
                    case 3:
                        apiResponse.content.setLotDetail3("");
                        break;
                    case 4:
                        apiResponse.content.setLotDetail4("");
                        break;
                    case 5:
                        apiResponse.content.setLotDetail5("");
                        break;
                    case 6:
                        apiResponse.content.setLotDetail6("");
                        break;
                    case 7:
                        apiResponse.content.setLotDetail7("");
                        break;
                    case 8:
                        apiResponse.content.setLotDetail8("");
                        break;
                    case 9:
                        apiResponse.content.setLotDetail9("");
                        break;
                    case 10:
                        apiResponse.content.setLotDetail10("");
                        break;
                    case 11:
                        apiResponse.content.setLotDetail11("");
                        break;
                    case 12:
                        apiResponse.content.setLotDetail12("");
                        break;
                    case 13:
                        apiResponse.content.setLotDetail13("");
                        break;
                    case 14:
                        apiResponse.content.setLotDetail14("");
                        break;
                    default:
                        System.out.println("Default case");
                }
            }

            if (apiResponse.content.getLotWeightDetail() != null) {
                if (apiResponse.content.getLotWeightDetail().size() > 0) {
                    for (int i = 0; i < apiResponse.content.getLotWeightDetail().size(); i++) {
                        switch (i) {
                            case 0:
                                apiResponse.content.setLotDetail0(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 1:

                                apiResponse.content.setLotDetail1(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 2:
                                apiResponse.content.setLotDetail2(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 3:
                                apiResponse.content.setLotDetail3(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 4:
                                apiResponse.content.setLotDetail4(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 5:
                                apiResponse.content.setLotDetail5(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 6:
                                apiResponse.content.setLotDetail6(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 7:
                                apiResponse.content.setLotDetail7(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 8:
                                apiResponse.content.setLotDetail8(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 9:
                                apiResponse.content.setLotDetail9(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 10:
                                apiResponse.content.setLotDetail10(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 11:
                                apiResponse.content.setLotDetail11(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 12:
                                apiResponse.content.setLotDetail12(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 13:
                                apiResponse.content.setLotDetail13(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 14:
                                apiResponse.content.setLotDetail14(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            default:
                                System.out.println("Default case");
                        }
                    }
                }
                apiResponse.content.setTotalcrates(String.valueOf(apiResponse.content.getLotWeightDetail().size()));
                apiResponse.content.setTotalamount(apiResponse.content.getLotSoldOutAmount());
            }
            apiResponse.content.setLogurl("/reports/Seal_of_Karnataka.PNG");
            if (apiResponse.content.getBidAmount().equals("0.0")) {
                apiResponse.content.setBidAmount("");
            } else {
                apiResponse.content.setBidAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getBidAmount()))));
            }
            if (apiResponse.content.getLotWeight().equals("0.0")) {
                apiResponse.content.setLotWeight("");
            }
            if (apiResponse.content.getLotSoldOutAmount().equals("0.0")) {
                apiResponse.content.setLotSoldOutAmount("");
            } else {
//                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));
                                apiResponse.content.setLotSoldOutAmount(String.valueOf(
                        (long) (Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())
                ));
            }
            if (apiResponse.content.getFeespaid().equals("0.0+0.0=0.0")) {
                apiResponse.content.setFeespaid("");
            }
            if (!apiResponse.content.getBidAmount().equals("")) {
                apiResponse.content.setReeleramount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.content.getTotalamount()) + apiResponse.content.getReelerMarketFee())));
            } else {
                apiResponse.content.setReeleramount("");
            }
            countries.add(apiResponse.content);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(countries);
    }

    private JRDataSource getDataSourceForTriplet(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApi(requestDto);
        List<Content> countries = new LinkedList<>();
        if (apiResponse.content != null) {

            long farmerMarketFee = toLong(apiResponse.content.getFarmerMarketFee());
            long reelerMarketFee = toLong(apiResponse.content.getReelerMarketFee());
            long totalMarketFee = farmerMarketFee + reelerMarketFee;

            String formatfees = farmerMarketFee + "+" + reelerMarketFee + "=" + totalMarketFee;
            apiResponse.content.setFeespaid(formatfees);


            long amountPaid = reelerMarketFee;  // Only assign reelerMarketFee to amountPaid

            String marketFees = String.valueOf(reelerMarketFee);  // Convert reelerMarketFee to string
            apiResponse.content.setAmountPaid(marketFees);

            long farmerMarketFeeLong = farmerMarketFee; // Ensure farmerMarketFee is a long
            long paidAmount = farmerMarketFeeLong;
            String format = farmerMarketFeeLong + "";
            apiResponse.content.setPaidAmount(format);


            long total = Math.round(Double.valueOf(apiResponse.content.getLotSoldOutAmount()));
            long farmerfee = Math.round(apiResponse.content.getFarmerMarketFee());
            long realerfee = Math.round(apiResponse.content.getReelerMarketFee());
            String farmeramout = "" + (total - farmerfee);
            String relaramout = "" + (total - realerfee);

            long slip1Amount = Math.round((total - farmerfee) + farmerfee + realerfee);

//            slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
            apiResponse.content.setAmountfarmer(farmeramout);
            apiResponse.content.setAmountrealar(relaramout);
            apiResponse.content.setLoginname_accountnumber_ifsccode(" (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getAccountNumber() + "                       IFSC  Code  :  "  + apiResponse.content.getIfscCode());
            apiResponse.content.setAccountnumber_ifsccode("  Farmer Bank A/c No. - " + apiResponse.content.getAccountNumber() );
            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + Math.round(apiResponse.content.getFarmerMarketFee()) + "+" + Math.round(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
//            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
            apiResponse.content.setIfsc("  IFSC Code : " + apiResponse.content.getIfscCode());


            String inputDateTime = "";
            if (apiResponse.content.getAuctionDateWithTime() != null) {
                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            } else {
                apiResponse.content.setAuctionDate_time("");
            }
            // Parse the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date parsedDate;
            try {
                if (inputDateTime != null && !inputDateTime.equals("")) {
                    parsedDate = inputFormat.parse(inputDateTime);
                    // Format the output date and time
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                    SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDateTime = outputFormat.format(parsedDate);
                    String formattedDateTime1 = outputFormat1.format(parsedDate);
                    apiResponse.content.setAuctionDate_time(formattedDateTime);
                    apiResponse.content.setAuctionDate(formattedDateTime1);
                    apiResponse.content.setDescription1( "  OUT PASS for Lot No " + apiResponse.content.getAllottedLotId() + " ,  Dtd  "  + apiResponse.content.getAuctionDate() + " ,  Wt  " + apiResponse.content.getLotWeight() + "  Kgs, Reeler  " +  apiResponse.content.getReelerLicense() + " ,  " + apiResponse.content.getReelerName() + " ,  " + apiResponse.content.getReelerAddress() );

                }
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing input date and time", e);
            }

            String smallBins = "";
            String bigBins = "";

            if (apiResponse.content.getReelerNameKannada() == null) {
                apiResponse.content.setReelerNameKannada("");
            }
            if (apiResponse.content.getFarmerNameKannada() == null) {
                apiResponse.content.setFarmerNameKannada("");
            }
            if (apiResponse.content.getFarmerAddress() == null) {
                apiResponse.content.setFarmerAddress("");
            }
            if (apiResponse.content.getFatherNameKan() == null) {
                apiResponse.content.setFatherNameKan("");
            }
            if (apiResponse.content.getReelerLicense() == null) {
                apiResponse.content.setReelerLicense("");
            }

            apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
            String farmerNumber = "";
            if (apiResponse.content.getFruitsId() != null && !apiResponse.content.getFruitsId().equals("")) {
                farmerNumber = apiResponse.content.getFruitsId();
            } else {
                farmerNumber = apiResponse.content.getFarmerNumber();
            }
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")                           ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());

            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText + " ,  ಶ್ರೀ /ಶ್ರೀಮತಿ.  " +apiResponse.content.getReelerName()+" ,  ಬಿನ್/ಕೋಂ  "  +apiResponse.content.getReelerNameKannada()+ " ,  " + reelerAddressText);
            if (apiResponse.content.getSmallBinList() != null) {
                List<String> smallBinList = apiResponse.content.getSmallBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                smallBins = String.join(",", smallBinList);
            }
            apiResponse.content.setAcknowledgmentString("ಈ ಮೇಲೆ ನಮೂದಿಸಿದ ವಿಷಯಗಳು ಸರಿಯಾಗಿವೆಯೆಂದು ದೃಢೀಕರಿಸುತ್ತೇನೆ ಹಾಗು ಲೈಸೆನ್ಸ್ ಪಡೆದವರಿಗೆ /ಪ್ರತಿನಿಧಿಗೆ ಕೆ.ಜಿ. ಗೂಡುಗಳನ್ನು " + apiResponse.content.getAuctionDate() + " ದಿನ _______ ಘಂಟೆಯೊಳಗಾಗಿ    ಸಾಗಿಸಲು ಅನುಮತಿ ನೀಡಿದ್ದೇನೆ.");

            if (apiResponse.content.getBigBinList() != null) {
                List<String> bigBinList = apiResponse.content.getBigBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                bigBins = String.join(",", bigBinList);
            }
//            apiResponse.content.setBinno("Big: " + bigBins + " Small: " + smallBins);
            apiResponse.content.setBinno("  ಜಾಲರಿ ಸಂಖ್ಯೆ: " + bigBins );

            for (int i = 0; i < 15; i++) {
                switch (i) {
                    case 0:
                        apiResponse.content.setLotDetail0("");
                        break;
                    case 1:
                        apiResponse.content.setLotDetail1("");
                        break;
                    case 2:
                        apiResponse.content.setLotDetail2("");
                        break;
                    case 3:
                        apiResponse.content.setLotDetail3("");
                        break;
                    case 4:
                        apiResponse.content.setLotDetail4("");
                        break;
                    case 5:
                        apiResponse.content.setLotDetail5("");
                        break;
                    case 6:
                        apiResponse.content.setLotDetail6("");
                        break;
                    case 7:
                        apiResponse.content.setLotDetail7("");
                        break;
                    case 8:
                        apiResponse.content.setLotDetail8("");
                        break;
                    case 9:
                        apiResponse.content.setLotDetail9("");
                        break;
                    case 10:
                        apiResponse.content.setLotDetail10("");
                        break;
                    case 11:
                        apiResponse.content.setLotDetail11("");
                        break;
                    case 12:
                        apiResponse.content.setLotDetail12("");
                        break;
                    case 13:
                        apiResponse.content.setLotDetail13("");
                        break;
                    case 14:
                        apiResponse.content.setLotDetail14("");
                        break;
                    default:
                        System.out.println("Default case");
                }
            }
            if (apiResponse.content.getLotWeightDetail() != null) {
                int lotWeightSize = apiResponse.content.getLotWeightDetail().size();
                for (int i = 0; i < lotWeightSize && i < 15; i++) {
                    try {
                        // Dynamically create the method name
                        Method method = apiResponse.content.getClass().getMethod("setLotDetail" + i, String.class);
                        // Format the value
                        String formattedValue = String.format("%.3f", Double.parseDouble(apiResponse.content.getLotWeightDetail().get(i).toString()));
                        // Invoke the method
                        method.invoke(apiResponse.content, formattedValue);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
//            if (apiResponse.content.getLotWeightDetail() != null) {
//                if (apiResponse.content.getLotWeightDetail().size() > 0) {
//                    for (int i = 0; i < apiResponse.content.getLotWeightDetail().size(); i++) {
                        switch (i) {
                            case 0:
                                apiResponse.content.setLotDetail0(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 1:
                                apiResponse.content.setLotDetail1(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 2:
                                apiResponse.content.setLotDetail2(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 3:
                                apiResponse.content.setLotDetail3(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 4:
                                apiResponse.content.setLotDetail4(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 5:
                                apiResponse.content.setLotDetail5(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 6:
                                apiResponse.content.setLotDetail6(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 7:
                                apiResponse.content.setLotDetail7(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 8:
                                apiResponse.content.setLotDetail8(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 9:
                                apiResponse.content.setLotDetail9(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 10:
                                apiResponse.content.setLotDetail10(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 11:
                                apiResponse.content.setLotDetail11(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 12:
                                apiResponse.content.setLotDetail12(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 13:
                                apiResponse.content.setLotDetail13(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            case 14:
                                apiResponse.content.setLotDetail14(apiResponse.content.getLotWeightDetail().get(i).toString());
                                break;
                            default:
                                System.out.println("Default case");
                        }
                    }
                }


//                apiResponse.content.setTotalcrates(String.valueOf(lotWeightDetails.size()));
                apiResponse.content.setTotalcrates(String.valueOf(apiResponse.content.getLotWeightDetail().size()));
                apiResponse.content.setTotalamount(String.valueOf(roundToWholeNumber(Double.parseDouble( apiResponse.content.getLotSoldOutAmount() ))));
//                                apiResponse.content.setTotalamount(String.valueOf(Math.round(Double.parseDouble("(" + apiResponse.content.getLotSoldOutAmount() + ")"))));

//                                String lotSoldOutAmountStr = apiResponse.content.getLotSoldOutAmount();
//                double lotSoldOutAmount = Double.parseDouble(lotSoldOutAmountStr);
//

            }
            apiResponse.content.setLogurl("/reports/Seal_of_Karnataka.PNG");
            if (apiResponse.content.getBidAmount().equals("0.0")) {
                apiResponse.content.setBidAmount("");
            } else {
                apiResponse.content.setBidAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getBidAmount()))));
            }
            if (apiResponse.content.getLotWeight().equals("0.0")) {
                apiResponse.content.setLotWeight("");
            } else {
                double doubleValue = Double.parseDouble(apiResponse.content.getLotWeight());
                String formattedValue = String.format("%.3f", doubleValue);
                apiResponse.content.setLotWeight(formattedValue);
            }
            if (apiResponse.content.getLotSoldOutAmount().equals("0.0")) {
                apiResponse.content.setLotSoldOutAmount("");
            } else {
                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));

//                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee() - apiResponse.content.getReelerMarketFee())));
            }
//            if (apiResponse.content.getFeespaid().equals("0.0+0.0=0.0")) {
//                apiResponse.content.setFeespaid("");
//            } else {
                System.out.println("Enter the first value:");
                String[] components = apiResponse.content.getFeespaid().split("[+=]");

                // Extract the symbols
                String additionSymbol = components[1]; // The addition symbol
                String equalitySymbol = components[2];
                int value1 = roundToWholeNumber(Double.parseDouble(additionSymbol));

                System.out.println("Enter the second value:");
                int value2 = roundToWholeNumber(Double.parseDouble(equalitySymbol));

                // Perform the addition
                double result = value1 + value2;

                // Round the result to the nearest integer
                int roundedResult = (int) Math.round(result);

                // Print the rounded result
                System.out.println("Rounded result: " + roundedResult);
//                apiResponse.content.setFeespaid(value1 + "+" + value2 + "=" + String.valueOf(roundedResult));
            //}
            if (!apiResponse.content.getBidAmount().equals("")) {
                apiResponse.content.setReeleramount("Balance: " + roundToWholeNumber(Double.parseDouble(apiResponse.content.getReelerbalance())));
            } else {
                apiResponse.content.setReeleramount("");
            }
            String markFee = "0";
            String totalFee = "0";
            if (apiResponse.content.getMarketFee() != null && !apiResponse.content.getMarketFee().equals("")) {
                markFee = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getMarketFee())));
            }
         else {
            markFee = "0"; // or any default value you prefer
        }
//            if (apiResponse.content.getTotalamount() != null && !apiResponse.content.getTotalamount().equals("")) {
//                totalFee = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount())));
//            }
//            else {
//                totalFee = "0"; // or any default value you prefer
//            }
////            String tot_amt = String.valueOf(roundToWholeNumber(Double.parseDouble(totalFee)) + roundToWholeNumber(Double.parseDouble(markFee)));
////            apiResponse.content.setReelerbalance("Lot value: " + roundToWholeNumber(Double.parseDouble(totalFee)) + "+" + roundToWholeNumber(Double.parseDouble(markFee)) + "=" + tot_amt);
//
//            String tot_amt = String.valueOf(roundToWholeNumber(Double.parseDouble(totalFee))  + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()));
//            apiResponse.content.setReelerbalance("Lot value: " + roundToWholeNumber(Double.parseDouble(totalFee)) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + tot_amt  );
//
            if (apiResponse.content.getTotalamount() != null && !apiResponse.content.getTotalamount().equals("")) {
                double totalAmount = Double.parseDouble(apiResponse.content.getTotalamount());
                totalFee = String.valueOf(Math.round(totalAmount)); // Convert to long
            } else {
                totalFee = "0"; // Default value
            }
            long marketFee = Math.round(apiResponse.content.getReelerMarketFee());

            String tot_amt = String.valueOf(Math.round(Double.parseDouble(totalFee)) + marketFee);
            apiResponse.content.setReelerbalance("Lot value: " + Math.round(Double.parseDouble(totalFee)) + "+" + marketFee + "=" + tot_amt);
            countries.add(apiResponse.content);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(countries);
    }

    private static int roundToWholeNumber(double value) {
        // Use BigDecimal for rounding to 2 decimal places


        // Extracting the fractional part
        double fractionalPart = value - Math.floor(value);

        // Rounding based on the fractional part
        int roundedNumber;
        if (fractionalPart < 0.5) {
            roundedNumber = (int) Math.floor(value);
        } else {
            roundedNumber = (int) Math.ceil(value);
        }

        return roundedNumber;
    }

    private static double roundToTwoDecimalPlaces(double value) {
        // Use BigDecimal for rounding to 2 decimal places
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static String roundToThreeDecimalPlaces(double value) {
        return String.format("%.3f", value);
    }

    public double parseDoubleOrDefault(String str, double defaultValue) {
        if (str == null || str.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue; // Return default value if parsing fails
        }
    }

    @PostMapping("/dtr-online-report")
    public ResponseEntity<?> dtrOnlineReport(@RequestBody DTROnlineRequest request) {

        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            JasperReport jasperReport = getJasperReport("dtr_online_report.jrxml");

            // 2. datasource "java object"
            JRBeanCollectionDataSource dataSource = getDtrOnlineReportData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private JRBeanCollectionDataSource getDtrOnlineReportData(DTROnlineRequest requestDto) throws JsonProcessingException {
        DTRReportResponse apiResponse = apiService.dtrReport(requestDto);
        DTROnlineReportUnitDetail content = new DTROnlineReportUnitDetail();
        String marketNameKannada = "";
        if (apiResponse.getContent().getMarketNameKannada() != null) {
            marketNameKannada = apiResponse.getContent().getMarketNameKannada();
        }
        if (apiResponse.getContent().getFarmerVillage() == null) {
            apiResponse.getContent().setFarmerVillage("");
        }
        if (apiResponse.getContent().getFarmerTaluk() == null) {
            apiResponse.getContent().setFarmerTaluk("");
        }


        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = requestDto.getToDate().format(formatter);
        content.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + marketNameKannada + " ದಿನವಹಿ ವಹಿವಾಟು ತಖ್ತೆ  : " + formattedDate);
//        content.setTotal_weight_with_amount_details("Wt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount()) + ", Farmer Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()) + ",MF: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Reeler Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
        content.setTotal_weight_with_amount_details(
                "Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()) +
                        " , Amount: " + (long) apiResponse.getContent().getTotalBidAmount() +
                        " , Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount() +
                        ", Farmer Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount() +
                        ", MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) +
                        ", Reeler Amt: " + (long) apiResponse.getContent().getTotalReelerAmount()
        );
        content.setTotal_lots("Total lots: " + apiResponse.getContent().getTotalLots());
        content.setTransacted_lots("Total Transacted Lots: " + apiResponse.getContent().getPaymentSuccessLots());
        if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
            content.setNot_transacted_lots("Not Transacted Lots: " +(apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
        } else {
            content.setNot_transacted_lots("Not Transacted Lots: 0");
        }
//        content.setFarmer_cheque("Farmer cheque Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
//        content.setMf_amount("MF Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//        content.setReeler_transaction_amt("Reeler transaction Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
        content.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
        content.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        content.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
        content.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
        content.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());
        content.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        content.setLotSoldOutAmount("Total Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());




        List<DTROnlineReportUnitDetail> contentList = new LinkedList<>();
        contentList.add(content);

        for (DTROnlineReportUnitDetail dtrOnlineReportUnitDetail : apiResponse.getContent().getDtrOnlineReportUnitDetailList()) {

            String farmerAddress = "";
            if (dtrOnlineReportUnitDetail.getFarmerAddress() != null) {
                farmerAddress = "/" + dtrOnlineReportUnitDetail.getFarmerAddress() + ",";
            }
            String villageName = "";
            if (dtrOnlineReportUnitDetail.getFarmerVillage() != null) {
                villageName = "/" + dtrOnlineReportUnitDetail.getFarmerVillage() + ",";
            }
            String talukName = "";
            if (dtrOnlineReportUnitDetail.getFarmerTaluk() != null) {
                talukName = "/" + dtrOnlineReportUnitDetail.getFarmerTaluk() + ",";
            }


            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getBankName() + "/" + dtrOnlineReportUnitDetail.getAccountNumber());
            dtrOnlineReportUnitDetail.setFarmerDetails(dtrOnlineReportUnitDetail.getFarmerFirstName() + " " + dtrOnlineReportUnitDetail.getFarmerMiddleName() + " " + dtrOnlineReportUnitDetail.getFarmerLastName() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + farmerAddress + " (" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ") "  +talukName + " ,  " + villageName );
            dtrOnlineReportUnitDetail.setReelerDetails(dtrOnlineReportUnitDetail.getReelerName() + "(" + dtrOnlineReportUnitDetail.getReelerLicense() + ")" + "(" + dtrOnlineReportUnitDetail.getReelerMobile() + ")");
            dtrOnlineReportUnitDetail.setMarketFee(String.valueOf((long) (Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee()))));
            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.valueOf((long) Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount())));
            dtrOnlineReportUnitDetail.setFarmerAmount(String.valueOf((long) Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount())));
            dtrOnlineReportUnitDetail.setReelerAmount(String.valueOf((long) Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount())));

            dtrOnlineReportUnitDetail.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + marketNameKannada + " ದಿನವಹಿ ವಹಿವಾಟು ತಖ್ತೆ  : " + formattedDate);
            //   dtrOnlineReportUnitDetail.setTotal_weight_with_amount_details("Wt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight())+" , Amount: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount())+", Farmer Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount())+ ",MF: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee())+", Reeler Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setTotal_lots("Total lots: " + apiResponse.getContent().getTotalLots());
            content.setTransacted_lots("Total Transacted Lots: " + apiResponse.getContent().getPaymentSuccessLots() );
            if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
                content.setNot_transacted_lots("Not Transacted Lots: "  + (apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
            } else {
                content.setNot_transacted_lots("Not Transacted Lots: 0");
            }
            dtrOnlineReportUnitDetail.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
            dtrOnlineReportUnitDetail.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
            dtrOnlineReportUnitDetail.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
            dtrOnlineReportUnitDetail.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
            dtrOnlineReportUnitDetail.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
            dtrOnlineReportUnitDetail.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());



            contentList.add(dtrOnlineReportUnitDetail);
        }
        DTROnlineReportUnitDetail contentLastColumn = new DTROnlineReportUnitDetail();
        contentLastColumn.setSerialNumber("");
        contentLastColumn.setAllottedLotId("");
        contentLastColumn.setFarmerDetails("");
        contentLastColumn.setWeight("Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()));
        contentLastColumn.setBidAmount("");
        contentLastColumn.setLotSoldOutAmount("Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());
        contentLastColumn.setFarmerAmount("F Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
        contentLastColumn.setMarketFee("MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());

        contentLastColumn.setReelerDetails("");
        contentLastColumn.setBankDetails("");
        contentLastColumn.setIfscCode("");
        contentLastColumn.setAccountNumber("");
        contentList.add(contentLastColumn);
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-bidding-report")
    public ResponseEntity<?> getBiddingReport(@RequestBody BiddingReportRequest request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("bidding_report_reeler.jrxml");

            // 2. datasource "java object"
            JRBeanCollectionDataSource dataSource = getBiddingReportData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

//    private JRBeanCollectionDataSource getBiddingReportData(BiddingReportRequest requestDto) throws Exception {
//        String marketName = "";
//        BiddingReportResponse apiResponse = apiService.biddingReport(requestDto);
//        List<LotReportResponse> contentList = new LinkedList<>();
//        LotReportResponse lotReportResponse1 = new LotReportResponse();
//
//        for (LotReportResponse lotReportResponse : apiResponse.getContent()) {
//            marketName = lotReportResponse.getMarketName();
//
//            if (lotReportResponse.getAcceptedBy() == null) {
//                lotReportResponse.setAcceptedBy("");
//            }
//            if (lotReportResponse.getAcceptedTime() == null) {
//                lotReportResponse.setAcceptedTime("");
//            } else {
//                lotReportResponse.setAcceptedTime(convertToTime(lotReportResponse.getAcceptedTime()));
//            }
//            if (lotReportResponse.getBidTime() == null) {
//                lotReportResponse.setBidTime("");
//            } else {
//                lotReportResponse.setBidTime(convertToTime(lotReportResponse.getBidTime()));
//            }
//            contentList.add(lotReportResponse);
//        }
//        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
//        lotReportResponse1.setHeaderText2("Lot Number = " + requestDto.getLotId() + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
//        contentList.add(0, lotReportResponse1);
//        return new JRBeanCollectionDataSource(contentList);
//    }

    private static final DateTimeFormatter TIME_FORMATTER_WITH_MILLIS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private JRBeanCollectionDataSource getBiddingReportData(BiddingReportRequest requestDto) throws Exception {
        String marketName = "";
        BiddingReportResponse apiResponse = apiService.biddingReport(requestDto);
        List<LotReportResponse> contentList = new LinkedList<>();
        LotReportResponse lotReportResponse1 = new LotReportResponse();

        for (LotReportResponse lotReportResponse : apiResponse.getContent()) {
            marketName = lotReportResponse.getMarketName();

            if (lotReportResponse.getAcceptedBy() == null) {
                lotReportResponse.setAcceptedBy("");
            }
            if (lotReportResponse.getAcceptedTime() == null) {
                lotReportResponse.setAcceptedTime("");
            } else {
                lotReportResponse.setAcceptedTime(convertToTimeWithMillis(lotReportResponse.getAcceptedTime()));
            }
            if (lotReportResponse.getBidTime() == null) {
                lotReportResponse.setBidTime("");
            } else {
                lotReportResponse.setBidTime(convertToTimeWithMillis(lotReportResponse.getBidTime()));
            }
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
        lotReportResponse1.setHeaderText2("Lot Number = " + requestDto.getLotId() + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(0, lotReportResponse1);
        lotReportResponse1.setSerialNumber("");
        contentList.add(lotReportResponse1);
        return new JRBeanCollectionDataSource(contentList);
    }

    private String convertToTimeWithMillis(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return localTime.format(TIME_FORMATTER_WITH_MILLIS);
    }


    @PostMapping("/get-reeler-bidding-report")
    public ResponseEntity<?> getReelerBiddingReport(@RequestBody ReelerBiddingReportRequest request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("bidding_report_reeler.jrxml");

            // 2. datasource "java object"
            JRBeanCollectionDataSource dataSource = getReelerBiddingReportData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private JRBeanCollectionDataSource getReelerBiddingReportData(ReelerBiddingReportRequest requestDto) throws Exception {
        String marketName = "";
        BiddingReportResponse apiResponse = apiService.reelerBiddingReport(requestDto);
        List<LotReportResponse> contentList = new LinkedList<>();
        LotReportResponse lotReportResponse1 = new LotReportResponse();
        for (LotReportResponse lotReportResponse : apiResponse.getContent()) {
            marketName = lotReportResponse.getMarketName();
            if (lotReportResponse.getAcceptedBy() == null) {
                lotReportResponse.setAcceptedBy("");
            }
            if (lotReportResponse.getAcceptedTime() == null) {
                lotReportResponse.setAcceptedTime("");
            } else {
                lotReportResponse.setAcceptedTime(convertToTimeWithMillis(lotReportResponse.getAcceptedTime()));
            }
            if (lotReportResponse.getBidTime() == null) {
                lotReportResponse.setBidTime("");
            } else {
                lotReportResponse.setBidTime(convertToTimeWithMillis(lotReportResponse.getBidTime()));
            }
            lotReportResponse.setAuctionNumber(Util.objectToInteger(lotReportResponse.getAuctionSession()));
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
        lotReportResponse1.setHeaderText2("Reeler Id = " + (requestDto.getReelerNumber() != null && !requestDto.getReelerNumber().equals("") ? requestDto.getReelerNumber() : "All") + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(0, lotReportResponse1);
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-pending-report")
    public ResponseEntity<?> getPendingReports(@RequestBody PendingReportRequest request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("pending_report.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getPendingReportsData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private JRBeanCollectionDataSource getPendingReportsData(PendingReportRequest requestDto) throws JsonProcessingException {
        PendingReportResponse apiResponse = apiService.pendingReportList(requestDto);
        List<LotPendingReportResponse> contentList = new LinkedList<>();
        LotPendingReportResponse lotReportResponse1 = new LotPendingReportResponse();
        lotReportResponse1.setHeaderText("Pending report for " + convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(lotReportResponse1);
        for (LotPendingReportResponse lotReportResponse : apiResponse.getContent()) {
            lotReportResponse.setAccpetedBy(lotReportResponse.getAcceptedBy());
            if (lotReportResponse.getShed() == null) {
                lotReportResponse.setShed("");
            }
            if (lotReportResponse.getReelerNumber() == null) {
                lotReportResponse.setReelerNumber("");
            }
            if (lotReportResponse.getReelerMobileNumber() == null) {
                lotReportResponse.setReelerMobileNumber("");
            }
            if (lotReportResponse.getReelerLicense() == null) {
                lotReportResponse.setReelerLicense("");
            }
            if (lotReportResponse.getAccpetedBy() == null) {
                lotReportResponse.setAccpetedBy("");
            }

            lotReportResponse.setReeler_amount(String.valueOf(lotReportResponse.getReelerAmount()));

            lotReportResponse.setFarmerDetails(lotReportResponse.getFarmerFirstName() + " " + lotReportResponse.getFarmerMiddleName() + " " + lotReportResponse.getFarmerLastName());

            lotReportResponse.setSerialNumber(String.valueOf(lotReportResponse.getSerailNumberForPagination()));
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-farmer-txn-report")
    public ResponseEntity<?> getFarmerTxnReport(@RequestBody FarmerTxnRequest request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("farmer_transaction.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getFarmerTxnReportsData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    @PostMapping("/get-reeler-pending-report")
    public ResponseEntity<?> getFarmerTxnReport(@RequestBody com.sericulture.model.RequestBody request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("reeler_pending_report.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getReelerPendingReport(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    @PostMapping("/get-dashboard-report")
    public ResponseEntity<?> getDashboardCountReports(@RequestBody DashboardReportRequest request) {

        try {
            System.out.println("enter to dashboard report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("dashboard_report.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getDashboardReportCount(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private JRBeanCollectionDataSource getDashboardReportCount(DashboardReportRequest requestDto) throws JsonProcessingException {
        DashboardResponse apiResponse = apiService.getDashboardReport(requestDto);
        List<DashboardReportInfo> contentList = new LinkedList<>();
        DashboardReportInfo lotReportResponse1 = new DashboardReportInfo();
        String bidStarted = "Not started";
        if (apiResponse.getContent().getAuctionStarted().equals("true")) {
            bidStarted = "Started";
        }
        String acceptanceStarted = "Not started";
        if (apiResponse.getContent().getAcceptanceStarted().equals("true")) {
            acceptanceStarted = "Started";
        }
        lotReportResponse1.setHeaderText("Dashboard " + apiResponse.getContent().getMarketName() + "- " + convertDate(String.valueOf(requestDto.getDashboardReportDate())) + "\n Bidding Status: " + bidStarted + "\n Acceptance Status: " + acceptanceStarted);
        contentList.add(lotReportResponse1);
        for (DashboardReportInfo lotReportResponse : apiResponse.getContent().getDashboardReportInfoList()) {
            lotReportResponse.setWeighedLots(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getWeighedLots()))));
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    private JRBeanCollectionDataSource getFarmerTxnReportsData(FarmerTxnRequest requestDto) throws JsonProcessingException {
        FarmerTxnResponse apiResponse = apiService.farmerTxnReportList(requestDto);
        List<FarmerTxnInfo> contentList = new LinkedList<>();
        FarmerTxnInfo lotReportResponse1 = new FarmerTxnInfo();
        lotReportResponse1.setHeaderText("e-Haraju Farmer Transaction Report - " + requestDto.getFarmerNumber() + " \n From " + convertDate(requestDto.getReportFromDate().toString()) + " to " + convertDate(requestDto.getReportToDate().toString()));
        lotReportResponse1.setFarmer_details_farmer_transaction("Farmer Details: " + apiResponse.getContent().getFarmerNumber() + " " + apiResponse.getContent().getFarmerFirstName() + " " + apiResponse.getContent().getFarmerMiddleName() + " " + apiResponse.getContent().getFarmerLastName() + "," + apiResponse.getContent().getVillage());
        lotReportResponse1.setTotal_sale_amount_farmer_transaction("Total sale amount: Rs." + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalSaleAmount()));
        lotReportResponse1.setTotal_market_fee_farmer_transaction("Total market fee: Rs." + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalMarketFee()));
        lotReportResponse1.setTotal_amount_farmer_transaction("Total amount: Rs." + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
        contentList.add(lotReportResponse1);
        for (FarmerTxnInfo lotReportResponse : apiResponse.getContent().getFarmerTxnInfoList()) {
            lotReportResponse.setLotTransactionDate(convertDate(lotReportResponse.getLotTransactionDate()));
            lotReportResponse.setFarmerDetails("");
            lotReportResponse.setFarmerAmount(roundToTwoDecimalPlaces(lotReportResponse.getFarmerAmount()));
            lotReportResponse.setFarmerMarketFee(roundToTwoDecimalPlaces(lotReportResponse.getFarmerMarketFee()));
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    private JRBeanCollectionDataSource getReelerPendingReport(com.sericulture.model.RequestBody requestDto) throws JsonProcessingException {
        ReelerPendingReposne apiResponse = apiService.getReelerPendingReport(requestDto);
        List<ReelerPendingInfo> contentList = new LinkedList<>();
        ReelerPendingInfo lotReportResponse1 = new ReelerPendingInfo();
        if (apiResponse.getContent().getReelerPendingInfoList().size() > 0) {
            lotReportResponse1.setHeaderText("Government Cocoon Market, " + apiResponse.getContent().getMarketName() + "\n Reeler balance report " + convertDate(String.valueOf(LocalDate.now())));
            lotReportResponse1.setDebit("Reeler deposit today: " + ((apiResponse.getContent().getCreditTotal() != null) ? apiResponse.getContent().getCreditTotal() : ""));
            lotReportResponse1.setCredit("Reeler credit balance: " + ((apiResponse.getContent().getBalance() != null) ? apiResponse.getContent().getBalance() : ""));
            lotReportResponse1.setDeposit("Reeler debit balance: " + ((apiResponse.getContent().getDebitTotal() != null) ? apiResponse.getContent().getDebitTotal() : ""));
            contentList.add(lotReportResponse1);
        }
        for (ReelerPendingInfo lotReportResponse : apiResponse.getContent().getReelerPendingInfoList()) {
            lotReportResponse1.setHeaderText("Government Cocoon Market, " + apiResponse.getContent().getMarketName() + "\n Reeler balance report " + convertDate(String.valueOf(LocalDate.now())));
            lotReportResponse1.setDebit("Reeler deposit today: " + ((apiResponse.getContent().getCreditTotal() != null) ? apiResponse.getContent().getCreditTotal() : ""));
            lotReportResponse1.setCredit("Reeler credit balance: " + ((apiResponse.getContent().getBalance() != null) ? apiResponse.getContent().getBalance() : ""));
            lotReportResponse1.setDeposit("Reeler debit balance: " + ((apiResponse.getContent().getDebitTotal() != null) ? apiResponse.getContent().getDebitTotal() : ""));
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-reeler-txn-report")
    public ResponseEntity<?> getReelerTxnReport(@RequestBody ReelerTxnRequest request) {

        try {
            System.out.println("enter to bidding report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("reeler_transaction.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getReelerTxnReportsData(request);

            // 3. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private JRBeanCollectionDataSource getReelerTxnReportsData(ReelerTxnRequest requestDto) throws JsonProcessingException {
        ReelerTxnResponse apiResponse = apiService.reelerTxnReportList(requestDto);
        List<ReelerTransactionReport> contentList = new LinkedList<>();
        ReelerTransactionReport lotReportResponse1 = new ReelerTransactionReport();
        lotReportResponse1.setTotal_sale_amount_farmer_transaction("Total purchase Rs." + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalPurchase()));
        lotReportResponse1.setFarmer_details_farmer_transaction("Deposited Rs." + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalDeposits()));
        lotReportResponse1.setHeaderText("e-Haraju Reeler Transaction Report " + requestDto.getReelerNumber() + " \n From " + convertDate(requestDto.getReportFromDate().toString()) + " to " + convertDate(requestDto.getReportToDate().toString()));
        lotReportResponse1.setReeler_amount_balance("Opening Balance of Reeler Id " + requestDto.getReelerNumber() + ", Name " + apiResponse.getContent().getName() + " as on " + convertDate(requestDto.getReportFromDate().toString()) + " is Rs." + apiResponse.getContent().getOpeningBalance());
        contentList.add(lotReportResponse1);
        for (ReelerTransactionReport lotReportResponse : apiResponse.getContent().getReelerTransactionReports()) {
            if (lotReportResponse.getDepositAmount() != null) {
                lotReportResponse.setDepositAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getDepositAmount()))));
            } else {
                lotReportResponse.setDepositAmount("");
            }
            if (lotReportResponse.getPaymentAmount() != null) {
                lotReportResponse.setPaymentAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getPaymentAmount()))));
            } else {
                lotReportResponse.setPaymentAmount("");
            }
            if (lotReportResponse.getBalance() != null) {
                lotReportResponse.setBalance(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getBalance()))));
            } else {
                lotReportResponse.setBalance("");
            }
            if (lotReportResponse.getTransactionDate() != null && !lotReportResponse.getTransactionDate().equals("")) {
                lotReportResponse.setTransactionDate(convertDate(lotReportResponse.getTransactionDate()));
            }
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

@PostMapping("/get-form-13-report")
public ResponseEntity<byte[]> getForm13Report(@RequestBody Form13Request request) {
    try {
        System.out.println("enter to form 14");
        logger.info("enter to form 13");
        String destFileName = "report_kannada.pdf";
        JasperReport jasperReport = getJasperReport("form_13_cb_report.jrxml");

        Form13ReportResponse apiResponse = apiService.getForm13Report(request);
        List<GroupLotStatus> groupStateLotStatuses = new ArrayList<>();

        for(int i=0; i<apiResponse.getContent().getStateWiseLotStatus().size(); i++) {
            GroupLotStatus groupLotStateStatus = new GroupLotStatus();
            groupLotStateStatus.setStateName(apiResponse.getContent().getStateWiseLotStatus().get(i).getDescription());
            groupLotStateStatus.setLot21(apiResponse.getContent().getStateWiseLotStatus().get(i).getLot());

            String weight = apiResponse.getContent().getStateWiseLotStatus().get(i).getWeight();
            groupLotStateStatus.setWeight21(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(weight, 0))));

            String amount = apiResponse.getContent().getStateWiseLotStatus().get(i).getAmount();
            groupLotStateStatus.setAmount21(String.valueOf((long) parseDoubleOrDefault(amount, 0)));

            String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
            groupLotStateStatus.setMax21(String.valueOf((long) parseDoubleOrDefault(max, 0)));

            String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
            groupLotStateStatus.setMin21(String.valueOf((long) parseDoubleOrDefault(min, 0)));

            String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
            groupLotStateStatus.setAvg21(String.valueOf((long) parseDoubleOrDefault(avg, 0)));

            String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
            groupLotStateStatus.setMf21(String.valueOf((long) parseDoubleOrDefault(mf, 0)));

            groupStateLotStatuses.add(groupLotStateStatus);
        }

        List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
        if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
            for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                groupLotGenderStatus.setAmount41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0)));
                groupLotGenderStatus.setMax41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0)));
                groupLotGenderStatus.setMin41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0)));
                groupLotGenderStatus.setAvg41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0)));
                groupLotGenderStatus.setMf41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0)));
                groupGenderLotStatuses.add(groupLotGenderStatus);
            }
        }

        List<GroupLotStatus> groupRaceLotStatuses = new ArrayList<>();
        if (apiResponse.getContent().getRaceWiseLotStatus().size() > 0) {
            for (int i = 0; i < apiResponse.getContent().getRaceWiseLotStatus().size(); i++) {
                GroupLotStatus groupLotRaceStatus = new GroupLotStatus();
                groupLotRaceStatus.setRaceName(apiResponse.getContent().getRaceWiseLotStatus().get(i).getDescription());
                groupLotRaceStatus.setLot31(apiResponse.getContent().getRaceWiseLotStatus().get(i).getLot());
                groupLotRaceStatus.setWeight31(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getWeight(), 0))));
                groupLotRaceStatus.setAmount31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0)));
                groupLotRaceStatus.setMax31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0)));
                groupLotRaceStatus.setMin31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0)));
                groupLotRaceStatus.setAvg31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0)));
                groupLotRaceStatus.setMf31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0)));
                groupRaceLotStatuses.add(groupLotRaceStatus);
            }
        }


        // 2. parameters "empty"
        Map<String, Object> parameters = getParameters();

        // 3. datasource "java object"
        JRDataSource dataSource = getForm13Data(request);
        parameters.put("datasource1", groupStateLotStatuses);
        parameters.put("datasource2", groupRaceLotStatuses);
        parameters.put("datasource3", groupGenderLotStatuses);
//        parameters.put("datasource4", groupLotTotalStatuses);

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

    } catch (JRException ex) {
        logger.error("Error generating Form 13 report", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }
}


    private JRBeanCollectionDataSource getForm13Data(Form13Request requestDto) throws JsonProcessingException {
        Form13ReportResponse apiResponse = apiService.getForm13Report(requestDto);

        if (apiResponse.getContent().getLotsFrom0to351().size() > 0) {
            int totalLot1 = 0;
            for (int i = 0; i < apiResponse.getContent().getLotsFrom0to351().size(); i++) {
                if (apiResponse.getContent().getLotsFrom0to351().get(i).getLot() != null && !apiResponse.getContent().getLotsFrom0to351().get(i).getLot().equals("")) {
                    totalLot1 = Integer.parseInt(apiResponse.getContent().getLotsFrom0to351().get(i).getLot());
                }
            }

            apiResponse.setLot1(apiResponse.getContent().getLotsFrom0to351().get(0).getLot());
            apiResponse.setLot2(apiResponse.getContent().getLotsFrom0to351().get(1).getLot());
            apiResponse.setLot3(apiResponse.getContent().getLotsFrom0to351().get(2).getLot());
            apiResponse.setLot4(apiResponse.getContent().getLotsFrom0to351().get(3).getLot());
            apiResponse.setLot5(apiResponse.getContent().getLotsFrom0to351().get(4).getLot());
            apiResponse.setLot6(apiResponse.getContent().getLotsFrom0to351().get(5).getLot());
            apiResponse.setLot7(apiResponse.getContent().getLotsFrom0to351().get(6).getLot());
            apiResponse.setTotalLot1(String.valueOf(totalLot1));

            double totalWeight1 = 0;
            for (int i = 0; i < apiResponse.getContent().getLotsFrom0to351().size(); i++) {
                if (apiResponse.getContent().getLotsFrom0to351().get(i).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(i).getWeight().equals("")) {
                    totalWeight1 = Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(i).getWeight());
                }
            }

            apiResponse.setWeight1((apiResponse.getContent().getLotsFrom0to351().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(0).getWeight()))) : "");
            apiResponse.setWeight2((apiResponse.getContent().getLotsFrom0to351().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(1).getWeight()))) : "");
            apiResponse.setWeight3((apiResponse.getContent().getLotsFrom0to351().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(2).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(2).getWeight()))) : "");
            apiResponse.setWeight4((apiResponse.getContent().getLotsFrom0to351().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(3).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(3).getWeight()))) : "");
            apiResponse.setWeight5((apiResponse.getContent().getLotsFrom0to351().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(4).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(4).getWeight()))) : "");
            apiResponse.setWeight6((apiResponse.getContent().getLotsFrom0to351().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(5).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(5).getWeight()))) : "");
            apiResponse.setWeight7((apiResponse.getContent().getLotsFrom0to351().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(6).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(6).getWeight()))) : "");
            apiResponse.setTotalWeight1(String.valueOf(roundToThreeDecimalPlaces(totalWeight1)));


            apiResponse.setPerc1((apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage()))) : "");
            apiResponse.setPerc2((apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage()))) : "");
            apiResponse.setPerc3((apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage()))) : "");
            apiResponse.setPerc4((apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage()))) : "");
            apiResponse.setPerc5((apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage()))) : "");
            apiResponse.setPerc6((apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage()))) : "");
            apiResponse.setPerc7((apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage()))) : "");
        }

        if (apiResponse.getContent().getLotsFrom201to300().size() > 0) {
            apiResponse.setLot41(apiResponse.getContent().getLotsFrom201to300().get(0).getLot());
            apiResponse.setLot42(apiResponse.getContent().getLotsFrom201to300().get(1).getLot());
            apiResponse.setLot43(apiResponse.getContent().getLotsFrom201to300().get(2).getLot());
            apiResponse.setLot44(apiResponse.getContent().getLotsFrom201to300().get(3).getLot());
            apiResponse.setLot45(apiResponse.getContent().getLotsFrom201to300().get(4).getLot());
            apiResponse.setLot46(apiResponse.getContent().getLotsFrom201to300().get(5).getLot());
            apiResponse.setLot47(apiResponse.getContent().getLotsFrom201to300().get(6).getLot());

            apiResponse.setWeight41((apiResponse.getContent().getLotsFrom201to300().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(0).getWeight()))) : "");
            apiResponse.setWeight42((apiResponse.getContent().getLotsFrom201to300().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(1).getWeight()))) : "");
            apiResponse.setWeight43((apiResponse.getContent().getLotsFrom201to300().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(2).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(2).getWeight()))) : "");
            apiResponse.setWeight44((apiResponse.getContent().getLotsFrom201to300().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(3).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(3).getWeight()))) : "");
            apiResponse.setWeight45((apiResponse.getContent().getLotsFrom201to300().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(4).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(4).getWeight()))) : "");
            apiResponse.setWeight46((apiResponse.getContent().getLotsFrom201to300().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(5).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(5).getWeight()))) : "");
            apiResponse.setWeight47((apiResponse.getContent().getLotsFrom201to300().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(6).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(6).getWeight()))) : "");

            apiResponse.setPerc41((apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage()))) : "");
            apiResponse.setPerc42((apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage()))) : "");
            apiResponse.setPerc43((apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage()))) : "");
            apiResponse.setPerc44((apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage()))) : "");
            apiResponse.setPerc45((apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage()))) : "");
            apiResponse.setPerc46((apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage()))) : "");
            apiResponse.setPerc47((apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage()))) : "");
        }

//        if (apiResponse.getContent().getTotalLotStatus().size() > 0) {
//            apiResponse.setLot11(apiResponse.getContent().getTotalLotStatus().get(0).getLot());
//            apiResponse.setWeight11(String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getWeight()))));
//            apiResponse.setAmount11(String.valueOf((long) Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAmount())));
//            apiResponse.setMax11(String.valueOf((long) Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMax())));
//            apiResponse.setMin11(String.valueOf((long) Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMin())));
//            apiResponse.setMf11(String.valueOf((long) Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMf())));
//            apiResponse.setAvg11(String.valueOf((long) Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAvg())));
//        }
        if (apiResponse.getContent().getTotalLotStatus().size() > 0) {
            apiResponse.setLot11(apiResponse.getContent().getTotalLotStatus().get(0).getLot());
            apiResponse.setWeight11(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getWeight(),0))));
            apiResponse.setAmount11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0)));
            apiResponse.setMax11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0)));
            apiResponse.setMin11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0)));
            apiResponse.setMf11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0)));
            apiResponse.setAvg11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0)));
        }

        if (apiResponse.getContent().getAverageLotStatus().size() > 0) {
            apiResponse.setAverageDesc1(apiResponse.getContent().getAverageLotStatus().get(0).getDescription());
            apiResponse.setLot51(apiResponse.getContent().getAverageLotStatus().get(0).getLot());
            apiResponse.setWeight51((apiResponse.getContent().getAverageLotStatus().get(0).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getWeight()))) : "");
            apiResponse.setPerc51((apiResponse.getContent().getAverageLotStatus().get(0).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getPercentage()))) : "");

            apiResponse.setAverageDesc2(apiResponse.getContent().getAverageLotStatus().get(1).getDescription());
            apiResponse.setLot52(apiResponse.getContent().getAverageLotStatus().get(1).getLot());
            apiResponse.setWeight52((apiResponse.getContent().getAverageLotStatus().get(1).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getWeight()))) : "");
            apiResponse.setPerc52((apiResponse.getContent().getAverageLotStatus().get(1).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getPercentage()))) : "");
        }

        List<Form13ReportResponse> form13ReportResponses = new LinkedList<>();
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + apiResponse.getContent().getMarketNameKannada() + " ದಿನವಹಿ ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(String.valueOf(requestDto.getAuctionDate())));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0)));
        apiResponse.setTotalStateLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0)));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0)));
        apiResponse.setTotalStateMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0)));
        apiResponse.setTotalStateMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0)));
        apiResponse.setTotalStateMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0)));
        apiResponse.setTotalStateAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0)));

        apiResponse.setTotalGenderLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0)));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0)));
        apiResponse.setTotalGenderMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0)));
        apiResponse.setTotalGenderMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0)));
        apiResponse.setTotalGenderMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0)));
        apiResponse.setTotalGenderAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0)));

        apiResponse.setTotalRaceLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0)));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0)));
        apiResponse.setTotalRaceMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0)));
        apiResponse.setTotalRaceMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0)));
        apiResponse.setTotalRaceMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0)));
        apiResponse.setTotalRaceAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0)));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0)));
        apiResponse.setTotalWeight2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalPercentage2(String.valueOf(roundToTwoDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalPercentage(), 0))));

        form13ReportResponses.add(apiResponse);
        return new JRBeanCollectionDataSource(form13ReportResponses);
    }

    @PostMapping("/get-form-13-report-by-dist")
    public ResponseEntity<byte[]> getForm13ReportByDist(@RequestBody Form13Request request) {
        try {
            System.out.println("enter to form 14");
            logger.info("enter to form 13");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("form_13_cb_report.jrxml");


            Form13ReportResponse apiResponse = apiService.getForm13ReportByDist(request);
            List<GroupLotStatus> groupStateLotStatuses = new ArrayList<>();
            for(int i=0; i<apiResponse.getContent().getStateWiseLotStatus().size(); i++) {
                GroupLotStatus groupLotStateStatus = new GroupLotStatus();
                groupLotStateStatus.setStateName(apiResponse.getContent().getStateWiseLotStatus().get(i).getDescription());
                groupLotStateStatus.setLot21(apiResponse.getContent().getStateWiseLotStatus().get(i).getLot());

                String weight = apiResponse.getContent().getStateWiseLotStatus().get(i).getWeight();
                groupLotStateStatus.setWeight21(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(weight, 0))));

                String amount = apiResponse.getContent().getStateWiseLotStatus().get(i).getAmount();
                groupLotStateStatus.setAmount21(String.valueOf((long) parseDoubleOrDefault(amount, 0)));

                String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
                groupLotStateStatus.setMax21(String.valueOf((long) parseDoubleOrDefault(max, 0)));

                String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
                groupLotStateStatus.setMin21(String.valueOf((long) parseDoubleOrDefault(min, 0)));

                String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
                groupLotStateStatus.setAvg21(String.valueOf((long) parseDoubleOrDefault(avg, 0)));

                String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
                groupLotStateStatus.setMf21(String.valueOf((long) parseDoubleOrDefault(mf, 0)));

                groupStateLotStatuses.add(groupLotStateStatus);
            }

            List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                    groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                    groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                    groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotGenderStatus.setAmount41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0)));
                    groupLotGenderStatus.setMax41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0)));
                    groupLotGenderStatus.setMin41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0)));
                    groupLotGenderStatus.setAvg41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0)));
                    groupLotGenderStatus.setMf41(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0)));
                    groupGenderLotStatuses.add(groupLotGenderStatus);
                }
            }

            List<GroupLotStatus> groupRaceLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getRaceWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getRaceWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotRaceStatus = new GroupLotStatus();
                    groupLotRaceStatus.setRaceName(apiResponse.getContent().getRaceWiseLotStatus().get(i).getDescription());
                    groupLotRaceStatus.setLot31(apiResponse.getContent().getRaceWiseLotStatus().get(i).getLot());
                    groupLotRaceStatus.setWeight31(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotRaceStatus.setAmount31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0)));
                    groupLotRaceStatus.setMax31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0)));
                    groupLotRaceStatus.setMin31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0)));
                    groupLotRaceStatus.setAvg31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0)));
                    groupLotRaceStatus.setMf31(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0)));
                    groupRaceLotStatuses.add(groupLotRaceStatus);
                }
            }


            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getForm13DataByDistService(request);
            parameters.put("datasource1", groupStateLotStatuses);
            parameters.put("datasource2", groupRaceLotStatuses);
            parameters.put("datasource3", groupGenderLotStatuses);
//            parameters.put("datasource4", groupLotTotalStatuses);

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

        } catch (JRException ex) {
            logger.error("Error generating Form 13 report", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private JRBeanCollectionDataSource getForm13DataByDistService(Form13Request requestDto) throws JsonProcessingException {
        Form13ReportResponse apiResponse = apiService.getForm13ReportByDist(requestDto);

        if (apiResponse.getContent().getLotsFrom0to351().size() > 0) {
            int totalLot1 = 0;
            for (int i = 0; i < apiResponse.getContent().getLotsFrom0to351().size(); i++) {
                if (apiResponse.getContent().getLotsFrom0to351().get(i).getLot() != null && !apiResponse.getContent().getLotsFrom0to351().get(i).getLot().equals("")) {
                    totalLot1 = Integer.parseInt(apiResponse.getContent().getLotsFrom0to351().get(i).getLot());
                }
            }

            apiResponse.setLot1(apiResponse.getContent().getLotsFrom0to351().get(0).getLot());
            apiResponse.setLot2(apiResponse.getContent().getLotsFrom0to351().get(1).getLot());
            apiResponse.setLot3(apiResponse.getContent().getLotsFrom0to351().get(2).getLot());
            apiResponse.setLot4(apiResponse.getContent().getLotsFrom0to351().get(3).getLot());
            apiResponse.setLot5(apiResponse.getContent().getLotsFrom0to351().get(4).getLot());
            apiResponse.setLot6(apiResponse.getContent().getLotsFrom0to351().get(5).getLot());
            apiResponse.setLot7(apiResponse.getContent().getLotsFrom0to351().get(6).getLot());
            apiResponse.setTotalLot1(String.valueOf(totalLot1));

            double totalWeight1 = 0;
            for (int i = 0; i < apiResponse.getContent().getLotsFrom0to351().size(); i++) {
                if (apiResponse.getContent().getLotsFrom0to351().get(i).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(i).getWeight().equals("")) {
                    totalWeight1 = Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(i).getWeight());
                }
            }

            apiResponse.setWeight1((apiResponse.getContent().getLotsFrom0to351().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(0).getWeight()))) : "");
            apiResponse.setWeight2((apiResponse.getContent().getLotsFrom0to351().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(1).getWeight()))) : "");
            apiResponse.setWeight3((apiResponse.getContent().getLotsFrom0to351().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(2).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(2).getWeight()))) : "");
            apiResponse.setWeight4((apiResponse.getContent().getLotsFrom0to351().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(3).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(3).getWeight()))) : "");
            apiResponse.setWeight5((apiResponse.getContent().getLotsFrom0to351().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(4).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(4).getWeight()))) : "");
            apiResponse.setWeight6((apiResponse.getContent().getLotsFrom0to351().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(5).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(5).getWeight()))) : "");
            apiResponse.setWeight7((apiResponse.getContent().getLotsFrom0to351().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(6).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(6).getWeight()))) : "");
            apiResponse.setTotalWeight1(String.valueOf(roundToThreeDecimalPlaces(totalWeight1)));


            apiResponse.setPerc1((apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(0).getPercentage()))) : "");
            apiResponse.setPerc2((apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(1).getPercentage()))) : "");
            apiResponse.setPerc3((apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(2).getPercentage()))) : "");
            apiResponse.setPerc4((apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(3).getPercentage()))) : "");
            apiResponse.setPerc5((apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(4).getPercentage()))) : "");
            apiResponse.setPerc6((apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(5).getPercentage()))) : "");
            apiResponse.setPerc7((apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage() != null && !apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(6).getPercentage()))) : "");
        }

        if (apiResponse.getContent().getLotsFrom201to300().size() > 0) {
            apiResponse.setLot41(apiResponse.getContent().getLotsFrom201to300().get(0).getLot());
            apiResponse.setLot42(apiResponse.getContent().getLotsFrom201to300().get(1).getLot());
            apiResponse.setLot43(apiResponse.getContent().getLotsFrom201to300().get(2).getLot());
            apiResponse.setLot44(apiResponse.getContent().getLotsFrom201to300().get(3).getLot());
            apiResponse.setLot45(apiResponse.getContent().getLotsFrom201to300().get(4).getLot());
            apiResponse.setLot46(apiResponse.getContent().getLotsFrom201to300().get(5).getLot());
            apiResponse.setLot47(apiResponse.getContent().getLotsFrom201to300().get(6).getLot());

            apiResponse.setWeight41((apiResponse.getContent().getLotsFrom201to300().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(0).getWeight()))) : "");
            apiResponse.setWeight42((apiResponse.getContent().getLotsFrom201to300().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(1).getWeight()))) : "");
            apiResponse.setWeight43((apiResponse.getContent().getLotsFrom201to300().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(2).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(2).getWeight()))) : "");
            apiResponse.setWeight44((apiResponse.getContent().getLotsFrom201to300().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(3).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(3).getWeight()))) : "");
            apiResponse.setWeight45((apiResponse.getContent().getLotsFrom201to300().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(4).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(4).getWeight()))) : "");
            apiResponse.setWeight46((apiResponse.getContent().getLotsFrom201to300().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(5).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(5).getWeight()))) : "");
            apiResponse.setWeight47((apiResponse.getContent().getLotsFrom201to300().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(6).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(6).getWeight()))) : "");

            apiResponse.setPerc41((apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(0).getPercentage()))) : "");
            apiResponse.setPerc42((apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(1).getPercentage()))) : "");
            apiResponse.setPerc43((apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(2).getPercentage()))) : "");
            apiResponse.setPerc44((apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(3).getPercentage()))) : "");
            apiResponse.setPerc45((apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(4).getPercentage()))) : "");
            apiResponse.setPerc46((apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(5).getPercentage()))) : "");
            apiResponse.setPerc47((apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage() != null && !apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(6).getPercentage()))) : "");
        }

        if (apiResponse.getContent().getTotalLotStatus().size() > 0) {
            apiResponse.setLot11(apiResponse.getContent().getTotalLotStatus().get(0).getLot());
            apiResponse.setWeight11(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getWeight(),0))));
            apiResponse.setAmount11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0)));
            apiResponse.setMax11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0)));
            apiResponse.setMin11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0)));
            apiResponse.setMf11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0)));
            apiResponse.setAvg11(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0)));
        }

        if (apiResponse.getContent().getAverageLotStatus().size() > 0) {
            apiResponse.setAverageDesc1(apiResponse.getContent().getAverageLotStatus().get(0).getDescription());
            apiResponse.setLot51(apiResponse.getContent().getAverageLotStatus().get(0).getLot());
            apiResponse.setWeight51((apiResponse.getContent().getAverageLotStatus().get(0).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getWeight()))) : "");
            apiResponse.setPerc51((apiResponse.getContent().getAverageLotStatus().get(0).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getPercentage()))) : "");

            apiResponse.setAverageDesc2(apiResponse.getContent().getAverageLotStatus().get(1).getDescription());
            apiResponse.setLot52(apiResponse.getContent().getAverageLotStatus().get(1).getLot());
            apiResponse.setWeight52((apiResponse.getContent().getAverageLotStatus().get(1).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getWeight().equals("")) ? String.valueOf(roundToThreeDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getWeight()))) : "");
            apiResponse.setPerc52((apiResponse.getContent().getAverageLotStatus().get(1).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getPercentage()))) : "");
        }

        List<Form13ReportResponse> form13ReportResponses = new LinkedList<>();
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + apiResponse.getContent().getMarketNameKannada() + " ದಿನವಹಿ ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(String.valueOf(requestDto.getAuctionDate())));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0)));
        apiResponse.setTotalStateLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0)));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0)));
        apiResponse.setTotalStateMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0)));
        apiResponse.setTotalStateMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0)));
        apiResponse.setTotalStateMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0)));
        apiResponse.setTotalStateAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0)));

        apiResponse.setTotalGenderLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0)));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0)));
        apiResponse.setTotalGenderMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0)));
        apiResponse.setTotalGenderMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0)));
        apiResponse.setTotalGenderMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0)));
        apiResponse.setTotalGenderAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0)));

        apiResponse.setTotalRaceLots(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0)));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0)));
        apiResponse.setTotalRaceMarketFee(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0)));
        apiResponse.setTotalRaceMin(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0)));
        apiResponse.setTotalRaceMax(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0)));
        apiResponse.setTotalRaceAvg(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0)));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf((long) parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0)));
        apiResponse.setTotalWeight2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalPercentage2(String.valueOf(roundToTwoDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalPercentage(), 0))));


        form13ReportResponses.add(apiResponse);
        return new JRBeanCollectionDataSource(form13ReportResponses);
    }


    @PostMapping("/get-dtr-all-market-report")
    public ResponseEntity<byte[]> getDTRAllMarketReport(@RequestBody Form13Request request) {
        try {
            System.out.println("enter to form 14");
            logger.info("enter to form 13");
            String destFileName = "report_kannada.pdf";
//            JasperReport jasperReport = getJasperReport("dtr_all_market.jrxml");
            JasperReport jasperReport = getJasperReport("dtr_mark.jrxml");

            DTRAllMarketResponse apiResponse = apiService.dtrAllReport(request);

            List<DTRRaceWithDetails> dtrRaceWithDetails = new ArrayList<>();

            for(int i=0; i<apiResponse.getContent().getDtrDataResponse().getDtrMarketResponses().size(); i++){
                DTRMarketResponse dtrMarketResponse = apiResponse.getContent().getDtrDataResponse().getDtrMarketResponses().get(i);
                for(int j=0; j<dtrMarketResponse.getDtrRaceResponses().size(); j++){
                    DTRRaceWithDetails dtrRaceWithDetails1 = new DTRRaceWithDetails();
                    dtrRaceWithDetails1.setMarketNameInKannada(i+"."+dtrMarketResponse.getMarketNameInKannada());

//                    dtrRaceWithDetails1.setMarketNameInKannada(dtrMarketResponse.getMarketNameInKannada());
                    dtrRaceWithDetails1.setRaceNameInKannada(dtrMarketResponse.getDtrRaceResponses().get(j).getRaceNameInKannada());
                    if(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses() != null) {
                        for (int k = 0; k < dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().size(); k++) {
                            dtrRaceWithDetails1.setWeight(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getWeight());
                            dtrRaceWithDetails1.setAvgAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getAvgAmount());
                            dtrRaceWithDetails1.setMaxAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getMaxAmount());
                            dtrRaceWithDetails1.setMinAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getMinAmount());

                            if(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses() != null) {
                                dtrRaceWithDetails1.setPrevWeight(String.valueOf(Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getWeight()) - Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses().get(k).getWeight())));
                                dtrRaceWithDetails1.setPrevAvg(String.valueOf(Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getAvgAmount()) - Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses().get(k).getAvgAmount())));
                            }else{
                                dtrRaceWithDetails1.setPrevWeight("0.000");
                            }

                            if(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses() != null) {
                                dtrRaceWithDetails1.setWeight(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getWeight());
                                dtrRaceWithDetails1.setAvgAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getAvgAmount());
                                dtrRaceWithDetails1.setMaxAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getMaxAmount());
                                dtrRaceWithDetails1.setMinAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getMinAmount());
                            }else{
                                dtrRaceWithDetails1.setLastWeight("0.000");
                            }

                            dtrRaceWithDetails.add(dtrRaceWithDetails1);

                        }
                    }else{
                        dtrRaceWithDetails1.setWeight("0.000");
                        if(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses() != null) {
                            for (int k = 0; k < dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses().size(); k++) {
                                if (dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses() != null) {
                                    dtrRaceWithDetails1.setPrevWeight(String.valueOf(Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getWeight()) - Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses().get(k).getWeight())));
                                    dtrRaceWithDetails1.setPrevAvg(String.valueOf(Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getDtrResponses().get(k).getAvgAmount()) - Float.parseFloat(dtrMarketResponse.getDtrRaceResponses().get(j).getPrevResponses().get(k).getAvgAmount())));
                                } else {
                                    dtrRaceWithDetails1.setPrevWeight("0.000");
                                }
                            }
                        }else{
                            dtrRaceWithDetails1.setPrevWeight("0.000");
                        }
                        if(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses() != null) {
                            for (int k = 0; k < dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().size(); k++) {
                                if (dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses() != null) {
                                    dtrRaceWithDetails1.setWeight(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getWeight());
                                    dtrRaceWithDetails1.setAvgAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getAvgAmount());
                                    dtrRaceWithDetails1.setMaxAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getMaxAmount());
                                    dtrRaceWithDetails1.setMinAmount(dtrMarketResponse.getDtrRaceResponses().get(j).getLastYearResponses().get(k).getMinAmount());
                                } else {
                                    dtrRaceWithDetails1.setLastWeight("0.000");
                                }
                            }
                        }else{
                            dtrRaceWithDetails1.setLastWeight("0.000");
                        }

                        dtrRaceWithDetails1.setLastDiff(String.format("%.3f", Double.parseDouble(dtrRaceWithDetails1.getWeight()) - Double.parseDouble(dtrRaceWithDetails1.getLastWeight())));

                        dtrRaceWithDetails.add(dtrRaceWithDetails1);
                    }
                }
            }

            List<DTRResponse> raceByToday = apiResponse.getContent().getDtrDataResponse().getRaceByToday();
            List<DTRResponse> raceByPrevYear = apiResponse.getContent().getDtrDataResponse().getRaceByPrevYear();

            List<DTRAllMarketResponse> dataSource1Response = new ArrayList<>();
            for(int i=0; i<raceByToday.size(); i++){
                DTRAllMarketResponse dtrAllMarketResponse = new DTRAllMarketResponse();
                dtrAllMarketResponse.setRaceNameInKannada1(i +"."+raceByToday.get(i).getRaceName());
                dtrAllMarketResponse.setWeight1(raceByToday.get(i).getWeight());
                if(raceByPrevYear.size()>0){
                    if(raceByPrevYear.get(i) != null){
                        dtrAllMarketResponse.setLastWeight1(raceByPrevYear.get(i).getWeight());
                    }else{
                        dtrAllMarketResponse.setLastWeight1("0.000");
                    }
                }else{
                    dtrAllMarketResponse.setLastWeight1("0.000");
                }
                dtrAllMarketResponse.setMinAmount1(raceByToday.get(i).getMinAmount());
                if(raceByPrevYear.size()>0){
                    if(raceByPrevYear.get(i) != null){
                        dtrAllMarketResponse.setLastMinAmount1(raceByPrevYear.get(i).getMinAmount());
                    }
                }
                dtrAllMarketResponse.setMaxAmount1(raceByToday.get(i).getMaxAmount());
                if(raceByPrevYear.size()>0){
                    if(raceByPrevYear.get(i) != null){
                        dtrAllMarketResponse.setLastMaxAmount1(raceByPrevYear.get(i).getMaxAmount());
                    }
                }
                dtrAllMarketResponse.setAvgAmount1(raceByToday.get(i).getAvgAmount());
                if(raceByPrevYear.size()>0){
                    if(raceByPrevYear.get(i) != null){
                        dtrAllMarketResponse.setLastAvgAmount1(raceByPrevYear.get(i).getAvgAmount());
                    }
                }
                dataSource1Response.add(dtrAllMarketResponse);
            }


            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDTRAllMarket(request);
            parameters.put("datasource1", dataSource1Response);
            parameters.put("datasource2", dtrRaceWithDetails);

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

        } catch (JRException ex) {
            logger.error("Error generating Form 13 report", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private JRBeanCollectionDataSource getDTRAllMarket(Form13Request requestDto) throws JsonProcessingException {
        DTRAllMarketResponse apiResponse = apiService.dtrAllReport(requestDto);

        DTRDataResponse dtrDataResponse = apiResponse.getContent().getDtrDataResponse();
        apiResponse.setTotalWeight(dtrDataResponse.getSumOfToday().getWeight());
        apiResponse.setTotalMin(dtrDataResponse.getSumOfToday().getMinAmount());
        apiResponse.setTotalMax(dtrDataResponse.getSumOfToday().getMaxAmount());
        apiResponse.setTotalAvg(dtrDataResponse.getSumOfToday().getAvgAmount());

        apiResponse.setLastTotalWeight(dtrDataResponse.getSumOfPreviousYear().getWeight());
        apiResponse.setLastAvg(dtrDataResponse.getSumOfPreviousYear().getAvgAmount());
        apiResponse.setLastMax(dtrDataResponse.getSumOfPreviousYear().getMaxAmount());
        apiResponse.setLastMin(dtrDataResponse.getSumOfPreviousYear().getMinAmount());

        apiResponse.setFinalDiff(dtrDataResponse.getTotalWeightDiff());

        apiResponse.setThisYearWeight(dtrDataResponse.getThisYearWeight());
        apiResponse.setThisYearAmount(dtrDataResponse.getThisYearAmount());
        apiResponse.setPrevYearAmount(dtrDataResponse.getPrevYearAmount());
        apiResponse.setPrevYearWeight(dtrDataResponse.getPrevYearWeight());

        apiResponse.setLogurl("/reports/Seal_of_Karnataka.PNG");
        apiResponse.setHeader1("ಕರ್ನಾಟಕ ರಾಜ್ಯದ ಪ್ರಮುಖ ವಾಣಿಜ್ಯ ರೇಷ್ಮೆ ಗೂಡು \nಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ ವಹಿವಾಟಾದ ರೇಷ್ಮೆ ಗೂಡಿನ ವಿವರ \n" + convertDate(requestDto.getAuctionDate().toString()));
        apiResponse.setYear1(requestDto.getAuctionDate().minusYears(1).getYear() +"-" + requestDto.getAuctionDate().getYear());
        apiResponse.setYear2(requestDto.getAuctionDate().minusYears(2).getYear() +"-" + requestDto.getAuctionDate().minusYears(1).getYear());
        apiResponse.setLogurl("/reports/Seal_of_Karnataka.PNG");


        if(apiResponse.getThisYearWeight().equals("")) {
            apiResponse.setThisYearAmount("0");
        }
        if(apiResponse.getPrevYearWeight().equals("")){
            apiResponse.setPrevYearWeight("0");
        }

        apiResponse.setWeightMonthDiff(String.format("%.3f", Double.parseDouble(apiResponse.getThisYearWeight()) - Double.parseDouble(apiResponse.getPrevYearWeight())));


        List<DTRAllMarketResponse> response = new LinkedList<>();
        response.add(apiResponse);
        return new JRBeanCollectionDataSource(response);
    }

    public static String convertDate(String dateString) {
        // Split the date string into year, month, and day
        String[] parts = dateString.split("-");
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];

        // Return the date string in "DD-MM-YYYY" format
        return day + "-" + month + "-" + year;
    }

    public static String convertToTime(String timeString) throws ParseException {
        SimpleDateFormat sdfInput = new SimpleDateFormat("HH:mm:ss.SSS");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("HH:mm:ss");

        if (!(timeString.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"))) {
            timeString = timeString + ".000";
        }

        Date date = sdfInput.parse(timeString);
        return sdfOutput.format(date);
    }



    private JRDataSource getDataSourceForAcknowledgementReceipt(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchData(requestDto);
      //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader(" ರೇಷ್ಮೆ ಯಾಂತ್ರೀಕರಣ-ಸ್ವೀಕೃತಿ ಪತ್ರ/ACKNOWLEDGEMENT RECEIPT");
            response.setAcceptedDate("ಸ್ವೀಕೃತಿ ಪತ್ರದ ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ  " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "  ಯೋಜನೆಯಡಿ   " +apiResponse.getContent().get(0).getDistrictName() + "  ಜಿಲ್ಲೆ , " +apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು , " + apiResponse.getContent().get(0).getHobliName()+ "  ಹೋಬಳಿ ,\n " +
                    "                                           \n"+
                    "" +apiResponse.getContent().get(0).getVillageName()+ "  ಹಳ್ಳಿಯ ನಿವಾಸಿಯಾದ  ಶ್ರೀ./ಶ್ರೀಮತಿ.  " +apiResponse.getContent().get(0).getFarmerFirstName()+ " ರವರಿಂದ  " +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "\n" +
                    "                                    \n"+
                    "ಯಂತ್ರೋಪಕರಣಕೆ ಸಹಾಯಧನ ಪಡೆಯಲು ಅರ್ಜಿಯನ್ನು ಸಲ್ಲಿಸುತ್ತಾರೆ . ಇವರ ನೋಂದಣಿ ಸಂಖ್ಯೆಯ  :  " +apiResponse.getContent().get(0).getFruitsId() + "\n" +
                    "                                                              \n" +
                    "ಇದ್ದು , ಈ ನೋಂದಣಿ ಸಂಖ್ಯೆಯನ್ನು ಮುಂದಿನ ವಿಚರಾಣೆಗೆ ಉಪಯೋಗಿಸತಕದ್ದು .");
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            acknowledgementReceiptResponseList.add(response);

          //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


        private JRDataSource getDataSourceAuthorisationLetterFromFarmer(WorkOrderPrintRequest requestDto) throws JsonProcessingException {

        WorkOrderReportResponse apiResponse = apiService.fetchDataApi(requestDto);
        List<WorkOrderGenerationReportResponse> workOrderGenerationReportResponseList = new LinkedList<>();
            WorkOrderGenerationReportResponse response = new WorkOrderGenerationReportResponse();
            if (apiResponse.getContent()!= null) {
                response.setHeader1("(ರೇಷ್ಮೆ  ಇಲಾಖೆ)");
                response.setHeader2("ಸಹಾಯಕ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರ ಕಛೇರಿ ");
                response.setLineItemComment("                    " + apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ  ಯೋಜನೆಯಡಿ  ಶ್ರೀ/ಶ್ರೀಮತಿ   " + apiResponse.getContent().get(0).getFarmerFirstName()+ "  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  +  " , " +apiResponse.getContent().get(0).getVillageName()+ " ಗ್ರಾಮ ,  " + apiResponse.getContent().get(0).getHobliName()+ " ಹೋಬಳಿ  , " +apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕು , " + apiResponse.getContent().get(0).getDistrictName()+ " ಜಿಲ್ಲೆ (ನೋಂದಣಿ \n " +
                        "                                            \n" +
                        "ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getFruitsId() + " , ಮೊಬೈಲ್ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getMobileNumber() + " )  ಆದ ನಾನು ಮೇ  :  " + apiResponse.getContent().get(0).getVendorName() + "  ಸಂಸ್ಥೆಯ ವತಿಯಿಂದ " +apiResponse.getContent().get(0).getScComponentName()+ "\n" +
                                "                                                                                                \n " +
                        "" +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ " ರೇಷ್ಮೆ ಯಂತ್ರೋಪಕರಣ/ಸಂಸ್ಕರಣಾ ಘಟಕವನ್ನು ರೇಷ್ಮೆ ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ \n"+
                        "                                                                                                           \n" +
                        "ಅನ್ವಯ ಪಡೆಯಲು ಅರ್ಜಿ ಸಲ್ಲಿಸಿದು,ಈ ಸಂಬಂಧ ನಾನು ರೈತರ ವಂತಿಕೆ ಮೊತ್ತ ರೂ.  " + apiResponse.getContent().get(0).getCost() +"  ಗಳನ್ನೂ ಮಾತ್ರ ಪಾವತಿಸಿರುತ್ತಾನೆ.");
                response.setHeader4("ಈ ಸಂಬಂಧ ಸರ್ಕಾರದ ಸಹಾಯಧನವನ್ನು ಮೇ: "+ apiResponse.getContent().get(0).getVendorName() + " ಸಂಸ್ಥೆಯ ಬ್ಯಾಂಕ್");
                response.setHeader5("ಗೆ ಅಥವಾ ಸದರಿ ಘಟಕವನ್ನು ಖರೀದಿಸಲು ಪಡೆಯಲಾದ ನನ್ನ ಬ್ಯಾಂಕ್" );
                response.setDate(apiResponse.getContent().get(0).getDate());
                response.setFarmerFirstName(" ಶ್ರೀ/ಶ್ರೀಮತಿ  "+apiResponse.getContent().get(0).getFarmerFirstName());
                response.setWorkOrderId(apiResponse.getContent().get(0).getWorkOrderId());
                response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
                response.setFarmerAccountNumber(apiResponse.getContent().get(0).getFarmerAccountNumber());
            response.setFarmerBankName(apiResponse.getContent().get(0).getFarmerBankName());
            response.setFarmerBankIfsc(apiResponse.getContent().get(0).getFarmerBankIfsc());
            response.setFarmerBranchName(apiResponse.getContent().get(0).getFarmerBranchName());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber(apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorBankIfsc(apiResponse.getContent().get(0).getVendorBankIfsc());
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBranchName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
                response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());

                workOrderGenerationReportResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(workOrderGenerationReportResponseList);
    }


            private JRDataSource getDataSourceForAuthorisationLetter(AuthorisationLetterPrintRequest requestDto) throws JsonProcessingException {

        AuthorisationResponse apiResponse = apiService.fetchDataFromAuth(requestDto);
        List<AuthorisationLetterReportResponse> authorisationLetterReportResponseList = new LinkedList<>();
                AuthorisationLetterReportResponse response = new AuthorisationLetterReportResponse();
                if (apiResponse.getContent()!= null) {
                    response.setHeader1("(ರೇಷ್ಮೆ  ಇಲಾಖೆ)");
                    response.setHeader2("ಸಹಾಯಕ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರ ಕಛೇರಿ ");
                    response.setDate(apiResponse.getContent().get(0).getDate());
                    response.setFarmerFirstName(" ಶ್ರೀ/ಶ್ರೀಮತಿ  "+ apiResponse.getContent().get(0).getFarmerFirstName());
                    response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
                    response.setFruitsId(apiResponse.getContent().get(0).getFruitsId());
                    response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
                    response.setDistrictName(apiResponse.getContent().get(0).getDistrictName()+"(Bank District)");
                    response.setTalukName(apiResponse.getContent().get(0).getTalukName());
                    response.setVillageName(apiResponse.getContent().get(0).getVillageName());
                    response.setHobliName(apiResponse.getContent().get(0).getHobliName());
                    response.setFarmerAccountNumber(apiResponse.getContent().get(0).getFarmerAccountNumber());
                    response.setFarmerBankName(apiResponse.getContent().get(0).getFarmerBankName());
                    response.setFarmerBankIfsc(apiResponse.getContent().get(0).getFarmerBankIfsc());
                    response.setFarmerBranchName(apiResponse.getContent().get(0).getFarmerBranchName());
                    response.setLineItemComment(apiResponse.getContent().get(0).getLineItemComment());
                    response.setCost( apiResponse.getContent().get(0).getCost() );
                    response.setVendorName(apiResponse.getContent().get(0).getVendorName());
                    response.setVendorAccountNumber(apiResponse.getContent().get(0).getVendorAccountNumber());
                    response.setVendorBankName(apiResponse.getContent().get(0).getVendorBankName()+ "(Bank)");
                    response.setVendorBankIfsc(apiResponse.getContent().get(0).getVendorBankIfsc());
                    response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBranchName()+" (Branch)");
                    response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
                    response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
                    response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
                    response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
                    authorisationLetterReportResponseList.add(response);
                }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(authorisationLetterReportResponseList);
    }

    private JRDataSource getDataSourceForSelectionLetter(SelectionLetterPrintRequest requestDto) throws JsonProcessingException {

        SelectionLetterResponse apiResponse = apiService.fetchDataFromSelection(requestDto);
        List<SelectionLetterReportResponse> selectionLetterReportResponseList = new LinkedList<>();
        SelectionLetterReportResponse response = new SelectionLetterReportResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader4("2021-22 ನೇ ಸಾಲಿನಲ್ಲಿ ರೇಷ್ಮೆ  ಯಾಂತ್ರೀಕರಣ ಯೋಜನೆಯಡಿ");
            response.setHeader1("ಆದ ನೀವು ಮೆ: JOHN DEER INDIA PRIVATE LIMITED ಎಂಪ್ಯಾನಲ್ಲೆದ್  ಸಂಸ್ಥೆಇಂದ Rotovater/Side Shift rotovater ");
            response.setHeader5("(Model: 13-18 Hp Tractor down Rotovater, 16-18  Blades, 60cm Working Width) ರೇಷ್ಮೆ  ಯಂತ್ರೋಪಕರಣ/ಸಂಸ್ಕರಣ ಘಟಕವನ್ನು ರೇಷ್ಮೆ  ಇಲಾಕೆಯಾ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಪಡೆಯಲು ಅರ್ಜಿ ");
            response.setHeader2(" ರೈತರ ವಂತಿಕೆಯನ್ನು 10 ದಿನಗಳೊಳಗಾಗಿ ಮೆ : JOHN DEER INDIA PRIVATE LIMITED ಸಂಸ್ತೆಯ ಬ್ಯಾಂಕ್ : ");
            response.setHeader3("ಸದರಿ ರೇಷ್ಮೆ  ಯಂತ್ರೋಪಕರಣ/ಸಂಸ್ಕರಣ ಘಟಕ ಸಂಬಂದಿಸಿದಂತೆ, ರೂ.");
            response.setHeader6("ಸಲಿಸಿ ಸಹಯಹೆಚಿಸುರುತಿರಿದನ ಪಡೆಯಲು ಹೆಚಿಸಿರುತ್ತಿರಿ.");
            response.setHeader7(" ಇವರಿಗೆ ಸಲ್ಲಿಸಲು ಈ ಮೂಲಕ ತಿಳಿಸಿದೆ.");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setLineItemComment(" ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  "+  apiResponse.getContent().get(0).getTalukName() + "  ತಾಲೂಕು");
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            selectionLetterReportResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(selectionLetterReportResponseList);
    }

    private JRDataSource getDataSourceForSanctionOrder(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSanction(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ರೇಷ್ಮೆ ಜಂಟಿ  ನಿರ್ದೇಶಕರು/ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರು ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ರವರ ನಡವಳಿಗಳು ");
            response.setHeader4("ವಿಷಯ  : ");
            response.setHeader20(apiResponse.getContent().get(0).getFinancialYear() + "ನೇ ಸಾಲಿನಲ್ಲಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಇಲಾಖೆಯು ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ  ಶ್ರೀಮತಿ./.ಶ್ರೀ.  " +apiResponse.getContent().get(0).getFarmerFirstName() + "  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan());
            response.setHeader21( " ,ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ನಿರ್ಮಿಸಿರುವ - ಚದರ ಅಡಿಗಳ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "  ಗಳ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2(" 1." +apiResponse.getContent().get(0).getFinancialYear() +" ಸಾಲಿಗೆ ಮುಂದವರೆದ ಸರ್ಕಾರದ ಆದೇಶ ¸ಸಂಖ್ಯೆ -ದಿನಾಂಕ " +apiResponse.getContent().get(0).getDate());
            response.setHeader3(" 2. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಮಾರ್ಗಸೂಚಿಯ ಸುತೋಲೆ ಸಂಖ್ಯೆ - ದಿನಾಂಕ  "  + apiResponse.getContent().get(0).getDate());
            response.setHeader6(" 3. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು0 ,ರವರ ಕ್ರಿಯಾಯೋಜನೆ ಸುತೋಲೆ ಸಂಖ್ಯ-ೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate());
            response.setHeader7(" 4. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಜ್ಞಾಪನ ಸಂಖ್ಯೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate());
            response.setHeader22(" 5.ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ :ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ :ತೋಇ/61/ರೇಕೃವಿ/2019.ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() );
            response.setHeader23(" 6 -ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರು ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ , -  ರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ: - ದಿನಾಂಕ : "  + apiResponse.getContent().get(0).getDate());
            response.setHeader24("ಪೀಠಿಕೆ ");
            response.setHeader8(  "            " + apiResponse.getContent().get(0).getFinancialYear() +" ನೇ ಸಾಲಿನಲ್ಲಿ ವಿವಿಧ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆಗಳಡಿ ವಿವಿಧ ಕಾರ್ಯಕ್ರಮಗಳ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಉಲ್ಲೇಖ (1) ರಲ್ಲಿ ಸರ್ಕಾರವು ಆಡಳಿತಾತ್ಮಕ ಅನುಮೋದನೆಯನ್ನು ನೀಡಿದ್ದು , ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ ಕೇಂದ್ರವಲಯ\n" +
                    "                                       \n"+
                    "ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆ ನಿರ್ಮಾಣಕ್ಕೆ ಸಹಾಯಧನ ಕಾರ್ಯಕಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಮಾರ್ಗಸೂಚಿಯನ್ನು ಉಲ್ಲೇಖ (3)ರಲ್ಲಿ ಕ್ರಿಯಾಯೋಜನೆ ಮತ್ತು ಉಲ್ಲೇಖ \n" +
                    "                                       \n"+
                    "(4)ರಲ್ಲಿ ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಅನುಧಾನ ಬಿಡುಗಡೆಯಾಗಿರುತ್ತದೆ .ಇಲಾಖೆಯು  ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಕೇಂದ್ರ ವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ  ಯೋಜನೆಯನ್ನು \n"+
                    "                                       \n"+
                    "ಅನುಷ್ಠಾನಗೊಳಿಸಲಾಗುತ್ತಿದೆ . ಸದರಿ ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ ಬೆಳೆಗಾರರು ನಿರ್ಮಾಣ ಮಾಡಿರುವ -  ಚದರ ಅಡಿ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ  ಮನೆ ನಿರ್ಮಾಣಕ್ಕೆ ಸಹಾಯಧನ ನೀಡಬೇಕಾಗಿದ್ದು, \n"+
                    "                                       \n"+
                    "ಸಾಮಾನ್ಯ /ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ  ವರ್ಗದಡಿ ಕೇಂದ್ರ : ರಾಜ್ಯ : ಫಲಾನುಭವಿ ಪಾಲು  50:25:25/65:25:10 ಆಗಿರುತ್ತದೆ .- ಚದರ ಅಡಿಯ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆ ನಿರ್ಮಾಣದ  ಘಟಕದರ  ರೂ .  " +apiResponse.getContent().get(0).getCost()  + "\n"+
                    "                                       \n"+
                    "ಲಕ್ಷಗಳಿಗೆ ನಿಗಧಿಪಡಿಸಿದ್ದು ,ಇದರಲ್ಲಿ ಶೇಕಡ 75 ರಷ್ಟನ್ನು  ಅಂದರೆ ರೂ . " +apiResponse.getContent().get(0).getCost() + "ಲಕ್ಷಗಳನ್ನು  ಸಹಾಯಧನವಾಗಿ ನೀಡಲಾಗುತ್ತದೆ . ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ .50/65 ಅಂದರೆ ರೂ." +apiResponse.getContent().get(0).getCost() + "\n" +
                    "                                       \n"+
                    "ಲಕ್ಷಗಳು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕದರದ ಶೇ . 25 ಅಂದರೆ ರೂ . " +apiResponse.getContent().get(0).getCost() + " ಲಕ್ಷಗಳು ಆಗಿರುತ್ತದೆ . ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ    ಅನುದಾನವನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು   \n"+
                    "                                       \n"+
                            "ಹಾಗು ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ  ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ . " +apiResponse.getContent().get(0).getCost() + "  ಲಕ್ಷಗಳನ್ನು (50/65%)  ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ   \n"+
                    "                                       \n"+
                    "ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯ , ಬೆಂಗಳೂರುರವರ ಕಛೇರಿಯಿಂದ ಡಿಬಿಟಿ  ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ . \n"+
                    "                                       \n"+
                    "ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423)   ಅಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರುರವರು   \n"+
                    "                                       \n"+
                    "ಖಜಾನೆ -2 ಮುಖಾಂತರ ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ  ಜಮಾ ಮಾಡಲಾಗುವುದು . " );
            response.setHeader9("ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ .50/65 ಅಂದರೆ ರೂ ಲಕ್ಷಗಳು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕದರದ ಶೇ . 25 ಅಂದರೆ ರೂ .   ಲಕ್ಷಗಳು ಆಗಿರುತ್ತದೆ . ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ  ಅನುದಾನವನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗು ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ . ಲಕ್ಷಗಳನ್ನು (50/65%)   ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯ , ಬೆಂಗಳೂರುರವರ ಕಛೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ . ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423) ಅಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರುರವರು  ಖಜಾನೆ -2 ಮುಖಾಂತರ ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ  ಜಮಾ ಮಾಡಲಾಗುವುದು .  ");
            response.setHeader10(  "             " +apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆಯ " + apiResponse.getContent().get(0).getTalukName() +"ತಾಲೂಕಿನ  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ  " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದಲ್ಲಿ  ಸಾಮಾನ್ಯ /ವರ್ಗಕ್ಕೆ ಸೇರಿದ  ಶ್ರೀ ./ಶ್ರೀಮತಿ ." + apiResponse.getContent().get(0).getFarmerFirstName() + "  ಬಿನ್/ಕೋಂ   " +apiResponse.getContent().get(0).getFatherNameKan() + "\n"+
                    "                                       \n"+
                    " ಇವರು " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ ಸರ್ವೆನಂ  - ರಲ್ಲಿ -ಹೆಕ್ಟೇರು ವಿಸ್ತೀರ್ಣದಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಹೊಂದಿದ್ದು   ಗ್ರಾಮದ ಸುರ್ವೆನಂ /ಖಾತೆ  ನಂ  -ರಲ್ಲಿ   ಚದರಡಿ ವಿಸ್ತೀರ್ಣದ -ಮೇಲ್ಚಾವಣಿಯ  \n" +
                    "                                                                     \n" +
                    "ಪ್ರತ್ಯೇಕ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಯನ್ನು ಅಂದಾಜು  ರೂ .  ಲಕ್ಷಗಳ ವೆಚ್ಚದಲ್ಲಿ (ಸ್ವಂತ  ವೆಚ್ಚ /ಬ್ಯಾಂಕಿನಿಂದ ಸಾಲ ಪಡೆದು )ನಿರ್ಮಿಸಿರುವುದನ್ನು  ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ -ಹಾಗೂ ರೇಷ್ಮೆ \n" +
                    "                                      \n" +
                    "ಸಹಾಯಕ ನಿರ್ದೇಶಕರು -ವಿಭಾಗರವರು ಪರಿಶೀಲಿಸಿ ಧ್ರಡೀಕರಿಸಿ ಸಲ್ಲಿಸಿದ ಎಲ್ಲ ಅಗತ್ಯ ದಾಖಲಾತಿಗಳನ್ನು ಒಳಗೊಂಡ ಪ್ರಸ್ತಾವನೆಯನ್ನು "+ apiResponse.getContent().get(0).getDistrictName() +"ಜಿಲ್ಲಾ   ಪಂಚಾಯತ್ ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು ಪರಿಶೀಲಿಸಿ ಧ್ರಡೀಕರಿಸಿ \n" +
                            "                         \n" +
                            "ಉಲ್ಲೇಖ (6)  ಈ ಕಛೇರಿಗೆ ಶಿಫಾರಸ್ಸು ಮಾಡಿ ಸಲ್ಲಿಸಿದ್ದು ,ಸದರಿ ಫಲಾನುಭವಿಗೆ  ರೂ . - ಗಳ ಸಹಾಯಧನವನ್ನು ಮಂಜೂರು ಮಾಡುವಂತೆ ಕೋರಿರುತ್ತಾರೆ . ಮಂಜೂರಾತಿಗೆ ಕೋರಲಾಗಿರುವ  ಸಹಾಯಧನ \n" +
                    "                                     \n"  +
                    "ಮಂಜೂರು ಮಾಡಲು ಉಲ್ಲೇಖ (5)ರ  ಸರ್ಕಾರಿ ಆದೇಶದ ರೀತ್ಯಾ  ಈ ಕಛೆರಿಯ ಅಧಿಕಾರ ಪ್ರತ್ಯಾಯೋಜನೆ  ವ್ಯಾಪ್ತಿಗೆ ಒಳಪಟ್ಟಿದ್ದು ಅದರಂತೆ ಸಹಾಯಧನ ಮಂಜೂರಾತಿಗಾಗಿ ಈ ಕೆಳಕಂಡ ಆದೇಶವನ್ನು ಹೊರಡಿಸಿದೆ  " );
            response.setHeader11("");
            response.setHeader12("ಆದೇಶ ");
            response.setHeader13("ಸಂಖ್ಯೆ ");
            response.setHeader14("ದಿನಾಂಕ ");
            response.setHeader15("");
            response.setHeader16("             ಮೇಲಿನ ಪೀಠಿಕೆಯಲ್ಲಿ ವಿವರಿಸಿರುವಂತೆ  ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು , ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ - ರವರು  ಶಿಫಾರಸ್ಸು  ಮಾಡಿರುವಂತೆ - ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ  ವ್ಯಾಪ್ತಿಯ"+apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ \n" +
                    "                                             \n" +
                    "ಸಾಮಾನ್ಯ /ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ ಗ್ರಾಮದ ಸಾಮಾನ್ಯ ಎಸ್ಸಿಪಿ  ಟಿಎಸ್ಪಿ ವರ್ಗಕ್ಕೆ ಸೇರಿದ ಶ್ರೀ . /ಶ್ರೀಮತಿ . "+ apiResponse.getContent().get(0).getFarmerFirstName()+"  ಬಿನ್/ಕೋಂ   " +apiResponse.getContent().get(0).getFatherNameKan() + "ರವರು ಕೇಂದ್ರ ವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ -ಚ.ಅಡಿಯ  \n" +
                    "                                              \n" +
                    "ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ಘಟಕ ದರದ ಶೇ . 75 / 90 ರಷ್ಟು  ಸಹಾಯಧನ ರೂ . -ಲಕ್ಷ (-ರೂ ಗಳು  ಮಾತ್ರ ) ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ . ಈ ಸಹಾಯಧನದ ಪೈಕಿ  ರೂ . -ಲಕ್ಷ (-ರೂ . ಗಳು ಮಾತ್ರ ) \n" +
                    "                                            \n" +
                    "ಕೇಂದ್ರದ  ಪಾಲಾಗಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ನೀಡಿರುವ ಮೊತ್ತದಲ್ಲಿ ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿಗಳ  ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುವುದು  ಮತ್ತು ರಾಜ್ಯದ \n" +
                    "                                                \n" +
                    "ಪಾಲಾಗಿ ರೂ . -ಲಕ್ಷಗಳನ್ನು (-ರೂ ಗಳು ಮಾತ್ರ ) ರಾಜ್ಯ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ  ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423)ರಡಿ  ಖಜಾನೆ -2 ರಲ್ಲಿ ಬಿಡುಗೋಡೆಗಳಿಸಿರುವ ಸಹಾಯಧನವನ್ನು ಸಂಬಂಧಿಸಿದ \n" +
                    "                                             \n" +
                    "ರೇಷ್ಮೆ ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು ಖಜಾನೆ -2 ಮೂಲಕ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡುವುದು . ಈ ವೆಚ್ಚವನ್ನು ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423)\n" +
                            "                             \n" +
                            "(ಸಾಮಾನ್ಯ / ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ ) ಅಡಿ  ಭರಿಸುವುದು ." );
            response.setHeader17("ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು ");
            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
                            "                       \n" +
                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
                    "                                      \n" +
                     "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setLineItemComment("  1. ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು ,ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ,- ರವರಿಗೆ ಎಲ್ಲ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ ಮುಂದಿನ ಅಗತ್ಯ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ .  \n"+
                    "                                       \n"+
                    " 2. ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು - ವಿಭಾಗರವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .  \n"+
                    "                                       \n"+
                    " 3. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ ,ತಾಂತ್ರಿಕ ಸೇವಾಕೇಂದ್ರ -ರವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .");
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }


    private JRDataSource getDataSourceForSanctionCompany(SanctionCompanyPrintRequest requestDto) throws JsonProcessingException {

        SanctionCompany apiResponse = apiService.fetchDataFromSanctionCompany(requestDto);
        List<SanctionCompanyResponse> sanctionCompanyResponseList = new LinkedList<>();
        SanctionCompanyResponse response = new SanctionCompanyResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ಸರಬರಾಜು ಸಂಸ್ಥೆ  ");
            response.setHeader2("ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು  ");
            response.setHeader3("ವಿಷಯ  : ");
            response.setHeader4( "         " + apiResponse.getContent().get(0).getFinancialYear() + "ನೇ ಸಾಲಿನಲ್ಲಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಇಲಾಖೆಯು ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ  ಶ್ರೀಮತಿ./.ಶ್ರೀ.  " +apiResponse.getContent().get(0).getFarmerFirstName() + "  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan() + "\n"  +
                    "                                         \n"  +
                    ",ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ   ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "ಗಳ  \n" +
                    "                                        \n" +
                    "ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader6(" 1." +apiResponse.getContent().get(0).getFinancialYear() +" ಸಾಲಿಗೆ ಮುಂದವರೆದ ಸರ್ಕಾರದ ಆದೇಶ ¸ಸಂಖ್ಯೆ -    \n" +
                    "                                                   \n"+
                    " 2. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಮಾರ್ಗಸೂಚಿಯ ಸುತೋಲೆ ಸಂಖ್ಯೆ - ದಿನಾಂಕ  "  + apiResponse.getContent().get(0).getDate() + "\n" +
                    "                                                 \n" +
                    " 3. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಕ್ರಿಯಾಯೋಜನೆ ಸುತೋಲೆ ಸಂಖ್ಯೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate()+ "\n" +
                    "                                                                     \n" +
                    " 4. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಜ್ಞಾಪನ ಸಂಖ್ಯೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate()  + "\n"  +
                    "                                                                 \n" +
                    " 5. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಸುತೋಲೆ ಸಂಖ್ಯೆ - ದಿನಾಂಕ  "  + apiResponse.getContent().get(0).getDate() +"\n" +
                    "                                                                                \n" +
                    " 6. ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ :ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ :ತೋಇ/61/ರೇಕೃವಿ/2019.ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n" +
                    "                                                                            \n" +
                    " 7. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ .ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು -ವಿಭಾಗರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ : - ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n"

            );

            response.setHeader7("ಪೀಠಿಕೆ ");
            response.setHeader8(  "               " + apiResponse.getContent().get(0).getFinancialYear() +" ನೇ ಸಾಲಿನಲ್ಲಿ ವಿವಿಧ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆಗಳಡಿ ವಿವಿಧ ಕಾರ್ಯಕ್ರಮಗಳ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಉಲ್ಲೇಖ (1) ರಲ್ಲಿ ಸರ್ಕಾರವು ಆಡಳಿತಾತ್ಮಕ ಅನುಮೋದನೆಯನ್ನು ನೀಡಿದ್ದು , ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ ರೇಷ್ಮೆ \n" +
                    "                                       \n"+
                    "ಹುಳು ಸಾಕಾಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ/ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಮಾರ್ಗಸೂಚಿಯನ್ನು\n" +
                    "                                       \n"+
                    "ಉಲ್ಲೇಖ(3)ರಲ್ಲಿ ಕ್ರಿಯಾಯೋಜನೆ ಮತ್ತು ಉಲ್ಲೇಖ (4)ರಲ್ಲಿ ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಅನುಧಾನ ಬಿಡುಗಡೆಯಾಗಿರುತ್ತದೆ .ಉಲ್ಲೇಖ (5)ರಲ್ಲಿ ಸರಬರಾಜು ಸಂಸ್ಥೆಗಳನ್ನು ಗುರುತಿಸಲಾಗಿದೆ.\n"+
                    "                                       \n"+
                    "ಗುರುತಿಸಲಾಗಿರುವ ಸಂಸ್ಥೆಗಳಿಂದ ಸರಬರಾಜು ಪಡೆದಲ್ಲಿ ಮಾತ್ರ ಸಹಾಯಧನ ಪಡೆಯಲು ಅವಕಾಶವಿರುತ್ತದೆ .ಇಲಾಖೆಯು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ\n"+
                    "                                       \n"+
                    " ಯೋಜನೆಯನ್ನು ಅನುಷ್ಠಾನಗೊಳಿಸುತ್ತಿದೆ .ಸದರಿ ಯೋಜನೆಯಡಿ) ರೇಷ್ಮೆ ಹುಳು ಶಾಸಕಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ)ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ\n"+
                    "                                       \n"+
                    "ಖರೀದಿಗೆ ಸಹಾಯಧನ ಸಾಮಾನ್ಯ(SCP/TSP)ವರ್ಗದಡಿ ಕೇಂದ್ : ರಾಜ್ಯ : ಫಲಾನುಭವಿ ಪಾಲು 50:25:25/65:25:10 ಆಗಿರುತ್ತದೆ.ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ)\n" +
                    "                                       \n"+
                    "ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ ಘಟಕ ದರ  ರೂ . "+apiResponse.getContent().get(0).getCost()+" ಲಕ್ಷಗಳಿಗೆ ನಿಗದಿಪಡಿಸಿದ್ದು .ಇದರಲ್ಲಿ  ಶೇಕಡ 75 ರಷ್ಟನ್ನು ಅಂದರೆ\n"+
                    "                                       \n"+
                    "ರೂ ."+apiResponse.getContent().get(0).getCost()+"ಲಕ್ಷಗಳನ್ನು ಸಹಾಯಧನವಾಗಿ ನೀಡಲಾಗುತ್ತದೆ .ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ . 50/65  ಅಂದರೆ ರೂ . "+apiResponse.getContent().get(0).getCost()+"ಲಕ್ಷಗಳು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕದರದ ಶೇ.25 ಅಂದರೆ\n"+
                    "                                       \n"+
                    "ರೂ . "+apiResponse.getContent().get(0).getCost()+"ಲಕ್ಷಗಳು ಆಗಿರುತ್ತದೆ . ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ ಅನುದಾನವನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ.\n"+
                    "                                       \n"+
                    "ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ . "+apiResponse.getContent().get(0).getCost()+" ಲಕ್ಷಗಳನ್ನು (50/65%) ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು,ರೇಷ್ಮೆ \n"+
                    "                                       \n"+
                    "ನಿರ್ದೇಶನಾಲಯ,ಬೆಂಗಳೂರುರವರ ಕಚೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ  ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ. ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ\n " +
                    "                                  \n" +
                    "ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423) ಅಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರುರವರು  ಖಜಾನೆ -2 ಮುಖಾಂತರ  ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು.\n" +
                    "                                     \n" +
                    "ಫಲಾನುಭವಿಯ  ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ  ಜಮಾ ಮಾಡಲಾಗುವುದು. ಫಲಾನುಭವಿ ವಂತಿಕೆ  ಶೇಕಡ 25 ರಷ್ಟನ್ನು ಅಂದರೆ  ರೂ . __ಲಕ್ಷಗಳನ್ನು (ರೂ . __ಮಾತ್ರ ) ಸರಬರಾಜು  ಸಂಸ್ಥೆಗೆ ಬೇಡಿಕೆ ಕರಡು\n" +
                    "                                                             \n" +
                    "ಮುಖಾಂತರ  ಪಾವತಿಸಿ ,ರೇಷ್ಮೆ ಬೆಳೆಗಾರರಿಂದ NOC ನ್ನು ಪಡೆದು ಸರಬರಾಜು ಸಂಸ್ಥೆಗೆ ಸಹಾಯಧನ ನೀಡಲು ಮಂಜೂರಾತಿ ನೀಡಬಹುದಾಗಿದೆ .");
            response.setHeader9(  "                " +apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆಯ " + apiResponse.getContent().get(0).getTalukName() +"ತಾಲೂಕಿನ  __ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ  " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದಲ್ಲಿ  ಸಾಮಾನ್ಯ /SCP/TSP ವರ್ಗಕ್ಕೆ ಸೇರಿದ  ಶ್ರೀ ./ಶ್ರೀಮತಿ ." + apiResponse.getContent().get(0).getFarmerFirstName() + "ಬಿನ್/ಕೋಂ   " +apiResponse.getContent().get(0).getFatherNameKan() + "\n"+
                    "                                       \n"+
                    " ಇವರು " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ ಸರ್ವೆನಂ  __ ರಲ್ಲಿ __ಹೆಕ್ಟೇರು ವಿಸ್ತೀರ್ಣದಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಹೊಂದಿದ್ದು " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ ಸುರ್ವೆನಂ /ಖಾತೆ  ನಂ  __ರಲ್ಲಿ  ಚದರಡಿ ವಿಸ್ತೀರ್ಣದ\n" +
                    "                                                                     \n" +
                    " __ಮೇಲ್ಚಾವಣಿಯ ಪ್ರತ್ಯೇಕ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಯನ್ನು ಹೊಂದಿರುತ್ತಾರೆ. ಸದರಿಯವರು ಫಲಾನುಭವಿ ವಂತಿಕೆ ಶೇಕಡ 25 ರಷ್ಟು ಅಂದರೆ ರೂ . __ ಲಕ್ಷಗಳನ್ನು(ರೂ . __ಮಾತ್ರ)\n" +
                    "                                                               \n" +
                    "ಬೇಡಿಕೆ ಕರಡು ಮೂಲಕ ಪಾವತಿಸಿ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ\n" +
                    "                                           \n" +
                    "ಕಾರ್ಯಕ್ರಮದಡಿ ಸಲಕರಣೆಗಳನ್ನು ಇಲಾಖೆಯು ಗುರುತಿಸಿರುವ ಸರಬರಾಜು ಸಂಸ್ಥೆಗಳಿಂದ ಸರಬರಾಜು ಪಡೆದಿರುತ್ತಾರೆ. ಸರಬರಾಜು ಸಂಸ್ಥೆಗೆ ಸಹಾಯಧನ ಪಾವತಿಸಲು NOC ನೀಡಿರುತ್ತಾರೆ.");
            response.setHeader10("                 ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ)  ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ   ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ\n" +
                    "                                                                    \n" +
                    "ಸರಬರಾಜು ಪಡೆದು ಸಲಕರಣೆಗಳ ವಿವರ  ಈ ಕೆಳಕಂಡಂತಿದೆ .  ");
            response.setHeader11("ಉಪಕರಣಗಳ ವಿವರ ");
            response.setHeader12("ಸಂಖ್ಯೆ  ");
            response.setHeader13("ದರ ");
            response.setHeader14("ಮೌಲ್ಯ ");
            response.setHeader15("ಖರೀದಿಸಿದ  ಸಂಸ್ಥೆ");
            response.setHeader16("ಬಿಲ್ ಸಂಖ್ಯೆ");
            response.setHeader17("ದಿನಾಂಕ ");
            response.setHeader18("              ರೂ . -ಲಕ್ಷಗಳ ವೆಚ್ಚದಲ್ಲಿ (ಸ್ವಂತ ವೆಚ್ಚ /ಬ್ಯಾಂಕಿನಿಂದ ಸಾಲ ಪಡೆದು)ಸರಬರಾಜು ಪಡೆದಿದ್ದು.--ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರರವರು/ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರವರು\n" +
                    "                                            \n" +
                    " -ವಿಭಾಗರವರು ಪರಿಶೀಲಿಸಿ ಧ್ರಡೀಕರಿಸಿ ಶಿಫಾರಿಸ್ಸಿನೊಂದಿಗೆ  ಸಂಬಂಧಿಸಿದ - ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರಿಗೆ ಎಲ್ಲ ಅಗತ್ಯ ದಾಖಲಾತಿಗಳನ್ನು ಒಳಗೊಂಡ ಪ್ರಸ್ತಾವನೆಯನ್ನು ಸಲ್ಲಿಸಿದ್ದು.ಸದರಿ\n" +
                    "                                           \n" +
                    "ಫಲಾನುಭವಿಗೆ  ರೂ . -- ಗಳ  ಸಹಾಯಧನವನ್ನು ಮಂಜೂರು ಮಾಡುವಂತೆ ಉಲ್ಲೇಖ(7) ರಲ್ಲಿ  ಕೋರಿರುತ್ತಾರೆ.ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರವರು --- ವಿಭಾಗರವರು /ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರವರು\n" +
                    "                                      \n" +
                    ""+ apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆಯ ಪಂಚಾಯತ್ ರವರ  ಮಂಜೂರಾತಿಗೆ ಕೊರಳಾಗಿರುವ' ಸಹಾಯಧನ  ಮಂಜೂರು ಮಾಡಲು ಉಲ್ಲೇಖ(6) ರಲ್ಲಿ ಸರ್ಕಾರೀ ಆದೇಶದ ರೀತ್ಯಾ ಈ ಕಛೇರಿಯ ಅಧಿಕಾರ  ಪ್ರತ್ಯಾಯೋಜನೆ\n " +
                    "                                                 \n" +
                    "ವ್ಯಾಪ್ತಿಗೆ ಒಳಪಟ್ಟಿದ್ದು  ಅದರಂತೆ  ಸಹಾಯಧನ ಮಂಜೂರಾತಿಗಾಗಿ  ಈ  ಕೆಳಕಂಡ ಆದೇಶವನ್ನು ಹೊರಡಿಸಿದೆ . ");
            response.setHeader19("ಆದೇಶ   ");
            response.setHeader20("ಸಂಖ್ಯೆ:");
            response.setHeader21("ದಿನಾಂಕ ");
            response.setHeader22("               ಮೇಲಿನ ಪೀಠಿಕೆಯಲ್ಲಿ ವಿವರಿಸಿರುವಂತೆ  ರೇಷ್ಮೆ ವಿಸ್ತರಣಾದಿಕಾರಿಗಳು --ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರರವರು /ರೇಷ್ಮೆ ಸಹಾಯಕ  ನಿರ್ದೇಶಕರವರು --ವಿಭಾಗರವರು  ಶಿಫಾರಸ್ಸು ಮಾಡಿರುವಂತೆ.\n" +
                    "                                    \n" +
                    " "+ apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮದ  ಸಾಮಾನ್ಯ/SCP/TSP  ವರ್ಗಕ್ಕೆ  ಸೇರಿದ ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan() +"ರವರು ಫಲಾನುಭವಿ ವಂತಿಕೆ  ಶೇಕಡ 25 ರಷ್ಟು ಅಂದರೆ ರೂ .  __ ಲಕ್ಷಗಳನ್ನು (ರೂ .\n" +
                    "                                                       \n" +
                    " __ ಮಾತ್ರ)ಬೇಡಿಕೆ ಕರಡು ಮೂಲಕ ಪಾವತಿಸಿ ಸರಬರಾಜು ಸಂಸ್ಥೆಗೆ  ಸಹಾಯಧನ ಪಾವತಿಸಲು  NOC ನೀಡಿರುತ್ತಾರೆ . ಆದುದರಿಂದ , ಕೇಂದ್ರ ವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ \n" +
                    "                                             \n" +
                    "ಹುಳು ಸಾಕಾಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ  ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ  ಘಟಕ ದರದ ಶೇ . 75/90 ರಷ್ಟು  ಸಹಾಯಧನ ರೂ . ___ಲಕ್ಷ\n" +
                    "                                     \n" +
                    "(__ರೂಪಾಯಿಗಳು  ಮಾತ್ರ) ಗಳಿಗೆ ಮಂಜೂರಾತಿ  ನೀಡಿದೆ .ಈ ಸಹಾಯಧನದ ಪೈಕಿ ರೂ . __ಲಕ್ಷ (__ರೂಪಾಯಿಗಳು ಮಾತ್ರ ) ಕೇಂದ್ರದ ಪಾಲಾಗಿ  ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ನೀಡಿರುವ ಮೊತ್ತದಲ್ಲಿ\n"+
                    "                               \n"+
                    "ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಸರಬರಾಜು ಸಂಸ್ಥೆಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುವುದು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲಾಗಿ ರೂ .__ಲಕ್ಷಗಳನ್ನು (___ ರೂಪಾಯಿಗಳು\n" +
                    "                                   \n" +
                    "ಮಾತ್ರ)ರಾಜ್ಯ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ  2851-00-107-1-35(106)(422)(423) ರಡಿ ಖಜಾನೆ -2 ರಲ್ಲಿ ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ ಸಹಾಯಧನವನ್ನು ಸಂಭಂದಿಸಿದ ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n" +
                    "                                                      \n"+
                    "ಖಜಾನೆ-2 ಮೂಲಕ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡುವುದು . ಈ ವೆಚ್ಚವನ್ನು  ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ  2851-00-107-1-35(106)(422)(423) (ಸಾಮಾನ್ಯ /SCP/TSP) ಅಡಿ  ಭರಿಸುವುದು.");
            response.setHeader23("ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು  ");
            response.setLineItemComment("ಪ್ರತಿಯನ್ನು \n" +
                    "                       \n" +
                    " 1. ______ಸಂಸ್ಥೆ\n" +
                            "                                \n" +
                    " 2. ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ,  ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName() + "\n" +
                    "                                      \n" +
                    " 3. ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು __ ವಿಭಾಗರವರಿಗೆ ಎಲ್ಲಾ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ ಮುಂದಿನ ಅಗತ್ಯ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ .\n" +
                    "                            \n" +
                    " 4. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ , ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ___ ರವರಿಗೆ ಎಲ್ಲ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ ಮುಂದಿನ ಅಗತ್ಯ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ .\n");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionCompanyResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionCompanyResponseList);
    }

    private JRDataSource getDataSourceForSanctionBeneficiary(SanctionBeneficiaryPrintRequest requestDto) throws JsonProcessingException {

        SanctionBeneficiary apiResponse = apiService.fetchDataFromSanctionBeneficiary(requestDto);
        List<SanctionBeneficiaryResponse> sanctionBeneficiaryResponseList = new LinkedList<>();
        SanctionBeneficiaryResponse response = new SanctionBeneficiaryResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ಫಲಾನುಭವಿ ");
            response.setHeader2(" ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು __ ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು  ");
            response.setHeader3("ವಿಷಯ  : ");
            response.setHeader4( "         " + apiResponse.getContent().get(0).getFinancialYear() + "ನೇ ಸಾಲಿನಲ್ಲಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಇಲಾಖೆಯು ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯಡಿ  ಶ್ರೀಮತಿ./.ಶ್ರೀ.  " +apiResponse.getContent().get(0).getFarmerFirstName() + "  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan() + "\n"  +
                    "                                         \n"  +
                    ",ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ   ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ರೂ ." +apiResponse.getContent().get(0).getCost()+ "ಗಳ \n" +
                    "                                        \n" +
                    "ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader6(" 1." +apiResponse.getContent().get(0).getFinancialYear() +" ಸಾಲಿಗೆ ಮುಂದವರೆದ ಸರ್ಕಾರದ ಆದೇಶ ¸ಸಂಖ್ಯೆ -    \n" +
                    "                                                   \n"+
                    " 2. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಮಾರ್ಗಸೂಚಿಯ ಸುತೋಲೆ ಸಂಖ್ಯೆ - ದಿನಾಂಕ  "  + apiResponse.getContent().get(0).getDate() + "\n" +
                    "                                                 \n" +
                    " 3. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಕ್ರಿಯಾಯೋಜನೆ ಸುತೋಲೆ ಸಂಖ್ಯೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate()+ "\n" +
                    "                                                                     \n" +
                    " 4. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಜ್ಞಾಪನ ಸಂಖ್ಯೆ  ದಿನಾಂಕ  " + apiResponse.getContent().get(0).getDate()  + "\n"  +
                    "                                                                 \n" +
                    " 5. ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಸುತೋಲೆ ಸಂಖ್ಯೆ - ದಿನಾಂಕ  "  + apiResponse.getContent().get(0).getDate() +"\n" +
                    "                                                                                \n" +
                    " 6. ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ :ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ :ತೋಇ/61/ರೇಕೃವಿ/2019.ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n" +
                    "                                                                            \n" +
                    " 7. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ .ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು -ವಿಭಾಗರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ : - ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n"

            );

            response.setHeader7("ಪೀಠಿಕೆ ");
            response.setHeader8(  "               " + apiResponse.getContent().get(0).getFinancialYear() +" ನೇ ಸಾಲಿನಲ್ಲಿ ವಿವಿಧ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆಗಳಡಿ ವಿವಿಧ ಕಾರ್ಯಕ್ರಮಗಳ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಉಲ್ಲೇಖ (1) ರಲ್ಲಿ ಸರ್ಕಾರವು ಆಡಳಿತಾತ್ಮಕ ಅನುಮೋದನೆಯನ್ನು ನೀಡಿದ್ದು , ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ \n" +
                    "                                       \n"+
                    "ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ) ಸಲಕರಣೆಗಳ/ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ\n" +
                    "                                       \n"+
                    "ಮಾರ್ಗಸೂಚಿಯನ್ನು ಉಲ್ಲೇಖ(3)ರಲ್ಲಿ ಕ್ರಿಯಾಯೋಜನೆ ಮತ್ತು ಉಲ್ಲೇಖ (4)ರಲ್ಲಿ ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಅನುಧಾನ ಬಿಡುಗಡೆಯಾಗಿರುತ್ತದೆ .ಉಲ್ಲೇ(5)ರಲ್ಲಿ ಸರಬರಾಜು ಸಂಸ್ಥೆಗಳನ್ನು\n"+
                    "                                       \n"+
                    "ಗುರುತಿಸಲಾಗಿದೆ.ಗುರುತಿಸಲಾಗಿರುವ ಸಂಸ್ಥೆಗಳಿಂದ ಸರಬರಾಜು ಪಡೆದಲ್ಲಿ ಮಾತ್ರ ಸಹಾಯಧನ ಪಡೆಯಲು ಅವಕಾಶವಿರುತ್ತದೆ .ಇಲಾಖೆಯು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ\n"+
                    "                                       \n"+
                    "ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ ಯೋಜನೆಯನ್ನು ಅನುಷ್ಠಾನಗೊಳಿಸುತ್ತಿದೆ .ಸದರಿ ಯೋಜನೆಯಡಿ) ರೇಷ್ಮೆ ಹುಳು ಶಾಸಕಣಿಕೆ(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ)ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ\n"+
                    "                                       \n"+
                    "ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಸಾಮಾನ್ಯ(SCP/TSP)ವರ್ಗದಡಿ ಕೇಂದ್ : ರಾಜ್ಯ : ಫಲಾನುಭವಿ ಪಾಲು 50:25:25/65:25:10 ಆಗಿರುತ್ತದೆ.ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ\n" +
                    "                                       \n"+
                    "(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ )ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ ಘಟಕ ದರ  ರೂ . "+apiResponse.getContent().get(0).getCost()+" ಲಕ್ಷಗಳಿಗೆ \n"+
                    "                                       \n"+
                    "ನಿಗದಿಪಡಿಸಿದ್ದು .ಇದರಲ್ಲಿ  ಶೇಕಡ 75 ರಷ್ಟನ್ನು  ಅಂದರೆ ರೂ . "+apiResponse.getContent().get(0).getCost()+ " ಲಕ್ಷಗಳನ್ನು ಸಹಾಯಧನವಾಗಿ ನೀಡಲಾಗುತ್ತದೆ .ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ . 50/65  ಅಂದರೆ ರೂ . "+apiResponse.getContent().get(0).getCost()+"ಲಕ್ಷಗಳು\n"+
                    "                                       \n"+
                    "ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕದರದ ಶೇ.25 ಅಂದರೆ ರೂ . "+apiResponse.getContent().get(0).getCost()+"ಲಕ್ಷಗಳು ಆಗಿರುತ್ತದೆ . ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ ಅನುದಾನವನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ\n"+
                    "                                       \n"+
                    "ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ . ಕೇಂದ್ರದ  ಪಾಲಿನ ಸಹಾಯಧನ ರೂ . "+apiResponse.getContent().get(0).getCost()+" ಲಕ್ಷಗಳನ್ನು (50/65%) ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ\n"+
                    "                                       \n"+
                    "ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು, ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯ,ಬೆಂಗಳೂರುರವರ ಕಚೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ  ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ \n " +
                    "                                  \n" +
                    "ಮಾಡಲಾಗುತ್ತದೆ. ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ  ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423) ಅಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು\n" +
                    "                                     \n" +
                    "ಬೆಂಗಳೂರುರವರು  ಖಜಾನೆ -2 ಮುಖಾಂತರ  ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು . ಫಲಾನುಭವಿಯ  ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ  ಜಮಾ ಮಾಡಲಾಗುವುದು");
            response.setHeader9(  "                " +apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆಯ " + apiResponse.getContent().get(0).getTalukName() +"ತಾಲೂಕಿನ  __ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ  " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದಲ್ಲಿ  ಸಾಮಾನ್ಯ /SCP/TSP ವರ್ಗಕ್ಕೆ ಸೇರಿದ  ಶ್ರೀ ./ಶ್ರೀಮತಿ ." + apiResponse.getContent().get(0).getFarmerFirstName() +"\n"+
                    "                                       \n"+
                    "  ಬಿನ್/ಕೋಂ   " +apiResponse.getContent().get(0).getFatherNameKan() + "ಇವರು " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ ಸರ್ವೆನಂ  __ ರಲ್ಲಿ __ಹೆಕ್ಟೇರು ವಿಸ್ತೀರ್ಣದಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಹೊಂದಿದ್ದು " +apiResponse.getContent().get(0).getVillageName()+ "ಗ್ರಾಮದ ಸುರ್ವೆನಂ /ಖಾತೆ  ನಂ  __ರಲ್ಲಿ\n" +
                    "                                                                     \n" +
                    "ಚದರಡಿ ವಿಸ್ತೀರ್ಣದ __ಮೇಲ್ಚಾವಣಿಯ ಪ್ರತ್ಯೇಕ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಯನ್ನು ಹೊಂದಿರುತ್ತಾರೆ . ಸದರಿಯವರು ರೇಷ್ಮೆ ಹುಳು ಇಲಾಖೆಯು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ)\n" +
                    "                                            \n" +
                    "ಸಲಕರಣೆಗಳು /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ ಸಲಕರಣೆಗಳನ್ನು ಗುರುತಿಸಿರುವ  ಸರಬರಾಜು ಸಂಸ್ಥೆಗಳಿಂದ ಸರಬರಾಜು  ಪಡೆದಿರುತ್ತಾರೆ .");
            response.setHeader10("                 ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ (ಸುಧಾರಿತ ಮೌಂಟೇಜಸ್ ಗಳು ಸೇರಿದಂತೆ)  ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ನಿರ್ವಹಣಾ   ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ\n" +
                    "                                                                    \n" +
                    "ಸರಬರಾಜು ಪಡೆದು ಸಲಕರಣೆಗಳ ವಿವರ  ಈ ಕೆಳಕಂಡಂತಿದೆ .  ");
            response.setHeader11("ಉಪಕರಣಗಳ ವಿವರ ");
            response.setHeader12("ಸಂಖ್ಯೆ  ");
            response.setHeader13("ದರ ");
            response.setHeader14("ಮೌಲ್ಯ ");
            response.setHeader15("ಖರೀದಿಸಿದ  ಸಂಸ್ಥೆ");
            response.setHeader16("ಬಿಲ್ ಸಂಖ್ಯೆ");
            response.setHeader17("ದಿನಾಂಕ ");
            response.setHeader18("              ರೂ . -ಲಕ್ಷಗಳ ವೆಚ್ಚದಲ್ಲಿ (ಸ್ವಂತ ವೆಚ್ಚ /ಬ್ಯಾಂಕಿನಿಂದ ಸಾಲ ಪಡೆದು)ಸರಬರಾಜು ಪಡೆದಿದ್ದು.--ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರರವರು/ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರವರು\n" +
                    "                                            \n" +
                    " -ವಿಭಾಗರವರು ಪರಿಶೀಲಿಸಿ ಧ್ರಡೀಕರಿಸಿ ಶಿಫಾರಿಸ್ಸಿನೊಂದಿಗೆ  ಸಂಬಂಧಿಸಿದ - ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರಿಗೆ ಎಲ್ಲ ಅಗತ್ಯ ದಾಖಲಾತಿಗಳನ್ನು ಒಳಗೊಂಡ ಪ್ರಸ್ತಾವನೆಯನ್ನು ಸಲ್ಲಿಸಿದ್ದು.\n" +
                    "                                           \n" +
                    "ಸದರಿ ಫಲಾನುಭವಿಗೆ  ರೂ . -- ಗಳ  ಸಹಾಯಧನವನ್ನು  ಮಂಜೂರು ಮಾಡುವಂತೆ ಉಲ್ಲೇಖ (7) ರಲ್ಲಿ  ಕೋರಿರುತ್ತಾರೆ .  ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರವರು ---\n" +
                    "                                      \n" +
                    "ವಿಭಾಗರವರು /ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರವರು" + apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆಯ  ಪಂಚಾಯತ್ ರವರ  ಮಂಜೂರಾತಿಗೆ ಕೊರಳಾಗಿರುವ' ಸಹಾಯಧನ  ಮಂಜೂರು ಮಾಡಲು ಉಲ್ಲೇಖ (6) ರಲ್ಲಿ  \n " +
                    "                                                 \n" +
                    "ಸರ್ಕಾರೀ ಆದೇಶದ ರೀತ್ಯಾ ಈ ಕಛೇರಿಯ ಅಧಿಕಾರ  ಪ್ರತ್ಯಾಯೋಜನೆ ವ್ಯಾಪ್ತಿಗೆ ಒಳಪಟ್ಟಿದ್ದು  ಅದರಂತೆ  ಸಹಾಯಧನ ಮಂಜೂರಾತಿಗಾಗಿ  ಈ  ಕೆಳಕಂಡ ಆದೇಶವನ್ನು ಹೊರಡಿಸಿದೆ . ");
            response.setHeader19("ಆದೇಶ   ");
            response.setHeader20("ಸಂಖ್ಯೆ:");
            response.setHeader21("ದಿನಾಂಕ ");
            response.setHeader22("               ಮೇಲಿನ ಪೀಠಿಕೆಯಲ್ಲಿ ವಿವರಿಸಿರುವಂತೆ  ರೇಷ್ಮೆ ವಿಸ್ತರಣಾದಿಕಾರಿಗಳು --ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರರವರು /ರೇಷ್ಮೆ ಸಹಾಯಕ  ನಿರ್ದೇಶಕರವರು --ವಿಭಾಗರವರು  ಶಿಫಾರಸ್ಸು ಮಾಡಿರುವಂತೆ.\n" +
                    "                                    \n" +
                    " "+ apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮದ  ಸಾಮಾನ್ಯ/SCP/TSP  ವರ್ಗಕ್ಕೆ  ಸೇರಿದ ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan() +"ರವರು ಕೇಂದ್ರ ವಲಯ ಸಿಲ್ಕ್ ಸಮಗ್ರ  ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ\n" +
                    "                                       \n" +
                    "(ಸುಧಾರಿತ ಮೌಂಟೇಜಸಗಳು ಸೇರಿದಂತೆ )ಸಲಕರಣೆಗಳ /ಹಿಪ್ಪುನೇರಳೆ  ತೋಟ ನಿರ್ವಹಣಾ ಸಲಕರಣೆಗಳ  ಖರೀದಿಗೆ ಘಟಕದರದ  ಶೇ .  75 / 90 ರಷ್ಟು  ಸಹಾಯಧನ ರೂ . -ಲಕ್ಷ(-ರೂ --\n" +
                    "                                     \n" +
                    "ಗಳು  ಮಾತ್ರ)ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ . ಈ ಸಹಾಯಧನದ ಪೈಕಿ  ರೂ . -ಲಕ್ಷ (-ರೂ . ಗಳು ಮಾತ್ರ) ಕೇಂದ್ರದ  ಪಾಲಾಗಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ನೀಡಿರುವೆ ಮೊತ್ತದಲ್ಲಿ ರೇಷ್ಮೆ \n" +
                    "                                      \n" +
                    "ನಿರ್ದೇಶನಾಲಯದಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿಗಳ  ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ  ಜಮಾ ಮಾಡಲಾಗುವುದು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲಾಗಿ ರೂ .-ಲಕ್ಷಗಳನ್ನು(-ರೂ ಗಳು ಮಾತ್ರ)ರಾಜ್ಯ \n" +
                    "                                       \n" +
                    "ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ  ಶೀರ್ಷಿಕೆ  2851-00-107-1-35(106)(422)(423)ರಡಿ  ಖಜಾನೆ-2 ರಲ್ಲಿ ಬಿಡುಗೋಡೆಗಳಿಸಿರುವ ಸಹಾಯಧನವನ್ನು  ಸಂಬಂಧಿಸಿದ ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n" +
                    "                                   \n" +
                    "ಖಜಾನೆ-2 ಮೂಲಕ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡುವುದು.ಈ ವೆಚ್ಚವನ್ನು ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ  2851-00-107-1-35(106)(422)(423) (ಸಾಮಾನ್ಯ/SCP/TSP)ಅಡಿ ಭರಿಸುವುದು.");
            response.setHeader23("ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು  ");
            response.setLineItemComment("ಪ್ರತಿಯನ್ನು \n" +
                    "                       \n" +
                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"\n" +
                    "                                      \n" +
                    " ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
                    "                            \n" +
                    " ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ,  ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName() );
            response.setHeader24(" 1. ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು ,ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ,-   \n"+
                    "                                       \n"+
                    " 2. ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು - ವಿಭಾಗರವರಿಗೆ ಎಲ್ಲಾ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ  ಮುಂದಿನ ಅಗತ್ಯ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ . \n"+
                    "                                       \n"+
                    " 3. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ ,ತಾಂತ್ರಿಕ ಸೇವಾಕೇಂದ್ರ -ರವರಿಗೆ ಎಲ್ಲಾ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ  ಮುಂದಿನ ಅಗತ್ಯ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ . ");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionBeneficiaryResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionBeneficiaryResponseList);
    }
    private JRDataSource getDataSourceForSupplyOrder(SupplyOrderPrintRequest requestDto) throws JsonProcessingException {

        SupplyOrderResponse apiResponse = apiService.fetchDataFromSupply(requestDto);
        List<SupplyOrderLetterReportResponse> supplyOrderLetterReportResponseList = new LinkedList<>();
        SupplyOrderLetterReportResponse response = new SupplyOrderLetterReportResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("  ಜಾನ್ ಡೀರ್ ಇಂಡಿಯಾ ಪ್ರೈವೇಟ್ ಲಿಮಿಟೆಡ್ ಸಂಸ್ಥೆಯ Rotavater/Side Shift Rotavater  (Model : 13-18HP");
            response.setHeader2(" ರೇಷ್ಮೆ  ಯಂತ್ರೋಪಕರಣ/ ಸಂಸ್ಕರಣಾ ಘಟಕವನ್ನು ರೇಷ್ಮೆ  ಇಲಾಖೆಯ ಮಾರ್ಗ ಸೂಚಿ ಅನ್ಮಯಾ");
            response.setFinancialYear("                " +apiResponse.getContent().get(0).getFinancialYear() +"ನೇ ಸಾಲಿನಲ್ಲಿ ರೇಷ್ಮೆ ಯಾಂತ್ರೀಕರಣ ಯೋಜನೆಯಡಿ /ರೇಷ್ಮೆ ಉತ್ಪನ್ನಗಳ ಸಂಸ್ಕರಣೆ " +apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆ , "  +apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , " + apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , " +apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ\n" +
                    "                                             \n " +
                            "ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() +" ರವರು  (ನೋಂದಣಿ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getFruitsId() + " , ಮೊಬೈಲ್ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getMobileNumber() + ")  ಅದ ಇವರು ಮೆ :  " +apiResponse.getContent().get(0).getVendorName() +" ಸಂಸ್ಥೆಯ \n" +
                    "                                                            \n" +
                    "" +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"  ರೇಷ್ಮೆ ಯಂತ್ರೋಪಕರಣ /ಸಂಸ್ಕರಣಾ ಘಟಕವನ್ನು  ರೇಷ್ಮೆ  ಇಲಾಖೆಯ  ಮಾರ್ಗ ಸೂಚಿ ಅನ್ವಯ   ಪಡೆಯಲು  ರೈತರ ವಂತಿಕೆ ರೂ . " + apiResponse.getContent().get(0).getCost() +  "\n" +
                    "                                                                  \n" +
                    "(UTR ಸಂಖ್ಯೆ : ==  ) ಗಳನ್ನೂ ಪಾವತಿಸಿದ್ದು ಇವರಿಗೆ ಸದರಿ ರೇಷ್ಮೆ  ಯಂತ್ರೋಪಕರಣ / ಸಂಸ್ಕರಣಾ ಘಟಕವನ್ನು " + apiResponse.getContent().get(0).getScComponentName() + "ಸರಬರಾಜು ಮಾಡಲು ಕಾರ್ಯಾದೇಶ  ನೀಡಲಾಗುತ್ತದೆ . \n" +
                    "                                                 \n" +
                    "(Schemes : Financial Year -  "+apiResponse.getContent().get(0).getFinancialYear() + " State Sector TSP -  RS. " + apiResponse.getContent().get(0).getCost() +  " , Financial Year - " + apiResponse.getContent().get(0).getFinancialYear() + "SMAM TSP - Rs. " + + apiResponse.getContent().get(0).getCost() +" )\n" +
                    "                                                     \n" +
                    "ಈ ಕಾರ್ಯಾದೇಶ ಪಡೆದ ನಂತರ ನಿಗಡಿತ ಅವಧಿಯೊಳಗೆ ಕಾರ್ಯ ಪೂರ್ಣಗೊಳಿಸಿ ಅಗತ್ಯ ದಾಖಲಾತಿಗಳನ್ನು ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಛೇರಿ ," + apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು ಇವರಿಗೆ \n" +
                            "                      \n" +
                    "ಸಲ್ಲಿಸಲು ಈ ಮೂಲಕ ತಿಳಿಸಿದೆ");
            response.setLineItemComment(" ಪ್ರತಿಯನ್ನು   ಶ್ರೀ . /ಶ್ರೀಮತಿ.   "+ apiResponse.getContent().get(0).getFarmerFirstName() + "  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan() + "   "+ apiResponse.getContent().get(0).getVillageName()+"  ಗ್ರಾಮ " + apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , " + apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , " +apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆ , \n " +
                    "                                                      \n" +
                    "ಇವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ  ಮತ್ತು ಮುಂದಿನ ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ ");
            response.setFruitsId( " (ನೋಂದಣಿ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getFruitsId() + " , ಮೊಬೈಲ್ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getMobileNumber() + ")  ಅದ ಇವರು ಮೆ :  " +apiResponse.getContent().get(0).getVendorName());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + " ಜಿಲ್ಲೆ , ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setAddressText(apiResponse.getContent().get(0).getAddressText());
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setScComponentName(apiResponse.getContent().get(0).getScComponentName());
            supplyOrderLetterReportResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(supplyOrderLetterReportResponseList);
    }
}
