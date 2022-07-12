<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

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

        $this->terminalID = $terminalID;
    }

    /**
     * Use this endpoint to accept payments using the Merchant Presented QR (MPQR) code.
     * This endpoint creates a payment order with a unique reference txID and returns a QR code string encoded with the merchant detail, amount, and txID.
     *
     * @param string $msgID Message ID
     * @param string $partnerTxID Partner transaction ID
     * @param int $amount Transaction amount as integer
     * @param string $currency Currency for the transaction
     */
    public function posCreateQRCode(string $msgID, string $partnerTxID, int $amount, string $currency): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'amount'      => $amount,
                'currency'    => $currency,
                'partnerTxID' => $partnerTxID,
                'msgID'       => $msgID,
            ];

            return $this->sendPostRequest($env['createQrCode'], $requestBody, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Use this endpoint to accept payments from a Consumer Presented QR (CPQR) code.
     * The endpoint performs a payment transaction based on the consumer presented QR code.
     * The transaction initiates a charge on the wallet associated with the requested QR code and completes a payout to the merchant Grab ID.
     *
     * @param string $msgID Message ID
     * @param string $partnerTxID Partner transaction ID
     * @param int $amount Transaction amount as integer
     * @param string $currency Currency for the transaction
     * @param string $code QR code
     */
    public function posPerformQRCode(string $msgID, string $partnerTxID, int $amount, string $currency, string $code): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'amount'      => $amount,
                'currency'    => $currency,
                'partnerTxID' => $partnerTxID,
                'msgID'       => $msgID,
                'code'        => $code,
            ];

            return $this->sendPostRequest($env['performTxn'], $requestBody, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Cancels a pending payment.
     * You can cancel a transaction if the payment status is in unknown state after 30 seconds.
     * Payments that are successfully charged cannot be cancelled; use the refund method instead.
     *
     * @param string $msgID Message ID
     * @param string $partnerTxID Partner transaction ID
     * @param string $origPartnerTxID Partner transaction ID to cancel
     * @param string $origTxID Original partner transaction ID
     * @param string $currency Currency Currency for the transaction
     */
    public function posCancel(string $msgID, string $partnerTxID, string $origPartnerTxID, string $origTxID, string $currency): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'currency'    => $currency,
                'origTxID'    => $origTxID,
                'partnerTxID' => $partnerTxID,
                'msgID'       => $msgID,
            ];

            return $this->sendPutRequest(str_replace('{origPartnerTxID}', $origPartnerTxID, $env['cancelQrTxn']), $requestBody, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
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
     * @param string $msgID Message ID
     * @param string $partnerTxID Refund transaction ID
     * @param int $amount Transaction amount as integer
     * @param string $currency Currency for the transaction
     * @param string $origPartnerTxID Original transaction ID to be refunded
     * @param string $description Description of the charge
     */
    public function posRefund(string $msgID, string $partnerTxID, int $amount, string $currency, string $origPartnerTxID, string $description): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $requestBody = [
                'currency'    => $currency,
                'amount'      => $amount,
                'reason'      => $description,
                'partnerTxID' => $partnerTxID,
                'msgID'       => $msgID,
            ];

            return $this->sendPutRequest(str_replace('{origPartnerTxID}', $origPartnerTxID, $env['posRefundTxn']), $requestBody, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Returns the payment transaction details.
     *
     * @param string $msgID Message ID
     * @param string $partnerTxID Partner transaction ID
     * @param string $currency Currency for the transaction
     */
    public function posGetTxnStatus(string $msgID, string $partnerTxID, string $currency): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $url = str_replace('{partnerTxID}', $partnerTxID, $env['posChargeStatus']);
            $url = str_replace('{currency}', $currency, $url);
            $url = str_replace('{msgID}', $msgID, $url);

            return $this->sendGetRequest($url, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }

    /**
     * Returns the refund transaction details.
     *
     * @param string $msgID Message ID
     * @param string $refundPartnerTxID Refund transaction ID
     * @param string $currency Currency for the transaction
     */
    public function posGetRefundStatus(string $msgID, string $refundPartnerTxID, string $currency): Response
    {
        try {
            $env = $this->getPartnerInfo();
            $url = str_replace('{refundPartnerTxID}', $refundPartnerTxID, $env['posChargeRefundStatus']);
            $url = str_replace('{currency}', $currency, $url);
            $url = str_replace('{msgID}', $msgID, $url);

            return $this->sendGetRequest($url, self::TYPE_OFFLINE);
        } catch (\Exception $ex) {
            return $this->handleException($ex);
        }
    }
}
