## Java reader for austrian E-Cards

Let's have some fun with austrian insurance cards called ecard. They hold some information readable in any smartcard reader.
Just connect to your computer a smartcard reader and start coding:

### Usage
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

Following information structure is provided for each E-Card:

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