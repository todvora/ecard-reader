package cz.tomasdvorak.ecard.reader.dto;

import java.util.Date;
import java.util.Map;

public class PersonalFile {
    private final String svNumber;
    private final String firstName;
    private final String lastName;
    private final String cardSequenceNumber;
    private final String sex;
    private final Date dateOfBirth;
    private final Map<String, String> additionalAttributes;
    private final String debugOutput;

    public PersonalFile(String svNumber, String firstName, String lastName, String cardSequenceNumber, String sex, Date dateOfBirth, Map<String, String> additionalAttributes, String debugOutput) {
        this.svNumber = svNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardSequenceNumber = cardSequenceNumber;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.additionalAttributes = additionalAttributes;
        this.debugOutput = debugOutput;
    }

    public String getSvNumber() {
        return svNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCardSequenceNumber() {
        return cardSequenceNumber;
    }

    public String getSex() {
        return sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
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
                "svNumber='" + svNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cardSequenceNumber='" + cardSequenceNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }
}
