package integration.regional;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.models.Currency;
import com.merchantsdk.payment.models.PaymentMethod;
import com.merchantsdk.payment.models.PosInitiateRequest;
import com.merchantsdk.payment.models.PosInitiateResponse;
import com.merchantsdk.payment.models.PosPaymentChannel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

public class TestMerchantIntegrationOfflineRegional {
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    @BeforeAll
    public static void setUp() {
        final EnvironmentType env = EnvironmentType.STAGING;
        final Country country = Country.SINGAPORE;
        final String partnerId = System.getenv("SG_STG_POS_PARTNER_ID");
        final String partnerSecret = System.getenv("SG_STG_POS_PARTNER_SECRET");
        final String merchantId = System.getenv("SG_STG_POS_MERCHANT_ID");
        final String terminalId = System.getenv("SG_STG_POS_TERMINAL_ID");

        merchantIntegrationOffline = new MerchantIntegrationOffline(
                env,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                terminalId);
    }

    @Test
    public void testInitiate() {
        String partnerTxID = "partner-" + Utils.getRandomString(24);
        String billRefNumber = Utils.getRandomString(25);
        long amount = 10000;
        PosInitiateRequest request = new PosInitiateRequest() {
            {
                setTransactionDetails(new PosInitiateRequest.TransactionDetails() {
                    {
                        setAmount(amount);
                        setCurrency(Currency.SGD);
                        setBillRefNumber(billRefNumber);
                        setPartnerGroupTxID(partnerTxID);
                        setPaymentChannel(PosPaymentChannel.MPQR);
                        setPartnerTxID(partnerTxID);
                        setPaymentExpiryTime(Instant.now().getEpochSecond() + 60L);
                    }
                });
                setPaymentMethod(new PaymentMethod() {
                    {
                        setMinAmtPostpaid(100);
                    }
                });
            }
        };
        PosInitiateResponse response = merchantIntegrationOffline.initiate(request);

        assertNotNull(response);
        assertNotNull(response.getPosDetails());
        assertNotNull(response.getStatusDetails());
        assertNotNull(response.getTransactionDetails());
        assertEquals(PosPaymentChannel.MPQR, response.getTransactionDetails().getPaymentChannel());
        assertEquals("PENDING", response.getStatusDetails().getStatus());
        assertEquals("Pending consumer action", response.getStatusDetails().getStatusReason());
    }
}
