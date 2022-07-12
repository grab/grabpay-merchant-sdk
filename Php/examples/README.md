# Sample PHP code for GP PHP SDK

# Starting Development Server

```bash
cd ~/merchant/Php
php -S localhost:8080 -t .
```

# Usage

1. Login to Grab Developer Platform on [Staging](https://developer-beta.stg-myteksi.com) or [Production](https://developer.grab.com).
2. You will need to whitelist the redirect URL, `http://localhost:8080/**`, on the Grab Developer Platform.
3. Copy `config-sample.php` to `config.php`.
4. From Grab Developer Platform, fill in your `Partner ID`, `Partner Secret`, and `Merchant ID`. `Client ID` and `Client Secret` are applicable for Online merchants, while `Terminal ID` is only applicable for Offline merchants.
5. For Online One-Time Charge (OTC), your url will be `http://localhost:8080/examples/online/otc.php`, and your redirect_uri will be `http://localhost:8080/examples/online/otc_complete.php`.
6. For Offline Merchant Present QR (MPQR), your sample HTML page with the QR Code will be `http://localhost:8080/examples/offline/mpqr.php`.
7. For Offline Consumer Present QR (CPQR), your url will be `http://localhost:8080/examples/offline/cpqr.php`.
