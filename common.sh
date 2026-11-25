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
  if [ $rc -eq 0 ]; then
    # 'gradlew run' does not forward stdin, and this app is a console menu,
    # so install the app with Gradle and feed the menu through its start script.
    ./gradlew --no-daemon --console=plain -q installDist || rc=$?
  fi
  if [ $rc -eq 0 ]; then
    echo "--- run (menu input: 1 add samples, 2 list, 0 exit) ---"
    local tmp; tmp="$(mktemp -d)"
    ( cd "$tmp" && printf '1\n2\n0\n' | "$PROJECT/app/build/install/app/bin/app" ) || rc=$?
    rm -rf "$tmp"
  fi
  return $rc
}
