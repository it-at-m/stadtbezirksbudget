# Release process

## Versioning

Version numbers are typically in the format `X.Y.Z` where each component has specific meaning:

- Major (X): This number changes when there are significant updates to the software that often include major new features, substantial improvements, or changes that may not be backward-compatible.
- Minor (Y): This number is incremented when new features are added in a way that is backward-compatible. Minor updates enhance the software with additional functionality or improvements without altering the core architecture.
- Patch (Z): This number increases when small changes are made, typically to fix bugs, address security issues, or make minor improvements that do not add new features.

## Release images

### 1. Create release branch

- Open [branches](https://github.com/it-at-m/stadtbezirksbudget/branches) of the repository
- Click "New branch"
- Enter branch name `release/vX.Y.Z` (see [versioning](#versioning) for details on the version number)
- Click "Create new branch"

### 2. Maven release

- Open [maven release workflow](https://github.com/it-at-m/stadtbezirksbudget/actions/workflows/maven-release.yml) of the repository
- Expand "Run workflow" by clicking on it
- Select your release branch
- Enter your version number in "Version to use when preparing a release (e.g., 1.2.3)" (only dots and digits)
- Enter the same version number with patch-value incremented by 1 in "Version to use for new local working copy (e.g., 1.2.4-SNAPSHOT)" and attach `-SNAPSHOT`
- Select the service in "Service-Name"-field that you want to release
- Click on "Run workflow"
- Wait for workflow to finish
- For reference, here is an example with release-Branch `release/v0.5.0`, version number `0.5.0`, therefore "Version to use for new local working copy" is `0.5.1-SNAPSHOT`, for service `stadtbezirksbudget-backend`:
  ![example of Maven Release](exampleMavenRelease.png)

### 3. NPM release

- Open [npm release workflow](https://github.com/it-at-m/stadtbezirksbudget/actions/workflows/npm-release.yml) of the repository
- Expand "Run workflow" by clicking on it
- Select your release branch
- Select your desired option in "Select version increment type (follow Semantic Versioning)"
- Select the service in "Select the node service to release"-field that you want to release
- Make sure to tick the box of "skip deployment to npm registry" if you dont want to deploy to npm registry.
- Click on "Run workflow"
- Wait for workflow to finish
- For reference, here is an example with release-Branch `release/v0.5.0`, version increment type `minor` for service `stadtbezirksbudget-frontend`:
  ![example of Npm Release](exampleNpmRelease.png)

### 4. Merge release branch

- Open [compare view](https://github.com/it-at-m/stadtbezirksbudget/compare) of the repository
- Click on "compare"-field and select your release branch
- Click on "Create pull request" or "View pull request"
- Enter a valid title, for example `🔖 Release vX.Y.Z`
- Optionally enter more information into pull-request
- Click "Create pull request"
- Wait for this pull request to be approved
- After approval, merge this pull request
- For reference, here is an example with release-Branch `release/v0.5.0`:
  ![example of create/view pull request view](exampleMergeRequest.png)

### 5. Additional information

Depending on your company infrastructure, there might be additional steps to take such as editing version-numbers or -tags in other connected repositories and merging these changes by pull-request. Also depending on pipeline settings there may be extra steps to the release process.
