# gradle-cache-warmer

Fills `~/.gradle` so Gradle 9.0.0 / OpenJDK 25.0.3 / JUnit 5 work **offline** (exam).

| Dir / script      | Purpose |
|-------------------|---------|
| `warmer/`         | Bigger project (streams, java.time, nio, records/sealed, Swing, many JUnit 5 features, jacoco, javadoc) that downloads everything. |
| `tester/`         | Independent "student-style" project, used only to verify the cache. |
| `./warm-cache.sh` | Run once per machine **with internet**. Builds `warmer/`, then rebuilds it `--offline`. |
| `./test-cache.sh` | Copies `tester/` to a fresh temp dir and builds it with `--offline`. Prints PASS/FAIL. Run after warming (or in the exam room, with internet off). |

Swing tests that need a display are skipped automatically on headless machines.

Notes:
- Exam projects must use the same dependency versions (`junit-bom:5.13.4`); other versions are not cached.
- The cache is per user: run as the account students use, or copy `~/.gradle` to every account.
- Plugins not used here (checkstyle, pmd, Kotlin plugin, ...) are not cached; add them to `warmer/build.gradle.kts`.
