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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")" + apiResponse.content.getFarmerNameKannada() + " " + apiResponse.content.getFarmerAddress());
            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText + apiResponse.content.getReelerNameKannada() + " " + reelerAddressText);

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
            if (apiResponse.content.getBidAmount().equals("0.0")) {
                apiResponse.content.setBidAmount("");
            }
            if (apiResponse.content.getLotWeight().equals("0.0")) {
                apiResponse.content.setLotWeight("");
            }
            if (apiResponse.content.getLotSoldOutAmount().equals("0.0")) {
                apiResponse.content.setLotSoldOutAmount("");
            } else {
                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));
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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")" + apiResponse.content.getFarmerNameKannada() + " " + apiResponse.content.getFarmerAddress());
            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText + apiResponse.content.getReelerNameKannada() + " " + reelerAddressText);

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
                apiResponse.content.setTotalamount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getLotSoldOutAmount()))));
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
            }
            if (apiResponse.content.getFeespaid().equals("0.0+0.0=0.0")) {
                apiResponse.content.setFeespaid("");
            } else {
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
                apiResponse.content.setFeespaid(value1 + "+" + value2 + "=" + String.valueOf(roundedResult));
            }
            if (!apiResponse.content.getBidAmount().equals("")) {
                apiResponse.content.setReeleramount("Balance: " + roundToWholeNumber(Double.parseDouble(apiResponse.content.getReelerbalance())));
            } else {
                apiResponse.content.setReeleramount("");
            }
            String markFee = "0";
            if (apiResponse.content.getMarketFee() != null && !apiResponse.content.getMarketFee().equals("")) {
                markFee = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getMarketFee())));
            }
            String tot_amt = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount())) + roundToWholeNumber(Double.parseDouble(markFee)));
            apiResponse.content.setReelerbalance("Lot value: " + roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount())) + "+" + roundToWholeNumber(Double.parseDouble(markFee)) + "=" + tot_amt);

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
        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = requestDto.getToDate().format(formatter);
        content.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + marketNameKannada + " ದಿನವಹಿ ವಹಿವಾಟು ತಖ್ತೆ  : " + formattedDate);
        content.setTotal_weight_with_amount_details("Wt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount()) + ", Farmer Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()) + ",MF: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Reeler Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
        content.setTotal_lots("Total lots: " + apiResponse.getContent().getTotalLots());
        content.setTransacted_lots("Total Transacted Lots: " + apiResponse.getContent().getPaymentSuccessLots());
        if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
            content.setNot_transacted_lots("Not Transacted Lots: " + (apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
        } else {
            content.setNot_transacted_lots("Not Transacted Lots: 0");
        }
        content.setFarmer_cheque("Farmer cheque Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
        content.setMf_amount("MF Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        content.setReeler_transaction_amt("Reeler transaction Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
        List<DTROnlineReportUnitDetail> contentList = new LinkedList<>();
        contentList.add(content);

        for (DTROnlineReportUnitDetail dtrOnlineReportUnitDetail : apiResponse.getContent().getDtrOnlineReportUnitDetailList()) {

            String farmerAddress = "";
            if (dtrOnlineReportUnitDetail.getFarmerAddress() != null) {
                farmerAddress = "/" + dtrOnlineReportUnitDetail.getFarmerAddress() + ",";
            }

            // Define date format
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//            // Format LocalDate to desired format
//            String formattedDate = requestDto.getToDate().format(formatter);

            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getBankName() + "/" + dtrOnlineReportUnitDetail.getAccountNumber());
            dtrOnlineReportUnitDetail.setFarmerDetails(dtrOnlineReportUnitDetail.getFarmerFirstName() + " " + dtrOnlineReportUnitDetail.getFarmerMiddleName() + " " + dtrOnlineReportUnitDetail.getFarmerLastName() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + farmerAddress + " (" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ")");
            dtrOnlineReportUnitDetail.setReelerDetails(dtrOnlineReportUnitDetail.getReelerName() + "(" + dtrOnlineReportUnitDetail.getReelerLicense() + ")" + "(" + dtrOnlineReportUnitDetail.getReelerMobile() + ")");
            dtrOnlineReportUnitDetail.setMarketFee(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee()))));
            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.valueOf((float) roundToTwoDecimalPlaces(Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount()))));
            dtrOnlineReportUnitDetail.setFarmerAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount()))));
            dtrOnlineReportUnitDetail.setReelerAmount(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount()))));
            dtrOnlineReportUnitDetail.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + marketNameKannada + " ದಿನವಹಿ ವಹಿವಾಟು ತಖ್ತೆ  : " + formattedDate);
            //   dtrOnlineReportUnitDetail.setTotal_weight_with_amount_details("Wt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight())+" , Amount: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount())+", Farmer Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount())+ ",MF: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee())+", Reeler Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setTotal_lots("Total lots: " + apiResponse.getContent().getTotalLots());
            content.setTransacted_lots("Total Transacted Lots: " + apiResponse.getContent().getPaymentSuccessLots());
            if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
                content.setNot_transacted_lots("Not Transacted Lots: " + (apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
            } else {
                content.setNot_transacted_lots("Not Transacted Lots: 0");
            }
            dtrOnlineReportUnitDetail.setFarmer_cheque("Farmer cheque Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
            dtrOnlineReportUnitDetail.setMf_amount("MF Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
            dtrOnlineReportUnitDetail.setReeler_transaction_amt("Reeler transaction Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            contentList.add(dtrOnlineReportUnitDetail);
        }
        DTROnlineReportUnitDetail contentLastColumn = new DTROnlineReportUnitDetail();
        contentLastColumn.setSerialNumber("");
        contentLastColumn.setAllottedLotId("");
        contentLastColumn.setFarmerDetails("");
        contentLastColumn.setWeight("Wt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight()));
        contentLastColumn.setBidAmount("");
        contentLastColumn.setLotSoldOutAmount("Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount()));
        contentLastColumn.setFarmerAmount("F Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount()));
        contentLastColumn.setMarketFee("MF: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        contentLastColumn.setReelerAmount("R Amt: " + roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
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
                lotReportResponse.setAcceptedTime(convertToTime(lotReportResponse.getAcceptedTime()));
            }
            if (lotReportResponse.getBidTime() == null) {
                lotReportResponse.setBidTime("");
            } else {
                lotReportResponse.setBidTime(convertToTime(lotReportResponse.getBidTime()));
            }
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
        lotReportResponse1.setHeaderText2("Lot Number = " + requestDto.getLotId() + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
        contentList.add(0, lotReportResponse1);
        return new JRBeanCollectionDataSource(contentList);
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
                lotReportResponse.setAcceptedTime(convertToTime(lotReportResponse.getAcceptedTime()));
            }
            if (lotReportResponse.getBidTime() == null) {
                lotReportResponse.setBidTime("");
            } else {
                lotReportResponse.setBidTime(convertToTime(lotReportResponse.getBidTime()));
            }
            contentList.add(lotReportResponse);
        }
        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
        lotReportResponse1.setHeaderText2("Reeler Id = " + requestDto.getReelerNumber() + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
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
            if(apiResponse.getContent().getStateWiseLotStatus().size()>0){
                for(int i=0; i<apiResponse.getContent().getStateWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotStateStatus = new GroupLotStatus();
                    groupLotStateStatus.setStateName(apiResponse.getContent().getStateWiseLotStatus().get(i).getDescription());
                    groupLotStateStatus.setLot21(apiResponse.getContent().getStateWiseLotStatus().get(i).getLot());
                    groupLotStateStatus.setWeight21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getWeight()))));
                    groupLotStateStatus.setAmount21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getAmount()))));
                    groupLotStateStatus.setMax21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getMax()))));
                    groupLotStateStatus.setMin21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getMin()))));
                    groupLotStateStatus.setAvg21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg()))));
                    groupLotStateStatus.setMf21(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getStateWiseLotStatus().get(i).getMf()))));
                    groupStateLotStatuses.add(groupLotStateStatus);
                }
            }

            List<GroupLotStatus> groupRaceLotStatuses = new ArrayList<>();
            if(apiResponse.getContent().getRaceWiseLotStatus().size()>0){
                for(int i=0; i<apiResponse.getContent().getRaceWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotRaceStatus = new GroupLotStatus();
                    groupLotRaceStatus.setRaceName(apiResponse.getContent().getRaceWiseLotStatus().get(i).getDescription());
                    groupLotRaceStatus.setLot31(apiResponse.getContent().getRaceWiseLotStatus().get(i).getLot());
                    groupLotRaceStatus.setWeight31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getWeight()))));
                    groupLotRaceStatus.setAmount31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount()))));
                    groupLotRaceStatus.setMax31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax()))));
                    groupLotRaceStatus.setMin31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin()))));
                    groupLotRaceStatus.setAvg31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg()))));
                    groupLotRaceStatus.setMf31(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf()))));
                    groupRaceLotStatuses.add(groupLotRaceStatus);
                }
            }

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getForm13Data(request);
            parameters.put("datasource1", groupStateLotStatuses);
            parameters.put("datasource2", groupRaceLotStatuses);

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

            apiResponse.setWeight1((apiResponse.getContent().getLotsFrom0to351().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(0).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(0).getWeight()))) : "");
            apiResponse.setWeight2((apiResponse.getContent().getLotsFrom0to351().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(1).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(1).getWeight()))) : "");
            apiResponse.setWeight3((apiResponse.getContent().getLotsFrom0to351().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(2).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(2).getWeight()))) : "");
            apiResponse.setWeight4((apiResponse.getContent().getLotsFrom0to351().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(3).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(3).getWeight()))) : "");
            apiResponse.setWeight5((apiResponse.getContent().getLotsFrom0to351().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(4).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(4).getWeight()))) : "");
            apiResponse.setWeight6((apiResponse.getContent().getLotsFrom0to351().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(5).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(5).getWeight()))) : "");
            apiResponse.setWeight7((apiResponse.getContent().getLotsFrom0to351().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom0to351().get(6).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom0to351().get(6).getWeight()))) : "");
            apiResponse.setTotalWeight1(String.valueOf(roundToTwoDecimalPlaces(totalWeight1)));


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

            apiResponse.setWeight41((apiResponse.getContent().getLotsFrom201to300().get(0).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(0).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(0).getWeight()))) : "");
            apiResponse.setWeight42((apiResponse.getContent().getLotsFrom201to300().get(1).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(1).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(1).getWeight()))) : "");
            apiResponse.setWeight43((apiResponse.getContent().getLotsFrom201to300().get(2).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(2).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(2).getWeight()))) : "");
            apiResponse.setWeight44((apiResponse.getContent().getLotsFrom201to300().get(3).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(3).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(3).getWeight()))) : "");
            apiResponse.setWeight45((apiResponse.getContent().getLotsFrom201to300().get(4).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(4).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(4).getWeight()))) : "");
            apiResponse.setWeight46((apiResponse.getContent().getLotsFrom201to300().get(5).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(5).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(5).getWeight()))) : "");
            apiResponse.setWeight47((apiResponse.getContent().getLotsFrom201to300().get(6).getWeight() != null && !apiResponse.getContent().getLotsFrom201to300().get(6).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getLotsFrom201to300().get(6).getWeight()))) : "");

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
            apiResponse.setWeight11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getWeight()))));
            apiResponse.setAmount11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAmount()))));
            apiResponse.setMax11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMax()))));
            apiResponse.setMin11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMin()))));
            apiResponse.setMf11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMf()))));
            apiResponse.setAvg11(String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAvg()))));
        }

        if (apiResponse.getContent().getAverageLotStatus().size() > 0) {
            apiResponse.setAverageDesc1(apiResponse.getContent().getAverageLotStatus().get(0).getDescription());
            apiResponse.setLot51(apiResponse.getContent().getAverageLotStatus().get(0).getLot());
            apiResponse.setWeight51((apiResponse.getContent().getAverageLotStatus().get(0).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getWeight()))) : "");
            apiResponse.setPerc51((apiResponse.getContent().getAverageLotStatus().get(0).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(0).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(0).getPercentage()))) : "");

            apiResponse.setAverageDesc2(apiResponse.getContent().getAverageLotStatus().get(1).getDescription());
            apiResponse.setLot52(apiResponse.getContent().getAverageLotStatus().get(1).getLot());
            apiResponse.setWeight52((apiResponse.getContent().getAverageLotStatus().get(1).getWeight() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getWeight().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getWeight()))) : "");
            apiResponse.setPerc52((apiResponse.getContent().getAverageLotStatus().get(1).getPercentage() != null && !apiResponse.getContent().getAverageLotStatus().get(1).getPercentage().equals("")) ? String.valueOf(roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageLotStatus().get(1).getPercentage()))) : "");
        }

        List<Form13ReportResponse> form13ReportResponses = new LinkedList<>();
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, " + apiResponse.getContent().getMarketNameKannada() + " ದಿನವಹಿ ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(String.valueOf(requestDto.getAuctionDate())));
        apiResponse.setAverageRate("Average Rate Rs."+roundToTwoDecimalPlaces(Double.parseDouble(apiResponse.getContent().getAverageRate())));
        form13ReportResponses.add(apiResponse);
        return new JRBeanCollectionDataSource(form13ReportResponses);
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
