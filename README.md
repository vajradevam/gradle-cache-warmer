# Exam simulation

Checks that the exam project builds and tests
offline. Run the steps in order. Each script tells you when to switch the internet.

| # | Internet | Command | What it does |
|---|----------|---------|--------------|
| 0 | ON  | `git clone https://github.com/vajradevam/gradle-cache-warmer.git ~/gradle-cache-warmer && cd ~/gradle-cache-warmer` | get the repo and enter it (once) |
| 1 | OFF | `./1-offline-empty-cache.sh` | deletes `~/.gradle`; build must fail |
| 2 | ON  | `./2-online-warm.sh` | build and test; fills the cache |
| 3 | OFF | `./3-offline-verify.sh` | must print `STEP 3 PASSED` |

If step 2 fails with a download timeout, just run it again.
