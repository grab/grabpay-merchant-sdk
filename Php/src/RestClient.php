<?php

declare(strict_types=1);

namespace GrabPay\Merchant;

use Unirest\Method;
use Unirest\Request;
use Unirest\Request\Body;
use Unirest\Response;

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
    public static function sendRequest(string $requestMethod, string $requestUrl, array $requestHeaders, array $requestBody): Response
    {
        // We want returned objects will be converted into associative arrays
        Request::jsonOpts(true);

        // Set request body
        $response = null;
        $requestBody = Body::json($requestBody);

        // Send request
        switch ($requestMethod) {
            case Method::GET:
                $response = Request::get($requestUrl, $requestHeaders);

                break;

            case Method::POST:
                $response = Request::post($requestUrl, $requestHeaders, $requestBody);

                break;

            case Method::PUT:
                $response = Request::put($requestUrl, $requestHeaders, $requestBody);

                break;
        }

        return $response;
    }
}
