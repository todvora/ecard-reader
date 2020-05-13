package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;

import javax.smartcardio.CardTerminal;
import java.sql.SQLOutput;

// https://stackoverflow.com/questions/41066803/how-to-decode-responseapdu-to-xml-from-an-austrian-e-card
// https://wiki.debian.org/Smartcards/OpenPGP
public class Main {
    public static void main(String[] args) {


        try {

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

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
