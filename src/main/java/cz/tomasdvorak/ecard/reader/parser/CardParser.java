package cz.tomasdvorak.ecard.reader.parser;

import cz.tomasdvorak.ecard.reader.dto.PersonalFile;
import cz.tomasdvorak.ecard.reader.dto.EHICData;

import javax.smartcardio.ResponseAPDU;

public interface CardParser {
    PersonalFile parsePersonalFile(ResponseAPDU responseAPDU);
    EHICData parseEHICData(ResponseAPDU responseAPDU);
}
