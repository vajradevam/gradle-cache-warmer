#!/usr/bin/env bash
# Run ONCE per lab machine WITH internet. Fills ~/.gradle so builds work offline.
set -euo pipefail
cd "$(dirname "$0")"

command -v gradle >/dev/null || { echo "gradle not on PATH"; exit 1; }
gradle --version | grep -E '^Gradle 9\.0\.0$' >/dev/null || echo "WARNING: Gradle is not 9.0.0"
java -version 2>&1 | grep -q '25\.0\.3' || echo "WARNING: JDK is not 25.0.3"

gradle --no-daemon clean build jacocoTestReport run
gradle --no-daemon --stop || true

echo "Verifying offline..."
gradle --no-daemon --offline clean build
echo "OK: cache is warm (~/.gradle)."
