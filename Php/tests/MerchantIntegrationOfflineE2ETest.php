<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOffline;

/**
 * @internal
 */
final class MerchantIntegrationOfflineE2ETest extends MerchantIntegrationTest
{
    private MerchantIntegrationOffline $merchantIntegrationOffline;

    protected function setUp(): void
    {
        parent::setUp();

        $this->merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_POS_PARTNER_ID'], $_ENV['SG_STG_POS_PARTNER_SECRET'], $_ENV['SG_STG_POS_MERCHANT_ID'], $_ENV['SG_STG_POS_TERMINAL_ID']);
    }

    public function testPosCreateQRCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();

        $response = $this->merchantIntegrationOffline->posCreateQRCode($msgID, MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD);
        $posCreateQRCode = $response->getBody();

        // Just to get the code coverage
        static::assertNotEmpty($response->getHeaders());

        static::assertNotEmpty($posCreateQRCode->qrcode);
        static::assertNotEmpty($posCreateQRCode->txID);
        static::assertNotEmpty($posCreateQRCode->expiryTime);
    }

    public function testPosPerformQRCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();

        $response = $this->merchantIntegrationOffline->posPerformQRCode($msgID, MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegrationOffline::SGD, self::QR_CODE);
        $posPerformQRCode = $response->getBody();

        static::assertNotEmpty($posPerformQRCode->txID);
        static::assertSame('success', $posPerformQRCode->status);
        static::assertSame(self::AMOUNT, $posPerformQRCode->amount);
        static::assertIsInt($posPerformQRCode->updated);
        static::assertSame(MerchantIntegration::SGD, $posPerformQRCode->currency);
    }

    public function testPosGetTxnStatus(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $this->merchantIntegrationOffline->posPerformQRCode(MerchantIntegration::generateRandomString(), $partnerTxID, self::AMOUNT, MerchantIntegrationOffline::SGD, self::QR_CODE);
        $response = $this->merchantIntegrationOffline->posGetTxnStatus($msgID, $partnerTxID, MerchantIntegrationOffline::SGD);
        $posGetTxnStatus = $response->getBody();

        static::assertNotEmpty($posGetTxnStatus->txID);
        static::assertSame('success', $posGetTxnStatus->status);
        static::assertSame(self::AMOUNT, $posGetTxnStatus->amount);
        static::assertIsInt($posGetTxnStatus->updated);
        static::assertSame(MerchantIntegration::SGD, $posGetTxnStatus->currency);
    }

    public function testPosCancel(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $posPerformQRCode = $this->merchantIntegrationOffline->posPerformQRCode(MerchantIntegration::generateRandomString(), $partnerTxID, self::AMOUNT, MerchantIntegrationOffline::SGD, self::QR_CODE);
        $response = $this->merchantIntegrationOffline->posCancel($msgID, MerchantIntegration::generateRandomString(), $partnerTxID, $posPerformQRCode->getBody()->txID, MerchantIntegrationOffline::SGD);
        $posCancel = $response->getBody();

        static::assertSame(40011, $posCancel->code);
        static::assertSame('transaction status is not supported', $posCancel->reason);
    }

    public function testPosRefund(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $posPerformQRCode = $this->merchantIntegrationOffline->posPerformQRCode(MerchantIntegration::generateRandomString(), $partnerTxID, self::AMOUNT, MerchantIntegrationOffline::SGD, self::QR_CODE);
        $response = $this->merchantIntegrationOffline->posRefund($msgID, MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegrationOffline::SGD, $partnerTxID, 'testing posRefund');
        $posRefund = $response->getBody();

        static::assertSame($msgID, $posRefund->msgID);
        static::assertNotEmpty($posRefund->txID);
        static::assertSame($posPerformQRCode->getBody()->txID, $posRefund->originTxID);
        static::assertSame('success', $posRefund->status);
        static::assertEmpty($posRefund->description);
        static::assertSame(self::AMOUNT, $posRefund->additionalInfo->amountBreakdown->paidAmount);
        static::assertSame(self::AMOUNT, $posRefund->additionalInfo->amountBreakdown->refundedChargeAmount);
        static::assertSame(self::AMOUNT, $posRefund->additionalInfo->amountBreakdown->revokePayoutAmount);
        static::assertEmpty($posRefund->msg);
    }

    public function testPosGetRefundStatus(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $refundPartnerTxID = MerchantIntegration::generateRandomString();

        $this->merchantIntegrationOffline->posPerformQRCode(MerchantIntegration::generateRandomString(), $partnerTxID, self::AMOUNT, MerchantIntegrationOffline::SGD, self::QR_CODE);
        $this->merchantIntegrationOffline->posRefund(MerchantIntegration::generateRandomString(), $refundPartnerTxID, self::AMOUNT, MerchantIntegrationOffline::SGD, $partnerTxID, 'testing posRefund');
        $response = $this->merchantIntegrationOffline->posGetRefundStatus($msgID, $refundPartnerTxID, MerchantIntegrationOffline::SGD);
        $posGetRefundStatus = $response->getBody();

        static::assertSame($msgID, $posGetRefundStatus->msgID);
        static::assertNotEmpty($posGetRefundStatus->txID);
        static::assertSame('success', $posGetRefundStatus->status);
        static::assertSame(self::AMOUNT, $posGetRefundStatus->amount);
        static::assertIsInt($posGetRefundStatus->updated);
        static::assertSame(MerchantIntegration::SGD, $posGetRefundStatus->currency);
    }
}
