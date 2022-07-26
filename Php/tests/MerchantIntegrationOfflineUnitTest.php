<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\CancelParams;
use GrabPay\Merchant\Models\Offline\CancelResponse;
use GrabPay\Merchant\Models\Offline\InitiateParams;
use GrabPay\Merchant\Models\Offline\InitiateResponse;
use GrabPay\Merchant\Models\Offline\InquiryParams;
use GrabPay\Merchant\Models\Offline\InquiryResponse;
use GrabPay\Merchant\Models\Offline\RefundParams;
use GrabPay\Merchant\Models\Offline\RefundResponse;

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
            ->setConstructorArgs([MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, $_ENV['SG_STG_POS_PARTNER_ID'], $_ENV['SG_STG_POS_PARTNER_SECRET'], $_ENV['SG_STG_POS_MERCHANT_ID'], $_ENV['SG_STG_POS_TERMINAL_ID']])
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
        $origPartnerTxID = MerchantIntegrationOffline::generateRandomString();

        $params = new CancelParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'originPartnerTxID' => $origPartnerTxID,
                'currency'          => MerchantIntegrationOffline::SGD,
            ],
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'transactionDetails' => [
                    'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                    'originPartnerTxID' => $origPartnerTxID,
                    'currency'          => MerchantIntegrationOffline::SGD,
                ],
                'statusDetails' => [
                    'status'       => '',
                    'statusCode'   => 'BUS-MEX-001',
                    'statusReason' => 'success',
                ],
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new CancelResponse($response));
        $cancel = $this->merchantIntegrationOffline->cancel($params);

        static::assertInstanceOf(CancelResponse::class, $cancel);
    }

    public function testInitiate(): void
    {
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();
        $amount = 10;
        $paymentExpiryTime = strtotime('+5 minutes');

        $params = new InitiateParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'paymentExpiryTime' => strtotime('+5 minutes'),
            ],
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'transactionDetails' => [
                    'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                    'grabTxID'          => MerchantIntegrationOffline::generateRandomString(),
                    'partnerTxID'       => $partnerTxID,
                    'partnerGroupTxID'  => $partnerGroupTxID,
                    'amount'            => $amount,
                    'currency'          => MerchantIntegrationOffline::SGD,
                    'paymentExpiryTime' => $paymentExpiryTime,
                ],
                'POSDetails' => [
                    'terminalID' => MerchantIntegrationOffline::generateRandomString(),
                    'qrPayload'  => MerchantIntegrationOffline::generateRandomString(),
                ],
                'statusDetails' => [
                    'status'       => '',
                    'statusCode'   => 'BUS-MEX-001',
                    'statusReason' => 'success',
                ],
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPostRequest')->willReturn(new InitiateResponse($response));
        $initiate = $this->merchantIntegrationOffline->initiate($params);

        static::assertInstanceOf(InitiateResponse::class, $initiate);
    }

    public function testInquiry(): void
    {
        $originPartnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();
        $amount = 10;
        $updatedTime = strtotime('+5 minutes');
        $paymentExpiryTime = strtotime('+5 minutes');

        $params = new InquiryParams([
            'transactionDetails' => [
                'paymentChannel' => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'currency'       => MerchantIntegrationOffline::SGD,
                'txType'         => MerchantIntegrationOffline::TX_TYPE_PAYMENT,
                'txRefType'      => MerchantIntegrationOffline::TX_REF_TYPE_PARTNERTXID,
                'txRefID'        => $partnerTxID,
            ],
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'transactionDetails' => [
                    'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                    'txType'            => MerchantIntegrationOffline::TX_TYPE_PAYMENT,
                    'grabTxID'          => MerchantIntegrationOffline::generateRandomString(),
                    'partnerTxID'       => $partnerTxID,
                    'partnerGroupTxID'  => $partnerGroupTxID,
                    'amount'            => $amount,
                    'currency'          => MerchantIntegrationOffline::SGD,
                    'paymentMethod'     => 'GPWALLET',
                    'updatedTime'       => $updatedTime,
                    'paymentExpiryTime' => $paymentExpiryTime,
                ],
                'POSDetails' => [
                    'terminalID' => MerchantIntegrationOffline::generateRandomString(),
                ],
                'originTxDetails' => [
                    'originGrabTxID'         => MerchantIntegrationOffline::generateRandomString(),
                    'originPartnerTxID'      => $originPartnerTxID,
                    'originPartnerGroupTxID' => MerchantIntegrationOffline::generateRandomString(),
                    'originAmount'           => $amount,
                ],
                'promoDetails' => [
                    'promoCode'              => 'promoCode',
                    'consumerPaidAmt'        => $amount,
                    'merchantFundedPromoAmt' => 0,
                    'pointsMultiplier'       => 'pointsMultiplier',
                    'pointsAwarded'          => 0,
                ],
                'statusDetails' => [
                    'status'       => '',
                    'statusCode'   => 'BUS-MEX-001',
                    'statusReason' => 'success',
                ],
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendGetRequest')->willReturn(new InquiryResponse($response));
        $inquiry = $this->merchantIntegrationOffline->inquiry($params);

        static::assertInstanceOf(InquiryResponse::class, $inquiry);
    }

    public function testRefund(): void
    {
        $originPartnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerTxID = MerchantIntegrationOffline::generateRandomString();
        $partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();
        $amount = 10;
        $updatedTime = strtotime('+5 minutes');

        $params = new RefundParams([
            'transactionDetails' => [
                'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                'originPartnerTxID' => $originPartnerTxID,
                'partnerTxID'       => $partnerTxID,
                'partnerGroupTxID'  => $partnerGroupTxID,
                'amount'            => $amount,
                'currency'          => MerchantIntegrationOffline::SGD,
                'reason'            => 'testing refund',
            ],
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'transactionDetails' => [
                    'paymentChannel'   => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
                    'grabTxID'         => MerchantIntegrationOffline::generateRandomString(),
                    'partnerTxID'      => $partnerTxID,
                    'partnerGroupTxID' => $partnerGroupTxID,
                    'amount'           => $amount,
                    'currency'         => MerchantIntegrationOffline::SGD,
                    'paymentMethod'    => 'GPWALLET',
                    'updatedTime'      => $updatedTime,
                ],
                'originTxDetails' => [
                    'originGrabTxID'         => MerchantIntegrationOffline::generateRandomString(),
                    'originPartnerTxID'      => $originPartnerTxID,
                    'originPartnerGroupTxID' => MerchantIntegrationOffline::generateRandomString(),
                    'originAmount'           => $amount,
                ],
                'promoDetails' => [
                    'consumerRefundedAmt'          => $amount,
                    'merchantFundedPromoRefundAmt' => $amount,
                    'pointsRefunded'               => 0,
                ],
                'statusDetails' => [
                    'status'       => '',
                    'statusCode'   => 'BUS-MEX-001',
                    'statusReason' => 'success',
                ],
            ],
        ];

        $this->merchantIntegrationOffline->expects(static::once())->method('sendPutRequest')->willReturn(new RefundResponse($response));
        $refund = $this->merchantIntegrationOffline->refund($params);

        static::assertInstanceOf(RefundResponse::class, $refund);
    }
}