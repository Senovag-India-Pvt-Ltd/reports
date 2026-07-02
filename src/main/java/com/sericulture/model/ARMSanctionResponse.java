package com.sericulture.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ARMSanctionResponse {

    private Long   applicationFormId;
    private int    serialNumber;
    private int    index;
    private String sanctionOrderDownloadUrl;
    private String securityKey;
    private String designationNameInKannada;
    private String designationNameInKannadaForSanctionOrder;
    private String createdByDesignation;
    private String createdByDesignationForSanctionOrder;
    private String drawingOfficerDesignation;
    private String drawingOfficerDesignationForSanctionOrder;
    private String hierarchyDesignationForSanctionOrder;
    private String hierarchyDesignation;
    private String previousStepDesignation;
    private String previousStepDesignationForSanctionOrder;
    private String assignedByUserProposalDate;
    private String username;
    private Long   loggedinUserId;
    private String loggedinUserFullName;
    private String financialYear;
    private String schemeNameInKannada;
    private String subSchemeNameInKannada;
    private String categoryNameInKannada;
    private String scComponentNameInKannada;
    private String scHeadAccountName;
    private String description;
    private String caName;
    private String divisionName;
    private String admGovtOrder;
    private Date   admGovtDate;
    private String schemeCircularNo;
    private Date   schemeCircularDate;
    private String allotReleaseNo;
    private Date   allotReleaseDate;
    private String deptDeleNo;
    private Date   deptDeleDate;
    private String releaseNo;
    private Date   releaseDate;
    private String reelerName;
    private String reelerFatherName;
    private String reelerNumber;
    private String mobileNumber;
    private String addressText;
    private String districtNameInKannada;
    private String talukNameInKannada;
    private String hobliNameInKannada;
    private String villageNameInKannada;
    private String fruitsId;
    private String reelingLicenseNumber;
    private String arn;
    private String vendorName;
    private String taxInvoiceNo;
    private String taxInvoiceDate;
    private String machineTypeName;
    private String icbBasinEnds;
    private String imcbTable;
    private Float  unitCost;
    private String shareInPercentage;
    private Float  schemeAmount;
    private Float  actualAmount;
    private String sanctionOrderNumber;
    private String sanctionNo;
    private Date   proposalDate;
    private String createdDate;
    private String modifiedDate;
    private String roofTypeNameInKannada;
    private String landVillageNameInKannada;
    private String workOrderNumber;
    private String selectionLetterDate;
    private String empanelledVendorApprovedBy;
    private String letterNo;
    private String empanelledVendorDate;
    private String districtName;
    private String talukName;
    private String villageName;
    private String loggedinUserDistrictName;
    private String loggedinUserTscName;
    private Float  centralSanctionAmount;
    private Float  stateSanctionAmount;

    // ESCROW Bank details
    private String escrowBankManagerName;
    private String escrowBankName;
    private String escrowBankAddress;
    private String escrowBankIfsc;
    private String escrowBankMicr;
    private String escrowAccountNumber;
    private String escrowBankLetterNo;
    private String escrowBankLetterDate;

    // ARM Land details
    private String armLandType;
    private String armSurveyNo;
    private String armAssessmentNo;
    private String armLandDistrictKan;
    private String armLandTalukKan;
    private String armLandHobliKan;
    private String armLandVillageKan;
    private String armLandPropertyNo;
    private String armLandAddress;

    // ARM unit info
    private String armEndsCount;
    private String armUnitName;
    private String reelerAadhaar;

    // Farmer bank details
    private String farmerBankName;
    private String farmerBankIfsc;

    // Vendor address (for advance payment letter body)
    private String vendorAddress;
}
