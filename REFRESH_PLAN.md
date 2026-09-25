# orienteer-logger Refresh Plan

Living plan for bringing orienteer-logger from its 2020 state (Java 8, unpinned 2016-era plugins, OSSRH publishing) to
current, supported tooling, in lockstep with [Orienteer's plan](https://github.com/OrienteerBAP/Orienteer/blob/master/REFRESH_PLAN.md)
(local: `~/Development/Orienteer/REFRESH_PLAN.md`). **Tick items off (`[ ]` → `[x]`) in the same commit that completes
them**, and add a short note (date, deviations) after the item when useful. Reference implementation for the process:
`~/Development/wicket-orientdb` (its `REFRESH_PLAN.md`, Stage A + phase R done).

Started: 2026-09-25. Versions below were checked on Maven Central on 2026-09-24/25 (same set as wicket-orientdb) —
re-check at the start of every stage (`./mvnw versions:display-dependency-updates versions:display-plugin-updates`).

## Progress overview

| Phase | Goal | Depends on | Unblocks Orienteer | Status |
|---|---|---|---|---|
| P0 | AI initialization: AGENTS.md + this plan | — | — | done |
| A | Stage A: JDK 21/25 build with `--release 21`, pinned plugins, `./mvnw install` of `1.4-SNAPSHOT` | P0 | P3 | todo |
| B | Stage B: nothing to port (no Wicket/servlet code) — verification only | A | P5 | todo (verification only) |
| C | Stage C: nothing to port (no `javax.servlet`/`javax.inject`/`javax.mail`) — verification only | A | P6 | todo (verification only) |
| R | Release & CI: GitHub Actions, Maven Central via the Central Portal: `1.4-SNAPSHOT` now, `1.4` after Orienteer P3 is green (D3) | A | P1/P3 "resolvable from a real repo" | todo |
| X | Cleanup, docs, known bugs & security | any time | — | todo |

## Guiding principles

1. **Never break the Orienteer contract** (below): coordinates, version `1.4-SNAPSHOT`/`1.4`, public and protected API,
   the JSON wire format of `OLoggerEvent.toJson()` and the fully qualified class names (Orienteer persists them).
2. **Keep the build green after every item.** One concern per commit; no big-bang changes.
3. **Stage A changes JDK/tooling only**, plus the one bug fix the owner approved (D9). Behavior-changing cleanups go to X.
4. **Zero runtime dependencies** stays true (README: "no extra dependencies"; plan D4).
5. **No reformat-only diffs;** match the surrounding indentation (tabs in most files, 4 spaces in two).

## Decisions log

| # | Date | Decision | Status |
|---|---|---|---|
| D1 | 2026-09-25 | Target `--release 21`; verify on JDK 21 **and** 25 (Temurin 21.0.11 / 25.0.4 via SDKMAN). JDK 27 (non-LTS) is not targeted; no JDK 8 support. Same as Orienteer D1. | accepted |
| D2 | 2026-09-25 | Coordinates stay `org.orienteer:logger`, a single jar (no parent pom, no test-jar); version stays **`1.4-SNAPSHOT`** for Stage A. | accepted |
| D3 | 2026-09-25 | **Versioning and publishing (Orienteer D9):** (1) Stage A is published as `1.4-SNAPSHOT` to the Central Portal snapshots repository (`https://central.sonatype.com/repository/maven-snapshots/`); (2) `1.4` is released from Stage A **only after Orienteer's P3 build is green against it** — fixes found there go into `1.4-SNAPSHOT` first; (3) after the release `master` moves to `1.5-SNAPSHOT`; (4) any later work (including a Stage B/C change, should one ever be needed) is never installed or published as `1.4-SNAPSHOT`. A jakarta/API-breaking change would get `2.0`. Since B/C need no code here, Orienteer keeps `1.4` through P5/P6. | accepted (Orienteer D9, 2026-09-24) |
| D4 | 2026-09-25 | **No runtime dependencies**, and no Lombok, Mockito, logging framework or Checkstyle are added: the library is 15 classes of plain JDK code. Diagnostics stay on `System.err`. | accepted |
| D5 | 2026-09-25 | Tests stay on **JUnit 4.13.2**. Jupiter/vintage are not needed (no Wicket testers here). | accepted |
| D6 | 2026-09-25 | **Frozen contract:** field names and shape of `OLoggerEvent.toJson()` (parsed by Orienteer's `ODocument.fromJSON`), public/protected signatures, and the FQNs of public classes (`org.orienteer.logger.impl.DefaultCorrelationIdGenerator` is stored in Orienteer's DB). | accepted |
| D7 | 2026-09-25 | Publishing (`distributionManagement` to OSSRH, nexus-staging, release plugin 2.5.3) is left untouched in Stage A and replaced in phase R (Central Portal), as in wicket-orientdb (its D7). There are no `<repositories>` to remove. | accepted |
| D8 | 2026-09-25 | Orienteer D8 (GraalJS instead of OrientDB's GraalVM 21.3.5) and the OrientDB 3.2.56 alignment **do not apply**: the library has no OrientDB dependency. | accepted |
| D9 | 2026-09-25 | **Fix `DefaultOLoggerEventFactory` in Stage A** (separate commit, with a test): it ignores the `source` and `metaData` arguments that the unreleased commit `159efd9` added to `IOLoggerEventFactory`/`OLogger.log(..)`, so `1.4` would ship a new API that does nothing. Null-guarded, so `OLogger.log(ex)` (Orienteer's only call) behaves exactly as before. | accepted (owner, 2026-09-25) |
| D10 | 2026-09-25 | POM `<url>` becomes the GitHub repository URL (`https://github.com/OrienteerBAP/orienteer-logger`) instead of `http://orienteer.org`, like wicket-orientdb (phase R). | accepted (owner, 2026-09-25) |
| D11 | 2026-09-25 | **Only the owner uploads** (snapshot deploys, releases) and pushes/tags; agents prepare and validate locally. The `org.orienteer` namespace must be verified on the Central Portal **with SNAPSHOTs enabled** before the first deploy — status unknown (open question). | accepted |

### Open questions (decide when the phase starts; record the answer above)

- [ ] **`org.orienteer` namespace** on the Central Portal: verified? SNAPSHOTs enabled? Blocks the first deploy. (R, owner)
- [ ] **Correlation IDs across JDKs:** exception IDs hash `StackTraceElement.toString()`, which since JDK 9 includes module
      names (`java.base/…`) and JDK-internal line numbers. When Orienteer moves from JDK 8 to 21, the same exception gets a
      new ID, so `IOLoggerDAO.getEventsByCorrelationId` no longer groups new incidents with stored ones. Accept the
      one-time regrouping (recommended, no code change), or hash a normalized frame (class, method, file, line) — which
      changes every ID once more and is a behavior change? (X, owner)
- [ ] **Dispatcher timeouts:** `DefaultOLoggerEventDispatcher` POSTs synchronously on the caller's thread (in Orienteer:
      the request thread) with no connect/read timeout; an unreachable collector blocks requests. Add default timeouts
      (behavior change, needs a config property)? (X)

## Contract with Orienteer (from Orienteer's "External dependency requirements")

| Stage | Orienteer phase | Required from this library | Here |
|---|---|---|---|
| A | P3 (Java 21, javax, Wicket 8) | JDK 21 build; release (or a published snapshot) | tooling only (section A) |
| B | P5 (Wicket 9) | — | nothing: no Wicket code |
| C | P6 (Wicket 10, jakarta) | jakarta if it touches servlet/mail APIs | nothing: it doesn't |

Orienteer's `orienteer-logger-server` (12 files) uses: `OLogger` (`log`, `set`), `OLoggerBuilder` (`setLoggerEventDispatcher`,
`addEnhancer`, `addDefaultEnhancers`, `create(config)`), `OLoggerEvent` (`getSeed`, `setMetaData(String, Object)`,
`toJson`), `IOLoggerConfiguration`, `IOLoggerEventDispatcher`, `IOLoggerEventEnhancer`, `IOCorrelationIdGenerator`,
`impl.DefaultOLoggerConfiguration`, and **extends** `impl.DefaultOLoggerEventDispatcher` (`dispatch`, `configure`,
protected `collectorUrl`, `syslog(OLoggerEvent)`, `syslog(String, Exception)`) and `impl.DefaultCorrelationIdGenerator`
(protected `generateDigestFromObject`). It stores `toJson()` output via `ODocument.fromJSON` and the generator class
names in the `OCorrelationIdGenerator` class.

---

## P0 — AI initialization

- [x] Analyze the repository: pom, plugins, JDK blockers, Stage B/C inventory, tests, branches, Orienteer usage — 2026-09-25
- [x] `AGENTS.md` (contract, stack, build status, commands, layout, conventions, testing, pitfalls) — 2026-09-25; single module, so no per-module files
- [x] This plan with decisions log, contract, stages, appendices — 2026-09-25

## A — Stage A: JDK 21/25 with `--release 21` (→ Orienteer P3)

Goal: `./mvnw clean verify` green on JDK 21 and 25 with `--release 21`; `./mvnw install` puts the jar and pom for
`1.4-SNAPSHOT` into `~/.m2`; class files major version 65. Size: **XS (< 1 hour)**.
Baseline (2026-09-25, untouched pom, `mvn` 3.9.16): `clean verify` **green on JDK 21.0.11 and 25.0.4** — 2 run,
0 failures, 0 skipped — with compiler 3.5.1 producing Java 8 bytecode (major 52) plus "source/target value 8 is obsolete"
warnings; surefire 3.5.4, jar 3.5.0, clean 3.2.0, resources 3.4.0 come unpinned from Maven's defaults. `-Prelease -Dgpg.skip`
also passes on JDK 21 (javadoc 2.10.4 still runs, 89 "no comment" warnings, 0 errors). Jar: 14 classes, no resources.

- [x] A1 Maven Wrapper 3.9.16 (`mvn wrapper:wrapper -Dmaven=3.9.16 -Dtype=only-script`): commit `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties` — 2026-09-25, wrapper plugin 3.3.4; scripts byte-identical to wicket-orientdb's; `./mvnw clean verify` green (JDK 21)
- [x] A1 `.sdkmanrc` (`java=21.0.11-tem`) and a minimal `.editorconfig` (UTF-8, LF, tabs for `*.java`/`*.xml`; no trailing-whitespace or final-newline enforcement; **no reformat**) — 2026-09-25; CRLF for `*.cmd`. The two 4-space Java files keep their style (noted in the file)
- [x] A2 Pin every build plugin once in `<pluginManagement>` with the wicket-orientdb versions (Appendix B); release-profile source/javadoc/gpg pinned too (versions only; wiring changes in R); nexus-staging and release plugin left for R (D7) — 2026-09-25: clean/resources 3.5.0, compiler 3.16.0 (config moved into `pluginManagement`, still source/target 1.8 until A3), surefire 3.6.0, jar 3.5.1, install/deploy 3.2.0, source 3.4.0, javadoc 3.12.0, gpg 3.2.8, plus dependency 3.11.0, wrapper 3.3.4, versions 2.22.0 (helper goals). Green on JDK 21; `-Prelease -Dgpg.skip -DskipTests verify` also green (javadoc 3.12.0: 0 errors). Nothing to remove: no dead plugins in the default build
- [x] A2 Add `maven-enforcer-plugin`: `requireMavenVersion [3.9,)`, `requireJavaVersion [21,)` — 2026-09-25 (enforcer 3.6.3; build-time only, not visible to consumers)
- [x] A3 Compiler: `maven.compiler.release=21` replaces `source`/`target` 1.8 (there is no `-parameters` to keep) — 2026-09-25: green on JDK 21 and 25, all class files major 65; the "obsolete source/target 8" warnings are gone, the only compiler warning left is the deprecated `new URL(String)` (X). Compiler 3.16.0 also writes an empty `package-info.class` (its `createMissingPackageInfoClass` default), so the jar has 15 class files instead of 14
- [x] A4 JUnit 4.13.1 → 4.13.2 (test scope) — 2026-09-25; hamcrest-core 1.3 unchanged; 2 run / 0 failures on JDK 21
- [ ] A5 JDK 21/25 incompatibilities: fix with minimal, behavior-preserving changes; add `--add-opens` only if a test proves it's needed and list it in the handoff (expected: none — no JAXB, Nashorn, `sun.*`, reflection or bytecode libraries)
- [ ] A6 Library hygiene: no compile/runtime dependencies (`dependency:tree` shows only `junit` in test scope); no logging binding/config, no servlet API; the jar holds only classes + Maven metadata (`jar tf`)
- [ ] A7 D9: `DefaultOLoggerEventFactory` applies `source` and `metaData` (null-guarded) + a unit test (`source`/`metaData` reach the dispatched event and `toJson()`)
- [ ] A8 `./mvnw clean verify` green on JDK 21 **and** 25; `./mvnw install`; confirm `~/.m2/repository/org/orienteer/logger/1.4-SNAPSHOT/` has the jar and pom, and all class files are major 65
- [ ] A9 Update `AGENTS.md` (build status) and write "Handoff to Orienteer" below
- [ ] Exit check: `./mvnw clean verify` green on JDK 21 and 25; `./mvnw install` done; test results match Appendix C (differences explained)

## B — Stage B: Wicket 9, still javax (→ Orienteer P5)

Goal: confirm the library needs nothing for Orienteer P5. Size: **XS (verification, < 15 minutes)**.

- [x] Inventory (Appendix D): no `org.apache.wicket`, `WicketTesterScope`, `util.time.Duration/Time`, page store,
      `ModalWindow`, danekja or `javax.servlet` usage in main or test sources — 2026-09-25 (0 files)
- [ ] When Orienteer starts P5: re-run the Appendix D search on `master`; if still 0 files, Orienteer keeps `1.4` and this
      stage is closed with no new version (D3)
- [ ] Exit check: `rg -l 'org\.apache\.wicket|javax\.servlet' src` finds nothing; `./mvnw clean verify` green on 21/25

## C — Stage C: jakarta (→ Orienteer P6)

Goal: confirm the library needs nothing for Orienteer P6 (jakarta Servlet 6, `jakarta.inject`/Guice 7, `jakarta.mail`,
`wicket-tester`). Size: **XS (verification, < 15 minutes)**.

- [x] Inventory (Appendix D): no `javax.servlet`, `javax.inject`, `com.google.inject`, `javax.mail`, `javax.activation`,
      `javax.annotation` or `javax.xml.bind` usage; the only `javax.*` import is `javax.net.ssl` (part of the JDK, not
      jakarta-renamed) — 2026-09-25
- [ ] When Orienteer starts P6: re-run the Appendix D search; if still clean, Orienteer keeps `1.4` (D3). If a
      jakarta-coupled feature is ever added, it gets its own major (`2.0`, D3)
- [ ] Exit check: `rg -l 'javax\.(servlet|inject|mail|activation|annotation|xml\.bind)' src` finds nothing; build green on 21/25

## R — Release & CI

- [ ] GitHub Actions workflow: push + PR, matrix JDK 21/25 (`actions/setup-java`, `distribution: temurin`, `cache: maven`), `./mvnw -B verify`, upload surefire reports, `concurrency` to cancel superseded runs
- [ ] Remove Travis and dead badges (there are none today); add the CI badge to `README.md`
- [ ] Replace OSSRH `distributionManagement` and `nexus-staging-maven-plugin` with `org.sonatype.central:central-publishing-maven-plugin` (0.11.0), server id from property `central.server.id` (default `central`), snapshots to the Central Portal snapshots URL (no demo module to exclude)
- [ ] Release profile: sources + javadoc jars (fix doclint errors), GPG signing (`maven-gpg-plugin` 3.x), required POM metadata (name, description, url per D10, https license URL, developers, scm); maven-release-plugin 2.5.3 → 3.x with tag format `v@{project.version}` (drop the reference to the non-existent `skip-integration-tests` profile)
- [ ] Snapshot publishing to `https://central.sonatype.com/repository/maven-snapshots/` (D3); Orienteer's root pom already has that repository (its P1, 2026-09-25)
- [ ] `.github/dependabot.yml` (maven + github-actions, weekly, grouped)
- [ ] JaCoCo report only (no thresholds)
- [ ] Local validation without any upload: build green on 21/25; `-Prelease -Dgpg.skip` builds sources/javadoc; list what a Central bundle would contain
- [ ] Owner: confirm the `org.orienteer` namespace is verified with SNAPSHOTs enabled (D11); first `1.4-SNAPSHOT` deploy
- [ ] Owner: release `1.4` once Orienteer P3 is green against `1.4-SNAPSHOT` (GPG key published to a keyserver); then `master` → `1.5-SNAPSHOT` (D3)
- [ ] Optional, owner's call: publish from CI (GitHub secrets + a workflow on tags), CodeQL
- [ ] Exit check: the snapshot (then `1.4`) resolves from the Central Portal in a clean `~/.m2`; Orienteer's "Stage A delivered" item can be ticked for logger

## X — Cleanup, docs, known bugs & security

### Docs & cleanup
- [ ] README: prerequisites (JDK 21+ to run, `./mvnw` to build), Maven coordinates snippet, the Central Portal snapshots repository while `1.4` isn't released
- [ ] `.gitignore`: add `.DS_Store`, `.vscode/`
- [ ] Unused imports in `OLoggerEvent`, `OLoggerUtils`, `OLoggerBuilder`, `DefaultOLoggerConfiguration`, `DefaultOLoggerEventFactory`, `DefaultOLoggerEventDispatcher` — remove only when those files are touched for another reason
- [ ] Keep `AGENTS.md` current at the end of every stage

### Known bugs / tech debt
- [ ] Correlation IDs depend on the runtime JDK (open question above)
- [ ] `DefaultOLoggerEventDispatcher`: no timeouts (open question); for HTTP ≥ 400 it calls `getInputStream()`, which throws, so the collector's error body is never shown (`getErrorStream()`); the connection is never closed/disconnected; no `Content-Type` header
- [ ] Default charset: `event.toJson().getBytes()`, `new String(bytes)` and the digest input use the platform charset (UTF-8 on JDK 18+, so identical on 21/25 everywhere; only JDK 8 on Windows differed) → make UTF-8 explicit
- [ ] `new URL(String)` is deprecated since JDK 20 (compile warning only) → `URI.create(..).toURL()`; note that `URI` parsing is stricter (e.g. spaces), so treat it as a behavior change
- [ ] `OLogger.get()` lazily initializes a non-`volatile` static without synchronization (benign race: two default instances may be built)
- [ ] `OLoggerEvent.toJson()`: metadata keys can duplicate the fixed fields (`message`, `eventId`, …) and every value is written as a JSON string (also `dateTime`) — part of the wire format (D6): document, don't change without Orienteer
- [ ] `OLoggerBuilder.clearEnanhcers()` is misspelled (public API): keep it; optionally add a correctly spelled alias
- [ ] `OLoggerBuilder.setEnhancers(list)` keeps the caller's list, and `addEnhancer` then mutates it
- [ ] `DefaultOLoggerConfiguration.getApplicationName()` scans all thread stacks (`Thread.getAllStackTraces()`) on every call until it finds a `main` frame

### Security
- [ ] Events carry full stack traces and `toString()` of arbitrary objects (possible personal data) to the collector, over plain HTTP if so configured — document; no authentication header support (Orienteer's receiver is unauthenticated, Orienteer P10)

---

## Appendix A — Dependency versions (current → target)

"Latest" = latest stable on Maven Central, 2026-09-24/25.

| Dependency | Current | Latest seen | Target | Stage |
|---|---|---|---|---|
| JDK (`release`) | 1.8 (source/target) | 25 LTS (27 non-LTS) | 21, verified on 21 + 25 | A |
| JUnit 4 (test) | 4.13.1 | 4.13.2 (Jupiter 6.x) | 4.13.2 (D5) | A |
| Runtime dependencies | none | — | none (D4) | — |
| Lombok / Mockito / logging | not used | — | not added (D4) | — |
| Maven | 3.9.16 (Homebrew) | 3.9.16 | wrapper 3.9.16 | A |

## Appendix B — Maven plugins (current → target, versions as in wicket-orientdb)

| Plugin | Current | Target | Action (stage) |
|---|---|---|---|
| maven-compiler-plugin | 3.5.1 (source/target 1.8) | 3.16.0 | bump, `release` 21 (A) |
| maven-surefire-plugin | unpinned (Maven default 3.5.4) | 3.6.0 | pin (A) |
| maven-jar-plugin | unpinned (3.5.0) | 3.5.1 | pin (A) |
| maven-clean / resources / install / deploy | unpinned (3.2.0 / 3.4.0 / 3.1.4 / 3.1.4) | 3.5.0 / 3.5.0 / 3.2.0 / 3.2.0 | pin (A) |
| maven-dependency / versions / wrapper (helper goals) | unpinned / — / — | 3.11.0 / 2.22.0 / 3.3.4 | pin (A) |
| maven-enforcer-plugin | — | 3.6.3 | add (A) |
| maven-source / javadoc / gpg (release profile) | 3.0.0 / 2.10.4 / 1.6 | 3.4.0 / 3.12.0 / 3.2.8 | pin (A), wire up (R) |
| maven-release-plugin | 2.5.3 | 3.3.1 | bump (R) |
| nexus-staging-maven-plugin | 1.6.7 (OSSRH gone) | — | remove → central-publishing-maven-plugin 0.11.0 (R) |
| jacoco-maven-plugin | — | 0.8.15 | add, report only (R) |

## Appendix C — Test inventory

| Test class | `@Test` | `@Ignore` | Baseline JDK 21 / 25: run / skipped | Notes |
|---|---|---|---|---|
| `OLoggerTest` | 2 | 0 | 2 / 0 and 2 / 0, 0 failures | `testGenericOLogger` goes through the static default logger (prints to `System.err`); `testExceptionCorelation` checks correlation IDs with an in-memory dispatcher |
| `TestOLoggerEventDispatcher` | — | — | — | helper (in-memory dispatcher) |

## Appendix D — Stage B/C inventory (2026-09-25, `rg -l` over `src/`)

| Item | Files | Stage |
|---|---|---|
| `javax.servlet` / `javax.inject` / `com.google.inject` / `javax.mail` | 0 / 0 / 0 / 0 | C: nothing to do |
| `org.apache.wicket` (any), `WicketTesterScope`, `util.time.Duration`/`Time`, page store, `ModalWindow`, `org.danekja` | 0 | B: nothing to do |
| `javax.xml.bind`, `javax.activation`, `javax.annotation`, `javax.script`/Nashorn | 0 | A: nothing to do |
| `sun.*`, `setAccessible`, bytecode libraries (ASM, ByteBuddy, javassist, cglib) | 0 | A: nothing to do |
| `javax.net.ssl` (JDK module `java.base`, not renamed by jakarta) | 1 (`DefaultOLoggerEventDispatcher`, `HttpsURLConnection.HTTP_OK`) | — |
| Deprecated JDK API: `new URL(String)` | 1 (`DefaultOLoggerEventDispatcher`) | X (warning only) |

## Appendix E — Useful commands

```bash
./mvnw versions:display-dependency-updates versions:display-plugin-updates   # what's outdated
./mvnw dependency:tree                                                       # must show junit (test) only
./mvnw test -Dtest=OLoggerTest                                               # one test class
javap -v -cp target/classes org.orienteer.logger.OLogger | grep major        # 65 = Java 21
unzip -l target/logger-1.4-SNAPSHOT.jar                                      # what the jar ships
rg -l 'org\.apache\.wicket|javax\.(servlet|inject|mail)' src                 # Stage B/C check (expect nothing)
```

## Handoff to Orienteer

Written when Stage A completes (item A9).
