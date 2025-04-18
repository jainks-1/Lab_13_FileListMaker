// Kaden Jain

// to move function -  store the variable, then remove it frm array, then move to users desired position

import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import static java.lang.System.out;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class FileListMaker {

    static ArrayList<String> mainArray = new ArrayList<>();
    static boolean needsToBeSaved = false;
    static File currentFile = null;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        try {
            OpenFile();

            for (String l : mainArray) {
                System.out.println(l);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String menuChoice;
        boolean cont = false;

        do {
            System.out.println(mainArray);
            displayMenu();

            menuChoice = SafeInput.getRegExString(in, "\nEnter your Choice [AaDdIiMmOoSsCcVvQq]", "^[AaDdIiMmOoSsCcVvQq]$").toUpperCase();

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
                case "M":
                    moveItem();
                    break;
                case "O":
                    if (needsToBeSaved) {
                        boolean save = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening new file?");
                        if (save) saveFile();
                    }
                    OpenFile();
                    break;
                case "S":
                    saveFile();
                    break;
                case "C":
                    clearList();
                    break;
                case "V":
                    printList();
                    break;
                case "Q":
                    if (needsToBeSaved) {
                        boolean save = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before quitting?");
                        if (save) saveFile();
                    }
                    cont = SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
                    break;
            }
        } while (!cont);
    }

    private static void OpenFile() throws IOException {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec;

        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            currentFile = selectedFile;
            mainArray.clear();

            Path file = selectedFile.toPath();
            InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            int line = 0;
            while (reader.ready()) {
                rec = reader.readLine();
                mainArray.add(rec);
                line++;
                System.out.printf("\nLine %4d %-60s ", line, rec);
            }

            System.out.println();
            reader.close();
            needsToBeSaved = false;
            System.out.println("\n\nData file read!");
        } else {
            System.out.println("Failed to choose a file to process");
            System.out.println("Run the program again!");
            System.exit(0);
        }
    }

    private static void saveFile() throws IOException {
        if (currentFile == null) {
            JFileChooser chooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                if (!currentFile.getName().endsWith(".txt")) {
                    currentFile = new File(currentFile.toString() + ".txt");
                }
            } else {
                System.out.println("Save cancelled.");
                return;
            }
        }

        Path file = currentFile.toPath();
        OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        for (String rec : mainArray) {
            writer.write(rec, 0, rec.length());
            writer.newLine();
        }

        writer.close();
        needsToBeSaved = false;
        System.out.println("Data file written!");
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("I - Insert an item into the list");
        System.out.println("M - Move an item in the list");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list to disk");
        System.out.println("C - Clear the current list");
        System.out.println("V - View the list");
        System.out.println("Q - Quit the program");
    }

    private static void addItem() {
        Scanner in = new Scanner(System.in);
        String userInput = SafeInput.getNonZeroLenString(in, "Enter item to add");
        mainArray.add(userInput);
        needsToBeSaved = true;
        System.out.println("Added item: " + userInput + "\n\n");
    }

    private static void deleteItem() {
        Scanner in = new Scanner(System.in);

        if (mainArray.isEmpty()) {
            System.out.println("The list is empty. Nothing to delete.\n\n");
            return;
        }

        printList();
        int index = SafeInput.getRangedInt(in, "Enter number of item to delete", 1, mainArray.size()) - 1;
        System.out.println("Deleted item: " + mainArray.remove(index) + "\n\n");
        needsToBeSaved = true;
    }

    private static void insertItem() {
        Scanner in = new Scanner(System.in);
        printList();
        String userInput = SafeInput.getNonZeroLenString(in, "Enter item to insert");
        int index = SafeInput.getRangedInt(in, "Enter position to insert at", 1, mainArray.size() + 1) - 1;
        mainArray.add(index, userInput);
        needsToBeSaved = true;
        System.out.println("Inserted item: " + userInput + " at position " + (index + 1) + "\n\n");
    }

    private static void moveItem() {
        Scanner in = new Scanner(System.in);

        if (mainArray.isEmpty()) {
            System.out.println("The list is empty. Nothing to move.\n\n");
            return;
        }

        printList();
        int fromIndex = SafeInput.getRangedInt(in, "Enter number of item to move", 1, mainArray.size()) - 1;
        int toIndex = SafeInput.getRangedInt(in, "Enter position to move to", 1, mainArray.size()) - 1;

        String item = mainArray.remove(fromIndex);
        mainArray.add(toIndex, item);
        needsToBeSaved = true;

        System.out.println("Moved item to position " + (toIndex + 1) + ": " + item + "\n\n");
    }

    private static void clearList() {
        Scanner in = new Scanner(System.in);

        boolean confirm = SafeInput.getYNConfirm(in, "Are you sure you want to clear the entire list?");
        if (confirm) {
            mainArray.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static void printList() {
        System.out.println("\nNumbered List:");
        for (int i = 0; i < mainArray.size(); i++) {
            System.out.println((i + 1) + ". " + mainArray.get(i));
        }
        System.out.println();
    }
}