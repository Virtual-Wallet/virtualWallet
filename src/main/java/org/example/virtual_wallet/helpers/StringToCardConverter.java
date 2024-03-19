//package org.example.virtual_wallet.converters;
//
//import org.example.virtual_wallet.models.Card;
//import org.example.virtual_wallet.repositories.contracts.CardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class StringToCardConverter implements Converter<String, Card> {
//
//
//    private final CardRepository cardRepository;
//
//    @Autowired
//    public StringToCardConverter(CardRepository cardRepository) {
//        this.cardRepository = cardRepository;
//    }
//
//    @Override
//    public Card convert(String source) {
//        // Assuming the source is the ID of the Card
//        return cardRepository.getById(Integer.parseInt(source))
//                .or(() -> new IllegalArgumentException("Invalid Card ID: " + source));
//    }
//}