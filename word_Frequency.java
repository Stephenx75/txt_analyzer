import java.awt.HeadlessException;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/*
    Algorithm:

    Goal: Taking a text file, find how often each word within occurs.

    1. Have the user select a txt file. 
    2. Taking the selected file, Read line by line and store into an ArrayList [text].
    3. In a seperate function that reads each line from [text]:
        a. Remove punctuation marks 
        b. Remove numbers
    4. Pass the line through a tokenizer to retrieve each word within.
    5. On a words first occurence:
        a. Add it to the word chart. [word_Chart]
        b. Begin counting its frequency at 1 by adding a new position in [word_Freq].
    6. When a word has already appeared before:
        a. Increase its frequency value by 1. [word_Freq].
    7. REPEAT until you reach the end of the [text] file.
    8. Pass these parallel arraylists to a printing function:
        a. Prints each word and its relative frequency on one line.concat
        b. REPEAT until the entire ArrayLists have been printed.
*/
        
public class word_Frequency
{
	public static void main(String[] args)
	{
        ArrayList text = get_Text();
        calc_Word_Freq(text);
	}	

	public static ArrayList get_Text()
    {
        ArrayList<String> txt = new ArrayList<>();
        try 
        {
        	File file = null;
            JFileChooser chooser = new JFileChooser(".");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files Only", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null); 
            if(returnVal == JFileChooser.APPROVE_OPTION) file = chooser.getSelectedFile();
            try (BufferedReader in = new BufferedReader(new FileReader(file))) 
            {
                String line = in.readLine();
                while(line != null)
                {
                  txt.add(line);
                  line = in.readLine();
                }
                in.close();
            }          
        }
        catch(HeadlessException | IOException e)
        {
            System.out.println("I/O Error");
            System.exit(1);
        }
        return txt;
    }

    public static void calc_Word_Freq(ArrayList text)
    {
        ArrayList<String> word_Chart = new ArrayList<>();
        ArrayList<Integer> word_Freq = new ArrayList<>();

        try
        {
        	for(int i = 0; i < text.size(); i++)
        	{
        		String line = (String) text.get(i);
        		StringTokenizer st = new StringTokenizer(line);
        		while(st.hasMoreTokens())
        		{
                    String word = st.nextToken();
                    if(word.matches("\\d+")) continue;
                    word = word.toLowerCase();
                    word = word.replaceAll("\\W", "");

                    int pos =  binarySearch(word_Chart, word);
                    if(pos < 0) 
                    {
                        word_Chart.add(-pos - 1, word);
                        word_Freq.add(-pos - 1, 1);
                    }
                    else
                    {
                        int temp = (int) word_Freq.get(pos) + 1;
                        word_Freq.set(pos, temp);
                    }
        		}
        	}
            System.out.println("The text provided contains " + word_Chart.size() + " unique words.");
            print_Results(word_Chart,word_Freq);
        }
        catch(Exception e)
        {
            System.out.println("Error Calculating Frequency Of Words");
        }
    }

    public static void print_Results(ArrayList word_Chart, ArrayList word_Freq)
    {
        try
        {
            FileWriter write = new FileWriter("results.txt");
            PrintWriter out = new PrintWriter(write);
            out.printf("%s \n\n %-15s %10s \n", "Here are the results of Glass.txt", "Word:", "Occurence:");
            for(int i = 0; i < word_Chart.size(); i++)
            {
                String word = word_Chart.get(i).toString();
                int freq = (int) word_Freq.get(i);
                out.printf("%-15s %6d\n", word, freq);
            }
            write.close();
        }
        catch(IOException e)
        {
            System.out.println("I/O Error");
        }
    }
    
    public static <T extends Comparable<T> > int binarySearch(ArrayList<T> table, T key)
    {
      int low = 0, high = table.size() - 1;
      while(high >= low)
      {
          int mid = (low + high)/2;
          T midElement = table.get(mid);
          int result = key.compareTo(midElement);
          if(result == 0)
             return mid;
          if(result < 0)
               high = mid - 1;
          else low  = mid + 1;
      }
      return -low - 1;
    }
}
