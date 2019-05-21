#!/bin/bash
echo "Compiling source files"
javac *.java

echo "Running test 1 - 'a'"
java REcompiler "a"

echo "======================"
echo "Running test 1 - 'ab'"
java REcompiler "ab"

echo "======================"
echo "Running test 1 - 'a*'"
java REcompiler "a*"

echo "======================"
echo "Running test 1 - 'a|b'"
java REcompiler "a|b"

echo "======================"
echo "Running test 1 - 'a*|b'"
java REcompiler "a*|b"

echo "======================"
echo "Running test 1 - 'a*|b*'"
java REcompiler "a*|b*"