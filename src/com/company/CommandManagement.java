package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Fernando de Almeida Coelho  and Ana Luiza Motta Gomes
 * on 15/09/14.
 *
 * The CommandManagement class manages how the command lines cd, as
 * well as how the  history command lines  are accessed. This class
 * also  output  the result  stream of each  command  even if dealt
 * elsewhere.
 */
public class CommandManagement
{

    /**
     * Method cdCommand() receives  Parameters and the Directories
     * to be changed, change the directory if necessary then calls
     * outputStream() to start the process.
     */
    public void cdCommand(ArrayList<String> parameters, ProcessBuilder directory) throws IOException
    {
        if (parameters.size() != 1) {
            if(parameters.get(1).equals("..")) {
                ProcessBuilder pb = new ProcessBuilder(parameters);
                pb.directory(new File(System.getProperty("user.dir")).getParentFile());

                if (directory.directory() != null) {
                    directory.directory(directory.directory().getParentFile());
                }
                else {
                    directory.directory(new File(System.getProperty("user.dir")).getParentFile());
                }
                outputStream(pb); // get the output stream
            } else if (parameters.get(1).charAt(0) == '/') {
                ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters

                if (directory.directory() != null) {
                    directory.directory(directory.directory().getParentFile());
                } else {
                    directory.directory(new File(System.getProperty("user.dir")).getParentFile());
                }

                String path = parameters.get(1);

                try {
                    validateFile(directory, pb, path, parameters.get(1));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                outputStream(pb); // get the output stream
            } else {
                ProcessBuilder pb = new ProcessBuilder(parameters); // create the process with the list of parameters
                String path;
                String path2;
                if (directory.directory() != null) {
                    pb.directory(directory.directory());
                    path = pb.directory() + "/" + parameters.get(1);
                    try {
                        validateFile(directory, pb, path,parameters.get(1));
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    path2 = System.getProperty("user.dir") + "/" + parameters.get(1);
                    try {
                        validateFile(directory, pb, path2,parameters.get(1));
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                outputStream(pb); // get the output stream
            }
        }
    }

    /**
     *
     * Method  accessHistoryCommands() receives  the parameters
     * and  history  ArrayLists  and compare  if the  number of
     * index given as a parameter exists and return the command
     * line from the history.
     */
    public void accessHistoryCommands(ArrayList<String> parameters, ArrayList<String> history) throws IOException
    {
        int length = parameters.get(0).length()-1;
        char[] number = new char[length];
        String[] historyWords;
        for (int j = 1; j < parameters.get(0).length(); j++) {
            number[j-1] = parameters.get(0).charAt(j);
        }

        int number2 = (Integer.parseInt(new String(number)))-1;
        if (number2<history.size() && number2>-1) {
            parameters.removeAll(parameters);
            historyWords = history.get(number2).split(" ");
            Collections.addAll(parameters, historyWords);

            for (String historyWord : historyWords) {
                System.out.print(historyWord + " ");
            }
            System.out.print("\n");
        } else {
            throw new IOException("!" +  number2 + ": event not found");
        }
    }

    /**
     *
     * Method validateFile() receives the directory, the process
     * builder,  the path  and the  parameter  to  validate  the
     * existence  of the  directory searched by  the cdCommand()
     * method.
     */
    private void validateFile(ProcessBuilder directory,
                                     ProcessBuilder pb, String path,String parameter) throws IOException
    {
        if (new File(path).exists()) {
            directory.directory(new File(path));
            pb.directory(new File(path));
        } else {
            throw new IOException("cd: " +  parameter + ": no such file or directory");
        }
    }


    /**
     * Method outputStream() receives the ProcessBuilder and
     * start  the process itself and outputs the result from
     * the process.
     */
    public void outputStream(ProcessBuilder pb) throws IOException
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
