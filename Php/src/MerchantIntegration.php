<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

/**
 * Class MerchantIntegration
 */
abstract class MerchantIntegration
{
    /**
     * Constant for online payment aka ONA
     *
     * @var string
     */
    public const TYPE_ONLINE = 'ONLINE';

    /**
     * Constant for offline payment aka POS
     *
     * @var string
     */
    public const TYPE_OFFLINE = 'OFFLINE';

    /**
     * Constant for staging (STG)
     *
     * @var string
     */
    public const STAGING = 'STG';

    /**
     * Constant for production (PRD)
     *
     * @var string
     */
    public const PRODUCTION = 'PRD';

    /**
     * Constant for Singapore (SG)
     *
     * @var string
     */
    public const SG = 'SG';

    /**
     * Constant for Malaysia (MY)
     *
     * @var string
     */
    public const MY = 'MY';

    /**
     * Constant for Thailand (TH)
     *
     * @var string
     */
    public const TH = 'TH';

    /**
     * Constant for Philippines (PH)
     *
     * @var string
     */
    public const PH = 'PH';

    /**
     * Constant for Vietnam (VN)
     *
     * @var string
     */
    public const VN = 'VN';

    /**
     * Constant for Indonesia (ID)
     *
     * @var string
     */
    public const ID = 'ID';

    /**
     * Constant for Myanmar (MM)
     *
     * @var string
     */
    public const MM = 'MM';

    /**
     * Constant for Cambodia (KH)
     *
     * @var string
     */
    public const KH = 'KH';

    /**
     * Constant for Singapore Dollars (SGD)
     *
     * @var string
     */
    public const SGD = 'SGD';

    /**
     * Constant for Malaysia Ringgit (MYR)
     *
     * @var string
     */
    public const MYR = 'MYR';

    /**
     * Constant for Thailand Baht (THB)
     *
     * @var string
     */
    public const THB = 'THB';

    /**
     * Constant for Philippines Peso (PHP)
     *
     * @var string
     */
    public const PHP = 'PHP';

    /**
     * Constant for Vietnam Dong (VND)
     *
     * @var string
     */
    public const VND = 'VND';

    /**
     * Constant for Indonesia Rupiah (IDR)
     *
     * @var string
     */
    public const IDR = 'IDR';

    /**
     * Constant for Myanmar Kyat (MMK)
     *
     * @var string
     */
    public const MMK = 'MMK';

    /**
     * Constant for Cambodia Riel (KHR)
     *
     * @var string
     */
    public const KHR = 'KHR';

    /**
     * Constant for header application/json
     *
     * @var string
     */
    public const HEADER_APPLICATION_JSON = 'application/json';

    /**
     * Constant for header application/x-www-form-urlencoded
     *
     * @var string
     */
    public const HEADER_APPLICATION_FORM = 'application/x-www-form-urlencoded';

    /**
     * Constant for method GET
     *
     * @var string
     */
    public const METHOD_GET = 'GET';

    /**
     * Constant for method POST
     *
     * @var string
     */
    public const METHOD_POST = 'POST';

    /**
     * Constant for method PUT
     *
     * @var string
     */
    public const METHOD_PUT = 'PUT';

    /**
     * Constant for SDK language
     *
     * @var string
     */
    public const SDK_LANGUAGE = 'PHP';

    /**
     * Constant for SDK version
     *
     * @var string
     */
    public const SDK_VERSION = '2.0.0';

    /**
     * Constant for SDK signature
     *
     * @var string
     */
    public const SDK_SIGNATURE = '6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7';

    /**
     * Constant for Regional Production API URL
     *
     * @var string
     */
    protected const REGIONAL_PRD_API_URL = 'https://partner-api.grab.com';

    /**
     * Constant for Moca Production API URL
     *
     * @var string
     */
    protected const MOCA_PRD_API_URL = 'https://partner-gw.moca.vn';

    /**
     * Constant for Regional Staging API URL
     *
     * @var string
     */
    protected const REGIONAL_STG_API_URL = 'https://partner-api.stg-myteksi.com';

    /**
     * Constant for Moca Staging API URL
     *
     * @var string
     */
    protected const MOCA_STG_API_URL = 'https://stg-paysi.moca.vn';

