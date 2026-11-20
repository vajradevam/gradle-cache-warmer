#!/usr/bin/env bash
# Verifies the cache: copies ./tester to a fresh temp dir (no build state, no
# .gradle dir) and builds it with --offline. Run after warm-cache.sh.
# Safe to run with internet on or off; --offline forbids Gradle from using it.
set -euo pipefail
SRC="$(cd "$(dirname "$0")" && pwd)/tester"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
cp -r "$SRC"/. "$TMP"/
cd "$TMP"

if gradle --no-daemon --offline clean build; then
    echo; echo "PASS: offline build works. Cache is good."
else
    echo; echo "FAIL: offline build failed. Re-run ./warm-cache.sh with internet." >&2
    exit 1
fi
