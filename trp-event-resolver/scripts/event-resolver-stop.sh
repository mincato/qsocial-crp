#!/bin/bash
ps ax | grep -i 'trp-event-resolver.jar' | grep java | grep -v grep | awk '{print $1}' | xargs kill -SIGTERM