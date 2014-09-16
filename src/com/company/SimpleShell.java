package com.company;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleShell
{
	public static void main(String[] args) throws java.io.IOException,CommandException {
        String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> history = new ArrayList<String>();
		HistoryManagement hm = new HistoryManagement();
        history = hm.load(history);

        // we break out with <control><C>
        while (true) {
            // read what they entered
            System.out.print("jsh>");
            commandLine = console.readLine();

            // if they entered a return, just loop again
            if (commandLine.equals("")) continue;
                // if they entered the word exit, kills the program
            else if (commandLine.equalsIgnoreCase("exit")) {
                System.out.println("Bye!");
                System.exit(0);
            }
            else if (commandLine.equalsIgnoreCase("quit")) {
                System.out.println("Bye!");
                System.exit(0);
            }

            ArrayList<String> parameters = new ArrayList<String>();
            String item = "";
            String[] lineSplit = commandLine.split(" ");

            //Using collections to add parameters given a line splitter
            Collections.addAll(parameters, lineSplit);

            //Using collections to add the hole line to the history
            history.add(commandLine.concat(item));

            if (parameters.get(0).compareTo("history")==0){
                for (int i = 0; i < history.size(); i++) {
                    System.out.println(i+1 +" " + history.get(i));
                }
            }else {
                // create the process with the list of parameters
                ProcessBuilder pb = new ProcessBuilder(parameters);
                Process process = pb.start();

                // get the output stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                // read what is returned by the command
                String aLine;
                while ((aLine = br.readLine()) != null) System.out.println(aLine);

                br.close();
            }


            //Saving every command in a text file
            hm.save(history);
        }
    }
}
