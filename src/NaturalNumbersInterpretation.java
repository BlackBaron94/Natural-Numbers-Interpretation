import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalNumbersInterpretation {

    // Helper method that removes spaces from an ArrayList
    public static ArrayList<Character> removeSpaces(ArrayList<Character> listWithSpaces){
        ArrayList<Character> returnList = new ArrayList<>();
        // Loops through parameter list
        for (Character c : listWithSpaces) {
            // If current character isn't a space, it is added to returnList
            if (!(c.equals(' '))) {
                returnList.add(c);
            }
        }
        return returnList;
    }

    // Compiles ArrayList of characters to string for output
    public static String compileString(ArrayList<Character> list){
        // Loops through it to build a StringBuilder
        StringBuilder result = new StringBuilder();
        for (Character c : list){
            result.append(c);
        }
        // Converts to string and returns the result
        return result.toString().replaceAll("\\s+","");
    }

    // Helper function for DRY principle, used for both beginning of potential number and after initial 0030
    public static boolean checkTwoOrSixNine(ArrayList<Character> initialArray){
        if (initialArray.get(0).equals('2')){
            return true;
        } else return (initialArray.get(0).equals('6')) && initialArray.get(1).equals('9');
    }

    // Checks if first 4 digits are 0030
    public static boolean check0030(ArrayList<Character> initialArray) {
        return (initialArray.get(0).equals('0')) &&
                (initialArray.get(1).equals('0')) &&
                (initialArray.get(2).equals('3')) &&
                (initialArray.get(3).equals('0'));
    }


    // Checks if a number adheres to length and first numbers rules
    public static boolean completelyCheckValidGreekNumber(ArrayList<Character> initialArray){
        ArrayList<Character> tempArray = new ArrayList<>(initialArray);
        int len = tempArray.size();
        // If length is wrong, does not proceed to number checks
        if ((len != 14) && (len != 10)){
            return false;
        }

        // Case of 10 length number starting with 2 or 69
        if (len == 10) {
            return (checkTwoOrSixNine(tempArray));
        // Case of 14 length number starting with 00302 or 003069
        } else {
            // Checks if number starts with 0030
            if (check0030(tempArray)) {
                // Removes 0030 from beginning of ArrayList, so that it works for helper function
                tempArray.subList(0, 4).clear();
                // Checks if 0030 is followed by 2 or 69 and returns result
                return (checkTwoOrSixNine(tempArray));
            // Case of 14 length number not starting with 0030
            } else {
                return false;
            }
        }
    }

    // Returns a List of Lists with possible interpretations for two-digit number.
    public static ArrayList<ArrayList<Character>> twoDigitInterpretations(ArrayList<Integer> twoDigitsIndex, ArrayList<Character> initialList) {

        ArrayList<Character> tempList = new ArrayList<>(initialList);
        ArrayList<Character> branching = new ArrayList<>(tempList);
        ArrayList<ArrayList<Character>> returnList = new ArrayList<>();
        // If second digit is 0, e.g. 20, 30 etc.
        if (tempList.get(twoDigitsIndex.get(1)).equals('0')){
            // Checks that this is not the final digit of the sequence
            if (tempList.size() > twoDigitsIndex.get(1) + 1) {
                // Checks if the number sequence ends with next digit or if the next digit is a single digit number
                if ((tempList.size() == twoDigitsIndex.get(1) + 3) ||
                        (tempList.size() >= twoDigitsIndex.get(1) + 3 &&
                        tempList.get(twoDigitsIndex.get(1) + 3).equals(' ')
                        )) {
                    /* Uncomment following code if speech detection detects in English, in which case, for example
                     * "ten, three" 10 3 can't be a misinterpreted "thirteen" 13,
                     * while in Greek "deka, tria" 10 3 can be a misinterpreted "dekatria" 13
                     * */
                    // If number is 10 x skips branching into 1 0 x and 1 x.
                 /*if (tempList.get(twoDigitsIndex.get(0)).equals('1')){
                    // 10 x becomes 1 0 x
                    tempList.add(twoDigitsIndex.get(1), ' ');
                    returnList.add(tempList);
                    return returnList;
                 }*/
                    // Case of 60 0 where 0 should not be absorbed
                    if (!(tempList.get(twoDigitsIndex.get(1) + 2).equals('0'))){
                        Character mergedCharacter = branching.get(twoDigitsIndex.get(1) + 2);
                        // Merges next single digit, "20 3" becomes "23 3"
                        branching.set(twoDigitsIndex.get(1), mergedCharacter);
                        // "23 3" becomes "2 3 3"
                        branching.add(twoDigitsIndex.get(1), ' ');
                        // "2 3 3" becomes "2 33"
                        branching.remove(twoDigitsIndex.get(1) + 2);
                        // "2 33" becomes "2 3"
                        branching.remove(twoDigitsIndex.get(1) + 2);
                        returnList.add(branching);
                    }

                }
            }

        // Case where second digit is not 0
        } else {

            /* Uncomment following code if speech detection detects English, in which case, for example "13" can't
             * be a misinterpreted "10, 3", while in Greek "dekatria" "13" can be a misinterpreted "deka, tria" "10, 3"
             */
            // If number is "1x" skips branching into "1 x" and "1 0 x", keeps only "1 x"
            /*if (tempList.get(twoDigitsIndex.get(0)).equals('1')){
                // Finalizes default interpretation
                tempList.add(twoDigitsIndex.get(1), ' ');
                returnList.add(tempList);
                return returnList;
            }*/

            // Exception for 11 and 12 as they are both audibly distinguishable from 10 1 and 10 2 in both Greek
            // and English
            // Following if-block runs only if numbers are not 11 or 12
            if (!((
                    tempList.get(twoDigitsIndex.get(0)).equals('1')) &&
                    (
                            (tempList.get(twoDigitsIndex.get(1)).equals('1')) || (tempList.get(twoDigitsIndex.get(1)).equals('2')))
                    )
                ) {

                // Creates branching interpretation by making 23 into 203
                branching.add(twoDigitsIndex.get(1), '0');
                // "203" is made into "2 03"
                branching.add(twoDigitsIndex.get(1), ' ');
                // "2 03" is made into "2 0 3"
                branching.add(twoDigitsIndex.get(1) + 2, ' ');
                returnList.add(branching);
            }
        }
        // Finalizes default interpretation by making e.g. 23 into 2 3 which is necessary for all cases
        tempList.add(twoDigitsIndex.get(1), ' ');
        returnList.addFirst(tempList);
        return returnList;
    }

    // Method for generating a List of Lists of Interpretations for three-digit numbers.
    // Only works the first digit, lets twoDigitInterpretations handle the other two
    // E.g. "235" is made into "2 35" and "2 0 0 35", with 35 handled by twoDigitInterpretation method afterward
    public static ArrayList<ArrayList<Character>> threeDigitInterpretations(ArrayList<Integer> threeDigitsIndex, ArrayList<Character> initialList) {
        ArrayList<Character> tempList = new ArrayList<>(initialList);
        ArrayList<Character> branching = new ArrayList<>(tempList);
        ArrayList<ArrayList<Character>> returnList = new ArrayList<>();
        // Case where last digits are both 0, e.g. 400
        if (
                initialList.get(threeDigitsIndex.get(1)).equals('0') &&
                initialList.get(threeDigitsIndex.get(2)).equals('0')
                ){
            /* Comment out this code if speech recognition is in English. Numbers in the 101-199 range are (almost)
            immune to misinterpretation in Greek.
            Example:
            100 3 "ekato, tria" can not (easily) be
            misinterpreted to 103 "ekaton tria" in Greek, but in English that is not the case for one hundred three 103
            and one hundred, three 100 3. If-block is differentiated from the next one for easy comment-in/-out
             */
            // Skips branching "100 12" to "1 0 0 12" and "1 1 2", creating only "1 0 0 12"
            if (initialList.get(threeDigitsIndex.get(0)).equals('1')){
                // "100" becomes "10 0"
                tempList.add(threeDigitsIndex.get(2),' ');
                // "10 0" becomes "1 0 0"
                tempList.add(threeDigitsIndex.get(1),' ');
                returnList.add(tempList);
                return returnList;
            }
            if (initialList.size() > threeDigitsIndex.get(2) + 1){// Case of having 400 x and then end of input or space
                if ((threeDigitsIndex.get(2) + 3 >= initialList.size()) || initialList.get(threeDigitsIndex.get(2) + 3).equals(' ')) {
                    // Only if 400 x where x!=0
                    if (!(initialList.get(threeDigitsIndex.get(2) + 2).equals('0'))){
                        // e.g. "400 x"
                        Character mergedChar = branching.get(threeDigitsIndex.get(2) + 2);
                        // "400 x" becomes "40x x"
                        branching.set(threeDigitsIndex.get(2), mergedChar);
                        // "40x x" becomes "40xx"
                        branching.remove(threeDigitsIndex.get(2) + 1);
                        // "40xx" becomes "40x"
                        branching.remove(threeDigitsIndex.get(2) + 1);
                        // "40x" becomes "40 x"
                        branching.add(threeDigitsIndex.get(2), ' ');
                        // "40 x" becomes "4 0 x"
                        branching.add(threeDigitsIndex.get(1), ' ');
                        returnList.add(branching);
                    }
                } else if ((threeDigitsIndex.get(2) + 4 >= initialList.size()) || initialList.get(threeDigitsIndex.get(2) + 4).equals(' ')) {
                    // Case of having "400 xy" and then end of input or space

                    // Grabs next two digit characters
                    Character mergedChar1 = branching.get(threeDigitsIndex.get(2) + 2);
                    Character mergedChar2 = branching.get(threeDigitsIndex.get(2) + 3);
                    // "400 xy" becomes "4x0 xy"
                    branching.set(threeDigitsIndex.get(1), mergedChar1);
                    // "400 xy" becomes "4xy xy"
                    branching.set(threeDigitsIndex.get(2), mergedChar2);
                    // "4xy xy" becomes "4xyxy"
                    branching.remove(threeDigitsIndex.get(2) + 1);
                    // "4xyxy" becomes "4xyy"
                    branching.remove(threeDigitsIndex.get(2) + 1);
                    // "4xyy" becomes "4xy"
                    branching.remove(threeDigitsIndex.get(2) + 1);
                    // "4xy" becomes "4 xy", now twoDigitInterpretations() will handle the rest
                    branching.add(threeDigitsIndex.get(1), ' ');
                    returnList.add(branching);
                }
            }
            // Needed for all cases, makes "400" into "40 0" and then into "4 0 0"
            tempList.add(threeDigitsIndex.get(2), ' ');
            tempList.add(threeDigitsIndex.get(1), ' ');
            returnList.addFirst(tempList);
            return returnList;
        } else {
            // Case where three-digit number is not of x00 form.

            /* Comment out this code if speech recognition is in English. Numbers in the 101-199 range are (almost)
            immune to misinterpretation in Greek.
            Example:
            100 3 "ekato, tria" can not (easily) be
            misinterpreted to 103 "ekaton tria" in Greek, but in English that is not the case for one hundred three 103
            and one hundred, three 100 3
             */
            if (initialList.get(threeDigitsIndex.get(0)).equals('1')){
                // "1xy" becomes "1 xy"
                tempList.add(threeDigitsIndex.get(1),' ');
                // If form is "10x", then the only possible form is "1 0 x", not "1 0x" (because 0x can not possibly
                // be recognized by speech recognition as a two-digit number
                if (initialList.get(threeDigitsIndex.get(1)).equals('0')){
                    // "1 0y" becomes "1 0 y"
                    tempList.add(threeDigitsIndex.get(2)+1, ' ');
                }
                returnList.add(tempList);
                return returnList;
            }
            // Case where middle digit is 0, hence should not be broken down
            // e.g. "401" not to "4 01", but to "400 1" and "4 0 1"
            if (initialList.get(threeDigitsIndex.get(1)).equals('0')){
                // "401" becomes "40 1"
                tempList.add(threeDigitsIndex.get(2), ' ');
                // "40 1" becomes "4 0 1"
                tempList.add(threeDigitsIndex.get(1), ' ');
                returnList.add(tempList);
                // "401" becomes "40 1"
                branching.add(threeDigitsIndex.get(2), ' ');
                // "40 1" becomes "400 1"
                branching.add(threeDigitsIndex.get(2), '0');
                // "400 1" becomes "40 0 1"
                branching.add(threeDigitsIndex.get(2), ' ');
                // "40 0 1" becomes "4 0 0 1"
                branching.add(threeDigitsIndex.get(1), ' ');
                returnList.add(branching);
                return returnList;
            }
            // Note: Cases of e.g. "440" will be also be handled by twoDigitInterpretations() in case it is followed
            // by a single digit, e.g. "440 6" can be "4 4 6" or "4 4 0 6"

            // "416" becomes "4 16", rest will be handled by twoDigitInterpretations()
            tempList.add(threeDigitsIndex.get(1), ' ');
            returnList.add(tempList);
            // "416" becomes "4 16"
            branching.add(threeDigitsIndex.get(1), ' ');
            // "4 16" becomes "40 16"
            branching.add(threeDigitsIndex.get(1), '0');
            // "40 16" becomes "4 0 16"
            branching.add(threeDigitsIndex.get(1), ' ');
            // "4 0 16" becomes "40 0 16"
            branching.add(threeDigitsIndex.get(1), '0');
            // "40 0 16" becomes "4 0 0 16"
            branching.add(threeDigitsIndex.get(1), ' ');
            returnList.add(branching);
            return returnList;
        }

    }

    // If returned array[0] == array[1], there are no multiDigits, array returned is of following form:
    // Array[0] == index of List in List of lists.
    // Array[1] == index of first multi digit
    // Array[2] == index of second multi digit
    // Array[3] == index of third multi digit, if it exists
    // If array[1] == array[2], no multi digits were found
    public static ArrayList<Integer> findMultiDigits(ArrayList<ArrayList<Character>> initialList){
        ArrayList<Integer> currentDigitsIndexes = new ArrayList<>();
        for (ArrayList<Character> individualList : initialList){
            // Initialization for each list
            currentDigitsIndexes = new ArrayList<>();
            for (int i = 0; i <= individualList.size(); i++) {
                // Uses end of list/space to discern end of digit sequence
                if ((i == individualList.size()) || (individualList.get(i).equals(' '))){
                    // If digit sequence is not a single digit
                    if (currentDigitsIndexes.size() >= 2){
                        if (currentDigitsIndexes.size() == 2) {
                            currentDigitsIndexes.addFirst(initialList.indexOf(individualList));
                            return currentDigitsIndexes;
                        } else if (currentDigitsIndexes.size() == 3) {
                            currentDigitsIndexes.addFirst(initialList.indexOf(individualList));
                            return currentDigitsIndexes;
                        } else {
                            // This exists for debugging in case of error in the speech recognition software's output.
                            System.out.println("---***---Parse error. 3++ digit sequence detected.---***---");
                            currentDigitsIndexes = new ArrayList<>();
                            currentDigitsIndexes.add(0);
                            currentDigitsIndexes.add(0);
                            currentDigitsIndexes.add(0);
                            return currentDigitsIndexes;
                        }
                    } else {
                        // Resetting the currentDigitsIndexes in case of size == 1
                        currentDigitsIndexes = new ArrayList<>();
                    }
                } else {
                    // If no space/end of sequence has been detected, index is added to the sequence indexes
                    currentDigitsIndexes.add(i);
                }
            }

        }
        // This code is reached only if no multi digits where found
        currentDigitsIndexes = new ArrayList<>();
        currentDigitsIndexes.add(0);
        currentDigitsIndexes.add(0);
        currentDigitsIndexes.add(0);
        return currentDigitsIndexes;
    }

    // Checks input for correct format
    public static boolean checkInput(String inputGiven){
        // RegEx checking for characters other than numeric and whitespaces
        Pattern nonPermittedSymbolsPattern = Pattern.compile("[^0-9 ]");
        Matcher nonPermittedSymbolsMatcher = nonPermittedSymbolsPattern.matcher(inputGiven);
        // RegEx checking for string not containing numbers
        Pattern numberPattern = Pattern.compile("[0-9]");
        Matcher numberMatcher = numberPattern.matcher(inputGiven);
        // If non-permitted character is found, app loops back to input
        if (nonPermittedSymbolsMatcher.find()){
            System.out.println("Input contains characters other than numeric and spaces.\nPermitted input is 0-9 and whitespaces.");
            return false;
        }
        // If string does not contain at least one number, app loops back to input
        if (!(numberMatcher.find())) {
            System.out.println("Input does not contain any numbers.\nPermitted input is 0-9 separated with whitespaces.");
            return false;
        }
        return true;
    }

    /* The following method makes biased interpretations of first digits to skip interpretations of e.g. "69"
        branching into "60 9", resulting in invalid numbers only. This directly contradicts the example in the assignment
        where input "0 0 30 69" is interpreted as both "0 0 3 0 6 9", but also "0 0 3 0 6 0 9" resulting in Invalid numbers only.
        This method ignores the chance of "0 0 30 69" being interpreted as "0 0 3 0 6 0 9" to limit useless invalid numbers.
    */
    // Assumes leading prefixes to be of valid form only.
    public static ArrayList<Character> biasedInterpretations(String inputString){
        ArrayList<Character> returnArray = new ArrayList<>();
        for (int i = 0; i < inputString.length(); i++){
            returnArray.add(inputString.charAt(i));
        }
        if (inputString.startsWith("69")) {
            // "69" is made into "6 9"
            returnArray.add(1, ' ');
        } else if (inputString.startsWith("60 9 ")) {
            // "60 9 " is made into "6 9 ",
            // Space after 9 is required as sixty, nine is a possible mistake for sixty-nine,
            // unlike sixty ninety-something which is not
            returnArray.remove(1);
        // Must NOT have empty character next to 9, either end of sequence or number. After that, must be space/end
        // E.g. 691 can be misinterpreted to 600 91, but six hundred nine can not ("600 9 x"), neither can 600 921
        } else if ((inputString.startsWith("600 9") &&
                (inputString.length() >= 6 && inputString.charAt(5) != ' ') &&
                (inputString.length() == 6 || inputString.charAt(6) == ' ')
                )) {
            // "600 97" becomes "60 97"
            returnArray.remove(1);
            // "60 97" becomes "6 97"
            returnArray.remove(1);
        } else if (inputString.startsWith("0 0 3")) {
            // Space required after 2, e.g. 321 is not a possible misinterpretation for 30 21
            if (inputString.startsWith("0 0 32 ")) {
                // "0 0 32 " becomes "0 0 302 "
                returnArray.add(5, '0');
                // "0 0 302 " becomes "0 0 30 2 "
                returnArray.add(6, ' ');
                // "0 0 30 2 " becomes "0 0 3 0 2 "
                returnArray.add(5, ' ');
            // Space required after 6, 369 is not a possible misinterpretation for 30 69
            } else if (inputString.startsWith("0 0 36 ")) {
                // "0 0 36 " becomes "0 0 306 "
                returnArray.add(5, '0');
                // "0 0 306 " becomes "0 0 3 06 "
                returnArray.add(5, ' ');
                // "0 0 3 06" becomes "0 0 3 0 6"
                returnArray.add(7, ' ');
            } else if (inputString.startsWith("0 0 30")) {
                if (inputString.startsWith("0 0 306") ||
                    inputString.startsWith("0 0 302")) {
                    // "0 0 30x" becomes "0 0 30 x"
                    returnArray.add(6, ' ');
                    // "0 0 30 x" becomes "0 0 3 0 x"
                    returnArray.add(5, ' ');
                } else if (inputString.startsWith("0 0 30 6")) {
                    if (inputString.startsWith("0 0 30 69")) {
                        // "0 0 30 69" becomes "0 0 30 6 9"
                        returnArray.add(8, ' ');
                        // "0 0 30 6 9" becomes "0 0 3 0 6 9"
                        returnArray.add(5, ' ');
                    // Space after 9 required for sixty-nine/sixty, nine misinterpretation as mentioned above
                    } else if (inputString.startsWith("0 0 30 60 9 ")) {
                        // "0 0 30 60 9 " becomes "0 0 30 6 9"
                        returnArray.remove(8);
                        // "0 0 30 6 9" becomes "0 0 3 0 6 9"
                        returnArray.add(5, ' ');
                    // 9 should have number next to it, and next to that a space or end of string
                    } else if (inputString.startsWith("0 0 30 600 9") &&
                            (inputString.length() >= 13 && inputString.charAt(12) != ' ') &&
                            (inputString.length() == 14 || inputString.charAt(13) == ' ')){
                        // "0 0 30 600 9" becomes "0 0 30 60 9"
                        returnArray.remove(8);
                        // "0 0 30 60 9" becomes "0 0 30 6 9"
                        returnArray.remove(8);
                        // "0 0 30 6 9" becomes "0 0 3 0 6 9"
                        returnArray.add(5,' ');
                    }  else if (inputString.startsWith("0 0 30 6 9")){
                        // "0 0 30 6 9" becomes "0 0 3 0 6 9"
                        returnArray.add(5, ' ');
                    }
                } else if (inputString.startsWith("0 0 30 2 ")) {
                    // "0 0 30 2" becomes "0 0 3 0 2"
                    returnArray.add(5, ' ');
                // Space necessary for correct fix
                } else if (inputString.startsWith(("0 0 300 2 "))||
                            inputString.startsWith("0 0 300 6 ")){
                    returnArray.remove(5);
                    returnArray.add(5, ' ');
                }
            } else if (inputString.startsWith("0 0 3 0")){
                if (inputString.startsWith("0 0 3 0 69")){
                    // "0 0 3 0 69" becomes "0 0 3 0 6 9"
                    returnArray.add(9, ' ');
                // Space after 9 required
                } else if (inputString.startsWith("0 0 3 0 60 9 ")){
                    // "0 0 3 0 60 9" becomes "0 0 3 0 6 9"
                    returnArray.remove(9);
                // 9 needs to have number after it, but after that either space or end of string
                } else if (inputString.startsWith("0 0 3 0 600 9") &&
                        (inputString.length() >= 14 && inputString.charAt(13) != ' ') &&
                        (inputString.length() == 15 || inputString.charAt(14) == ' ')){
                    // "0 0 3 0 600 9" becomes "0 0 3 0 60 9"
                    returnArray.remove(9);
                    // "0 0 3 0 60 9" becomes "0 0 3 0 6 9"
                    returnArray.remove(9);
                }
            }
        }
        return returnArray;
    }

    // Method that gets interpretations of a given number sequence
    public static ArrayList<ArrayList<Character>> getInterpretations(String inputString){
        inputString = inputString.strip();
        // Creates an ArrayList of input
        ArrayList<Character> inputArray = new ArrayList<>();
        for (int i = 0; i < inputString.length(); i++) {
            inputArray.add(inputString.charAt(i));
        }
        // Interpretations array to be returned
        ArrayList<ArrayList<Character>> returnArray = new ArrayList<>();
        // Temp list of interpretations of specific list in returnArray
        ArrayList<ArrayList<Character>> tempListOfLists = new ArrayList<>();

        // Comment out following call to skip making biased interpretations for leading potential "6 9"
        inputArray = biasedInterpretations(inputString);

        // Initially, inputArray is the only array to be interpreted
        returnArray.add(inputArray);
        while (true){
            // List for index of list that has multi-digit sequences, and indexes of the sequence in said list
            ArrayList<Integer> currentMultiDigitIndexes = findMultiDigits(returnArray);
            // findMultiDigits returns [1]==[2] when there are no multi-digit sequences in any of the lists
            // Signals end of branching interpretations break-down
            if (currentMultiDigitIndexes.get(1).equals(currentMultiDigitIndexes.get(2))){
                break;
            }

            // Keeps index of list where multi-digits where found
            int listIndex = currentMultiDigitIndexes.get(0);
            // Index of list is removed
            currentMultiDigitIndexes.removeFirst();
            // Calls appropriate method to get interpretations
            if (currentMultiDigitIndexes.size() == 2){
                // Gets interpretations of specific multi-digit sequence in specific list in returnArray
                tempListOfLists = twoDigitInterpretations(currentMultiDigitIndexes, returnArray.get(listIndex));
            }
            if (currentMultiDigitIndexes.size() == 3){
                // Gets interpretations of specific multi-digit sequence in specific list in returnArray
                tempListOfLists = threeDigitInterpretations(currentMultiDigitIndexes, returnArray.get(listIndex));
            }
            // List from which interpretations were derived is removed
            returnArray.remove(listIndex);
            // Interpretations are added
            returnArray.addAll(tempListOfLists);
        }
        return returnArray;
    }

    // Method that returns an Array of output for printing
    public static ArrayList<String> prepareOutput(String inputString){
        // List of Lists for various interpretations of number
        ArrayList<ArrayList<Character>> interpretations = getInterpretations(inputString);
        // Prepares output for each interpretation and the appropriate result in terms of validness
        String outputNumber;
        StringBuilder output = new StringBuilder();
        ArrayList<String> returnArray = new ArrayList<>();
        for (int i = 0; i < interpretations.size(); i++){
            output = new StringBuilder();
            outputNumber = compileString(interpretations.get(i));
            output.append("Interpretation #").append(i + 1).append(": ").append(outputNumber).append(" [phone number: ");
            if (completelyCheckValidGreekNumber(removeSpaces(interpretations.get(i)))){
                output.append("VALID]");
            } else {
                output.append("INVALID]");
            }
            returnArray.add(output.toString());
        }
        return returnArray;
    }

    public static void main(String[] args) {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 16);

        JFrame frame = new JFrame("Natural Numbers Interpreter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel("Enter input digits here:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(defaultFont);

        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(400, 30));
        textField.setFont(defaultFont);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton("Submit");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setFont(defaultFont);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(defaultFont);
        outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5,10,5,10))
        );

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(e -> {
            String input = textField.getText().trim();
            if (!NaturalNumbersInterpretation.checkInput(input)) {
                outputArea.setText("Invalid input. Only digits and spaces are allowed.");
                return;
            }

            ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
            StringBuilder resultBuilder = new StringBuilder();
            for (String line : output) {
                resultBuilder.append(line).append("\n");
            }
            outputArea.setText(resultBuilder.toString());
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}