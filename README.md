<a id="readme-top"></a>


# Natural Numbers Interpretation

Java application that processes number sequences as spoken inputs recorded by speech recognition and reconstructs possible Greek phone numbers.

## Contents
- [Description](#description)
- [Features](#features)
- [How to Run](#how-to-run)
- [Design Decisions](#design-decisions)
- [Example Usage](#example-usage)
- [Contact](#contact)
- [License](#license)

## Description

The app displays a GUI interface and receives as input numbers uttered by people and received by speech recognition software, with spaces seperating the numbers recognized. Possible ambiguities in natural speech are then resolved, and multiple interpretations generated. Each resulting number is shown with a flag showing whether it is a valid or invalid Greek number.

Ambiguities include double-digit numbers that have been incorrectly seperated by speech recognition, e.g. "40 2" being a wrongfully interpreted "forty-two" and not a "forty, two", and vice versa.
This also applies to triple-digit numbers, like "200 60 2" being a misinterpreted "262" or "260 2".

A valid Greek number must be in one of the following categories:
- Have a length of 10 digits and:
  - Start with "2" or
  - Start with "69"
- Have a length of 14 digits and:
  - Start with "00302" or
  - Start with "003069" 

Taking these rules into consideration, my code makes biased interpretations for the first parts of the input, e.g. always resolving leading "60 9" as "69" instead of keeping "60 9"-branching interpretations, a branch that would result in only invalid numbers.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Features

- Rebuilds number from spaced digit input
- Handles speech ambiguities
- Returns resulting interpretations marked as valid or invalid Greek numbers (rules regarding length and starting numbers)
- Biased interpretation logic for higher accuracy
- JUnit tests included

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## How to Run

1. Clone repo
```
git clone https://github.com/BlackBaron94/Natural-Numbers-Interpretation.git
cd Natural-Numbers-Interpretation
```

2. With Java installed, main app can be run with:
```
java NaturalNumbersInterpretation.java
```

To run the tests, use IntelliJ or any IDE with Java and JUnit 5.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Design Decisions

There are many fine points to be made for the rules of the app.

- Numbers 11 and 12 can never be confused for 10 1 and 10 2, regardless of speaker language, and there is code to prevent the splitting and merging of those cases.

- Depending whether speech recognition transcribes numbers from English or Greek speakers, the following distinctions are encountered:
  - In English "14" (fourteen) can never be a "10 4" (ten four), while in Greek it can (14 = dekatessera, 10 4 = deka, tessera) (range extends from 13-19). 
  - In English "13" (thirteen) may be an incorrectly recognized "30" (thirty), "14" maybe "40" etc.
  - In Greek "110" (ekaton deka) is audibly distinguishalbe from "100 10" (ekato, deka), while in English it is not (110 = one hundred ten, 100 10 = one hundred, ten).
The app assumes the input is derived from Greek speakers, but code resolving the case of English speakers is also included but commented out. Greek specific code is also modularized, so that it can be easily commented out.

- There is no apparent reason to make unbiased interpretations for the prefixes, thus my code skips branching ambiguities into only invalid prefix numbers. Code can easily be commented out in case this is an undesirable effect, when method biasedInterpretations() is called and alters inputArray, inside the getInterpretations() method.

Regarding other design choices, I opted to parse the contents in a character level. The only case where substrings were used was for biasedInterpretations for readability purposes. Code makes which case is being examined apparent instantly by using .startsWith(). 
When input contains a 4-digit sequence, the app recognizes it and returns only interpretations made up to that point for previous multi-digit sequences, while printing a message about illegal entry.
Input is also filtered using RegEx, so that input contains only spaces and numbers.

Tests include base and advanced tests, e.g. Greek specific test cases (101-199 numbers), input with a lot of whitespaces, uniquely interpreted input, a lot of biasedInterpretations tests and many more edge cases.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Example Usage

Below is an image example of using the app. 

As you can see below, "0 0 30 69 700 24 1 3 50 2" taken as is, results in an Invalid number. Thus, begin the interpretations.
- The program skips splitting 69 into possible misinterpreted "60 9", avoiding a lot of invalid numbers.
  - 700 24 is combined in case it was a misinterpreted "724" or "720 4".
  - 24 is also interpreted as a possible "20 4".
  - 50 2 is also interpreted as a possible "52".
Ambiguity of 3 cases with 2 interpretations each, result in 2^3 = 8 different possibilities, as displayed below.

<div align="center">
    <img src="https://raw.githubusercontent.com/BlackBaron94/images/main/Natural-Number-Interpretation/Use_Case.jpg" alt="Use_Case" width="750"/>
</div>


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

Γιώργος Τσολακίδης - [Linked In: Giorgos Tsolakidis](https://www.linkedin.com/in/black-baron/) - black_baron94@hotmail.com 

Project Link: [Natural Numbers Interpretation](https://github.com/BlackBaron94/Natural-Numbers-Interpretation)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## License


This project is licensed under the MIT License – see the [LICENSE](./LICENSE) file for details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>
