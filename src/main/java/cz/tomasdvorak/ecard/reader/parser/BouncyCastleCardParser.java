package cz.tomasdvorak.ecard.reader.parser;

import cz.tomasdvorak.ecard.reader.dto.PersonalFile;
import cz.tomasdvorak.ecard.reader.dto.PersonalFileBuilder;
import cz.tomasdvorak.ecard.reader.dto.EHICBuilder;
import cz.tomasdvorak.ecard.reader.dto.EHICData;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.util.ASN1Dump;

import javax.smartcardio.ResponseAPDU;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;

public class BouncyCastleCardParser implements CardParser {
    @Override
    public PersonalFile parsePersonalFile(ResponseAPDU responseAPDU) {
        ASN1InputStream input = new ASN1InputStream(responseAPDU.getData());
        try {
            ASN1Primitive p = input.readObject();
            ASN1Sequence asn1 = ASN1Sequence.getInstance(p);
            PersonalFileBuilder personalFileBuilder = new PersonalFileBuilder();
            personalFileBuilder.setDebugOutput(ASN1Dump.dumpAsString(p));
            for(ASN1Encodable prop : asn1) {
                ASN1Sequence seq = DLSequence.getInstance(prop);
                ASN1ObjectIdentifier objectId = (ASN1ObjectIdentifier) seq.getObjectAt(0);
                String identifier = objectId.getId();
                ASN1Set svn = DLSet.getInstance(seq.getObjectAt(1));
                ASN1Encodable value = svn.getObjectAt(0);
                switch (identifier) {
                    case "1.2.40.0.10.1.4.1.1":
                        personalFileBuilder.setSvNumber(DERNumericString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.3":
                        personalFileBuilder.setCardSequenceNumber(ASN1Integer.getInstance(value).toString());
                        break;
                    case "2.5.4.42":
                        personalFileBuilder.setFirstName(DERUTF8String.getInstance(value).getString());
                        break;
                    case "2.5.4.4":
                        personalFileBuilder.setLastName(parseLastName(svn));
                        break;
                    case "1.3.6.1.5.5.7.9.3":
                        personalFileBuilder.setSex(DERPrintableString.getInstance(value).getString());
                        break;
                    case "1.3.6.1.5.5.7.9.1":
                        personalFileBuilder.setDateOfBirth(decodeDate(value));
                        break;
                    default:
                        personalFileBuilder.addUnknownParameter(identifier, value.toString());
                }
            }
            return personalFileBuilder.build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * There is a difference between old cards without photo and new with photo!
     */
    private String parseLastName(ASN1Set svn) {
        final Enumeration<ASN1Encodable> enumeration = svn.getObjects();
        ASN1Encodable firstObject = enumeration.nextElement();
        if(enumeration.hasMoreElements()) {
            // this is the new card, surname in second element
            return DERUTF8String.getInstance(enumeration.nextElement()).getString();
        } else {
            // old card, decode and return
            return DERUTF8String.getInstance(firstObject).getString();
        }
    }

    @Override
    public EHICData parseEHICData(ResponseAPDU responseAPDU) {
        ASN1InputStream input = new ASN1InputStream(responseAPDU.getData());
        try {
            ASN1Primitive p = input.readObject();
            ASN1Sequence asn1 = ASN1Sequence.getInstance(p);
            EHICBuilder dataBuilder = new EHICBuilder(ASN1Dump.dumpAsString(p));
            for(ASN1Encodable prop : asn1) {
                ASN1Sequence seq = DLSequence.getInstance(prop);
                ASN1ObjectIdentifier objectId = (ASN1ObjectIdentifier) seq.getObjectAt(0);
                String identifier = objectId.getId();
                ASN1Set svn = DLSet.getInstance(seq.getObjectAt(1));
                ASN1Encodable value = svn.getObjectAt(0);
                switch (identifier) {
                    case "2.5.4.6":
                        dataBuilder.setCountryCode(DERPrintableString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.25":
                        dataBuilder.setLastName(DERUTF8String.getInstance(value).toString());
                        break;
                        case "1.2.40.0.10.1.4.1.26":
                        dataBuilder.setFirstName(DERUTF8String.getInstance(value).toString());
                        break;
                    case "1.3.6.1.5.5.7.9.1":
                        dataBuilder.setDateOfBirth(decodeDate(value));
                        break;
                    case "1.2.40.0.10.1.4.1.27":
                        dataBuilder.setSvNumber(DERNumericString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.21":
                        dataBuilder.setInsuranceCompanyNumber(DERNumericString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.20":
                        dataBuilder.setInsuranceCompanyCode(decodeHexString(value));
                        break;
                    case "1.2.40.0.10.1.4.1.22":
                        dataBuilder.setEhicCardNumber(DERNumericString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.23":
                        dataBuilder.setValidity(decodeDate(value));
                        break;
                    default:
                        dataBuilder.addUnknownAttribute(identifier, value.toString());
                }
            }
            return dataBuilder.build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Date decodeDate(ASN1Encodable value) {
        try {
            return DERGeneralizedTime.getInstance(value).getDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String decodeHexString(ASN1Encodable value) {
        String hexValue = ASN1OctetString.getInstance(value).toString();
        byte[] bytes = javax.xml.bind.DatatypeConverter.parseHexBinary(hexValue.substring(1));
        return new String(bytes);
    }
}

