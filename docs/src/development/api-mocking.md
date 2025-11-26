# API Mocking

## Software under consideration

- [Prism](https://github.com/stoplightio/prism)
- [Imposter](https://www.imposter.sh/)
- [Mock Server](https://www.mock-server.com/)
- [SoapUI](https://www.soapui.org/tools/soapui/)
- [WireMock](https://github.com/wiremock/wiremock)

## Criteria

- Compatible with OpenAPI 3.1
- Validation of Requests/Responses
- Compatible with multipart requests
- Dummy Data in Response
- Open Source
- Docker Container with OpenAPI Support

## Comparison

|                                       | Prism | Imposter | Mock Server | SoapUI | WireMock |
| ------------------------------------- | ----- | -------- | ----------- | ------ | -------- |
| OpenAPI 3.1                           | ✅    | ✅       | ✅          | ❌     | ✅       |
| Multipart Requests                    | ❌    | ✅       | ✅          | ✅     | ✅       |
| Dummy Data Response                   | ✅    | ✅       | ✅          | ✅     | ✅       |
| Open Source                           | ✅    | ✅       | ✅          | ❌     | ✅       |
| Validation                            | ✅    | ✅       | ✅          | ✅     | ✅       |
| Docker Container with OpenAPI Support | ✅    | ✅       | ✅          | ❌     | ❌       |

:::info
For SoapUI, only the free version has been considered.
:::

## Result

We have decided to use Imposter due to its ease of setup and its ability to seamlessly import the OpenAPI specification.
