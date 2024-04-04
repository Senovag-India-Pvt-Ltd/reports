package com.sericulture.model.MarketWiseReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sericulture.model.MarketReport.MarketReportRaceWise;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionWiseMarketResponse {
    private DivisionWiseReport divisionWiseReport;
    private List<MarketReportRaceWise> karnatakaData;
    private List<MarketReportRaceWise> andraData;
    private List<MarketReportRaceWise> tamilNaduData;
    private List<MarketReportRaceWise> maharashtraData;
    private List<MarketReportRaceWise> otherStateData;
    private List<MarketReportRaceWise> otherStateExcKarData;
    private List<MarketReportRaceWise> overAllStateTotal;
}