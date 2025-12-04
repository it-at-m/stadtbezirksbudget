# API Mocking

## Software under consideration

- [Prism](https://stoplight.io/open-source/prism)
- [Imposter](https://www.imposter.sh/)
- [Mock Server](https://www.mock-server.com/)
- [SoapUI](https://www.soapui.org/tools/soapui/)
- [WireMock](https://wiremock.org/)

## Criteria

- Compatible with OpenAPI 3.1
- Validation of Requests/Responses
- Compatible with multipart requests
- Dummy Data in Response
- Open Source
- Docker Container with OpenAPI Support

## Comparison

<!-- markdownlint-disable MD060 -->

|                                       | Prism | Imposter | Mock Server | SoapUI | WireMock |
| ------------------------------------- | ----- | -------- | ----------- | ------ | -------- |
| OpenAPI 3.1                           | ✅    | ✅       | ✅          | ❌     | ✅       |
| Multipart Requests                    | ❌    | ✅       | ✅          | ✅     | ✅       |
| Dummy Data Response                   | ✅    | ✅       | ✅          | ✅     | ✅       |
| Open Source                           | ✅    | ✅       | ✅          | ✅     | ✅       |
| Validation                            | ✅    | ✅       | ✅          | ✅     | ❌       |
| Docker Container with OpenAPI Support | ✅    | ✅       | ✅          | ❌     | ❌       |

<!-- markdownlint-enable MD060 -->

:::info
For SoapUI, only the free version has been considered.
:::

## Decision

We chose Imposter for this project. Short rationale:

- Works better as a standalone mock server.
- Easy import of OpenAPI specifications (low friction for keeping mocks in sync with the API contract).
- Good multipart / file-upload handling which we need for certain endpoints.
- Built-in validation and generated dummy responses for quick developer feedback.
- Simple containerized operation which fits local development and CI workflows.
