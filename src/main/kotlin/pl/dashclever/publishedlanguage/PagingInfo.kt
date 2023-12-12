package pl.dashclever.publishedlanguage

data class PagingInfo<T>(
    val totalElements: Long,
    val pageNumber: Int,
    val totalPages: Int,
    val content: List<T>
)
