package com.sericulture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.DTRAllMarket.*;
import jakarta.xml.bind.JAXBException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.text.BreakIterator;
import java.util.Locale;
import java.text.Normalizer;

import static com.google.common.math.DoubleMath.roundToLong;
import static org.apache.http.client.utils.DateUtils.formatDate;
import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;
import static org.hibernate.type.descriptor.java.CoercionHelper.toLong;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@RestController
@RequestMapping("marketreport")
public class ReportsController {
    private final ApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    public ReportsController(ApiService apiService) {
        this.apiService = apiService;
    }

    @Autowired
    private RestTemplate restTemplate;

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


    @PostMapping("/gettripletpdf-kannada-seed")
    public ResponseEntity<?> gettripletpdfKannadaForSeedCocoon(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("kannada_triplicate_with_variable_seed_cocoon_1.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForTripletSeedCocoon(requestDto);

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

    @PostMapping("/gettripletpdf-kannada-silk")
    public ResponseEntity<?> gettripletpdfKannadaForSilk(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("kannada_triplicate_silk_type_new.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForTripletSilk(requestDto);

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


    @PostMapping("/getTransportationAck")
    public ResponseEntity<?> getTransportationAck(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getTransportationAck");
            logger.info("enter to getTransportationAck");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("TransportationAcknowledgement.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceAcknowledgementTransportation(requestDto);

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

    @PostMapping("/getIncentive30Ack")
    public ResponseEntity<?> getIncentive30Ack(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getIncentive30Ack");
            logger.info("enter to getIncentive30Ack");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("ACKIncentive30.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceAckIncentive30(requestDto);

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

    @PostMapping("/getChawki1000Ack")
    public ResponseEntity<?> getChawki1000Ack(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getChawki1000Ack");
            logger.info("enter to getChawki1000Ack");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("AckChawki1000.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceAckChawki1000(requestDto);

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


    @PostMapping("/getChawkiAck1500")
    public ResponseEntity<?> getChawkiAck1500(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getChawkiAck1500");
            logger.info("enter to getChawki1500Ack");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("AckChawki1500.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceAckChawki1500(requestDto);

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


    @PostMapping("/getReelerAcknowledgement")
    public ResponseEntity<?> getReelerAcknowledgement(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getReelerAcknowledgement");
            logger.info("enter to getReelerAcknowledgement");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("ReelerAcknowledgement.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceReelerAcknowledgement(requestDto);

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

    @PostMapping("/getSilkIncentive")
    public ResponseEntity<?> getSilkIncentiveAcknowledgement(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSilkIncentive");
            logger.info("enter to getSilkIncentive");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("AcknowledgementSilkIncentive.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceSilkIncentiveAcknowledgement(requestDto);

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


    @PostMapping("/getReelerAcknowledgementHRU")
    public ResponseEntity<?> getReelerAcknowledgementHRU(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getReelerAcknowledgement");
            logger.info("enter to getReelerAcknowledgement");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("ReelerAcknowledgementHRU.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceReelerAcknowledgementHRU(requestDto);

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

    @PostMapping("/get-seed-cocoon")
    public ResponseEntity<?> getSeedCocoon(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to seed cocoon");
            logger.info("enter to seed cocoon");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("SanctionOrderBonus.jrxml");

            JRDataSource dataSource = getDataSourceForSeedCocoon(requestDto);

            // 2. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

            // 3. datasource "java object"

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

    @PostMapping("/sanction-silk-incentive")
    public ResponseEntity<?> getSanctionSilkIncentive(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            JasperReport jasperReport = getJasperReport("sanctionOrderSilkIncentive.jrxml");

            // Get the FULL list (header + detail + total)
            JRBeanCollectionDataSource fullDs = getDataSourceForSanctionSilkIncentive(requestDto);
            @SuppressWarnings("unchecked")
            List<SanctionOrderResponse> fullList =
                    (List<SanctionOrderResponse>) fullDs.getData();

            // ---------- MAIN DATASOURCE: ONLY HEADER ----------
            List<SanctionOrderResponse> headerList = new ArrayList<>();
            if (!fullList.isEmpty()) {
                headerList.add(fullList.get(0));   // first element is "response"
            }
            JRBeanCollectionDataSource mainDataSource =
                    new JRBeanCollectionDataSource(headerList);

            // ---------- TABLE FOR sanctionBonus (big table) ----------
            // Exclude header (index 0)
            List<SanctionOrderResponse> sanctionBonusList = new ArrayList<>();
            if (fullList.size() > 1) {
                sanctionBonusList.addAll(fullList.subList(1, fullList.size())); // detail + total
            }
            JRBeanCollectionDataSource sanctionBonusTableDataSource =
                    new JRBeanCollectionDataSource(sanctionBonusList);

            // ---------- TABLE FOR silk (top table) ----------
            // If you want to show just the machine/category row, use header only:
            JRBeanCollectionDataSource silkTableDataSource =
                    new JRBeanCollectionDataSource(headerList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", silkTableDataSource);      // subDataset "silk"
            parameters.put("CollectionBeanParam1", sanctionBonusTableDataSource); // subDataset "sanctionBonus"

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mainDataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
        }
    }

    @PostMapping("/getChawkiSanctionOrderPdf")
    public ResponseEntity<?> getChawkiSanctionOrderPdf(
            @RequestBody SanctionOrderPrintRequest requestDto
    ) throws JsonProcessingException, JRException, FileNotFoundException {

        try {
            logger.info("enter to getChawkiSanctionOrderPdf");

            JasperReport jasperReport = getJasperReport("Sanction_OrderCRC.jrxml");

            JRBeanCollectionDataSource dataSource =
                    (JRBeanCollectionDataSource) getDataSourceForChawkiSanctionOrder(requestDto);

            @SuppressWarnings("unchecked")
            List<SanctionOrderResponse> fullList =
                    (List<SanctionOrderResponse>) dataSource.getData();

            if (fullList == null || fullList.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("No data found for CRC sanction order".getBytes(StandardCharsets.UTF_8));
            }

            Map<String, Object> parameters = getParameters(); // same helper used elsewhere

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Chawki_Sanction_Order.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error generating CRC sanction order PDF", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(
                    ex.getMessage().getBytes(StandardCharsets.UTF_8),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }



    @PostMapping("/sanction-psfa-reeling-shed")
    public ResponseEntity<?> getPsfaReelingShedSanction(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            JasperReport jasperReport = getJasperReport("sanctionOrderReelingShed.jrxml");

            JRBeanCollectionDataSource fullDs = getDataSourceForPsfaReelingShed(requestDto);

            @SuppressWarnings("unchecked")
            List<SanctionOrderResponse> fullList =
                    (List<SanctionOrderResponse>) fullDs.getData();

            List<SanctionOrderResponse> headerList = new ArrayList<>();
            if (!fullList.isEmpty()) {
                headerList.add(fullList.get(0));      // use first row as header bean
            }
            JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(headerList);

            List<SanctionOrderResponse> reelingShedList = new ArrayList<>();
            if (fullList.size() > 1) {
                reelingShedList.add(fullList.get(1));
            } else if (!fullList.isEmpty()) {
                reelingShedList.add(fullList.get(0));
            }

            JRBeanCollectionDataSource reelingShedDs =
                    new JRBeanCollectionDataSource(reelingShedList);

            Map<String, Object> parameters = new HashMap<>();
            // This must match the parameter name in JRXML: CollectionBeanParam1
            parameters.put("CollectionBeanParam1", reelingShedDs);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mainDataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "psfa_reeling_shed_sanction.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
        }
    }




    @PostMapping("/sanction-heat-unit")
    public ResponseEntity<?> getHeatRecoveryUnit(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            JasperReport jasperReport = getJasperReport("sanctionHeatRecoveryUnit.jrxml");

            JRBeanCollectionDataSource fullDs = getDataSourceForPsfaHRU(requestDto);
            @SuppressWarnings("unchecked")
            List<SanctionOrderResponse> fullList =
                    (List<SanctionOrderResponse>) fullDs.getData();

            List<SanctionOrderResponse> headerList = new ArrayList<>();
            if (!fullList.isEmpty()) {
                headerList.add(fullList.get(0));      // use first row as header bean
            }
            JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(headerList);

            List<SanctionOrderResponse> reelingShedList = new ArrayList<>();
            if (fullList.size() > 1) {
                reelingShedList.add(fullList.get(1));
            } else if (!fullList.isEmpty()) {
                reelingShedList.add(fullList.get(0));
            }

            JRBeanCollectionDataSource reelingShedDs =
                    new JRBeanCollectionDataSource(reelingShedList);

            Map<String, Object> parameters = new HashMap<>();
            // This must match the parameter name in JRXML: CollectionBeanParam1
            parameters.put("CollectionBeanParam1", reelingShedDs);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, mainDataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "psfa_reeling_shed_sanction.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
        }
    }


    @PostMapping("/get-Permit")
    public ResponseEntity<?> getPermit(@RequestBody LotStatusSeedMarketRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to get Permit");
            logger.info("enter to get Permit");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Permit.jrxml");

            // 3. datasource "java object"
//            JRDataSource dataSource = getDataSourceForPermit(requestDto);
            JRDataSource dataSource1 = getDataSourceForPermit(requestDto);        // ✅ Table 1 data
            JRDataSource dataSource2 = getDataSourceForPermit2(requestDto);

            // 2. parameters "empty"
//            Map<String, Object> parameters = new HashMap<String, Object>();
//            parameters.put("CollectionBeanParam", dataSource);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource1);
            parameters.put("CollectionBeanParam2", dataSource2);



//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource1);


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

    @PostMapping("/get-Rasheedi")
    public ResponseEntity<?> getRasheedi(@RequestBody LotStatusSeedMarketRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to Cash Reciept");
            logger.info("enter to Cash Reciept");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Rasheedi.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceCashReciept(requestDto);

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

    @PostMapping("/get-market-reciept")
    public ResponseEntity<?> getMarketReciept(@RequestBody LotStatusSeedMarketRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to Cash Reciept");
            logger.info("enter to Cash Reciept");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Marketfee.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceMarketReciept(requestDto);

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
    }

    @PostMapping("/get-Invoice")
    public ResponseEntity<?> getInvoice(@RequestBody LotStatusSeedMarketRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {
        try {
            System.out.println("enter to get Invoice");
            logger.info("enter to get Invoice");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Invoice2.jrxml");

//            // ✅ Get the data as a list
//            JRDataSource dataSource1 = getDataSourceForInvoice(requestDto);
//            JRDataSource dataSource2 = getDataSourceForInvoice(requestDto);
//
//
////            JRDataSource dataSource1 = getDataSourceForInvoice(requestDto); // For CollectionBeanParam
////            JRDataSource dataSource2 = getDataSourceForInvoice(requestDto); // For CollectionBeanParam2 (same data)
//
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("CollectionBeanParam", dataSource1);
//            parameters.put("CollectionBeanParam2", dataSource2);
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource1);


            JRDataSource dataSource1 = getDataSourceForInvoice(requestDto);
            JRDataSource dataSource2 = getDataSourceForInvoice2(requestDto);
            JRDataSource dataSource3 = getDataSourceForInvoice3(requestDto);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource1);
            parameters.put("CollectionBeanParam2", dataSource2);
            parameters.put("CollectionBeanParam3", dataSource3);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource1);


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
    }



    @PostMapping("/get-TransportSubsidy")
    public ResponseEntity<?> getTransportSubsidyReport(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, JRException, FileNotFoundException {

        JasperReport jasperReport = getJasperReport("transportationCharges.jrxml");
        JRDataSource dataSource   = getDataSourceForTransportSubsidy(requestDto);

        Map<String, Object> params = new HashMap<>();
        params.put("CollectionBeanParam", dataSource);

        JasperPrint jp = JasperFillManager.fillReport(jasperReport, params, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jp));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Transport_Subsidy_Report.pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping("/get-PriceStabilizationIncentive")
    public ResponseEntity<?> getPriceStabilizationIncentiveReport(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, JRException, FileNotFoundException {
        JasperReport jasperReport = getJasperReport("Incentive30.jrxml");
        JRDataSource dataSource   = getDataSourceForPriceStabilizationIncentive(requestDto);

        Map<String, Object> params = new HashMap<>();
        params.put("CollectionBeanParam", dataSource);

        JasperPrint jp = JasperFillManager.fillReport(jasperReport, params, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jp));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Transport_Subsidy_Report.pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping("/get-Bonus")
    public ResponseEntity<?> getBonus(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to get Bonus");
            logger.info("enter to get Bonus");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Bonus225.jrxml");

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForBonus225(requestDto);

            // 2. parameters "empty"
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
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
            //return  ex.getMessage();
            //throw new RuntimeException("fail export file: " + ex.getMessage());
        }


        //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

    }

    @PostMapping("/get-Incentive")
    public ResponseEntity<?> getIncentive(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to get Incentive");
            logger.info("enter to get Incentive");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Incentive120.jrxml");

            JRDataSource dataSource = getDataSourceForIncentive120(requestDto);

            // 2. parameters "empty"
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", dataSource);

            // 3. datasource "java object"

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


    @PostMapping("/getAcknowledementPMKSY")
    public ResponseEntity<?> getAcknowledementPMKSY(@RequestBody ApplicationFormPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("pmksyAcknowledgement.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForAcknowledgementReceiptPMKSY(requestDto);

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

    @PostMapping("/get-MscSeedChawki")
    public ResponseEntity<?> getMscSeedChawki(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            logger.info("enter to get Msc Seed Chawki report");

            JasperReport jasperReport = getJasperReport("mscseedchawki1500.jrxml");

            JRDataSource dataSource = getDataSourceForMscSeedChawki(requestDto);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Msc_Seed_Chawki_Report.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error generating Msc Seed Chawki report", ex);
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8),
                    HttpStatus.OK);
        }
    }

    @PostMapping("/get-MscSeedChawki1000")
    public ResponseEntity<?> getMscSeedChawki1000(@RequestBody CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            logger.info("enter to get Msc Seed Chawki report");

            JasperReport jasperReport = getJasperReport("mscseedchawki1000.jrxml");

            JRDataSource dataSource = getDataSourceForMscSeedChawki1000(requestDto);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Msc_Seed_Chawki_Report.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error generating Msc Seed Chawki report", ex);
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8),
                    HttpStatus.OK);
        }
    }




    @PostMapping("/getSanctionOrderRH")
    public ResponseEntity<?> getSanctionOrderRH(@RequestBody SanctionOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");

            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_OrderRH.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrderRH(requestDto); // ✅ Change: null check inside this method

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            // ✅ Change: Proper logging and error response
            logger.error("Error generating sanction order PDF", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getSanctionOrderRHEquipment")
    public ResponseEntity<?> getSanctionOrderRHEquipmentPdf(
            @RequestBody SanctionOrderPrintRequest requestDto
    ) throws JsonProcessingException, JRException, FileNotFoundException {

        try {
            logger.info("enter to getSanctionOrderRHEquipmentPdf");

            JasperReport jasperReport = getJasperReport("Sanction_OrderRHEquipment.jrxml");

            // 🔹 This should return a JRBeanCollectionDataSource of your response beans
            JRBeanCollectionDataSource fullDs =
                    (JRBeanCollectionDataSource) getDataSourceForSanctionOrderRHEquipment(requestDto);

            @SuppressWarnings("unchecked")
            List<SanctionOrderResponse> fullList =
                    (List<SanctionOrderResponse>) fullDs.getData();

            if (fullList == null || fullList.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("No data found for sanction order".getBytes(StandardCharsets.UTF_8));
            }

            // 🔹 1) HEADER BEAN – only first row
            List<SanctionOrderResponse> headerList = new ArrayList<>();
            headerList.add(fullList.get(0));  // use first row as header

            JRBeanCollectionDataSource mainDataSource =
                    new JRBeanCollectionDataSource(headerList);

            // 🔹 2) TABLE BEANS – all equipment rows (could also be fullList.subList(1, ...) if needed)
            List<SanctionOrderResponse> equipmentList = new ArrayList<>(fullList);

            JRBeanCollectionDataSource equipmentDs =
                    new JRBeanCollectionDataSource(equipmentList);

            // 🔹 Parameters
            Map<String, Object> parameters = getParameters(); // keep your existing parameters
            parameters.put("CollectionBeanParam", equipmentDs);  // 👈 must match JRXML param name

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, mainDataSource);

            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "SanctionOrderRHEquipment.pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
            pdfExporter.exportReport();

            return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error generating RHEquipment sanction order PDF", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(
                    ex.getMessage().getBytes(StandardCharsets.UTF_8),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }




    @PostMapping("/download/sanction-order")
    public ResponseEntity<?> downloadSanctionOrder(@RequestBody SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException {

        try {
            String type = requestDto.getType();   // RH or SILK
            String apiUrl = "";

            if ("RH".equalsIgnoreCase(type)) {
                apiUrl = "http://localhost:8013/dbt/v1/service/getSanctionOrderRH";
            } else if ("SILK".equalsIgnoreCase(type)) {
                apiUrl = "http://localhost:8013/dbt/v1/service/sanction-silk-incentive";
            } else {
                return ResponseEntity.badRequest().body("Invalid type. Must be RH or SILK");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
            headers.setBearerAuth(Util.getTokenData());

            HttpEntity<SanctionOrderPrintRequest> requestEntity = new HttpEntity<>(requestDto, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            HttpHeaders pdfHeaders = new HttpHeaders();
            pdfHeaders.setContentType(MediaType.APPLICATION_PDF);
            pdfHeaders.setContentDispositionFormData("attachment", "SanctionOrder.pdf");

            return new ResponseEntity<>(response.getBody(), pdfHeaders, HttpStatus.OK);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating sanction order: " + ex.getMessage()).getBytes());
        }
    }


    @PostMapping("/getSanctionOrderPmksy")
    public ResponseEntity<?> getSanctionOrder(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_Order_pmksy.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrderPmksy(requestDto);

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

    @PostMapping("/pdmcWorkOrder")
    public ResponseEntity<?> getPdmcWorkOrder(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to Work Order");
            logger.info("enter to Work Order");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("pdmc_work_order.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForPDMCWorkOrder(requestDto);

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

    @PostMapping("/getSanctionOrderPmksyCompany")
    public ResponseEntity<?> getSanctionOrderPmksyCompany(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_Order_pmksy_company.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrderPmksycompany(requestDto);

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



    @PostMapping("/getSanctionOrderPDMC")
    public ResponseEntity<?> getSanctionOrderPDMC(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_Order_pdmc.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrderPDMC(requestDto);

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

    @PostMapping("/getSanctionOrderPDMCCompany")
    public ResponseEntity<?> getSanctionOrderPDMCCompany(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getSanctionOrder");
            logger.info("enter to getSanctionOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("Sanction_Order_pdmc_company.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSanctionOrderPDMCcompany(requestDto);

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

    @PostMapping("/getWorkOrder")
    public ResponseEntity<?> getWorkOrder(@RequestBody WorkOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getWorkOrder");
            logger.info("enter to getWorkOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("workorder.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForWorkOrder(requestDto);

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
    }


    @PostMapping("/getWorkOrderRHEquipment")
    public ResponseEntity<?> getWorkOrderEquipment(@RequestBody SanctionOrderPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getWorkOrder");
            logger.info("enter to getWorkOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("workorderEquipment.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForWorkOrderRHEquipment(requestDto);

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
    }


        @PostMapping("/getWorkOrderHRU")
        public ResponseEntity<?> getWorkOrderForHRU(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

            try {
                System.out.println("enter to getWorkOrder");
                logger.info("enter to getWorkOrder");
                String destFileName = "report_kannada.pdf";
                JasperReport jasperReport = getJasperReport("workOrderHRU.jrxml");

                // 2. parameters "empty"
                Map<String, Object> parameters = getParameters();

                // 3. datasource "java object"
                JRDataSource dataSource = getDataSourceHRUWorkOrder(requestDto);

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


    @PostMapping("/getWorkOrderReelingShed")
    public ResponseEntity<?> getWorkOrderForReelingShed(@RequestBody CheckInspectionStatusRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to getWorkOrder");
            logger.info("enter to getWorkOrder");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("workOrderReelingShed.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceReelingShedWorkOrder(requestDto);

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
    @PostMapping("/getfarmercopy-kannada-seed")
    public ResponseEntity<?> getfarmercopyKannadaForSeedCocoon(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("farmer_copy_with_variable_seed_cocoon_1.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceBidSlipForSeedCocoon(requestDto);

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

    @PostMapping("/getfarmercopy-kannada-silk")
    public ResponseEntity<?> getfarmercopyKannadaForSilk(@RequestBody MarketAuctionForPrintRequest requestDto) throws JsonProcessingException, FileNotFoundException, JRException {

        try {
            System.out.println("enter to gettripletpdf");
            logger.info("enter to gettripletpdf");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("bid_slip_silk_type_.jrxml");

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSourceForSilk(requestDto);

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
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")\n" +
                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk() +" , Mob No- "+ apiResponse.content.getFarmerMobileNumber());
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

    private JRDataSource getDataSourceForSilk(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApiSilk(requestDto);
        List<Content> countries = new LinkedList<>();
        if (apiResponse.content != null) {

            apiResponse.content.setIfscCode(apiResponse.content.getReelerIfscCode());
            apiResponse.content.setAccountNumber(apiResponse.content.getReelerAccountNumber());
            apiResponse.content.setFarmerMobileNumber(apiResponse.content.getReelerMobileNumber());
            apiResponse.content.setTraderLicense("  Trader License No. " +apiResponse.content.getTraderLicenseNumber());
            apiResponse.content.setReelerLicens("  Reeler License No. " +apiResponse.content.getReelerLicense());
            apiResponse.content.setSilkTypes(apiResponse.content.getSilkType());
            apiResponse.content.setSilkQualities(apiResponse.content.getSilkQuality());
//            apiResponse.content.setApproximateWeight(apiResponse.content.getApproximateEstimatedWeight());
            apiResponse.content.setApproximateWeight(apiResponse.content.getApproximateEstimatedWeight());

            apiResponse.content.setBags(apiResponse.content.getNoOfBags());
            apiResponse.content.setForm(apiResponse.content.getFormNo());


            String traderNumberText = "";
            String traderAddressText = "";
            if (apiResponse.content.getTraderLicenseNumber() != null) {
                traderNumberText = "(" + apiResponse.content.getTraderLicenseNumber() + ")";
            }
            if (apiResponse.content.getTraderAddress() != null) {
                traderAddressText = apiResponse.content.getTraderAddress();
            }
            apiResponse.content.setTraderDetails(traderNumberText+"   ಶ್ರೀ /ಶ್ರೀಮತಿ.  " + apiResponse.content.getTraderFirstName() + " " +apiResponse.content.getTraderLastName()+ "  ,  ಬಿನ್/ಕೋಂ " + apiResponse.content.getTraderFatherName()+ traderAddressText);

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
            apiResponse.content.setLoginname_accountnumber_ifsccode("    (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getReelerAccountNumber() + "(" + apiResponse.content.getReelerIfscCode() + ")");
            apiResponse.content.setAccountnumber_ifsccode("Bank - " + apiResponse.content.getReelerAccountNumber() + "(" + apiResponse.content.getReelerIfscCode() + ")");
            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount.toString());
            //  apiResponse.content.setReeleramount(relaramout);
            String inputDateTime = "";
            if (apiResponse.content.getAuctionDateWithTime() != null) {
                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            } else {
                apiResponse.content.setAuctionDate_time("");
            }
            // Parse the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")\n" +
                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());
            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails(reelerNumberText + "  ಶ್ರೀ /ಶ್ರೀಮತಿ. " + apiResponse.content.getReelerName()+ "  ,  ಬಿನ್/ಕೋಂ " +apiResponse.content.getReelerFatherName() + " " + reelerAddressText);

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

//            long farmerMarketFee = Math.round(apiResponse.content.getFarmerMarketFee());
//            long reelerMarketFee = Math.round(apiResponse.content.getReelerMarketFee());
//            long totalMarketFee = Math.round(farmerMarketFee + reelerMarketFee);
//
//            String formatfees = farmerMarketFee + "+" + reelerMarketFee + "=" + totalMarketFee;
//            apiResponse.content.setFeespaid(formatfees);
//
//            long amountPaid = Math.round(reelerMarketFee);  // Apply rounding to amountPaid
//
//            String marketFees = String.valueOf(reelerMarketFee);  // Convert reelerMarketFee to string
//            apiResponse.content.setAmountPaid(marketFees);
//


// ...

            DecimalFormat df = new DecimalFormat("#.00");

            double farmerMarketFee = apiResponse.content.getFarmerMarketFee();
            double reelerMarketFee = apiResponse.content.getReelerMarketFee();
            double totalMarketFee = farmerMarketFee + reelerMarketFee;

            String formatFees = df.format(farmerMarketFee) + "+" + df.format(reelerMarketFee) + "=" + df.format(totalMarketFee);
            apiResponse.content.setFeespaid(formatFees);

            String amountPaid = df.format(reelerMarketFee);  // Format reelerMarketFee to two decimal places
            apiResponse.content.setAmountPaid(amountPaid);



            apiResponse.content.setAuctionDate(apiResponse.content.getAuctionDate());
//            long farmerMarketFeeLong = farmerMarketFee; // Ensure farmerMarketFee is a long
//            long paidAmount = farmerMarketFeeLong;
//            String format = farmerMarketFeeLong + "";
//            apiResponse.content.setPaidAmount(format);
//
//
            long total = Math.round(Double.valueOf(apiResponse.content.getLotSoldOutAmount()));
            long farmerfee = Math.round(apiResponse.content.getFarmerMarketFee());
            long realerfee = Math.round(apiResponse.content.getReelerMarketFee());
            String farmeramout = "" + (total - farmerfee);
            String relaramout = "" + (total - realerfee);

            long slip1Amount = Math.round((total - farmerfee) + farmerfee + realerfee);

            // Assuming farmerMarketFee is a double or can be converted to double
//            double farmerMarketFeeDouble = (double) farmerMarketFee;
//            double paidAmount = farmerMarketFeeDouble;
//            String format = String.valueOf(farmerMarketFeeDouble);
//            apiResponse.content.setPaidAmount(format);

//            double farmerMarketFeeDouble = (double) farmerMarketFee;
//            long paidAmount = Math.round(farmerMarketFeeDouble); // Math.round returns a long
//            String format = String.valueOf(paidAmount); // Convert to string without decimal
//            apiResponse.content.setPaidAmount(format);


// Assuming farmerMarketFee is a double or a float
            double farmerMarketFeeDouble = (double) farmerMarketFee;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String format = decimalFormat.format(farmerMarketFeeDouble); // Format to 2 decimal places
            apiResponse.content.setPaidAmount(format);





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
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
                    apiResponse.content.setDescription1( "  ಲಾಟ್ ಸಂಖ್ಯೆಗಾಗಿ ನಿರ್ಗಮನ ಪಾಸ್ " + apiResponse.content.getAllottedLotId() + " ,  ದಿನಾಂಕ  "  + apiResponse.content.getAuctionDate() + " , ತೂಕ  " + apiResponse.content.getLotWeight() + "  ಕಿಲೋಗ್ರಾಂಗಳು  , ಖರೀದಿದಾರ : " +  apiResponse.content.getReelerLicense() + " ,  " + apiResponse.content.getReelerName() + " ,  " + apiResponse.content.getReelerAddress() );

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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ") \n" +
                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());

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
                apiResponse.content.setLotSoldOutAmount(String.format("%.2f", Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee()));

//                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));

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
    private double parseDoubleOrDefault(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // Return default value in case of parsing error
            return 0.0; // You can change this to any default value
        }
    }
//    private JRDataSource getDataSourceForTripletSeedCocoon(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
//
//        ContentRoot apiResponse = apiService.fetchDataFromApiSeedCocoonTriplet(requestDto);
//        List<Content> countries = new LinkedList<>();
//        if (apiResponse.content != null) {
//
////            long farmerMarketFee = Math.round(apiResponse.content.getFarmerMarketFee());
////            long reelerMarketFee = Math.round(apiResponse.content.getReelerMarketFee());
////            long totalMarketFee = Math.round(farmerMarketFee + reelerMarketFee);
////
////            String formatfees = farmerMarketFee + "+" + reelerMarketFee + "=" + totalMarketFee;
////            apiResponse.content.setFeespaid(formatfees);
////
////            long amountPaid = Math.round(reelerMarketFee);  // Apply rounding to amountPaid
////
////            String marketFees = String.valueOf(reelerMarketFee);  // Convert reelerMarketFee to string
////            apiResponse.content.setAmountPaid(marketFees);
////
//
//
//// ...
////            apiResponse.content.setSadodLot(apiResponse.content.getSadodLotNumber());
////            apiResponse.content.setDescription1( "                  \n" +
////                    "  ಕರ್ನಾಟಕ ಸಿಲ್ಕ್  ವರ್ಮ್ ಸೀಡ್, ಕಕೂನ್  ಅಂಡ್ ಸಿಲ್ಕ್  ಯಾರ್ನ್ \n" +
////                    "  (ರೆಗ್ಯುಲೇಷನ್ ಆಫ್ ಪ್ರೊಡಕ್ಸನ್, ಸಪ್ಲೈ  , ಡಿಸ್ಟ್ರಿಬ್ಯೂಸನ್  ಅಂಡ್ ಸೇಲ್ಸ್ )\n" +
////                    "  ರೂಲ್ಸ್  ೧೯೬೦-ಫಾರಂ ೭ಬಿ , ಬಿಡ್ ಸ್ಲಿಪ್ ನಂ."+ apiResponse.content.getAllottedLotId());t
//
//            if (apiResponse != null && apiResponse.content != null) {
//                // Set the sadodLot with null check
//                if (apiResponse.content.getSadodLotNumber() != null) {
//                    apiResponse.content.setSadodLot(apiResponse.content.getSadodLotNumber());
//                } else {
//                    // Handle the case where sadodLotNumber is null (e.g., set to a default value)
//                    apiResponse.content.setSadodLot("DefaultSadodLot"); // Replace with an appropriate default value
//                }
//
//                // Build the description with null checks
//                String allottedLotId = apiResponse.content.getAllottedLotId() != null ?
//                        apiResponse.content.getAllottedLotId() : "DefaultLotId"; // Replace with an appropriate default value
//
//                apiResponse.content.setDescription1("                  \n" +
//                        "  ಕರ್ನಾಟಕ ಸಿಲ್ಕ್  ವರ್ಮ್ ಸೀಡ್, ಕಕೂನ್  ಅಂಡ್ ಸಿಲ್ಕ್  ಯಾರ್ನ್ \n" +
//                        "  (ರೆಗ್ಯುಲೇಶನ್ ಆಫ್ ಪ್ರೊಡಕ್ಸನ್, ಸಪ್ಲೈ  , ಡಿಸ್ಟ್ರಿಬ್ಯೂಸನ್  ಅಂಡ್ ಸೇಲ್ಸ್ )\n" +
//                        "  ರೂಲ್ಸ್  ೧೯೬೦-ಫಾರಂ ೭ಬಿ , ಬಿಡ್ ಸ್ಲಿಪ್ ನಂ." + allottedLotId);
//            }
//
//            DecimalFormat df = new DecimalFormat("#.00");
//
//            double farmerMarketFee = apiResponse.content.getFarmerMarketFee();
//            double reelerMarketFee = apiResponse.content.getReelerMarketFee();
//            double totalMarketFee = farmerMarketFee + reelerMarketFee;
//
//            String formatFees = df.format(farmerMarketFee) + "+" + df.format(reelerMarketFee) + "=" + df.format(totalMarketFee);
//            apiResponse.content.setFeespaid(formatFees);
//
//            String amountPaid = df.format(reelerMarketFee);  // Format reelerMarketFee to two decimal places
//            apiResponse.content.setAmountPaid(amountPaid);
//
//
//
//            apiResponse.content.setAuctionDate(apiResponse.content.getAuctionDate());
////            long farmerMarketFeeLong = farmerMarketFee; // Ensure farmerMarketFee is a long
////            long paidAmount = farmerMarketFeeLong;
////            String format = farmerMarketFeeLong + "";
////            apiResponse.content.setPaidAmount(format);
////
////
//            long total = Math.round(Double.valueOf(apiResponse.content.getLotSoldOutAmount()));
//            long farmerfee = Math.round(apiResponse.content.getFarmerMarketFee());
//            long realerfee = Math.round(apiResponse.content.getReelerMarketFee());
//            String farmeramout = "" + (total - farmerfee);
//            String relaramout = "" + (total - realerfee);
//
//            long slip1Amount = Math.round((total - farmerfee) + farmerfee + realerfee);
//
//            // Assuming farmerMarketFee is a double or can be converted to double
////            double farmerMarketFeeDouble = (double) farmerMarketFee;
////            double paidAmount = farmerMarketFeeDouble;
////            String format = String.valueOf(farmerMarketFeeDouble);
////            apiResponse.content.setPaidAmount(format);
//
////            double farmerMarketFeeDouble = (double) farmerMarketFee;
////            long paidAmount = Math.round(farmerMarketFeeDouble); // Math.round returns a long
////            String format = String.valueOf(paidAmount); // Convert to string without decimal
////            apiResponse.content.setPaidAmount(format);
//
//
//// Assuming farmerMarketFee is a double or a float
//            double farmerMarketFeeDouble = (double) farmerMarketFee;
//            DecimalFormat decimalFormat = new DecimalFormat("#.00");
//            String format = decimalFormat.format(farmerMarketFeeDouble); // Format to 2 decimal places
//            apiResponse.content.setPaidAmount(format);
//
//
//
//
//
////            slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
//            apiResponse.content.setAmountfarmer(farmeramout);
//            apiResponse.content.setAmountrealar(relaramout);
//            apiResponse.content.setLoginname_accountnumber_ifsccode(" (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getAccountNumber() + "                       IFSC  Code  :  "  + apiResponse.content.getIfscCode());
//            apiResponse.content.setAccountnumber_ifsccode("  Farmer Bank A/c No. - " + apiResponse.content.getAccountNumber() );
//            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + Math.round(apiResponse.content.getFarmerMarketFee()) + "+" + Math.round(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
////            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
//            apiResponse.content.setIfsc("  IFSC Code : " + apiResponse.content.getIfscCode());
//
//
//            String inputDateTime = "";
//            if (apiResponse.content.getAuctionDateWithTime() != null) {
//                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
//            } else {
//                apiResponse.content.setAuctionDate_time("");
//            }
//            // Parse the input date and time
//            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//            Date parsedDate;
//            try {
//                if (inputDateTime != null && !inputDateTime.equals("")) {
//                    parsedDate = inputFormat.parse(inputDateTime);
//                    // Format the output date and time
//                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
//                    SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd-MM-yyyy");
//                    String formattedDateTime = outputFormat.format(parsedDate);
//                    String formattedDateTime1 = outputFormat1.format(parsedDate);
//                    apiResponse.content.setAuctionDate_time(formattedDateTime);
//                    apiResponse.content.setAuctionDate(formattedDateTime1);
//
//
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException("Error parsing input date and time", e);
//            }
//
//            String smallBins = "";
//            String bigBins = "";
//
//            if (apiResponse.content.getReelerNameKannada() == null) {
//                apiResponse.content.setReelerNameKannada("");
//            }
//            if (apiResponse.content.getFarmerNameKannada() == null) {
//                apiResponse.content.setFarmerNameKannada("");
//            }
//            if (apiResponse.content.getFarmerAddress() == null) {
//                apiResponse.content.setFarmerAddress("");
//            }
//            if (apiResponse.content.getFatherNameKan() == null) {
//                apiResponse.content.setFatherNameKan("");
//            }
//            if (apiResponse.content.getReelerLicense() == null) {
//                apiResponse.content.setReelerLicense("");
//            }
//            if (apiResponse.content.getLotWeight() == null) {
//                apiResponse.content.setLotWeight("");
//            }
//
//            apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
//            String farmerNumber = "";
//            if (apiResponse.content.getFruitsId() != null && !apiResponse.content.getFruitsId().equals("")) {
//                farmerNumber = apiResponse.content.getFruitsId();
//            } else {
//                farmerNumber = apiResponse.content.getFarmerNumber();
//            }
//            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ") \n" +
//                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());
//
//            String reelerNumberText = "";
//            String externalUnitLicenseNumberText ="";
//            String externalUnitLicenseAddresssText ="";
//            String reelerAddressText = "";
//            if (apiResponse.content.getExternalUnitLicenseNumber() != null) {
//                externalUnitLicenseNumberText = "(" + apiResponse.content.getExternalUnitLicenseNumber() + ")";
//            }
//            if (apiResponse.content.getReelerNumber() != null) {
//                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
//            }
//            if (apiResponse.content.getReelerAddress() != null) {
//                reelerAddressText = apiResponse.content.getReelerAddress();
//            }
//            if (apiResponse.content.getExternalUnitAddress() != null) {
//                externalUnitLicenseAddresssText = apiResponse.content.getExternalUnitAddress();
//            }
////            apiResponse.content.setReelerDetails(reelerNumberText + " ,  ಶ್ರೀ /ಶ್ರೀಮತಿ.  " +apiResponse.content.getReelerName()+" ,  ಬಿನ್/ಕೋಂ  "  +apiResponse.content.getReelerNameKannada()+ " ,  " + reelerAddressText);
//            apiResponse.content.setReelerDetails(externalUnitLicenseNumberText + " ,  " +apiResponse.content.getExternalUnitName()+" ,   "  +externalUnitLicenseAddresssText);
//
//            if (apiResponse.content.getSmallBinList() != null) {
//                List<String> smallBinList = apiResponse.content.getSmallBinList().stream()
//                        .map(Object::toString)
//                        .collect(Collectors.toList());
//                smallBins = String.join(",", smallBinList);
//            }
//            apiResponse.content.setAcknowledgmentString("ಈ ಮೇಲೆ ನಮೂದಿಸಿದ ವಿಷಯಗಳು ಸರಿಯಾಗಿವೆಯೆಂದು ದೃಢೀಕರಿಸುತ್ತೇನೆ ಹಾಗು ಲೈಸೆನ್ಸ್ ಪಡೆದವರಿಗೆ /ಪ್ರತಿನಿಧಿಗೆ ಕೆ.ಜಿ. ಗೂಡುಗಳನ್ನು " + apiResponse.content.getAuctionDate() + " ದಿನ _______ ಘಂಟೆಯೊಳಗಾಗಿ    ಸಾಗಿಸಲು ಅನುಮತಿ ನೀಡಿದ್ದೇನೆ.");
//
//            if (apiResponse.content.getBigBinList() != null) {
//                List<String> bigBinList = apiResponse.content.getBigBinList().stream()
//                        .map(Object::toString)
//                        .collect(Collectors.toList());
//                bigBins = String.join(",", bigBinList);
//            }
////            apiResponse.content.setBinno("Big: " + bigBins + " Small: " + smallBins);
//            apiResponse.content.setBinno("  ಜಾಲರಿ ಸಂಖ್ಯೆ: " + bigBins );
//
//
//            for (int i = 0; i < 15; i++) {
//                switch (i) {
//                    case 0:
//                        apiResponse.content.setLotDetail0("");
//                        break;
//                    case 1:
//                        apiResponse.content.setLotDetail1("");
//                        break;
//                    case 2:
//                        apiResponse.content.setLotDetail2("");
//                        break;
//                    case 3:
//                        apiResponse.content.setLotDetail3("");
//                        break;
//                    case 4:
//                        apiResponse.content.setLotDetail4("");
//                        break;
//                    case 5:
//                        apiResponse.content.setLotDetail5("");
//                        break;
//                    case 6:
//                        apiResponse.content.setLotDetail6("");
//                        break;
//                    case 7:
//                        apiResponse.content.setLotDetail7("");
//                        break;
//                    case 8:
//                        apiResponse.content.setLotDetail8("");
//                        break;
//                    case 9:
//                        apiResponse.content.setLotDetail9("");
//                        break;
//                    case 10:
//                        apiResponse.content.setLotDetail10("");
//                        break;
//                    case 11:
//                        apiResponse.content.setLotDetail11("");
//                        break;
//                    case 12:
//                        apiResponse.content.setLotDetail12("");
//                        break;
//                    case 13:
//                        apiResponse.content.setLotDetail13("");
//                        break;
//                    case 14:
//                        apiResponse.content.setLotDetail14("");
//                        break;
//                    default:
//                        System.out.println("Default case");
//                }
//            }
//            if (apiResponse.content.getLotWeightDetail() != null) {
//                int lotWeightSize = apiResponse.content.getLotWeightDetail().size();
//                for (int i = 0; i < lotWeightSize && i < 15; i++) {
//                    try {
//                        // Dynamically create the method name
//                        Method method = apiResponse.content.getClass().getMethod("setLotDetail" + i, String.class);
//                        // Format the value
//                        String formattedValue = String.format("%.3f", Double.parseDouble(apiResponse.content.getLotWeightDetail().get(i).toString()));
//                        // Invoke the method
//                        method.invoke(apiResponse.content, formattedValue);
//                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                        e.printStackTrace();
////            if (apiResponse.content.getLotWeightDetail() != null) {
////                if (apiResponse.content.getLotWeightDetail().size() > 0) {
////                    for (int i = 0; i < apiResponse.content.getLotWeightDetail().size(); i++) {
//                        switch (i) {
//                            case 0:
//                                apiResponse.content.setLotDetail0(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 1:
//                                apiResponse.content.setLotDetail1(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 2:
//                                apiResponse.content.setLotDetail2(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 3:
//                                apiResponse.content.setLotDetail3(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 4:
//                                apiResponse.content.setLotDetail4(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 5:
//                                apiResponse.content.setLotDetail5(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 6:
//                                apiResponse.content.setLotDetail6(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 7:
//                                apiResponse.content.setLotDetail7(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 8:
//                                apiResponse.content.setLotDetail8(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 9:
//                                apiResponse.content.setLotDetail9(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 10:
//                                apiResponse.content.setLotDetail10(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 11:
//                                apiResponse.content.setLotDetail11(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 12:
//                                apiResponse.content.setLotDetail12(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 13:
//                                apiResponse.content.setLotDetail13(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            case 14:
//                                apiResponse.content.setLotDetail14(apiResponse.content.getLotWeightDetail().get(i).toString());
//                                break;
//                            default:
//                                System.out.println("Default case");
//                        }
//                    }
//                }
//
//
////                apiResponse.content.setTotalcrates(String.valueOf(lotWeightDetails.size()));
//                apiResponse.content.setTotalcrates(String.valueOf(apiResponse.content.getLotWeightDetail().size()));
//                apiResponse.content.setTotalamount(String.valueOf(roundToWholeNumber(Double.parseDouble( apiResponse.content.getLotSoldOutAmount() ))));
////                                apiResponse.content.setTotalamount(String.valueOf(Math.round(Double.parseDouble("(" + apiResponse.content.getLotSoldOutAmount() + ")"))));
//
////                                String lotSoldOutAmountStr = apiResponse.content.getLotSoldOutAmount();
////                double lotSoldOutAmount = Double.parseDouble(lotSoldOutAmountStr);
////
//
//            }
//            apiResponse.content.setLogurl("/reports/Seal_of_Karnataka.PNG");
//            if (apiResponse.content.getBidAmount().equals("0.0")) {
//                apiResponse.content.setBidAmount("");
//            } else {
//                apiResponse.content.setBidAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getBidAmount()))));
//            }
////            if (apiResponse.content.getLotWeight().equals("0.0")) {
////                apiResponse.content.setLotWeight("");
////            } else {
////                double doubleValue = Double.parseDouble(apiResponse.content.getLotWeight());
////                String formattedValue = String.format("%.3f", doubleValue);
////                apiResponse.content.setLotWeight(formattedValue);
////            }
//            if (apiResponse.content.getLotSoldOutAmount().equals("0.0")) {
//                apiResponse.content.setLotSoldOutAmount("");
//            } else {
//                apiResponse.content.setLotSoldOutAmount(String.format("%.2f", Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee()));
//
////                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));
//
////                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee() - apiResponse.content.getReelerMarketFee())));
//            }
////            if (apiResponse.content.getFeespaid().equals("0.0+0.0=0.0")) {
////                apiResponse.content.setFeespaid("");
////            } else {
//            System.out.println("Enter the first value:");
//            String[] components = apiResponse.content.getFeespaid().split("[+=]");
//
//            // Extract the symbols
//            String additionSymbol = components[1]; // The addition symbol
//            String equalitySymbol = components[2];
//            int value1 = roundToWholeNumber(Double.parseDouble(additionSymbol));
//
//            System.out.println("Enter the second value:");
//            int value2 = roundToWholeNumber(Double.parseDouble(equalitySymbol));
//
//            // Perform the addition
//            double result = value1 + value2;
//
//            // Round the result to the nearest integer
//            int roundedResult = (int) Math.round(result);
//
//            // Print the rounded result
//            System.out.println("Rounded result: " + roundedResult);
////                apiResponse.content.setFeespaid(value1 + "+" + value2 + "=" + String.valueOf(roundedResult));
//            //}
//            if (!apiResponse.content.getBidAmount().equals("")) {
//                apiResponse.content.setReeleramount("Balance: " + roundToWholeNumber(Double.parseDouble(apiResponse.content.getReelerbalance())));
//            } else {
//                apiResponse.content.setReeleramount("");
//            }
//            String markFee = "0";
//            String totalFee = "0";
//            if (apiResponse.content.getMarketFee() != null && !apiResponse.content.getMarketFee().equals("")) {
//                markFee = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getMarketFee())));
//            }
//            else {
//                markFee = "0"; // or any default value you prefer
//            }
////            if (apiResponse.content.getTotalamount() != null && !apiResponse.content.getTotalamount().equals("")) {
////                totalFee = String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount())));
////            }
////            else {
////                totalFee = "0"; // or any default value you prefer
////            }
//////            String tot_amt = String.valueOf(roundToWholeNumber(Double.parseDouble(totalFee)) + roundToWholeNumber(Double.parseDouble(markFee)));
//////            apiResponse.content.setReelerbalance("Lot value: " + roundToWholeNumber(Double.parseDouble(totalFee)) + "+" + roundToWholeNumber(Double.parseDouble(markFee)) + "=" + tot_amt);
////
////            String tot_amt = String.valueOf(roundToWholeNumber(Double.parseDouble(totalFee))  + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()));
////            apiResponse.content.setReelerbalance("Lot value: " + roundToWholeNumber(Double.parseDouble(totalFee)) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + tot_amt  );
////
//            if (apiResponse.content.getTotalamount() != null && !apiResponse.content.getTotalamount().equals("")) {
//                double totalAmount = Double.parseDouble(apiResponse.content.getTotalamount());
//                totalFee = String.valueOf(Math.round(totalAmount)); // Convert to long
//            } else {
//                totalFee = "0"; // Default value
//            }
//            long marketFee = Math.round(apiResponse.content.getReelerMarketFee());
//
//            String tot_amt = String.valueOf(Math.round(Double.parseDouble(totalFee)) + marketFee);
//            apiResponse.content.setReelerbalance("Lot value: " + Math.round(Double.parseDouble(totalFee)) + "+" + marketFee + "=" + tot_amt);
//            countries.add(apiResponse.content);
//        }
//        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
//        return new JRBeanCollectionDataSource(countries);
//    }


    private JRDataSource getDataSourceForTripletSeedCocoon(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {
        ContentRoot apiResponse = apiService.fetchDataFromApiSeedCocoonTriplet(requestDto);
        List<Content> countries = new LinkedList<>();

        if (apiResponse == null || apiResponse.content == null) {
            // No content -> return empty datasource so Jasper doesn't NPE
            return new JRBeanCollectionDataSource(Collections.emptyList());
        }

        // --- existing code (kept mostly as you had it) ---
        // set sadodLot safely
        if (apiResponse.content.getSadodLotNumber() != null) {
            apiResponse.content.setSadodLot(apiResponse.content.getSadodLotNumber());
        } else {
            apiResponse.content.setSadodLot("DefaultSadodLot");
        }

        String allottedLotId = apiResponse.content.getAllottedLotId() != null ? apiResponse.content.getAllottedLotId() : "DefaultLotId";
        apiResponse.content.setDescription1("                  \n" +
                "  ಕರ್ನಾಟಕ ಸಿಲ್ಕ್  ವರ್ಮ್ ಸೀಡ್, ಕಕೂನ್  ಅಂಡ್ ಸಿಲ್ಕ್  ಯಾರ್ನ್ \n" +
                "  (ರೆಗ್ಯುಲೇಶನ್ ಆಫ್ ಪ್ರೊಡಕ್ಸನ್, ಸಪ್ಲೈ  , ಡಿಸ್ಟ್ರಿಬ್ಯೂಸನ್  ಅಂಡ್ ಸೇಲ್ಸ್ )\n" +
                "  ರೂಲ್ಸ್  ೧೯೬೦-ಫಾರಂ ೭ಬಿ , ಬಿಡ್ ಸ್ಲಿಪ್ ನಂ." + allottedLotId);

        DecimalFormat df = new DecimalFormat("#.00");

        double farmerMarketFee = apiResponse.content.getFarmerMarketFee();
        double reelerMarketFee = apiResponse.content.getReelerMarketFee();
        double totalMarketFee = farmerMarketFee + reelerMarketFee;

        String formatFees = df.format(farmerMarketFee) + "+" + df.format(reelerMarketFee) + "=" + df.format(totalMarketFee);
        apiResponse.content.setFeespaid(formatFees);
        apiResponse.content.setAmountPaid(df.format(reelerMarketFee));
        apiResponse.content.setAuctionDate(apiResponse.content.getAuctionDate());

        long total = 0;
        try {
            total = Math.round(Double.valueOf(Optional.ofNullable(apiResponse.content.getLotSoldOutAmount()).orElse("0")));
        } catch (Exception ignored) {}
        long farmerfee = Math.round(apiResponse.content.getFarmerMarketFee());
        long realerfee = Math.round(apiResponse.content.getReelerMarketFee());
        String farmeramout = "" + (total - farmerfee);
        String relaramout = "" + (total - realerfee);
        long slip1Amount = Math.round((total - farmerfee) + farmerfee + realerfee);

        apiResponse.content.setAmountfarmer(farmeramout);
        apiResponse.content.setAmountrealar(relaramout);
        apiResponse.content.setLoginname_accountnumber_ifsccode(" (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getAccountNumber() + "                       IFSC  Code  :  " + apiResponse.content.getIfscCode());
        apiResponse.content.setAccountnumber_ifsccode("  Farmer Bank A/c No. - " + apiResponse.content.getAccountNumber());
        apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + Math.round(apiResponse.content.getFarmerMarketFee()) + "+" + Math.round(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
        apiResponse.content.setIfsc("  IFSC Code : " + apiResponse.content.getIfscCode());

        // parse auctionDateWithTime defensively
        if (apiResponse.content.getAuctionDateWithTime() != null) {
            String inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date parsedDate = inputFormat.parse(inputDateTime);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                apiResponse.content.setAuctionDate_time(outputFormat.format(parsedDate));
                apiResponse.content.setAuctionDate(outputFormat1.format(parsedDate));
            } catch (ParseException e) {
                // don't fail report generation because of date parsing — set blank and log if needed
                apiResponse.content.setAuctionDate_time("");
            }
        } else {
            apiResponse.content.setAuctionDate_time("");
        }

        // null-safe strings
        if (apiResponse.content.getReelerNameKannada() == null) apiResponse.content.setReelerNameKannada("");
        if (apiResponse.content.getFarmerNameKannada() == null) apiResponse.content.setFarmerNameKannada("");
        if (apiResponse.content.getFarmerAddress() == null) apiResponse.content.setFarmerAddress("");
        if (apiResponse.content.getFatherNameKan() == null) apiResponse.content.setFatherNameKan("");
        if (apiResponse.content.getReelerLicense() == null) apiResponse.content.setReelerLicense("");

        // ------------------- IMPORTANT SANITIZATION FOR lotWeight -------------------
        // This is the actual fix for the Jasper error you saw.
        // If lotWeight contains an array (e.g. StackTraceElement[]) or other unexpected object,
        // set a safe JR-friendly value (empty string) instead of leaving the array object.
        try {
            Object rawLotWeight = apiResponse.content.getLotWeight(); // raw can be String, Number, Collection, array, etc.

            if (rawLotWeight == null) {
                // keep it as empty string (your DTO previously used empty string)
                apiResponse.content.setLotWeight("");
            } else {
                Class<?> cls = rawLotWeight.getClass();
                if (cls.isArray()) {
                    // Example from your logs: [Ljava.lang.StackTraceElement;@...
                    // Arrays are not suitable for Jasper field expecting String/Number -> set blank
                    apiResponse.content.setLotWeight("");
                } else if (rawLotWeight instanceof Number) {
                    // format numeric with 3 decimals (or choose your preferred formatting)
                    double dv = ((Number) rawLotWeight).doubleValue();
                    apiResponse.content.setLotWeight(String.format(Locale.ENGLISH, "%.3f", dv));
                } else if (rawLotWeight instanceof Collection) {
                    // if a collection arrived accidentally, join items with comma
                    Collection<?> col = (Collection<?>) rawLotWeight;
                    String joined = col.stream().map(Object::toString).collect(Collectors.joining(","));
                    apiResponse.content.setLotWeight(joined);
                } else {
                    // treat as string but sanitize the common stacktrace.toString scenario
                    String s = rawLotWeight.toString();
                    // if string looks like an array-like "[L...;" form, sanitize to empty
                    if (s.startsWith("[L") && s.contains(";@")) {
                        apiResponse.content.setLotWeight("");
                    } else {
                        // try numeric parse and format, otherwise keep string
                        try {
                            double dv = Double.parseDouble(s);
                            apiResponse.content.setLotWeight(String.format(Locale.ENGLISH, "%.3f", dv));
                        } catch (NumberFormatException nfe) {
                            apiResponse.content.setLotWeight(s);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // very defensive fallback: set empty string so Jasper doesn't fail
            apiResponse.content.setLotWeight("");
        }
        // ---------------------------------------------------------------------------

        // continue with rest of formatting you had
        if (apiResponse.content.getLotWeight() == null) {
            apiResponse.content.setLotWeight("");
        }

        apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
        String farmerNumber = (apiResponse.content.getFruitsId() != null && !apiResponse.content.getFruitsId().equals("")) ? apiResponse.content.getFruitsId() : apiResponse.content.getFarmerNumber();
        apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ") \n" +
                "  ಶ್ರೀ /ಶ್ರೀಮತಿ. " + apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan() + " ,  " + apiResponse.content.getFarmerVillage() + " , " + apiResponse.content.getFarmerTaluk());

        String externalUnitLicenseNumberText = "";
        String externalUnitLicenseAddresssText = "";
        if (apiResponse.content.getExternalUnitLicenseNumber() != null) externalUnitLicenseNumberText = "(" + apiResponse.content.getExternalUnitLicenseNumber() + ")";
        if (apiResponse.content.getExternalUnitAddress() != null) externalUnitLicenseAddresssText = apiResponse.content.getExternalUnitAddress();
        apiResponse.content.setReelerDetails(externalUnitLicenseNumberText + " ,  " + apiResponse.content.getExternalUnitName() + " ,   " + externalUnitLicenseAddresssText);

        String smallBins = "";
        String bigBins = "";
        if (apiResponse.content.getSmallBinList() != null) {
            List<String> smallBinList = apiResponse.content.getSmallBinList().stream().map(Object::toString).collect(Collectors.toList());
            smallBins = String.join(",", smallBinList);
        }
        if (apiResponse.content.getBigBinList() != null) {
            List<String> bigBinList = apiResponse.content.getBigBinList().stream().map(Object::toString).collect(Collectors.toList());
            bigBins = String.join(",", bigBinList);
        }
        apiResponse.content.setAcknowledgmentString("ಈ ಮೇಲೆ ನಮೂದಿಸಿದ ವಿಷಯಗಳು ಸರಿಯಾಗಿವೆಯೆಂದು ದೃಢೀಕರಿಸುತ್ತೇನೆ ಹಾಗು ಲೈಸೆನ್ಸ್ ಪಡೆದವರಿಗೆ /ಪ್ರತಿನಿಧಿಗೆ ಕೆ.ಜಿ. ಗೂಡುಗಳನ್ನು " + apiResponse.content.getAuctionDate() + " ದಿನ _______ ಘಂಟೆಯೊಳಗಾಗಿ    ಸಾಗಿಸಲು ಅನುಮತಿ ನೀಡಿದ್ದೇನೆ.");
        apiResponse.content.setBinno("  ಜಾಲರಿ ಸಂಖ್ಯೆ: " + bigBins);

        // initialize lotDetail0..lotDetail14
        for (int i = 0; i < 15; i++) {
            try {
                Method method = apiResponse.content.getClass().getMethod("setLotDetail" + i, String.class);
                method.invoke(apiResponse.content, "");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                // ignore — fallback handled later
                switch (i) {
                    case 0: apiResponse.content.setLotDetail0(""); break;
                    case 1: apiResponse.content.setLotDetail1(""); break;
                    case 2: apiResponse.content.setLotDetail2(""); break;
                    case 3: apiResponse.content.setLotDetail3(""); break;
                    case 4: apiResponse.content.setLotDetail4(""); break;
                    case 5: apiResponse.content.setLotDetail5(""); break;
                    case 6: apiResponse.content.setLotDetail6(""); break;
                    case 7: apiResponse.content.setLotDetail7(""); break;
                    case 8: apiResponse.content.setLotDetail8(""); break;
                    case 9: apiResponse.content.setLotDetail9(""); break;
                    case 10: apiResponse.content.setLotDetail10(""); break;
                    case 11: apiResponse.content.setLotDetail11(""); break;
                    case 12: apiResponse.content.setLotDetail12(""); break;
                    case 13: apiResponse.content.setLotDetail13(""); break;
                    case 14: apiResponse.content.setLotDetail14(""); break;
                }
            }
        }

        if (apiResponse.content.getLotWeightDetail() != null) {
            int lotWeightSize = apiResponse.content.getLotWeightDetail().size();
            for (int i = 0; i < lotWeightSize && i < 15; i++) {
                try {
                    Method method = apiResponse.content.getClass().getMethod("setLotDetail" + i, String.class);
                    String formattedValue = String.format("%.3f", Double.parseDouble(apiResponse.content.getLotWeightDetail().get(i).toString()));
                    method.invoke(apiResponse.content, formattedValue);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // fallback switch (keeps your previous behavior)
                    switch (i) {
                        case 0: apiResponse.content.setLotDetail0(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 1: apiResponse.content.setLotDetail1(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 2: apiResponse.content.setLotDetail2(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 3: apiResponse.content.setLotDetail3(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 4: apiResponse.content.setLotDetail4(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 5: apiResponse.content.setLotDetail5(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 6: apiResponse.content.setLotDetail6(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 7: apiResponse.content.setLotDetail7(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 8: apiResponse.content.setLotDetail8(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 9: apiResponse.content.setLotDetail9(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 10: apiResponse.content.setLotDetail10(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 11: apiResponse.content.setLotDetail11(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 12: apiResponse.content.setLotDetail12(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 13: apiResponse.content.setLotDetail13(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                        case 14: apiResponse.content.setLotDetail14(apiResponse.content.getLotWeightDetail().get(i).toString()); break;
                    }
                }
            }

            apiResponse.content.setTotalcrates(String.valueOf(apiResponse.content.getLotWeightDetail().size()));
            try {
                apiResponse.content.setTotalamount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getLotSoldOutAmount()))));
            } catch (Exception ex) {
                apiResponse.content.setTotalamount("0");
            }
        }

        apiResponse.content.setLogurl("/reports/Seal_of_Karnataka.PNG");

        if ("0.0".equals(apiResponse.content.getBidAmount())) {
            apiResponse.content.setBidAmount("");
        } else {
            try {
                apiResponse.content.setBidAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getBidAmount()))));
            } catch (Exception ex) {
                // keep as-is
            }
        }

        if ("0.0".equals(apiResponse.content.getLotSoldOutAmount())) {
            apiResponse.content.setLotSoldOutAmount("");
        } else {
            try {
                apiResponse.content.setLotSoldOutAmount(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee()));
            } catch (Exception ex) {
                // ignore and keep value
            }
        }

        // Safe parsing of feespaid, avoid fragile index assumptions
        try {
            String fees = Optional.ofNullable(apiResponse.content.getFeespaid()).orElse("");
            String[] comps = fees.split("\\+|=");
            if (comps.length >= 2) {
                int v1 = roundToWholeNumber(Double.parseDouble(comps[0]));
                int v2 = comps.length >= 2 ? roundToWholeNumber(Double.parseDouble(comps[1])) : 0;
                apiResponse.content.setFeespaid(v1 + "+" + v2 + "=" + (v1 + v2));
            }
        } catch (Exception ignored) {}

        if (apiResponse.content.getBidAmount() != null && !apiResponse.content.getBidAmount().equals("")) {
            try {
                apiResponse.content.setReeleramount("Balance: " + roundToWholeNumber(Double.parseDouble(apiResponse.content.getReelerbalance())));
            } catch (Exception ex) {
                apiResponse.content.setReeleramount("");
            }
        } else {
            apiResponse.content.setReeleramount("");
        }

        // reeler balance / lot value formation (safe)
        try {
            String totalFee = Optional.ofNullable(apiResponse.content.getTotalamount()).orElse("0");
            long marketFee = Math.round(apiResponse.content.getReelerMarketFee());
            String tot_amt = String.valueOf(Math.round(Double.parseDouble(totalFee)) + marketFee);
            apiResponse.content.setReelerbalance("Lot value: " + Math.round(Double.parseDouble(totalFee)) + "+" + marketFee + "=" + tot_amt);
        } catch (Exception ex) {
            apiResponse.content.setReelerbalance("Lot value: 0+0=0");
        }

        // add to JR list
        countries.add(apiResponse.content);

        return new JRBeanCollectionDataSource(countries);
    }


    private JRDataSource getDataSourceForTripletSilk(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApiSilk(requestDto);
        List<Content> countries = new LinkedList<>();
        if (apiResponse.content != null) {

//            long farmerMarketFee = Math.round(apiResponse.content.getFarmerMarketFee());
//            long reelerMarketFee = Math.round(apiResponse.content.getReelerMarketFee());
//            long totalMarketFee = Math.round(farmerMarketFee + reelerMarketFee);
//
//            String formatfees = farmerMarketFee + "+" + reelerMarketFee + "=" + totalMarketFee;
//            apiResponse.content.setFeespaid(formatfees);
//
//            long amountPaid = Math.round(reelerMarketFee);  // Apply rounding to amountPaid
//
//            String marketFees = String.valueOf(reelerMarketFee);  // Convert reelerMarketFee to string
//            apiResponse.content.setAmountPaid(marketFees);
//


// ...
            String traderNumberText = "";
            String traderAddressText = "";
            if (apiResponse.content.getTraderLicenseNumber() != null) {
                traderNumberText = "(" + apiResponse.content.getTraderLicenseNumber() + ")";
            }
            if (apiResponse.content.getTraderAddress() != null) {
                traderAddressText = apiResponse.content.getTraderAddress();
            }
            apiResponse.content.setTraderDetails("   ಶ್ರೀ /ಶ್ರೀಮತಿ.  " + apiResponse.content.getTraderFirstName() + " " +apiResponse.content.getTraderLastName()+ "  ,  ಬಿನ್/ಕೋಂ " + apiResponse.content.getTraderFatherName()+ traderAddressText);
            apiResponse.content.setTraderLicense("  Trader License No. " +apiResponse.content.getTraderLicenseNumber());
            apiResponse.content.setReelerLicens("  Reeler License No. " +apiResponse.content.getReelerLicense());
            apiResponse.content.setSilkTypes(apiResponse.content.getSilkType());
            apiResponse.content.setSilkQualities(apiResponse.content.getSilkQuality());
//            apiResponse.content.setApproximateWeight(apiResponse.content.getApproximateEstimatedWeight());
            apiResponse.content.setApproximateWeight(apiResponse.content.getApproximateEstimatedWeight());

            apiResponse.content.setBags(apiResponse.content.getNoOfBags());
            apiResponse.content.setForm(apiResponse.content.getFormNo());
//            apiResponse.content.setAmount(apiResponse.content.getTotalamount());

            DecimalFormat df = new DecimalFormat("#.00");

            double reelerMarketFee = apiResponse.content.getReelerMarketFee();
            double traderMarketFee = apiResponse.content.getTraderMarketFee();
            double totalMarketFee = reelerMarketFee + traderMarketFee;

            String formatFees = df.format(reelerMarketFee) + "+" + df.format(traderMarketFee) + "=" + df.format(totalMarketFee);
            apiResponse.content.setFeespaid(formatFees);

            String amountPaid = df.format(traderMarketFee);  // Format reelerMarketFee to two decimal places
            apiResponse.content.setAmountPaid(amountPaid);



            apiResponse.content.setAuctionDate(apiResponse.content.getAuctionDate());

//            long total = Math.round(Double.valueOf(apiResponse.content.getLotSoldOutAmount()));
//            long reelerfee = Math.round(apiResponse.content.getReelerMarketFee());
//            long traderfee = Math.round(apiResponse.content.getTraderMarketFee());
//            String farmeramout = "" + (total - reelerfee);
//            String relaramout = "" + (total - traderfee);
//
//            long slip1Amount = Math.round((total - reelerfee) + reelerfee + traderfee);
//
//
//// Assuming farmerMarketFee is a double or a float
//            double farmerMarketFeeDouble = (double) reelerfee;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
//            String format = decimalFormat.format(farmerMarketFeeDouble); // Format to 2 decimal places
//            apiResponse.content.setPaidAmount(format);


            long total = Math.round(Double.valueOf(apiResponse.content.getLotSoldOutAmount()));
            long traderfee = Math.round(apiResponse.content.getTraderMarketFee());
            String farmeramout = "" + total; // No reelerfee considered
            String relaramout = "" + (total - traderfee);
            long slip1Amount = Math.round(total + traderfee);
            String format = decimalFormat.format((double) traderfee);
            apiResponse.content.setPaidAmount(format);


//            slip1Amount = roundToTwoDecimalPlaces((total - farmerfee) + farmerfee + realerfee);
            apiResponse.content.setAmountfarmer(farmeramout);
            apiResponse.content.setAmountrealar(relaramout);
            apiResponse.content.setLoginname_accountnumber_ifsccode(" (" + apiResponse.content.getLoginName() + ")" + "//Bank - " + apiResponse.content.getReelerAccountNumber() + "                       IFSC  Code  :  "  + apiResponse.content.getReelerIfscCode());
            apiResponse.content.setAccountnumber_ifsccode("  Reeler Bank A/c No. - " + apiResponse.content.getReelerAccountNumber() );
            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + Math.round(apiResponse.content.getTraderMarketFee()) + "+" + Math.round(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
//            apiResponse.content.setFarmeramount_farmermf_reelermf(farmeramout + "+" + roundToTwoDecimalPlaces(apiResponse.content.getFarmerMarketFee()) + "+" + roundToTwoDecimalPlaces(apiResponse.content.getReelerMarketFee()) + "=" + slip1Amount);
            apiResponse.content.setIfsc("  IFSC Code : " + apiResponse.content.getReelerIfscCode());


            String inputDateTime = "";
            if (apiResponse.content.getAuctionDateWithTime() != null) {
                inputDateTime = apiResponse.content.getAuctionDateWithTime().toString();
            } else {
                apiResponse.content.setAuctionDate_time("");
            }
            // Parse the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
//                    apiResponse.content.setDescription1( "  ಲಾಟ್ ಸಂಖ್ಯೆಗಾಗಿ ನಿರ್ಗಮನ ಪಾಸ್ " + apiResponse.content.getAllottedLotId() + " ,  ದಿನಾಂಕ  "  + apiResponse.content.getAuctionDate() + " , ತೂಕ  " + apiResponse.content.getLotWeight() + "  ಕಿಲೋಗ್ರಾಂಗಳು  , ಖರೀದಿದಾರ : " +  apiResponse.content.getReelerLicense() + " ,  " + apiResponse.content.getReelerName() + " ,  " + apiResponse.content.getReelerAddress() );
                    apiResponse.content.setDescription1( "  I Here by accept the rate i.e. Rs. "  + apiResponse.content.getBidAmount() +"  per Kg. Offered by the highest bidder. \n" +
                            "   ಹರಾಜಿನಲ್ಲಿ ಅತಿ ಹೆಚ್ಚು  ಸವಾಲು ಕೂಗಿದವರಿಂದ   ಒಂದು ಕೆಜಿಗೆ ರೂ . "  + apiResponse.content.getBidAmount() +"  ದರದಂತೆ ಒಪ್ಪಿಕೊಳ್ಳಲಾಗಿದೆ .  ");

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
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ") \n" +
                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());

            String reelerNumberText = "";
            String reelerAddressText = "";
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            apiResponse.content.setReelerDetails( "  ಶ್ರೀ /ಶ್ರೀಮತಿ. " + apiResponse.content.getReelerName()+ "  ,  ಬಿನ್/ಕೋಂ " +apiResponse.content.getReelerFatherName() + " " + reelerAddressText);
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
                apiResponse.content.setLotSoldOutAmount(String.format("%.2f", Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getTraderMarketFee()));

//                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee())));

//                apiResponse.content.setLotSoldOutAmount(String.valueOf(roundToWholeNumber(Double.parseDouble(apiResponse.content.getTotalamount()) - apiResponse.content.getFarmerMarketFee() - apiResponse.content.getReelerMarketFee())));
            }
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


    private JRDataSource getDataSourceBidSlipForSeedCocoon(MarketAuctionForPrintRequest requestDto) throws JsonProcessingException {

        ContentRoot apiResponse = apiService.fetchDataFromApiSeedCocoon(requestDto);
        List<Content> countries = new LinkedList<>();
        if (apiResponse.content != null) {

            apiResponse.content.setSadodLot(apiResponse.content.getSadodLotNumber());
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
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
            if (apiResponse.content.getFarmerEstimatedWeight() == null) {
                apiResponse.content.setFarmerEstimatedWeight(0f);
            }

            apiResponse.content.setReelerbalance(String.valueOf(roundToTwoDecimalPlaces(apiResponse.content.getReelerCurrentBalance())));
            String farmerNumber = "";
            if (apiResponse.content.getFruitsId() != null && !apiResponse.content.getFruitsId().equals("")) {
                farmerNumber = apiResponse.content.getFruitsId();
            } else {
                farmerNumber = apiResponse.content.getFarmerNumber();
            }
            apiResponse.content.setFarmerNameKannadaWithSerialNumber("(" + farmerNumber + ")\n" +
                    "  ಶ್ರೀ /ಶ್ರೀಮತಿ. "+ apiResponse.content.getFarmerNameKannada() + " ,  ಬಿನ್/ಕೋಂ    " + apiResponse.content.getFatherNameKan()  + " ,  " + apiResponse.content.getFarmerVillage() +" , "+ apiResponse.content.getFarmerTaluk());
            String reelerNumberText = "";
            String reelerAddressText = "";
            String externalUnitLicenseNumberText ="";
            String externalUnitLicenseAddresssText ="";
            if (apiResponse.content.getExternalUnitLicenseNumber() != null) {
                externalUnitLicenseNumberText = "(" + apiResponse.content.getExternalUnitLicenseNumber() + ")";
            }
            if (apiResponse.content.getReelerNumber() != null) {
                reelerNumberText = "(" + apiResponse.content.getReelerNumber() + ")";
            }
            if (apiResponse.content.getReelerAddress() != null) {
                reelerAddressText = apiResponse.content.getReelerAddress();
            }
            if (apiResponse.content.getExternalUnitAddress() != null) {
                externalUnitLicenseAddresssText = apiResponse.content.getExternalUnitAddress();
            }
//            apiResponse.content.setReelerDetails(reelerNumberText + " ,  ಶ್ರೀ /ಶ್ರೀಮತಿ.  " +apiResponse.content.getReelerName()+" ,  ಬಿನ್/ಕೋಂ  "  +apiResponse.content.getReelerNameKannada()+ " ,  " + reelerAddressText);
            apiResponse.content.setReelerDetails(externalUnitLicenseNumberText + " ,  " +apiResponse.content.getExternalUnitName()+" ,   "  +externalUnitLicenseAddresssText);

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
            apiResponse.content.setDescription("                  \n" +
                    "  ಕರ್ನಾಟಕ ಸಿಲ್ಕ್  ವರ್ಮ್ ಸೀಡ್, ಕಕೂನ್  ಅಂಡ್ ಸಿಲ್ಕ್  ಯಾರ್ನ್ \n" +
                    "  (ರೆಗ್ಯುಲೇಷನ್ ಆಫ್ ಪ್ರೊಡಕ್ಸನ್, ಸಪ್ಲೈ  , ಡಿಸ್ಟ್ರಿಬ್ಯೂಸನ್  ಅಂಡ್ ಸೇಲ್ಸ್ )\n" +
                    "  ರೂಲ್ಸ್  ೧೯೬೦-ಫಾರಂ ೭ಬಿ , ಬಿಡ್ ಸ್ಲಿಪ್ ನಂ."+ apiResponse.content.getAllottedLotId());

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
        if (apiResponse.getContent().getRaceName() == null) {
            apiResponse.getContent().setRaceName("");
        }
//        if (apiResponse.getContent().getCocoonAge() == null) {
//            apiResponse.getContent().setCocoonAge("");
//        }


        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = requestDto.getToDate().format(formatter);
        content.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ   ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ  ,  " + marketNameKannada + "   ದಿನವಹಿ  ವಹಿವಾಟು ತಖ್ತೆಃ  : " + formattedDate);
//        content.setTotal_weight_with_amount_details("Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " + (long) apiResponse.getContent().getTotalBidAmount() + " , Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount() +", Farmer Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount() +", MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Reeler Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        content.setTotal_weight_with_amount_details("Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " +  apiResponse.getContent().getTotalBidAmount() + " , Amount: " +  apiResponse.getContent().getTotallotSoldOutAmount() +", Farmer Amt: " + apiResponse.getContent().getTotalFarmerAmount() +", MF: " +  (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Reeler Amt: " +  apiResponse.getContent().getTotalReelerAmount());
        content.setTotal_lots("ಒಟ್ಟು   ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getTotalLots());
        content.setTransacted_lots("ಒಟ್ಟು   ವಹಿವಾಟಾಗಿರುವ ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getPaymentSuccessLots());
        if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
            content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : " +(apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
        } else {
            content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : 0");
        }

//        content.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//        content.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//        content.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
//        content.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
//        content.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());
//        content.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//        content.setLotSoldOutAmount("Total Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());
        content.setFarmer_cheque("ರೈತರ ಚೆಕ್ ಮೊತ್ತ : " + (Math.round(apiResponse.getContent().getTotalFarmerAmount())));
        content.setMf_amount("ಮಾರುಕಟ್ಟೆ   ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        content.setMin_amount("ಕನಿಷ್ಠ   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMinAmount()));
        content.setMax_amount("ಗರಿಷ್ಠ   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMaxAmount()));
        content.setAvg_amount("ಸರಾಸರಿ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getAvgAmount()));
        content.setReeler_transaction_amt("ಖರೀದಿದಾರರ ವ್ಯವಹಾರ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalReelerAmount()));
        content.setLotSoldOutAmount("ಒಟ್ಟು   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotallotSoldOutAmount()));
        content.setWeight("ಒಟ್ಟು   ತೂಕ : " + (roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight())));

        content.setTotal_weight_with_amount_details(roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()));





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
            String talukNameInKannada = "";
            if (dtrOnlineReportUnitDetail.getTalukNameInKannada() != null) {
                talukNameInKannada = "/" + dtrOnlineReportUnitDetail.getTalukNameInKannada() + ",";
            }
            String villageNameInKannada = "";
            if (dtrOnlineReportUnitDetail.getVillageNameInKannada() != null) {
                villageNameInKannada = "/" + dtrOnlineReportUnitDetail.getVillageNameInKannada() + ",";
            }

            double parsedWeight = parseDoubleOrDefault(dtrOnlineReportUnitDetail.getWeight());
//            double roundedWeight = roundToThreeDecimalPlaces(parsedWeight);
            dtrOnlineReportUnitDetail.setWeight(String.valueOf(parsedWeight));
            dtrOnlineReportUnitDetail.setWeight(
                    String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(dtrOnlineReportUnitDetail.getWeight()))));
            //            groupLotRaceStatus.setWeight31(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getWeight(), 0))));
            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getBankName() + "/" + dtrOnlineReportUnitDetail.getAccountNumber());
//            dtrOnlineReportUnitDetail.setFarmerDetails(dtrOnlineReportUnitDetail.getFarmerFirstName() + " " + dtrOnlineReportUnitDetail.getFarmerMiddleName() + " " + dtrOnlineReportUnitDetail.getFarmerLastName() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + farmerAddress + " (" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ") "  +talukName + " ,  " + villageName );
            dtrOnlineReportUnitDetail.setFarmerDetails( "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ")  "+" ಶ್ರೀ /ಶ್ರೀಮತಿ. " +dtrOnlineReportUnitDetail.getFarmerNameKannada() + " , ಬಿನ್/ಕೋಂ " + dtrOnlineReportUnitDetail.getFatherNameKannada() +"  " + dtrOnlineReportUnitDetail.getFarmerAddress() + "(" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ") "  +dtrOnlineReportUnitDetail.getTalukNameInKannada()+ ", " + dtrOnlineReportUnitDetail.getVillageNameInKannada());
            dtrOnlineReportUnitDetail.setReelerDetails(dtrOnlineReportUnitDetail.getReelerName() + "(" + dtrOnlineReportUnitDetail.getReelerLicense() + ")" + "(" + dtrOnlineReportUnitDetail.getReelerMobile() + ")");

//            dtrOnlineReportUnitDetail.setMarketFee(String.valueOf(Math.round((Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee())))));
//            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount()))));
//            dtrOnlineReportUnitDetail.setFarmerAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount()))));
//            dtrOnlineReportUnitDetail.setReelerAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount()))));
            dtrOnlineReportUnitDetail.setRaceName(dtrOnlineReportUnitDetail.getRaceName());
            dtrOnlineReportUnitDetail.setCocoonAge(dtrOnlineReportUnitDetail.getCocoonAge());
            dtrOnlineReportUnitDetail.setMarketFee(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee())));
            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount())));
            dtrOnlineReportUnitDetail.setFarmerAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount())));
            dtrOnlineReportUnitDetail.setReelerAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount())));



            dtrOnlineReportUnitDetail.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ ,  " + marketNameKannada + "  ದಿನವಹಿ  ವಹಿವಾಟು ತಖ್ತೆಃ  : " + formattedDate);
            //   dtrOnlineReportUnitDetail.setTotal_weight_with_amount_details("Wt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight())+" , Amount: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount())+", Farmer Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount())+ ",MF: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee())+", Reeler Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setTotal_lots("ಒಟ್ಟು   ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getTotalLots());
            content.setTransacted_lots("ಒಟ್ಟು    ವಹಿವಾಟಾಗಿರುವ ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getPaymentSuccessLots() );
            if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
                content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : "  + (apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
            } else {
                content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : 0");
            }
//            dtrOnlineReportUnitDetail.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//            dtrOnlineReportUnitDetail.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//            dtrOnlineReportUnitDetail.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//            dtrOnlineReportUnitDetail.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
//            dtrOnlineReportUnitDetail.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
//            dtrOnlineReportUnitDetail.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());
            dtrOnlineReportUnitDetail.setFarmer_cheque("ರೈತರ ಚೆಕ್ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalFarmerAmount()));
            dtrOnlineReportUnitDetail.setMf_amount("ಮಾರುಕಟ್ಟೆ   ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
            dtrOnlineReportUnitDetail.setReeler_transaction_amt("ಖರೀದಿದಾರರ ವ್ಯವಹಾರ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setMin_amount("ಕನಿಷ್ಠ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMinAmount()));
            dtrOnlineReportUnitDetail.setMax_amount("ಗರಿಷ್ಠ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMaxAmount()));
            dtrOnlineReportUnitDetail.setAvg_amount("ಸರಾಸರಿ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getAvgAmount()));

            contentList.add(dtrOnlineReportUnitDetail);
        }
        DTROnlineReportUnitDetail contentLastColumn = new DTROnlineReportUnitDetail();
        contentLastColumn.setSerialNumber("");
        contentLastColumn.setAllottedLotId("");
        contentLastColumn.setFarmerDetails("");
        contentLastColumn.setWeight("ತೂಕ : " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()));
        contentLastColumn.setBidAmount("");
//        contentLastColumn.setLotSoldOutAmount("Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());
//        contentLastColumn.setFarmerAmount("F Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//        contentLastColumn.setMarketFee("MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        contentLastColumn.setLotSoldOutAmount("ಒಟ್ಟು ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotallotSoldOutAmount()));
        contentLastColumn.setFarmerAmount("ರೈತರ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalFarmerAmount()));
        contentLastColumn.setMarketFee("ಮಾರುಕಟ್ಟೆ ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        contentLastColumn.setReelerAmount("ಖರೀದಿದಾರರ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalReelerAmount()));
        contentLastColumn.setReelerDetails("");
        contentLastColumn.setBankDetails("");
        contentLastColumn.setIfscCode("");
        contentLastColumn.setAccountNumber("");
        contentLastColumn.setRaceName("");
        contentLastColumn.setCocoonAge("");
        contentList.add(contentLastColumn);
        return new JRBeanCollectionDataSource(contentList);
    }

    @PostMapping("/dtr-online-report-silk")
    public ResponseEntity<?> dtrOnlineReportSilk(@RequestBody DTROnlineRequest request) {

        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            JasperReport jasperReport = getJasperReport("dtr_online_report_silk.jrxml");

            // 2. datasource "java object"
            JRBeanCollectionDataSource dataSource = getDtrOnlineReportDataForSilkType(request);

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

    private JRBeanCollectionDataSource getDtrOnlineReportDataForSilkType(DTROnlineRequest requestDto) throws JsonProcessingException {
        DTRReportResponse apiResponse = apiService.dtrReportForSilkType(requestDto);
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
        if (apiResponse.getContent().getRaceName() == null) {
            apiResponse.getContent().setRaceName("");
        }
//        if (apiResponse.getContent().getCocoonAge() == null) {
//            apiResponse.getContent().setCocoonAge("");
//        }


        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = requestDto.getToDate().format(formatter);
        content.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ   ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ  ,  " + marketNameKannada + "   ದಿನವಹಿ  ವಹಿವಾಟು ತಖ್ತೆಃ  : " + formattedDate);
//        content.setTotal_weight_with_amount_details("Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " + (long) apiResponse.getContent().getTotalBidAmount() + " , Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount() +", Farmer Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount() +", MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Reeler Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        content.setTotal_weight_with_amount_details("Wt: " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()) + " , Amount: " +  apiResponse.getContent().getTotalBidAmount() + " , Amount: " +  apiResponse.getContent().getTotallotSoldOutAmount() +", Reeler Amt: " + apiResponse.getContent().getTotalReelerAmount() +", MF: " +  (apiResponse.getContent().getTotalTraderMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()) + ", Trader Amt: " +  apiResponse.getContent().getTotalTraderAmount());
        content.setTotal_lots("ಒಟ್ಟು   ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getTotalLots());
        content.setTransacted_lots("ಒಟ್ಟು   ವಹಿವಾಟಾಗಿರುವ ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getPaymentSuccessLots());
        if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
            content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : " +(apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
        } else {
            content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : 0");
        }

//        content.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//        content.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//        content.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
//        content.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
//        content.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());
//        content.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//        content.setLotSoldOutAmount("Total Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());
        content.setFarmer_cheque("ರೀಲರ್ ಚೆಕ್ ಮೊತ್ತ : " + (Math.round(apiResponse.getContent().getTotalReelerAmount())));
        content.setMf_amount("ಮಾರುಕಟ್ಟೆ   ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        content.setMin_amount("ಕನಿಷ್ಠ   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMinAmount()));
        content.setMax_amount("ಗರಿಷ್ಠ   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMaxAmount()));
        content.setAvg_amount("ಸರಾಸರಿ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getAvgAmount()));
        content.setReeler_transaction_amt("ಟ್ರೇಡರ್   ವ್ಯವಹಾರ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalTraderAmount()));
        content.setLotSoldOutAmount("ಒಟ್ಟು   ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotallotSoldOutAmount()));
        content.setWeight("ಒಟ್ಟು   ತೂಕ : " + (roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight())));

        content.setTotal_weight_with_amount_details(roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()));





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
            String talukNameInKannada = "";
            if (dtrOnlineReportUnitDetail.getTalukNameInKannada() != null) {
                talukNameInKannada = "/" + dtrOnlineReportUnitDetail.getTalukNameInKannada() + ",";
            }
            String villageNameInKannada = "";
            if (dtrOnlineReportUnitDetail.getVillageNameInKannada() != null) {
                villageNameInKannada = "/" + dtrOnlineReportUnitDetail.getVillageNameInKannada() + ",";
            }

            double parsedWeight = parseDoubleOrDefault(dtrOnlineReportUnitDetail.getWeight());
//            double roundedWeight = roundToThreeDecimalPlaces(parsedWeight);
            dtrOnlineReportUnitDetail.setWeight(String.valueOf(parsedWeight));
            dtrOnlineReportUnitDetail.setWeight(
                    String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(dtrOnlineReportUnitDetail.getWeight()))));
            //            groupLotRaceStatus.setWeight31(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getWeight(), 0))));
//            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getBankName() + "/" + dtrOnlineReportUnitDetail.getAccountNumber());
            dtrOnlineReportUnitDetail.setBankDetails(dtrOnlineReportUnitDetail.getReelerBankName() + "/" + dtrOnlineReportUnitDetail.getReelerBankAccountNumber());
//            dtrOnlineReportUnitDetail.setFarmerDetails(dtrOnlineReportUnitDetail.getFarmerFirstName() + " " + dtrOnlineReportUnitDetail.getFarmerMiddleName() + " " + dtrOnlineReportUnitDetail.getFarmerLastName() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + farmerAddress + " (" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ") "  +talukName + " ,  " + villageName );
//            dtrOnlineReportUnitDetail.setFarmerDetails(" ಶ್ರೀ /ಶ್ರೀಮತಿ. " +dtrOnlineReportUnitDetail.getFarmerNameKannada() + " , ಬಿನ್/ಕೋಂ " + dtrOnlineReportUnitDetail.getFatherNameKannada() + "(" + dtrOnlineReportUnitDetail.getFarmerNumber() + ") " + dtrOnlineReportUnitDetail.getFarmerAddress() + "(" + dtrOnlineReportUnitDetail.getFarmerMobileNumber() + ") "  +dtrOnlineReportUnitDetail.getTalukNameInKannada()+ ", " + dtrOnlineReportUnitDetail.getVillageNameInKannada());
            dtrOnlineReportUnitDetail.setReelerDetails(" ಶ್ರೀ /ಶ್ರೀಮತಿ. " +dtrOnlineReportUnitDetail.getReelerName() +  " ("+ dtrOnlineReportUnitDetail.getReelerLicense() + ")" + "(" + dtrOnlineReportUnitDetail.getReelerMobile() + ")");
            dtrOnlineReportUnitDetail.setFarmerDetails(" ಶ್ರೀ /ಶ್ರೀಮತಿ. " +dtrOnlineReportUnitDetail.getTraderFirstName() + " "+dtrOnlineReportUnitDetail.getTraderLastName() +" , ಬಿನ್/ಕೋಂ " + dtrOnlineReportUnitDetail.getTraderFatherName() + "(" + dtrOnlineReportUnitDetail.getTraderLicenseNumber() + ") " + dtrOnlineReportUnitDetail.getTraderAddress() + "(" + dtrOnlineReportUnitDetail.getTraderMobileNumber() + ") "  +dtrOnlineReportUnitDetail.getTraderDistrictNameInKannada());
            dtrOnlineReportUnitDetail.setIfscCode(dtrOnlineReportUnitDetail.getReelerIfscCode());
            dtrOnlineReportUnitDetail.setAccountNumber(dtrOnlineReportUnitDetail.getReelerBankAccountNumber());


//            dtrOnlineReportUnitDetail.setMarketFee(String.valueOf(Math.round((Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee())))));
//            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount()))));
//            dtrOnlineReportUnitDetail.setFarmerAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount()))));
//            dtrOnlineReportUnitDetail.setReelerAmount(String.valueOf(Math.round(Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount()))));
            dtrOnlineReportUnitDetail.setRaceName(dtrOnlineReportUnitDetail.getRaceName());
            dtrOnlineReportUnitDetail.setCocoonAge(dtrOnlineReportUnitDetail.getCocoonAge());
            dtrOnlineReportUnitDetail.setMarketFee(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerMarketFee()) + Double.parseDouble(dtrOnlineReportUnitDetail.getReelerMarketFee())));
            dtrOnlineReportUnitDetail.setLotSoldOutAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getLotSoldOutAmount())));
//            dtrOnlineReportUnitDetail.setFarmerAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getFarmerAmount())));
            dtrOnlineReportUnitDetail.setFarmerAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getTraderAmount())));

            dtrOnlineReportUnitDetail.setReelerAmount(String.format("%.2f", Double.parseDouble(dtrOnlineReportUnitDetail.getReelerAmount())));



            dtrOnlineReportUnitDetail.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ ,  " + marketNameKannada + "  ದಿನವಹಿ  ವಹಿವಾಟು ತಖ್ತೆಃ  : " + formattedDate);
            //   dtrOnlineReportUnitDetail.setTotal_weight_with_amount_details("Wt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalWeight())+" , Amount: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalBidAmount())+", Farmer Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerAmount())+ ",MF: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalFarmerMarketFee()+apiResponse.getContent().getTotalReelerMarketFee())+", Reeler Amt: "+roundToTwoDecimalPlaces(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setTotal_lots("ಒಟ್ಟು   ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getTotalLots());
            content.setTransacted_lots("ಒಟ್ಟು    ವಹಿವಾಟಾಗಿರುವ ಲಾಟ್ ಗಳು : " + apiResponse.getContent().getPaymentSuccessLots() );
            if ((apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()) > 0) {
                content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : "  + (apiResponse.getContent().getTotalLots() - apiResponse.getContent().getPaymentSuccessLots()));
            } else {
                content.setNot_transacted_lots("ವಹಿವಾಟಾಗದ ಲಾಟ್ ಗಳು : 0");
            }
//            dtrOnlineReportUnitDetail.setFarmer_cheque("Farmer cheque Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//            dtrOnlineReportUnitDetail.setMf_amount("MF Amt: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//            dtrOnlineReportUnitDetail.setReeler_transaction_amt("Reeler transaction Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//            dtrOnlineReportUnitDetail.setMin_amount("Min Amount : " + (long) apiResponse.getContent().getMinAmount());
//            dtrOnlineReportUnitDetail.setMax_amount(" Max Amount : " +(long) apiResponse.getContent().getMaxAmount());
//            dtrOnlineReportUnitDetail.setAvg_amount(" Avg Amount : " + (long) apiResponse.getContent().getAvgAmount());
            dtrOnlineReportUnitDetail.setFarmer_cheque("ರೀಲರ್ ಚೆಕ್ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalReelerAmount()));
            dtrOnlineReportUnitDetail.setMf_amount("ಮಾರುಕಟ್ಟೆ   ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalTraderMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
            dtrOnlineReportUnitDetail.setReeler_transaction_amt("ಟ್ರೇಡರ್  ವ್ಯವಹಾರ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalTraderAmount()));
            dtrOnlineReportUnitDetail.setMin_amount("ಕನಿಷ್ಠ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMinAmount()));
            dtrOnlineReportUnitDetail.setMax_amount("ಗರಿಷ್ಠ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getMaxAmount()));
            dtrOnlineReportUnitDetail.setAvg_amount("ಸರಾಸರಿ  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getAvgAmount()));

            contentList.add(dtrOnlineReportUnitDetail);
        }
        DTROnlineReportUnitDetail contentLastColumn = new DTROnlineReportUnitDetail();
        contentLastColumn.setSerialNumber("");
        contentLastColumn.setAllottedLotId("");
        contentLastColumn.setFarmerDetails("");
        contentLastColumn.setWeight("ತೂಕ : " + roundToThreeDecimalPlaces(apiResponse.getContent().getTotalWeight()));
        contentLastColumn.setBidAmount("");
//        contentLastColumn.setLotSoldOutAmount("Amount: " + (long) apiResponse.getContent().getTotallotSoldOutAmount());
//        contentLastColumn.setFarmerAmount("F Amt: " + (long) apiResponse.getContent().getTotalFarmerAmount());
//        contentLastColumn.setMarketFee("MF: " + (long) (apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
//        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
//        contentLastColumn.setReelerAmount("R Amt: " + (long) apiResponse.getContent().getTotalReelerAmount());
        contentLastColumn.setLotSoldOutAmount("ಒಟ್ಟು ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotallotSoldOutAmount()));
        contentLastColumn.setFarmerAmount("ಟ್ರೇಡರ್ ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalTraderAmount()));
        contentLastColumn.setMarketFee("ಮಾರುಕಟ್ಟೆ ಶುಲ್ಕ : " + Math.round(apiResponse.getContent().getTotalFarmerMarketFee() + apiResponse.getContent().getTotalReelerMarketFee()));
        contentLastColumn.setReelerAmount("ರೀಲರ್  ಮೊತ್ತ : " + Math.round(apiResponse.getContent().getTotalReelerAmount()));
        contentLastColumn.setReelerDetails("");
        contentLastColumn.setBankDetails("");
        contentLastColumn.setIfscCode("");
        contentLastColumn.setAccountNumber("");
        contentLastColumn.setRaceName("");
        contentLastColumn.setCocoonAge("");
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
//                lotReportResponse.setAcceptedTime(convertToTimeWithMillis(lotReportResponse.getAcceptedTime()));
//            }
//            if (lotReportResponse.getBidTime() == null) {
//                lotReportResponse.setBidTime("");
//            } else {
//                lotReportResponse.setBidTime(convertToTimeWithMillis(lotReportResponse.getBidTime()));
//            }
//            contentList.add(lotReportResponse);
//        }
//        lotReportResponse1.setHeaderText("Government Cocoon Market, " + marketName + "\n BIDDING REPORT");
//        lotReportResponse1.setHeaderText2("Lot Number = " + requestDto.getLotId() + " and Bid Date = " + convertDate(requestDto.getReportFromDate().toString()));
//        contentList.add(0, lotReportResponse1);
//        lotReportResponse1.setSerialNumber("");
//        contentList.add(lotReportResponse1);
//        return new JRBeanCollectionDataSource(contentList);
//    }
    private JRBeanCollectionDataSource getBiddingReportData(BiddingReportRequest requestDto) throws Exception {
        String marketName = "";
        BiddingReportResponse apiResponse = apiService.biddingReport(requestDto);
        List<LotReportResponse> contentList = new LinkedList<>();
        LotReportResponse lotReportResponse1 = new LotReportResponse();

        for (LotReportResponse lotReportResponse : apiResponse.getContent()) {
            marketName = lotReportResponse.getMarketNameInKannada();

            if (lotReportResponse.getAcceptedBy() == null) {
                lotReportResponse.setAcceptedBy("");

            }
            if (lotReportResponse.getMarketNameInKannada() == null) {
                lotReportResponse.setMarketNameInKannada("");
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
        lotReportResponse1.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ  , " + marketName+ "   ಬಿಡ್  ರಿಪೋರ್ಟ್");
//    lotReportResponse1.setMarketNameInKannada("ಸರ್ಕಾರಿ ಕಕೂನ್  ಮಾರುಕಟ್ಟೆ  , " + marketName+ "   ಬಿಡ್  ರಿಪೋರ್ಟ್");


        // Determine lot number text
        String lotNumberText = (requestDto.getLotId() == 0) ? " ಒಟ್ಟು   ಲಾಟ್ಸ್ " : String.valueOf(requestDto.getLotId());
        lotReportResponse1.setHeaderText2("ಲಾಟ್  ಸಂಖ್ಯೆ  : " + lotNumberText + " ,  ಬಿಡ್ ದಿನಾಂಕ : " + convertDate(requestDto.getReportFromDate().toString()));

        contentList.add(0, lotReportResponse1);
        lotReportResponse1.setSerialNumber("");
//    contentList.add(lotReportResponse1);
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
            marketName = lotReportResponse.getMarketNameInKannada();
            if (lotReportResponse.getAcceptedBy() == null) {
                lotReportResponse.setAcceptedBy("");
            }
            if (lotReportResponse.getMarketNameInKannada() == null) {
                lotReportResponse.setMarketNameInKannada("");
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
        lotReportResponse1.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ   , " + marketName  + "  ಬಿಡ್  ರಿಪೋರ್ಟ್");
        lotReportResponse1.setHeaderText2("ಖರೀದಿದಾರರ ಐಡಿ :  " + (requestDto.getReelerNumber() != null && !requestDto.getReelerNumber().equals("") ? requestDto.getReelerNumber() :"ಒಟ್ಟು    ಐಡಿ") + "  ,  ಬಿಡ್ ದಿನಾಂಕ  : " + convertDate(requestDto.getReportFromDate().toString()));
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
//        lotReportResponse1.setHeaderText("Pending report for " + convertDate(requestDto.getReportFromDate().toString()));
        lotReportResponse1.setHeaderText("ಬಾಕಿಯಿರುವ ರಿಪೋರ್ಟ್ , ದಿನಾಂಕ : " + convertDate(requestDto.getReportFromDate().toString()));

        contentList.add(lotReportResponse1);
        for (LotPendingReportResponse lotReportResponse : apiResponse.getContent()) {
            lotReportResponse.setAccpetedBy(lotReportResponse.getAcceptedBy());
            if (lotReportResponse.getShed() == null) {
                lotReportResponse.setShed("");
            }
            if (lotReportResponse.getReelerNumber() == null) {
                lotReportResponse.setReelerNumber("");
            }
            if (lotReportResponse.getFarmerVillageInKannada() == null) {
                lotReportResponse.setFarmerVillageInKannada("");
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
            lotReportResponse.setFarmerDetails(lotReportResponse.getFarmerNameKannada());
            lotReportResponse.setVillage(lotReportResponse.getFarmerVillageInKannada());
            lotReportResponse.setReeler_amount(String.valueOf(lotReportResponse.getReelerAmount()));

            lotReportResponse.setFarmerDetails(lotReportResponse.getFarmerNameKannada());

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



    @PostMapping("/get-dashboard-report-silk")
    public ResponseEntity<?> getDashboardCountReportsSilkType(@RequestBody DashboardReportRequest request) {

        try {
            System.out.println("enter to dashboard report pdf");
            logger.info("enter to bidding report pdf");
            JasperReport jasperReport = getJasperReport("dashboard_report.jrxml");

            // 2. datasource "java object"
            JRDataSource dataSource = getDashboardReportCountSilkType(request);

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

    private JRBeanCollectionDataSource getDashboardReportCountSilkType(DashboardReportRequest requestDto) throws JsonProcessingException {
        DashboardResponse apiResponse = apiService.getDashboardReportSilkType(requestDto);
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
                groupLotStateStatus.setAmount21(String.valueOf(Math.round( parseDoubleOrDefault(amount, 0))));

                String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
                groupLotStateStatus.setMax21(String.valueOf(Math.round(parseDoubleOrDefault(max, 0))));

                String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
                groupLotStateStatus.setMin21(String.valueOf(Math.round(parseDoubleOrDefault(min, 0))));

                String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
                groupLotStateStatus.setAvg21(String.valueOf(Math.round(parseDoubleOrDefault(avg, 0))));

                String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
                groupLotStateStatus.setMf21(String.valueOf(Math.round(parseDoubleOrDefault(mf, 0))));

                groupStateLotStatuses.add(groupLotStateStatus);
            }

            List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                    groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                    groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                    groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotGenderStatus.setAmount41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotGenderStatus.setMax41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0))));
                    groupLotGenderStatus.setMin41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0))));
                    groupLotGenderStatus.setAvg41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotGenderStatus.setMf41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0))));
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
                    groupLotRaceStatus.setAmount31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotRaceStatus.setMax31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0))));
                    groupLotRaceStatus.setMin31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0))));
                    groupLotRaceStatus.setAvg31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotRaceStatus.setMf31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0))));
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
//            apiResponse.setAmount11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAmount())));
//            apiResponse.setMax11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMax())));
//            apiResponse.setMin11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMin())));
//            apiResponse.setMf11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMf())));
//            apiResponse.setAvg11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAvg())));
//        }
        if (apiResponse.getContent().getTotalLotStatus().size() > 0) {
            apiResponse.setLot11(apiResponse.getContent().getTotalLotStatus().get(0).getLot());
            apiResponse.setWeight11(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getWeight(),0))));
            apiResponse.setAmount11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0))));
            apiResponse.setMax11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0))));
            apiResponse.setMin11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0))));
            apiResponse.setMf11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0))));
            apiResponse.setAvg11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0))));
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
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ , " + apiResponse.getContent().getMarketNameKannada() + "  ದಿನವಹಿ ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(requestDto.getFromDate().toString())+ " to " + convertDate(requestDto.getToDate().toString()));
//        apiResponse.setHeaderText1("Form 13 Abstract " +
//                (apiResponse.getContent().getRaceName() != null && !apiResponse.getContent().getRaceName().isEmpty()
//                        ? apiResponse.getContent().getRaceName()
//                        : "all"));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0))));
        apiResponse.setTotalStateLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0))));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0))));
        apiResponse.setTotalStateMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0))));
        apiResponse.setTotalStateMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0))));
        apiResponse.setTotalStateMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0))));
        apiResponse.setTotalStateAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0))));

        apiResponse.setTotalGenderLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0))));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0))));
        apiResponse.setTotalGenderMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0))));
        apiResponse.setTotalGenderMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0))));
        apiResponse.setTotalGenderMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0))));
        apiResponse.setTotalGenderAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0))));

        apiResponse.setTotalRaceLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0))));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0))));
        apiResponse.setTotalRaceMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0))));
        apiResponse.setTotalRaceMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0))));
        apiResponse.setTotalRaceMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0))));
        apiResponse.setTotalRaceAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0))));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0))));
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
                groupLotStateStatus.setAmount21(String.valueOf(Math.round(parseDoubleOrDefault(amount, 0))));

                String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
                groupLotStateStatus.setMax21(String.valueOf(Math.round(parseDoubleOrDefault(max, 0))));

                String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
                groupLotStateStatus.setMin21(String.valueOf(Math.round(parseDoubleOrDefault(min, 0))));

                String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
                groupLotStateStatus.setAvg21(String.valueOf(Math.round(parseDoubleOrDefault(avg, 0))));

                String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
                groupLotStateStatus.setMf21(String.valueOf(Math.round(parseDoubleOrDefault(mf, 0))));

                groupStateLotStatuses.add(groupLotStateStatus);
            }

            List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                    groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                    groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                    groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotGenderStatus.setAmount41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotGenderStatus.setMax41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0))));
                    groupLotGenderStatus.setMin41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0))));
                    groupLotGenderStatus.setAvg41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotGenderStatus.setMf41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0))));
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
                    groupLotRaceStatus.setAmount31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotRaceStatus.setMax31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0))));
                    groupLotRaceStatus.setMin31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0))));
                    groupLotRaceStatus.setAvg31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotRaceStatus.setMf31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0))));
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
            apiResponse.setAmount11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0))));
            apiResponse.setMax11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0))));
            apiResponse.setMin11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0))));
            apiResponse.setMf11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0))));
            apiResponse.setAvg11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0))));
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
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ  , " + apiResponse.getContent().getMarketNameKannada() + "  ದಿನವಹಿ  ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(requestDto.getFromDate().toString()) + " to " + convertDate(requestDto.getToDate().toString()));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0))));
        apiResponse.setTotalStateLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0))));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0))));
        apiResponse.setTotalStateMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0))));
        apiResponse.setTotalStateMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0))));
        apiResponse.setTotalStateMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0))));
        apiResponse.setTotalStateAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0))));

        apiResponse.setTotalGenderLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0))));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0))));
        apiResponse.setTotalGenderMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0))));
        apiResponse.setTotalGenderMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0))));
        apiResponse.setTotalGenderMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0))));
        apiResponse.setTotalGenderAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0))));

        apiResponse.setTotalRaceLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0))));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0))));
        apiResponse.setTotalRaceMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0))));
        apiResponse.setTotalRaceMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0))));
        apiResponse.setTotalRaceMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0))));
        apiResponse.setTotalRaceAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0))));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0))));
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
    @PostMapping("/get-form-13-report-silk")
    public ResponseEntity<byte[]> getForm13ReportSilk(@RequestBody Form13Request request) {
        try {
            System.out.println("enter to form 14");
            logger.info("enter to form 13");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("form_13_cb_report_silk.jrxml");

            Form13ReportResponse apiResponse = apiService.getForm13ReportSilk(request);
            List<GroupLotStatus> groupStateLotStatuses = new ArrayList<>();

            for(int i=0; i<apiResponse.getContent().getStateWiseLotStatus().size(); i++) {
                GroupLotStatus groupLotStateStatus = new GroupLotStatus();
                groupLotStateStatus.setStateName(apiResponse.getContent().getStateWiseLotStatus().get(i).getDescription());
                groupLotStateStatus.setLot21(apiResponse.getContent().getStateWiseLotStatus().get(i).getLot());

                String weight = apiResponse.getContent().getStateWiseLotStatus().get(i).getWeight();
                groupLotStateStatus.setWeight21(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(weight, 0))));

                String amount = apiResponse.getContent().getStateWiseLotStatus().get(i).getAmount();
                groupLotStateStatus.setAmount21(String.valueOf(Math.round( parseDoubleOrDefault(amount, 0))));

                String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
                groupLotStateStatus.setMax21(String.valueOf(Math.round(parseDoubleOrDefault(max, 0))));

                String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
                groupLotStateStatus.setMin21(String.valueOf(Math.round(parseDoubleOrDefault(min, 0))));

                String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
                groupLotStateStatus.setAvg21(String.valueOf(Math.round(parseDoubleOrDefault(avg, 0))));

                String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
                groupLotStateStatus.setMf21(String.valueOf(Math.round(parseDoubleOrDefault(mf, 0))));

                groupStateLotStatuses.add(groupLotStateStatus);
            }

            List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                    groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                    groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                    groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotGenderStatus.setAmount41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotGenderStatus.setMax41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0))));
                    groupLotGenderStatus.setMin41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0))));
                    groupLotGenderStatus.setAvg41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotGenderStatus.setMf41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0))));
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
                    groupLotRaceStatus.setAmount31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotRaceStatus.setMax31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0))));
                    groupLotRaceStatus.setMin31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0))));
                    groupLotRaceStatus.setAvg31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotRaceStatus.setMf31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0))));
                    groupRaceLotStatuses.add(groupLotRaceStatus);
                }
            }


            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getForm13DataSilkType(request);
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


    private JRBeanCollectionDataSource getForm13DataSilkType(Form13Request requestDto) throws JsonProcessingException {
        Form13ReportResponse apiResponse = apiService.getForm13ReportSilk(requestDto);

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
//            apiResponse.setAmount11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAmount())));
//            apiResponse.setMax11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMax())));
//            apiResponse.setMin11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMin())));
//            apiResponse.setMf11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getMf())));
//            apiResponse.setAvg11(String.valueOf(Math.round(Double.parseDouble(apiResponse.getContent().getTotalLotStatus().get(0).getAvg())));
//        }
        if (apiResponse.getContent().getTotalLotStatus().size() > 0) {
            apiResponse.setLot11(apiResponse.getContent().getTotalLotStatus().get(0).getLot());
            apiResponse.setWeight11(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getWeight(),0))));
            apiResponse.setAmount11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0))));
            apiResponse.setMax11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0))));
            apiResponse.setMin11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0))));
            apiResponse.setMf11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0))));
            apiResponse.setAvg11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0))));
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
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ , " + apiResponse.getContent().getMarketNameKannada() + "  ದಿನವಹಿ ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(requestDto.getFromDate().toString())+ " to " + convertDate(requestDto.getToDate().toString()));
//        apiResponse.setHeaderText1("Form 13 Abstract " +
//                (apiResponse.getContent().getRaceName() != null && !apiResponse.getContent().getRaceName().isEmpty()
//                        ? apiResponse.getContent().getRaceName()
//                        : "all"));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0))));
        apiResponse.setTotalStateLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0))));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0))));
        apiResponse.setTotalStateMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0))));
        apiResponse.setTotalStateMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0))));
        apiResponse.setTotalStateMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0))));
        apiResponse.setTotalStateAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0))));

        apiResponse.setTotalGenderLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0))));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0))));
        apiResponse.setTotalGenderMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0))));
        apiResponse.setTotalGenderMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0))));
        apiResponse.setTotalGenderMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0))));
        apiResponse.setTotalGenderAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0))));

        apiResponse.setTotalRaceLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0))));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0))));
        apiResponse.setTotalRaceMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0))));
        apiResponse.setTotalRaceMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0))));
        apiResponse.setTotalRaceMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0))));
        apiResponse.setTotalRaceAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0))));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0))));
        apiResponse.setTotalWeight2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalPercentage2(String.valueOf(roundToTwoDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalPercentage(), 0))));

        form13ReportResponses.add(apiResponse);
        return new JRBeanCollectionDataSource(form13ReportResponses);
    }

    @PostMapping("/get-form-13-report-by-dist-silk")
    public ResponseEntity<byte[]> getForm13ReportByDistSilkType(@RequestBody Form13Request request) {
        try {
            System.out.println("enter to form 14");
            logger.info("enter to form 13");
            String destFileName = "report_kannada.pdf";
            JasperReport jasperReport = getJasperReport("form_13_cb_report.jrxml");


            Form13ReportResponse apiResponse = apiService.getForm13ReportByDistSilkType(request);
            List<GroupLotStatus> groupStateLotStatuses = new ArrayList<>();
            for(int i=0; i<apiResponse.getContent().getStateWiseLotStatus().size(); i++) {
                GroupLotStatus groupLotStateStatus = new GroupLotStatus();
                groupLotStateStatus.setStateName(apiResponse.getContent().getStateWiseLotStatus().get(i).getDescription());
                groupLotStateStatus.setLot21(apiResponse.getContent().getStateWiseLotStatus().get(i).getLot());

                String weight = apiResponse.getContent().getStateWiseLotStatus().get(i).getWeight();
                groupLotStateStatus.setWeight21(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(weight, 0))));

                String amount = apiResponse.getContent().getStateWiseLotStatus().get(i).getAmount();
                groupLotStateStatus.setAmount21(String.valueOf(Math.round(parseDoubleOrDefault(amount, 0))));

                String max = apiResponse.getContent().getStateWiseLotStatus().get(i).getMax();
                groupLotStateStatus.setMax21(String.valueOf(Math.round(parseDoubleOrDefault(max, 0))));

                String min = apiResponse.getContent().getStateWiseLotStatus().get(i).getMin();
                groupLotStateStatus.setMin21(String.valueOf(Math.round(parseDoubleOrDefault(min, 0))));

                String avg = apiResponse.getContent().getStateWiseLotStatus().get(i).getAvg();
                groupLotStateStatus.setAvg21(String.valueOf(Math.round(parseDoubleOrDefault(avg, 0))));

                String mf = apiResponse.getContent().getStateWiseLotStatus().get(i).getMf();
                groupLotStateStatus.setMf21(String.valueOf(Math.round(parseDoubleOrDefault(mf, 0))));

                groupStateLotStatuses.add(groupLotStateStatus);
            }

            List<GroupLotStatus> groupGenderLotStatuses = new ArrayList<>();
            if (apiResponse.getContent().getGenderWiseLotStatus().size() > 0) {
                for (int i = 0; i < apiResponse.getContent().getGenderWiseLotStatus().size(); i++) {
                    GroupLotStatus groupLotGenderStatus = new GroupLotStatus();
                    groupLotGenderStatus.setGender(apiResponse.getContent().getGenderWiseLotStatus().get(i).getDescription());
                    groupLotGenderStatus.setLot41(apiResponse.getContent().getGenderWiseLotStatus().get(i).getLot());
                    groupLotGenderStatus.setWeight41(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getWeight(), 0))));
                    groupLotGenderStatus.setAmount41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotGenderStatus.setMax41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMax(), 0))));
                    groupLotGenderStatus.setMin41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMin(), 0))));
                    groupLotGenderStatus.setAvg41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotGenderStatus.setMf41(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getGenderWiseLotStatus().get(i).getMf(), 0))));
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
                    groupLotRaceStatus.setAmount31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAmount(), 0))));
                    groupLotRaceStatus.setMax31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMax(), 0))));
                    groupLotRaceStatus.setMin31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMin(), 0))));
                    groupLotRaceStatus.setAvg31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getAvg(), 0))));
                    groupLotRaceStatus.setMf31(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getRaceWiseLotStatus().get(i).getMf(), 0))));
                    groupRaceLotStatuses.add(groupLotRaceStatus);
                }
            }


            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getForm13ReportByDistSilk(request);
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

    private JRBeanCollectionDataSource getForm13ReportByDistSilk(Form13Request requestDto) throws JsonProcessingException {
        Form13ReportResponse apiResponse = apiService.getForm13ReportByDistSilkType(requestDto);

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
            apiResponse.setAmount11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAmount(),0))));
            apiResponse.setMax11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMax(),0))));
            apiResponse.setMin11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMin(),0))));
            apiResponse.setMf11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getMf(),0))));
            apiResponse.setAvg11(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalLotStatus().get(0).getAvg(),0))));
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
        apiResponse.setHeaderText("ಸರ್ಕಾರಿ ರೇಷ್ಮೆ  ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ  , " + apiResponse.getContent().getMarketNameKannada() + "  ದಿನವಹಿ  ವಹಿವಾಟು ಘೋಷ್ವರೆ   : " + convertDate(requestDto.getFromDate().toString()) + " to " + convertDate(requestDto.getToDate().toString()));
        apiResponse.setAverageRate("Average Rate Rs. " + String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getAverageRate(), 0.0))));
        apiResponse.setTotalStateLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalLots(),0))));
        apiResponse.setTotalStateWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalStateAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAmount(), 0))));
        apiResponse.setTotalStateMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMarketFee(), 0))));
        apiResponse.setTotalStateMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMin(), 0))));
        apiResponse.setTotalStateMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalMax(), 0))));
        apiResponse.setTotalStateAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(0).getTotalAvg(), 0))));

        apiResponse.setTotalGenderLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalLots(),0))));
        apiResponse.setTotalGenderWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalWeight(), 0))));
        apiResponse.setTotalGenderAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAmount(), 0))));
        apiResponse.setTotalGenderMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMarketFee(), 0))));
        apiResponse.setTotalGenderMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMin(), 0))));
        apiResponse.setTotalGenderMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalMax(), 0))));
        apiResponse.setTotalGenderAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(1).getTotalAvg(), 0))));

        apiResponse.setTotalRaceLots(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalLots(),0))));
        apiResponse.setTotalRaceWeight(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalWeight(), 0))));
        apiResponse.setTotalRaceAmount(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAmount(), 0))));
        apiResponse.setTotalRaceMarketFee(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMarketFee(), 0))));
        apiResponse.setTotalRaceMin(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMin(), 0))));
        apiResponse.setTotalRaceMax(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalMax(), 0))));
        apiResponse.setTotalRaceAvg(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getTotalStatus().get(2).getTotalAvg(), 0))));

        apiResponse.setDescription2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getDescription(), 0))));
        apiResponse.setTotalLots2(String.valueOf(Math.round(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalLots(), 0))));
        apiResponse.setTotalWeight2(String.valueOf(roundToThreeDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalWeight(), 0))));
        apiResponse.setTotalPercentage2(String.valueOf(roundToTwoDecimalPlaces(parseDoubleOrDefault(apiResponse.getContent().getLotsFrom0to351Total().get(0).getTotalPercentage(), 0))));


        form13ReportResponses.add(apiResponse);
        return new JRBeanCollectionDataSource(form13ReportResponses);
    }



    private JRDataSource getDataSourceForAcknowledgementReceipt(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchData(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader(" ಸ್ವೀಕೃತಿ   ಪತ್ರ  ( ACKNOWLEDGEMENT LETTER )");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "     ಯೋಜನೆಯಡಿ   " +apiResponse.getContent().get(0).getDistrictNameInKannada() + "   ಜಿಲ್ಲೆ   ,  " +apiResponse.getContent().get(0).getTalukNameInKannada()+ "   ತಾಲ್ಲೂಕು ," +
                    apiResponse.getContent().get(0).getHobliNameInKannada()+ "    ಹೋಬಳಿ,  " +apiResponse.getContent().get(0).getVillageNameInKannada()+ "  ಹಳ್ಳಿಯ   ನಿವಾಸಿಯಾದ   ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getFarmerFirstName()+ "   ರವರಿಂದ  " +
                    apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "   ಪಡೆಯಲು  ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸುತ್ತಾರೆ ." +
                    "ಇವರ  ನೋಂದಣಿ  ಸಂಖ್ಯೆಯ  :  " +apiResponse.getContent().get(0).getFruitsId() + "  ಇದ್ದು   ,   Arn No : " +apiResponse.getContent().get(0).getArn()+" ," +
                    "ಈ   ನೋಂದಣಿ   ಸಂಖ್ಯೆಯನ್ನು      ಮುಂದಿನ  ವಿಚರಾಣೆಗೆ   ಉಪಯೋಗಿಸತಕದ್ದು  .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು \n"+
                    "                        \n" +
                            apiResponse.getContent().get(0).getTscName() +"  ತಾಂತ್ರಿಕ  ಸೇವಾ  ಕೇಂದ್ರ");

            response.setHeader2("ARN No:  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceAcknowledgementTransportation(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataFromCommercialMarket(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            String formattedDate = "";
            try {
                String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

                // Parse input format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                // Define output format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Convert and format
                Date date = inputFormat.parse(inputDate);
                formattedDate = outputFormat.format(date);

            } catch (Exception e) {
                formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
            }
            response.setHeader(apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ  ಸಾಲಿನಲ್ಲಿ       "+apiResponse.getContent().get(0).getSchemeNameInKannada() +
                    "    ಯೋಜನೆ("+ apiResponse.getContent().get(0).getCategoryName()+")  ಅಡಿ   ಉತ್ತರ   ಕರ್ನಾಟಕ    ಜಿಲ್ಲೆಗಳಲ್ಲಿ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಉತ್ಪಾದಿಸುವ   ದ್ವಿತಳಿ    ರೇಷ್ಮೆ    ಗೂಡನ್ನು    ರಾಜ್ಯದ    ಯಾವುದೇ   ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    "+
                    "  ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ      ವಹಿವಾಟು     ಮಾಡಿದ   ದ್ವಿತಳಿ   ಸಂಕರಣ     ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಸಾಗಾಣಿಕೆ    ವೆಚ್ಚ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮ");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +formattedDate);
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "     ಯೋಜನೆಯಡಿ    ಶ್ರೀ./ಶ್ರೀಮತಿ.  " +
                     apiResponse.getContent().get(0).getFarmerFirstName()+  "   ಬಿನ್ /ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+  "    ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getVillageNameInKannada()+
                    "    ತಾಲ್ಲೂ ಕಿನ     "+apiResponse.getContent().get(0).getTalukNameInKannada()+  "     ಇವರು    ಉತ್ತರ   ಕರ್ನಾಟಕ    ಜಿಲ್ಲೆಗಳಲ್ಲಿ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಉತ್ಪಾದಿಸುವ   ದ್ವಿತಳಿ    ರೇಷ್ಮೆ    ಗೂಡನ್ನು    ರಾಜ್ಯದ    ಯಾವುದೇ   ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    " +
                            "ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ      ವಹಿವಾಟು     ಮಾಡಿದ   ದ್ವಿತಳಿ   ಸಂಕರಣ     ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಸಾಗಾಣಿಕೆ    ವೆಚ್ಚ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮದಡಿ    " +apiResponse.getContent().get(0).getCocoonsWeight()+
                    "  ಕೆ.ಜಿ    ದ್ವಿತಳಿ    ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಪ್ರತಿ    ಕೆ.ಜಿ ಗೆ    ರೂ. "+Math.round(apiResponse.getContent().get(0).getUnitCost())+ "/-  ರಂತೆ   ಸಾಗಾಣಿಕಾ   ವೆಚ್ಚ   ರೂ.  "+Math.round(apiResponse.getContent().get(0).getSchemeAmount())+
                            " /-  ಗಳನ್ನು     ಪಡೆಯಲು    ಸಲ್ಲಿಸಿದ     ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಿರುತ್ತಾರೆ .   ಪಡೆಯಲು    ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಿದ್ದು ,   ಅರ್ಜಿಯ    ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getArn()+
                    "   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ     ಸ್ಥಿತಿಯನ್ನು     ತಿಳಿಯಲು    ARN   ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ    ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕದ್ದು .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು \n"+
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,\n"+
                    apiResponse.getContent().get(0).getLoggedinUserTscName());

            response.setHeader2("ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceAckIncentive30(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataFromCommercialMarket(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            String formattedDate = "";
            try {
                String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

                // Parse input format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                // Define output format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Convert and format
                Date date = inputFormat.parse(inputDate);
                formattedDate = outputFormat.format(date);

            } catch (Exception e) {
                formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
            }
            response.setHeader(apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ  ಸಾಲಿನಲ್ಲಿ       "+apiResponse.getContent().get(0).getSchemeNameInKannada() +
                    "    ಯೋಜನೆ("+ apiResponse.getContent().get(0).getCategoryName()+")  ಅಡಿ   ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    "+
                    "  ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ      ವಹಿವಾಟು     ಮಾಡಿದ   ದ್ವಿತಳಿ   ಸಂಕರಣ     ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಪ್ರೋತ್ಸಾಹಧನ  ಕಾರ್ಯಕ್ರಮ");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +formattedDate);
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "     ಯೋಜನೆಯಡಿ    ಶ್ರೀ./ಶ್ರೀಮತಿ.  " +
                    apiResponse.getContent().get(0).getFarmerFirstName()+  "   ಬಿನ್ /ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+  "    ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getVillageNameInKannada()+
                    "    ತಾಲ್ಲೂ ಕಿನ     "+apiResponse.getContent().get(0).getTalukNameInKannada()+  "     "+apiResponse.getContent().get(0).getDistrictNameInKannada()+ "    ಜಿಲ್ಲೆ      ಇವರು    ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    " +
                    "ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ      ವಹಿವಾಟು     ಮಾಡಿದ   ದ್ವಿತಳಿ   ಸಂಕರಣ     ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಪ್ರೋತ್ಸಾಹಧನ  ಕಾರ್ಯಕ್ರಮದಲ್ಲಿ     " +apiResponse.getContent().get(0).getCocoonsWeight()+
                    "  ಕೆ.ಜಿ    ದ್ವಿತಳಿ    ರೇಷ್ಮೆ    ಗೂಡಿಗೆ    ಪ್ರತಿ    ಕೆ.ಜಿ ಗೆ    ರೂ. "+Math.round(apiResponse.getContent().get(0).getUnitCost())+ "/-  ರಂತೆ   ಪ್ರೋತ್ಸಾಹಧನ   ರೂ.  "+Math.round(apiResponse.getContent().get(0).getSchemeAmount())+
                    " /-  ಗಳನ್ನು     ಪಡೆಯಲು     ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಿರುತ್ತಾರೆ .  ಅರ್ಜಿ    ಸಲ್ಲಿಸಿದ್ದು ,   ಅರ್ಜಿಯ    ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getArn()+
                    "   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ     ಸ್ಥಿತಿಯನ್ನು     ತಿಳಿಯಲು    ARN   ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ    ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕದ್ದು .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು \n"+
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,\n"+
                    apiResponse.getContent().get(0).getLoggedinUserTscName());

            response.setHeader2("ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceAckChawki1000(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataFromCommercialMarket(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            String formattedDate = "";
            try {
                String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

                // Parse input format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                // Define output format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Convert and format
                Date date = inputFormat.parse(inputDate);
                formattedDate = outputFormat.format(date);

            } catch (Exception e) {
                formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
            }
            String raceName = apiResponse.getContent().get(0).getRaceName();
            String raceNameWithoutFirstWord = removeFirstWord(raceName);

            response.setHeader(apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ  ಸಾಲಿನಲ್ಲಿ       "+apiResponse.getContent().get(0).getSchemeNameInKannada() +
                    "    ಯೋಜನೆ("+ apiResponse.getContent().get(0).getCategoryName()+")  ಅಡಿ   "+raceNameWithoutFirstWord+"     ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ     ಸಹಾಯಧನ    ಕಾರ್ಯಕ್ರಮ ");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +formattedDate);
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "     ಯೋಜನೆಯಡಿ    ಶ್ರೀ./ಶ್ರೀಮತಿ.  " +
                    apiResponse.getContent().get(0).getFarmerFirstName()+  "   ಬಿನ್ /ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+  "    ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getVillageNameInKannada()+
                    "    ತಾಲ್ಲೂ ಕಿನ     "+apiResponse.getContent().get(0).getTalukNameInKannada()+  "     "+apiResponse.getContent().get(0).getDistrictNameInKannada()+ "    ಜಿಲ್ಲೆ      ಇವರು  " +raceNameWithoutFirstWord+"     ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ   " +
                    "   ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ     ಸಹಾಯಧನ    ಕಾರ್ಯಕ್ರಮದಡಿ     " +apiResponse.getContent().get(0).getCocoonsWeight()+ "    ಕೆ.ಜಿ    "+raceNameWithoutFirstWord+"    ರೇಷ್ಮೆ    ಮೊಟ್ಟೆಗಳಿಗೆ ,  ಪ್ರತಿ  100   ಮೊಟ್ಟೆಗಳಿಗೆ   ಸಹಾಯಧನ   ರೂ. "+Math.round(apiResponse.getContent().get(0).getUnitCost())+ "/-  ರಂತೆ   ಒಟ್ಟು     ರೂ.  "+Math.round(apiResponse.getContent().get(0).getSchemeAmount())+
                    " /-  ಗಳ    ಸಹಾಯಧನ   ಪಡೆಯಲು     ಅರ್ಜಿ    ಸಲ್ಲಿಸಿದ್ದು ,   ಅರ್ಜಿಯ    ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getArn()+
                    "   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ     ಸ್ಥಿತಿಯನ್ನು     ತಿಳಿಯಲು    ARN   ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ    ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕದ್ದು .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು \n"+
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,\n"+
                    apiResponse.getContent().get(0).getLoggedinUserTscName());

            response.setHeader2("ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }

    private JRDataSource getDataSourceAckChawki1500(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataFromSeedMarket(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            String formattedDate = "";
            try {
                String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

                // Parse input format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                // Define output format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Convert and format
                Date date = inputFormat.parse(inputDate);
                formattedDate = outputFormat.format(date);

            } catch (Exception e) {
                formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
            }
            String raceName = apiResponse.getContent().get(0).getRaceName();
            String raceNameWithoutFirstWord = removeFirstWord(raceName);

            response.setHeader("              "+apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ   ಸಾಲಿನಲ್ಲಿ        "+apiResponse.getContent().get(0).getSchemeNameInKannada() +
                    "      ಯೋಜನೆ("+ apiResponse.getContent().get(0).getScCategoryName()+")  ಯಡಿ   "+apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"     ಸಹಾಯಧನಕ್ಕಾಗಿ     ಶ್ರೀಮತಿ/ಶ್ರೀ     " +
                    apiResponse.getContent().get(0).getReelerName()+  "   ಬಿನ್/ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+  " ,     "+apiResponse.getContent().get(0).getVillageName()+"   ಗ್ರಾಮ     "+
                    apiResponse.getContent().get(0).getTalukName()+ "    ತಾಲ್ಲೂಕು    ("+apiResponse.getContent().get(0).getFruitsId()+")    ಇವರ    ಅರ್ಜಿಯನ್ನು      ಸ್ವೀಕರಿಸಲಾಗಿದೆ.    ಅರ್ಜಿಯ     ಪ್ರಸ್ತುತ     ಸ್ಥಿತಿಯನ್ನು      ಇ-ರೇಷ್ಮೆ     " +
                    "ವೆಬ್ಸೈಟ್      https://e-reshme.karnataka.gov.in/seriui    ನಲ್ಲಿ      ARN/FID/Mob No.    ನಮೂದಿಸಿ    ಪರಿಶೀಲಿಸಬಹುದು.");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +formattedDate);
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
//            response.setLineItemComment( "              " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "     ಯೋಜನೆಯಡಿ    ಶ್ರೀ./ಶ್ರೀಮತಿ.  " +
//                    apiResponse.getContent().get(0).getFarmerFirstName()+  "   ಬಿನ್ /ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+  "    ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getVillageNameInKannada()+
//                    "    ತಾಲ್ಲೂ ಕಿನ     "+apiResponse.getContent().get(0).getTalukNameInKannada()+  "     "+apiResponse.getContent().get(0).getDistrictNameInKannada()+ "    ಜಿಲ್ಲೆ      ಇವರು  " +raceNameWithoutFirstWord+"     ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ   " +
//                    "   ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ     ಸಹಾಯಧನ    ಕಾರ್ಯಕ್ರಮದಡಿ     " +apiResponse.getContent().get(0).getCocoonsWeight()+ "    ಕೆ.ಜಿ    "+raceNameWithoutFirstWord+"    ರೇಷ್ಮೆ    ಮೊಟ್ಟೆಗಳಿಗೆ ,  ಪ್ರತಿ  100   ಮೊಟ್ಟೆಗಳಿಗೆ   ಸಹಾಯಧನ   ರೂ. "+Math.round(apiResponse.getContent().get(0).getUnitCost())+ "/-  ರಂತೆ   ಒಟ್ಟು     ರೂ.  "+Math.round(apiResponse.getContent().get(0).getSchemeAmount())+
//                    " /-  ಗಳ    ಸಹಾಯಧನ   ಪಡೆಯಲು     ಅರ್ಜಿ    ಸಲ್ಲಿಸಿದ್ದು ,   ಅರ್ಜಿಯ    ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getArn()+
//                    "   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ     ಸ್ಥಿತಿಯನ್ನು     ತಿಳಿಯಲು    ARN   ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ    ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕದ್ದು .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು \n"+
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,\n"+
                    apiResponse.getContent().get(0).getTscName());

            response.setHeader2("ARN No: " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }




    private JRDataSource getDataSourceReelerAcknowledgement(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataReelerAcknowledgement(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader(" ಸ್ವೀಕೃತಿ   ಪತ್ರ  ( ACKNOWLEDGEMENT LETTER )");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());


            response.setLineItemComment( "              ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getReelerName()+ "(" +apiResponse.getContent().get(0).getFruitsId()+")   ಬಿನ್/ಕೋಂ  "+apiResponse.getContent().get(0).getFatherNameKan()+"" +
                    apiResponse.getContent().get(0).getVillageName()+ "  ,  "+ apiResponse.getContent().get(0).getHobliName()+ " ,  ಹೋಬಳಿ,   " +apiResponse.getContent().get(0).getTalukName()+ "  ತಾ.   " +apiResponse.getContent().get(0).getDistrictName()+ "   ಇವರು   " +apiResponse.getContent().get(0).getCategoryName()+ "   ವರ್ಗಕ್ಕೆ     ಸೇರಿದ್ದು, " +
                    "ರೀಲಿಂಗ್    ರಹದಾರಿ ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getReelingLicenseNumber()+ "   ಅನ್ನು     ಹೊಂದಿದ್ದು   "+apiResponse.getContent().get(0).getReelingShedSqft()+" ಚ.ಅಡಿ   "  +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "   ನಿರ್ಮಾಣಕ್ಕೆ       ಸಹಾಯಧನ "+
                    "ಪಡೆಯಲು    ಅರ್ಜಿ ಯನ್ನು    ಸಲ್ಲಿಸಿದ್ದು,    ಅರ್ಜಿಯ   ಸಂಖ್ಯೆ  : " + apiResponse.getContent().get(0).getArn() +"   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ    ಮುಂದಿನ    ಸ್ಥಿತಿಯನ್ನು  "+
                            "ತಿಳಿಯಲು    ARN    ಸಂಖ್ಯೆಯನ್ನು      ಉಪಯೋಗಿಸತಕ್ಕದ್ದು");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು\n" +
                            "ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ   \n "+apiResponse.getContent().get(0).getLoggedinUserTscName());
            response.setHeader3("           "+ apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ   ಸಾಲಿನಲ್ಲಿ      “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”    "+ apiResponse.getContent().get(0).getCategoryName() +""+
                            "ಅಡಿ   ರೇಷ್ಮೆ    ನೂಲು   ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು    "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಾಣಕ್ಕೆ     ಸಹಾಯಧನ");

            response.setHeader2("ARN No:  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceSilkIncentiveAcknowledgement(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataReelerAcknowledgement(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader(" ಸ್ವೀಕೃತಿ   ಪತ್ರ  ( ACKNOWLEDGEMENT LETTER )");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());

            response.setHeader3("           "+ apiResponse.getContent().get(0).getFinancialYear() + "     ನೇ   ಸಾಲಿನಲ್ಲಿ        “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”      "+ apiResponse.getContent().get(0).getCategoryName() +""+
                    "    ಅಡಿ      ರಾಜ್ಯದಲ್ಲಿ      ರೇಷ್ಮೆ     ನೂಲು    ಬಿಚ್ಚಾಣಿಕೆದಾರರು     ಉತ್ಪಾದಿಸಿದ    ಗುಣಮಟ್ಟದ    ಕಚ್ಚಾ     ರೇಷ್ಮೆ ಗೆ      ಪ್ರೋತ್ಸಾಹಧನ ಕಾರ್ಯಕ್ರಮ");

//            response.setLineItemComment( "           "+ apiResponse.getContent().get(0).getFinancialYear() + "     ನೇ   ಸಾಲಿನಲ್ಲಿ        “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”  ಅಡಿ        ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getReelerName()+ "(" +apiResponse.getContent().get(0).getFruitsId()+")     ಬಿನ್/ಕೋಂ    "+apiResponse.getContent().get(0).getFatherNameKan()+"    " +
//                    "  ಗ್ರಾಮ     "+apiResponse.getContent().get(0).getVillageName()+ ",   ತಾಲ್ಲೂಕು     " +apiResponse.getContent().get(0).getTalukName()+ "    ಇವರು   " +apiResponse.getContent().get(0).getCategoryName()+ "   ವರ್ಗಕ್ಕೆ     ಸೇರಿದ್ದು, " +
//                    "ರೀಲಿಂಗ್    ರಹದಾರಿ ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getReelingLicenseNumber()+ "   ಅನ್ನು     ಹೊಂದಿದ್ದು   "+apiResponse.getContent().get(0).getReelingShedSqft()+" ಚ.ಅಡಿ   "  +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "   ನಿರ್ಮಾಣಕ್ಕೆ       ಸಹಾಯಧನ "+
//                    "ಪಡೆಯಲು    ಅರ್ಜಿ ಯನ್ನು    ಸಲ್ಲಿಸಿದ್ದು,    ಅರ್ಜಿಯ   ಸಂಖ್ಯೆ  : " + apiResponse.getContent().get(0).getArn() +"   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ    ಮುಂದಿನ    ಸ್ಥಿತಿಯನ್ನು  "+
//                    "ತಿಳಿಯಲು    ARN    ಸಂಖ್ಯೆಯನ್ನು      ಉಪಯೋಗಿಸತಕ್ಕದ್ದು");

            response.setLineItemComment("               " + apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ    ಸಾಲಿನಲ್ಲಿ      “" + apiResponse.getContent().get(0).getSchemeNameInKannada() + "” ಅಡಿ    "+
                    "   ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getReelerName()+ "(" +apiResponse.getContent().get(0).getFruitsId()+")    ಬಿನ್/ಕೋಂ   "+apiResponse.getContent().get(0).getFatherNameKan()+  "     "+
                    "   ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getVillageName()+ "  ,ತಾಲೂಕು   "+apiResponse.getContent().get(0).getTalukName()+  "   ರವರು   "+apiResponse.getContent().get(0).getMachineTypeName()+  "    ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ      "+
                    apiResponse.getContent().get(0).getMonth()+ " ರ     ಮಾಹೆಯಲ್ಲಿ     " +apiResponse.getContent().get(0).getMachineQuantity()+ "    ಕೆ.ಜಿ     ಕಚ್ಚಾ     ರೇಷ್ಮೆ ಯನ್ನು      ಉತ್ಪಾದಿಸಿದ್ದು ,      " +
                            "ಪ್ರತಿ    ಕೆ.ಜಿ. ಗೆ    ನಿಗದಿ    ಪಡಿಸಿರುವ     ಒಟ್ಟು      ರೂ.  "+Math.round(apiResponse.getContent().get(0).getSchemeAmount())+"/- ಗಳ   ಪ್ರೋತ್ಸಾಹಧನ ಪಡೆಯಲು   ಅರ್ಜಿಯನ್ನು      ಸಲ್ಲಿಸಿದ್ದು ,   "+
                            "   ಇವರ    ನೋಂದಣಿ    ಸಂಖ್ಯೆ   : "+apiResponse.getContent().get(0).getArn()+"    ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ     ಸ್ಥಿತಿಯನ್ನು    ತಿಳಿಯಲು     ARN    ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ   ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕ್ಕದ್ದು,");
            response.setHeader1("ರೇಷ್ಮೆ    ಸಹಾಯಕ   ನಿರ್ದೇಶಕರು\n"
                    +"ಸರ್ಕಾರೀ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ \n "+
                    apiResponse.getContent().get(0).getLoggedinUserDistrictName());

            response.setHeader2("ARN No:  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceReelerAcknowledgementHRU(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchDataReelerAcknowledgement(requestDto);

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader(" ಸ್ವೀಕೃತಿ   ಪತ್ರ  ( ACKNOWLEDGEMENT LETTER )");
            response.setAcceptedDate("ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());


            response.setLineItemComment( "              ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getReelerName()+ "(" +apiResponse.getContent().get(0).getFruitsId()+")   ಬಿನ್/ಕೋಂ  "+apiResponse.getContent().get(0).getFatherNameKan()+"" +
                    apiResponse.getContent().get(0).getVillageName()+ "  ,  "+ apiResponse.getContent().get(0).getHobliName()+ " ,  ಹೋಬಳಿ,   " +apiResponse.getContent().get(0).getTalukName()+ "  ತಾ.   " +apiResponse.getContent().get(0).getDistrictName()+ "   ಇವರು   " +apiResponse.getContent().get(0).getCategoryName()+ "   ವರ್ಗಕ್ಕೆ     ಸೇರಿದ್ದು, " +
                    "ರೀಲಿಂಗ್    ರಹದಾರಿ ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getReelingLicenseNumber()+ "  ) ರಲ್ಲಿ     "+apiResponse.getContent().get(0).getNumberOfBasins()+"   ಬೇಸಿನ್  "+apiResponse.getContent().get(0).getMachineTypeName()+"    ರೀಲಿಂಗ್      ಘಟಕ    ಹೊಂದಿದ್ದು,    ತಮ್ಮ     ರೀಲಿಂಗ್    ಘಟಕದಲ್ಲಿ    "+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "   ಘಟಕ   ಖರೀದಿಸಿ    ಅಳವಡಿಸುವುದಕ್ಕಾ ಗಿ    ಸಹಾಯಧನ    ಪಡೆಯಲು    ಅರ್ಜಿಯನ್ನು      ಸಲ್ಲಿ ಸಿದ್ದು,"+
                    "     ಅರ್ಜಿಯ   ಸಂಖ್ಯೆ  : " + apiResponse.getContent().get(0).getArn() +"   ಆಗಿರುತ್ತದೆ.    ಅರ್ಜಿಯ    ಸ್ಥಿತಿಯನ್ನು  "+
                    "ತಿಳಿಯಲು    ARN    ಸಂಖ್ಯೆಯನ್ನು    ಮುಂದಿನ   ವಿಚಾರಣೆಗೆ    ಉಪಯೋಗಿಸತಕ್ಕ ದ್ದು .");
            response.setHeader1("ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು\n" +
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ  \n  "+apiResponse.getContent().get(0).getLoggedinUserTscName());
            response.setHeader3("           "+ apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ   ಸಾಲಿನಲ್ಲಿ      “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”    "+ apiResponse.getContent().get(0).getCategoryName() +""+
                    "ಅಡಿ   ರೇಷ್ಮೆ    ನೂಲು   ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು   ತಮ್ಮ     ರೀಲಿಂಗ್   ಘಟಕದಲ್ಲಿ     ಹೀಟ್     ರಿಕವರಿ  ಯೂನಿಟ್    ಅಳವಡಿಕೆಗೆ      ಸಹಾಯಧನ");

            response.setHeader2("ARN No:  " + apiResponse.getContent().get(0).getArn());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }

    public class NumberToWordsConverter {

        private static final String[] units = {
                "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
                "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
                "Seventeen", "Eighteen", "Nineteen"
        };

        private static final String[] tens = {
                "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
        };

        private static String convertLessThanThousand(int number) {
            String word = "";
            if (number % 100 < 20){
                word = units[number % 100];
                number /= 100;
            } else {
                word = units[number % 10];
                number /= 10;

                word = tens[number % 10] + (word.isEmpty() ? "" : " " + word);
                number /= 10;
            }
            if (number == 0) return word;
            return units[number] + " Hundred" + (word.isEmpty() ? "" : " " + word);
        }

        public static String convert(long number) {
            if (number == 0) { return "Zero"; }

            String[] bigUnits = {"", "Thousand", "Million", "Billion"};
            int[] parts = new int[4];
            int partIndex = 0;

            while (number > 0) {
                parts[partIndex++] = (int)(number % 1000);
                number /= 1000;
            }

            StringBuilder words = new StringBuilder();
            for (int i = partIndex - 1; i >= 0; i--) {
                if (parts[i] != 0) {
                    words.append(convertLessThanThousand(parts[i]))
                            .append(" ")
                            .append(bigUnits[i])
                            .append(" ");
                }
            }

            return words.toString().trim();
        }
    }

    public class KannadaNumberToWords {

        private static final String[] units = {
                "", "ಒಂದು", "ಎರಡು", "ಮೂರು", "ನಾಲ್ಕು", "ಐದು", "ಆರು", "ಏಳು", "ಎಂಟು", "ಒಂಬತ್ತು"
        };

        private static final String[] teens = {
                "ಹತ್ತು", "ಹನ್ನೊಂದು", "ಹನ್ನೆರಡು", "ಹದಿಮೂರು", "ಹದಿನಾಲ್ಕು", "ಹದಿನೈದು", "ಹದಿನಾರು",
                "ಹದಿನೇಳು", "ಹದಿನೆಂಟು", "ಹತ್ತೊಂಬತ್ತು"
        };

        private static final String[] tens = {
                "", "", "ಇಪ್ಪತ್ತು", "ಮುವತ್ತು", "ನಲವತ್ತು", "ಐವತ್ತು", "ಅರವತ್ತು", "ಎಪ್ಪತ್ತು", "ಎಂಭತ್ತು", "ತೊಂಬತ್ತು"
        };

        public static String convert(long number) {
            if (number == 0) return "ಸೊನ್ನೆ";

            if (number < 10) return units[(int) number];
            if (number < 20) return teens[(int) (number % 10)];
            if (number < 100) {
                return tens[(int) (number / 10)] + (number % 10 != 0 ? " " + convert(number % 10) : "");
            }
            if (number < 1000) {
                return convert(number / 100) + " ನೂರು" + (number % 100 != 0 ? " " + convert(number % 100) : "");
            }
            if (number < 100000) {
                return convert(number / 1000) + " ಸಾವಿರ" + (number % 1000 != 0 ? " " + convert(number % 1000) : "");
            }
            if (number < 10000000) {
                return convert(number / 100000) + " ಲಕ್ಷ" + (number % 100000 != 0 ? " " + convert(number % 100000) : "");
            }
            return convert(number / 10000000) + " ಕೋಟಿ" + (number % 10000000 != 0 ? " " + convert(number % 10000000) : "");
        }
    }


    private JRDataSource getDataSourceCashReciept(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {

        SeedMarket apiResponse = apiService.fetchDataCashAndMarketReciept(requestDto);
        List<LotDistributeResponse> lotDistributeResponseList = new LinkedList<>();
        LotDistributeResponse response = new LotDistributeResponse();
        if (apiResponse.getContent()!= null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String formattedFromDate = "";
            String formattedToDate = "";

            try {
                // Parse full datetime → then take only LocalDate
                LocalDateTime fromDateTime = LocalDateTime.parse(apiResponse.getContent().get(0).getSpunFromDate(), inputFormatter);
                formattedFromDate = fromDateTime.toLocalDate().format(outputFormatter);
            } catch (Exception e) {
                formattedFromDate = ""; // fallback if parsing fails
            }

            try {
                LocalDateTime toDateTime = LocalDateTime.parse(apiResponse.getContent().get(0).getSpunToDate(), inputFormatter);
                formattedToDate = toDateTime.toLocalDate().format(outputFormatter);
            } catch (Exception e) {
                formattedToDate = ""; // fallback if parsing fails
            }

            DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if timestamp includes time
            DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String formattedMarketAuctionDate = "";
            try {
                LocalDate auctionDate = LocalDate.parse(apiResponse.getContent().get(0).getMarketAuctionDate(), inputFormatter2);
                formattedMarketAuctionDate = auctionDate.format(outputFormatter2);
            } catch (Exception e) {
                formattedMarketAuctionDate = ""; // fallback if parsing fails
            }

            Float amountFloat = apiResponse.getContent().get(0).getSoldAmount();
            long amountLong = amountFloat.longValue();

            String amountInKannada = KannadaNumberToWords.convert(amountLong);
            System.out.println("Amount in Kannada: " + amountInKannada);



            response.setHeader(formattedFromDate  + "   -   " +  formattedToDate  +"   ರಲ್ಲಿ     ಹಣ್ಣಾದ  " + apiResponse.getContent().get(0).getNoOfCocoonPerKg()  +  "   ಸಾವಿರ   ಬೈವೋಲ್ಟಿನ್/" +
                    "  ಮೈಸೂರು    ತಳಿ    ಬಿತ್ತನೆ     ಗೂಡುಗಳನ್ನು      "+ apiResponse.getContent().get(0).getFarmerVillage()  +      "     ಗ್ರಾಮದ   ಬಿತ್ತನೆ   " +
                    "  ಗೂಡು    ಸಾಕಣೆಗೆ    ಅನುಜ್ಞಾ     ಪತ್ರ     ಪಡೆದಿರುವ    ಶ್ರೀ     "+ apiResponse.getContent().get(0).getFarmerFullName()  + "   ರವರಿಂದ    " +
                    apiResponse.getContent().get(0).getNoOfCocoonPerKg() + "    ಗೂಡುಗಳಿಗೆ     ರೂ.   "+ apiResponse.getContent().get(0).getAmount()  + "" +
                    "   ದರದ    ಪ್ರಕಾರ   "+ formattedMarketAuctionDate  + "  ರಂದು     ಕೊಂಡು __________________________ ಲಾಟಿಗೆ   ಉಪಯೋಗಿಸಲು   ಸಂಭಂದಿಸಿದ   ದಾಸ್ತಾನು  " +
                    "  ಪುಸ್ತಕದ   ಪುಟ   _______________________ ರಲ್ಲಿ     " + formattedMarketAuctionDate  + "   ರಂದು  "+
                    "   ದಾಖಲು    ಮಾಡಿಕೊಂಡು   _______________________________  ದ    ಬಿತ್ತನೆ     ಕೋಠಿಗೆ      ಸರಕು   ರವಾನೆ   ಮೂಲಕ    ರವಾನಿಸಲಾಗಿದೆಯೆಂದು    ಪ್ರಮಾಣೀಕರಿಸುತ್ತೇನೆ .\n" +
                    "    \n" +
                    "ಒಟ್ಟು      ಮೊಬಲಗು    " +amountInKannada+ "   ರೂ. ಗಳನ್ನು     " +
                    "   ನಗದು/ಚೆಕ್      ಸಂಖ್ಯೆ     ________________________________ ಕೊಡಲಾಗಿದೆ.");
            response.setHeader1("ಬಿತ್ತನೆ      ಪ್ರಚಾರ    ಶಾಖೆ/ ಕೃಷಿ     ಕ್ಷೇತ್ರ   /ಕೋಠಿಯ    ಅಧಿಕಾರಿ      "+ apiResponse.getContent().get(0).getFarmerFullName()  +
                    apiResponse.getContent().get(0).getFarmerVillage()  + "    ಅವರಿಂದ    ತಾರೀಖು    " +formattedFromDate + "   -   " +  formattedFromDate  +
                    "  ರಲ್ಲಿ       ಹಣ್ಣಾಗಿದ್ದು   ,  ಒಂದು     ಕಿಲೋಗೆ     "+  apiResponse.getContent().get(0).getNoOfCocoonPerKg()  + "    ಸಂಖ್ಯೆಯಲ್ಲಿದ್ದ    " +
                    "  ಬೈವೋಲ್ಟಿನ್ / ಮೈಸೂರು     ತಳಿ     ಬಿತ್ತನೆ     ಗೂಡನ್ನು       ದರ   "+  apiResponse.getContent().get(0).getAmount()  + " ಕ್ಕೆ      ಸರಬರಾಜು   ಮಾಡಿದಕ್ಕಾಗಿ   " +apiResponse.getContent().get(0).getMarketName()  +
                    "   ರಿಂದ   ____________________________________________  ರವರೆಗೆ   ಒಟ್ಟು    ಕಿ.ಮೀ.   ____________________________ ಸಾಗಣೆ    ವೆಚ್ಚ  _____________________________  ಸೇರಿದಂತೆ   " +
                    "ಒಟ್ಟು      ಮೊಬಲಗು    " +amountInKannada + "    ಸ್ವೀಕರಿಸಿದ್ದೇನೆ.\n" +
                    "    \n" +
                    "                        ಅನುಜ್ಞಾ      ಪಾತ್ರ      ಪಡೆದಿರುವ     ಸಾಕಣೆದಾರನ    ಸಹಿ    ಅಥವಾ    ಹೆಬ್ಬೆಟ್ಟಿನ    ಗುರುತು.   ಪಾವತಿ    ಮಾಡಿರುವ    ದರ   ಚಾಲ್ತಿಯಲ್ಲಿರುವ   " +
                    "   ಕೊಳ್ಳುವ      ದರಕ್ಕಿಂತ     ಹೆಚ್ಚಿಲ್ಲವೆಂದೂ     ಮೇಲಾಧಿಕಾರಿಯ    ಮಂಜೂರಾತಿಯನ್ನು    ದಿನಾಂಕ.   ________________________________  ರಂದು _______________________________________" +
                    "ರ  ಸಂಖ್ಯೆಯಲ್ಲಿ       ಪಡೆದ್ದಿದೆನೆಂದೂ     ಹಣಪಾವತಿ    ಪ್ರಮಾಣೀಕರಿಸುತ್ತೇನೆ.");

            response.setHeader3("ಬಿತ್ತನೆ    ಪ್ರಚಾರ   ಶಾಖೆ / ಕೃಷಿ   ಕ್ಷೇತ್ರ \n" +
                    "ಕೋಠಿಯ    ಅಧಿಕಾರಿಯ   ಸಹಿ");
            response.setHeader4("ದಿನಾಂಕ : " + formattedMarketAuctionDate);
//            response.setHeader3("ಬಿತ್ತನೆ  ಪ್ರಚಾರ  ಶಾಖೆ / ಕೃಷಿ  ಕ್ಷೇತ್ರ   ಕೋಠಿಯ ಅಧಿಕಾರಿಯ ಸಹಿ ರುಜು ಮತ್ತು ಹುದ್ದೆ.");

            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            lotDistributeResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(lotDistributeResponseList);
    }

    private JRDataSource getDataSourceMarketReciept(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {

        SeedMarket apiResponse = apiService.fetchDataCashAndMarketReciept(requestDto);
        List<LotDistributeResponse> lotDistributeResponseList = new LinkedList<>();
        LotDistributeResponse response = new LotDistributeResponse();
        if (apiResponse.getContent()!= null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedTestDate = "";

            try {
                LocalDate testDate = LocalDate.parse(apiResponse.getContent().get(0).getTestDate(), inputFormatter);
                formattedTestDate = testDate.format(outputFormatter);
            } catch (Exception e) {
                formattedTestDate = ""; // fallback if parsing fails
            }

            Float amountFloat = apiResponse.getContent().get(0).getSoldAmount();
            long amountLong = amountFloat.longValue();

            String amountInKannada = KannadaNumberToWords.convert(amountLong);
            System.out.println("Amount in Kannada: " + amountInKannada);



            DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if timestamp includes time
            DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String formattedMarketAuctionDate = "";
            try {
                LocalDate auctionDate = LocalDate.parse(apiResponse.getContent().get(0).getMarketAuctionDate(), inputFormatter2);
                formattedMarketAuctionDate = auctionDate.format(outputFormatter2);
            } catch (Exception e) {
                formattedMarketAuctionDate = ""; // fallback if parsing fails
            }

            try {
                LocalDate testDate = LocalDate.parse(apiResponse.getContent().get(0).getTestDate(), inputFormatter);
                formattedTestDate = testDate.format(outputFormatter);
            } catch (Exception e) {
                formattedTestDate = ""; // fallback if parsing fails
            }

            Float amountFloat1 = apiResponse.getContent().get(0).getAmount();
            long amountLong1 = amountFloat1.longValue();

            String amountInKannadas = KannadaNumberToWords.convert(amountLong1);
            System.out.println("Amount in Kannada: " + amountInKannadas);
            response.setHeader(apiResponse.getContent().get(0).getFarmerVillage() + "    ಗ್ರಾಮದ    ಶ್ರೀ    " +apiResponse.getContent().get(0).getFatherNameKan()+"    ಇವರ    ಮಗನಾದ /" +
                    "  ಮಗಳಾದ    ಶ್ರೀ  /ಶ್ರೀಮತಿ    " + apiResponse.getContent().get(0).getFarmerFullName() + "    ಇವರಿಂದ     ನೂಲು   ಬಿಚ್ಚುವ /" +
                    "    ರೇಷ್ಮೆ     ಗೂಡುಗಳ     ಮಾರಾಟ    ನಿಮಿತ್ತವಾಗಿ     ರೂ .   " + String.format("%.2f", apiResponse.getContent().get(0).getAmount()) +
                    "     ರೂ .  ( ಅಕ್ಷರಗಳಲ್ಲಿ  )   "+ amountInKannadas + "    ಇದರಿಂದ   " + String.format("%.2f", apiResponse.getContent().get(0).getLotWeight()) +
                    "     ಕೆ.ಜಿ.   " + String.format("%.2f", apiResponse.getContent().get(0).getMarketFee()) + "    ರೂಪಾಯಿ)    ಮಾತ್ರ       ಮಾರುಕಟ್ಟೆ       ಶುಲ್ಕವನ್ನು     " +
                    "  ಪಡೆಯಲಾಗಿದೆ  .    ಈ    ಸರಕಿನ     ಒಟ್ಟು       ಮೌಲ್ಯ      " + String.format("%.2f", apiResponse.getContent().get(0).getSoldAmount()) + "   ರೂಪಾಯಿಗಳು");

            response.setHeader3("ಬಿತ್ತನೆ   ಪ್ರಚಾರ   ಶಾಖೆ / ಕೃಷಿ    ಕ್ಷೇತ್ರ    " +
                    "ಕೋಠಿಯ   ಅಧಿಕಾರಿಯ    ಸಹಿ");
            response.setHeader2("ರೇಷ್ಮೆ     ಸಹಾಯಕ   ನಿರ್ದೇಶಕರು\n" +
                    "ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ     ಗೂಡಿನ   ಮಾರುಕಟ್ಟೆ     " +
                    apiResponse.getContent().get(0).getMarketName());
            response.setHeader4("ದಿನಾಂಕ : " + formattedMarketAuctionDate);

            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            lotDistributeResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(lotDistributeResponseList);
    }

    private String formatDate(String dateStr, String inputPattern) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(inputPattern))
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "";
        }
    }

    private String formatDate1(String date) {
        try {
            return (date == null || date.isEmpty())
                    ? ""
                    : LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "";
        }
    }


    private JRBeanCollectionDataSource getDataSourceForInvoice(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        SeedMarket apiResponse = apiService.fetchDataFromInvoice(requestDto);
        List<LotDistributeResponse> lotDistributeResponseList = new LinkedList<>();

        double totalLotWeight = 0.0;
        double totalTotalNumber = 0.0;
        double totalSoldAmount = 0.0;

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        LotDistributeResponse response = new LotDistributeResponse();
        lotDistributeResponseList.add(response);

        int serialNo = 1;
        for (LotDistributeResponse lotDistributeResponse : apiResponse.getContent()) {


            String fullName = lotDistributeResponse.getFarmerFullName();
            String fruitsId = lotDistributeResponse.getFarmerFruitsId();

            if (fullName == null) fullName = "";
            if (fruitsId == null) fruitsId = "";

            lotDistributeResponse.setFarmerFullName(fullName + " " + fruitsId);

            if (lotDistributeResponse.getRace() == null) {
                lotDistributeResponse.setRace("");
            }
            if (lotDistributeResponse.getNoOfCocoonPerKg() == null) {
                lotDistributeResponse.setNoOfCocoonPerKg(0L);
            }

            // ✅ DEFINE VALUES PROPERLY
            double lotWeight = lotDistributeResponse.getLotWeight() == null
                    ? 0.0 : lotDistributeResponse.getLotWeight();

            double totalNumber = lotDistributeResponse.getTotalNumber() == null
                    ? 0.0 : lotDistributeResponse.getTotalNumber();

            double soldAmount = lotDistributeResponse.getSoldAmount() == null
                    ? 0.0 : lotDistributeResponse.getSoldAmount();

            // ✅ SUM
            totalLotWeight += lotWeight;
            totalTotalNumber += totalNumber;
            totalSoldAmount += soldAmount;

            // ✅ FORMAT
            lotDistributeResponse.setLotWeightStr(df.format(lotWeight));
            lotDistributeResponse.setTotalNumberStr(df.format(totalNumber));
            lotDistributeResponse.setSoldAmountStr(df.format(soldAmount));
            lotDistributeResponse.setAmountStr(
                    lotDistributeResponse.getAmount() == null
                            ? "0.00"
                            : df.format(lotDistributeResponse.getAmount())
            );

            lotDistributeResponse.setSpunFromDate(formatDate1(lotDistributeResponse.getSpunFromDate()));
            lotDistributeResponse.setSpunToDate(formatDate1(lotDistributeResponse.getSpunToDate()));

            if (lotDistributeResponse.getInvoiceNumber() == null) {
                lotDistributeResponse.setInvoiceNumber("");
            }

            lotDistributeResponse.setSerialNumber(serialNo++);
            lotDistributeResponseList.add(lotDistributeResponse);
        }

        LotDistributeResponse totalRow = new LotDistributeResponse();
        totalRow.setFarmerFullName("ಒಟ್ಟು");
        totalRow.setLotWeightStr(df.format(totalLotWeight));
        totalRow.setTotalNumberStr(df.format(totalTotalNumber));
        totalRow.setSoldAmountStr(df.format(totalSoldAmount));
        totalRow.setAmountStr("");
//        totalRow.setSerialNumber(null);

        lotDistributeResponseList.add(totalRow);

        DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if timestamp includes time
        DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String formattedMarketAuctionDate = "";
        try {
            LocalDate auctionDate = LocalDate.parse(apiResponse.getContent().get(0).getMarketAuctionDate(), inputFormatter2);
            formattedMarketAuctionDate = auctionDate.format(outputFormatter2);
        } catch (Exception e) {
            formattedMarketAuctionDate = "";
        }
        response.setHeader2("ರವರ    ಕಛೆರಿ\n" +
                "ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ \n"+
                apiResponse.getContent().get(0).getMarketName() + "\n"+
                "ತಾರೀಖು     " + formattedMarketAuctionDate);

        response.setHeader1("ಗೆ,                \n" +
                "ಶ್ರೀ    " + apiResponse.getContent().get(0).getBuyerName() +"\n"+
                "__________________________");
        response.setHeader3("ರುಜು ___________________________                                              ರುಜು ___________________________ \n" +
                "ಹುದ್ದೆಯ ಹೆಸರು ______________________________                         ಹುದ್ದೆಯ ಹೆಸರು ______________________________ ");
        response.setHeader("ಸ್ಥಳ         : ______________________________________\n"+
                "ದಿನಾಂಕ  : ______________________________________");
        response.setHeader4("ಪೀಠಿಕೆ: ");
        response.setInvoiceNumber(" No : " + apiResponse.getContent().get(0).getInvoiceNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        return new JRBeanCollectionDataSource(lotDistributeResponseList);

    }


    private JRBeanCollectionDataSource getDataSourceForInvoice2(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        SeedMarket apiResponse = apiService.fetchDataFromInvoice(requestDto);
        List<LotDistributeResponse> lotDistributeResponseList = new LinkedList<>();
        double totalLotWeight = 0.0;
        double totalTotalNumber = 0.0;
        double totalSoldAmount = 0.0;

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        LotDistributeResponse response = new LotDistributeResponse();

        int serialNo = 1;
        for (LotDistributeResponse lotDistributeResponse : apiResponse.getContent()) {


            String fullName = lotDistributeResponse.getFarmerFullName();
            String fruitsId = lotDistributeResponse.getFarmerFruitsId();

            if (fullName == null) fullName = "";
            if (fruitsId == null) fruitsId = "";

            lotDistributeResponse.setFarmerFullName(fullName + " " + fruitsId);

            if (lotDistributeResponse.getRace() == null) {
                lotDistributeResponse.setRace("");
            }
            if (lotDistributeResponse.getNoOfCocoonPerKg() == null) {
                lotDistributeResponse.setNoOfCocoonPerKg(0L);
            }

            // ✅ DEFINE VALUES PROPERLY
            double lotWeight = lotDistributeResponse.getLotWeight() == null
                    ? 0.0 : lotDistributeResponse.getLotWeight();

            double totalNumber = lotDistributeResponse.getTotalNumber() == null
                    ? 0.0 : lotDistributeResponse.getTotalNumber();

            double soldAmount = lotDistributeResponse.getSoldAmount() == null
                    ? 0.0 : lotDistributeResponse.getSoldAmount();

            // ✅ SUM
            totalLotWeight += lotWeight;
            totalTotalNumber += totalNumber;
            totalSoldAmount += soldAmount;

            // ✅ FORMAT
            lotDistributeResponse.setLotWeightStr(df.format(lotWeight));
            lotDistributeResponse.setTotalNumberStr(df.format(totalNumber));
            lotDistributeResponse.setSoldAmountStr(df.format(soldAmount));
            lotDistributeResponse.setAmountStr(
                    lotDistributeResponse.getAmount() == null
                            ? "0.00"
                            : df.format(lotDistributeResponse.getAmount())
            );

            lotDistributeResponse.setSpunFromDate(formatDate1(lotDistributeResponse.getSpunFromDate()));
            lotDistributeResponse.setSpunToDate(formatDate1(lotDistributeResponse.getSpunToDate()));

            if (lotDistributeResponse.getInvoiceNumber() == null) {
                lotDistributeResponse.setInvoiceNumber("");
            }

            lotDistributeResponse.setSerialNumber(serialNo++);
            lotDistributeResponseList.add(lotDistributeResponse);
        }

        LotDistributeResponse totalRow = new LotDistributeResponse();
        totalRow.setFarmerFullName("ಒಟ್ಟು");
        totalRow.setLotWeightStr(df.format(totalLotWeight));
        totalRow.setTotalNumberStr(df.format(totalTotalNumber));
        totalRow.setSoldAmountStr(df.format(totalSoldAmount));
        totalRow.setAmountStr("");
//        totalRow.setSerialNumber(null);

        lotDistributeResponseList.add(totalRow);

        DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if timestamp includes time
        DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String formattedMarketAuctionDate = "";
        try {
            LocalDate auctionDate = LocalDate.parse(apiResponse.getContent().get(0).getMarketAuctionDate(), inputFormatter2);
            formattedMarketAuctionDate = auctionDate.format(outputFormatter2);
        } catch (Exception e) {
            formattedMarketAuctionDate = "";
        }
        response.setHeader2("ರವರ    ಕಛೆರಿ\n" +
                "ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ \n"+
                apiResponse.getContent().get(0).getMarketName() + "\n"+
                "ತಾರೀಖು     " + formattedMarketAuctionDate);

        response.setHeader1("ಗೆ,                \n" +
                "ಶ್ರೀ    " + apiResponse.getContent().get(0).getBuyerName() +"\n"+
                "__________________________");
        response.setHeader3("ರುಜು ___________________________                                              ರುಜು ___________________________ \n" +
                "ಹುದ್ದೆಯ ಹೆಸರು ______________________________                         ಹುದ್ದೆಯ ಹೆಸರು ______________________________ ");
        response.setHeader("ಸ್ಥಳ         : ______________________________________\n"+
                "ದಿನಾಂಕ  : ______________________________________");
        response.setHeader4("ಪೀಠಿಕೆ: ");
        response.setInvoiceNumber(" No : " + apiResponse.getContent().get(0).getInvoiceNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        return new JRBeanCollectionDataSource(lotDistributeResponseList);

    }


    private JRBeanCollectionDataSource getDataSourceForInvoice3(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {
        SeedMarket apiResponse = apiService.fetchDataFromInvoice(requestDto);
        List<LotDistributeResponse> lotDistributeResponseList = new LinkedList<>();
        double totalLotWeight = 0.0;
        double totalTotalNumber = 0.0;
        double totalSoldAmount = 0.0;

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        LotDistributeResponse response = new LotDistributeResponse();

        int serialNo = 1;
        for (LotDistributeResponse lotDistributeResponse : apiResponse.getContent()) {


            String fullName = lotDistributeResponse.getFarmerFullName();
            String fruitsId = lotDistributeResponse.getFarmerFruitsId();

            if (fullName == null) fullName = "";
            if (fruitsId == null) fruitsId = "";

            lotDistributeResponse.setFarmerFullName(fullName + " " + fruitsId);

            if (lotDistributeResponse.getRace() == null) {
                lotDistributeResponse.setRace("");
            }
            if (lotDistributeResponse.getNoOfCocoonPerKg() == null) {
                lotDistributeResponse.setNoOfCocoonPerKg(0L);
            }

            // ✅ DEFINE VALUES PROPERLY
            double lotWeight = lotDistributeResponse.getLotWeight() == null
                    ? 0.0 : lotDistributeResponse.getLotWeight();

            double totalNumber = lotDistributeResponse.getTotalNumber() == null
                    ? 0.0 : lotDistributeResponse.getTotalNumber();

            double soldAmount = lotDistributeResponse.getSoldAmount() == null
                    ? 0.0 : lotDistributeResponse.getSoldAmount();

            // ✅ SUM
            totalLotWeight += lotWeight;
            totalTotalNumber += totalNumber;
            totalSoldAmount += soldAmount;

            // ✅ FORMAT
            lotDistributeResponse.setLotWeightStr(df.format(lotWeight));
            lotDistributeResponse.setTotalNumberStr(df.format(totalNumber));
            lotDistributeResponse.setSoldAmountStr(df.format(soldAmount));
            lotDistributeResponse.setAmountStr(
                    lotDistributeResponse.getAmount() == null
                            ? "0.00"
                            : df.format(lotDistributeResponse.getAmount())
            );

            lotDistributeResponse.setSpunFromDate(formatDate1(lotDistributeResponse.getSpunFromDate()));
            lotDistributeResponse.setSpunToDate(formatDate1(lotDistributeResponse.getSpunToDate()));

            if (lotDistributeResponse.getInvoiceNumber() == null) {
                lotDistributeResponse.setInvoiceNumber("");
            }

            lotDistributeResponse.setSerialNumber(serialNo++);
            lotDistributeResponseList.add(lotDistributeResponse);
        }

        LotDistributeResponse totalRow = new LotDistributeResponse();
        totalRow.setFarmerFullName("ಒಟ್ಟು");
        totalRow.setLotWeightStr(df.format(totalLotWeight));
        totalRow.setTotalNumberStr(df.format(totalTotalNumber));
        totalRow.setSoldAmountStr(df.format(totalSoldAmount));
        totalRow.setAmountStr("");
//        totalRow.setSerialNumber(null);

        lotDistributeResponseList.add(totalRow);

        DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if timestamp includes time
        DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String formattedMarketAuctionDate = "";
        try {
            LocalDate auctionDate = LocalDate.parse(apiResponse.getContent().get(0).getMarketAuctionDate(), inputFormatter2);
            formattedMarketAuctionDate = auctionDate.format(outputFormatter2);
        } catch (Exception e) {
            formattedMarketAuctionDate = "";
        }
        response.setHeader2("ರವರ    ಕಛೆರಿ\n" +
                "ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ \n"+
                apiResponse.getContent().get(0).getMarketName() + "\n"+
                "ತಾರೀಖು     " + formattedMarketAuctionDate);

        response.setHeader1("ಗೆ,                \n" +
                "ಶ್ರೀ    " + apiResponse.getContent().get(0).getBuyerName() +"\n"+
                "__________________________");
        response.setHeader3("ರುಜು ___________________________                                              ರುಜು ___________________________ \n" +
                "ಹುದ್ದೆಯ ಹೆಸರು ______________________________                         ಹುದ್ದೆಯ ಹೆಸರು ______________________________ ");
        response.setHeader("ಸ್ಥಳ         : ______________________________________\n"+
                "ದಿನಾಂಕ  : ______________________________________");
        response.setHeader4("ಪೀಠಿಕೆ: ");
        response.setInvoiceNumber(" No : " + apiResponse.getContent().get(0).getInvoiceNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        return new JRBeanCollectionDataSource(lotDistributeResponseList);

    }

    private Float roundTwoDecimals(Float value) {
        return Float.parseFloat(String.format("%.2f", value));
    }




    private JRBeanCollectionDataSource getDataSourceForPermit(
            LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {

        SeedMarket apiResponse = apiService.fetchDataFromPermit(requestDto);
        List<LotDistributeResponse> list = new LinkedList<>();

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        double totalLotWeightSum = 0.0;
        double totalSoldOutAmountSum = 0.0;
        double totalNumberSum = 0.0;
        double totalNoOfCocoonPerKg = 0.0;

        int serialNo = 1;

        // ✅ HEADER ROW
        LotDistributeResponse headerRow = new LotDistributeResponse();
        headerRow.setTotalRow(false);
        list.add(headerRow);

        // ✅ DATA ROWS
        if (apiResponse.getContent() != null && !apiResponse.getContent().isEmpty()) {

            for (LotDistributeResponse row : apiResponse.getContent()) {

                row.setFarmerFullName(
                        (row.getFarmerFullName() == null ? "" : row.getFarmerFullName()) + " " +
                                (row.getFarmerFruitsId() == null ? "" : row.getFarmerFruitsId())
                );

                double lotWeight = row.getTotalLotWeight() == null ? 0.0 : row.getTotalLotWeight();
                double soldAmount = row.getTotalSoldOutAmount() == null ? 0.0 : row.getTotalSoldOutAmount();
                double amount = row.getAmount() == null ? 0.0 : row.getAmount();
                double totalNumber = row.getTotalNumber() == null ? 0.0 : row.getTotalNumber();
                double noOfCocoonPerKg = row.getNoOfCocoonPerKg() == null ? 0.0 : row.getNoOfCocoonPerKg();

                // ✅ TOTALS
                totalLotWeightSum += lotWeight;
                totalSoldOutAmountSum += soldAmount;
                totalNumberSum += totalNumber;
                totalNoOfCocoonPerKg += noOfCocoonPerKg;

                // ✅ DISPLAY
                row.setLotWeightStr(df.format(lotWeight));
                row.setTotalNumberStr(df.format(totalNumber));
                row.setAmountStr(df.format(amount));
                row.setSoldAmountStr(df.format(soldAmount));

                row.setSerialNumber(serialNo++);
                row.setTotalRow(false);

                list.add(row);
            }
        }

        // ✅ TOTAL ROW
        LotDistributeResponse totalRow = new LotDistributeResponse();
        totalRow.setTotalRow(true);
        totalRow.setSerialNumber(null);
        totalRow.setFarmerFullName("ಒಟ್ಟು");
        totalRow.setFarmerFruitsId("");
        totalRow.setLotWeightStr(df.format(totalLotWeightSum));
        totalRow.setTotalNumberStr(df.format(totalNumberSum));
        totalRow.setSoldAmountStr(df.format(totalSoldOutAmountSum));

        totalRow.setAmountStr("");
        totalRow.setNoOfCocoonPerKg(null);

        list.add(totalRow);

        // ✅ DATE FORMATTING
        String formattedToDate =
                formatDate(apiResponse.getContent().get(0).getSpunToDate(), "yyyy-MM-dd");

        String formattedFromDate =
                formatDate(apiResponse.getContent().get(0).getSpunFromDate(), "yyyy-MM-dd");

        String formattedMarketAuctionDate =
                formatDate(apiResponse.getContent().get(0).getMarketAuctionDate(), "yyyy-MM-dd");


        // ✅ HEADER TEXT
        try {
            headerRow.setHeader("ಶ್ರೀ    " + apiResponse.getContent().get(0).getBuyerName() +"   ಖಾಸಗಿ    ಬಿತ್ತನೆದಾರರು    ಈ   ದಿನ    ಮಾರುಕಟ್ಟೆಯಿಂದ    " +
                            formattedFromDate + " - " + formattedToDate + "    ದಿನಾಂಕದಲ್ಲಿ      ಗೂಡು     ಕಟ್ಟಿದ     " + +  Math.round(totalLotWeightSum) +   "    ಕೆ.ಜಿ     ಒಟ್ಟು     ಸಂಖ್ಯೆ      "+Math.round(totalNumberSum)+
                            "    ಮೈಸೂರು    ಬಿತ್ತನೆ    ಗೂಡುಗಳನ್ನು     ಖರೀದಿಸಿರುತ್ತಾರೆ.    ಮೇಲ್ಕಂಡ    ಬಿತ್ತನೆ    ಗೂಡುಗಳನ್ನು      " + apiResponse.getContent().get(0).getMarketName() +
                            "    ಇಂದ    " + apiResponse.getContent().get(0).getRspAddress() + "    ಇಲ್ಲಿಗೆ    ಸಾಗಿಸಲು    ಅನುಮತಿ    ನೀಡಲಾಗಿದೆ.    ಈ    ಪರ್ಮಿಟ್ಟಿನ    ಅವಧಿ    " + formattedMarketAuctionDate);

            headerRow.setHeader1("ದಿನಾಂಕ : " + formattedMarketAuctionDate);

            headerRow.setHeader2(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು\n" +
                            "ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ\n" +
                            apiResponse.getContent().get(0).getMarketName()
            );

            headerRow.setHeader3("ರಹದಾರಿ   ಸಂಖ್ಯೆ   :  " +
                    apiResponse.getContent().get(0).getLicenseNo());

        } catch (Exception ignored) {}

        headerRow.setLogurl("/reports/Seal_of_Karnataka.PNG");

        return new JRBeanCollectionDataSource(list);
    }

    private JRBeanCollectionDataSource getDataSourceForPermit2(LotStatusSeedMarketRequest requestDto) throws JsonProcessingException {

        SeedMarket apiResponse = apiService.fetchDataFromPermit(requestDto);
        List<LotDistributeResponse> list = new LinkedList<>();

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        double totalLotWeightSum = 0.0;
        double totalSoldOutAmountSum = 0.0;
        double totalNumberSum = 0.0;
        double totalNoOfCocoonPerKg = 0.0;

        int serialNo = 1;

        LotDistributeResponse headerRow = new LotDistributeResponse();
        headerRow.setTotalRow(false);


        // ✅ DATA ROWS
        if (apiResponse.getContent() != null && !apiResponse.getContent().isEmpty()) {

            for (LotDistributeResponse row : apiResponse.getContent()) {

                row.setFarmerFullName(
                        (row.getFarmerFullName() == null ? "" : row.getFarmerFullName()) + " " +
                                (row.getFarmerFruitsId() == null ? "" : row.getFarmerFruitsId())
                );

                double lotWeight = row.getTotalLotWeight() == null ? 0.0 : row.getTotalLotWeight();
                double soldAmount = row.getTotalSoldOutAmount() == null ? 0.0 : row.getTotalSoldOutAmount();
                double amount = row.getAmount() == null ? 0.0 : row.getAmount();
                double totalNumber = row.getTotalNumber() == null ? 0.0 : row.getTotalNumber();
                double noOfCocoonPerKg = row.getNoOfCocoonPerKg() == null ? 0.0 : row.getNoOfCocoonPerKg();

                totalLotWeightSum += lotWeight;
                totalSoldOutAmountSum += soldAmount;
                totalNumberSum += totalNumber;

                // ✅ DISPLAY
                row.setLotWeightStr(df.format(lotWeight));
                row.setTotalNumberStr(df.format(totalNumber));
                row.setAmountStr(df.format(amount));
                row.setSoldAmountStr(df.format(soldAmount));

                row.setSerialNumber(serialNo++);
                row.setTotalRow(false);

                list.add(row);
            }
        }

        // ✅ TOTAL ROW
        LotDistributeResponse totalRow = new LotDistributeResponse();
        totalRow.setTotalRow(true);
        totalRow.setSerialNumber(null);
        totalRow.setFarmerFullName("ಒಟ್ಟು");
        totalRow.setFarmerFruitsId("");
        totalRow.setLotWeightStr(df.format(totalLotWeightSum));
        totalRow.setTotalNumberStr(df.format(totalNumberSum));
        totalRow.setSoldAmountStr(df.format(totalSoldOutAmountSum));
        totalRow.setAmountStr("");
        totalRow.setNoOfCocoonPerKg(null);

        list.add(totalRow);

        // ✅ DATE FORMATTING
        String formattedToDate =
                formatDate(apiResponse.getContent().get(0).getSpunToDate(), "yyyy-MM-dd");

        String formattedFromDate =
                formatDate(apiResponse.getContent().get(0).getSpunFromDate(), "yyyy-MM-dd");

        String formattedMarketAuctionDate =
                formatDate(apiResponse.getContent().get(0).getMarketAuctionDate(), "yyyy-MM-dd");


        // ✅ HEADER TEXT
        try {
            headerRow.setHeader("ಶ್ರೀ    " + apiResponse.getContent().get(0).getBuyerName() +"   ಖಾಸಗಿ    ಬಿತ್ತನೆದಾರರು    ಈ   ದಿನ    ಮಾರುಕಟ್ಟೆಯಿಂದ    " +
                    formattedFromDate + " - " + formattedToDate + "    ದಿನಾಂಕದಲ್ಲಿ      ಗೂಡು     ಕಟ್ಟಿದ     " +  Math.round(totalLotWeightSum) +   "    ಕೆ.ಜಿ     ಒಟ್ಟು     ಸಂಖ್ಯೆ      "+Math.round(totalNumberSum)+
                    "    ಮೈಸೂರು    ಬಿತ್ತನೆ    ಗೂಡುಗಳನ್ನು     ಖರೀದಿಸಿರುತ್ತಾರೆ.    ಮೇಲ್ಕಂಡ    ಬಿತ್ತನೆ    ಗೂಡುಗಳನ್ನು      " + apiResponse.getContent().get(0).getMarketName() +
                    "    ಇಂದ    " + apiResponse.getContent().get(0).getRspAddress() + "    ಇಲ್ಲಿಗೆ    ಸಾಗಿಸಲು    ಅನುಮತಿ    ನೀಡಲಾಗಿದೆ.    ಈ    ಪರ್ಮಿಟ್ಟಿನ   ಅವಧಿ    " + formattedMarketAuctionDate);

            headerRow.setHeader1("ದಿನಾಂಕ : " + formattedMarketAuctionDate);

            headerRow.setHeader2(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು\n" +
                            "ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ\n" +
                            apiResponse.getContent().get(0).getMarketName()
            );

            headerRow.setHeader3("ರಹದಾರಿ   ಸಂಖ್ಯೆ   :  " +
                    apiResponse.getContent().get(0).getLicenseNo());

        } catch (Exception ignored) {}

        headerRow.setLogurl("/reports/Seal_of_Karnataka.PNG");

        return new JRBeanCollectionDataSource(list);
    }

    public static String getKannadaInitialsFromSentence(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // ✅ Normalize Kannada text (CRITICAL for PDF)
        text = Normalizer.normalize(text, Normalizer.Form.NFC);

        StringBuilder result = new StringBuilder();
        String[] words = text.trim().split("\\s+");

        for (String word : words) {
            if (!word.isBlank()) {
                result.append(getFirstKannadaAkshara(word));
            }
        }

        return result.toString(); // ✅ NO SPACES
    }

    private static String getFirstKannadaAkshara(String word) {
        // ✅ Normalize each word
        word = Normalizer.normalize(word, Normalizer.Form.NFC);

        BreakIterator it = BreakIterator.getCharacterInstance(Locale.ROOT);
        it.setText(word);

        int start = it.first();
        int end = it.next();

        if (end != BreakIterator.DONE) {
            return word.substring(start, end); // ✅ FULL AKSHARA (ಸ್ಥಿ, ಕ್ಷ, ಣ್ಯ)
        }
        return "";
    }


    public static String removeFirstWord(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] words = text.trim().split("\\s+");

        // If only one word exists, return empty
        if (words.length <= 1) {
            return "";
        }

        // Join from 2nd word onwards
        return String.join(" ", Arrays.copyOfRange(words, 1, words.length));
    }






    private JRBeanCollectionDataSource getDataSourceForBonus225(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSeedCocoon(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // ✅ Format date fields safely
        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);


        // ✅ NEW: formatting helpers for amounts and weights
// CHANGE: Round perKgRate for all usages (paragraph + table)
        long roundedPerKgRate = Math.round(apiResponse.getContent().get(0).getPerKgRate());
        String perKgRateText = String.valueOf(roundedPerKgRate);

// CHANGE: Round totalSchemeAmount for paragraph
        long roundedTotalSchemeAmount = Math.round(apiResponse.getContent().get(0).getTotalSchemeAmount());
        String totalSchemeAmountText = String.valueOf(roundedTotalSchemeAmount);


        String raceName = apiResponse.getContent().get(0).getRaceName();
        String raceNameWithoutFirstWord = removeFirstWord(raceName);





        // ✅ Clean formatted date for sanction order
        // ✅ Clean formatted date for sanction order
        String formattedDate1 = "";
        try {
            // Example input: Wed Nov 19 05:30:00 IST 2025
            String inputDate = apiResponse.getContent().get(0).getCurrentDate().toString();

            // Input format (Java default Date.toString())
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            // Output format → what you want
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = inputFormat.parse(inputDate);
            formattedDate1 = outputFormat.format(date);

        } catch (Exception e) {
            // fallback
            formattedDate1 = String.valueOf(apiResponse.getContent().get(0).getCurrentDate());
        }

        String shortRace = getKannadaShortForm(apiResponse.getContent().get(0).getRaceName());


        // ✅ Clean formatted date for sanction order
// ✅ Clean formatted date for sanction order
        String formattedDate = "";
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

            // Parse input format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            // Define output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Convert and format
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);

        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
        }


        String schemeNameKannada =
                apiResponse.getContent().get(0).getSchemeNameInKannada();

        String schemeInitials =
                getKannadaInitialsFromSentence(schemeNameKannada);



        response.setHeader("ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರ   ಕಛೇರಿ,  ರೇಷ್ಮೆ   ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ ,  " +apiResponse.getContent().get(0).getUserMarket() + "  ಇವರ  ಕಛೇರಿ  ನಡವಳಿಗಳು");
        response.setHeader2("ವಿಷಯ: ");
        response.setHeader3("ಉಲ್ಲೇಖ: ");
        response.setHeader4("ಪೀಠಿಕೆ:-");

        response.setHeader1(  apiResponse.getContent().get(0).getFinancialYear() +  "   ನೇ   ಸಾಲಿನಲ್ಲಿ      "+apiResponse.getContent().get(0).getSchemeNameInKannada() +"  ದಡಿ     "+raceNameWithoutFirstWord+"    ಬಿತ್ತನೆ    ಪ್ರದೇಶದಲ್ಲಿ     ಉತ್ಪಾದನೆಯಾಗುವ     "+apiResponse.getContent().get(0).getRaceName()+"    ತಳಿ   ಬಿತ್ತ ನೆ      ಗೂಡು      ಬಿತ್ತ ನೆಗೆ      ಯೋಗ್ಯ ವಾಗಿದ್ದು ,     ಬೇಡಿಕೆ      ಇಲ್ಲ ದೆ      ನೂಲು    ಬಿಚ್ಚಾ ಣಿಕೆಗೆ     ವಿಲೇವಾರಿಯಾದ    ಬಿತ್ತನೆ    ಗೂಡಿಗೆ    ಬೋನಸ್    ಮೊತ್ತ ವನ್ನು     " +
                "ಮಂಜೂರಾತಿ     ನೀಡುವ     ಬಗ್ಗೆ.\n\n"+

                "1) ಸರ್ಕಾರದ    ಆದೇಶ    ಸಂಖ್ಯೆ  : " +apiResponse.getContent().get(0).getAdmGovtOrder() + "  ದಿನಾಂಕ :  " + admGovtDate  + "\n" +
                "2) ರೇಷ್ಮೆ    ಕೃ ಷಿ     ಆಭಿವೃ ದ್ಧಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು , ಬೆ೦ಗಳೂರು ರವರ    ಸುತ್ತೋ ಲೆ    ಸಂಖ್ಯೆ  : \n" +
                "     " + apiResponse.getContent().get(0).getSchemeCircularNo() + "   ದಿನಾಂಕ :  " +schemeCircularDate  + " \n" +
                "3) ಸರ್ಕಾರದ    ಆದೇಶ    ಸ೦ಖ್ಯೆ  :  " +apiResponse.getContent().get(0).getDeptDeleNo() + "    ದಿನಾಂಕ : " +deptDeleDate  + "\n" +
                "4) ರೇಷ್ಮೆ    ಉಪ ನಿರ್ದೇಶಕರು,    " +apiResponse.getContent().get(0).getLoggedinUserTalukName() + "  ರವರ   ಜ್ಞಾಪನ    ಪತ್ರದ   ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getSReleaseNo() + "\n" +
                "     ದಿನಾಂಕ : " +sReleaseDate + " \n\n" +

                "                 "+apiResponse.getContent().get(0).getFinancialYear() +"    ನೇ   ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ ಇಲಾಖೆಯ    ವಿವಿಧ     ಕಾರ್ಯಕ್ರ ಮಗಳ     ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ     ವಿವಿಧ    ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆಗಳಡಿ    ಉಲ್ಲೇಖ(1)ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮ ಕ    ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು ,   ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ " +
                apiResponse.getContent().get(0).getSchemeNameInKannada() +" ದಡಿ   "+raceNameWithoutFirstWord+"     ಬಿತ್ತ ನೆ      ಪ್ರ ದೇಶದಲ್ಲಿ     ಉತ್ಪಾ ದನೆಯಾಗುವ     "+apiResponse.getContent().get(0).getRaceName() +
                "   ತಳಿ     ಬಿತ್ತ ನೆ     ಗೂಡು     ಬಿತ್ತ ನೆಗೆ     ಯೋಗ್ಯ ವಾಗಿದ್ದು,     ಬೇಡಿಕೆ     ಇಲ್ಲ ದೆ    ನೂಲು     ಬಿಚ್ಚಾ ಣಿಕೆಗೆ    ವಿಲೇವಾರಿಯಾದ    ಬಿತ್ತ ನೆ    ಗೂಡಿಗೆ     ಪ್ರ ತಿ     ಕೆ.ಜಿ.ಗೆ     ರೂ. " +perKgRateText +"/-   ಗಳ    ʼಬೋನಸ್    ಹಣ    ಕಾರ್ಯಕ್ರ ಮದ     ಅನುಷ್ಟಾನಕ್ಕಾಗಿ    ಮಾರ್ಗಸೂಚಿಯನ್ನು     ನೀಡಲಾಗಿದೆ.\n "+

                "                  ಬಿತ್ತ ನೆ     ಪ್ರ ದೇ ಶದಲ್ಲಿ       ನಿರಂತರ     ರೇಷ್ಮೆ   ಬಿತ್ತ ನೆ      ಗೂಡುಗಳನ್ನು      ಉತ್ಪಾ ದಿಸಿ   ಸರಬರಾಜು    ಮಾಡುವ    ಉದ್ದೇಶದಿಂದ    ಸರ್ಕಾರಿ      ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ ,  "+apiResponse.getContent().get(0).getUserMarket() +"    ಇಲ್ಲಿ     ವಹಿವಾಟು     ಮಾಡಿದ   "+apiResponse.getContent().get(0).getRaceName() +"   ತಳಿ     ಬಿತ್ತ ನೆ      ಗೂಡು     ಬಿತ್ತ ನೆಗೆ      ಯೋಗ್ಯ ವಾಗಿದ್ದು,    ಬೇಡಿಕೆ     ಇಲ್ಲ ದೆ      ನೂಲು     ಬಿಚ್ಚಾ ಣಿಕೆಗೆ     ವಿಲೇವಾರಿಯಾದ     ಬಿತ್ತ ನೆ     ಗೂಡಿನ     ವಿವರಗಳು    ಈ    ಕೆಳಕಂಡಂತಿದೆ. ");
        Float amountFloat = apiResponse.getContent().get(0).getTotalSchemeAmount();
        long amountLong = amountFloat.longValue();

        String amountInWords = KannadaNumberUtil.convertNumberToKannadaWords(amountLong);

        response.setHeader6("ಅದೇಶ ಸಂಖ್ಯೆ/ ರೇಸನಿ /ಸರೇಗೂಮಾ/ " +apiResponse.getContent().get(0).getUserMarket() + "/ತಾಂ/ಬೆಸ್ಥಿನಿಅಅಕಾ/ಬೋನಸ್/ ಮಂ/"+apiResponse.getContent().get(0).getSanctionOrderNumber() + " / ದಿನಾಂಕ:  "+ formattedDate);


//        response.setHeader8("ಈ    ಕಚೇರಿಯ   ಲೆಕ್ಕ    ಶಾಖೆಗೆ   ಮುಂದಿನ   ಕ್ರಮಕ್ಕಾಗಿ\n" +
//                "ಪ್ರತಿಯನ್ನು    ರೇಷ್ಮೆ     ಉಪ ನಿರ್ದೇಶಕರು,    ಮೈಸೂರು    ಬಿತ್ತನೆ    ಪ್ರದೇಶ,   "+apiResponse.getContent().get(0).getUserDistrict()+"\n"+
//                "ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು / ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,    ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ, "+apiResponse.getContent().get(0).getLoggedinUserTscName());

                response.setHeader8("ಈ    ಕಚೇರಿಯ   ಲೆಕ್ಕ    ಶಾಖೆಗೆ   ಮುಂದಿನ   ಕ್ರಮಕ್ಕಾಗಿ\n" +
                "ಪ್ರತಿಯನ್ನು    ರೇಷ್ಮೆ     ಉಪ ನಿರ್ದೇಶಕರು,   ಜಿಲ್ಲಾ      ಪಂಚಾಯತ್    ");

        response.setAcceptedDate(" ಸ್ವೀಕೃತಿ ಪತ್ರದ  ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        response.setAddressText( apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName( apiResponse.getContent().get(0).getTalukName());
        response.setHobliName( apiResponse.getContent().get(0).getHobliName());
        response.setVillageName( apiResponse.getContent().get(0).getVillageName());
        response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
        String headerFarmerName   = apiResponse.getContent().get(0).getFarmerFirstName() == null ? "" : apiResponse.getContent().get(0).getFarmerFirstName();
        String headerFruitsId     = apiResponse.getContent().get(0).getFruitsId() == null ? "" : apiResponse.getContent().get(0).getFruitsId();
        String headerFatherName   = apiResponse.getContent().get(0).getFatherNameKan() == null ? "" : apiResponse.getContent().get(0).getFatherNameKan();

        String headerFullName = "ಶ್ರೀ./ಶ್ರೀಮತಿ. " + headerFarmerName
                + " (" + headerFruitsId + ") ಬಿನ್/ಕೋಂ " + headerFatherName;

        response.setFarmerFirstName(headerFullName);
        response.setLineItemComment( "ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n" +
                "ಸರ್ಕಾರಿ ರೇಷ್ಮೆ   ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ \n" +
                apiResponse.getContent().get(0).getUserMarket());
        response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn( apiResponse.getContent().get(0).getArn());
        response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
        response.setVillageNameInKannada( apiResponse.getContent().get(0).getVillageNameInKannada());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
//        sanctionOrderResponseList.add(response);

        long totalNoOfCocoonsPerKg = 0L;
        float totalQuantityOfCocoonsProduced = 0f;
        float totalRawSilkProduced = 0f;



        if (apiResponse.getContent()!= null) {
            sanctionOrderResponseList.add(response);
            int serialNo = 1;
            for(SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()){
                if (sanctionOrderResponse.getFarmerFirstName() == null){
                    sanctionOrderResponse.setFarmerFirstName("");
                }
                if (sanctionOrderResponse.getVillageNameInKannada() == null){
                    sanctionOrderResponse.setVillageNameInKannada("");
                }
                if (sanctionOrderResponse.getPerKgRate() == null){
                    sanctionOrderResponse.setPerKgRate(0f);
                }

                if (sanctionOrderResponse.getLotWeight() == null){
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getMarketAuctionDate() == null) {
                    sanctionOrderResponse.setMarketAuctionDate("");
                }
                if (sanctionOrderResponse.getTotalCocoonsWeight() == null) {
                    sanctionOrderResponse.setTotalCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
                    sanctionOrderResponse.setTotalSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }
                if (sanctionOrderResponse.getRawSilkProduced() == null) {
                    sanctionOrderResponse.setRawSilkProduced(0f);
                }

                totalRawSilkProduced += sanctionOrderResponse.getRawSilkProduced();

                if (sanctionOrderResponse.getQuantityOfCocoonsProduced() == null) {
                    sanctionOrderResponse.setQuantityOfCocoonsProduced(0f);
                }

                totalQuantityOfCocoonsProduced +=
                        sanctionOrderResponse.getQuantityOfCocoonsProduced();


                Float value = sanctionOrderResponse.getNoOfCocoonsPerKg();

                if (value == null) {
                    value = 0f;
                }
                float roundedValue = (float) Math.round(value);
                sanctionOrderResponse.setNoOfCocoonsPerKg(roundedValue);
                totalNoOfCocoonsPerKg += Math.round(value);

                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
        response.setTotalNoOfCocoonsPerKg(totalNoOfCocoonsPerKg);
        response.setTotalQuantityOfCocoonsProduced(totalQuantityOfCocoonsProduced);
        String totalNoOfCocoonsPerKgText = String.valueOf(totalNoOfCocoonsPerKg);
        String totalRawSilkProducedText =
                String.format("%.2f", totalRawSilkProduced);
        response.setRawSilkProduced(totalRawSilkProduced);



        response.setHeader7("            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ    ಎಲ್ಲಾ     ಅಂಶಗಳನ್ನು     ಪರಶೀಲಿಸಲಾಗಿ,    ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ     " +apiResponse.getContent().get(0).getUserMarket() + "   ಯಲ್ಲಿ    ಅನುಬಂಧದಲ್ಲಿ     ತೋರಿಸಿರುವ      ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರಿಗೆ    ಸಂಬಂಧಿಸಿದ   " + totalRawSilkProducedText+ "  ಕೆ.ಜಿ    "+apiResponse.getContent().get(0).getRaceName()+"   ತಳಿ   ಬಿತ್ತನೆ     ಗೂಡು     ಬಿತ್ತನೆಗೆ     ಯೋಗ್ಯವಾಗಿದ್ದು,    ಬೇಡಿಕೆ     ಇಲ್ಲದೆ     ನೂಲು    ಬಿಚ್ಚಾಣಿಕೆಗೆ     ವಹಿವಾಟಾದ    ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ.  "+perKgRateText+"    ರಂತೆ     ಬೋನಸ್     ಮೊತ್ತ     ರೂ.    "+totalSchemeAmountText+"   ಗಳನ್ನು      (ರೂ. "+amountInWords+" ) ಗಳನ್ನು      ಮಂಜೂರು     ಮಾಡಿದೆ.    "+apiResponse.getContent().get(0).getSchemeNameInKannada()+"   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ:  "+apiResponse.getContent().get(0).getScHeadAccountName()+"("+apiResponse.getContent().get(0).getDescription()+")  ರಡಿ    ಖಜಾನೆ-2   ರಲ್ಲಿ      ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ     ಸಹಾಯಧನದ      ಅನದಾನದಲ್ಲಿ      ಡಿಬಿಟಿ    ಮುಖಾಂತರ    ಫಲಾನುಭವಿ    ಬ್ಯಾಂಕ್      ಖಾತೆಗೆ   ನೇರವಾಗಿ    ಜಮಾ   ಮಾಡುವುದು.\n"+
                "            ಸದರಿ    ವೆಚ್ಚ ವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ:  "+apiResponse.getContent().get(0).getScHeadAccountName() +"("+apiResponse.getContent().get(0).getDescription() +")  ಅಡಿ    ಭರಿಸುವುದು.");


        response.setHeader5("ಪ್ರ ಸ್ತಾ ವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ    ಮೇಲ್ಕಂಡ    ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರು     ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆ     " +apiResponse.getContent().get(0).getUserMarket() +"   ಇಲ್ಲಿ   " +
                " "+apiResponse.getContent().get(0).getRaceName() +"   ತಳಿ    ಬಿತ್ತ ನೆ       ಗೂಡು     ಬಿತ್ತ ನೆಗೆ     ಯೋಗ್ಯ ವಾಗಿದ್ದು,    ಬೇಡಿಕೆ     ಇಲ್ಲ ದೆ     ನೂಲು    ಬಿಚ್ಚಾ ಣಿಕೆಗೆ     ವಹಿವಾಟಾದ     "+totalRawSilkProducedText+" " +
                "   ಕೆ.ಜಿ.    ರೇಷ್ಮೆ     ಗೂಡಿಗೆ      ಪ್ರ ತಿ     ಕೆ.ಜಿ.ಗೆ      ರೂ. "+perKgRateText +"   ರಂತೆ    ಬೋನಸ್     ಮೊತ್ತ      ರೂ. "+totalSchemeAmountText+" ಗಳನ್ನು      ಪಡೆಯಲು    ಅರ್ಹರಿರುತ್ತಾರೆ     ಉಲ್ಲೇಖ (3) ರ    ಸರ್ಕಾರದ " +
                "   ಆದೇಶದ    ರೀತ್ಯಾ    ಈ    ಕಛೇರಿಯ    ಅಧಿಕಾರ    ಪ್ರತ್ಯಾ ಯೋಜನೆ   ವ್ಯಾ ಪ್ತಿ ಯಲ್ಲಿದ್ದು,    ಉಲ್ಲೇಖ(4) ರಲ್ಲಿ      ಸದರಿ    ಕಾರ್ಯಕ್ರ ಮದ     ಅನುಷ್ಟಾ ನಕ್ಕಾ ಗಿ    ನೀಡಿರುವ    ಮಾರ್ಗಸೂಚಿಯನ್ವ ಯ     ಬೋನಸ್     ಮೊತ್ತ ವನ್ನು     ಪಾವತಿಸಲು     ಅನುದಾನ    ಬಿಡುಗಡೆ    ಮಾಡಲಾಗಿದೆ,   ಅದರಂತೆ    ಈ   ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ   ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }



    private JRBeanCollectionDataSource getDataSourceForIncentive120(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSeedCocoon(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();


        // ✅ Date formatter
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // ✅ Format date fields safely
        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

        long roundedPerKgRate = Math.round(apiResponse.getContent().get(0).getPerKgRate());
        String perKgRateText = String.valueOf(roundedPerKgRate);

        long roundedTotalSchemeAmount = Math.round(apiResponse.getContent().get(0).getTotalSchemeAmount());
        String totalSchemeAmountText = String.valueOf(roundedTotalSchemeAmount);

        String raceName = apiResponse.getContent().get(0).getRaceName();
        String raceNameWithoutFirstWord = removeFirstWord(raceName);

        String formattedDate1 = "";
        try {
            // Example input: Wed Nov 19 05:30:00 IST 2025
            String inputDate = apiResponse.getContent().get(0).getCurrentDate().toString();

            // Input format (Java default Date.toString())
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            // Output format → what you want
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = inputFormat.parse(inputDate);
            formattedDate1 = outputFormat.format(date);

        } catch (Exception e) {
            // fallback
            formattedDate1 = String.valueOf(apiResponse.getContent().get(0).getCurrentDate());
        }

        String shortRace = getKannadaShortForm(apiResponse.getContent().get(0).getRaceName());


        String formattedDate = "";
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

            // Parse input format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            // Define output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Convert and format
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);

        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
        }

        String schemeNameKannada =
                apiResponse.getContent().get(0).getSchemeNameInKannada();

        String schemeInitials =
                getKannadaInitialsFromSentence(schemeNameKannada);



        response.setHeader("ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರ   ಕಛೇರಿ,  ರೇಷ್ಮೆ   ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ ,  " +apiResponse.getContent().get(0).getUserMarket() + "  ಇವರ  ಕಛೇರಿ  ನಡವಳಿಗಳು");
        response.setHeader2("ವಿಷಯ: ");
        response.setHeader3("ಉಲ್ಲೇಖ: ");
        response.setHeader4("ಪೀಠಿಕೆ:-");

        response.setHeader1(  apiResponse.getContent().get(0).getFinancialYear() +  "   ನೇ   ಸಾಲಿನಲ್ಲಿ      "+apiResponse.getContent().get(0).getSchemeNameInKannada() +"  ದಡಿ     "+raceNameWithoutFirstWord+"    ಬಿತ್ತನೆ    ಪ್ರದೇಶದಲ್ಲಿ     ಉತ್ಪಾದನೆಯಾಗುವ    "+apiResponse.getContent().get(0).getRaceName()+"    ತಳಿ   ಬಿತ್ತನೆ    ಗೂಡುಗಳಿಗೆ    ಉತ್ಪಾದಕತೆ   ಮತ್ತು    ಗುಣಮಟ್ಟ ದ    ಆಧಾರದಮೇಲೆ    ಪ್ರೋತ್ಸಾಹಧನ ಮೊತ್ತವನ್ನು    ಮಂಜೂರಾತಿ   ನೀಡುವ    ಬಗ್ಗೆ.\n\n"+

                "1) ಸರ್ಕಾರದ    ಆದೇಶ    ಸಂಖ್ಯೆ  : " +apiResponse.getContent().get(0).getAdmGovtOrder() + "  ದಿನಾಂಕ :  " + admGovtDate  + "\n" +
                "2) ರೇಷ್ಮೆ    ಕೃ ಷಿ     ಆಭಿವೃ ದ್ಧಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು , ಬೆ೦ಗಳೂರು ರವರ    ಸುತ್ತೋ ಲೆ    ಸಂಖ್ಯೆ  : \n" +
                "     " + apiResponse.getContent().get(0).getSchemeCircularNo() + "   ದಿನಾಂಕ :  " +schemeCircularDate  + " \n" +
                "3) ಸರ್ಕಾರದ    ಆದೇಶ    ಸ೦ಖ್ಯೆ  :  " +apiResponse.getContent().get(0).getDeptDeleNo() + "    ದಿನಾಂಕ : " +deptDeleDate  + "\n" +
                        "4) ರೇಷ್ಮೆ    ಸಹಾಯಕ   ನಿರ್ದೇಶಕರು,    " +apiResponse.getContent().get(0).getUserMarket() + "  ರವರ   ಜ್ಞಾಪನ    ಪತ್ರದ   ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getSReleaseNo() + "\n" +
                "     ದಿನಾಂಕ : " +sReleaseDate + " \n\n" +

                "                 "+apiResponse.getContent().get(0).getFinancialYear() +"    ನೇ   ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ ಇಲಾಖೆಯ    ವಿವಿಧ     ಕಾರ್ಯ ಕ್ರ ಮಗಳ     ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ     ವಿವಿಧ    ಲೆಕ್ಕ    " +
                " ಶೀರ್ಷಿಕೆಗಳಡಿ    ಉಲ್ಲೇಖ(1) ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮ ಕ    ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು ,   ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ " +
                apiResponse.getContent().get(0).getSchemeNameInKannada() +" ದಡಿ  "+raceNameWithoutFirstWord+"     ಬಿತ್ತ ನೆ      ಪ್ರ ದೇಶದಲ್ಲಿ     ಉತ್ಪಾ ದನೆಯಾಗುವ    "+apiResponse.getContent().get(0).getRaceName() +
                "ತಳಿ     ಬಿತ್ತ ನೆ     ಗೂಡುಗಳಿಗೆ     ಉತ್ಪಾದಕತೆ    ಮತ್ತು     ಗುಣಮಟ್ಟದ    ಆಧಾರದ    ಮೇಲೆ     ಪ್ರ ತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ." +perKgRateText +"/-   ಗಳ " +
                "   ಪ್ರೋತ್ಸಾಹಧನ     ನೀಡುವ      ಕಾರ್ಯಕ್ರ ಮದ     ಅನುಷ್ಟಾ ನಕ್ಕಾಗಿ    ಮಾರ್ಗಸೂಚಿಯನ್ನು    ನೀಡಲಾಗಿದೆ.\n "+

                "                  ಬಿತ್ತ ನೆ     ಪ್ರ ದೇ ಶದಲ್ಲಿ       ನಿರಂತರ     ರೇಷ್ಮೆ   ಬಿತ್ತ ನೆ      ಗೂಡುಗಳನ್ನು      ಉತ್ಪಾ ದಿಸಿ   ಸರಬರಾಜು    ಮಾಡುವ    ಉದ್ದೇಶದಿಂದ    ಸರ್ಕಾರಿ      ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ ,  "+apiResponse.getContent().get(0).getUserMarket() +"    ಇಲ್ಲಿ     ವಹಿವಾಟು     ಮಾಡಿದ  " +
                "  "+apiResponse.getContent().get(0).getRaceName() +"   ತಳಿ    ಬಿತ್ತ ನೆ     ಗೂಡುಗಳಿಗೆ     ಉತ್ಪಾ ದಕತೆ    ಮತ್ತು     ಗುಣಮಟ್ಟ ದ    ಆಧಾರದ    ಮೇಲೆ    ಪ್ರ ತಿ     ಕೆ.ಜಿ.ಗೆ     ರೂ."+perKgRateText +"/- ಗಳ    ಪ್ರೋತ್ಸಾಹಧನ  ನೀಡುವ    ಕಾರ್ಯಕ್ರ ಮದಡಿ     ಪ್ರೋತ್ಸಾಹಧನದ  ವಿವರಗಳು   ಈ   ಕೆಳಕಂಡಂತಿದೆ.  ");
        Float amountFloat = apiResponse.getContent().get(0).getTotalSchemeAmount();
        long amountLong = amountFloat.longValue();

        String amountInWords = KannadaNumberUtil.convertNumberToKannadaWords(amountLong);

        response.setHeader6("ಅದೇಶ ಸಂಖ್ಯೆ/ ರೇಸನಿ /ಸರೇಗೂಮಾ/ " +apiResponse.getContent().get(0).getUserMarket() + "/ತಾಂ/ಬೆಸ್ಥಿನಿಅಅಕಾ/ಬಿಗೂಪ್ರೋಧನ/ ಮಂ/"+apiResponse.getContent().get(0).getSanctionOrderNumber() + " / ದಿನಾಂಕ:  "+ formattedDate);


        response.setHeader8("ಈ    ಕಚೇರಿಯ   ಲೆಕ್ಕ    ಶಾಖೆಗೆ   ಮುಂದಿನ   ಕ್ರಮಕ್ಕಾಗಿ\n" +
                "ಪ್ರತಿಯನ್ನು    ರೇಷ್ಮೆ     ಉಪ ನಿರ್ದೇಶಕರು,   ಜಿಲ್ಲಾ      ಪಂಚಾಯತ್ ");
        response.setAcceptedDate(" ಸ್ವೀಕೃತಿ ಪತ್ರದ  ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        SanctionOrderResponse first = apiResponse.getContent().get(0);

        String headerFarmerName   = first.getFarmerFirstName() == null ? "" : first.getFarmerFirstName();
        String headerFruitsId     = first.getFruitsId() == null ? "" : first.getFruitsId();
        String headerFatherName   = first.getFatherNameKan() == null ? "" : first.getFatherNameKan();

        String headerFullName = "ಶ್ರೀ./ಶ್ರೀಮತಿ. " + headerFarmerName
                + " (" + headerFruitsId + ") ಬಿನ್/ಕೋಂ " + headerFatherName;

        response.setFarmerFirstName(headerFullName);

        response.setAddressText( apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName( apiResponse.getContent().get(0).getTalukName());
        response.setHobliName( apiResponse.getContent().get(0).getHobliName());
        response.setVillageNameInKannada( apiResponse.getContent().get(0).getVillageNameInKannada());
        response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
        response.setLineItemComment( "ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n" +
                "ಸರ್ಕಾರಿ ರೇಷ್ಮೆ   ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ \n" +
                apiResponse.getContent().get(0).getUserMarket());
        response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn( apiResponse.getContent().get(0).getArn());
        response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");


        long totalNoOfCocoonsPerKg = 0L;
        float totalQuantityOfCocoonsProduced = 0f;


        if (apiResponse.getContent()!= null) {
            sanctionOrderResponseList.add(response);
            int serialNo = 1;
            for(SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()){
                if (sanctionOrderResponse.getFarmerFirstName() == null){
                    sanctionOrderResponse.setFarmerFirstName("");
                }
                if (sanctionOrderResponse.getVillageNameInKannada() == null){
                    sanctionOrderResponse.setVillageNameInKannada("");
                }
                if (sanctionOrderResponse.getPerKgRate() == null){
                    sanctionOrderResponse.setPerKgRate(0f);
                }

                if (sanctionOrderResponse.getLotWeight() == null){
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getMarketAuctionDate() == null) {
                    sanctionOrderResponse.setMarketAuctionDate("");
                }
                if (sanctionOrderResponse.getTotalCocoonsWeight() == null) {
                    sanctionOrderResponse.setTotalCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
                    sanctionOrderResponse.setTotalSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }
                if (sanctionOrderResponse.getQuantityOfCocoonsProduced() == null) {
                    sanctionOrderResponse.setQuantityOfCocoonsProduced(0f);
                }

                totalQuantityOfCocoonsProduced +=
                        sanctionOrderResponse.getQuantityOfCocoonsProduced();

                // -------- noOfCocoonsPerKg ROUND + TOTAL --------
                Float value = sanctionOrderResponse.getNoOfCocoonsPerKg();

                if (value == null) {
                    value = 0f;
                }
                float roundedValue = (float) Math.round(value);
                sanctionOrderResponse.setNoOfCocoonsPerKg(roundedValue);
                totalNoOfCocoonsPerKg += Math.round(value);

//                // ✅ build full Kannada farmer name for list row
//                String fullName = "ಶ್ರೀ./ಶ್ರೀಮತಿ. "
//                        + sanctionOrderResponse.getFarmerFirstName()
//                        + " (" + sanctionOrderResponse.getFruitsId()
//                        + ") ಬಿನ್/ಕೋಂ " + sanctionOrderResponse.getFatherNameKan();

//                sanctionOrderResponse.setFarmerFirstName(fullName);

                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
        response.setTotalNoOfCocoonsPerKg(totalNoOfCocoonsPerKg);
        response.setTotalQuantityOfCocoonsProduced(totalQuantityOfCocoonsProduced);

        String totalNoOfCocoonsPerKgText = String.valueOf(totalNoOfCocoonsPerKg);
        String totalQuantityProducedText =
                String.format("%.2f", totalQuantityOfCocoonsProduced);



        response.setHeader7("            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ    ಎಲ್ಲಾ     ಅಂಶಗಳನ್ನು     ಪರಶೀಲಿಸಲಾಗಿ,    ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ     " +apiResponse.getContent().get(0).getUserMarket() + "   ಇಲ್ಲಿ     ಅನುಬಂಧದಲ್ಲಿ  " +
                "   ತೋರಿಸಿರುವ      ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರಿಗೆ    ಸಂಬಂಧಿಸಿದ   " + totalQuantityProducedText + "  ಕೆ.ಜಿ    "+apiResponse.getContent().get(0).getRaceName()+"   ತಳಿ   ಬಿತ್ತ ನೆ    ಗೂಡುಗಳಿಗೆ " +
                "    ಉತ್ಪಾದಕತೆ    ಮತ್ತು    ಗುಣಮಟ್ಟದ    ಆಧಾರದ   ಮೇಲೆ    ಪ್ರ ತಿ   ಕೆ.ಜಿ.ಗೆ     ರೂ. "+perKgRateText+" ಗಳಂತೆ    ಪ್ರೋತ್ಸಾಹಧನದ    ಮೊತ್ತ    ರೂ. "+totalSchemeAmountText+" ಗಳನ್ನು   (ರೂ. "+amountInWords+" ) ಗಳನ್ನು    " +
                "  ಮಂಜೂರು     ಮಾಡಿದೆ.    "+apiResponse.getContent().get(0).getSchemeNameInKannada()+"   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ:  "+apiResponse.getContent().get(0).getScHeadAccountName()+"("+apiResponse.getContent().get(0).getDescription()+")  ರಡಿ    ಖಜಾನೆ-2   ರಲ್ಲಿ   " +
                "  ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ     ಅನದಾನದಲ್ಲಿ      ಡಿಬಿಟಿ    ಮುಖಾಂತರ    ಫಲಾನುಭವಿ    ಬ್ಯಾಂಕ್      ಖಾತೆಗೆ   ನೇರವಾಗಿ    ಜಮಾ   ಮಾಡುವುದು.\n"+
                "            ಸದರಿ    ವೆಚ್ಚ ವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ:  "+apiResponse.getContent().get(0).getScHeadAccountName() +"("+apiResponse.getContent().get(0).getDescription() +")  ಅಡಿ    ಭರಿಸುವುದು.");


        response.setHeader5("ಪ್ರ ಸ್ತಾ ವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ    ಮೇಲ್ಕಂಡ    ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರು     ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆ     " +apiResponse.getContent().get(0).getUserMarket() +"   ಇಲ್ಲಿ     ವಹಿವಾಟು " +
                "   ಮಾಡಿದ   "+totalQuantityProducedText +"    ಕೆ.ಜಿ.   "+apiResponse.getContent().get(0).getRaceName() +"   ತಳಿ    ಬಿತ್ತ ನೆ    ಗೂಡುಗಳಿಗೆ     ಉತ್ಪಾ ದಕತೆ    ಮತ್ತು    ಗುಣಮಟ್ಟ ದ     ಆಧಾರದ    ಮೇಲೆ     ಪ್ರ ತಿ    ಕೆ.ಜಿ.ಗೆ " +
                "    ರೂ."+perKgRateText  +"/- ಗಳಂತೆ     ಪ್ರೋತ್ಸಾಹಧನ  ರೂ. "+totalSchemeAmountText+" ಗಳನ್ನು     ಪಡೆಯಲು    ಅರ್ಹರಿರುತ್ತಾರೆ     ಉಲ್ಲೇಖ (3) ರ    ಸರ್ಕಾರದ    ಆದೇಶದ " +
                "   ರೀತ್ಯಾ    ಈ    ಕಛೇರಿಯ    ಅಧಿಕಾರ    ಪ್ರತ್ಯಾ ಯೋಜನೆ   ವ್ಯಾ ಪ್ತಿ ಯಲ್ಲಿದ್ದು ,     ಉಲ್ಲೇಖ(4) ರಲ್ಲಿ      ಸದರಿ    ಕಾರ್ಯಕ್ರ ಮದ     ಅನುಷ್ಟಾ ನಕ್ಕಾ ಗಿ    ನೀಡಿರುವ    ಮಾರ್ಗಸೂಚಿಯನ್ವ ಯ     ಪ್ರೋತ್ಸಾಹಧನ ಮೊತ್ತ ವನ್ನು    ಪಾವತಿಸಲು " +
                "   ಅನುದಾನ   ಬಿಡುಗಡೆ   ಮಾಡಲಾಗಿದೆ,   ಅದರಂತೆ    ಈ   ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ   ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");

        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private JRBeanCollectionDataSource getDataSourceForSeedCocoon(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSeedCocoon(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<SanctionOrderResponse> sanctionOrderResponseList= new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        // ✅ Date formatter
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // ✅ Format date fields safely
        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);


        // ✅ Clean formatted date for sanction order
        // ✅ Clean formatted date for sanction order
        String formattedDate1 = "";
        try {
            // Example input: Wed Nov 19 05:30:00 IST 2025
            String inputDate = apiResponse.getContent().get(0).getCurrentDate().toString();

            // Input format (Java default Date.toString())
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            // Output format → what you want
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = inputFormat.parse(inputDate);
            formattedDate1 = outputFormat.format(date);

        } catch (Exception e) {
            // fallback
            formattedDate1 = String.valueOf(apiResponse.getContent().get(0).getCurrentDate());
        }



        String formattedDate = "";
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

            // Parse input format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            // Define output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Convert and format
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);

        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
        }
        response.setHeader("ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರ  ಕಚೇರಿ  ಸರ್ಕಾರಿ  ರೇಷ್ಮೆ   ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ  " +apiResponse.getContent().get(0).getUserMarket() + "  ದಿನಾ೦ಕ:  " +apiResponse.getContent().get(0).getMarketAuctionDate() );
        response.setHeader2("ವಿಷಯ: ");
        response.setHeader3("ಉಲ್ಲೇಖ: ");
        response.setHeader4("ಪೀಠಿಕೆ:");
        Float amountFloat = apiResponse.getContent().get(0).getTotalSchemeAmount();
        long amountLong = amountFloat.longValue();

        String amountInWords = KannadaNumberUtil.convertNumberToKannadaWords(amountLong);

        response.setHeader1( "ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    ಗೂಡಿನ  ಮಾರುಕಟ್ಟೆ ,  " +apiResponse.getContent().get(0).getUserMarket() + "  ಸ೦ಸ್ಮೆಯಲ್ಲಿ   ಸರ್ಕಾರಿ  ಬಿತ್ತನೆ  ಕೋಠಿಗಳಿಗೆ ಬಿತ್ತನೆ  ಗೂಡು  ಖರೀಸಿದ\n" +
                "            \n"+
                " ಬಾಬ್ತು   ರೂ  " +apiResponse.getContent().get(0).getTotalSchemeAmount() + " /- ಗಳಿಗೆ  ಮಂಜೂರಾತಿ  ವೀಡುವ ಬಗ್ಗೆ.\n" +
                "            \n"+
                "1) ರೇಷ್ಮೆ   ಕೃಷಿ  ಆಭಿವೃದ್ಧಿ    ಆಯುಕ್ತರು ಹಾಗೂ  ರೇಷ್ಮೆ    ವಿರ್ದೇಶಕರು,  ಬೆ೦ಗಳೂರು  ರವರ  ಸುತ್ತೋಲೆ  ಪತ್ರದ \n" +
                "            \n"+
                "   ಸಂಖ್ಯೆ  :  " +apiResponse.getContent().get(0).getSchemeCircularNo() + " ದಿನಾಂಕ :  " +schemeCircularDate  + " \n" +
                "                     \n" +
                "2) ರೇಷ್ಮೆ     ಉಪ ವಿರ್ದೇಶಕರು,  "+apiResponse.getContent().get(0).getLoggedinUserTalukName() + "  ರವರ ಜ್ನಾಪನ  ಪತ್ರದ \n" +
                "                     \n" +
                "   ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getSReleaseNo() + "   ದಿನಾಂಕ : " +sReleaseDate + " \n" +
                "                    \n" +
                "3) ಪ್ರತ್ಯಾಯೋಜನೆ ಆದೇಶ  ಸ೦ಖ್ಯೆ  : " +apiResponse.getContent().get(0).getDeptDeleNo() + "    ದಿನಾಂಕ : " +deptDeleDate  + " ರ ಭಾಗ ||| ರ ಕ್ರಮ   ಸ೦ಖ್ಯೆ   3 ರಂತೆ.\n" +
                "                     \n" +
                "                                                                      ***************                   \n" +
                "            \n"+
                "            \n"+
                "              ಉಲ್ಲೇಖ  (1)  ರಿ೦ದ (3) ರ  ವರೆಗಿನ  ಆದೇಶ, ಸುತ್ತೋಲೆ  ಹಾಗೂ  ಜ್ನ್ಞಾಪನಗಳಲ್ಲಿ    ಸೂಚಿಸಿರುವಂತೆ.  ಸರ್ಕಾರಿ \n" +
                "            \n"+
                "ರೇಷ್ಮೆ    ಗೂಡಿನ   ಮಾರುಕಟ್ಟೆ ,  " +apiResponse.getContent().get(0).getUserMarket() + "  ಸಂಸ್ಮೆಯಲ್ಲಿ    ದಿನಾ೦ಕ:"+ formattedDate1 +"ರ  ವರೆಗೆ, ಸರ್ಕಾರಿ\n" +
                "            \n"+
                "ಬಿತ್ತನೆ   ಕೋಠಿಗಳ ಉಪಯೋಗಕ್ಕಾಗಿ   ಮೈಸೂರು  ಬಿತ್ತನೆ  ಗೂಡುಗಳನ್ನು   , ಈ ಕೆಳಕಂಡ ರೈತರಿ೦ದ  ಖರೀದಿಸಿದ  ಗೂಡಿನ\n" +
                "            \n"+
                "ಬಾಬ್ತು   ರೂ  " +apiResponse.getContent().get(0).getTotalSchemeAmount() + "  ಗಳಿಗೆ  ಮ೦ಜೂರಾತಿ  ನೀಡಬೇಕಾಗಿದೆ. ವಿವರ ಕೆಳಗಿನಂತಿದೆ.");
        response.setHeader5("              ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸಿರುವಂತೆ ,  ಸರ್ಕಾರಿ  ರೇಷ್ಮೆ    ಬಿತ್ತನೆ  ಕೋಠಿಗಳ ಉಪಯೋಗಕ್ಕಾಗಿ  ಮೈಸೂರು  ಬಿತ್ತನೆ  \n" +
                "            \n"+
                "ಗೂಡುಗಳನ್ನು    ಖರೀದಿಸಿದ   ಬಾಬ್ತು ರೂ  " +apiResponse.getContent().get(0).getTotalSchemeAmount() + " /- (ರೂಪಾಯಿ  " + amountInWords +"  ರೂ ಮಾತ್ರ )  ಗಳಿಗೆ\n" +
                "            \n"+
                "ಉಲ್ಲೇಖ (3)ರ  ರೀತ್ಯ    ಮ೦ಜೂರಾತಿ   ನೀಡಲಾಗಿದೆ.\n" +
                "            \n"+
                "ಸದರಿ   ವೆಚ್ಚವನ್ನು    "+apiResponse.getContent().get(0).getSchemeNameInKannada()+"  ಲೆಕ್ಕ   ಶೀರ್ಷಿಕೆ  \n" +
                "            \n"+
                apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಲ್ಲಿ    ಭರಿಸಲು ಮ೦ಜೂರಾತಿ ನೀಡಿದೆ.\n");

        response.setHeader6(" ( Rupees." +amountInWords + " )");
        response.setHeader7("ಸಂ:ರೇಸನಿ:ರೇಗೂಮಾ :" +apiResponse.getContent().get(0).getUserMarket() + ": ಬಿಗೂಐ:ಮಂ:02:2024-25 ");
        response.setLineItemComment( "ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n" +
                "            \n"+
                "ಸರ್ಕಾರಿ ರೇಷ್ಮೆ   ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ \n" +
                "            \n"+
                apiResponse.getContent().get(0).getUserMarket() +  " \n");
        response.setHeader8("ಮಂಜೂರಾತಿ ಆದೇಶ ಸ೦ಖ್ಯೆ  ಸ೦:ರೇಸನಿ:ರೇಗೂಮಾ:" +apiResponse.getContent().get(0).getUserMarket() + " :ಬಿಗೂಖ:ಮಂ:"+apiResponse.getContent().get(0).getSanctionOrderNumber() + " / ದಿನಾಂಕ:  "+ formattedDate+"\n");
        response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn( apiResponse.getContent().get(0).getArn());
        response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        //        sanctionOrderResponseList.add(response);


        if (apiResponse.getContent()!= null) {
            sanctionOrderResponseList.add(response);
            int serialNo = 1;
            for(SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()){
                if (sanctionOrderResponse.getFarmerFirstName() == null){
                    sanctionOrderResponse.setFarmerFirstName("");
                }
                if (sanctionOrderResponse.getVillageNameInKannada() == null){
                    sanctionOrderResponse.setVillageNameInKannada("");
                }
                if (sanctionOrderResponse.getPerKgRate() == null){
                    sanctionOrderResponse.setPerKgRate(0f);
                }
                if (sanctionOrderResponse.getCocoonsWeight() == null){
                    sanctionOrderResponse.setCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getLotWeight() == null){
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getMarketAuctionDate() == null) {
                    sanctionOrderResponse.setMarketAuctionDate("");
                }
                if (sanctionOrderResponse.getTotalCocoonsWeight() == null) {
                    sanctionOrderResponse.setTotalCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
                    sanctionOrderResponse.setTotalSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }

                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }

        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private JRBeanCollectionDataSource getDataSourceForSanctionSilkIncentive(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSilkIncentive(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<SanctionOrderResponse> sanctionOrderResponseList= new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

        // 🔹 Created date split
        String createdDateTime = apiResponse.getContent().get(0).getCreatedDate();
        String datePart = "";
        String timePart = "";

        if (createdDateTime != null && !createdDateTime.isEmpty()) {
            try {
                DateTimeFormatter in = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                LocalDateTime ldt = LocalDateTime.parse(createdDateTime, in);
                datePart = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                timePart = ldt.format(DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                try {
                    DateTimeFormatter in = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(createdDateTime, in);
                    datePart = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    timePart = ldt.format(DateTimeFormatter.ofPattern("HH:mm"));
                } catch (Exception ignore) {
                    datePart = createdDateTime;
                }
            }
        }

        response.setHeader("ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,  ಗೂಡಿನ  ನಂತರದ  ಚಟುವಟಿಕೆ,  " +apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "  ರವರ  ಕಛೇರಿ ನಡವಳಿಗಳು ");
        response.setHeader2("ವಿಷಯ : ");
        response.setHeader3("ಉಲ್ಲೇಖ: ");
        response.setHeader4("ಪೀಠಿಕೆ : ");
//        Float amountFloat = apiResponse.getContent().get(0).getTotalSchemeAmount();
//        long amountLong = amountFloat.longValue();

        String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        int schemeAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getSchemeAmount())));


        String schemeAmountWords = KannadaNumberUtil.convertNumberToKannadaWords(schemeAmount);

//        String amountInWords = NumberToWordsConverter.convert(amountLong);

        response.setHeader5(apiResponse.getContent().get(0).getFinancialYear() + "   ನೇ     ಸಾಲಿನಲ್ಲಿ      “"+apiResponse.getContent().get(0).getSchemeNameInKannada()+"”    "+apiResponse.getContent().get(0).getScCategoryName()+
                "ಅಡಿ      ರಾಜ್ಯ ದಲ್ಲಿ        ರೇಷ್ಮೆ     ನೂಲು     ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು      ಉತ್ಪಾ ದಿಸಿದ    ಗುಣಮಟ್ಟ ದ     ಕಚ್ಚಾ     ರೇಷ್ಮೆ ಗೆ     ಪ್ರೋತ್ಸಾಹಧನ  ರೇಷ್ಮೆ    ನೂಲು" +
                "    ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು    ತಮ್ಮ       ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ       ಉತ್ಪಾ ದಿಸಿದ      ಕಚ್ಚಾ     ರೇಷ್ಮೆ ಗೆ     ಪ್ರೋತ್ಸಾಹಧನ  ಕಾರ್ಯಕ್ರಮ ಮಂಜೂರಾತಿ    ನೀಡುವ    ಕುರಿತು.");

        response.setHeader1( "1. ಸರ್ಕಾರದ    ಆದೇಶ    ಸಂಖ್ಯೆ    : " +apiResponse.getContent().get(0).getAdmGovtOrder() + "   ದಿನಾಂಕ :  " +admGovtDate  + "\n" +
                "2. ರೇಷ್ಮೆ    ಕೃಷಿ     ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ    ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು  ರವರ    ಸುತ್ತೋಲೆ \n" +
                "     ಸಂಖ್ಯೆ  :  " +apiResponse.getContent().get(0).getSchemeCircularNo() + " ದಿನಾಂಕ :  " +schemeCircularDate  + " \n" +
                "3. ಸರ್ಕಾರದ    ಆದೇಶ     ಸಂಖ್ಯೆ  : " +apiResponse.getContent().get(0).getDeptDeleNo() + "    ದಿನಾಂಕ : " +deptDeleDate  + " \n" +
                "4. ರೇಷ್ಮೆ   ಕೃ ಷಿ   ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು,  ಬೆಂಗಳೂರು  ರವರ   ಸುತ್ತೋಲೆ\n" +
                "     ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getReleaseNo() + "   ದಿನಾಂಕ : " +releaseDate + " \n" +
                "5. ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,    ಗೂಡಿನ    ನಂತರದ    ಚಟುವಟಿಕೆ      "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() +"    ಇವರ    ಪ್ರ ಸ್ತಾ ವನೆ     ದಿನಾಂಕ : "+  proposalDate);
        response.setHeader6("              " +apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ   ಸಾಲಿನಲ್ಲಿ     ಉಲ್ಲೇಖ(1) ರ   ಸರ್ಕಾರದ   ಆದೇಶ   ಹಾಗೂ   ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ       ರೇಷ್ಮೆ    ಕೃಷಿ    ಅಭಿವೃ ದ್ದಿ " +
                           "ಆಯುಕ್ತ ರು    ಹಾಗೂ     ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು  ರವರು  “ "+apiResponse.getContent().get(0).getSchemeNameInKannada()+"”" +
                apiResponse.getContent().get(0).getScCategoryName() + "    ಅಡಿ    ರಾಜ್ಯ ದಲ್ಲಿ      ರೇಷ್ಮೆ    ನೂಲು   ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು    ಉತ್ಪಾ ದಿಸಿದ    ಗುಣಮಟ್ಟ ದ     ಕಚ್ಚಾ "+
                "ರೇಷ್ಮೆ ಗೆ     ಪ್ರೋತ್ಸಾಹಧನ  ನೀಡುವ   ಕಾರ್ಯಕ್ರ ಮವನ್ನು    ಅನುಷ್ಟಾನಗೊಳಿಸಲು   ಮಾರ್ಗಸೂಚಿಯನ್ನು    ನೀಡಿರುತ್ತಾ ರೆ,  ವಿವರಗಳು" +
                "ಈ  ಕೆಳಕಂಡಂತಿದೆ.");

//        response.setHeader6(" ( Rupees." +amountInWords + " Only )");
//      response.setHeader7("ಉಲ್ಲೇಖ (5) ರಲ್ಲಿ      ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,  ಗೂಡಿನ   ನಂತರದ   ಚಟುವಟಿಕೆ,   "+ apiResponse.getContent().get(0).getLoggedinUserTscName() +
//              "    ಇವರು   ಈ   ಕೆಳಕಂಡ    ಅನುಬಂಧದಲ್ಲಿ       ತೋರಿಸಿರುವ     ರೇಷ್ಮೆ   ನೂಲು    ಬಿಚ್ಚಾಣಿಕೆದಾರರು   " + apiResponse.getContent().get(0).getMachineTypeName()+ "    ಘಟಕದಲ್ಲಿ     ಉತ್ಪಾದಿಸಿದ    "+ apiResponse.getContent().get(0).getRaceName() +
//                      "       ರೇಷ್ಮೆಗೆ,      ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ      ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆಯಿಂದ     ಗೂಡು    ಖರೀದಿಸಿದ    ವರದಿ,    ರೇಷ್ಮೆ    ಮಾರಾಟದ    17ಜೆ    ನಮೂನೆಯ   ವರದಿಯೊಂದಿಗೆ    ಮತ್ತು      ಇತರೆ      "+
//              apiResponse.getContent().get(0).getMachineTypeName()+ "    ಘಟಕದಲ್ಲಿ     ಉತ್ಪಾದಿಸಿದ     "+ apiResponse.getContent().get(0).getRaceName() + "    ದಾಖಲಾತಿಗಳೊಂದಿಗೆ     ಈ    ಕೆಳಕಂಡ     ರೇಷ್ಮೆ     ನೂಲು     ಬಿಚ್ಚಾಣಿಕೆದಾರರು , "+
//                "  ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಯಿಂದ     ಗೂಡು     ಖರೀದಿಸಿದ   ವರದಿ,    ರೇಷ್ಮೆ     ಪರೀಕ್ಷಣಾ    ವರದಿ,   ( ರೇಷ್ಮೆ    ಮಾರಾಟ     ಮಾಡುವ " +
//                "ಮುನ್ನಾ      ಮಲ್ಟಿ    ಎಂಡ್‌   ರೀಲಿಂಗ್‌   ಮತ್ತು      ಸ್ವಯಂ   ಚಾಲಿತ   ರೀಲಿಂಗ್‌   ಘಟಕದಲ್ಲಿ     ಉತ್ಪಾ ದಿಸಿದ    ರೇಷ್ಮೆ ಗೆ )  ರೇಷ್ಮೆ ಉತ್ಪಾದನೆ   ಹಾಗೂ   ಮಾರಾಟ   ಮತ್ತು     ಇತರೆ " +
//                "ದಾಖಲಾತಿಗಳೊಂದಿಗೆ    ಈ   ಕೆಳಕಂಡ   ರೇಷ್ಮೆ  ನೂಲು   ಬಿಚ್ಚಾಣಿಕೆದಾರರು   ತಮ್ಮ    "+ apiResponse.getContent().get(0).getMachineTypeName() +  "  ದಲ್ಲಿ    "+ apiResponse.getContent().get(0).getRaceName() + "   ರೇಷ್ಮೆ   ನೂಲು   ಉತ್ಪಾದನೆ   ಮಾಡಿರುವುದಕ್ಕಾ ಗಿ   ಪ್ರತಿ   ಕೆ.ಜಿ.ಗೆ   ರೂ. " +apiResponse.getContent().get(0).getAmountPerKg() +"/-" +
//                "ರಂತೆ    ಪ್ರೋತ್ಸಾಹಧನ ಮಂಜೂರಾತಿಗಾಗಿ   ಪ್ರಸ್ತಾ ವನೆಯನ್ನು     ಸಲ್ಲಿ ಸಿರುತ್ತಾರೆ.  ಪ್ರಸ್ತಾ ವನೆಯನ್ನು   ಪರಿಶೀ ಲಿಸಿದ್ದು     ಉಲ್ಲೇ ಖ (2) ರ   ರೇಷ್ಮೆ    ಕೃಷಿ     ಅಭಿವೃದ್ದಿ      ಆಯುಕ್ತರು " +
//                      "ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು  ಬೆಂಗಳೂರು   ರವರ    ಸುತ್ತೋ ಲೆಯ   ಮಾರ್ಗ   ಸೂಚಿಯಂತೆ    "+apiResponse.getContent().get(0).getSchemeNameInKannada()+
//                      apiResponse.getContent().get(0).getScCategoryName() +  "    ಅಡಿ    ಅನುಬಂಧದಲ್ಲಿ    ತೋ ರಿಸಿರುವಂತೆ     ಪ್ರೋ ತ್ಸಾ ಹಧನ       ಪಡೆಯಲು ಅರ್ಹ ರಿರುತ್ತಾರೆ.");


        response.setLineItemComment( "ರೇಷ್ಮೆ     ಸಹಾಯಕ   ನಿರ್ದೇಶಕರು\n" +
                "ಗೂಡಿನ    ನಂತರದ    ಚಟುವಟಿಕೆ\n" +
                apiResponse.getContent().get(0).getLoggedinUserTalukName());

        response.setHeader11("ಇವರಿಗೆ, \n" +
                "ರೇಷ್ಮೆ    ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,\n" +
                "ಗೂಡಿನ    ನಂತರದ   ಚಟುವಟಿಕೆ,   " +apiResponse.getContent().get(0).getLoggedinUserTalukName());
        response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());

        response.setMachineTypeName( apiResponse.getContent().get(0).getMachineTypeName());
        response.setScCategoryName( apiResponse.getContent().get(0).getScCategoryName());

        response.setRenditta( apiResponse.getContent().get(0).getRenditta());

        response.setDailyLimit( apiResponse.getContent().get(0).getDailyLimit());
        response.setNumberOfBasins( apiResponse.getContent().get(0).getNumberOfBasins());
        response.setMax( apiResponse.getContent().get(0).getMax());




        response.setIncentiveAmountPerKg( apiResponse.getContent().get(0).getIncentiveAmountPerKg());


        response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn( apiResponse.getContent().get(0).getArn());
        response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        response.setSerialNumber(1);  // <-- UPDATED LINE

        sanctionOrderResponseList.add(response);

        // ---------- TOTALS ----------
        float totalNoOfCocoonsNeedToProduce = 0f;
        float totalNoOfRawSilkProduced = 0f;
        float totalMachineQuantity = 0f;
        float totalMax = 0f;
        float totalSchemeAmount = 0f;
        DecimalFormat df3 = new DecimalFormat("#0.000");
        DecimalFormat df2 = new DecimalFormat("#0.00");

        if (apiResponse.getContent()!= null) {
//            sanctionOrderResponseList.add(response);
            int serialNo = 1;
            for(SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()){
                if (sanctionOrderResponse.getFarmerFirstName() == null){
                    sanctionOrderResponse.setFarmerFirstName("");
                }
                if (sanctionOrderResponse.getVillageNameInKannada() == null){
                    sanctionOrderResponse.setVillageNameInKannada("");
                }

                if (sanctionOrderResponse.getReelerName() == null){
                    sanctionOrderResponse.setReelerName("");
                }
                if (sanctionOrderResponse.getMonth() == null){
                    sanctionOrderResponse.setMonth("");
                }
                if (sanctionOrderResponse.getNumberOfBasins() == null){
                    sanctionOrderResponse.setNumberOfBasins("");
                }
//                if (sanctionOrderResponse.getNoOfCocoonsNeedToProduce() == null){
//                    sanctionOrderResponse.setNoOfCocoonsNeedToProduce("");
//                }
//                if (sanctionOrderResponse.getNoOfRawSilkProduced() == null){
//                    sanctionOrderResponse.setNoOfRawSilkProduced("");
//                }
                if (sanctionOrderResponse.getRenditta() == null){
                    sanctionOrderResponse.setRenditta("");
                }
                if (sanctionOrderResponse.getSilkExchangeName() == null){
                    sanctionOrderResponse.setSilkExchangeName("");
                }
                if (sanctionOrderResponse.getForm17jNo() == null){
                    sanctionOrderResponse.setForm17jNo("");
                }
//                if (sanctionOrderResponse.getMachineQuantity() == null){
//                    sanctionOrderResponse.setMachineQuantity(0f);
//                }
                if (sanctionOrderResponse.getMax() == null){
                    sanctionOrderResponse.setMax(0f);
                }
//                if (sanctionOrderResponse.getSchemeAmount() == null){
//                    sanctionOrderResponse.setSchemeAmount(0f);
//                }
                if (sanctionOrderResponse.getMachineTypeName() == null){
                    sanctionOrderResponse.setMachineTypeName("");
                }
                if (sanctionOrderResponse.getScCategoryName() == null){
                    sanctionOrderResponse.setScCategoryName("");
                }
                if (sanctionOrderResponse.getRenditta() == null){
                    sanctionOrderResponse.setRenditta("");
                }
                if (sanctionOrderResponse.getDailyLimit() == null){
                    sanctionOrderResponse.setDailyLimit("");
                }
//                if (sanctionOrderResponse.getIncentiveAmountPerKg() == null){
//                    sanctionOrderResponse.setIncentiveAmountPerKg("");
//                }
                if (sanctionOrderResponse.getPerKgRate() == null){
                    sanctionOrderResponse.setPerKgRate(0f);
                }
                if (sanctionOrderResponse.getCocoonsWeight() == null){
                    sanctionOrderResponse.setCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getLotWeight() == null){
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getMarketAuctionDate() == null) {
                    sanctionOrderResponse.setMarketAuctionDate("");
                }
                if (sanctionOrderResponse.getTotalCocoonsWeight() == null) {
                    sanctionOrderResponse.setTotalCocoonsWeight(0f);
                }
//                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
//                    sanctionOrderResponse.setTotalSchemeAmount(0f);
//                }
//                if (sanctionOrderResponse.getSanctionAmount() == null) {
//                    sanctionOrderResponse.setSanctionAmount(0f);
//                }

                // ---- NULL SAFETY FOR FLOAT FIELDS ----

                if (sanctionOrderResponse.getNoOfCocoonsNeedToProduce() == null) {
                    sanctionOrderResponse.setNoOfCocoonsNeedToProduce(df3.format(0));
                }

                if (sanctionOrderResponse.getNoOfRawSilkProduced() == null) {
                    sanctionOrderResponse.setNoOfRawSilkProduced(df3.format(0));
                }

                if (sanctionOrderResponse.getMachineQuantity() == null) {
                    sanctionOrderResponse.setMachineQuantity(0.000f);
                }

                if (sanctionOrderResponse.getSchemeAmount() == null) {
                    sanctionOrderResponse.setSchemeAmount(0.00f);
                }


                sanctionOrderResponse.setSerialNumber(serialNo++);

                totalNoOfCocoonsNeedToProduce += safeParseFloat(
                        sanctionOrderResponse.getNoOfCocoonsNeedToProduce());

                totalNoOfRawSilkProduced += safeParseFloat(
                        sanctionOrderResponse.getNoOfRawSilkProduced());

                totalMachineQuantity += (sanctionOrderResponse.getMachineQuantity() == null ? 0f : sanctionOrderResponse.getMachineQuantity());
                totalMax += (sanctionOrderResponse.getMax() == null ? 0f : sanctionOrderResponse.getMax());
                totalSchemeAmount += (sanctionOrderResponse.getSchemeAmount() == null ? 0f : sanctionOrderResponse.getSchemeAmount());

                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
        SanctionOrderResponse totalRow = new SanctionOrderResponse();

        totalRow.setReelerName("ಒಟ್ಟು");

        totalRow.setSerialNumber(null);

        // Set totals
        totalRow.setNoOfCocoonsNeedToProduce(String.valueOf(totalNoOfCocoonsNeedToProduce));
        totalRow.setNoOfRawSilkProduced(String.valueOf(totalNoOfRawSilkProduced));
        totalRow.setMachineQuantity(totalMachineQuantity);
        totalRow.setMax(totalMax);
        totalRow.setSchemeAmount(totalSchemeAmount);


// 1️⃣ Amount per Kg (rounded)
        long roundedAmountPerKg =
                Math.round(apiResponse.getContent().get(0).getIncentiveAmountPerKg());

// 2️⃣ Total Raw Silk Produced (.000)
        String totalRawSilkProducedFormatted =
                df3.format(totalNoOfRawSilkProduced);

// 3️⃣ Total Scheme Amount (.00)
        String totalSchemeAmountFormatted =
                df2.format(totalSchemeAmount);

        response.setHeader7("          ಉಲ್ಲೇಖ(5)ರಲ್ಲಿ      ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,    ಗೂಡಿನ    ನಂತರದ    ಚಟುವಟಿಕೆ,    "+ apiResponse.getContent().get(0).getLoggedinUserTscName() +
                "   ಇವರು    ಈ    ಕೆಳಕಂಡ    ಅನುಬಂಧದಲ್ಲಿ     ತೋರಿಸಿರುವ    ರೇಷ್ಮೆ    ನೂಲು    ಬಿಚ್ಚಾಣಿಕೆದಾರರು   "+ apiResponse.getContent().get(0).getMachineTypeName()+"   ಘಟಕದಲ್ಲಿ      "+
                "ಉತ್ಪಾದಿಸಿದ     ಕಚ್ಚಾ   ರೇಷ್ಮೆಗೆ,   ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ   ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆಯಿಂದ   ಗೂಡು    ಖರೀದಿಸಿದ    ವರದಿ,    ರೇಷ್ಮೆ    "+
                "ಮಾರಾಟದ     17ಜೆ     ನಮೂನೆಯ    ವರದಿಯೊಂದಿಗೆ    ಮತ್ತು    ಇತರೆ    ದಾಖಲಾತಿಗಳೊಂದಿಗೆ    ಈ    ಕೆಳಕಂಡ    ರೇಷ್ಮೆ   ನೂಲು   ಬಿಚ್ಚಾಣಿಕೆದಾರರು ,  "+ apiResponse.getContent().get(0).getMachineTypeName()+
                "      ಘಟಕದಲ್ಲಿ     ಕಚ್ಚಾ   ರೇಷ್ಮೆ    ನೂಲು    ಉತ್ಪಾದನೆ    ಮಾಡಿರುವುದಕ್ಕಾಗಿ    ಪ್ರತಿ   ಕೆ.ಜಿಗೆ    ರೂ. "+ roundedAmountPerKg +"/- ರಂತೆ  ಪ್ರೋತ್ಸಾಹಧನ ಮಂಜೂರಾತಿಗಾಗಿ,    " +
                "ಪ್ರಸ್ತಾವನೆಯನ್ನು      ಸಲ್ಲಿಸಿರುತ್ತಾರೆ.    ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಿದ್ದು,    ಉಲ್ಲೇಖ (2)ರ     ರೇಷ್ಮೆ      ಕೃಷಿ    ಅಭಿವೃದ್ಧಿ       ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು ರವರ      " +
                "ಸುತ್ತೋಲೆಯ     ಮಾರ್ಗಸೂಚಿಯಂತೆ     "+apiResponse.getContent().get(0).getSchemeNameInKannada()+ "    "+apiResponse.getContent().get(0).getScCategoryName() + " , ಅನುಬಂಧದಲ್ಲಿ      "+
                "ತೋರಿಸಿರುವಂತೆ    ಪ್ರೋತ್ಸಾಹಧನ    ಪಡೆಯಲು    ಅರ್ಹರಿರುತ್ತಾರೆ.");

//                response.setHeader8("              ಮೇಲ್ಕಂಡ     ರೇಷ್ಮೆ      ನೂಲು     ಬಿಚ್ಚಾಣಿಕೆದಾರರು    ತಮ್ಮ      ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ       ಉತ್ಪಾದಿಸಿ     ವಹಿವಾಟು     ಮಾಡಿದ    "+ apiResponse.getContent().get(0).getNoOfRawSilkProduced() +"     ಪ್ರಮಾಣದ    "+ apiResponse.getContent().get(0).getRaceName() +
//                        "ರೇಷ್ಮೆಗೆ    ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ.  "+ apiResponse.getContent().get(0).getAmountPerKg() +"/-  ರಂತೆ    ಪ್ರೋತ್ಸಾಹಧನವನ್ನು     ಉಲ್ಲೇಖ (3) ರ   ಸರ್ಕಾರದ   ಆದೇಶದ   ರೀತ್ಯಾ   ಈ   ಕಛೇರಿಯ   ಅಧಿಕಾರ " +
//                        "ಪ್ರತ್ಯಾಯೋಜನೆ   ವ್ಯಾಪ್ತಿಯಲ್ಲಿದ್ದು,  ಉಲ್ಲೇಖ (4)  ರಲ್ಲಿ     ಸದರಿ    ಕಾರ್ಯಕ್ರಮದ   ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ನೀಡಿರುವ   ಮಾರ್ಗಸೂಚಿಯನ್ವಯ   ಸಹಾಯಧನ   ಮಂಜೂರು" +
//                        "ಮಾಡಲು   ಅನುದಾನ   ಬಿಡುಗಡೆ    ಮಾಡಲಾಗಿದೆ.   ಅದರಂತೆ   ಈ  ಕೆಳಕಂಡ  ಮಂಜೂರಾತಿ  ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");

        response.setHeader8("              ಮೇಲ್ಕಂಡ     ರೇಷ್ಮೆ      ನೂಲು     ಬಿಚ್ಚಾಣಿಕೆದಾರರು,    ತಮ್ಮ      ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ       ಉತ್ಪಾದಿಸಿ     ವಹಿವಾಟು     ಮಾಡಿದ    "+totalRawSilkProducedFormatted +
                "   ಕೆ.ಜಿ     ಪ್ರಮಾಣದ      ರೇಷ್ಮೆಗೆ    ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ.  "+ roundedAmountPerKg +"/-  ರಂತೆ    ಪ್ರೋತ್ಸಾಹಧನವನ್ನು      ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ      ಸದರಿ     ಕಾರ್ಯಕ್ರಮದ      " +
                "ಅನುಷ್ಠಾನಕ್ಕಾಗಿ     ನೀಡಿರುವ     ಮಾರ್ಗಸೂಚಿಯನ್ವಯ     ಸಹಾಯಧನ    ಮಂಜೂರು   ಮಾಡಲು    ಅನುದಾನ    ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿದೆ.     ಅದರಂತೆ    ಈ    ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ    ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");


        response.setHeader9("ಸಂಖ್ಯೆ  :  ರೇಸನಿ/ಗೂನಚ/"+ shortDistrictKannada +  "  /ಬೆಸ್ಥಿಅ/"+ apiResponse.getContent().get(0).getScCategoryName() +" /ಕ.ರೇಷ್ಮೆ/ಪ್ರೋಧನ/ "+ apiResponse.getContent().get(0).getArn() +" /"+ apiResponse.getContent().get(0).getFinancialYear() +"/ದಿನಾಂಕ : "+ datePart);
//        response.setHeader10("            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವಂತೆ      ತಾಂತ್ರಿ ಕ      ಸೇವಾ    ಕೇಂದ್ರ   "+ apiResponse.getContent().get(0).getLoggedinUserTscName() +"    ವ್ಯಾಪ್ತಿ ಯಲ್ಲಿ ರುವ   "+ apiResponse.getContent().get(0).getTotalReelers() +"   ಜನ   ರೇಷ್ಮೆ  ನೂಲು   ಬಿಚ್ಚಾ ಣಿಕೆದಾರರು" +
//                "ತಮ್ಮ     ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ      ಮೇಲ್ಕಂಡ    ಅನುಬಂಧದಲ್ಲಿ       ತೋರಿಸಿರುವಂತೆ   ಉತ್ಪಾ ದಿಸಿದ     "+ apiResponse.getContent().get(0).getNoOfRawSilkProduced() + "   ಕೆ.ಜಿ      ರೇಷ್ಗೆ    ಪ್ರ ತಿ     ಕೆ.ಜಿ.ಗೆ    ರೂ.  "+ apiResponse.getContent().get(0).getAmountPerKg() + " /-    ಗಳಂತೆ " +
//                "ಒಟ್ಟು     ರೂ.  "+ apiResponse.getContent().get(0).getSchemeAmount() + "  ( ರೂ.  "+ schemeAmountWords + "  ಮಾತ್ರ  ) ಗಳಿಗೆ   ಪ್ರೋ ತ್ಸಾ ಹಧನ ಮಂಜೂರು ಮಾಡಿದೆ. \n" +
//                "    ಈ    ವೆಚ್ಚ ವನ್ನು     ಲೆಕ್ಕ     ಶೀ ರ್ಷಿಕೆ    "+ apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription() +") ( "+ apiResponse.getContent().get(0).getScCategoryName() + ")  ಅಡಿ ಭರಿಸುವುದು.");

        response.setHeader10("              ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿರುವಂತೆ    ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,   ಗೂಡಿನ    ನಂತರದ    ಚಟುವಟಿಕೆ   "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() +",   ವ್ಯಾಪ್ತಿಯಲ್ಲಿರುವ      "
                + apiResponse.getContent().get(0).getTotalReelers() +"    ಜನ    ರೇಷ್ಮೆ   ನೂಲು   ಬಿಚ್ಚಾಣಿಕೆದಾರರು   ತಮ್ಮ    ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ     ಮೇಲ್ಕಂಡ     ಅನುಬಂಧದಲ್ಲಿ      "
                +"ತೋರಿಸಿರುವಂತೆ     ಉತ್ಪಾದಿಸಿದ      "+ totalRawSilkProducedFormatted +" ಕೆ.ಜಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ   ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ      "
                +"ರೂ."+ roundedAmountPerKg +"/-   ರಂತೆ    ಒಟ್ಟು      ರೂ.   "+ totalSchemeAmountFormatted +"/-   "
                +"(ರೂ.  "+ schemeAmountWords + "  ಮಾತ್ರ  ) ಗಳಿಗೆ    ಪ್ರೋತ್ಸಾಹಧನ  ಮಂಜೂರು   ಮಾಡಿದೆ. \n" +
                "                 ಈ    ವೆಚ್ಚ ವನ್ನು     ಲೆಕ್ಕ     ಶೀ ರ್ಷಿಕೆ    "+ apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription() +") ( "+ apiResponse.getContent().get(0).getScCategoryName() + ")  ಅಡಿ ಭರಿಸುವುದು.");

        totalRow.setMonth("");
        totalRow.setNumberOfBasins("");
        totalRow.setRenditta("");
        totalRow.setSilkExchangeName("");
        totalRow.setForm17jNo("");

        sanctionOrderResponseList.add(totalRow);

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    /**
     * Safely parse String to float. Returns 0f if null/empty/invalid.
     */
    private float safeParseFloat(String val) {
        if (val == null) return 0f;
        String t = val.trim();
        if (t.isEmpty()) return 0f;
        try {
            return Float.parseFloat(t);
        } catch (Exception e) {
            return 0f;
        }
    }

    /**
     * Helper to format float as string (you can customize decimals).
     */
    private String formatFloat(float value) {
        // If you want fixed 3 decimals like 1.000, use DecimalFormat
        // return new DecimalFormat("#0.000").format(value);
        return String.valueOf(value);
    }

    private JRBeanCollectionDataSource getDataSourceForPsfaReelingShed(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromPsfaReelingShed(requestDto);

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SanctionOrderResponse first = apiResponse.getContent().get(0);

        String admGovtDate        = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String releaseDate        = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String proposalDate       = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate       = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);


        String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        int schemeAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getSchemeAmount())));
        String schemeAmountWords = KannadaNumberUtil.convertNumberToKannadaWords(schemeAmount);


        response.setHeader(
                "ರೇಷ್ಮೆ     ಜಂಟಿ     ನಿರ್ದೇಶಕರು,     " + apiResponse.getContent().get(0).getDivisionName()
                        + "     ವಿಭಾಗ,     " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ರವರ     ನಡವಳಿಗಳು"
        );

        response.setHeader2("ವಿಷಯ");

        response.setHeader5(apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ    ಸಾಲಿನ    “" + apiResponse.getContent().get(0).getSchemeNameInKannada() + "”    ಅಡಿ   " + apiResponse.getContent().get(0).getScCategoryName() + "    ವರ್ಗಕ್ಕೆ    ಸೇರಿದ    ಶ್ರೀ./ಶ್ರೀಮತಿ.    " + apiResponse.getContent().get(0).getReelerName() + "    (" + apiResponse.getContent().get(0).getFruitsId()
                        + ")    ಬಿನ್/ಕೋಂ    "
                        + apiResponse.getContent().get(0).getReelerFatherName()
                        + "    "
                        + apiResponse.getContent().get(0).getVillageName()
                        + "    ,    "
                        + apiResponse.getContent().get(0).getHobliName()
                        + "    ,    ಹೋಬಳಿ,    "
                        + apiResponse.getContent().get(0).getTalukName()
                        + "    ತಾ.    "
                        + apiResponse.getContent().get(0).getDistrictName()
                        + "   ಜಿಲ್ಲೆ     ಇವರು    ನಿರ್ಮಿಸಿರುವ    "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡುವ    ಬಗ್ಗೆ."
        );


        response.setHeader3("ಉಲ್ಲೇಖ:     ");
        response.setHeader4("ಪೀಠಿಕೆ:     ");

        response.setHeader1(
                          "1. ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ     :     " + apiResponse.getContent().get(0).getAdmGovtOrder() + "     ದಿನಾಂಕ     :     " + admGovtDate + " \n\n"
                        + "2. ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName()+"     ರವರ     ಸುತ್ತೋಲೆ \n\n"
                        + "     ಸಂಖ್ಯೆ     :     " + apiResponse.getContent().get(0).getSchemeCircularNo() + "     ದಿನಾಂಕ     :     " + schemeCircularDate + "\n\n"
                        + "3. ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ     :     " + apiResponse.getContent().get(0).getDeptDeleNo() + "     ದಿನಾಂಕ     :     " + deptDeleDate + " \n\n"
                        + "4. " + apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ    ಸಾಲಿನ     ಗೂಡಿನ    ನಂತರದ   ಚಟುವಟಿಕೆ    ಕಾರ್ಯಕ್ರಮಗಳ     ಅನುಷ್ಠಾನಕ್ಕಾಗಿ \n\n" +
                        "      ಫಲಾನುಭವಿಗಳ    ಪಟ್ಟಿಯ ಅನುಮೋದನೆ   ದಿನಾಂಕ :  "+ proposalDate+"\n\n"
                        + "5. ರೇಷ್ಮೆ    ಕೃಷಿ    ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು    ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು, "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "   ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ \n\n"
                        + "     ಸಂಖ್ಯೆ   :  "+apiResponse.getContent().get(0).getSReleaseNo() +"   ದಿನಾಂಕ  :   " +sReleaseDate  + "\n\n"
                        + "6. ರೇಷ್ಮೆ   ಉಪ  ನಿರ್ದೇಶಕರು,  ಸರ್ಕಾರಿ  ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ,    "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName()+"    ಇವರ   ಪ್ರಸ್ತಾವನೆ    ದಿನಾಂಕ :  "+proposalDate+"");



        response.setHeader6(
                "              " + apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ    ಸಾಲಿನಲ್ಲಿ      ರೇಷ್ಮೆ       ಇಲಾಖೆಯ    ವಿವಿಧ    ಕಾರ್ಯಕ್ರಮಗಳ    ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ವಿವಿಧ    ಲೆಕ್ಕ    "
                        + "   ಶೀರ್ಷಿಕೆಗಳಡಿ    ಉಲ್ಲೇಖ (1)ರಲ್ಲಿ       ಸರ್ಕಾರವು       ಆಡಳಿತಾತ್ಮಕ     ಅನುಮೋದನೆಯನ್ನು       ನೀಡಿದ್ದು,    ಉಲ್ಲೇಖ(2)ರಲ್ಲಿ     "
                        +  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +  "    ನಿರ್ಮಾಣ    ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ಮಾರ್ಗಸೂಚಿಯನ್ನು      ನೀಡಲಾಗಿದೆ.    ಇಲಾಖೆಯು    "
                        + "“" + apiResponse.getContent().get(0).getSchemeNameInKannada() + "”    "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "    ಅಡಿ    "+  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಾಣಕ್ಕೆ      ನಿಗದಿಪಡಿಸಿದ      ಘಟಕ      ದರ/ಸಹಾಯಧನದ      ವಿವರ    ಇಂತಿದೆ;    "
        );



        response.setHeader7(
                "              ಶ್ರೀ./ಶ್ರೀಮತಿ.    " + apiResponse.getContent().get(0).getReelerName()
                        + "    (" + apiResponse.getContent().get(0).getFruitsId() + ")    ಬಿನ್/ಕೋಂ    "
                        + apiResponse.getContent().get(0).getReelerFatherName() + " ,   "
                        + apiResponse.getContent().get(0).getVillageName() + " ,   "
                        + apiResponse.getContent().get(0).getHobliName() + " ,  ಹೋಬಳಿ,   "
                        + apiResponse.getContent().get(0).getTalukName()
                        + "    ತಾ.    " + apiResponse.getContent().get(0).getDistrictName()
                        + "    ಜಿಲ್ಲೆ    ಇವರು    " + apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ    ಸಾಲಿಗೆ    ಉಲ್ಲೇಖ  (3)    ರನ್ವಯ    "+  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಾಣದ    "
                        + "ಆಯ್ಕೆಗೊಂಡ    ಫಲಾನುಭವಿಯಾಗಿರುತ್ತಾರೆ    (ಆಯ್ಕೆ    ಪಟ್ಟಿ    ಕ್ರಮ    ಸಂ.25,   "+  apiResponse.getContent().get(0).getLoggedinUserDistrictName() +"    ವ್ಯಾಪ್ತಿಯಲ್ಲಿ    ಸಂಖ್ಯೆ     "+  apiResponse.getContent().get(0).getSanctionNo() +"   ಸದರಿಯವರು    "
                        + apiResponse.getContent().get(0).getDistrictName() + "    ಜಿಲ್ಲೆ    "
                        + apiResponse.getContent().get(0).getTalukName()
                        + "    ತಾಲ್ಲೂಕು,    " + apiResponse.getContent().get(0).getReelingShedDetails()
                        + "    ಗ್ರಾಮದ    ಸ್ವತ್ತಿನ    ಸಂಖ್ಯೆ   :__________    ರ    "+  apiResponse.getContent().get(0).getRhSqft() +"    "
                        + "ಚ.ಮೀಟರ್    ನಿವೇಶನದಲ್ಲಿ      "+  apiResponse.getContent().get(0).getScComponentName() + "   ಚದರ    ಅಡಿಗಳ    "+  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಿಸಿದ್ದು,    ಇವರು   __________  ಕೊನೆಗಳ    "
                        + apiResponse.getContent().get(0).getNumberOfBasins()
                        + "    ಬೇಸಿನ್    ಸುಧಾರಿತ    "
                        + apiResponse.getContent().get(0).getMachineTypeName()
                        + "    ರೀಲಿಂಗ್    ಯಂತ್ರೋಪಕರಣ    ಅಳವಡಿಸಿರುತ್ತಾರೆ.    "
                        + "ಹಾಗೂ    ಸದರಿಯವರು    ಚಾಲ್ತಿಯಲ್ಲಿರುವ    ರೀಲಿಂಗ್    ರಹದಾರಿ    (ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getReelingLicenseNumber()
                        + ")    ಅನ್ನು    ಹೊಂದಿರುತ್ತಾರೆ    ಸದರಿಯವರು    "+  apiResponse.getContent().get(0).getScComponentName() +"    "
                        + "ಚದರ    ಅಡಿ    ನಿರ್ಮಾಣದ    ರೀಲೀಂಗ್    ಶೆಡ್  ಗೆ    ಸಹಾಯಧನ    ಪಡೆಯಲು    ಅರ್ಹರಿದ್ದು,    ನಿರ್ಮಿಸಿರುವ    "+  apiResponse.getContent().get(0).getScComponentName() +"    ಚ.ಅಡಿಗಳ    "
                        +  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ಅನ್ನು    "+  apiResponse.getContent().get(0).getScComponentName() +"    ಚ.ಅಡಿಗಳಿಗೆ    ಸೀಮಿತಗೊಳಿಸಿ,    ಘಟಕ    ದರ    ರೂ.    "
                        + apiResponse.getContent().get(0).getUnitPrice() + "/-    ಗಳ    ಶೇಕಡಾ    "+  apiResponse.getContent().get(0).getStateSharePercentage() +" ರ    ಸಹಾಯಧನ    ರೂ.    "
                        + apiResponse.getContent().get(0).getSchemeAmount()
                        + "/-    ಗಳನ್ನು     ಮಂಜೂರು    ಮಾಡುವಂತೆ    ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,    ತಾಂತ್ರಿಕ    ಸೇವಾ    ಕೇಂದ್ರ  (ರೀ),   "
                        + apiResponse.getContent().get(0).getLoggedinUserTscName()
                        + "    ಇವರು    ಸಲ್ಲಿಸಿರುವ    ಪ್ರಸ್ತಾವನೆಯು    ರೇಷ್ಮೆ    ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,    ಗೂ.ನಂ.ಚ.  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                        + "    ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,    ರೇ.ಗೂ.ಮಾ.    "
                        + apiResponse.getContent().get(0).getLoggedinUserDistrictName()
                        + "    ಇವರಿಂದ    ಉಲ್ಲೇಖ(5)ರಲ್ಲಿ    ಶಿಫಾರಸ್ಸುಗೊಂಡಿರುತ್ತದೆ. \n   "
                        + "              ಈ    ಪ್ರಸ್ತಾವನೆಯನ್ನು    ಉಲ್ಲೇಖ(2)ರ    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ    ಪರಿಶೀಲಿಸಲಾಗಿದ್ದು,    ಎಲ್ಲಾ     ಅಗತ್ಯ     ದಾಖಲೆಗಳನ್ನು     "
                        + "ಒಳಗೊಂಡಿರುತ್ತದೆ.    ಆದ್ದರಿಂದ,    ಶ್ರೀ./ಶ್ರೀಮತಿ.    " + apiResponse.getContent().get(0).getReelerName()
                        + "    (" + apiResponse.getContent().get(0).getFruitsId() + ")   ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getReelerFatherName() + " ,    " + apiResponse.getContent().get(0).getVillageName() + "  ,   "
                        + apiResponse.getContent().get(0).getHobliName() + " ,  ಹೋಬಳಿ,  " + apiResponse.getContent().get(0).getTalukName() + "  ತಾ.  " + apiResponse.getContent().get(0).getDistrictName()
                        + "   ಇವರಿಗೆ    "+  apiResponse.getContent().get(0).getScComponentName() +"    ಚದರ    ಅಡಿಗಳ    "+  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಾಣಕ್ಕೆ      ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡಬಹುದಾಗಿದೆ.    ಅದರಂತೆ    ಈ    ಆದೇಶ."
        );


        response.setHeader8("ಸಂಖ್ಯೆ  : ರೇಜಂನಿ/" + shortDistrictKannada + "ವಿ/ತಾಂ/" + apiResponse.getContent().get(0).getSubSchemeNameInKannada() + "/" + apiResponse.getContent().get(0).getReelerName() + "/" + apiResponse.getContent().get(0).getSanctionOrderNumber()+ "/ದಿನಾಂಕ : " + proposalDate);

        response.setHeader9(
                "         ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸಿದಂತೆ,    "
                        + apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ    ಸಾಲಿನ    “"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada()
                        + "”    "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "    ಅಡಿ    "+  apiResponse.getContent().get(0).getScComponentName() +"    ಚ.ಅಡಿ    "+  apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"   ನಿರ್ಮಾಣಕ್ಕೆ    ಉಲ್ಲೇಖ(3)ರ    ವಿತ್ತೀಯ    ಪ್ರತ್ಯಾಧಿಕಾರದನ್ವಯ    "
                        + "ಫಲಾನುಭವಿಗಳ    ಆಧಾರಿತ    ಕಾರ್ಯಕ್ರಮಗಳಿಗೆ    ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡಲು    ರೇಷ್ಮೆ    ಜಂಟಿ    ನಿರ್ದೇಶಕರು,    "
                        + apiResponse.getContent().get(0).getDivisionName() + "    ವಿಭಾಗ,    "
                        + apiResponse.getContent().get(0).getLoggedinUserDistrictName()
                        + "    ರವರಿಗೆ    ಪೂರ್ಣ    ಅಧಿಕಾರವಿದ್ದು,    ಈ    ಕೆಳಗೆ    ವಿವರಿಸಿದಂತೆ    ಮಂಜೂರಾತಿ    ನೀಡಿದೆ. "
        );

        response.setHeader10(
                "("+schemeAmountWords+") \n  "
                        + "              ಸದರಿ    ಮೊತ್ತವನ್ನು    “"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada()
                        + "”    "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ:    "
                        + apiResponse.getContent().get(0).getScHeadAccountName()
                        + "    ಯೋಜನೆ    ಅಡಿಯಲ್ಲಿ    ಭರಿಸುವುದು.    ಖಜಾನೆ-2    ರಲ್ಲಿ    ಸಹಾಯಧನದ    ಮೊತ್ತವನ್ನು    "
                        + "ಡಿಬಿಟಿ    ಮುಖಾಂತರ    ಫಲಾನುಭವಿ    ಖಾತೆಗೆ    ಜಮಾ    ಮಾಡುವುದು."
        );


        response.setLineItemComment("ರೇಷ್ಮೆ     ಜಂಟಿ     ನಿರ್ದೇಶಕರು\n\n" + apiResponse.getContent().get(0).getDivisionName() + "   ವಿಭಾಗ,   " + apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        response.setHeader11("ಇವರಿಗೆ,\n\n"
                        + "ರೇಷ್ಮೆ   ಸಹಾಯಕ   ನಿರ್ದೇಶಕರು,\n\n"
                +"ಗೂಡಿನ   ನಂತರದ   ಚಟುವಟಿಕೆ,   "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() +"\n\n"
                +"ಪ್ರತಿಯನ್ನು   ;\n\n"
        +"1. ಸಂಬಂಧಿಸಿದ  ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು,\n\n"
                +"2. ರೇಷ್ಮೆ    ಉಪ  ನಿರ್ದೇಶಕರು,    ರೇಷ್ಮೆ   ಗೂಡು   ಮಾರುಕಟ್ಟೆ,   "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() +"\n\n"
        +"3. ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು, ತಾಂತ್ರಿಕ   ಸೇವಾಕೇಂದ್ರ (ರೀಲಿಂಗ್), "+ apiResponse.getContent().get(0).getLoggedinUserTscName() +"");

        // ================== REST (UNCHANGED LOGIC) ==================

        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());

        response.setMachineTypeName(apiResponse.getContent().get(0).getMachineTypeName());
        response.setScCategoryName(apiResponse.getContent().get(0).getScCategoryName());

        response.setRenditta(apiResponse.getContent().get(0).getRenditta());
        response.setDailyLimit(apiResponse.getContent().get(0).getDailyLimit());
        response.setNumberOfBasins(apiResponse.getContent().get(0).getNumberOfBasins());
        response.setMax(apiResponse.getContent().get(0).getMax());

        response.setIncentiveAmountPerKg(apiResponse.getContent().get(0).getIncentiveAmountPerKg());

        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        response.setSerialNumber(1);

        sanctionOrderResponseList.add(response);

        float totalNoOfCocoonsNeedToProduce = 0f;
        float totalNoOfRawSilkProduced      = 0f;
        float totalMachineQuantity          = 0f;
        float totalMax                      = 0f;
        float totalSchemeAmount             = 0f;

        if (apiResponse.getContent() != null) {
            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {

                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName("");
                }

                if (sanctionOrderResponse.getReelerName() == null) {
                    sanctionOrderResponse.setReelerName("");
                }

                if (sanctionOrderResponse.getSchemeAmount() == null) {
                    sanctionOrderResponse.setSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getUnitPrice() == null) {
                    sanctionOrderResponse.setUnitPrice(0f);
                }
                if (sanctionOrderResponse.getScComponentName() == null) {
                    sanctionOrderResponse.setScComponentName("");
                }

                String reelerDetails =
                        "ಶ್ರೀ./ಶ್ರೀಮತಿ.    " + sanctionOrderResponse.getReelerName()
                                + "    (" + sanctionOrderResponse.getFruitsId() + ")    ಬಿನ್/ಕೋಂ    "
                                + sanctionOrderResponse.getReelerFatherName()
                                + "    "
                                + sanctionOrderResponse.getVillageName()
                                + "    ,    "
                                + sanctionOrderResponse.getHobliName()
                                + "    ,    ಹೋಬಳಿ,    "
                                + sanctionOrderResponse.getTalukName()
                                + "    ತಾ.    "
                                + sanctionOrderResponse.getDistrictName()
                                + "   ಜಿಲ್ಲೆ   ";

                sanctionOrderResponse.setReelerDetails(reelerDetails);


                sanctionOrderResponse.setSerialNumber(serialNo++);

                totalNoOfCocoonsNeedToProduce += safeParseFloat(sanctionOrderResponse.getNoOfCocoonsNeedToProduce());
                totalNoOfRawSilkProduced      += safeParseFloat(sanctionOrderResponse.getNoOfRawSilkProduced());
                totalMachineQuantity          += (sanctionOrderResponse.getMachineQuantity() == null ? 0f : sanctionOrderResponse.getMachineQuantity());
                totalMax                      += (sanctionOrderResponse.getMax() == null ? 0f : sanctionOrderResponse.getMax());
                totalSchemeAmount             += (sanctionOrderResponse.getSchemeAmount() == null ? 0f : sanctionOrderResponse.getSchemeAmount());

                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }

        SanctionOrderResponse totalRow = new SanctionOrderResponse();

        totalRow.setReelerName("ಒಟ್ಟು");
        totalRow.setSerialNumber(null);

        totalRow.setNoOfCocoonsNeedToProduce(formatFloat(totalNoOfCocoonsNeedToProduce));
        totalRow.setNoOfRawSilkProduced(formatFloat(totalNoOfRawSilkProduced));
        totalRow.setMachineQuantity(totalMachineQuantity);
        totalRow.setMax(totalMax);
        totalRow.setSchemeAmount(totalSchemeAmount);

        totalRow.setMonth("");
        totalRow.setNumberOfBasins("");
        totalRow.setRenditta("");
        totalRow.setSilkExchangeName("");
        totalRow.setForm17jNo("");

        sanctionOrderResponseList.add(totalRow);

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }


    private JRBeanCollectionDataSource getDataSourceForPsfaHRU(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromPsfaReelingShed(requestDto);

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SanctionOrderResponse first = apiResponse.getContent().get(0);

        String admGovtDate        = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String releaseDate        = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String proposalDate       = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate       = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);


        String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        int schemeAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getSchemeAmount())));
        String schemeAmountWords = KannadaNumberUtil.convertNumberToKannadaWords(schemeAmount);


        response.setHeader("ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     ಗೂಡಿನ     ನಂತರದ     ಚಟುವಟಿಕೆ,     " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ರವರ     ಕಛೇರಿ     ನಡವಳಿಗಳು");
        response.setHeader2("ವಿಷಯ:");
        response.setHeader5(
                apiResponse.getContent().get(0).getFinancialYear() + "   ನೇ   ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "  " + apiResponse.getContent().get(0).getScCategoryName() +
                        "ಅಡಿ    ಶ್ರೀ./ಶ್ರೀಮತಿ.    "+ apiResponse.getContent().get(0).getReelerName() + " (" + apiResponse.getContent().get(0).getFruitsId() + ")    ಬಿನ್/ಕೋಂ    " + apiResponse.getContent().get(0).getReelerFatherName() + ",  " + apiResponse.getContent().get(0).getVillageName() + ",  " + apiResponse.getContent().get(0).getHobliName() + ","
                        +"ಹೋಬಳಿ,    "+ apiResponse.getContent().get(0).getTalukName() + "    ತಾ.   " + apiResponse.getContent().get(0).getDistrictName() + "    ಜಿಲ್ಲೆ      ಇವರು    ತಮ್ಮ     ರೀಲಿಂಗ್     ಘಟಕದಲ್ಲಿ     " + apiResponse.getContent().get(0).getSubSchemeNameInKannada()  + "    ಅಳವಡಿಕೆಗೆ"
                +"ಸಹಾಯಧನ   ಮಂಜೂರು   ಮಾಡುವ   ಬಗ್ಗೆ.");

        response.setHeader3("ಉಲ್ಲೇಖ:");

        response.setHeader4("ಪೀಠಿಕೆ:");

        response.setHeader1(
                "1.  ಸರ್ಕಾರದ  ಆದೇಶ  ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder()
                        + "  ದಿನಾಂಕ  :  " + admGovtDate + " \n\n"
                        + "2.  ರೇಷ್ಮೆ  ಕೃಷಿ  ಅಭಿವೃದ್ದಿ  ಆಯುಕ್ತರು  ಹಾಗೂ  ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರು,  ಬೆಂಗಳೂರು  ರವರ  ಸುತ್ತೋಲೆ  \n\n"
                        + "      ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getSchemeCircularNo() + "  ದಿನಾಂಕ  :  " + schemeCircularDate + " \n\n"
                        + "3.  ಸರ್ಕಾರದ  ಆದೇಶ  ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getDeptDeleNo() + "  ದಿನಾಂಕ  :  " + deptDeleDate + " \n\n"
                        + "4.  " + apiResponse.getContent().get(0).getFinancialYear() + "   ನೇ   ಸಾಲಿನ   ಗೂಡಿನ   ನಂತರದ    ಚಟುವಟಿಕೆ    ಕಾರ್ಯಕ್ರಮಗಳ    ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ಫಲಾನುಭವಿಗಳ    ಆಯ್ಕೆ \n\n"
                        + "        ಸಮಿತಿ    ಸಭೆಯ    ನಡವಳಿಗಳು,    ದಿನಾಂಕ  :  "+proposalDate + " \n\n"
                        + "5.  ರೇಷ್ಮೆ    ಕೃಷಿ    ಅಭಿವೃದ್ದಿ      ಆಯುಕ್ತರು    ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,  " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "   ರವರ    ಸುತ್ತೋಲೆ    ಸಂಖ್ಯೆ  :\n\n"
                        +"       "+ apiResponse.getContent().get(0).getSReleaseNo()  + "  ದಿನಾಂಕ  :  " + sReleaseDate +"\n\n"
                        + "6.  ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,    ತಾಂತ್ರಿಕ    ಸೇವಾ    ಕೇಂದ್ರ   (ರೀಲಿಂಗ್),  " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ಇವರ  ಪ್ರಸ್ತಾವನೆ  ದಿನಾಂಕ  :  " + proposalDate);

        response.setHeader6(
                "              " + apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ    ಸಾಲಿನಲ್ಲಿ    ಉಲ್ಲೇಖ(1)ರ    ಸರ್ಕಾರಿ    ಆದೇಶ    ಹಾಗೂ    ಉಲ್ಲೇಖ(2)ರ    ಮೂಲಕ    ರೇಷ್ಮೆ    ಕೃಷಿ    ಅಭಿವೃದ್ದಿ    "
                        + "ಆಯುಕ್ತರು    ಹಾಗೂ    ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು,    " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "    ರವರು    ”"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada() + "”    "
                        + apiResponse.getContent().get(0).getScCategoryName() + "    ಅಡಿ   "
                        + apiResponse.getContent().get(0).getSubSchemeNameInKannada() + "    "
                        + "ಅಳವಡಿಕೆಗೆ    ಘಟಕ    ದರ    ರೂ.    " + apiResponse.getContent().get(0).getUnitPrice() + "/-    ಗಳಿಗೆ    ಶೇ.    "
                        + apiResponse.getContent().get(0).getStateSharePercentage() + "  "+apiResponse.getContent().get(0).getScCategoryName()+"   ರಂತೆ    ರೂ. "+apiResponse.getContent().get(0).getSchemeAmount()+"/-    ಸಹಾಯಧನ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮವನ್ನು    ಅನುಷ್ಠಾನಗೊಳಿಸಲು    "
                        + "ಮಾರ್ಗಸೂಚಿಯನ್ನು   ನೀಡಿರುತ್ತಾರೆ\n"
                        + "ಉಲ್ಲೇಖ(5)    ರಲ್ಲಿ    ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,    ತಾಂತ್ರಿಕ    ಸೇವಾ    ಕೇಂದ್ರ    "
                        + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "    ಇವರು    ಶ್ರೀ./ಶ್ರೀಮತಿ.    "
                        + apiResponse.getContent().get(0).getReelerName() + "(" + apiResponse.getContent().get(0).getFruitsId() + ")    ಬಿನ್/ಕೋಂ    "
                        + apiResponse.getContent().get(0).getReelerFatherName() + "    ,    " + apiResponse.getContent().get(0).getVillageName()
                        + "    ,    " + apiResponse.getContent().get(0).getHobliName() + "  ,    ಹೋಬಳಿ,    "
                        + apiResponse.getContent().get(0).getTalukName() + "    ತಾ.    "
                        + apiResponse.getContent().get(0).getDistrictName() + "    ಇವರು    "
                        + apiResponse.getContent().get(0).getScCategoryName() + "    ವರ್ಗಕ್ಕೆ    ಸೇರಿದ್ದು    ,    ರೀಲಿಂಗ್    ರಹದಾರಿ    ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getReelingLicenseNumber() + "    "
                        + "ರಲ್ಲಿ    " + apiResponse.getContent().get(0).getNumberOfBasins() + "    ಬೇಸಿನ್    "
                        + apiResponse.getContent().get(0).getMachineTypeName() + "    ರೀಲಿಂಗ್    ಘಟಕ    ಹೊಂದಿದ್ದು    ,    "
                        + "ಇವರು    " + apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ    ಸಾಲಿಗೆ    ಉಲ್ಲೇಖ(3)    ರನ್ವಯ    " + apiResponse.getContent().get(0).getSubSchemeNameInKannada());

        response.setHeader7("    ಅಳವಡಿಸಲು    ಆಯ್ಕೆಗೊಂಡ    ಫಲಾನುಭವಿಯಾಗಿರುತ್ತಾರೆ    (ಆಯ್ಕೆ    ಪಟ್ಟಿ    ಕ್ರಮ    ಸಂ."+ apiResponse.getContent().get(0).getSReleaseNo()+",    " + apiResponse.getContent().get(0).getFinancialYear()+
                " ವ್ಯಾಪ್ತಿಯಲ್ಲಿ    ಸಂಖ್ಯೆ    4 ತಮ್ಮ    ರೀಲಿಂಗ್    ಘಟಕದಲ್ಲಿ    "+ apiResponse.getContent().get(0).getVendorName()+",   ಶಿಡ್ಲಘಟ್ಟ     ಇವರ   ಟ್ಯಾಕ್ಸ್     ಇನ್ವಾಯ್ಸ್     ಸಂ:   848     ದಿನಾಂಕ     28/07/2025     ರಂತೆ     ರೂ.    "+ apiResponse.getContent().get(0).getUnitPrice()+"/-     ಗಳ     ವೆಚ್ಚದಲ್ಲಿ     "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"     ಘಟಕ     ಖರೀದಿಸಿ     ಅಳವಡಿಸಿದ್ದು     ಸಹಾಯಧನ     ಮಂಜೂರಾತಿಗಾಗಿ     ಅಗತ್ಯ     ದಾಖಲಾತಿಗಳೊಂದಿಗೆ     ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಸಲ್ಲಿಸಿದ್ದಾರೆ.     ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಿದ್ದು     ಉಲ್ಲೇಖ(2)ರ     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗು     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು   " +apiResponse.getContent().get(0).getLoggedinUserDistrictName() +
                        "     ರವರ     ಸುತ್ತೋಲೆಯ     ಮಾರ್ಗಸೂಚಿಯಂತೆ     “"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada() +
                        "”     "
                        + apiResponse.getContent().get(0).getScCategoryName() +
                        "     ಅಡಿ     ಸಹಾಯಧನ     ಪಡೆಯಲು     ಅರ್ಹರಾಗಿರುತ್ತಾರೆ.     ಉಲ್ಲೇಖ(3)ರ     ಸರ್ಕಾರಿ     ಆದೇಶದ     ರೀತಿ     ಈ     ಕಛೇರಿಯ     ಅಧಿಕಾರ     ಪ್ರತ್ಯಾಯೋಜನೆ     ವ್ಯಾಪ್ತಿಯಲ್ಲಿ     ಇದ್ದು,     ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ     ಸದರಿ     ಕಾರ್ಯಕ್ರಮದ     ಅನುಷ್ಠಾನಕ್ಕಾಗಿ     ನೀಡಿರುವ     ಮಾರ್ಗಸೂಚಿಯನ್ವಯ     ಸಹಾಯಧನ     ಮಂಜೂರು     ಮಾಡಲು     ಅನುದಾನ     ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿದೆ.     ಅದರಂತೆ     ಈ     ಕೆಳಕಂಡ     ಮಂಜೂರಾತಿ     ಆದೇಶ     ಹೊರಡಿಸಿದೆ."
        );

        response.setHeader8(
                "ಸಂಖ್ಯೆ  :  ರೇಸನಿ/" + shortDistrictKannada + "/"
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "/"
                        + apiResponse.getContent().get(0).getUnitPrice() + "/"
                        + apiResponse.getContent().get(0).getScCategoryName() + "/"+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"/"
                        + apiResponse.getContent().get(0).getArn() + "/" + apiResponse.getContent().get(0).getFinancialYear()
                        + "   ದಿನಾಂಕ  :  " + proposalDate
        );

        response.setHeader9("ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿದಂತೆ,   "+ apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ   ಸಾಲಿನ   “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”     "+ apiResponse.getContent().get(0).getScCategoryName() +"   ಅಡಿ   ರೇಷ್ಮೆ   ನೂಲು   ಬಿಚ್ಚಾಣಿಕೆದಾರರು"+
                ""+ apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"    ಅಳವಡಿಕೆಗೆ     ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ಧಿ     ಅಯುಕ್ತರು     ಹಾಗು     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು     ಅನುದಾನ     ಬಿಡುಗಡೆ     ಗೊಳಿಸಿದ್ದು,     ಉಲ್ಲೇಖ(3)ರ     ವಿತ್ತೀಯ    ಪ್ರತ್ಯಾಧಿಕಾರದನ್ವಯ     ಫಲಾನುಭವಿಗಳ"+
                        "ಆಧಾರಿತ     ಕಾರ್ಯಕ್ರಮಗಳಿಗೆ     ಸಹಾಯಧನ     ಮಂಜೂರು     ಮಾಡಲು     ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,   ಗೂಡಿನ    ನಂತರದ    ಚಟುವಟಿಕೆ     ಮಾರುಕಟ್ಟೆ,     "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() +"    ರವರಿಗೆ     ಪೂರ್ಣ     ಅಧಿಕಾರವಿದ್ದು,     ಈ"+
                        "ಕೆಳಗೆ     ವಿವರಿಸಿದಂತೆ     ಮಂಜೂರಾತಿ     ನೀಡಿದೆ.");

        response.setHeader10(
                "(ರೂ.     " + schemeAmountWords + "     ಮಾತ್ರ)\n"
                        + "ಮೇಲ್ಕಂಡ     ಫಲಾನುಭವಿಯು     ತನ್ನ     ಪಾಲಿನ     ಮೊತ್ತವನ್ನು     ಸಂಬಂಧಿತ     ಸಂಸ್ಥೆಗೆ     ಪಾವತಿಸಿರುವುದರಿಂದ,"
                        + "ಸಹಾಯಧನದ     ಮೊತ್ತ     ರೂ.     " +apiResponse.getContent().get(0).getSchemeAmount()
                        + "/-     ಗಳನ್ನು     "
                        + apiResponse.getContent().get(0).getVendorName()
                        + "     ಸಂಸ್ಥೆಗೆ     ಪಾವತಿಸಲು     ಆದೇಶಿಸಲಾಗುತ್ತದೆ."
                        + "ಸದರಿ     ವೆಚ್ಚವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ     "
                        + apiResponse.getContent().get(0).getScHeadAccountName()
                        + " ("+apiResponse.getContent().get(0).getDescription()+")    ಯೋಜನೆ     ಅಡಿಯಲ್ಲಿ     ಭರಿಸುವುದು."

                        + "ರೂ.     ಹದಿನೆಂಟು     ಸಾವಿರದ     ಎರಡು     ನೂರ     ಐವತ್ತು     ಮಾತ್ರ  "
                        + "ಮೇಲ್ಕಂಡ     ಫಲಾನುಭವಿಯು     ತನ್ನ     ಪಾಲಿನ     ಮೊತ್ತವನ್ನು     ಸಂಬಂಧಿಸಿದ     ಸಂಸ್ಥೆಗೆ     ಪಾವತಿಸಿರುತ್ತಾರೆ.     ಆದುದರಿಂದ     ಸಹಾಯಧನದ     ಮೊತ್ತ     ರೂ.     "+ apiResponse.getContent().get(0).getSchemeAmount()+"/-     ಗಳನ್ನು     "+ apiResponse.getContent().get(0).getVendorName()+",     (Empanelled     vender     Address)     ಸಂಸ್ಥೆಗೆ     ಪಾವತಿಸುವುದು.     ನಿಯಮಾನುಸಾರ     ಕಡಿತಗಳನ್ನು     ಕಟಾಯಿಸಿ     ಉಳಿಕೆ     ಮೊತ್ತವನ್ನು     ಪಾವತಿಸುವುದು."
                        + "ಸದರಿ     ವೆಚ್ಚವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ      "+ apiResponse.getContent().get(0).getScHeadAccountName() +"    (" + apiResponse.getContent().get(0).getDescription()+")     " + apiResponse.getContent().get(0).getScCategoryName()+"    ಅಡಿ     ಭರಿಸುವುದು.");


        // SIGNATURE BLOCK
        response.setLineItemComment(
                "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು"
                        + "ಗೂಡಿನ   ನಂತರದ   ಚಟುವಟಿಕೆ,  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
        );

        // COPY-TO BLOCK
        response.setHeader11(
                "ಈ  ಕಚೇರಿಯ  ಲೆಕ್ಕ    ಶಾಖೆಗೆ\n"
                        + "ಇವರಿಗೆ,\n"
                        + "ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,\n"
                +"ತಾಂತ್ರಿಕ   ಸೇವಾ  ಕೇಂದ್ರ   (ರೀಲಿಂಗ್),\n"
                        + apiResponse.getContent().get(0).getLoggedinUserTscName());
        

        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());

        response.setMachineTypeName(apiResponse.getContent().get(0).getMachineTypeName());
        response.setScCategoryName(apiResponse.getContent().get(0).getScCategoryName());

        response.setRenditta(apiResponse.getContent().get(0).getRenditta());
        response.setDailyLimit(apiResponse.getContent().get(0).getDailyLimit());
        response.setNumberOfBasins(apiResponse.getContent().get(0).getNumberOfBasins());
        response.setMax(apiResponse.getContent().get(0).getMax());

        response.setIncentiveAmountPerKg(apiResponse.getContent().get(0).getIncentiveAmountPerKg());

        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        response.setSerialNumber(1);

        sanctionOrderResponseList.add(response);

        float totalNoOfCocoonsNeedToProduce = 0f;
        float totalNoOfRawSilkProduced      = 0f;
        float totalMachineQuantity          = 0f;
        float totalMax                      = 0f;
        float totalSchemeAmount             = 0f;

        if (apiResponse.getContent() != null) {
            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {


                if (sanctionOrderResponse.getVendorName() == null) {
                    sanctionOrderResponse.setVendorName("");
                }
                if (sanctionOrderResponse.getMonth() == null) {
                    sanctionOrderResponse.setMonth("");
                }
                if (sanctionOrderResponse.getNumberOfBasins() == null) {
                    sanctionOrderResponse.setNumberOfBasins("");
                }
                if (sanctionOrderResponse.getUnitPrice() == null) {
                    sanctionOrderResponse.setUnitPrice(0f);
                }
                if (sanctionOrderResponse.getNoOfRawSilkProduced() == null) {
                    sanctionOrderResponse.setNoOfRawSilkProduced("");
                }
                if (sanctionOrderResponse.getMachineQuantity() == null) {
                    sanctionOrderResponse.setMachineQuantity(0f);
                }
                if (sanctionOrderResponse.getSchemeAmount() == null) {
                    sanctionOrderResponse.setSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getMachineTypeName() == null) {
                    sanctionOrderResponse.setMachineTypeName("");
                }
                if (sanctionOrderResponse.getScCategoryName() == null) {
                    sanctionOrderResponse.setScCategoryName("");
                }
                if (sanctionOrderResponse.getRenditta() == null) {
                    sanctionOrderResponse.setRenditta("");
                }
                if (sanctionOrderResponse.getDailyLimit() == null) {
                    sanctionOrderResponse.setDailyLimit("");
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }

                String reelerDetails =
                        "ಶ್ರೀ./ಶ್ರೀಮತಿ.    " + sanctionOrderResponse.getReelerName()
                                + "    (" + sanctionOrderResponse.getFruitsId() + ")    ಬಿನ್/ಕೋಂ    "
                                + sanctionOrderResponse.getReelerFatherName()
                                + "    "
                                + sanctionOrderResponse.getVillageName()
                                + "    ,    "
                                + sanctionOrderResponse.getHobliName()
                                + "    ,    ಹೋಬಳಿ,    "
                                + sanctionOrderResponse.getTalukName()
                                + "    ತಾ.    "
                                + sanctionOrderResponse.getDistrictName()
                                + "   ಜಿಲ್ಲೆ   ";

                sanctionOrderResponse.setReelerDetails(reelerDetails);

                sanctionOrderResponse.setSerialNumber(serialNo++);

                totalNoOfCocoonsNeedToProduce += safeParseFloat(sanctionOrderResponse.getNoOfCocoonsNeedToProduce());
                totalNoOfRawSilkProduced      += safeParseFloat(sanctionOrderResponse.getNoOfRawSilkProduced());
                totalMachineQuantity          += (sanctionOrderResponse.getMachineQuantity() == null ? 0f : sanctionOrderResponse.getMachineQuantity());
                totalMax                      += (sanctionOrderResponse.getMax() == null ? 0f : sanctionOrderResponse.getMax());
                totalSchemeAmount             += (sanctionOrderResponse.getSchemeAmount() == null ? 0f : sanctionOrderResponse.getSchemeAmount());

                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }

        SanctionOrderResponse totalRow = new SanctionOrderResponse();

        totalRow.setReelerName("ಒಟ್ಟು");
        totalRow.setSerialNumber(null);

        totalRow.setNoOfCocoonsNeedToProduce(formatFloat(totalNoOfCocoonsNeedToProduce));
        totalRow.setNoOfRawSilkProduced(formatFloat(totalNoOfRawSilkProduced));
        totalRow.setMachineQuantity(totalMachineQuantity);
        totalRow.setMax(totalMax);
        totalRow.setSchemeAmount(totalSchemeAmount);

        totalRow.setMonth("");
        totalRow.setNumberOfBasins("");
        totalRow.setRenditta("");
        totalRow.setSilkExchangeName("");
        totalRow.setForm17jNo("");

        sanctionOrderResponseList.add(totalRow);

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    // Use this at class level (outside any other method)
    private String formatCreatedDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            return output.format(input.parse(dateTime));
        } catch (Exception e) {
            // log the error if you have a logger
            // log.error("Error parsing createdDate: {}", dateTime, e);
            return "";
        }
    }


    private JRBeanCollectionDataSource getDataSourceReelingShedWorkOrder(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromPsfaReelingShed(requestDto);

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String createdDate = formatCreatedDateTime(apiResponse.getContent().get(0).getCreatedDate());





        String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        int schemeAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getSchemeAmount())));
        String schemeAmountWords = KannadaNumberUtil.convertNumberToKannadaWords(schemeAmount);




        response.setHeader2("ಕಾರ್ಯಾದೇಶ");

        response.setHeader(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "  ನೇ  ಸಾಲಿನಲ್ಲಿ  “"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada()
                        + "”  "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "  ಅಡಿ  "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"  ನಿರ್ಮಾಣಕ್ಕೆ    ಸಹಾಯಧನ."
        );

        response.setHeader3("ಸಂಖ್ಯೆ  : ಕೇಂದ್ರ  ವಲಯ/"+apiResponse.getContent().get(0).getSchemeNameInKannada()+"  /"  +apiResponse.getContent().get(0).getScCategoryName()+ "/" +apiResponse.getContent().get(0).getWorkOrderNumber());
        response.setHeader4("ದಿನಾಂಕ : " + createdDate);

        response.setHeader6(
                "                  ಶ್ರೀ/ಶ್ರೀಮತಿ    " + apiResponse.getContent().get(0).getReelerName() + "    ಬಿನ್/ಕೋಂ    " + apiResponse.getContent().get(0).getReelerFatherName() + " ,    "
                        + apiResponse.getContent().get(0).getVillageName()
                        + "  ,    "
                        + apiResponse.getContent().get(0).getHobliName()
                        + "  ,    "
                        + apiResponse.getContent().get(0).getTalukName()
                        + "    ತಾ.    "
                        + apiResponse.getContent().get(0).getDistrictName()
                        + "    ಜಿಲ್ಲೆ    ಇವರು    "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "    ವರ್ಗಕ್ಕೆ    ಸೇರಿರುವರಾಗಿದ್ದು,    ರೀಲಿಂಗ್    ಪರವಾನಗಿ    ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getReelingLicenseNumber()
                        + "    ಅನ್ನು    ಹೊಂದಿರುತ್ತಾರೆ.    ಸದರಿಯವರು   "
                        + apiResponse.getContent().get(0).getNumberOfBasins()
                        + "    ಬೇಸಿನ್‌    "
                        + apiResponse.getContent().get(0).getMachineTypeName()
                        + "    ರೀಲಿಂಗ್    ಘಟಕವನ್ನು    ಸ್ಥಾಪಿಸಿದ್ದು,   "+ apiResponse.getContent().get(0).getScComponentName()+ "   ಚದರ    ಅಡಿ    ವಿಸ್ತೀರ್ಣದ    "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"   ನಿರ್ಮಾಣಕ್ಕಾಗಿ    ಸಹಾಯಧನ    ಕೋರಿ    ARN    ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getArn()
                        + "    ರಂತೆ    ಅರ್ಜಿಯನ್ನು    ಸಲ್ಲಿಸಿದ್ದಾರೆ.    ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿ    ತಾಂತ್ರಿಕ    ಸೇವಾ    ಕೇಂದ್ರ    (ರೀಲಿಂಗ್),    "
                        + apiResponse.getContent().get(0).getLoggedinUserTscName()
                        + "  ರವರು    ಅರ್ಜಿ    ಮತ್ತು    ದಾಖಲಾತಿಗಳ    ಪೂರ್ವಪರಿಶೀಲನೆಯನ್ನು    ದಿನಾಂಕ:    "
                        + formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf)
                        + "    ರಂದು    ಕೈಗೊಂಡಿದ್ದು,    ಅರ್ಜಿದಾರರು    ಸರ್ವೇ    ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getSurveyNumber()
                        + "    ಯ    ಜಾಗದಲ್ಲಿ      "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"   ನಿರ್ಮಾಣಕ್ಕಾಗಿ    ಕಾರ್ಯಕ್ರಮದ    ಸೌಲಭ್ಯಕ್ಕಾಗಿ    ಅರ್ಹತೆಯ    ಬಗ್ಗೆ    ದೃಢಪಡಿಸಿರುತ್ತಾರೆ.    ಅದರಂತೆ,    ಅರ್ಜಿದಾರರು    ಮೇಲ್ಕಾಣಿಸಿದ    ಸರ್ವೇ    ಸಂಖ್ಯೆಯ    ಜಾಗದಲ್ಲಿ    ಇಲಾಖೆಯ    ಮಾರ್ಗಸೂಚಿಗಳಂತೆ    ರೀಲಿಂಗ್    ಶೆಡ್    ನಿರ್ಮಾಣಕ್ಕಾಗಿ    ಕಾರ್ಯಾದೇಶ    ನೀಡಲಾಗಿದೆ.\n"+
                        "                  "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"    ಪೂರ್ಣಗೊಳಿಸಿ    ಅಗತ್ಯ    ದಾಖಲಾತಿಗಳೊಂದಿಗೆ    ಪ್ರಸ್ತಾವನೆ    ಸಲ್ಲಿಸಿದ    ನಂತರ    ಸಹಾಯಧನ    ಮಂಜೂರಾತಿಗಾಗಿ    ಕ್ರಮ    ಕೈಗೊಳ್ಳಲಾಗುವುದು.    "+ apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"    ನಿರ್ಮಾಣದಲ್ಲಿ    ಮಾರ್ಗಸೂಚಿಯ    ಉಲ್ಲಂಘನೆ    ಕಂಡುಬಂದಲ್ಲಿ,    ಈ    ಕಾರ್ಯಾದೇಶವನ್ನು    ರದ್ದುಪಡಿಸುವ    ಅಧಿಕಾರವನ್ನು    ಇಲಾಖೆ    ಹೊಂದಿರುತ್ತದೆ."
        );


        // Signature – as per reeling shed pdf (ಸರ್ಕಾರಿ ರೇಷ್ಮೆ ಗೂಡಿನ ಮಾರುಕಟ್ಟೆ, ಕೊಳ್ಳೇಗಾಲ) :contentReference[oaicite:6]{index=6}
        response.setLineItemComment(
                "ರೇಷ್ಮೆ   ಉಪ   ನಿರ್ದೇಶಕರು,\nಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ,  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
        );

        response.setHeader11(
                "ಇವರಿಗೆ,\n"
                        + "1.  ರೇಷ್ಮೆ  ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,  ಗೂಡಿನ  ನಂತರದ  ಚಟುವಟಿಕೆ,  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                        + "\n"
                        + "2.  ರೇಷ್ಮೆ  ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,  ತಾಂತ್ರಿಕ  ಸೇವಾ  ಕೇಂದ್ರ  (ರೀಲಿಂಗ್),  "
                        + apiResponse.getContent().get(0).getLoggedinUserTscName()
                        + "\n"
                        + "3.  ಸಂಬಂಧಿಸಿದ    ರೇಷ್ಮೆ     ನೂಲುಬಿಚ್ಚಾಣೆಕೆದಾರರಿಗೆ,"
        );
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");

        sanctionOrderResponseList.add(response);
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }



    private JRBeanCollectionDataSource getDataSourceHRUWorkOrder(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromPsfaReelingShed(requestDto);

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SanctionOrderResponse first = apiResponse.getContent().get(0);

        String admGovtDate        = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String proposalDate       = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate       = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);

        // Same as Reeling Shed – use createdDate (yyyy-MM-dd HH:mm:ss.SSS) -> dd/MM/yyyy
        String createdDate        = formatCreatedDateTime(apiResponse.getContent().get(0).getCreatedDate());

        String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        int schemeAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getSchemeAmount())));
        String schemeAmountWords = KannadaNumberUtil.convertNumberToKannadaWords(schemeAmount);

        // ---------- HEADER & TITLE (same style as Reeling Shed Work Order) ----------

        response.setHeader2("ಕಾರ್ಯಾದೇಶ");

        // Subject line – from HRU Work Order PDF
        response.setHeader(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "  ನೇ  ಸಾಲಿನಲ್ಲಿ  “"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada()
                        + "”  "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "  ಅಡಿ  ರೇಷ್ಮೆ  ನೂಲು  ಬಿಚ್ಚಾಣಿಕೆದಾರರು  ತಮ್ಮ    ರೀಲಿಂಗ್    ಘಟಕದಲ್ಲಿ    "
                        + apiResponse.getContent().get(0).getSubSchemeNameInKannada()
                        + "  ಅಳವಡಿಕೆಗೆ  ಸಹಾಯಧನ  ಕಾರ್ಯಾದೇಶ."
        );

        // Number & Date – same pattern as Reeling Shed Work Order
        response.setHeader3(
                "ಸಂಖ್ಯೆ  : ಕೇಂದ್ರ  ವಲಯ/"
                        + apiResponse.getContent().get(0).getSchemeNameInKannada()
                        + "  /"
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "/"
                        + apiResponse.getContent().get(0).getWorkOrderNumber()
        );

        response.setHeader4("ದಿನಾಂಕ : " + createdDate);

        response.setHeader6(
                "                  ಶ್ರೀ/ಶ್ರೀಮತಿ    "
                        + apiResponse.getContent().get(0).getReelerName()
                        + "    (FID    " + apiResponse.getContent().get(0).getFruitsId() + ")    ಬಿನ್/ಕೋಂ    "
                        + apiResponse.getContent().get(0).getReelerFatherName()
                        + "    ,    "
                        + apiResponse.getContent().get(0).getVillageName()
                        + "    ,    "
                        + apiResponse.getContent().get(0).getHobliName()
                        + "    ,    "
                        + apiResponse.getContent().get(0).getTalukName()
                        + "    ತಾ.    "
                        + apiResponse.getContent().get(0).getDistrictName()
                        + "    ಜಿಲ್ಲೆ    ಇವರು    "
                        + apiResponse.getContent().get(0).getScCategoryName()
                        + "    ವರ್ಗಕ್ಕೆ    ಸೇರಿಿದ್ದು,    ರೀಲಿಂಗ್    ರಹದಾರಿ    ಸಂಖ್ಯೆ    "
                        + apiResponse.getContent().get(0).getReelingLicenseNumber()
                        + "    ರಲ್ಲಿ    "
                        + apiResponse.getContent().get(0).getNumberOfBasins()
                        + "    ಬೇಸಿನ್    "
                        + apiResponse.getContent().get(0).getMachineTypeName()
                        + "    ರೀಲಿಂಗ್    ಘಟಕ    ಹೊಂದಿಿದ್ದು.    ತಮ್ಮ    ರೀಲಿಂಗ್    ಘಟಕದಲ್ಲಿ    "
                        + apiResponse.getContent().get(0).getSubSchemeNameInKannada()
                        + "  ಘಟಕ   ಅಳವಡಿಸಲು     ಅನುಮೋದಿಸಿದ    ಸಂಸ್ಥೆ    "+ apiResponse.getContent().get(0).getVendorName()
                        +"ರವರಿಂದ     ರೇ ಷ್ಮೆ       ಇಲಾಖೆಯ     ಮಾರ್ಗ      ಸೂಚಿಯನ್ವ ಯ    ಸಂಪೂರ್ಣ ವಾಗಿ     ಘಟಕವನ್ನು     ಅಳವಡಿಸಿಕೊಳ್ಳು ವುದು.     "+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"   ಸಂ ಪೂರ್ಣ ವಾಗಿ     ಅಳವಡಿಸಿಕೊಂಡ     ನಂತರ    ಅಗತ್ಯ    ದಾಖಲೆಗಳೊಂದಿಗೆ     ಪ್ರ ಸ್ತಾ ವನೆ    ಸಲ್ಲಿ ಸಿದಲ್ಲಿ      ಸಹಾಯಧನ     ಮಂಜೂರಾತಿಗಾಗಿ     ಕ್ರಮ     ಕೈಗೊಳ್ಳಲಾಗುವುದು. "+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"     ಅಳವಡಿಕೆಯಲ್ಲಿ       ಮಾರ್ಗಸೂಚಿಯ    ಉಲ್ಲಂಘನೆ     ಕಂಡುಬಂದಲ್ಲಿ,    ಈ    ಕಾರ್ಯಾದೇಶವನ್ನು    ರದ್ದುಪಡಿಸುವ   ಅಧಿಕಾರವನ್ನು    ಇಲಾಖೆ   ಹೊಂದಿರುತ್ತದೆ.");



        response.setLineItemComment(
                "ರೇಷ್ಮೆ   ಉಪ   ನಿರ್ದೇಶಕರು,\nಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ,  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
        );


        response.setHeader11(
                "ಇವರಿಗೆ,\n"
                        + "1.  ರೇಷ್ಮೆ  ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,  ಗೂಡಿನ  ನಂತರದ  ಚಟುವಟಿಕೆ,  "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                        + "\n"
                        + "2.  ರೇಷ್ಮೆ  ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,  ತಾಂತ್ರಿಕ  ಸೇವಾ  ಕೇಂದ್ರ  (ರೀಲಿಂಗ್),  "
                        + apiResponse.getContent().get(0).getLoggedinUserTscName()
                        + "\n"
                        + "3.  ಸಂಬಂಧಿತ  ರೇಷ್ಮೆ  ನೂಲುಬಿಚ್ಚಾಣಿಕೆದಾರರು,\n"
                        + "4.  "+apiResponse.getContent().get(0).getVendorName()+" , ಶಿಡ್ಲಘಟ್ಟ     ಇವರಿಗೆ   ಮುಂದಿನ   ಕ್ರಮಕ್ಕಾಗಿ"
        );

        response.setLogurl("/reports/Seal_of_Karnataka.PNG");

        sanctionOrderResponseList.add(response);
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }





    private JRDataSource getDataSourceForAcknowledgementReceiptPMKSY(ApplicationFormPrintRequest requestDto) throws JsonProcessingException {

        AcknowledgementResponse apiResponse = apiService.fetchAcknowledgementPmksy(requestDto);
        //  AcknowledgementReceiptResponse content = new AcknowledgementReceiptResponse();

        List<AcknowledgementReceiptResponse> acknowledgementReceiptResponseList = new LinkedList<>();
        AcknowledgementReceiptResponse response = new AcknowledgementReceiptResponse();

        if (apiResponse.getContent()!= null) {
            response.setHeader(" ಸ್ವೀಕೃತಿ  ಪತ್ರ  ( ACKNOWLEDGEMENT LETTER )");
            response.setHeader1(" FID ಸಂಖ್ಯೆ  :  "+apiResponse.getContent().get(0).getFruitsId() + "   BID ಸಂಖ್ಯೆ  :  \n" +
                    "                      \n" +
                    "________________________________(ಕಛೇರಿಯಿಂದ   ನೀಡುವುದು)] ");
            response.setLineItemComment( "            ಶ್ರೀ./ಶ್ರೀಮತಿ.   " +apiResponse.getContent().get(0).getFarmerFirstName()+  "   ಬಿನ್/ಕೋಂ    " +apiResponse.getContent().get(0).getFatherNameKan() + "   ರವರು    " +apiResponse.getContent().get(0).getVillageName()+"    ಗ್ರಾಮದ   " +apiResponse.getContent().get(0).getSurveyNumber()+"   ಸರ್ವೇ   ನಂಬರಿನಲ್ಲಿ  \n" +
                    "                                                \n"+
                    "____________________________________  ಬೆಳೆಗೆ    " +apiResponse.getContent().get(0).getVendorName()+"   ಕಂಪನಿ   ರವರು   ಹನಿ   ನೀರಾವರಿ   ಪದ್ಧತಿಯನ್ನು     ಅಳವಡಿಸಿ  ,   ಸಹಾಯಧನಕ್ಕಾಗಿ \n" +
                    "                  \n" +
                    "ದಿನಾಂಕ:  _____________________________________ ರಂದು  (ಕಾರ್ಯದೇಶ   ನೀಡಿದ   ________________________________ ದಿನಗಳು   ಒಳಗಾಗಿ )   ಸಂಬಂಧಿಸಿದ \n" +
                    "                  \n" +
                    "ಎಲ್ಲಾ     ದಾಖಲಾತಿಗಳೊಂದಿಗೆ     ಕಡತವನ್ನು    ಸಲ್ಲಿಸಿರುತ್ತಾರೆ.");
            response.setHeader2("(ಸಹಿ/-)\n" +
                    "                      \n"+
                    "ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು (ತಾಸ )\n" +
                    "                              \n"+
                    apiResponse.getContent().get(0).getTalukName()+ " ತಾಲ್ಲೂಕು  _________________");
            response.setAcceptedDate(" ಸ್ವೀಕೃತಿ ಪತ್ರದ  ದಿನಾಂಕ  :  " +apiResponse.getContent().get(0).getDate());
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
            response.setAddressText( apiResponse.getContent().get(0).getAddressText());
            response.setDistrictName( apiResponse.getContent().get(0).getDistrictName());
            response.setTalukName( apiResponse.getContent().get(0).getTalukName());
            response.setHobliName( apiResponse.getContent().get(0).getHobliName());
            response.setVillageName( apiResponse.getContent().get(0).getVillageName());
            response.setFruitsId( apiResponse.getContent().get(0).getFruitsId());
            response.setFinancialYear( apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada( apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada( apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan( apiResponse.getContent().get(0).getFatherNameKan());
            response.setArn( apiResponse.getContent().get(0).getArn());
            response.setMobileNumber( apiResponse.getContent().get(0).getMobileNumber());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            acknowledgementReceiptResponseList.add(response);

            //  acknowledgementReceiptResponseList.add(acknowledgementReceiptResponseList);
        }
        //countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(acknowledgementReceiptResponseList);
    }


    private JRDataSource getDataSourceAuthorisationLetterFromFarmer(WorkOrderPrintRequest requestDto) throws JsonProcessingException , JAXBException {

        WorkOrderReportResponse apiResponse = apiService.fetchDataApi(requestDto);
        List<WorkOrderGenerationReportResponse> workOrderGenerationReportResponseList = new LinkedList<>();
        WorkOrderGenerationReportResponse response = new WorkOrderGenerationReportResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("(ರೇಷ್ಮೆ  ಇಲಾಖೆ)");
            response.setHeader2("ಸಹಾಯಕ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರ ಕಛೇರಿ ");
            response.setLineItemComment("                    " + apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    ಯೋಜನೆಯಡಿ  ಶ್ರೀ/ಶ್ರೀಮತಿ   " + apiResponse.getContent().get(0).getFarmerFirstName()+ "   ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  +  " , " +apiResponse.getContent().get(0).getVillageName()+ "    ಗ್ರಾಮ ,  " + apiResponse.getContent().get(0).getHobliName()+ "   ಹೋಬಳಿ  ,\n " +
                    "                                            \n" +
                    apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು ,    " + apiResponse.getContent().get(0).getDistrictName()+ "    ಜಿಲ್ಲೆ    (ನೋಂದಣಿ  ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getFruitsId() + " , ಮೊಬೈಲ್ ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getMobileNumber() + " )  ಆದ ನಾನು ಮೇ  :  " + apiResponse.getContent().get(0).getVendorName() + "  ಸಂಸ್ಥೆಯ ವತಿಯಿಂದ   " +apiResponse.getContent().get(0).getScComponentName()+ "\n" +
                    "                                                                                                \n " +
                    "" +apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ " ರೇಷ್ಮೆ ಯಂತ್ರೋಪಕರಣ/ಸಂಸ್ಕರಣಾ ಘಟಕವನ್ನು ರೇಷ್ಮೆ ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಪಡೆಯಲು ಅರ್ಜಿ ಸಲ್ಲಿಸಿದು,\n"+
                    "                                                                                                           \n" +
                    "ಈ ಸಂಬಂಧ ನಾನು ರೈತರ ವಂತಿಕೆ ಮೊತ್ತ ರೂ.  " + apiResponse.getContent().get(0).getSchemeAmount() +"  ಗಳನ್ನೂ ಮಾತ್ರ ಪಾವತಿಸಿರುತ್ತಾನೆ.");
            response.setHeader4("ಈ  ಸಂಬಂಧ  ಸರ್ಕಾರದ ಸಹಾಯಧನವನ್ನು    ಮೇ: "+ apiResponse.getContent().get(0).getVendorName() + " ಸಂಸ್ಥೆಯ ಬ್ಯಾಂಕ್  ಗೆ ಅಥವಾ ಸದರಿ ಘಟಕವನ್ನು    ಖರೀದಿಸಲು ಪಡೆಯಲಾದ ನನ್ನ    ಬ್ಯಾಂಕ್");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(" ಶ್ರೀ/ಶ್ರೀಮತಿ  "+apiResponse.getContent().get(0).getFarmerFirstName());
            response.setWorkOrderNumber(apiResponse.getContent().get(0).getWorkOrderNumber());
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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            workOrderGenerationReportResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(workOrderGenerationReportResponseList);
    }


    private JRDataSource getDataSourceForWorkOrder(WorkOrderPrintRequest requestDto) throws JsonProcessingException , JAXBException {

        WorkOrderReportResponse apiResponse = apiService.fetchDataApiWorkOrder(requestDto);
        List<WorkOrderGenerationReportResponse> workOrderGenerationReportResponseList = new LinkedList<>();

        WorkOrderGenerationReportResponse apiData = apiResponse.getContent().get(0);
        WorkOrderGenerationReportResponse response = new WorkOrderGenerationReportResponse();

        if (apiResponse.getContent() != null && !apiResponse.getContent().isEmpty()) {

            // ✅ Split created_date into date (dd/MM/yyyy) and time (HH:mm)
            String createdDateTime = apiData.getCreatedDate();
            String datePart = "";
            String timePart = "";

            if (createdDateTime != null && !createdDateTime.isEmpty()) {
                try {
                    // Try parsing with milliseconds
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    LocalDateTime ldt = LocalDateTime.parse(createdDateTime, inputFormatter);
                    datePart = ldt.format(outputDateFormatter);
                    timePart = ldt.format(outputTimeFormatter);

                } catch (Exception e1) {
                    try {
                        // Try parsing without milliseconds
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                        LocalDateTime ldt = LocalDateTime.parse(createdDateTime, inputFormatter);
                        datePart = ldt.format(outputDateFormatter);
                        timePart = ldt.format(outputTimeFormatter);

                    } catch (Exception e2) {
                        try {
                            // Try parsing only date (if no time present)
                            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            LocalDate date = LocalDate.parse(createdDateTime.substring(0, 10), inputFormatter);
                            datePart = date.format(outputDateFormatter);
                            timePart = "";
                        } catch (Exception e3) {
                            // Final fallback – reverse manually if it's yyyy/MM/dd
                            if (createdDateTime.contains("/")) {
                                String[] parts = createdDateTime.split("/");
                                if (parts.length == 3) {
                                    datePart = parts[2] + "/" + parts[1] + "/" + parts[0];
                                }
                            } else if (createdDateTime.contains("-")) {
                                String[] parts = createdDateTime.split("-");
                                if (parts.length == 3) {
                                    datePart = parts[2] + "/" + parts[1] + "/" + parts[0];
                                }
                            }
                        }
                    }
                }
            }
            String surveyNumber = Util.objectToString(apiResponse.getContent().get(0).getSurveyNumber());
            String kaneshNo = Util.objectToString(apiResponse.getContent().get(0).getKaneshNo());

            String surveyText = "";
            if (!surveyNumber.isEmpty()) {
                surveyText = "ಸರ್ವೆ ನಂ. " + surveyNumber;
            } else if (!kaneshNo.isEmpty()) {
                surveyText = "ಖಾತೆ ನಂ. " + kaneshNo;
            }

            response.setHeader1("ಸಂಖ್ಯೆ  : ಕೇಂದ್ರ  ವಲಯ/"+apiResponse.getContent().get(0).getSchemeName()+"  /"  +apiResponse.getContent().get(0).getCategoryName()+ "/" +apiResponse.getContent().get(0).getWorkOrderNumber());
            response.setHeader2("ದಿನಾಂಕ : " +datePart );
            response.setHeader3("ಕೇಂದ್ರ   ವಲಯ    “"+apiResponse.getContent().get(0).getSchemeName()+"”  ಯೋಜನೆ   " +apiResponse.getContent().get(0).getCategoryName()+
                    "ರೇಷ್ಮೆ   ಹುಳು ಸಾಕಾಣಿಕೆ  ಮನೆ  ನಿರ್ಮಾಣಕ್ಕೆ   ಸಂಬಂಧಿಸಿದಂತೆ  ಕಾರ್ಯಾದೇಶ");
            response.setLineItemComment("                    ಮೇಲ್ಕಾ ಣಿಸಿದ    ಇವರ    ಜಮೀನಿಗೆ   ದಿನಾಂಕ :   " + datePart  + "   ರಂದು    " + timePart  + "   ಘಂಟೆ   " +
                            "   ಸಮಯದಲ್ಲಿ    ತಾಂತ್ರಿ  ಕ    ಸೇವಾ   ಕೇಂದ್ರ   " + apiResponse.getContent().get(0).getUserTscName() + "  ಕ್ಕೆ    ಸೇರಿದ  ರೇಷ್ಮೆ  ವಿಸ್ತ ರಣಾಧಿಕಾರಿಗಳು     " +
                            "ಶ್ರೀ /ಶ್ರೀ ಮತಿ     " + apiResponse.getContent().get(0).getNameKan() + "   ಬಿನ್/ಕೋಂ  "  + apiResponse.getContent().get(0).getFatherNameKan() + "   ರವರು    ಸರ್ವೆ   ನಂಬರು   " + apiResponse.getContent().get(0).getSurveyNumber()  +
                            "     ರಲ್ಲಿ     " + apiResponse.getContent().get(0).getDevAcre() + "  ಎಕರೆ    " +apiResponse.getContent().get(0).getDevGunta() + "   ಗುಂಟೆಗಳ    ವಿಸ್ತೀರ್ಣದ    ಪ್ರ  ದೇಶದಲ್ಲಿ      ಬೆಳೆಸಿರುವ    ಹಿಪ್ಪು ನೇರಳೆ " +
                               "     ತೋಟವನ್ನು     ಪರಿಶೀಲಿಸಲಾಯಿತು.\n" +
                                       "                     "+apiResponse.getContent().get(0).getVillageName() + "    ಗ್ರಾ  ಮದ    ಸರ್ವೆ   ನಂಬರಿನಲ್ಲಿ     ರೈತರು   ಜಮೀನು   ಹೊಂದಿದ್ದು ,   " +
                            "   ರೇಷ್ಮೆ   ಹುಳು   ಸಾಕಾಣಿಕೆ   ಮನೆ   ನಿರ್ಮಾಣ   ಮಾಡಲು   ಕಾರ್ಯಾದೇಶ   ನೀಡಲಾಗಿದೆ.");
            response.setHeader4        ("                    ಶ್ರೀ /ಶ್ರೀಮತಿ    " + apiResponse.getContent().get(0).getNameKan() + " ( " + apiResponse.getContent().get(0).getFruitsId() +  " )   ಬಿನ್/ಕೋಂ  " + apiResponse.getContent().get(0).getFatherNameKan() +"       "+
                            apiResponse.getContent().get(0).getVillageName() + "     ರವರು     " +apiResponse.getContent().get(0).getLandVillage()+"    ಗ್ರಾ  ಮ     " +surveyText + "     ಸರ್ವೆ     ನಂಬರಿನಲ್ಲಿ     " +
                                    "  ರೇಷ್ಮೆ    ಹುಳುಸಾಕಾಣಿಕೆ     ಮನೆ   ನಿರ್ಮಿಸಲು    ನೋಂದಣಿ   ಅರ್ಜಿ   ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getArn() +
                            "      ಸಲ್ಲಿ ಸಿರುತ್ತಾರೆ.  ");
            response.setHeader5("ರೇಷ್ಮೆ   ಉಪ   ನಿರ್ದೇಶಕರು,\n" +
                    "ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್,\n" +
                    apiResponse.getContent().get(0).getUserDistrictName());
            response.setHeader6("ಇವರಿಗೆ,\n" +
                    "ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,  " + apiResponse.getContent().get(0).getUserTaluk() + " ವಿಭಾಗ, \n" +
                    "ರೇಷ್ಮೆ   ವಿಸ್ತ ರಣಾಧಿಕಾರಿಗಳು, ತಾಂತ್ರಿ ಕ ಸೇವಾ ಕೇಂದ್ರ  ,  " + apiResponse.getContent().get(0).getUserTscName() + " .\n" +
                    "ಸಂಬಂಧಿಸಿದ  ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರಿಗೆ\n");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(" ಶ್ರೀ/ಶ್ರೀಮತಿ  "+apiResponse.getContent().get(0).getFarmerFirstName());
            response.setWorkOrderNumber(apiResponse.getContent().get(0).getWorkOrderNumber());
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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
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


    public class SanctionAmountToWords {

        private static final String[] units = {
                "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen"
        };

        private static final String[] tens = {
                "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
        };

        public static String convertToWords(int number) {
            if (number < 20) {
                return units[number];
            }
            if (number < 100) {
                return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
            }
            if (number < 1000) {
                return units[number / 100] + " Hundred" + (number % 100 != 0 ? " and " + convertToWords(number % 100) : "");
            }
            if (number < 1000000) {
                return convertToWords(number / 1000) + " Thousand" + (number % 1000 != 0 ? " " + convertToWords(number % 1000) : "");
            }
            return convertToWords(number / 1000000) + " Million" + (number % 1000000 != 0 ? " " + convertToWords(number % 1000000) : "");
        }

        public static void main(String[] args) {
            // Calculate the sanction amount
            BigDecimal sanctionAmount = new BigDecimal("1234.56"); // Example value
            BigDecimal calculatedAmount = sanctionAmount.multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP);

            // Convert to words
            int amountInInt = calculatedAmount.intValue();
            String sanctionAmountInWords = convertToWords(amountInInt);

            // Build the output string
            String output = "(ಕೇಂದ್ರ    ಪಾಲು  ಶೇ 33 ಮತ್ತು    ರಾಜ್ಯ    ಪಾಲು ಶೇ 22) ರೂ.  "
                    + calculatedAmount + " /- (ರೂ.  "
                    + sanctionAmountInWords + " ) ಮಾತ್ರಗಳನ್ನು";

            // Print the result
            System.out.println(output);
        }
    }


    private JRDataSource getDataSourceForSanctionOrderPDMC(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchSanctionOrderPDMCFarmer(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        if (apiResponse.getContent()!= null) {
//            // Calculate the sanction amount
//            BigDecimal sanctionAmount = new BigDecimal("1234.56"); // Example value
//            BigDecimal calculatedAmount = sanctionAmount.multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP);
//
//            // Convert to words
//            int amountInInt = calculatedAmount.intValue();
//            String sanctionAmountInWords = SanctionAmountToWords.convertToWords(amountInInt);
//
//            // Build the output string
//            String output = "(ಕೇಂದ್ರ    ಪಾಲು  ಶೇ 33 ಮತ್ತು    ರಾಜ್ಯ    ಪಾಲು ಶೇ 22) ರೂ.  "
//                    + calculatedAmount + " /- (ರೂ.  "
//                    + sanctionAmountInWords + " ) ಮಾತ್ರಗಳನ್ನು";
//
//            // Print the result
//            System.out.println(output);
            // Extract the sanction amount from the API response
            // Extract the sanction amount from the API response

//                String sanctionAmountFromApi = apiResponse.getContent().get(0).getSanctionAmount();
//                BigDecimal sanctionAmount;
//
//                try {
//                    // Convert the extracted sanction amount to BigDecimal
//                    sanctionAmount = new BigDecimal(sanctionAmountFromApi);
//                } catch (NumberFormatException e) {
//                    System.out.println("Invalid sanction amount: " + sanctionAmountFromApi);
//                    return; // Exit or handle the error appropriately
//                }
//
//                // Perform the calculation
//                BigDecimal calculatedAmount = sanctionAmount.multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP);
//
//                // Convert to words
//                int amountInInt = calculatedAmount.intValue(); // Use only the integer part
//                String sanctionAmountInWords = SanctionAmountToWords.convertToWords(amountInInt);
//
//                // Build the output string
//                String output = "(ಕೇಂದ್ರ    ಪಾಲು  ಶೇ 33 ಮತ್ತು    ರಾಜ್ಯ    ಪಾಲು ಶೇ 22) ರೂ.  "
//                        + calculatedAmount + " /- (ರೂ.  "
//                        + sanctionAmountInWords + " ) ಮಾತ್ರಗಳನ್ನು";
//
//                // Print the result
//                System.out.println(output);

            response.setHeader1("ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು,   ಜಿಲ್ಲಾ   ಪಂಚಾಯತ್ ,  " +apiResponse.getContent().get(0).getDistrictName() +"  ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು");
            response.setHeader4("ವಿಷಯ  : ");
            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ ಸಾಲಿನಲ್ಲಿ     ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ  ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ  \n" +
                        "                            \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() + "   ರವರಿಗೆ ರೂ. " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳ ಸಹಾಯಧನ ಮಂಜೂರಾತಿ ಬಗ್ಗೆ .");
            }else{
                response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ ಸಾಲಿನಲ್ಲಿ     ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ  ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ  \n" +
                        "                            \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() + "   ರವರಿಗೆ ರೂ. " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳ ಸಹಾಯಧನ ಮಂಜೂರಾತಿ ಬಗ್ಗೆ .");
            }
//            response.setHeader21( " ,ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ನಿರ್ಮಿಸಿರುವ - ಚದರ ಅಡಿಗಳ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "  ಗಳ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1.	ರೇಷ್ಮೆ   ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆ ಸಂಖ್ಯೆ  : _________________________________________ ದಿನಾಂಕ:________________________________\n"+
                    "                          \n"+
                    "2.	ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  "  +apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ ಇವರ ಪ್ರಸ್ತಾವನೆ ದಿನಾಂಕ:______________________________\n"+
                    "                     \n"+
                    "3.	ರೇಷ್ಮೆ   ಅಭಿವೃದ್ದಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ಕೃಷಿ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರು ರವರ ಪತ್ರದ ಸಂಖ್ಯೆ  : ___________________________________ ದಿನಾಂಕ:_______________________________\n"+
                    "                                   \n"+
                    "4.	ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ  :  ತೋಇ 61 ರೇಕೃವಿ 2019, ಬೆಂಗಳೂರು, ದಿನಾಂಕ:22.08.2023");

            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader8 ("         " +apiResponse.getContent().get(0).getFinancialYear() +"    ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC) ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ\n" +
                        "                                      \n"+
                        "ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್  ಮೇಲ್ಪಟ್ಟು    5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ\n" +
                        "                         \n" +
                        "ಘಟಕ ದರದ  ಶೇ.45 ರ  ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ    ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                        "                     \n"+
                        apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗದ ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ(2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು   ಪರಿಶೀಲಿಸಿದೆ.  "  +apiResponse.getContent().get(0).getTalukName()+  "  ತಾಲ್ಲೂಕಿನ   " +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                        "                           \n"+
                        "ಕೇಂದ್ರದ ವ್ಯಾಪ್ತಿಯ  ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು   " +apiResponse.getContent().get(0).getScCategoryName()+  "   ವರ್ಗಕ್ಕೆ ಸೇರಿದವರಾಗಿದ್ದು  ,   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು ,  "+apiResponse.getContent().get(0).getHobliName()+ "   ಹೋಬಳಿ,  "+apiResponse.getContent().get(0).getVillageName()+ "\n" +
                        "     \n" +
                        "ಗ್ರಾಮದ  ಸರ್ವೆ ನಂ.  " +apiResponse.getContent().get(0).getSurveyNumber() + " ನ  " +apiResponse.getContent().get(0).getHectareName() + "  ಹೆಕ್ಟೇರ್  ಪ್ರದೇಶದಲ್ಲಿ   " +apiResponse.getContent().get(0).getSpacingName() + "  ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ   ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು ಇಲಾಖೆ ಅಂಗೀಕೃತ ಸಂಸ್ಥೆ \n"+
                        "                                                  \n"+
                        apiResponse.getContent().get(0).getVendorName()+ "    ಸರಬರಾಜು  ಪಡೆದು ಸದರಿ ಸಂಸ್ಥೆಯ  ಟ್ಯಾಕ್ಸ್    ಇನ್ವಾಯ್ಸ್     ಸಂಖ್ಯೆ  : _____________________________________ದಿನಾಂಕ :________________________________ರನ್ವಯ\n" +
                        "                           \n" +
                        "ದಿನಾಂಕ :____________________________________ರಂದು  ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು, ಅಳವಡಿಸಿರುವ  ಹನಿ  ನೀರಾವರಿ  ಘಟಕವು ತೃಪ್ತಿಕರವಾಗಿ\n"+
                        "                                               \n"+
                        "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ   ಶ್ರೀ/ಶ್ರೀಮತಿ   "  +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು ದೃಢಪಡಿಸಿರುತ್ತಾರೆ . ಸದರಿ ಘಟಕವು  ಶ್ರೀ/ಶ್ರೀಮತಿ     " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರ  2  ಹೆಕ್ಟೇರ್  ಮೇಲ್ಪಟ್ಟ  \n" +
                        "                       \n"+
                        "ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ     ಅಳವಡಿಸಿಕೊಂಡಿರುವುದಾಗಿದ್ದು     ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,   " +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ ಇವರಿಂದ ದಿನಾಂಕ : ______________________________\n" +
                        "              \n" +
                        "ರಂದು  ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ  ಹಾಗೂ ಮಂಜೂರಾತಿ ನೀಡಲು  ಶಿಫಾರಸ್ಸು   ಮಾಡಿರುತ್ತಾರೆ.\n" +
                        "                   \n"+
                        "ಕಾರ್ಯಕ್ರಮದ  ಮಾರ್ಗಸೂಚಿ  ಅನ್ವಯ  ಘಟಕದ  ಸಂಪೂರ್ಣ  ವೆಚ್ಚವನ್ನು    ಫಲಾನುಭವಿಯೇ   ಭರಿಸಿದ್ದಲ್ಲಿ    ಸಂಬಂಧಿಸಿದ ಫಲಾನುಭವಿಯ   ಬ್ಯಾಂಕ್  ಖಾತೆಗೆ  ಸಹಾಯಧನವನ್ನು \n" +
                        "    \n" +
                        "ಪಾವತಿಸಲು ಅವಕಾಶವಿದೆ . ಉಲ್ಲೇಖ (3) ರನ್ವಯ ಘಟಕದರ ರೂ. " +apiResponse.getContent().get(0).getSanctionAmount()+ " /- ಗಳ ಕೇಂದ್ರ ಹಾಗೂ ರಾಜ್ಯದ ಕಡ್ಡಾಯ ಪಾಲಿನ ಶೇ 45 ರ ಸಹಾಯಧನ ರೂ. "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು \n"+
                        "              \n" +
                        "ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ ಮಂಜೂರು ಮಾಡಲು ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,   " +apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ ರವರು ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ . ಉಲ್ಲೇಖ (3) ರ ಪತ್ರದಲ್ಲಿ \n" +
                        "                \n" +
                        "ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "    ರಡಿ   ಅನುದಾನವು  ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ   ಘಟಕ ದರದ  ಶೇ 45 ಸಹಾಯಧನ ರೂ.    "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP) + "  /- ಗಳನ್ನು    ಆರ್ಥಿಕ  ಪ್ರತ್ಯಾಯೋಜನೆ\n" +
                        "      \n" +
                        "ರೀತ್ಯಾ    ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದೆ . ಅದರಂತೆ  ಈ ಆದೇಶ .");
            }else{

                response.setHeader8 ("         " +apiResponse.getContent().get(0).getFinancialYear() +"    ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC) ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ ಎಲ್ಲಾ \n" +
                        "                                      \n"+
                        "ವರ್ಗದ ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್  ಮೇಲ್ಪಟ್ಟು    5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ ಘಟಕ\n" +
                        "                         \n" +
                        "ದರದ  ಶೇ.45 ರ  ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ    ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                        "                     \n"+
                        apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗದ ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ(2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು   ಪರಿಶೀಲಿಸಿದೆ.  "  +apiResponse.getContent().get(0).getTalukName()+  "  ತಾಲ್ಲೂಕಿನ   " +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                        "                           \n"+
                        "ಕೇಂದ್ರದ ವ್ಯಾಪ್ತಿಯ  ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು   " +apiResponse.getContent().get(0).getScCategoryName()+  "   ವರ್ಗಕ್ಕೆ ಸೇರಿದವರಾಗಿದ್ದು  ,   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು ,  "+apiResponse.getContent().get(0).getHobliName()+ "   ಹೋಬಳಿ,  "+apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮದ\n" +
                        "     \n" +
                        "ಸರ್ವೆ ನಂ.  " +apiResponse.getContent().get(0).getSurveyNumber() + " ನ  " +apiResponse.getContent().get(0).getHectareName() + "  ಹೆಕ್ಟೇರ್  ಪ್ರದೇಶದಲ್ಲಿ   " +apiResponse.getContent().get(0).getSpacingName() + "  ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ   ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು ಇಲಾಖೆ ಅಂಗೀಕೃತ ಸಂಸ್ಥೆ   " +apiResponse.getContent().get(0).getVendorName()+ "\n"+
                        "                                                  \n"+
                        "ಸರಬರಾಜು  ಪಡೆದು ಸದರಿ ಸಂಸ್ಥೆಯ  ಟ್ಯಾಕ್ಸ್    ಇನ್ವಾಯ್ಸ್     ಸಂಖ್ಯೆ  : _______________________________________ದಿನಾಂಕ :_______________________________________ರನ್ವಯ\n" +
                        "                           \n" +
                        "ದಿನಾಂಕ :____________________________________ರಂದು  ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು, ಅಳವಡಿಸಿರುವ  ಹನಿ  ನೀರಾವರಿ  ಘಟಕವು ತೃಪ್ತಿಕರವಾಗಿ\n"+
                        "                           \n" +
                        "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ   ಶ್ರೀ/ಶ್ರೀಮತಿ   "  +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು   ದೃಢಪಡಿಸಿರುತ್ತಾರೆ.  ಸದರಿ ಘಟಕವು ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  " +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ \n" +
                        "                   \n" +
                        "ಇವರಿಂದ ದಿನಾಂಕ : _______________________________________ರಂದು ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ ಹಾಗೂ ಮಂಜೂರಾತಿ ನೀಡಲು ಶಿಫಾರಸ್ಸು  ಮಾಡಿರುತ್ತಾರೆ . \n" +
                        "          \n" +
                        "ಕಾರ್ಯಕ್ರಮದ  ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ  ಘಟಕದ ಸಂಪೂರ್ಣ ವೆಚ್ಚವನ್ನು    ಫಲಾನುಭವಿಯೇ ಭರಿಸಿದ್ದಲ್ಲಿ    ಸಂಬಂಧಿಸಿದ ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಸಹಾಯಧನವನ್ನು  \n"+
                        "                             \n" +
                        "ಪಾವತಿಸಲು ಅವಕಾಶವಿದೆ. ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ  ಅಳವಡಿಸಿದ  ಹನಿ ನೀರಾವರಿ ಘಟಕಕ್ಕೆ    ಅನ್ವಯಿಸುವ ಘಟಕದರ  ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount()+ " /- ಗಳ ಶೇ.90 ರ ಸಹಾಯಧನ ರೂ.  " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP) + "/-\n"+
                        "                            \n"+
                        "ಗಳಲ್ಲಿ    ಕೇಂದ್ರ  ಹಾಗೂ ರಾಜ್ಯದ  ಕಡ್ಡಾಯ ಪಾಲಿನ ಶೇ 55 ಸಹಾಯಧನ  ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಮತ್ತು    ರಾಜ್ಯದ ಹೆಚ್ಚುವರಿ ಪಾಲಿನ \n" +
                        "                         \n"+
                        "ಶೇ 35 ಸಹಾಯಧನ  ರೂ.  "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + "   ಅನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   " +apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ ಮಂಜೂರು ಮಾಡಲು ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,   " +apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ ರವರು ಶಿಫಾರಸ್ಸು \n"+
                        "                                                   \n" +
                        "ಮಾಡಿರುತ್ತಾರೆ.  ಉಲ್ಲೇಖ(3) ರ  ಪತ್ರದಲ್ಲಿ    ಲೆಕ್ಕ  ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ ಕೇಂದ್ರ   ಪಾಲು ಶೇ 33 ಮತ್ತು     ರಾಜ್ಯ    ಪಾಲು ಶೇ 22  ಅನುದಾನವು ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ\n"+
                        "              \n" +
                        "ಘಟಕ ದರದ ಶೇ 55 ಸಹಾಯಧನ  ರೂ.  "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು    ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ರೀತ್ಯಾ    ಮಂಜೂರು  ಮಾಡಬಹುದಾಗಿದೆ . ಅದರಂತೆ ಈ ಆದೇಶ.");
            }


            response.setHeader9("ಆದೇಶ ಸಂಖ್ಯೆ   :_______________________________________________                                                                                               ದಿನಾಂಕ: ________________________________");

            response.setHeader24("ಪೀಠಿಕೆ  :-");


            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader10( "             ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿರುವಂತೆ  "+apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ    "+apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕಿನ   " +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ  ವ್ಯಾಪ್ತಿಯ\n" +
                        "                  \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)ಯೋಜನೆಯಡಿ ______________________________ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ  ______________________________ \n" +
                        "                                                       \n"+
                        "ಅಂತರದಲ್ಲಿ   ಬೆಳೆಸಿರುವ  2 ಹೆಕ್ಟೇರ್   ಮೇಲ್ಪಟ್ಟ     ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ    ಹನಿ  ನೀರಾವರಿ  ಘಟಕ  ಅಳವಡಿಸಿರುವುದಕ್ಕೆ    ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ” \n" +
                        "                             \n" +
                        apiResponse.getContent().get(0).getScHeadAccountName() +"   ರಡಿ ಕೇಂದ್ರ    ಹಾಗೂ  ರಾಜ್ಯದ   ಕಡ್ಡಾಯ   ಪಾಲು   ಘಟಕ   ದರದ   ಶೇ 45 ರ   ಸಹಾಯಧನ   ರೂ.  _________________________________________________________________  /-\n" +
                        "   \n" +
                        "(ರೂ.______________________________________________________________________________________________________________________)  ಮಾತ್ರಗಳನ್ನು  ಮಾರ್ಗಸೂಚಿಯನ್ವಯ ಘಟಕದ ಸಂಪೂರ್ಣ \n" +
                        "                 \n" +
                        "ವೆಚ್ಚವನ್ನು    ಫಲಾನುಭವಿಯೇ ಭರಿಸಿದ್ದಲ್ಲಿ    ಸರ್ಕಾರದ   ಸಹಾಯಧನವನ್ನು    ಫಲಾನುಭವಿಗೆ ಪಾವತಿಸಲು ಅವಕಾಶವಿರುವಂತೆ, ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನ ಆದೇಶ ಸಂಖ್ಯೆ   : ತೋಇ\n" +
                        "       \n" +
                        "61 ರೇಕೃವಿ   2019 , ಬೆಂಗಳೂರು , ದಿನಾಂಕ:22.08.2023 ರಂತೆ  ರೂ.   _________________________________________________  /- ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ. ಸದರಿ ವೆಚ್ಚವನ್ನು   \n" +
                        "                     \n" +
                        "ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ಯಡಿ ಭರಿಸುವುದು.");
            }else{
                response.setHeader10( "             ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿರುವಂತೆ  "+apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ    "+apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕಿನ   " +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯ\n" +
                        "                  \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)ಯೋಜನೆಯಡಿ _______________________________ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ   ____________________________\n" +
                        "                                              \n" +
                        "ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ  ಘಟಕ ಅಳವಡಿಸಿರುವುದಕ್ಕೆ     ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ” "+apiResponse.getContent().get(0).getScHeadAccountName() + "\n" +
                        "                           \n" +
                        "ರಡಿ  ಕೇಂದ್ರ    ಪಾಲು  ಶೇ 33  ಮತ್ತು     ರಾಜ್ಯ     ಪಾಲು ಶೇ 22   ಘಟಕ ದರದ ಶೇ 55 ರ ಸಹಾಯಧನ   ರೂ.  ___________________________________________________________ /-\n"  +
                        "                                          \n" +
                        "(ರೂ.   ___________________________________________________________________________________________________) ಮಾತ್ರಗಳನ್ನು   ಮಾರ್ಗಸೂಚಿಯನ್ವಯ ಘಟಕದ ಸಂಪೂರ್ಣ ವೆಚ್ಚವನ್ನು  \n" +
                        "                                           \n"+
                        "ಫಲಾನುಭವಿಯೇ  ಭರಿಸಿದ್ದಲ್ಲಿ   ಸರ್ಕಾರದ ಸಹಾಯಧನವನ್ನು    ಫಲಾನುಭವಿಗೆ ಪಾವತಿಸಲು ಅವಕಾಶವಿರುವಂತೆ , ಆರ್ಥಿಕ  ಪ್ರತ್ಯಾಯೋಜನೆ  ಆದೇಶ ಸಂಖ್ಯೆ : ತೋಇ 61\n" +
                        "                                                                \n" +
                        "ರೇಕೃವಿ 2019 ,   ಬೆಂಗಳೂರು ,  ದಿನಾಂಕ:22.08.2023  ರಂತೆ ರೂ. ______________________________ /-ಗಳಿಗೆ  ಮಂಜೂರಾತಿ ನೀಡಿದೆ . ಸದರಿ ವೆಚ್ಚವನ್ನು   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ  "+apiResponse.getContent().get(0).getScHeadAccountName() + "\n" +
                        "                                            \n" +
                        "ಯಡಿ ಭರಿಸುವುದು." );

            }
            response.setHeader11("ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು\n" +
                    "      \n"+
                    "ಜಿ.ಪಂ.,  " +apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("ಇವರಿಗೆ  ;");

            response.setHeader12("ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  " +apiResponse.getContent().get(0).getDistrictName() +"  ವಿಭಾಗ \n" +
                    "                  \n"+
                    "ಪ್ರತಿಯನ್ನು   ಮಾಹಿತಿ ಹಾಗೂ ಕ್ರಮಕ್ಕಾಗಿ\n" +
                    "                  \n"+
                    "1 ಜಿಲ್ಲಾ    ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು , _______________________________________\n" +
                    "                    \n"+
                    "2. ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು ,  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ   " +apiResponse.getContent().get(0).getTscName()+"\n" +
                    "                     \n"+
                    "3. ರೇಷ್ಮೆ   ಜಂಟಿ ನಿರ್ದೇಶಕರು , ಬೆಂಗಳೂರು ವಿಭಾಗ ,  ಬೆಂಗಳೂರು \n" +
                    "                    \n"+
                    "4. ಶ್ರೀ/ಶ್ರೀಮತಿ   "  +apiResponse.getContent().get(0).getFarmerFirstName() +"   ರವರುಗಳ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .\n"+
                    "                            \n"+
                    "                            \n"+
                    "                         \n"+
                    "                                                                                                                            ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName());

//            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
//                    "                       \n" +
//                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
//                    "                                      \n" +
//                    "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }
    private JRDataSource getDataSourceForPDMCWorkOrder(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchPDMCWorkOrder(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ  -  ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC) ಯೋಜನೆಯಡಿ ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮ ಕಾರ್ಯಾದೇಶ.");

//            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
            response.setHeader4("              ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಬಿನ್/ಕೋಂ  " +apiResponse.getContent().get(0).getFatherNameKan() + "  ರವರು   "+apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮ   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು   "+apiResponse.getContent().get(0).getDistrictName()+ "  ಜಿಲ್ಲೆ   ಇವರು\n" +
                    "                  \n" +
                    apiResponse.getContent().get(0).getFinancialYear() +  "   ಸಾಲಿನ  ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ ಅರ್ಜಿ  ನೋಂದಣಿ ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getReferenceNo()+ "   ಆಗಿರುತ್ತದೆ .\n" +
                    "                    \n" +
                    "ಸದರಿಯವರು   " +apiResponse.getContent().get(0).getScCategoryName()+ "  ವರ್ಗದಡಿ ಆಯ್ಕೆಗೊಂಡ ಫಲಾನುಭವಿಯಾಗಿದ್ದು ,  " +apiResponse.getContent().get(0).getSurveyNumber() + "  ಸರ್ವೆ ನಂಬರ್ ನ  " +apiResponse.getContent().get(0).getHectareName() + " ಹೆಕ್ಟೇರ್  ವಿಸ್ತೀರ್ಣದಲ್ಲಿ   " +apiResponse.getContent().get(0).getSpacingName() + "  ಅಂತರದಲ್ಲಿ \n" +
                    "                  \n" +
                    "ಹಿಪ್ಪುನೇರಳೆ  ತೋಟ  ಹೊಂದಿರುತ್ತಾರೆ.  " +apiResponse.getContent().get(0).getFinancialYear() + "   ನೇ ಸಾಲಿನ  ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ  -  ಪ್ರತಿ  ಹನಿಗೆ   ಅಧಿಕ   ಬೆಳೆ  (PDMC) ಯೋಜನೆಯಡಿ  ಹನಿ  ನೀರಾವರಿ\n" +
                    "                                      \n"+
                    "ಘಟಕ  ಅಳವಡಿಕೆಗಾಗಿ  ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದ  ಮಾರ್ಗಸೂಚಿಯನ್ವಯ  ಸದರಿಯವರು ಘಟಕ ದರ ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount() + "  ಗಳ  ಶೇ 90 ರ  ರೂ.  " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP)+"\n" +
                    "        \n"+
                    "ಗಳ  ಸಹಾಯಧನ ಪಡೆಯಲು ಮಾತ್ರ   ಅರ್ಹರಿರುತ್ತಾರೆ.  ಮೇಲ್ಕಾಣಿಸಿದ  ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ    ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ  " +apiResponse.getContent().get(0).getFatherNameKan() + "\n"+
                    "                                 \n"+
                    "ರವರು ಹನಿ ನೀರಾವರಿ  ಘಟಕ  ಅಳವಡಿಕೆಗಾಗಿ  ಅಧಿಕೃತ ಹನಿ ನೀರಾವರಿ  ಘಟಕ ಸರಬರಾಜುದಾರರಾದ   ಮೆII   " +apiResponse.getContent().get(0).getVendorName() +"    ಇವರನ್ನು    ಆಯ್ಕೆ   \n" +
                    "              \n" +
                    "ಮಾಡಿಕೊಂಡಿದ್ದು  ,  ಇದರಂತೆ   ಕಾರ್ಯಾದೇಶ ನೀಡಿದೆ .");

//            }else{
//                response.setHeader4("           ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಬಿನ್/ಕೋಂ  " +apiResponse.getContent().get(0).getFatherNameKan() + "  ರವರು   "+apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮ   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು   "+apiResponse.getContent().get(0).getDistrictName()+ "  ಜಿಲ್ಲೆ    ಇವರು  " +apiResponse.getContent().get(0).getFinancialYear() +  "\n" +
//                        "                  \n" +
//                        "ಸಾಲಿನ ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದಡಿ ಅರ್ಜಿ  ನೋಂದಣಿ ಸಂಖ್ಯೆ    " +apiResponse.getContent().get(0).getReferenceNo()+ "   ಆಗಿರುತ್ತದೆ .  ಸದರಿಯವರು\n" +
//                        "                    \n" +
//                        apiResponse.getContent().get(0).getScCategoryName()+ "   ವರ್ಗದಡಿ ಆಯ್ಕೆಗೊಂಡ ಫಲಾನುಭವಿಯಾಗಿದ್ದು ,  " +apiResponse.getContent().get(0).getSurveyNumber() + "   ಸರ್ವೆ ನಂಬರ್ ನ   " +apiResponse.getContent().get(0).getHectareName() + "  ವಿಸ್ತೀರ್ಣದಲ್ಲಿ    " +apiResponse.getContent().get(0).getSpacingName() + "   ಅಂತರದಲ್ಲಿ    ಹಿಪ್ಪುನೇರಳೆ ತೋಟ\n" +
//                        "                  \n" +
//                        "ಹೊಂದಿರುತ್ತಾರೆ.  " +apiResponse.getContent().get(0).getFinancialYear() + "   ನೇ ಸಾಲಿನ  ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ  -  ಪ್ರತಿ  ಹನಿಗೆ   ಅಧಿಕ   ಬೆಳೆ  (PDMC) ಯೋಜನೆಯಡಿ  ಹನಿ  ನೀರಾವರಿ  ಘಟಕ  ಅಳವಡಿಕೆಗಾಗಿ\n" +
//                        "                                      \n"+
//                        "ಸಹಾಯಧನ ಕಾರ್ಯಕ್ರಮದ  ಮಾರ್ಗಸೂಚಿಯನ್ವಯ  ಸದರಿಯವರು ಘಟಕ ದರದ ಶೇ.  55  ರ ಗರಿಷ್ಠ ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount() + "   ಗಳ ಸಹಾಯಧನ ಪಡೆಯಲು ಮಾತ್ರ\n" +
//                        "        \n"+
//                        "ಅರ್ಹರಿರುತ್ತಾರೆ.  ಮೇಲ್ಕಾಣಿಸಿದ  ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ    ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಬಿನ್/ಕೋಂ  " +apiResponse.getContent().get(0).getFatherNameKan() + "   ರವರು ಹನಿ ನೀರಾವರಿ  ಘಟಕ  ಅಳವಡಿಕೆಗಾಗಿ\n"+
//                        "                                 \n"+
//                        "ಅಧಿಕೃತ ಹನಿ ನೀರಾವರಿ  ಘಟಕ ಸರಬರಾಜುದಾರರಾದ   ಮೆII   " +apiResponse.getContent().get(0).getVendorName() +"    ಇವರನ್ನು    ಆಯ್ಕೆ    ಮಾಡಿಕೊಂಡಿದ್ದು   ,   ಇದರಂತೆ   ಕಾರ್ಯಾದೇಶ ನೀಡಿದೆ .");

//            }


            response.setHeader24("ಪೀಠಿಕೆ :-");



            response.setHeader11("ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು\n" +
                    "      \n"+
                    "ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್    "+apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("ಇವರಿಗೆ ;");

            response.setHeader12("ಮೆII   " +apiResponse.getContent().get(0).getVendorName()+ "\n"+
                    "                           \n"+
                    "________________________________________________________\n"+
                    "                                        \n"+
                    "________________________________________________________\n"+
                    "       \n"+
                    "ಪ್ರತಿ ಮಾಹಿತಿಗಾಗಿ\n"+
                    "                              \n"+
                    "ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  "+apiResponse.getContent().get(0).getTalukName()+ " ,\n"+
                    "                        \n"+
                    "ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು , ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ,   "+apiResponse.getContent().get(0).getTscName()+ "  \n"+
                    "                            \n"+
                    "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");


//            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
//                    "                       \n" +
//                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
//                    "                                      \n" +
//                    "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
//            response.setHeader19("");
//            response.setDate(apiResponse.getContent().get(0).getDate());
//            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
//            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
//            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
//            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
//            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
//            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
//            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
//            response.setCost(apiResponse.getContent().get(0).getCost());
//            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
//            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
//            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
//            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
//            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
//            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
//            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
//            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
//            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
//            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
//            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
//            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private JRDataSource getDataSourceForSanctionOrderPmksy(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchSanctionOrderPmksy(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು ,   ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್ ,  " +apiResponse.getContent().get(0).getDistrictName() +"  ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು");
            response.setHeader4("ವಿಷಯ  : ");
            response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ    ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ (PMKSY) ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ\n" +
                    "                            \n"+
                    "ಅಳವಡಿಕೆಗಾಗಿ  ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() + "   ರವರಿಗೆ  ಹೆಚ್ಚುವರಿ  ರಾಜ್ಯದ ಪಾಲು  ರೂ.  "+new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳ ಸಹಾಯಧನ ಮಂಜೂರಾತಿ ಬಗ್ಗೆ .");
//            response.setHeader21( " ,ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ನಿರ್ಮಿಸಿರುವ - ಚದರ ಅಡಿಗಳ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "  ಗಳ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1.	ರೇಷ್ಮೆ    ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆ ಸಂಖ್ಯೆ  : ___________________________________ ದಿನಾಂಕ:__________________________\n"+
                    "                          \n"+
                    "2.	ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,   "  +apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ ಇವರ ಪ್ರಸ್ತಾವನೆ ದಿನಾಂಕ:_________________________\n"+
                    "                     \n"+
                    "3.	ರೇಷ್ಮೆ    ಅಭಿವೃದ್ದಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ಕೃಷಿ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರು ರವರ ಪತ್ರದ ಸಂಖ್ಯೆ  : ________________________________________________ ದಿನಾಂಕ:_______________________\n"+
                    "                                   \n"+
                    "4.	ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ   : ತೋಇ 61 ರೇಕೃವಿ 2019 , ಬೆಂಗಳೂರು , ದಿನಾಂಕ:22.08.2023");


            response.setHeader8 ("         " +apiResponse.getContent().get(0).getFinancialYear() +"    ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ     ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ (PMKSY)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ\n" +
                    "                                      \n"+
                    "ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್ ಮೇಲ್ಪಟ್ಟು     5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ \n" +
                    "                         \n" +
                    "ವರ್ಗದ ರೈತರಿಗೆ ಘಟಕ ದರದ ಶೇ.45 ರ ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ    ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                    "                     \n"+
                    apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗದ ರೇಷ್ಮೆ     ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ (2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಿದೆ.  "  +apiResponse.getContent().get(0).getTalukName()+  "  ತಾಲ್ಲೂಕಿನ   " +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                    "                           \n"+
                    "ಕೇಂದ್ರದ ವ್ಯಾಪ್ತಿಯ ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು  " +apiResponse.getContent().get(0).getScCategoryName()+  "  ವರ್ಗಕ್ಕೆ ಸೇರಿದವರಾಗಿದ್ದು   ,   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು ,  "+apiResponse.getContent().get(0).getHobliName()+ "  ಹೋಬಳಿ,  " +apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮದ\n" +
                    "                                    \n"+
                    "ಸರ್ವೆನಂ. " +apiResponse.getContent().get(0).getSurveyNumber() + " ರ   " +apiResponse.getContent().get(0).getHectareName() + "  ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ     " +apiResponse.getContent().get(0).getSpacingName()+  "   ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು  ಇಲಾಖೆ ಅಂಗೀಕೃತ  ಸಂಸ್ಥೆ \n"+
                    "                                             \n"+
                    apiResponse.getContent().get(0).getVendorName() + "  ಸರಬರಾಜು ಪಡೆದು  ಸದರಿ ಸಂಸ್ಥೆಯ ಟ್ಯಾಕ್ಸ್    ಇನ್ವಾಯ್ಸ್    ಸಂಖ್ಯೆ  : _______________________________________  ದಿನಾಂಕ:_______________________________________ ರನ್ವಯ \n" +
                    "                       \n"+
                    "ದಿನಾಂಕ:___________________________________________ರಂದು ಹನಿ ನೀರಾವರಿ  ಘಟಕ  ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು,  ಅಳವಡಿಸಿರುವ ಹನಿ ನೀರಾವರಿ ಘಟಕವು ತೃಪ್ತಿಕರವಾಗಿ \n" +
                    "                           \n" +
                    "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ   ಶ್ರೀ/ಶ್ರೀಮತಿ  "+apiResponse.getContent().get(0).getFarmerFirstName() +  "  ಇವರು  ದೃಢಪಡಿಸಿರುತ್ತಾರೆ. ಸದರಿ ಘಟಕವು ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  "  +apiResponse.getContent().get(0).getTalukName()+  "  ವಿಭಾಗ ಇವರಿಂದ\n"+
                    "                       \n" +
                    "ದಿನಾಂಕ: _______________________________________ರಂದು  ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ ಹಾಗೂ ಮಂಜೂರಾತಿ  ನೀಡಲು ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ.\n" +
                    "                   \n"+
                    " ಕಾರ್ಯಕ್ರಮದ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಘಟಕದ ಸಂಪೂರ್ಣ ವೆಚ್ಚವನ್ನು    ಫಲಾನುಭವಿಯೇ ಭರಿಸಿದ್ದಲ್ಲಿ    ಸಂಬಂಧಿಸಿದ ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಸಹಾಯಧನವನ್ನು \n"+
                    "                             \n" +
                    "ಪಾವತಿಸಲು ಅವಕಾಶವಿದೆ. ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ    ಅಳವಡಿಸಿದ ಹನಿ ನೀರಾವರಿ ಘಟಕಕ್ಕೆ    ಅನ್ವಯಿಸುವ ಘಟಕದರ  ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount()+ "  /- ಗಳ ಶೇ.90 ರ ಸಹಾಯಧನ ರೂ. " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP) + " /- \n"+
                    "                            \n"+
                    "ಗಳಲ್ಲಿ   ಕೇಂದ್ರ    ಹಾಗೂ ರಾಜ್ಯದ ಕಡ್ಡಾಯ ಪಾಲಿನ ಶೇ 55 ಸಹಾಯಧನ ರೂ.   " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + " ರಡಿ ಮತ್ತು    ರಾಜ್ಯದ ಹೆಚ್ಚುವರಿ  ಪಾಲಿನ\n" +
                    "                         \n"+
                    "ಶೇ 35 ಸಹಾಯಧನ  ರೂ.  " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + "  ಅನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ  "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಮಂಜೂರು ಮಾಡಲು ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,  "+apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ  ರವರು\n"+
                    "                                                   \n" +
                    "ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ. ಉಲ್ಲೇಖ(3)ರ  ಪತ್ರದಲ್ಲಿ    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ  "+apiResponse.getContent().get(0).getScHeadAccountName() +  "  ರಡಿ ಹೆಚ್ಚುವರಿ ರಾಜ್ಯದ  ಪಾಲಿನ ಅನುದಾನವು ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ ಘಟಕ\n"+
                    "              \n" +
                    "ದರದ  ಶೇ 35ರ  ಸಹಾಯಧನ  ರೂ.   " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + "/- ಗಳನ್ನು   ಆರ್ಥಿಕ  ಪ್ರತ್ಯಾಯೋಜನೆ ರೀತ್ಯಾ    ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದೆ. ಅದರಂತೆ ಈ ಆದೇಶ.");

            response.setHeader9("       ಆದೇಶ ಸಂಖ್ಯೆ  :__________________________________________                                                                                                    ದಿನಾಂಕ: ___________________________________");

            response.setHeader24("ಪೀಠಿಕೆ :-");


            response.setHeader10( "        ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸಿರುವಂತೆ  "+apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ  "+apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕಿನ   "  +apiResponse.getContent().get(0).getTscName()+  "  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ \n" +
                    "                  \n"+
                    "ವ್ಯಾಪ್ತಿಯ  ಶ್ರೀ/ಶ್ರೀಮತಿ     " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ  ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ    ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ(PMKSY)  ಯೋಜನೆಯಡಿ _____________________________\n" +
                    "                                              \n" +
                    "ಹೆಕ್ಟೇರ್  ಪ್ರದೇಶದಲ್ಲಿ   ____________________________ ಅಂತರದಲ್ಲಿ   ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿರುವುದಕ್ಕೆ    ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ\n" +
                    "                       \n" +
                    "ಅನ್ವಯ   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ”   " +apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ   ಹೆಚ್ಚುವರಿ  ರಾಜ್ಯದ   ಪಾಲಿನ  ಘಟಕ  ದರದ   ಶೇ 35ರ  ಸಹಾಯಧನ  ರೂ. _______________________________________________________ /-\n" +
                    "                           \n" +
                    "(ರೂ.__________________________________________________________________________________________________)ಮಾತ್ರಗಳನ್ನು    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ  ಘಟಕದ ಸಂಪೂರ್ಣ ವೆಚ್ಚವನ್ನು \n" +
                    "                                           \n"+
                    "ಫಲಾನುಭವಿಯೇ  ಭರಿಸಿದ್ದಲ್ಲಿ    ಸರ್ಕಾರದ   ಸಹಾಯಧನವನ್ನು     ಫಲಾನುಭವಿಗೆ   ಪಾವತಿಸಲು  ಅವಕಾಶವಿರುವಂತೆ,  ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ಆದೇಶ ಸಂಖ್ಯೆ    : ತೋಇ\n"+
                    "     \n"+
                    "61 ರೇಕೃವಿ 2019 , ಬೆಂಗಳೂರು , ದಿನಾಂಕ:22.08.2023 ರಂತೆ ರೂ. _____________________________________________________ /-  ಗಳಿಗೆ   ಮಂಜೂರಾತಿ  ನೀಡಿದೆ. ಸದರಿ ವೆಚ್ಚವನ್ನು  \n" +
                    "                           \n" +
                    "ಲೆಕ್ಕ   ಶೀರ್ಷಿಕೆ    " +apiResponse.getContent().get(0).getScHeadAccountName() + "   ಯಡಿ ಭರಿಸುವುದು." );
            response.setHeader11("ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು\n" +
                    "      \n"+
                    "ಜಿ.ಪಂ.,  " +apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("ಇವರಿಗೆ ;");

            response.setHeader12("ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು , " +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ  \n" +
                    "                  \n"+
                    "ಪ್ರತಿಯನ್ನು    ಮಾಹಿತಿ ಹಾಗೂ ಕ್ರಮಕ್ಕಾಗಿ ;\n" +
                    "                  \n"+
                    "1 ಜಿಲ್ಲಾ    ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು , ________________________________________\n" +
                    "                    \n"+
                    "2. ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು, ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ  " +apiResponse.getContent().get(0).getTscName()+ "\n" +
                    "                     \n"+
                    "3. ರೇಷ್ಮೆ    ಜಂಟಿ ನಿರ್ದೇಶಕರು , ಬೆಂಗಳೂರು ವಿಭಾಗ , ಬೆಂಗಳೂರು \n" +
                    "                    \n"+
                    "4. ಶ್ರೀ/ಶ್ರೀಮತಿ   "  +apiResponse.getContent().get(0).getFarmerFirstName() +"  ರವರುಗಳ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ. \n" +
                    "                            \n"+
                    "                            \n"+
                    "                            \n"+
                    "                                                                                                                            ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName());


            response.setLogurl("/reports/Seal_of_Karnataka.PNG");


//            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
//                    "                       \n" +
//                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
//                    "                                      \n" +
//                    "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
//            response.setHeader19("");
//            response.setDate(apiResponse.getContent().get(0).getDate());
//            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
//            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
//            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
//            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
//            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
//            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
//            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
//            response.setCost(apiResponse.getContent().get(0).getCost());
//            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
//            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
//            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
//            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
//            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
//            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
//            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
//            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
//            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
//            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
//            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
//            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
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
    private JRDataSource getDataSourceForSanctionOrderPmksycompany(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchSanctionOrderPmksyCompany(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು,   ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,  " +apiResponse.getContent().get(0).getDistrictName() +"   ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು");
            response.setHeader4("ವಿಷಯ  : ");
            response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ     ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ (PMKSY) ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕ \n" +
                    "                            \n"+
                    "ಅಳವಡಿಕೆಗಾಗಿ  ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() + "   ರವರಿಗೆ  ಹೆಚ್ಚುವರಿ  ರಾಜ್ಯದ ಪಾಲು  ರೂ.  " +new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + "/- ಗಳ   ಸಹಾಯಧನ   ಮಂಜೂರಾತಿ   ಬಗ್ಗೆ.");
//            response.setHeader21( " ,ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ನಿರ್ಮಿಸಿರುವ - ಚದರ ಅಡಿಗಳ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "  ಗಳ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1.	ರೇಷ್ಮೆ    ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆ ಸಂಖ್ಯೆ  : _____________________________________ ದಿನಾಂಕ:__________________________________\n"+
                    "                          \n"+
                    "2.	ರೇಷ್ಮೆ  ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,    "  +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ ಇವರ ಪ್ರಸ್ತಾವನೆ ದಿನಾಂಕ:_________________________________\n"+
                    "                     \n"+
                    "3.	ರೇಷ್ಮೆ  ಅಭಿವೃದ್ದಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ಕೃಷಿ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರು ರವರ ಪತ್ರದ ಸಂಖ್ಯೆ   : ________________________________________ ದಿನಾಂಕ:_____________________________\n"+
                    "                                   \n"+
                    "4.	ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ  : ತೋಇ 61 ರೇಕೃವಿ 2019, ಬೆಂಗಳೂರು, ದಿನಾಂಕ :22.08.2023");


            response.setHeader8 ("           " +apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ     ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ (PMKSY)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ\n" +
                    "                                      \n"+
                    "ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ  ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್ ಮೇಲ್ಪಟ್ಟು    5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ \n" +
                    "                         \n" +
                    "ವರ್ಗದ  ರೈತರಿಗೆ  ಘಟಕ ದರದ ಶೇ.45 ರ  ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ     ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                    "                     \n"+
                    apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗದ ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ (2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಿದೆ.  " +apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕಿನ    " +apiResponse.getContent().get(0).getTscName()+ "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                    "                           \n"+
                    "ಕೇಂದ್ರದ  ವ್ಯಾಪ್ತಿಯ ಶ್ರೀ/ಶ್ರೀಮತಿ   "+apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು  " +apiResponse.getContent().get(0).getScCategoryName() +"  ವರ್ಗಕ್ಕೆ    ಸೇರಿದವರಾಗಿದ್ದು  ,   "+apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕು ,  "+apiResponse.getContent().get(0).getHobliName()+ "  ಹೋಬಳಿ , " +apiResponse.getContent().get(0).getVillageName()+ "  ಗ್ರಾಮದ\n" +
                    "                                    \n"+
                    "ಸರ್ವೆ ನಂ. " +apiResponse.getContent().get(0).getSurveyNumber() +  " ರ   " +apiResponse.getContent().get(0).getHectareName() + "   ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ     " +apiResponse.getContent().get(0).getSpacingName()+   "    ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು    ಇಲಾಖೆ ಅಂಗೀಕೃತ ಸಂಸ್ಥೆ \n" +
                    "     \n" +
                    apiResponse.getContent().get(0).getVendorName() +  "   ಸರಬರಾಜು ಪಡೆದು  ಸದರಿ ಸಂಸ್ಥೆಯ ಟ್ಯಾಕ್ಸ್    ಇನ್ವಾಯ್ಸ್     ಸಂಖ್ಯೆ  : ________________________________________ ದಿನಾಂಕ :__________________________________ರನ್ವಯ\n"+
                    "                                                  \n"+
                    "ದಿನಾಂಕ:______________________________________________ರಂದು  ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು,  ಅಳವಡಿಸಿರುವ ಹನಿ ನೀರಾವರಿ ಘಟಕವು ತೃಪ್ತಿಕರವಾಗಿ\n" +
                    "                   \n" +
                    "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ ಶ್ರೀ/ಶ್ರೀಮತಿ    " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು ದೃಢಪಡಿಸಿರುತ್ತಾರೆ . ಸದರಿ ಘಟಕವು ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,   "  +apiResponse.getContent().get(0).getTalukName()+  "  ವಿಭಾಗ\n" +
                    "                        \n" +
                    "ಇವರಿಂದ ದಿನಾಂಕ  : _____________________________________________ರಂದು   ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ  ಹಾಗೂ ಮಂಜೂರಾತಿ ನೀಡಲು ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ. \n" +
                    "                   \n"+
                    "ಕಾರ್ಯಕ್ರಮದ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಫಲಾನುಭವಿಯು ಘಟಕ ದರದ ತನ್ನ    ಪಾಲಿನ ಹಣವನ್ನು    ಮಾತ್ರವೇ ಘಟಕ ಸರಬರಾಜುದಾರರಿಗೆ ಪಾವತಿಸಿದ್ದಲ್ಲಿ  ,  ರೈತರಿಂದ NOC\n" +
                    "                  \n"+
                    "ಪಡೆದು  ಸಂಬಂಧಿಸಿದ ಸಂಸ್ಥೆಗೆ ಸಹಾಯಧನ ಪಾವತಿಸಲು ಅವಕಾಶವಿದೆ . ಉಲ್ಲೇಖ (3) ರನ್ವಯ ಘಟಕದರ ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount()+ "  /- ಗಳ ಶೇ.90 ರ ಸಹಾಯಧನ ರೂ.   " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP) + "  /-\n"+
                    "                            \n"+
                    "ಗಳಲ್ಲಿ   ಕೇಂದ್ರ  ಹಾಗೂ ರಾಜ್ಯದ ಕಡ್ಡಾಯ ಪಾಲಿನ ಶೇ 55 ಸಹಾಯಧನ ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) +   " /- ಗಳನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಮತ್ತು    ರಾಜ್ಯದ ಹೆಚ್ಚುವರಿ ಪಾಲಿನ\n" +
                    "                         \n"+
                    "ಶೇ 35 ಸಹಾಯಧನ  ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) +   "   ಅನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಮಂಜೂರು ಮಾಡಲು ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,  "+apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ  ರವರು\n"+
                    "                                                   \n" +
                    "ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ .  ಉಲ್ಲೇಖ (3) ರ  ಪತ್ರದಲ್ಲಿ    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ  "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ  ಹೆಚ್ಚುವರಿ ರಾಜ್ಯದ ಪಾಲು  ಅನುದಾನವು ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ ಘಟಕ\n"+
                    "              \n" +
                    "ದರದ  ಶೇ 35ರ ಸಹಾಯಧನ  ರೂ.  "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) +"  /-ಗಳನ್ನು    ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ರೀತ್ಯಾ     ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದೆ . ಅದರಂತೆ ಈ ಆದೇಶ.");

            response.setHeader9("       ಆದೇಶ ಸಂಖ್ಯೆ  :__________________________________________                                                                                                    ದಿನಾಂಕ: ___________________________________");

            response.setHeader24("ಪೀಠಿಕೆ  :-");

            response.setHeader10( "              ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸಿರುವಂತೆ  "+apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ  "+apiResponse.getContent().get(0).getTalukName()+ "  ತಾಲ್ಲೂಕಿನ    "+apiResponse.getContent().get(0).getTscName()+ "   ತಾಂತ್ರಿಕ ಸೇವಾ  ಕೇಂದ್ರ\n" +
                    "                  \n"+
                    "ವ್ಯಾಪ್ತಿಯ  ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ   ಪುರಸ್ಕೃತ ಪ್ರಧಾನ ಮಂತ್ರಿ    ಕೃಷಿ ಸಿಂಚಾಯಿ ಯೋಜನೆ (PMKSY)  ಯೋಜನೆಯಡಿ ___________________________________ \n" +
                    "                                              \n" +
                    "ಹೆಕ್ಟೇರ್  ಪ್ರದೇಶದಲ್ಲಿ   _____________________________ ಅಂತರದಲ್ಲಿ   ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿರುವುದಕ್ಕೆ    ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ\n" +
                    "                       \n" +
                    "ಅನ್ವಯ  ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ”  "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ  ಹೆಚ್ಚುವರಿ ರಾಜ್ಯದ ಪಾಲು ಘಟಕ  ದರದ  ಶೇ 35ರ  ಸಹಾಯಧನ ರೂ.  _____________________________________________________/-\n" +
                    "                           \n" +
                    "(ರೂ.____________________________________________________________________________________________________________________)ಮಾತ್ರಗಳನ್ನು    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ ಫಲಾನುಭವಿಯ\n"+
                    "                                          \n" +
                    "ವಂತಿಗೆಯನ್ನು     ಅನುಮೋದಿತ  ಸಂಸ್ಥೆಗೆ  ಪಾವತಿಸಿ ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಲ್ಲಿ    ಫಲಾನುಭವಿಗಳಿಂದ NOC ಪಡೆದು ಸರ್ಕಾರದ ಸಹಾಯಧನವನ್ನು   \n" +
                    "                                           \n"+
                    "ಸರಬರಾಜು  ಸಂಸ್ಥೆಗೆ  ಪಾವತಿಸಲು  ಅವಕಾಶವಿರುವಂತೆ ,  ಆರ್ಥಿಕ   ಪ್ರತ್ಯಾಯೋಜನೆ  ಆದೇಶ ಸಂಖ್ಯೆ    : ತೋಇ 61 ರೇಕೃವಿ 2019 , ಬೆಂಗಳೂರು , ದಿನಾಂಕ:22.08.2023 \n" +
                    "                 \n" +
                    "ರಂತೆ ಮೆ :   "  +apiResponse.getContent().get(0).getVendorName() + "   ಇವರಿಗೆ   ಪಾವತಿಸಲು ರೂ.  _____________________________________________________/- ಗಳಿಗೆ  ಮಂಜೂರಾತಿ  ನೀಡಿದೆ . ಸದರಿ ವೆಚ್ಚವನ್ನು  ಲೆಕ್ಕ   ಶೀರ್ಷಿಕೆ\n" +
                    "                       \n" +
                    apiResponse.getContent().get(0).getScHeadAccountName() + "   ಯಡಿ ಭರಿಸುವುದು." );
            response.setHeader11("ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು\n" +
                    "      \n"+
                    "ಜಿ.ಪಂ.,   " +apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("ಇವರಿಗೆ ;");
            response.setHeader12("ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು , " +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ  \n" +
                    "                  \n"+
                    "ಪ್ರತಿಯನ್ನು    ಮಾಹಿತಿ ಹಾಗೂ ಕ್ರಮಕ್ಕಾಗಿ ;\n" +
                    "                  \n"+
                    "1 ಜಿಲ್ಲಾ    ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು , ________________________________________\n" +
                    "                    \n"+
                    "2. ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು, ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ  " +apiResponse.getContent().get(0).getTscName()+ "\n" +
                    "                     \n"+
                    "3. ರೇಷ್ಮೆ    ಜಂಟಿ ನಿರ್ದೇಶಕರು , ಬೆಂಗಳೂರು ವಿಭಾಗ , ಬೆಂಗಳೂರು \n" +
                    "                    \n"+
                    "4. ಮೆ :   " +apiResponse.getContent().get(0).getVendorName()+ "\n" +
                    "                            \n"+
                    "5. ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName() +"   ರವರುಗಳ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ. \n" +
                    "                            \n"+
                    "                            \n"+
                    "                            \n"+
                    "                                                                                                                            ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName());

            response.setLogurl("/reports/Seal_of_Karnataka.PNG");

//            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
//                    "                       \n" +
//                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
//                    "                                      \n" +
//                    "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
//            response.setHeader19("");
//            response.setDate(apiResponse.getContent().get(0).getDate());
//            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
//            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
//            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
//            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
//            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
//            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
//            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
//            response.setCost(apiResponse.getContent().get(0).getCost());
//            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
//            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
//            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
//            response.setVendorBankName("   ಬ್ಯಾಂಕ್ ಶಕೇ :  " +apiResponse.getContent().get(0).getVendorName());
//            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
//            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
//            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
//            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
//            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
//            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
//            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
//            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private JRDataSource getDataSourceForSanctionOrderPDMCcompany(CheckInspectionStatusRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchSanctionOrderPDMCCompany(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು,   ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್,   "  +apiResponse.getContent().get(0).getDistrictName()+  "   ರವರ ಕಚೇರಿ ನಡವಳಿಗಳು");
            response.setHeader4("ವಿಷಯ  : ");
            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ ಸಾಲಿನಲ್ಲಿ     ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ \n" +
                        "                            \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() + "  ರವರಿಗೆ ರೂ.  " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳ ಸಹಾಯಧನ ಮಂಜೂರಾತಿ ಬಗ್ಗೆ  .");
            }else{

                response.setHeader20( apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ ಸಾಲಿನಲ್ಲಿ     ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)  ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ \n" +
                        "                            \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() + "  ರವರಿಗೆ ರೂ.  " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳ ಸಹಾಯಧನ ಮಂಜೂರಾತಿ ಬಗ್ಗೆ  .");
            }
//            response.setHeader21( " ,ರವರು(ಸಾಮಾನ್ಯ/SCP/TSP) ನಿರ್ಮಿಸಿರುವ - ಚದರ ಅಡಿಗಳ ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ ರೂ . " +apiResponse.getContent().get(0).getCost()+ "  ಗಳ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಬಗ್ಗೆ ");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1.	ರೇಷ್ಮೆ   ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆ ಸಂಖ್ಯೆ   : ______________________________________________ ದಿನಾಂಕ:__________________________________\n"+
                    "                          \n"+
                    "2.	ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,    "  +apiResponse.getContent().get(0).getTalukName()+  "  ವಿಭಾಗ ಇವರ ಪ್ರಸ್ತಾವನೆ ದಿನಾಂಕ:__________________________________\n"+
                    "                     \n"+
                    "3.	ರೇಷ್ಮೆ   ಅಭಿವೃದ್ದಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ  ಕೃಷಿ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರು ರವರ ಪತ್ರದ ಸಂಖ್ಯೆ   : ______________________________________ ದಿನಾಂಕ:___________________________\n"+
                    "                                   \n"+
                    "4.	ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ  :  ತೋಇ 61 ರೇಕೃವಿ 2019, ಬೆಂಗಳೂರು, ದಿನಾಂಕ  :  22.08.2023");

            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader8 ("             " +apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)   ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ\n" +
                        "                                      \n"+
                        "ಎಲ್ಲಾ    ವರ್ಗದ  ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್ ಮೇಲ್ಪಟ್ಟು     5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ\n" +
                        "                         \n" +
                        "ಘಟಕ   ದರದ ಶೇ.45 ರ  ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ      ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                        "                     \n"+
//
                        apiResponse.getContent().get(0).getTalukName()+   "  ವಿಭಾಗದ ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ (2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು    ಪರಿಶೀಲಿಸಿದೆ   "  +apiResponse.getContent().get(0).getTalukName()+  "   ತಾಲ್ಲೂಕಿನ   "  +apiResponse.getContent().get(0).getTscName()+ "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                        "                           \n"+
                        "ಕೇಂದ್ರದ  ವ್ಯಾಪ್ತಿಯ ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು   " +apiResponse.getContent().get(0).getScCategoryName()+  "   ವರ್ಗಕ್ಕೆ ಸೇರಿದವರಾಗಿದ್ದು  ,  "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು,   "+apiResponse.getContent().get(0).getHobliName()+ "  ಹೋಬಳಿ  ,  "+apiResponse.getContent().get(0).getVillageName()+ "\n" +
                        "                                    \n"+
                        "ಗ್ರಾಮದ  ಸರ್ವೆ ನಂ.  " +apiResponse.getContent().get(0).getSurveyNumber() + "  ರ   " +apiResponse.getContent().get(0).getHectareName() + "  ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ    " +apiResponse.getContent().get(0).getSpacingName() + "  ಅಂತರದಲ್ಲಿ     ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು    ಇಲಾಖೆ ಅಂಗೀಕೃತ ಸಂಸ್ಥೆ  \n" +
                        "     \n" +
                        apiResponse.getContent().get(0).getVendorName() + "   ಸರಬರಾಜು ಪಡೆದು  ಸದರಿ ಸಂಸ್ಥೆಯ ಟ್ಯಾಕ್ಸ್   ಇನ್ ವಾಯ್ಸ್   ಸಂಖ್ಯೆ  :______________________________________ ದಿನಾಂಕ :________________________________ ರನ್ವಯ\n"+
                        "                                                  \n"+
                        "ದಿನಾಂಕ: ________________________________ ರಂದು ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು,  ಅಳವಡಿಸಿರುವ ಹನಿ ನೀರಾವರಿ ಘಟಕವು ತೃಪ್ತಿಕರವಾಗಿ\n" +
                        "                                                   \n"+
                        "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ  ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ದೃಢಪಡಿಸಿರುತ್ತಾರೆ. ಸದರಿ   ಘಟಕವು    ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರ  2  ಹೆಕ್ಟೇರ್  ಮೇಲ್ಪಟ್ಟ \n"+
                        "                                               \n"+
                        "ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ     ಅಳವಡಿಸಿಕೊಂಡಿರುವುದಾಗಿದ್ದು      ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  "+apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ  ಇವರಿಂದ ದಿನಾಂಕ : ________________________________\n" +
                        "                       \n"+
                        " ರಂದು ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ  ಹಾಗೂ ಮಂಜೂರಾತಿ ನೀಡಲು  ಶಿಫಾರಸ್ಸು   ಮಾಡಿರುತ್ತಾರೆ.\n" +
                        "                           \n" +
                        "ಕಾರ್ಯಕ್ರಮದ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಫಲಾನುಭವಿಯು ಘಟಕ ದರದ ತನ್ನ   ಪಾಲಿನ  ಹಣವನ್ನು    ಮಾತ್ರವೇ ಘಟಕ ಸರಬರಾಜುದಾರರಿಗೆ  ಪಾವತಿಸಿದ್ದಲ್ಲಿ   , ರೈತರಿಂದ NOC\n" +
                        "              \n"+
                        "ಪಡೆದು ಸಂಬಂಧಿಸಿದ ಸಂಸ್ಥೆಗೆ ಸಹಾಯಧನ ಪಾವತಿಸಲು ಅವಕಾಶವಿದೆ . ಉಲ್ಲೇಖ (3) ರನ್ವಯ ಘಟಕದರ ರೂ.   " +apiResponse.getContent().get(0).getSanctionAmount()+ " /- ಗಳ ಕೇಂದ್ರ   ಹಾಗೂ ರಾಜ್ಯದ ಕಡ್ಡಾಯ ಪಾಲಿನ \n" +
                        "                   \n" +
                        "ಶೇ 45 ರ ಸಹಾಯಧನ ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP) + "  /- ಗಳನ್ನು   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಮಂಜೂರು ಮಾಡಲು ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ,  "+apiResponse.getContent().get(0).getTalukName()+ "  ವಿಭಾಗ\n" +
                        "                     \n" +
                        "ರವರು  ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುತ್ತಾರೆ . ಉಲ್ಲೇಖ (3) ರ ಪತ್ರದಲ್ಲಿ   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ರಡಿ ಅನುದಾನವು ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ ಘಟಕ ದರದ ಶೇ 45\n" +
                        "           \n" +
                        "ಸಹಾಯಧನ  ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ರೀತ್ಯಾ ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದೆ. ಅದರಂತೆ ಈ ಆದೇಶ.");

            }else{
                response.setHeader8 ("             " +apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ    ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC)   ಯೋಜನೆಯಡಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಕೆಗಾಗಿ\n" +
                        "                                      \n"+
                        "ಎಲ್ಲಾ    ವರ್ಗದ  ರೈತರಿಗೆ ಮೊದಲ 2.೦೦ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದ ವರಗೆ ಘಟಕ ದರದ ಶೇ.90 ಹಾಗೂ 2.00 ಹೆಕ್ಟೇರ್ ಮೇಲ್ಪಟ್ಟು     5.೦೦ ಹೆಕ್ಟೇರ್ ವರಗೆ ಎಲ್ಲಾ    ವರ್ಗದ ರೈತರಿಗೆ \n" +
                        "                         \n" +
                        "ಘಟಕ  ದರದ ಶೇ.45 ರ  ಸಹಾಯಧನ ನೀಡಲು ಉಲ್ಲೇಖ (1) ರ ಮಾರ್ಗಸೂಚಿಯಲ್ಲಿ      ಅವಕಾಶವಿರುತ್ತದೆ.\n"+
                        "                     \n"+
//
                        apiResponse.getContent().get(0).getTalukName()+   "  ವಿಭಾಗದ ರೇಷ್ಮೆ    ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಉಲ್ಲೇಖ (2)ರ ಪ್ರಸ್ತಾವನೆಯನ್ನು    ಪರಿಶೀಲಿಸಿದೆ   "  +apiResponse.getContent().get(0).getTalukName()+  "   ತಾಲ್ಲೂಕಿನ   "  +apiResponse.getContent().get(0).getTscName()+ "  ತಾಂತ್ರಿಕ ಸೇವಾ\n" +
                        "                           \n"+
                        "ಕೇಂದ್ರದ  ವ್ಯಾಪ್ತಿಯ ಶ್ರೀ/ಶ್ರೀಮತಿ  " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು   " +apiResponse.getContent().get(0).getScCategoryName()+  "   ವರ್ಗಕ್ಕೆ ಸೇರಿದವರಾಗಿದ್ದು  ,  "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕು,   "+apiResponse.getContent().get(0).getHobliName()+ "  ಹೋಬಳಿ  ,  "+apiResponse.getContent().get(0).getVillageName()+ "\n" +
                        "                                    \n"+
                        "ಗ್ರಾಮದ  ಸರ್ವೆ ನಂ.  " +apiResponse.getContent().get(0).getSurveyNumber() + "  ರ    " +apiResponse.getContent().get(0).getHectareName() + "  ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ    " +apiResponse.getContent().get(0).getSpacingName() + "  ಅಂತರದಲ್ಲಿ     ಬೆಳೆಸಿರುವ ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕವನ್ನು    ಇಲಾಖೆ ಅಂಗೀಕೃತ ಸಂಸ್ಥೆ  \n" +
                        "     \n" +
                        apiResponse.getContent().get(0).getVendorName() + "   ಸರಬರಾಜು ಪಡೆದು  ಸದರಿ ಸಂಸ್ಥೆಯ ಟ್ಯಾಕ್ಸ್   ಇನ್ ವಾಯ್ಸ್   ಸಂಖ್ಯೆ  :______________________________________ ದಿನಾಂಕ :________________________________ ರನ್ವಯ\n"+
                        "                                                  \n"+
                        "ದಿನಾಂಕ: ________________________________ ರಂದು ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಿರುತ್ತಾರೆಂದು,   ಅಳವಡಿಸಿರುವ ಹನಿ ನೀರಾವರಿ ಘಟಕವು  ತೃಪ್ತಿಕರವಾಗಿ\n" +
                        "                                                   \n"+
                        "ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವುದಾಗಿ    ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"   ಇವರು  ದೃಢಪಡಿಸಿರುತ್ತಾರೆ.  ಸದರಿ   ಘಟಕವು  ರೇಷ್ಮೆ  ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು ,  "  +apiResponse.getContent().get(0).getTalukName()+  "   ವಿಭಾಗ  \n" +
                        "                   \n" +
                        "ಇವರಿಂದ  ದಿನಾಂಕ : ________________________________ ರಂದು  ಪರಿಶೀಲಿಸಲ್ಪಟ್ಟಿರುತ್ತದೆ   ಹಾಗೂ  ಮಂಜೂರಾತಿ  ನೀಡಲು  ಶಿಫಾರಸ್ಸು     ಮಾಡಿರುತ್ತಾರೆ.\n" +
                        "                           \n" +
                        "ಕಾರ್ಯಕ್ರಮದ  ಮಾರ್ಗಸೂಚಿ  ಅನ್ವಯ ಫಲಾನುಭವಿಯು ಘಟಕ ದರದ ತನ್ನ     ಪಾಲಿನ ಹಣವನ್ನು     ಮಾತ್ರವೇ  ಘಟಕ ಸರಬರಾಜುದಾರರಿಗೆ ಪಾವತಿಸಿದ್ದಲ್ಲಿ  , ರೈತರಿಂದ NOC\n" +
                        "                  \n"+
                        "ಪಡೆದು  ಸಂಬಂಧಿಸಿದ  ಸಂಸ್ಥೆಗೆ  ಸಹಾಯಧನ  ಪಾವತಿಸಲು  ಅವಕಾಶವಿದೆ .  ಉಲ್ಲೇಖ(3) ರನ್ವಯ  ಘಟಕದರ  ರೂ.   " +apiResponse.getContent().get(0).getSanctionAmount()+ " /- ಗಳ  ಶೇ.90 ರ  ಸಹಾಯಧನ  ರೂ.   " + new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP) + " /- \n"+
                        "                            \n"+
                        "ಗಳಲ್ಲಿ    ಕೇಂದ್ರ  ಹಾಗೂ  ರಾಜ್ಯದ  ಕಡ್ಡಾಯ  ಪಾಲಿನ ಶೇ 55 ಸಹಾಯಧನ ರೂ.  "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "    ರಡಿ ಮತ್ತು ರಾಜ್ಯದ ಹೆಚ್ಚುವರಿ ಪಾಲಿನ\n" +
                        "                         \n"+
                        "ಶೇ 35 ಸಹಾಯಧನ  ರೂ.  "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.35")).setScale(2, RoundingMode.HALF_UP) + "   ಅನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ  ಮಂಜೂರು  ಮಾಡಲು ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು ,   "  +apiResponse.getContent().get(0).getTalukName()+  "  ವಿಭಾಗ  ರವರು\n"+
                        "                                                   \n" +
                        "ಶಿಫಾರಸ್ಸು      ಮಾಡಿರುತ್ತಾರೆ  .   ಉಲ್ಲೇಖ (3) ರ   ಪತ್ರದಲ್ಲಿ      ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName()+ "   ರಡಿ   ಕೇಂದ್ರ    ಪಾಲು  ಶೇ 33  ಮತ್ತು     ರಾಜ್ಯ    ಪಾಲು  ಶೇ 22  ಅನುದಾನವು \n"+
                        "              \n" +
                        "ಬಿಡುಗಡೆಯಾಗಿರುವಂತೆ   ಘಟಕ ದರದ ಶೇ 55 ಸಹಾಯಧನ  ರೂ.   "+ new BigDecimal(apiResponse.getContent().get(0).getSanctionAmount()).multiply(new BigDecimal("0.55")).setScale(2, RoundingMode.HALF_UP) + " /- ಗಳನ್ನು    ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ರೀತ್ಯಾ     ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದೆ.  ಅದರಂತೆ ಈ ಆದೇಶ .");
            }
            response.setHeader9("ಆದೇಶ ಸಂಖ್ಯೆ   :_______________________________________________                                                                                               ದಿನಾಂಕ: ________________________________");

            response.setHeader24("ಪೀಠಿಕೆ  :-");

            if (Double.parseDouble(apiResponse.getContent().get(0).getHectareName()) > 2) {
                response.setHeader10( "               ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿರುವಂತೆ  " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕಿನ    "+apiResponse.getContent().get(0).getTscName()+ "  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ   ವ್ಯಾಪ್ತಿಯ\n" +
                        "                  \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC) ಯೋಜನೆಯಡಿ _________________________ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ   _________________________\n" +
                        "                                              \n" +
                        "ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ  2 ಹೆಕ್ಟೇರ್ ಮೇಲ್ಪಟ್ಟ    ಹಿಪ್ಪುನೇರಳೆ  ತೋಟಕ್ಕೆ    ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿರುವುದಕ್ಕೆ    ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಲೆಕ್ಕ   ಶೀರ್ಷಿಕೆ ”\n" +
                        "                       \n"+
                        apiResponse.getContent().get(0).getScHeadAccountName() +"   ರಡಿ  ಕೇಂದ್ರ   ಹಾಗೂ  ರಾಜ್ಯದ  ಕಡ್ಡಾಯ   ಪಾಲು  ಘಟಕ   ದರದ   ಶೇ 45 ರ   ಸಹಾಯಧನ   ರೂ. _________________________________________________ /- \n" +
                        "                       \n" +
                        "(ರೂ._____________________________________________________________________________________________.)  ಮಾತ್ರಗಳನ್ನು  ಮಾರ್ಗಸೂಚಿಯನ್ವಯ ಫಲಾನುಭವಿಯ ವಂತಿಗೆಯನ್ನು \n" +
                        "                \n" +
                        "ಅನುಮೋದಿತ ಸಂಸ್ಥೆಗೆ ಪಾವತಿಸಿ ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಲ್ಲಿ    ಫಲಾನುಭವಿಗಳಿಂದ NOC ಪಡೆದು  ಸರ್ಕಾರದ ಸಹಾಯಧನವನ್ನು    ಸರಬರಾಜು ಸಂಸ್ಥೆಗೆ \n" +
                        "                  \n" +
                        "ಪಾವತಿಸಲು ಅವಕಾಶವಿರುವಂತೆ,  ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ ಆದೇಶ ಸಂಖ್ಯೆ  : ತೋಇ 61 ರೇಕೃವಿ 2019, ಬೆಂಗಳೂರು,  ದಿನಾಂಕ:22.08.2023 ರಂತೆ ಮೆ : "+apiResponse.getContent().get(0).getVendorName() +  "\n" +
                        "                  \n" +
                        "ಇವರಿಗೆ ಪಾವತಿಸಲು ರೂ.  __________________________________ /- ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ. ಸದರಿ ವೆಚ್ಚವನ್ನು   ಲೆಕ್ಕ  ಶೀರ್ಷಿಕೆ   "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ಯಡಿ ಭರಿಸುವುದು.");

            }else{
                response.setHeader10( "               ಪೀಠಿಕೆಯಲ್ಲಿ     ವಿವರಿಸಿರುವಂತೆ  " +apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ     " +apiResponse.getContent().get(0).getDistrictName() +"  ಜಿಲ್ಲೆಯ   "+apiResponse.getContent().get(0).getTalukName()+ "   ತಾಲ್ಲೂಕಿನ   "+apiResponse.getContent().get(0).getTscName()+ "  ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ     ವ್ಯಾಪ್ತಿಯ\n" +
                        "                  \n"+
                        "ಶ್ರೀ/ಶ್ರೀಮತಿ   " +apiResponse.getContent().get(0).getFarmerFirstName() +"  ಇವರು ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ ಪ್ರತಿ ಹನಿಗೆ ಅಧಿಕ ಬೆಳೆ (PDMC) ಯೋಜನೆಯಡಿ _________________________ ಹೆಕ್ಟೇರ್ ಪ್ರದೇಶದಲ್ಲಿ   _________________________\n" +
                        "                                              \n" +
                        "ಅಂತರದಲ್ಲಿ    ಬೆಳೆಸಿರುವ  ಹಿಪ್ಪುನೇರಳೆ ತೋಟಕ್ಕೆ     ಹನಿ ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿರುವುದಕ್ಕೆ     ಇಲಾಖೆಯ ಮಾರ್ಗಸೂಚಿ ಅನ್ವಯ ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ ”  "  +apiResponse.getContent().get(0).getScHeadAccountName() +"\n" +
                        "                       \n" +
                        "ರಡಿ   ಕೇಂದ್ರ     ಪಾಲು   ಶೇ 33   ಮತ್ತು      ರಾಜ್ಯ      ಪಾಲು   ಶೇ 22  ಘಟಕ   ದರದ 55 ರ   ಸಹಾಯಧನ   ರೂ.   ___________________________________________________ /- \n" +
                        "                           \n" +
                        "(ರೂ._____________________________________________________________________________________________) ಮಾತ್ರಗಳನ್ನು    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ ಫಲಾನುಭವಿಯ  ವಂತಿಗೆಯನ್ನು \n"+
                        "                                          \n" +
                        "ಅನುಮೋದಿತ  ಸಂಸ್ಥೆಗೆ ಪಾವತಿಸಿ ಹನಿ  ನೀರಾವರಿ ಘಟಕ ಅಳವಡಿಸಿಕೊಂಡಲ್ಲಿ    ಫಲಾನುಭವಿಗಳಿಂದ NOC ಪಡೆದು   ಸರ್ಕಾರದ  ಸಹಾಯಧನವನ್ನು    ಸರಬರಾಜು  ಸಂಸ್ಥೆಗೆ \n" +
                        "                                           \n"+
                        "ಪಾವತಿಸಲು   ಅವಕಾಶವಿರುವಂತೆ ,ಆರ್ಥಿಕ  ಪ್ರತ್ಯಾಯೋಜನೆ  ಆದೇಶ ಸಂಖ್ಯೆ   :  ತೋಇ 61 ರೇಕೃವಿ 2019, ಬೆಂಗಳೂರು, ದಿನಾಂಕ : 22.08.2023 ರಂತೆ ಮೆ : "+apiResponse.getContent().get(0).getVendorName() +  "\n"+
                        "            \n" +
                        "ಇವರಿಗೆ ಪಾವತಿಸಲು ರೂ.  ___________________________________ /-  ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ .  ಸದರಿ ವೆಚ್ಚವನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ  "+apiResponse.getContent().get(0).getScHeadAccountName() + "  ಯಡಿ ಭರಿಸುವುದು ." );

            }
            response.setHeader11("ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು\n" +
                    "      \n"+
                    "ಜಿ.ಪಂ.,   " +apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("ಇವರಿಗೆ  ;");

            response.setHeader12("ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು , " +apiResponse.getContent().get(0).getTalukName()+ "   ವಿಭಾಗ  \n" +
                    "                  \n"+
                    "ಪ್ರತಿಯನ್ನು    ಮಾಹಿತಿ ಹಾಗೂ ಕ್ರಮಕ್ಕಾಗಿ ;\n" +
                    "                  \n"+
                    "1 ಜಿಲ್ಲಾ    ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು , ________________________________________\n" +
                    "                    \n"+
                    "2. ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು, ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ  " +apiResponse.getContent().get(0).getTscName()+ "\n" +
                    "                     \n"+
                    "3. ರೇಷ್ಮೆ    ಜಂಟಿ ನಿರ್ದೇಶಕರು , ಬೆಂಗಳೂರು ವಿಭಾಗ , ಬೆಂಗಳೂರು \n" +
                    "                    \n"+
                    "4. ಮೆ :   " +apiResponse.getContent().get(0).getVendorName()+ "\n" +
                    "                            \n"+
                    "5. ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName() +"   ರವರುಗಳ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ. \n" +
                    "                            \n"+
                    "                            \n"+
                    "                            \n"+
                    "                                                                                                                                ಶ್ರೀ/ಶ್ರೀಮತಿ    "  +apiResponse.getContent().get(0).getFarmerFirstName());
//            "ರೇಷ್ಮೆ   ಸಹಾಯಕ ನಿರ್ದೇಶಕರು , __________________ ವಿಭಾಗ \n" +
//                    "                  \n"+
//                    "ಪ್ರತಿಯನ್ನು    ಮಾಹಿತಿ ಹಾಗೂ ಕ್ರಮಕ್ಕಾಗಿ\n" +
//                    "                  \n"+
//                    "1 ಜಿಲ್ಲಾ    ಖಜಾನೆ ಅಧಿಕಾರಿಗಳು , ___________________\n" +
//                    "                    \n"+
//                    "2. ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು , ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ   _________________\n" +
//                    "                     \n"+
//                    "3. ರೇಷ್ಮೆ   ಜಂಟಿ ನಿರ್ದೇಶಕರು , ಬೆಂಗಳೂರು ವಿಭಾಗ , ಬೆಂಗಳೂರು \n" +
//                    "                    \n"+
//                    "4. ಮೆ :__________________________________  \n" +
//                    "                    \n"+
//                    "5. ಶ್ರೀ/ಶ್ರೀಮತಿ  "  +apiResponse.getContent().get(0).getFarmerFirstName() +" ರವರುಗಳ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .");

//            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
//                    "                       \n" +
//                    " ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
//                    "                                      \n" +
//                    "ಗ್ರಾಮ" +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ " + apiResponse.getContent().get(0).getDistrictName());
//            response.setHeader19("");
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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }





    private JRDataSource getDataSourceForSanctionOrder(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSanction(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();
        if (apiResponse.getContent()!= null) {
            response.setHeader1("ರೇಷ್ಮೆ   ಜಂಟಿ  ನಿರ್ದೇಶಕರು/ರೇಷ್ಮೆ   ಉಪ ನಿರ್ದೇಶಕರು ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್ ರವರ ನಡವಳಿಗಳು ");
            response.setHeader4("ವಿಷಯ  : ");
            response.setHeader20( "           " + apiResponse.getContent().get(0).getFinancialYear() + "  ನೇ ಸಾಲಿನಲ್ಲಿ    ಕೇಂದ್ರ  ರೇಷ್ಮೆ   ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಇಲಾಖೆಯು ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್   ಸಮಗ್ರ    ಯೋಜನೆಯಡಿ  ಶ್ರೀಮತಿ./.ಶ್ರೀ. \n" +
                    "                    \n" +
                    apiResponse.getContent().get(0).getFarmerFirstName()  +   "    ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  +"  ,ರವರು   " +apiResponse.getContent().get(0).getScCategoryName()+ "   ನಿರ್ಮಿಸಿರುವ - ಚದರ  ಅಡಿಗಳ ರೇಷ್ಮೆ    ಹುಳು  ಸಾಕಾಣಿಕೆ  ಮನೆಗೆ  ರೂ . " +apiResponse.getContent().get(0).getSanctionAmount()+ "   ಗಳ  ಸಹಾಯಧನ \n " +
                    "        \n" +
                    "ಮಂಜೂರು  ಮಾಡುವ  ಬಗ್ಗೆ.");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1. " +apiResponse.getContent().get(0).getFinancialYear() +"  ಸಾಲಿಗೆ ಮುಂದವರೆದ ಸರ್ಕಾರದ ಆದೇಶ ¸ಸಂಖ್ಯೆ   ______________________________________________ ದಿನಾಂಕ   ______________________________________________ \n"+
                    "                               \n"+
                    "2. ರೇಷ್ಮೆ  ಕೃಷಿ ಅಭಿವೃದ್ಧಿ   ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಮಾರ್ಗಸೂಚಿಯ ಸುತೋಲೆ ಸಂಖ್ಯೆ   ______________________________________________ ದಿನಾಂಕ  ______________________________________________\n" +
                    "                        \n" +
                    "3. ರೇಷ್ಮೆ   ಕೃಷಿ ಅಭಿವೃದ್ಧಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಕ್ರಿಯಾಯೋಜನೆ ಸುತೋಲೆ ಸಂಖ್ಯೆ    ದಿನಾಂಕ  ______________________________________________\n" +
                    "                    \n"+
                    "4. ರೇಷ್ಮೆ   ಕೃಷಿ ಅಭಿವೃದ್ಧಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರು ,ಬೆಂಗಳೂರು ,ರವರ ಜ್ಞಾಪನ ಸಂಖ್ಯೆ    ದಿನಾಂಕ  ______________________________________________\n"+
                    "                                     \n"+
                    "5. ಆರ್ಥಿಕ ಪ್ರತ್ಯಾಯೋಜನೆ :ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ   :ತೋಇ/61/ರೇಕೃವಿ/2019.ದಿನಾಂಕ  ______________________________________________\n" +
                    "                                                \n" +
                    "6  ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರು ಜಿಲ್ಲಾ   ಪಂಚಾಯತ್ ,  "+ apiResponse.getContent().get(0).getDistrictName()  +"   ರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ  : ______________________________________________ ದಿನಾಂಕ : ______________________________________________");
            response.setHeader24("ಪೀಠಿಕೆ ");
            response.setHeader8(  "              " + apiResponse.getContent().get(0).getFinancialYear() +"  ನೇ  ಸಾಲಿನಲ್ಲಿ   ವಿವಿಧ ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆಗಳಡಿ ವಿವಿಧ ಕಾರ್ಯಕ್ರಮಗಳ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಉಲ್ಲೇಖ (1) ರಲ್ಲಿ   ಸರ್ಕಾರವು ಆಡಳಿತಾತ್ಮಕ  ಅನುಮೋದನೆಯನ್ನು\n" +
                    "                                       \n"+
                    "ನೀಡಿದ್ದು ,  ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ   ಕೇಂದ್ರವಲಯ ಸಿಲ್ಕ್   ಸಮಗ್ರ    ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ   ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆ ನಿರ್ಮಾಣಕ್ಕೆ ಸಹಾಯಧನ ಕಾರ್ಯಕಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ \n" +
                    "                                       \n"+
                    "ಮಾರ್ಗಸೂಚಿಯನ್ನು    ಉಲ್ಲೇಖ (3)ರಲ್ಲಿ ಕ್ರಿಯಾಯೋಜನೆ ಮತ್ತು ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ    ರೇಷ್ಮೆ   ನಿರ್ದೇಶನಾಲಯದಿಂದ ಅನುಧಾನ ಬಿಡುಗಡೆಯಾಗಿರುತ್ತದೆ .ಇಲಾಖೆಯು  ಕೇಂದ್ರ\n"+
                    "                                       \n"+
                    "ರೇಷ್ಮೆ   ಮಂಡಳಿಯ  ಸಹಯೋಗದೊಂದಿಗೆ ಕೇಂದ್ರ   ವಲಯ ಸಿಲ್ಕ್    ಸಮಗ್ರ    ಯೋಜನೆಯನ್ನು     ಅನುಷ್ಠಾನಗೊಳಿಸಲಾಗುತ್ತಿದೆ.  ಸದರಿ ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ  ಬೆಳೆಗಾರರು\n"+
                    "                                       \n"+
                    "ನಿರ್ಮಾಣ ಮಾಡಿರುವ    "  +apiResponse.getContent().get(0).getSpacingName()+   "    ಚದರ ಅಡಿ ರೇಷ್ಮೆ   ಹುಳು ಸಾಕಾಣಿಕೆ  ಮನೆ ನಿರ್ಮಾಣಕ್ಕೆ   ಸಹಾಯಧನ ನೀಡಬೇಕಾಗಿದ್ದು ,  ಸಾಮಾನ್ಯ  /ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ   ವರ್ಗದಡಿ ಕೇಂದ್ರ  : ರಾಜ್ಯ :\n"+
                    "                                       \n"+
                    "ಫಲಾನುಭವಿ ಪಾಲು  50:25:25/65:25:10 ಆಗಿರುತ್ತದೆ .  " +apiResponse.getContent().get(0).getSpacingName()+   "   ಚದರ ಅಡಿಯ ರೇಷ್ಮೆ   ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆ ನಿರ್ಮಾಣದ  ಘಟಕದರ  ರೂ .  " +apiResponse.getContent().get(0).getSanctionAmount()  + "    ಲಕ್ಷಗಳಿಗೆ  ನಿಗಧಿಪಡಿಸಿದ್ದು  ,\n" +
                    "                                       \n"+
                    "ಇದರಲ್ಲಿ     ಶೇಕಡ 75 ರಷ್ಟನ್ನು    ಅಂದರೆ ರೂ . " +apiResponse.getContent().get(0).getSanctionAmount() + "   ಲಕ್ಷಗಳನ್ನು    ಸಹಾಯಧನವಾಗಿ ನೀಡಲಾಗುತ್ತದೆ . ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ .50/65 ಅಂದರೆ ರೂ.  " +apiResponse.getContent().get(0).getSanctionAmount() + "\n"+
                    "                                       \n"+
                    "ಲಕ್ಷಗಳು  ಮತ್ತು    ರಾಜ್ಯದ  ಪಾಲು  ಘಟಕದರದ ಶೇ . 25 ಅಂದರೆ ರೂ .  " +apiResponse.getContent().get(0).getSanctionAmount() + "  ಲಕ್ಷಗಳು  ಆಗಿರುತ್ತದೆ .  ಕೇಂದ್ರ   ರೇಷ್ಮೆ    ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ    ಅನುದಾನವನ್ನು    ರೇಷ್ಮೆ \n"+
                    "                                       \n"+
                    "ಕೃಷಿ  ಅಭಿವೃದ್ಧಿ     ಆಯುಕ್ತರು    ಹಾಗು ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ  ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ .   " +apiResponse.getContent().get(0).getSanctionAmount() + "   ಲಕ್ಷಗಳನ್ನು     (50/65%)  ಕೇಂದ್ರ \n"+
                    "                                       \n"+
                    "ರೇಷ್ಮೆ    ಮಂಡಳಿ  ಭರಿಸುವುದರಿಂದ ಇದನ್ನು    ರೇಷ್ಮೆ   ಕೃಷಿ    ಅಭಿವೃದ್ಧಿ    ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು , ರೇಷ್ಮೆ   ನಿರ್ದೇಶನಾಲಯ , ಬೆಂಗಳೂರುರವರ  \n" +
                    "                       \n" +
                    "ಕಛೇರಿಯಿಂದ   ಡಿಬಿಟಿ   ಮುಖಾಂತರ ಫಲಾನುಭವಿ  ಬ್ಯಾಂಕ್  ಖಾತೆಗೆ  ನೇರವಾಗಿ  ಜಮಾ  ಮಾಡಲಾಗುತ್ತದೆ . ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು    ರೇಷ್ಮೆ   ಅಭಿವೃದ್ಧಿ   \n"+
                    "                                       \n"+
                    "ಯೋಜನೆ  ಲೆಕ್ಕ   ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + "    ಅಡಿ ರೇಷ್ಮೆ   ಅಭಿವೃದ್ಧಿ   ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರುರವರು    ಖಜಾನೆ  __________________________ 2 \n " +
                    "    \n" +
                    "ಮುಖಾಂತರ   ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು   ಫಲಾನುಭವಿಯ  ಬ್ಯಾಂಕ್  ಖಾತೆಗೆ   ಜಮಾ  ಮಾಡಲಾಗುವುದು . " );
            response.setHeader9("ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ .50/65 ಅಂದರೆ ರೂ ಲಕ್ಷಗಳು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕದರದ ಶೇ . 25 ಅಂದರೆ ರೂ .   ಲಕ್ಷಗಳು ಆಗಿರುತ್ತದೆ . ಕೇಂದ್ರ   ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ  ಅನುದಾನವನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗು ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರವರ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಒದಗಿಸಿರುತ್ತದೆ ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ . ಲಕ್ಷಗಳನ್ನು (50/65%)   ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ,ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯ , ಬೆಂಗಳೂರುರವರ ಕಛೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ . ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35(106)(422)(423) ಅಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ಬೆಂಗಳೂರುರವರು  ಖಜಾನೆ -2 ಮುಖಾಂತರ ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ  ಜಮಾ ಮಾಡಲಾಗುವುದು .  ");
            response.setHeader10(  "              " +apiResponse.getContent().get(0).getDistrictName() + "    ಜಿಲ್ಲೆಯ    " + apiResponse.getContent().get(0).getTalukName() +"    ತಾಲೂಕಿನ   ತಾಂತ್ರಿಕ  ಸೇವಾ  ಕೇಂದ್ರ    ವ್ಯಾಪ್ತಿಯಲ್ಲಿ    " +apiResponse.getContent().get(0).getVillageName()+ "    ಗ್ರಾಮದಲ್ಲಿ    " +apiResponse.getContent().get(0).getScCategoryName()+ "   ವರ್ಗಕ್ಕೆ     ಸೇರಿದ    ಶ್ರೀ ./ಶ್ರೀಮತಿ . \n"+
                    "                                       \n"+
                    apiResponse.getContent().get(0).getFarmerFirstName() + "    ಬಿನ್/ಕೋಂ    " +apiResponse.getContent().get(0).getFatherNameKan() + "   ಇವರು   " +apiResponse.getContent().get(0).getVillageName()+ "   ಗ್ರಾಮದ ಸರ್ವೆನಂ   " +apiResponse.getContent().get(0).getSurveyNumber() + "   ರಲ್ಲಿ   " +  apiResponse.getContent().get(0).getHectareName() + "   ಹೆಕ್ಟೇರು ವಿಸ್ತೀರ್ಣದಲ್ಲಿ    ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಹೊಂದಿದ್ದು    ಗ್ರಾಮದ ಸುರ್ವೆನಂ\n" +
                    "                                                                     \n" +
                    "/ಖಾತೆ  ನ. _____________________________ರಲ್ಲಿ   ಚದರಡಿ ವಿಸ್ತೀರ್ಣದ _____________________________ ಮೇಲ್ಚಾವಣಿಯ    ಪ್ರತ್ಯೇಕ ರೇಷ್ಮೆ  ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಯನ್ನು    ಅಂದಾಜು  ರೂ .  \n" +
                    "               \n" +
                    apiResponse.getContent().get(0).getSanctionAmount()+ "     ಲಕ್ಷಗಳ  ವೆಚ್ಚದಲ್ಲಿ     (ಸ್ವಂತ  ವೆಚ್ಚ   /ಬ್ಯಾಂಕಿನಿಂದ  ಸಾಲ  ಪಡೆದು )ನಿರ್ಮಿಸಿರುವುದನ್ನು     ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿ ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ    ಹಾಗೂ ರೇಷ್ಮೆ   ಸಹಾಯಕ \n" +
                    "                                      \n" +
                    "ನಿರ್ದೇಶಕರು ,   " +apiResponse.getContent().get(0).getTalukName()+ "    ವಿಭಾಗರವರು   ಪರಿಶೀಲಿಸಿ  ಧ್ರಡೀಕರಿಸಿ ಸಲ್ಲಿಸಿದ ಎಲ್ಲ     ಅಗತ್ಯ    ದಾಖಲಾತಿಗಳನ್ನು    ಒಳಗೊಂಡ ಪ್ರಸ್ತಾವನೆಯನ್ನು    "+ apiResponse.getContent().get(0).getDistrictName() +"   ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್\n" +
                    "                         \n" +
                    "ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು  ಪರಿಶೀಲಿಸಿ   ಧ್ರಡೀಕರಿಸಿ  ಉಲ್ಲೇಖ (6)  ಈ ಕಛೇರಿಗೆ ಶಿಫಾರಸ್ಸು     ಮಾಡಿ ಸಲ್ಲಿಸಿದ್ದು    ,ಸದರಿ ಫಲಾನುಭವಿಗೆ  ರೂ .   " +apiResponse.getContent().get(0).getSanctionAmount()+ "  ಗಳ  ಸಹಾಯಧನವನ್ನು   \n" +
                    "                                     \n"  +
                    "ಮಂಜೂರು   ಮಾಡುವಂತೆ  ಕೋರಿರುತ್ತಾರೆ . ಮಂಜೂರಾತಿಗೆ   ಕೋರಲಾಗಿರುವ  ಸಹಾಯಧನ   ಮಂಜೂರು ಮಾಡಲು ಉಲ್ಲೇಖ (5)ರ  ಸರ್ಕಾರಿ ಆದೇಶದ ರೀತ್ಯಾ   ಈ ಕಛೆರಿಯ\n" +
                    "         \n" +
                    "ಅಧಿಕಾರ ಪ್ರತ್ಯಾಯೋಜನೆ  ವ್ಯಾಪ್ತಿಗೆ ಒಳಪಟ್ಟಿದ್ದು    ಅದರಂತೆ ಸಹಾಯಧನ ಮಂಜೂರಾತಿಗಾಗಿ   ಈ ಕೆಳಕಂಡ ಆದೇಶವನ್ನು   ಹೊರಡಿಸಿದೆ ." );
            response.setHeader11("");
            response.setHeader12("ಆದೇಶ ");
            response.setHeader13("ಸಂಖ್ಯೆ ");
            response.setHeader14("ದಿನಾಂಕ ");
            response.setHeader15("");
            response.setHeader16("             ಮೇಲಿನ  ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸಿರುವಂತೆ  ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು , ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್    " +apiResponse.getContent().get(0).getDistrictName() +"    ರವರು   ಶಿಫಾರಸ್ಸು      ಮಾಡಿರುವಂತೆ     "+apiResponse.getContent().get(0).getTscName()+ "\n" +
                    "                                             \n" +
                    "ತಾಂತ್ರಿಕ   ಸೇವಾ  ಕೇಂದ್ರ    ವ್ಯಾಪ್ತಿಯ    "+apiResponse.getContent().get(0).getVillageName()+  "    ಗ್ರಾಮದ   ಸಾಮಾನ್ಯ /ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ ಗ್ರಾಮದ ಸಾಮಾನ್ಯ ಎಸ್ಸಿಪಿ  ಟಿಎಸ್ಪಿ ವರ್ಗಕ್ಕೆ ಸೇರಿದ ಶ್ರೀ . /ಶ್ರೀಮತಿ . "+ apiResponse.getContent().get(0).getFarmerFirstName()+"\n" +
                    "                                              \n" +
                    " ಬಿನ್/ಕೋಂ    " +apiResponse.getContent().get(0).getFatherNameKan() + "   ರವರು ಕೇಂದ್ರ   ವಲಯ ಸಿಲ್ಕ್    ಸಮಗ್ರ    ಯೋಜನೆಯಡಿ     "+apiResponse.getContent().get(0).getSpacingName() + "     ಚ.ಅಡಿಯ  ರೇಷ್ಮೆ   ಹುಳು ಸಾಕಾಣಿಕೆ ಮನೆಗೆ   ಘಟಕ ದರದ ಶೇ . 75 / 90 ರಷ್ಟು    \n" +
                    "                                \n"+
                    "ಸಹಾಯಧನ ರೂ .  " +apiResponse.getContent().get(0).getSanctionAmount()  + "   ಲಕ್ಷ (   __________________________________________________________________________________________________  ರೂ ಗಳು  ಮಾತ್ರ ) ಗಳಿಗೆ   ಮಂಜೂರಾತಿ \n" +
                    "                                            \n" +
                    "ನೀಡಿದೆ . ಈ ಸಹಾಯಧನದ ಪೈಕಿ   ರೂ .    " +apiResponse.getContent().get(0).getSanctionAmount()  + "    ಲಕ್ಷ   (__________________________________________________________________________________________________ ರೂ . ಗಳು ಮಾತ್ರ )\n" +
                    "              \n" +
                    "ಕೇಂದ್ರದ  ಪಾಲಾಗಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ    ಮಂಡಳಿ ನೀಡಿರುವ ಮೊತ್ತದಲ್ಲಿ      ರೇಷ್ಮೆ    ನಿರ್ದೇಶನಾಲಯದಿಂದ  ಡಿಬಿಟಿ  ಮುಖಾಂತರ  ಫಲಾನುಭವಿಗಳ   ಬ್ಯಾಂಕ್  ಖಾತೆಗೆ  ನೇರವಾಗಿ \n" +
                    "                                                \n" +
                    "ಜಮಾ  ಮಾಡಲಾಗುವುದು  ಮತ್ತು     ರಾಜ್ಯದ   ಪಾಲಾಗಿ ರೂ .   " +apiResponse.getContent().get(0).getSanctionAmount()  + "   ಲಕ್ಷಗಳನ್ನು   ( __________________________________________________________________________________________________\n" +
                    "                   \n" +
                    "ರೂ  ಗಳು ಮಾತ್ರ ) ರಾಜ್ಯ    ರೇಷ್ಮೆ    ಅಭಿವೃದ್ಧಿ    ಯೋಜನೆ   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ    "+apiResponse.getContent().get(0).getScHeadAccountName() + "   ರಡಿ  ಖಜಾನೆ ________________________________2 ರಲ್ಲಿ     ಬಿಡುಗೋಡೆಗಳಿಸಿರುವ   \n" +
                    "                                             \n" +
                    "ಸಹಾಯಧನವನ್ನು    ಸಂಬಂಧಿಸಿದ    ರೇಷ್ಮೆ     ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು ಖಜಾನೆ ____________________________________________2  ಮೂಲಕ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಖಾತೆಗೆ ನೇರವಾಗಿ\n" +
                    "                             \n" +
                    "ಜಮಾ  ಮಾಡುವುದು . ಈ ವೆಚ್ಚವನ್ನು    ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ      "+apiResponse.getContent().get(0).getScHeadAccountName() + "  (ಸಾಮಾನ್ಯ / ಎಸ್ಸಿಪಿ /ಟಿಎಸ್ಪಿ ) ಅಡಿ  ಭರಿಸುವುದು ." );
            response.setHeader17("ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು ");
            response.setHeader18("ಪ್ರತಿಯನ್ನು \n" +
                    "                       \n" +
                    "ಶ್ರೀ /.ಶ್ರೀಮತಿ. "+ apiResponse.getContent().get(0).getFarmerFirstName() +"  ಬಿನ್/ಕೋಂ   " + apiResponse.getContent().get(0).getFatherNameKan()  + "\n" +
                    "                                      \n" +
                    "ಗ್ರಾಮ    " +  apiResponse.getContent().get(0).getVillageName()+  " ಜಿಲ್ಲೆ    " + apiResponse.getContent().get(0).getDistrictName());
            response.setHeader19("");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setLineItemComment("  1. ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು ,ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್ ,    " +apiResponse.getContent().get(0).getDistrictName() +"   ರವರಿಗೆ  ಎಲ್ಲ    ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ  ಮುಂದಿನ ಅಗತ್ಯ    ಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ .  \n"+
                    "                                       \n"+
                    " 2. ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು   " +apiResponse.getContent().get(0).getDistrictName() +"  ವಿಭಾಗರವರಿಗೆ  ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .  \n"+
                    "                                       \n"+
                    " 3. ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿ ,ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ      " +apiResponse.getContent().get(0).getTscName() +" ರವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ .");
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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
            sanctionOrderResponseList.add(response);
        }
//        countries.add(new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"));
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private String formatDate(Object dateObj, SimpleDateFormat sdf) {
        if (dateObj == null) return "";
        try {
            return sdf.format(dateObj);
        } catch (Exception e) {
            return dateObj.toString();
        }
    }

    /* ✅ Helper for clean rounding (removes .0, rounds .5 up) */
    private static String formatAmount(Float amount) {
        if (amount == null) return "0";
        long rounded = Math.round(amount);
        return String.valueOf(rounded);
    }

    public class KannadaNumberUtil {

        private static final String[] units = {
                "", "ಒಂದು", "ಎರಡು", "ಮೂರು", "ನಾಲ್ಕು  ", "ಐದು", "ಆರು", "ಏಳು", "ಎಂಟು", "ಒಂಬತ್ತು  ",
                "ಹತ್ತು  ", "ಹನ್ನೊಂದು", "ಹನ್ನೆ ರಡು", "ಹದಿಮೂರು", "ಹದಿನಾಲ್ಕು  ", "ಹದಿನೈದು",
                "ಹದಿನಾರು", "ಹದಿನೇಳು", "ಹದಿನೆಂಟು", "ಹತ್ತೊಂಬತ್ತು  "
        };

        // ✅ Exact Kannada words for 20–99
        private static final String[] twoDigits = {
                "", "ಒಂದು", "ಎರಡು", "ಮೂರು", "ನಾಲ್ಕು  ", "ಐದು", "ಆರು", "ಏಳು", "ಎಂಟು", "ಒಂಬತ್ತು  ",
                "ಹತ್ತು  ", "ಹನ್ನೊಂದು", "ಹನ್ನೆರಡು ", "ಹದಿಮೂರು", "ಹದಿನಾಲ್ಕು  ", "ಹದಿನೈ ದು", "ಹದಿನಾರು",
                "ಹದಿನೇಳು", "ಹದಿನೆಂಟು", "ಹತ್ತೊಂಬತ್ತು", "ಇಪ್ಪ ತ್ತು  ", "ಇಪ್ಪ ತ್ತೊಂದು", "ಇಪ್ಪ ತ್ತೆ ರಡು", "ಇಪ್ಪ ತ್ತ್ಮೂ ರು",
                "ಇಪ್ಪ ತ್ತ್ನಾ ಲ್ಕು  ", "ಇಪ್ಪ ತ್ತೈ ದು", "ಇಪ್ಪ ತ್ತಾ ರು", "ಇಪ್ಪ ತ್ತೇಳು", "ಇಪ್ಪ ತ್ತೆಂಟು", "ಇಪ್ಪ ತ್ತೊಂಬತ್ತು",
                "ಮೂವತ್ತು ", "ಮೂವತ್ತೊಂದು", "ಮೂವತ್ತೆ ರಡು", "ಮೂವತ್ತ್ಮೂ ರು", "ಮೂವತ್ತ್ನಾ ಲ್ಕು  ", "ಮೂವತ್ತೈ ದು",
                "ಮೂವತ್ತಾ ರು", "ಮೂವತ್ತ ೇಳು", "ಮೂವತ್ತೆ ಂಟು", "ಮೂವತ್ತೊಂಬತ್ತು  ", "ನಲವತ್ತು ", "ನಲವತ್ತೊಂದು",
                "ನಲವತ್ತೆ ರಡು", "ನಲವತ್ತ್ಮೂ ರು", "ನಲವತ್ತ್ನಾ ಲ್ಕು ", "ನಲವತ್ತೈ ದು", "ನಲವತ್ತಾ ರು", "ನಲವತ್ತೇಳು",
                "ನಲವತ್ತೆಂಟು", "ನಲವತ್ತೊಂಬತ್ತು ", "ಐವತ್ತು ", "ಐವತ್ತೊಂದು", "ಐವತ್ತೆ ರಡು", "ಎಪ್ಪ ತ್ತ  ಮೂರು",
                "ಐವತ್ತ್ನಾ ಲ್ಕು ", "ಐವತ್ತೈ ದು", "ಐವತ್ತಾ ರು", "ಐವತ್ತೇಳು", "ಐವತ್ತೆಂಟು", "ಐವತ್ತೊಂಬತ್ತು",
                "ಅರವತ್ತು ", "ಅರವತ್ತೊಂದು", "ಅರವತ್ತೆ ರಡು", "ಅರವತ್ತ್ಮೂ ರು", "ಅರವತ್ತ್ನಾ ಲ್ಕು ", "ಅರವತ್ತೈ ದು",
                "ಅರವತ್ತಾ ರು", "ಅರವತ್ತೇಳು", "ಅರವತ್ತೆಂಟು", "ಅರವತ್ತೊಂಬತ್ತು ", "ಎಪ್ಪ ತ್ತು ", "ಎಪ್ಪ ತ್ತೊಂದು",
                "ಎಪ್ಪ ತ್ತೆರಡು", "ಎಪ್ಪ ತ್ತ್ಮೂ ರು", "ಎಪ್ಪ ತ್ತ್ನಾ ಲ್ಕು  ", "ಎಪ್ಪ ತ್ತೈ ದು", "ಎಪ್ಪ ತ್ತಾ ರು", "ಎಪ್ಪ ತ್ತೇಳು",
                "ಎಪ್ಪ ತ್ತೆಂಟು", "ಎಪ್ಪ ತ್ತೊಂಬತ್ತು", "ಎಂಭತ್ತು ", "ಎಂಭತ್ತೊಂದು", "ಎಂಭತ್ತೆ ರಡು", "ಎಂಭತ್ತ್ಮೂ ರು",
                "ಎಂಭತ್ತ್ನಾ ಲ್ಕು ", "ಎಂಭತ್ತೈ ದು", "ಎಂಭತ್ತಾ ರು", "ಎಂಭತ್ತೇಳು", "ಎಂಭತ್ತೆಂಟು", "ಎಂಭತ್ತೊಂಬತ್ತು ",
                "ತೊಂಬತ್ತು", "ತೊಂಬತ್ತೊಂದು", "ತೊಂಬತ್ತೆರಡು", "ತೊಂಬತ್ತ್ಮೂರು", "ತೊಂಬತ್ತ್ನಾ ಲ್ಕು", "ತೊಂಬತ್ತೈದು",
                "ತೊಂಬತ್ತಾರು", "ತೊಂಬತ್ತೇಳು", "ತೊಂಬತ್ತೆಂಟು", "ತೊಂಬತ್ತೊಂಬತ್ತು"
        };

        public static String convertNumberToKannadaWords(long number) {
            if (number == 0) return "ಸೊನ್ನೆ ಮಾತ್ರ";
            if (number < 0) return "ಋಣ " + convertNumberToKannadaWords(-number);

            String words = convertCore(number).trim();
            return words + "  ಮಾತ್ರ";
        }

        private static String convertCore(long number) {
            StringBuilder words = new StringBuilder();

            if (number >= 10000000) { // Crore
                words.append(convertCore(number / 10000000)).append(" ಕೋಟಿ ");
                number %= 10000000;
            }
            if (number >= 100000) { // Lakh
                words.append(convertCore(number / 100000)).append(" ಲಕ್ಷ ದ ");
                number %= 100000;
            }
            if (number >= 1000) { // Thousand
                words.append(convertCore(number / 1000)).append(" ಸಾವಿರ ");
                number %= 1000;
            }
            if (number >= 100) { // Hundred
                words.append(convertCore(number / 100)).append(" ನೂರು ");
                number %= 100;
            }
            if (number > 0) {
                if (number < 100) {
                    words.append(twoDigits[(int) number]).append(" ");
                }
            }

            return words.toString().trim();
        }

        public static void main(String[] args) {
            System.out.println(convertNumberToKannadaWords(158437));
            System.out.println(convertNumberToKannadaWords(75000));
            System.out.println(convertNumberToKannadaWords(1000000));
            System.out.println(convertNumberToKannadaWords(250));
        }
    }





    private String trimWords(String input, int maxWords) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String[] words = input.trim().split("\\s+");
        if (words.length <= maxWords) {
            return input.trim();
        }

        return String.join(" ", Arrays.copyOfRange(words, 0, maxWords));
    }

//    private String getFirstTwoLetters(String input) {
//        if (input == null || input.isBlank())


    /* ✅ Helper method 2: Get first two visible Kannada letters */
    private String getFirstTwoLetters(String input) {
        if (input == null || input.isBlank()) return "";
        return input.codePoints()
                .limit(2) // takes 2 Unicode characters (safe for Kannada)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

            private JRDataSource getDataSourceForSanctionOrderRH(SanctionOrderPrintRequest requestDto) throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSanction(requestDto);

        // ✅ Change: Null check for API response to avoid NullPointerException
        if (apiResponse == null || apiResponse.getContent() == null || apiResponse.getContent().isEmpty()) {
            logger.warn("No data returned from sanction API for applicationFormId: {}", requestDto.getApplicationFormId());
            return new JREmptyDataSource(); // Safe fallback for empty PDF
        }
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();

        SanctionOrderResponse response = new SanctionOrderResponse();

                // ✅ Date formatter
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                // ✅ Format date fields safely
                String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
                String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
                String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
                String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
                String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
                String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

                // ✅ FIXED: Define actualAmount before using it
                Float actualAmount = Float.valueOf(formatAmount(
                        apiResponse.getContent().get(0).getActualAmount() == null
                                ? 0f
                                : apiResponse.getContent().get(0).getActualAmount()
                ));

                long actualAmounts = Math.round(
                        apiResponse.getContent().get(0).getActualAmount() == null
                                ? 0f
                                : apiResponse.getContent().get(0).getActualAmount()
                );

                String shortDistrictKannadas = getKannadaShortForm(apiResponse.getContent().get(0).getDistrictName());



                String shortDistrictKannada = getKannadaShortForm(apiResponse.getContent().get(0).getLoggedinUserDistrictName());

                // ✅ Clean formatted date for sanction order
                String formattedDate = "";
                try {
                    String inputDate = apiResponse.getContent().get(0).getDate().toString(); // e.g. "2025-10-29 14:35:22.123"

                    // Parse input format
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                    // Define output format
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    // Convert and format
                    Date date = inputFormat.parse(inputDate);
                    formattedDate = outputFormat.format(date);

                } catch (Exception e) {
                    formattedDate = apiResponse.getContent().get(0).getDate().toString(); // fallback if parsing fails
                }

                String surveyNumber = Util.objectToString(apiResponse.getContent().get(0).getSurveyNumber());
                String kaneshNo = Util.objectToString(apiResponse.getContent().get(0).getKaneshNo());

                String surveyText = "";
                if (!surveyNumber.isEmpty()) {
                    surveyText = "ಸರ್ವೆ ನಂ. " + surveyNumber;
                } else if (!kaneshNo.isEmpty()) {
                    surveyText = "ಖಾತೆ ನಂ. " + kaneshNo;
                }

                int centralShareAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getCentralSanctionAmount())));
                int stateShareAmount = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getStateSanctionAmount())));
                int centralSharePercentage = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getCentralSharePercentage())));
                int stateSharePercentage = Math.round(Float.parseFloat(formatAmount(apiResponse.getContent().get(0).getStateSharePercentage())));

                int beneficiarySharePercentage = 100 - (centralSharePercentage + stateSharePercentage);
                if (beneficiarySharePercentage < 0) beneficiarySharePercentage = 0;

                int totalAmount = centralShareAmount + stateShareAmount;
                int beneficiaryShareAmount = Math.round((totalAmount * beneficiarySharePercentage) / 100f);

                String centralShareWords = KannadaNumberUtil.convertNumberToKannadaWords(centralShareAmount);
                String stateShareWords = KannadaNumberUtil.convertNumberToKannadaWords(stateShareAmount);
                String totalSubsidyWords = KannadaNumberUtil.convertNumberToKannadaWords(totalAmount);

                String shareDisplay = centralSharePercentage + ":" + stateSharePercentage + ":" + beneficiarySharePercentage;
                String totalAmountDisplay = String.valueOf(totalAmount);


                if (apiResponse == null || apiResponse.getContent() == null || apiResponse.getContent().isEmpty()) {
            throw new RuntimeException("No data returned from sanction API for applicationFormId: " + requestDto.getApplicationFormId());
        }
            response.setHeader1("ರೇಷ್ಮೆ    ಜಂಟಿ  ನಿರ್ದೇಶಕರು,  "+ apiResponse.getContent().get(0).getDivisionName() +"   ವಿಭಾಗ,  "+ apiResponse.getContent().get(0).getDivisionName() +" ರವರ  ಕಛೇರಿ  ನಡವಳಿಗಳು");
            response.setHeader4("ವಿಷಯ  : ");
            response.setHeader20( "                " + apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ ಸಾಲಿನಲ್ಲಿ      ಇಲಾಖೆಯು    ಕೇಂದ್ರ      ರೇಷ್ಮೆ     ಮಂಡಳಿಯ    ಸಹಯೋಗದೊಂದಿಗೆ    ಅನುಷ್ಟಾ  ನಗೊಳಿಸುತ್ತಿ ರುವ   ಕೇಂದ್ರ   " +
                                  "ವಲಯ   “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”   ಯೋಜನೆಯಡಿ    " +apiResponse.getContent().get(0).getScComponentName() +
                                  "       " +apiResponse.getContent().get(0).getScCategoryName() + "   ಅಡಿ    ಸಹಾಯಧನ    ಮಂಜೂರಾತಿ    ನೀಡುವ    ಕುರಿತು.");
            response.setHeader5( "ಉಲ್ಲೇಖ : ");
            response.setHeader2("1. ಸರ್ಕಾರದ   ಆದೇಶ  ಸಂಖ್ಯೆ  : " +apiResponse.getContent().get(0).getAdmGovtOrder() + "  ದಿನಾಂಕ :  " +admGovtDate  + "\n" +
                    "2. ರೇಷ್ಮೆ    ಕೃ ಷಿ  ಅಭಿವೃ ದ್ದಿ    ಆಯುಕ್ತ ರು   ಹಾಗೂ  ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು, ಬೆಂಗಳೂರು  ರವರ  ಸುತ್ತೋ ಲೆ  \n" +
                    "   ಸಂಖ್ಯೆ  :  " +apiResponse.getContent().get(0).getSchemeCircularNo() + " ದಿನಾಂಕ :  " +schemeCircularDate  + " \n" +
                    "3. ಸರ್ಕಾರದ  ಆದೇಶ ಸಂಖ್ಯೆ  : " +apiResponse.getContent().get(0).getDeptDeleNo() + "    ದಿನಾಂಕ : " +deptDeleDate  + " \n" +
                    "4. ರೇಷ್ಮೆ  ಕೃ ಷಿ  ಅಭಿವೃ ದ್ದಿ    ಆಯುಕ್ತ  ರು  ಹಾಗೂ  ರೇಷ್ಮೆ    ನಿರ್ದೇಶಕರು, ಬೆಂಗಳೂರು  ರವರ  ಜ್ಞಾ  ಪನ  ಪತ್ರ  ದ\n" +
                    "   ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getReleaseNo() + "   ದಿನಾಂಕ : " +releaseDate + " \n" +
                    "5. ರೇಷ್ಮೆ    ಉಪ  ನಿರ್ದೇಶಕರು,  ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,    "+ apiResponse.getContent().get(0).getLoggedinUserDistrictName() +"  ರವರ   ಪ್ರ  ಸ್ತಾ ವನೆ   ದಿನಾಂಕ :  "+  proposalDate);
            response.setHeader24("ಪೀಠಿಕೆ : ");
                response.setHeader8(
                        "    " + apiResponse.getContent().get(0).getFinancialYear() + "    ನೇ  ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ     ಇಲಾಖೆಯ   ವಿವಿಧ   ಕಾರ್ಯಕ್ರ ಮಗಳ   ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ   ವಿವಿಧ   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆಗಳಡಿ   ಉಲ್ಲೇ ಖ(1)ರಲ್ಲಿ     ಸರ್ಕಾರವು    " +
                                "    ಆಡಳಿತಾತ್ಮ ಕ  ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು  , ಉಲ್ಲೇಖ (2)  ರಲ್ಲಿ    ರೇಷ್ಮೆ  ಹುಳು  ಸಾಕಾಣಿಕೆ   ಮನೆ   ನಿರ್ಮಾಣ  ಕಾರ್ಯಕ್ರ  ಮದ  ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ  ಮಾರ್ಗಸೂಚಿಯನ್ನು    " +
                                "    ನೀಡಲಾಗಿದೆ.  ಇಲಾಖೆಯು  ಕೇಂದ್ರ  ರೇಷ್ಮೆ  ಮಂಡಳಿಯ  ಸಹಯೋಗದೊಂದಿಗೆ   ಕೇಂದ್ರ  ವಲಯ  “" + apiResponse.getContent().get(0).getSchemeNameInKannada() + "”  ಯೋಜನೆಯನ್ನು    ಅನುಷ್ಟಾ  ನಗೊಳಲಾಗುತ್ತಿ ದೆ.    " +
                                "    ಸದರಿ  ಯೋಜನೆಯಡಿ  ರೇಷ್ಮೆ   ಬೆಳೆಗಾರರು   ನಿರ್ಮಾಣ   ಮಾಡಿರುವ   " + apiResponse.getContent().get(0).getScComponentName() + "  ನೀಡಬೇಕಾಗಿದ್ದು ,    " +
                                "    " + apiResponse.getContent().get(0).getScCategoryName() + " ಅಡಿ  ಕೇಂದ್ರ :ರಾಜ್ಯ  :ಫಲಾನುಭವಿ  ಪಾಲು " + centralSharePercentage + ":"  + stateSharePercentage + ":" + beneficiarySharePercentage + " ಆಗಿರುತ್ತ ದೆ.    " +
                                "    " + apiResponse.getContent().get(0).getScComponentName() + " ಘಟಕ  ದರ  ರೂ.  " + actualAmounts + "/- ಗಳಿಗೆ   ನಿಗಧಿಪಡಿಸಿದ್ದು,  ಇದರಲ್ಲಿ   ಶೇಕಡ  " + (centralSharePercentage + stateSharePercentage) + "  ರಷ್ಟ ನ್ನು   ಅಂದರೆ  ರೂ.   " + (centralShareAmount + stateShareAmount) + "/-  ಗಳನ್ನು   ಸಹಾಯಧನವಾಗಿ  ನೀಡಲಾಗುತ್ತಿ  ದೆ.    " +
                                "    ಇದರಲ್ಲಿ   ಕೇಂದ್ರ ಪಾಲು  ಘಟಕ  ದರದ   ಶೇ." + centralSharePercentage + "  ಅಂದರೆ   ರೂ.   " + centralShareAmount + "/-  ಗಳು  ಮತ್ತು    ರಾಜ್ಯ  ದ   ಪಾಲು   ಘಟಕ  ದರದ  ಶೇ." + stateSharePercentage + "  ಅಂದರೆ   ರೂ.   " + stateShareAmount  + "/-   ಗಳು   ಆಗಿರುತ್ತ  ದೆ.    " +
                                "    ಕೇಂದ್ರ   ರೇಷ್ಮೆ ಮಂಡಳಿಯು   ಕೇಂದ್ರ ದ   ಪಾಲಿನ    ಅನುದಾನವನ್ನು     PFMS   ಮುಖಾಂತರ   ಒದಗಿಸಿದ್ದು     SBI, ಬ್ಯಾಂಕ್  ಬಹುಮಹಡಿ   ಕಟ್ಟ ಡ   ಶಾಖೆಯ  ಬ್ಯಾಂಕ್  ಖಾತೆಯಲ್ಲಿ    " +
                                "    ಜಮೆಯಾಗಿರುತ್ತ ದೆ.   ಆದ್ದ ರಿಂದ  ಕೇಂದ್ರ ದ  ಪಾಲಿನ  ಸಹಾಯಧನ   ರೂ.  " + centralShareAmount + "/-  ಗಳನ್ನು   (" + centralSharePercentage + "%)  ಕೇಂದ್ರ   ರೇಷ್ಮೆ   ಮಂಡಳಿ  ಭರಿಸುವುದರಿಂದ    ಇದನ್ನು    ಆಯಾ   ಜಿಲ್ಲೆ ಗಳ    " +
                                "    ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್  ರೇಷ್ಮೆ    ಉಪ  ನಿರ್ದೇಶಕರುಗಳ   ಕಛೇರಿಯಿಂದ   ಡಿಬಿಟಿ   ಮುಖಾಂತರ    ಫಲಾನುಭವಿ   ಬ್ಯಾಂಕ್  ಖಾತೆಗೆ   ನೇರವಾಗಿ   ಜಮಾ   ಮಾಡಲಾಗುತ್ತ ದೆ.    " +
                                "    ರಾಜ್ಯ  ದ   ಪಾಲಿನ   ಸಹಾಯಧನವನ್ನು    ರೇಷ್ಮೆ   ಅಭಿವೃ ದ್ದಿ    ಯೋಜನೆ   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ :  "
                                + apiResponse.getContent().get(0).getScHeadAccountName() + "  (" + apiResponse.getContent().get(0).getDescription() + ")   ರಡಿ   ರೇಷ್ಮೆ    ಅಭಿವೃ ದ್ದಿ    ಆಯುಕ್ತ ರು   ಹಾಗೂ   ರೇಷ್ಮೆ    " +
                                "    ನಿರ್ದೇಶಕರು,  ಬೆಂಗಳೂರು   ರವರು   ಖಜಾನೆ-2   ಮುಖಾಂತರ   ಬಿಡುಗಡೆಗೊಳಿಸಿ   ರಾಜ್ಯ  ದ   ಪಾಲಿನ   ಸಹಾಯಧನವನ್ನು    ರೇಷ್ಮೆ  ಸಹಾಯಕ  ನಿರ್ದೇಶಕರ  ಕಚೇರಿಯಿಂದ    " +
                                "    ಡಿಬಿಟಿ   ಮುಖಾಂತರ   ಫಲಾನುಭವಿಯ  ಬ್ಯಾಂಕ್   ಖಾತೆಗೆ   ನೇರವಾಗಿ   ಜಮಾ   ಮಾಡಲಾಗುವುದು.");

                response.setHeader10(
                        "              " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "    ಜಿಲ್ಲೆ ಯ    " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ತಾಲ್ಲೂ  ಕಿನ  "
                                + apiResponse.getContent().get(0).getLoggedinUserTscName() + "    ತಾಂತ್ರಿ ಕ    ಸೇವಾ    ಕೇಂದ್ರ  ದ    ವ್ಯಾ ಪ್ತಿ ಯ    "
                                + apiResponse.getContent().get(0).getVillageName() + "  ಗ್ರಾ  ಮದ  " + apiResponse.getContent().get(0).getCategoryShortName() + "   ವರ್ಗಕ್ಕೆ   ಸೇರಿದ   ಶ್ರೀ  /ಶ್ರೀ  ಮತಿ    "
                                + apiResponse.getContent().get(0).getFarmerFirstName() + " (" + apiResponse.getContent().get(0).getFruitsId() + ")  ಬಿನ್ /ಕೋಂ  "
                                + apiResponse.getContent().get(0).getFatherNameKan() + "    ಇವರು    " + apiResponse.getContent().get(0).getLandVillage() + "  ಗ್ರಾ ಮದ    ಸರ್ವೆನಂ    "
                                + apiResponse.getContent().get(0).getSurveyNumber() + "    ರಲ್ಲಿ    " + apiResponse.getContent().get(0).getExtentOfMulberry() + "    ಎಕರೆ    ವಿಸ್ತೀ ರ್ಣದಲ್ಲಿ    ಹಿಪ್ಪು    ನೇರಳೆ    ತೋಟ    ಹೊಂದಿದ್ದು    ,    "
                                + apiResponse.getContent().get(0).getLandVillage() + "    ಗ್ರಾ ಮದ    " + surveyText + "    ರಲ್ಲಿ    " + apiResponse.getContent().get(0).getRhSqft() + "   ಚದರಅಡಿ    ವಿಸ್ತೀ ರ್ಣದ    "
                                + apiResponse.getContent().get(0).getRoofTypeNameInKannada() + "    ಮೇಲ್ಚಾ  ವಣಿಯ    ಪ್ರ ತ್ಯೇಕ    ರೇಷ್ಮೆ    ಹುಳು    ಸಾಕಾಣಿಕೆ    ಮನೆಯನ್ನು    ಅಂದಾಜು    ರೂ.    "
                                + apiResponse.getContent().get(0).getEstimatedCost() + "    ಲಕ್ಷ ಗಳ    ವೆಚ್ಚದಲ್ಲಿ    (ಸ್ವಂತ ವೆಚ್ಚ  /ಬ್ಯಾಂಕಿನಿಂದ  ಸಾಲ  ಪಡೆದು)    ನಿರ್ಮಿಸಿರುವುದನ್ನು    ರೇಷ್ಮೆ    ವಿಸ್ತ  ರಣಾಧಿಕಾರಿಗಳು    ತಾಂತ್ರಿ  ಕ    ಸೇವಾ    ಕೇಂದ್ರದ  "
                                + apiResponse.getContent().get(0).getLoggedinUserTscName() + "    ಹಾಗೂ    ರೇಷ್ಮೆ    ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು    "
                                + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "    ವಿಭಾಗ    ರವರು    ಪರಿಶೀಲಿಸಿ    ದೃ  ಢೀಕರಿಸಿ    ಸಲ್ಲಿ ಸಿದ    ಎಲ್ಲಾ    ಅಗತ್ಯ    ದಾಖಲಾತಿಗಳನ್ನು    ಒಳಗೊಂಡ    ಪ್ರ ಸ್ತಾ ವನೆಯನ್ನು    ರೇಷ್ಮೆ    "+
                                "ಉಪನಿರ್ದೇಶಕರು,  ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್ ,    " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "    ಪರಿಶೀಲಿಸಿ    ದೃಢಿಕರಿಸಿ    ಉಲ್ಲೇಖ(5) ರನ್ವ ಯ    ಈ    ಕಛೇರಿಗೆ    ಶಿಫಾರಸ್ಸು     ಮಾಡಿ    ಸಲ್ಲಿಸಿದ್ದು  "+
                                ",  ಸದರಿ    ಫಲಾನುಭವಿಗೆ    ರೂ.    " + (centralShareAmount + stateShareAmount) + " /-ಗಳ    ಸಹಾಯಧನವನ್ನು    ಮಂಜೂರು    ಮಾಡುವಂತೆ    ಕೋರಿರುತ್ತಾರೆ.    "+
                                "ಮಂಜೂರಾತಿಗೆ    ಕೋರಲಾಗಿರುವ    ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡಲು    ಉಲ್ಲೇಖ(3)ರ    ಸರ್ಕಾರದ    ಆದೇಶದ    ರೀತ್ಯಾ    ಈ    ಕಛೇರಿಯ    ಅಧಿಕಾರ    ಪ್ರ ತ್ಯಾ ಯೋಜನೆ    "+
                                "ವ್ಯಾಪ್ತಿ ಯಲ್ಲಿ ದ್ದು ,    ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ    ಸದರಿ    ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಠಾ ನಕ್ಕಾಗಿ    ನೀಡಿರುವ    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ    ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡಲು    ಅನುದಾನ    ಬಿಡುಗಡೆ    ಮಾಡಲಾಗಿದೆ."+
                                "      ಅದರಂತೆ    ಅಂತಿಮ    ಹಂತದ/ಮೂರು    ಹಂತದ    ಜಿ.ಪಿ.ಎಸ್    ಪೋಟೊಗಳನ್ನು    ಸಲ್ಲಿ  ಸಿರುವುದರಿಂದ    ಸಹಾಯಧನ    ಮಂಜೂರು    ಮಾಡಬಹುದಾಗಿದ್ದು  ,   ಈ    ಕೆಳಕಂಡ    ಆದೇಶವನ್ನು    ಹೊರಡಿಸಿದೆ.");
                    response.setHeader11("");
            response.setHeader12("ಆದೇಶ ");
            response.setHeader13("ಸಂಖ್ಯೆ  : ರೇಜಂನಿ/ " +shortDistrictKannada  +  " ವಿ /ತಾಂ/ “"+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"”/" +apiResponse.getContent().get(0).getLoggedinUserDistrictName() + " /ರೇಹುಸಾಮ/ಸಧನ/ಸಾ/ಮಂ/" +apiResponse.getContent().get(0).getScCategoryName() + " / "+apiResponse.getContent().get(0).getSanctionOrderNumber() + " / ದಿನಾಂಕ:  "+ formattedDate + "\n");
            response.setHeader14("ದಿನಾಂಕ ");
            response.setHeader15("");
                response.setHeader16(
                        "            ಮೇಲಿನ    ಪೀಠಿಕೆಯಲ್ಲಿ    ವಿವರಿಸುವಂತೆ    ರೇಷ್ಮೆ    ಉಪನಿರ್ದೇಶಕರು,    ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್,    "
                                + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "    ರವರು    ಶಿಫಾರಸ್ಸು    ಮಾಡಿರುವಂತೆ    "
                                + apiResponse.getContent().get(0).getLoggedinUserTscName() + "    ತಾಂತ್ರಿ  ಕ    ಸೇವಾ    ಕೇಂದ್ರ    ದ    ವ್ಯಾಪ್ತಿಯ    "
                                + apiResponse.getContent().get(0).getVillageName() + "    ಗ್ರಾ  ಮದ    "
                                + apiResponse.getContent().get(0).getCategoryShortName() + "    ವರ್ಗಕ್ಕೆ    ಸೇರಿದ    ಶ್ರೀ  /ಶ್ರೀ  ಮತಿ    "
                                + apiResponse.getContent().get(0).getFarmerFirstName() + "    (" + apiResponse.getContent().get(0).getFruitsId() + ")    ಬಿನ್    /ಕೋಂ.    "
                                + apiResponse.getContent().get(0).getFatherNameKan() + "    ರವರು    ಕೇಂದ್ರ    ವಲಯ    “"
                                + apiResponse.getContent().get(0).getSchemeNameInKannada() + "”    ಯೋಜನೆಯಡಿ    "
                                + apiResponse.getContent().get(0).getRhSqft() + "    ಚದರಡಿ    ರೇಷ್ಮೆ    ಹುಳು    ಸಾಕಾಣಿಕೆ    ಮನೆಗೆ    ಘಟಕ    ದರದ    ಶೇಕಡ    "
                                + (centralSharePercentage + stateSharePercentage) + "    ರಷ್ಟು    ಸಹಾಯಧನ    ರೂ.    "
                                + (centralShareAmount + stateShareAmount) + "/-    (ರೂ.    " + totalSubsidyWords
                                + ")    ಗಳಿಗೆ    ಮುಚ್ಚ  ಳಿಕೆಯಲ್ಲಿ  ನ    ಷರತ್ತು    ಮತ್ತು    ತಗಾದೆಗಳಿಗೆ    ಸಂಬಂಧಧಿಸಿದ    ಫಲಾನುಭವಿ    ಹಾಗೂ    ಶಿಫಾರಸ್ಸು    ಮಾಡಿದ    ಕ್ಷೇತ್ರ    ಮಟ್ಟ    ದ    ಅಧಿಕಾರಿಗಳನ್ನು    ಜವಾಬ್ದಾ ರಿ    ಮಾಡಿ    ಮಂಜೂರಾತಿ    ನೀಡಿದೆ.    "
                                + "ಈ    ಸಹಾಯದನದ    ಪೈಕಿ    ರೂ.    " + centralShareAmount + "/-    (ರೂ.    "
                                + centralShareWords + ")    ಗಳು    ಕೇಂದ್ರ    ದ    ಪಾಲಾಗಿ    ಕೇಂದ್ರ    ರೇಷ್ಮೆ    ಮಂಡಳಿ    ನೀಡಿರುವ    ಮೊತ್ತ    ದಲ್ಲಿ    "
                                + "ಮತ್ತು    ರಾಜ್ಯ    ದ    ಪಾಲಾಗಿ    ರೂ.    " + stateShareAmount + "/-    (ರೂ.    "
                                + stateShareWords + " )    ಗಳನ್ನು    ರಾಜ್ಯ    "
                                + apiResponse.getContent().get(0).getSchemeNameInKannada()
                                + "    ಯೋಜನೆಯ    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ    "
                                + apiResponse.getContent().get(0).getScHeadAccountName() + " ("
                                + apiResponse.getContent().get(0).getDescription() + " )    ರಡಿ    ಖಜಾನೆ - 2    ರಲ್ಲಿ    ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ    ಸಹಾಯಧನದ    ಅನದಾನದಲ್ಲಿ,    ಸಂಬಂಧಿಸಿದ    ರೇಷ್ಮೆ    ಸಹಾಯಕ    ನಿರ್ದೇಶಕರುಗಳು    ಖಜಾನೆ-2    ರಲ್ಲಿ    "
                                + "ಹಾಗು    ಕೇಂದ್ರ    ದ    ಪಾಲಿನ    ಮೊತ್ತವನ್ನು    ಸಂಬಂಧಿಸಿದ    ಜಿಲ್ಲಾ    ಪಂಚಾಯತ್    ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರುಗಳು    ಡಿಬಿಟಿ    ಮೂಲಕ    ಫಲಾನುಭವಿ    ಬ್ಯಾಂಕ್‌    ಖಾತೆಗೆ    ನೇರವಾಗಿ    ಜಮಾ    ಮಾಡಲು    ಸೂಚಿಸಿದೆ.\n"
                                + "                                        ಈ    ವೆಚ್ಚ    ವನ್ನು    ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ    " + apiResponse.getContent().get(0).getScHeadAccountName() + " (" + apiResponse.getContent().get(0).getDescription() + ")  ("
                                + apiResponse.getContent().get(0).getScCategoryName() + " )    ಅಡಿ    ಭರಿಸುವುದು.");

                response.setHeader17("ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು\n" +
                    apiResponse.getContent().get(0).getDivisionName() + "   ವಿಭಾಗ\n");
            response.setHeader18("ಇವರಿಗೆ \n" +
                    "ಶ್ರೀ/ಶ್ರೀಮತಿ    " +apiResponse.getContent().get(0).getFarmerFirstName() + "   ಬಿನ್/ಕೋಂ.   " +apiResponse.getContent().get(0).getFatherNameKan() + " \n" +
                    apiResponse.getContent().get(0).getVillageName() + "  ಗ್ರಾಮ    " +apiResponse.getContent().get(0).getTalukName() + "   ತಾಲ್ಲೂಕು \n" +
                    "ಪ್ರತಿಗಳು: \n " +
                    "1. ರೇಷ್ಮೆ   ಸಹಾಯಕ  ನಿರ್ದೇಶಕರು,  " +apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ವಿಭಾಗ  ಇವರಿಗೆ  ಎಲ್ಲಾ   ಮೂಲ   ದಾಖಲಾತಿಗಳೊಂದಿಗೆ   ಮುಂದಿನ   ಅಗತ್ಯಕ್ರಮಕ್ಕಾಗಿ   ಕಳುಹಿಸಿದೆ. \n" +
                    "2. ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿ,  ತಾಂತ್ರಿಕ   ಸೇವಾ  ಕೇಂದ್ರ  , " +apiResponse.getContent().get(0).getLoggedinUserTscName() + "     ಇವರಿಗೆ   ಮಾಹಿತಿಗಾಗಿ  ಕಳುಹಿಸಿದೆ.\n" +
                    "3. ರೇಷ್ಮೆ   ಉಪನಿರ್ದೇಶಕರು,  ಜಿಲ್ಲಾ   ಪಂಚಾಯತ್,    " +apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "   ಇವರ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ.");
            response.setHeader19("");
            response.setDate(apiResponse.getContent().get(0).getDate());
            response.setFarmerFirstName(  " ಶ್ರೀ /.ಶ್ರೀಮತಿ.  "+ apiResponse.getContent().get(0).getFarmerFirstName() );
            response.setFarmerNumber(apiResponse.getContent().get(0).getFarmerNumber());
            response.setFarmerAddressText(apiResponse.getContent().get(0).getFarmerAddressText());
            response.setDistrictName(apiResponse.getContent().get(0).getDistrictName() + "ಜಿಲ್ಲೆ, ");
            response.setTalukName(apiResponse.getContent().get(0).getTalukName() + " ತಾಲ್ಲೂಕು , ");
            response.setHobliName(apiResponse.getContent().get(0).getHobliName() + " ಹೋಬಳಿ , ");
            response.setVillageName(apiResponse.getContent().get(0).getVillageName()+ " ಹಳಿಯ ನಿವಾಸಿಯಾದ ");
            response.setLineItemComment("1. ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು, ದೇವನಹಳ್ಳಿ ವಿಭಾಗ ಇವರಿಗೆ ಎಲ್ಲಾ ಮೂಲ ದಾಖಲಾತಿಗಳೊಂದಿಗೆ ಮುಂದಿನ ಅಗತ್ಯಕ್ರಮಕ್ಕಾಗಿ ಕಳುಹಿಸಿದೆ. \n" +
                    "      \n"+
                    "2. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ, ತಾಂತ್ರಿಕ ಸೇವಾಕೇಂದ್ರ, ದೇವನಹಳ್ಳಿ ಇವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ.\n" +
                    "      \n"+
                    "3. ರೇಷ್ಮೆಉಪನಿರ್ದೇಶಕರು, ಜಿಲ್ಲಾ ಪಂಚಾಯತ್, ಬೆಂಗಳೂರು ಗ್ರಾಮಾಂತರ ಇವರ ಮಾಹಿತಿಗಾಗಿ ಕಳುಹಿಸಿದೆ.\n");
            response.setCost(apiResponse.getContent().get(0).getCost());
            response.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getFruitsId());
            response.setVendorName(apiResponse.getContent().get(0).getVendorName());
            response.setVendorAccountNumber("ಖಾತೆ ಸಂಖ್ಯೆ :  " +apiResponse.getContent().get(0).getVendorAccountNumber());
            response.setVendorBankIfsc(", ಐ.ಎಫ್.ಎಸ್.ಸೀ (IFSC) ಸಂಖ್ಯೆ   :  " +apiResponse.getContent().get(0).getVendorBankIfsc() + " ಗೆ ಪಾವತಿಸಲು, ಪಾವತಿಸಿರುವ ಬಗ್ಗೆ ವಿವರಗಳನ್ನು (ಬ್ಯಾಂಕ್ ಚಲ್ಲನ್ ಸಂಖ್ಯೆ/ಅರ್.ತೀ.ಜೀ.ಎಸ್ ಸಂಖ್ಯೆ) ಸಹಾಯಕ ರೇಷ್ಮೆ  ನಿರ್ದೇಶಕರ ಕಚೇರಿ,  ");
            response.setVendorBranchName(apiResponse.getContent().get(0).getVendorBankName());
            response.setVendorUpi(apiResponse.getContent().get(0).getVendorUpi());
            response.setSanctionNo(apiResponse.getContent().get(0).getSanctionNo());
            response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
            response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
            response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
            response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
        sanctionOrderResponseList.add(response);

        // ✅ Change: Always return a JRBeanCollectionDataSource
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private String withFourSpaces(String text) {
        if (text == null) return null;
        // Replace every single space with four spaces
        return text.replace(" ", "    ");
    }

    private JRDataSource getDataSourceForSanctionOrderRHEquipment(SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromSanctionEquipment(requestDto);

        if (apiResponse == null || apiResponse.getContent() == null || apiResponse.getContent().isEmpty()) {
            logger.warn("No data returned from sanction RHEquipment API for applicationFormId: {}",
                    requestDto.getApplicationFormId());
            return new JREmptyDataSource();
        }



        List<SanctionOrderResponse> list = new ArrayList<>();
        SanctionOrderResponse dto = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String admGovtDate      = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate     = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate      = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String proposalDate     = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

        // Amounts & shares
        int centralShareAmount = Math.round(
                apiResponse.getContent().get(0).getCentralSanctionAmount() == null
                        ? 0f
                        : apiResponse.getContent().get(0).getCentralSanctionAmount()
        );
        int stateShareAmount = Math.round(
                apiResponse.getContent().get(0).getStateSanctionAmount() == null
                        ? 0f
                        : apiResponse.getContent().get(0).getStateSanctionAmount()
        );

        int centralSharePercentage = Math.round(
                apiResponse.getContent().get(0).getCentralSharePercentage() == null
                        ? 0f
                        : apiResponse.getContent().get(0).getCentralSharePercentage()
        );
        int stateSharePercentage = Math.round(
                apiResponse.getContent().get(0).getStateSharePercentage() == null
                        ? 0f
                        : apiResponse.getContent().get(0).getStateSharePercentage()
        );

        int beneficiarySharePercentage = 100 - (centralSharePercentage + stateSharePercentage);
        if (beneficiarySharePercentage < 0) beneficiarySharePercentage = 0;

        int totalSubsidyAmount = centralShareAmount + stateShareAmount;

        int beneficiaryShareAmount = Math.round(
                apiResponse.getContent().get(0).getBeneficiaryShareAmount() == null
                        ? 0f
                        : apiResponse.getContent().get(0).getBeneficiaryShareAmount()
        );

        String centralShareWords   = KannadaNumberUtil.convertNumberToKannadaWords(centralShareAmount);
        String stateShareWords     = KannadaNumberUtil.convertNumberToKannadaWords(stateShareAmount);
        String totalSubsidyWords   = KannadaNumberUtil.convertNumberToKannadaWords(totalSubsidyAmount);
        String beneficiaryShareWords =
                KannadaNumberUtil.convertNumberToKannadaWords(beneficiaryShareAmount);

        String shortDistrictKannada = getKannadaShortForm(
                apiResponse.getContent().get(0).getLoggedinUserDistrictName()
        );

        // Date for sanction order number
        String formattedDate;
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString();
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
            Date d = in.parse(inputDate);
            formattedDate = out.format(d);
        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString();
        }

        String surveyNumber = Util.objectToString(apiResponse.getContent().get(0).getSurveyNumber());
        String kaneshNo = Util.objectToString(apiResponse.getContent().get(0).getKaneshNo());
        String surveyText = "";
        if (!surveyNumber.isEmpty()) {
            surveyText = "ಸರ್ವೆ ನಂ. " + surveyNumber;
        } else if (!kaneshNo.isEmpty()) {
            surveyText = "ಖಾತೆ ನಂ. " + kaneshNo;
        }

        String financialYear       = Util.objectToString(apiResponse.getContent().get(0).getFinancialYear());
        String schemeNameKannada   = Util.objectToString(apiResponse.getContent().get(0).getSchemeNameInKannada());
        String schemeCategoryName  = Util.objectToString(apiResponse.getContent().get(0).getScCategoryName());
        String componentName       = Util.objectToString(apiResponse.getContent().get(0).getScComponentName());
        String headAccountName     = Util.objectToString(apiResponse.getContent().get(0).getScHeadAccountName());
        String headDescription     = Util.objectToString(apiResponse.getContent().get(0).getDescription());
        String divisionName        = Util.objectToString(apiResponse.getContent().get(0).getDivisionName());
        String districtName        = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserDistrictName());
        String talukName           = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserTalukName());
        String tscName             = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserTscName());
        String farmerVillage       = Util.objectToString(apiResponse.getContent().get(0).getVillageName());
        String categoryShortName   = Util.objectToString(apiResponse.getContent().get(0).getCategoryShortName());
        String farmerName          = Util.objectToString(apiResponse.getContent().get(0).getFarmerFirstName());
        String fatherNameKan       = Util.objectToString(apiResponse.getContent().get(0).getFatherNameKan());
        String fruitsId            = Util.objectToString(apiResponse.getContent().get(0).getFruitsId());
        String extentOfMulberry    = Util.objectToString(apiResponse.getContent().get(0).getExtentOfMulberry());
        String landVillage         = Util.objectToString(apiResponse.getContent().get(0).getLandVillage());
        String equipmentName       = Util.objectToString(apiResponse.getContent().get(0).getEquipmentName());
        String taxInvoiceNo        = Util.objectToString(apiResponse.getContent().get(0).getTaxInvoiceNo());
        String taxInvoiceDate      = Util.objectToString(apiResponse.getContent().get(0).getTaxInvoiceDate());
        String vendorName          = Util.objectToString(apiResponse.getContent().get(0).getVendorName());
        String sanctionOrderNumber = Util.objectToString(apiResponse.getContent().get(0).getSanctionOrderNumber());

        String loggedinUserDistrictName    = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserDistrictName());
        String loggedinUserTalukName         = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserTalukName());
        String loggedinUserTscName       = Util.objectToString(apiResponse.getContent().get(0).getLoggedinUserTscName());



        dto.setHeader1(withFourSpaces(
                "ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು,  " + talukName + " ವಿಭಾಗ,  " + talukName + " ರವರ ಕಛೇರಿ ನಡವಳಿಗಳು"
        ));

        // Subject label
        dto.setHeader4(withFourSpaces("ವಿಷಯ  : "));

        // Subject content
        dto.setHeader20(withFourSpaces(financialYear + " ನೇ ಸಾಲಿನಲ್ಲಿ ಇಲಾಖೆಯು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ  ಸಹಯೋಗದೊಂದಿಗೆ ಅನುಷ್ಟಾನಗೊಳಿಸುತ್ತಿರುವ  ಕೇಂದ್ರ ಪುರಸ್ಕೃತ " + schemeNameKannada + "ಯೋಜನೆ " + schemeCategoryName + " ಅಡಿ   ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆಗಳನ್ನು  ಖರೀದಿಸಿರುವುದಕ್ಕಾಗಿ  ಸಹಾಯಧನ ಮಂಜೂರಾತಿ  ನೀಡುವ ಕುರಿತು."));

        // "ಉಲ್ಲೇಖ : "
        dto.setHeader5("ಉಲ್ಲೇಖ : ");

        // Reference list (1..5)
        dto.setHeader2(
                "1. ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ   :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ :  " + admGovtDate + "\n"
                        + "2. ರೇಷ್ಮೆ     ಕೃಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು   ರವರ    ಸುತ್ತೋಲೆ\n" + "   ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getSchemeCircularNo()
                        + " , ದಿನಾಂಕ : " + schemeCircularDate + "\n"
                        + "3. ಸರ್ಕಾರದ    ಆದೇಶ    ಸಂಖ್ಯೆ : " + apiResponse.getContent().get(0).getDeptDeleNo()
                        + " , ದಿನಾಂಕ : " + deptDeleDate + "\n"
                        + "4. ರೇಷ್ಮೆ     ಉಪ ನಿರ್ದೇಶಕರು,    ಜಿಲ್ಲಾ       ಪಂಚಾಯತ್,   " + districtName + "   ರವರ   ಜ್ಞಾಪನಪತ್ರ    ಸಂ : " + apiResponse.getContent().get(0).getAllotReleaseNo()
                        + " , ದಿನಾಂಕ : " + allotReleaseDate + "\n"
                        + "5. ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು, " + talukName
                        + " ವಿಭಾಗ    ರವರ    ಪ್ರಸ್ತಾವನೆ    ದಿನಾಂಕ : " + proposalDate);

        // "ಪೀಠಿಕೆ : "
        dto.setHeader24("ಪೀಠಿಕೆ : ");


        dto.setHeader8(withFourSpaces("    " + financialYear + "  ನೇ ಸಾಲಿನಲ್ಲಿ ರೇಷ್ಮೆ ಇಲಾಖೆಯ ವಿವಿಧ ಕಾರ್ಯಕ್ರಮಗಳ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ವಿವಿಧ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆಗಳಡಿ ಉಲ್ಲೇಖ(1)ರಲ್ಲಿ ಸರ್ಕಾರವು ಆಡಳಿತಾತ್ಮಕ ಅನುಮೋದನೆಯನ್ನು ನೀಡಿದ್ದು, ಉಲ್ಲೇಖ(2)ರಲ್ಲಿ "+ componentName + " ಕಾರ್ಯಕ್ರಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ಮಾರ್ಗಸೂಚಿಯನ್ನು ನೀಡಲಾಗಿದೆ. ಇಲಾಖೆಯು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಕೇಂದ್ರ ಪುರಸ್ಕೃತ " +
                "“"+ schemeNameKannada +"’’ ಯೋಜನೆಯನ್ನು ಅನುಷ್ಟಾನಗೊಳಿಸಲಾಗುತ್ತಿದೆ. ಸದರಿ ಯೋಜನೆಯಡಿ ರೇಷ್ಮೆ ಬೆಳೆಗಾರರು ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆಗಳನ್ನು ಖರೀದಿಸಿರುವುದಕ್ಕಾಗಿ  ಸಹಾಯಧನ ನೀಡಬೇಕಾಗಿದ್ದು, "+ schemeCategoryName +"  ಅಡಿ ಕೇಂದ್ರ:ರಾಜ್ಯ:ಫಲಾನುಭವಿ ಪಾಲು "+ centralSharePercentage + ":" + stateSharePercentage + ":"
                + beneficiarySharePercentage + " ಆಗಿರುತ್ತದೆ. ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆಗಳನ್ನು ಖರೀದಿಸಲು  ಘಟಕ ದರ ರೂ."+ Math.round(apiResponse.getContent().get(0).getActualAmount() == null ? 0f : apiResponse.getContent().get(0).getActualAmount())
                +"/-  ಗಳಿಗೆ ನಿಗಧಿಪಡಿಸಿದ್ದು, ಇದರಲ್ಲಿ ಶೇಕಡ "+(centralSharePercentage + stateSharePercentage)+"  ರಷ್ಟನ್ನು ಅಂದರೆ ರೂ. "+ totalSubsidyAmount+"/-  ಗಳನ್ನು ಸಹಾಯಧನವಾಗಿ ನೀಡಲಾಗುತ್ತಿದೆ. ಇದರಲ್ಲಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕ ದರದ ಶೇ."+centralSharePercentage+" ಅಂದರೆ ರೂ."+centralShareAmount+"/-"+
                " ಗಳು ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು ಘಟಕ ದರದ ಶೇ."+stateSharePercentage+" ಅಂದರೆ ರೂ."+stateShareAmount+"/- ಗಳು ಆಗಿರುತ್ತದೆ. ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು ಕೇಂದ್ರದ ಪಾಲಿನ ಅನುದಾನವನ್ನು PFMS ಮುಖಾಂತರ ಒದಗಿಸಿದ್ದು SBI, ಬ್ಯಾಂಕ್ ಬಹುಮಹಡಿ ಕಟ್ಟಡ ಶಾಖೆಯ ಬ್ಯಾಂಕ್ ಖಾತೆಯಲ್ಲಿ ಜಮೆಯಾಗಿರುತ್ತದೆ. ಆದ್ದರಿಂದ ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನ ರೂ."+centralShareAmount+" ಗಳನ್ನು("+centralSharePercentage+"%) ಕೇಂದ್ರ ರೇಷ್ಮೆ " +
                "ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ ಇದನ್ನು ಆಯಾ ಜಿಲ್ಲೆಗಳ ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರುಗಳ ಕಛೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮುಖಾಂತರ  ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ. ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ದಿ ಯೋಜನೆ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ:"+headAccountName+"("+headDescription+")"+
        " ರಡಿ ರೇಷ್ಮೆ ಅಭಿವೃದ್ದಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು, ಬೆಂಗಳೂರು ರವರು ಖಜಾನೆ-2 ಮುಖಾಂತರ ಬಿಡುಗಡೆಗೊಳಿಸಿ ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ಸಹ ಡಿಬಿಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುವುದು."));

        dto.setHeader10(withFourSpaces("    "+ districtName +"   ಜಿಲ್ಲೆಯ   "+ talukName +" ತಾಲ್ಲೂಕಿನ "+ tscName + " ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ " + farmerVillage +
                " ಗ್ರಾಮದಲ್ಲಿ "+ schemeCategoryName +" ವರ್ಗಕ್ಕೆ ಸೇರಿದ ಶ್ರೀ/ಶ್ರೀಮತಿ "+ farmerName + " (" + fruitsId + ") ಬಿನ್/ಕೋಂ. " + fatherNameKan + " ಇವರು  "+ landVillage + " ಗ್ರಾಮದ ಸರ್ವೆ ನಂ "+ surveyText
                +" ರಲ್ಲಿ  "+extentOfMulberry+" ಎಕರೆ ವಿಸ್ತೀರ್ಣದಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಹೊಂದಿರುತ್ತಾರೆ. ಸದರಿಯವರು ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆಯಾದ "+ equipmentName +"  ಯನ್ನು ರೇಷ್ಮೆ" +
                " ಇಲಾಖೆಯಿಂದ ಅಧಿಕೃತವಾಗಿ ಗುರುತಿಸಿರುವ ರೇಷ್ಮೆ ಹುಳುಸಾಕಾಣಿಕೆ ಸಲಕರಣೆ ಸಂಸ್ಥೆ ಯಾದ ಮೆII "+ vendorName +"   ಇವರಿಂದ  ಟ್ಯಾಕ್ಸ್ ಇನ್ ವಾಯ್ಸ್   ಸಂಖ್ಯೆ "+ taxInvoiceNo + " ದಿನಾಂಕ :"+ taxInvoiceDate+
                " ರಂತೆ  ರೂ."+ beneficiaryShareAmount+"/- ಗಳ ವೆಚ್ಚದಲ್ಲಿ ಖರೀದಿಸಲು ತಮ್ಮ ಪಾಲಿನ ಹಣ ರೂ."+ beneficiaryShareAmount+"/- ( ರೂ."+beneficiaryShareWords+" )  ಗಳನ್ನು (ಶೇ.10)(Percentage) " +
                "ರಸೀದಿ  ಸಂಖ್ಯೆ"+ taxInvoiceNo +" ದಿನಾಂಕ:"+ taxInvoiceDate+"  ಪಾವತಿಸಿದ್ದು,  ಸದರಿ ಸಲಕರಣೆಗಳನ್ನು ಫಲಾನುಭವಿಗೆ ಸರಬರಾಜು ಮಾಡಿರುತ್ತಾರೆ.\n" +
                "  ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ "+ loggedinUserDistrictName+" ಹಾಗೂ ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು "+ divisionName+" ವಿಭಾಗ ರವರು ಪರಿಶೀಲಿಸಿ ದೃಢೀಕರಿಸಿ ಸಲ್ಲಿಸಿದ ಎಲ್ಲಾ ಅಗತ್ಯ " +
                "ದಾಖಲಾತಿಗಳನ್ನು ಒಳಗೊಂಡ ಪ್ರಸ್ತಾವನೆಯನ್ನು   ಉಲ್ಲೇಖ (5) ರನ್ವಯ ಈ ಕಛೇರಿಗೆ ಶಿಫಾರಸ್ಸು ಮಾಡಿ ಸಲ್ಲಿಸಿದ್ದು, ಸದರಿ ಫಲಾನುಭವಿಗೆ ರೂ. "+totalSubsidyAmount+"/- ಗಳ ಸಹಾಯಧನವನ್ನು ಮಂಜೂರು ಮಾಡುವಂತೆ ಕೋರಿರುತ್ತಾರೆ." +
                " ಮಂಜೂರಾತಿಗೆ ಕೋರಲಾಗಿರುವ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡಲು ಉಲ್ಲೇಖ (3)ರ ಸರ್ಕಾರಿ ಆದೇಶದ ರೀತ್ಯಾ ಈ ಕಛೇರಿಯ ಅಧಿಕಾರ ಪ್ರತ್ಯಾಯೋಜನೆ ವ್ಯಾಪ್ತಿಯಲ್ಲಿದ್ದು, ಉಲ್ಲೇಖ (4)ರಲ್ಲಿ ಸದರಿ ಕಾರ್ಯಕ್ರಮದ ಅನುಷ್ಠಾನಕ್ಕಾಗಿ ನೀಡಿರುವ ಮಾರ್ಗಸೂಚಿಯನ್ವಯ" +
                " ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡಲು ಅನುದಾನ ಬಿಡುಗಡೆ ಮಾಡಲಾಗಿದೆ. ಅದರಂತೆ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡಬಹುದಾಗಿದ್ದು, ಈ ಕೆಳಕಂಡ ಆದೇಶವನ್ನು ಹೊರಡಿಸಿದೆ. "));


        dto.setHeader12("ಆದೇಶ");

        dto.setHeader13(withFourSpaces(
                "ಸಂಖ್ಯೆ  : ರೇಜಂನಿ/ " + shortDistrictKannada + " ವಿ/ಸಲಕರಣೆ/\""
                        + financialYear + "\"/" + districtName
                        + "/ರೇಹುಸಾಮ/ಸಧನ/ಸಾ/ಮಂ/" + sanctionOrderNumber
                        + " / ದಿನಾಂಕ:  " + formattedDate
        ));

        dto.setHeader16(withFourSpaces("  ಪೀಠಿಕೆಯಲ್ಲಿ ವಿವರಿಸಿದಂತೆ, "+financialYear+" ನೇ ಸಾಲಿನ ಕೇಂದ್ರ ಪುರಸ್ಕೃತ “"+schemeNameKannada+"”  ಯೋಜನೆ” "+schemeCategoryName+" ಅಡಿ "+tscName+"ತಾಂತ್ರಿಕ " +
                "ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯ     "+districtName+" ಜಿಲ್ಲೆಯ "+ talukName + " ತಾಲ್ಲೂಕಿನ ದೇವನಹಳ್ಳಿ   "+ tscName +
                "ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ "+ farmerVillage + " ಗ್ರಾಮದ ಶ್ರೀ/ಶ್ರೀಮತಿ "+ farmerName + " (" + fruitsId + ") ಬಿನ್/ಕೋಂ. " + fatherNameKan+" ಇವರು ರೇಷ್ಮೆ ಹುಳು ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆ  ಖರೀದಿಸಿರುವ ವಿವರಗಳು ಈ ಕೆಳಕಂಡಂತಿದೆ. "));


        dto.setHeader14(withFourSpaces(" (ರೂ. "+ totalSubsidyWords+")\n" +
                "ಮೇಲ್ಕಂಡ ಸಹಾಯಧನ ರೂ."+totalSubsidyAmount+"/- ( ರೂ. "+totalSubsidyWords+" ) ಗಳಿಗೆ ಮುಚ್ಚಳಿಕೆಯಲ್ಲಿನ ಷರತ್ತು ಮತ್ತು ತಗಾದೆಗಳಿಗೆ ಸಂಬಂಧಿಸಿದ ಫಲಾನುಭವಿ ಹಾಗೂ ಶಿಫಾರಸ್ಸು " +
                "ಮಾಡಿದ ಕ್ಷೇತ್ರಮಟ್ಟದ ಅಧಿಕಾರಿಗಳನ್ನು ಜವಾಬ್ದಾರಿ ಮಾಡಿ ಮಂಜೂರಾತಿ ನೀಡಿದೆ. ಈ  ಸಹಾಯಧನದ ಪೈಕಿ ರಾಜ್ಯದ ಪಾಲು ರೂ. "+beneficiaryShareAmount+"/-(ರೂ. "+beneficiaryShareWords+") ಮಾತ್ರ)ಗಳನ್ನು ರಾಜ್ಯ " +
                "ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆಯ ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ "+headAccountName+"("+headDescription+")  ರಡಿ ಖಜಾನೆ-2 ರಲ್ಲಿ ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ ಅನುದಾನದಲ್ಲಿ, ಹಾಗೂ ಕೇಂದ್ರದ ಪಾಲು  ರೂ. "+centralShareAmount+"/-" +
                " (ರೂ. "+centralShareWords+") ಗಳನ್ನು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ನೀಡಿರುವ ಮೊತ್ತದಲ್ಲಿ ಸಂಬಂಧಿಸಿದ ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರುಗಳು ಸಲಕರಣೆ ಸರಬರಾಜು ಮಾಡಿದ ಸಂಸ್ಥೆಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಡಿಬಿಟಿ ಮೂಲಕ ಜಮಾ ಮಾಡಲಾಗುವುದು.\n" +
                "     ಈ ವೆಚ್ಚವನ್ನು ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ "+headAccountName+"("+headDescription+")   "+ schemeCategoryName+"  ಅಡಿ ಭರಿಸುವುದು."));


        dto.setVendorName(vendorName);
        dto.setEquipmentName(equipmentName);
        dto.setTaxInvoiceNo(taxInvoiceNo);
        dto.setTaxInvoiceDate(taxInvoiceDate);

        // If your SanctionOrderResponse has Float fields for these, you can use
        // the original values from apiResponse or the rounded ints:
        dto.setBeneficiaryShareAmount(
                apiResponse.getContent().get(0).getBeneficiaryShareAmount()
        );
        dto.setCentralSanctionAmount(
                apiResponse.getContent().get(0).getCentralSanctionAmount()
        );
        dto.setStateSanctionAmount(
                apiResponse.getContent().get(0).getStateSanctionAmount()
        );
        dto.setSanctionAmount(
                apiResponse.getContent().get(0).getSanctionAmount()
        );

        // Signatory – ADS
        dto.setHeader17(withFourSpaces(
                "ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು\n"
                        + divisionName + " ವಿಭಾಗ,\n" + talukName + ".\n"
        ));

        // Recipients – you can tweak as needed
        dto.setHeader18(withFourSpaces("ಇವರಿಗೆ,\n" +
                "ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು, \n" +
                divisionName +"  ವಿಭಾಗ,  "+loggedinUserTalukName+". \n"
                        + "ಪ್ರತಿಯನ್ನು:\n"
                        + "1. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ, ತಾಂತ್ರಿಕ ಸೇವಾ  ಕೇಂದ್ರ,  " + loggedinUserDistrictName
                        + " ಇವರ ಮಾಹಿತಿಗಾಗಿ.\n"
                        + "2. ಶ್ರೀ/ಶ್ರೀಮತಿ "+farmerName+" ಬಿನ್/ಕೋಂ. "+fatherNameKan+" , "+ farmerVillage+" ಗ್ರಾಮ "+talukName+"" +
                           " ತಾಲ್ಲೂಕು  ಇವರ ಮಾಹಿತಿಗಾಗಿ, " + tscName + " ಇವರಿಗೆ ಮಾಹಿತಿಗಾಗಿ.\n"+
                          "3.	ಮೆ|| "+vendorName+",  ಇವರ ಮಾಹಿತಿಗಾಗಿ" +
                          "4. ಸಂಬಂಧಿಸಿದ ಸಂಸ್ಥೆಗೆ"));

        dto.setLogurl("/reports/Seal_of_Karnataka.PNG");

        // (optional) set some common fields – if needed elsewhere
        dto.setFarmerFirstName(" ಶ್ರೀ /.ಶ್ರೀಮತಿ.  " + farmerName);
        dto.setFruitsId(" ರವರು (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + fruitsId + ")");
        dto.setDistrictName(districtName + " ಜಿಲ್ಲೆ, ");
        dto.setTalukName(talukName + " ತಾಲ್ಲೂಕು , ");
        dto.setVillageName(farmerVillage + " ಗ್ರಾಮದ ನಿವಾಸಿಯಾದ ");

        list.add(dto);
        return new JRBeanCollectionDataSource(list);
    }


    private JRDataSource getDataSourceForWorkOrderRHEquipment(SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException, JAXBException {

        SanctionOrder apiResponse = apiService.fetchDataFromSanctionEquipment(requestDto);
        List<SanctionOrderResponse> workOrderGenerationReportResponseList = new LinkedList<>();

        if (apiResponse.getContent() != null && !apiResponse.getContent().isEmpty()) {

            SanctionOrderResponse apiData = apiResponse.getContent().get(0);
            SanctionOrderResponse response = new SanctionOrderResponse();

            // ✅ Split created_date into date (dd/MM/yyyy) and time (HH:mm)
            String createdDateTime = apiData.getCreatedDate();
            String datePart = "";
            String timePart = "";

            if (createdDateTime != null && !createdDateTime.isEmpty()) {
                try {
                    // with milliseconds
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    LocalDateTime ldt = LocalDateTime.parse(createdDateTime, inputFormatter);
                    datePart = ldt.format(outputDateFormatter);
                    timePart = ldt.format(outputTimeFormatter);

                } catch (Exception e1) {
                    try {
                        // without milliseconds
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                        LocalDateTime ldt = LocalDateTime.parse(createdDateTime, inputFormatter);
                        datePart = ldt.format(outputDateFormatter);
                        timePart = ldt.format(outputTimeFormatter);

                    } catch (Exception e2) {
                        try {
                            // only date (yyyy-MM-dd)
                            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            LocalDate date = LocalDate.parse(createdDateTime.substring(0, 10), inputFormatter);
                            datePart = date.format(outputDateFormatter);
                            timePart = "";
                        } catch (Exception e3) {
                            // last fallback – simple reverse if separated by / or -
                            if (createdDateTime.contains("/")) {
                                String[] parts = createdDateTime.split("/");
                                if (parts.length == 3) {
                                    datePart = parts[2] + "/" + parts[1] + "/" + parts[0];
                                }
                            } else if (createdDateTime.contains("-")) {
                                String[] parts = createdDateTime.split("-");
                                if (parts.length == 3) {
                                    datePart = parts[2] + "/" + parts[1] + "/" + parts[0];
                                }
                            }
                        }
                    }
                }
            }

            // ------------ FIELD SHORTCUTS FROM SanctionOrderResponse ------------
            String schemeNameKannada   = Util.objectToString(apiData.getSchemeNameInKannada());
            String schemeCategoryName  = Util.objectToString(apiData.getScCategoryName());
            String districtName        = Util.objectToString(apiData.getLoggedinUserDistrictName());
            String talukName           = Util.objectToString(apiData.getLoggedinUserTalukName());
            String tscName             = Util.objectToString(apiData.getLoggedinUserTscName());
            String farmerName          = Util.objectToString(apiData.getFarmerFirstName());
            String fatherNameKan       = Util.objectToString(apiData.getFatherNameKan());
            String fruitsId            = Util.objectToString(apiData.getFruitsId());
            String equipmentName       = Util.objectToString(apiData.getEquipmentName());
            String vendorName          = Util.objectToString(apiData.getVendorName());
            String arn                 = Util.objectToString(apiData.getArn());

            // -------- HEADERS AS PER SS 2025-26 REARING-EQUIPMENT WORK ORDER --------

            // ಸಂಖ್ಯೆ + ARN + ಕೆಲಸದ ಆದೇಶ ಸಂಖ್ಯೆ
            response.setHeader1(
                    "ಸಂಖ್ಯೆ  :  "
                            + Util.objectToString(apiData.getWorkOrderNumber())
                            + "     ARN ಸಂಖ್ಯೆ  :  "
                            + arn
            );

            // ದಿನಾಂಕ
            response.setHeader2("ದಿನಾಂಕ  :  " + datePart);

            // Subject line: Scheme + Category + equipment subsidy work order
            response.setHeader3(
                    "ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ     “"
                            + schemeNameKannada
                            + "”     ಯೋಜನೆ     "
                            + schemeCategoryName
                            + "     ಅಡಿ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರು     ರೇಷ್ಮೆ     ಹುಳು     ಸಾಕಾಣಿಕೆ"
                            + "     ಸಲಕರಣೆಗಳ     ಖರೀದಿಗೆ     ಸಹಾಯಧನಕ್ಕೆ     ಸಂಬಂಧಿಸಿದಂತೆ     ಕಾರ್ಯಾದೇಶ"
            );

            // Main body paragraph – aligned with SS 2025-26 template
            response.setLineItemComment(
                    "              "
                            + tscName
                            + "     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರದ     ವ್ಯಾಪ್ತಿಯ     "
                            + districtName
                            + "     ಜಿಲ್ಲೆಯ     "
                            + talukName
                            + "     ತಾಲ್ಲೂಕಿನಲ್ಲಿ     ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತಿರುವ     "
                            + schemeCategoryName
                            + "     ವರ್ಗಕ್ಕೆ     ಸೇರಿದ     ಶ್ರೀ/ಶ್ರೀಮತಿ     "
                            + farmerName
                            + "     ಬಿನ್/ಕೋಂ.     "
                            + fatherNameKan
                            + "     ಇವರು     ಕೇಂದ್ರ     ಪುರಸ್ಕೃತ     “"
                            + schemeNameKannada
                            + "”     ಯೋಜನೆ     "
                            + schemeCategoryName
                            + "     ಅಡಿಯಲ್ಲಿ     ರೇಷ್ಮೆ     ಹುಳು     ಸಾಕಾಣಿಕೆ\n"
                            + "ಸಲಕರಣೆಯಾದ     "
                            + equipmentName
                            + "     ಖರೀದಿಸಲು     ಹಾಗೂ     ಅಳವಡಿಕೆಗೆ     ಇಲಾಖೆಯ     ನಿಗದಿಪಡಿಸಿರುವ     ಘಟಕದರದ\n"
                            + "ಅನುಸಾರ     ಸಹಾಯಧನ     ಪಡೆಯಲು     ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಿರುವುದು.\n"
                            + "              ಸದರಿ     ಸಲಕರಣೆಯನ್ನು     ರೇಷ್ಮೆ     ಇಲಾಖೆಯಿಂದ     ಅಧಿಕೃತವಾಗಿ     ಗುರುತಿಸಲ್ಪಟ್ಟ\n"
                            + "ರೇಷ್ಮೆ     ಹುಳುಸಾಕಾಣಿಕೆ     ಸಲಕರಣೆ     ಸಂಸ್ಥೆಯಾದ     ಮೆ||     "
                            + vendorName
                            + "     (ಎಂಪ್ಯಾನೆಲ್ಡ್     ವೆಂಡರ್)     ಇವರಿಂದ\n"
                            + "ಖರೀದಿಸಲು     ಇಲಾಖೆಯ     ನಿಗದಿಪಡಿಸಿರುವ     ಘಟಕದರದ     ಅನುಸಾರ     ಖರೀದಿಸಿ     ಅಳವಡಿಸಿಕೊಳ್ಳಲು\n"
                            + "ಫಲಾನುಭವಿಗೆ     ಅನುಮತಿ     ನೀಡಲಾಗಿದೆ.\n"
                            + "              ಇದಕ್ಕೆ     ಸಂಬಂಧವಾಗಿ     ಫಲಾನುಭವಿಯು     ಸದರಿ     ಸಲಕರಣೆಗೆ     ನಿಗದಿಪಡಿಸಿರುವ\n"
                            + "ತನ್ನ     ಪಾಲಿನ     ಶೇ.25     ರಷ್ಟು     ಮೊತ್ತವನ್ನು     ಸಂಬಂಧಿತ     ಸಂಸ್ಥೆಗೆ     ಪಾವತಿಸಿ,     ಅಗತ್ಯ\n"
                            + "ದಾಖಲಾತಿಗಳನ್ನು     ಸೇರಿಸಿ     ಸಹಾಯಧನ     ಮಂಜೂರಾತಿಗಾಗಿ     ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಬೇಕಾಗಿದೆ."
            );

            // ARN / registration note – small separate para like in sample
            response.setHeader4(
                    "                    ಶ್ರೀ/ಶ್ರೀಮತಿ     "
                            + farmerName
                            + "  ("
                            + fruitsId
                            + ")     ಬಿನ್/ಕೋಂ  "
                            + fatherNameKan
                            + "     ಇವರಿಂದ     ಮೇಲ್ಕಾಣಿಸಿದ     ಯೋಜನೆಯ     ಅಡಿಯಲ್ಲಿ     ಸಲಕರಣೆ\n"
                            + "ಖರೀದಿಗಾಗಿ     ARN     ಸಂಖ್ಯೆ     "
                            + arn
                            + "     ರಂತೆ     ಅರ್ಜಿಯನ್ನು     ಸಲ್ಲಿಸಿರುವುದು     ದಾಖಲಾಗಿದೆ."
            );

            // Signature block – DDS (ZP)
            response.setHeader5(
                    "ರೇಷ್ಮೆ     ಉಪ     ನಿರ್ದೇಶಕರು,\n"
                            + "ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,\n"
                            + districtName
            );

            // Copy-to block
            response.setHeader6(
                    "ಇವರಿಗೆ,\n"
                            + "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,  "
                            + talukName
                            + "     ವಿಭಾಗ,\n"
                            + "ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ,  "
                            + tscName
                            + ".\n"
                            + "ಸಂಬಂಧಿಸಿದ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರಿಗೆ."
            );

            // --------- OTHER FIELDS (OPTIONAL / IF USED IN JRXML) ---------

            response.setLogurl("/reports/Seal_of_Karnataka.PNG");

            response.setFarmerFirstName(farmerName);
            response.setFatherNameKan(fatherNameKan);
            response.setFruitsId(fruitsId);
            response.setSchemeNameInKannada(schemeNameKannada);
            response.setScCategoryName(schemeCategoryName);

            workOrderGenerationReportResponseList.add(response);
        }

        return new JRBeanCollectionDataSource(workOrderGenerationReportResponseList);
    }


    private JRDataSource getDataSourceForChawkiSanctionOrder(SanctionOrderPrintRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromChawkiSanctionOrder(requestDto);

        if (apiResponse == null
                || apiResponse.getContent() == null
                || apiResponse.getContent().isEmpty()) {

            logger.warn("No data returned from CRC sanction API for applicationFormId: {}",
                    requestDto.getApplicationFormId());
            return new JREmptyDataSource();
        }

        List<SanctionOrderResponse> list = new ArrayList<>();
        SanctionOrderResponse r = apiResponse.getContent().get(0);
        SanctionOrderResponse dto = new SanctionOrderResponse();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String admGovtDate        = formatDate(r.getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(r.getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(r.getDeptDeleDate(), sdf);
        String allotReleaseDate   = formatDate(r.getAllotReleaseDate(), sdf);
        String releaseDate        = formatDate(r.getReleaseDate(), sdf);
        String proposalDate       = formatDate(r.getProposalDate(), sdf);

        // ---- Amounts & shares ----
        int centralShareAmount = Math.round(
                r.getCentralSanctionAmount() == null ? 0f : r.getCentralSanctionAmount());
        int stateShareAmount = Math.round(
                r.getStateSanctionAmount() == null ? 0f : r.getStateSanctionAmount());

        int centralSharePercentage = Math.round(
                r.getCentralSharePercentage() == null ? 0f : r.getCentralSharePercentage());
        int stateSharePercentage = Math.round(
                r.getStateSharePercentage() == null ? 0f : r.getStateSharePercentage());

        // CRC side – total unit cost & subsidy from aggregated fields
        int totalClaimedAmount = Math.round(
                r.getTotalClaimed() == null ? 0f : r.getTotalClaimed());
        int totalEligibleAmount = Math.round(
                r.getTotalEligible() == null ? 0f : r.getTotalEligible());
        int totalSubsidyAmount = Math.round(
                r.getTotalSubsidy() == null ? 0f : r.getTotalSubsidy());

        // number to words
        String totalSubsidyWords   = KannadaNumberUtil.convertNumberToKannadaWords(totalSubsidyAmount);
        String centralShareWords   = KannadaNumberUtil.convertNumberToKannadaWords(centralShareAmount);
        String stateShareWords     = KannadaNumberUtil.convertNumberToKannadaWords(stateShareAmount);

        // beneficiary share is just difference between unit cost and subsidy (if you need it)
        int beneficiaryShareAmount = totalEligibleAmount - totalSubsidyAmount;
        if (beneficiaryShareAmount < 0) beneficiaryShareAmount = 0;
        String beneficiaryShareWords =
                KannadaNumberUtil.convertNumberToKannadaWords(beneficiaryShareAmount);

        // ---- Common text fields from response ----
        String financialYear      = Util.objectToString(r.getFinancialYear());
        String schemeNameKannada  = Util.objectToString(r.getSchemeNameInKannada());
        String categoryName       = Util.objectToString(r.getCategoryName());
        String componentName      = Util.objectToString(r.getScComponentName());
        String headAccountName    = Util.objectToString(r.getScHeadAccountName());
        String headDescription    = Util.objectToString(r.getDescription());
        String divisionName       = Util.objectToString(r.getDivisionName());
        String districtName       = Util.objectToString(r.getLoggedinUserDistrictName());
        String talukName          = Util.objectToString(r.getLoggedinUserTalukName());
        String tscName            = Util.objectToString(r.getLoggedinUserTscName());
        String farmerVillage      = Util.objectToString(r.getVillageNameInKannada());
        String farmerName         = Util.objectToString(r.getFarmerFirstName());
        String fatherNameKan      = Util.objectToString(r.getFatherNameKan());
        String fruitsId           = Util.objectToString(r.getFruitsId());
        String landVillage        = Util.objectToString(r.getLandVillage());
        String surveyNumber       = Util.objectToString(r.getSurveyNumber());
        String extentOfMulberry   = Util.objectToString(r.getExtentOfMulberry());

        String shortDistrictKannada = getKannadaShortForm(districtName);

        // Date for sanction order number (like RHEquipment)
        String formattedDate;
        try {
            String inputDate = r.getCreatedDate();   // createdDate is string in your mapping
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
            Date d = in.parse(inputDate);
            formattedDate = out.format(d);
        } catch (Exception e) {
            formattedDate = Util.objectToString(r.getCreatedDate());
        }

        String kaneshNo       = Util.objectToString(r.getKaneshNo());
        String sanctionNo     = Util.objectToString(r.getSanctionNo());
        String sanctionOrderNumber = Util.objectToString(r.getSanctionOrderNumber());

        // ========= HEADER 1 – OFFICE =========
        dto.setHeader1(withFourSpaces(
                "ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ದಿ ಆಯುಕ್ತರು ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು ಇವರ ಕಛೇರಿ ನಡವಳಿಗಳು"
        ));

        // ========= SUBJECT =========
        dto.setHeader4(withFourSpaces("ವಿಷಯ  : "));
        dto.setHeader20(withFourSpaces(
                financialYear
                        + " ನೇ ಸಾಲಿನಲ್ಲಿ ಇಲಾಖೆಯು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಅನುಷ್ಟಾನಗೊಳಿಸುತ್ತಿರುವ "
                        + "ಕೇಂದ್ರ ವಲಯ “ಸಿಲ್ಕ್ ಸಮಗ್ರ-2” ಯೋಜನೆಯಡಿ ಹೊಸದಾಗಿ ಸ್ಥಾಪಿಸಲ್ಪಡುವ ನೊಂದಾಯಿತ ಖಾಸಗಿ "
                        + "ದಿ.ತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರಗಳಿಗೆ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡುವ ಕುರಿತು."
        ));

        // ========= REFERENCES (ಉಲ್ಲೇಖ) =========
        dto.setHeader5("ಉಲ್ಲೇಖ : ");

        dto.setHeader2(
                "1. ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ : " + r.getAdmGovtOrder() + " , ದಿನಾಂಕ : " + admGovtDate + "\n"
                        + "2. ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆ ಸಂಖ್ಯೆ : " + r.getSchemeCircularNo()
                        + " , ದಿನಾಂಕ : " + schemeCircularDate + "\n"
                        + "3. ಸರ್ಕಾರದ ಆದೇಶ ಸಂಖ್ಯೆ : " + r.getDeptDeleNo()
                        + " , ದಿನಾಂಕ : " + deptDeleDate + "\n"
                        + "4. ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು, " + divisionName + " ವಿಭಾಗ ರವರ ಪತ್ರ ಸಂ : "
                        + Util.objectToString(r.getAllotReleaseNo()) + " , ದಿನಾಂಕ : " + allotReleaseDate + "."
        );

        // ========= ಪೀಠಿಕೆ =========
        dto.setHeader24("ಪೀಠಿಕೆ : ");

        dto.setHeader8(withFourSpaces(
                financialYear
                        + " ನೇ ಸಾಲಿನಲ್ಲಿ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯ ಸಹಯೋಗದೊಂದಿಗೆ ಇಲಾಖೆಯು ಕೇಂದ್ರ ವಲಯ “ಸಿಲ್ಕ್ ಸಮಗ್ರ-2” "
                        + "ಯೋಜನೆಯಡಿ ಹೊಸದಾಗಿ ಸ್ಥಾಪಿಸಿರುವ ನೊಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರಗಳಿಗೆ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ "
                        + "ಸ್ಥಾಪನೆ/ನಿರ್ವಹಣೆ, ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ನಿರ್ಮಾಣ ಮತ್ತು ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಸಲಕರಣೆ ಖರೀದಿಗೆ ಸಹಾಯಧನ ನೀಡಲು "
                        + "ಉಲ್ಲೇಖ (1)ರ ಸುತ್ತೋಲೆಯಲ್ಲಿ ಮಾರ್ಗಸೂಚಿ ನೀಡಿ ಕಾರ್ಯಕ್ರಮವನ್ನು ಅನುಷ್ಟಾನಗೊಳಿಸಲಾಗುತ್ತಿದೆ. ಸದರಿ ಯೋಜನೆಯಡಿ "
                        + "ಇದಕ್ಕಾಗಿ ಘಟಕ ದರ ರೂ.13.00 ಲಕ್ಷಗಳನ್ನು ನಿಗದಿಪಡಿಸಿದ್ದು, ಸಹಾಯಧನ ಸಾಮಾನ್ಯ ವರ್ಗಕ್ಕೆ ಶೇ 75 ರಷ್ಟು ಅಂದರೆ "
                        + "ರೂ.9.75 ಲಕ್ಷ ನೀಡಲಾಗುವುದು. ಇದರ ಪೈಕಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕದರದ ಶೇ 50 ಅಂದರೆ ರೂ.6.50 ಲಕ್ಷ ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು "
                        + "ಘಟಕ ದರದ ಶೇ 25 ರೂ.3.25 ಲಕ್ಷಗಳಾಗಿದ್ದು, ಪರಿಶಿಷ್ಟ ಜಾತಿ/ಪರಿಶಿಷ್ಟ ಪಂಗಡದ ವರ್ಗಕ್ಕೆ ಶೇ.90ರಷ್ಟು ಅಂದರೆ "
                        + "ರೂ.11.70 ಲಕ್ಷಗಳನ್ನು ನೀಡಲಾಗುವುದು. ಇದರ ಪೈಕಿ ಕೇಂದ್ರದ ಪಾಲು ಘಟಕ ದರದ ಶೇ.65 (ರೂ.8.45 ಲಕ್ಷ) ಮತ್ತು ರಾಜ್ಯದ ಪಾಲು "
                        + "ಘಟಕ ದರದ ಶೇ.25 (ರೂ.3.25 ಲಕ್ಷ) ಗಳಾಗಿದ್ದು, ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ಭರಿಸುವುದರಿಂದ "
                        + "ಇದನ್ನು ಆಯಾ ಜಿಲ್ಲೆಯ ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರುಗಳ ಕಚೇರಿಯಿಂದ ಡಿ.ಬಿ.ಟಿ ಮುಖಾಂತರ ಫಲಾನುಭವಿ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ "
                        + "ನೇರವಾಗಿ ಸಹಾಯಧನವನ್ನು ಜಮಾ ಮಾಡಲಾಗುವುದು. ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆ (ಸಾಮಾನ್ಯ) "
                        + "ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-1-35 (106) ಅಡಿ ಖಜಾನೆ-2ರ ಮುಖಾಂತರ ಫಲಾನುಭವಿಯ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲಾಗುವುದು. "
                        + "ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಸ್ಥಾಪನೆ/ನಿರ್ವಹಣೆ: " + districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ "
                        + farmerVillage + " ಗ್ರಾಮದ ಶ್ರೀ/ಶ್ರೀಮತಿ " + farmerName + " (" + fruitsId + ") ಬಿನ್/ಕೋಂ. "
                        + fatherNameKan + " ರವರು " + categoryName + " ವರ್ಗದವರಾಗಿದ್ದು, "
                        + districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ " + tscName
                        + " ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯ " + landVillage
                        + " ಗ್ರಾಮದಲ್ಲಿ ಹೊಸದಾಗಿ ಅಮ್ಮ ನೊಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಹುಳು ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರ "
                        // 🔹 If you have CRC name from API, replace the empty string below:
                        // + r.getCrcName()
                        + " ಸ್ಥಾಪಿಸಲು ಕೇಂದ್ರೀಯ ರೇಷ್ಮೆ ಸಂಶೋಧನೆ ಮತ್ತು ತರಬೇತಿ ಸಂಸ್ಥೆ, ಮೈಸೂರುದಲ್ಲಿ ತರಬೇತಿಯನ್ನು ಪಡೆದಿದ್ದು, "
                        + "ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯಿಂದ ನೋಂದಣಿಯನ್ನು ಪಡೆದಿರುತ್ತಾರೆ. ಇವರು ಕೇಂದ್ರ ವಲಯ “ಸಿಲ್ಕ್ ಸಮಗ್ರ-2” ಯೋಜನೆಯ "
                        + "ಉಲ್ಲೇಖ (2) ರ ಮಾರ್ಗಸೂಚಿಯಂತೆ " + landVillage + " ಗ್ರಾಮದ ಸರ್ವೆ ನಂ. " + surveyNumber + " ರಲ್ಲಿ "
                        + extentOfMulberry + " ಎಕರೆಗಳಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ಚಾಕಿ ತೋಟ ಸ್ಥಾಪನೆ ಮತ್ತು ನಿರ್ವಹಣೆ ಮಾಡಿರುವ ಬಗ್ಗೆ ಸಂಬಂಧಿಸಿದ "
                        + "ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು " + tscName + " ವಿಭಾಗ, " + districtName
                        + ", ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ " + districtName
                        + " ರವರ ಶಿಫಾರಸ್ಸಿನೊಂದಿಗೆ ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು " + divisionName
                        + " ವಿಭಾಗ ರವರು ಪರಿಶೀಲಿಸಿ ಉಲ್ಲೇಖ (4) ರಲ್ಲಿ ಸಹಾಯಧನ ಮಂಜೂರಾತಿಗೆ ಪ್ರಸ್ತಾವನೆಯನ್ನು ಸಲ್ಲಿಸಿರುತ್ತಾರೆ."
        ));


        // ========= Beneficiary / CRC details – like second paragraph of PDF =========
        dto.setHeader10(withFourSpaces("ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ನಿರ್ಮಾಣ: "
                        + "ಶ್ರೀ/ಶ್ರೀಮತಿ " + farmerName + " ಬಿನ್ " + fatherNameKan + " ರವರು "
                        + districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ "
                        + farmerVillage + " ಗ್ರಾಮದ ಸರ್ವೆ ನಂ. " + surveyNumber
                        + " ರಲ್ಲಿ ಉದ್ದಗಲ " + Util.objectToString(r.getUnitCost()) + " X "
                        + Util.objectToString(r.getUnitCost())
                        + " ಅಡಿಗಳಂತೆ ಒಟ್ಟು " + Util.objectToString(r.getUnitCost())
                        + " ಚದರಡಿ ವಿಸ್ತೀರ್ಣದ ಪ್ರತ್ಯೇಕ ಚಾಕಿ ಹುಳು ಸಾಕಾಣಿಕಾ ಮನೆಯನ್ನು ನಿರ್ಮಿಸಿದ್ದು, "
                        + "ವಲಯಾಧಿಕಾರಿಗಳು ಪ್ರಸ್ತಾವನೆಯನ್ನು ಸಂಬಂಧಿಸಿದ ಮೇಲಧಿಕಾರಿಗಳ ಶಿಫಾರಸ್ಸಿನೊಂದಿಗೆ ಸಹಾಯಧನಕ್ಕಾಗಿ ಸಲ್ಲಿಸಿರುತ್ತಾರೆ.\n\n"

                        // ----------------------------------------------------
                        // 🔵 ADDING PARAGRAPH 2 – “ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಸಲಕರಣೆಗಳ ಖರೀದಿ”
                        // ----------------------------------------------------
                        + "ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಸಲಕರಣೆಗಳ ಖರೀದಿ: "
                        + "ಶ್ರೀ/ಶ್ರೀಮತಿ " + farmerName + " ಬಿನ್ " + fatherNameKan + " ರವರು "
                        + districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ "
                        + farmerVillage + " ಗ್ರಾಮದಲ್ಲಿ ಹೊಸದಾಗಿ ಸ್ಥಾಪಿಸಿರುವ ಅಮ್ಮ ನೊಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ "
                        + "ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರದ ಉಪಯೋಗಕ್ಕಾಗಿ "
                        + Util.objectToString(r.getCrcName())
                        + " ಮಾರ್ಗಸೂಚಿಯ ರೀತ್ಯಾ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರ ನಡೆಸುವುದಕ್ಕೆ ಅಗತ್ಯವಿರುವ "
                        + "ಸಲಕರಣೆಗಳನ್ನು ಕೆಳಕಂಡಂತೆ ಖರೀದಿಸಿದ್ದು, ಸಹಾಯಧನಕ್ಕಾಗಿ ಪ್ರಸ್ತಾವನೆ ಸಲ್ಲಿಸಿರುತ್ತಾರೆ."
        ));

        // ========= ORDER – "ಆದೇಶ" heading =========
        dto.setHeader12("ಆದೇಶ");

        // sanction order number line (similar to CRC PDF)
        dto.setHeader13(withFourSpaces(
                "ಆದೇಶ ಸಂಖ್ಯೆ : DOS/CRC/" + shortDistrictKannada + "/" + financialYear
                        + "/" + sanctionNo + " / ದಿನಾಂಕ : " + formattedDate
        ));

        // ==== Summary table values: use totalClaimed, totalEligible, totalSubsidy ====
        // (You will show these directly in JRXML fields, not only in text)

        // ORDER main paragraph (similar to last page of CRC PDF)
        String jdInspectionDate = releaseDate; // or formatDate(r.getReleaseDate(), sdf);

// ================== HEADER 16 – YOUR PARAGRAPH ==================
        dto.setHeader16(withFourSpaces(
                "ಉಪಕರಣಗಳ ಖರೀದಿಯನ್ನು ಪರಿಶೀಲಿಸಿದಾಗ, ಈ ಕಚೇರಿಯ ಉಲ್ಲೇಖ (2)ರ ಸುತ್ತೋಲೆಯಲ್ಲಿ ನಮೂದಿಸಿರುವಂತೆ  ಉಪಕರಣಗಳನ್ನು " +
                        "ಖರೀದಿಸಲಾಗಿರುತ್ತದೆ. ಸಹಾಯಧನಕ್ಕಾಗಿ ಅರ್ಹವಿರುವ ಉಪಕರಣಗಳ ಸಂಖ್ಯೆ ಮತ್ತು ಮೌಲ್ಯವನ್ನು ಮೇಲಿನ ಪಟ್ಟಿಯಲ್ಲಿ ನಮೂದಿಸಿದೆ. " +
                        "ಅದರಂತೆ ಸಹಾಯಧನಕ್ಕಾಗಿ ಅರ್ಹವಿರುವ ಬರುವ ಘಟಕದ ಮೊತ್ತವು ರೂ." + totalEligibleAmount + "/- (ಘಟಕದ ದರ) ಆಗಿದ್ದು, " +
                        "ಸದರಿ ಫಲಾನುಭವಿಯು " + categoryName + " ವರ್ಗಕ್ಕೆ ಸೇರಿದ್ದು ಶೇ. " +
                        (centralSharePercentage + stateSharePercentage) + " ರ ಸಹಾಯಧನಕ್ಕೆ ಅರ್ಹತೆ ಹೊಂದಿರುತ್ತಾರೆ. " +
                        "ಅದರಂತೆ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನದ ಮೊತ್ತ ರೂ." + totalSubsidyAmount +
                        "/- (ಸಹಾಯಧನದ ಮೊತ್ತ) ಗಳನ್ನು ಪರಿಗಣಿಸಿದೆ.\n" +
                        "ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು, " + divisionName + " ವಿಭಾಗ ರವರು ದಿನಾಂಕ: " + jdInspectionDate +
                        " ರಂದು ಶ್ರೀ " + farmerName + " ಬಿನ್ " + fatherNameKan + " ರವರು " +
                        districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ " + farmerVillage +
                        " ಗ್ರಾಮದಲ್ಲಿ ಹೊಸದಾಗಿ ಸ್ಥಾಪಿಸಿರುವ ಅಮ್ಮ ನೊಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರದ " +
                        "ಹಿಪ್ಪುನೇರಳೇ ಚಾಕಿ ತೋಟ, ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ಹಾಗೂ ಸಲಕರಣೆಗಳನ್ನು ಪರಿಶೀಲಿಸಿ, ಪರಿವೀಕ್ಷಿಸಿ ಮತ್ತು ದೃಡೀಕರಿಸಿ " +
                        "ಉಲ್ಲೇಖ (4)ರ ಪ್ರಸ್ತಾವನೆಯಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಸ್ಥಾಪನೆ/ ನಿರ್ವಹಣೆ, ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ನಿರ್ಮಾಣ ಮತ್ತು ಸಲಕರಣೆ " +
                        "ಖರೀದಿಗೆ ಶೇ. " + (centralSharePercentage + stateSharePercentage) + " ರಷ್ಟು ಸಹಾಯಧನದ ಒಟ್ಟು ಮೊತ್ತ ರೂ." +
                        totalSubsidyAmount + "/- (ರೂ. " + totalSubsidyWords +
                        " ) ಗಳನ್ನು ಮಂಜೂರು ಮಾಡಲು ಶಿಫಾರಸ್ಸು ಮಾಡಿರುತ್ತಾರೆ. ಉಲ್ಲೇಖ(2)ರ ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆಯಲ್ಲಿ " +
                        "ನೋಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರ ಸ್ಥಾಪನೆಗೆ ನಿಗದಿಪಡಿಸಿರುವ ಮಾನದಂಡಗಳ ಪ್ರಕಾರ ಸಹಾಯಧನ " +
                        "ಮಂಜೂರು ಮಾಡಲು ಅರ್ಹತಾ ಮೊತ್ತವನ್ನು ಪರಿಗಣಿಸಿರುವ ವಿವರ ಕೆಳಕಂಡಂತಿದೆ."
        ));

        // Detailed share break-up paragraph (like CRC PDF last pages)
        dto.setHeader14(withFourSpaces(
                "ಮೇಲನ ವಿಷಯಕ್ಕೆ ಸಂಬಂಧಿಸಿದಂತೆ, ಸಹಾಯಧನದ ಪೈಕಿ ಕೇಂದ್ರದ ಪಾಲಾಗಿ ರೂ." + centralShareAmount
                        + "/- (ರೂ. " + centralShareWords + " ಮಾತ್ರ) ಹಾಗೂ ರಾಜ್ಯದ ಪಾಲಾಗಿ ರೂ." + stateShareAmount
                        + "/- (ರೂ. " + stateShareWords + " ಮಾತ್ರ) ಆಗಿ ಒಟ್ಟು ರೂ." + totalSubsidyAmount
                        + "/- (ರೂ. " + totalSubsidyWords
                        + " ಮಾತ್ರ) ಗಳನ್ನು ಮಂಜೂರು ಮಾಡಲಾಗಿದೆ. ಉಳಿದ ರೂ." + beneficiaryShareAmount
                        + "/- (ರೂ. " + beneficiaryShareWords
                        + " ಮಾತ್ರ) ಗಳನ್ನು ಫಲಾನುಭವಿಯಿಂದಲೇ ಭರಿಸಬೇಕಾಗುತ್ತದೆ. "
                        + "ಕೇಂದ್ರದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿಯು PFMS ಮುಖಾಂತರ ಬಿಡುಗಡೆ ಮಾಡಲಿದ್ದು, "
                        + "ಸಂಬಂಧಿತ ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರು Zero Balance Account ಮುಖಾಂತರ "
                        + "ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಡಿಬಿಟಿ ಮೂಲಕ ಜಮಾ ಮಾಡಬೇಕು. ರಾಜ್ಯದ ಪಾಲಿನ ಸಹಾಯಧನವನ್ನು "
                        + headAccountName + " (" + headDescription
                        + ") ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ ಅಡಿ ಖಜಾನೆ-2 ಮುಖಾಂತರ ಬಿಡುಗಡೆಗೊಂಡ ಅನುದಾನದಿಂದ, "
                        + "ಸಂಬಂಧಿತ ವಿಭಾಗದ ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರ ಕಛೇರಿಯಿಂದ ಡಿಬಿಟಿ ಮೂಲಕ ಫಲಾನುಭವಿಯ "
                        + "ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಬೇಕು."
        ));

        // ========= Signatory =========
        dto.setHeader17(withFourSpaces(
                "ರೇಷ್ಮೆ ಕೃಷಿ ಅಭಿವೃದ್ದಿ ಆಯುಕ್ತರು\n"
                        + "ಹಾಗೂ ರೇಷ್ಮೆ ನಿರ್ದೇಶಕರು."
        ));

        // ========= Recipients (ಪ್ರತಿ) =========
        dto.setHeader18(withFourSpaces(
                "ಇವರಿಗೆ,\n"
                        + "ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು, " + talukName + " ವಿಭಾಗ, " + districtName + " ಜಿಲೆ.\n"
                        + "ಪ್ರತಿಯನ್ನು :\n"
                        + "1. ರೇಷ್ಮೆ ಉಪ ನಿರ್ದೇಶಕರು, ಜಿಲ್ಲಾ ಪಂಚಾಯತ್, " + districtName + " ಜಿಲೆ.\n"
                        + "2. ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು, " + divisionName + " ವಿಭಾಗ.\n"
                        + "3. ತಹಸೀಲ್ದಾರ್, " + talukName + " ತಹಸಿಲ್, " + districtName + " ಜಿಲೆ.\n"
                        + "4. ಶ್ರೀ/ಶ್ರೀಮತಿ " + farmerName + " ಬಿನ್/ಕೋಂ. " + fatherNameKan + ", "
                        + farmerVillage + " ಗ್ರಾಮ, " + talukName + " ತಹಸಿಲ್, " + districtName
                        + " ಜಿಲೆ – ಇವರ ಮಾಹಿತಿಗಾಗಿ."
        ));


        dto.setHeader19(withFourSpaces(
                "ಪೀಠಿಕೆಯಲ್ಲಿ ವಿವರಿಸಿರುವಂತೆ ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು, " + divisionName
                        + " ವಿಭಾಗ ರವರು, ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು, ಜಿಲ್ಲಾ ಪಂಚಾಯತ್ " + districtName
                        + " ಮತ್ತು ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು, " + talukName
                        + " ವಿಭಾಗ ಇವರು ಪರಿಶೀಲಿಸಿ, ಶಿಫಾರಸ್ಸು ಮಾಡಿರುವ ಪ್ರಕಾರ " + districtName
                        + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ " + tscName
                        + " ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ ವ್ಯಾಪ್ತಿಯ " + farmerVillage + " ಗ್ರಾಮದ ಶ್ರೀ/ಶ್ರೀಮತಿ "
                        + farmerName + " (" + fruitsId + ") ಬಿನ್/ಕೋಂ. " + fatherNameKan
                        + " ರವರು, " + Util.objectToString(r.getCrcName())
                        + " ಅಮ್ಮ ನೋಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರ ಪ್ರಾರಂಭಿಸಲು ಹಿಪ್ಪುನೇರಳೆ ತೋಟ ಸ್ಥಾಪನೆ/ನಿರ್ವಹಣೆ, "
                        + "ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ನಿರ್ಮಾಣ ಮತ್ತು ಚಾಕಿ ಸಾಕಾಣಿಕೆ ಸಲಕರಣೆ ಖರೀದಿಗೆ, ಕೇಂದ್ರ ವಲಯ “ಸಿಲ್ಕ್ ಸಮಗ್ರ-2” "
                        + "ಯೋಜನೆ-2 ರಡಿ ಘಟಕ ದರ ರೂ." + totalEligibleAmount + "/- ಗಳಿಗೆ ಶೇ 75/90 ರ ಸಹಾಯಧನ ರೂ."
                        + totalSubsidyAmount + "/- (ರೂ. " + totalSubsidyWords
                        + " ಮಾತ್ರ) ಗಳಿಗೆ ಮಂಜೂರಾತಿ ನೀಡಿದೆ. ಈ ಸಹಾಯಧನದ ಪೈಕಿ ಕೇಂದ್ರದ ಪಾಲಾಗಿ ರೂ."
                        + centralShareAmount + "/- (ರೂ. " + centralShareWords
                        + " ಮಾತ್ರ) ಗಳನ್ನು ರೇಷ್ಮೆ ನಿರ್ದೇಶನಾಲಯದಿಂದ ಕೇಂದ್ರ ರೇಷ್ಮೆ ಮಂಡಳಿ ನೀಡಿರುವ ಮೊತ್ತದಲ್ಲಿ ರೇಷ್ಮೆ ಉಪನಿರ್ದೇಶಕರು "
                        + "ತೆರೆದಿರುವ Zero Balance Account ಗೆ RTGS ಮುಖಾಂತರ ಜಮಾ ಮಾಡಲಾಗುತ್ತದೆ. ಸಂಬಂಧಿಸಿದ ಜಿಲ್ಲೆಯ ರೇಷ್ಮೆ "
                        + "ಉಪನಿರ್ದೇಶಕರು Zero Balance Account ನಿಂದ ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ RTGS ಮುಖಾಂತರ ನೇರವಾಗಿ ಸಹಾಯಧನವನ್ನು "
                        + "ಜಮಾ ಮಾಡಲು ಸೂಚಿಸಿದೆ ಮತ್ತು ರಾಜ್ಯದ ಪಾಲಾಗಿ ರೂ." + stateShareAmount + "/- ( ರೂ. "
                        + stateShareWords + " ಮಾತ್ರ) ಗಳನ್ನು ರಾಜ್ಯ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆಯ ಸಾಮಾನ್ಯ/ವಿಶೇಷ ಘಟಕ ಯೋಜನೆ/"
                        + "ಗಿರಿಜನ ಉಪಯೋಜನೆಯ (" + schemeNameKannada
                        + ") ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ 2851-00-107-00-1-35(106) (422), (423) HOA ವರ್ಗದಡಿ ಖಜಾನೆ-2 ರಲ್ಲಿ ಬಿಡುಗಡೆಗೊಳಿಸಿದ್ದು, "
                        + "ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ ಸಹಾಯಧನವನ್ನು ಸಂಬಂಧಿಸಿದ ವಿಭಾಗದ ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು ಖಜಾನೆ-2ರ ಮುಖಾಂತರ "
                        + "ಫಲಾನುಭವಿಯ ಬ್ಯಾಂಕ್ ಖಾತೆಗೆ ಡಿಬಿಟಿ ಮೂಲಕ ನೇರವಾಗಿ ಜಮಾ ಮಾಡಲು ಸೂಚಿಸಿದೆ.\n\n"

                        + "ರಾಜ್ಯದ ಪಾಲಾಗಿ ರೂ." + stateShareAmount + "/- (ರೂ. " + stateShareWords
                        + " ಮಾತ್ರ) ಗಳನ್ನು ರಾಜ್ಯ ರೇಷ್ಮೆ ಅಭಿವೃದ್ಧಿ ಯೋಜನೆಯಡಿ ಸಾಮಾನ್ಯ/ವಿಶೇಷ ಘಟಕ ಉಪ ಯೋಜನೆ/ಗಿರಿಜನ ಉಪಯೋಜನೆಯ "
                        + "ಲೆಕ್ಕ ಶೀರ್ಷಿಕೆ : 2851-00-107-1-35(106) (ಸಾಮಾನ್ಯ) (422) (ವಿಶೇಷ ಘಟಕ ಉಪ ಯೋಜನೆ), 423 (ಗಿರಿಜನ ಉಪಯೋಜನೆ) "
                        + "HOA ವರ್ಗದಡಿ ಭರಿಸುವುದು.\n\n"

                        + "ಉಪಕರಣಗಳ ಖರೀದಿಯನ್ನು ಪರಿಶೀಲಿಸಿದಾಗ, ಈ ಕಚೇರಿಯ ಉಲ್ಲೇಖ (2)ರ ಸುತ್ತೋಲೆಯಲ್ಲಿ ನಮೂದಿಸಿರುವಂತೆ ಉಪಕರಣಗಳನ್ನು "
                        + "ಖರೀದಿಸಲಾಗಿರುತ್ತದೆ. ಸಹಾಯಧನಕ್ಕಾಗಿ ಅರ್ಹವಿರುವ ಉಪಕರಣಗಳ ಸಂಖ್ಯೆ ಮತ್ತು ಮೌಲ್ಯವನ್ನು ಮೇಲಿನ ಪಟ್ಟಿಯಲ್ಲಿ "
                        + "ನಮೂದಿಸಿದೆ. ಅದರಂತೆ ಸಹಾಯಧನಕ್ಕಾಗಿ ಅರ್ಹವಿರುವ ಬರುವ ಘಟಕದ ಮೊತ್ತವು ರೂ."
                        + totalEligibleAmount + "/- ಆಗಿದ್ದು, ಸದರಿ ಫಲಾನುಭವಿಯು ಸಾಮಾನ್ಯ/ಪರಿಶಿಷ್ಟ ಜಾತಿ/ಪರಿಶಿಷ್ಟ ಪಂಗಡಕ್ಕೆ "
                        + "ಸೇರಿದ್ದು ಶೇ 75/90 ರ ಸಹಾಯಧನಕ್ಕೆ ಅರ್ಹತೆ ಹೊಂದಿರುತ್ತಾರೆ. ಅದರಂತೆ ಸಲಕರಣೆಗಳ ಖರೀದಿಗೆ ಸಹಾಯಧನದ ಮೊತ್ತ "
                        + "ರೂ." + totalSubsidyAmount + "/- ಗಳನ್ನು ಪರಿಗಣಿಸಿದೆ.\n\n"

                        + "ರೇಷ್ಮೆ ಜಂಟಿ ನಿರ್ದೇಶಕರು, " + divisionName + " ವಿಭಾಗ ರವರು ದಿನಾಂಕ: " + proposalDate
                        + " ರಂದು ಶ್ರೀ/ಶ್ರೀಮತಿ " + farmerName + " ಬಿನ್/ಕೋಂ. " + fatherNameKan + " ರವರು "
                        + districtName + " ಜಿಲ್ಲೆಯ " + talukName + " ತಾಲ್ಲೂಕಿನ " + farmerVillage
                        + " ಗ್ರಾಮದಲ್ಲಿ ಹೊಸದಾಗಿ ಸ್ಥಾಪಿಸಿರುವ " + Util.objectToString(r.getCrcName())
                        + " ನೊಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರದ ಹಿಪ್ಪುನೇರಳೆ ಚಾಕಿ ತೋಟ, ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ಹಾಗೂ "
                        + "ಸಲಕರಣೆಗಳನ್ನು ಪರಿಶೀಲಿಸಿ, ಪರಿವೀಕ್ಷಿಸಿ ಮತ್ತು ದೃಡೀಕರಿಸಿ ಉಲ್ಲೇಖ (4)ರ ಪ್ರಸ್ತಾವನೆಯಲ್ಲಿ ಹಿಪ್ಪುನೇರಳೆ ತೋಟ "
                        + "ಸ್ಥಾಪನೆ/ನಿರ್ವಹಣೆ, ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕಟ್ಟಡ ನಿರ್ಮಾಣ ಮತ್ತು ಸಲಕರಣೆ ಖರೀದಿಗೆ ಶೇ 75/90 ರಷ್ಟು ಸಹಾಯಧನದ ಒಟ್ಟು "
                        + "ಮೊತ್ತ ರೂ." + totalSubsidyAmount + "/-ಗಳನ್ನು ಮಂಜೂರು ಮಾಡಲು ಶಿಫಾರಸ್ಸು ಮಾಡಿರುತ್ತಾರೆ. ಉಲ್ಲೇಖ(2)ರ ರೇಷ್ಮೆ "
                        + "ನಿರ್ದೇಶನಾಲಯದ ಸುತ್ತೋಲೆಯಲ್ಲಿ ನೋಂದಾಯಿತ ಖಾಸಗಿ ದ್ವಿತಳಿ ಚಾಕಿ ಸಾಕಾಣಿಕಾ ಕೇಂದ್ರ ಸ್ಥಾಪನೆಗೆ ನಿಗದಿಪಡಿಸಿರುವ "
                        + "ಮಾನದಂಡಗಳ ಪ್ರಕಾರ ಸಹಾಯಧನ ಮಂಜೂರು ಮಾಡಲು ಅರ್ಹತಾ ಮೊತ್ತವನ್ನು ಪರಿಗಣಿಸಿರುವ ವಿವರ ಕೆಳಕಂಡಂತಿದೆ."
        ));


        // Logo
        dto.setLogurl("/reports/Seal_of_Karnataka.PNG");

        // Some basic “identity” fields like you did in RHEquipment
        dto.setFarmerFirstName(" ಶ್ರೀ / ಶ್ರೀಮತಿ " + farmerName);
        dto.setFruitsId(" (ನೋಂದಣಿ ಸಂಖ್ಯೆ : " + fruitsId + ")");
        dto.setDistrictName(districtName + " ಜಿಲ್ಲೆ, ");
        dto.setTalukName(talukName + " ತಾಲ್ಲೂಕು, ");
        dto.setVillageName(farmerVillage + " ಗ್ರಾಮ ");

        // Set numeric fields you may directly show in CRC JRXML tables
        dto.setTotalClaimed(r.getTotalClaimed());
        dto.setTotalEligible(r.getTotalEligible());
        dto.setTotalSubsidy(r.getTotalSubsidy());
        dto.setEstablishmentOfMulberryGardenEligibleAmount(
                r.getEstablishmentOfMulberryGardenEligibleAmount());
        dto.setEstablishmentOfMulberryGardenClaimedAmount(
                r.getEstablishmentOfMulberryGardenClaimedAmount());
        dto.setEstablishmentOfMulberryGardenPercentageOfSubsidyAmount(
                r.getEstablishmentOfMulberryGardenPercentageOfSubsidyAmount());

        dto.setInstallationOfDripIrrigationEligibleAmount(
                r.getInstallationOfDripIrrigationEligibleAmount());
        dto.setInstallationOfDripIrrigationClaimedAmount(
                r.getInstallationOfDripIrrigationClaimedAmount());
        dto.setInstallationOfDripIrrigationPercentageOfSubsidyAmount(
                r.getInstallationOfDripIrrigationPercentageOfSubsidyAmount());

        dto.setChawkiRearingBuildingEligibleAmount(
                r.getChawkiRearingBuildingEligibleAmount());
        dto.setChawkiRearingBuildingClaimedAmount(
                r.getChawkiRearingBuildingClaimedAmount());
        dto.setChawkiRearingBuildingPercentageOfSubsidyAmount(
                r.getChawkiRearingBuildingPercentageOfSubsidyAmount());

        dto.setEquipmentEligibleTotal(r.getEquipmentEligibleTotal());
        dto.setEquipmentPurchasedTotal(r.getEquipmentPurchasedTotal());
        dto.setEquipmentPercentageTotal(r.getEquipmentPercentageTotal());

        // shares & sanction amounts
        dto.setCentralSanctionAmount(r.getCentralSanctionAmount());
        dto.setStateSanctionAmount(r.getStateSanctionAmount());
        dto.setCentralSharePercentage(r.getCentralSharePercentage());
        dto.setStateSharePercentage(r.getStateSharePercentage());

        list.add(dto);
        return new JRBeanCollectionDataSource(list);
    }





    private JRBeanCollectionDataSource getDataSourceForMscSeedChawki1000(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromMscCommercialMarket(requestDto);
        if (apiResponse == null || apiResponse.getContent() == null || apiResponse.getContent().isEmpty()) {
            return new JRBeanCollectionDataSource(new ArrayList<>());
        }

        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        Float totalDfl = 0f;
        Float sanctionAmountTotal = 0f;

        SanctionOrderResponse first = apiResponse.getContent().get(0);
        Long amount = first.getAmount();
        if (amount == null) {
            amount = 0L;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String currentDate = formatDate(apiResponse.getContent().get(0).getCurrentDate(), sdf);
        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

        // Date for sanction order number line
        String formattedDate;
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);
        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString();
        }



        // Other simple fields
        response.setAcceptedDate("ಸ್ವೀಕೃತಿ ಪತ್ರದ ದಿನಾಂಕ : " + apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
        response.setAddressText(apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName(apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName(apiResponse.getContent().get(0).getTalukName());
        response.setHobliName(apiResponse.getContent().get(0).getHobliName());
        response.setVillageName(apiResponse.getContent().get(0).getVillageName());
        response.setFruitsId(apiResponse.getContent().get(0).getFruitsId());
        response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");


        if (apiResponse.getContent() != null) {
            sanctionOrderResponseList.add(response);

            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {

                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName(
                            "" + sanctionOrderResponse.getFruitsId()
                    );
                }
                if (sanctionOrderResponse.getVillageName() == null) {
                    sanctionOrderResponse.setVillageName("");
                }
                if (sanctionOrderResponse.getFruitsId() == null) {
                    sanctionOrderResponse.setFruitsId("");
                }
                if (sanctionOrderResponse.getFatherNameKan() == null) {
                    sanctionOrderResponse.setFatherNameKan("");
                }
                if (sanctionOrderResponse.getChawkiPercentage() == null) {
                    sanctionOrderResponse.setChawkiPercentage(0f);
                }
                if (sanctionOrderResponse.getCrcName() == null) {
                    sanctionOrderResponse.setCrcName("");
                }
                if (sanctionOrderResponse.getLotNo() == null) {
                    sanctionOrderResponse.setLotNo("");
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }
                if (sanctionOrderResponse.getUnitCost() == null) {
                    sanctionOrderResponse.setUnitCost(0f);
                }
                if (sanctionOrderResponse.getSubsidyAmount() == null) {
                    sanctionOrderResponse.setSubsidyAmount(0f);
                }
                sanctionAmountTotal += sanctionOrderResponse.getSanctionAmount();
                if (sanctionOrderResponse.getNoOfDfls() == null
                        || sanctionOrderResponse.getNoOfDfls().trim().isEmpty()
                        || "null".equalsIgnoreCase(sanctionOrderResponse.getNoOfDfls())) {
                    sanctionOrderResponse.setNoOfDfls("0");
                }

                // ---------- String → Float ----------
                Float dfls = 0f;
                try {
                    dfls = Float.parseFloat(sanctionOrderResponse.getNoOfDfls().trim());
                } catch (Exception e) {
                    dfls = 0f;
                }

                totalDfl += dfls;
                sanctionOrderResponse.setTotalDfl(totalDfl);


                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
            String sanctionAmountTotalInWords =
                KannadaNumberUtil.convertNumberToKannadaWords(
                        sanctionAmountTotal.longValue()
                );

        response.setTotalSanctionAmount(sanctionAmountTotal);
        response.setTotalDfl(totalDfl);
        response.setTotalSanctionAmountInWords(sanctionAmountTotalInWords);

        String raceName = apiResponse.getContent().get(0).getRaceName();
        String raceNameWithoutFirstWord = removeFirstWord(raceName);

        response.setHeader2(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "   ಯೋಜನೆಯಡಿ     ದ್ಧಿತಳಿ    ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ   ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ     ಸಹಾಯಧನ    ಮಂಜೂರಾತಿ    ನೀಡುವ    ಬಗ್ಗೆ.");


        response.setHeader4("                 " + apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ     ಇಲಾಖೆಯ     ವಿವಿಧ     ಕಾರ್ಯಕ್ರಮಗಳ     ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ     ವಿವಿಧ     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆಗಳಡಿ   "
                        + "ಉಲ್ಲೇಖ(1) ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮಕ     ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು,    ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + ""
                        + "   ಯೋಜನೆಯಡಿ     "+raceNameWithoutFirstWord+"    ಮೊಟ್ಟೆ ಗಳಿಗೆ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚ ಕ್ಕೆ     ಸಹಾಯಧನ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ಮಾರ್ಗಸೂಚಿಯನ್ನು     ನೀಡಲಾಗಿದೆ.    ಸದರಿ    ಕಾರ್ಯಕ್ರ ಮದಡಿ    "
                        +"ಅಂತರರಾಷ್ಟ್ರೀ ಯ     ಗುಣಮಟ್ಟ ದ     ದ್ವಿ ತಳಿ    ರೇಷ್ಮೆ ಗೆ      ಹೆಚ್ಚಿನ     ಬೇಡಿಕೆ    ಇದ್ದು ,   "+raceNameWithoutFirstWord+"    ಸಾಕಾಣಿಕೆಗೆ     ಚಾಕಿ    ಹಂತದಲ್ಲಿ     ಶೈ ತ್ಯಾಂಶ   ಮತ್ತು      ಉಷ್ಣಾಂಶ,     ಉತ್ತ ಮ    ಗುಣಮಟ್ಟ ದ    "
                       +"ಹಿಪ್ಪು ನೇರಳೆ    ಸೊಪ್ಪು   ನೀಡಿ,   ಸೋಂಕುರಹಿತ    ವಾತಾವರಣ    ಒದಗಿಸುವುದು    ಅತ್ಯಾ ವಶ್ಯ ಕವಾಗಿರುತ್ತ ದೆ.   ಇದರಿಂದ   ಮುಂದಿನ    ಹಂತಗಳಲ್ಲಿ      ಯಶಸ್ವಿ ಯಾಗಿ     ಬೆಳೆ   ಹಣ್ಣು     ಮಾಡಿ,    ಉತ್ತ ಮ   "
                       +" ಗುಣಮಟ್ಟ ದ    ಗೂಡು    ಉತ್ಪಾ ದನೆ     ಮಾಡಬಹುದಾಗಿದೆ.    ಈ   ಹಿನ್ನೆ ಲೆಯಲ್ಲಿ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆಯನ್ನು    ಪ್ರೋತ್ಸಾ ಹಿಸುವ    ನಿಟ್ಟಿ ನಲ್ಲಿ    ನೋಂದಾಯಿತ   ದ್ವಿ ತಳಿ    ಚಾಕಿ    ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರಗಳು    "
                +"ಚಾಕಿ   ಮಾಡಿ   ವಿತರಿಸುವ   ಪ್ರತಿ   100   "+raceNameWithoutFirstWord+"   ಮೊಟ್ಟೆ ಗಳಿಗೆ   ರೂ. " + Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/- ಗಳನ್ನು     ರೈತರಿಗೆ    ನೀಡುವ    ಕಾರ್ಯಕ್ರ ಮವಿರುತ್ತದೆ.\n "
                        + "                 ಉಲ್ಲೇಖ(5) ರಂತೆ   ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ  ಸೇವಾ   ಕೇಂದ್ರ   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ಇವರು   ಪರಿಶೀಲಿಸಿ   "
                +"ದೃಢೀಕರಿಸಿ   ಸಲ್ಲಿಸಿದ    ಎಲ್ಲಾ    ಅಗತ್ಯ    ದಾಖಲಾತಿಗಳನ್ನು    ಒಳಗೊಂಡ   ಪ್ರಸ್ತಾವನೆಯನ್ನು    ಸಲ್ಲಿಸಿದ್ದು    ವಿವರಗಳು   ಈ   ಕೆಳಕಂಡಂತಿದೆ.");
        response.setHeader6("                 ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ     ಮೇಲ್ಕಂಡ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು     ನೋಂದಾಯಿತ    "+raceNameWithoutFirstWord+"    ಚಾಕಿ    ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರಗಳಲ್ಲಿ     ಪಡೆದ  " + totalDfl + "   "
                +"   "+raceNameWithoutFirstWord+"    ರೇಷ್ಮೆ     ಮೊಟ್ಟೆಗಳಿಗೆ    ಪ್ರತಿ   100    ಮೊಟ್ಟೆಗಳಿಗೆ    ಸಹಾಯಧನದ   ಘಟಕ   ದರ    ರೂ. " + Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/- ರಂತೆ,    ಒಟ್ಟು    ರೂ.   "
                + Math.round(sanctionAmountTotal) + " /- ಗಳನ್ನು      ಪಡೆಯಲು    ಅರ್ಹರಿರುತ್ತಾರೆ.    ಉಲ್ಲೇಖ(3)ರ     ಸರ್ಕಾರದ    ಆದೇಶದ   ರೀತ್ಯಾ    ಈ    ಕಛೇರಿಯ    ಅಧಿಕಾರ    ಪ್ರತ್ಯಾಯೋಜನೆ    ವ್ಯಾಪ್ತಿಯಲ್ಲಿದ್ದು,     ಉಲ್ಲೇಖ(4) ರಲ್ಲಿ     "
                        +"ಸದರಿ     ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಠಾನಕ್ಕಾಗಿ    ನೀಡಿರುವ    ಮಾರ್ಗಸೂಚಿಯನ್ವಯ    ಸಹಾಯಧನ    ಮಂಜೂರು   ಮಾಡಲು   ಅನುದಾನ   ಬಿಡುಗಡೆ   ಮಾಡಲಾಗಿದೆ.   ಅದರಂತೆ   ಈ   ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ   ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");


        response.setHeader8("                 ಪೀ ಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ     ಅಂಶಗಳನ್ನು     ಪರಶೀ ಲಿಸಲಾಗಿ,      " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ತಾಲ್ಲೂ ಕಿನ    ತಾಂತ್ರಿ ಕ    ಸೇವಾ    ಕೇಂದ್ರ   " +
                apiResponse.getContent().get(0).getLoggedinUserTscName() + "    ವ್ಯಾ ಪ್ತಿಯ     "+raceNameWithoutFirstWord+"    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು     ಅನುಬಂಧದಲ್ಲಿ     ತೋರಿಸಿರುವಂತೆ    "+raceNameWithoutFirstWord+"    ಚಾಕಿ    ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರ ಗಳಿಂದ    ಖರೀದಿಸಿರುವ   " + totalDfl + "   "
                +"ಮೊಟ್ಟೆಗಳಿಗೆ    ಪ್ರತಿ   100   ಮೊಟ್ಟೆಗೆ     ಸಹಾಯಧನದ   ಘಟಕ    ದರ   ರೂ.  " + Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/-   ಗಳಂತೆ   ಒಟ್ಟು     ರೂ.  " + Math.round(sanctionAmountTotal) + "/-  ( ರೂಪಾಯಿ   " + sanctionAmountTotalInWords + " )  "
                + "  ಗಳನ್ನು     ಮಂಜೂರು    ಮಾಡಿದೆ.     " + apiResponse.getContent().get(0).getSchemeNameInKannada() + "  ಯೋಜನೆಯ   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ:   " + apiResponse.getContent().get(0).getScHeadAccountName() +
                "  (" + apiResponse.getContent().get(0).getDescription() + ") ರಡಿ    ಖಜಾನೆ-2  ರಲ್ಲಿ     ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ  ಸಹಾಯಧನದ   ಅನದಾನದಲ್ಲಿ     ಡಿಬಿಟಿ    ಮುಖಾಂತರ    ಫಲಾನುಭವಿ    ಖಾತೆಗೆ    ನೇರವಾಗಿ    ಜಮಾ    ಮಾಡುವುದು.\n"
                + "                   ಸದರಿ   ವೆಚ್ಚ ವನ್ನು    ಲೆಕ್ಕ    ಶೀ ರ್ಷಿಕೆ:  " + apiResponse.getContent().get(0).getScHeadAccountName() + "  (" + apiResponse.getContent().get(0).getDescription() + ")   ಅಡಿ    ಭರಿಸುವುದು.");


        if (sanctionAmountTotal <= amount) {
            response.setHeader(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು  ,     "
                            + apiResponse.getContent().get(0).getDivisionName()
                            + "     ವಿಭಾಗ,     "
                            + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                            + "     ಇವರ     ಕಛೇರಿಯ      ನಡವಳಿಗಳು"
            );

            response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                    + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                    + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                    + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                    + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                    + "5)     ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ಇವರ   ಪ್ರಸ್ತಾವನೆ   ದಿನಾಂಕ  :  " + proposalDate);


            response.setHeader10(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು, \n"
                            + apiResponse.getContent().get(0).getDivisionName() + "   ವಿಭಾಗ,   " + apiResponse.getContent().get(0).getLoggedinUserDistrictName());


            response.setHeader9(
                    "ಈ     ಕಚೇರಿಯ     ಲೆಕ್ಕ     ಶಾಖೆಗೆ     ಮುಂದಿನ     ಕ್ರಮಕ್ಕಾಗಿ.\n "
                            + "ಪ್ರತಿಯನ್ನು     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು  /  ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ, \n" +
                            apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ರವರಿಗೆ     ಮಾಹಿತಿಗಾಗಿ ");

            response.setHeader7(
                    "ಸಂಖ್ಯೆ : ರೇಸನಿ /ಮೈ  ಬಿಪ್ರ /" + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "/ತಾಂ/ ರೇ ಅಯೋ  /ಮೈ ತಳಿ/ಚಾಸೇ ಶು/ಮಂ/" + apiResponse.getContent().get(0).getSanctionOrderNumber() + "  ದಿನಾಂಕ:  " + formattedDate);


        }
        else

        {


            response.setHeader("ರೇಷ್ಮೆ     ಉಪ    ನಿರ್ದೇಶಕರು,    ಜಿಲ್ಲಾ      ಪಂಚಾಯತ್,   " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ಇವರ     ಕಛೇರಿಯ      ನಡವಳಿಗಳು");

            response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                    + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                    + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                    + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                    + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                    + "5)     ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,     " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ರವರ     ಜ್ಞಾಪನಪತ್ರ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSReleaseNo() + " ,   ದಿನಾಂಕ  :  " + sReleaseDate);


            response.setHeader10(
                    "ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,\n" +
                            "ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,\n"
                            + apiResponse.getContent().get(0).getLoggedinUserDistrictName());


            response.setHeader9(
                    "ಇವರಿಗೆ,\n" +
                            "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,\n" +
                            apiResponse.getContent().get(0).getDivisionName() + "    ವಿಭಾಗ,  " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "\n" +
                            "ಪ್ರತಿಯನ್ನು    \n" +
                            "ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು/ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ , " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "\n" +
                            "ರವರಿಗೆ    ಮಾಹಿತಿಗಾಗಿ");

            response.setHeader7(
                    "ಸಂಖ್ಯೆ  : ರೇಉನಿ /ಮೈ  ಬಿಪ್ರ /" + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "/ತಾಂ/ ರೇ ಅಯೋ  /ಮೈ ತಳಿ/ಚಾಸೇ ಶು/ಮಂ/" + apiResponse.getContent().get(0).getSanctionOrderNumber() + "  ದಿನಾಂಕ:  " + formattedDate);
        }

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }

    private JRBeanCollectionDataSource getDataSourceForMscSeedChawki(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchSanctionSeedMarketDetails(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        // 🔹 CHANGED: Compute total subsidyAmountCa (∑ noOfDfls * subsidyAmount / 100) and totalNoOfDfls
        float totalSubsidyAmountCa = 0f;     // CHANGED: new accumulator
        int   totalNoOfDfls        = 0;      // CHANGED: new accumulator
        Float sanctionAmountTotal = 0f;

        Long amount = apiResponse.getContent().get(0).getAmount();
        if (amount == null) {
            amount = 0L;
        }

        if (apiResponse.getContent() != null) {
            for (SanctionOrderResponse r : apiResponse.getContent()) {

                // 1) Parse noOfDfls safely
                int noOfDfls = 0;
                try {
                    if (r.getNoOfDfls() != null) {
                        noOfDfls = Integer.parseInt(r.getNoOfDfls().trim());
                    }
                } catch (Exception e) {
                    noOfDfls = 0;
                }
                totalNoOfDfls += noOfDfls;   // CHANGED: accumulate total DFLs

                // 2) Take subsidyAmount (amount per 100 DFLs)
                Float subsidyPer100 = r.getSubsidyAmount();
                if (subsidyPer100 == null) subsidyPer100 = 0f;

                // 3) Compute row amount = noOfDfls * subsidyAmount / 100
                float rowAmount = subsidyPer100 * (noOfDfls / 100f);
                totalSubsidyAmountCa += rowAmount;   // CHANGED: accumulate total subsidy
            }
        }

        // 🔹 CHANGED: Put computed totals into header bean
        response.setTotalSchemeAmount(totalSubsidyAmountCa);          // CHANGED: use computed sum
        response.setTotalNoOfDfls((float) totalNoOfDfls);             // CHANGED: pass Float, not String

        String amountInWords =
                KannadaNumberUtil.convertNumberToKannadaWords((long) totalSubsidyAmountCa); // CHANGED: words from computed total
        response.setSanctionAmount75InWords(amountInWords);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String currentDate        = formatDate(apiResponse.getContent().get(0).getCurrentDate(), sdf);
        String admGovtDate        = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate   = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String releaseDate        = formatDate(apiResponse.getContent().get(0).getReleaseDate(), sdf);
        String sReleaseDate       = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);
        String proposalDate       = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);

        // Date for sanction order number line
        String formattedDate;
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString();
            SimpleDateFormat inputFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);
        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString();
        }
        if (totalSubsidyAmountCa <= amount) {
        response.setHeader(apiResponse.getContent().get(0).getDesignationName() + ",     " + apiResponse.getContent().get(0).getDesignationNameForSanctionOrder() + "     ವಿಭಾಗ,     "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "     ಇವರ     ಕಛೇರಿ     ನಡವಳಿಗಳು");

        response.setHeader2(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ    " + apiResponse.getContent().get(0).getSchemeNameInKannada() +"   ಯೋಜನೆ  (  "+ apiResponse.getContent().get(0).getScCategoryName()+ "  )  ಯಡಿ     "+
                        apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"    ಸಹಾಯಧನ     ಮಂಜೂರಾತಿ       ನೀಡುವ      ಬಗ್ಗೆ. ");

        response.setHeader3("1. ರೇಷ್ಮೆ    ಕೃಷಿ     ಅಭಿವೃದ್ದಿ      ಆಯುಕ್ತರು    ಹಾಗೂ   ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು   ರವರ   ಸುತ್ತೋಲೆ   ಸಂಖ್ಯೆ   :\n" +
                            "    "+apiResponse.getContent().get(0).getSchemeCircularNo() + "  ದಿನಾಂಕ :  " + schemeCircularDate + " \n"+
                "2. ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,   ಬಿತ್ತನೆ   ವಲಯ,   "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ಇವರ   ಪತ್ರದ    ಸಂಖ್ಯೆ  :  \n" +
                "    "+apiResponse.getContent().get(0).getSReleaseNo() +",    ದಿನಾಂಕ : "+sReleaseDate+" \n" +
                "3. ರೇಷ್ಮೆ   ವಿಸ್ತ ರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName()+ "   ಇವರ    ಪ್ರಸ್ತಾವನೆ   ದಿನಾಂಕ: "+proposalDate+"\n" +
                "4. ಸರ್ಕಾರದ    ಆದೇಶ   ಸಂಖ್ಯೆ  : "+ apiResponse.getContent().get(0).getDeptDeleNo()+"   ದಿನಾಂಕ:   "+deptDeleDate+".");


        response.setHeader4("                 "+apiResponse.getContent().get(0).getFinancialYear()+"   ನೇ    ಸಾಲಿನಲ್ಲಿ      "+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"   ಯೋಜನೆ  (  "+ apiResponse.getContent().get(0).getScCategoryName()+ " )  ಯಡಿ     "+
                "    ಪ್ರತಿ      100      ಶುದ್ದ     ಮೈಸೂರು   ತಳಿ    ಮೊಟ್ಟೆಗಳ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆಗೆ    ತಗಲಬಹುದಾದ    ಒಟ್ಟು       ವೆಚ್ಚ      ರೂ."+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/- ಗಳಿಗೆ   " +
                "  ಶೇ.50 ರಂತೆ    ರೂ."+ Math.round(apiResponse.getContent().get(0).getSubsidyAmount()) +"/- ಗಳನ್ನು      ಸಹಾಯಧನವಾಗಿ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಟಾನಕ್ಕಾಗಿ     ಉಲ್ಲೇಖ (1)ರಲ್ಲಿ      ಇಲಾಖೆಯಿಂದ     ಮಾರ್ಗಸೂಚಿಯನ್ನು      ನೀಡಲಾಗಿರುತ್ತದೆ. \n" +
                "                 ಉಲ್ಲೇಖ (2)ರಲ್ಲಿ       ಸದರಿ      ಕಾರ್ಯಕ್ರಮ     ಅನುಷ್ಟಾನಗೊಳಿಸಲು     ಅನುದಾನ     ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿರುತ್ತದೆ.     ಉಲ್ಲೇಖ (3)ರಲ್ಲಿ      ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,     ತಾಂತ್ರಿಕ     ಸೇವಾ    ಕೇಂದ್ರ,   " +
                " "+apiResponse.getContent().get(0).getLoggedinUserTscName()+"     ಇವರು     ಸಲ್ಲಿಸಿರುವ     ಪ್ರಸ್ತಾವನೆಯನ್ನು      "+
                "   ಪರಿಶೀಲಿಸಲಾಗಿ     ನೋಂದಾಯಿತ     ಶುದ್ದ     ಮೈಸೂರು   ತಳಿ    ಚಾಕಿ     ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರ ಗಳಿಂದ   ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಪಡೆದ " +
                "  ಚಾಕಿ   ಹುಳುಗಳಿಗೆ   ಚಾಕಿ    ಸಾಕಾಣಿಕಾ    ವೆಚ್ಚದ    ಸಹಾಯಧನಕ್ಕಾಗಿ   ಅರ್ಹರಿರುವ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರ     ವಿವರಗಳು    ಈ    ಕೆಳಕಂಡಂತಿವೆ:");

        response.setHeader6("                 ಉಲ್ಲೇಖ (4)ರ    ಆರ್ಥಿಕ     ಅಧಿಕಾರ    ಪ್ರತ್ಯಾಯೋಜನೆ    ಅನ್ವಯ    ಮೇಲ್ಕಂಡ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರಿಗೆ   " +
                " ಚಾಕಿ     ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ    ಸಹಾಯಧನವನ್ನು     ಮಂಜೂರು    ಮಾಡಬಹುದಾಗಿದ್ದು,   ಈ   ಕೆಳಕಂಡಂತೆ    ಮಂಜೂರಾತಿ   ಆದೇಶವನ್ನು    ಹೊರಡಿಸಿದೆ.");

        response.setHeader8("            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ      ಅಂಶಗಳನ್ನು      ಪರಿಶೀಲಿಸಲಾಗಿ,    ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ   "+apiResponse.getContent().get(0).getLoggedinUserTscName()+"    ವ್ಯಾಪ್ತಿಯ    ರೇಷ್ಮೆ     "+
                "ಬೆಳೆಗಾರರು    ಪಡೆದ    "+totalNoOfDfls+"    ಶುದ್ದ     ಮೈಸೂರು     ತಳಿ    ರೇಷ್ಮೆ   ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ    ಸಹಾಯಧನ   ಪ್ರತಿ   100   ಮೊಟ್ಟೆಗಳಿಗೆ    ರೂ."+ Math.round(apiResponse.getContent().get(0).getSubsidyAmount()) +"/-   ರಂತೆ   "+
                " ಒಟ್ಟು     ರೂ.  "+totalSubsidyAmountCa+"  (ರೂ. "+amountInWords+"   ) ಗಳನ್ನು     ಮಂಜೂರು    ಮಾಡಿದೆ.    ಸಹಾಯಧನದ   ಮೊತ್ತವನ್ನು     ಖಜಾನೆ-2/ಡಿಬಿಟಿ    ಮುಖಾಂತರ      " +
                "ಫಲಾನುಭವಿ   ಬ್ಯಾಂಕ್   ಖಾತೆಗೆ    ನೇರವಾಗಿ    ಜಮಾ    ಮಾಡುವುದು.  \n" +
                "              ಸದರಿ    ವೆಚ್ಚವನ್ನು     ರೇಷ್ಮೆ    ಅಭಿವೃದ್ಧಿ      ಯೋಜನೆಯ ("+apiResponse.getContent().get(0).getScCategoryName()+"  )   ಲೆಕ್ಕ     " +
                "  ಶೀರ್ಷಿಕೆ : "+apiResponse.getContent().get(0).getScHeadAccountName()+" ("+apiResponse.getContent().get(0).getDescription()+" )  ಅಡಿ    ಭರಿಸುವುದು.");


        response.setStatus("Approved By "
                + apiResponse.getContent().get(0).getUser());

        response.setHeader7("S.O.No.SDP/GEN/PM/CRC/SD1/2025-26, Date:09/06/2025\n" +
                "ಆದೇಶ ಸಂಖ್ಯೆ:ರೇಅಯೋ/ಸಾ/ಮೈ ತಳಿ/ಚಾಸಾವೆ/ಸಧನ/ಮಂ/S.O.No. SD1/2025-26    ದಿನಾಂಕ:09/06/2025  ");

        response.setHeader11("S.O.No.SDP/GEN/PM/CRC/SD1/2025-26, Date:09/06/2025");

        response.setHeader9(
                "ಈ     ಕಚೇರಿಯ     ಲೆಕ್ಕ     ಶಾಖೆಗೆ     ಮುಂದಿನ     ಕ್ರಮಕ್ಕಾಗಿ. \n"
                        + "ಪ್ರತಿಯನ್ನು    : ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,  ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ,   "+ apiResponse.getContent().get(0).getLoggedinUserTscName()+"   ಇವರಿಗೆ    ಮಾಹಿತಿಗಾಗಿ");

        response.setHeader10(apiResponse.getContent().get(0).getDesignationName() + ",\n"+
                        apiResponse.getContent().get(0).getDesignationNameForSanctionOrder()   +"   ವಿಭಾಗ,   "+ apiResponse.getContent().get(0).getLoggedinUserTalukName());

        }else {

            response.setHeader(apiResponse.getContent().get(0).getDesignationName() + ",     " + apiResponse.getContent().get(0).getDesignationNameForSanctionOrder() + " ,    "
                    + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "     ಇವರ     ಕಛೇರಿ     ನಡವಳಿಗಳು");

            response.setHeader2(
                    apiResponse.getContent().get(0).getFinancialYear()
                            + "     ನೇ     ಸಾಲಿನಲ್ಲಿ    " + apiResponse.getContent().get(0).getSchemeNameInKannada() +"   ಯೋಜನೆ  (  "+ apiResponse.getContent().get(0).getScCategoryName()+ "  )  ಯಡಿ     "+
                            apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"    ಸಹಾಯಧನ     ಮಂಜೂರಾತಿ       ನೀಡುವ      ಬಗ್ಗೆ. ");

            response.setHeader3("1. ರೇಷ್ಮೆ    ಕೃಷಿ     ಅಭಿವೃದ್ದಿ      ಆಯುಕ್ತರು    ಹಾಗೂ   ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು   ರವರ   ಸುತ್ತೋಲೆ   ಸಂಖ್ಯೆ   :\n" +
                    "    "+apiResponse.getContent().get(0).getSchemeCircularNo() + "  ದಿನಾಂಕ :  " + schemeCircularDate + " \n"+
                    "2. ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,   ಮೈಸೂರು   ಬಿತ್ತನೆ   ವಲಯ,   "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ಇವರ   ಪತ್ರದ    ಸಂಖ್ಯೆ  : \n" +
                    "    "+apiResponse.getContent().get(0).getSReleaseNo() +",    ದಿನಾಂಕ : "+sReleaseDate+" \n" +
                    "3. "+apiResponse.getContent().get(0).getDesignationName() + ",   " + apiResponse.getContent().get(0).getDesignationNameForSanctionOrder() + "     ವಿಭಾಗ,    "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ಇವರ    ಪ್ರಸ್ತಾವನೆ   ದಿನಾಂಕ: "+proposalDate+"\n" +
                    "4. ಸರ್ಕಾರದ    ಆದೇಶ   ಸಂಖ್ಯೆ  : "+ apiResponse.getContent().get(0).getDeptDeleNo()+"   ದಿನಾಂಕ:   "+deptDeleDate+".");


            response.setHeader4("                 "+apiResponse.getContent().get(0).getFinancialYear()+"   ನೇ    ಸಾಲಿನಲ್ಲಿ      "+ apiResponse.getContent().get(0).getSchemeNameInKannada() +"   ಯೋಜನೆ  (  "+ apiResponse.getContent().get(0).getScCategoryName()+ " )  ಯಡಿ     "+
                    "    ಪ್ರತಿ      100      ಶುದ್ದ     ಮೈಸೂರು   ತಳಿ    ಮೊಟ್ಟೆಗಳ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆಗೆ    ತಗಲಬಹುದಾದ    ಒಟ್ಟು       ವೆಚ್ಚ      ರೂ."+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/- ಗಳಿಗೆ   " +
                    "  ಶೇ.50 ರಂತೆ    ರೂ."+ Math.round(apiResponse.getContent().get(0).getSubsidyAmount()) +"/- ಗಳನ್ನು      ಸಹಾಯಧನವಾಗಿ    ನೀಡುವ    ಕಾರ್ಯಕ್ರಮದ    ಅನುಷ್ಟಾನಕ್ಕಾಗಿ     ಉಲ್ಲೇಖ (1)ರಲ್ಲಿ      ಇಲಾಖೆಯಿಂದ     ಮಾರ್ಗಸೂಚಿಯನ್ನು      ನೀಡಲಾಗಿರುತ್ತದೆ. \n" +
                    "                 ಉಲ್ಲೇಖ (2)ರಲ್ಲಿ       ಸದರಿ      ಕಾರ್ಯಕ್ರಮ     ಅನುಷ್ಟಾನಗೊಳಿಸಲು     ಅನುದಾನ     ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿರುತ್ತದೆ.     ಉಲ್ಲೇಖ (3)ರಲ್ಲಿ      "+apiResponse.getContent().get(0).getDesignationName() + ",     " +
                     apiResponse.getContent().get(0).getDesignationNameForSanctionOrder() + "     ವಿಭಾಗ,    "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() + "    ಇವರು     ಸಲ್ಲಿಸಿರುವ     ಪ್ರಸ್ತಾವನೆಯನ್ನು      "+
                    "    ಪರಿಶೀಲಿಸಲಾಗಿ      ನೋಂದಾಯಿತ     ಶುದ್ದ     ಮೈಸೂರು   ತಳಿ    ಚಾಕಿ     ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರ ಗಳಿಂದ   ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಪಡೆದ " +
                    "    ಚಾಕಿ   ಹುಳುಗಳಿಗೆ   ಚಾಕಿ    ಸಾಕಾಣಿಕಾ    ವೆಚ್ಚದ    ಸಹಾಯಧನಕ್ಕಾಗಿ   ಅರ್ಹರಿರುವ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರ     ವಿವರಗಳು    ಈ    ಕೆಳಕಂಡಂತಿವೆ:");

            response.setHeader6("                 ಉಲ್ಲೇಖ (4)ರ    ಆರ್ಥಿಕ     ಅಧಿಕಾರ    ಪ್ರತ್ಯಾಯೋಜನೆ    ಅನ್ವಯ    ಮೇಲ್ಕಂಡ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರಿಗೆ   " +
                    " ಚಾಕಿ     ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ    ಸಹಾಯಧನವನ್ನು     ಮಂಜೂರು    ಮಾಡಬಹುದಾಗಿದ್ದು,   ಈ   ಕೆಳಕಂಡಂತೆ    ಮಂಜೂರಾತಿ   ಆದೇಶವನ್ನು    ಹೊರಡಿಸಿದೆ.");

            response.setHeader8("            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ      ಅಂಶಗಳನ್ನು      ಪರಿಶೀಲಿಸಲಾಗಿ,    ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ   "+apiResponse.getContent().get(0).getLoggedinUserTscName()+"    ವ್ಯಾಪ್ತಿಯ    ರೇಷ್ಮೆ     "+
                    "ಬೆಳೆಗಾರರು    ಪಡೆದ    "+totalNoOfDfls+"    ಶುದ್ದ     ಮೈಸೂರು     ತಳಿ    ರೇಷ್ಮೆ   ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ    ಸಾಕಾಣಿಕೆ    ವೆಚ್ಚದ    ಸಹಾಯಧನ   ಪ್ರತಿ   100   ಮೊಟ್ಟೆಗಳಿಗೆ    ರೂ."+ Math.round(apiResponse.getContent().get(0).getSubsidyAmount()) +"/-   ರಂತೆ   "+
                    " ಒಟ್ಟು     ರೂ.  "+totalSubsidyAmountCa+"  (ರೂ. "+amountInWords+"   ) ಗಳನ್ನು     ಮಂಜೂರು    ಮಾಡಿದೆ.    ಸಹಾಯಧನದ   ಮೊತ್ತವನ್ನು     ಖಜಾನೆ-2/ಡಿಬಿಟಿ    ಮುಖಾಂತರ      " +
                    "ಫಲಾನುಭವಿ   ಬ್ಯಾಂಕ್   ಖಾತೆಗೆ    ನೇರವಾಗಿ    ಜಮಾ    ಮಾಡುವುದು.  \n" +
                    "              ಸದರಿ    ವೆಚ್ಚವನ್ನು     ರೇಷ್ಮೆ    ಅಭಿವೃದ್ಧಿ      ಯೋಜನೆಯ ("+apiResponse.getContent().get(0).getScCategoryName()+"  )   ಲೆಕ್ಕ     " +
                    "  ಶೀರ್ಷಿಕೆ : "+apiResponse.getContent().get(0).getScHeadAccountName()+" ("+apiResponse.getContent().get(0).getDescription()+" )  ಅಡಿ    ಭರಿಸುವುದು.");


            response.setStatus("Approved By "
                    + apiResponse.getContent().get(0).getUser());

            response.setHeader7("S.O.No.SDP/GEN/PM/CRC/SD1/2025-26, Date:09/06/2025\n" +
                    "ಆದೇಶ ಸಂಖ್ಯೆ:ರೇಅಯೋ/ಸಾ/ಮೈ ತಳಿ/ಚಾಸಾವೆ/ಸಧನ/ಮಂ/S.O.No. SD1/2025-26    ದಿನಾಂಕ:09/06/2025  ");

            response.setHeader11("S.O.No.SDP/GEN/PM/CRC/SD1/2025-26, Date:09/06/2025");

            response.setHeader9(
                    "ಈ     ಕಚೇರಿಯ     ಲೆಕ್ಕ     ಶಾಖೆಗೆ     ಮುಂದಿನ     ಕ್ರಮಕ್ಕಾಗಿ. \n"
                            + "ಪ್ರತಿಯನ್ನು       "+apiResponse.getContent().get(0).getDesignationName() + " ,     " + apiResponse.getContent().get(0).getDesignationNameForSanctionOrder() +"    ವಿಭಾಗ,    "+ apiResponse.getContent().get(0).getLoggedinUserTalukName());

            response.setHeader10(apiResponse.getContent().get(0).getDesignationName() + ",\n"+
                    "ಮೈಸೂರು    ಬಿತ್ತನೆ    ವಲಯ,   "+ apiResponse.getContent().get(0).getLoggedinUserTalukName());

        }


// ಉಲ್ಲೇಖ – points with 5-space gaps
//        response.setHeader3(
//                "1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getAdmGovtOrder()
//                        + "     ದಿನಾಂಕ  :     " + admGovtDate + " \n"
//                        + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
//                        + "         "+apiResponse.getContent().get(0).getSchemeCircularNo() + "     ದಿನಾಂಕ  :     " + schemeCircularDate + " \n"
//                        + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo()
//                        + "     ದಿನಾಂಕ  :     " + deptDeleDate + " \n"
//                        + "4)     " + apiResponse.getContent().get(0).getLoggedinUserTalukName()
//                        + "     ರೇಷ್ಮೆ     ಉಪ     ನಿರ್ದೇಶಕರು  /  ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್‌     ರವರ     ಜ್ಞಾಪನ     ಪತ್ರದ     ಸಂಖ್ಯೆ  :  \n"
//                        + "       "+apiResponse.getContent().get(0).getSReleaseNo() + "     ದಿನಾಂಕ  :     " + sReleaseDate + " \n"
//                        + "5)     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "     ಇವರ     ಪ್ರಸ್ತಾವನೆ     ದಿನಾಂಕ  :     " + proposalDate
//        );

//        response.setHeader3(
////                "1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getAdmGovtOrder()+ "     ದಿನಾಂಕ  :     " + admGovtDate + " \n"+
//                "1)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
//                        + "         "+apiResponse.getContent().get(0).getSchemeCircularNo() + "     ದಿನಾಂಕ  :     " + schemeCircularDate + " \n"
//                        + "2)     ರೇಷ್ಮೆ  ಉಪ   ನಿರ್ದೇಶಕರು,    ಬಿತ್ತನೆ ವಲಯ, "
//                        + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo()
//                        + "     ದಿನಾಂಕ  :     " + deptDeleDate + " \n"
//                        + "4)     " + apiResponse.getContent().get(0).getLoggedinUserTalukName()
//                        + "     ರೇಷ್ಮೆ     ಉಪ     ನಿರ್ದೇಶಕರು  /  ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್‌     ರವರ     ಜ್ಞಾಪನ     ಪತ್ರದ     ಸಂಖ್ಯೆ  :  \n"
//                        + "       "+apiResponse.getContent().get(0).getSReleaseNo() + "     ದಿನಾಂಕ  :     " + sReleaseDate + " \n"
//                        + "5)     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "     ಇವರ     ಪ್ರಸ್ತಾವನೆ     ದಿನಾಂಕ  :     " + proposalDate);

//        response.setHeader4(
//                             "                 "+apiResponse.getContent().get(0).getFinancialYear()
//                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ     ಇಲಾಖೆಯ     ವಿವಿಧ     ಕಾರ್ಯಕ್ರಮಗಳ     ಅನುಷ್ಠಾನಕ್ಕೆ     ವಿವಿಧ     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆಗಳಡಿ  "
//                        + "ಉಲ್ಲೇಖ     (1)     ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮಕ     ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು,    ಉಲ್ಲೇಖ     (2)   ರಲ್ಲಿ   "+ apiResponse.getContent().get(0).getSchemeNameInKannada()+""
//                        +"ಯೋಜನೆಯಡಿ     ಶುದ್ಧ     ಮೈಸೂರು     ತಳಿ     ಮೊಟ್ಟೆಗಳಿಗೆ    ಚಾಕಿ   ಸಾಕಾಣಿಕೆ     ವೆಚ್ಚಕ್ಕೆ     ಸಹಾಯಧನ   ನೀಡುವ   ಕಾರ್ಯಕ್ರಮದ"
//                        + "ಅನುಷ್ಠಾನಕ್ಕಾಗಿ     ಮಾರ್ಗಸೂಚಿಯನ್ನು     ನೀಡಲಾಗಿದೆ.    ಸದರಿ     ಕಾರ್ಯಕ್ರಮದಡಿ   ವಾಣಿಜ್ಯ     ರೇಷ್ಮೆ     ಮೊಟ್ಟೆ     ಉತ್ಪಾದನೆಯಲ್ಲಿ     ಶುದ್ಧ "
//                        +"ಮೈಸೂರು     ತಳಿ     ಹಾಗೂ  "+apiResponse.getContent().get(0).getRaceName()+"   ರೇಷ್ಮೆ     ಗೂಡುಗಳ     ಉತ್ಪಾದನೆ     ಬಹಳ     ಪ್ರಮುಖವಾದ  ಅಂಶವಾಗಿರುತ್ತದೆ.  ಮಿಶ್ರತಳಿ  ಹಾಗೂ "
//                        + apiResponse.getContent().get(0).getRaceName()+"    ಸಂಕರಣ  /  ಶುದ್ಧ     ತಳಿ     ಮೊಟ್ಟೆ     ಉತ್ಪಾದಿಸಲು     ನಿರಂತರವಾಗಿ     ಗುಣಮಟ್ಟದ     ಶುದ್ಧ     ಮೈಸೂರು     ತಳಿ     ಹಾಗೂ"
//                        +apiResponse.getContent().get(0).getRaceName()+"     ರೇಷ್ಮೆ     ಬಿತ್ತನೆ     ಗೂಡುಗಳ     ಅವಶ್ಯಕತೆಯಿರುತ್ತದೆ.    ಈ     ಅತ್ಯಾವಶ್ಯಕ     ಬೇಡಿಕೆಯನ್ನು     ಪೂರೈಸಲು   "+apiResponse.getContent().get(0).getRaceName()+"   ಮತ್ತು "
//                        + "ಮೈಸೂರು     ಬಿತ್ತನೆ     ವಲಯಗಳಲ್ಲಿ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರನ್ನು     ಪ್ರೋತ್ಸಾಹಿಸಲು     ಅನೇಕ     ಫಲಾನುಭವಿ     ಆಧಾರಿತ     ಕಾರ್ಯಕ್ರಮಗಳ"
//                        +"ಅನುಷ್ಠಾನದ     ಜೊತೆಗೆ     ತಾಂತ್ರಿಕ     ಮಾಹಿತಿಯನ್ನು     ಒದಗಿಸಲಾಗುತ್ತಿದೆ.\n"
//                        + "                 ಬಿತ್ತನೆ     ಗೂಡಿಗೆ     ಕೊರತೆಯಾಗದಂತೆ     ವೈಜ್ಞಾನಿಕವಾಗಿ     ಮೊಟ್ಟೆ     ಉತ್ಪಾದನಾ     ಕಾರ್ಯಕ್ರಮವನ್ನು     ಹಮ್ಮಿಕೊಳ್ಳಲಾಗಿದೆ. "
//                        + "ಮೈಸೂರು  ಬಿತ್ತನ    ಪ್ರದೇಶದಲ್ಲಿ     ಚಾಕಿ   ಸಾಕಾಣಿಕೆಯನ್ನು     ಪ್ರೋತ್ಸಾಹಿಸುವ   ದೃಷ್ಟಿಯಿಂದ   ಈಗಾಗಲೇ   "+apiResponse.getContent().get(0).getRaceName()+"  ಮೊಟ್ಟೆಗಳಿಗೆ "
//                        +"ನೀಡುತ್ತಿರುವಂತೆ     ಖಾಸಗಿ   ಚಾಕಿ     ಸಾಕಾಣಿಕಾ   ಕೇಂದ್ರಗಳಲ್ಲಿ     ಪ್ರತಿ   100   ಮೊಟ್ಟೆಗಳಿಗೆ     ತಗಲಬಹುದಾದ  ಒಟ್ಟು     ವೆಚ್ಚ "
//                        +"ರೂ."+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/-ಗಳಲ್ಲಿ   ಶೇ.50  ರಂತೆ   ರೂ."+ apiResponse.getContent().get(0).getSubsidyAmount() +"/-ಗಳ   ಚಾಕಿ  ಸೇವಾ   ಶುಲ್ಕವನ್ನು    ನೀಡುವ   ಕಾರ್ಯಕ್ರಮವಿರುತ್ತದೆ.\n"
//                        + "                 ಉಲ್ಲೇಖ (5)    ರಂತೆ   ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ  ಸೇವಾ   ಕೇಂದ್ರ   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "  ಇವರು  ಪರಿಶೀಲಿಸಿ  ದೃಢೀಕರಿಸಿ  ಎಲ್ಲಾ     ಅಗತ್ಯ  "
//                        +"ದಾಖಲಾತಿಗಳನ್ನು     ಒಳಗೊಂಡ     ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಸಲ್ಲಿಸಿದ್ದು     ವಿವರಗಳು  ಸಲ್ಲಿಸಿದ   ಈ   ಕೆಳಕಂಡಂತಿವೆ. ");


//        response.setHeader6(
//                "                 ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ     ಮೇಲ್ಕಂಡ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರು     ನೋಂದಾಯಿತ     ಶುದ್ಧ     ಮೈಸೂರು     ತಳಿ     ಚಾಕಿ     ಸಾಕಾಣಿಕಾ "
//                +"ಕೇಂದ್ರಗಳಿಂದ     ಪಡೆದ   "+ totalNoOfDfls +"   ಶುದ್ಧ     ಮೈಸೂರು     ತಳಿ     ರೇಷ್ಮೆ     ಮೊಟ್ಟೆಗಳಿಗೆ,     ಪ್ರತಿ    100  ಮೊಟ್ಟೆಗಳಿಗೆ   ತಗಲಬಹುದಾದ     ಒಟ್ಟು "
//                +"ವೆಚ್ಚ     ರೂ."+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/-ಗಳ   ಚಾಕಿ   ಸೇವಾ   ಶುಲ್ಕ,   ಒಟ್ಟು   ರೂ. "+ apiResponse.getContent().get(0).getSubsidyAmount() +" /- ಗಳನ್ನು    ಚಾಕಿ   ಸೇವಾ   ಶುಲ್ಕ,   ಒಟ್ಟು   ರೂ. " + totalSubsidyAmountCa+" /- ಗಳನ್ನು     ಪಡೆಯಲು  "
//                +"ಅರ್ಹರಾಗಿರುತ್ತಾರೆ.    ಉಲ್ಲೇಖ (3) ರ     ಸರ್ಕಾರದ   ಆದೇಶದ   ರೀತ್ಯಾ    ಈ     ಕಛೇರಿಯ  ಅಧಿಕಾರ  ಪ್ರತ್ಯಾಯೋಜನೆ   ವ್ಯಾಪ್ತಿಯಲ್ಲಿದ್ದು,     ಉಲ್ಲೇಖ (4)   ರಲ್ಲಿ "
//                +"ಸದರಿ   ಕಾರ್ಯಕ್ರಮದ   ಅನುಷ್ಠಾನಕ್ಕಾಗಿ   ನೀಡಿರುವ   ಮಾರ್ಗಸೂಚಿಯನ್ವಯ    ಸೇವಾಶುಲ್ಕ     ಮಂಜೂರು     ಮಾಡಲು     ಅನುದಾನ     ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿದೆ."
//                +"ಅದರಂತೆ     ಈ     ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ   ಆದೇಶ   ಹೊರಡಿಸಿದೆ.");

// ಮಂಜೂರಾತಿ ಆದೇಶ ಸಂಖ್ಯೆ line – short, fine

//        response.setHeader7(
//                "ಸಂಖ್ಯೆ :ರೇಉನಿ/ಮೈ  ಬಿಪ್ರ /" + apiResponse.getContent().get(0).getLoggedinUserTalukName()+"/ತಾಂ/ ರೇ ಅಯೋ  /ಮೈ ತಳಿ/ಚಾಸೇ ಶು/ಮಂ/" + apiResponse.getContent().get(0).getSanctionOrderNumber() +"  ದಿನಾಂಕ:  " + formattedDate);



//        response.setHeader8("                 ಪೀ ಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ     ಅಂ ಶಗಳನ್ನು     ಪರಶೀ ಲಿಸಲಾಗಿ,      "+ apiResponse.getContent().get(0).getLoggedinUserTalukName() + "   ತಾಲ್ಲೂ ಕಿನ    ತಾಂತ್ರಿ ಕ    ಸೇ ವಾ    ಕೇಂ ದ್ರ    " +
//                 apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ವ್ಯಾ ಪ್ತಿ ಯ   ಶುದ್ದ     ತಳಿ     ರೇ ಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಅನುಬಂಧದಲ್ಲಿ     ತೋ ರಿಸಿರುವಂತೆ     ಚಾಕಿ     ಸಾಕಾಣಿಕಾ    ಕೇಂದ್ರ ಗಳಿಂದ"
//                +"ಖರೀ ದಿಸಿರುವ   "+ totalNoOfDfls  +"   ಶುದ್ದ    ಮೈ ಸೂರು    ತಳಿ    ರೇ ಷ್ಮೆ     ಮೊಟ್ಟೆ ಗಳಿಗೆ    ಪ್ರ ತಿ    100    ಮೊಟ್ಟೆ ಗಳಿಗೆ    ತಗಲಬಹುದಾದ    ಒಟ್ಟು    ವೆಚ್ಚ  "
//                +"ರೂ."+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/-   ಗಳಲ್ಲಿ    ಶೇ.50ರಂತೆ   ರೂ."+ apiResponse.getContent().get(0).getSubsidyAmount() +"/- ಗಳ     ಚಾಕಿ    ಸೇ ವಾ    ಶುಲ್ಕ    ಒಟ್ಟು    ರೂ. "+ totalSubsidyAmountCa +"/-  ("+amountInWords+" )"
//                +"ಮಾಡಿದೆ.   "+apiResponse.getContent().get(0).getSchemeNameInKannada() + "  ಯೋಜನೆಯ   ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ:   "+ apiResponse.getContent().get(0).getScHeadAccountName() +"  ("+ apiResponse.getContent().get(0).getDescription()+ ")  ರಡಿ   ಖಜಾನೆ-2"
//                +"ರಲ್ಲಿ      ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ    ಅನದಾನದಲ್ಲಿ     ಡಿಬಿಟಿ   ಮುಖಾಂತರ   ಫಲಾನುಭವಿ   ಖಾತೆಗೆ   ನೇರವಾಗಿ   ಜಮಾ   ಮಾಡುವುದು.\n"
//               +"                 ಸದರಿ   ವೆಚ್ಚ ವನ್ನು   ಲೆಕ್ಕ    ಶೀ ರ್ಷಿಕೆ:  "+ apiResponse.getContent().get(0).getScHeadAccountName() +"  ("+ apiResponse.getContent().get(0).getDescription()+ ") ಅಡಿ ಭರಿಸುವುದು.");






//        response.setSanctionOrderNumber(
//                "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು, \n"
//                        + apiResponse.getContent().get(0).getLoggedinUserDistrictName()   +"   ವಿಭಾಗ,   "+ apiResponse.getContent().get(0).getDivisionName());





        // Other simple fields
        response.setAcceptedDate("ಸ್ವೀಕೃತಿ ಪತ್ರದ ದಿನಾಂಕ : " + apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
        response.setAddressText(apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName(apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName(apiResponse.getContent().get(0).getTalukName());
        response.setHobliName(apiResponse.getContent().get(0).getHobliName());
        response.setVillageName(apiResponse.getContent().get(0).getVillageName());
        response.setFruitsId(apiResponse.getContent().get(0).getFruitsId());
        response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");



        if (apiResponse.getContent() != null) {
            sanctionOrderResponseList.add(response);

            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {

                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName(
                            "" + sanctionOrderResponse.getFruitsId()
                    );
                }
                if (sanctionOrderResponse.getVillageName() == null) {
                    sanctionOrderResponse.setVillageName("");
                }
                if (sanctionOrderResponse.getFruitsId() == null) {
                    sanctionOrderResponse.setFruitsId("");
                }
                if (sanctionOrderResponse.getFatherNameKan() == null) {
                    sanctionOrderResponse.setFatherNameKan("");
                }
                if (sanctionOrderResponse.getCrcName() == null) {
                    sanctionOrderResponse.setCrcName("");
                }

                if (sanctionOrderResponse.getExternalUserOrganisationName() == null) {
                    sanctionOrderResponse.setExternalUserOrganisationName("");
                }
                if (sanctionOrderResponse.getExternalUserAddress() == null) {
                    sanctionOrderResponse.setExternalUserAddress("");
                }
                if (sanctionOrderResponse.getLotWeight() == null) {
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getExternalUserLicenseNumber() == null) {
                    sanctionOrderResponse.setExternalUserLicenseNumber("");
                }
                if (sanctionOrderResponse.getChawkiReceiptNo() == null) {
                    sanctionOrderResponse.setChawkiReceiptNo("");
                }
                if (sanctionOrderResponse.getCrcBillNo() == null) {
                    sanctionOrderResponse.setCrcBillNo("");
                }
                if (sanctionOrderResponse.getMarketName() == null) {
                    sanctionOrderResponse.setMarketName("");
                }
                if (sanctionOrderResponse.getLotWeight() == null) {
                    sanctionOrderResponse.setLotWeight(0f);
                }
                if (sanctionOrderResponse.getCdcmBiddingSlipNo() == null) {
                    sanctionOrderResponse.setCdcmBiddingSlipNo("");
                }
                if (sanctionOrderResponse.getCdcmTransactionDate() == null) {
                    sanctionOrderResponse.setCdcmTransactionDate("");
                }
                if (sanctionOrderResponse.getArn() == null) {
                    sanctionOrderResponse.setArn("");
                }
//                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
//                    sanctionOrderResponse.setTotalSchemeAmount(0f);
//                }
//                if (sanctionOrderResponse.getSchemeAmount() == null) {
//                    sanctionOrderResponse.setSchemeAmount(0f);
//                }
                if (sanctionOrderResponse.getLotNo() == null) {
                    sanctionOrderResponse.setLotNo("");
                }


                if (sanctionOrderResponse.getSubsidyAmount() == null) {
                    sanctionOrderResponse.setSubsidyAmount(0f);
                }

                // 1) Parse noOfDfls for this row
                int noOfDflsRow = 0;
                try {
                    if (sanctionOrderResponse.getNoOfDfls() != null) {
                        noOfDflsRow = Integer.parseInt(sanctionOrderResponse.getNoOfDfls().trim());
                    }
                } catch (Exception e) {
                    noOfDflsRow = 0;
                }

                // 2) Per-100 DFL rate
                Float subsidyPer100Row = sanctionOrderResponse.getSubsidyAmount();
                if (subsidyPer100Row == null) subsidyPer100Row = 0f;

                // 3) Per-row calculation → 50 * 30 / 100 = 15
                float subsidyAmountCaRow = (noOfDflsRow * subsidyPer100Row) / 100f;
                sanctionOrderResponse.setSubsidyAmountCa(subsidyAmountCaRow);

                // 4) Put the **already computed grand totals** on every row
                sanctionOrderResponse.setTotalNoOfDfls((float) totalNoOfDfls);
                sanctionOrderResponse.setTotalSubsidyAmountCa(totalSubsidyAmountCa);

                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
        response.setTotalSchemeAmount(totalSubsidyAmountCa);
        response.setTotalNoOfDfls((float) totalNoOfDfls);
        response.setTotalSubsidyAmountCa(totalSubsidyAmountCa);

        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }


    private JRBeanCollectionDataSource getDataSourceForTransportSubsidy(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromMscCommercialMarket(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();


        // 🔹 NEW: totals
        float totalTransportAmount = 0f;
        Float sanctionAmountTotal = 0f;
        float totalCocoonsWeight = 0f;

        Long amount = apiResponse.getContent().get(0).getAmount();
        if (amount == null) {
            amount = 0L;
        }


// CHANGED: split units vs actual kg
        float totalQtyUnits        = 0f;  // per-100kg units, used only for calculation
        float totalQtyKg           = 0f;  // NEW: actual kg for display (50, 100, …)

        int   totalNoOfDfls        = 0;




        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String admGovtDate        = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate       = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate   = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String proposalDate       = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate       = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);


        String formattedDate;
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString();
            SimpleDateFormat in  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = out.format(in.parse(inputDate));
        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString();
        }

        Float totalAmount = null;
        long totalLong = 0L;
        String amountInWords = "";

// Null-safety for apiResponse + content + element + amount
        if (apiResponse != null
                && apiResponse.getContent() != null
                && !apiResponse.getContent().isEmpty()
                && apiResponse.getContent().get(0) != null
                && apiResponse.getContent().get(0).getTotalSchemeAmount() != null) {

            totalAmount = apiResponse.getContent().get(0).getTotalSchemeAmount();
            totalLong = totalAmount.longValue();
            amountInWords = KannadaNumberUtil.convertNumberToKannadaWords(totalLong);
        }

// Now you can safely use totalAmount, totalLong, amountInWords
// even if totalSchemeAmount was null


        Float totalKgs   = apiResponse.getContent().get(0).getGrandTotalQuantityOfCocoonsProduced(); // 270
        Float unitCost   = apiResponse.getContent().get(0).getPerKgRate();          // 10

        // ===== HEADERS =====

        // Header (office)




            // ಪೀಠಿಕೆ – from page 2 of Transport PDF, compacted but same meaning :contentReference[oaicite:3]{index=3}
        response.setHeader4(
                "                 "+apiResponse.getContent().get(0).getFinancialYear()
                        + "    ನೇ     ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ     ಇಲಾಖೆಯ     ವಿವಿಧ   ಕಾರ್ಯಕ್ರಮಗಳ     ಅನುಷ್ಟಾನಕ್ಕಾಗಿ     ವಿವಿಧ   ಲೆಕ್ಕ   "
                        +"ಶೀರ್ಷಿಕೆಗಳಡಿ     ಉಲ್ಲೇಖ (1)ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮಕ     ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು,  ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ    "
                        + ""+apiResponse.getContent().get(0).getSchemeNameInKannada() +"    ಯೋಜನೆಯ   ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ  :  " + apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription()+ ")   ರಡಿ    " +
                        "ಉತ್ತರ     ಕರ್ನಾಟಕದ     ಜಿಲ್ಲೆಗಳಲ್ಲಿ     "+apiResponse.getContent().get(0).getRaceName()+"    ರೇಷ್ಮೆ    ಗೂಡು   ಉತ್ಪಾ ದನೆಗೆ     ಪ್ರಾ ಮುಖ್ಯ ತೆ      ನೀಡುವ    ಸಲುವಾಗಿ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    "+
                        "  ತಾವು   ಉತ್ಪಾ ದಿಸುವ    "+apiResponse.getContent().get(0).getRaceName()+"   ರೇಷ್ಮೆ   ಗೂಡನ್ನು    ರಾಜ್ಯ ದ    "+
                        "ಯಾವುದೇ    ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ    ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ ಗಳಲ್ಲಿ     ಮಾರಾಟ    ಮಾಡಲು   ಸಾಗಾಣಿಕೆ    ಮಾಡುವ    ಪ್ರತಿ    ಕೆ.ಜಿ     "+apiResponse.getContent().get(0).getRaceName()+"    ರೇಷ್ಮೆ     ಗೂಡಿಗೆ  "
                        +"  ರೂ. "+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/-   ರಂತೆ    "+apiResponse.getContent().get(0).getSubSchemeNameInKannada() +"'     ನೀಡುವ    ಕಾರ್ಯಕ್ರಮದ      ಅನುಷ್ಟಾನಕ್ಕಾಗಿ    "
                        +"    ಮಾರ್ಗಸೂಚಿಯನ್ನು     ನೀಡಲಾಗಿರುತ್ತದೆ.\n\n"
                        + "                 ಉಲ್ಲೇಖ     (5)     ರಂತೆ     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ,     " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "  ಇವರು     ಪರಿಶೀಲಿಸಿ    "
                        + "    ದೃಢೀಕರಿಸಿ     ಸಲ್ಲಿಸಿರುವ     ಎಲ್ಲಾ     ಅಗತ್ಯ     ದಾಖಲಾತಿಗಳನ್ನು     ಒಳಗೊಂಡ     ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಸಲ್ಲಿಸಿದ್ದು,     ವಿವರಗಳು "
                        +"   ಈ     ಕೆಳಕಂಡಂತಿವೆ. " );







        // Other simple fields
        response.setAcceptedDate("ಸ್ವೀಕೃತಿ ಪತ್ರದ ದಿನಾಂಕ : " + apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
        response.setAddressText(apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName(apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName(apiResponse.getContent().get(0).getTalukName());
        response.setHobliName(apiResponse.getContent().get(0).getHobliName());
        response.setVillageName(apiResponse.getContent().get(0).getVillageName());
        response.setFruitsId(apiResponse.getContent().get(0).getFruitsId());
        response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");

        // ====== DETAIL ROWS ======
        if (apiResponse.getContent() != null) {
            sanctionOrderResponseList.add(response);

            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {

                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName(
                            "" + sanctionOrderResponse.getFruitsId()
                    );
                }
                if (sanctionOrderResponse.getVillageName() == null) {
                    sanctionOrderResponse.setVillageName("");
                }
                if (sanctionOrderResponse.getChawkiPercentage() == null) {
                    sanctionOrderResponse.setChawkiPercentage(0f);
                }
                if (sanctionOrderResponse.getFruitsId() == null) {
                    sanctionOrderResponse.setFruitsId("");
                }
                if (sanctionOrderResponse.getFatherNameKan() == null) {
                    sanctionOrderResponse.setFatherNameKan("");
                }
                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName("");
                }

                if (sanctionOrderResponse.getGrainageMasterName() == null) {
                    sanctionOrderResponse.setGrainageMasterName("");
                }
                if (sanctionOrderResponse.getNoOfDfls() == null) {
                    sanctionOrderResponse.setNoOfDfls("");
                }
                if (sanctionOrderResponse.getRaceName() == null) {
                    sanctionOrderResponse.setRaceName("");
                }
                if (sanctionOrderResponse.getCdcmBiddingSlipNo() == null) {
                    sanctionOrderResponse.setCdcmBiddingSlipNo(
                            "" + sanctionOrderResponse.getCdcmTransactionDate()
                    );
                }
                if (sanctionOrderResponse.getLotNo() == null) {
                    sanctionOrderResponse.setLotNo("");
                }
                if (sanctionOrderResponse.getQuantityOfCocoonsProduced() == null) {
                    sanctionOrderResponse.setQuantityOfCocoonsProduced(0f);
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }
                sanctionAmountTotal += sanctionOrderResponse.getSanctionAmount();

                Float cocoonsWeight = sanctionOrderResponse.getCocoonsWeight();
                if (cocoonsWeight == null) {
                    cocoonsWeight = 0f;
                }
                float cocoonsWeightFormatted =
                        Float.parseFloat(String.format("%.3f", cocoonsWeight));

                sanctionOrderResponse.setCocoonsWeight(cocoonsWeightFormatted);
                totalCocoonsWeight += cocoonsWeightFormatted;

                if (sanctionOrderResponse.getTotalQuantityOfCocoonsProduced() == null) {
                    sanctionOrderResponse.setTotalQuantityOfCocoonsProduced(0f);
                }
                if (sanctionOrderResponse.getUnitCost() == null) {
                    sanctionOrderResponse.setUnitCost(0f);
                }

                int noOfDflsRow = 0;
                try {
                    if (sanctionOrderResponse.getNoOfDfls() != null) {
                        noOfDflsRow = Integer.parseInt(sanctionOrderResponse.getNoOfDfls().trim());
                    }
                } catch (Exception e) {
                    noOfDflsRow = 0;
                }
                totalNoOfDfls += noOfDflsRow;

                // 🔹 SAFE values in kg
                Float qtyKg = sanctionOrderResponse.getQuantityOfCocoonsProduced();
                if (qtyKg == null) qtyKg = 0f;

                Float unitCostRow = sanctionOrderResponse.getUnitCost();
                if (unitCostRow == null) unitCostRow = 0f;

                // 🔹 scheme is per 100 kg → convert 50 kg → 0.5 units (for calc only)
                float schemeAmountRow = qtyKg * unitCostRow;
//                float schemeAmountRow = qtyUnits * unitCostRow;  // correct amount

// CHANGED: keep ACTUAL KG (50, 100, …) for display in JRXML
                sanctionOrderResponse.setQuantityOfCocoonsProduced(qtyKg);


                // put on row
                sanctionOrderResponse.setSchemeAmounts(schemeAmountRow);
                sanctionOrderResponse.setTransportAmount(schemeAmountRow);

                totalTransportAmount += schemeAmountRow;
                totalQtyKg           += qtyKg;
                sanctionOrderResponse.setTotalTransportAmount(totalTransportAmount);
                sanctionOrderResponse.setGrandTotalNoOfDfls((float) totalNoOfDfls);

// CHANGED: footer & header should show ACTUAL KG, not units
                sanctionOrderResponse.setTotalQuantityOfCocoonsProduced(totalQtyKg);


                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }
        }
        String sanctionAmountTotalInWords =
                KannadaNumberUtil.convertNumberToKannadaWords(
                        sanctionAmountTotal.longValue()
                );

        response.setTotalSanctionAmount(sanctionAmountTotal);
        response.setTotalSanctionAmountInWords(sanctionAmountTotalInWords);

        response.setTotalTransportAmount(totalTransportAmount);
        response.setGrandTotalNoOfDfls((float) totalNoOfDfls);

        response.setGrandTotalQuantityOfCocoonsProduced(totalQtyKg);
        response.setTotalQuantityOfCocoonsProduced(totalQtyKg); // NEW: for $F{totalQuantityOfCocoonsProduced}

        String totalQtyInWords =
                KannadaNumberUtil.convertNumberToKannadaWords((long) totalQtyKg);

        String totalTransportInWords =
                KannadaNumberUtil.convertNumberToKannadaWords((long) totalTransportAmount);

        String totalCocoonsWeightFormatted =
                String.format("%.3f", totalCocoonsWeight);


        response.setTotalQuantityOfCocoonsProducedInWords(totalQtyInWords);
        response.setTotalTransportAmountInWords(totalTransportInWords);
        response.setHeader7(
                "                 ಪೀಠಿಕೆಯಲ್ಲೆ     ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ     ಅಂಶಗಳನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ,     " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "     ತಾಲ್ಲೂಕಿನ     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ     " + apiResponse.getContent().get(0).getLoggedinUserTscName()+"    "
                        + "ವ್ಯಾಪ್ತಿಯ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರಿಗೆ,     ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ     ವಹಿವಾಟು     ಮಾಡಿದ     ಒಟ್ಟು     " +  totalCocoonsWeightFormatted    + "   ಕೆ.ಜಿ    "+apiResponse.getContent().get(0).getRaceName()+"     ರೇಷ್ಮೆ   "
                        +"ಗೂಡಿಗೆ     ಪ್ರತಿ     ಕೆ.ಜಿ.ಗೆ     ರೂ. " +  Math.round(apiResponse.getContent().get(0).getUnitCost()) +  "/-     ರಂತೆ   "+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+ "  ರೂ. " +   Math.round(response.getTotalSanctionAmount())      + "/-     (ರೂಪಾಯಿ     " + response.getTotalSanctionAmountInWords()   + " )     "
                        +"ಗಳನ್ನು     ಮಂಜೂರು   ಮಾಡಲಾಗಿದೆ.   ರೇಷ್ಮೆ   ಅಭಿವೃದ್ಧಿ   ಯೋಜನೆಯ  ಲೆಕ್ಕ    ಶೀರ್ಷಿಕೆ   " + apiResponse.getContent().get(0).getScHeadAccountName() + "   ಅಡಿ   ಖಜಾನೆ-2   ರಲ್ಲಿ     ಬಿಡುಗಡೆಗೊಂಡಿರುವ    " +
                        "ಅನುದಾನದಲ್ಲಿ    ಡಿ.ಬಿ.ಟಿ     ಮುಖಾಂತರ     ಫಲಾನುಭವಿಗಳ     ಖಾತೆಗೆ     ನೇರವಾಗಿ     ಜಮಾ     ಮಾಡಬೇಕು. \n"
                        + "                 ಸದರಿ     ವೆಚ್ಚವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ   : " + apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription()+ ")  ಅಡಿ  ಭರಿಸುವುದು.");

        response.setHeader5(
                "                 ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ     "+apiResponse.getContent().get(0).getFinancialYear()+"  ನೇ   ಸಾಲಿನ    "+apiResponse.getContent().get(0).getSchemeNameInKannada()+"    ಯೋಜನೆಯ    ಲೆಕ್ಕ       "
                +"  ಶೀ ರ್ಷಿಕೆ    :   " + apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription()+ ")   ಅಡಿ    ಮೇಲ್ಕಂಡ     ರೇಷ್ಮೆ     ಬೆಳೆಗಾರರು     ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     "
                +"ಗೂಡಿನ   ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ     ವಹಿವಾಟು   ಮಾಡಿದ   " +  totalCocoonsWeightFormatted + "   ಕೆ.ಜಿ    "+apiResponse.getContent().get(0).getRaceName()+"     ರೇಷ್ಮೆ     ಗೂಡಿಗೆ,     ಪ್ರತಿ     ಕೆ.ಜಿ.ಗೆ     ರೂ. " +  Math.round(apiResponse.getContent().get(0).getUnitCost()) +  "/-  ರಂತೆ  "+apiResponse.getContent().get(0).getSubSchemeNameInKannada()+"   ರೂ. " + Math.round(response.getTotalSanctionAmount()) + "/-     ಗಳನ್ನು     ಪಡೆಯಲು   "
                        + "   ಅರ್ಹರಾಗಿದ್ದಾರೆ.    ಉಲ್ಲೇಖ(3)ರ     ಸರ್ಕಾರದ     ಆದೇಶದ     ರೀತ್ಯಾ     ಈ     ಕಛೇರಿಯ     ಅಧಿಕಾರ     ಪ್ರತ್ಯಾಯೋಜನೆ     ವ್ಯಾಪ್ತಿಯಲ್ಲಿದ್ದು,   "
                        +"ಉಲ್ಲೇಖ(4)ರಲ್ಲಿ       ಸದರಿ     ಕಾರ್ಯಕ್ರಮದ     ಅನುಷ್ಟಾ ನಕ್ಕಾ ಗಿ     ನೀಡಿರುವ     ಮಾರ್ಗಸೂಚಿಯನ್ವಯ     ಸಹಾಯಧನ     ಮಂಜೂರು     ಮಾಡಲು     "
                +"ಅನುದಾನ     ಬಿಡುಗಡೆ     ಮಾಡಲಾಗಿದೆ.      ಅದರಂತೆ     ಈ     ಕೆಳಕಂಡ     ಮಂಜೂರಾತಿ     ಆದೇಶ    ಹೊರಡಿಸಿದೆ.");


        response.setHeader2(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     "+apiResponse.getContent().get(0).getSchemeNameInKannada() +"  ಯೋಜನೆಯಡಿ   ಉತ್ತರ    ಕರ್ನಾಟಕದ   ಜಿಲ್ಲೆ ಗಳಲ್ಲಿ     ರೇಷ್ಮೆ   ಬೆಳೆಗಾರರು     ಉತ್ಪಾದಿಸಿದ   "+apiResponse.getContent().get(0).getRaceName()+""
                        +"     ರೇಷ್ಮೆ    ಗೂಡನ್ನು      ರಾಜ್ಯದ    ಯಾವುದೇ    ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ   ಗೂಡಿನ   ಮಾರುಕಟ್ಟೆ ಗಳಲ್ಲಿ      ಮಾರಾಟ     ಮಾಡಲು " +
                        "  ಸಾಗಾಣಿಕೆ    ಮಾಡುವ    ಪ್ರತಿ    ಕೆ.ಜಿ   ರೇಷ್ಮೆ   ಗೂಡಿಗೆ     ರೂ.  "+Math.round(apiResponse.getContent().get(0).getUnitCost())+"/-  ರಂತೆ    ಸಾಗಾಣಿಕೆ    ವೆಚ್ಚ       ಮಂಜೂರಾತಿ     ನೀಡುವ     ಕುರಿತು. ");
        if (sanctionAmountTotal <= amount) {

            response.setHeader(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     "
                            + apiResponse.getContent().get(0).getDivisionName()
                            + "     ವಿಭಾಗ,     "
                            + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                            + "     ಇವರ     ಕಛೇರಿಯ     ನಡವಳಿಗಳು"
            );

            response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                    + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                    + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                    + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                    + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                    + "5)     ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ಇವರ   ಪ್ರಸ್ತಾವನೆ   ದಿನಾಂಕ  :  " + proposalDate);


            response.setHeader6(
                    "ಆದೇಶ     ಸಂಖ್ಯೆ  :  ರೇಸನಿ : " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + " : ತಾಂ : " + apiResponse.getContent().get(0).getRaceName() + " : ರೇಗೂ : ಸಾ.ವೆಚ್ಚ:  ಫ್ರೋಧನ: " + apiResponse.getContent().get(0).getSanctionOrderNumber() + " /  ದಿನಾಂಕ  : " + proposalDate);

            response.setHeader8(
                    "ಈ     ಕಚೇರಿಯ     ಲೆಕ್ಕ     ಶಾಖೆಗೆ     ಮುಂದಿನ     ಕ್ರಮಕ್ಕಾಗಿ.\n "
                            + "ಪ್ರತಿಯನ್ನು     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು  /  ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ, \n" +
                            apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ರವರಿಗೆ     ಮಾಹಿತಿಗಾಗಿ ");

            // Signature
            response.setLineItemComment(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು \n"
                            + apiResponse.getContent().get(0).getDivisionName()
                            + "     ವಿಭಾಗ,     "
                            + apiResponse.getContent().get(0).getLoggedinUserTalukName());

        }else {

            response.setHeader("ರೇಷ್ಮೆ     ಉಪ    ನಿರ್ದೇಶಕರು,    ಜಿಲ್ಲಾ      ಪಂಚಾಯತ್,   " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ಇವರ     ಕಛೇರಿಯ      ನಡವಳಿಗಳು");

            response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                    + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                    + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                    + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                    + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                    + "5)     ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,     " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ರವರ     ಜ್ಞಾಪನಪತ್ರ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSReleaseNo() + " ,   ದಿನಾಂಕ  :  " + sReleaseDate);



            response.setHeader6(
                    "ಆದೇಶ     ಸಂಖ್ಯೆ  :  ರೇಉನಿ : ಜಿ. ಪಂ : " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + " : ತಾಂ : "+apiResponse.getContent().get(0).getRaceName()+" : ರೇಗೂ : ಸಾ.ವೆಚ್ಚ:  ಫ್ರೋಧನ: " + apiResponse.getContent().get(0).getSanctionOrderNumber() + " /  ದಿನಾಂಕ  : " + proposalDate);

            response.setHeader8(
                    "ಇವರಿಗೆ,\n" +
                            "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,\n" +
                            apiResponse.getContent().get(0).getDivisionName() + "    ವಿಭಾಗ,  " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "\n" +
                            "ಪ್ರತಿಯನ್ನು    \n" +
                            "ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು/ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ , " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "\n" +
                            "ರವರಿಗೆ    ಮಾಹಿತಿಗಾಗಿ");
            // Signature
            response.setLineItemComment(
                    "ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,\n" +
                            "ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,\n"
                            + apiResponse.getContent().get(0).getLoggedinUserDistrictName());

        }


        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }


    private String formatDateDDMMYYYY(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }


    private JRBeanCollectionDataSource getDataSourceForPriceStabilizationIncentive(CheckInspectionStatusRequest requestDto)
            throws JsonProcessingException {

        SanctionOrder apiResponse = apiService.fetchDataFromMscCommercialMarket(requestDto);
        List<SanctionOrderResponse> sanctionOrderResponseList = new LinkedList<>();
        SanctionOrderResponse response = new SanctionOrderResponse();

        // ✅ totals
        float totalIncentiveAmount = 0f;
        int totalNoOfDfls = 0;
        Float sanctionAmountTotal = 0f;
        float totalCocoonsWeight = 0f;

        Long amount = apiResponse.getContent().get(0).getAmount();
        if (amount == null) {
            amount = 0L;
        }


// CHANGED: keep both – units for calc, kg for display
        float totalQtyUnits = 0f;  // quantityOfCocoonsProduced / 100, used for calc
        float totalQtyKg = 0f;  // NEW: actual kg for display & totals


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String admGovtDate = formatDate(apiResponse.getContent().get(0).getAdmGovtDate(), sdf);
        String schemeCircularDate = formatDate(apiResponse.getContent().get(0).getSchemeCircularDate(), sdf);
        String deptDeleDate = formatDate(apiResponse.getContent().get(0).getDeptDeleDate(), sdf);
        String allotReleaseDate = formatDate(apiResponse.getContent().get(0).getAllotReleaseDate(), sdf);
        String proposalDate = formatDate(apiResponse.getContent().get(0).getProposalDate(), sdf);
        String sReleaseDate = formatDate(apiResponse.getContent().get(0).getSReleaseDate(), sdf);


        String formattedDate;
        try {
            String inputDate = apiResponse.getContent().get(0).getDate().toString();
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat out = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = out.format(in.parse(inputDate));
        } catch (Exception e) {
            formattedDate = apiResponse.getContent().get(0).getDate().toString();
        }


//        if (totalAmount != null) {
//            totalLong = totalAmount.longValue();
//            amountInWords = KannadaNumberUtil.convertNumberToKannadaWords(totalLong);
//        }


        Float totalKgs = apiResponse.getContent().get(0).getGrandTotalQuantityOfCocoonsProduced(); // 270
        Float unitCost = apiResponse.getContent().get(0).getPerKgRate();          // 10

        // ===== HEADERS =====

        // Header (office)



        response.setHeader4(
                "                 " + apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     ರೇಷ್ಮೆ     ಇಲಾಖೆಯ     ವಿವಿಧ     ಕಾರ್ಯಕ್ರಮಗಳ     ಅನುಷ್ಟಾನಕ್ಕಾಗಿ     ವಿವಿಧ     ಲೆಕ್ಕ     "
                        + "ಶೀರ್ಷಿಕೆಗಳಡಿ     ಉಲ್ಲೇಖ(1)ರಲ್ಲಿ     ಸರ್ಕಾರವು     ಆಡಳಿತಾತ್ಮಕ     ಅನುಮೋದನೆಯನ್ನು     ನೀಡಿದ್ದು,  ಉಲ್ಲೇಖ(2) ರಲ್ಲಿ      "
                        + apiResponse.getContent().get(0).getSchemeNameInKannada() + "    ಯೋಜನೆಯಡಿ    ಲೆಕ್ಕ      ಶೀರ್ಷಿಕೆ:  " + apiResponse.getContent().get(0).getScHeadAccountName() + "(" + apiResponse.getContent().get(0).getDescription() + ")    ರಡಿ   ರಾಜ್ಯ ದ    ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು    ಪ್ರತಿ   " +
                        "100   ರೋಗರಹಿತ    ದ್ವಿ ತಳಿ     ಸಂಕರಣ     ಮೊಟ್ಟೆ  /ಚಾಕಿ    ಹುಳುಗಳಿಗೆ    60 ಕೆ.ಜಿ    ಗಿಂತ    ಹೆಚ್ಚು     ಇಳುವರಿ    ಹಾಗೂ"
                        + "     ಗರಿಷ್ಠ     90    ಕೆ.ಜಿ    ರೇಷ್ಮೆ      ಗೂಡು    ಉತ್ಪಾ ದಿಸಿ    ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ     ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆ ಗಳ      ಮೂಲಕ    " +
                        "ವಹಿವಾಟಾಗುವ     ದ್ವಿ ತಳಿ      ಸಂಕರಣ     ರೇಷ್ಮೆ    ಗೂಡುಗಳಿಗೆ     ಪ್ರತಿ     ಕೆ.ಜಿ ಗೆ     ರೂ. " + Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/- ರಂತೆ     ಪ್ರೋತ್ಸಾಹಧನದ   ನೀಡುವ " +
                        "      ಕಾರ್ಯಕ್ರಮದ     ಅನುಷ್ಠಾ ನಕ್ಕಾಗಿ    ಮಾರ್ಗ  ಸೂಚಿಯನ್ನು     ನೀಡಲಾಗಿರುತ್ತದೆ.\n"
                        + "                 ಉಲ್ಲೇಖ(5)     ರಂತೆ     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ,     " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "    ಇವರು     ಬೆಲೆಸ್ಥಿ ರಿಕರಣ    ನಿಧಿ    ಅನುದಾನದಿಂದ    " +
                        "ರಾಜ್ಯ ದ     ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ    ಗೂಡಿನ     ಮಾರುಕಟ್ಟೆಗಳ     ಮೂಲಕ     ವಹಿವಾಟಾಗುವ    ದ್ವಿ ತಳಿ     ಸಂಕರಣ    ರೇಷ್ಮೆ    ಗೂಡುಗಳಿಗೆ     ಪ್ರ ತಿ     ಕೆ.ಜಿ ಗೆ     ರೂ. " + Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/-    ರಂತೆ      " +
                        "ಪ್ರೋತ್ಸಾಹಧನ    ನೀಡಲು    ಎಲ್ಲಾ     ಅಗತ್ಯ     ದಾಖಲಾತಿಗಳನ್ನು    ಒಳಗೊಂಡ    ಪ್ರಸ್ತಾ ವನೆಯನ್ನು    ಸಲ್ಲಿಸಿದ್ದು     ವಿವರಗಳು   ಈ    ಕೆಳಕಂಡಂತಿದೆ.");


        response.setAcceptedDate("ಸ್ವೀಕೃತಿ ಪತ್ರದ ದಿನಾಂಕ : " + apiResponse.getContent().get(0).getDate());
        response.setDate(apiResponse.getContent().get(0).getDate());
        response.setFarmerFirstName(apiResponse.getContent().get(0).getFarmerFirstName());
        response.setAddressText(apiResponse.getContent().get(0).getAddressText());
        response.setDistrictName(apiResponse.getContent().get(0).getDistrictName());
        response.setTalukName(apiResponse.getContent().get(0).getTalukName());
        response.setHobliName(apiResponse.getContent().get(0).getHobliName());
        response.setVillageName(apiResponse.getContent().get(0).getVillageName());
        response.setFruitsId(apiResponse.getContent().get(0).getFruitsId());
        response.setFinancialYear(apiResponse.getContent().get(0).getFinancialYear());
        response.setSchemeNameInKannada(apiResponse.getContent().get(0).getSchemeNameInKannada());
        response.setSubSchemeNameInKannada(apiResponse.getContent().get(0).getSubSchemeNameInKannada());
        response.setFatherNameKan(apiResponse.getContent().get(0).getFatherNameKan());
        response.setArn(apiResponse.getContent().get(0).getArn());
        response.setMobileNumber(apiResponse.getContent().get(0).getMobileNumber());
        response.setLogurl("/reports/Seal_of_Karnataka.PNG");

        // ====== DETAIL ROWS ======
        if (apiResponse.getContent() != null) {
            sanctionOrderResponseList.add(response);

            int serialNo = 1;
            for (SanctionOrderResponse sanctionOrderResponse : apiResponse.getContent()) {

                if (sanctionOrderResponse.getBonusRNoAndDate() == null) {
                    sanctionOrderResponse.setBonusRNoAndDate("");
                }
                if (sanctionOrderResponse.getVillageName() == null) {
                    sanctionOrderResponse.setVillageName("");
                }
                if (sanctionOrderResponse.getFruitsId() == null) {
                    sanctionOrderResponse.setFruitsId("");
                }
                if (sanctionOrderResponse.getFatherNameKan() == null) {
                    sanctionOrderResponse.setFatherNameKan("");
                }
                if (sanctionOrderResponse.getFarmerFirstName() == null) {
                    sanctionOrderResponse.setFarmerFirstName("");
                }
                if (sanctionOrderResponse.getPerKgRate() == null) {
                    sanctionOrderResponse.setPerKgRate(0f);
                }
                if (sanctionOrderResponse.getChawkiPercentage() == null) {
                    sanctionOrderResponse.setChawkiPercentage(0f);
                }
                if (sanctionOrderResponse.getCocoonsWeight() == null) {
                    sanctionOrderResponse.setCocoonsWeight(0f);
                }
                if (sanctionOrderResponse.getSanctionAmount() == null) {
                    sanctionOrderResponse.setSanctionAmount(0f);
                }
                sanctionAmountTotal += sanctionOrderResponse.getSanctionAmount();

                if (sanctionOrderResponse.getReceiptNo() == null) {
                    sanctionOrderResponse.setReceiptNo(" ");
                }
                if (sanctionOrderResponse.getTotalSchemeAmount() == null) {
                    sanctionOrderResponse.setTotalSchemeAmount(0f);
                }

                Date distDate = sanctionOrderResponse.getDateOfDistribution();

                if (distDate != null) {
                    sanctionOrderResponse.setDateOfDistributionString(
                            formatDateDDMMYYYY(distDate)
                    );
                } else {
                    sanctionOrderResponse.setDateOfDistributionString("");
                }


                if (sanctionOrderResponse.getAfMarketName() == null) {
                    sanctionOrderResponse.setAfMarketName(" ");
                }

                if (sanctionOrderResponse.getTotalCocoonsWeight() == null) {
                    sanctionOrderResponse.setTotalCocoonsWeight(0f);
                }

                if (sanctionOrderResponse.getLotNo() == null) {
                    sanctionOrderResponse.setLotNo("");
                }
                if (sanctionOrderResponse.getNoOfDfls() == null) {
                    sanctionOrderResponse.setNoOfDfls("");
                }
                Float cocoonsWeight = sanctionOrderResponse.getCocoonsWeight();
                if (cocoonsWeight == null) {
                    cocoonsWeight = 0f;
                }
                float cocoonsWeightFormatted =
                        Float.parseFloat(String.format("%.3f", cocoonsWeight));

                sanctionOrderResponse.setCocoonsWeight(cocoonsWeightFormatted);
                totalCocoonsWeight += cocoonsWeightFormatted;


                if (sanctionOrderResponse.getQuantityOfCocoonsProduced() == null) {
                    sanctionOrderResponse.setQuantityOfCocoonsProduced(0f);
                }
                if (sanctionOrderResponse.getAverageYield() == null) {
                    sanctionOrderResponse.setAverageYield(0f);
                }
                if (sanctionOrderResponse.getUnitCost() == null) {
                    sanctionOrderResponse.setUnitCost(0f);
                }

                // qty in kg
                Float qtyKg = sanctionOrderResponse.getQuantityOfCocoonsProduced();
                if (qtyKg == null) qtyKg = 0f;

                Float unitCostRow = sanctionOrderResponse.getUnitCost();
                if (unitCostRow == null) unitCostRow = 0f;

                float incentiveAmountRow = qtyKg * unitCostRow;

                sanctionOrderResponse.setQuantityOfCocoonsProduced(qtyKg);

                sanctionOrderResponse.setIncentiveAmount(incentiveAmountRow);

// accumulate totals
                totalIncentiveAmount += incentiveAmountRow;
                totalQtyKg += qtyKg;


// running totals for footer/header
                sanctionOrderResponse.setTotalIncentiveAmount(totalIncentiveAmount);
                sanctionOrderResponse.setGrandTotalNoOfDfls((float) totalNoOfDfls);

// CHANGED: use KG totals (fixes wrong 600 in footer)
                sanctionOrderResponse.setGrandTotalQuantityOfCocoonsProduced(totalQtyKg);
                sanctionOrderResponse.setTotalQuantityOfCocoonsProduced(totalQtyKg); // NEW: for $F{totalQuantityOfCocoonsProduced}


                if (sanctionOrderResponse.getAverageYield() == null) {
                    sanctionOrderResponse.setAverageYield(0f);
                }
                if (sanctionOrderResponse.getGrandTotalSchemeAmount() == null) {
                    sanctionOrderResponse.setGrandTotalSchemeAmount(0f);
                }
                if (sanctionOrderResponse.getCrcName() == null) {
                    sanctionOrderResponse.setCrcName("");
                }

                sanctionOrderResponse.setSerialNumber(serialNo++);
                sanctionOrderResponseList.add(sanctionOrderResponse);
            }

        }
        // ✅ CONVERT TOTAL TO WORDS (AFTER LOOP)
        String sanctionAmountTotalInWords =
                KannadaNumberUtil.convertNumberToKannadaWords(
                        sanctionAmountTotal.longValue()
                );

        // ✅ SET TOTALS ON HEADER RESPONSE (AFTER LOOP)
        response.setTotalSanctionAmount(sanctionAmountTotal);
        response.setTotalSanctionAmountInWords(sanctionAmountTotalInWords);

        // numeric totals on header (ACTUAL KG)
        response.setTotalIncentiveAmount(totalIncentiveAmount);
        response.setGrandTotalNoOfDfls((float) totalNoOfDfls);
        response.setGrandTotalSchemeAmount(totalIncentiveAmount);

// CHANGED: keep total in kg
        response.setGrandTotalQuantityOfCocoonsProduced(totalQtyKg);
        response.setTotalQuantityOfCocoonsProduced(totalQtyKg); // NEW: same field JRXML footer uses

// NOW: compute in-words using TOTAL KG
        String totalQtyInWords =
                KannadaNumberUtil.convertNumberToKannadaWords((long) totalQtyKg);

        String totalIncentiveInWords =
                KannadaNumberUtil.convertNumberToKannadaWords((long) totalIncentiveAmount);

//        response.setTotalCocoonsWeight(totalCocoonsWeightFormatted);
        response.setTotalQuantityOfCocoonsProducedInWords(totalQtyInWords);
        response.setTotalIncentiveAmountInWords(totalIncentiveInWords);
        String totalCocoonsWeightFormatted =
                String.format("%.3f", totalCocoonsWeight);

        if (sanctionAmountTotal <= amount) {
            response.setStatus("Approved By Narasimha Swamy ");

            response.setHeader(
                    "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     "
                            + apiResponse.getContent().get(0).getDivisionName()
                            + "     ವಿಭಾಗ,     "
                            + apiResponse.getContent().get(0).getLoggedinUserTalukName()
                            + "     ಇವರ     ಕಛೇರಿಯ     ನಡವಳಿಗಳು"
            );

            response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                    + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                    + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                    + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                    + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                    + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                    + "5)     ರೇಷ್ಮೆ   ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ   ಕೇಂದ್ರ,   " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ಇವರ   ಪ್ರಸ್ತಾವನೆ   ದಿನಾಂಕ  :  " + proposalDate);


            response.setHeader6(
                "ಆದೇಶ     ಸಂಖ್ಯೆ  :   ರೇಸನಿ : " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + " : ತಾಂ : ದ್ವಿ ತಳಿ  : ರೇಗೂ : ಸಾ.ವೆಚ್ಚ :  ಫ್ರೋಧನ " + apiResponse.getContent().get(0).getSanctionOrderNumber() + " /  ದಿನಾಂಕ  : " + proposalDate);

            response.setHeader8(
                    "ಈ     ಕಚೇರಿಯ     ಲೆಕ್ಕ     ಶಾಖೆಗೆ     ಮುಂದಿನ     ಕ್ರಮಕ್ಕಾಗಿ.\n "
                            + "ಪ್ರತಿಯನ್ನು     ರೇಷ್ಮೆ     ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು  /  ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ, \n" +
                            apiResponse.getContent().get(0).getLoggedinUserTscName() + "   ರವರಿಗೆ     ಮಾಹಿತಿಗಾಗಿ ");

            // Signature
        response.setLineItemComment(
                "ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು \n"
                        + apiResponse.getContent().get(0).getDivisionName()
                        + "     ವಿಭಾಗ,     "
                        + apiResponse.getContent().get(0).getLoggedinUserTalukName());
    }else

    {
        response.setStatus("Approved By Narasimha Swamy ");
        response.setHeader("ರೇಷ್ಮೆ     ಉಪ    ನಿರ್ದೇಶಕರು,    ಜಿಲ್ಲಾ      ಪಂಚಾಯತ್,   " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ಇವರ     ಕಛೇರಿಯ      ನಡವಳಿಗಳು");

        response.setHeader3("1)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :  " + apiResponse.getContent().get(0).getAdmGovtOrder() + " ,   ದಿನಾಂಕ  : " + admGovtDate + " \n"
                + "2)     ರೇಷ್ಮೆ     ಕೃಷಿ     ಅಭಿವೃದ್ದಿ     ಆಯುಕ್ತರು     ಹಾಗೂ     ರೇಷ್ಮೆ     ನಿರ್ದೇಶಕರು,     ಬೆಂಗಳೂರು     ರವರ     ಸುತ್ತೋಲೆ     ಸಂಖ್ಯೆ  :\n"
                + "         " + apiResponse.getContent().get(0).getSchemeCircularNo() + " ,   ದಿನಾಂಕ  : " + schemeCircularDate + " \n"
                + "3)     ಸರ್ಕಾರದ     ಆದೇಶ     ಸಂಖ್ಯೆ  :     " + apiResponse.getContent().get(0).getDeptDeleNo() + ",    ದಿನಾಂಕ  :  " + deptDeleDate + " \n"
                + "4)     ರೇಷ್ಮೆ    ಕೃ ಷಿ    ಅಭಿವೃ ದ್ದಿ     ಆಯುಕ್ತ ರು    ಹಾಗೂ   ರೇಷ್ಮೆ   ನಿರ್ದೇಶಕರು,   ಬೆಂಗಳೂರು ರವರ    ಜ್ಞಾಪನ    ಪತ್ರದ    ಸಂಖ್ಯೆ :\n"
                + "         " + apiResponse.getContent().get(0).getAllotReleaseNo() + ".  ದಿನಾಂಕ :  " + allotReleaseDate + "\n"
                + "5)     ರೇಷ್ಮೆ     ಸಹಾಯಕ     ನಿರ್ದೇಶಕರು,     ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,     " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "     ರವರ     ಜ್ಞಾಪನಪತ್ರ     ಸಂಖ್ಯೆ  :\n"
                + "         " + apiResponse.getContent().get(0).getSReleaseNo() + " ,   ದಿನಾಂಕ  :  " + sReleaseDate);

        response.setHeader6(
                "ಆದೇಶ     ಸಂಖ್ಯೆ  :  ರೇಉನಿ : ಜಿ. ಪಂ : " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + " : ತಾಂ : "+apiResponse.getContent().get(0).getRaceName()+" : ರೇಗೂ : ಸಾ.ವೆಚ್ಚ:  ಫ್ರೋಧನ: " + apiResponse.getContent().get(0).getSanctionOrderNumber() + " /  ದಿನಾಂಕ  : " + proposalDate);

        response.setHeader8(
                "ಇವರಿಗೆ,\n" +
                        "ರೇಷ್ಮೆ     ಸಹಾಯಕ    ನಿರ್ದೇಶಕರು,\n" +
                        apiResponse.getContent().get(0).getDivisionName() + "    ವಿಭಾಗ,  " + apiResponse.getContent().get(0).getLoggedinUserDistrictName() + "\n" +
                        "ಪ್ರತಿಯನ್ನು    \n" +
                        "ರೇಷ್ಮೆ    ವಿಸ್ತರಣಾಧಿಕಾರಿಗಳು/ಪ್ರಭಾರಾಧಿಕಾರಿಗಳು,   ತಾಂತ್ರಿಕ   ಸೇವಾ    ಕೇಂದ್ರ , " + apiResponse.getContent().get(0).getLoggedinUserTscName() + "\n" +
                        "ರವರಿಗೆ    ಮಾಹಿತಿಗಾಗಿ");
        // Signature
        response.setLineItemComment(
                "ರೇಷ್ಮೆ    ಉಪ    ನಿರ್ದೇಶಕರು,\n" +
                        "ಜಿಲ್ಲಾ     ಪಂಚಾಯತ್,\n"
                        + apiResponse.getContent().get(0).getLoggedinUserDistrictName());

    }
        response.setHeader2(
                apiResponse.getContent().get(0).getFinancialYear()
                        + "     ನೇ     ಸಾಲಿನಲ್ಲಿ     "+apiResponse.getContent().get(0).getSchemeNameInKannada() +"    ಯೋಜನೆಯಡಿ   ಸರ್ಕಾರಿ    ರೇಷ್ಮೆ      ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ   ಗಳ     ಮೂಲಕ   ವಹಿವಾಟಾಗುವ     ದ್ವಿ ತಳಿ      ಸಂಕರಣ " +
                        "   ರೇಷ್ಮೆ      ಗೂಡುಗಳಿಗೆ    ಪ್ರ ತಿ    ಕೆ.ಜಿ   ರೇಷ್ಮೆ ಗೂಡಿಗೆ    ರೂ.  "+ Math.round(apiResponse.getContent().get(0).getUnitCost()) + "/-  ರಂತೆ     ಪ್ರೋತ್ಸಾಹಧನ  ಮಂಜೂರಾತಿ     ನೀಡುವ     ಕುರಿತು. ");

        response.setHeader7(
                "            ಪೀಠಿಕೆಯಲ್ಲಿ      ವಿವರಿಸಿರುವ     ಎಲ್ಲಾ     ಅಂಶಗಳನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ,     " + apiResponse.getContent().get(0).getLoggedinUserTalukName() + "     ತಾಲ್ಲೂಕಿನ     ತಾಂತ್ರಿಕ     ಸೇವಾ     ಕೇಂದ್ರ     " + apiResponse.getContent().get(0).getLoggedinUserTscName()+""
                        + "     ವ್ಯಾಪ್ತಿಯ    " + apiResponse.getContent().get(0).getTotalFarmers() +"   ಜನ   ರೇಷ್ಮೆ    ಬೆಳೆಗಾರರು ,   ಸರ್ಕಾರಿ   ರೇಷ್ಮೆ    ಗೂಡಿನ   ಮಾರುಕಟ್ಟೆಗಳಲ್ಲಿ     ವಹಿವಾಟು     ಮಾಡಿದ     ಒಟ್ಟು     " + totalCocoonsWeightFormatted  + "     ಕೆ.ಜಿ   ದ್ವಿ ತಳಿ "
                        +"   ಸಂಕರಣ    ರೇಷ್ಮೆ   ಗೂಡಿಗೆ    ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ.  " +  Math.round(apiResponse.getContent().get(0).getUnitCost()) +  "/- ರಂತೆ     ಪ್ರೋತ್ಸಾಹಧನದ    ರೂ. " +   Math.round(response.getTotalSanctionAmount())      + "/- (ರೂಪಾಯಿ  " + response.getTotalSanctionAmountInWords()  + " ) ಗಳನ್ನು    "
                        +"  ಮಂಜೂರು   ಮಾಡಿದೆ.   ರೇಷ್ಮೆ   ಅಭಿವೃದ್ಧಿ    ಯೋಜನೆಯ  ಲೆಕ್ಕ      ಶೀರ್ಷಿಕೆ    " + apiResponse.getContent().get(0).getScHeadAccountName() + "("+ apiResponse.getContent().get(0).getDescription()+ ")   ರಾಜ್ಯ    ವಲಯ   ಅಡಿ    ಖಜಾನೆ-2   ರಲ್ಲಿ      ಬಿಡುಗಡೆಗೊಳಿಸಿರುವ " +
                        "   ಸಹಾಯಧನದ    ಅನುದಾನದಲ್ಲಿ    ಡಿ.ಬಿ.ಟಿ     ಮುಖಾಂತರ     ಫಲಾನುಭವಿ   ಬ್ಯಾಂಕ್    ಖಾತೆಗೆ     ನೇರವಾಗಿ     ಜಮಾ     ಮಾಡುವುದು. \n"
                        + "            ಸದರಿ     ವೆಚ್ಚವನ್ನು     ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ     " + apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription()+ ") ರಾಜ್ಯ    ವಲಯ   ಅಡಿ  ಭರಿಸುವುದು.");

        response.setHeader5(
                "            ಪ್ರಸ್ತಾವನೆಯನ್ನು     ಪರಿಶೀಲಿಸಲಾಗಿ     "+apiResponse.getContent().get(0).getFinancialYear() +"   ನೇ    ಸಾಲಿನ    ರೇಷ್ಮೆ    ಅಭಿವೃ ದ್ಧಿ     ಯೋಜನೆಯ    ಲೆಕ್ಕ     ಶೀರ್ಷಿಕೆ    "+ apiResponse.getContent().get(0).getScHeadAccountName() +"("+ apiResponse.getContent().get(0).getDescription()+")     "+
                        "ಅಡಿ    ಮೇಲ್ಕಂಡ     ರೇಷ್ಮೆ      ಬೆಳೆಗಾರರು     ಸರ್ಕಾರಿ     ರೇಷ್ಮೆ     ಗೂಡಿನ    ಮಾರುಕಟ್ಟೆ  ಗಳಲ್ಲಿ    ವಹಿವಾಟು    ಮಾಡಿದ  "+  totalCocoonsWeightFormatted  +"   ಕೆ.ಜಿ    ದ್ವಿ ತಳಿ    ರೇಷ್ಮೆ    ಗೂಡಿಗೆ     ಪ್ರತಿ    ಕೆ.ಜಿ.ಗೆ    ರೂ. "+ Math.round(apiResponse.getContent().get(0).getUnitCost()) +"/-   ರಂತೆ    ಪ್ರೋತ್ಸಾಹಧನ   ರೂ. "+Math.round(response.getTotalSanctionAmount())+"/-    " +
                        "  ಗಳನ್ನು       ಪಡೆಯಲು    ಅರ್ಹರಿರುತ್ತಾರೆ.    ಉಲ್ಲೇಖ (3) ರ   ಸರ್ಕಾರದ    ಆದೇಶದ    ರೀತ್ಯಾ   ಈ    ಕಛೇರಿಯ    ಅಧಿಕಾರ    ಪ್ರತ್ಯಾ ಯೋಜನೆ    ವ್ಯಾಪ್ತಿ ಯಲ್ಲಿದ್ದು,    ಉಲ್ಲೇಖ (4) ರಲ್ಲಿ   ಸದರಿ   ಕಾರ್ಯಕ್ರ ಮದ   ಅನುಷ್ಠಾ ನಕ್ಕಾ ಗಿ    ನೀಡಿರುವ     ಮಾರ್ಗಸೂಚಿಯನ್ವಯ     "+
                        "ಪ್ರೋತ್ಸಾಹಧನ  ಮಂಜೂರು    ಮಾಡಲು    ಅನುದಾನ    ಬಿಡುಗಡೆ    ಮಾಡಲಾಗಿದೆ.    ಅದರಂತೆ    ಈ   ಕೆಳಕಂಡ   ಮಂಜೂರಾತಿ   ಆದೇಶ   ಹೊರಡಿಸಿದೆ. "

        );
        return new JRBeanCollectionDataSource(sanctionOrderResponseList);
    }







    private String getKannadaShortForm(String districtName) {
        if (districtName == null || districtName.isEmpty()) {
            return "";
        }
        // Return only the first 2 characters (can be adjusted)
        return districtName.length() >= 2 ? districtName.substring(0, 2) : districtName;}



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
                    " 7. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ .ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು -ವಿಭಾಗರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ :- ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n"

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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
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
                    " 7. ರೇಷ್ಮೆ ವಿಸ್ತರಣಾಧಿಕಾರಿ .ತಾಂತ್ರಿಕ ಸೇವಾ ಕೇಂದ್ರ /ರೇಷ್ಮೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು -ವಿಭಾಗರವರ ಪ್ರಸ್ತಾವನೆ ಸಂಖ್ಯೆ :- ದಿನಾಂಕ " + apiResponse.getContent().get(0).getDate() + "\n"

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
            response.setLogurl("/reports/Seal_of_Karnataka.PNG");
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
