package com.sericulture.controller;


import com.sericulture.helper.Util;
import com.sericulture.model.*;
import com.sericulture.model.AudioVisual.AudioVisualReportRequest;
import com.sericulture.model.AudioVisual.AudioVisualResponse;
import com.sericulture.model.AudioVisual.MonthWiseReport;
import com.sericulture.model.DTRAllMarket.DTRMarketResponse;
import com.sericulture.model.DTRAllMarket.DTRRaceResponse;
import com.sericulture.model.MarketReport.MarketResponse;
import com.sericulture.model.MarketWiseReport.DivisionResponse;
import com.sericulture.model.MonthlyReport.MonthlyReportRequest;
import com.sericulture.model.MonthlyReport.ReportMonthlyResponse;
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
        subHeaderRow.createCell(1).setCellValue("Race");

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
        subHeaderRow.createCell(1).setCellValue("Race");

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
