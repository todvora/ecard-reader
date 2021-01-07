package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;

import javax.smartcardio.CardTerminal;

// https://stackoverflow.com/questions/41066803/how-to-decode-responseapdu-to-xml-from-an-austrian-e-card
// https://wiki.debian.org/Smartcards/OpenPGP
// http://read.pudn.com/downloads437/sourcecode/windows/dotnet/1846228/ECardExpress/ECardExpress.cs__.htm
public class ReaderTest {


    public static void main(String[] args) throws InterruptedException {
        ECardListener listener = new ECardListener() {
            @Override
            public void onCardRemoved(CardTerminal terminal) {
                System.out.println("Card removed from terminal " + terminal.getName());
            }

            @Override
            public void onCardInserted(CardTerminal terminal, ECard eCard) {
                System.out.println("Card inserted into terminal " + terminal.getName());
                System.out.println(eCard);
            }
        };
        ECardReader eCardReader = new ECardReader(listener);
        eCardReader.start();
        eCardReader.join();
    }
}
