# orienteer-logger

Tiny, dependency-free Java library that turns objects and exceptions into `OLoggerEvent`s (message, correlation ID,
application, node, metadata) and dispatches them — by default as JSON, POSTed over HTTP to a collector URL.
It is the **client side** of Orienteer's incident logger: the [Orienteer](https://github.com/OrienteerBAP/Orienteer)
module `orienteer-logger-server` (local checkout `~/Development/Orienteer`) embeds it and also receives its events.
Modernized in lockstep with Orienteer; dormant from Dec 2020 to Sep 2026.
**Read [`REFRESH_PLAN.md`](REFRESH_PLAN.md) before touching dependencies, build or infra; tick items off there in the same commit.**

## Contract with Orienteer — don't break it

- Coordinates `org.orienteer:logger` (one jar: no parent pom, no test-jar), version **`1.4-SNAPSHOT`**.
  Never change groupId/artifactId/version without the owner (plan D2/D3).
- `orienteer-logger-server` uses `OLogger`, `OLoggerBuilder`, `OLoggerEvent` (`getSeed`, `setMetaData`, `toJson`), all
  `IO*` interfaces and `impl.DefaultOLoggerConfiguration`. It **subclasses** `impl.DefaultOLoggerEventDispatcher`
  (protected `collectorUrl`, `syslog(..)`) and `impl.DefaultCorrelationIdGenerator` (protected `generateDigest*`):
  protected members are API too.
- **Wire format:** `OLoggerEvent.toJson()` is POSTed to the collector and parsed by Orienteer with `ODocument.fromJSON`;
  keep its field names and shape. **Class names are persisted:** Orienteer stores
  `org.orienteer.logger.impl.DefaultCorrelationIdGenerator` in its database — never rename or move public classes.
- Stages: A → Orienteer P3 (JDK 21). B/C (Wicket 9/10, jakarta) need nothing here: no servlet/Wicket/inject/mail code.

## Stack

- Java: `--release 21` (`maven.compiler.release`), built and tested on JDK 21 **and** 25. Plain JDK APIs only
  (`HttpURLConnection`, `MessageDigest`, `ManagementFactory`).
- **No runtime dependencies** — keep it that way (README promise, plan D4). No Lombok, no logging framework.
- Tests: JUnit 4 only. Build: Maven 3.9.16; Temurin 21.0.11 default, Temurin 25.0.4 for verification.

## Build status — read first

- **Stage A done (2026-09-25):** `./mvnw clean verify` is green on Temurin 21 and 25 (4 tests), no `--add-opens`.
  `master` = Stage A = `1.4-SNAPSHOT`, which Orienteer builds against. **Never install or publish later work as
  `1.4-SNAPSHOT`** (plan D3): `1.4` is released from Stage A after Orienteer P3 is green, then `master` → `1.5-SNAPSHOT`.
- Stages B/C need no code here (no Wicket/servlet/inject/mail); re-check with the searches in plan sections B/C.
- Use `./mvnw` (Maven 3.9.16, script-only wrapper). Enforcer requires Maven ≥ 3.9 and JDK ≥ 21; `.sdkmanrc` pins
  Temurin 21.0.11. Plugin versions live only in `<pluginManagement>`.
- CI: `.github/workflows/ci.yml` (push + PR, JDK 21/25, surefire + JaCoCo reports); Dependabot: `.github/dependabot.yml`.
- Publishing: `central-publishing-maven-plugin` (server id `${central.server.id}`, default `central`): `-SNAPSHOT`s to the
  Central Portal snapshots repository, releases as a portal bundle; runbook in plan phase R. `1.4-SNAPSHOT` is
  published there (2026-09-28; snapshots expire after 90 days). **Only the owner deploys, releases, pushes or tags** (D11). OSSRH and jcenter are dead.

## Commands

```bash
./mvnw clean verify                                                 # build + tests
./mvnw install                                                      # jar + pom into ~/.m2 for Orienteer
./mvnw test -Dtest=OLoggerTest                                      # one test class
JAVA_HOME=~/.sdkman/candidates/java/25.0.4-tem ./mvnw clean verify  # JDK 25 check
./mvnw -Prelease -Dgpg.skip verify                                  # + sources/javadoc jars; never `deploy` (owner only)
```

## Layout and key classes

- `org.orienteer.logger`:
  - `OLogger`: entry point. Static `log(..)` uses the default instance (`get()`/`set()`); `logEvent` runs
    factory → enhancers → dispatcher. `OLoggerBuilder` assembles one and hands the configuration to every part
    implementing `IOLoggerConfigurable`.
  - `OLoggerEvent`: the event, plus `toJson()`. `OLoggerUtils`: JSON escaping, stream copy.
  - Interfaces: `IOLoggerEventFactory`, `IOLoggerEventEnhancer`, `IOLoggerEventDispatcher`, `IOLoggerConfiguration`,
    `IOLoggerConfigurable`, `IOCorrelationIdGenerator`.
- `org.orienteer.logger.impl`:
  - `DefaultOLoggerConfiguration`: copies the `ologger.*` system properties (`application`, `nodeId`, `collectorUrl`).
  - `DefaultOLoggerEventFactory`: message from the stack trace or `toString()`, plus the correlation ID.
  - `DefaultCorrelationIdGenerator`: SHA-256 over the root cause's stack trace, or over class name + `toString()`.
  - `DefaultOLoggerEventDispatcher`: POSTs `toJson()` to `collectorUrl`; without one, prints to `System.err`.
- `src/test`: `OLoggerTest`, and `TestOLoggerEventDispatcher` (in-memory dispatcher).

## Conventions

- Tabs in most files, but `DefaultCorrelationIdGenerator` and `IOCorrelationIdGenerator` use 4 spaces and
  `OLoggerUtils` is mixed: match the surrounding file. No reformat-only diffs, no import cleanups in untouched files.
- Interfaces start with `IO` (`IOLogger…`), classes with `O`.
- Keep public/protected signatures stable. If a change is unavoidable, list it in the plan's "Handoff to Orienteer".
- Diagnostics go to `System.err` by design (no logging framework in the library).

## Testing

- Plain unit tests: no network, no ports. `TestOLoggerEventDispatcher` captures events in memory.
- Static `OLogger.log(..)` uses the default dispatcher, which prints to `System.err`. Never set `ologger.collectorUrl`
  in tests, and reset the default instance (`OLogger.set(null)`) if a test replaces it.
- Correlation-ID tests rely on exceptions created on the same source line (the stack trace is hashed): keep that.

## Pitfalls

- Exception correlation IDs hash `StackTraceElement.toString()`, which contains JDK module names and JDK line numbers:
  the same exception gets a different ID on a different runtime JDK, measured 21 vs 25 (plan open question).
- `OLogger.log(obj, source, metaData)`: source/metadata are applied by `DefaultOLoggerEventFactory` since Stage A (D9).
- `DefaultOLoggerEventDispatcher` has no timeouts and runs on the caller's thread (in Orienteer: the request thread).
- `new DefaultOLoggerConfiguration()` copies **all** `ologger.*` system properties at construction time.
