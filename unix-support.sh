#!/bin/bash

function removeWinLineBreak() {
  awk '{ sub("\r$", ""); print }' "$1" > temp.sh
  cat temp.sh > "$1"
  rm temp.sh
}

export -f removeWinLineBreak

find . -iname '*.sh' ! -iname 'win-support.sh' -exec bash -c 'removeWinLineBreak "$0"' {} \;
