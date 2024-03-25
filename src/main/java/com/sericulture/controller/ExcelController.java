package com.sericulture.controller;


import com.sericulture.helper.Util;
import com.sericulture.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("excel-report")
public class ExcelController {
    private final ApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    public ExcelController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/average-report")
    public ResponseEntity<?> averageReport(@RequestBody AverageReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateExcel(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"average_report"+ Util.getISTLocalDate()+".csv")
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(resource);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private void generateExcel(AverageReportRequest requestDto) throws Exception {
        AverageReportDataResponse reportDataResponse = apiService.averageReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Race wise Average Rate");

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("Month");

        Row raceHeader = sheet.createRow(2);
        // Create some data rows
        Row dataRow1 = sheet.createRow(3);
        dataRow1.createCell(0).setCellValue(1);
        dataRow1.createCell(1).setCellValue("April");

        Row dataRow2 = sheet.createRow(4);
        dataRow2.createCell(0).setCellValue(2);
        dataRow2.createCell(1).setCellValue("May");

        Row dataRow3 = sheet.createRow(5);
        dataRow3.createCell(0).setCellValue(3);
        dataRow3.createCell(1).setCellValue("June");

        Row dataRow4 = sheet.createRow(6);
        dataRow4.createCell(0).setCellValue(4);
        dataRow4.createCell(1).setCellValue("July");

        Row dataRow5 = sheet.createRow(7);
        dataRow5.createCell(0).setCellValue(5);
        dataRow5.createCell(1).setCellValue("August");

        Row dataRow6 = sheet.createRow(8);
        dataRow6.createCell(0).setCellValue(6);
        dataRow6.createCell(1).setCellValue("September");

        Row dataRow7 = sheet.createRow(9);
        dataRow7.createCell(0).setCellValue(7);
        dataRow7.createCell(1).setCellValue("October");

        Row dataRow8 = sheet.createRow(10);
        dataRow8.createCell(0).setCellValue(8);
        dataRow8.createCell(1).setCellValue("November");

        Row dataRow9 = sheet.createRow(11);
        dataRow9.createCell(0).setCellValue(9);
        dataRow9.createCell(1).setCellValue("December");

        Row dataRow10 = sheet.createRow(12);
        dataRow10.createCell(0).setCellValue(10);
        dataRow10.createCell(1).setCellValue("January");

        Row dataRow11 = sheet.createRow(13);
        dataRow11.createCell(0).setCellValue(11);
        dataRow11.createCell(1).setCellValue("Febraury");

        Row dataRow12 = sheet.createRow(14);
        dataRow12.createCell(0).setCellValue(12);
        dataRow12.createCell(1).setCellValue("March");

        Row dataRow13 = sheet.createRow(15);
        dataRow13.createCell(1).setCellValue("Total");


        //Dynamic data binds here
        //Starting 0th and 1st column cells are hardcoded, So dynamic data column starts from 2nd column

        int dynamicColumnStartsFrom = 2;
        for(int j=0; j<reportDataResponse.getContent().getAverageRateYearWiseList().size(); j++){
            AverageRateYearWise response = reportDataResponse.getContent().getAverageRateYearWiseList().get(j);
            if(response.getAverageRateRaceWiseList().size()>0) {
                subHeaderRow.createCell(dynamicColumnStartsFrom).setCellValue(response.getYear());
            }

            for(int k=0; k<response.getAverageRateRaceWiseList().size(); k++) {
                AverageRateRaceWise averageRateRaceWise = response.getAverageRateRaceWiseList().get(k);
                double totalWeight = 0.0;
                double totalAmount = 0.0;
                for(int l=0; l<averageRateRaceWise.getAverageRateValues().size(); l++) {
                    Row currentDataRow;
                    raceHeader.createCell(dynamicColumnStartsFrom).setCellValue("Quantity");
                    raceHeader.createCell(dynamicColumnStartsFrom+1).setCellValue(averageRateRaceWise.getRaceName());
                    if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("April")){
                        currentDataRow = dataRow1;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("May")){
                        currentDataRow = dataRow2;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("June")){
                        currentDataRow = dataRow3;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("July")){
                        currentDataRow = dataRow4;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("August")){
                        currentDataRow = dataRow5;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("September")){
                        currentDataRow = dataRow6;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("October")){
                        currentDataRow = dataRow7;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("November")){
                        currentDataRow = dataRow8;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("December")){
                        currentDataRow = dataRow9;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("January")){
                        currentDataRow = dataRow10;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("February")){
                        currentDataRow = dataRow11;
                    }else if(averageRateRaceWise.getAverageRateValues().get(l).getMonth().equals("March")) {
                        currentDataRow = dataRow12;
                    }else{
                        currentDataRow = null;
                    }
                    if (currentDataRow != null) {
                        totalWeight = totalWeight+ Util.objectToFloat(averageRateRaceWise.getAverageRateValues().get(l).getWeight());
                        totalAmount = totalAmount+ Util.objectToFloat(averageRateRaceWise.getAverageRateValues().get(l).getLotSoldAmount());

                        currentDataRow.createCell(dynamicColumnStartsFrom).setCellValue(averageRateRaceWise.getAverageRateValues().get(l).getWeight());
                        currentDataRow.createCell(dynamicColumnStartsFrom+1).setCellValue(averageRateRaceWise.getAverageRateValues().get(l).getLotSoldAmount());

                    }
                }
                //+2 for quantity and rate
                dataRow13.createCell(dynamicColumnStartsFrom).setCellValue(totalWeight);
                dataRow13.createCell(dynamicColumnStartsFrom+1).setCellValue(totalAmount);
                dynamicColumnStartsFrom = dynamicColumnStartsFrom + 2;
            }
        }

        // Auto-size all columns
        for (int columnIndex = 0; columnIndex <= dynamicColumnStartsFrom + 1; columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
        String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @PostMapping("/average-cocoon-report")
    public ResponseEntity<?> averageCocoonReport(@RequestBody AverageReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateCocoonExcel(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"average_cocoon_report"+ Util.getISTLocalDate()+".csv")
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(resource);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage() + ex.getStackTrace());
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(ex.getMessage().getBytes(StandardCharsets.UTF_8), org.springframework.http.HttpStatus.OK);
        }
    }

