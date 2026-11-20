#!/usr/bin/env bash
# STEP 1 of 2. Internet must be ON. Run once per lab machine. Fills ~/.gradle so builds work offline.
# Uses the machine's own gradle + java.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
source "$HERE/common.sh"
check_tools

echo "== Checking internet (needed for warming)"
if ! curl -fsS -o /dev/null --max-time 15 https://services.gradle.org/distributions/ \
   || ! curl -fsS -o /dev/null --max-time 15 https://repo.maven.apache.org/maven2/; then
    echo "ERROR: no internet. Connect and re-run ./warm-cache.sh" >&2
    exit 1
fi

TMP="$(mktemp -d)"
trap 'gradle --stop >/dev/null 2>&1 || true; rm -rf "$TMP"' EXIT

echo "== Warming: application project (gradle init + our sources)"
new_project "$TMP/warm-app" warm-app app "$HERE/sources/warmer/src"
( cd "$TMP/warm-app"
  gradle build jacocoTestReport javadoc run
  ./gradlew --version >/dev/null     # caches the wrapper distribution too
  ./gradlew build )

echo "== Warming: library project (gradle init)"
new_project "$TMP/warm-lib" warm-lib lib ""
( cd "$TMP/warm-lib" && gradle build javadoc )

echo "== Re-checking offline"
( cd "$TMP/warm-app" && gradle --offline clean build jacocoTestReport javadoc )
( cd "$TMP/warm-lib" && gradle --offline clean build )

echo
echo "=============================================="
echo " STEP 1 DONE: ~/.gradle is warm."
echo " >>> Now turn the internet OFF, then run ./2-test-cache.sh"
echo "=============================================="
