#!/usr/bin/env bash

echo -e "make sure your working directory is /test before proceeding/n"


# check gcc presence
if hash g++ 2>/dev/null; then
        echo "g++ environment set up."
    else
        echo "g++ not available on your system."
        exit -1
    fi

# run all tests
echo "running all tests."
DIRECTORY=.
for i in ${DIRECTORY}/test*.out.cpp; do
    echo -e ">> testing "${i}"\n"
    if g++ -o "temp.out" ${i}; then
        echo "compilation pass."
    else
        echo "error during g++ compilation."
        exit -1
    fi

    ./temp.out
done