// Code generated by mockery v0.0.0-dev. DO NOT EDIT.

package gemocks

import (
	dto "github.com/grab/grabpay-merchant-sdk/dto"
	context "context"

	http "net/http"

	mock "github.com/stretchr/testify/mock"
)

// OfflineTransaction is an autogenerated mock type for the OfflineTransaction type
type OfflineTransaction struct {
	mock.Mock
}

// PosCancel provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosCancel(ctx context.Context, params *dto.PosCancelParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosCancelParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosCancelParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// PosCreateQRCode provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosCreateQRCode(ctx context.Context, params *dto.PosCreateQRCodeParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosCreateQRCodeParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosCreateQRCodeParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// PosGetRefundDetails provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosGetRefundDetails(ctx context.Context, params *dto.PosGetRefundDetailsParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosGetRefundDetailsParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosGetRefundDetailsParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// PosGetTxnDetails provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosGetTxnDetails(ctx context.Context, params *dto.PosGetTxnDetailsParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosGetTxnDetailsParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosGetTxnDetailsParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// PosPerformQRCode provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosPerformQRCode(ctx context.Context, params *dto.PosPerformQRCodeParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosPerformQRCodeParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosPerformQRCodeParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}

// PosRefund provides a mock function with given fields: ctx, params
func (_m *OfflineTransaction) PosRefund(ctx context.Context, params *dto.PosRefundParams) (*http.Response, error) {
	ret := _m.Called(ctx, params)

	var r0 *http.Response
	if rf, ok := ret.Get(0).(func(context.Context, *dto.PosRefundParams) *http.Response); ok {
		r0 = rf(ctx, params)
	} else {
		if ret.Get(0) != nil {
			r0 = ret.Get(0).(*http.Response)
		}
	}

	var r1 error
	if rf, ok := ret.Get(1).(func(context.Context, *dto.PosRefundParams) error); ok {
		r1 = rf(ctx, params)
	} else {
		r1 = ret.Error(1)
	}

	return r0, r1
}
