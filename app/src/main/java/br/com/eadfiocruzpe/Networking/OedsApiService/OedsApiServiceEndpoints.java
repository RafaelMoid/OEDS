package br.com.eadfiocruzpe.Networking.OedsApiService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import br.com.eadfiocruzpe.Models.DataResponses.ListUfsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailableCitiesUfResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailablePeriodsYearResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListYearsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.RankingCitiesYearlyCitizenValuePHResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ParamsAdvancedSearchResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.FederalIndicatorsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.StateIndicatorsResponseAPI;

interface OedsApiServiceEndpoints {

    @GET("/get_list_ufs_from_db/")
    Call<ListUfsResponseAPI> requestListUFs();

    @GET("/get_list_cities_uf_from_db/")
    Call<ListAvailableCitiesUfResponseAPI> requestListCitiesUf(
            @Query("ufCode") String uf);

    @GET("/get_list_years_from_db/")
    Call<ListYearsResponseAPI> requestListYears();

    @GET("/get_list_periods_year_from_db/")
    Call<ListAvailablePeriodsYearResponseAPI> requestListPeriodsYear();

    @GET("/get_summary_ranking_cities_by_ph_indicator_from_db/")
    Call<RankingCitiesYearlyCitizenValuePHResponseAPI> requestSummaryNationalRankingMainIndicator(
            @Query("year") String year,
            @Query("ufCode") String ufCode,
            @Query("cityCode") String cityCode);

    @GET("/get_search_params_city_uf_names_latest_period_year/")
    Call<ParamsAdvancedSearchResponseAPI> requestParamsAdvancedSearch(
            @Query("ufName") String ufName,
            @Query("cityName") String cityName);

    @GET("/get_national_indicators_from_db/")
    Call<FederalIndicatorsResponseAPI> requestNationalIndicatorsOnPH(
            @Query("year") String year);

    @GET("/get_state_indicators_from_db/")
    Call<StateIndicatorsResponseAPI> requestStateIndicatorsOnPH(
            @Query("year") String year,
            @Query("ufCode") String ufCode);

}