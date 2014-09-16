package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


public class SimpleShell
{
    public static void main(String[] args) throws java.io.IOException
    {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> history = new ArrayList<String>();
		HistoryManagement hm = new HistoryManagement();
        ProcessBuilder directory = new ProcessBuilder(null,null);
        String commandLine;
        history = hm.load(history);

        Runtime.getRuntime().addShutdownHook(new Thread() { // If they entered Ctrl+C kills the program
            @Override
            public void run() { System.out.println("Bye!"); }
        });

        while (true) // we break out with <control><C>
        {
            System.out.print("jsh>"); // read what they entered
            commandLine = console.readLine();

            if (commandLine.equals("")) continue; // if they entered a return, just loop again
            else if (commandLine.equalsIgnoreCase("exit")) { // if enter the word exit, kills the program
                System.out.println("Bye!");
                System.exit(0);
            }

            ArrayList<String> parameters = new ArrayList<String>();
            String item = "";
            String[] lineSplit = commandLine.split(" ");

            Collections.addAll(parameters, lineSplit); // Using collections to add parameters given a line splitter

            history.add(commandLine.concat(item)); // Adding the whole line to the history

            switch (parameters.get(0)) { // Comparing if the first parameter is 'history', 'cd' or the default behavior
                case "history":
                    for (int i = 0; i < history.size(); i++) System.out.println(i+1 + " " + history.get(i));
                    break;

                case "cd":
                    cdCommand(parameters, directory);
                    break;

                default:
                    ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters
                    if (directory.directory()!=null) pb.directory(directory.directory());
                    outputStream(pb); // get the output stream
            }

            hm.save(history); // Saving all command lines in this session in a text file
        }
    }

    /**
     * Method cdCommand() receives  Parameters and the Directories
     * to be changed, change the directory if necessary then calls
     * outputStream() to start the process.
     */
    public static void cdCommand(ArrayList<String> parameters, ProcessBuilder directory) throws IOException
    {
        if (parameters.get(1).compareTo("..")==0)
        {
            ProcessBuilder pb = new ProcessBuilder(parameters);
            pb.directory(new File(System.getProperty("user.dir")).getParentFile());

            if (directory.directory() != null) directory.directory(directory.directory().getParentFile());
            else  directory.directory(new File(System.getProperty("user.dir")).getParentFile());

            outputStream(pb); // get the output stream
        } else if (parameters.get(1).contains("/")) {
            ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters

            if (directory.directory() != null) directory.directory(directory.directory().getParentFile());
            else  directory.directory(new File(System.getProperty("user.dir")).getParentFile());

            String path = parameters.get(1);

            if (new File(path).exists()) {
                directory.directory(new File(path));
                pb.directory(new File(path));
            }
            outputStream(pb); // get the output stream
        } else{
            ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters

            if (directory.directory() != null) pb.directory(directory.directory());

            String path = System.getProperty("user.dir") + "/" + parameters.get(1);

            if (new File(path).exists()) {
                directory.directory(new File(path));
                pb.directory(new File(path));
            }
            outputStream(pb); // get the output stream
        }
    }


    /**
     * Method outputStream() receives the ProcessBuilder and
     * start  the process itself and outputs the result from
     * the process.
     */
    public static void outputStream(ProcessBuilder pb) throws IOException
    {
        Process process = pb.start();

        InputStream is = process.getInputStream(); // get the output stream
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String aLine; // read what is returned by the command
        while ((aLine = br.readLine()) != null) System.out.println(aLine);

        br.close();
    }
}
