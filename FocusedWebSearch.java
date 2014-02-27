
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.List;
import java.util.Stack;



public class FocusedWebSearch {
	static private FileReader     fileReader = null;
	static private BufferedReader buffReader = null;
	static private List<String>   links      = null;


	public static List<String> getLinksFromFile(String filename) {
		System.out.println("Opening file: " + filename);
		Stack<String> fileLinks = new Stack<String>();

		try {
			fileReader = new FileReader(filename);
		} catch(FileNotFoundException fnfe) {
			System.out.println("Error finding file - " + fnfe.getMessage());
		}

		buffReader = new BufferedReader(fileReader);

		String wholeNextLine;

		try {
	 		while( (wholeNextLine = buffReader.readLine()) != null) {
				String nextLine = wholeNextLine.trim();
				if(nextLine.length() > 0) {
					fileLinks.add(nextLine);
				}
			}

			buffReader.close();
			fileReader.close();
		} catch(IOException ioe) {
			System.out.println("Error Message: " + ioe.getMessage());
			ioe.printStackTrace();
		}

		return fileLinks;
	}


	private static String checkArguments(String [] args) {
		System.out.println("Verifying command line arguments ...");

		if(args.length != 1) {
			System.out.println("ERROR: missing search string");
			System.exit(1);
		}

		System.out.println("OK");

		return args[0];
	}


	private static boolean isSearchTermInHtml(BufferedReader reader, String searchTerm) throws IOException {
		boolean isFound  = false;
		String  nextLine = null;

		int i = 0;
		while( (nextLine = reader.readLine()) != null) {
			i++;
			int searchIndex = nextLine.indexOf(searchTerm);
			if(searchIndex != -1) {
				isFound = true;
				//  System.out.println("  line found: " + nextLine);
				break;
			}
		}

		return isFound;
	}


	private static void checkLinksForSearchTerm(List<String> links, String searchTerm) {
		InputStream   response      = null;
		URL           url           = null;
		URLConnection urlConnection = null;


		for(int i=0; i < links.size(); i++) {
			String link = links.get(i);
			System.out.println("  Searching link: " + link);
			try {
				url = new URL(link);
			} catch(MalformedURLException e) {
				System.out.println("Error creating url: " + e.getMessage());
			}

			try {
				urlConnection = url.openConnection();
				response = urlConnection.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(response, "utf-8");
				BufferedReader reader = new BufferedReader(inputStreamReader);
				if(isSearchTermInHtml(reader, searchTerm)) {
					System.out.println("    Found!");
				}
				reader.close();
				inputStreamReader.close();
			} catch(IOException e) {
				System.out.println("Error opening url connection: " + e.getMessage());
			}

			try {
				Thread.sleep(300);
			} catch(InterruptedException e) {
				System.out.println("Error trying to make thread sleep: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}


	public static void main(String [] args) {
		System.out.println("Starting ...");
		String searchTerm = checkArguments(args);
		links = getLinksFromFile("links.txt");
		// System.out.println("links = " + links);
		checkLinksForSearchTerm(links, searchTerm);
		System.out.println("Done.");
	}

}



