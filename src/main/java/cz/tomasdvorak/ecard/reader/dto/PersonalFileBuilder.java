package cz.tomasdvorak.ecard.reader.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersonalFileBuilder {
    private String svNumber;
    private  String firstName;
    private  String lastName;
    private  String cardSequenceNumber;
    private  String sex;
    private  Date dateOfBirth;
    private  Map<String, String> additionalAttributes;
    private String debugOutput;

    public void setSvNumber(String svNumber) {
        this.svNumber = svNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCardSequenceNumber(String cardSequenceNumber) {
        this.cardSequenceNumber = cardSequenceNumber;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDebugOutput(String debugOutput) {
        this.debugOutput = debugOutput;
    }

    public void addUnknownParameter(String identifier, String value) {
        if(this.additionalAttributes == null) {
            this.additionalAttributes = new LinkedHashMap<>();
        }
        this.additionalAttributes.put(identifier, value);
    }

    public PersonalFile build() {
        return new PersonalFile(svNumber, firstName, lastName, cardSequenceNumber, sex, dateOfBirth, additionalAttributes,debugOutput);
    }
}
