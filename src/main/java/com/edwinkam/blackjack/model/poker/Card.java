package com.edwinkam.blackjack.model.poker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private int number;

    public Card(String str) {
        switch (str) {
            case "A":
                number = 1;
                break;
            case "2":
                number = 2;
                break;
            case "3":
                number = 3;
                break;
            case "4":
                number = 4;
                break;
            case "5":
                number = 5;
                break;
            case "6":
                number = 6;
                break;
            case "7":
                number = 7;
                break;
            case "8":
                number = 8;
                break;
            case "9":
                number = 9;
                break;
            case "10":
                number = 10;
                break;
            case "J":
                number = 11;
                break;
            case "Q":
                number = 12;
                break;
            case "K":
                number = 13;
                break;
            default:
                throw new IllegalArgumentException("Card string must be a number between 2 and 10, or one of 'A', 'J', 'Q', 'K'");
        }
    }

    public int getValue() {
        switch (number) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return number;
            case 11:
            case 12:
            case 13:
                return 10;
            default:
                return 0;
        }
    }
    public String toString() {
        switch (number) {
            case 1:
                return "A";
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return String.valueOf(number);
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                return "UNKNOWN";
        }
    }

}
