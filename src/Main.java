//Code referenced, but updated from, https://github.com/EricCharnesky/CIS2353-Summer2023/blob/main/HoldemStart/src/project2/holdem/Project2Holdem.java
//Personal updates include:
//  -made the computer player always call instead of it being random
//  -implemented print statements to display the odds of winning

import java.util.Scanner;

public class Main {

    static int playerMoney = 100;
    static int computerMoney = 100;
    static Scanner keyboard = new Scanner(System.in);
    static boolean playerBetsFirst = true;
    static int pot;

    public static void main(String[] args) {

        while (playerMoney > 1 && computerMoney > 1) {
            System.out.println("Player has $" + playerMoney);
            System.out.println("Computer has $" + computerMoney);
            pot = 3;

            if (playerBetsFirst) {
                System.out.println("Player bets first $2");
                System.out.println("Computer bets $1");
                playerMoney -= 2;
                computerMoney--;
            } else {
                System.out.println("Computer bets first $2");
                System.out.println("Player bets $1");
                computerMoney -= 2;
                playerMoney--;
            }

            Deck deck = new Deck();
            HoldemHand playerHand = new HoldemHand(deck.deal(), deck.deal());
            HoldemHand computerHand = new HoldemHand(deck.deal(), deck.deal());

            System.out.println("Players Hand: " + playerHand.toString());
            playerHand.oddsOfWinning();

            if (bettingRoundDidSomeoneFold(1)) {
                continue;
            }

            Card sharedCard1 = deck.deal();
            Card sharedCard2 = deck.deal();
            Card sharedCard3 = deck.deal();

            playerHand.addSharedCard(sharedCard1);
            playerHand.addSharedCard(sharedCard2);
            playerHand.addSharedCard(sharedCard3);

            computerHand.addSharedCard(sharedCard1);
            computerHand.addSharedCard(sharedCard2);
            computerHand.addSharedCard(sharedCard3);

            System.out.println("Shared cards: "
                + sharedCard1 + ", "
                + sharedCard2 + ", "
                + sharedCard3);

            System.out.println("Players Hand: " + playerHand.toString());
            playerHand.oddsOfWinning();

            if (playerBetsFirst) {
                String choice = promptForCallRaiseOrFold();
                if (choice.equalsIgnoreCase("fold")) {
                    System.out.println("You fold!");
                    computerMoney += pot;
                    continue; //ends the round, starts the loop again
                } else if (choice.equalsIgnoreCase("call")) {
                    System.out.println("You call!");
                    System.out.println("Computer calls!");
                } else {
                    int raiseAmount = getRaiseAmount();
                    if (bettingRoundDidSomeoneFold(raiseAmount)) {
                        continue;
                    }
                }
            } else {
                if (bettingRoundDidSomeoneFold(0)) {
                    continue;
                }
            }

            Card sharedCard4 = deck.deal();

            playerHand.addSharedCard(sharedCard4);
            computerHand.addSharedCard(sharedCard4);

            System.out.println("Shared cards: "
                + sharedCard1 + ", "
                + sharedCard2 + ", "
                + sharedCard3 + ", "
                + sharedCard4);

            System.out.println("Players Hand: " + playerHand.toString());
            playerHand.oddsOfWinning();

            if (playerBetsFirst) {
                String choice = promptForCallRaiseOrFold();

                if (choice.equalsIgnoreCase("fold")) {
                    System.out.println("You fold!");
                    computerMoney += pot;
                    continue; //ends the round, starts the loop again
                } else if (choice.equalsIgnoreCase("call")) {
                    System.out.println("You call!");
                    System.out.println("Computer calls!");
                } else  {
                    int raiseAmount = getRaiseAmount();
                    if (bettingRoundDidSomeoneFold(raiseAmount)) {
                        continue;
                    }
                }
            } else {
                if (bettingRoundDidSomeoneFold(0)) {
                    continue;
                }
            }

            Card sharedCard5 = deck.deal();

            playerHand.addSharedCard(sharedCard5);
            computerHand.addSharedCard(sharedCard5);

            System.out.println("Shared cards: "
                    + sharedCard1 + ", "
                    + sharedCard2 + ", "
                    + sharedCard3 + ", "
                    + sharedCard4 + ", "
                    + sharedCard5);

            System.out.println("Players Hand: " + playerHand.toString());
            playerHand.oddsOfWinning();

            if (playerBetsFirst) {
                String choice = promptForCallRaiseOrFold();

                if (choice.equalsIgnoreCase("fold")) {
                    System.out.println("You fold!");
                    computerMoney += pot;
                    continue; //ends the round, starts the loop again
                } else if (choice.equalsIgnoreCase("call")) {
                    System.out.println("You call!");
                    System.out.println("Computer calls!");
                } else  {
                    int raiseAmount = getRaiseAmount();
                    if (bettingRoundDidSomeoneFold(raiseAmount)) {
                        continue;
                    }
                }
            } else {
                if (bettingRoundDidSomeoneFold(0)) {
                    continue;
                }
            }

            System.out.println("Players Hand: " + playerHand.toString());
            System.out.println("Computers Hand: " + computerHand.toString());
            System.out.println("Players Best Hand: " + playerHand.getBestPossibleHand());
            System.out.println("Computers Best Hand: " + computerHand.getBestPossibleHand());

            int result = playerHand.getBestPossibleHand().compareTo(computerHand.getBestPossibleHand());

            if (result > 0) {
                System.out.println("Player Wins!");
                playerMoney += pot;
            } else if (result == 0) {
                System.out.println("Draw!");
                playerMoney += pot / 2;
                computerMoney += pot / 2;
            } else {
                System.out.println("Computer Wins!");
                computerMoney += pot;
            }

            playerBetsFirst = !playerBetsFirst;
        }
    }

    private static boolean bettingRoundDidSomeoneFold(int raiseAmount) {

        if (playerBetsFirst) {
            System.out.println("Computer calls!");
            computerMoney -= raiseAmount;
            pot += raiseAmount;
        } else {
            String choice = promptForCallRaiseOrFold();
            if (choice.equalsIgnoreCase("fold")) {
                System.out.println("You fold!");
                computerMoney += pot;
                return true; //ends the round, starts the loop again
            } else if (choice.equalsIgnoreCase("call")) {
                pot++;
                playerMoney -= raiseAmount;
                System.out.println("You call!");
                System.out.println("Computer calls!");
            } else {
                pot += raiseAmount;
                raiseAmount = getRaiseAmount();
                pot += raiseAmount;
                playerMoney -= raiseAmount;
                System.out.println("Computer calls");
                computerMoney -= raiseAmount;
                pot += raiseAmount;
            }
        }
        return false;
    }

    private static int getRaiseAmount() {
        System.out.println("How much do you want to raise?");
        int raise = Integer.parseInt(keyboard.nextLine());

        while (raise > computerMoney || raise > playerMoney) {
            System.out.println("You can't raise more than $" + Math.min(computerMoney, playerMoney));
            System.out.println("How much do you want to raise?");
            raise =  Integer.parseInt(keyboard.nextLine());
        }
        return raise;
    }

    private static String promptForCallRaiseOrFold() {
        String choice = " ";

        while (!choice.equalsIgnoreCase("call") &&
                !choice.equalsIgnoreCase("raise") &&
                !choice.equalsIgnoreCase("fold")) {

            System.out.println("Do you want to call, raise, or fold?");
            choice = keyboard.nextLine();
        }
        return choice;
    }
}