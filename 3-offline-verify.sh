#!/usr/bin/env bash
# STEP 3: INTERNET OFF.  Same build again, now from the cache only.
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "=== STEP 3 (internet OFF, cache filled) ==="
if [ "$(net_state)" = ON ]; then
  echo "Internet is still ON. Turn it OFF and re-run."; exit 1
fi
pkill -u "$USER" -f GradleDaemon 2>/dev/null; sleep 1
rm -rf "$PROJECT/.gradle" "$PROJECT/build" "$PROJECT/app/build"   # keep ~/.gradle
if run_build; then
  echo
  echo "================================================================"
  echo "STEP 3 PASSED: gradle AND ./gradlew build+test OFFLINE."
  echo "================================================================"
else
  echo; echo "STEP 3 FAILED: the cache is NOT enough for the exam project."; exit 1
fi
