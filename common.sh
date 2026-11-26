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

# run_build: test BOTH ways a student can build, because the exam zip has both:
#   1) the machine's installed `gradle`
#   2) the project's own ./gradlew (downloads the pinned Gradle, 9.0.0)
# Both must pass. No --offline flag: the exam machine just has no internet.
run_build() {
  cd "$PROJECT" || exit 2
  echo "Java:   $(java -version 2>&1 | head -1)"
  echo "Internet detected: $(net_state)"
  local rc1=0 rc2=0
  echo; echo "##### A) installed gradle #####"
  if command -v gradle >/dev/null; then
    echo "Gradle: $(gradle --version --no-daemon 2>/dev/null | grep -m1 '^Gradle') ($(readlink -f "$(command -v gradle)"))"
    gradle --no-daemon --console=plain clean build test || rc1=$?
  else
    echo "ERROR: 'gradle' not found on PATH."; rc1=127
  fi
  echo; echo "##### B) project wrapper: ./gradlew #####"
  echo "Wrapper pins: $(grep distributionUrl gradle/wrapper/gradle-wrapper.properties | sed 's/.*gradle-//; s/-bin.zip//')"
  ./gradlew --no-daemon --console=plain clean build test || rc2=$?
  echo; echo "RESULT: gradle=$([ $rc1 -eq 0 ] && echo PASS || echo FAIL)  ./gradlew=$([ $rc2 -eq 0 ] && echo PASS || echo FAIL)"
  [ $rc1 -eq 0 ] && [ $rc2 -eq 0 ]
}
