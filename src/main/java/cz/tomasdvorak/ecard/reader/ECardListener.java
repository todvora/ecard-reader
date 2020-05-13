package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;

import javax.smartcardio.CardTerminal;

public interface ECardListener {
    void onCardRemoved(CardTerminal terminal);
    void onCardInserted(CardTerminal terminal, ECard eCard);
}
