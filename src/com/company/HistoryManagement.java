package com.company;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Fernando on 15/09/14.
 */
public class HistoryManagement {
    ArrayList<String> history;

    public void save(ArrayList<String> history) {
        File file = new File("history.txt");

        try(PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter("history.txt", true)
                )
        )) {
            for (String aHistory : history) {
                out.println(aHistory);
            }
        } catch (IOException e) {
            //logger.error(e);
        }

    }

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
}
