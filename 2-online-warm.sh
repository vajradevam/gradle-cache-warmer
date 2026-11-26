#!/usr/bin/env bash
# STEP 2: INTERNET ON.  Build and test the exam project; fills ~/.gradle.
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "=== STEP 2 (internet ON) ==="
if [ "$(net_state)" = OFF ]; then
  echo "Internet is OFF. Turn it ON and re-run."; exit 1
fi
if run_build; then
  echo
  echo "================================================================"
  echo "STEP 2 DONE: gradle AND ./gradlew build+test OK; cache filled."
  echo "Now turn internet OFF, then run ./3-offline-verify.sh"
  echo "================================================================"
else
  echo; echo "STEP 2 FAILED (check connection; wrapper download can time out; re-run)."; exit 1
fi
