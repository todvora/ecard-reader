package cz.tomasdvorak.ecard.reader;

public class CardParserFactory {
    public static CardParser getParser() {
        return new BouncyCastleCardParser();
    }
}
