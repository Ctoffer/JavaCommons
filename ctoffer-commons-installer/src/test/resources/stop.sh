#!/bin/bash

pid=$( pgrep -f ctoffer-yt-service-2022.8.12.1.jar )

if [[ ! -z "${pid}" ]]; then
  for cid in $(pgrep -P ${pid}); do sudo kill -9 $cid; done
  sudo kill -9 $pid
fi
