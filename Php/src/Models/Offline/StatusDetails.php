<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class StatusDetails extends DTO
{
    /**
     * This field contains the status of a transaction.
     */
    public string $status;

    /**
     * This field contains the status code of a transaction. The status code will be unique to each of the available status.
     */
    public string $statusCode;

    /**
     * This field will provide the reason for the status of a transaction, and will be useful for understanding the reason for non-successful transaction statuses.
     */
    public string $statusReason;
}