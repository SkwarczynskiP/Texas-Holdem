//Code referenced from https://github.com/EricCharnesky/CIS2353-Summer2023/blob/main/HoldemStart/src/project2/holdem/PokerHand.java

import java.util.ArrayList;
import java.util.Collections;

public class PokerHand implements Comparable<PokerHand> {

    private ArrayList<Card> cards;
    private int[] countOfFaces;

    public PokerHand(ArrayList<Card> cards) {
        this.cards = new ArrayList<Card>(5);

        for (Card card : cards) {
            this.cards.add(new Card(card));
        }

        Collections.sort(this.cards);

        countOfFaces = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (Card card : cards) {
            countOfFaces[card.getFace().ordinal()]++;
        }
    }


    public enum Rank {
        HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH
    }

    public enum Result {
        WIN, DRAW, LOSE
    }

    @Override
    public int compareTo(PokerHand o) {
        Result result = call(o);
        if (result == result.WIN) {
            return 1;
        } else if (result == result.LOSE) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getHandRank().toString());
        builder.append(" - ");
        for (Card card : cards) {
            builder.append(card.toString());
            builder.append(" ");
        }
        return builder.toString();
    }

    public Result call(PokerHand o) {
        if (getHandRank().ordinal() > o.getHandRank().ordinal()) {
            return Result.WIN;
        }

        if (getHandRank().ordinal() < o.getHandRank().ordinal()) {
            return Result.LOSE;
        }

        //Tie Breaker
        if (getHandRank() == Rank.STRAIGHT_FLUSH || getHandRank() == Rank.STRAIGHT) {
            if (cards.get(0).getFace().ordinal() > o.cards.get(0).getFace().ordinal()) {
                return Result.WIN;
            }
            if (cards.get(0).getFace().ordinal() < o.cards.get(0).getFace().ordinal()) {
                return Result.LOSE;
            }
            return Result.DRAW;
        }

        if (getHandRank() == Rank.FOUR_OF_A_KIND) {
            int myFourOfKindOrdinal = getIndexForFaceXTimes(4);
            int otherFourOfKindOrdinal = o.getIndexForFaceXTimes(4);
            if (myFourOfKindOrdinal > otherFourOfKindOrdinal) {
                return Result.WIN;
            }
            if (myFourOfKindOrdinal < otherFourOfKindOrdinal) {
                return Result.LOSE;
            }
            return Result.DRAW;
        }

        if (getHandRank() == Rank.FLUSH) {
            return tieBreakAllFiveCards(o);
        }

        if (getHandRank() == Rank.THREE_OF_A_KIND || getHandRank() == Rank.FULL_HOUSE) {
            int myThreeOfKindOrdinal = getIndexForFaceXTimes(3);
            int otherThreeOfKindOrdinal = getIndexForFaceXTimes(3);
            if (myThreeOfKindOrdinal > otherThreeOfKindOrdinal) {
                return Result.WIN;
            }
            if (myThreeOfKindOrdinal < otherThreeOfKindOrdinal) {
                return Result.LOSE;
            }
            return Result.DRAW;
        }

        if (getHandRank() == Rank.TWO_PAIR) {
            int myLowerPairOrdinal = getIndexForFaceXTimes(2);
            int otherLowerPairOrdinal = getIndexForFaceXTimes(2);

            int myHigherPairOrdinal = getIndexForHigherPair();
            int otherHigherPairOrdinal = getIndexForHigherPair();

            if (myHigherPairOrdinal > otherHigherPairOrdinal) {
                return Result.WIN;
            }
            if (myHigherPairOrdinal < otherHigherPairOrdinal) {
                return Result.LOSE;
            }

            if (myLowerPairOrdinal > otherLowerPairOrdinal) {
                return Result.WIN;
            }
            if (myLowerPairOrdinal < otherLowerPairOrdinal) {
                return Result.LOSE;
            }

            return tieBreakAllFiveCards(o);
        }

        //if we haven't tie broken already, it must be high card
        return tieBreakAllFiveCards(o);
    }

    private Result tieBreakAllFiveCards(PokerHand o) {
        for (int index = cards.size() - 1; index >= 0; index--) {
            if (cards.get(index).getFace().ordinal() > o.cards.get(index).getFace().ordinal()) {
                return Result.WIN;
            }
            if (cards.get(index).getFace().ordinal() < o.cards.get(index).getFace().ordinal()) {
                return Result.LOSE;
            }
        }

        return Result.DRAW;
    }

    public Rank getHandRank() {
        if (isStraightFlush()) {
            return Rank.STRAIGHT_FLUSH;
        }
        if (isFourOfAKind()) {
            return Rank.FOUR_OF_A_KIND;
        }
        if (isFullHouse()) {
            return Rank.FULL_HOUSE;
        }
        if (isFlush()) {
            return Rank.FLUSH;
        }
        if (isStraight()) {
            return Rank.STRAIGHT;
        }
        if (isThreeOfAKind()) {
            return Rank.THREE_OF_A_KIND;
        }
        if (isTwoPair()) {
            return Rank.TWO_PAIR;
        }
        if (isPair()) {
            return Rank.PAIR;
        }

        return Rank.HIGH_CARD;
    }

    private boolean isStraightFlush() {
        return isFlush() && isStraight();
    }

    private boolean isFourOfAKind() {
        return checkForFaceXTimes(4);
    }

    private boolean isFullHouse() {
        return isThreeOfAKind() && isPair();
    }

    private boolean isFlush() {
        return cards.get(0).getSuit() == cards.get(1).getSuit() &&
                cards.get(1).getSuit() == cards.get(2).getSuit() &&
                cards.get(2).getSuit() == cards.get(3).getSuit() &&
                cards.get(3).getSuit() == cards.get(4).getSuit();
    }

    private boolean isStraight() {
        return cards.get(0).getFace().ordinal() == cards.get(1).getFace().ordinal() - 1 &&
                cards.get(1).getFace().ordinal() == cards.get(2).getFace().ordinal() - 1 &&
                cards.get(2).getFace().ordinal() == cards.get(3).getFace().ordinal() - 1 &&
                cards.get(3).getFace().ordinal() == cards.get(4).getFace().ordinal() - 1;
    }

    private boolean isThreeOfAKind() {
        return checkForFaceXTimes(3);
    }

    private boolean isTwoPair() {
        int pairCount = 0;
        for (int faceCount : countOfFaces) {
            if (faceCount == 2) {
                pairCount++;
            }
        }
        return pairCount == 2;
    }

    private boolean isPair() {
        return checkForFaceXTimes(2);
    }

    private boolean checkForFaceXTimes(int times) {
        for (int faceCount : countOfFaces) {
            if (faceCount == times) {
                return true;
            }
        }
        return false;
    }

    private int getIndexForFaceXTimes(int times) {
        for (int index = 0; index < countOfFaces.length; index++) {
            if (countOfFaces[index] == times) {
                return index;
            }
        }
        return -1;
    }

    private int getIndexForHigherPair() {
        for (int index = countOfFaces.length - 1; index >= 0; index--) {
            if (countOfFaces[index] == 2) {
                return index;
            }
        }
        return -1;
    }
}