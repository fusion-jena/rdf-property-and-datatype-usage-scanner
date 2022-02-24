#!/bin/bash
pushd "$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )" > /dev/null
mkdir -p logs
printf "\n\nStart measuring at %s ...\n" "$(date)" >> logs/logging.log
nohup java -jar target/Scanner.jar --scan $1 < /dev/null >> /dev/null 2>&1 &
popd > /dev/null
