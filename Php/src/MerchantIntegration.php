<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

use GrabPay\Merchant\Models\ErrorResponse;
use GrabPay\Merchant\Models\Response;

/**
 * Class MerchantIntegration
 */
abstract class MerchantIntegration
{
    /**
     * Constant for header application/x-www-form-urlencoded.
     *
     * @var string
     */
    public const HEADER_APPLICATION_FORM = 'application/x-www-form-urlencoded';

    /**
     * Constant for header application/json.
     *
     * @var string
     */
    public const HEADER_APPLICATION_JSON = 'application/json';

    /**
     * Constant for Indonesia (ID).
     *
     * @var string
     */
    public const ID = 'ID';

    /**
     * Constant for Indonesia Rupiah (IDR).
     *
     * @var string
     */
    public const IDR = 'IDR';

    /**
     * Constant for Cambodia (KH).
     *
     * @var string
     */
    public const KH = 'KH';

    /**
     * Constant for Cambodia Riel (KHR).
     *
     * @var string
     */
    public const KHR = 'KHR';

    /**
     * Constant for method GET.
     *
     * @var string
     */
    public const METHOD_GET = 'GET';

    /**
     * Constant for method POST.
     *
     * @var string
     */
    public const METHOD_POST = 'POST';

    /**
     * Constant for method PUT.
     *
     * @var string
     */
    public const METHOD_PUT = 'PUT';

    /**
     * Constant for Myanmar (MM).
     *
     * @var string
     */
    public const MM = 'MM';

    /**
     * Constant for Myanmar Kyat (MMK).
     *
     * @var string
     */
    public const MMK = 'MMK';

    /**
     * Constant for Malaysia (MY).
     *
     * @var string
     */
    public const MY = 'MY';

    /**
     * Constant for Malaysia Ringgit (MYR).
     *
     * @var string
     */
    public const MYR = 'MYR';

    /**
     * Constant for Philippines (PH).
     *
     * @var string
     */
    public const PH = 'PH';

    /**
     * Constant for Philippines Peso (PHP).
     *
     * @var string
     */
    public const PHP = 'PHP';

    /**
     * Constant for production (PRD).
     *
     * @var string
     */
    public const PRODUCTION = 'PRD';

    /**
     * Constant for SDK language.
     *
     * @var string
     */
    public const SDK_LANGUAGE = 'PHP';

    /**
     * Constant for SDK signature.
     *
     * @var string
     */
    public const SDK_SIGNATURE = '6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7';

    /**
     * Constant for SDK version.
     *
     * @var string
     */
    public const SDK_VERSION = '2.0.0';

    /**
     * Constant for Singapore (SG).
     *
     * @var string
     */
    public const SG = 'SG';

    /**
     * Constant for Singapore Dollars (SGD).
     *
     * @var string
     */
    public const SGD = 'SGD';

    /**
     * Constant for staging (STG).
     *
     * @var string
     */
    public const STAGING = 'STG';

    /**
     * Constant for Thailand (TH).
     *
     * @var string
     */
    public const TH = 'TH';

    /**
     * Constant for Thailand Baht (THB).
     *
     * @var string
     */
    public const THB = 'THB';

    /**
     * Constant for Vietnam (VN).
     *
     * @var string
     */
    public const VN = 'VN';

    /**
     * Constant for Vietnam Dong (VND).
     *
     * @var string
     */
    public const VND = 'VND';

    /**
     * Constant for GrabID v1 oauth2 authorize path.
     *
     * @var string
     */
    protected const GRABID_V1_OAUTH2_AUTHORIZE = '/grabid/v1/oauth2/authorize';

    /**
     * Constant for GrabID v1 oauth2 token path.
     *
     * @var string
     */
    protected const GRABID_V1_OAUTH2_TOKEN = '/grabid/v1/oauth2/token';

    /**
     * Constant for Moca partner v2 Path.
     *
     * @var string
     */
    protected const MOCA_PARTNER_V2_PATH = '/mocapay/partner/v2';

    /**
     * Constant for Moca Production API URL.
     *
     * @var string
     */
    protected const MOCA_PRD_API_URL = 'https://partner-gw.moca.vn';

    /**
     * Constant for Moca Staging API URL.
     *
     * @var string
     */
    protected const MOCA_STG_API_URL = 'https://stg-paysi.moca.vn';

    /**
     * Constant for partner v3 payment path.
     *
     * @var string
     */
    protected const PARTNER_V3_PAYMENT_PATH = '/grabpay/partner/v3/payment';

    /**
     * Constant for Regional partner v2 path.
     *
     * @var string
     */
    protected const REGIONAL_PARTNER_V2_PATH = '/grabpay/partner/v2';

