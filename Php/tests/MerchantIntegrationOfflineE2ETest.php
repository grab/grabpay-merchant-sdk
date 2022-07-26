<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\CancelParams;
use GrabPay\Merchant\Models\Offline\InitiateParams;
use GrabPay\Merchant\Models\Offline\InquiryParams;
use GrabPay\Merchant\Models\Offline\RefundParams;

/**
 * @internal
 * @coversNothing
 */
final class MerchantIntegrationOfflineE2ETest extends MerchantIntegrationTest
{
    private MerchantIntegrationOffline $merchantIntegrationOffline;

    protected function setUp(): void
    {
        parent::setUp();

        $this->merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, $_ENV['SG_STG_POS_PARTNER_ID'], $_ENV['SG_STG_POS_PARTNER_SECRET'], $_ENV['SG_STG_POS_MERCHANT_ID'], $_ENV['SG_STG_POS_TERMINAL_ID']);
    }

    public function testCancel(): void
    {
        $amount = 10;
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

        $initiateParams = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
        ]);
        $this->merchantIntegrationOffline->initiate($initiateParams);
        sleep(1);
        $cancelParams = new CancelParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'originPartnerTxID' => $partnerTxID,
                'currency'          => MerchantIntegrationOffline::SGD,
            ],
        ]);
        $response = $this->merchantIntegrationOffline->cancel($cancelParams);
        $cancel = $response->data;

        static::assertSame(MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, $cancel->transactionDetails->paymentChannel);
        static::assertSame(MerchantIntegrationOffline::SGD, $cancel->transactionDetails->currency);
        static::assertSame($partnerTxID, $cancel->transactionDetails->originPartnerTxID);
        static::assertSame('CANCELLED', $cancel->statusDetails->status);
    }

    public function testInitiateCpqr(): void
    {
        $amount = 10;
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

        $initiateParams = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
            'POSDetails' => [
                'consumerIdentifier' => self::QR_CODE,
            ],
        ]);
        $response = $this->merchantIntegrationOffline->initiate($initiateParams);
        $initiate = $response->data;

        static::assertSame(MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR, $initiate->transactionDetails->paymentChannel);
        static::assertSame(MerchantIntegrationOffline::SGD, $initiate->transactionDetails->currency);
        static::assertNotEmpty($initiate->transactionDetails->grabTxID);
        static::assertSame($partnerTxID, $initiate->transactionDetails->partnerTxID);
        static::assertSame($partnerGroupTxID, $initiate->transactionDetails->partnerGroupTxID);
        static::assertSame($amount, $initiate->transactionDetails->amount);
        static::assertSame('PENDING', $initiate->statusDetails->status);
    }

    public function testInitiateMpqr(): void
    {
        $amount = 10;
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

        $initiateParams = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
        ]);
        $response = $this->merchantIntegrationOffline->initiate($initiateParams);
        $initiate = $response->data;

        static::assertSame(MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, $initiate->transactionDetails->paymentChannel);
        static::assertSame(MerchantIntegrationOffline::SGD, $initiate->transactionDetails->currency);
        static::assertNotEmpty($initiate->transactionDetails->grabTxID);
        static::assertSame($partnerTxID, $initiate->transactionDetails->partnerTxID);
        static::assertSame($partnerGroupTxID, $initiate->transactionDetails->partnerGroupTxID);
        static::assertSame($amount, $initiate->transactionDetails->amount);
        static::assertSame('PENDING', $initiate->statusDetails->status);
        static::assertNotEmpty($initiate->POSDetails->qrPayload);
    }

    public function testInquiry(): void
    {
        $amount = 10;
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

        $initiateParams = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
        ]);
        $this->merchantIntegrationOffline->initiate($initiateParams);
        sleep(1);
        $inquiryParams = new InquiryParams([
            'transactionDetails' => [
                'paymentChannel' => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'currency'       => MerchantIntegrationOffline::SGD,
                'txType'         => MerchantIntegrationOffline::TX_TYPE_PAYMENT,
                'txRefType'      => MerchantIntegrationOffline::TX_REF_TYPE_PARTNERTXID,
                'txRefID'        => $partnerTxID,
            ],
        ]);
        $response = $this->merchantIntegrationOffline->inquiry($inquiryParams);
        $inquiry = $response->data;

        static::assertSame(MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, $inquiry->transactionDetails->paymentChannel);
        static::assertSame(MerchantIntegrationOffline::SGD, $inquiry->transactionDetails->currency);
        static::assertSame($partnerTxID, $inquiry->transactionDetails->partnerTxID);
        static::assertSame($partnerGroupTxID, $inquiry->transactionDetails->partnerGroupTxID);
        static::assertSame(MerchantIntegrationOffline::TX_TYPE_PAYMENT, $inquiry->transactionDetails->txType);
        static::assertSame('PENDING', $inquiry->statusDetails->status);
    }

    public function testRefund(): void
    {
        $amount = 10;
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

        $initiateParams = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
        ]);
        $this->merchantIntegrationOffline->initiate($initiateParams);
        sleep(1);
        $refundParams = new RefundParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'originPartnerTxID' => $partnerTxID,
                'partnerTxID'       => MerchantIntegrationOffline::generateRandomString(),
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'reason'            => 'testing refund',
            ],
        ]);
        $response = $this->merchantIntegrationOffline->refund($refundParams);
        $refund = $response->data;
        static::assertSame('BUS-TXN-NTF', $refund->statusDetails->statusCode);
    }
}
