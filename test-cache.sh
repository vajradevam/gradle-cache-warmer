#!/usr/bin/env bash
# Verifies the cache: generates a brand-new project with `gradle init` in a temp
# dir, overlays the tester sources, and builds it with --offline (via both
# `gradle` and `./gradlew`). Prints PASS/FAIL. Run after warm-cache.sh.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
source "$HERE/common.sh"
check_tools

TMP="$(mktemp -d)"
trap 'gradle --stop >/dev/null 2>&1 || true; rm -rf "$TMP"' EXIT

new_project "$TMP/exam-sim" exam-sim app "$HERE/sources/tester/src"
cd "$TMP/exam-sim"
if gradle --offline clean build && ./gradlew --offline clean build; then
    echo; echo "PASS: offline builds work (gradle and ./gradlew). Cache is good."
else
    echo; echo "FAIL: offline build failed. Re-run ./warm-cache.sh with internet." >&2
    exit 1
fi
