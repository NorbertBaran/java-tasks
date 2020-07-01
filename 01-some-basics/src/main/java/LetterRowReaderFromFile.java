import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class LetterRowReaderFromFile implements LetterRowReader {
    private static int ASCII_SHIFT=64;
    private static int PATTERN_HEIGHT=8;
    private static List<String> alphabet;

    public LetterRowReaderFromFile() throws IOException {
        InputStream ALPHABET_FILE_NAME = LetterRowReaderFromFile.class.getResourceAsStream("banner/letters.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(ALPHABET_FILE_NAME));
        alphabet=br.lines().collect(Collectors.toList());
    }

    @Override
    public String getLetterRow(char letter, int rowNumber) throws IOException {
        if(letter==' ')
            return "  ";
        int letterNumber=(int)(Character.toUpperCase(letter))-ASCII_SHIFT;
        int rowNumberBefore=(letterNumber-1)*PATTERN_HEIGHT;
        String letterRow=alphabet.get(rowNumberBefore+rowNumber);
        return letterRow;
    }
}