    /**
     * Constant for GrabID v1 oauth2 token path.
     *
     * @var string
     */
    protected const GRABID_V1_OAUTH2_TOKEN = '/grabid/v1/oauth2/token';

    /**
     * Constant for GrabID v1 oauth2 authorize path.
     *
     * @var string
     */
    protected const GRABID_V1_OAUTH2_AUTHORIZE = '/grabid/v1/oauth2/authorize';

    /**
     * Constant for Moca partners v1 path.  Note that partner is with s.
     *
     * @var string
     */
    protected const MOCA_PARTNERS_V1_PATH = '/mocapay/partners/v1';

    /**
     * Constant for Moca partner v2 Path.
     *
     * @var string
     */
    protected const MOCA_PARTNER_V2_PATH = '/mocapay/partner/v2';

    /**
     * Constant for Regional partner v1 path.
     *
     * @var string
     */
    protected const REGIONAL_PARTNER_V1_PATH = '/grabpay/partner/v1';

    /**
     * Constant for Regional partner v2 path.
     *
     * @var string
     */
    protected const REGIONAL_PARTNER_V2_PATH = '/grabpay/partner/v2';

    /**
     * Environment. Can be either "stg" or "prd".
     */
    protected string $environment;

    /**
     * Country code (alpha-2).
     */
    protected string $country;

    /**
     * Partner ID.
     */
    protected string $partnerID;

    /**
     * Partner secret.
     */
    protected string $partnerSecret;

    /**
     * Merchant ID.
     */
    protected string $merchantID;

    /**
     * Terminal ID.
     */
    protected string $terminalID = '';

    /**
     * Client ID.
     */
    protected string $clientID = '';

    /**
     * Client secret.
     */
    protected string $clientSecret = '';

    /**
     * Redirect URL.
     */
    protected string $redirectUrl = '';

    /**
     * MerchantIntegration constructor.
     *
     * @param string $environment Environment
     * @param string $country Country
     * @param string $partnerID Partner ID
     * @param string $partnerSecret Partner Secret
     * @param string $merchantID Merchant ID
     */
    public function __construct(string $environment, string $country, string $partnerID, string $partnerSecret, string $merchantID)
    {
        $this->environment = strtoupper($environment);
        $this->country = strtoupper($country);
        $this->partnerID = $partnerID;
        $this->partnerSecret = $partnerSecret;
        $this->merchantID = $merchantID;
    }

    /**
     * Generate random string.
     *
     * @param int $length Length
     */
    public static function generateRandomString(int $length = 32): string
    {
        try {
            $bytesLength = (int) ($length / 2);

            return bin2hex(random_bytes($bytesLength));
        } catch (\Exception $ex) {
            // This should not happen
            // @codeCoverageIgnoreStart
            return '';
            // @codeCoverageIgnoreEnd
        }
    }

    /**
     * Requires a special case of base64encode for URLs.
     *
     * @param string $url URL
     */
    public static function base64UrlEncode(string $url): string
    {
        return str_replace(['=', '+', '/'], ['', '-', '_'], base64_encode($url));
    }

    /**
     * Generate HMAC signature for authentication with API.
     *
     * @param string $partnerSecret Partner secret
     * @param string $requestMethod Request method
     * @param string $apiUrl Request URL
     * @param string $contentType Content-Type header
     * @param array $requestBody Request body
     * @param string $date Date header
     */
    public static function generateHmacSignature(string $partnerSecret, string $requestMethod, string $apiUrl, string $contentType, array $requestBody, string $date): string
    {
        $hashedPayload = '';
        if (! empty($requestBody)) {
            $hashedPayload = base64_encode(hash('sha256', json_encode($requestBody), true));
        }

        $content = '';
        $content .= $requestMethod;
        $content .= "\n";
        $content .= $contentType;
        $content .= "\n";
        $content .= $date;
        $content .= "\n";
        $content .= $apiUrl;
        $content .= "\n";
        $content .= $hashedPayload;
        $content .= "\n";

        return base64_encode(hash_hmac('sha256', $content, $partnerSecret, true));
    }

