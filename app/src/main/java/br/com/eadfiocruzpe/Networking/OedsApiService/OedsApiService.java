package br.com.eadfiocruzpe.Networking.OedsApiService;

import retrofit2.Call;

import br.com.eadfiocruzpe.BuildConfig;
import br.com.eadfiocruzpe.Networking.BaseService;
import br.com.eadfiocruzpe.Models.DataResponses.ListYearsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.RankingCitiesYearlyCitizenValuePHResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailableCitiesUfResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailablePeriodsYearResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListUfsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ParamsAdvancedSearchResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.FederalIndicatorsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.StateIndicatorsResponseAPI;

public class OedsApiService extends BaseService<OedsApiServiceEndpoints>
        implements OedsApiServiceEndpoints {

    private OedsApiService() {
        super(OedsApiServiceEndpoints.class, BuildConfig.OEDS_API_URL, true);
    }

    public static OedsApiService build() {
        return new OedsApiService();
    }

    @Override
    public Call<ListUfsResponseAPI> requestListUFs() {
        return mService.requestListUFs();
    }

    @Override
    public Call<ListAvailableCitiesUfResponseAPI> requestListCitiesUf(String uf) {
        return mService.requestListCitiesUf(uf);
    }

    @Override
    public Call<ListYearsResponseAPI> requestListYears() {
        return mService.requestListYears();
    }

    @Override
    public Call<ListAvailablePeriodsYearResponseAPI> requestListPeriodsYear() {
        return mService.requestListPeriodsYear();
    }

    @Override
    public Call<RankingCitiesYearlyCitizenValuePHResponseAPI>
    requestSummaryNationalRankingMainIndicator(String year, String ufCode, String cityCode) {
        return mService.requestSummaryNationalRankingMainIndicator(year, ufCode, cityCode);
    }

    @Override
    public Call<ParamsAdvancedSearchResponseAPI> requestParamsAdvancedSearch(String ufName, String cityName) {
        return mService.requestParamsAdvancedSearch(ufName, cityName);
    }

    @Override
    public Call<FederalIndicatorsResponseAPI> requestNationalIndicatorsOnPH(String year) {
        return mService.requestNationalIndicatorsOnPH(year);
    }

    @Override
    public Call<StateIndicatorsResponseAPI> requestStateIndicatorsOnPH(String year, String ufCode) {
        return mService.requestStateIndicatorsOnPH(year, ufCode);
    }

}