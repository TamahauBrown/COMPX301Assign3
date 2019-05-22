#!/bin/bash
echo "Compiling source files"
javac *.java

echo "Running test 1 - 'a'"
java REcompiler "a"

echo "======================"
echo "Running test 2 - 'ab'"
java REcompiler "ab"

echo "======================"
echo "Running test 3 - 'a*'"
java REcompiler "a*"

echo "======================"
echo "Running test 4 - 'a|b'"
java REcompiler "a|b"

echo "======================"
echo "Running test 5 - 'a*|b'"
java REcompiler "a*|b"

echo "======================"
echo "Running test 6 - 'a|b*'"
java REcompiler "a|b*"

echo "======================"
echo "Running test 7 - 'a*|b*'"
java REcompiler "(a*|b)*"