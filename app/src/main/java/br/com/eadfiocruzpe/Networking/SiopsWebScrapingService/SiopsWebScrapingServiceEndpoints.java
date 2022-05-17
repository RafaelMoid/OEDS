package br.com.eadfiocruzpe.Networking.SiopsWebScrapingService;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.GET;

import br.com.eadfiocruzpe.Models.DataResponses.BaseWebScrapingResponse;

interface SiopsWebScrapingServiceEndpoints {

    @GET("/consdetalhereenvio2.php")
    Call<BaseWebScrapingResponse> requestCityIndicatorsPublicHealth(
            @Query("Ano") String year,
            @Query("UF") String ufCode,
            @Query("Periodo") String periodYearCode,
            @Query("CodMunicipio") String cityCode,
            @Query("Ordenacao") String order,
            @Query("Autentica") String auth,
            @Query("CodHist") String codHist);

    @FormUrlEncoded
    @POST("/rel_LRF.php")
    Call<BaseWebScrapingResponse> requestGovInvestmentsDeclaredExpensesCityPH(
            @Field("cmbAno") String year,
            @Field("cmbUF") String ufCode,
            @Field("cmbPeriodo") String periodYearCode,
            @Field("cmbMunicipio") String cityCode,
            @Field("BtConsultar") String btConsultar);

}