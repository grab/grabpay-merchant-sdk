<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class RefundData extends DTO
{
    /**
     * Refund metadata.
     */
    public ?Metadata $metadata;

    /**
     * Refund origin transaction details.
     */
    public OriginTxDetails $originTxDetails;

    /**
     * Refund promo details.
     */
    public PromoDetails $promoDetails;

    /**
     * Refund status details.
     */
    public StatusDetails $statusDetails;

    /**
     * Refund transaction details.
     */
    public ResponseTransactionDetails $transactionDetails;
}
