public class HistWord
{

	int count;
	String word;

	HistWord(int count, String word){		// HistWord Constructor
		this.count = count;
		this.word = word;

	}

	public String getWord(){				// Gets the word
		return word;
	}

	public int getCount(){					// Gets the word's count
		return count;
	}
	
}