    /**
     * Generate POP Signature for authentication with API via X-GID-AUX-POP header.
     *
     * @param string $clientSecret Client secret
     * @param string $accessToken Access token
     * @param string $date GMT date
     */
    public static function generatePopSignature(string $clientSecret, string $accessToken, string $date): string
    {
        $timestamp = strtotime($date);
        $message = $timestamp . $accessToken;
        $signature = hash_hmac('sha256', $message, $clientSecret, true);
        $payload = [
            'time_since_epoch' => $timestamp,
            'sig'              => self::base64UrlEncode($signature),
        ];

        return self::base64UrlEncode(json_encode($payload));
    }

    /**
     * Print out partner information.
     */
    public function getPartnerInfo(): array
    {
        $partnerInfo = [
            'sdkVersion'            => self::SDK_VERSION,
            'sdkSignature'          => self::SDK_SIGNATURE,
            'environment'           => $this->getEnvironment(),
            'partnerID'             => $this->getPartnerID(),
            'partnerSecret'         => $this->getPartnerSecret(),
            'merchantID'            => $this->getMerchantID(),
            'terminalID'            => $this->getTerminalID(),
            'clientID'              => $this->getClientID(),
            'clientSecret'          => $this->getClientSecret(),
            'country'               => $this->getCountry(),
            'url'                   => $this->getApiUrl(),
            'chargeInit'            => '',
            'OAuth2Token'           => '',
            'chargeComplete'        => '',
            'onaChargeStatus'       => '',
            'onaRefundTxn'          => '',
            'onaCheckRefundTxn'     => '',
            'oneTimeChargeStatus'   => '',
            'createQrCode'          => '',
            'cancelQrTxn'           => '',
            'posRefundTxn'          => '',
            'performTxn'            => '',
            'posChargeStatus'       => '',
            'posChargeRefundStatus' => '',
        ];

        if ($this->isCountryVietnam()) {
            // online path
            $partnerInfo['chargeInit'] = self::MOCA_PARTNER_V2_PATH . '/charge/init';
            $partnerInfo['OAuth2Token'] = self::GRABID_V1_OAUTH2_TOKEN;
            $partnerInfo['chargeComplete'] = self::MOCA_PARTNER_V2_PATH . '/charge/complete';
            $partnerInfo['onaChargeStatus'] = self::MOCA_PARTNER_V2_PATH . '/charge/{partnerTxID}/status?currency={currency}';
            $partnerInfo['onaRefundTxn'] = self::MOCA_PARTNER_V2_PATH . '/refund';
            $partnerInfo['onaCheckRefundTxn'] = self::MOCA_PARTNER_V2_PATH . '/refund/{refundPartnerTxID}/status?currency={currency}';
            $partnerInfo['oneTimeChargeStatus'] = self::MOCA_PARTNER_V2_PATH . '/one-time-charge/{partnerTxID}/status?currency={currency}';
            // offline path
            $partnerInfo['createQrCode'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/qrcode/create';
            $partnerInfo['cancelQrTxn'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{origPartnerTxID}/cancel';
            $partnerInfo['posRefundTxn'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{origPartnerTxID}/refund';
            $partnerInfo['performTxn'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/perform';
            $partnerInfo['posChargeStatus'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType=P2M';
            $partnerInfo['posChargeRefundStatus'] = self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{refundPartnerTxID}?currency={currency}&msgID={msgID}&txType=Refund';
        } else {
            // online path
            $partnerInfo['chargeInit'] = self::REGIONAL_PARTNER_V2_PATH . '/charge/init';
            $partnerInfo['OAuth2Token'] = self::GRABID_V1_OAUTH2_TOKEN;
            $partnerInfo['chargeComplete'] = self::REGIONAL_PARTNER_V2_PATH . '/charge/complete';
            $partnerInfo['onaChargeStatus'] = self::REGIONAL_PARTNER_V2_PATH . '/charge/{partnerTxID}/status?currency={currency}';
            $partnerInfo['onaRefundTxn'] = self::REGIONAL_PARTNER_V2_PATH . '/refund';
            $partnerInfo['onaCheckRefundTxn'] = self::REGIONAL_PARTNER_V2_PATH . '/refund/{refundPartnerTxID}/status?currency={currency}';
            $partnerInfo['oneTimeChargeStatus'] = self::REGIONAL_PARTNER_V2_PATH . '/one-time-charge/{partnerTxID}/status?currency={currency}';
            // offline path
            $partnerInfo['createQrCode'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/qrcode/create';
            $partnerInfo['cancelQrTxn'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{origPartnerTxID}/cancel';
            $partnerInfo['posRefundTxn'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{origPartnerTxID}/refund';
            $partnerInfo['performTxn'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/perform';
            $partnerInfo['posChargeStatus'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType=P2M';
            $partnerInfo['posChargeRefundStatus'] = self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{refundPartnerTxID}?currency={currency}&msgID={msgID}&txType=Refund';
        }

        return $partnerInfo;
    }

    /**
     * Get Environment.
     */
    protected function getEnvironment(): string
    {
        return $this->environment;
    }

    /**
     * Is environment staging?
     * This is not being used, so adding codeCoverageIgnore for now.
     *
     * @codeCoverageIgnore
     */
    protected function isEnvironmentStaging(): bool
    {
        return $this->environment === self::STAGING;
    }

    /**
     * Is environment production?
     */
    protected function isEnvironmentProduction(): bool
    {
        return $this->environment === self::PRODUCTION;
    }

    /**
     * Get Country.
     */
    protected function getCountry(): string
    {
        return $this->country;
    }

    /**
     * Get Is country Vietnam?
     */
    protected function isCountryVietnam(): bool
    {
        return $this->country === self::VN;
    }

    /**
     * Get Partner ID.
     */
    protected function getPartnerID(): string
    {
        return $this->partnerID;
    }

    /**
     * Get Partner Secret.
     */
    protected function getPartnerSecret(): string
    {
        return $this->partnerSecret;
    }

    /**
     * Get Merchant ID.
     */
    protected function getMerchantID(): string
    {
        return $this->merchantID;
    }

    /**
     * Get Terminal ID.
     */
    protected function getTerminalID(): string
    {
        return $this->terminalID;
    }

    /**
     * Get Client ID.
     */
    protected function getClientID(): string
    {
        return $this->clientID;
    }

    /**
     * Get Client Secret.
     */
    protected function getClientSecret(): string
    {
        return $this->clientSecret;
    }

    /**
     * Get Redirect URL.
     */
    protected function getRedirectUrl(): string
    {
        return $this->redirectUrl;
    }

    /**
     * Get API URL depending on region and environment.
     */
    protected function getApiUrl(): string
    {
        if ($this->isEnvironmentProduction()) {
            return $this->isCountryVietnam() ? self::MOCA_PRD_API_URL : self::REGIONAL_PRD_API_URL;
        }

        return $this->isCountryVietnam() ? self::MOCA_STG_API_URL : self::REGIONAL_STG_API_URL;
    }

    /**
     * Handle general exception
     */
    protected function handleException(\Exception $ex): Response
    {
        return new Response(0, [], 'Caught exception: ' . $ex->getMessage());
    }

    /**
     * Prepare request API path.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     * @param string $apiPath API path
     * @param string $type Type, ONLINE or OFFLINE
     */
    protected function prepareRequestPath(string $requestMethod, string $apiPath, string $type): string
    {
        if ($type === self::TYPE_ONLINE) {
            return $apiPath;
        }

        // We only need to modify OFFLINE apiURL
        $credentials = [
            'grabID'     => $this->getMerchantID(),
            'terminalID' => $this->getTerminalID(),
        ];

        if ($requestMethod === self::METHOD_GET) {
            if (strpos($apiPath, '?') !== false) {
                $apiPath .= '&';
            } else {
                // So far, all internal GET URLs have query params in them already
                // @codeCoverageIgnoreStart
                $apiPath .= '?';
                // @codeCoverageIgnoreEnd
            }
            $apiPath .= urldecode(http_build_query($credentials));
        }

        return $apiPath;
    }

    /**
     * Prepare request headers.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $type Type, ONLINE or OFFLINE
     * @param string $accessToken Access token
     */
    protected function prepareRequestHeaders(string $requestMethod, string $apiPath, array $requestBody, string $type, string $accessToken): array
    {
        $env = $this->getPartnerInfo();

        // Determine Content-Type
        $contentType = $requestMethod === self::METHOD_GET ? self::HEADER_APPLICATION_FORM : self::HEADER_APPLICATION_JSON;

        $headers = [
            'Accept'          => self::HEADER_APPLICATION_JSON,
            'X-Request-ID'    => self::generateRandomString(),
            'Content-Type'    => $contentType,
            'X-Sdk-Country'   => $this->getCountry(),
            'X-Sdk-Version'   => self::SDK_VERSION,
            'X-Sdk-Language'  => self::SDK_LANGUAGE,
            'X-Sdk-Signature' => self::SDK_SIGNATURE,
        ];

        // Set header for API OAuth token
        if ($apiPath === $env['OAuth2Token']) {
            return $headers;
        }

        // Get current date and time
        $now = new \DateTime('now', new \DateTimeZone('UTC'));
        $gmtTime = $now->format(\DateTime::RFC7231);

        // Generate HMAC signature
        $hmac = self::generateHmacSignature($this->getPartnerSecret(), $requestMethod, $apiPath, $contentType, $requestBody, $gmtTime);

        // Online
        if ($type === self::TYPE_ONLINE && $apiPath !== $env['chargeInit'] && ! str_contains($apiPath, 'one-time-charge')) {
            return array_merge($headers, [
                'Date'          => $gmtTime,
                'X-GID-AUX-POP' => self::generatePopSignature($this->getClientSecret(), $accessToken, $gmtTime),
                'Authorization' => 'Bearer ' . $accessToken,
            ]);
        }

        // Offline
        return array_merge($headers, [
            'Date'          => $gmtTime,
            'Authorization' => ($this->getPartnerID() . ':' . $hmac),
        ]);
    }

    /**
     * Prepare request body.
     *
     * @param array $requestBody Request body
     * @param string $type Type, ONLINE or OFFLINE
     */
    protected function prepareRequestBody(array $requestBody, string $type): array
    {
        if ($type === self::TYPE_ONLINE) {
            return $requestBody;
        }

        $credentials = [
            'grabID'     => $this->getMerchantID(),
            'terminalID' => $this->getTerminalID(),
        ];

        return array_merge($requestBody, $credentials);
    }

    /**
     * Wrapper for sending GET request.
     *
     * @param string $apiPath API path
     * @param string $type Type, ONLINE or OFFLINE
     * @param string $accessToken Access token
     */
    protected function sendGetRequest(string $apiPath, string $type, string $accessToken = ''): Response
    {
        $requestPath = $this->prepareRequestPath(self::METHOD_GET, $apiPath, $type);
        $requestUrl = $this->getApiUrl() . $requestPath;
        $requestHeaders = $this->prepareRequestHeaders(self::METHOD_GET, $requestPath, [], $type, $accessToken);

        $response = RestClient::sendRequest(self::METHOD_GET, $requestUrl, $requestHeaders, []);

        return new Response($response->code, $response->headers, $response->body);
    }

    /**
     * Wrapper for sending POST request.
     *
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $type Type
     * @param string $accessToken Access token
     */
    protected function sendPostRequest(string $apiPath, array $requestBody, string $type, string $accessToken = ''): Response
    {
        $requestPath = $this->prepareRequestPath(self::METHOD_POST, $apiPath, $type);
        $requestUrl = $this->getApiUrl() . $apiPath;
        $requestBody = $this->prepareRequestBody($requestBody, $type);
        $requestHeaders = $this->prepareRequestHeaders(self::METHOD_POST, $requestPath, $requestBody, $type, $accessToken);

        $response = RestClient::sendRequest(self::METHOD_POST, $requestUrl, $requestHeaders, $requestBody);

        return new Response($response->code, $response->headers, $response->body);
    }

    /**
     * Wrapper for sending PUT request.
     *
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $type Type
     * @param string $accessToken Access token
     */
    protected function sendPutRequest(string $apiPath, array $requestBody, string $type, string $accessToken = ''): Response
    {
        $requestPath = $this->prepareRequestPath(self::METHOD_PUT, $apiPath, $type);
        $requestUrl = $this->getApiUrl() . $apiPath;
        $requestBody = $this->prepareRequestBody($requestBody, $type);
        $requestHeaders = $this->prepareRequestHeaders(self::METHOD_PUT, $requestPath, $requestBody, $type, $accessToken);

        $response = RestClient::sendRequest(self::METHOD_PUT, $requestUrl, $requestHeaders, $requestBody);

        return new Response($response->code, $response->headers, $response->body);
    }
}