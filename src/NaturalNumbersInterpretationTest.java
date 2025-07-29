import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class NaturalNumbersInterpretationTest {

    @Test
    public void assignmentExample1() {
        String input = "2 10 6 9 30 6 6 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("2106930664 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("210693664 [phone number: INVALID]")));
    }

    @Test
    public void assignmentExample2() {
        String input = "2 10 69 30 6 6 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("2106930664 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("210693664 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("21060930664 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("2106093664 [phone number: VALID]")));
    }

    @Test
    public void assignmentExample3() {
        String input = "0 0 30 69 700 24 1 3 50 2";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("0030697002413502 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("003069700241352 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("00306972413502 [phone number: VALID]")));
        // This will be false because of biased interpretations added
        assertFalse(output.stream().anyMatch(s -> s.contains("00306097241352 [phone number: INVALID]")));
    }


    @Test
    // Tests that twoDigitInterpretation correctly stops when it's the last sequence
    public void lastSequenceDoubleDigitZero() {
        String input = "69 7 36 82 5 50";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682550 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69730682550 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736802550 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697306802550 [phone number: INVALID]")));
    }

    @Test
    public void twoDigitInterpretationBoundaryCheck() {
        String input = "6 9 7 3 6 8 2 50 5";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682505 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697368255 [phone number: INVALID]")));
    }

    @Test
    public void doubleDoubleDigitSequence() {
        String input = "6 9 7 3 6 8 50 52";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973685052 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736850502 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("69736850052 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("697368500502 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("697368552 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("6973685502 [phone number: VALID]")));
    }

    @Test
    public void lastSequenceTripleDigitNoZero() {
        String input = "6 9 7 3 6 8 2 542";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682542 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697368250042 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682500402 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736825402 [phone number: INVALID]")));
    }

    @Test
    public void lastSequenceTripleDigitZero() {
        String input = "6 9 7 3 6 8 2 500";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682500 [phone number: VALID]")));
    }

    @Test
    public void lastTwoSequencesThreeDigitsTwoDigits() {
        String input = "6 9 7 3 6 400 25";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973640025 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736425 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736425 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697364205 [phone number: INVALID]")));
    }

    @Test
    public void sequentialTripleDigits() {
        String input = "6 9 7 300 400 5";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973004005 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697300405 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("69734005 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("697304005 [phone number: INVALID]")));
    }

    @Test
    public void doubleDigitDoesNotAbsorbZero() {
        String input = "6 90 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6900621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("690621989 [phone number: INVALID]")));

    }

    @Test
    public void tripleDigitDoesNotAbsorbZero() {
        String input = "6 900 0 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6900021989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("690021989 [phone number: INVALID]")));
    }

    @Test
    public void testNoInterpretationCase() {
        String input = "6 9 7 6 7 8 9 8 6 8";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertEquals(1, output.size());

        assertTrue(output.contains("Interpretation #1: 6976789868 [phone number: VALID]"));
    }

    @Test
    public void elevenNoInterpretation() {
        String input = "6 9 7 6 7 8 9 8 11";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertEquals(1, output.size());

        assertTrue(output.contains("Interpretation #1: 6976789811 [phone number: VALID]"));

        assertFalse(output.stream().anyMatch(s -> s.contains("69767898101 [phone number: INVALID]")));
    }

    @Test
    public void twelveNoInterpretation() {
        String input = "6 9 7 6 7 8 9 8 12";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertEquals(1, output.size());

        assertTrue(output.contains("Interpretation #1: 6976789812 [phone number: VALID]"));

        assertFalse(output.stream().anyMatch(s -> s.contains("69767898102 [phone number: INVALID]")));
    }

    @Test
    public void greekOneHundredNoInterpretation() {
        String input = "6 9 7 6 7 8 9 110";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertEquals(1, output.size());

        assertTrue(output.contains("Interpretation #1: 6976789110 [phone number: VALID]"));

        assertFalse(output.stream().anyMatch(s -> s.contains("6976789810010 [phone number: INVALID]")));
    }

    @Test
    public void extraWhiteSpaces() {
        String input = "           69              7               36              82            5           54       ";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682554 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69730682554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736802554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697306802554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736825504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697306825504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697368025504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6973068025504 [phone number: INVALID]")));
    }

    @Test
    public void randomExample1() {
        String input = "2 31 0 6 21 9 89";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("2310621989 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106219809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106201989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("231062019809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23010621989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("230106219809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106201989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("231062019809 [phone number: INVALID]")));
    }

    @Test
    public void randomExample2() {
        String input = "2 3 10 61 61 89";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("2310616189 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106161809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106160189 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("231061601809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("23106016189 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("231060161809 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("231060160189 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("2310601601809 [phone number: INVALID]")));
    }

    @Test
    public void randomExample3() {
        String input = "6 94 6 28 77 94";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6946287794 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694628707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462087794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694620877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694620870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6946208707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69046287794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904628707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462087794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904620877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904620870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69046208707904 [phone number: INVALID]")));
    }

    @Test
    public void randomExample4() {
        String input = "6 94 6 28 77 94";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6946287794 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694628707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69462087794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694620877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("694620870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6946208707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69046287794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904628707904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("690462087794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904620877904 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6904620870794 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69046208707904 [phone number: INVALID]")));
    }

    // BIASED INTERPRETATION TESTS, COMMENT OUT IF BIASED INTERPRETATION IS COMMENTED OUT
    @Test
    public void biasedInterpretations69OnlyMerged() {
        String input = "69 7 36 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69730680104 [phone number: INVALID]")));
        // Following numbers should not appear because of biased interpretation of "69"
        assertFalse(output.stream().anyMatch(s -> s.contains("60973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("609730680104 [phone number: INVALID]")));
    }


    @Test
    public void biasedInterpretations60_9OnlyMerged() {
        String input = "60 9 7 3 6 8 0 1 0 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("60973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations600_97OnlyMerged() {
        String input = "600 97 3 6 8 0 1 0 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69073680104 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("600973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("6009073680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations600_9NotMerged() {
        String input = "600 9 7 3 6 8 0 1 0 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertFalse(output.stream().anyMatch(s -> s.contains("6973680104 [phone number: VALID]")));

        assertTrue(output.stream().anyMatch(s -> s.contains("600973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations697OnlyMerged69() {
        String input = "697 3 6 8 0 1 0 4";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69073680104 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("600973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("6009073680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations0032OnlyBrokenUp() {
        String input = "0 0 32 3 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0032310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations0030_2OnlyBrokenUp() {
        String input = "0 0 30 2 3 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0032310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00302OnlyMerged() {
        String input = "0 0 302 3 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003002310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00300_2OnlyMerged() {
        String input = "0 0 300 2 3 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003002310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00300_23NotFixed() {
        String input = "0 0 300 23 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertFalse(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertTrue(output.stream().anyMatch(s -> s.contains("003002310621989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("0030020310621989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("0032310621989 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("00320310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations0030_2OnlyMerged() {
        String input = "0 0 300 2 3 1 0 6 2 1 9 8 9";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00302310621989 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003002310621989 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations0030_6OnlyBrokenUp() {
        String input = "0 0 30 6 9 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0036973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00306OnlyMerged() {
        String input = "0 0 306 9 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003006973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00300_6OnlyMerged() {
        String input = "0 0 300 6 9 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003006973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations00300_69NotMergedIncorrectBiasedForm() {
        String input = "0 0 300 69 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertFalse(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertTrue(output.stream().anyMatch(s -> s.contains("003006973680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("0030060973680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("0036973680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("00360973680104 [phone number: INVALID]")));
    }


    @Test
    public void biasedInterpretations0030_69OnlyMerged() {
        String input = "0 0 30 69 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003060973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("00360973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("0036973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations0030_697OnlyMerged() {
        String input = "0 0 30 697 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("003069073680104 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0030600973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("00306009073680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations003_0_69OnlyMerged(){
        String input = "0 0 3 0 69 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003060973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations003_0_60_9OnlyMerged(){
        String input = "0 0 3 0 60 9 7 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("003060973680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations003_0_600_97OnlyMerged(){
        String input = "0 0 3 0 600 97 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("003069073680104 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0030600973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("00306009073680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations003_0_600_973NotMerged(){
        String input = "0 0 3 0 600 973 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertFalse(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("003069073680104 [phone number: INVALID]")));

        assertTrue(output.stream().anyMatch(s -> s.contains("0030600973680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("003060090073680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("0030600900703680104 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("00306009703680104 [phone number: INVALID]")));
    }

    @Test
    public void biasedInterpretations003_0_697NotBrokenUp69(){
        String input = "0 0 3 0 697 3 6 80 104";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("00306973680104 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("003069073680104 [phone number: INVALID]")));

        assertFalse(output.stream().anyMatch(s -> s.contains("0030600973680104 [phone number: INVALID]")));
        assertFalse(output.stream().anyMatch(s -> s.contains("00306009073680104 [phone number: INVALID]")));
    }

    @Test
    public void lastSequenceDoubleDigitNoZero() {
        String input = "69 7 36 82 5 54";
        ArrayList<String> output = NaturalNumbersInterpretation.prepareOutput(input);
        assertTrue(output.stream().anyMatch(s -> s.contains("6973682554 [phone number: VALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69730682554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736802554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697306802554 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("69736825504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697306825504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("697368025504 [phone number: INVALID]")));
        assertTrue(output.stream().anyMatch(s -> s.contains("6973068025504 [phone number: INVALID]")));
    }

}
