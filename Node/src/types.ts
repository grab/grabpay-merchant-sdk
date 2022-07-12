export type TCountry = 'SG' | 'MY' | 'TH' | 'PH' | 'VN' | 'ID' | 'MM' | 'KH';
export type TEnvironment = 'STG' | 'PRD';
export type TCurrency = 'MYR' | 'IDR' | 'VND' | 'THB' | 'SGD' | 'PHP' | 'MMK' | 'KHR';
export type TPaymentMethod = 'INSTALMENT' | 'POSTPAID' | 'CARD';
export type THttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH' | 'OPTIONS' | 'HEAD';
export type TTxStatus =
  | 'success'
  | 'failed'
  | 'processing'
  | 'cancelled'
  | 'authorised'
  | 'authorisation_declined'
  | 'transaction_already_exist';
