package cz.tomasdvorak.ecard.reader;

import cz.tomasdvorak.ecard.reader.dto.ECard;
import cz.tomasdvorak.ecard.reader.dto.PersonalFile;
import cz.tomasdvorak.ecard.reader.dto.EHICData;
import cz.tomasdvorak.ecard.reader.parser.BouncyCastleCardParser;
import cz.tomasdvorak.ecard.reader.parser.CardParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.smartcardio.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECardReader extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ECardReader.class);
    public static final byte[] MAIN_FILE_AID = {(byte) 0xD0, 0x40, 0x00, 0x00, 0x17, 0x01, 0x01, 0x01};

    public static final byte[] PERSONALDATA_FILE_AID = {(byte) 0xEF, 0x01};
    public static final byte[] EHIC_FILE_AID = {(byte) 0xEF, 0x02};

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
                    terminalFactory.terminals().list(CardTerminals.State.CARD_INSERTION).forEach(terminal -> listener.onCardInserted(terminal, readECard(terminal)));
                } catch (Exception e) {
                    if (e.getCause() != null && e.getCause().getMessage().equals("SCARD_E_NO_READERS_AVAILABLE")) {
                        logger.info("No card reader detected, connect one to the computer, re-trying after 5s");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException interruptedException) {
                            // ignored
                        }
                    } else{
                        logger.warn("Failed to watch for terminal changes", e);
                    }
                }
            }
        });
    }

    public void shutdown() {
        logger.info("Shutdown of the card reader called");
        executorService.shutdownNow();
        this.interrupt();
    }

    private ECard readECard(CardTerminal terminal)  {
        logger.debug("Reading card from terminal {}", terminal.getName());
        try {
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            // Select the MF
            ResponseAPDU response = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, MAIN_FILE_AID, 0x100));
            // Select the Personaladata-file
            ResponseAPDU response2 = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x02, 0x04, PERSONALDATA_FILE_AID, 0x100));
            // Get the data from the file
            PersonalFile personalFile = cardParser.parsePersonalFile(channel.transmit(new CommandAPDU(0x00, 0xB0, 0x00, 0x00, 0xFF)));
            // Select the Personaladata-file
            channel.transmit(new CommandAPDU(0x00, 0xA4, 0x02, 0x04, EHIC_FILE_AID,0x100));
            // Get the data from the file
            EHICData ehicData = cardParser.parseEHICData(channel.transmit(new CommandAPDU(0x00, 0xB0, 0x00, 0x00, 0xFF)));
            card.disconnect(false);
            return new ECard(personalFile, ehicData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
