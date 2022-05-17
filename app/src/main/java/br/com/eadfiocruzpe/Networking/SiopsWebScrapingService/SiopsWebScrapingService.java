package br.com.eadfiocruzpe.Networking.SiopsWebScrapingService;

import retrofit2.Call;

import br.com.eadfiocruzpe.BuildConfig;
import br.com.eadfiocruzpe.Networking.BaseService;
import br.com.eadfiocruzpe.Models.DataResponses.BaseWebScrapingResponse;

public class SiopsWebScrapingService extends BaseService<SiopsWebScrapingServiceEndpoints>
        implements SiopsWebScrapingServiceEndpoints {

    // List UFs, List government investments on the public health of a city
    public static final String PARAM_BTN_CONSULTAR = "Consultar";

    // List city indicators
    public static final String PARAM_ORDER_BY = "Codigo";
    public static final String PARAM_AUTH = "-1";
    public static final String PARAM_CODE_HIST = "-1";

    private SiopsWebScrapingService() {
        super(SiopsWebScrapingServiceEndpoints.class, BuildConfig.SIOPS_URL, false);
    }

    public static SiopsWebScrapingService build() {
        return new SiopsWebScrapingService();
    }

    @Override
    public Call<BaseWebScrapingResponse> requestCityIndicatorsPublicHealth(
            String year, String ufCode, String periodYearCode, String cityCode, String order, String auth, String codHist) {

        return mService.requestCityIndicatorsPublicHealth(
                year, ufCode, periodYearCode, cityCode, order, auth, codHist);
    }

    @Override
    public Call<BaseWebScrapingResponse> requestGovInvestmentsDeclaredExpensesCityPH(
            String year, String ufCode, String periodYearCode, String cityCode, String btConsultar) {

        return mService.requestGovInvestmentsDeclaredExpensesCityPH(
                year, ufCode, periodYearCode, cityCode, btConsultar);
    }

}