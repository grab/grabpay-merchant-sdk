<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

use GrabPay\Merchant\Models\Offline\CancelTxnParams;
use GrabPay\Merchant\Models\Offline\CancelTxnResponse;
use GrabPay\Merchant\Models\Offline\CreateQrCodeParams;
use GrabPay\Merchant\Models\Offline\CreateQrCodeResponse;
use GrabPay\Merchant\Models\Offline\GetTxnDetailsParams;
use GrabPay\Merchant\Models\Offline\GetTxnDetailsResponse;
use GrabPay\Merchant\Models\Offline\PerformQrCodeTxnParams;
use GrabPay\Merchant\Models\Offline\PerformQrCodeTxnResponse;
use GrabPay\Merchant\Models\Offline\RefundTxnParams;
use GrabPay\Merchant\Models\Offline\RefundTxnResponse;
use GrabPay\Merchant\Models\Response;

class MerchantIntegrationOffline extends MerchantIntegration
{
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

        $this->type = self::TYPE_OFFLINE;
        $this->terminalID = $terminalID;

        // Setup API paths
        $apiPathPrefix = $this->isCountryVietnam() ? self::MOCA_PARTNERS_V1_PATH : self::REGIONAL_PARTNER_V1_PATH;
        $this->apiPaths = [
            'POS_CREATE_QR_CODE'      => $apiPathPrefix . '/terminal/qrcode/create',
            'POS_CANCEL_TRANSACTION'  => $apiPathPrefix . '/terminal/transaction/{origPartnerTxID}/cancel',
            'POS_REFUND_TRANSACTION'  => $apiPathPrefix . '/terminal/transaction/{origPartnerTxID}/refund',
            'POS_PERFORM_TRANSACTION' => $apiPathPrefix . '/terminal/transaction/perform',
            'POS_GET_TXN_DETAIL'      => $apiPathPrefix . '/terminal/transaction/{partnerTxID}',
        ];
    }

    /**
     * Cancels a pending payment.
     * You can cancel a transaction if the payment status is in unknown state after 30 seconds.
     * Payments that are successfully charged cannot be cancelled; use the refund method instead.
     *
     * @param CancelTxnParams $cancelTxnParams Params
     *
     * @return CancelTxnResponse|ErrorResponse
     */
    public function cancel(CancelTxnParams $cancelTxnParams): Response
    {
        $url = str_replace('{origPartnerTxID}', $cancelTxnParams->origPartnerTxID, $this->apiPaths['POS_CANCEL_TRANSACTION']);

        $requestBody = [
            'currency' => $cancelTxnParams->currency,
            'msgID'    => $cancelTxnParams->msgID,
        ];

        return $this->sendPutRequest(CancelTxnResponse::class, $url, $requestBody);
    }

    /**
     * Use this endpoint to accept payments using the Merchant Presented QR (MPQR) code.
     * This endpoint creates a payment order with a unique reference txID and returns a QR code string encoded with the merchant detail, amount, and txID.
     *
     * @param CreateQrCodeParams $createQrCodeParams Params
     *
     * @return CreateQrCodeResponse|ErrorResponse
     */
    public function createQrCode(CreateQrCodeParams $createQrCodeParams): Response
    {
        $requestBody = [
            'amount'      => $createQrCodeParams->amount,
            'currency'    => $createQrCodeParams->currency,
            'msgID'       => $createQrCodeParams->msgID,
            'partnerTxID' => $createQrCodeParams->partnerTxID,
        ];

        return $this->sendPostRequest(CreateQrCodeResponse::class, $this->apiPaths['POS_CREATE_QR_CODE'], $requestBody);
    }

    /**
     * Returns the refund transaction details.
     *
     * @param GetTxnDetailsParams $getTxnDetailsParams Params
     *
     * @return ErrorResponse|GetTxnDetailsResponse
     */
    public function getRefundDetails(GetTxnDetailsParams $getTxnDetailsParams): Response
    {
        $url = str_replace('{partnerTxID}', $getTxnDetailsParams->partnerTxID, $this->apiPaths['POS_GET_TXN_DETAIL']);
        $url = $url . '?' . http_build_query([
            'currency' => $getTxnDetailsParams->currency,
            'msgID'    => $getTxnDetailsParams->msgID,
            'txType'   => 'Refund',
        ]);

        return $this->sendGetRequest(GetTxnDetailsResponse::class, $url);
    }

    /**
     * Returns the payment transaction details.
     *
     * @param GetTxnDetailsParams $getTxnDetailsParams Params
     *
     * @return ErrorResponse|GetTxnDetailsResponse
     */
    public function getTxnDetails(GetTxnDetailsParams $getTxnDetailsParams): Response
    {
        $url = str_replace('{partnerTxID}', $getTxnDetailsParams->partnerTxID, $this->apiPaths['POS_GET_TXN_DETAIL']);
        $url = $url . '?' . http_build_query([
            'currency' => $getTxnDetailsParams->currency,
            'msgID'    => $getTxnDetailsParams->msgID,
            'txType'   => 'P2M',
        ]);

        return $this->sendGetRequest(GetTxnDetailsResponse::class, $url);
    }

    /**
     * Use this endpoint to accept payments from a Consumer Presented QR (CPQR) code.
     * The endpoint performs a payment transaction based on the consumer presented QR code.
     * The transaction initiates a charge on the wallet associated with the requested QR code and completes a payout to the merchant Grab ID.
     *
     * @param PerformQrCodeTxnParams $performQrCodeTxnParams Params
     *
     * @return ErrorResponse|PerformQrCodeTxnResponse
     */
    public function performQrCode(PerformQrCodeTxnParams $performQrCodeTxnParams): Response
    {
        $requestBody = [
            'amount'      => $performQrCodeTxnParams->amount,
            'code'        => $performQrCodeTxnParams->code,
            'currency'    => $performQrCodeTxnParams->currency,
            'msgID'       => $performQrCodeTxnParams->msgID,
            'partnerTxID' => $performQrCodeTxnParams->partnerTxID,
        ];

        return $this->sendPostRequest(PerformQrCodeTxnResponse::class, $this->apiPaths['POS_PERFORM_TRANSACTION'], $requestBody);
    }

    /**
     * Refunds a previously successful payment.
     * Returns a unique refund reference txID for this request.
     * You can refund:
     * 1. A transaction's full amount.
     * 2. A partial amount if the full amount of the transaction was paid using GrabPay.
     * 3. Multiple (partial) amounts as long as their sum doesn't exceed the charged amount.
     *
     * You can request refund for only charges that were generated since the last 30 days.
     *
     * @param RefundTxnParams $refundTxnParams Params
     *
     * @return ErrorResponse|RefundTxnResponse
     */
    public function refund(RefundTxnParams $refundTxnParams): Response
    {
        $url = str_replace('{origPartnerTxID}', $refundTxnParams->origPartnerTxID, $this->apiPaths['POS_REFUND_TRANSACTION']);

        $requestBody = [
            'amount'      => $refundTxnParams->amount,
            'currency'    => $refundTxnParams->currency,
            'msgID'       => $refundTxnParams->msgID,
            'partnerTxID' => $refundTxnParams->partnerTxID,
            'reason'      => $refundTxnParams->reason,
        ];

        return $this->sendPutRequest(RefundTxnResponse::class, $url, $requestBody);
    }
}
