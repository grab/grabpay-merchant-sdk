<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

class Response
{
    /**
     * HTTP response code
     * public to be compatible with other languages SDK
     */
    public int $code;

    /**
     * HTTP response headers
     * public to be compatible with other languages SDK
     */
    public array $headers;

    /**
     * Parsed response body where applicable, JSON responses are parsed to Objects
     * public to be compatible with other languages SDK
     * It is not typed to mixed because of PHP 7.4
     */
    public $body;

    /**
     * Response constructor.
     *
     * @param int $code HTTP response code
     * @param array $headers HTTP response headers
     * @param $body Parsed response body
     */
    public function __construct(int $code, array $headers, $body = null)
    {
        $this->code = $code;
        $this->headers = $headers;
        $this->body = $body;
    }

    /**
     * Get HTTP response code.
     */
    public function getCode(): int
    {
        return $this->code;
    }

    /**
     * Get  HTTP response headers.
     */
    public function getHeaders(): array
    {
        return $this->headers;
    }

    /**
     * Get Parsed response body.
     */
    public function getBody()
    {
        return $this->body;
    }
}