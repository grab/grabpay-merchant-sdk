<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOffline;
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

/**
 * @internal
 * @coversNothing
 */
final class MerchantIntegrationOfflineUnitTest extends MerchantIntegrationTest
{
    private MerchantIntegrationOffline $merchantIntegrationOffline;

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
    }

    public function testCancel(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $origPartnerTxID = MerchantIntegration::generateRandomString();

        $params = new CancelTxnParams([
            'currency'        => MerchantIntegration::SGD,
            'msgID'           => $msgID,
            'origPartnerTxID' => $origPartnerTxID,
        ]);

        $response = ['status' => 200,
            'headers'         => [],
            'data'            => null, ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new CancelTxnResponse($response));
        $cancel = $this->merchantIntegrationOffline->cancel($params);

        static::assertInstanceOf(CancelTxnResponse::class, $cancel);
    }

    public function testCreateQrCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $qrcode = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $qrid = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $params = new CreateQrCodeParams([
            'amount'      => self::AMOUNT,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'expiryTime' => date('c'),
                'msgID'      => $msgID,
                'qrcode'     => $qrcode,
                'qrid'       => $qrid,
                'txID'       => $txID,
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPostRequest')->willReturn(new CreateQrCodeResponse($response));
        $createQrCode = $this->merchantIntegrationOffline->createQrCode($params);

        static::assertInstanceOf(CreateQrCodeResponse::class, $createQrCode);
        static::assertSame($msgID, $createQrCode->data->msgID);
    }

    public function testGetRefundDetails(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $params = new GetTxnDetailsParams([
            'currency'    => MerchantIntegration::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'amount'   => self::AMOUNT,
                'currency' => MerchantIntegration::SGD,
                'msgID'    => $msgID,
                'status'   => 'success',
                'txID'     => $txID,
                'updated'  => 1657211215,
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendGetRequest')->willReturn(new GetTxnDetailsResponse($response));
        $getRefundDetails = $this->merchantIntegrationOffline->getRefundDetails($params);

        static::assertInstanceOf(GetTxnDetailsResponse::class, $getRefundDetails);
        static::assertSame($msgID, $getRefundDetails->data->msgID);
    }

    public function testGetTxnDetails(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $params = new GetTxnDetailsParams([
            'currency'    => MerchantIntegration::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'amount'   => self::AMOUNT,
                'currency' => MerchantIntegration::SGD,
                'msgID'    => $msgID,
                'status'   => 'success',
                'txID'     => $txID,
                'updated'  => 1657211215,
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendGetRequest')->willReturn(new GetTxnDetailsResponse($response));
        $getTxnDetails = $this->merchantIntegrationOffline->getTxnDetails($params);

        static::assertInstanceOf(GetTxnDetailsResponse::class, $getTxnDetails);
        static::assertSame($msgID, $getTxnDetails->data->msgID);
    }

    public function testPerformQrCode(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $params = new PerformQrCodeTxnParams([
            'amount'      => self::AMOUNT,
            'code'        => self::QR_CODE,
            'currency'    => MerchantIntegrationOffline::SGD,
            'msgID'       => $msgID,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'amount'   => self::AMOUNT,
                'currency' => MerchantIntegration::SGD,
                'msgID'    => $msgID,
                'status'   => 'success',
                'txID'     => $txID,
                'updated'  => 1657211215,
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPostRequest')->willReturn(new PerformQrCodeTxnResponse($response));
        $performQrCode = $this->merchantIntegrationOffline->performQrCode($params);

        static::assertInstanceOf(PerformQrCodeTxnResponse::class, $performQrCode);
        static::assertSame($msgID, $performQrCode->data->msgID);
    }

    public function testRefund(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $originTxID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $origPartnerTxID = MerchantIntegration::generateRandomString();

        $params = new RefundTxnParams([
            'amount'          => self::AMOUNT,
            'currency'        => MerchantIntegration::SGD,
            'msgID'           => $msgID,
            'origPartnerTxID' => $origPartnerTxID,
            'partnerTxID'     => $partnerTxID,
            'reason'          => 'testing refund',
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'additionalInfo' => ['amountBreakdown' => [
                    'paidAmount'           => self::AMOUNT,
                    'refundedChargeAmount' => self::AMOUNT,
                    'revokePayoutAmount'   => self::AMOUNT,
                ]],
                'description' => 'description',
                'msg'         => 'msg',
                'msgID'       => $msgID,
                'originTxID'  => $originTxID,
                'status'      => 'success',
                'txID'        => $txID,
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new RefundTxnResponse($response));
        $refund = $this->merchantIntegrationOffline->refund($params);

        static::assertInstanceOf(RefundTxnResponse::class, $refund);
        static::assertSame($msgID, $refund->data->msgID);
    }
}