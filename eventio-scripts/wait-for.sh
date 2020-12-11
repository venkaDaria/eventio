#!/bin/bash
set -e

host="$1"
shift
cmd="$@"

while true; do curl -s $host > /dev/null && break; done

>&2 echo "Server is up"

exec $cmd
