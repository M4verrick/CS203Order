# Codex handoff — CS203Order

## Decision: CI/build approved; live release target unresolved
Reviewed 2026-09-19: complete root inventory and pom.xml (blob `37c911516f60ffd6662edab2dd3f25bdb48b8707`). No README or Actions directory was found. Maven project is Spring Boot 3.1.3, Java 17, JPA/H2/MySQL, OAuth resource server/client, Spring Security and Stripe Java. Do not assume a deployed AWS service merely from related CS203Event documentation.

## Implement
1. Record current main SHA. Inspect Maven wrapper files/checksums, application/test configuration and Event/auth/Stripe integration boundaries. The checked-in mvnw is mode 100644; invoke `bash mvnw -B verify` or deliberately fix its executable bit. Do not invent unavailable npm scripts or skip failing integration tests.
2. Add `.github/workflows/ci.yml` on PR/main with pinned checkout/setup-java, Java 17, read-only permissions, bounded timeout and cancellable concurrency. Run wrapper-based verification and upload JUnit reports without exposing secrets. Use H2 or disposable MySQL only as supported by actual tests, with synthetic OAuth/Stripe fixtures.
3. Test payment/auth failure behavior without live charges, provider credentials or production issuer requests. A CI profile must not weaken production authorization or bake test secrets into a release JAR. Inspect configuration and artifact contents for credentials before making artifacts available.
4. Produce a versioned JAR and optionally a container only after a deployment/runtime contract is established. Bind artifacts to the tested SHA and record checksums. Before any CD, identify the owner-approved host/service, database, issuer and Event service endpoints. No target was verified in this review.
5. Keep a future manual-release template outside active workflows until those prerequisites are approved. Future cloud deployment needs short-lived scoped OIDC credentials where supported, protected main/environment, successful exact-SHA CI, immutable image promotion, verified backup/migration compatibility, health/stabilization and a known-good rollback. No static AWS keys or GCP JSON keys.

## Acceptance
Run actual Maven tests/package checks, actionlint and source/artifact secret scan with redaction. Add tests for missing provider configuration, untrusted release SHA, failed database startup and auth/payment mocks. Record outstanding runtime/dependency issues rather than hiding failures. This is deployment-readiness work, not approval to expose an old payment service publicly.

Deliver CI/build/test changes and a target/prerequisite/rollback checklist on this branch. No merge, production workflow dispatch, cloud creation, credential access, schema mutation or real payment operation. Check GitHub-plan environment protection capabilities before later commissioning.

References: https://docs.github.com/en/actions/concepts/security/openid-connect
