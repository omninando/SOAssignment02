package com.company;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Fernando de Almeida Coelho and Ana Luiza Motta Gomes
 * on 15/09/14.
 */
public class HistoryManagement {
    ArrayList<String> history;

    /**
     * Method load() opens a saved text file with the history then
     * copies all of it to the history ArrayList.
     */
    public ArrayList<String> load(ArrayList<String> history) throws IOException{
        File aFile = new File("history.txt");

        if (!aFile.isFile()) return history;
        BufferedReader reader = null;

        try { reader = new BufferedReader(new FileReader(aFile)); }
        catch (FileNotFoundException e1) {
            // TODO handle Exception
            e1.printStackTrace();
            return history;
        }
        String aLine = null;
        while ((aLine = reader.readLine()) != null) { history.add(aLine); }

        reader.close();
        return history;
    }

    /**
     * Method save() receive the history and save it in a text file.
     */
    public void save(ArrayList<String> history)
    {
        File file = new File("history.txt");
        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter("history.txt", true)
                )
        )) { for (String aHistory : history) out.println(aHistory); }
        catch (IOException e) { /*logger.error(e);*/ }

    }
}
