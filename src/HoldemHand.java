//Code referenced from https://github.com/EricCharnesky/CIS2353-Summer2023/blob/main/HoldemStart/src/project2/holdem/HoldemHand.java
//Personal updates/additions include:
//  - created oddsOfWinning, beginSimulation, getExtraSharedCards, getTwoOpponentCards, getBestPossibleHandSimulation methods
//  - updated the getBestPossibleHand method to work with my oddsOfWinning method

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class HoldemHand implements Comparable<HoldemHand> {

    private Card card1;
    private Card card2;
    private ArrayList<Card> sharedCards;

    public HoldemHand(Card card1, Card card2) {
        this.card1 = card1;
        this.card2 = card2;
        sharedCards = new ArrayList<>();
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
     return card2;
    }

    public void addSharedCard(Card card) {

        if (sharedCards.size() > 5) {
            throw new IllegalArgumentException("You can't have more than 5 cards");
        }

        sharedCards.add(card);
    }

    public ArrayList<Card> getSharedCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(sharedCards);
        return cards;
    }

    public PokerHand getBestPossibleHand() {
        if (sharedCards.size() > 5) {
            throw new IllegalArgumentException("You can't have more than 5 cards");
        }

        ArrayList<Card> allCards = new ArrayList<>();

        allCards.add(card1);
        allCards.add(card2);
        allCards.addAll(sharedCards);

        return getBestPossibleHandSimulation(allCards);
    }

    @Override
    public int compareTo(HoldemHand o) {
        return getBestPossibleHand().compareTo(o.getBestPossibleHand());
    }

    public PokerHand.Result call(HoldemHand o) {
        return getBestPossibleHand().call(o.getBestPossibleHand());
    }

    @Override
    public String toString() {
        return card1.toString() + ", " + card2.toString();
    }

    public void oddsOfWinning() {
        DecimalFormat df = new DecimalFormat("#.##%");
        ArrayList<Card> simulationDeck = new ArrayList<>();
        ArrayList<Card> previousOpponentHands = new ArrayList<>();
        ArrayList<Card> simulationPlayerHand = new ArrayList<>();
        ArrayList<Card> simulationComputerHand = new ArrayList<>();
        int[] winCount = {0,0,0};

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Face face : Card.Face.values()) {
                simulationDeck.add(new Card(face, suit));
            }
        }

        Collections.shuffle(simulationDeck);

        //River - Player and PC have 2 cards. 5 shared cards. Since the player does not know the opponents hand, the
        //          percent of the player winning is calculated by predicting the possible card combinations the opponent has.
        //          Player has 2 cards. 5 shared cards. Player only knows 7 cards. 45 cards unknown.
        //          45 c 2 = 990 possible opponent hand combinations
        if (sharedCards.size() == 5) {

            for (int simulationCount = 0; simulationCount < 990; simulationCount++) {

                int result = beginSimulation(simulationDeck, simulationPlayerHand, simulationComputerHand, previousOpponentHands);

                if (result == 1) {
                    winCount[0]++;
                } else if (result == 0) {
                    winCount[2]++;
                } else {
                    winCount[1]++;
                }
            }

            System.out.println("\n\tWin percent for Player: " +  df.format(winCount[0] / 990.0));
            System.out.println("\tWin percent for Computer: " + df.format(winCount[1] / 990.0));
            System.out.println("\tDraw percent: " +  df.format(winCount[2] / 990.0) + "\n");
        }

        //Turn - Player and PC have 2 cards. 4 shared cards. Player only knows 6 cards. 46 cards unknown.
        //       46 c 3 = 15180 possible 1 card combinations for the rest of the shared cards and opponent hand
        //       10% of 15180 = 1518
        else if (sharedCards.size() == 4) {

            for (int simulationCount = 0; simulationCount < 1518; simulationCount++) {

                int result = beginSimulation(simulationDeck, simulationPlayerHand, simulationComputerHand, previousOpponentHands);

                if (result == 1) {
                    winCount[0]++;
                } else if (result == 0) {
                    winCount[2]++;
                } else {
                    winCount[1]++;
                }
            }

            System.out.println("\n\tWin percent for Player: " +  df.format(winCount[0] / 1518.0));
            System.out.println("\tWin percent for Computer: " + df.format(winCount[1] / 1518.0));
            System.out.println("\tDraw percent: " +  df.format(winCount[2] / 1518.0) + "\n");
        }

        //Flop - Player and PC have 2 cards. 3 shared cards. Player only knows 5 cards. 47 cards unknown.
        //       47 c 4 = 178365 possible 2 card combinations for the rest of the shared cards and opponent hand
        //       1% of 178365 = 1783
        else if (sharedCards.size() == 3) {

            for (int simulationCount = 0; simulationCount < 1783; simulationCount++) {

                int result = beginSimulation(simulationDeck, simulationPlayerHand, simulationComputerHand, previousOpponentHands);

                if (result == 1) {
                    winCount[0]++;
                } else if (result == 0) {
                    winCount[2]++;
                } else {
                    winCount[1]++;
                }
            }

            System.out.println("\n\tWin percent for Player: " +  df.format(winCount[0] / 1783.0));
            System.out.println("\tWin percent for Computer: " + df.format(winCount[1] / 1783.0));
            System.out.println("\tDraw percent: " +  df.format(winCount[2] / 1783.0) + "\n");
        }

        //Pre-Flop - Player and PC have 2 cards. 0 shared cards. Player only knows 2 cards. 50 cards unknown.
        //           50 c 7 = 99884400 possible shared card combinations
        //           0.001% of 99884400 = 9988
        else {

            for (int simulationCount = 0; simulationCount < 9988; simulationCount++) {

                int result = beginSimulation(simulationDeck, simulationPlayerHand, simulationComputerHand, previousOpponentHands);

                if (result == 1) {
                    winCount[0]++;
                } else if (result == 0) {
                    winCount[2]++;
                } else {
                    winCount[1]++;
                }
            }

            System.out.println("\n\tWin percent for Player: " +  df.format(winCount[0] / 9988.0));
            System.out.println("\tWin percent for Computer: " + df.format(winCount[1] / 9988.0));
            System.out.println("\tDraw percent: " +  df.format(winCount[2] / 9988.0) + "\n");
        }
    }

    public int beginSimulation(ArrayList<Card> simulationDeck, ArrayList<Card> simulationPlayerHand, ArrayList<Card> simulationComputerHand, ArrayList<Card> previousOpponentHands) {

        ArrayList<Card> simulationPlayedCards = new ArrayList<>();
        simulationPlayedCards.add(card1);
        simulationPlayedCards.add(card2);
        simulationPlayedCards.addAll(sharedCards);

        simulationPlayerHand.clear();
        simulationComputerHand.clear();

        simulationPlayerHand.add(card1);
        simulationPlayerHand.add(card2);
        simulationPlayerHand.addAll(sharedCards);

        ArrayList<Card> extraSharedCards = getExtraSharedCards(simulationDeck, simulationPlayedCards, 5 - sharedCards.size());
        simulationPlayerHand.addAll(extraSharedCards);
        simulationComputerHand.addAll(extraSharedCards);
        simulationPlayedCards.addAll(extraSharedCards);

        simulationComputerHand.addAll(getTwoOpponentCards(simulationDeck, simulationPlayedCards, previousOpponentHands, 2));
        simulationComputerHand.addAll(sharedCards);

        return getBestPossibleHandSimulation(simulationPlayerHand).compareTo(getBestPossibleHandSimulation(simulationComputerHand));
    }

    public ArrayList<Card> getExtraSharedCards(ArrayList<Card> simulationDeck, ArrayList<Card> simulationPlayedCards, int numberOfCardsNeeded) {

        ArrayList<Card> extraSharedCards = new ArrayList<>();
        int count = 0;

        Collections.shuffle(simulationDeck);

        //Check to see if dealt card has already been dealt
        for (int j = 0; j < simulationDeck.size(); j++) {
            int p = 0;
            while (p < simulationPlayedCards.size()) {
                if (simulationDeck.get(j) != simulationPlayedCards.get(p)) {
                    if (p == 6 - numberOfCardsNeeded && count < numberOfCardsNeeded) {
                        extraSharedCards.add(simulationDeck.get(j));
                        count++;
                    }
                    p++;
                } else {
                    j++;
                    p = 0;
                }
            }
        }
        return extraSharedCards;
    }

    public ArrayList<Card> getTwoOpponentCards(ArrayList<Card> simulationDeck, ArrayList<Card> simulationPlayedCards, ArrayList<Card> previousOpponentHands, int numberOfCardsNeeded) {

        ArrayList<Card> pairOfOpponentCards = new ArrayList<>();

        if (previousOpponentHands.size() == 1980) {
            previousOpponentHands.clear();
        }

        int count;
        int check = 0;

        while (check == 0) {
            count = 0;
            Collections.shuffle(simulationDeck);

            //Check to see if dealt card has already been dealt
            for (int j = 0; j < simulationDeck.size(); j++) {
                int p = 0;
                while (p < simulationPlayedCards.size() && j < simulationDeck.size()) {
                    if (simulationDeck.get(j) != simulationPlayedCards.get(p)) {
                        if (p == 6 - numberOfCardsNeeded && count < numberOfCardsNeeded) {
                            pairOfOpponentCards.add(simulationDeck.get(j));
                            count++;
                        }
                        p++;
                    } else {
                        j++;
                        p = 0;
                    }
                }
            }

            Collections.sort(pairOfOpponentCards);

            //Check to see if opponent's hand has already been used in a previous simulation
            if (previousOpponentHands.size() == 0) {
                previousOpponentHands.addAll(pairOfOpponentCards);
                check++;
            } else {
                for (int k = 0; k < previousOpponentHands.size(); k++) {
                    if (pairOfOpponentCards.size() != 0) {
                        if (previousOpponentHands.get(k) == pairOfOpponentCards.get(0)
                                && previousOpponentHands.get(k + 1) == pairOfOpponentCards.get(1)
                                || previousOpponentHands.get(k) == pairOfOpponentCards.get(1)
                                && previousOpponentHands.get(k + 1) == pairOfOpponentCards.get(0)) {
                            pairOfOpponentCards.clear();
                        }
                    }
                    k++;
                }

                if (pairOfOpponentCards.size() != 0) {
                    previousOpponentHands.addAll(pairOfOpponentCards);
                    check++;
                }
            }
        }

        return pairOfOpponentCards;
    }

    public PokerHand getBestPossibleHandSimulation(ArrayList<Card> handToBeTested) {
        if (sharedCards.size() > 5) {
            throw new IllegalArgumentException("You can't have more than 5 cards");
        }

        ArrayList<PokerHand> hands = new ArrayList<>();

        for (int firstCardIndex = 0; firstCardIndex < 3; firstCardIndex++) {
            for (int secondCardIndex = firstCardIndex + 1; secondCardIndex < 4; secondCardIndex++) {
                for (int thirdCardIndex = secondCardIndex + 1; thirdCardIndex < 5; thirdCardIndex++) {
                    for (int fourthCardIndex = thirdCardIndex + 1; fourthCardIndex < 6; fourthCardIndex++) {
                        for (int fifthCardIndex = fourthCardIndex + 1; fifthCardIndex < 7; fifthCardIndex++) {
                            ArrayList<Card> cards = new ArrayList<>();
                            cards.add(handToBeTested.get(firstCardIndex));
                            cards.add(handToBeTested.get(secondCardIndex));
                            cards.add(handToBeTested.get(thirdCardIndex));
                            cards.add(handToBeTested.get(fourthCardIndex));
                            cards.add(handToBeTested.get(fifthCardIndex));

                            PokerHand hand = new PokerHand(cards);
                            hands.add(hand);
                        }
                    }
                }
            }
        }

        PokerHand bestHand = hands.get(0);

        for (int handIndex = 1; handIndex < hands.size(); handIndex++) {
            if (hands.get(handIndex).compareTo(bestHand) > 0) {
                bestHand = hands.get(handIndex);
            }
        }

        return bestHand;
    }
}