    private void generateCocoonExcel(AverageReportRequest requestDto) throws Exception {
        CocoonReport reportDataResponse = apiService.averageCocoonReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Quantity and Average Rate of Cocoons  transacted( month wise/year wise)  from " +requestDto.getStartYear().getYear() +"-"+ (requestDto.getStartYear().getYear()+1) +" to "+(requestDto.getEndYear().getYear()-1)+"-"+requestDto.getEndYear().getYear());

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("Month");

        Row raceHeader = sheet.createRow(2);
        // Create some data rows
        Row dataRow1 = sheet.createRow(3);
        dataRow1.createCell(0).setCellValue(1);
        dataRow1.createCell(1).setCellValue("April");

        Row dataRow2 = sheet.createRow(4);
        dataRow2.createCell(0).setCellValue(2);
        dataRow2.createCell(1).setCellValue("May");

        Row dataRow3 = sheet.createRow(5);
        dataRow3.createCell(0).setCellValue(3);
        dataRow3.createCell(1).setCellValue("June");

        Row dataRow4 = sheet.createRow(6);
        dataRow4.createCell(0).setCellValue(4);
        dataRow4.createCell(1).setCellValue("July");

        Row dataRow5 = sheet.createRow(7);
        dataRow5.createCell(0).setCellValue(5);
        dataRow5.createCell(1).setCellValue("August");

        Row dataRow6 = sheet.createRow(8);
        dataRow6.createCell(0).setCellValue(6);
        dataRow6.createCell(1).setCellValue("September");

        Row dataRow7 = sheet.createRow(9);
        dataRow7.createCell(0).setCellValue(7);
        dataRow7.createCell(1).setCellValue("October");

        Row dataRow8 = sheet.createRow(10);
        dataRow8.createCell(0).setCellValue(8);
        dataRow8.createCell(1).setCellValue("November");

        Row dataRow9 = sheet.createRow(11);
        dataRow9.createCell(0).setCellValue(9);
        dataRow9.createCell(1).setCellValue("December");

        Row dataRow10 = sheet.createRow(12);
        dataRow10.createCell(0).setCellValue(10);
        dataRow10.createCell(1).setCellValue("January");

        Row dataRow11 = sheet.createRow(13);
        dataRow11.createCell(0).setCellValue(11);
        dataRow11.createCell(1).setCellValue("Febraury");

        Row dataRow12 = sheet.createRow(14);
        dataRow12.createCell(0).setCellValue(12);
        dataRow12.createCell(1).setCellValue("March");

        Row dataRow13 = sheet.createRow(15);
        dataRow13.createCell(1).setCellValue("Total");


        //Dynamic data binds here
        //Starting 0th and 1st column cells are hardcoded, So dynamic data column starts from 2nd column

        int dynamicColumnStartsFrom = 2;
        double totalWeight = 0.0;
        double totalAmount = 0.0;
        for (int j = 0; j < reportDataResponse.getContent().getAverageCocoonYearWises().size(); j++) {
                AverageCocoonYearWise response = reportDataResponse.getContent().getAverageCocoonYearWises().get(j);
                if (response.getAverageCocoonReports().size() > 0) {
                    subHeaderRow.createCell(dynamicColumnStartsFrom).setCellValue(response.getYear());
                }

                for (int k = 0; k < response.getAverageCocoonReports().size(); k++) {
                    AverageCocoonReport averageRateRaceWise = response.getAverageCocoonReports().get(k);


                    Row currentDataRow;
                    raceHeader.createCell(dynamicColumnStartsFrom).setCellValue("Qty (M.tons)");
                    raceHeader.createCell(dynamicColumnStartsFrom + 1).setCellValue("Avg Rates (Rs)");
                    if (averageRateRaceWise.getMonth().equals("4")) {
                        currentDataRow = dataRow1;
                    } else if (averageRateRaceWise.getMonth().equals("5")) {
                        currentDataRow = dataRow2;
                    } else if (averageRateRaceWise.getMonth().equals("6")) {
                        currentDataRow = dataRow3;
                    } else if (averageRateRaceWise.getMonth().equals("7")) {
                        currentDataRow = dataRow4;
                    } else if (averageRateRaceWise.getMonth().equals("8")) {
                        currentDataRow = dataRow5;
                    } else if (averageRateRaceWise.getMonth().equals("9")) {
                        currentDataRow = dataRow6;
                    } else if (averageRateRaceWise.getMonth().equals("10")) {
                        currentDataRow = dataRow7;
                    } else if (averageRateRaceWise.getMonth().equals("11")) {
                        currentDataRow = dataRow8;
                    } else if (averageRateRaceWise.getMonth().equals("12")) {
                        currentDataRow = dataRow9;
                    } else if (averageRateRaceWise.getMonth().equals("1")) {
                        currentDataRow = dataRow10;
                    } else if (averageRateRaceWise.getMonth().equals("2")) {
                        currentDataRow = dataRow11;
                    } else if (averageRateRaceWise.getMonth().equals("3")) {
                        currentDataRow = dataRow12;
                    } else {
                        currentDataRow = null;
                    }
                    if (currentDataRow != null) {
                        totalWeight = totalWeight + Util.objectToFloat(averageRateRaceWise.getWeight());
                        totalAmount = totalAmount + Util.objectToFloat(averageRateRaceWise.getLotSoldAmount());

                        currentDataRow.createCell(dynamicColumnStartsFrom).setCellValue(averageRateRaceWise.getWeight());
                        currentDataRow.createCell(dynamicColumnStartsFrom + 1).setCellValue(averageRateRaceWise.getLotSoldAmount());

                    }


                }

            }
        //+2 for quantity and rate
        dataRow13.createCell(dynamicColumnStartsFrom).setCellValue(totalWeight);
        dataRow13.createCell(dynamicColumnStartsFrom + 1).setCellValue(totalAmount);
        dynamicColumnStartsFrom = dynamicColumnStartsFrom + 2;


        // Auto-size all columns
        for (int columnIndex = 1; columnIndex <= dynamicColumnStartsFrom + 1; columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
        String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

}
