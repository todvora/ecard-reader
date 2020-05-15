package cz.tomasdvorak.ecard.reader.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class EHICBuilder {
    private String countryCode;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String svNumber;
    private String insuranceCompanyNumber;
    private String insuranceCompanyCode;
    private String ehicCardNumber;
    private Date validity;
    private Map<String, String> additionalAttributes;
    private String debugOutput;

    public EHICBuilder(final String debugOutput) {
        this.debugOutput = debugOutput;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setSvNumber(String svNumber) {
        this.svNumber = svNumber;
    }

    public void setInsuranceCompanyCode(String insuranceCompanyCode) {
        this.insuranceCompanyCode = insuranceCompanyCode;
    }

    public void setInsuranceCompanyNumber(String insuranceCompanyNumber) {
        this.insuranceCompanyNumber = insuranceCompanyNumber;
    }

    public void setEhicCardNumber(String ehicCardNumber) {
        this.ehicCardNumber = ehicCardNumber;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public void addUnknownAttribute(final String oid, final String value) {
        if(this.additionalAttributes == null) {
            this.additionalAttributes = new LinkedHashMap<>();
        }
        this.additionalAttributes.put(oid, value);
    }

    public void setDebugOutput(String debugOutput) {
        this.debugOutput = debugOutput;
    }

    public EHICData build() {
        return new EHICData(countryCode, firstName, lastName, dateOfBirth, svNumber, insuranceCompanyNumber, insuranceCompanyCode, ehicCardNumber, validity, additionalAttributes, debugOutput);
    }
}
