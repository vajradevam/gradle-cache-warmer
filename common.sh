#!/usr/bin/env bash
# Shared helpers for the exam simulation. Sourced, not run.
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT="$HERE/project"
GHOME="${GRADLE_USER_HOME:-$HOME/.gradle}"

net_state() {
  if curl -sI -m 6 -o /dev/null https://repo.maven.apache.org/maven2/ 2>/dev/null; then
    echo ON
  else
    echo OFF
  fi
}

# run_build: use the MACHINE'S installed `gradle` (not ./gradlew) and Java 25,
# because that is what students run in the exam. No --offline flag: the exam
# machine just has no internet, so we do not hand Gradle any help.
run_build() {
  command -v gradle >/dev/null || { echo "ERROR: 'gradle' not found on PATH."; return 127; }
  cd "$PROJECT" || exit 2
  echo "Java:   $(java -version 2>&1 | head -1)"
  echo "Gradle: $(gradle --version --no-daemon 2>/dev/null | grep -m1 '^Gradle') ($(readlink -f "$(command -v gradle)"))"
  echo "Internet detected: $(net_state)"
  echo
  local rc=0
  gradle --no-daemon --console=plain clean build test || rc=$?
  return $rc
}
