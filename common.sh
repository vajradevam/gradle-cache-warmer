# Shared helpers, sourced by warm-cache.sh and test-cache.sh.
# Uses THIS machine's gradle and java, so the cache matches what students get
# from `gradle init` here.

check_tools() {
    command -v gradle >/dev/null || { echo "gradle not on PATH" >&2; exit 1; }
    command -v java   >/dev/null || { echo "java not on PATH" >&2; exit 1; }
    echo "== $(gradle --version | grep -E '^Gradle ')"
    echo "== $(java -version 2>&1 | head -1)"
    JAVA_MAJOR="$(java -version 2>&1 | sed -n '1s/.*"\([0-9]*\)[."].*/\1/p')"
}

# new_project <dir> <name> <app|lib> <sources-dir or "">
# Runs `gradle init` exactly like a student would, then overlays our sources.
new_project() {
    local dir="$1" name="$2" kind="$3" src="$4" type sub
    if [ "$kind" = app ]; then type=java-application; sub=app; else type=java-library; sub=lib; fi
    mkdir -p "$dir"
    ( cd "$dir" && gradle init --type "$type" --dsl kotlin --test-framework junit-jupiter \
        --package demo --project-name "$name" --no-incubating \
        --java-version "$JAVA_MAJOR" --use-defaults >/dev/null )
    if [ -n "$src" ]; then
        cp -r "$src"/. "$dir/$sub/src/"
    fi
    cat >> "$dir/$sub/build.gradle.kts" <<'EOK'

apply(plugin = "jacoco")

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:-missing", "-quiet")
}

tasks.named<Test>("test") {
    testLogging {
        events("passed", "failed", "skipped")
    }
}
EOK
}
