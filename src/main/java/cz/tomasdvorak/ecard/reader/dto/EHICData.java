package cz.tomasdvorak.ecard.reader.dto;

import java.util.Date;
import java.util.Map;

public class EHICData {
    private final String countryCode;
    private final String firstName;
    private final String lastName;
    private final Date dateOfBirth;
    private final String svNumber;
    private final String insuranceCompanyNumber;
    private String insuranceCompanyCode;
    private final String ehicCardNumber;
    private final Date validity;
    private final Map<String, String> additionalAttributes;
    private final String debugOutput;

    public EHICData(String countryCode, String firstName, String lastName, Date dateOfBirth, String svNumber, String insuranceCompanyNumber, final String insuranceCompanyCode, String ehicCardNumber, Date validity, Map<String, String> additionalAttributes, String debugOutput) {
        this.countryCode = countryCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.svNumber = svNumber;
        this.insuranceCompanyNumber = insuranceCompanyNumber;
        this.insuranceCompanyCode = insuranceCompanyCode;
        this.ehicCardNumber = ehicCardNumber;
        this.validity = validity;
        this.additionalAttributes = additionalAttributes;
        this.debugOutput = debugOutput;
    }

    /**
     * ISO 3166
     */
    public String getCountryCode() {
        return countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSvNumber() {
        return svNumber;
    }

    public String getInsuranceCompanyNumber() {
        return insuranceCompanyNumber;
    }

    public String getInsuranceCompanyCode() {
        return insuranceCompanyCode;
    }

    public String getEhicCardNumber() {
        return ehicCardNumber;
    }

    public Date getValidity() {
        return validity;
    }

    public Map<String, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public String getDebugOutput() {
        return debugOutput;
    }

    @Override
    public String toString() {
        return "{" +
                "countryCode='" + countryCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", svNumber='" + svNumber + '\'' +
                ", insuranceCompanyNumber='" + insuranceCompanyNumber + '\'' +
                ", insuranceCompanyCode='" + insuranceCompanyCode + '\'' +
                ", ehicCardNumber='" + ehicCardNumber + '\'' +
                ", validity=" + validity +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }
}
