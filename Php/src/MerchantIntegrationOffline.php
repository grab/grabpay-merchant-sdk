<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

use GrabPay\Merchant\Models\Offline\CancelParams;
use GrabPay\Merchant\Models\Offline\CancelResponse;
use GrabPay\Merchant\Models\Offline\InitiateParams;
use GrabPay\Merchant\Models\Offline\InitiateResponse;
use GrabPay\Merchant\Models\Offline\InquiryParams;
use GrabPay\Merchant\Models\Offline\InquiryResponse;
use GrabPay\Merchant\Models\Offline\RefundParams;
use GrabPay\Merchant\Models\Offline\RefundResponse;
use GrabPay\Merchant\Models\Response;

class MerchantIntegrationOffline extends MerchantIntegration
{
    /**
     * Constant for consumer present payment channel (CPQR).
     *
     * @var string
     */
    public const PAYMENT_CHANNEL_CPQR = 'CPQR';

    /**
     * Constant for merchant present payment channel (MPQR).
     *
     * @var string
     */
    public const PAYMENT_CHANNEL_MPQR = 'MPQR';

    /**
     * Transaction reference type for grabTxID (GRABTXID).
     *
     * @var string
     */
    public const TX_REF_TYPE_GRABTXID = 'GRABTXID';

    /**
     * Transaction reference type for partnerTxID (PARTNERTXID).
     *
     * @var string
     */
    public const TX_REF_TYPE_PARTNERTXID = 'PARTNERTXID';

    /**
     * Transaction type for payment (PAYMENT).
     *
     * @var string
     */
    public const TX_TYPE_PAYMENT = 'PAYMENT';

    /**
     * Transaction type for refund (REFUND).
     *
     * @var string
     */
    public const TX_TYPE_REFUND = 'REFUND';

    /**
     * MerchantIntegrationOffline constructor.
     *
     * @param string $environment Environment
     * @param string $country Country
     * @param string $partnerID Partner ID
     * @param string $partnerSecret Partner Secret
     * @param string $merchantID Merchant ID
     * @param string $terminalID Terminal ID
     */
    public function __construct(string $environment, string $country, string $partnerID, string $partnerSecret, string $merchantID, string $terminalID)
    {
        parent::__construct($environment, $country, $partnerID, $partnerSecret, $merchantID);

        $this->terminalID = $terminalID;

        // Setup API paths
        $this->apiPaths = [
            'POS_INITIATE' => self::PARTNER_V3_PAYMENT_PATH . '/init',
            'POS_INQUIRY'  => self::PARTNER_V3_PAYMENT_PATH . '/inquiry',
            'POS_REFUND'   => self::PARTNER_V3_PAYMENT_PATH . '/refund',
            'POS_CANCEL'   => self::PARTNER_V3_PAYMENT_PATH . '/cancellation',
        ];
    }

    /**
     * A cancellation request can be made using the Cancellation API in the following scenarios:
     * 1. When a consumer decides to cancel a Grab payment at a self-service terminal.
     * 2. When a cashier needs to cancel a Grab payment.
     * 3. When a transaction is not completed before paymentExpiryTime.
     *
     * @param CancelParams $cancelParams Params
     *
     * @return CancelResponse|ErrorResponse
     */
    public function cancel(CancelParams $cancelParams): Response
    {
        $requestBody = $cancelParams->toArray();

        return $this->sendPutRequest(CancelResponse::class, $this->apiPaths['POS_CANCEL'], $requestBody);
    }

    /**
     * The Payment Initiate API allows a merchant to initiate both a Merchant Present QR (MPQR), and well as a Consumer Present QR payment (CPQR).
     *
     * @param InitiateParams $initiateParams Params
     *
     * @return ErrorResponse|InitiateResponse
     */
    public function initiate(InitiateParams $initiateParams): Response
    {
        $requestBody = $initiateParams->toArray();

        return $this->sendPostRequest(InitiateResponse::class, $this->apiPaths['POS_INITIATE'], $requestBody);
    }

    /**
     * The Inquiry API allows the merchant to perform the following checks:
     * 1. When an ongoing transaction is in PENDING status, and merchant has yet to receive a terminal transaction status (SUCCESS / FAILURE).
     * 2. When merchant needs to check the details of a historical transaction.
     * Merchants are to implement rate limiting when making an inquiry call.
     *
     * Merchant will need to implement rate limiting feature, and restrict to a maximum of 50 API calls per second per Partner ID. This will help avoid unnecessary HTTP 429 response status.
     * When using the Inquiry API poll for PENDING transactions, merchants are advised to rate limit to 1 Inquiry API call per second per transaction.
     *
     * @param InquiryParams $inquiryParams Params
     *
     * @return ErrorResponse|InquiryResponse
     */
    public function inquiry(InquiryParams $inquiryParams): Response
    {
        $params = $this->dot($this->prefixIds($inquiryParams->toArray()));
        $url = $this->apiPaths['POS_INQUIRY'] . '?' . http_build_query($params);

        return $this->sendGetRequest(InquiryResponse::class, $url);
    }

    /**
     * A refund request can be made for a payment transaction. The Refund API supports the following refunds:
     * 1. Partial refund.
     * 2. Full refund.
     * Merchants will need to provide the original payment partnerTxID in the originPartnerTxID parameter in the transactionDetails object in order to initiate a refund for a specific payment.
     *
     * The refund validity is 90 days from the date of payment.
     *
     * @param RefundParams $refundParams Params
     *
     * @return ErrorResponse|RefundResponse
     */
    public function refund(RefundParams $refundParams): Response
    {
        $requestBody = $refundParams->toArray();

        return $this->sendPutRequest(RefundResponse::class, $this->apiPaths['POS_REFUND'], $requestBody);
    }

    /**
     * Prepare request body.
     * For offline, we need to append grabID and terminalID to all GET requests.
     *
     * @param array $requestBody Request body
     */
    protected function prepareRequestBody(array $requestBody): array
    {
        return $this->prefixIds($requestBody);
    }

    /**
     * Flatten a multi-dimensional associative array with dots.
     *
     * @param array $array Array to flatten to dot notation
     */
    private function dot(array $array, string $prepend = ''): array
    {
        $results = [];

        foreach ($array as $key => $value) {
            if (\is_array($value) && ! empty($value)) {
                $results = array_merge($results, $this->dot($value, $prepend . $key . '.'));
            } else {
                $results[$prepend . $key] = $value;
            }
        }

        return $results;
    }

    /**
     * Return storeGrabID and terminalID to be added to GET request path and POST/PUT request body.
     *
     * @param array $body Body of the payload to prefix the IDs with
     */
    private function prefixIds(array $body): array
    {
        $ids = [
            'transactionDetails' => [
                'storeGrabID' => $this->merchantID,
            ],
            'POSDetails' => [
                'terminalID' => $this->terminalID,
            ],
        ];

        foreach ($ids as $key => $value) {
            if (isset($body[$key])) {
                $body[$key] = array_merge($body[$key], $value);
            } else {
                $body[$key] = $value;
            }
        }

        return $body;
    }
}
