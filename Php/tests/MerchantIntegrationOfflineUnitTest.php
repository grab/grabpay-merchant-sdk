<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Response;

/**
 * @internal
 */
final class MerchantIntegrationOfflineUnitTest extends MerchantIntegrationTest
{
    private MerchantIntegrationOffline $merchantIntegrationOffline;
    private MerchantIntegrationOffline $merchantIntegrationOfflineException;

    protected function setUp(): void
    {
        parent::setUp();

        $mockBuilder = $this->getMockBuilder(MerchantIntegrationOffline::class)
            ->setConstructorArgs([MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_POS_PARTNER_ID'], $_ENV['SG_STG_POS_PARTNER_SECRET'], $_ENV['SG_STG_POS_MERCHANT_ID'], $_ENV['SG_STG_POS_TERMINAL_ID']])
            ->disableOriginalClone()
            ->disableArgumentCloning()
            ->disallowMockingUnknownTypes()
        ;

        $this->merchantIntegrationOffline = $mockBuilder
            ->onlyMethods(['sendGetRequest', 'sendPostRequest', 'sendPutRequest'])
            ->getMock()
        ;

        $this->merchantIntegrationOfflineException = $mockBuilder
            ->onlyMethods(['getPartnerInfo'])
            ->getMock()
        ;
    }

    public function testPosCreateQRCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $qrcode = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $qrid = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'  => $msgID,
            'qrcode' => $qrcode,
            'txID'   => $txID,
            'qrid'   => $qrid,
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $posCreateQRCode = $this->merchantIntegrationOffline->posCreateQRCode($msgID, $partnerTxID, self::AMOUNT, MerchantIntegration::SGD);

        static::assertSame($response, $posCreateQRCode->getBody());
    }

    public function testPosCreateQRCodeException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posCreateQRCode = $this->merchantIntegrationOfflineException->posCreateQRCode(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD);

        static::assertSame('Caught exception: ', $posCreateQRCode->getBody());
    }

    public function testPosPerformQRCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'    => $msgID,
            'txID'     => $txID,
            'status'   => 'success',
            'amount'   => self::AMOUNT,
            'updated'  => 1657211215,
            'currency' => MerchantIntegration::SGD,
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $posPerformQRCode = $this->merchantIntegrationOffline->posPerformQRCode($msgID, $partnerTxID, self::AMOUNT, MerchantIntegration::SGD, self::QR_CODE);

        static::assertSame($response, $posPerformQRCode->getBody());
    }

    public function testPosPerformQRCodeException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posPerformQRCode = $this->merchantIntegrationOfflineException->posPerformQRCode(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD, self::QR_CODE);

        static::assertSame('Caught exception: ', $posPerformQRCode->getBody());
    }

    public function testPosCancel(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $origPartnerTxID = MerchantIntegration::generateRandomString();
        $origTxID = MerchantIntegration::generateRandomString();

        $response = null;

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new Response(200, [], $response));
        $posCancel = $this->merchantIntegrationOffline->posCancel($msgID, $partnerTxID, $origPartnerTxID, $origTxID, MerchantIntegration::SGD);

        static::assertSame($response, $posCancel->getBody());
    }

    public function testPosCancelException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posCancel = $this->merchantIntegrationOfflineException->posCancel(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), MerchantIntegration::SGD);

        static::assertSame('Caught exception: ', $posCancel->getBody());
    }

    public function testPosRefund(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $originTxID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $origPartnerTxID = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'          => $msgID,
            'txID'           => $txID,
            'originTxID'     => $originTxID,
            'status'         => 'success',
            'description'    => '',
            'additionalInfo' => ['amountBreakdown' => [
                'paidAmount'           => self::AMOUNT,
                'refundedChargeAmount' => self::AMOUNT,
                'revokePayoutAmount'   => self::AMOUNT,
            ]],
            'msg' => '',
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new Response(200, [], $response));
        $posRefund = $this->merchantIntegrationOffline->posRefund($msgID, $partnerTxID, self::AMOUNT, MerchantIntegration::SGD, $origPartnerTxID, 'testing posRefund');

        static::assertSame($response, $posRefund->getBody());
    }

    public function testPosRefundException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posRefund = $this->merchantIntegrationOfflineException->posRefund(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD, MerchantIntegration::generateRandomString(), 'testing posRefund');

        static::assertSame('Caught exception: ', $posRefund->getBody());
    }

    public function testPosGetTxnStatus(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'    => $msgID,
            'txID'     => $txID,
            'status'   => 'success',
            'amount'   => self::AMOUNT,
            'updated'  => 1657211215,
            'currency' => MerchantIntegration::SGD,
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendGetRequest')->willReturn(new Response(200, [], $response));
        $posGetTxnStatus = $this->merchantIntegrationOffline->posGetTxnStatus($msgID, $partnerTxID, MerchantIntegration::SGD);

        static::assertSame($response, $posGetTxnStatus->getBody());
    }

    public function testPosGetTxnStatusException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posGetTxnStatus = $this->merchantIntegrationOfflineException->posGetTxnStatus(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), MerchantIntegration::SGD);

        static::assertSame('Caught exception: ', $posGetTxnStatus->getBody());
    }

    public function testPosGetRefundStatus(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $refundPartnerTxID = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'    => $msgID,
            'txID'     => $txID,
            'status'   => 'success',
            'amount'   => self::AMOUNT,
            'updated'  => 1657211215,
            'currency' => MerchantIntegration::SGD,
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendGetRequest')->willReturn(new Response(200, [], $response));
        $posGetRefundStatus = $this->merchantIntegrationOffline->posGetRefundStatus($msgID, $refundPartnerTxID, MerchantIntegration::SGD);

        static::assertSame($response, $posGetRefundStatus->getBody());
    }

    public function testPosGetRefundStatusException(): void
    {
        $this->merchantIntegrationOfflineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $posGetRefundStatus = $this->merchantIntegrationOfflineException->posGetRefundStatus(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), MerchantIntegration::SGD);

        static::assertSame('Caught exception: ', $posGetRefundStatus->getBody());
    }
}