# GrabPay Merchant Integration SDK

[Documentations](https://developer.grab.com/docs/) | [Developer Portal](https://developer.grab.com/)

The GrabPay Merchant Integration SDK provides the adapters written in Go, PHP, Node.js, Python, Java and .NET, which can be used to integrate payment functionality with GrabPay APIs.

The official document for GrabPay:

- [One-Time Charge (OTC)](https://developer.grab.com/docs/paysi-agent-partner-v2-otc/)
- [Point of Sale (POS)](https://developer.grab.com/docs/paysi-partner/)

## Supported Functionality

For each function, we support merchant to generate headers, which include: authorization, date, SDK version, country. We also help to correct path, http method, parameters in request to GrabPay backend.

The SDK supports 2 types of payment:

### One-Time Charge (OTC)

| Function             | Description                                               |
| :------------------- | :-------------------------------------------------------- |
| `onaChargeInit`      | To init new transaction                                   |
| `onaCreateWebUrl`    | To generate web URL from API charge init                  |
| `onaOAuth2Token`     | To get the access_token for using later                   |
| `onaChargeComplete`  | To complete transaction                                   |
| `onaGetChargeStatus` | To get status of the transaction, which already completed |
| `onaRefund`          | To refund the amount of a specific success transaction    |
| `onaGetRefundStatus` | To check the status of the refund transaction             |
| `onaGetOTCStatus`    | To get the OAuthCode after user's payment success on IOS  |

### Point of Sale (POS)

| Function              | Description                                            |
| :-------------------- | :----------------------------------------------------- |
| `posCreateQRCode`     | To create new QR code                                  |
| `posPerformQRCode`    | To support payment from customer's QR code             |
| `posCancel`           | To cancel transaction, do not pay                      |
| `posRefund`           | To refund the amount of a specific success transaction |
| `posGetTxnDetails`    | To check the status of the specific transaction        |
| `posGetRefundDetails` | To check the status of the refund transaction          |

## Usage

The SDK is provided in several programming languages. You can read more about each SDK package for the programming language of your interest at:

- [Go](Go/README.md)
- [PHP](Php/README.md)
- [Node](Node/README.md)
- [Python 3](Python/README.md)
- [Java](Java/README.md)
- [Net](Net/README.md)

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
