import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import { generateHmac, getGMTString } from './Utils';
import { SDK_SIGNATURE, SDK_VERSION } from '../configs/Version';
import { ClientConfig } from '../configs/Config';
import { THttpMethod } from '../types';

export interface IRequester {
  hmacRequest<TResponseBody, TRequestBody>(
    request: Readonly<RequestParams<TRequestBody>>,
  ): Promise<Response<TResponseBody>>;
  request<TResponseBody, TRequestBody>(
    request: Readonly<RequestParams<TRequestBody>>,
  ): Promise<Response<TResponseBody>>;
}

export type RequestParams<TRequestBody> = {
  path: string;
  method: THttpMethod;
  query?: Record<string, string>;
  headers?: Record<string, string>;
} & ({ body: string } | { data: TRequestBody } | {});

export type Response<TResponseBody> = {
  status: number;
  headers: Record<string, string>;
  data: TResponseBody;
};

function buildHeadersForHmacRequest<TRequestBody>(
  requestParams: Readonly<RequestParams<TRequestBody>>,
  partnerId: string,
  partnerSecret: string,
): Record<string, string> {
  const { headers = {}, path, method, query } = requestParams;
  const contentType = headers['Content-Type'] || 'application/json';
  const timestamp = headers['Date'] || getGMTString();
  let pathWithParams = path;
  if (query && Object.keys(query).length > 0) {
    pathWithParams = `${pathWithParams}?${new URLSearchParams(query).toString()}`;
  }
  const hmac = generateHmac({
    contentType,
    httpMethod: method || 'GET',
    requestBody: 'body' in requestParams ? requestParams.body : '',
    requestUrl: pathWithParams,
    secret: partnerSecret,
    timestamp,
  });
  return {
    Authorization: `${partnerId}:${hmac}`,
    'Content-Type': contentType,
    Date: timestamp,
    ...headers,
  };
}

export default class AxiosRequester implements IRequester {
  _config: ClientConfig;
  _requester: AxiosInstance;

  constructor(config: ClientConfig) {
    const { baseUrl } = config.envVars;
    this._requester = axios.create({ baseURL: baseUrl, transformRequest: [] });
    this._config = config;
  }

  hmacRequest<TResponseBody, TRequestBody>(requestParams: Readonly<RequestParams<TRequestBody>>) {
    const { path, method, query } = requestParams;
    let body = '';
    if ('body' in requestParams) {
      body = requestParams.body;
    } else if ('data' in requestParams) {
      body = JSON.stringify(requestParams.data);
    }
    const hmacSignedHeaders = buildHeadersForHmacRequest(
      {
        ...requestParams,
        body,
      },
      this._config.partnerId,
      this._config.partnerSecret,
    );

    return this.request<TResponseBody, never>({
      headers: hmacSignedHeaders,
      body,
      method,
      path,
      query,
    });
  }

  async request<TResponseBody, TRequestBody>(
    requestParams: Readonly<RequestParams<TRequestBody>>,
  ): Promise<Response<TResponseBody>> {
    let body = '';
    if ('body' in requestParams) {
      body = requestParams.body;
    } else if ('data' in requestParams) {
      body = JSON.stringify(requestParams.data);
    }
    const axiosConfig: AxiosRequestConfig = {
      method: requestParams.method,
      url: requestParams.path,
      headers: {
        ...requestParams.headers,
        'X-Sdk-Country': this._config.country,
        'X-Sdk-Language': 'nodejs',
        'X-Sdk-Signature': SDK_SIGNATURE,
        'X-Sdk-Version': SDK_VERSION,
      },
      ...(requestParams.method === 'GET' ? {} : { data: body }),
      ...(requestParams.query && Object.keys(requestParams.query).length > 0 ? { params: requestParams.query } : {}),
    };

    try {
      const response = await this._requester.request<TResponseBody>(axiosConfig);
      return {
        status: response.status,
        headers: response.headers,
        data: response.data,
      };
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        return {
          status: err.response.status,
          headers: err.response.headers,
          data: err.response.data,
        };
      }
      throw err;
    }
  }
}
