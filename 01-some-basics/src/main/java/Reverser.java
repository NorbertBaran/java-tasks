public class Reverser {
    public String reverse(String input) {
        if(input!=null)
            return new StringBuilder(input.trim()).reverse().toString();
        return null;
    }
    public String reverseWords(String input) {
        if(input!=null){
            String[] outputArray=input.split("\\s+");
            StringBuilder output=new StringBuilder();
            for(String word: outputArray)
                output.insert(0, " ").insert(0, word);
            return output.toString().trim();
        }
        return null;
    }
}
