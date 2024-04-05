package com.sericulture.controller;


import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.AudioVisual.AudioVisualReportRequest;
import com.sericulture.model.AudioVisual.AudioVisualResponse;
import com.sericulture.model.AudioVisual.MonthWiseReport;
import com.sericulture.model.DTRAllMarket.DTRMarketResponse;
import com.sericulture.model.DTRAllMarket.DTRRaceResponse;
import com.sericulture.model.MarketReport.MarketReportRaceWise;
import com.sericulture.model.MarketReport.MarketResponse;
import com.sericulture.model.MarketReport.MarketWiseInfo;
import com.sericulture.model.MarketWiseReport.DivisionReport;
import com.sericulture.model.MarketWiseReport.DivisionResponse;
import com.sericulture.model.MonthlyReport.MonthlyReportInfo;
import com.sericulture.model.MonthlyReport.MonthlyReportRaceWise;
import com.sericulture.model.MonthlyReport.MonthlyReportRequest;
import com.sericulture.model.MonthlyReport.ReportMonthlyResponse;
import com.sericulture.model.VahivaatuReport.DistrictWise;
import com.sericulture.model.VahivaatuReport.RaceWiseReport;
import com.sericulture.model.VahivaatuReport.Report27bResponse;
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
import java.util.HashSet;
import java.util.Set;

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
        //String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("average_report"+Util.getISTLocalDate()+".xlsx");

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
        //String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("average_coccon_report"+Util.getISTLocalDate()+".xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @PostMapping("/audio-visual-report")
    public ResponseEntity<?> audioVisualReport(@RequestBody AudioVisualReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateAudioVisualExcel(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"audio_visual_report"+ Util.getISTLocalDate()+".csv")
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

    private void generateAudioVisualExcel(AudioVisualReportRequest requestDto) throws Exception {
        AudioVisualResponse reportDataResponse = apiService.audioVisualReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Audio visual header");

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("Market");
        subHeaderRow.createCell(2).setCellValue("Race");

        Row belowSubHeaderRow = sheet.createRow(2);

        int dynamicColumnStartsFrom = 3;
        int dynamicRowStartsFrom = 3;
        Set<Integer> createdRows = new HashSet<>();
        Set<Integer> innerRows = new HashSet<>();
        int weightDataCount = 3;
        for (int j = 0; j < reportDataResponse.getContent().getAudioReportResponse().getMonthWiseReports().size(); j++) {
            MonthWiseReport response = reportDataResponse.getContent().getAudioReportResponse().getMonthWiseReports().get(j);
            subHeaderRow.createCell(dynamicColumnStartsFrom).setCellValue(response.getMonth());
            belowSubHeaderRow.createCell(dynamicColumnStartsFrom).setCellValue("Weight");
            belowSubHeaderRow.createCell(dynamicColumnStartsFrom+1).setCellValue("Min");
            belowSubHeaderRow.createCell(dynamicColumnStartsFrom+2).setCellValue("Max");
            belowSubHeaderRow.createCell(dynamicColumnStartsFrom+3).setCellValue("Avg");

            int updateRaceWiseRowCount = dynamicRowStartsFrom;
            for(int k=0; k<response.getDtrMarketResponses().size(); k++){
                Row currentRow;
                if (createdRows.contains(updateRaceWiseRowCount)) {
                    // If the row has already been created, get it from the sheet
                    currentRow = sheet.getRow(updateRaceWiseRowCount);
                } else {
                    // Otherwise, create a new row and add its index to the set
                    currentRow = sheet.createRow(updateRaceWiseRowCount);
                    createdRows.add(updateRaceWiseRowCount);
                }
               // Row currentRow = sheet.createRow(updateRaceWiseRowCount);
                currentRow.createCell(0).setCellValue(k+1);

                DTRMarketResponse dtrMarketResponse = response.getDtrMarketResponses().get(k);
                currentRow.createCell(1).setCellValue(dtrMarketResponse.getMarketNameInKannada());

                int raceWiseRowCount = updateRaceWiseRowCount+1;
                for(int l=0; l<dtrMarketResponse.getDtrRaceResponses().size(); l++){
                    DTRRaceResponse dtrRaceResponse = dtrMarketResponse.getDtrRaceResponses().get(l);
                    if(l==0) {
                        currentRow.createCell(2).setCellValue(dtrRaceResponse.getRaceNameInKannada());
                        if(dtrRaceResponse.getDtrResponses() != null) {
                            if (dtrRaceResponse.getDtrResponses().size() > 0) {
                                for (int m = 0; m < dtrRaceResponse.getDtrResponses().size(); m++) {
                                    currentRow.createCell(weightDataCount).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getWeight());
                                    currentRow.createCell(weightDataCount+1).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getMinAmount());
                                    currentRow.createCell(weightDataCount+2).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getMaxAmount());
                                    currentRow.createCell(weightDataCount+3).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getAvgAmount());
                                }
                            }
                        }
                    }else{
                        Row currentRowRace;
                        if (innerRows.contains(raceWiseRowCount)) {
                            // If the row has already been created, get it from the sheet
                            currentRowRace = sheet.getRow(raceWiseRowCount);
                        } else {
                            // Otherwise, create a new row and add its index to the set
                            currentRowRace = sheet.createRow(raceWiseRowCount);
                            innerRows.add(raceWiseRowCount);
                        }
                        //Row currentRowRace = sheet.createRow(raceWiseRowCount);
                        currentRowRace.createCell(2).setCellValue(dtrRaceResponse.getRaceNameInKannada());
                        if(dtrRaceResponse.getDtrResponses() != null) {
                            if (dtrRaceResponse.getDtrResponses().size() > 0) {
                                for (int m = 0; m < dtrRaceResponse.getDtrResponses().size(); m++) {
                                    currentRowRace.createCell(weightDataCount).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getWeight());
                                    currentRowRace.createCell(weightDataCount+1).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getMinAmount());
                                    currentRowRace.createCell(weightDataCount+2).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getMaxAmount());
                                    currentRowRace.createCell(weightDataCount+3).setCellValue(dtrRaceResponse.getDtrResponses().get(m).getAvgAmount());
                                }
                            }
                        }
                        raceWiseRowCount = raceWiseRowCount+1;
                    }
                }
                updateRaceWiseRowCount = updateRaceWiseRowCount + dtrMarketResponse.getDtrRaceResponses().size();
            }
            weightDataCount = weightDataCount+ 4;
            dynamicColumnStartsFrom = dynamicColumnStartsFrom+4;
        }
        // Get the index of the last row
        int lastRowIndex = sheet.getLastRowNum();
        Row lastRow = sheet.createRow(lastRowIndex + 1);
        lastRow.createCell(2).setCellValue("Total");

        // Initialize the total sum
        int sumColumnIndex = 3;
        int minColumnIndex = 4;
        int maxColumnIndex = 5;
        int averageColumnIndex = 6;
        int totalColumnStartsFrom = 3;

        for (int j = 0; j < reportDataResponse.getContent().getAudioReportResponse().getMonthWiseReports().size(); j++) {
            double sum = 0.0;
            int count = 0;
            double maxValue = Double.MIN_VALUE;
            double minValue = Double.MAX_VALUE;
            double totalSum = 0.0;
            // Iterate through rows starting from row index 3
            for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (currentRow != null) {
                    Cell cell = currentRow.getCell(sumColumnIndex);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        // Add the cell value to the total sum
                        totalSum += Double.parseDouble(cell.getStringCellValue());
                    }

                    Cell minCell = currentRow.getCell(minColumnIndex);
                    if (minCell != null && minCell.getCellType() == CellType.STRING) {
                        // Compare the cell value with the current minimum value
                        double cellValue = Double.parseDouble(minCell.getStringCellValue());
                        if (cellValue < minValue) {
                            minValue = cellValue; // Update the minimum value
                        }
                    }

                    Cell maxCell = currentRow.getCell(maxColumnIndex);
                    if (maxCell != null && maxCell.getCellType() == CellType.STRING) {
                        // Compare the cell value with the current minimum value
                        double cellValue = Double.parseDouble(maxCell.getStringCellValue());
                        if (cellValue > maxValue) {
                            maxValue = cellValue; // Update the maximum value
                        }
                    }

                    Cell avgCell = currentRow.getCell(averageColumnIndex);
                    if (avgCell != null && avgCell.getCellType() == CellType.STRING) {
                        // Add the cell value to the sum and increment count
                        sum += Double.parseDouble(avgCell.getStringCellValue());
                        count++;
                    }
                }
            }
            double average = count > 0 ? sum / count : 0.0;
            lastRow.createCell(totalColumnStartsFrom).setCellValue(totalSum);
            lastRow.createCell(totalColumnStartsFrom+1).setCellValue(minValue);
            lastRow.createCell(totalColumnStartsFrom+2).setCellValue(maxValue);
            lastRow.createCell(totalColumnStartsFrom+3).setCellValue(average);
            totalColumnStartsFrom = totalColumnStartsFrom+4;
            averageColumnIndex = averageColumnIndex +4;
            maxColumnIndex = maxColumnIndex +4;
            minColumnIndex = minColumnIndex +4;
            sumColumnIndex = sumColumnIndex +4;
        }

        // Auto-size all columns
        for (int columnIndex = 1; columnIndex <= lastRow.getLastCellNum(); columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
       // String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("audio_visual_report"+Util.getISTLocalDate()+".xlsx");
      //  Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @PostMapping("/27-b-report")
    public ResponseEntity<?> get27bReport(@RequestBody MonthlyReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generate27bReport(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"27_b_report"+ Util.getISTLocalDate()+".csv")
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

    private void generate27bReport(MonthlyReportRequest requestDto) throws Exception {
        Report27bResponse reportDataResponse = apiService.get27bReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("header with month and year"+requestDto.getStartDate().getMonth() +"-"+requestDto.getStartDate().getYear());

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("District");

        Row subHeaderRow1 = sheet.createRow(2);

        int dynamicRaceStartsFrom = 2;
        if(reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().size()>0) {
            if (reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().get(0).getRaceWiseReports().size() > 0) {
                for (int j = 0; j < reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().get(0).getRaceWiseReports().size(); j++) {
                    RaceWiseReport raceWiseReport = reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().get(0).getRaceWiseReports().get(j);
                    subHeaderRow.createCell(dynamicRaceStartsFrom).setCellValue(raceWiseReport.getRaceName() +"Starting");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom).setCellValue("Utpaadane");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom+1).setCellValue("Vahivaatu");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom+2).setCellValue("Shekada");
                    subHeaderRow.createCell(dynamicRaceStartsFrom+3).setCellValue(raceWiseReport.getRaceName() +"Ending");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom+3).setCellValue("Utpaadane");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom+4).setCellValue("Vahivaatu");
                    subHeaderRow1.createCell(dynamicRaceStartsFrom+5).setCellValue("Shekada");
                    dynamicRaceStartsFrom = dynamicRaceStartsFrom + 6;
                }
            }
        }

        int dynamicRowStartsFrom = 3;
        for(int j=0; j<reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().size();j++){
            DistrictWise districtWise = reportDataResponse.getContent().getVahivaatuReport().getDistrictWises().get(j);

            Row currentRow = sheet.createRow(dynamicRowStartsFrom);
            currentRow.createCell(0).setCellValue(j+1);
            currentRow.createCell(1).setCellValue(districtWise.getDistrictName());
            int raceColumnStartsFrom = 2;
            for(int k=0;k<districtWise.getRaceWiseReports().size();k++){
                RaceWiseReport raceWiseReport = districtWise.getRaceWiseReports().get(k);
                if(raceWiseReport.getVahivaatuInfo() != null) {
                    currentRow.createCell(raceColumnStartsFrom).setCellValue(raceWiseReport.getVahivaatuInfo().getTotalCocoonStarting());
                    currentRow.createCell(raceColumnStartsFrom+1).setCellValue(raceWiseReport.getVahivaatuInfo().getSoldOutCocoonStarting());
                    currentRow.createCell(raceColumnStartsFrom+2).setCellValue("");
                    currentRow.createCell(raceColumnStartsFrom+3).setCellValue(raceWiseReport.getVahivaatuInfo().getTotalCocoonEnding());
                    currentRow.createCell(raceColumnStartsFrom+4).setCellValue(raceWiseReport.getVahivaatuInfo().getSoldOutCocoonEnding());
                    currentRow.createCell(raceColumnStartsFrom+5).setCellValue("");

                    raceColumnStartsFrom = raceColumnStartsFrom + 6;

                }

            }
            dynamicRowStartsFrom = dynamicRowStartsFrom+1;
        }

        Row totalRow = sheet.createRow(dynamicRowStartsFrom);
        totalRow.createCell(1).setCellValue("Total");
        int totalDynamicColumn = 2;
        if(reportDataResponse.getContent().getVahivaatuReport().getOverAllSum() != null) {
            for (int s = 0; s < reportDataResponse.getContent().getVahivaatuReport().getOverAllSum().getRaceWiseReports().size(); s++){
                RaceWiseReport raceWiseReport = reportDataResponse.getContent().getVahivaatuReport().getOverAllSum().getRaceWiseReports().get(s);
                if(raceWiseReport.getVahivaatuInfo() != null) {
                    totalRow.createCell(totalDynamicColumn).setCellValue(raceWiseReport.getVahivaatuInfo().getTotalCocoonStarting());
                    totalRow.createCell(totalDynamicColumn+1).setCellValue(raceWiseReport.getVahivaatuInfo().getSoldOutCocoonStarting());
                    totalRow.createCell(totalDynamicColumn+2).setCellValue("");
                    totalRow.createCell(totalDynamicColumn+3).setCellValue(raceWiseReport.getVahivaatuInfo().getTotalCocoonEnding());
                    totalRow.createCell(totalDynamicColumn+4).setCellValue(raceWiseReport.getVahivaatuInfo().getSoldOutCocoonEnding());
                    totalRow.createCell(totalDynamicColumn+5).setCellValue("");

                    totalDynamicColumn = totalDynamicColumn + 6;
                }
            }
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
       // String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("27_b_report"+Util.getISTLocalDate()+".xlsx");
      //  Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @PostMapping("/monthly-report")
    public ResponseEntity<?> getMonthlyReport(@RequestBody MonthlyReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateMonthlyReport(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"monthly_report"+ Util.getISTLocalDate()+".csv")
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

    private void generateMonthlyReport(MonthlyReportRequest requestDto) throws Exception {
        ReportMonthlyResponse reportDataResponse = apiService.getMonthlyReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("header with month and year"+requestDto.getStartDate().getMonth() +"-"+requestDto.getStartDate().getYear());

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("Race");
        subHeaderRow.createCell(2).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getThisYearDate());
        subHeaderRow.createCell(8).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getPrevYearDate());

        Row subHeaderRow1 = sheet.createRow(2);
        subHeaderRow1.createCell(2).setCellValue("Parimaana");
        subHeaderRow1.createCell(4).setCellValue("Moulya");
        subHeaderRow1.createCell(6).setCellValue("Saraasari");
        subHeaderRow1.createCell(8).setCellValue("Parimaana");
        subHeaderRow1.createCell(10).setCellValue("Moulya");
        subHeaderRow1.createCell(12).setCellValue("Saraasari");

        Row subHeaderRow2 = sheet.createRow(3);
        subHeaderRow2.createCell(2).setCellValue("Starting");
        subHeaderRow2.createCell(3).setCellValue("Ending");
        subHeaderRow2.createCell(4).setCellValue("Starting");
        subHeaderRow2.createCell(5).setCellValue("Ending");
        subHeaderRow2.createCell(6).setCellValue("Starting");
        subHeaderRow2.createCell(7).setCellValue("Ending");
        subHeaderRow2.createCell(8).setCellValue("Starting");
        subHeaderRow2.createCell(9).setCellValue("Ending");
        subHeaderRow2.createCell(10).setCellValue("Starting");
        subHeaderRow2.createCell(11).setCellValue("Ending");
        subHeaderRow2.createCell(12).setCellValue("Starting");
        subHeaderRow2.createCell(13).setCellValue("Ending");

        int dynamicRowStartsFrom = 4;
        double sumTStartWeight = 0.0;
        double sumTAmount = 0.0;
        double sumTAvg = 0.0;
        double sumTEStartWeight = 0.0;
        double sumTEAmount = 0.0;
        double sumTEAvg = 0.0;
        double sumPStartWeight = 0.0;
        double sumPAmount = 0.0;
        double sumPAvg = 0.0;
        double sumPEStartWeight = 0.0;
        double sumPEAmount = 0.0;
        double sumPEAvg = 0.0;
        for(int j=0; j<reportDataResponse.getContent().getMonthlyReportResponse().getMonthlyReportRaceWiseList().size(); j++){
            MonthlyReportRaceWise response = reportDataResponse.getContent().getMonthlyReportResponse().getMonthlyReportRaceWiseList().get(j);
            Row currentRow = sheet.createRow(dynamicRowStartsFrom);
            currentRow.createCell(0).setCellValue(j+1);
            currentRow.createCell(1).setCellValue(response.getRaceName());
            if(response.getThisYearReport() != null) {
                currentRow.createCell(2).setCellValue(response.getThisYearReport().getStartWeight());
                currentRow.createCell(3).setCellValue(response.getThisYearReport().getEndWeight());
                currentRow.createCell(4).setCellValue(response.getThisYearReport().getStartAmount());
                currentRow.createCell(5).setCellValue(response.getThisYearReport().getEndAmount());
                currentRow.createCell(6).setCellValue(response.getThisYearReport().getStartAvg());
                currentRow.createCell(7).setCellValue(response.getThisYearReport().getEndAvg());
            }
            if(response.getPrevYearReport() != null) {
                currentRow.createCell(8).setCellValue(response.getPrevYearReport().getStartWeight());
                currentRow.createCell(9).setCellValue(response.getPrevYearReport().getEndWeight());
                currentRow.createCell(10).setCellValue(response.getPrevYearReport().getStartAmount());
                currentRow.createCell(11).setCellValue(response.getPrevYearReport().getEndAmount());
                currentRow.createCell(12).setCellValue(response.getPrevYearReport().getStartAvg());
                currentRow.createCell(13).setCellValue(response.getPrevYearReport().getEndAvg());
            }
            Row differenceRow = sheet.createRow(dynamicRowStartsFrom+1);
            differenceRow.createCell(1).setCellValue("Difference");

            differenceRow.createCell(2).setCellValue(calculateDifference(response.getPrevYearReport().getStartWeight() ,response.getThisYearReport().getStartWeight()));
            differenceRow.createCell(3).setCellValue(calculateDifference(response.getPrevYearReport().getEndWeight() ,response.getThisYearReport().getEndAmount()));
            differenceRow.createCell(4).setCellValue(calculateDifference(response.getPrevYearReport().getStartAmount() ,response.getThisYearReport().getStartAmount()));
            differenceRow.createCell(5).setCellValue(calculateDifference(response.getPrevYearReport().getEndAmount() ,response.getThisYearReport().getEndAmount()));
            differenceRow.createCell(6).setCellValue(calculateDifference(response.getPrevYearReport().getStartAvg() ,response.getThisYearReport().getStartAvg()));
            differenceRow.createCell(7).setCellValue(calculateDifference(response.getPrevYearReport().getEndAvg() ,response.getThisYearReport().getEndAvg()));

            if(response.getThisYearReport().getStartWeight() != null) {
                if(!response.getThisYearReport().getStartWeight().equals("")) {
                    sumTStartWeight = sumTStartWeight + Double.parseDouble(response.getThisYearReport().getStartWeight());
                }
            }
            if(response.getThisYearReport().getEndWeight() != null) {
                if(!response.getThisYearReport().getEndWeight().equals("")) {
                    sumTEStartWeight = sumTEStartWeight + Double.parseDouble(response.getThisYearReport().getEndWeight());
                }
            }
            if(response.getThisYearReport().getStartAmount() != null) {
                if(!response.getThisYearReport().getStartAmount().equals("")) {
                    sumTAmount = sumTAmount + Double.parseDouble(response.getThisYearReport().getStartAmount());
                }
            }
            if(response.getThisYearReport().getEndAmount() != null) {
                if(!response.getThisYearReport().getEndAmount().equals("")) {
                    sumTEAmount = sumTEAmount + Double.parseDouble(response.getThisYearReport().getEndAmount());
                }
            }
            if(response.getThisYearReport().getStartAvg() != null) {
                if(!response.getThisYearReport().getStartAvg().equals("")) {
                    sumTAvg = sumTAvg + Double.parseDouble(response.getThisYearReport().getStartAvg());
                }
            }
            if(response.getThisYearReport().getEndAvg() != null) {
                if(!response.getThisYearReport().getEndAvg().equals("")) {
                    sumTEAvg = sumTEAvg + Double.parseDouble(response.getThisYearReport().getEndAvg());
                }
            }

            if(response.getPrevYearReport().getStartWeight() != null) {
                if(!response.getPrevYearReport().getStartWeight().equals("")) {
                    sumPStartWeight = sumPStartWeight + Double.parseDouble(response.getPrevYearReport().getStartWeight());
                }
            }
            if(response.getPrevYearReport().getEndWeight() != null) {
                if(!response.getPrevYearReport().getEndWeight().equals("")) {
                    sumPEStartWeight = sumPEStartWeight + Double.parseDouble(response.getPrevYearReport().getEndWeight());
                }
            }
            if(response.getPrevYearReport().getStartAmount() != null) {
                if(!response.getPrevYearReport().getStartAmount().equals("")) {
                    sumPAmount = sumPAmount + Double.parseDouble(response.getPrevYearReport().getStartAmount());
                }
            }
            if(response.getPrevYearReport().getEndAmount() != null) {
                if(!response.getPrevYearReport().getEndAmount().equals("")) {
                    sumPEAmount = sumPEAmount + Double.parseDouble(response.getPrevYearReport().getEndAmount());
                }
            }
            if(response.getPrevYearReport().getStartAvg() != null) {
                if(!response.getPrevYearReport().getStartAvg().equals("")) {
                    sumPAvg = sumPAvg + Double.parseDouble(response.getPrevYearReport().getStartAvg());
                }
            }
            if(response.getPrevYearReport().getEndAvg() != null) {
                if(!response.getPrevYearReport().getEndAvg().equals("")) {
                    sumPEAvg = sumPEAvg + Double.parseDouble(response.getPrevYearReport().getEndAvg());
                }
            }

            dynamicRowStartsFrom = dynamicRowStartsFrom+2;
        }

        Row overAllTotal = sheet.createRow(dynamicRowStartsFrom);
        overAllTotal.createCell(1).setCellValue("Over all Total");
        overAllTotal.createCell(2).setCellValue(sumTStartWeight);
        overAllTotal.createCell(3).setCellValue(sumTEStartWeight);
        overAllTotal.createCell(4).setCellValue(sumTAmount);
        overAllTotal.createCell(5).setCellValue(sumTEAmount);
        overAllTotal.createCell(6).setCellValue(sumTAvg);
        overAllTotal.createCell(7).setCellValue(sumTEAvg);
        overAllTotal.createCell(8).setCellValue(sumPStartWeight);
        overAllTotal.createCell(9).setCellValue(sumPEStartWeight);
        overAllTotal.createCell(10).setCellValue(sumPAmount);
        overAllTotal.createCell(11).setCellValue(sumPEAmount);
        overAllTotal.createCell(12).setCellValue(sumPAvg);
        overAllTotal.createCell(13).setCellValue(sumPEAvg);

        Row overAllDifference = sheet.createRow(dynamicRowStartsFrom+1);
        overAllDifference.createCell(1).setCellValue("Over all difference");
        overAllDifference.createCell(2).setCellValue(sumPStartWeight - sumTStartWeight);
        overAllDifference.createCell(3).setCellValue(sumPEStartWeight - sumTEStartWeight);
        overAllDifference.createCell(4).setCellValue(sumPAmount - sumTAmount);
        overAllDifference.createCell(5).setCellValue(sumPEAmount - sumTEAmount);
        overAllDifference.createCell(6).setCellValue(sumPAvg - sumTAvg);
        overAllDifference.createCell(7).setCellValue(sumPEAvg - sumTEAvg);

        double sumOTStartWeight = 0.0;
        double sumOTAmount = 0.0;
        double sumOTAvg = 0.0;
        double sumOTEStartWeight = 0.0;
        double sumOTEAmount = 0.0;
        double sumOTEAvg = 0.0;
        double sumOPStartWeight = 0.0;
        double sumOPAmount = 0.0;
        double sumOPAvg = 0.0;
        double sumOPEStartWeight = 0.0;
        double sumOPEAmount = 0.0;
        double sumOPEAvg = 0.0;
        Row tamilNadu = sheet.createRow(dynamicRowStartsFrom+1);
        if(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse() != null){
            tamilNadu.createCell(1).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getRaceName());
            if(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport() != null){
                tamilNadu.createCell(2).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getStartWeight());
                tamilNadu.createCell(3).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getEndWeight());
                tamilNadu.createCell(4).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getStartAmount());
                tamilNadu.createCell(5).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getEndAmount());
                tamilNadu.createCell(6).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getStartAvg());
                tamilNadu.createCell(7).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getThisYearReport();
                sumOTStartWeight = sumOTStartWeight + convertStringToDouble(res.getStartWeight());
                sumOTAmount = sumOTAmount + convertStringToDouble(res.getStartAmount());
                sumOTAvg = sumOTAvg + convertStringToDouble(res.getStartAvg());
                sumOTEStartWeight = sumOTEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOTEAmount = sumOTEAmount + convertStringToDouble(res.getEndAmount());
                sumOTEAvg = sumOTEAvg + convertStringToDouble(res.getEndAvg());
            }
            if(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport() != null){
                tamilNadu.createCell(8).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getStartWeight());
                tamilNadu.createCell(9).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getEndWeight());
                tamilNadu.createCell(10).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getStartAmount());
                tamilNadu.createCell(11).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getEndAmount());
                tamilNadu.createCell(12).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getStartAvg());
                tamilNadu.createCell(13).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getTamilNaduResponse().getPrevYearReport();
                sumOPStartWeight = sumOPStartWeight + convertStringToDouble(res.getStartWeight());
                sumOPAmount = sumOPAmount + convertStringToDouble(res.getStartAmount());
                sumOPAvg = sumOPAvg + convertStringToDouble(res.getStartAvg());
                sumOPEStartWeight = sumOPEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOPEAmount = sumOPEAmount + convertStringToDouble(res.getEndAmount());
                sumOPEAvg = sumOPEAvg + convertStringToDouble(res.getEndAvg());
            }
        }

        Row andra = sheet.createRow(dynamicRowStartsFrom+1);
        if(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse() != null){
            andra.createCell(1).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getRaceName());
            if(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport() != null){
                andra.createCell(2).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getStartWeight());
                andra.createCell(3).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getEndWeight());
                andra.createCell(4).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getStartAmount());
                andra.createCell(5).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getEndAmount());
                andra.createCell(6).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getStartAvg());
                andra.createCell(7).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getThisYearReport();
                sumOTStartWeight = sumOTStartWeight + convertStringToDouble(res.getStartWeight());
                sumOTAmount = sumOTAmount + convertStringToDouble(res.getStartAmount());
                sumOTAvg = sumOTAvg + convertStringToDouble(res.getStartAvg());
                sumOTEStartWeight = sumOTEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOTEAmount = sumOTEAmount + convertStringToDouble(res.getEndAmount());
                sumOTEAvg = sumOTEAvg + convertStringToDouble(res.getEndAvg());
            }
            if(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport() != null){
                andra.createCell(8).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getStartWeight());
                andra.createCell(9).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getEndWeight());
                andra.createCell(10).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getStartAmount());
                andra.createCell(11).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getEndAmount());
                andra.createCell(12).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getStartAvg());
                andra.createCell(13).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getAndraPradeshResponse().getPrevYearReport();
                sumOPStartWeight = sumOPStartWeight + convertStringToDouble(res.getStartWeight());
                sumOPAmount = sumOPAmount + convertStringToDouble(res.getStartAmount());
                sumOPAvg = sumOPAvg + convertStringToDouble(res.getStartAvg());
                sumOPEStartWeight = sumOPEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOPEAmount = sumOPEAmount + convertStringToDouble(res.getEndAmount());
                sumOPEAvg = sumOPEAvg + convertStringToDouble(res.getEndAvg());
            }
        }

        Row others = sheet.createRow(dynamicRowStartsFrom+3);
        if(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse() != null){
            others.createCell(1).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getRaceName());
            if(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport() != null){
                others.createCell(2).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getStartWeight());
                others.createCell(3).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getEndWeight());
                others.createCell(4).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getStartAmount());
                others.createCell(5).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getEndAmount());
                others.createCell(6).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getStartAvg());
                others.createCell(7).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getThisYearReport();
                sumOTStartWeight = sumOTStartWeight + convertStringToDouble(res.getStartWeight());
                sumOTAmount = sumOTAmount + convertStringToDouble(res.getStartAmount());
                sumOTAvg = sumOTAvg + convertStringToDouble(res.getStartAvg());
                sumOTEStartWeight = sumOTEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOTEAmount = sumOTEAmount + convertStringToDouble(res.getEndAmount());
                sumOTEAvg = sumOTEAvg + convertStringToDouble(res.getEndAvg());
            }
            if(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport() != null){
                others.createCell(8).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getStartWeight());
                others.createCell(9).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getEndWeight());
                others.createCell(10).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getStartAmount());
                others.createCell(11).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getEndAmount());
                others.createCell(12).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getStartAvg());
                others.createCell(13).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport().getEndAvg());

                MonthlyReportInfo res = reportDataResponse.getContent().getMonthlyReportResponse().getOtherStateResponse().getPrevYearReport();
                sumOPStartWeight = sumOPStartWeight + convertStringToDouble(res.getStartWeight());
                sumOPAmount = sumOPAmount + convertStringToDouble(res.getStartAmount());
                sumOPAvg = sumOPAvg + convertStringToDouble(res.getStartAvg());
                sumOPEStartWeight = sumOPEStartWeight + convertStringToDouble(res.getEndWeight());
                sumOPEAmount = sumOPEAmount + convertStringToDouble(res.getEndAmount());
                sumOPEAvg = sumOPEAvg + convertStringToDouble(res.getEndAvg());
            }
        }

        Row otherStateTotal = sheet.createRow(dynamicRowStartsFrom+4);
        otherStateTotal.createCell(1).setCellValue("Other State Total");
        otherStateTotal.createCell(2).setCellValue(sumOTStartWeight);
        otherStateTotal.createCell(3).setCellValue(sumOTAmount);
        otherStateTotal.createCell(4).setCellValue(sumOTAmount);
        otherStateTotal.createCell(5).setCellValue(sumOTEAmount);
        otherStateTotal.createCell(6).setCellValue(sumOTAvg);
        otherStateTotal.createCell(7).setCellValue(sumOTEAvg);
        otherStateTotal.createCell(8).setCellValue(sumOPStartWeight);
        otherStateTotal.createCell(9).setCellValue(sumOPEStartWeight);
        otherStateTotal.createCell(10).setCellValue(sumOPAmount);
        otherStateTotal.createCell(11).setCellValue(sumOPEAmount);
        otherStateTotal.createCell(12).setCellValue(sumOPAvg);
        otherStateTotal.createCell(13).setCellValue(sumOPEAvg);

        double karWeightStart = 0.0;
        double karWeightEnd = 0.0;
        double karAmountStart = 0.0;
        double karAmountEnd = 0.0;
        double karAvgStart = 0.0;
        double karAvgEnd = 0.0;
        Row karnataka = sheet.createRow(dynamicRowStartsFrom+5);
        if(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse() != null){
            karnataka.createCell(1).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getRaceName());
            MonthlyReportRaceWise res = reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse();
            if(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport() != null){
                karnataka.createCell(2).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getStartWeight());
                karnataka.createCell(3).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getEndWeight());
                karnataka.createCell(4).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getStartAmount());
                karnataka.createCell(5).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getEndAmount());
                karnataka.createCell(6).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getStartAvg());
                karnataka.createCell(7).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getThisYearReport().getEndAvg());
            }
            if(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport() != null){
                karnataka.createCell(8).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getStartWeight());
                karnataka.createCell(9).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getEndWeight());
                karnataka.createCell(10).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getStartAmount());
                karnataka.createCell(11).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getEndAmount());
                karnataka.createCell(12).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getStartAvg());
                karnataka.createCell(13).setCellValue(reportDataResponse.getContent().getMonthlyReportResponse().getKarnatakaResponse().getPrevYearReport().getEndAvg());
            }
            karWeightStart = Double.parseDouble(calculateDifference(res.getPrevYearReport().getStartWeight(), res.getThisYearReport().getStartWeight()));
            karWeightEnd = Double.parseDouble(calculateDifference(res.getPrevYearReport().getEndWeight(), res.getThisYearReport().getEndWeight()));
            karAmountStart = Double.parseDouble(calculateDifference(res.getPrevYearReport().getStartAmount(), res.getThisYearReport().getStartAmount()));
            karAmountEnd = Double.parseDouble(calculateDifference(res.getPrevYearReport().getEndAmount(), res.getThisYearReport().getEndAmount()));
            karAvgStart = Double.parseDouble(calculateDifference(res.getPrevYearReport().getStartAvg(), res.getThisYearReport().getStartAvg()));
            karAvgEnd = Double.parseDouble(calculateDifference(res.getPrevYearReport().getEndAvg(), res.getThisYearReport().getEndAvg()));

        }

        Row karnatakaDiff = sheet.createRow(dynamicRowStartsFrom+6);
        karnatakaDiff.createCell(2).setCellValue(karWeightStart);
        karnatakaDiff.createCell(3).setCellValue(karWeightEnd);
        karnatakaDiff.createCell(4).setCellValue(karAmountStart);
        karnatakaDiff.createCell(5).setCellValue(karAmountEnd);
        karnatakaDiff.createCell(6).setCellValue(karAvgStart);
        karnatakaDiff.createCell(7).setCellValue(karAvgEnd);
        karnatakaDiff.createCell(1).setCellValue("Karnataka difference");

        Row lots = sheet.createRow(dynamicRowStartsFrom+7);
        lots.createCell(1).setCellValue("LOTS");

        Row marketFee = sheet.createRow(dynamicRowStartsFrom+8);
        marketFee.createCell(1).setCellValue("MARKET FEE");

        // Auto-size all columns
        for (int columnIndex = 1; columnIndex <= sheet.getRow(4).getLastCellNum(); columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
        //String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("monthly_report"+Util.getISTLocalDate()+".xlsx");
      //  Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private String calculateDifference(String prevYear, String thisYear){
        if(prevYear == null){
            prevYear = "0.0";
        }else{
            if(prevYear.equals("")){
                prevYear = "0.0";
            }
        }
        if(thisYear == null){
            thisYear = "0.0";
        }else{
            if(thisYear.equals("")){
                thisYear = "0.0";
            }
        }
        return String.valueOf(Double.parseDouble(prevYear) - Double.parseDouble(thisYear));
    }

    private double convertStringToDouble(String amount){
        if(amount == null){
            amount = "0.0";
        }else{
            if(amount.equals("")){
                amount = "0.0";
            }
        }
        return Double.parseDouble(amount);
    }

    @PostMapping("/market-report")
    public ResponseEntity<?> getMarketReport(@RequestBody MonthlyReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateMarketReport(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"market_report"+ Util.getISTLocalDate()+".csv")
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

    private void generateMarketReport(MonthlyReportRequest requestDto) throws Exception {
        MarketResponse reportDataResponse = apiService.getMarketReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("header with month and year"+requestDto.getStartDate().getMonth() +"-"+requestDto.getStartDate().getYear());

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("Market");

        int dynamicColumnStartsFrom = 3;
        int raceNameStartsFrom = 2;
        Row subHeader = sheet.createRow(2);

        for (int j = 0; j < 1; j++) {
            MarketWiseInfo response = reportDataResponse.getContent().getMarketReports().getMarketWiseInfos().get(j);
            Row currentRow = sheet.createRow(dynamicColumnStartsFrom);
            currentRow.createCell(0).setCellValue(j+1);
            currentRow.createCell(1).setCellValue(response.getMarketName());
            dynamicColumnStartsFrom = dynamicColumnStartsFrom+1;

            for(int k=0; k<response.getMarketReportRaceWises().size();k++) {
                MarketReportRaceWise marketReportRaceWise = response.getMarketReportRaceWises().get(k);
                subHeaderRow.createCell(raceNameStartsFrom).setCellValue(marketReportRaceWise.getRaceName() + "(starting)");
                subHeaderRow.createCell(raceNameStartsFrom+3).setCellValue(marketReportRaceWise.getRaceName() + "(ending)");

                subHeader.createCell(raceNameStartsFrom).setCellValue("Weight");
                subHeader.createCell(raceNameStartsFrom+1).setCellValue("Amount");
                subHeader.createCell(raceNameStartsFrom+2).setCellValue("Avg");

                subHeader.createCell(raceNameStartsFrom+3).setCellValue("Weight");
                subHeader.createCell(raceNameStartsFrom+4).setCellValue("Amount");
                subHeader.createCell(raceNameStartsFrom+5).setCellValue("Avg");

                raceNameStartsFrom = raceNameStartsFrom + 6;
            }

            subHeaderRow.createCell(raceNameStartsFrom).setCellValue("Total pramana");
            subHeader.createCell(raceNameStartsFrom).setCellValue("Month Starting");
            subHeader.createCell(raceNameStartsFrom+1).setCellValue("Month Ending");

            subHeaderRow.createCell(raceNameStartsFrom+2).setCellValue("Total moulya");
            subHeader.createCell(raceNameStartsFrom+2).setCellValue("Month Starting");
            subHeader.createCell(raceNameStartsFrom+3).setCellValue("Month Ending");

            subHeaderRow.createCell(raceNameStartsFrom+4).setCellValue("Sarasari Dharane");
            subHeader.createCell(raceNameStartsFrom+4).setCellValue("Month Starting");
            subHeader.createCell(raceNameStartsFrom+5).setCellValue("Month Ending");

            subHeaderRow.createCell(raceNameStartsFrom+6).setCellValue("Markukatte shulka");
            subHeader.createCell(raceNameStartsFrom+6).setCellValue("Month Starting");
            subHeader.createCell(raceNameStartsFrom+7).setCellValue("Month Ending");

            subHeaderRow.createCell(raceNameStartsFrom+8).setCellValue("Thandagalu");
            subHeader.createCell(raceNameStartsFrom+8).setCellValue("Month Starting");
            subHeader.createCell(raceNameStartsFrom+9).setCellValue("Month Ending");

        }

        int dynamicColumnDataStartsFrom = 3;
        int raceNameDataStartsFrom = 2;
        for (int j = 0; j < reportDataResponse.getContent().getMarketReports().getMarketWiseInfos().size(); j++) {
            MarketWiseInfo response = reportDataResponse.getContent().getMarketReports().getMarketWiseInfos().get(j);
            Row currentRow = sheet.createRow(dynamicColumnDataStartsFrom);
            currentRow.createCell(0).setCellValue(j+1);
            currentRow.createCell(1).setCellValue(response.getMarketName());

            for(int k=0; k<response.getMarketReportRaceWises().size();k++) {
                MarketReportRaceWise marketReportRaceWise = response.getMarketReportRaceWises().get(k);
                if(marketReportRaceWise.getMarketReportInfo() != null){
                    currentRow.createCell(raceNameDataStartsFrom).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                    currentRow.createCell(raceNameDataStartsFrom+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                    currentRow.createCell(raceNameDataStartsFrom+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                    currentRow.createCell(raceNameDataStartsFrom+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                    currentRow.createCell(raceNameDataStartsFrom+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                    currentRow.createCell(raceNameDataStartsFrom+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                }
                raceNameDataStartsFrom = raceNameDataStartsFrom + 6;
            }
            currentRow.createCell(raceNameDataStartsFrom).setCellValue(response.getTotalWeightStarting());
            currentRow.createCell(raceNameDataStartsFrom+1).setCellValue(response.getTotalWeightEnding());
            currentRow.createCell(raceNameDataStartsFrom+2).setCellValue(response.getTotalAmountStarting());
            currentRow.createCell(raceNameDataStartsFrom+3).setCellValue(response.getTotalAmountEnding());
            currentRow.createCell(raceNameDataStartsFrom+4).setCellValue(response.getAvgAmountStarting());
            currentRow.createCell(raceNameDataStartsFrom+5).setCellValue(response.getAvgAmountEnding());
            currentRow.createCell(raceNameDataStartsFrom+6).setCellValue(response.getMarketFeeStarting());
            currentRow.createCell(raceNameDataStartsFrom+7).setCellValue(response.getMarketFeeEnding());
            currentRow.createCell(raceNameDataStartsFrom+8).setCellValue(response.getLotsStarting());
            currentRow.createCell(raceNameDataStartsFrom+9).setCellValue(response.getLotsEnding());

            raceNameDataStartsFrom = 2;

            dynamicColumnDataStartsFrom = dynamicColumnDataStartsFrom+1;

        }
        // Get the index of the last row
        int lastRowIndex = sheet.getLastRowNum();
        Row lastRow = sheet.createRow(lastRowIndex + 1);
        lastRow.createCell(1).setCellValue("Total");

        // Initialize the total sum
        int sumWeightIndex = 2;
        int sumAmountIndex = 3;
        int avgAmountIndex = 4;

        int totalColumnStartsFrom = 2;

        if(reportDataResponse.getContent().getMarketReports().getMarketWiseInfos().size()>0) {
            for (int j = 0; j < (reportDataResponse.getContent().getMarketReports().getMarketWiseInfos().get(0).getMarketReportRaceWises().size() * 2); j++) {
                double sum = 0.0;
                int count = 0;
                double totalWeightSum = 0.0;
                double totalAmountSum = 0.0;
                // Iterate through rows starting from row index 3
                for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);
                    if (currentRow != null) {
                        Cell weightCell = currentRow.getCell(sumWeightIndex);
                        if (weightCell != null && weightCell.getCellType() == CellType.STRING) {
                            // Add the cell value to the total sum
                            totalWeightSum += Double.parseDouble(weightCell.getStringCellValue());
                        }

                        Cell amountCell = currentRow.getCell(sumAmountIndex);
                        if (amountCell != null && amountCell.getCellType() == CellType.STRING) {
                            // Add the cell value to the total sum
                            totalAmountSum += Double.parseDouble(amountCell.getStringCellValue());
                        }

                        Cell avgCell = currentRow.getCell(avgAmountIndex);
                        if (avgCell != null && avgCell.getCellType() == CellType.STRING) {
                            // Add the cell value to the sum and increment count
                            sum += Double.parseDouble(avgCell.getStringCellValue());
                            count++;
                        }
                    }
                }
                double average = count > 0 ? sum / count : 0.0;
                lastRow.createCell(totalColumnStartsFrom).setCellValue(totalWeightSum);
                lastRow.createCell(totalColumnStartsFrom + 1).setCellValue(totalAmountSum);
                lastRow.createCell(totalColumnStartsFrom + 2).setCellValue(average);
                avgAmountIndex = avgAmountIndex + 3;
                sumAmountIndex = sumAmountIndex + 3;
                sumWeightIndex = sumWeightIndex + 3;
                totalColumnStartsFrom = totalColumnStartsFrom + 3;
            }
        }

        double sumWeight1 = 0.0;
        double sumWeight2 = 0.0;
        double sumAmount1 = 0.0;
        double sumAmount2 = 0.0;
        double avg1 = 0.0;
        double avg2 = 0.0;
        double market1 = 0.0;
        double market2 = 0.0;
        double lot1 = 0.0;
        double lot2 = 0.0;
        for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            if (currentRow != null) {
                Cell weightCell = currentRow.getCell(totalColumnStartsFrom);
                if (weightCell != null && weightCell.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    sumWeight1 += Double.parseDouble(weightCell.getStringCellValue());
                }

                Cell weightCell1 = currentRow.getCell(totalColumnStartsFrom+1);
                if (weightCell1 != null && weightCell1.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    sumWeight2 += Double.parseDouble(weightCell1.getStringCellValue());
                }

                Cell amountCell = currentRow.getCell(totalColumnStartsFrom+2);
                if (amountCell != null && amountCell.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    sumAmount1 += Double.parseDouble(amountCell.getStringCellValue());
                }

                Cell amountCell1 = currentRow.getCell(totalColumnStartsFrom+3);
                if (amountCell1 != null && amountCell1.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    sumAmount2 += Double.parseDouble(amountCell1.getStringCellValue());
                }

                Cell avgCell = currentRow.getCell(totalColumnStartsFrom+4);
                if (avgCell != null && avgCell.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    avg1 += Double.parseDouble(avgCell.getStringCellValue());
                }

                Cell avgCell1 = currentRow.getCell(totalColumnStartsFrom+5);
                if (avgCell1 != null && avgCell1.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    avg2 += Double.parseDouble(avgCell1.getStringCellValue());
                }

                Cell marketCell = currentRow.getCell(totalColumnStartsFrom+6);
                if (marketCell != null && !marketCell.toString().equals("") &&marketCell.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    market1 += Double.parseDouble(marketCell.getStringCellValue());
                }

                Cell marketCell1 = currentRow.getCell(totalColumnStartsFrom+7);
                if (marketCell1 != null && !marketCell1.toString().equals("") && marketCell1.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    market2 += Double.parseDouble(marketCell1.getStringCellValue());
                }

                Cell lotCell = currentRow.getCell(totalColumnStartsFrom+8);
                if (lotCell != null && !lotCell.toString().equals("") && lotCell.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    lot1 += Double.parseDouble(lotCell.getStringCellValue());
                }

                Cell lotCell1 = currentRow.getCell(totalColumnStartsFrom+9);
                if (lotCell1 != null && !lotCell1.toString().equals("") && lotCell1.getCellType() == CellType.STRING) {
                    // Add the cell value to the total sum
                    lot2 += Double.parseDouble(lotCell1.getStringCellValue());
                }
            }
        }
        lastRow.createCell(totalColumnStartsFrom).setCellValue(sumWeight1);
        lastRow.createCell(totalColumnStartsFrom+1).setCellValue(sumWeight2);
        lastRow.createCell(totalColumnStartsFrom+2).setCellValue(sumAmount1);
        lastRow.createCell(totalColumnStartsFrom+3).setCellValue(sumAmount2);
        lastRow.createCell(totalColumnStartsFrom+4).setCellValue(avg1);
        lastRow.createCell(totalColumnStartsFrom+5).setCellValue(avg2);
        lastRow.createCell(totalColumnStartsFrom+6).setCellValue(market1);
        lastRow.createCell(totalColumnStartsFrom+7).setCellValue(market2);
        lastRow.createCell(totalColumnStartsFrom+8).setCellValue(lot1);
        lastRow.createCell(totalColumnStartsFrom+9).setCellValue(lot2);

        // Auto-size all columns
        for (int columnIndex = 1; columnIndex <= sheet.getRow(2).getLastCellNum(); columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }
        // Write the workbook content to a file
        // Specify the directory where the file will be saved
        //String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("market_report"+Util.getISTLocalDate()+".xlsx");
        //Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @PostMapping("/district-report")
    public ResponseEntity<?> getDistrictReport(@RequestBody MonthlyReportRequest request) {
        try {
            System.out.println("enter to dtr online report pdf");
            logger.info("enter to dtr online report pdf");
            generateDistrictReport(request);

            FileInputStream fileInputStream = new FileInputStream("sample.xlsx");
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +"district_report"+ Util.getISTLocalDate()+".csv")
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

    private void generateDistrictReport(MonthlyReportRequest requestDto) throws Exception {
        DivisionResponse reportDataResponse = apiService.getDistrictWiseReport(requestDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("header with month and year"+requestDto.getStartDate().getMonth() +"-"+requestDto.getStartDate().getYear());

        Row subHeaderRow = sheet.createRow(1);
        subHeaderRow.createCell(0).setCellValue("Serial No");
        subHeaderRow.createCell(1).setCellValue("District");

        Row subHeaderRow1 = sheet.createRow(2);

        int dynamicColumnStartsFrom = 2;
        if(reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().size()>0) {
            if(reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().size()>0) {
                if(reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().get(0).getMarketReportRaceWises().size()>0) {
                    for (int j = 0; j < reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().get(0).getMarketReportRaceWises().size(); j++) {
                        subHeaderRow.createCell(dynamicColumnStartsFrom).setCellValue(reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().get(0).getMarketReportRaceWises().get(j).getRaceName());
                        subHeaderRow1.createCell(dynamicColumnStartsFrom).setCellValue("Weight Starting");
                        subHeaderRow1.createCell(dynamicColumnStartsFrom+1).setCellValue("Weight Ending");
                        subHeaderRow1.createCell(dynamicColumnStartsFrom+2).setCellValue("Amount Starting");
                        subHeaderRow1.createCell(dynamicColumnStartsFrom+3).setCellValue("Amount Ending");
                        subHeaderRow1.createCell(dynamicColumnStartsFrom+4).setCellValue("Avg Starting");
                        subHeaderRow1.createCell(dynamicColumnStartsFrom+5).setCellValue("Avg Ending");
                        dynamicColumnStartsFrom = dynamicColumnStartsFrom +6;
                    }
                    subHeaderRow1.createCell(dynamicColumnStartsFrom).setCellValue("Total Starting Weight");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+1).setCellValue("Total Ending Weight");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+2).setCellValue("Total Starting Amount");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+3).setCellValue("Total Ending Amount");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+4).setCellValue("Total Starting Avg");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+5).setCellValue("Total Ending Avg");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+2).setCellValue("Total Starting MF");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+3).setCellValue("Total Ending MF");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+4).setCellValue("Total Starting Lot");
                    subHeaderRow1.createCell(dynamicColumnStartsFrom+5).setCellValue("Total Ending Lot");

                }
            }
        }

        int dynamicRowStartsFrom = 3;
        for (int j = 0; j < reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().size(); j++) {
            DivisionReport divisionReport = reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(j);
            Row divisionRow = sheet.createRow(dynamicRowStartsFrom);
            divisionRow.createCell(1).setCellValue(divisionReport.getDivisionName());
            int dynamicRowStartsFromRace = dynamicRowStartsFrom + 1;
            double sumSWeight = 0.0;
            double sumEWeight = 0.0;
            double sumSAmount = 0.0;
            double sumEAmount = 0.0;
            double sumSAvg = 0.0;
            double sumEAvg = 0.0;
            for (int k = 0; k < divisionReport.getMarketWiseInfoList().size(); k++) {
                MarketWiseInfo marketWiseInfo = divisionReport.getMarketWiseInfoList().get(k);
                Row currentRow = sheet.createRow(dynamicRowStartsFromRace);
                currentRow.createCell(1).setCellValue(marketWiseInfo.getMarketName());

                int raceDynamicColumnStartsFrom = 2;
                for (int l = 0; l < marketWiseInfo.getMarketReportRaceWises().size(); l++) {
                    MarketReportRaceWise marketReportRaceWise = marketWiseInfo.getMarketReportRaceWises().get(l);
                    if (marketReportRaceWise.getMarketReportInfo() != null) {
                        currentRow.createCell(raceDynamicColumnStartsFrom).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                        currentRow.createCell(raceDynamicColumnStartsFrom + 1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                        currentRow.createCell(raceDynamicColumnStartsFrom + 2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                        currentRow.createCell(raceDynamicColumnStartsFrom + 3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                        currentRow.createCell(raceDynamicColumnStartsFrom + 4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                        currentRow.createCell(raceDynamicColumnStartsFrom + 5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                        raceDynamicColumnStartsFrom = raceDynamicColumnStartsFrom + 6;

                        sumSWeight = sumSWeight + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                        sumEWeight = sumEWeight + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                        sumSAmount = sumSAmount + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                        sumEAmount = sumEAmount + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                        sumSAvg = sumSAvg + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                        sumEAvg = sumEAvg + convertStringToDouble(marketReportRaceWise.getMarketReportInfo().getEndingAvg());

                    }
                }
                currentRow.createCell(raceDynamicColumnStartsFrom).setCellValue(marketWiseInfo.getTotalWeightStarting());
                currentRow.createCell(raceDynamicColumnStartsFrom + 1).setCellValue(marketWiseInfo.getTotalWeightEnding());
                currentRow.createCell(raceDynamicColumnStartsFrom + 2).setCellValue(marketWiseInfo.getTotalAmountStarting());
                currentRow.createCell(raceDynamicColumnStartsFrom + 3).setCellValue(marketWiseInfo.getTotalAmountEnding());
                currentRow.createCell(raceDynamicColumnStartsFrom + 4).setCellValue(marketWiseInfo.getAvgAmountStarting());
                currentRow.createCell(raceDynamicColumnStartsFrom + 5).setCellValue(marketWiseInfo.getAvgAmountEnding());
                currentRow.createCell(raceDynamicColumnStartsFrom + 6).setCellValue(marketWiseInfo.getMarketFeeStarting());
                currentRow.createCell(raceDynamicColumnStartsFrom + 7).setCellValue(marketWiseInfo.getMarketFeeEnding());
                currentRow.createCell(raceDynamicColumnStartsFrom + 8).setCellValue(marketWiseInfo.getLotsStarting());
                currentRow.createCell(raceDynamicColumnStartsFrom + 9).setCellValue(marketWiseInfo.getLotsEnding());
                dynamicRowStartsFromRace = dynamicRowStartsFromRace + 1;
            }
            Row divisionSpecificSum = sheet.createRow(dynamicRowStartsFromRace);
            divisionSpecificSum.createCell(1).setCellValue("Sum");
            /*int dynamicColumnStartsFrom1 = 2;
            if(reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().size()>0) {
                if (reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().size() > 0) {
                    if (reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().get(0).getMarketReportRaceWises().size() > 0) {
                        for (int s = 0; s < reportDataResponse.getContent().getDivisionWiseReport().getDivisionReportList().get(0).getMarketWiseInfoList().get(0).getMarketReportRaceWises().size(); s++) {
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1).setCellValue(sumSWeight);
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1+1).setCellValue(sumEWeight);
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1+2).setCellValue(sumSAmount);
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1+3).setCellValue(sumEAmount);
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1+4).setCellValue(sumSAvg);
                            divisionSpecificSum.createCell(dynamicColumnStartsFrom1+5).setCellValue(sumEAvg);
                            dynamicColumnStartsFrom1 = dynamicColumnStartsFrom1 + 6;
                        }
                    }
                }
            }*/
            if(divisionReport.getDivisionWiseSum().size()>0) {
                int dynamicColumnStartsFrom1 = 2;
                for (int s = 0; s < divisionReport.getDivisionWiseSum().get(0).getMarketReportRaceWises().size(); s++) {
                    MarketReportRaceWise divisionSpecific = divisionReport.getDivisionWiseSum().get(0).getMarketReportRaceWises().get(s);
                    if(divisionSpecific.getMarketReportInfo() != null) {
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1).setCellValue(divisionSpecific.getMarketReportInfo().getStartingWeight());
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1 + 1).setCellValue(divisionSpecific.getMarketReportInfo().getEndingWeight());
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1 + 2).setCellValue(divisionSpecific.getMarketReportInfo().getStartingAmount());;
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1 + 3).setCellValue(divisionSpecific.getMarketReportInfo().getEndingAmount());
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1 + 4).setCellValue(divisionSpecific.getMarketReportInfo().getStartingAvg());
                        divisionSpecificSum.createCell(dynamicColumnStartsFrom1 + 5).setCellValue(divisionSpecific.getMarketReportInfo().getEndingAvg());
                    }
                    dynamicColumnStartsFrom1 = dynamicColumnStartsFrom1 + 6;
                }
            }
            dynamicRowStartsFrom = dynamicRowStartsFrom + (divisionReport.getMarketWiseInfoList().size()+2);
        }


        Row karRow = sheet.createRow(dynamicRowStartsFrom);
        karRow.createCell(1).setCellValue("Rajyada ottu");
        int dynKar = 2;
        for(int x=0; x<reportDataResponse.getContent().getKarnatakaData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getKarnatakaData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                karRow.createCell(dynKar).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                karRow.createCell(dynKar+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                karRow.createCell(dynKar+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                karRow.createCell(dynKar+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                karRow.createCell(dynKar+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                karRow.createCell(dynKar+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynKar = dynKar + 6;
            }
        }

        Row andraRow = sheet.createRow(dynamicRowStartsFrom+1);
        andraRow.createCell(1).setCellValue("Andra");
        int dynAnd = 2;
        for(int x=0; x<reportDataResponse.getContent().getAndraData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getAndraData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                andraRow.createCell(dynAnd).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                andraRow.createCell(dynAnd+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                andraRow.createCell(dynAnd+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                andraRow.createCell(dynAnd+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                andraRow.createCell(dynAnd+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                andraRow.createCell(dynAnd+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynAnd = dynAnd + 6;
            }
        }

        Row tamilNaduRow = sheet.createRow(dynamicRowStartsFrom+2);
        tamilNaduRow.createCell(1).setCellValue("Tamilnaadu");
        int dynTam = 2;
        for(int x=0; x<reportDataResponse.getContent().getTamilNaduData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getTamilNaduData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                tamilNaduRow.createCell(dynTam).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                tamilNaduRow.createCell(dynTam+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                tamilNaduRow.createCell(dynTam+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                tamilNaduRow.createCell(dynTam+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                tamilNaduRow.createCell(dynTam+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                tamilNaduRow.createCell(dynTam+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynTam = dynTam + 6;
            }
        }

        Row maharashtraRow = sheet.createRow(dynamicRowStartsFrom+3);
        maharashtraRow.createCell(1).setCellValue("Maharashtra");
        int dynMah = 2;
        for(int x=0; x<reportDataResponse.getContent().getMaharashtraData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getMaharashtraData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                maharashtraRow.createCell(dynMah).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                maharashtraRow.createCell(dynMah+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                maharashtraRow.createCell(dynMah+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                maharashtraRow.createCell(dynMah+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                maharashtraRow.createCell(dynMah+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                maharashtraRow.createCell(dynMah+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynMah = dynMah + 6;
            }
        }

        Row ithareRow = sheet.createRow(dynamicRowStartsFrom+4);
        ithareRow.createCell(1).setCellValue("Ithare");
        int dynOth = 2;
        for(int x=0; x<reportDataResponse.getContent().getOtherStateData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getOtherStateData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                ithareRow.createCell(dynOth).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                ithareRow.createCell(dynOth+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                ithareRow.createCell(dynOth+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                ithareRow.createCell(dynOth+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                ithareRow.createCell(dynOth+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                ithareRow.createCell(dynOth+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynOth = dynOth + 6;
            }
        }

        Row ithareOttuRow = sheet.createRow(dynamicRowStartsFrom+5);
        ithareOttuRow.createCell(1).setCellValue("Ithare Rajyada ottu");
        int dynOthTot = 2;
        for(int x=0; x<reportDataResponse.getContent().getOtherStateExcKarData().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getOtherStateExcKarData().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                ithareOttuRow.createCell(dynOthTot).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                ithareOttuRow.createCell(dynOthTot+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                ithareOttuRow.createCell(dynOthTot+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                ithareOttuRow.createCell(dynOthTot+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                ithareOttuRow.createCell(dynOthTot+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                ithareOttuRow.createCell(dynOthTot+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynOthTot = dynOthTot + 6;
            }
        }

        Row ellaOttu = sheet.createRow(dynamicRowStartsFrom+6);
        ellaOttu.createCell(1).setCellValue("Ella ottu");
        int dynAllOTot = 2;
        for(int x=0; x<reportDataResponse.getContent().getOverAllStateTotal().size();x++){
            MarketReportRaceWise marketReportRaceWise = reportDataResponse.getContent().getOverAllStateTotal().get(x);
            if(marketReportRaceWise.getMarketReportInfo() != null) {
                ellaOttu.createCell(dynAllOTot).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingWeight());
                ellaOttu.createCell(dynAllOTot+1).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingWeight());
                ellaOttu.createCell(dynAllOTot+2).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAmount());
                ellaOttu.createCell(dynAllOTot+3).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAmount());
                ellaOttu.createCell(dynAllOTot+4).setCellValue(marketReportRaceWise.getMarketReportInfo().getStartingAvg());
                ellaOttu.createCell(dynAllOTot+5).setCellValue(marketReportRaceWise.getMarketReportInfo().getEndingAvg());
                dynAllOTot = dynAllOTot + 6;
            }
        }


            // Auto-size all columns
        for (int columnIndex = 1; columnIndex <= ellaOttu.getLastCellNum(); columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }

        // Write the workbook content to a file
        // Specify the directory where the file will be saved
       // String directoryPath = "C:\\Users\\Swathi V S\\Downloads\\";
        // Specify the directory where the file will be saved
        String userHome = System.getProperty("user.home");

        // Define the directory path relative to the user's home directory
        String directoryPath = Paths.get(userHome, "Downloads").toString();
        Path directory = Paths.get(directoryPath);
        Files.createDirectories(directory);
        Path filePath = directory.resolve("district_report"+Util.getISTLocalDate()+".xlsx");

//        Path filePath = directory.resolve("sample.xlsx");

        // Write the workbook content to the specified file path
        FileOutputStream fileOut = new FileOutputStream(filePath.toString());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
