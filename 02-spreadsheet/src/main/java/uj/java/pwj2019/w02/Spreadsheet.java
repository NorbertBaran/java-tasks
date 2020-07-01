package uj.java.pwj2019.w02;

public class Spreadsheet {
    private String[][] input;

    private String setValue(int row, int ceil){
        if(input[row][ceil].charAt(0)=='=')
            input[row][ceil]=getFormulaValue(row, ceil);
        else if(input[row][ceil].charAt(0)=='$')
            input[row][ceil]=getReferenceValue(row, ceil);
        return input[row][ceil];
    }

    private String getReferenceValue(int row, int ceil){
        String reference=input[row][ceil];
        return setValue(Integer.parseInt(reference.substring(2))-1, reference.charAt(1)-65);
    }

    private String getFormulaArgValue(String expr){
        if(expr.charAt(0)=='$')
            expr=setValue(Integer.parseInt(expr.substring(2))-1, expr.charAt(1)-65);
        return expr;
    }

    private String getFormulaValue(int row, int ceil){
        String formula=input[row][ceil];

        String firstParam=formula.substring(5, formula.indexOf(','));
        String secondParam=formula.substring(formula.indexOf(',')+1, formula.indexOf(')'));

        int firstValue= Integer.parseInt(getFormulaArgValue(firstParam));
        int secondValue= Integer.parseInt(getFormulaArgValue(secondParam));

        switch (formula.substring(1,4)){
            case "ADD":
                return String.valueOf(firstValue+secondValue);
            case "SUB":
                return String.valueOf(firstValue-secondValue);
            case "MUL":
                return String.valueOf(firstValue*secondValue);
            case "DIV":
                return String.valueOf(firstValue/secondValue);
            case "MOD":
                return String.valueOf(firstValue%secondValue);
        }

        return null;
    }

    public String[][] calculate(String[][] input) {
        this.input=input.clone();
        for (int row = 0; row < input.length; row++)
            for (int ceil = 0; ceil < input[row].length; ceil++)
                setValue(row, ceil);
        return this.input;
    }
}
