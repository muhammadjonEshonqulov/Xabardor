package uz.xabardor.rest.models

data class ExchangeRatesData(
    val id : Int?,
    val Code : String?,
    val Ccy : String?,
    val CcyNm_RU : String?,
    val CcyNm_UZ : String?,
    val CcyNm_UZC : String?,
    val CcyNm_EN : String?,
    val Nominal : String?,
    val Rate : String?,
    val Diff : String?,
    val Date : String?,
)
