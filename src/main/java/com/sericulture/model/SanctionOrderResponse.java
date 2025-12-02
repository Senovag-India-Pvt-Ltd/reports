package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SanctionOrderResponse {
    private String logurl;
    private String arn;
    private String addressText;
    private String acceptedDate;
    private String header;
    private String header1;
    private String header2;
    private String header3;
    private String header4;
    private String header5;
    private String header6;
    private String header7;
    private String header8;
    private String header9;
    private String header10;
    private String header11;
    private String header12;
    private String header13;
    private String header14;
    private String header15;
    private String header16;
    private String header17;
    private String header18;
    private String header19;
    private String header20;
    private String header21;
    private String header22;
    private String header23;
    private String header24;
    private String header25;
    private String header26;
    private String header27;
    private String date;
    private String farmerFirstName;
    private String farmerNumber;
    private String fruitsId;
    private String farmerAddressText;
    private String districtName;
    private String talukName;
    private String hobliName;
    private String villageName;
    private String farmerAccountNumber;
    private String farmerBankName;
    private String farmerBankIfsc;
    private String farmerBranchName;
    private String lineItemComment;
    private Float cost;
    private String vendorName;
    private String vendorAccountNumber;
    private String vendorBankName;
    private String vendorBankIfsc;
    private String vendorBranchName;
    private String vendorUpi;
    private String financialYear;
    private String sanctionNo;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String fatherNameKan;
    private String mobileNumber;
    private String scComponentName;
    private String surveyNumber;
    private String scCategoryName;
    private String scHeadAccountName;
    private String sanctionOrderNumber;
    private Float sanctionAmount;
    private String workOrderNumber;
    private Long schemeId;
    private Long subSchemeId;
    private Long approvalStageId;
    private Long categoryId;
    private Long componentId;
    private String hectareName;
    private String spacingName;
    private String referenceNo;
    private String tscName;
    // new
    private Integer serialNumber;
    private int index;
    private Long applicationFormId;
    private String farmerMiddleName;
    private String farmerLastName;
    private String workFlowType;
    private String action;
    private Float eligibleAmount;
    private String khazaneRecipientId;
    private Long biddingSlipLotNo;
    private Float  totalSchemeAmount;
    private Float  lotWeight;
    private Float  perKgRate;
    private Float  cocoonsWeight;
//    private String  addressText;
    private String  marketName;
    private String  marketAuctionDate;
    private String  districtNameInKannada;
    private String  talukNameInKannada;
    private String  hobliNameInKannada;
    private String  villageNameInKannada;
    private String userDistrict;
    private String userMarket;
    private Float totalCocoonsWeight;
    private Float totalCocoonsWeights;
    private String  loggedinUserDistrictName;
    private String loggedinUserTalukName;
    private String loggedinUserTscName;

    // 🆕 Newly added fields from query
    private String admGovtOrder;
    private String schemeCircularNo;
    private String deptDeleNo;
    private String allotReleaseNo;
    private Float actualAmount;
    private String extentOfMulberry;
    private String rhSqft;
    private Float estimatedCost;
    private String roofTypeNameInKannada;
    private String kaneshNo;
    private String releaseNo;
    private Date admGovtDate;
    private Date schemeCircularDate;
    private Date deptDeleDate;
    private Date allotReleaseDate;
    private Date releaseDate;
    private Date proposalDate;

    private Float sanctionAmount75;
    private Float centralShare50;
    private Float stateShare25;
    private Float beneficiaryShare25;

    private String sanctionAmount75InWords;
    private String centralShare50InWords;
    private String stateShare25InWords;
    private String beneficiaryShare25InWords;

    private Float centralSharePercentage;
    private Float stateSharePercentage;
    private Float centralSanctionAmount;
    private Float stateSanctionAmount;

    private String description;
    private String categoryShortName;
    private String divisionName;
    private String username;

    private String userName;
    private Long userId;

    private String userFullName;
    private String landVillage;


    private String reelerNumber;
    private String reelerName;
    private String reelerFatherName;
    private Float schemeAmount;
    private Float amountPerKg;
    private String rendittaGrade;
    private String silkTableBasinEnds;
    private String noOfCocoonsNeedToProduce;
    private String noOfRawSilkProduced;
    private String raceName;
    private String renditta;
    private String silkTable;
    private String form17jNo;
    private String dailyLimit;
    private Long machineTypeId;
    private String machineTypeName;
    private String roofTypeName;
    private String createdDate;
    private Float machineQuantity;
    private Long totalReelers;
    private Float incentiveAmountPerKg;
    private String numberOfBasins;
    private Float max;
    private Date currentDate;

    private String sReleaseNo;
    private Date sReleaseDate;

    /* CONFIGURE BIVOLTINE AMOUNT */
    private Float unitPrice;
    private Float unitCost;
    private Float subsidyAmount;
    private Float subsidyAmountCa;

    private Float totalSubsidyAmount;


    /* COMMERCIAL MARKET DATA */
    private Long raceMasterId;
    private Long grainageId;
    private String receiptNo;
    private String cdcmTransactionDate;

    private String lotNo;
    private String noOfDfls;

    private Object dateOfBrushing;
    private Object dateOfDistribution;

    private Float chawkiPercentage;
    private Object spunOnDate;

    private Float quantityOfCocoonsProduced;
    private Float averageYield;

    private Long marketId;
    private String cdcmBiddingSlipNo;

    private Float cocoonRatePerKg;
    private Object spunOnToDate;

    private String crcName;
    private String grainageMasterName;


    private Float schemeAmounts;
    private Float totalQuantityOfCocoonsProduced;
    private Float totalNoOfDfls;

    private Float grandTotalSubsidyAmount;
    private Float grandTotalNoOfDfls;
    private Float grandTotalSchemeAmount;
    private Float grandTotalQuantityOfCocoonsProduced;
    private Long  totalFarmers;

    private String month;
    private String silkExchangeName;


    private Float totalSubsidyAmountCa;

    private Float transportAmount;
    private Float totalTransportAmount;

    private Float incentiveAmount;
    private Float totalIncentiveAmount;

    private String totalQuantityOfCocoonsProducedInWords;
    private String totalTransportAmountInWords;

// getters & setters
private String totalIncentiveAmountInWords;
    private String reelingLicenseNumber;

    private String reelingShedDetails;
    private String reelerDetails;
    private Float reelingShedSqft;
    private String fullNameField;
    private String transactionDate;
    private Float noOfCocoonsPerKg;
    private String bonusRNoAndDate;

// + getter & setter





}
