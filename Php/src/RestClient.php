<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

class RestClient
{
    /**
     * Send request.
     *
     * @param string $requestMethod Request method
     * @param string $requestUrl Request URL
     * @param string $requestHeaders Request headers
     * @param array $requestBody Request body
     */
    public static function sendRequest(string $requestMethod, string $requestUrl, array $requestHeaders, array $requestBody): \Unirest\Response
    {
        // Set request body
        $response = null;
        $requestBody = \Unirest\Request\Body::json($requestBody);

        // Send request
        switch ($requestMethod) {
            case \Unirest\Method::GET:
                $response = \Unirest\Request::get($requestUrl, $requestHeaders);

                break;

            case \Unirest\Method::POST:
                $response = \Unirest\Request::post($requestUrl, $requestHeaders, $requestBody);

                break;

            case \Unirest\Method::PUT:
                $response = \Unirest\Request::put($requestUrl, $requestHeaders, $requestBody);

                break;
        }

        return $response;
    }
}
