<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

use GrabPay\Merchant\Models\Online\ChargeCompleteParams;
use GrabPay\Merchant\Models\Online\ChargeCompleteResponse;
use GrabPay\Merchant\Models\Online\ChargeInitParams;
use GrabPay\Merchant\Models\Online\ChargeInitResponse;
use GrabPay\Merchant\Models\Online\GenerateWebUrlParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusResponse;
use GrabPay\Merchant\Models\Online\GetOtcStatusParams;
use GrabPay\Merchant\Models\Online\GetOtcStatusResponse;
use GrabPay\Merchant\Models\Online\GetRefundStatusParams;
use GrabPay\Merchant\Models\Online\GetRefundStatusResponse;
use GrabPay\Merchant\Models\Online\Oauth2TokenParams;
use GrabPay\Merchant\Models\Online\Oauth2TokenResponse;
use GrabPay\Merchant\Models\Online\RefundParams;
use GrabPay\Merchant\Models\Online\RefundResponse;
use GrabPay\Merchant\Models\Response;

class MerchantIntegrationOnline extends MerchantIntegration
{
    /**
     * MerchantIntegrationOnline constructor.
     *
     * @param string $environment Environment
     * @param string $country Country
     * @param string $partnerID Partner ID
     * @param string $partnerSecret Partner Secret
     * @param string $merchantID Merchant ID
     * @param string $clientID Client ID
     * @param string $clientSecret Client Secret
     * @param string $redirectUrl Redirect URL
     */
    public function __construct(string $environment, string $country, string $partnerID, string $partnerSecret, string $merchantID, string $clientID, string $clientSecret, string $redirectUrl)
    {
        parent::__construct($environment, $country, $partnerID, $partnerSecret, $merchantID);

        $this->type = self::TYPE_ONLINE;
        $this->clientID = $clientID;
        $this->clientSecret = $clientSecret;
        $this->redirectUrl = $redirectUrl;

        // Setup API paths
        $apiPathPrefix = $this->isCountryVietnam() ? self::MOCA_PARTNER_V2_PATH : self::REGIONAL_PARTNER_V2_PATH;
        $this->apiPaths = [
            'ONA_CHARGE_INIT'            => $apiPathPrefix . '/charge/init',
            'OAUTH_TOKEN'                => self::GRABID_V1_OAUTH2_TOKEN,
            'ONA_CHARGE_COMPLETE'        => $apiPathPrefix . '/charge/complete',
            'ONA_CHARGE_STATUS'          => $apiPathPrefix . '/charge/{partnerTxID}/status',
            'ONA_REFUND'                 => $apiPathPrefix . '/refund',
            'ONA_REFUND_STATUS'          => $apiPathPrefix . '/refund/{partnerTxID}/status',
            'ONA_ONE_TIME_CHARGE_STATUS' => $apiPathPrefix . '/one-time-charge/{partnerTxID}/status',
        ];
    }

    /**
     * Complete the payment authorized by the user.
     *
     * @param ChargeCompleteParams $chargeCompleteParams Params
     *
     * @return ChargeCompleteResponse|ErrorResponse
     */
    public function chargeComplete(ChargeCompleteParams $chargeCompleteParams): Response
    {
        $requestBody = [
            'partnerTxID' => $chargeCompleteParams->partnerTxID,
        ];

        return $this->sendPostRequest(ChargeCompleteResponse::class, $this->apiPaths['ONA_CHARGE_COMPLETE'], $requestBody, $chargeCompleteParams->accessToken);
    }

    /**
     * Set up the details required to initiate a one-time payment.
     *
     * @param ChargeInitParams $chargeInitParams Params
     *
     * @return ChargeInitResponse|ErrorResponse
     */
    public function chargeInit(ChargeInitParams $chargeInitParams): Response
    {
        $requestBody = [
            'amount'           => $chargeInitParams->amount,
            'currency'         => $chargeInitParams->currency,
            'description'      => $chargeInitParams->description,
            'merchantID'       => $this->merchantID,
            'partnerGroupTxID' => $chargeInitParams->partnerGroupTxID,
            'partnerTxID'      => $chargeInitParams->partnerTxID,
        ];
        if (! empty($chargeInitParams->hidePaymentMethods)) {
            $requestBody = array_merge($requestBody, ['hidePaymentMethods' => $chargeInitParams->hidePaymentMethods]);
        }
        if (! empty($chargeInitParams->metaInfo)) {
            $requestBody = array_merge($requestBody, ['metaInfo' => json_decode(json_encode($chargeInitParams->metaInfo), true)]);
        }
        if (! empty($chargeInitParams->items)) {
            $requestBody = array_merge($requestBody, ['items' => json_decode(json_encode($chargeInitParams->items), true)]);
        }
        if (! empty($chargeInitParams->shippingDetails)) {
            $requestBody = array_merge($requestBody, ['shippingDetails' => (array) $chargeInitParams->shippingDetails]);
        }

        // This to get the which path can runing on their country
        return $this->sendPostRequest(ChargeInitResponse::class, $this->apiPaths['ONA_CHARGE_INIT'], $requestBody);
    }

