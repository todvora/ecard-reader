package cz.tomasdvorak.ecard.reader.dto;

import cz.tomasdvorak.ecard.reader.dto.EHICData;
import cz.tomasdvorak.ecard.reader.dto.PersonalFile;

public class ECard {
    private final PersonalFile personalFile;
    private final EHICData ehicData;

    public ECard(PersonalFile personalFile, EHICData ehicData) {
        this.personalFile = personalFile;
        this.ehicData = ehicData;
    }

    public PersonalFile getPersonalFile() {
        return personalFile;
    }

    public EHICData getEhicData() {
        return ehicData;
    }

    @Override
    public String toString() {
        return "ECard{" +
                "personalFile=" + personalFile +
                ", ehicData=" + ehicData +
                '}';
    }
}
