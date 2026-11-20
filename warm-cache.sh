#!/usr/bin/env bash
# Run ONCE per lab machine WITH internet. Fills ~/.gradle so builds work offline.
set -euo pipefail
cd "$(dirname "$0")/warmer"

command -v gradle >/dev/null || { echo "gradle not on PATH"; exit 1; }
gv="$(gradle --version)"; jv="$(java -version 2>&1)"
grep -qE '^Gradle 9\.0\.0$' <<<"$gv" || echo "WARNING: Gradle is not 9.0.0"
grep -q '"25\.0\.3"' <<<"$jv" || echo "WARNING: JDK is not 25.0.3"

gradle --no-daemon clean build jacocoTestReport javadoc run
gradle --no-daemon --stop || true

echo "Verifying offline..."
gradle --no-daemon --offline clean build javadoc
rm -rf build .gradle
echo "OK: warmer finished. Now run ./test-cache.sh to verify with a fresh project."
