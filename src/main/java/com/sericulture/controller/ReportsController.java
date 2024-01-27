package com.sericulture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sericulture.model.Content;
import com.sericulture.model.ContentRoot;
import com.sericulture.model.MarketAuctionForPrintRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
            JasperReport jasperReport = getJasperReport("kannada_triplicate_with_variable.jrxml");

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

    @PostMapping("/getfarmercopy-kannada")
    public ResponseEntity<?> getfarmercopyKannada(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("farmer_copy_with_variable.jrxml");

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
        File template = ResourceUtils.getFile("/reports/"+reportpath);
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
        String formatfees = roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + roundToTwoDecimalPlaces((apiResponse.content.getFarmerMarketFee()+apiResponse.content.getReelerMarketFee()));
        apiResponse.content.setFeespaid(formatfees);

        Double total = Double.valueOf(apiResponse.content.getLotSoldOutAmount());
        Double farmerfee = apiResponse.content.getFarmerMarketFee();
        Double realerfee = apiResponse.content.getReelerMarketFee();
        String farmeramout = ""+ roundToTwoDecimalPlaces((total - farmerfee));
        String relaramout = ""+ roundToTwoDecimalPlaces((total - realerfee));
        Double slip1Amount = 0.0;
        slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
        apiResponse.content.setAmountfarmer(farmeramout);
        apiResponse.content.setAmountrealar(relaramout);
        apiResponse.content.setLoginname_accountnumber_ifsccode("(" + apiResponse.content.getLoginName() +")" + "//Bank - " +apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
        apiResponse.content.setAccountnumber_ifsccode("Bank - " +apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
        apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee() )+ "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" +slip1Amount.toString());
        apiResponse.content.setReeleramount(relaramout);

        String inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
        // Parse the input date and time
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date parsedDate;
        try {
            parsedDate = inputFormat.parse(inputDateTime);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing input date and time", e);
        }
        // Format the output date and time
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
        String formattedDateTime = outputFormat.format(parsedDate);
        apiResponse.content.setAuctionDate_time(formattedDateTime);

        apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
        apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" +apiResponse.content.getSerialNumber() + ")" +apiResponse.content.getFarmerNameKannada());

        List<String> smallBinList = apiResponse.content.getSmallBinList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        String smallBins = String.join(",", smallBinList);

        List<String> bigBinList = apiResponse.content.getBigBinList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        String bigBins = String.join(",", bigBinList);
        apiResponse.content.setBinno("Big: "+bigBins + " Small: "+smallBins);


        apiResponse.content.setLogurl("/reports/Seal_of_Karnataka.PNG");
        countries.add(apiResponse.content);
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(countries);
    }

    private static double roundToTwoDecimalPlaces(double value) {
        // Use BigDecimal for rounding to 2 decimal places
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
