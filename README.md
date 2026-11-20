# gradle-cache-warmer

Run once on each lab machine **with internet** (Gradle 9.0.0, OpenJDK 25.0.3):

    ./warm-cache.sh

It builds a Kotlin-DSL project with JUnit 5 (Jupiter, incl. params) and fills `~/.gradle`.
During the exam (no internet) use `gradle --offline test` (or just `gradle test`;
cached dependencies are used without network, but `--offline` avoids slow timeouts).

Important:
- Exam projects must use the **same dependency versions** as `build.gradle.kts`
  (`junit-bom:5.13.4`, launcher via BOM). Different versions = not cached.
- Only the same user's `~/.gradle` is used; warm it as the account students log in with
  (or copy the filled `~/.gradle` to every account/image).
- Don't use `gradle clean`-style cache wipes or `~/.gradle` cleanup scripts between warm-up and exam.
- Plugins other than `java`, `application`, `jacoco` are not cached; add any others
  you use (checkstyle, pmd, ...) to `build.gradle.kts` and run the relevant tasks in `warm-cache.sh`.
