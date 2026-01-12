# OpenAPI Contract Validation

## Summary
Provide a validation feature that verifies Spring Cloud Contract DSL contracts against a supplied OpenAPI 3.x
specification. The validation must be available through a static `main` entry point and a JUnit 5 extension.

## Inputs
- OpenAPI specification file path (YAML or JSON).
- Contracts directory path (recursively scanned for SCC DSL contracts: Groovy, Java, YAML).

## Behavior
- For every contract, verify:
  - The request path matches an OpenAPI path (path parameters are treated as wildcards).
  - The HTTP method is defined for the matching OpenAPI path.
  - The response status is defined for that OpenAPI operation.
- Ignore contracts marked as `ignored` or `inProgress`.
- Collect all violations and report them together; do not fail on the first mismatch.

## Outputs
- A report summarizing all violations (contract file + contract name + reason).
- Success message when no violations are found.
- CLI exit code is non-zero when violations are present.

## JUnit 5 Extension
- Allow configuration of the OpenAPI spec path and contracts directory.
- Run validation once per test class.
- Fail the test suite with a report if violations are found.

## Test Coverage Requirements
- Validate OpenAPI specs provided as YAML and JSON files.
- Validate SCC contracts provided as YAML, Groovy, and Java DSL.
- Cover the full matrix of (OpenAPI format x contract format) for both valid and invalid combinations.
