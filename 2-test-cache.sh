#!/usr/bin/env bash
# STEP 2 of 2. Turn internet OFF first (checked below). Verifies the cache: generates a brand-new project with `gradle init` in a temp
# dir, overlays the tester sources, and builds it with --offline (via both
# `gradle` and `./gradlew`). Prints PASS/FAIL. Run after warm-cache.sh.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
source "$HERE/common.sh"
check_tools

if curl -fsS -o /dev/null --max-time 5 https://repo.maven.apache.org/maven2/ 2>/dev/null; then
    echo "NOTE: internet still looks ON. Builds below use --offline anyway, but turn it OFF for a real test."
fi

TMP="$(mktemp -d)"
trap 'gradle --stop >/dev/null 2>&1 || true; rm -rf "$TMP"' EXIT

ok=1

echo "== Check A: gradle init + tester sources, offline build with gradle and ./gradlew"
new_project "$TMP/exam-sim" exam-sim app "$HERE/sources/tester/src"
( cd "$TMP/exam-sim" && gradle --offline clean build && ./gradlew --offline clean build ) || ok=0

echo "== Check B: plain gradle init (nothing copied), offline build, test, run"
new_project "$TMP/plain" plain app "" plain
( cd "$TMP/plain" && gradle --offline build && gradle --offline test --rerun && gradle --offline run ) || ok=0

echo "== Check C: project pinned to Gradle $PIN_GRADLE (like a zip from the professor), offline ./gradlew and gradle"
new_project "$TMP/pinned" pinned app "$HERE/sources/tester/src"
sed -i "s#gradle-[0-9.]*-bin.zip#gradle-$PIN_GRADLE-bin.zip#" "$TMP/pinned/gradle/wrapper/gradle-wrapper.properties"
( cd "$TMP/pinned" && ./gradlew --offline build && gradle --offline clean build ) || ok=0

if [ "$ok" = 1 ]; then
    echo; echo "=============================================="
    echo " STEP 2 PASSED: offline builds work (checks A, B and C). Cache is good."
    echo " >>> Now turn the internet ON again (normal lab use / next machine)."
    echo "=============================================="
else
    echo; echo "FAIL: offline build failed. Turn internet ON and re-run ./1-warm-cache.sh, then OFF and ./2-test-cache.sh again." >&2
    exit 1
fi
