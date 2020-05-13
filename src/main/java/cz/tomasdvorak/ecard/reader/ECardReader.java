package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;

import javax.smartcardio.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECardReader extends Thread {

    private final ECardListener listener;
    private final TerminalFactory terminalFactory;
    private final ExecutorService executorService;
    private final CardParser cardParser;


    public ECardReader(final ECardListener listener, final TerminalFactory terminalFactory, final ExecutorService executorService, final CardParser cardParser) {
        super();
        this.listener = listener;
        this.terminalFactory = terminalFactory;
        this.executorService = executorService;
        this.cardParser = cardParser;
    }

    public ECardReader(final ECardListener listener) {
        this(listener, TerminalFactory.getDefault(), Executors.newSingleThreadExecutor(), new BouncyCastleCardParser());
    }

    public void start() {
        executorService.submit(() -> {
            while(!interrupted()) {
                try {

                    terminalFactory.terminals().waitForChange();
                    terminalFactory.terminals().list(CardTerminals.State.CARD_REMOVAL).forEach(listener::onCardRemoved);
                    terminalFactory.terminals().list(CardTerminals.State.CARD_PRESENT).forEach(terminal -> listener.onCardInserted(terminal, readECard(terminal)));
                } catch (CardException e) {
                    // ignored
                }
            }
        });
    }

    private ECard readECard(CardTerminal terminal)  {
        try {
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            // Select the MF
            byte[] aid = {(byte) 0xD0, 0x40, 0x00, 0x00, 0x17, 0x01, 0x01, 0x01};
            channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
            // Select the Personaladata-file
            byte[] aid2 = {(byte) 0xEF, 0x01};
            channel.transmit(new CommandAPDU(0x00, 0xA4, 0x02, 0x04, aid2));
            // Get the data from the file
            ResponseAPDU responseAPDU = channel.transmit(new CommandAPDU(0x00, 0xB0, 0x00, 0x00, 0xFF));
            ECard eCard = cardParser.parse(responseAPDU);
            card.disconnect(false);
            return eCard;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
