#!/usr/bin/env bash
# Run ONCE per lab machine WITH internet. Fills ~/.gradle so builds work offline.
# Uses the machine's own gradle + java.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
source "$HERE/common.sh"
check_tools

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

echo "OK: warmer finished. Now run ./test-cache.sh to verify."
