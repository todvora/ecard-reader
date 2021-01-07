## Java reader for austrian E-Cards

This is an example project to demonstrate reading of the personal data from austrian ecards. 

You will need an smartcard reader connected to the computer.

### Example
The reader provides notifications on card insertion and removal. Both events allow identification of the terminal.

```java
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
```

This example code prints following statements:

```
Card inserted into terminal Gemalto PC Twin Reader (9BF12708) 00 00
ECard{personalFile=...
Card removed from terminal Gemalto PC Twin Reader (9BF12708) 00 00
```

Where the ecard information has a structure like this:

```
ECard{
    personalFile={
        svNumber='XXXXXXXX', 
        firstName='Tomas', 
        lastName='Dvorak', 
        cardSequenceNumber='6', 
        sex='M', 
        dateOfBirth=XXXXXXXX, 
        additionalAttributes=null
    }, 
    ehicData={
        countryCode='AT', 
        firstName='TOMAS', 
        lastName='DVORAK', 
        dateOfBirth=XXXXXXXX, 
        svNumber='XXXXXXXX', 
        insuranceCompanyNumber='XXXXXXXX', 
        insuranceCompanyCode='XXXXXXXX', 
        ehicCardNumber='XXXXXXXX', 
        validity=XXXXXXXX, 
        additionalAttributes=null
    }
}
```

(`XXXXXXXX` is just a placeholder to hide my personal information, you get full information from your card)

All attributes are available via the `ECard` response type with `personalFile` and `ehicData`

The reader keeps reading and pushing notifications as long as you keep it running. 