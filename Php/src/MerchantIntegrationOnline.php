<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

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

        $this->clientID = $clientID;
        $this->clientSecret = $clientSecret;
        $this->redirectUrl = $redirectUrl;
    }

    /**
     * Initialise the charge.
     *
     * @param string $partnerTxID Partner transaction ID
     * @param string $partnerGroupTxID Partner order ID
     * @param int $amount Transaction amount as integer
     * @param string $currency Currency for the transaction
     * @param string $description Description of the charge
     * @param array $metaInfo Meta information regarding the transaction
     * @param array $items Items within the transaction
     * @param array $shippingDetails Shipping details for the transaction
     * @param array $hidePaymentMethods Payment method to hide for the transaction
     */
    public function onaChargeInit(string $partnerTxID, string $partnerGroupTxID, int $amount, string $currency, string $description, array $metaInfo = [], array $items = [], array $shippingDetails = [], array $hidePaymentMethods = []): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'partnerTxID'      => $partnerTxID,
                'partnerGroupTxID' => $partnerGroupTxID,
                'amount'           => $amount,
                'currency'         => $currency,
                'merchantID'       => $this->getMerchantID(),
                'description'      => $description,
            ];
            if (! empty($hidePaymentMethods)) {
                $requestBody = array_merge($requestBody, ['hidePaymentMethods' => $hidePaymentMethods]);
            }
            if (! empty($metaInfo)) {
                $requestBody = array_merge($requestBody, ['metaInfo' => $metaInfo]);
            }
            if (! empty($items)) {
                $requestBody = array_merge($requestBody, ['items' => $items]);
            }
            if (! empty($shippingDetails)) {
                $requestBody = array_merge($requestBody, ['shippingDetails' => $shippingDetails]);
            }

            // This to get the which path can runing on their country
            return $this->sendPostRequest($env['chargeInit'], $requestBody, self::TYPE_ONLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Generate a web URL that provides a web interface for Oauth authentication. This helps the end-user login or register for GP.
     *
     * @param string $currency Currency for the transaction
     * @param string $codeVerifier Code verifier
     * @param string $requestToken Request token
     */
    public function onaCreateWebUrl(string $currency, string $codeVerifier, string $requestToken): string
    {
        $params = [
            'acr_values'            => 'consent_ctx:countryCode=' . $this->getCountry() . ',currency=' . $currency,
            'client_id'             => $this->getClientID(),
            'code_challenge'        => $this->base64URLEncode(hash('sha256', $codeVerifier, true)),
            'code_challenge_method' => 'S256',
            'nonce'                 => $this->generateRandomString(),
            'redirect_uri'          => $this->getRedirectUrl(),
            'request'               => $requestToken,
            'response_type'         => 'code',
            'scope'                 => $this->isCountryVietnam() ? 'payment.vn.one_time_charge' : 'payment.one_time_charge',
            'state'                 => $this->generateRandomString(),
        ];

        return $this->getApiUrl() . self::GRABID_V1_OAUTH2_AUTHORIZE . '?' . http_build_query($params);
    }

    /**
     * Get oauth2 access token.
     *
     * @param string $code Code
     * @param string $codeVerifier Code verifier
     */
    public function onaOAuth2Token(string $code, string $codeVerifier): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'client_id'     => $this->getClientID(),
                'client_secret' => $this->getClientSecret(),
                'code'          => $code,
                'code_verifier' => $codeVerifier,
                'grant_type'    => 'authorization_code',
                'redirect_uri'  => $this->getRedirectUrl(),
            ];

            return $this->sendPostRequest($env['OAuth2Token'], $requestBody, self::TYPE_ONLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Complete the charge.
     *
     * @param string $partnerTxID Partner transaction ID
     * @param string $accessToken Access token
     */
    public function onaChargeComplete(string $partnerTxID, string $accessToken): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBodyChargeComplete = [
                'partnerTxID' => $partnerTxID,
            ];

            return $this->sendPostRequest($env['chargeComplete'], $requestBodyChargeComplete, self::TYPE_ONLINE, $accessToken);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     *  Get status of the transaction.
     *
     * @param string $partnerTxID Partner transaction ID
     * @param string $currency Currency for the transaction
     * @param string $accessToken Access token
     */
    public function onaGetChargeStatus(string $partnerTxID, string $currency, string $accessToken): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $url = str_replace('{partnerTxID}', $partnerTxID, $env['onaChargeStatus']);
            $url = str_replace('{currency}', $currency, $url);

            return $this->sendGetRequest($url, self::TYPE_ONLINE, $accessToken);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Get the status of the transaction without the need of an oauth access token.
     *
     * @param string $partnerTxID Partner transaction ID
     * @param string $currency Currency for the transaction
     */
    public function onaGetOTCStatus(string $partnerTxID, string $currency): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $url = str_replace('{partnerTxID}', $partnerTxID, $env['oneTimeChargeStatus']);
            $url = str_replace('{currency}', $currency, $url);

            return $this->sendGetRequest($url, self::TYPE_ONLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Refund transaction that has been charged.
     *
     * @param string $partnerTxID Partner transaction ID to refund
     * @param string $partnerGroupTxID Partner order ID
     * @param int $amount Transaction amount as integer
     * @param string $currency Currency for the transaction
     * @param string $originTxID Original transaction ID
     * @param string $description Description of the refund
     * @param string $accessToken Access token
     */
    public function onaRefund(string $partnerTxID, string $partnerGroupTxID, int $amount, string $currency, string $originTxID, string $description, string $accessToken): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'partnerTxID'      => $partnerTxID,
                'partnerGroupTxID' => $partnerGroupTxID,
                'amount'           => (int) $amount,
                'currency'         => $currency,
                'merchantID'       => $this->getMerchantID(),
                'description'      => $description,
                'originTxID'       => $originTxID,
            ];

            return $this->sendPostRequest($env['onaRefundTxn'], $requestBody, self::TYPE_ONLINE, $accessToken);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Get status of the refunded transaction.
     *
     * @param string $refundPartnerTxID Partner transaction ID to refund
     * @param string $currency Currency for the transaction
     * @param string $accessToken Access token
     */
    public function onaGetRefundStatus(string $refundPartnerTxID, string $currency, string $accessToken): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $url = str_replace('{refundPartnerTxID}', $refundPartnerTxID, $env['onaCheckRefundTxn']);
            $url = str_replace('{currency}', $currency, $url);

            return $this->sendGetRequest($url, self::TYPE_ONLINE, $accessToken);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }
}
