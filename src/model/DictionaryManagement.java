package model;

import model.Dictionary;

import java.util.Scanner;
import java.io.*;

public class DictionaryManagement {
    public Dictionary EnVi = new Dictionary();

    public boolean insertFromFile() {
        try {
            File dicFile = new File("dictionaries.txt");
            FileReader fileReader = new FileReader(dicFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] word = line.split("\t");
                EnVi.add(word[0], word[1]);
            }
            EnVi.sort();
            reader.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("Can't open dictionaries.txt!");
            return false;
        }
    }
    public void dictionaryLookup() {
        Scanner readInput = new Scanner(System.in);
        System.out.print("Lookup definition of: ");
        boolean found = false;
        String word = readInput.nextLine();
        for (int i = 0; i < EnVi.getSize(); i++)
            if (word.equals(EnVi.getEn(i))) {
                found = true;
                System.out.println(EnVi.getVi(i));
                break;
            }
        if (!found)
            System.out.println("Couldn't find any definition for \"" + word + "\"");
        System.out.println();
    }
    public void search() {
        System.out.print("Find all word begin with: ");
        Scanner readInput = new Scanner(System.in);
        String target = readInput.nextLine();
        boolean found = false;
        for (int i = 0; i < EnVi.getSize(); i++)
            if (EnVi.getEn(i).indexOf(target) == 0) {
                System.out.println(EnVi.getEn(i));
                found = true;
            }
        if (!found) {
            System.out.println("Couldn't find any word begin with \"" + target + "\"");
        }
        System.out.println();
    }
}