package com.sericulture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sericulture.model.*;
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
import java.time.format.DateTimeFormatter;
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
            JasperReport jasperReport = getJasperReport("kannada_triplicate_with_variable_1.jrxml");

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
        if(apiResponse.content != null) {

            String formatfees = roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + roundToTwoDecimalPlaces((apiResponse.content.getFarmerMarketFee() + apiResponse.content.getReelerMarketFee()));
            apiResponse.content.setFeespaid(formatfees);

            Double total = Double.valueOf(apiResponse.content.getLotSoldOutAmount());
            Double farmerfee = apiResponse.content.getFarmerMarketFee();
            Double realerfee = apiResponse.content.getReelerMarketFee();
            String farmeramout = "" + roundToTwoDecimalPlaces((total - farmerfee));
            String relaramout = "" + roundToTwoDecimalPlaces((total - realerfee));
            Double slip1Amount = 0.0;
            slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
            apiResponse.content.setAmountfarmer(farmeramout);
            apiResponse.content.setAmountrealar(relaramout);
            apiResponse.content.setLoginname_accountnumber_ifsccode("(" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
            apiResponse.content.setAccountnumber_ifsccode("Bank - " + apiResponse.content.getAccountNumber() + "(" + apiResponse.content.getIfscCode() + ")");
            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount.toString());
          //  apiResponse.content.setReeleramount(relaramout);
            String inputDateTime = "";
            if (apiResponse.content.getAuctionDateWithTime() != null) {
                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            }else{
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

            if(apiResponse.content.getReelerNameKannada() == null){
                apiResponse.content.setReelerNameKannada("");
            }
            if(apiResponse.content.getFarmerNameKannada() == null){
                apiResponse.content.setFarmerNameKannada("");
            }
            if(apiResponse.content.getReelerLicense() == null){
                apiResponse.content.setReelerLicense("");
            }

            apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + apiResponse.content.getFarmerNumber() + ")" + apiResponse.content.getFarmerNameKannada() +" " + apiResponse.content.getFarmerAddress());
            String reelerNumberText = "";
            String reelerAddressText = "";
            if(apiResponse.content.getReelerNumber()!= null){
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if(apiResponse.content.getReelerAddress()!= null){
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText + apiResponse.content.getReelerNameKannada() +" " + reelerAddressText);

            if (apiResponse.content.getSmallBinList() != null) {
                List<String> smallBinList = apiResponse.content.getSmallBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                smallBins = String.join(",", smallBinList);
            }
            apiResponse.content.setAcknowledgmentString("ಈ ಮೇಲೆ ನಮೂದಿಸಿದ ವಿಷಯಗಳು ಸರಿಯಾಗಿವೆಯೆಂದು ದೃಢೀಕರಿಸುತ್ತೇನೆ ಹಾಗು ಲೈಸೆನ್ಸ್ ಪಡೆದವರಿಗೆ /ಪ್ರತಿನಿಧಿಗೆ ಕೆ.ಜಿ. ಗೂಡುಗಳನ್ನು " +apiResponse.content.getAuctionDate() + " ದಿನ _______ ಘಂಟೆಯೊಳಗಾಗಿ    ಸಾಗಿಸಲು ಅನುಮತಿ ನೀಡಿದ್ದೇನೆ.");

            if (apiResponse.content.getBigBinList() != null) {
                List<String> bigBinList = apiResponse.content.getBigBinList().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                bigBins = String.join(",", bigBinList);
            }
            apiResponse.content.setBinno("Big: " + bigBins + " Small: " + smallBins);

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
            if(apiResponse.content.getBidAmount().equals("0.0")){
                apiResponse.content.setBidAmount("");
            }
            if(apiResponse.content.getLotWeight().equals("0.0")){
                apiResponse.content.setLotWeight("");
            }
            if(apiResponse.content.getLotSoldOutAmount().equals("0.0")){
                apiResponse.content.setLotSoldOutAmount("");
            }else{
                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));
            }
            if(apiResponse.content.getFeespaid().equals("0.0+0.0=0.0")){
                apiResponse.content.setFeespaid("");
            }
            if(!apiResponse.content.getBidAmount().equals("")) {
                apiResponse.content.setReeleramount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.content.getTotalamount()) + apiResponse.content.getReelerMarketFee())));
            }else{
                apiResponse.content.setReeleramount("");
            }
            countries.add(apiResponse.content);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(countries);
    }

    private static double roundToTwoDecimalPlaces(double value) {
        // Use BigDecimal for rounding to 2 decimal places
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    @PostMapping("/dtr-online-report")
    public ResponseEntity<?> dtrOnlineReport(@RequestBody DTROnlineRequest request){

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

    private  JRBeanCollectionDataSource getDtrOnlineReportData(DTROnlineRequest requestDto) throws JsonProcessingException {
        DTRReportResponse apiResponse = apiService.dtrReport(requestDto);
        List<DTROnlineReportUnitDetail> contentList = new LinkedList<>();
        for(DTROnlineReportUnitDetail dtrOnlineReportUnitDetail: apiResponse.getContent().getDtrOnlineReportUnitDetailList()) {

            String farmerAddress = "";
            if(dtrOnlineReportUnitDetail.getFarmerAddress() != null){
                farmerAddress = "/" + dtrOnlineReportUnitDetail.getFarmerAddress() +",";
            }

            String marketNameKannada = "";
            if(dtrOnlineReportUnitDetail.getMarketNameKannada() != null){
                marketNameKannada = dtrOnlineReportUnitDetail.getMarketNameKannada();
            }
            // Define date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Format LocalDate to desired format
            String formattedDate = requestDto.getToDate().format(formatter);

            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getBankName() + "/" + dtrOnlineReportUnitDetail.getAccountNumber());
            dtrOnlineReportUnitDetail.setFarmerDetails(dtrOnlineReportUnitDetail.getFarmerFirstName() + " " +dtrOnlineReportUnitDetail.getFarmerMiddleName() +" " + dtrOnlineReportUnitDetail.getFarmerLastName() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + farmerAddress + " (" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() +")");
            dtrOnlineReportUnitDetail.setReelerDetails(dtrOnlineReportUnitDetail.getReelerName() + "(" + dtrOnlineReportUnitDetail.getReelerLicense() + ")" + "(" + dtrOnlineReportUnitDetail.getReelerMobile() +")");
            dtrOnlineReportUnitDetail.setMarketFee(roundToTwoDecimalPlaces(dtrOnlineReportUnitDetail.getFarmerMarketFee() + dtrOnlineReportUnitDetail.getReelerMarketFee()));
            dtrOnlineReportUnitDetail.setLotSoldOutAmount((float) roundToTwoDecimalPlaces(dtrOnlineReportUnitDetail.getLotSoldOutAmount()));
            dtrOnlineReportUnitDetail.setFarmerAmount(roundToTwoDecimalPlaces(dtrOnlineReportUnitDetail.getFarmerAmount()));
            dtrOnlineReportUnitDetail.setReelerAmount(roundToTwoDecimalPlaces(dtrOnlineReportUnitDetail.getReelerAmount()));
            dtrOnlineReportUnitDetail.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, "+marketNameKannada+" ದಿನವಹಿ ವಹಿವಾಟು ತಖ್ತೆ  : "+formattedDate);
            dtrOnlineReportUnitDetail.setTotal_weight_with_amount_details("Wt: , Amount: 0, Farmer Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount())+ ",MF: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee())+", Reeler Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setTotal_lots("Total lots: "+apiResponse.getContent().getTotalLots());
            dtrOnlineReportUnitDetail.setFarmer_cheque("Farmer cheque Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
            dtrOnlineReportUnitDetail.setMf_amount("MF Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee()));
            dtrOnlineReportUnitDetail.setReeler_transaction_amt("Reeler transaction Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            contentList.add(dtrOnlineReportUnitDetail);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-bidding-report")
    public ResponseEntity<?> getBiddingReport(@RequestBody BiddingReportRequest request){

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

    private  JRBeanCollectionDataSource getBiddingReportData(BiddingReportRequest requestDto) throws Exception {
        String marketName = "";
        BiddingReportResponse apiResponse = apiService.biddingReport(requestDto);
        List<LotReportResponse> contentList = new LinkedList<>();
        LotReportResponse lotReportResponse1 = new LotReportResponse();

        for(LotReportResponse lotReportResponse: apiResponse.getContent()) {
            marketName = lotReportResponse.getMarketName();

            if(lotReportResponse.getAcceptedBy() == null){
                lotReportResponse.setAcceptedBy("");
            }
            if(lotReportResponse.getAcceptedTime() == null){
                lotReportResponse.setAcceptedTime("");
            }else{
                lotReportResponse.setAcceptedTime(convertToTime(lotReportResponse.getAcceptedTime()));
            }
            if(lotReportResponse.getBidTime() == null){
                lotReportResponse.setBidTime("");
            }else{
                lotReportResponse.setBidTime(convertToTime(lotReportResponse.getBidTime()));
            }
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, "+marketName+"\n BIDDING REPORT" );
        lotReportResponse1.setHeaderText2("Lot Number = "+requestDto.getLotId() +" and Bid Date = "+convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(0,lotReportResponse1);
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-reeler-bidding-report")
    public ResponseEntity<?> getReelerBiddingReport(@RequestBody ReelerBiddingReportRequest request){

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

    private  JRBeanCollectionDataSource getReelerBiddingReportData(ReelerBiddingReportRequest requestDto) throws Exception {
        String marketName = "";
        BiddingReportResponse apiResponse = apiService.reelerBiddingReport(requestDto);
        List<LotReportResponse> contentList = new LinkedList<>();
        LotReportResponse lotReportResponse1 = new LotReportResponse();
        for(LotReportResponse lotReportResponse: apiResponse.getContent()) {
            marketName = lotReportResponse.getMarketName();
            if(lotReportResponse.getAcceptedBy() == null){
                lotReportResponse.setAcceptedBy("");
            }
            if(lotReportResponse.getAcceptedTime() == null){
                lotReportResponse.setAcceptedTime("");
            }else{
                lotReportResponse.setAcceptedTime(convertToTime(lotReportResponse.getAcceptedTime()));
            }
            if(lotReportResponse.getBidTime() == null){
                lotReportResponse.setBidTime("");
            }else{
                lotReportResponse.setBidTime(convertToTime(lotReportResponse.getBidTime()));
            }
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, "+marketName+"\n BIDDING REPORT" );
        lotReportResponse1.setHeaderText2("Reeler Id = "+requestDto.getReelerNumber() +" and Bid Date = "+convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(0,lotReportResponse1);
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-pending-report")
    public ResponseEntity<?> getPendingReports(@RequestBody PendingReportRequest request){

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

    private  JRBeanCollectionDataSource getPendingReportsData(PendingReportRequest requestDto) throws JsonProcessingException {
        PendingReportResponse apiResponse = apiService.pendingReportList(requestDto);
        List<Content> contentList = new LinkedList<>();
        Content lotReportResponse1 = new Content();
        lotReportResponse1.setHeaderText("Pending report for "+convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(lotReportResponse1);
        for(Content lotReportResponse: apiResponse.getContent()) {
            lotReportResponse.setShed("");
            if(lotReportResponse.getReelerNumber() == null){
                lotReportResponse.setReelerNumber("");
            }
            if(lotReportResponse.getReelerMobileNumber() == null){
                lotReportResponse.setReelerMobileNumber("");
            }
            if(lotReportResponse.getAccpetedBy() == null){
                lotReportResponse.setAccpetedBy("");
            }
            if(lotReportResponse.getReeler_amount() == null){
                lotReportResponse.setReeler_amount("");
            }else{
                lotReportResponse.setReeler_amount(String.valueOf(lotReportResponse.getReeleramount()));
            }
            lotReportResponse.setFarmerDetails(lotReportResponse.getFarmerFirstName() + " " + lotReportResponse.getFarmerMiddleName() + " " + lotReportResponse.getFarmerLastName());
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-farmer-txn-report")
    public ResponseEntity<?> getFarmerTxnReport(@RequestBody FarmerTxnRequest request){

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

    private  JRBeanCollectionDataSource getFarmerTxnReportsData(FarmerTxnRequest requestDto) throws JsonProcessingException {
        FarmerTxnResponse apiResponse = apiService.farmerTxnReportList(requestDto);
        List<FarmerTxnInfo> contentList = new LinkedList<>();
        FarmerTxnInfo lotReportResponse1 = new FarmerTxnInfo();
        lotReportResponse1.setHeaderText("e-Haraju Farmer Transaction Report - " +requestDto.getFarmerNumber()+ " \n From "+convertDate(requestDto.getReportFromDate().toString()) + " to "+convertDate(requestDto.getReportToDate().toString()));
        lotReportResponse1.setFarmer_details_farmer_transaction("Farmer Details: " +apiResponse.getContent().getFarmerNumber() + " " +apiResponse.getContent().getFarmerFirstName() + " " +apiResponse.getContent().getFarmerMiddleName() + " " +apiResponse.getContent().getFarmerLastName() + ","+apiResponse.getContent().getVillage());
        lotReportResponse1.setTotal_sale_amount_farmer_transaction("Total sale amount: Rs."+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalSaleAmount()));
        lotReportResponse1.setTotal_market_fee_farmer_transaction("Total market fee: Rs."+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalMarketFee()));
        lotReportResponse1.setTotal_amount_farmer_transaction("Total amount: Rs."+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
        contentList.add(lotReportResponse1);
        for(FarmerTxnInfo lotReportResponse: apiResponse.getContent().getFarmerTxnInfoList()) {
            lotReportResponse.setLotTransactionDate(convertDate(lotReportResponse.getLotTransactionDate()));
            lotReportResponse.setFarmerDetails("");
            lotReportResponse.setFarmerAmount(roundToTwoDecimalPlaces(lotReportResponse.getFarmerAmount()));
            lotReportResponse.setFarmerMarketFee(roundToTwoDecimalPlaces(lotReportResponse.getFarmerMarketFee()));
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/get-reeler-txn-report")
    public ResponseEntity<?> getReelerTxnReport(@RequestBody ReelerTxnRequest request){

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

    private  JRBeanCollectionDataSource getReelerTxnReportsData(ReelerTxnRequest requestDto) throws JsonProcessingException {
        ReelerTxnResponse apiResponse = apiService.reelerTxnReportList(requestDto);
        List<ReelerTransactionReport> contentList = new LinkedList<>();
        ReelerTransactionReport lotReportResponse1 = new ReelerTransactionReport();
        lotReportResponse1.setTotal_sale_amount_farmer_transaction("Total purchase Rs."+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalPurchase()));
        lotReportResponse1.setFarmer_details_farmer_transaction("Deposited Rs."+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalDeposits()));
        lotReportResponse1.setHeaderText("e-Haraju Reeler Transaction Report " + requestDto.getReelerNumber() +" \n From "+convertDate(requestDto.getReportFromDate().toString()) + " to "+convertDate(requestDto.getReportToDate().toString()));
        lotReportResponse1.setReeler_amount_balance("Opening Balance of Reeler Id " + requestDto.getReelerNumber() +", Name "+ apiResponse.getContent().getName() +" as on "  +convertDate(requestDto.getReportFromDate().toString()) + " is Rs." +apiResponse.getContent().getOpeningBalance());
        contentList.add(lotReportResponse1);
        for(ReelerTransactionReport lotReportResponse: apiResponse.getContent().getReelerTransactionReports()) {
            if(lotReportResponse.getDepositAmount() != null) {
                lotReportResponse.setDepositAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getDepositAmount()))));
            }else{
                lotReportResponse.setDepositAmount("");
            }
            if(lotReportResponse.getPaymentAmount() != null) {
                lotReportResponse.setPaymentAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getPaymentAmount()))));
            }else{
                lotReportResponse.setPaymentAmount("");
            }
            if(lotReportResponse.getBalance() != null) {
                lotReportResponse.setBalance(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(lotReportResponse.getBalance()))));
            }else{
                lotReportResponse.setBalance("");
            }
            if(lotReportResponse.getTransactionDate() != null && !lotReportResponse.getTransactionDate().equals("")) {
                lotReportResponse.setTransactionDate(convertDate(lotReportResponse.getTransactionDate()));
            }
            contentList.add(lotReportResponse);
        }
        return new JRBeanCollectionDataSource(contentList);
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

        Date date = sdfInput.parse(timeString);
        return sdfOutput.format(date);
    }
}
