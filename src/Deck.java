//Code referenced from https://github.com/EricCharnesky/CIS2353-Summer2023/blob/main/HoldemStart/src/project2/holdem/Deck.java

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Face face : Card.Face.values()) {
                cards.add (new Card(face, suit));
            }
        }

        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.remove(cards.size() - 1);
    }
}