    /**
     * Constant for Regional Production API URL.
     *
     * @var string
     */
    protected const REGIONAL_PRD_API_URL = 'https://partner-api.grab.com';

    /**
     * Constant for Regional Staging API URL.
     *
     * @var string
     */
    protected const REGIONAL_STG_API_URL = 'https://partner-api.stg-myteksi.com';

    /**
     * API paths.
     */
    protected array $apiPaths;

    /**
     * Client ID.
     */
    protected string $clientID = '';

    /**
     * Client secret.
     */
    protected string $clientSecret = '';

    /**
     * Country code (alpha-2).
     */
    protected string $country;

    /**
     * Environment. Can be either "STG" or "PRD".
     */
    protected string $environment;

    /**
     * Merchant ID.
     */
    protected string $merchantID;

    /**
     * Partner ID.
     */
    protected string $partnerID;

    /**
     * Partner secret.
     */
    protected string $partnerSecret;

    /**
     * Redirect URL.
     */
    protected string $redirectUrl = '';

    /**
     * Terminal ID.
     */
    protected string $terminalID = '';

    /**
     * Merchan type. ONLINE or OFFLINE.
     */
    protected string $type;

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
     * @param string $dateTime Date/time
     */
    public static function generateHmacSignature(string $partnerSecret, string $requestMethod, string $apiUrl, string $contentType, array $requestBody, string $dateTime): string
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
        $content .= $dateTime;
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
     * @param string $dateTime Date/time
     */
    public static function generatePopSignature(string $clientSecret, string $accessToken, string $dateTime): string
    {
        $timestamp = strtotime($dateTime);
        $message = $timestamp . $accessToken;
        $signature = hash_hmac('sha256', $message, $clientSecret, true);
        $payload = [
            'time_since_epoch' => $timestamp,
            'sig'              => self::base64UrlEncode($signature),
        ];

        return self::base64UrlEncode(json_encode($payload));
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
     * Format response.
     *
     * @param $responseClass Response class name
     * @param int $status HTTP status code
     * @param array|string $data
     */
    protected function formatResponse($responseClass, int $status, array $headers, $data): Response
    {
        $isSuccessfulResponse = $status >= 200 && $status <= 299;

        if (! $isSuccessfulResponse) {
            $responseClass = ErrorResponse::class;
        }

        if (empty($data)) {
            $data = [];
        }

        return new $responseClass([
            'status'  => $status,
            'headers' => $headers,
            'data'    => $data,
        ]);
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
     * Get Content-Type.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     */
    protected function getContentType(string $requestMethod): string
    {
        return $requestMethod === self::METHOD_GET ? self::HEADER_APPLICATION_FORM : self::HEADER_APPLICATION_JSON;
    }

    /**
     * Get default request headers.
     *
     * @param string $contentType Content-Type
     */
    protected function getDefaultRequestHeaders(string $contentType): array
    {
        return [
            'Accept'          => self::HEADER_APPLICATION_JSON,
            'X-Request-ID'    => self::generateRandomString(),
            'Content-Type'    => $contentType,
            'X-Sdk-Country'   => $this->country,
            'X-Sdk-Version'   => self::SDK_VERSION,
            'X-Sdk-Language'  => self::SDK_LANGUAGE,
            'X-Sdk-Signature' => self::SDK_SIGNATURE,
        ];
    }

    /**
     * Get HMAC request headers.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     * @param string $apiPath API path
     * @param array $requestBody Request body
     */
    protected function getHmacRequestHeaders(string $requestMethod, string $apiPath, array $requestBody): array
    {
        $dateTime = $this->getUTCDateTimeInRFC7231();
        $contentType = $this->getContentType($requestMethod);
        $defaultHeaders = $this->getDefaultRequestHeaders($contentType);
        $hmac = self::generateHmacSignature($this->partnerSecret, $requestMethod, $apiPath, $contentType, $requestBody, $dateTime);

        return array_merge($defaultHeaders, [
            'Date'          => $dateTime,
            'Authorization' => ($this->partnerID . ':' . $hmac),
        ]);
    }

    /**
     * Get POP signature request headers.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     * @param string $accessToken Access token
     */
    protected function getPopRequestHeaders(string $requestMethod, string $accessToken): array
    {
        $dateTime = $this->getUTCDateTimeInRFC7231();
        $contentType = $this->getContentType($requestMethod);
        $defaultHeaders = $this->getDefaultRequestHeaders($contentType);

        return array_merge($defaultHeaders, [
            'Date'          => $dateTime,
            'X-GID-AUX-POP' => self::generatePopSignature($this->clientSecret, $accessToken, $dateTime),
            'Authorization' => 'Bearer ' . $accessToken,
        ]);
    }

    /**
     * Get UTC Date/Time in RFC7231 (D, d M Y H:i:s \G\M\T).
     */
    protected function getUTCDateTimeInRFC7231(): string
    {
        $now = new \DateTime('now', new \DateTimeZone('UTC'));

        return $now->format(\DateTime::RFC7231);
    }

    /**
     * Handle general exception.
     *
     * @param $responseClass Response class name
     * @params \Exception $ex Exception
     */
    protected function handleException($responseClass, \Exception $ex): Response
    {
        return $this->formatResponse($responseClass, 0, [], [
            'code'   => $ex->getCode(),
            'reason' => 'Caught exception: ' . $ex->getMessage(),
        ]);
    }

    /**
     * Get Is country Vietnam?
     */
    protected function isCountryVietnam(): bool
    {
        return $this->country === self::VN;
    }

    /**
     * Is environment production?
     */
    protected function isEnvironmentProduction(): bool
    {
        return $this->environment === self::PRODUCTION;
    }

    /**
     * Prepare GET request API path.
     * We let Offline override it.
     *
     * @param string $apiPath API path
     */
    protected function prepareGetRequestPath(string $apiPath): string
    {
        return $apiPath;
    }

    /**
     * Prepare request body.
     * We let Offline override it.
     *
     * @param array $requestBody Request body
     */
    protected function prepareRequestBody(array $requestBody): array
    {
        return $requestBody;
    }

    /**
     * Prepare request headers.
     *
     * @param string $requestMethod Request method, GET, POST, PUT
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $accessToken Access token
     */
    protected function prepareRequestHeaders(string $requestMethod, string $apiPath, array $requestBody, string $accessToken): array
    {
        // If we have access token, we send it as verification
        if (! empty($accessToken)) {
            return $this->getPopRequestHeaders($requestMethod, $accessToken);
        }

        // If not we assume we are using HMAC as a verification method
        return $this->getHmacRequestHeaders($requestMethod, $apiPath, $requestBody);
    }

    /**
     * Wrapper for sending GET request.
     *
     * @param $responseClass Response class name
     * @param string $apiPath API path
     * @param string $accessToken Access token
     */
    protected function sendGetRequest($responseClass, string $apiPath, string $accessToken = ''): Response
    {
        try {
            $requestPath = $this->prepareGetRequestPath($apiPath);
            $requestUrl = $this->getApiUrl() . $requestPath;
            $requestHeaders = $this->prepareRequestHeaders(self::METHOD_GET, $requestPath, [], $accessToken);
            $response = RestClient::sendRequest(self::METHOD_GET, $requestUrl, $requestHeaders, []);

            return $this->formatResponse($responseClass, $response->code, $response->headers, $response->body);
        } catch (\Exception $ex) {
            return $this->handleException($responseClass, $ex);
        }
    }

    /**
     * Wrapper for sending POST request.
     *
     * @param $responseClass Response class name
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $accessToken Access token
     */
    protected function sendPostRequest($responseClass, string $apiPath, array $requestBody, string $accessToken = ''): Response
    {
        $requestBody = $this->prepareRequestBody($requestBody);
        $requestHeaders = $this->prepareRequestHeaders(self::METHOD_POST, $apiPath, $requestBody, $accessToken);

        return $this->sendRawPostRequest($responseClass, $apiPath, $requestBody, $requestHeaders);
    }

    /**
     * Wrapper for sending PUT request.
     *
     * @param $responseClass Response class name
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param string $accessToken Access token
     */
    protected function sendPutRequest($responseClass, string $apiPath, array $requestBody, string $accessToken = ''): Response
    {
        try {
            $requestUrl = $this->getApiUrl() . $apiPath;
            $requestBody = $this->prepareRequestBody($requestBody);
            $requestHeaders = $this->prepareRequestHeaders(self::METHOD_PUT, $apiPath, $requestBody, $accessToken);
            $response = RestClient::sendRequest(self::METHOD_PUT, $requestUrl, $requestHeaders, $requestBody);

            return $this->formatResponse($responseClass, $response->code, $response->headers, $response->body);
        } catch (\Exception $ex) {
            return $this->handleException($responseClass, $ex);
        }
    }

    /**
     * Wrapper for sending raw POST request.
     *
     * @param $responseClass Response class name
     * @param string $apiPath API path
     * @param array $requestBody Request body
     * @param array $requestHeaders Request header
     */
    protected function sendRawPostRequest($responseClass, string $apiPath, array $requestBody, array $requestHeaders = []): Response
    {
        try {
            $requestUrl = $this->getApiUrl() . $apiPath;
            $response = RestClient::sendRequest(self::METHOD_POST, $requestUrl, $requestHeaders, $requestBody);

            return $this->formatResponse($responseClass, $response->code, $response->headers, $response->body);
        } catch (\Exception $ex) {
            return $this->handleException($responseClass, $ex);
        }
    }
}