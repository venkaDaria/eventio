#!/bin/bash

cmd="$1"
shift
args="$@"

exec ./eventio-scripts/$cmd.sh $args

# call: ./eventio.sh [cmd] => run
