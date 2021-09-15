# A basic bash script to generate files used for testing purposes

#! /bin/bash

NUM_SETS=$2
SIZE=$3
NUM_CHECK="^[0-9]+$"

case "$1" in
    # Generate compilation of files with all the test cases
    "-genAll")
        # ARGUMENT VALIDATIONS
        if [[ $# -ne 3 ]]; then
            echo ""
            echo "ERROR:"
            echo "  [Number of sets] [Number of characters] required as integer argements"
            echo ""
            echo "TRY: '-genRand 5 100' -- Example will generate 5 sets of valid files containing 100 randomly generated characters each"
            echo ""
            exit 1
        elif ! [[ $2 =~ $NUM_CHECK ]] || ! [[ $3 =~ $NUM_CHECK ]]; then
            echo ""
            echo "ERROR:"
            echo "  [Number of sets] [Number of characters] required as integer argements"
            echo ""
            echo "TRY: '-genRand 5 100' -- Example will generate 5 sets of valid files containing 100 randomly generated characters each"
            echo ""
            exit 1
        fi

        # Random data in files
        for ((i=1; i <= NUM_SETS ; i++)) ; do
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.txt
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.md
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.java
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.cs
        done
        
        # 100% similarity
        echo "100% Similarity" > hundredFile.txt 
        echo "100% Similarity" > hundredFile.md 
        echo "100% Similarity" > hundredFile.java 
        echo "100% Similarity" > hundredFile.cs 

        # 66.7% similarity
        echo "123456789a" > SixtySixFile.txt
        echo "12345" > SixtySixFile.md

        # 50% similarity
        echo "abcdefghi" > FiftyFile.txt
        echo "abc" > FiftyFile.cs

        # 0% similarity
        echo "A" > zeroFile.txt 
        echo "B" > zeroFile.md 
        echo "CD" > zerFile.java 
        echo "EFG" > zeroFile.cs

        # Empty files
        touch emptyFile.txt
        touch emptyFile.md
        touch emptyFile.java
        touch emptyFile.cs

        # Invalid files
        echo "Invalid file" > invalidFile.c 
        echo "Invalid file" > invalidFile.html 
        echo "Invalid file" > invalidFile.js
        echo "Invalid file" > invalidFile.cpp
        ;;

    # Generate files with random data
    "-genRand")
        # ARGUMENT VALIDATIONS
        if [[ $# -ne 3 ]]; then
            echo ""
            echo "ERROR:"
            echo "  [Number of sets] [Number of characters] required as integer argements"
            echo ""
            echo "TRY: '-genRand 5 100' -- Example will generate 5 sets of valid files containing 100 randomly generated characters each"
            echo ""
            exit 1
        elif ! [[ $2 =~ $NUM_CHECK ]] || ! [[ $3 =~ $NUM_CHECK ]]; then
            echo ""
            echo "ERROR:"
            echo "  [Number of sets] [Number of characters] required as integer argements"
            echo ""
            echo "TRY: '-genRand 5 100' -- Example will generate 5 sets of valid files containing 100 randomly generated characters each"
            echo ""
            exit 1
        fi

        for ((i=1; i <= NUM_SETS ; i++)) ; do
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.txt
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.md
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.java
            tr -dc "[:space:][:print:]" </dev/urandom | head -c $3 > randFile$i.cs
        done
        ;;
    
    # Generate 100% similarity files
    "-gen100")
        echo "100% Similarity" > hundredFile.txt 
        echo "100% Similarity" > hundredFile.md 
        echo "100% Similarity" > hundredFile.java 
        echo "100% Similarity" > hundredFile.cs 
        ;;

    # Generate two files with 66.7% (0.66667) similarity
    "-gen66")
        echo "123456789a" > SixtySixFile.txt
        echo "12345" > SixtySixFile.md
        ;;

    # Generate two files with 50% (0.5) similarity
    "-gen50")
        echo "abcdefghi" > FiftyFile.txt
        echo "abc" > FiftyFile.cs
        ;;

    # Generate 0% similarity files
    "-gen0")
        echo "A" > zeroFile.txt 
        echo "B" > zeroFile.md 
        echo "CD" > zerFile.java 
        echo "EFG" > zeroFile.cs 
        ;;

    # Generate empty files
    "-genEmpty")
        touch emptyFile.txt
        touch emptyFile.md
        touch emptyFile.java
        touch emptyFile.cs
        ;;

    # Generate invalid files
    "-genInvalid")
        echo "Invalid file" > invalidFile.c 
        echo "Invalid file" > invalidFile.html 
        echo "Invalid file" > invalidFile.js
        echo "Invalid file" > invalidFile.cpp 
        ;;

    "-clean")
        rm *.txt
        rm *.md
        rm *.java
        rm *.cs
        rm *.c
        rm *.html
        rm *.js
        rm *.cpp
        ;;

    *)
        echo ""
        echo "INVALID COMMAND"
        echo "---------------"
        echo "Try running either of the following arguments:"
        echo "  '-genRand [Number of valid file sets] [Number of characters]' -- To generate sets of random data files"
        echo "  '-genEmpty' -- To generate a set of empty files"
        echo "  '-gen0' -- To generate a set of files with 0% similarity"
        echo "  '-gen100' -- To generate a set of files with 100% similarity"
        echo "  '-genInvalid' -- To generate a set of invalid files"
        echo "  '-clean' -- To delete any files created."
        echo ""
        echo "----------------------------------------------------------------------------------"
        echo "WARNING: Generate files in a specific directory separate to other project files!!!"
        echo "----------------------------------------------------------------------------------"
        echo ""

        exit 1
        ;;
esac