package com.company;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Fernando de Almeida Coelho  and Ana Luiza Motta Gomes
 * on 15/09/14.
 *
 * The HistoryManagement class manages loading and saving the whole
 * history of commands  typed by the user in a text  file so it can
 * be preserved through different sessions.
 */
public class HistoryManagement
{

    /**
     * Method load() opens a saved text file with the history then
     * copies all of it, line by line, to the history ArrayList of
     * type String.
     */
    public ArrayList<String> load(ArrayList<String> history) throws IOException
    {
        File aFile = new File("history.txt");

        if (!aFile.isFile()) {
            return history;
        }
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(aFile));
        }
        catch (FileNotFoundException e1) {
            // TODO handle Exception
            e1.printStackTrace();
            return history;
        }
        String aLine = null;
        while ((aLine = reader.readLine()) != null) {
            history.add(aLine);
        }

        reader.close();
        return history;
    }

    /**
     * Method save() receive the history in a ArrayList of type
     * String and  save it in a  text file by iterating through
     * its lines.
     */
    public void save(ArrayList<String> history) throws IOException
    {
        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter("history.txt", true)
                )
        )) {
            for (String aHistory : history) out.println(aHistory);
        }
        catch (IOException e) {
            throw new IOException("no such file or directory");
        }
    }
}
