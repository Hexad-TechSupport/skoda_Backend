#!/bin/bash
# Stop the running application
pid=$(pgrep -f app.jar)
if [ -n "$pid" ]; then
    kill $pid
fi
exit 0
