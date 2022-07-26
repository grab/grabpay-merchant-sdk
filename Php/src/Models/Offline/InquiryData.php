<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InquiryData extends DTO
{
    /**
     * Inquiry metadata.
     */
    public ?Metadata $metadata;

    /**
     * Inquiry origin transaction details.
     */
    public ?OriginTxDetails $originTxDetails;

    /**
     * Inquiry POS details.
     */
    public ?PosDetails $POSDetails;

    /**
     * Inquiry promo details.
     */
    public PromoDetails $promoDetails;

    /**
     * Inquiry status details.
     */
    public StatusDetails $statusDetails;

    /**
     * Inquiry transaction details.
     */
    public ResponseTransactionDetails $transactionDetails;
}
