<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\CancelTxnParams;
use GrabPay\Merchant\Models\Offline\CreateQrCodeParams;
use GrabPay\Merchant\Models\Offline\GetTxnDetailsParams;
use GrabPay\Merchant\Models\Offline\PerformQrCodeTxnParams;
use GrabPay\Merchant\Models\Offline\RefundTxnParams;

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

        $this->merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_POS_PARTNER_ID'], $_ENV['SG_STG_POS_PARTNER_SECRET'], $_ENV['SG_STG_POS_MERCHANT_ID'], $_ENV['SG_STG_POS_TERMINAL_ID']);
    }

    public function testCancel(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $performQrCodeTxnParams = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => MerchantIntegration::generateRandomString(),
            'partnerTxID' => $partnerTxID,
        ]);
        $this->merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);
        $cancelTxnParams = new CancelTxnParams([
            'currency'        => MerchantIntegrationOffline::SGD,
            'msgID'           => $msgID,
            'origPartnerTxID' => $partnerTxID,
        ]);
        $response = $this->merchantIntegrationOffline->cancel($cancelTxnParams);
        $cancel = $response->data;

        static::assertSame(40011, $cancel->code);
        static::assertSame('transaction status is not supported', $cancel->reason);
    }

    public function testCreateQrCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();

        $createQrCodeParams = new CreateQrCodeParams([
            'amount'      => self::AMOUNT,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);

        $response = $this->merchantIntegrationOffline->createQrCode($createQrCodeParams);
        $createQrCode = $response->data;

        // Just to get the code coverage
        static::assertNotEmpty($response->headers);

        static::assertNotEmpty($createQrCode->qrcode);
        static::assertNotEmpty($createQrCode->txID);
        static::assertNotEmpty($createQrCode->expiryTime);
    }

    public function testGetRefundDetails(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $refundPartnerTxID = MerchantIntegration::generateRandomString();

        $performQrCodeTxnParams = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => MerchantIntegration::generateRandomString(),
            'partnerTxID' => $partnerTxID,
        ]);
        $this->merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);
        $refundTxnParams = new RefundTxnParams([
            'amount'          => self::AMOUNT,
            'currency'        => MerchantIntegrationOffline::SGD,
            'msgID'           => MerchantIntegrationOffline::generateRandomString(),
            'origPartnerTxID' => $partnerTxID,
            'partnerTxID'     => $refundPartnerTxID,
            'reason'          => 'testing refund',
        ]);
        $this->merchantIntegrationOffline->refund($refundTxnParams);
        $getTxnDetailsParams = new GetTxnDetailsParams([
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $refundPartnerTxID,
        ]);
        $response = $this->merchantIntegrationOffline->getRefundDetails($getTxnDetailsParams);
        $getRefundDetails = $response->data;

        static::assertSame($msgID, $getRefundDetails->msgID);
        static::assertNotEmpty($getRefundDetails->txID);
        static::assertSame('success', $getRefundDetails->status);
        static::assertSame(self::AMOUNT, $getRefundDetails->amount);
        static::assertIsInt($getRefundDetails->updated);
        static::assertSame(MerchantIntegration::SGD, $getRefundDetails->currency);
    }

    public function testGetTxnDetails(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $performQrCodeTxnParams = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => MerchantIntegration::generateRandomString(),
            'partnerTxID' => $partnerTxID,
        ]);
        $this->merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);
        $getTxnDetailsParams = new GetTxnDetailsParams([
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $partnerTxID,
        ]);
        $response = $this->merchantIntegrationOffline->getTxnDetails($getTxnDetailsParams);
        $getTxnDetails = $response->data;

        static::assertNotEmpty($getTxnDetails->txID);
        static::assertSame('success', $getTxnDetails->status);
        static::assertSame(self::AMOUNT, $getTxnDetails->amount);
        static::assertIsInt($getTxnDetails->updated);
        static::assertSame(MerchantIntegration::SGD, $getTxnDetails->currency);
    }

    public function testPerformQrCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();

        $performQrCodeTxnParams = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);
        $performQrCode = $response->data;

        static::assertNotEmpty($performQrCode->txID);
        static::assertSame('success', $performQrCode->status);
        static::assertSame(self::AMOUNT, $performQrCode->amount);
        static::assertIsInt($performQrCode->updated);
        static::assertSame(MerchantIntegration::SGD, $performQrCode->currency);
    }

    public function testRefund(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $performQrCodeTxnParams = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => MerchantIntegration::generateRandomString(),
            'partnerTxID' => $partnerTxID,
        ]);
        $performQrCode = $this->merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);
        $refundTxnParams = new RefundTxnParams([
            'amount'          => self::AMOUNT,
            'currency'        => MerchantIntegrationOffline::SGD,
            'msgID'           => $msgID,
            'origPartnerTxID' => $partnerTxID,
            'partnerTxID'     => MerchantIntegration::generateRandomString(),
            'reason'          => 'testing refund',
        ]);
        $response = $this->merchantIntegrationOffline->refund($refundTxnParams);
        $refund = $response->data;

        static::assertSame($msgID, $refund->msgID);
        static::assertNotEmpty($refund->txID);
        static::assertSame($performQrCode->data->txID, $refund->originTxID);
        static::assertSame('success', $refund->status);
        static::assertEmpty($refund->description);
        static::assertSame(self::AMOUNT, $refund->additionalInfo->amountBreakdown->paidAmount);
        static::assertSame(self::AMOUNT, $refund->additionalInfo->amountBreakdown->refundedChargeAmount);
        static::assertSame(self::AMOUNT, $refund->additionalInfo->amountBreakdown->revokePayoutAmount);
        static::assertEmpty($refund->msg);
    }
}
