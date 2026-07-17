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

    // ARM Selection Letter admin fields
    private String dateOfGovtApproval;
    private String eOfficeFileNo;
    private String dateOfArmSelectionProceedings;

    // Farmer bank details
    private String farmerBankName;
    private String farmerBankIfsc;

    // Vendor address (for advance payment letter body)
    private String vendorAddress;

    // cc designations from sc_application_work_flow_detail (steps 2, 3, 4)
    private String cc2DesignationKan;       // designation_name_in_kannada for rn=2
    private String cc3DesignationKan;       // designation_name_in_kannada for rn=3
    private String cc4DesignationKan;       // designation_name_in_kannada for rn=4
    private String cc2DesignationKanSanct;  // designation_name_in_kannada_for_sanction_order for rn=2
    private String cc3DesignationKanSanct;  // designation_name_in_kannada_for_sanction_order for rn=3
    private String cc4DesignationKanSanct;  // designation_name_in_kannada_for_sanction_order for rn=4

    // Vendor 1 (from arm_proforma_invoice_vendor, vendor_no = 1)
    private String vendor1Name;
    private String vendor1Address;
    private String vendor1MfrAddress;
    private String vendor1BankName;
    private String vendor1BankAddress;
    private String vendor1AccountNo;
    private String vendor1MicrCode;
    private String vendor1InvoiceNo;
    private String vendor1InvoiceDate;
    private Float  vendor1GuidelineTotal;
    private Float  vendor1InvoiceTotal;
    private Float  vendor1EligibleTotal;
    private Float  vendor1ReleaseAmount;

    // Vendor 2 (from arm_proforma_invoice_vendor, vendor_no = 2)
    private String vendor2Name;
    private String vendor2Address;
    private String vendor2MfrAddress;
    private String vendor2BankName;
    private String vendor2BankAddress;
    private String vendor2AccountNo;
    private String vendor2MicrCode;
    private String vendor2InvoiceNo;
    private String vendor2InvoiceDate;
    private Float  vendor2GuidelineTotal;
    private Float  vendor2InvoiceTotal;
    private Float  vendor2EligibleTotal;
    private Float  vendor2ReleaseAmount;

    // Combined totals for this release stage
    private Float  totalGuidelineTotal;
    private Float  totalInvoiceTotal;
    private Float  releaseBaseAmount;
    private Float  releasePercentage;
    private Float  releaseAmount;
    private Float  cumulativeReleasedAmount;

    // CSTRI letter reference (from arm_release_details)
    private String cstriLetterNo;
    private String cstriLetterDate;
    private String releaseType;
}
