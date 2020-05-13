package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;
import cz.tomasdvorak.ecard.reader.dto.ECardBuilder;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.util.ASN1Dump;

import javax.smartcardio.ResponseAPDU;
import java.io.IOException;
import java.text.ParseException;

public class BouncyCastleCardParser implements CardParser {
    @Override
    public ECard parse(ResponseAPDU responseAPDU) {
        ASN1InputStream input = new ASN1InputStream(responseAPDU.getData());
        try {
            ASN1Primitive p = input.readObject();
            ASN1Sequence asn1 = ASN1Sequence.getInstance(p);
            ECardBuilder eCardBuilder = new ECardBuilder();
            eCardBuilder.setDebugOutput(ASN1Dump.dumpAsString(p));
            for(ASN1Encodable prop : asn1) {
                ASN1Sequence seq = DLSequence.getInstance(prop);
                ASN1ObjectIdentifier objectId = (ASN1ObjectIdentifier) seq.getObjectAt(0);
                String identifier = objectId.getId();
                ASN1Set svn = DLSet.getInstance(seq.getObjectAt(1));
                ASN1Encodable value = svn.getObjectAt(0);
                switch (identifier) {
                    case "1.2.40.0.10.1.4.1.1":
                        eCardBuilder.setSvNumber(DERNumericString.getInstance(value).getString());
                        break;
                    case "1.2.40.0.10.1.4.1.3":
                        eCardBuilder.setCardSequenceNumber(ASN1Integer.getInstance(value).toString());
                        break;
                    case "2.5.4.42":
                        eCardBuilder.setFirstName(DERUTF8String.getInstance(value).getString());
                        break;
                    case "2.5.4.4":
                        eCardBuilder.setLastName(DERUTF8String.getInstance(value).getString());
                        break;
                    case "1.3.6.1.5.5.7.9.3":
                        eCardBuilder.setSex(DERPrintableString.getInstance(value).getString());
                        break;
                    case "1.3.6.1.5.5.7.9.1":
                        try {
                            eCardBuilder.setDateOfBirth(DERGeneralizedTime.getInstance(value).getDate());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        eCardBuilder.addUnknownParameter(identifier, value.toString());
                }
            }
            return eCardBuilder.build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

