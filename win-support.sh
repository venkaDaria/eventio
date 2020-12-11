#!/bin/bash

function addWinLineBreak() {
  awk '{ sub("$", "\r"); print }' "$1" > temp.sh
  cat temp.sh > "$1"
  rm temp.sh
}

function removeWinLineBreak() {
  awk '{ sub("\r$", ""); print }' "$1" > temp.sh
  cat temp.sh > "$1"
  rm temp.sh
}

export -f removeWinLineBreak
export -f addWinLineBreak

find . -iname '*.sh' -exec bash -c 'removeWinLineBreak "$0"' {} \;

find . -iname '*.sh' ! -iname 'unix-support.sh' -exec bash -c 'addWinLineBreak "$0"' {} \;