    /**
     * Generate a Web URL that provides a web interface for Grab's user-level authentication.
     * This helps the end-user login or register for GrabPay.
     *
     * @param GenerateWebUrlParams $generateWebUrlParams Params
     */
    public function generateWebUrl(GenerateWebUrlParams $generateWebUrlParams): string
    {
        $params = [
            'acr_values'            => 'consent_ctx:countryCode=' . $this->country . ',currency=' . $generateWebUrlParams->currency,
            'client_id'             => $this->clientID,
            'code_challenge'        => $this->base64URLEncode(hash('sha256', $generateWebUrlParams->codeVerifier, true)),
            'code_challenge_method' => 'S256',
            'nonce'                 => $this->generateRandomString(),
            'redirect_uri'          => $this->redirectUrl,
            'request'               => $generateWebUrlParams->request,
            'response_type'         => 'code',
            'scope'                 => $this->isCountryVietnam() ? 'payment.vn.one_time_charge' : 'payment.one_time_charge',
            'state'                 => $this->generateRandomString(),
        ];

        return $this->getApiUrl() . self::GRABID_V1_OAUTH2_AUTHORIZE . '?' . http_build_query($params);
    }

    /**
     * Check the payment status.
     * This endpoint requires an OAuth access token.
     *
     * @param GetChargeStatusParams $getChargeStatusParams Params
     *
     * @return ErrorResponse|GetChargeStatusResponse
     */
    public function getChargeStatus(GetChargeStatusParams $getChargeStatusParams): Response
    {
        $url = str_replace('{partnerTxID}', $getChargeStatusParams->partnerTxID, $this->apiPaths['ONA_CHARGE_STATUS']);
        $url = $url . '?' . http_build_query([
            'currency' => $getChargeStatusParams->currency,
        ]);

        return $this->sendGetRequest(GetChargeStatusResponse::class, $url, $getChargeStatusParams->accessToken);
    }

    /**
     * Check the payment status.
     * This endpoint DOES NOT requires an oauth access token.
     *
     * @param GetOtcStatusParams $getOtcStatusParams Params
     *
     * @return ErrorResponse|GetOtcStatusResponse
     */
    public function getOtcStatus(GetOtcStatusParams $getOtcStatusParams): Response
    {
        $url = str_replace('{partnerTxID}', $getOtcStatusParams->partnerTxID, $this->apiPaths['ONA_ONE_TIME_CHARGE_STATUS']);
        $url = $url . '?' . http_build_query([
            'currency' => $getOtcStatusParams->currency,
        ]);

        return $this->sendGetRequest(GetOtcStatusResponse::class, $url);
    }

    /**
     * Check the refund status.
     *
     * @param GetRefundStatusParams $getRefundStatusParams Params
     *
     * @return ErrorResponse|GetRefundStatusResponse
     */
    public function getRefundStatus(GetRefundStatusParams $getRefundStatusParams): Response
    {
        $url = str_replace('{partnerTxID}', $getRefundStatusParams->partnerTxID, $this->apiPaths['ONA_REFUND_STATUS']);
        $url = $url . '?' . http_build_query([
            'currency' => $getRefundStatusParams->currency,
        ]);

        return $this->sendGetRequest(GetRefundStatusResponse::class, $url, $getRefundStatusParams->accessToken);
    }

    /**
     * Generate an OAuth 2.0 token by passing code received in the return URL from GrabPay.
     *
     * @param Oauth2TokenParams $oauth2TokenParams Params
     *
     * @return ErrorResponse|Oauth2TokenResponse
     */
    public function oauth2Token(Oauth2TokenParams $oauth2TokenParams): Response
    {
        $requestBody = [
            'client_id'     => $this->clientID,
            'client_secret' => $this->clientSecret,
            'code'          => $oauth2TokenParams->code,
            'code_verifier' => $oauth2TokenParams->codeVerifier,
            'grant_type'    => 'authorization_code',
            'redirect_uri'  => $this->redirectUrl,
        ];

        return $this->sendPostRequest(Oauth2TokenResponse::class, $this->apiPaths['OAUTH_TOKEN'], $requestBody);
    }

    /**
     * This operation allows you to perform full or partial refunds for a given completed transaction.
     *
     * RefundParams $refundParams Params
     *
     * @return ErrorResponse|RefundResponse
     */
    public function refund(RefundParams $refundParams): Response
    {
        $requestBody = [
            'amount'           => $refundParams->amount,
            'currency'         => $refundParams->currency,
            'description'      => $refundParams->description,
            'merchantID'       => $this->merchantID,
            'originTxID'       => $refundParams->originTxID,
            'partnerGroupTxID' => $refundParams->partnerGroupTxID,
            'partnerTxID'      => $refundParams->partnerTxID,
        ];

        return $this->sendPostRequest(RefundResponse::class, $this->apiPaths['ONA_REFUND'], $requestBody, $refundParams->accessToken);
    }
}
