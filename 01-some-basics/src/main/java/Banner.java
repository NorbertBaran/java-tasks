import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Banner {
    private static int LINE_HEIGHT = 7;
    private LetterRowReader letterRowReader;

    public Banner(){
        try {
            this.letterRowReader=new LetterRowReaderFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] toBanner(String input){
        String[] banner=new String[0];
        if(input!=null && input.length()>0){
            banner=new String[7];
            for (int rowNumber = 0; rowNumber < LINE_HEIGHT; rowNumber++){
                var bannerRow=new StringBuilder();
                for(var letter : input.toCharArray()){
                    try {
                        bannerRow.append(letterRowReader.getLetterRow(letter, rowNumber));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bannerRow.append(" ");
                }
                banner[rowNumber]=("A"+bannerRow.toString()).trim().substring(1);
            }
        }
        return banner;
    }

    public static void main(String[] args){
        if (args.length > 0) {
            String str = Arrays.stream(args).collect(Collectors.joining(" "));
            var toDisplay = new Banner().toBanner(str);
            for (var line : toDisplay) {
                System.out.println(line);
            }
        }
    }
}
