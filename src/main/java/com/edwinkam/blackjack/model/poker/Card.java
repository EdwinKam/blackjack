package com.edwinkam.blackjack.model.poker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private int id;

    public Card(String str) {
        switch (str) {
            case "A":
                id = 1;
                break;
            case "2":
                id = 2;
                break;
            case "3":
                id = 3;
                break;
            case "4":
                id = 4;
                break;
            case "5":
                id = 5;
                break;
            case "6":
                id = 6;
                break;
            case "7":
                id = 7;
                break;
            case "8":
                id = 8;
                break;
            case "9":
                id = 9;
                break;
            case "10":
                id = 10;
                break;
            case "J":
                id = 11;
                break;
            case "Q":
                id = 12;
                break;
            case "K":
                id = 13;
                break;
            default:
                throw new IllegalArgumentException("Card string must be a id between 2 and 10, or one of 'A', 'J', 'Q', 'K'");
        }
    }

    public int getValue() {
        switch (id) {
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
                return id;
            case 11:
            case 12:
            case 13:
                return 10;
            default:
                return 0;
        }
    }
    public String toString() {
        switch (id) {
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
                return String.valueOf(id);
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
