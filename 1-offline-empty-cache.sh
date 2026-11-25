#!/usr/bin/env bash
# STEP 1: INTERNET OFF.  Deletes the Gradle cache, then builds offline.
# A build with an EMPTY cache and no internet is EXPECTED TO FAIL: this proves
# the cache is really gone, so the later pass in step 3 is meaningful.
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "=== STEP 1 (internet OFF, empty cache) ==="
echo "Will DELETE: $GHOME  and  $PROJECT/.gradle  $PROJECT/build"
read -r -p "Type yes to continue: " a; [ "$a" = yes ] || { echo aborted; exit 1; }
pkill -u "$USER" -f GradleDaemon 2>/dev/null; sleep 1
rm -rf "$GHOME" "$PROJECT/.gradle" "$PROJECT/build" "$PROJECT/app/build"
echo "Cache deleted."
if [ "$(net_state)" = ON ]; then
  echo "WARNING: internet is still ON. Turn it OFF and re-run this script."; exit 1
fi
if run_build; then
  echo; echo "UNEXPECTED: build passed with an empty cache and no internet."; exit 1
fi
echo
echo "================================================================"
echo "STEP 1 DONE: build failed offline with an empty cache (expected)."
echo "Now turn internet ON, then run ./2-online-warm.sh"
echo "================================================================"
