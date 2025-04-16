// Kaden Jain

import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class FileListMaker {

    private static ArrayList<String> mainArray = new ArrayList<>(); // Make it static

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String menuChoice = " ";

        boolean cont = false; // determines if program terminates or not from get y/n confirm

        do {
            // print the main array as an array, non list view
            System.out.println(mainArray);

            // print the menu
            displayMenu();

            // get user choice
            menuChoice = SafeInput.getRegExString(in, "\nEnter your Choice [Aa, Dd, Ii, Pp, Qq]", "^[AaDdIiPpQq]$");
            menuChoice = menuChoice.toUpperCase(); // makes uppercase

            switch (menuChoice) {
                case "A":
                    addItem();
                    break;
                case "D":
                    deleteItem();
                    break;
                case "I":
                    insertItem();
                    break;
                case "P":
                    printList();
                    break;
                case "Q":
                    cont = SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
            }
        }while (!cont);

    }

    // make the menu
    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("I - Insert an item into the list");
        System.out.println("P - Print numbered list");
        System.out.println("Q - Quit the program");
    }

    // adds item to end of list
    private static void addItem() {
        Scanner in = new Scanner(System.in);
        String userInput= " ";
        userInput = SafeInput.getNonZeroLenString(in, "Enter item to add");

        mainArray.add(userInput);
        System.out.println("Added item: " + userInput + "\n\n");
    }

    // deletes item by number in list
    private static void deleteItem(){
        Scanner in = new Scanner(System.in);

        if (mainArray.isEmpty()) {
            System.out.println("The list is empty. Nothing to delete.\n\n");
            return;
        }

        printList();
        int index = SafeInput.getRangedInt(in, "Enter number of item to delete", 1, mainArray.size()) - 1;

        System.out.println("Deleted item: " + mainArray.remove(index) + "\n\n");
    }

    // insert an item into a specific part of the list
    private static void insertItem(){
        Scanner in = new Scanner(System.in);

        printList();

        String userInput = SafeInput.getNonZeroLenString(in, "Enter item to insert");
        int index = SafeInput.getRangedInt(in, "Enter position to insert at", 1, mainArray.size() + 1) - 1;

        mainArray.add(index, userInput);
        System.out.println("Inserted item: " + userInput + " at position " + (index + 1) + "\n\n");
    }

    // prints the list vertically + numbered
    private static void printList(){
        System.out.println("\nNumbered List:");
        for (int i = 0; i < mainArray.size(); i++) {
            System.out.println((i + 1) + ". " + mainArray.get(i));
        }
        System.out.println();
    }

}