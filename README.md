# Exam simulation

Checks that the exam project (Gradle 9.0.0, Java 25) builds, tests and runs
offline. Run the steps in order. Each script tells you when to switch the internet.

| # | Internet | Command | What it does |
|---|----------|---------|--------------|
| 0 | ON  | `git clone https://github.com/vajradevam/gradle-cache-warmer.git ~/gradle-cache-warmer` | get the repo (once) |
| 1 | OFF | `cd ~/gradle-cache-warmer && ./1-offline-empty-cache.sh` | deletes `~/.gradle`; build must fail |
| 2 | ON  | `cd ~/gradle-cache-warmer && ./2-online-warm.sh` | build, test, run; fills the cache |
| 3 | OFF | `cd ~/gradle-cache-warmer && ./3-offline-verify.sh` | must print `STEP 3 PASSED` |

If step 2 fails with a download timeout, just run it again.
