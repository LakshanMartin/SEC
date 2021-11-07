# Text Editor Application

Software Engineering Concepts: Assignment 2

Semester 2, 2021

Author: Lakshan Martin

Submission: 7^th^ November 2021

## Purpose

This application is a demonstration and implementation of the following features:

- Domain Specific Language (DSL) used to create specific keybinding within the text editor application 
- Plugins can be loaded through class Reflection
- Internationalisation (I18N) of all GUI content
- Loading/Saving files with encoding options of UTF-8, UTF-16 or UTF-32

## How to compile

Compilation of this application requires the following:

- Gradle Build Tool --version 7.2 (https://gradle.org/)
- Java
- JavaCC (https://javacc.github.io/javacc/)

## Usage

### Running application

The application can be run with the default language pack by entering the following command:

- ```./gradlew run```

An imaginary language as been created to demonstrate the use of I18N. This language simply has an `A` appended to the end of each word. This language pack can be used by entering the following command line arguments:

- `./gradlew run --args='--locale=fake-A'`







