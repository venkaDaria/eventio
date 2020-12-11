#!/bin/bash
GREEN=`tput setaf 2`
RESET=`tput sgr0`

echo "
  ${GREEN}help${RESET} - help about all commands that exists
  ${GREEN}run [+sonar]${RESET} - start app
  ${GREEN}restart <service_name>${RESET} - restart service by name
  ${GREEN}stop${RESET} - stop app
  ${GREEN}clean [+images]${RESET} - stop app and clean all
  ${GREEN}support${RESET} - add Sonar-Kotlin support
  ${GREEN}lt${RESET} - restart localtunnel (endless work)
  ${GREEN}wait-for${RESET} - wait for service to be started
"
