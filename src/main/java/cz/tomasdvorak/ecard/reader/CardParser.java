package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;

import javax.smartcardio.ResponseAPDU;

public interface CardParser {
    ECard parse(ResponseAPDU responseAPDU);
}
