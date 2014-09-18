package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Edited by Fernando de Almeida Coelho and Ana Luiza Motta Gomes
 * on 15/09/14.
 *
 * The SimpleShell class has the program to run a Shell Interface
 * created using  Java Language. This  application was made as an
 * Assignment for CSCI 215 Operating Systems at Clark University.
 */
public class SimpleShell
{
    public static void main(String[] args) throws java.io.IOException
    {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> history = new ArrayList<String>();
		HistoryManagement hm = new HistoryManagement();
        ProcessBuilder directory = new ProcessBuilder(null,null);
        CommandManagement cm = new CommandManagement();
        String commandLine;
        history = hm.load(history);

        while (true) // we break out with <control><C>
        {
            System.out.print("jsh>"); // read what they entered
            commandLine = console.readLine();

            if (commandLine.equals("")) continue; // if they entered a return, just loop again
            else if (commandLine.equalsIgnoreCase("exit")) { // if enter the word exit, kills the program
                System.out.println("Bye!");
                System.exit(0);
            }

            ArrayList<String> parameters = new ArrayList<>();
            String item = "";
            String[] lineSplit = commandLine.split(" ");

            Collections.addAll(parameters, lineSplit); // Using collections to add parameters given a line splitter

            history.add(commandLine.concat(item)); // Adding the whole line to the history


            if (parameters.get(0).charAt(0) == '!') // Option to access history by typing "! + number"
            {
                try {
                    cm.accessHistoryCommands(parameters, history); // Calling the method to access history by typing "! + number"
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            try {
                // Comparing if the first parameter is '^C', 'history', 'cd' or the default behavior
                switch (parameters.get(0).toLowerCase()) { // It is case sensitive
                    case "^C": // If the user type Ctrl+C, kills the program
                        System.out.println("Bye!");
                        System.exit(0);
                        break;

                    case "history": // Method to deal with the history command
                        for (int i = 0; i < history.size(); i++)
                        {
                            System.out.println(i+1 + " " + history.get(i));
                        }
                        break;

                    case "cd": // Method to deal with the cd command
                        cm.cdCommand(parameters, directory);
                        break;

                    default: // deal with other commands
                        ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters
                        if (directory.directory()!=null) {
                            pb.directory(directory.directory());
                        }
                        cm.outputStream(pb); // get the output stream
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            hm.save(history); // Saving all command lines in this session in a text file
        }
    }
}
