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

# run_build <label>: use the PROJECT'S OWN wrapper (Gradle 9.0.0) and the
# machine's Java 25 (toolchain), exactly like the exam. No --offline flag:
# the exam machine just has no internet, so we do not hand Gradle any help.
run_build() {
  cd "$PROJECT" || exit 2
  echo "Java:   $(java -version 2>&1 | head -1)"
  echo "Gradle: $(grep distributionUrl gradle/wrapper/gradle-wrapper.properties | sed 's/.*gradle-//; s/-bin.zip//') (from the project's wrapper)"
  echo "Internet detected: $(net_state)"
  echo
  local rc=0
  ./gradlew --no-daemon --console=plain clean build test || rc=$?
  return $rc
}
