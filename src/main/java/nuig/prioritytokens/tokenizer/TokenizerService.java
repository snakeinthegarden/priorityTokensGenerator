package nuig.prioritytokens.tokenizer;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TokenizerService
{

	private static final String tokenModelSource = "input/binFiles/en-token.bin";


	
	
	/**
	 * Tokenize a single email (passed as a single string).
	 * Also, filter out the tokens by comparing to stopWordsList.
	 * The tokenized files will be written to a file and returned in an ArrayList.
	 * 
	 * @param emailAsSentence is an email where all words are converted to a single String object
	 * @param stopWordsList is a list of stop words generated by service classes.
	 * @param outputSingleEmail is the file path the tokenized file will be sent to
	 * @return ArrayList<String> which is an arrayList containing the tokens after stop words removed.
	 */
	public ArrayList<String> tokenizeEmail(String emailAsSentence, ArrayList<String> stopWordsList, String outputSingleEmail)
	{
		InputStream inputStreamObject = null;

		//create a list of words to store the remaining part of the sentence with the stop words removed
		ArrayList<String> wordsArrayListAfterFilteringStopWords = new ArrayList<String>();

		try 
		{
			//Load the Tokenizer model 
			inputStreamObject = new FileInputStream(tokenModelSource); 
			//generate an object of the tokenizer model and pass it an inputSteam
			TokenizerModel tokenizerModelObject = new TokenizerModel(inputStreamObject); 
			//Instantiating the TokenizerME class from the Tokenizer jars
			TokenizerME tokenizer = new TokenizerME(tokenizerModelObject); 
			//Tokenize the supplied raw text String value into an array
			String tokensArray[] = tokenizer.tokenize(emailAsSentence);       
			
			//using a FOR loop, compare the sentence to the stop words list
			for(String wordElement : tokensArray)
			{ 
				//initialise a String called wordCompare and equate it to the value of the element in lower case
				//this removes the need to add double the # of words for upper/lower case situations in email.
				String wordCompare = wordElement.toLowerCase();
				//if the sentence has a word not on the stop words list, then add to the new array. (else do nothing).
				if(!stopWordsList.contains(wordCompare))
				{ 
					wordsArrayListAfterFilteringStopWords.add(wordElement);
				} 
			} 
			//Transfer the arrayList back to an array so it can be written to a file
			String[] arrayAfterFilteringStopWords = new String[wordsArrayListAfterFilteringStopWords.size()];
			//equate the contents of the array list to the array
			arrayAfterFilteringStopWords = wordsArrayListAfterFilteringStopWords.toArray(arrayAfterFilteringStopWords);
			//pass the new array object to the method to create a .txt file from the array of stop words.
			writeEmailTokensToFile(arrayAfterFilteringStopWords, outputSingleEmail);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (inputStreamObject != null) 
			{ 
				try 
				{ 
					inputStreamObject.close();
				} 
				catch (IOException e) 
				{ 	
					
				} 
			} 
		}
		return wordsArrayListAfterFilteringStopWords;
	}

	
	
	
	/**
	 * Write list of tokens to a file.
	 * This method is called inside tokenizeEmail method.
	 * 
	 * @param tokensArray
	 * @param outputFileName
	 */
	private static void writeEmailTokensToFile(String[] tokensArray, String outputFileName) 
	{
		try 
		{ 
			FileWriter fileWriterOutputFile = new FileWriter(outputFileName);
			BufferedWriter bufferedWriterOutputFile = new BufferedWriter(fileWriterOutputFile);
			//using a FOR loop, write the array to the .txt file line by line
			for (int i=0; i < tokensArray.length; i++)
			{ 
				bufferedWriterOutputFile.write(tokensArray[i] + "\r\n");
			}

			//output to console so can see that tokenizing operation has been completed
			System.out.println("The email has been written to a text file");
			bufferedWriterOutputFile.close();
			fileWriterOutputFile.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
	
	
}