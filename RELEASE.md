# Release notes

This page outline changes for each release of the Merchant Integration SDK. 

## 1.0.1 (18 Mar, 2022)

This version includes bug fixes and changes:

- Fix bug on POS functions in Java SDK.
- Fix bug on request functions in Node SDK
- Add signature and language to headers of functions.
- Add Golang SDK
- Update parameters
    - Items: change from object to array of object
        - OnaChargeInit
        - OnaCreateWebUrl
- Update License(s) for SDKs

## 1.0.0 (12 Dec, 2021)

This initial version of the Merchant Integration SDK with supported features:

- Providing functions to help merchants for interacting with GrabPay backend APIs.
- Generating headers, which include: authorization, date, SDK version, country.
- Generating body request with given parameters.
- Correcting url, http method, parameters in request to GrabPay backend.

