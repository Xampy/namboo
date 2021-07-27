package com.xampy.namboo.tools;

public class StringTools {

    /**
     * Set spaces in the value writing in order
     * to match the local money writing
     * Ex; 1000 => 1 000
     * @param value the integer value
     * @return a formatted string
     */
    public static String matchLocalMoneyWriting(int value){
        String s = String.valueOf(value);
        StringBuilder result = new StringBuilder();

        //Set spaces
        short count = 0;

        int i = s.length() - 1;

        while ( i > -1 ) {  //We need To reach zero

            result.append(s.charAt(i));
            count += 1;

            if ( count == 3){
                count = 0;
                result.append(" "); //Delimiter
            }


            i = i - 1;
        }


        return result.reverse().toString().trim();
    }
}
