<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

class RefundData extends ChargeCompleteData
{
    /**
     * A unique code needed for the merchant to redeem the access token, and eventually complete the payment.
     * The OAuthCode parameter will only be returned when:
     * 1. User has consented to the payment,
     * 2. There is at least a redirect attempt to the merchant's redirect_uri, and
     * 3. The transaction is pending a charge/complete call on the merchant's end.
     *
     * The txStatus field will also be of the value authorized.
     *
     * There might be cases where the charge is authorized, but the OAuth code parameter is empty.
     * You should retry the operation after waiting 5 seconds.
     * If it is still empty, you can mark these transactions as failed and the amount authorized will be refunded to the GrabPay user.
     */
    public ?string $oAuthCode;
}